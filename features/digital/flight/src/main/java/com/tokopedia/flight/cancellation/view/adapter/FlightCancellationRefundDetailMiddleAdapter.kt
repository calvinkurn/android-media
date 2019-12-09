package com.tokopedia.flight.cancellation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.bookingV2.presentation.viewmodel.SimpleViewModel
import com.tokopedia.flight.orderlist.data.cloud.entity.CancellationTitleContentEntity
import com.tokopedia.flight.orderlist.data.cloud.entity.KeyValueEntity

/**
 * @author by furqan on 02/09/2019
 */
class FlightCancellationRefundDetailMiddleAdapter(var items: MutableList<CancellationTitleContentEntity>) : RecyclerView.Adapter<FlightCancellationRefundDetailMiddleAdapter.CancellationRefundDetailMiddleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancellationRefundDetailMiddleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.tokopedia.flight.R.layout.item_flight_cancellation_refund_detail_middle, parent, false)

        return CancellationRefundDetailMiddleViewHolder(view)
    }

    override fun onBindViewHolder(holder: CancellationRefundDetailMiddleViewHolder, position: Int) {
        holder.bindData(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class CancellationRefundDetailMiddleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(com.tokopedia.design.R.id.tv_title)
        private val rvItems: RecyclerView = itemView.findViewById(com.tokopedia.flight.R.id.rv_item)

        fun bindData(info: CancellationTitleContentEntity) {
            title.text = info.title

            val refundMiddleAdapter = FlightCancellationRefundBottomAdapter(FlightCancellationRefundBottomAdapter.TYPE_NORMAL)
            refundMiddleAdapter.addData(generateSimpleViewModel(info.content))
            rvItems.layoutManager = LinearLayoutManager(itemView.context)
            rvItems.adapter = refundMiddleAdapter
        }

        private fun generateSimpleViewModel(items: List<KeyValueEntity>): List<SimpleViewModel> {
            val datas = arrayListOf<SimpleViewModel>()

            for (item in items) {
                datas.add(SimpleViewModel(item.key, item.value))
            }

            return datas
        }
    }
}