package com.tokopedia.hotel.cancellation.presentation.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.data.HotelCancellationModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.widget_hotel_cancellation_policy_item.view.*

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
        fun bind(item: HotelCancellationModel.CancelPolicy.Policy, isLastItem: Boolean, isOnlyOneItem: Boolean) {
            with(itemView) {
                hotel_cancellation_policy_item_title.text = item.title
                hotel_cancellation_policy_item_desc.text = item.desc

                if (item.styling.equals(ACTIVE_STATE, true)){
                    hotel_cancellation_policy_item_icon_deactivate.hide()
                } else if (item.styling.equals(GREYOUT_STATE, true)) {
                    hotel_cancellation_policy_item_icon_deactivate.show()
                    hotel_cancellation_policy_item_title.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
                    hotel_cancellation_policy_item_desc.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
                }

                if (isOnlyOneItem || isLastItem) hotel_cancellation_refund_connecting_line.hide()
            }
        }
    }

    companion object {
        const val ACTIVE_STATE = "active"
        const val GREYOUT_STATE = "greyout"
    }
}