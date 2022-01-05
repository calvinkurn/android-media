package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSwitcherUiModel.Home15mSwitcher

object SwitcherMapper {

    fun create15mSwitcherUiModel(onClick: () -> Unit): HomeLayoutItemUiModel {
        val now15mSwitcher = Home15mSwitcher { onClick() }
        return HomeLayoutItemUiModel(now15mSwitcher, HomeLayoutItemState.LOADED)
    }
}