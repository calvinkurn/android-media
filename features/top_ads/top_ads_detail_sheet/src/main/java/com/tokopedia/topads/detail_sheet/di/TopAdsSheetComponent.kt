package com.tokopedia.topads.detail_sheet.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topads.detail_sheet.TopAdsDetailSheet
import dagger.Component

/**
 * Author errysuprayogi on 22,October,2019
 */
@TopAdsSheetScope
@Component(modules = [TopAdsSheetModule::class, ViewModelModule::class], dependencies = [BaseAppComponent::class])
interface TopAdsSheetComponent {
    fun inject(topAdsDetailSheet: TopAdsDetailSheet)
}
