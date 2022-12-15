package com.tokopedia.tkpd.flashsale.data.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleProductPerCriteriaResponse
import com.tokopedia.tkpd.flashsale.domain.entity.Category
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaSelection
import javax.inject.Inject

class GetFlashSaleProductPerCriteriaMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun map(response: GetFlashSaleProductPerCriteriaResponse) = response.getFlashSaleProductPerCriteria.productCriteria.map {
        CriteriaSelection(
            criteriaId = it.criteriaId,
            selectionCount = it.countSubmitted,
            categoryTitle = mapTitle(it.categoryList),
            categoryTitleComplete = mapTitleComplete(it.categoryList),
            selectionCountMax = it.maxSubmission,
            hasMoreData = hasMoreData(it.categoryList),
            categoryList = mapCategory(it.categoryList),
        )
    }

    private fun hasMoreData(
        categoryList: List<GetFlashSaleProductPerCriteriaResponse.CategoryList>
    ) = categoryList.size > 2

    private fun mapCategory(
        categoryList: List<GetFlashSaleProductPerCriteriaResponse.CategoryList>
    ): List<Category> {
        return categoryList.map {
            Category(
                categoryId = it.categoryId.toString(),
                categoryName = it.categoryName
            )
        }
    }

    private fun mapTitle(categoryList: List<GetFlashSaleProductPerCriteriaResponse.CategoryList>): String {
        return if (hasMoreData(categoryList)) {
            context.getString(
                R.string.choose_product_category_title_format,
                categoryList.first().categoryName,
                categoryList.size.dec()
            )
        } else {
            mapTitleComplete(categoryList)
        }
    }

    private fun mapTitleComplete(
        categoryList: List<GetFlashSaleProductPerCriteriaResponse.CategoryList>
    ) = categoryList.joinToString(", ") {
        it.categoryName
    }

}