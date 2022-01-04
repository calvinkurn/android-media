package com.tokopedia.tokopedianow.home.presentation.uimodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

sealed class HomeSwitcherUiModel(
    @StringRes val title: Int,
    @StringRes val subtitle: Int,
    @DrawableRes val icon: Int,
    open val onClick: () -> Unit
): HomeLayoutUiModel(HomeStaticLayoutId.SWITCH_SERVICE_WIDGET) {

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class Home15mSwitcher(override val onClick: () -> Unit): HomeSwitcherUiModel(
        R.string.tokopedianow_15m_switcher_title,
        R.string.tokopedianow_15m_switcher_subtitle,
        R.drawable.tokopedianow_ic_15m,
        { onClick }
    )

    data class Home2hSwitcher(override val onClick: () -> Unit): HomeSwitcherUiModel(
        R.string.tokopedianow_2h_switcher_title,
        R.string.tokopedianow_2h_switcher_subtitle,
        R.drawable.tokopedianow_ic_2h,
        onClick
    )
}