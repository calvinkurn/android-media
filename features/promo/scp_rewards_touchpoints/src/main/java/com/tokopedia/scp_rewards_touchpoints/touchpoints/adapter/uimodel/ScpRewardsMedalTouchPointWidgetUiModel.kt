package com.tokopedia.scp_rewards_touchpoints.touchpoints.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.scp_rewards_touchpoints.touchpoints.adapter.typefactory.ScpRewardsMedalTouchPointWidgetTypeFactory

data class ScpRewardsMedalTouchPointWidgetUiModel(
    val title:String = String.EMPTY,
    val subtitle:String = String.EMPTY,
    val iconImage: String = String.EMPTY,
    val sunburstImage: String = String.EMPTY,
    val backgroundIconImage: String = String.EMPTY,
    val backgroundWidgetImage: String = String.EMPTY,
    val ctaAppLink: String = String.EMPTY,
    val ctaIsShown: Boolean = false
): Visitable<ScpRewardsMedalTouchPointWidgetTypeFactory> {
    override fun type(typeFactory: ScpRewardsMedalTouchPointWidgetTypeFactory): Int = typeFactory.type(this)
}
