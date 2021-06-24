package com.tokopedia.travel.passenger.presentation.adapter

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.tokopedia.travel.passenger.R
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel

/**
 * @author by jessica on 2019-08-29
 */

class TravelContactArrayAdapter(@get:JvmName("getContext_") val context: Context,
                                private val textViewResourceId: Int,
                                private var items: MutableList<TravelContactListModel.Contact>,
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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(textViewResourceId, null)
        } else {
            view = convertView
        }
        val contact = getItem(position)
        if (contact != null) {
            val label = view?.findViewById<TextView>(R.id.label)
            if (label != null) {
                label.text = getSpandableBoldText(contact.fullName, listener.getFilterText())
            }
        }
        return view
    }

    override fun getItem(position: Int): TravelContactListModel.Contact? {
        return try {
            suggestions[position]
        } catch (t: Throwable) {
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
            return (resultValue as TravelContactListModel.Contact).fullName
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return if (constraint != null) {
                suggestions.clear()
                suggestions.addAll(itemsAll.filter { it.fullName.toLowerCase().startsWith(constraint.toString().toLowerCase()) })

                val filterResults = FilterResults()
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                filterResults
            } else FilterResults()
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            if (results.count > 0) {
                clear()
                val filteredList = results.values as ArrayList<TravelContactListModel.Contact>
                for (contact in filteredList) add(contact)
                notifyDataSetChanged()
            }
        }
    }

    private fun getSpandableBoldText(strToPut: String, stringToHighlighted: String): CharSequence {

        val spannableStringBuilder = SpannableStringBuilder(strToPut)
        if (stringToHighlighted.length <= strToPut.length) {
            spannableStringBuilder.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    0, stringToHighlighted.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spannableStringBuilder
    }

    interface ContactArrayListener {
        fun getFilterText(): String
    }
}