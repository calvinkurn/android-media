package com.tokopedia.dilayanitokopedia.home.domain.mapper

import com.tokopedia.dilayanitokopedia.home.domain.model.HomeLayoutResponse
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.AnchorTabUiModel
import timber.log.Timber

/**
 * Created by irpan on 07/11/22.
 */
object AnchorTabMapper {

    /**
     * Mapping layout list to list menu Anchor tab
     */
    fun MutableList<AnchorTabUiModel>.mapMenuList(homeLayoutResponse: List<HomeLayoutResponse>): List<AnchorTabUiModel> {
        val listMenu = mutableListOf<AnchorTabUiModel>()

        val homeLayoutDistinctByGroupId = homeLayoutResponse.map { it.groupId }.distinctBy { it }

        this.forEach { anchorTab ->
            if (homeLayoutDistinctByGroupId.contains(anchorTab.groupId)) {
                listMenu.add(anchorTab)
            }
        }

        return listMenu
    }
}
