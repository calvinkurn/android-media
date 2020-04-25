package com.tokopedia.vouchercreation.create.view.uimodel.vouchertips

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.create.view.typefactory.VoucherTipsItemTypeFactory

class DottedVoucherTipsItemUiModel(
        @StringRes val descRes: Int
) : Visitable<VoucherTipsItemTypeFactory> {

    override fun type(typeFactory: VoucherTipsItemTypeFactory): Int =
            typeFactory.type(this)
}