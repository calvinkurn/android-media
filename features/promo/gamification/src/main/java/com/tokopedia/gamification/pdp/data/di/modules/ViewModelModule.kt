package com.tokopedia.gamification.pdp.data.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.gamification.pdp.data.di.scopes.GamificationPdpScope
import com.tokopedia.gamification.pdp.presentation.viewmodels.PdpDialogViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @GamificationPdpScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @GamificationPdpScope
    @Binds
    @IntoMap
    @ViewModelKey(PdpDialogViewModel::class)
    internal abstract fun PdpDialogVM(viewModel: PdpDialogViewModel): ViewModel

}