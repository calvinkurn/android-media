package com.tokopedia.common.travel.widget.autocompletetextview

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.tokopedia.common.travel.data.entity.TravelContactListModel

/**
 * @author by jessica on 2019-08-29
 */

class TravelContactArrayAdapter(@get:JvmName("getContext_") val context: Context, val textViewResourceId: Int,
                                var items: MutableList<TravelContactListModel.Contact>,
                                var listener: ContactArrayListener)
    : ArrayAdapter<TravelContactListModel.Contact>(context, textViewResourceId, items) {

    var itemsAll: MutableList<TravelContactListModel.Contact> = arrayListOf()
    var suggestions: MutableList<TravelContactListModel.Contact> = arrayListOf()

    init {
        itemsAll = items
    }

    fun updateItem(list: MutableList<TravelContactListModel.Contact>) {
        this.items = list
        this.itemsAll = list
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(textViewResourceId, null)
        }
        val contact = getItem(position)
        if (contact != null) {
            val label = view?.findViewById<TextView>(com.tokopedia.common.travel.R.id.label)
            if (label != null) {
                label.text = getSpandableBoldText(contact.fullName, listener.getFilterText())
            }
        }
        return view
    }

    override fun getItem(position: Int): TravelContactListModel.Contact? {
        return suggestions.get(position)
    }

    override fun getCount(): Int {
        return suggestions.size
    }

    override fun getFilter(): Filter {
        return contactFilter
    }

    val contactFilter = object: Filter() {

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as TravelContactListModel.Contact).fullName
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            if (constraint != null) {
                suggestions.clear()
                suggestions.addAll(itemsAll.filter { it.fullName.toLowerCase().startsWith(constraint.toString().toLowerCase()) })

                val filterResults = FilterResults()
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                return filterResults
            } else return FilterResults()
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results != null && results.count > 0) {
                clear()
                val filteredList = results?.values as ArrayList<TravelContactListModel.Contact>
                for (contact in filteredList) add(contact)
                notifyDataSetChanged()
            }
        }
    }

    private fun getSpandableBoldText(strToPut: String, stringToHighlighted: String): CharSequence {

        var indexStartHighlighted = -1
        var indexEndHighlighted = -1
        if (TextUtils.isEmpty(stringToHighlighted) || TextUtils.isEmpty(strToPut)) {
            return strToPut
        }

        val strToPutLowerCase = strToPut.toLowerCase()
        val strToBoldLowerCase = stringToHighlighted.toLowerCase()
        val spannableStringBuilder = SpannableStringBuilder(strToPut)
        indexStartHighlighted = strToPutLowerCase.indexOf(strToBoldLowerCase)
        if (indexStartHighlighted != -1) {
            indexEndHighlighted = indexStartHighlighted + stringToHighlighted.length

            if (indexEndHighlighted > strToPut.length) {
                indexEndHighlighted = strToPut.length - 1
            }
        }

        if (indexStartHighlighted == -1) {
            spannableStringBuilder.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    0, strToPut.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return spannableStringBuilder
        } else {
            if (indexStartHighlighted > 0) spannableStringBuilder.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    0, indexStartHighlighted, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            if (indexEndHighlighted < strToPut.length) spannableStringBuilder.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    indexEndHighlighted, strToPut.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return spannableStringBuilder
        }
    }

    interface ContactArrayListener {
        fun getFilterText(): String
    }
}