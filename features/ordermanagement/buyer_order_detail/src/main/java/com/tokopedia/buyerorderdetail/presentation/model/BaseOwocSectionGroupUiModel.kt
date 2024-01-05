package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocSectionGroupTypeFactoryImpl

interface BaseOwocSectionGroupUiModel: Visitable<OwocSectionGroupTypeFactoryImpl> {
    fun shouldShow(context: Context?): Boolean
}
