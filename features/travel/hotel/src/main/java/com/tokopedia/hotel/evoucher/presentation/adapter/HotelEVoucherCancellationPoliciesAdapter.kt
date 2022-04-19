package com.tokopedia.hotel.evoucher.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.orderdetail.data.model.HotelTransportDetail
import kotlinx.android.synthetic.main.item_hotel_e_voucher_cancellation_policies.view.*

/**
 * @author by furqan on 17/05/19
 */
class HotelEVoucherCancellationPoliciesAdapter(private var cancellationPolicies: List<HotelTransportDetail.Cancellation.CancellationPolicy>) : RecyclerView.Adapter<HotelEVoucherCancellationPoliciesAdapter.HotelEVoucherCancellationPoliciesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelEVoucherCancellationPoliciesViewHolder =
            HotelEVoucherCancellationPoliciesViewHolder(LayoutInflater.from(parent.context).inflate(
                    HotelEVoucherCancellationPoliciesViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = cancellationPolicies.size

    override fun onBindViewHolder(holder: HotelEVoucherCancellationPoliciesViewHolder, position: Int) {
        holder.bind(cancellationPolicies[position])
    }

    class HotelEVoucherCancellationPoliciesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(element: HotelTransportDetail.Cancellation.CancellationPolicy) {
            with(itemView) {
                itv_cancellation_policy.setTitleAndDescription(element.longTitle, element.longDesc)
                itv_cancellation_policy.truncateDescription = false
                itv_cancellation_policy.buildView()
            }
        }

        companion object {
            val LAYOUT = R.layout.item_hotel_e_voucher_cancellation_policies
        }

    }
}
