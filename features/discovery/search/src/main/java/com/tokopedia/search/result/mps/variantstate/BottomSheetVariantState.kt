package com.tokopedia.search.result.mps.variantstate

import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetDataView
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetProductDataView

data class BottomSheetVariantState(
    val isOpen: Boolean = false,
    val variantModel: BottomSheetVariantModel? = null,
) {
    fun openBottomSheetVariant(
        mpsShopWidget: MPSShopWidgetDataView,
        mpsShopWidgetProduct: MPSShopWidgetProductDataView
    ) = copy(
        isOpen = true,
        variantModel = BottomSheetVariantModel(
            productId= mpsShopWidgetProduct.parentId,
            shopId= mpsShopWidget.id,
            trackerCDListName= SearchTracking.getActionFieldString(
                false,
                0,
                mpsShopWidget.componentId,
            )
        )
    )

    fun closeBottomSheetVariant() = copy(isOpen = false)
}
