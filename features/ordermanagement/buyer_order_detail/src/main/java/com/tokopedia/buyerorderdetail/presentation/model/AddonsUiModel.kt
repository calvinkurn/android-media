package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager

data class AddonsUiModel(
    val addonsLogoUrl: String,
    val addonsTitle: String,
    val price: Double,
    val priceText: String,
    val addonsId: String,
    val addOnsName: String,
    val addOnsThumbnailUrl: String,
    val quantity: Int,
    val totalPrice: String,
    val totalPriceText: String,
    val toPerson: String,
    val fromPerson: String
): BaseVisitableUiModel {
    override fun shouldShow(context: Context?): Boolean {
        return true
    }

    override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
        return null
    }

    override fun type(typeFactory: BuyerOrderDetailTypeFactory): Int {
        return typeFactory.type(this)
    }

}