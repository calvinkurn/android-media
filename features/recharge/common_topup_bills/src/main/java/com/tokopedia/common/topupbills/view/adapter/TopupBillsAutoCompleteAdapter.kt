package com.tokopedia.common.topupbills.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoComplete
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactModel
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteEmptyModel
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteHeaderModel
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import java.lang.IllegalArgumentException

class TopupBillsAutoCompleteAdapter(
    @get:JvmName("getContext_") val context: Context,
    private val textViewResourceId: Int,
    private var favorites: MutableList<TopupBillsAutoComplete>,
    private var contacts: MutableList<TopupBillsAutoComplete>,
    private var emptyStateUnitTxt: String,
    var listener: ContactArrayListener
) : ArrayAdapter<TopupBillsAutoComplete>(context, textViewResourceId, favorites) {

    var allFavorites: MutableList<TopupBillsAutoComplete> = arrayListOf()
    var allContacts: MutableList<TopupBillsAutoComplete> = arrayListOf()
    var suggestions: MutableList<TopupBillsAutoComplete> = arrayListOf()

    init {
        allFavorites = favorites
        allContacts = contacts
    }

    fun updateItems(
        favoriteNumbers: MutableList<TopupBillsAutoComplete>,
        contactNumbers: MutableList<TopupBillsAutoComplete>
    ) {
        this.favorites = favoriteNumbers
        this.allFavorites = favoriteNumbers
        this.contacts = contactNumbers
        this.allContacts = contactNumbers

        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (getItemViewType(position)) {
            TopupBillsAutoCompleteView.EMPTY_STATE.type -> {
                getAutoCompleteEmptyView(view, layoutInflater)
            }
            TopupBillsAutoCompleteView.HEADER.type -> {
                getAutoCompleteHeaderView(view, layoutInflater, position)
            }
            TopupBillsAutoCompleteView.CONTACT.type -> {
                getAutoCompleteNumberView(view, layoutInflater, position)
            }
            else -> throw IllegalArgumentException("Illegal Autocomplete ViewType")
        }
    }

    private fun getAutoCompleteEmptyView(view: View?, inflater: LayoutInflater): View {
        val tempView = inflater.inflate(R.layout.item_topup_bills_autocomplete_empty, null)

        val tvDesc: TextView = tempView.findViewById(R.id.common_topup_bills_autocomplete_empty_desc)
        tvDesc.text = context.getString(R.string.common_topup_autocomplete_empty_desc, emptyStateUnitTxt)

        return tempView
    }

    private fun getAutoCompleteHeaderView(view: View?, inflater: LayoutInflater, pos: Int): View {
        var tempView = view
        var holder: AutoCompleteHeaderViewHolder

        if (tempView == null) {
            tempView = inflater.inflate(R.layout.item_topup_bills_autocomplete_header, null)
            holder = AutoCompleteHeaderViewHolder(
                tempView.findViewById(R.id.common_topup_bills_autocomplete_header)
            )
            tempView.tag = holder
        } else {
            holder = tempView.tag as AutoCompleteHeaderViewHolder
        }

        holder.run {
            val header = getItem(pos) as TopupBillsAutoCompleteHeaderModel
            if (header.text.isNotEmpty()) {
                setTitle(header.text)
            }
        }
        return tempView!!
    }

    private fun getAutoCompleteNumberView(view: View?, inflater: LayoutInflater, pos: Int): View {
        var tempView = view
        var holder: AutoCompleteItemViewHolder

        if (tempView == null) {
            tempView = inflater.inflate(R.layout.item_topup_bills_autocomplete_number, null)
            holder = AutoCompleteItemViewHolder(
                tempView.findViewById(R.id.common_topup_bills_autocomplete_contact_container),
                tempView.findViewById(R.id.common_topup_bills_autocomplete_name),
                tempView.findViewById(R.id.common_topup_bills_autocomplete_number)
            )
            tempView.tag = holder
        } else {
            holder = tempView.tag as AutoCompleteItemViewHolder
        }

        holder.run {
            val contact = getItem(pos) as TopupBillsAutoCompleteContactModel
            if (contact.name.isNotEmpty()) {
                setToTwoLineView(
                    getSpandableBoldText(contact.name, listener.getFilterText()),
                    getSpandableBoldText(contact.phoneNumber, listener.getFilterText())
                )
            } else {
                setToOneLineView(
                    getSpandableBoldText(contact.phoneNumber, listener.getFilterText())
                )
            }
            requestLayout()
        }

        return tempView!!
    }

    override fun getViewTypeCount(): Int {
        return NUM_OF_AUTOCOMPLETE_TYPE
    }

    override fun getItemViewType(position: Int): Int {
        return suggestions[position].getViewType().type
    }

    override fun getItem(position: Int): TopupBillsAutoComplete? {
        return if (suggestions.isNotEmpty() && suggestions.size > position) {
            suggestions[position]
        } else {
            null
        }
    }

    override fun getCount(): Int {
        return suggestions.size
    }

    override fun isEnabled(position: Int): Boolean {
        return getItemViewType(position) == TopupBillsAutoCompleteView.CONTACT.type
    }

    override fun getFilter(): Filter {
        return contactFilter
    }

    val contactFilter = object : Filter() {

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return when (resultValue) {
                is TopupBillsAutoCompleteContactModel -> {
                    resultValue.phoneNumber
                }
                else -> ""
            }
        }

        @SuppressLint("PII Data Exposure")
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return if (constraint != null) {
                val filteredFavorites: MutableList<TopupBillsAutoComplete> = mutableListOf()
                val filteredContacts: MutableList<TopupBillsAutoComplete> = mutableListOf()
                val filteredSuggestions: MutableList<TopupBillsAutoComplete> = mutableListOf()

                // Favorite Section
                filteredFavorites.addAll(
                    allFavorites
                        .filterIsInstance<TopupBillsAutoCompleteContactModel>()
                        .filter {
                            it.phoneNumber.startsWith(constraint.toString()) ||
                                it.name.contains(constraint.toString(), ignoreCase = true)
                        }
                )
                if (filteredFavorites.isNotEmpty()) {
                    filteredFavorites.add(
                        0,
                        TopupBillsAutoCompleteHeaderModel(
                            context.getString(R.string.common_topup_autocomplete_header_favorite)
                        )
                    )
                    filteredSuggestions.addAll(filteredFavorites)
                }

                // Contact Section
                filteredContacts.addAll(
                    allContacts
                        .filterIsInstance<TopupBillsAutoCompleteContactModel>()
                        .filter {
                            it.phoneNumber.startsWith(constraint.toString()) ||
                                it.name.contains(constraint.toString(), ignoreCase = true)
                        }
                )

                if (filteredContacts.isNotEmpty()) {
                    filteredContacts.add(
                        0,
                        TopupBillsAutoCompleteHeaderModel(
                            context.getString(R.string.common_topup_autocomplete_header_contact)
                        )
                    )
                    filteredSuggestions.addAll(filteredContacts)
                }

                // Condition Check
                if (!constraint.matches(REGEX_IS_NUMERIC.toRegex()) &&
                    filteredSuggestions.isEmpty()
                ) {
                    filteredSuggestions.add(TopupBillsAutoCompleteEmptyModel())
                }

                FilterResults().apply {
                    values = filteredSuggestions
                    count = filteredSuggestions.size
                }
            } else {
                FilterResults().apply {
                    values = listOf<String>()
                    count = 0
                }
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            suggestions.clear()
            (results.values as? MutableList<TopupBillsAutoComplete>)?.let {
                if (it.size > 0) {
                    suggestions.addAll(it)
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }

    private fun getSpandableBoldText(strToPut: String, stringToHighlighted: String): CharSequence {
        val start = strToPut.indexOf(stringToHighlighted, ignoreCase = true)
        val end = start + stringToHighlighted.length

        val spannableStringBuilder = SpannableStringBuilder(strToPut)
        if (start >= 0 && end <= strToPut.length) {
            if (stringToHighlighted.length <= strToPut.length) {
                spannableStringBuilder
                    .setSpan(
                        android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                        start,
                        end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
            }
        }
        return spannableStringBuilder
    }

    inner class AutoCompleteItemViewHolder(
        private var container: ConstraintLayout,
        private var firstLine: TextView,
        private var secondLine: TextView
    ) {
        fun setToOneLineView(txt1: CharSequence) {
            firstLine.text = txt1
            secondLine.hide()
            firstLine.gravity = Gravity.CENTER_VERTICAL
        }

        fun setToTwoLineView(txt1: CharSequence, txt2: CharSequence) {
            firstLine.text = txt1
            secondLine.text = txt2
            secondLine.show()
            firstLine.gravity = Gravity.BOTTOM
        }

        fun requestLayout() {
            container.requestLayout()
        }
    }

    inner class AutoCompleteHeaderViewHolder(
        private var title: TextView
    ) {
        fun setTitle(text: String) {
            title.text = text
        }
    }

    interface ContactArrayListener {
        fun getFilterText(): String
    }

    companion object {
        private const val REGEX_IS_NUMERIC = "^[0-9]*$"
        const val NUM_OF_AUTOCOMPLETE_TYPE = 3
    }
}
