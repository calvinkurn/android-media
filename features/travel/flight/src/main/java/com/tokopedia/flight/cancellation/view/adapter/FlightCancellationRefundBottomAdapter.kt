package com.tokopedia.flight.cancellation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.detail.view.model.SimpleModel

/**
 * @author by furqan on 02/09/2019
 *
 * type 1 = key normal, value bold black
 * type 2 = key bold black, value bold red
 */


class FlightCancellationRefundBottomAdapter(val layout: Int = TYPE_NORMAL) : RecyclerView.Adapter<FlightCancellationRefundBottomAdapter.CancellationRefundBottomViewHolder>() {

    var items: MutableList<SimpleModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancellationRefundBottomViewHolder {
        val view = if (layout == TYPE_RED)
            LayoutInflater.from(parent.context).inflate(com.tokopedia.flight.R.layout.item_flight_cancellation_detail_bottom_red, parent, false)
        else
            LayoutInflater.from(parent.context).inflate(com.tokopedia.flight.R.layout.item_flight_cancellation_detail_bottom, parent, false)

        return CancellationRefundBottomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CancellationRefundBottomViewHolder, position: Int) {
        holder.bindData(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addData(newData: List<SimpleModel>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    class CancellationRefundBottomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleInfo = itemView.findViewById(com.tokopedia.flight.R.id.key) as TextView
        private val descInfo = itemView.findViewById(com.tokopedia.flight.R.id.value) as TextView

        fun bindData(info: SimpleModel) {
            titleInfo.text = info.label.trim()
            descInfo.text = info.description.trim()
        }
    }

    companion object {
        const val TYPE_NORMAL = 1
        const val TYPE_RED = 2
    }
}