package com.tokopedia.topads.detail_sheet.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topads.detail_sheet.viewmodel.TopAdsSheetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Author errysuprayogi on 22,October,2019
 */
@Module
abstract class ViewModelModule {

    @TopAdsSheetScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @TopAdsSheetScope
    @Binds
    @IntoMap
    @ViewModelKey(TopAdsSheetViewModel::class)
    internal abstract fun provideTopAdsSheetViewModel(viewModel: TopAdsSheetViewModel): ViewModel

}