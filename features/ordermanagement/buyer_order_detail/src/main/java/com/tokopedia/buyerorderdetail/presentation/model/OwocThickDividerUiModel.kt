package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocTypeFactoryImpl

class OwocThickDividerUiModel : BaseOwocVisitableUiModel {

    override fun type(typeFactory: OwocTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    override fun shouldShow(context: Context?): Boolean {
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ThickDividerUiModel) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
