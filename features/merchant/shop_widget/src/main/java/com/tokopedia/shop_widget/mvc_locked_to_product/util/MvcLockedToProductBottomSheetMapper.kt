package com.tokopedia.shop_widget.mvc_locked_to_product.util

import com.tokopedia.shop_widget.mvc_locked_to_product.domain.model.MvcLockedToProductSortListResponse
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductSortUiModel

object MvcLockedToProductBottomSheetMapper {

    fun mapToSortListUiModel(
        response: MvcLockedToProductSortListResponse,
        selectedSortData: MvcLockedToProductSortUiModel
    ): List<MvcLockedToProductSortUiModel> {
        return response.filterSortProduct.data.sort.map {
            val isSelected = it.value == selectedSortData.value
            MvcLockedToProductSortUiModel(
                it.name,
                it.value,
                isSelected
            )
        }
    }
}
