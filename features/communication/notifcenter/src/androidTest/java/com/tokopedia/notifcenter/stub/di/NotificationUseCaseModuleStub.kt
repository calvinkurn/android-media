package com.tokopedia.notifcenter.stub.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.notifcenter.stub.data.repository.FakeTopAdsRepository
import com.tokopedia.notifcenter.stub.data.repository.FakeTopAdsRestRepository
import com.tokopedia.recommendation_widget_common.di.RecommendationModule
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [RecommendationModule::class])
object NotificationUseCaseModuleStub {

    @Provides
    @ActivityScope
    fun provideTopAdsImageViewUseCase(
        userSession: UserSessionInterface,
        fakeRepo: FakeTopAdsRepository
    ): TopAdsImageViewUseCase {
        return TopAdsImageViewUseCase(userSession.userId, fakeRepo, "fakeIrisSession")
    }

    @Provides
    @ActivityScope
    fun provideFakeTopAdsRestRepository(): FakeTopAdsRestRepository {
        return FakeTopAdsRestRepository()
    }

    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository
}
