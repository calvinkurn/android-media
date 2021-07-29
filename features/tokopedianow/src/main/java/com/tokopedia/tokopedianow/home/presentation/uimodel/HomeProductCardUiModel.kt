package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.home.constant.HomeLayoutType
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeProductCardAdapter.*

data class HomeProductCardUiModel (
    val shopId: String,
    val productId: String,
    val quantity: Int,
    var product: ProductCardModel = ProductCardModel(),
    @HomeLayoutType val type: String
): Visitable<HomeProductCardTypeFactory> {

    override fun type(typeFactory: HomeProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}