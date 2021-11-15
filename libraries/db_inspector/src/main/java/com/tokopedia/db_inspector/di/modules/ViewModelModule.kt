package com.tokopedia.db_inspector.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.db_inspector.presentation.databases.DatabaseViewModel
import com.tokopedia.db_inspector.presentation.schema.SchemaViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DatabaseViewModel::class)
    internal abstract fun bindsDatabaseListViewModel(viewModel: DatabaseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SchemaViewModel::class)
    internal abstract fun bindsSchemaViewModel(viewModel: SchemaViewModel): ViewModel

}