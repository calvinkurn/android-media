package com.tokopedia.ordermanagement.snapshot.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.ordermanagement.snapshot.view.viewmodel.SnapshotViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by fwidjaja on 1/25/21.
 */

@Module
abstract class SnapshotViewModelModule {

    @SnapshotScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @SnapshotScope
    @Binds
    @IntoMap
    @ViewModelKey(SnapshotViewModel::class)
    internal abstract fun snapshotViewModel(viewModel: SnapshotViewModel): ViewModel
}