package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

object HomeProgressBarUiModel: HomeLayoutUiModel(HomeStaticLayoutId.PROGRESS_BAR) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}