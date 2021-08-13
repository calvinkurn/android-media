package com.tokopedia.vouchercreation.create.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.create.view.typefactory.CreateVoucherTypeFactory

class NextButtonUiModel(val onNext: () -> Unit?, var isEnabled: Boolean) : Visitable<CreateVoucherTypeFactory> {

    override fun type(typeFactory: CreateVoucherTypeFactory): Int =
            typeFactory.type(this)

}