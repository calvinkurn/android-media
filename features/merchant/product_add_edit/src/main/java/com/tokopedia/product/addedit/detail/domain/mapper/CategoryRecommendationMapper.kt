package com.tokopedia.product.addedit.detail.domain.mapper

import com.tokopedia.product.addedit.detail.domain.model.GetCategoryRecommendationDataModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import javax.inject.Inject

class CategoryRecommendationMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(categoryRecommendationData: GetCategoryRecommendationDataModel?): List<ListItemUnify> =
            categoryRecommendationData?.categories?.map {
                ListItemUnify(
                        title = it.name.orEmpty(),
                        description = ""
                ).apply { setVariant(null, ListItemUnify.RADIO_BUTTON, null)
                    listActionText = listOf(it.id, it.confidenceScore, it.precision).joinToString()
                    isBold = false
                }
            }.orEmpty()
}
