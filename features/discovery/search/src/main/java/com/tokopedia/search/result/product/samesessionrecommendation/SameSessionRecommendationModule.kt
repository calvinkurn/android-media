package com.tokopedia.search.result.product.samesessionrecommendation

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.iris.Iris
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
object SameSessionRecommendationModule {
    @JvmStatic
    @Provides
    @SearchScope
    fun provideSameSessionRecommendationPreference(
        @ApplicationContext context: Context
    ): SameSessionRecommendationPreference {
        return SameSessionRecommendationPreferenceImpl(context)
    }

    @JvmStatic
    @Provides
    @SearchScope
    fun provideSameSessionRecommendationListener(
        sameSessionRecommendationPresenterDelegate: SameSessionRecommendationPresenterDelegate,
        iris: Iris,
    ): SameSessionRecommendationListener {
        return SameSessionRecommendationListenerDelegate(
            sameSessionRecommendationPresenterDelegate,
            iris,
        )
    }
}
