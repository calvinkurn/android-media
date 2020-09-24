package com.tokopedia.review.feature.inbox.buyerreview.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTracking;
import com.tokopedia.review.feature.inbox.buyerreview.data.factory.ReputationFactory;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.InboxReputationDetailMapper;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.InboxReputationMapper;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.ReportReviewMapper;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.SendSmileyReputationMapper;
import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository;
import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepositoryImpl;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetCacheInboxReputationUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetInboxReputationUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.GetInboxReputationDetailUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.GetReviewUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.SendSmileyReputationUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.report.ReportReviewUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.network.ReputationService;
import com.tokopedia.review.feature.inbox.buyerreview.network.product.ReviewProductService;
import com.tokopedia.review.feature.inbox.buyerreview.network.shop.ReputationActService;
import com.tokopedia.review.feature.inbox.buyerreview.network.tome.TomeService;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.Dispatchers;

/**
 * @author by nisie on 8/11/17.
 */

@Module(includes = {ReputationRawModule.class})
public class ReputationModule {

    @ReputationScope
    @Provides
    GraphqlRepository provideGraphqlRepository() { return GraphqlInteractor.getInstance().getGraphqlRepository(); }

    @ReputationScope
    @Provides
    @Named("Main")
    CoroutineDispatcher provideMainDispatcher() {
        return Dispatchers.getMain();
    }


    @ReputationScope
    @Provides
    PersistentCacheManager providePersistentCacheManager(@ApplicationContext Context context) {
        return new PersistentCacheManager(context);
    }

    @ReputationScope
    @Provides
    GetFirstTimeInboxReputationUseCase
    provideGetFirstTimeInboxReputationUseCase(GetInboxReputationUseCase getInboxReputationUseCase,
                                              GetCacheInboxReputationUseCase
                                                      getCacheInboxReputationUseCase,
                                              ReputationRepository reputationRepository) {
        return new GetFirstTimeInboxReputationUseCase(
                getInboxReputationUseCase,
                getCacheInboxReputationUseCase,
                reputationRepository);
    }

    @ReputationScope
    @Provides
    GetInboxReputationUseCase
    provideGetInboxReputationUseCase(ReputationRepository reputationRepository) {
        return new GetInboxReputationUseCase(reputationRepository);
    }

    @ReputationScope
    @Provides
    ReputationRepository provideReputationRepository(ReputationFactory reputationFactory) {
        return new ReputationRepositoryImpl(reputationFactory);
    }

    @ReputationScope
    @Provides
    ReputationFactory provideReputationFactory(
            TomeService tomeService,
            ReputationService reputationService,
            InboxReputationMapper inboxReputationMapper,
            InboxReputationDetailMapper inboxReputationDetailMapper,
            SendSmileyReputationMapper sendSmileyReputationMapper,
            ShopFavoritedMapper shopFavoritedMapper,
            ReportReviewMapper reportReviewMapper,
            PersistentCacheManager persistentCacheManager,
            FaveShopActService faveShopActService,
            FaveShopMapper faveShopMapper,
            DeleteReviewResponseMapper deleteReviewResponseMapper,
            ReplyReviewMapper replyReviewMapper,
            GetLikeDislikeMapper getLikeDislikeMapper,
            LikeDislikeMapper likeDislikeMapper,
            ReviewProductService reviewProductService,
            UserSessionInterface userSession) {
        return new ReputationFactory(tomeService, reputationService, inboxReputationMapper,
                inboxReputationDetailMapper, sendSmileyReputationMapper, reportReviewMapper,
                shopFavoritedMapper,
                persistentCacheManager,
                faveShopActService,
                faveShopMapper,
                deleteReviewResponseMapper,
                replyReviewMapper,
                getLikeDislikeMapper,
                likeDislikeMapper,
                reviewProductService,
                userSession);
    }

