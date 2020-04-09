package com.tokopedia.flight.filter.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import kotlinx.android.synthetic.main.item_flight_search_sort.view.*

/**
 * @author by jessica on 2020-02-18
 */

class FlightSortAdapter(private val sortOptions: List<Pair<Int, String>>) : RecyclerView.Adapter<FlightSortAdapter.ViewHolder>() {

    var selectedId: Int? = null

    var onClickItemListener: OnClickItemListener? = null

    init { setHasStableIds(true) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(ViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = sortOptions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sortOptions[position], onClickItemListener, selectedId)
    }

    override fun getItemId(position: Int): Long {
        return sortOptions[position].first.toLong()
    }

    fun onClickItem(id: Int) {
        selectedId = id
        notifyDataSetChanged()
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Pair<Int, String>, listener: OnClickItemListener?, selectedId: Int?) {
            with(itemView) {
                flightSortRadioButton.isChecked = item.first == selectedId
                if (tvFlightSortDescription.text.isEmpty()) {
                    tvFlightSortDescription.text = item.second
                    setOnClickListener { listener?.onClickItemListener(item.first) }
                    flightSortRadioButton.setOnClickListener { listener?.onClickItemListener(item.first) }
                }
            }
        }

        companion object {
            val LAYOUT = R.layout.item_flight_search_sort
        }
    }

    interface OnClickItemListener {
        fun onClickItemListener(selectedId: Int)
    }
}