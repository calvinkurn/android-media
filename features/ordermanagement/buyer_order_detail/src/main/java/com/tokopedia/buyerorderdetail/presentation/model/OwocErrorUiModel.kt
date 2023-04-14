package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocProductListTypeFactoryImpl

class OwocErrorUiModel(val throwable: Throwable): BaseOwocVisitableUiModel {

    override fun type(typeFactory: OwocProductListTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    override fun shouldShow(context: Context?): Boolean {
        return true
    }
}
