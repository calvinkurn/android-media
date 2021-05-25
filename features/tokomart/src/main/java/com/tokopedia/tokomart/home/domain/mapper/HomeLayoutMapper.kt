package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryGridUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSectionUiModel

object HomeLayoutMapper {

    fun mapToHomeUiModel(response: List<HomeLayoutResponse>): List<Visitable<*>> {
        // Temp Dummy Data
        return response.run {
            listOf(
                HomeSectionUiModel("2", "Another section"),
                HomeCategoryGridUiModel("3", "Home", emptyList())
            )
        }
    }
}
