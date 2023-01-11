package com.tokopedia.tokopedianow.seeallcategories.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.seeallcategories.di.scope.SeeAllCategoriesScope
import com.tokopedia.tokopedianow.seeallcategories.persentation.viewmodel.TokoNowSeeAllCategoriesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SeeAllCategoriesViewModelModule {

    @Binds
    @SeeAllCategoriesScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TokoNowSeeAllCategoriesViewModel::class)
    internal abstract fun categoryMenuViewModel(categoryMenuViewModel: TokoNowSeeAllCategoriesViewModel): ViewModel
}
