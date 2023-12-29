package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPenaltyInfoNotificationBinding
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemPenaltyInfoNotificationListener
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyInfoNotificationUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPenaltyInfoNotificationViewHolder(
    view: View,
    private val listener: ItemPenaltyInfoNotificationListener?
): AbstractViewHolder<ItemPenaltyInfoNotificationUiModel>(view) {

    private val binding: ItemPenaltyInfoNotificationBinding? by viewBinding()

    override fun bind(element: ItemPenaltyInfoNotificationUiModel) {
        binding?.tvPenaltyInfoNotification?.text = getString(
            R.string.title_penalty_info_notification,
            element.notificationCount.toString()
        )
        binding?.notifPenaltyInfoNotification?.showWithCondition(element.shouldShowDot)
        binding?.cardPenaltyInfoNotification?.setOnClickListener {
            listener?.onNotYetPenaltyCardClicked(element.latestOngoingId)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_penalty_info_notification
    }

}
