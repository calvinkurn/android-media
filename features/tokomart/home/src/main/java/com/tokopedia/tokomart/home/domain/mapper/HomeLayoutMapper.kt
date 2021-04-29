package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSectionUiModel

object HomeLayoutMapper {

    fun mapToSectionUiModel(response: List<HomeLayoutResponse>): List<HomeSectionUiModel> {
        return response.map { HomeSectionUiModel(it.id, it.title) }
    }
}
