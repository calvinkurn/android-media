package com.tokopedia.common.topupbills.view.adapter

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
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteView
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteEmptyDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteHeaderDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import java.lang.IllegalArgumentException

class TopupBillsAutoCompleteAdapter(
    @get:JvmName("getContext_") val context: Context,
    private val textViewResourceId: Int,
    private var items: MutableList<TopupBillsAutoComplete>,
    var listener: ContactArrayListener
): ArrayAdapter<TopupBillsAutoComplete>(context, textViewResourceId, items) {

    var itemsAll: MutableList<TopupBillsAutoComplete> = arrayListOf()
    var suggestions: MutableList<TopupBillsAutoComplete> = arrayListOf()

    init {
        itemsAll = items
    }

    fun updateItems(list: MutableList<TopupBillsAutoComplete>) {
        this.items = list
        this.itemsAll = list
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
                getAutoCompleteHeaderView(view, layoutInflater)
            }
            TopupBillsAutoCompleteView.CONTACT.type -> {
                getAutoCompleteNumberView(view, layoutInflater, position)
            }
            else -> throw IllegalArgumentException("Illegal Autocomplete ViewType")
        }
    }

    private fun getAutoCompleteEmptyView(view: View?, inflater: LayoutInflater): View {
        return inflater.inflate(R.layout.item_topup_bills_autocomplete_empty, null)
    }

    private fun getAutoCompleteHeaderView(view: View?, inflater: LayoutInflater): View {
        return inflater.inflate(R.layout.item_topup_bills_autocomplete_header, null)
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
            val contact = getItem(pos) as TopupBillsAutoCompleteContactDataView
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
        return if (position == 0) {
            if (suggestions[position] is TopupBillsAutoCompleteEmptyDataView) {
                TopupBillsAutoCompleteView.EMPTY_STATE.type
            } else {
                TopupBillsAutoCompleteView.HEADER.type
            }
        } else {
            TopupBillsAutoCompleteView.CONTACT.type
        }
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
                is TopupBillsAutoCompleteContactDataView -> {
                    resultValue.phoneNumber
                }
                else -> ""
            }
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return if (constraint != null) {
                val filteredSuggestion: MutableList<TopupBillsAutoComplete> = mutableListOf()
                filteredSuggestion.addAll(
                    itemsAll
                        .filterIsInstance<TopupBillsAutoCompleteContactDataView>()
                        .filter {
                            it.phoneNumber.startsWith(constraint.toString()) ||
                            it.name.contains(constraint.toString(), ignoreCase = true)
                        })
                if (filteredSuggestion.isNotEmpty()) {
                    filteredSuggestion.add(0, TopupBillsAutoCompleteHeaderDataView())
                } else {
                    if (!constraint.matches(REGEX_IS_NUMERIC.toRegex()) && itemsAll.isNotEmpty())
                        filteredSuggestion.add(TopupBillsAutoCompleteEmptyDataView())
                }

                FilterResults().apply {
                    values = filteredSuggestion
                    count = filteredSuggestion.size
                }
            } else FilterResults().apply {
                values = listOf<String>()
                count = 0
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
        if (start >= 0) {
            if (stringToHighlighted.length <= strToPut.length) {
                spannableStringBuilder
                    .setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    0, start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableStringBuilder
                    .setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                        end, strToPut.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
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

    interface ContactArrayListener {
        fun getFilterText(): String
    }

    companion object {
        private const val REGEX_IS_NUMERIC = "^[0-9]*$"
        const val NUM_OF_AUTOCOMPLETE_TYPE = 3
    }
}