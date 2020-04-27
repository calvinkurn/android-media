package com.tokopedia.vouchercreation.create.view.uimodel.vouchertips

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.create.view.typefactory.VoucherTipsItemTypeFactory

data class TipsItemUiModel(
        var isOpen: Boolean = false,
        @StringRes val titleRes: Int,
        val tipsItemList: ArrayList<Visitable<VoucherTipsItemTypeFactory>>
)