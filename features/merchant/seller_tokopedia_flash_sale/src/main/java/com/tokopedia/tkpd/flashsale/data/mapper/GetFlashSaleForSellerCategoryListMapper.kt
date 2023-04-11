package com.tokopedia.tkpd.flashsale.data.mapper


import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleForSellerCategoryListResponse
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleCategory
import javax.inject.Inject

class GetFlashSaleForSellerCategoryListMapper @Inject constructor() {

    fun map(response: GetFlashSaleForSellerCategoryListResponse): List<FlashSaleCategory> {
        return response.getFlashSaleForSellerCategoryList.categoryList.map { category ->
            FlashSaleCategory(category.categoryId.toLongOrZero(), category.categoryName)
        }
    }

}

