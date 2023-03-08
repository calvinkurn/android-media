package com.tokopedia.search.mps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.mps.MPSState
import com.tokopedia.search.result.mps.MPSViewModel
import com.tokopedia.search.result.mps.domain.usecase.MPSUseCaseModule
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

@Module
class FakeMPSViewModelModule(
    private val mpsState: MPSState
) {

    @SearchScope
    @Provides
    fun provideMPSState(): MPSState = mpsState

    @SearchScope
    @Provides
    fun providesViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory = viewModelFactory

    @Provides
    @IntoMap
    @ViewModelKey(MPSViewModel::class)
    @SearchScope
    fun providesMPSViewModel(): ViewModel {
        val mutableStateFlow = MutableStateFlow(mpsState)

        return mockk<MPSViewModel>(relaxed = true) {
            every { stateFlow } returns mutableStateFlow
        }
    }
}
