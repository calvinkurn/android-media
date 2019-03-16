package com.tokopedia.tkpd.tkpdreputation.di;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.accounts.UploadImageService;
import com.tokopedia.core.network.apiservices.tome.TomeService;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.apiservices.user.FaveShopActService;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.tkpd.tkpdreputation.analytic.ReputationTracking;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.DeleteReviewResponseMapper;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.GetLikeDislikeMapper;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.LikeDislikeMapper;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.ReportReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.DeleteReviewResponseUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.GetLikeDislikeReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.LikeDislikeReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.ReportReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.factory.ReputationFactory;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.FaveShopMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationDetailMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.ReplyReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendReviewSubmitMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendReviewValidateMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendSmileyReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.ShopFavoritedMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SkipReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepositoryImpl;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetCacheInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.CheckShopFavoritedUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.FavoriteShopUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.GetInboxReputationDetailUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.GetReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.SendReplyReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.SendSmileyReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.EditReviewSubmitUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.EditReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.EditReviewValidateUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.GetSendReviewFormUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewSubmitUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewValidateUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SetReviewFormCacheUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SkipReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.source.ReviewProductApi;
import com.tokopedia.tkpd.tkpdreputation.review.product.domain.ReviewProductGetHelpfulUseCase;
import com.tokopedia.tkpd.tkpdreputation.review.product.domain.ReviewProductGetListUseCase;
import com.tokopedia.tkpd.tkpdreputation.review.product.domain.ReviewProductGetRatingUseCase;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.ReviewProductListMapper;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.presenter.ReviewProductPresenter;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.factory.ImageUploadFactory;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.mapper.GenerateHostMapper;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.mapper.UploadImageMapper;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.repository.ImageUploadRepository;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.repository.ImageUploadRepositoryImpl;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor.GenerateHostUseCase;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor.UploadImageUseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author by nisie on 8/11/17.
 */

@Module
public class ReputationModule {

