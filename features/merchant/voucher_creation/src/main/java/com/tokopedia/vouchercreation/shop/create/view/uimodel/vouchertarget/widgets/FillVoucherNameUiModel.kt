package com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.widgets

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.shop.create.view.typefactory.vouchertarget.VoucherTargetTypeFactory

class FillVoucherNameUiModel : Visitable<VoucherTargetTypeFactory> {

    override fun type(typeFactory: VoucherTargetTypeFactory): Int =
            typeFactory.type(this)
}