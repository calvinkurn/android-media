package com.tokopedia.bmsm_widget.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.bmsm_widget.di.module.BmsmWidgetModule
import com.tokopedia.bmsm_widget.di.module.BmsmWidgetViewModelModule
import com.tokopedia.bmsm_widget.di.scope.BmsmWidgetScope
import com.tokopedia.bmsm_widget.presentation.bottomsheet.GiftListBottomSheet
import dagger.Component

@BmsmWidgetScope
@Component(
    modules = [BmsmWidgetModule::class, BmsmWidgetViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface BmsmWidgetComponent {
    fun inject(activity: GiftListBottomSheet)
}
