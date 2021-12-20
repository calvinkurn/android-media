package com.tokopedia.home.beranda.domain.interactor.usecase

interface HomePlayUseCase {
    fun onBannerTotalViewUpdated()
    fun onTotalViewUpdate()
    fun onWidgetReminder()
    fun onShouldUpdatePlayWidgetToggleReminder()
}