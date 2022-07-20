package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant.EMPTY_STATE_PENALTY_URL
import com.tokopedia.shop.score.databinding.ItemEmptyStatePenaltyBinding
import com.tokopedia.utils.view.binding.viewBinding

class ItemPenaltyEmptyViewHolder(view: View) : AbstractViewHolder<EmptyModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_empty_state_penalty
    }

    private val binding: ItemEmptyStatePenaltyBinding? by viewBinding()

    override fun bind(element: EmptyModel?) {
        binding?.run {
            if (!DeviceScreenInfo.isTablet(root.context)) {
                emptyStatePenalty?.setImageUrl(EMPTY_STATE_PENALTY_URL)
            }
        }
    }
}