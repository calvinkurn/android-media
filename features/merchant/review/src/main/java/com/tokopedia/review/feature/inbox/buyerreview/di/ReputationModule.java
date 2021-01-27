package com.tokopedia.review.feature.inbox.buyerreview.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTracking;
import com.tokopedia.review.feature.inbox.buyerreview.data.factory.ReputationFactory;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.DeleteReviewResponseMapper;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.InboxReputationDetailMapper;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.InboxReputationMapper;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.ReplyReviewMapper;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.ReportReviewMapper;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.SendSmileyReputationMapper;
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.ShopFavoritedMapper;
import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository;
import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepositoryImpl;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetCacheInboxReputationUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetInboxReputationUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.CheckShopFavoritedUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.DeleteReviewResponseUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.GetInboxReputationDetailUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.GetReviewUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.SendReplyReviewUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.SendSmileyReputationUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.report.ReportReviewUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.network.ReputationService;
import com.tokopedia.review.feature.inbox.buyerreview.network.tome.TomeService;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * @author by nisie on 8/11/17.
 */

@Module
public class ReputationModule {

    @ReputationScope
    @Provides
    GraphqlRepository provideGraphqlRepository() { return GraphqlInteractor.getInstance().getGraphqlRepository(); }

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
            DeleteReviewResponseMapper deleteReviewResponseMapper,
            ReplyReviewMapper replyReviewMapper,
            UserSessionInterface userSession) {
        return new ReputationFactory(tomeService, reputationService, inboxReputationMapper,
                inboxReputationDetailMapper, sendSmileyReputationMapper, reportReviewMapper,
                shopFavoritedMapper,
                persistentCacheManager,
                replyReviewMapper,
                deleteReviewResponseMapper,
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
    TomeService provideTomeService(@ApplicationContext Context context, NetworkRouter networkRouter, UserSession userSession) {
        return new TomeService(
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
