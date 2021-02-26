package com.tokopedia.tkpd.tkpdreputation.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers;
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.tkpd.tkpdreputation.analytic.ReputationTracking;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.DeleteReviewResponseMapper;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.GetLikeDislikeMapper;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.LikeDislikeMapper;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.DeleteReviewResponseUseCaseV2;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.LikeDislikeReviewUseCaseV2;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.factory.ReputationFactory;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepositoryV2;
import com.tokopedia.tkpd.tkpdreputation.network.ReputationServiceV2;
import com.tokopedia.tkpd.tkpdreputation.network.product.ReviewProductServiceV2;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * @author by nisie on 8/11/17.
 */

@Module(includes = {ReviewProductViewModelModule.class})
public class ReputationModule {

    @ReputationScope
    @Provides
    CoroutineDispatchers provideCoroutineDispatchers(){
        return CoroutineDispatchersProvider.INSTANCE;
    }

    @ReputationScope
    @Provides

    GraphqlRepository provideGraphqlRepository() {
        return GraphqlInteractor.getInstance().getGraphqlRepository();
    }

    @ReputationScope
    @Provides
    PersistentCacheManager providePersistentCacheManager(@ApplicationContext Context context) {
        return new PersistentCacheManager(context);
    }

    @ReputationScope
    @Provides
    ReputationRepositoryV2 provideReputationRepositoryV2(ReputationFactory reputationFactory) {
        return new ReputationRepositoryV2(reputationFactory);
    }

    @ReputationScope
    @Provides
    ReputationFactory provideReputationFactory(
            ReputationServiceV2 reputationService,
            ReviewProductServiceV2 reviewProductService,
            UserSessionInterface userSession) {
        return new ReputationFactory(reputationService, reviewProductService, userSession);
    }

    @ReputationScope
    @Provides
    NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        if (context instanceof NetworkRouter) {
            return (NetworkRouter) context;
        }
        throw new IllegalStateException("Application must implement NetworkRouter");
    }

    @ReputationScope
    @Provides
    ReputationServiceV2 provideReputationServiceV2(@ApplicationContext Context context, NetworkRouter networkRouter, UserSession userSession) {
        return new ReputationServiceV2(
                context,
                networkRouter,
                userSession
        );
    }

    @ReputationScope
    @Provides
    ReviewProductServiceV2 provideReviewProductServiceV2(@ApplicationContext Context context, NetworkRouter networkRouter, UserSession userSession) {
        return new ReviewProductServiceV2(
                context,
                networkRouter,
                userSession
        );
    }

    @ReputationScope
    @Provides
    LikeDislikeReviewUseCaseV2 provideLikeDislikeReviewUseCaseV2(ReputationRepositoryV2 reputationRepository) {
        return new LikeDislikeReviewUseCaseV2(reputationRepository);
    }

    @ReputationScope
    @Provides
    GetLikeDislikeMapper provideGetLikeDislikeMapper() {
        return new GetLikeDislikeMapper();
    }


    @ReputationScope
    @Provides
    LikeDislikeMapper provideLikeDislikeMapper() {
        return new LikeDislikeMapper();
    }

    @ReputationScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @ReputationScope
    @Provides
    DeleteReviewResponseUseCaseV2 provideDeleteReviewResponseUseCaseV2(ReputationRepositoryV2 reputationRepository) {
        return new DeleteReviewResponseUseCaseV2(reputationRepository);
    }

    @ReputationScope
    @Provides
    DeleteReviewResponseMapper provideDeleteReviewResponseMapper() {
        return new DeleteReviewResponseMapper();
    }

    @ReputationScope
    @Provides
    public UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @ReputationScope
    @Provides
    ReputationTracking reputationTracking() {
        return new ReputationTracking();
    }
}
