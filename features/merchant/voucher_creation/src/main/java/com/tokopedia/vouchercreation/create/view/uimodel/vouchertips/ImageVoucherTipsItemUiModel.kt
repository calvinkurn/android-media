package com.tokopedia.vouchercreation.create.view.uimodel.vouchertips

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.create.view.typefactory.VoucherTipsItemTypeFactory

class ImageVoucherTipsItemUiModel(
        @DrawableRes val iconRes: Int,
        @StringRes val titleRes: Int,
        @StringRes val descRes: Int
) : Visitable<VoucherTipsItemTypeFactory> {

    override fun type(typeFactory: VoucherTipsItemTypeFactory): Int =
        typeFactory.type(this)

}