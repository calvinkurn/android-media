package com.tokopedia.core.drawer.interactor;

import android.content.Context;

import com.tokopedia.core.drawer.var.NotificationItem;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationData;
import com.tokopedia.core.network.apiservices.transaction.DepositService;
import com.tokopedia.core.network.apiservices.user.NotificationService;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.var.TkpdCache;

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
    private static final String KEY_TOKOCASH_DATA = TkpdCache.Key.KEY_TOKOCASH_DATA;

    private final PeopleService peopleService;
    private final DepositService depositService;
    private final NotificationService notificationService;
    private final CompositeSubscription compositeSubscription;

    public NetworkInteractorImpl() {
        peopleService = new PeopleService();
        depositService = new DepositService();
        notificationService = new NotificationService();
        compositeSubscription = new CompositeSubscription();
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
                        listener.onError("Terjadi Kesalahan Koneksi");
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

}
