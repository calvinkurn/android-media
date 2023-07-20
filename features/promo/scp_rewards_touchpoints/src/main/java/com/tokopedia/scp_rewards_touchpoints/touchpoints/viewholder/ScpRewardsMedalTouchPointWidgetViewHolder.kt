package com.tokopedia.scp_rewards_touchpoints.touchpoints.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.scp_rewards_touchpoints.R
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.model.ScpRewardsMedalTouchPointModel
import com.tokopedia.scp_rewards_touchpoints.databinding.ItemScpRewardsMedalTouchPointBinding
import com.tokopedia.scp_rewards_touchpoints.touchpoints.uimodel.ScpRewardsMedalTouchPointWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ScpRewardsMedalTouchPointWidgetViewHolder(
    itemView: View?
): AbstractViewHolder<ScpRewardsMedalTouchPointWidgetUiModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.item_scp_rewards_medal_touch_point
    }

    private val binding: ItemScpRewardsMedalTouchPointBinding? by viewBinding()

    override fun bind(element: ScpRewardsMedalTouchPointWidgetUiModel?) {
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