    @ReputationScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }

    @ReputationScope
    @Provides
    GetFirstTimeInboxReputationUseCase
    provideGetFirstTimeInboxReputationUseCase(ThreadExecutor threadExecutor,
                                              PostExecutionThread postExecutionThread,
                                              GetInboxReputationUseCase getInboxReputationUseCase,
                                              GetCacheInboxReputationUseCase
                                                      getCacheInboxReputationUseCase,
                                              ReputationRepository reputationRepository) {
        return new GetFirstTimeInboxReputationUseCase(
                threadExecutor,
                postExecutionThread,
                getInboxReputationUseCase,
                getCacheInboxReputationUseCase,
                reputationRepository);
    }

    @ReputationScope
    @Provides
    GetInboxReputationUseCase
    provideGetInboxReputationUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     ReputationRepository reputationRepository) {
        return new GetInboxReputationUseCase(
                threadExecutor,
                postExecutionThread,
                reputationRepository);
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
            SendReviewValidateMapper sendReviewValidateMapper,
            SendReviewSubmitMapper sendReviewSubmitMapper,
            SkipReviewMapper skipReviewMapper,
            ShopFavoritedMapper shopFavoritedMapper,
            ReportReviewMapper reportReviewMapper,
            GlobalCacheManager globalCacheManager,
            FaveShopActService faveShopActService,
            FaveShopMapper faveShopMapper,
            DeleteReviewResponseMapper deleteReviewResponseMapper,
            ReplyReviewMapper replyReviewMapper,
            GetLikeDislikeMapper getLikeDislikeMapper,
            LikeDislikeMapper likeDislikeMapper,
            ReviewProductApi reputationReviewApi) {
        return new ReputationFactory(tomeService, reputationService, inboxReputationMapper,
                inboxReputationDetailMapper, sendSmileyReputationMapper,
                sendReviewValidateMapper, sendReviewSubmitMapper,
                skipReviewMapper, reportReviewMapper,
                shopFavoritedMapper,
                globalCacheManager,
                faveShopActService,
                faveShopMapper,
                deleteReviewResponseMapper,
                replyReviewMapper,
                getLikeDislikeMapper,
                likeDislikeMapper,
                reputationReviewApi);
    }

    @ReputationScope
    @Provides
    ReviewProductApi provideReputationReviewApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(ReviewProductApi.class);
    }


    @ReputationScope
    @Provides
    InboxReputationMapper provideInboxReputationMapper() {
        return new InboxReputationMapper();
    }

    @ReputationScope
    @Provides
    ReputationService provideReputationService() {
        return new ReputationService();
    }

    @ReputationScope
    @Provides
    InboxReputationDetailMapper provideInboxReputationDetailMapper() {
        return new InboxReputationDetailMapper();
    }

    @ReputationScope
    @Provides
    GetReviewUseCase
    provideGetReviewUseCase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            ReputationRepository reputationRepository) {
        return new GetReviewUseCase(
                threadExecutor,
                postExecutionThread,
                reputationRepository);
    }

    @ReputationScope
    @Provides
    GetInboxReputationDetailUseCase
    provideGetInboxReputationDetailUseCase(ThreadExecutor threadExecutor,
                                           PostExecutionThread postExecutionThread,
                                           GetInboxReputationUseCase getInboxReputationUseCase,
                                           GetReviewUseCase getReviewUseCase,
                                           CheckShopFavoritedUseCase checkShopFavoritedUseCase) {
        return new GetInboxReputationDetailUseCase(
                threadExecutor,
                postExecutionThread,
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
    provideSendSmileyReputationUseCase(ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread,
                                       ReputationRepository reputationRepository) {
        return new SendSmileyReputationUseCase(
                threadExecutor,
                postExecutionThread,
                reputationRepository);
    }


    @ReputationScope
    @Provides
    SendReviewValidateMapper provideSendReviewValidateMapper() {
        return new SendReviewValidateMapper();
    }


    @ReputationScope
    @Provides
    SendReviewValidateUseCase
    provideSendReviewValidateUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     ReputationRepository reputationRepository) {
        return new SendReviewValidateUseCase(
                threadExecutor,
                postExecutionThread,
                reputationRepository);
    }

    @ReputationScope
    @Provides
    SendReviewUseCase
    provideSendReviewUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             SendReviewValidateUseCase sendReviewValidateUseCase,
                             GenerateHostUseCase generateHostUseCase,
                             UploadImageUseCase uploadImageUseCase,
                             SendReviewSubmitUseCase sendReviewSubmitUseCase
    ) {
        return new SendReviewUseCase(
                threadExecutor,
                postExecutionThread,
                sendReviewValidateUseCase,
                generateHostUseCase,
                uploadImageUseCase,
                sendReviewSubmitUseCase
        );
    }

    @ReputationScope
    @Provides
    GetSendReviewFormUseCase
    provideGetSendReviewFormUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    GlobalCacheManager globalCacheManager) {
        return new GetSendReviewFormUseCase(
                threadExecutor,
                postExecutionThread,
                globalCacheManager);
    }

    @ReputationScope
    @Provides
    SetReviewFormCacheUseCase
    provideSetReviewFormCacheUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     GlobalCacheManager globalCacheManager) {
        return new SetReviewFormCacheUseCase(
                threadExecutor,
                postExecutionThread,
                globalCacheManager);
    }

    @ReputationScope
    @Provides
    UploadImageUseCase
    provideUploadImageUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              ImageUploadRepository imageUploadRepository) {
        return new UploadImageUseCase(
                threadExecutor,
                postExecutionThread,
                imageUploadRepository);
    }

    @ReputationScope
    @Provides
    GenerateHostUseCase
    provideGenerateHostUseCase(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread,
                               ImageUploadRepository imageUploadRepository) {
        return new GenerateHostUseCase(
                threadExecutor,
                postExecutionThread,
                imageUploadRepository);
    }

    @ReputationScope
    @Provides
    ImageUploadRepository
    provideImageUploadRepository(ImageUploadFactory imageUploadFactory) {
        return new ImageUploadRepositoryImpl(imageUploadFactory);
    }

    @ReputationScope
    @Provides
    ImageUploadFactory
    provideImageUploadFactory(GenerateHostActService generateHostActService,
                              UploadImageService uploadImageService,
                              GenerateHostMapper generateHostMapper,
                              UploadImageMapper uploadImageMapper) {
        return new ImageUploadFactory(generateHostActService,
                uploadImageService,
                generateHostMapper,
                uploadImageMapper);
    }

    @ReputationScope
    @Provides
    GenerateHostActService
    provideGenerateHostActService() {
        return new GenerateHostActService();
    }

    @ReputationScope
    @Provides
    UploadImageService
    provideUploadImageService() {
        return new UploadImageService();
    }

    @ReputationScope
    @Provides
    GenerateHostMapper
    provideGenerateHostMapper() {
        return new GenerateHostMapper();
    }

    @ReputationScope
    @Provides
    UploadImageMapper
    provideUploadImageMapper() {
        return new UploadImageMapper();
    }

    @ReputationScope
    @Provides
    SendReviewSubmitUseCase
    provideSendReviewSubmitUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   ReputationRepository reputationRepository) {
        return new SendReviewSubmitUseCase(
                threadExecutor,
                postExecutionThread,
                reputationRepository);
    }

    @ReputationScope
    @Provides
    SendReviewSubmitMapper provideSendReviewSubmitMapper() {
        return new SendReviewSubmitMapper();
    }


    @ReputationScope
    @Provides
    SkipReviewUseCase provideSkipReviewUseCase(ThreadExecutor threadExecutor,
                                               PostExecutionThread postExecutionThread,
                                               ReputationRepository reputationRepository) {
        return new SkipReviewUseCase(threadExecutor, postExecutionThread, reputationRepository);
    }


    @ReputationScope
    @Provides
    SkipReviewMapper provideSkipReviewMapper() {
        return new SkipReviewMapper();
    }

    @ReputationScope
    @Provides
    EditReviewValidateUseCase provideEditReviewValidateUseCase(ThreadExecutor threadExecutor,
                                                               PostExecutionThread postExecutionThread,
                                                               ReputationRepository reputationRepository) {
        return new EditReviewValidateUseCase(threadExecutor, postExecutionThread, reputationRepository);
    }

    @ReputationScope
    @Provides
    EditReviewSubmitUseCase provideEditReviewSubmitUseCase(ThreadExecutor threadExecutor,
                                                           PostExecutionThread postExecutionThread,
                                                           ReputationRepository reputationRepository) {
        return new EditReviewSubmitUseCase(threadExecutor, postExecutionThread, reputationRepository);
    }

    @ReputationScope
    @Provides
    EditReviewUseCase provideEditReviewUseCase(ThreadExecutor threadExecutor,
                                               PostExecutionThread postExecutionThread,
                                               EditReviewValidateUseCase editReviewValidateUseCase,
                                               GenerateHostUseCase generateHostUseCase,
                                               UploadImageUseCase uploadImageUseCase,
                                               EditReviewSubmitUseCase editReviewSubmitUseCase) {
        return new EditReviewUseCase(threadExecutor, postExecutionThread,
                editReviewValidateUseCase, generateHostUseCase,
                uploadImageUseCase, editReviewSubmitUseCase);
    }

    @ReputationScope
    @Provides
    ReportReviewMapper provideReportReviewMapper() {
        return new ReportReviewMapper();
    }

    @ReputationScope
    @Provides
    ReportReviewUseCase provideReportReviewUseCase(ThreadExecutor threadExecutor,
                                                   PostExecutionThread postExecutionThread,
                                                   ReputationRepository reputationRepository) {
        return new ReportReviewUseCase(threadExecutor, postExecutionThread, reputationRepository);
    }

    @ReputationScope
    @Provides
    GetCacheInboxReputationUseCase provideGetCacheInboxReputationUseCase(ThreadExecutor threadExecutor,
                                                                         PostExecutionThread postExecutionThread,
                                                                         ReputationRepository reputationRepository) {
        return new GetCacheInboxReputationUseCase(threadExecutor, postExecutionThread, reputationRepository);
    }


    @ReputationScope
    @Provides
    TomeService provideTomeService() {
        return new TomeService();
    }

    @ReputationScope
    @Provides
    ShopFavoritedMapper provideShopFavoritedMapper() {
        return new ShopFavoritedMapper();
    }

    @ReputationScope
    @Provides
    CheckShopFavoritedUseCase provideCheckShopFavoritedUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ReputationRepository reputationRepository
    ) {
        return new CheckShopFavoritedUseCase(threadExecutor, postExecutionThread, reputationRepository);
    }

    @ReputationScope
    @Provides
    FavoriteShopUseCase provideFavoriteShopUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ReputationRepository reputationRepository
    ) {
        return new FavoriteShopUseCase(threadExecutor, postExecutionThread, reputationRepository);
    }

    @ReputationScope
    @Provides
    FaveShopMapper provideFaveShopMapper() {
        return new FaveShopMapper();
    }

    @ReputationScope
    @Provides
    FaveShopActService provideFaveShopActService() {
        return new FaveShopActService();
    }

    @ReputationScope
    @Provides
    DeleteReviewResponseUseCase provideDeleteReviewResponseUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ReputationRepository reputationRepository) {
        return new DeleteReviewResponseUseCase(threadExecutor, postExecutionThread,
                reputationRepository);
    }

    @ReputationScope
    @Provides
    DeleteReviewResponseMapper provideDeleteReviewResponseMapper() {
        return new DeleteReviewResponseMapper();
    }

    @ReputationScope
    @Provides
    SendReplyReviewUseCase provideSendReplyReviewUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ReputationRepository reputationRepository) {
        return new SendReplyReviewUseCase(threadExecutor, postExecutionThread,
                reputationRepository);
    }

    @ReputationScope
    @Provides
    ReplyReviewMapper provideReplyReviewMapper() {
        return new ReplyReviewMapper();
    }

    @ReputationScope
    @Provides
    LikeDislikeReviewUseCase provideLikeDislikeReviewUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ReputationRepository reputationRepository) {
        return new LikeDislikeReviewUseCase(threadExecutor, postExecutionThread,
                reputationRepository);
    }

    @ReputationScope
    @Provides
    GetLikeDislikeMapper provideGetLikeDislikeMapper() {
        return new GetLikeDislikeMapper();
    }

    @ReputationScope
    @Provides
    GetLikeDislikeReviewUseCase provideGetLikeDislikeReviewUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ReputationRepository reputationRepository) {
        return new GetLikeDislikeReviewUseCase(threadExecutor, postExecutionThread,
                reputationRepository);
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
    ReputationTracking reputationTracking(@ApplicationContext Context context){
        if(context instanceof ReputationRouter){
            return new ReputationTracking((ReputationRouter) context);
        }else{
            return null;
        }
    }
}
