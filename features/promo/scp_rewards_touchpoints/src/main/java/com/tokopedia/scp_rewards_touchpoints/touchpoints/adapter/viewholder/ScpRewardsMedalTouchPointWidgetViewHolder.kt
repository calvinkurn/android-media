package com.tokopedia.scp_rewards_touchpoints.touchpoints.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.scp_rewards_touchpoints.R
import com.tokopedia.scp_rewards_touchpoints.databinding.ItemScpRewardsMedalTouchPointBinding
import com.tokopedia.scp_rewards_touchpoints.touchpoints.adapter.uimodel.ScpRewardsMedalTouchPointWidgetUiModel
import com.tokopedia.scp_rewards_touchpoints.touchpoints.data.model.ScpRewardsMedalTouchPointModel
import com.tokopedia.utils.view.binding.viewBinding

class ScpRewardsMedalTouchPointWidgetViewHolder(
    itemView: View?,
    val listener: ScpRewardsMedalTouchPointWidgetListener? = null
) : AbstractViewHolder<ScpRewardsMedalTouchPointWidgetUiModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.item_scp_rewards_medal_touch_point
    }

    private val binding: ItemScpRewardsMedalTouchPointBinding? by viewBinding()

    override fun bind(element: ScpRewardsMedalTouchPointWidgetUiModel) {
        binding?.root?.apply {
            setMargin(
                left = element.marginLeft,
                top = element.marginTop,
                right = element.marginRight,
                bottom = element.marginBottom
            )
            setData(
                model = ScpRewardsMedalTouchPointModel(
                    title = element.title,
                    subtitle = element.subtitle,
                    iconImage = element.iconImage,
                    sunburstImage = element.sunburstImage,
                    backgroundWidgetImage = element.backgroundWidgetImage,
                    backgroundIconImage = element.backgroundIconImage,
                    chevronIsShown = element.ctaIsShown
                )
            )
            setOnClickListener {
                listener?.onClickWidgetListener(
                    appLink = element.ctaAppLink
                )
            }
        }
    }

    interface ScpRewardsMedalTouchPointWidgetListener {
        fun onClickWidgetListener(
            appLink: String
        )
    }
}
