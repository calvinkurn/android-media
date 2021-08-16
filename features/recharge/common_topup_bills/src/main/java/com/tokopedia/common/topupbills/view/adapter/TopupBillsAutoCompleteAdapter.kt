package com.tokopedia.common.topupbills.view.adapter

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoComplete
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteView
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteHeaderDataView
import com.tokopedia.kotlin.extensions.view.hide
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
//        val view: View
//        if (convertView == null) {
//            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            view = inflater.inflate(textViewResourceId, null)
//        } else {
//            view = convertView
//        }

        /* TODO: [Misael] check ini
        *   val layoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater*/

        val layoutInflater = LayoutInflater.from(parent.context)
        return when (getItemViewType(position)) {
            TopupBillsAutoCompleteView.EMPTY_STATE.type -> {
                layoutInflater.inflate(R.layout.item_topup_bills_autocomplete_empty, null)
            }
            TopupBillsAutoCompleteView.HEADER.type -> {
                layoutInflater.inflate(R.layout.item_topup_bills_autocomplete_header, null)
            }
            TopupBillsAutoCompleteView.CONTACT.type -> {
                val contact = getItem(position)
                val view = layoutInflater.inflate(R.layout.item_topup_bills_autocomplete_phonenumber, null)
                contact?.let {
                    val contact = it as TopupBillsAutoCompleteContactDataView
                    val tvName = view.findViewById<TextView>(R.id.common_topup_bills_autocomplete_name)
                    if (contact.name.isNotEmpty()) {
                        tvName.text = getSpandableBoldText(contact.name, listener.getFilterText())
                    } else {
                        tvName.hide()
                    }
                    view.findViewById<TextView>(R.id.common_topup_bills_autocomplete_number)
                        .text = getSpandableBoldText(contact.phoneNumber, listener.getFilterText())
                }
                view
            }
            else -> throw IllegalArgumentException("Illegal Autocomplete Viewtype")
        }
    }

    override fun getViewTypeCount(): Int {
        return 3
    }

    override fun getItemViewType(position: Int): Int {
        return if (suggestions.isEmpty()) {
            TopupBillsAutoCompleteView.EMPTY_STATE.type
        } else {
            if (position == 0) {
                TopupBillsAutoCompleteView.HEADER.type
            } else {
                TopupBillsAutoCompleteView.CONTACT.type
            }
        }
    }

    // TODO: [Misael] check ini
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

    override fun getFilter(): Filter {
        return contactFilter
    }

    val contactFilter = object : Filter() {

        override fun convertResultToString(resultValue: Any?): CharSequence {
            // TODO: [Misael] check kembalian header
            return when (resultValue) {
                is TopupBillsAutoCompleteContactDataView -> resultValue.phoneNumber
                else -> ""
            }
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return if (constraint != null) {
                suggestions.clear()
                suggestions.addAll(
                    itemsAll
                        .filterIsInstance<TopupBillsAutoCompleteContactDataView>()
                        .filter {
                            it.phoneNumber.startsWith(constraint.toString()) ||
                            it.name.contains(constraint.toString(), ignoreCase = true)
                        })
                if (suggestions.isNotEmpty()) {
                    suggestions.add(0, TopupBillsAutoCompleteHeaderDataView())
                } else {
                    suggestions.add(TopupBillsAutoCompleteEmptyDataView())
                }

                FilterResults().apply {
                    values = suggestions
                    count = suggestions.size
                }
            } else FilterResults().apply {
                values = listOf<String>()
                count = 0
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            if (results.count > 0) {
                clear()
                val filteredList = results.values as ArrayList<TopupBillsAutoComplete>
                for (contact in filteredList) add(contact)
                notifyDataSetChanged()
            }
        }
    }

    private fun getSpandableBoldText(strToPut: String, stringToHighlighted: String): CharSequence {
        val start = strToPut.indexOf(stringToHighlighted)
        val end = start + stringToHighlighted.length

        val spannableStringBuilder = SpannableStringBuilder(strToPut)
        if (start >= 0) {
            if (stringToHighlighted.length <= strToPut.length) {
                spannableStringBuilder.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        return spannableStringBuilder
    }

    interface ContactArrayListener {
        fun getFilterText(): String
    }

    companion object {
        private const val REGEX_IS_ALPHANUMERIC_AND_SPACE = "^[a-zA-Z0-9\\s]*$"
    }
}