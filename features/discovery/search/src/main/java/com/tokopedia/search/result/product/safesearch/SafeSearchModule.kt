package com.tokopedia.search.result.product.safesearch

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
object SafeSearchModule {
    @JvmStatic
    @Provides
    @SearchScope
    fun provideSafeSearchPreference(): SafeSearchPreference {
        return SafeSearchMemoryPreference
    }

    @JvmStatic
    @Provides
    @SearchScope
    fun provideMutableSafeSearchPreference(): MutableSafeSearchPreference {
        return SafeSearchMemoryPreference
    }

    @JvmStatic
    @Provides
    @SearchScope
    fun provideSafeSearchView(
        @SearchContext context: Context,
    ): SafeSearchView {
        return SafeSearchViewDelegate(
            context as? LifecycleOwner,
        )
    }

    @JvmStatic
    @Provides
    @SearchScope
    fun provideSafeSearchPresenter(
        safeSearchPreference: MutableSafeSearchPreference,
        safeSearchView: SafeSearchView,
    ): SafeSearchPresenter {
        return SafeSearchPresenterDelegate(
            safeSearchPreference,
            safeSearchView,
        )
    }
}
