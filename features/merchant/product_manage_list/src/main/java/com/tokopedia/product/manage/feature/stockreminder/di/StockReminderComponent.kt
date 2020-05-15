package com.tokopedia.product.manage.feature.stockreminder.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.product.manage.common.di.ProductManageComponent
import com.tokopedia.product.manage.feature.stockreminder.view.activity.StockReminderActivity
import com.tokopedia.product.manage.feature.stockreminder.view.fragment.StockReminderFragment
import dagger.Component

@StockReminderScope
@Component(modules = [StockReminderModule::class, ViewModelModule::class], dependencies = [ProductManageComponent::class])
interface StockReminderComponent {
    fun inject(stockReminderFragment: StockReminderFragment)
}