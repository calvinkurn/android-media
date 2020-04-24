package com.tokopedia.vouchercreation.create.view.uimodel.vouchertips

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.create.view.typefactory.VoucherTipsItemTypeFactory

class BasicVoucherTipsItemUiModel : Visitable<VoucherTipsItemTypeFactory> {

    override fun type(typeFactory: VoucherTipsItemTypeFactory): Int =
            typeFactory.type(this)
}