package com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.widget

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeBudgetTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel

class PromotionTypeInputUiModel(val onShouldShowBanner : (BannerVoucherUiModel<PromotionTypeBudgetTypeFactory>) -> Unit = {}) : Visitable<PromotionTypeBudgetTypeFactory> {

    override fun type(typeFactory: PromotionTypeBudgetTypeFactory): Int =
            typeFactory.type(this)
}