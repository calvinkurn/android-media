package com.tokopedia.hotel.cancellation.presentation.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.data.HotelCancellationModel
import com.tokopedia.hotel.databinding.WidgetHotelCancellationPolicyItemBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by jessica on 04/05/20
 */

class HotelCancellationPolicyAdapter: RecyclerView.Adapter<HotelCancellationPolicyAdapter.ViewHolder>() {

    private var items: List<HotelCancellationModel.CancelPolicy.Policy> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.widget_hotel_cancellation_policy_item, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position == itemCount - 1, itemCount == 1)
    }

    fun updatePolicy(policies: List<HotelCancellationModel.CancelPolicy.Policy>) {
        items = policies
        notifyDataSetChanged()
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val binding = WidgetHotelCancellationPolicyItemBinding.bind(view)

        fun bind(item: HotelCancellationModel.CancelPolicy.Policy, isLastItem: Boolean, isOnlyOneItem: Boolean) {
            with(binding) {
                hotelCancellationPolicyItemTitle.text = item.title
                hotelCancellationPolicyItemDesc.text = item.desc

                if (item.styling.equals(ACTIVE_STATE, true)){
                    hotelCancellationPolicyItemIconDeactivate.hide()
                } else if (item.styling.equals(GREYOUT_STATE, true)) {
                    hotelCancellationPolicyItemIconDeactivate.show()
                    hotelCancellationPolicyItemTitle.setTextColor(ContextCompat.getColor(root.context, unifyprinciplesR.color.Unify_NN950_44))
                    hotelCancellationPolicyItemDesc.setTextColor(ContextCompat.getColor(root.context, unifyprinciplesR.color.Unify_NN950_44))
                }

                if (isOnlyOneItem || isLastItem) hotelCancellationRefundConnectingLine.hide()
            }
        }
    }

    companion object {
        const val ACTIVE_STATE = "active"
        const val GREYOUT_STATE = "greyout"
    }
}
