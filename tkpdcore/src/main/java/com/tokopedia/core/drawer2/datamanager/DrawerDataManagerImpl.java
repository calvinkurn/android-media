package com.tokopedia.core.drawer2.datamanager;

import android.content.Context;
import android.util.Log;

import com.tokopedia.core.drawer.model.LoyaltyItem.LoyaltyItem;
import com.tokopedia.core.drawer.model.notification.NotificationData;
import com.tokopedia.core.drawer.model.profileinfo.ProfileData;
import com.tokopedia.core.drawer.model.topcastItem.TopCashItem;
import com.tokopedia.core.drawer2.interactor.DepositNetworkInteractor;
import com.tokopedia.core.drawer2.interactor.DepositNetworkInteractorImpl;
import com.tokopedia.core.drawer2.interactor.NotificationNetworkInteractor;
import com.tokopedia.core.drawer2.interactor.NotificationNetworkInteractorImpl;
import com.tokopedia.core.drawer2.interactor.ProfileNetworkInteractor;
import com.tokopedia.core.drawer2.interactor.ProfileNetworkInteractorImpl;
import com.tokopedia.core.drawer2.interactor.TokoCashNetworkInteractor;
import com.tokopedia.core.drawer2.interactor.TokoCashNetworkInteractorImpl;
import com.tokopedia.core.drawer2.interactor.TopPointsNetworkInteractor;
import com.tokopedia.core.drawer2.interactor.TopPointsNetworkInteractorImpl;
import com.tokopedia.core.drawer2.viewmodel.DrawerDeposit;
import com.tokopedia.core.drawer2.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.viewmodel.DrawerTopPoints;
import com.tokopedia.core.network.apiservices.clover.CloverService;
import com.tokopedia.core.network.apiservices.transaction.DepositService;
import com.tokopedia.core.network.apiservices.transaction.TokoCashService;
import com.tokopedia.core.network.apiservices.user.NotificationService;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.drawer2.viewmodel.DrawerData;
import com.tokopedia.core.util.Drawer;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONException;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by nisie on 1/23/17.
 */

public class DrawerDataManagerImpl implements DrawerDataManager {

    private static final String TAG = DrawerDataManagerImpl.class.getSimpleName();
    private final ProfileNetworkInteractor profileNetworkInteractor;
    private final NotificationNetworkInteractor notificationNetworkInteractor;
    private final DepositNetworkInteractor depositNetworkInteractor;
    private final TopPointsNetworkInteractor topPointsNetworkInteractor;
    private final TokoCashNetworkInteractor tokoCashNetworkInteractor;


    public DrawerDataManagerImpl() {
        profileNetworkInteractor = new ProfileNetworkInteractorImpl(new PeopleService());
        notificationNetworkInteractor = new NotificationNetworkInteractorImpl(new NotificationService());
        depositNetworkInteractor = new DepositNetworkInteractorImpl(new DepositService());
        topPointsNetworkInteractor = new TopPointsNetworkInteractorImpl(new CloverService());
        tokoCashNetworkInteractor = new TokoCashNetworkInteractorImpl(new TokoCashService());
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
    public Observable<DrawerDeposit> getDeposit(Context context) {
        return depositNetworkInteractor.getDeposit(context, new TKPDMapParam<String, String>())
                .flatMap(new Func1<Response<TkpdResponse>, Observable<DrawerDeposit>>() {
                    @Override
                    public Observable<DrawerDeposit> call(Response<TkpdResponse> response) {
                        String deposit = convertToDeposit(response);
                        DrawerDeposit drawerDeposit = new DrawerDeposit();
                        drawerDeposit.setDeposit(deposit);
                        return Observable.just(drawerDeposit);
                    }
                });
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
    public Observable<DrawerTokoCash> getTokoCash(String accessToken) {
        return tokoCashNetworkInteractor.getTokoCash(accessToken)
                .flatMap(new Func1<Response<TopCashItem>, Observable<DrawerTokoCash>>() {
                    @Override
                    public Observable<DrawerTokoCash> call(Response<TopCashItem> response) {
                        TopCashItem topCashItem = response.body();
                        DrawerTokoCash drawerTokoCash = new DrawerTokoCash();
                        drawerTokoCash.setHasTokoCash(hasTokoCash(topCashItem));
                        drawerTokoCash.setTokoCash(topCashItem.getData().getBalance());
                        drawerTokoCash.setTokoCashUrl(topCashItem.getData().getRedirectUrl());
                        drawerTokoCash.setTokoCashLabel(topCashItem.getData().getText());
                        return Observable.just(drawerTokoCash);
                    }
                });
    }

    @Override
    public Observable<DrawerNotification> getNotification(Context context) {
        return notificationNetworkInteractor.getNotification(context, new TKPDMapParam<String, String>())
                .flatMap(new Func1<Response<TkpdResponse>, Observable<DrawerNotification>>() {
                    @Override
                    public Observable<DrawerNotification> call(Response<TkpdResponse> response) {
                        NotificationData notificationData = response.body().convertDataObj(NotificationData.class);
                        return Observable.just(convertToDrawerNotification(notificationData));
                    }
                });
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

    private String convertToDeposit(Response<TkpdResponse> response) {
        try {
            return response.body().getJsonData().getString("deposit_total");
        } catch (JSONException e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public void unsubscribe() {

    }
}
