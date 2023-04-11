package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocTypeFactoryImpl

class OwocErrorUiModel(val throwable: Throwable): BaseOwocVisitableUiModel {

    override fun type(typeFactory: OwocTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    override fun shouldShow(context: Context?): Boolean {
        return true
    }
}
