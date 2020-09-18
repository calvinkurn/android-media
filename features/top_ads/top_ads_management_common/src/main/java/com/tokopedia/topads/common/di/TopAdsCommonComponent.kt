package com.tokopedia.topads.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topads.common.di.module.TopAdsCommonModule
import com.tokopedia.topads.common.di.module.ViewModelModule
import com.tokopedia.topads.common.view.widget.AutoAdsWidgetCommon
import dagger.Component

/**
 * Created by Pika on 18/9/20.
 */
@TopAdsCommonScope
@Component(modules = [TopAdsCommonModule::class, ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface TopAdsCommonComponent {
    fun inject(autoAdsWidget: AutoAdsWidgetCommon)
}