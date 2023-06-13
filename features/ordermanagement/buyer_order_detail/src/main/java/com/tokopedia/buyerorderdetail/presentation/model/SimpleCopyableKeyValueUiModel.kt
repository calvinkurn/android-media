package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero

class SimpleCopyableKeyValueUiModel(
    override val copyableText: String,
    override val copyLabel: StringRes,
    override val copyMessage: StringRes,
    override val label: StringRes
) : BaseCopyableKeyValueUiModel() {
    override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }
}
