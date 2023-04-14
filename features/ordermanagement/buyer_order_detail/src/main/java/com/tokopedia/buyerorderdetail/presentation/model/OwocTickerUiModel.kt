package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocProductListTypeFactoryImpl

data class OwocTickerUiModel(
    val actionKey: String,
    val actionText: String,
    val actionUrl: String,
    val description: String,
    val type: String
) : BaseOwocVisitableUiModel {

    var marginBottom: Int? = null

    var marginTop: Int? = null

    override fun type(typeFactory: OwocProductListTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    override fun shouldShow(context: Context?): Boolean {
        return description.isNotBlank() || actionText.isNotBlank()
    }
}

