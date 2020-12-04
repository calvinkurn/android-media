package com.tokopedia.sellerappwidget.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.sellerappwidget.di.module.AppWidgetModule
import com.tokopedia.sellerappwidget.view.service.GetChatService
import com.tokopedia.sellerappwidget.view.service.GetOrderService
import dagger.Component

/**
 * Created By @ilhamsuaib on 16/11/20
 */

@AppWidgetScope
@Component(
        modules = [AppWidgetModule::class],
        dependencies = [BaseAppComponent::class]
)
interface AppWidgetComponent {

    fun inject(gerOrderService: GetOrderService)

    fun inject(gerOrderService: GetChatService)
}