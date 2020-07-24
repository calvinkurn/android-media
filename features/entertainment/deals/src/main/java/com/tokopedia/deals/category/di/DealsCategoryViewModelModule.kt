package com.tokopedia.deals.category.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.deals.category.ui.viewmodel.DealCategoryViewModel
import com.tokopedia.deals.common.ui.viewmodel.DealsBaseViewModel
import com.tokopedia.deals.common.ui.viewmodel.DealsBrandCategoryActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DealsCategoryViewModelModule{

    @DealsCategoryScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DealsBaseViewModel::class)
    internal abstract fun dealsBaseViewModel(viewModel: DealsBaseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DealsBrandCategoryActivityViewModel::class)
    internal abstract fun dealsBrandCategoryViewModel(activityViewModelBrand: DealsBrandCategoryActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DealCategoryViewModel::class)
    abstract fun dealsCategoryViewModel(viewModel: DealCategoryViewModel):ViewModel
}