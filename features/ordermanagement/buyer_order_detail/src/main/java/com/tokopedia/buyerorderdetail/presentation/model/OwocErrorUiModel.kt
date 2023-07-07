package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocSectionGroupTypeFactoryImpl

class OwocErrorUiModel(val throwable: Throwable): BaseOwocSectionGroupUiModel {

    override fun type(typeFactory: OwocSectionGroupTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    override fun shouldShow(context: Context?): Boolean {
        return true
    }
}
