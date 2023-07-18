package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.ItemScpRewardsMedalTouchPointBinding
import com.tokopedia.buyerorderdetail.presentation.model.ScpRewardsMedalTouchPointUiModel
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.model.ScpRewardsMedalTouchPointModel
import com.tokopedia.utils.view.binding.viewBinding

class ScpRewardsMedalTouchPointViewHolder(
    itemView: View?
): AbstractViewHolder<ScpRewardsMedalTouchPointUiModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.item_scp_rewards_medal_touch_point
    }

    private val binding: ItemScpRewardsMedalTouchPointBinding? by viewBinding()

    override fun bind(element: ScpRewardsMedalTouchPointUiModel?) {
        binding?.scpRewardsMedalTouchPointWidget?.setData(model = ScpRewardsMedalTouchPointModel(
            title = "mana dia",
            description = "dimana kah itu",
            iconImage = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/23/generalicon.png",
            sunburstImage = "https://images.tokopedia.net/img/HThbdi/scp/2023/05/23/sunburst.png",
            backgroundWidgetImage = "https://images.tokopedia.net/img/HThbdi/scp/2023/07/05/medali-pattern-widget.png",
            backgroundIconImage = "https://images.tokopedia.net/img/HThbdi/scp/2023/07/13/icon_medali_background_3x.png"
        ))
    }
}
