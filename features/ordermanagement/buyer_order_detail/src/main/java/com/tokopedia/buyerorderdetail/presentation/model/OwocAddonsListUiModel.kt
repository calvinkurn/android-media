package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocProductListTypeFactoryImpl

data class OwocAddonsListUiModel(
    val addonsLogoUrl: String,
    val addonsTitle: String,
    val addonsItemList: List<AddonsListUiModel.AddonItemUiModel>,
) : BaseOwocVisitableUiModel {

    override fun type(typeFactory: OwocProductListTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    override fun shouldShow(context: Context?): Boolean {
        return addonsLogoUrl.isNotBlank() && addonsTitle.isNotBlank()
    }
}
