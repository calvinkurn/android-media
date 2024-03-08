package com.tokopedia.deals.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.deals.common.ui.viewmodel.DealsBaseViewModel
import com.tokopedia.deals.common.ui.viewmodel.DealsBrandCategoryActivityViewModel
import com.tokopedia.deals.ui.brand.DealsBrandViewModel
import com.tokopedia.deals.ui.brand_detail.DealsBrandDetailViewModel
import com.tokopedia.deals.ui.category.DealCategoryViewModel
import com.tokopedia.deals.ui.home.ui.viewmodel.DealsHomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by jessica on 15/06/20
 */

@Module
abstract class DealsViewModelModule {

    @ActivityScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DealsBaseViewModel::class)
    abstract fun dealsBaseViewModel(viewModel: DealsBaseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DealsBrandCategoryActivityViewModel::class)
    abstract fun dealsBrandCategoryViewModel(activityViewModelBrand: DealsBrandCategoryActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DealsHomeViewModel::class)
    abstract fun dealsHomeViewModel(viewModel: DealsHomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DealsBrandViewModel::class)
    abstract fun dealsBrandViewModel(viewModel: DealsBrandViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DealCategoryViewModel::class)
    abstract fun dealsCategoryVM(viewModel: DealCategoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DealsBrandDetailViewModel::class)
    abstract fun dealsBrandDetailViewModel(viewModel: DealsBrandDetailViewModel): ViewModel
}
