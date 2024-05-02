package com.tokopedia.navigation.domain.validator

import com.tokopedia.navigation.presentation.model.BottomNavHomeType
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import javax.inject.Inject

class BottomNavigationValidator @Inject constructor() {

    fun validate(models: List<BottomNavBarUiModel>) {
        models.requireDistinctTypes()
        models.requireHomeType()
        models.requireMandatoryImageAssets()
    }

    private fun List<BottomNavBarUiModel>.requireDistinctTypes() {
        val nonDistinctType = groupingBy { it.uniqueId }.eachCount().filter { it.value > 1 }
        require(nonDistinctType.isEmpty()) { "Bottom nav bar contains duplication" }
    }

    private fun List<BottomNavBarUiModel>.requireHomeType() {
        require(any { it.type == BottomNavHomeType }) { "Bottom nav bar require \"home\" type" }
    }

    private fun List<BottomNavBarUiModel>.requireMandatoryImageAssets() {
        forEach {
            val assets = it.assets
            requireNotNull(assets["selected_icon_light_mode"])
            requireNotNull(assets["unselected_icon_light_mode"])
            requireNotNull(assets["selected_icon_dark_mode"])
            requireNotNull(assets["unselected_icon_dark_mode"])
        }
    }
}
