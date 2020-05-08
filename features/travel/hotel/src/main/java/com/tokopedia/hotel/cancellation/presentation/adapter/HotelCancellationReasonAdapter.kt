package com.tokopedia.hotel.cancellation.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.data.HotelCancellationModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.layout_hotel_cancellation_reason_item.view.*

/**
 * @author by jessica on 05/05/20
 */

class HotelCancellationReasonAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var items: List<HotelCancellationModel.Reason> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_HEADER -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_hotel_cancellation_reason_title,parent,false))
            else -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_hotel_cancellation_reason_item, parent, false))
        }
    }

    override fun getItemCount(): Int = items.size + 1

    override fun getItemViewType(position: Int): Int {
        return if(position == 0) TYPE_HEADER else TYPE_LIST
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position > 0) {
            (holder as ViewHolder).bind(items[position - 1], position == items.size)
        }
    }

    fun updateItems(reasons: List<HotelCancellationModel.Reason>) {
        items = reasons
        notifyDataSetChanged()
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(reason: HotelCancellationModel.Reason, isLastItem: Boolean) {
            with(itemView) {
                hotel_cancellation_reason_hotel_description.text = reason.title
                if (reason.freeText) {
                    hotel_cancellation_reason_free_text_tf.show()
                }
                if (isLastItem) hotel_cancellation_seperator.hide()
            }
        }
    }

    class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view)

    companion object {
        const val TYPE_HEADER = 10
        const val TYPE_LIST = 11
    }
}