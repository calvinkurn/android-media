package com.tokopedia.vouchercreation.common.view.textfield.vouchertype

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.common.view.VoucherCommonTypeFactory
import com.tokopedia.vouchercreation.shop.create.view.enums.PromotionType

class VoucherTextFieldUiModel(
        val type: VoucherTextFieldType,
        @StringRes val labelRes: Int? = null,
        override val maxValue: Int = 0,
        override val minValue: Int = 0,
        @StringRes override val minAlertRes: Int,
        @StringRes override val maxAlertRes: Int,
        var currentValue: Int? = null,
        var currentErrorPair: Pair<Boolean, String>? = null,
        val promotionType: PromotionType,
        val onValueChanged: (Int?, PromotionType) -> Unit = { _, _ -> },
        val onSetErrorMessage: (Boolean, String?, PromotionType) -> Unit = { _,_,_ -> }) : Visitable<VoucherCommonTypeFactory>, VoucherTextField {

    override fun type(typeFactory: VoucherCommonTypeFactory): Int =
            typeFactory.type(this)

}