package com.tokopedia.vouchercreation.create.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.create.view.typefactory.VoucherTargetTypeFactory

class VoucherTargetUiModel(val onShouldShowBottomSheet: () -> Unit = {}) : Visitable<VoucherTargetTypeFactory> {

    override fun type(typeFactory: VoucherTargetTypeFactory): Int =
            typeFactory.type(this)

}