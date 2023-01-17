package com.tokopedia.search.di.module

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.utils.SmallGridSpanCount
import dagger.Module
import dagger.Provides

@Module
class StaggeredGridLayoutManagerModule {

    @SearchScope
    @Provides
    fun provideStaggeredGridLayoutManager(
        smallGridSpanCount: SmallGridSpanCount
    ): StaggeredGridLayoutManager {
        return StaggeredGridLayoutManager(
            smallGridSpanCount(),
            StaggeredGridLayoutManager.VERTICAL
        ).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        }
    }
}