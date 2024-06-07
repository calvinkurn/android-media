package com.tokopedia.navigation.domain.adjustor

import com.tokopedia.navigation.domain.model.defaultBottomNavModel
import com.tokopedia.navigation.presentation.model.BottomNavHomeType
import com.tokopedia.navigation_common.ui.BottomNavBarAsset.Key
import com.tokopedia.navigation_common.ui.BottomNavBarAsset.Type
import com.tokopedia.navigation_common.ui.BottomNavBarAsset.Variant
import com.tokopedia.navigation_common.ui.BottomNavBarItemType
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import com.tokopedia.navigation_common.ui.DiscoId
import com.tokopedia.navigation_common.ui.plus
import javax.inject.Inject

class BottomNavigationAdjustor @Inject constructor() {

    fun adjust(models: List<BottomNavBarUiModel>): List<BottomNavBarUiModel> {
        return models
            .defaultDataIfEmpty()
            .homeToFirstIndex()
    }

    private fun List<BottomNavBarUiModel>.defaultDataIfEmpty(): List<BottomNavBarUiModel> {
        return ifEmpty { defaultBottomNavModel }
    }

    private fun List<BottomNavBarUiModel>.homeToFirstIndex(): List<BottomNavBarUiModel> {
        val firstModel = firstOrNull() ?: return this
        if (firstModel.type == BottomNavHomeType) return this
        val homeModel = firstOrNull { it.type == BottomNavHomeType } ?: return this
        return listOf(homeModel) + filterNot { it == homeModel }
    }
}
