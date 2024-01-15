package com.tokopedia.hotel.evoucher.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.ItemHotelEVoucherCancellationPoliciesBinding
import com.tokopedia.hotel.orderdetail.data.model.HotelTransportDetail

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

        private val binding = ItemHotelEVoucherCancellationPoliciesBinding.bind(view)

        fun bind(element: HotelTransportDetail.Cancellation.CancellationPolicy) {
            with(binding) {
                itvCancellationPolicy.setTitleAndDescription(element.longTitle, element.longDesc)
                itvCancellationPolicy.truncateDescription = false
                itvCancellationPolicy.buildView()
            }
        }

        companion object {
            val LAYOUT = R.layout.item_hotel_e_voucher_cancellation_policies
        }

    }
}
