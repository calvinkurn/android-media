package com.tokopedia.tokopedianow.home.presentation.uimodel

import androidx.annotation.StringRes
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

sealed class HomeSwitcherUiModel(
    @StringRes val title: Int,
    @StringRes val subtitle: Int,
): HomeLayoutUiModel(HomeStaticLayoutId.SWITCH_SERVICE_WIDGET) {

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    class Home20mSwitcher: HomeSwitcherUiModel(
        R.string.tokopedianow_20m_switcher_title,
        R.string.tokopedianow_20m_switcher_subtitle
    ) {
        override fun equals(other: Any?): Boolean {
            return (other as? Home20mSwitcher)?.visitableId == visitableId
        }

        override fun hashCode(): Int {
            return visitableId.toIntOrZero()
        }
    }

    class Home2hSwitcher: HomeSwitcherUiModel(
        R.string.tokopedianow_2h_switcher_title,
        R.string.tokopedianow_2h_switcher_subtitle
    ) {
        override fun equals(other: Any?): Boolean {
            return (other as? Home2hSwitcher)?.visitableId == visitableId
        }

        override fun hashCode(): Int {
            return visitableId.toIntOrZero()
        }
    }
}