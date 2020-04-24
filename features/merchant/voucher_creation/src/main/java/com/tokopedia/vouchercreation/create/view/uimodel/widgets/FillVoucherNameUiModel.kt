package com.tokopedia.vouchercreation.create.view.uimodel.widgets

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.create.view.typefactory.VoucherTargetTypeFactory

class FillVoucherNameUiModel : Visitable<VoucherTargetTypeFactory> {

    override fun type(typeFactory: VoucherTargetTypeFactory): Int =
            typeFactory.type(this)
}