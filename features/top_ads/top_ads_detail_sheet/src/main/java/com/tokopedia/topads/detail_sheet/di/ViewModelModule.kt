package com.tokopedia.topads.detail_sheet.di

import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topads.detail_sheet.viewmodel.TopAdsSheetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Author errysuprayogi on 22,October,2019
 */
@Module
@TopAdsSheetScope
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(TopAdsSheetViewModel::class)
    internal abstract fun provideTopAdsSheetViewModel(viewModel: TopAdsSheetViewModel)
}