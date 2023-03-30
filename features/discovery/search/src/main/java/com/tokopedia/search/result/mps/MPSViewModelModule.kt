package com.tokopedia.search.result.mps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.domain.usecase.getdynamicfilter.GetDynamicFilterCoroutineUseCase
import com.tokopedia.search.result.domain.usecase.getdynamicfilter.GetDynamicFilterCoroutineUseCaseModule
import com.tokopedia.search.result.mps.domain.usecase.MPSUseCaseModule
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [
    MPSStateModule::class,
    MPSUseCaseModule::class,
    GetDynamicFilterCoroutineUseCaseModule::class,
])
class MPSViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(MPSViewModel::class)
    @SearchScope
    fun provideMPSViewModel(mpsViewModel: MPSViewModel): ViewModel = mpsViewModel
}
