package com.tokopedia.flight.cancellationdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.databinding.ItemFlightCancellationRefundDetailMiddleBinding
import com.tokopedia.flight.detail.view.model.SimpleModel
import com.tokopedia.flight.orderdetail.data.OrderDetailCancellation

/**
 * @author by furqan on 02/09/2019
 */
class FlightCancellationRefundDetailMiddleAdapter(var items: List<OrderDetailCancellation.OrderDetailRefundTitleContent>)
    : RecyclerView.Adapter<FlightCancellationRefundDetailMiddleAdapter.CancellationRefundDetailMiddleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancellationRefundDetailMiddleViewHolder {
        val binding = ItemFlightCancellationRefundDetailMiddleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CancellationRefundDetailMiddleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CancellationRefundDetailMiddleViewHolder, position: Int) {
        holder.bindData(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class CancellationRefundDetailMiddleViewHolder(val binding: ItemFlightCancellationRefundDetailMiddleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(info: OrderDetailCancellation.OrderDetailRefundTitleContent) {
            with(binding){
                tvTitle.text = info.title

                val refundMiddleAdapter =
                    FlightCancellationRefundBottomAdapter(FlightCancellationRefundBottomAdapter.TYPE_NORMAL)
                refundMiddleAdapter.addData(generateSimpleViewModel(info.content))
                rvItem.layoutManager = LinearLayoutManager(itemView.context)
                rvItem.adapter = refundMiddleAdapter
            }
        }

        private fun generateSimpleViewModel(items: List<OrderDetailCancellation.OrderDetailRefundKeyValue>): List<SimpleModel> {
            val datas = arrayListOf<SimpleModel>()

            for (item in items) {
                datas.add(SimpleModel(item.key, item.value))
            }

            return datas
        }
    }
}