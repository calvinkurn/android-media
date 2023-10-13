package com.tokopedia.recharge_pdp_emoney.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.databinding.ItemEmoneyRecentTransactionBinding

/**
 * @author by jessica on 05/04/21
 */

class RecentTransactionViewHolder(
    itemView: View,
    private val listener: ActionListener
) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(item: TopupBillsRecommendation) {
        val bind = ItemEmoneyRecentTransactionBinding.bind(itemView)
        with(bind) {
            emoneyRecentNumberImage.loadImage(item.iconUrl)
            emoneyRecentNumberTitle.text = item.clientNumber
            emoneyRecentNumberDescription.text = item.title

            root.setOnClickListener {
                listener.onClickItem(item)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_emoney_recent_transaction
    }

    interface ActionListener {
        fun onClickItem(item: TopupBillsRecommendation)
    }
}
