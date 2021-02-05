package com.tokopedia.entertainment.pdp.di

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.entertainment.pdp.viewmodel.EventPDPViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Author firman on 06-04-20
 */

@Module
abstract class EventPDPViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(EventPDPViewModel::class)
    internal abstract fun providePDPViewModel(viewModel: EventPDPViewModel): ViewModel

}