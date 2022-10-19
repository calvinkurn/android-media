package com.tokopedia.search.result.product.safesearch

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
object SafeSearchModule {
    @Provides
    @SearchScope
    fun provideSafeSearchPreference(
        @SearchContext context: Context
    ): SafeSearchPreference {
        return SafeSearchSharedPreference(context)
    }

    @Provides
    @SearchScope
    fun provideSafeSearchPresenter(
        safeSearchPreference: SafeSearchPreference,
        @SearchContext context: Context,
    ) : SafeSearchPresenter {
        return SafeSearchPresenterDelegate(
            safeSearchPreference,
            context as? LifecycleOwner,
        )
    }
}
