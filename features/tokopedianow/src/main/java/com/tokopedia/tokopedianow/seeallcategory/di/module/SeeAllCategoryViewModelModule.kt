package com.tokopedia.tokopedianow.seeallcategory.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.seeallcategory.di.scope.SeeAllCategoryScope
import com.tokopedia.tokopedianow.seeallcategory.persentation.viewmodel.TokoNowSeeAllCategoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SeeAllCategoryViewModelModule {

    @Binds
    @SeeAllCategoryScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TokoNowSeeAllCategoryViewModel::class)
    internal abstract fun categoryMenuViewModel(categoryMenuViewModel: TokoNowSeeAllCategoryViewModel): ViewModel
}
