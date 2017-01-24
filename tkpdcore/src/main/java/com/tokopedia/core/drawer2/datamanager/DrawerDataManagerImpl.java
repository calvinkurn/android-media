package com.tokopedia.core.drawer2.datamanager;

import android.content.Context;
import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.database.model.category.CategoryData;
import com.tokopedia.core.drawer.model.LoyaltyItem.LoyaltyItem;
import com.tokopedia.core.drawer.model.notification.NotificationData;
import com.tokopedia.core.drawer.model.profileinfo.ProfileData;
import com.tokopedia.core.drawer2.interactor.DepositNetworkInteractor;
import com.tokopedia.core.drawer2.interactor.DepositNetworkInteractorImpl;
import com.tokopedia.core.drawer2.interactor.NotificationNetworkInteractor;
import com.tokopedia.core.drawer2.interactor.NotificationNetworkInteractorImpl;
import com.tokopedia.core.drawer2.interactor.ProfileNetworkInteractor;
import com.tokopedia.core.drawer2.interactor.ProfileNetworkInteractorImpl;
import com.tokopedia.core.drawer2.interactor.TopPointsNetworkInteractor;
import com.tokopedia.core.drawer2.interactor.TopPointsNetworkInteractorImpl;
import com.tokopedia.core.drawer2.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.viewmodel.DrawerProfile;
import com.tokopedia.core.network.apiservices.clover.CloverService;
import com.tokopedia.core.network.apiservices.transaction.DepositService;
import com.tokopedia.core.network.apiservices.user.NotificationService;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.drawer2.viewmodel.DrawerData;

import org.json.JSONException;

import retrofit2.Response;
import rx.Observable;
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


    public DrawerDataManagerImpl() {
        profileNetworkInteractor = new ProfileNetworkInteractorImpl(new PeopleService());
        notificationNetworkInteractor = new NotificationNetworkInteractorImpl(new NotificationService());
        depositNetworkInteractor = new DepositNetworkInteractorImpl(new DepositService());
        topPointsNetworkInteractor = new TopPointsNetworkInteractorImpl(new CloverService());
    }

    @Override
    public Observable<DrawerData> getDrawerData(Context context) {
        DrawerData drawerData = new DrawerData();

//        Observable<Response<TkpdResponse>> notificationObservable =
//                profileNetworkInteractor.getProfileInfo(context, new TKPDMapParam<String, String>());

        return Observable.merge(getProfileInfoObservable(drawerData, context),
                getDepositObservable(drawerData, context),
                getTopPointsObservable(drawerData, context),
                getNotificationsObservable(drawerData, context)
        ).first(new Func1<DrawerData, Boolean>() {
            @Override
            public Boolean call(DrawerData drawerData) {
                return drawerData.getDrawerProfile() != null
                        && drawerData.getDrawerProfile().getDeposit() != null
                        && drawerData.getDrawerProfile().getUserName() != null
                        && drawerData.getDrawerProfile().getTopPoints() != null
                        && drawerData.getDrawerNotification() != null;
            }
        });

    }

    private Observable<DrawerData> getNotificationsObservable(final DrawerData drawerData, Context context) {
        return notificationNetworkInteractor.getNotification(context, new TKPDMapParam<String, String>())
                .flatMap(new Func1<Response<TkpdResponse>, Observable<DrawerData>>() {
                    @Override
                    public Observable<DrawerData> call(Response<TkpdResponse> response) {
                        NotificationData notificationData = response.body().convertDataObj(NotificationData.class);
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

                        drawerData.setDrawerNotification(drawerNotification);
                        return Observable.just(drawerData);
                    }
                })
                .onErrorReturn(new Func1<Throwable, DrawerData>() {
                    @Override
                    public DrawerData call(Throwable throwable) {
                        Log.d(TAG, throwable.toString());
                        drawerData.setDrawerNotification(new DrawerNotification());
                        return null;
                    }
                });
    }

    private Observable<DrawerData> getTopPointsObservable(final DrawerData drawerData, Context context) {
        return topPointsNetworkInteractor.getTopPoints(
                context, new TKPDMapParam<String, String>())
                .flatMap(new Func1<Response<TkpdResponse>, Observable<DrawerData>>() {
                    @Override
                    public Observable<DrawerData> call(Response<TkpdResponse> response) {
                        LoyaltyItem topPoints = convertToTopPoints(response);
                        drawerData.getDrawerProfile().setTopPoints(topPoints.getLoyaltyPoint().getAmount());
                        drawerData.getDrawerProfile().setTopPointsUrl(topPoints.getUri());
                        return Observable.just(drawerData);
                    }
                })
                .onErrorReturn(new Func1<Throwable, DrawerData>() {
                    @Override
                    public DrawerData call(Throwable throwable) {
                        Log.d(TAG, throwable.toString());
                        drawerData.getDrawerProfile().setTopPoints("");
                        drawerData.getDrawerProfile().setTopPointsUrl("");
                        return drawerData;
                    }
                });
    }

    private LoyaltyItem convertToTopPoints(Response<TkpdResponse> response) {
        return response.body().convertDataObj(LoyaltyItem.class);
    }

    private Observable<DrawerData> getDepositObservable(final DrawerData drawerData, Context context) {
        return depositNetworkInteractor.getDeposit(
                context, new TKPDMapParam<String, String>())
                .flatMap(new Func1<Response<TkpdResponse>, Observable<DrawerData>>() {
                    @Override
                    public Observable<DrawerData> call(Response<TkpdResponse> depositResponse) {

                        String deposit = convertToDeposit(depositResponse);
                        drawerData.getDrawerProfile().setDeposit(deposit);
                        return Observable.just(drawerData);
                    }
                })
                .onErrorReturn(new Func1<Throwable, DrawerData>() {
                    @Override
                    public DrawerData call(Throwable throwable) {
                        Log.d(TAG, throwable.toString());
                        drawerData.getDrawerProfile().setDeposit("");
                        return drawerData;
                    }
                });
    }

    private Observable<DrawerData> getProfileInfoObservable(final DrawerData drawerData, Context context) {
        return profileNetworkInteractor.getProfileInfo(
                context, new TKPDMapParam<String, String>())
                .flatMap(new Func1<Response<TkpdResponse>, Observable<DrawerData>>() {
                    @Override
                    public Observable<DrawerData> call(Response<TkpdResponse> profileResponse) {
                        DrawerProfile drawerProfile = convertToDrawerProfile(profileResponse);
                        drawerData.setDrawerProfile(drawerProfile);
                        return Observable.just(drawerData);
                    }
                })
                .onErrorReturn(new Func1<Throwable, DrawerData>() {
                    @Override
                    public DrawerData call(Throwable throwable) {
                        Log.d(TAG, throwable.toString());
                        DrawerProfile drawerProfile = new DrawerProfile();
                        drawerData.setDrawerProfile(drawerProfile);
                        return drawerData;
                    }
                });

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
