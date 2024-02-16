package com.tokopedia.search.result

import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.ACTIVE_TAB
import com.tokopedia.discovery.common.constants.SearchConstant.ActiveTab.MPS
import com.tokopedia.discovery.common.constants.SearchConstant.ActiveTab.SHOP
import com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition.TAB_FIRST_POSITION
import com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition.TAB_SECOND_POSITION
import com.tokopedia.home_component.usecase.thematic.ThematicModel
import com.tokopedia.search.utils.mvvm.SearchUiState as ISearchUiState

data class SearchState(
    val searchParameter: Map<String, String> = mapOf(),
    val activeTabPosition: Int = searchParameter.getDefaultActiveTab(),
    val isOpeningAutoComplete: Boolean = false,
    val thematicModel: ThematicModel? = null
) : ISearchUiState {

    fun openAutoComplete() = copy(isOpeningAutoComplete = true)

    fun openAutoCompleteHandled() = copy(isOpeningAutoComplete = false)

    fun activeTab(position: Int) = copy(activeTabPosition = position)

    fun updateThematicModel(thematicModel: ThematicModel) = copy(thematicModel = thematicModel)

    fun shouldShowThematic() = thematicModel?.isShown == true

    fun isThematicDarkMode() = thematicModel?.colorMode == ThematicModel.COLOR_DARK
    fun isThematicLightMode() = thematicModel?.colorMode == ThematicModel.COLOR_LIGHT
}

private fun Map<String, String>.getDefaultActiveTab() =
    when (this[ACTIVE_TAB]) {
        SHOP, MPS -> TAB_SECOND_POSITION
        else -> TAB_FIRST_POSITION
    }
