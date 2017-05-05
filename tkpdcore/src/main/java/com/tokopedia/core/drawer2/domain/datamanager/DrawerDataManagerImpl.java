package com.tokopedia.core.drawer2.domain.datamanager;

import android.content.Context;
import android.os.Bundle;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer.model.LoyaltyItem.LoyaltyItem;
import com.tokopedia.core.drawer.model.profileinfo.ProfileData;
import com.tokopedia.core.drawer.model.topcastItem.TopCashItem;
import com.tokopedia.core.drawer2.data.factory.DepositSourceFactory;
import com.tokopedia.core.drawer2.data.factory.NotificationSourceFactory;
import com.tokopedia.core.drawer2.data.factory.TokoCashSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.DepositMapper;
import com.tokopedia.core.drawer2.data.mapper.NotificationMapper;
import com.tokopedia.core.drawer2.data.mapper.TokoCashMapper;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationData;
import com.tokopedia.core.drawer2.data.repository.DepositRepositoryImpl;
import com.tokopedia.core.drawer2.data.repository.NotificationRepositoryImpl;
import com.tokopedia.core.drawer2.data.repository.TokoCashRepositoryImpl;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTopPoints;
import com.tokopedia.core.drawer2.domain.DepositRepository;
import com.tokopedia.core.drawer2.domain.NotificationRepository;
import com.tokopedia.core.drawer2.domain.TokoCashRepository;
import com.tokopedia.core.drawer2.domain.interactor.DepositUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.ProfileNetworkInteractor;
import com.tokopedia.core.drawer2.domain.interactor.ProfileNetworkInteractorImpl;
import com.tokopedia.core.drawer2.domain.interactor.TokoCashUseCase;
import com.tokopedia.core.drawer2.domain.interactor.TopPointsNetworkInteractor;
import com.tokopedia.core.drawer2.domain.interactor.TopPointsNetworkInteractorImpl;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.subscriber.GetDepositSubscriber;
import com.tokopedia.core.drawer2.view.subscriber.NotificationSubscriber;
import com.tokopedia.core.drawer2.view.subscriber.TokoCashSubscriber;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.apiservices.clover.CloverService;
import com.tokopedia.core.network.apiservices.transaction.DepositService;
import com.tokopedia.core.network.apiservices.user.NotificationService;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nisie on 1/23/17.
 */

public class DrawerDataManagerImpl implements DrawerDataManager {

    private static final String TAG = DrawerDataManagerImpl.class.getSimpleName();
    private final ProfileNetworkInteractor profileNetworkInteractor;
    private final TopPointsNetworkInteractor topPointsNetworkInteractor;
    private final DepositUseCase depositUseCase;
    private final NotificationUseCase notificationUseCase;
    private final TokoCashUseCase tokoCashUseCase;
    private final DrawerDataListener viewListener;
    private final SessionHandler sessionHandler;
    private final LocalCacheHandler drawerCache;
    private final GlobalCacheManager cacheManager;

    public DrawerDataManagerImpl(Context context, DrawerDataListener viewListener) {
        this.viewListener = viewListener;
        sessionHandler = new SessionHandler(context);
        cacheManager = new GlobalCacheManager();
        drawerCache = new LocalCacheHandler(context, DrawerHelper.DRAWER_CACHE);
        profileNetworkInteractor = new ProfileNetworkInteractorImpl(new PeopleService());
        topPointsNetworkInteractor = new TopPointsNetworkInteractorImpl(new CloverService());

        Bundle bundle = new Bundle();
        String authKey = sessionHandler.getAccessToken(context);
        authKey = "Bearer" + " " + authKey;
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        AccountsService accountsService = new AccountsService(bundle);

        TokoCashSourceFactory tokoCashSourceFactory = new TokoCashSourceFactory(
                context,
                accountsService,
                new TokoCashMapper(),
                cacheManager);

        TokoCashRepository tokoCashRepository = new TokoCashRepositoryImpl(tokoCashSourceFactory);
        tokoCashUseCase = new TokoCashUseCase(
                new JobExecutor(),
                new UIThread(),
                tokoCashRepository
        );

        NotificationSourceFactory notificationSourceFactory = new NotificationSourceFactory(
                context,
                new NotificationService(),
                new NotificationMapper(),
                drawerCache
        );
        NotificationRepository notificationRepository = new NotificationRepositoryImpl(notificationSourceFactory);
        notificationUseCase = new NotificationUseCase(
                new JobExecutor(),
                new UIThread(),
                notificationRepository
        );

        DepositSourceFactory depositSourceFactory = new DepositSourceFactory(
                context,
                new DepositService(),
                new DepositMapper(),
                drawerCache);
        DepositRepository depositRepository = new DepositRepositoryImpl(depositSourceFactory);
        depositUseCase = new DepositUseCase(
                new JobExecutor(),
                new UIThread(),
                depositRepository);
    }

    @Override
    public Observable<DrawerProfile> getDrawerProfile(Context context) {
        return profileNetworkInteractor.getProfileInfo(context, new TKPDMapParam<String, String>())
                .flatMap(new Func1<Response<TkpdResponse>, Observable<DrawerProfile>>() {
                    @Override
                    public Observable<DrawerProfile> call(Response<TkpdResponse> response) {
                        DrawerProfile drawerProfile = convertToDrawerProfile(response);
                        return Observable.just(drawerProfile);
                    }
                });
    }

