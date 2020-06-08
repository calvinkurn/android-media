package com.tokopedia.product.manage.feature.quickedit.variant.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.viewmodel.QuickEditVariantViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@QuickEditVariantScope
abstract class QuickEditVariantViewModelModule {

    @QuickEditVariantScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(QuickEditVariantViewModel::class)
    internal abstract fun productManageFilterViewModel(viewModel: QuickEditVariantViewModel): ViewModel
}