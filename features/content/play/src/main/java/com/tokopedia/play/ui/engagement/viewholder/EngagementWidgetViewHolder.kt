package com.tokopedia.play.ui.engagement.viewholder

import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.ui.engagement.model.EngagementUiModel
import com.tokopedia.play_common.databinding.ViewGameInteractiveBinding

/**
 * @author by astidhiyaa on 24/08/22
 */
class EngagementWidgetViewHolder(
    private val binding: ViewGameInteractiveBinding,
    private val listener: Listener
) :
    BaseViewHolder(binding.root) {

    fun bind(item: EngagementUiModel) {
        when(item){
            is EngagementUiModel.Game -> setupGame(item)
            is EngagementUiModel.Promo -> setupPromo(item)
        }
    }

    private fun setupPromo(item: EngagementUiModel.Promo){

    }

    private fun setupGame(item: EngagementUiModel.Game) {

    }

    interface Listener {
    }
}