    @Override
    public void getDeposit() {
        depositUseCase.execute(getDepositParam(), new GetDepositSubscriber(viewListener));
    }

    private RequestParams getDepositParam() {
        return RequestParams.EMPTY;
    }

    @Override
    public Observable<DrawerTopPoints> getTopPoints(Context context) {
        return topPointsNetworkInteractor.getTopPoints(context, new TKPDMapParam<String, String>())
                .flatMap(new Func1<Response<TkpdResponse>, Observable<DrawerTopPoints>>() {
                    @Override
                    public Observable<DrawerTopPoints> call(Response<TkpdResponse> response) {
                        LoyaltyItem topPoints = convertToTopPoints(response);
                        DrawerTopPoints drawerTopPoints = new DrawerTopPoints();
                        drawerTopPoints.setTopPoints(topPoints.getLoyaltyPoint().getAmount());
                        drawerTopPoints.setTopPointsUrl(topPoints.getUri());
                        return Observable.just(drawerTopPoints);
                    }
                });
    }

    @Override
    public void getTokoCash() {

        tokoCashUseCase.execute(getTokoCashParam(), new TokoCashSubscriber(viewListener));

//        return tokoCashNetworkInteractor.getTokoCash(accessToken)
//                .flatMap(new Func1<Response<TopCashItem>, Observable<DrawerTokoCash>>() {
//                    @Override
//                    public Observable<DrawerTokoCash> call(Response<TopCashItem> response) {
//                        TopCashItem topCashItem = response.body();
//                        DrawerTokoCash drawerTokoCash = new DrawerTokoCash();
//                        drawerTokoCash.setHasTokoCash(hasTokoCash(topCashItem));
//                        drawerTokoCash.setTokoCash(topCashItem.getData().getBalance());
//                        drawerTokoCash.setTokoCashUrl(topCashItem.getData().getRedirectUrl());
//                        drawerTokoCash.setTokoCashLabel(topCashItem.getData().getText());
//                        return Observable.just(drawerTokoCash);
//                    }
//                });
    }

    private RequestParams getTokoCashParam() {
        return RequestParams.EMPTY;
    }

    @Override
    public void getNotification() {
        notificationUseCase.execute(getNotificationParam(), new NotificationSubscriber(viewListener));
    }

    private RequestParams getNotificationParam() {
        return RequestParams.EMPTY;
    }

    private DrawerNotification convertToDrawerNotification(NotificationData notificationData) {
        DrawerNotification drawerNotification = new DrawerNotification();
        drawerNotification.setInboxMessage(notificationData.getInbox().getInboxMessage());
        drawerNotification.setInboxResCenter(notificationData.getResolution());
        drawerNotification.setInboxReview(notificationData.getInbox().getInboxReputation());
        drawerNotification.setInboxTalk(notificationData.getInbox().getInboxTalk());
        drawerNotification.setInboxTicket(notificationData.getInbox().getInboxTicket());

        drawerNotification.setPurchaseDeliveryConfirm(notificationData.getPurchase().getPurchaseDeliveryConfirm());
        drawerNotification.setPurchaseOrderStatus(notificationData.getPurchase().getPurchaseOrderStatus());
        drawerNotification.setPurchasePaymentConfirm(notificationData.getPurchase().getPurchasePaymentConfirm());
        drawerNotification.setPurchaseReorder(notificationData.getPurchase().getPurchaseReorder());

        drawerNotification.setSellingNewOrder(notificationData.getSales().getSalesNewOrder());
        drawerNotification.setSellingShippingConfirmation(notificationData.getSales().getSalesShippingConfirm());
        drawerNotification.setSellingShippingStatus(notificationData.getSales().getSalesShippingStatus());
        drawerNotification.setTotalNotif(notificationData.getTotalNotif());
        drawerNotification.setIncrNotif(notificationData.getIncrNotif());
        drawerNotification.setTotalCart(notificationData.getTotalCart());
        return drawerNotification;
    }

    private boolean hasTokoCash(TopCashItem topCashItem) {
        return (topCashItem.getData().getLink() == 1) || (topCashItem.getData().getLink() == 0 && topCashItem.getData().getAction() != null);
    }

    private LoyaltyItem convertToTopPoints(Response<TkpdResponse> response) {
        return response.body().convertDataObj(LoyaltyItem.class);
    }

    private DrawerProfile convertToDrawerProfile(Response<TkpdResponse> response) {
        ProfileData profileData = response.body()
                .convertDataObj(ProfileData.class);
        DrawerProfile profile = new DrawerProfile();
        profile.setUserName(profileData.getUserInfo().getUserName());
        profile.setUserAvatar(profileData.getUserInfo().getUserImage());
        profile.setShopName(profileData.getShopInfo().getShopName());
        profile.setShopCover(profileData.getShopInfo().getShopCover());
        profile.setShopAvatar(profileData.getShopInfo().getShopAvatar());
        return profile;
    }

    @Override
    public void unsubscribe() {

    }
}
