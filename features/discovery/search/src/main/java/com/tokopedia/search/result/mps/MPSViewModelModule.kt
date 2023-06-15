package com.tokopedia.search.result.mps

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.search.di.module.LocalCacheModule
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.domain.usecase.getdynamicfilter.GetDynamicFilterCoroutineUseCaseModule
import com.tokopedia.search.result.mps.domain.usecase.MPSUseCaseModule
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [
    MPSStateModule::class,
    MPSUseCaseModule::class,
    GetDynamicFilterCoroutineUseCaseModule::class,
    LocalCacheModule::class,
])
class MPSViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(MPSViewModel::class)
    @SearchScope
    fun provideMPSViewModel(mpsViewModel: MPSViewModel): ViewModel = mpsViewModel
}
