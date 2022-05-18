package com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.shop.create.view.typefactory.vouchertarget.VoucherTipsItemTypeFactory

data class TipsItemUiModel(
        var isOpen: Boolean = false,
        @StringRes val titleRes: Int,
        val tipsItemList: ArrayList<Visitable<VoucherTipsItemTypeFactory>>
)