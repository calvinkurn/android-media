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
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.DeleteReviewResponseUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.GetLikeDislikeReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.LikeDislikeReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.factory.ReputationFactory;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepositoryImpl;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepositoryV2;
import com.tokopedia.tkpd.tkpdreputation.network.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.network.ReputationServiceV2;
import com.tokopedia.tkpd.tkpdreputation.network.product.ReviewProductService;
import com.tokopedia.tkpd.tkpdreputation.network.product.ReviewProductServiceV2;
import com.tokopedia.tkpd.tkpdreputation.review.product.domain.ReviewProductGetHelpfulUseCase;
import com.tokopedia.tkpd.tkpdreputation.review.product.domain.ReviewProductGetListUseCase;
import com.tokopedia.tkpd.tkpdreputation.review.product.domain.ReviewProductGetRatingUseCase;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.ReviewProductListMapper;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.presenter.ReviewProductPresenter;
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
    ReputationRepository provideReputationRepository(ReputationFactory reputationFactory) {
        return new ReputationRepositoryImpl(reputationFactory);
    }

    @ReputationScope
    @Provides
    ReputationRepositoryV2 provideReputationRepositoryV2(ReputationFactory reputationFactory) {
        return new ReputationRepositoryV2(reputationFactory);
    }

    @ReputationScope
    @Provides
    ReputationFactory provideReputationFactory(
            ReputationService reputationService,
            ReputationServiceV2 reputationServiceV2,
            DeleteReviewResponseMapper deleteReviewResponseMapper,
            GetLikeDislikeMapper getLikeDislikeMapper,
            LikeDislikeMapper likeDislikeMapper,
            ReviewProductService reputationReviewApi,
            ReviewProductServiceV2 reviewProductServiceV2,
            UserSessionInterface userSession) {
        return new ReputationFactory(reputationService, reputationServiceV2, deleteReviewResponseMapper,
                getLikeDislikeMapper, likeDislikeMapper, reputationReviewApi, reviewProductServiceV2, userSession);
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
    ReputationService provideReputationService(@ApplicationContext Context context, NetworkRouter networkRouter, UserSession userSession) {
        return new ReputationService(
                context,
                networkRouter,
                userSession
        );
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
    ReviewProductService provideReviewProductService(@ApplicationContext Context context, NetworkRouter networkRouter, UserSession userSession) {
        return new ReviewProductService(
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
    LikeDislikeReviewUseCase provideLikeDislikeReviewUseCase(ReputationRepository reputationRepository) {
        return new LikeDislikeReviewUseCase(reputationRepository);
    }

    @ReputationScope
    @Provides
    GetLikeDislikeMapper provideGetLikeDislikeMapper() {
        return new GetLikeDislikeMapper();
    }

    @ReputationScope
    @Provides
    GetLikeDislikeReviewUseCase provideGetLikeDislikeReviewUseCase(ReputationRepository reputationRepository) {
        return new GetLikeDislikeReviewUseCase(reputationRepository);
    }


    @ReputationScope
    @Provides
    LikeDislikeMapper provideLikeDislikeMapper() {
        return new LikeDislikeMapper();
    }

    @ReputationScope
    @Provides
    ReviewProductPresenter provideProductReviewPresenter(ReviewProductGetListUseCase productReviewGetListUseCase,
                                                         ReviewProductGetHelpfulUseCase productReviewGetHelpfulUseCase,
                                                         ReviewProductGetRatingUseCase productReviewGetRatingUseCase,
                                                         LikeDislikeReviewUseCase likeDislikeReviewUseCase,
                                                         DeleteReviewResponseUseCase deleteReviewResponseUseCase,
                                                         ReviewProductListMapper productReviewListMapper,
                                                         UserSessionInterface userSession) {
        return new ReviewProductPresenter(productReviewGetListUseCase, productReviewGetHelpfulUseCase, productReviewGetRatingUseCase,
                likeDislikeReviewUseCase, deleteReviewResponseUseCase, productReviewListMapper, userSession);
    }

    @ReputationScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @ReputationScope
    @Provides
    DeleteReviewResponseUseCase provideDeleteReviewResponseUseCase(ReputationRepository reputationRepository) {
        return new DeleteReviewResponseUseCase(reputationRepository);
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
