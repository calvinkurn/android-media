package com.tokopedia.hotel.orderdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.ItemOrderDetailCancellationPoliciesBinding
import com.tokopedia.hotel.orderdetail.data.model.HotelTransportDetail

/**
 * @author by jessica on 13/05/19
 */

class CancellationAdapter(val list: List<HotelTransportDetail.Cancellation.CancellationPolicy>):
        RecyclerView.Adapter<CancellationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_order_detail_cancellation_policies, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list.get(position))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val itemview: View): RecyclerView.ViewHolder(itemview) {

        private val binding = ItemOrderDetailCancellationPoliciesBinding.bind(itemview)

        fun bind(data: HotelTransportDetail.Cancellation.CancellationPolicy) {
            with(binding) {
                title.text = data.longTitle
                text.text = data.longDesc
            }
        }
    }
}

