package com.tokopedia.product.manage.feature.stockreminder.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.product.manage.feature.stockreminder.view.activity.StockReminderActivity
import com.tokopedia.product.manage.feature.stockreminder.view.fragment.StockReminderFragment
import dagger.Component

@StockReminderScope
@Component(modules = [StockReminderModule::class, ViewModelModule::class, CoroutineDispatcherModule::class], dependencies = [BaseAppComponent::class])
interface StockReminderComponent {
    fun inject(stockReminderActivity: StockReminderActivity)
    fun inject(stockReminderFragment: StockReminderFragment)
}