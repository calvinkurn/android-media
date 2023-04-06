package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocTypeFactoryImpl

class OwocShimmerUiModel: BaseOwocVisitableUiModel {
    override fun shouldShow(context: Context): Boolean {
        return true
    }

    override fun type(typeFactory: OwocTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
