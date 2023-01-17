package com.tokopedia.search.di.module

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class RecycledViewPoolModule {

    @SearchScope
    @Provides
    fun provideRecycledViewPool(): RecyclerView.RecycledViewPool =
        RecyclerView.RecycledViewPool()

}