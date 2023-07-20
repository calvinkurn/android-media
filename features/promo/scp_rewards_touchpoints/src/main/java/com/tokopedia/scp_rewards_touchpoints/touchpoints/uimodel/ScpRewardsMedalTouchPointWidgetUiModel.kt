package com.tokopedia.scp_rewards_touchpoints.touchpoints.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.scp_rewards_touchpoints.touchpoints.typefactory.ScpRewardsMedalTouchPointWidgetTypeFactory

data class ScpRewardsMedalTouchPointWidgetUiModel(
    val title:String = String.EMPTY,
    val subtitle:String = String.EMPTY,
    val badgeImage: String = String.EMPTY,
    val sunflare: String = String.EMPTY,
    val ctaIsShown:Boolean = false
): Visitable<ScpRewardsMedalTouchPointWidgetTypeFactory> {
    override fun type(typeFactory: ScpRewardsMedalTouchPointWidgetTypeFactory): Int = typeFactory.type(this)
}
