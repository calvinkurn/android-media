package com.tokopedia.home.beranda.data.usecase

interface HomePlayUseCase {
    fun onBannerTotalViewUpdated()
    fun onTotalViewUpdate()
    fun onWidgetReminder()
    fun onShouldUpdatePlayWidgetToggleReminder()
}