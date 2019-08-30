package com.tokopedia.common.travel.widget.autocompletetextview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.tokopedia.common.travel.R
import com.tokopedia.common.travel.data.entity.TravelContactListModel

/**
 * @author by jessica on 2019-08-29
 */

class TravelContactArrayAdapter(@get:JvmName("getContext_") val context: Context, val textViewResourceId: Int, var items: List<TravelContactListModel.Contact>)
    : ArrayAdapter<TravelContactListModel.Contact>(context, textViewResourceId, items) {

    var itemsAll: MutableList<TravelContactListModel.Contact> = arrayListOf()
    var suggestions: MutableList<TravelContactListModel.Contact> = arrayListOf()

    init {
        itemsAll = items.toMutableList()
    }

    fun updateItem(list: List<TravelContactListModel.Contact>) {
        this.items = list
        this.itemsAll = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(textViewResourceId, null)
        }
        val contact = items.getOrNull(position)
        if (contact != null) {
            val label = view?.findViewById<TextView>(R.id.label)
            if (label != null) label.text = contact.fullName
        }
        return view
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
                for (contact in itemsAll) {
                    if (contact.fullName.toLowerCase().contains(constraint.toString())) suggestions.add(contact)
                }
                val filterResults = FilterResults()
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                return filterResults
            } else return FilterResults()
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            suggestions.clear()
            if (results != null && results.count > 0) {
                val filteredList = results?.values as ArrayList<TravelContactListModel.Contact>
                for (contact in filteredList) add(contact)
                notifyDataSetChanged()
            }
        }
    }
}