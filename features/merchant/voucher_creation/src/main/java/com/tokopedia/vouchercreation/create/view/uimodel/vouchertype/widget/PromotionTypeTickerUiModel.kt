package com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.widget

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeItemTypeFactory

class PromotionTypeTickerUiModel(@StringRes val descRes: Int,
                                 val onDismissTicker: () -> Unit = {}) : Visitable<PromotionTypeItemTypeFactory> {

    override fun type(typeFactory: PromotionTypeItemTypeFactory): Int =
            typeFactory.type(this)
}