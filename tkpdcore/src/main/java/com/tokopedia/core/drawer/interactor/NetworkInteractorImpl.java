package com.tokopedia.core.drawer.interactor;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer.model.DrawerHeader;
import com.tokopedia.core.drawer.model.LoyaltyItem.LoyaltyItem;
import com.tokopedia.core.drawer.model.notification.NotificationData;
import com.tokopedia.core.drawer.model.profileinfo.ProfileData;
import com.tokopedia.core.drawer.model.topcastItem.TopCashItem;
import com.tokopedia.core.drawer.var.NotificationItem;
import com.tokopedia.core.exception.SessionExpiredException;
import com.tokopedia.core.network.apiservices.clover.CloverService;
import com.tokopedia.core.network.apiservices.transaction.DepositService;
import com.tokopedia.core.network.apiservices.transaction.TokoCashService;
import com.tokopedia.core.network.apiservices.user.NotificationService;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONException;

import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Angga.Prasetiyo on 18/12/2015.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class NetworkInteractorImpl implements NetworkInteractor {
    private static final String TAG = NetworkInteractorImpl.class.getSimpleName();
    private static final String KEY_TOKOCASH_DATA = "TOKOCASH_DATA";

    private final PeopleService peopleService;
    private final CloverService cloverService;
    private final DepositService depositService;
    private final NotificationService notificationService;
    private final TokoCashService tokoCashService;
    private final CompositeSubscription compositeSubscription;

    public NetworkInteractorImpl() {
        peopleService = new PeopleService();
        depositService = new DepositService();
        cloverService = new CloverService();
        notificationService = new NotificationService();
        tokoCashService = new TokoCashService();
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
    public void getTokoCash(final Context context, final TopCashListener listener) {
        SessionHandler sessionHandler = new SessionHandler(context);
        tokoCashService.setToken(sessionHandler.getAccessToken(context));
        Observable<TopCashItem> observable = Observable
                .concat(getObservableFetchCacheTokoCashData(),
                        getObservableFetchNetworkTokoCashData())
                .first(isTokoCashDataAvailable());
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fetchTokoCashSubscriber(context, listener)));
    }

    @Override
    public void updateTokoCash(final Context context, final TopCashListener listener) {
        SessionHandler sessionHandler = new SessionHandler(context);
        tokoCashService.setToken(sessionHandler.getAccessToken(context));
        compositeSubscription.add(getObservableFetchNetworkTokoCashData()
                .subscribe(fetchTokoCashSubscriber(context, listener)));
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

    private DrawerHeader parseToDrawerHeader(ProfileData data) {
        DrawerHeader drawerHeader = new DrawerHeader();

        if (data.getShopInfo() != null) {
            drawerHeader.shopName = MethodChecker.fromHtml(data.getShopInfo().getShopName()).toString();
            drawerHeader.shopIcon = data.getShopInfo().getShopAvatar();
            drawerHeader.shopCover = data.getShopInfo().getShopCover();
            drawerHeader.ShopId = data.getShopInfo().getShopId();
        } else {
            drawerHeader.shopName = null;
            drawerHeader.shopIcon = null;
            drawerHeader.shopCover = null;
            drawerHeader.ShopId = null;
        }
        drawerHeader.userName = MethodChecker.fromHtml(data.getUserInfo().getUserName()).toString();
        drawerHeader.userIcon = data.getUserInfo().getUserImage();
        drawerHeader.timestamp = Long.toString(System.currentTimeMillis() / 1000);
        return drawerHeader;
    }

    private Observable<TopCashItem> getObservableFetchNetworkTokoCashData() {
        return tokoCashService.getApi()
                .getTokoCash()
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<TopCashItem>, Observable<TopCashItem>>() {
                    @Override
                    public Observable<TopCashItem> call(Response<TopCashItem> topCashItemResponse) {
                        return Observable.just(topCashItemResponse.body());
                    }
                }).doOnNext(storeTokoCashItemToDatabase());
    }

    private Observable<TopCashItem> getObservableFetchCacheTokoCashData() {
        return Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Boolean, TopCashItem>() {
            @Override
            public TopCashItem call(Boolean aBoolean) {
                GlobalCacheManager cacheManager = new GlobalCacheManager();
                return CacheUtil.convertStringToModel(cacheManager
                        .getValueString(KEY_TOKOCASH_DATA),
                        new TypeToken<TopCashItem>(){}.getType());
            }
        }).onErrorReturn(new Func1<Throwable, TopCashItem>() {
                    @Override
                    public TopCashItem call(Throwable throwable) {
                        return null;
                    }
                });
    }

    private Action1<TopCashItem> storeTokoCashItemToDatabase() {
        return new Action1<TopCashItem> () {
            @Override
            public void call(TopCashItem topCashItem) {
                GlobalCacheManager cacheManager = new GlobalCacheManager();
                if (topCashItem != null) {
                    cacheManager.setKey(KEY_TOKOCASH_DATA);
                    cacheManager.setValue(CacheUtil.convertModelToString(topCashItem,
                            new TypeToken<TopCashItem>() {
                            }.getType()));
                    cacheManager.setCacheDuration(60);
                    cacheManager.store();
                }
            }
        };
    }

    private Func1<TopCashItem, Boolean> isTokoCashDataAvailable() {
        return new Func1<TopCashItem, Boolean>() {
            @Override
            public Boolean call(TopCashItem topCashItem) {
                boolean dataIsNotNull = topCashItem != null && topCashItem.getData()!= null;
                return topCashItem != null && topCashItem.getData()!= null;
            }
        };
    }

    private Subscriber<TopCashItem> fetchTokoCashSubscriber(final Context context,
                                                            final TopCashListener listener) {
        return new Subscriber<TopCashItem>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof SessionExpiredException
                        && SessionHandler.isV4Login(context)) {
                    listener.onTokenExpire();
                }
            }

            @Override
            public void onNext(TopCashItem topCashItemResponse) {
                listener.onSuccess(topCashItemResponse);
            }
        };
    }
}
