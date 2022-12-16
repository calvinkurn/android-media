package com.tokopedia.dilayanitokopedia.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.dilayanitokopedia.home.domain.model.HomeLayoutResponse
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.AnchorTabUiModel

/**
 * Created by irpan on 07/11/22.
 */
object AnchorTabMapper {

    /**
     * Mapping layout list to list menu Anchor tab
     */
    fun MutableList<AnchorTabUiModel>.mapMenuList(
        homeLayoutResponse: List<HomeLayoutResponse>,
        homeLayoutList: List<Visitable<*>>
    ): List<AnchorTabUiModel> {
        var listMenu = mutableListOf<AnchorTabUiModel>()

        /**
         *
         * looping list layout
         * each layout will looping anchor tab hard code data
         * if group id from hard code data same with groupId with visitable then add to list
         * the item added to list and get modified the visitable since still null
         */
        homeLayoutList.forEachIndexed { index, visitable ->

            this.forEach anchorTabForEach@{ anchorTab ->

                if (anchorTab.groupId == homeLayoutResponse[index].groupId) {
                    anchorTab.apply {
                        this.visitable = homeLayoutList[index]
                        // remove later
                        this.title = homeLayoutResponse[index].header.name
                    }
                    listMenu.add(anchorTab)
                    listMenu = listMenu.distinctBy { it.groupId }.toMutableList()
                    return@anchorTabForEach
                }
            }
        }
        return listMenu.toList()
    }
}
