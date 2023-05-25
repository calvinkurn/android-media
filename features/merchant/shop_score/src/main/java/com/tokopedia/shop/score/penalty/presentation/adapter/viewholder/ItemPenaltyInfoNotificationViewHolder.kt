package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPenaltyInfoNotificationBinding
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyInfoNotificationUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPenaltyInfoNotificationViewHolder(view: View): AbstractViewHolder<ItemPenaltyInfoNotificationUiModel>(view) {

    private val binding: ItemPenaltyInfoNotificationBinding? by viewBinding()

    override fun bind(element: ItemPenaltyInfoNotificationUiModel) {
        binding?.tvPenaltyInfoNotification?.text = getString(
            R.string.title_penalty_info_notification,
            element.notificationCount.toString()
        )
        binding?.notifPenaltyInfoNotification?.showWithCondition(element.shouldShowDot)
    }

    companion object {
        val LAYOUT = R.layout.item_penalty_info_notification
    }

}
