package com.tokopedia.explore.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.affiliatecommon.data.network.TopAdsApi
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.explore.analytics.ContentExploreAnalytics
import com.tokopedia.explore.domain.interactor.ExploreDataUseCase
import com.tokopedia.explore.view.listener.ContentExploreContract
import com.tokopedia.explore.view.presenter.ContentExplorePresenter
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor.Companion.getInstance
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * @author by milhamj on 23/07/18.
 */

@Module
class ExploreModule {
    @ExploreScope
    @Provides
    fun provideContentExplorePresenter(getExploreDataUseCase: ExploreDataUseCase, trackAffiliateClickUseCase: TrackAffiliateClickUseCase): ContentExploreContract.Presenter {
        return ContentExplorePresenter(getExploreDataUseCase, trackAffiliateClickUseCase)
    }

    @ExploreScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @ExploreScope
    @Provides
    fun provideCoroutineGqlRepo(): GraphqlRepository {
        return getInstance().graphqlRepository
    }

    @ExploreScope
    @Provides
    fun provideTopAdsApi(retrofitBuilder: Retrofit.Builder): TopAdsApi {
        return retrofitBuilder.build().create(TopAdsApi::class.java)
    }

    @Provides
    fun providesFeedAnalyticTracker(): ContentExploreAnalytics {
        return ContentExploreAnalytics()
    }
}