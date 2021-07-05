package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero

open class CopyableKeyValueUiModel(
        open val copyableText: String = "",
        open val copyableTextToShow: String = "",
        open val copyLabel: String = "",
        open val copyMessage: String = "",
        open val label: String = ""
): Visitable<BuyerOrderDetailTypeFactory> {
    override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    fun shouldShow(): Boolean {
        return copyableTextToShow.isNotBlank()
    }
}