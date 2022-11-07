package com.tokopedia.dilayanitokopedia.home.domain.mapper

import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.AnchorTabUiModel
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutItemUiModel

/**
 * Created by irpan on 07/11/22.
 */
object AnchorTabMapper {

    /**
     * Mapping layout list to list menu
     */
    fun MutableList<HomeLayoutItemUiModel>.mapMenuList(): List<AnchorTabUiModel> {
        val listMenu = this.mapIndexed { index, homeLayoutItemUiModel ->
            AnchorTabUiModel(index, "title ${homeLayoutItemUiModel.layout}", "")
        }


        return listMenu

    }
}
