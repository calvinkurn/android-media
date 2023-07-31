package com.tokopedia.editor.di.module

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.editor.data.repository.NavigationToolRepository
import com.tokopedia.editor.data.repository.NavigationToolRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class EditorRepositoryModule {

    @Binds
    @ActivityScope
    internal abstract fun bindNavigationToolRepository(
        impl: NavigationToolRepositoryImpl
    ): NavigationToolRepository
}
