package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocProductListTypeFactoryImpl

class OwocShimmerUiModel: BaseOwocVisitableUiModel {
    override fun shouldShow(context: Context): Boolean {
        return true
    }

    override fun type(typeFactory: OwocProductListTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
