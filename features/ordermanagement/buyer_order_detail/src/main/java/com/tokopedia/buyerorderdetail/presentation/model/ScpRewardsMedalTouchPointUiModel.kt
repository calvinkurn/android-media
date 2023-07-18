package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager
import com.tokopedia.kotlin.extensions.view.EMPTY

data class ScpRewardsMedalTouchPointUiModel(
    val title:String = String.EMPTY,
    val subtitle:String = String.EMPTY,
    val badgeImage: String = String.EMPTY,
    val sunflare: String = String.EMPTY,
    val ctaIsShown:Boolean = false
): BaseVisitableUiModel {
    override fun type(typeFactory: BuyerOrderDetailTypeFactory): Int = typeFactory.type(this)

    override fun shouldShow(context: Context?): Boolean = title.isNotBlank() && subtitle.isNotBlank()

    override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? = null
}
