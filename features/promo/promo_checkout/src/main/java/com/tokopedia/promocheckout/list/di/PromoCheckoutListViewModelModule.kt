package com.tokopedia.promocheckout.list.di

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.promocheckout.list.view.viewmodel.PromoCheckoutListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author: astidhiyaa on 30/07/21.
 */
@Module
abstract class PromoCheckoutListViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PromoCheckoutListViewModel::class)
    internal abstract fun providePromoCheckoutListViewModel(viewModel: PromoCheckoutListViewModel): ViewModel
}