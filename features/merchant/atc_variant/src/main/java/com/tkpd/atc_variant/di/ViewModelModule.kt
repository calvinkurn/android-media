package com.tkpd.atc_variant.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tkpd.atc_variant.views.AtcVariantViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Yehezkiel on 07/05/21
 */

@Module
abstract class ViewModelModule {

    @AtcVariantScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AtcVariantViewModel::class)
    internal abstract fun provideAtcVariantViewModel(viewModel: AtcVariantViewModel): ViewModel

}