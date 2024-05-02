package com.tokopedia.navigation.domain.validator

import com.tokopedia.navigation.presentation.model.BottomNavHomeType
import com.tokopedia.navigation_common.ui.BottomNavBarAsset.Key
import com.tokopedia.navigation_common.ui.BottomNavBarAsset.Variant
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import com.tokopedia.navigation_common.ui.plus
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
            requireNotNull(assets[Key.ImageActive + Variant.Light])
            requireNotNull(assets[Key.ImageInactive + Variant.Light])
            requireNotNull(assets[Key.ImageActive + Variant.Dark])
            requireNotNull(assets[Key.ImageInactive + Variant.Dark])
        }
    }
}
