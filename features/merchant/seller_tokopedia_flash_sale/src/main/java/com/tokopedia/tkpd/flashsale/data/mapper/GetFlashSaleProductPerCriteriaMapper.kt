package com.tokopedia.tkpd.flashsale.data.mapper

import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleProductPerCriteriaResponse
import com.tokopedia.tkpd.flashsale.domain.entity.Category
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaSelection
import javax.inject.Inject

class GetFlashSaleProductPerCriteriaMapper @Inject constructor() {

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
            categoryList.first().categoryName + " , +" + categoryList.size.dec() + " lainnya"
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