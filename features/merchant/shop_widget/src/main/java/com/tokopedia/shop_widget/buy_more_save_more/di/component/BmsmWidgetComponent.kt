package com.tokopedia.shop_widget.buy_more_save_more.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop_widget.buy_more_save_more.di.module.BmsmWidgetModule
import com.tokopedia.shop_widget.buy_more_save_more.di.module.BmsmWidgetViewModelModule
import com.tokopedia.shop_widget.buy_more_save_more.di.scope.BmsmWidgetScope
import com.tokopedia.shop_widget.buy_more_save_more.presentation.fragment.BmsmWidgetTabFragment
import dagger.Component

@BmsmWidgetScope
@Component(modules = [BmsmWidgetModule::class, BmsmWidgetViewModelModule::class], dependencies = [BaseAppComponent::class])
interface BmsmWidgetComponent {
    fun inject(bmsmWidgetTabFragment: BmsmWidgetTabFragment?)
}
