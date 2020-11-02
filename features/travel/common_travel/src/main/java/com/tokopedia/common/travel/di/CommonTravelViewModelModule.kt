package com.tokopedia.common.travel.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.common.travel.viewmodel.TravelVideoBannerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 02/11/2020
 */
@CommonTravelScope
@Module
abstract class CommonTravelViewModelModule {

    @CommonTravelScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TravelVideoBannerViewModel::class)
    abstract fun travelVideoBannerViewModel(travelVideoBannerViewModel: TravelVideoBannerViewModel): ViewModel

}