    @ReputationScope
    @Provides
    InboxReputationMapper provideInboxReputationMapper() {
        return new InboxReputationMapper();
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
    ReviewProductService provideReviewProductService(@ApplicationContext Context context, NetworkRouter networkRouter, UserSession userSession) {
        return new ReviewProductService(
                context,
                networkRouter,
                userSession
        );
    }

    @ReputationScope
    @Provides
    ReputationActService provideReputationActService(@ApplicationContext Context context, NetworkRouter networkRouter, UserSession userSession) {
        return new ReputationActService(
                context,
                networkRouter,
                userSession
        );
    }

    @ReputationScope
    @Provides
    ShopService provideShopService(@ApplicationContext Context context, NetworkRouter networkRouter, UserSession userSession) {
        return new ShopService(
                context,
                networkRouter,
                userSession
        );
    }

    @ReputationScope
    @Provides
    ReviewActService provideReviewActService(@ApplicationContext Context context, NetworkRouter networkRouter, UserSession userSession) {
        return new ReviewActService(
                context,
                networkRouter,
                userSession
        );
    }

    @ReputationScope
    @Provides
    UploadImageService provideUploadImageService(@ApplicationContext Context context, NetworkRouter networkRouter, UserSession userSession) {
        return new UploadImageService(
                context,
                networkRouter,
                userSession
        );
    }

    @ReputationScope
    @Provides
    GenerateHostActService provideGenerateHostActService(@ApplicationContext Context context, NetworkRouter networkRouter, UserSession userSession) {
        return new GenerateHostActService(
                context,
                networkRouter,
                userSession
        );
    }

    @ReputationScope
    @Provides
    TomeService provideTomeService(@ApplicationContext Context context, NetworkRouter networkRouter, UserSession userSession) {
        return new TomeService(
                context,
                networkRouter,
                userSession
        );
    }

    @ReputationScope
    @Provides
    FaveShopActService provideFaveShopActService(@ApplicationContext Context context, NetworkRouter networkRouter, UserSession userSession) {
        return new FaveShopActService(
                context,
                networkRouter,
                userSession
        );
    }

    @ReputationScope
    @Provides
    InboxReputationDetailMapper provideInboxReputationDetailMapper() {
        return new InboxReputationDetailMapper();
    }

    @ReputationScope
    @Provides
    GetReviewUseCase
    provideGetReviewUseCase(ReputationRepository reputationRepository) {
        return new GetReviewUseCase(reputationRepository);
    }

    @ReputationScope
    @Provides
    GetInboxReputationDetailUseCase
    provideGetInboxReputationDetailUseCase(GetInboxReputationUseCase getInboxReputationUseCase,
                                           GetReviewUseCase getReviewUseCase,
                                           CheckShopFavoritedUseCase checkShopFavoritedUseCase) {
        return new GetInboxReputationDetailUseCase(
                getInboxReputationUseCase,
                getReviewUseCase,
                checkShopFavoritedUseCase);
    }

    @ReputationScope
    @Provides
    SendSmileyReputationMapper provideSendSmileyReputationMapper() {
        return new SendSmileyReputationMapper();
    }

    @ReputationScope
    @Provides
    SendSmileyReputationUseCase
    provideSendSmileyReputationUseCase(ReputationRepository reputationRepository) {
        return new SendSmileyReputationUseCase(reputationRepository);
    }

    @ReputationScope
    @Provides
    ReportReviewMapper provideReportReviewMapper() {
        return new ReportReviewMapper();
    }

    @ReputationScope
    @Provides
    ReportReviewUseCase provideReportReviewUseCase(ReputationRepository reputationRepository) {
        return new ReportReviewUseCase(reputationRepository);
    }

    @ReputationScope
    @Provides
    GetCacheInboxReputationUseCase provideGetCacheInboxReputationUseCase(ReputationRepository reputationRepository) {
        return new GetCacheInboxReputationUseCase(reputationRepository);
    }

    @ReputationScope
    @Provides
    ShopFavoritedMapper provideShopFavoritedMapper() {
        return new ShopFavoritedMapper();
    }

    @ReputationScope
    @Provides
    CheckShopFavoritedUseCase provideCheckShopFavoritedUseCase(
            ReputationRepository reputationRepository
    ) {
        return new CheckShopFavoritedUseCase(reputationRepository);
    }

    @ReputationScope
    @Provides
    FaveShopMapper provideFaveShopMapper() {
        return new FaveShopMapper();
    }

    @ReputationScope
    @Provides
    SendReplyReviewUseCase provideSendReplyReviewUseCase(ReputationRepository reputationRepository) {
        return new SendReplyReviewUseCase(reputationRepository);
    }

    @ReputationScope
    @Provides
    ReplyReviewMapper provideReplyReviewMapper() {
        return new ReplyReviewMapper();
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
                                                         UserSessionInterface userSession){
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
    public UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @ReputationScope
    @Provides
    ReputationTracking reputationTracking(){
        return new ReputationTracking();
    }
}
