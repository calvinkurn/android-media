package com.tokopedia.core.drawer.interactor;

import android.content.Context;
import android.text.Html;

import com.tokopedia.core.drawer.DrawerVariable;
import com.tokopedia.core.drawer.model.LoyaltyItem.LoyaltyItem;
import com.tokopedia.core.drawer.model.notification.NotificationData;
import com.tokopedia.core.drawer.model.profileinfo.ProfileData;
import com.tokopedia.core.drawer.var.NotificationItem;
import com.tokopedia.core.network.apiservices.clover.CloverService;
import com.tokopedia.core.network.apiservices.transaction.DepositService;
import com.tokopedia.core.network.apiservices.user.NotificationService;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import org.json.JSONException;

import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Angga.Prasetiyo on 18/12/2015.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class NetworkInteractorImpl implements NetworkInteractor {
    private static final String TAG = NetworkInteractorImpl.class.getSimpleName();

    private final PeopleService peopleService;
    private final CloverService cloverService;
    private final DepositService depositService;
    private final NotificationService notificationService;
    private final CompositeSubscription compositeSubscription;

    public NetworkInteractorImpl() {
        peopleService = new PeopleService();
        depositService = new DepositService();
        cloverService = new CloverService();
        notificationService = new NotificationService();
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getProfileInfo(final Context context, final ProfileInfoListener listener) {
        Observable<Response<TkpdResponse>> observable = peopleService.getApi()
                .getPeopleInfo(AuthUtil.generateParams(context, new HashMap<String, String>()));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.toString());
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(parseToDrawerHeader(response.body()
                            .convertDataObj(ProfileData.class)));
                } else {
                    listener.onError(response.message());
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    public void getDeposit(Context context, final DepositListener listener) {
        Observable<Response<TkpdResponse>> observable = depositService.getApi()
                .getDeposit(AuthUtil.generateParams(context, new HashMap<String, String>()));

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<TkpdResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            try {
                                listener.onSuccess(response.body().getJsonData().getString("deposit_total"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.onError("Fail parsing");
                            }
                        } else {
                            listener.onError(response.message());
                        }
                    }
                }));
    }

    @Override
    public void getNotification(final Context context, final NotificationListener listener) {
        Observable<Response<TkpdResponse>> observable = notificationService.getApi()
                .getNotification(AuthUtil.generateParams(context, new HashMap<String, String>()));

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<TkpdResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                listener.onSuccess(parseNotifItem(context,
                                        response.body().convertDataObj(NotificationData.class)));
                            }
                        } else {
                            listener.onError(response.message());
                        }
                    }
                }));
    }

    @Override
    public void getLoyalty(Context context, final LoyaltyListener listener) {
        Observable<Response<TkpdResponse>> observable = cloverService.getApi()
                .getTopPoints(AuthUtil.generateParams(context, new HashMap<String, String>()));

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<TkpdResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body().convertDataObj(LoyaltyItem.class));
                        } else {
                            listener.onError(response.message());
                        }
                    }
                }));
    }

    @Override
    public void resetNotification(Context context, final ResetNotificationListener listener) {
        Observable<Response<TkpdResponse>> observable = notificationService.getApi()
                .resetNotification(AuthUtil.generateParams(context, new HashMap<String, String>()));

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<TkpdResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess();
                        } else {
                            listener.onError(response.message());
                        }
                    }
                }));
    }

    @Override
    public void unsubscribe() {
        compositeSubscription.unsubscribe();
    }

    private NotificationItem parseNotifItem(Context context, NotificationData data) {
        NotificationItem notif = new NotificationItem(context);
        notif.new_order = data.getSales().getSalesNewOrder();
        notif.shipping_confirm = data.getSales().getSalesShippingConfirm();
        notif.shipping_status = data.getSales().getSalesShippingStatus();
        notif.payment_conf = data.getPurchase().getPurchasePaymentConf();
        notif.payment_confirmed = data.getPurchase().getPurchasePaymentConfirm();
        notif.order_status = data.getPurchase().getPurchaseOrderStatus();
        notif.delivery_confirm = data.getPurchase().getPurchaseDeliveryConfirm();
        notif.reorder = data.getPurchase().getPurchaseReorder();
        notif.message = data.getInbox().getInboxMessage();
        notif.talk = data.getInbox().getInboxTalk();
        notif.reputation = data.getInbox().getInboxReputation();
        notif.ticket = data.getInbox().getInboxTicket();
        notif.resolution = data.getResolution();
        notif.inc_notif = data.getIncrNotif();
        notif.total_cart = data.getTotalCart();
        notif.total_notif = data.getTotalNotif();
        notif.setNotifToCache();
        return notif;
    }

    private DrawerVariable.DrawerHeader parseToDrawerHeader(ProfileData data) {
        DrawerVariable.DrawerHeader drawerHeader = new DrawerVariable.DrawerHeader();

        if (data.getShopInfo() != null) {
            drawerHeader.shopName = Html.fromHtml(data.getShopInfo().getShopName()).toString();
            drawerHeader.shopIcon = data.getShopInfo().getShopAvatar();
            drawerHeader.shopCover = data.getShopInfo().getShopCover();
            drawerHeader.ShopId = data.getShopInfo().getShopId();
        } else {
            drawerHeader.shopName = null;
            drawerHeader.shopIcon = null;
            drawerHeader.shopCover = null;
            drawerHeader.ShopId = null;
        }
        drawerHeader.userName = Html.fromHtml(data.getUserInfo().getUserName()).toString();
        drawerHeader.userIcon = data.getUserInfo().getUserImage();
        drawerHeader.timestamp = Long.toString(System.currentTimeMillis() / 1000);
        return drawerHeader;
    }


}
