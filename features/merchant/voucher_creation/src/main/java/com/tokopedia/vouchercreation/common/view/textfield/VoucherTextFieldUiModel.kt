package com.tokopedia.vouchercreation.common.view.textfield

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.common.view.VoucherCommonTypeFactory
import com.tokopedia.vouchercreation.create.view.enums.PromotionTextField

class VoucherTextFieldUiModel(
        val type: VoucherTextFieldType,
        @StringRes val labelRes: Int? = null,
        override val maxValue: Int = 0,
        override val minValue: Int = 0,
        @StringRes override val minAlertRes: Int,
        @StringRes override val maxAlertRes: Int,
        val isLastTextField: Boolean = false,
        val promotionTextFieldType: PromotionTextField = PromotionTextField.FreeDelivery.MinimumPurchase,
        val onValueChanged: (Int?, PromotionTextField) -> Unit = { _,_ -> }) : Visitable<VoucherCommonTypeFactory>, VoucherTextField {

    override fun type(typeFactory: VoucherCommonTypeFactory): Int =
            typeFactory.type(this)

}