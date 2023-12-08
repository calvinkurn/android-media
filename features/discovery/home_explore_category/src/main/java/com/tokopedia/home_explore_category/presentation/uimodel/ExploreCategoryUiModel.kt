package com.tokopedia.home_explore_category.presentation.uimodel

import androidx.compose.runtime.Stable
import com.tokopedia.nest.principles.utils.ImpressionHolder

@Stable
data class ExploreCategoryUiModel(
    val id: String,
    val categoryTitle: String,
    val categoryImageUrl: String,
    val isSelected: Boolean = false,
    val subExploreCategoryList: List<SubExploreCategoryUiModel>
) {
    @Stable
    data class SubExploreCategoryUiModel(
        val id: String,
        val name: String,
        val imageUrl: String,
        val appLink: String,
        val categoryLabel: String,
        val buIdentifier: String
    ) {
        val impressHolderCompose: ImpressionHolder = ImpressionHolder()
    }
}
