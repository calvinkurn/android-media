package com.tokopedia.product.addedit.detail.domain.mapper

import com.tokopedia.product.addedit.detail.domain.model.GetCategoryRecommendationDataModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import javax.inject.Inject

class CategoryRecommendationMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(categoryRecommendationData: GetCategoryRecommendationDataModel?): List<ListItemUnify> =
            categoryRecommendationData?.categories?.map {

                var categoryName = it.name.orEmpty()
                if (categoryName.isNotBlank()) categoryName = categoryName.replace("/","-")

                ListItemUnify(
                        categoryName,
                        description = ""
                ).apply {
                    setVariant(null, ListItemUnify.RADIO_BUTTON, null)
                    listActionText = (it.id ?: 0L).toString()
                    isBold = false
                }
            }.orEmpty()
}