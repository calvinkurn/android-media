package com.tokopedia.sellerapp.home.utils;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.tkpd.library.utils.network.CommonListener;
import com.tkpd.library.utils.network.ManyRequestErrorException;
import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.common.ticker.model.Ticker;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.GCMHandlerListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.util.ShopNetworkController;
import com.tokopedia.sellerapp.home.model.deposit.DepositModel;
import com.tokopedia.sellerapp.home.model.notification.Notification;
import com.tokopedia.sellerapp.home.model.rescenter.ResCenterInboxData;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tkpd.library.utils.network.BaseNetworkController.onResponseError;

/**
 * Created by normansyahputa on 8/30/16.
 */

public class ShopController extends BaseController{

    private static final String TAG = "BaseController";
    private GCMHandler gcmHandler;
    private ShopNetworkController shopNetworkController;
    private NotifNetworkController notifNetworkController;
    private InboxResCenterNetworkController inboxResCenterNetworkController;
    private DepositNetworkController depositNetworkController;
    private ShopTransactionController shopTransactionController;
    private MojitoController mojitoController;
    private Gson gson;

    public ShopController(GCMHandler gcmHandler, ShopNetworkController shopNetworkController,
                          NotifNetworkController notifNetworkController,
                          InboxResCenterNetworkController inboxResCenterNetworkController,
                          DepositNetworkController depositNetworkController,
                          ShopTransactionController shopTransactionController,
                          MojitoController mojitoController, Gson gson) {
        this.gcmHandler = gcmHandler;
        this.shopNetworkController = shopNetworkController;
        this.notifNetworkController = notifNetworkController;
        this.inboxResCenterNetworkController = inboxResCenterNetworkController;
        this.depositNetworkController = depositNetworkController;
        this.shopTransactionController = shopTransactionController;
        this.mojitoController = mojitoController;
        this.gson = gson;
    }

    public void init(GCMHandlerListener gcmHandlerListener){
        if(gcmHandlerListener == null)
            return;

        gcmHandler.actionRegisterOrUpdateDevice(gcmHandlerListener);
    }

    public void getShopInfo(String gcmId, String userId,
                            ShopNetworkController.ShopInfoParam shopInfoParam,
                            ShopNetworkController.GetShopInfo getShopInfo){
        shopNetworkController.getShopInfo(userId, gcmId, shopInfoParam, getShopInfo);
    }

    public void getNotif(String gcmId, String userId, NotifNetworkController.GetNotif getNotif){
        notifNetworkController.getNotif(userId, gcmId, getNotif);
    }

    public void getResCenter(String userId, String deviceId,
                             InboxResCenterNetworkController.InboxResCenterListener inboxResCenterListener){
        inboxResCenterNetworkController.getResCenterList(userId, deviceId, inboxResCenterListener);
    }

    public void getDeposit(String userId, String deviceId, final DepositNetworkController.DepositListener depositListener){
        depositNetworkController.getDeposit(userId, deviceId, depositListener);
    }

    public void getNewOrder(String userId, String deviceId, ShopTransactionController.GetNewOrder getNewOrder){
        ShopTransactionController.GetNewOrderModel orderModel = new ShopTransactionController.GetNewOrderModel();
        orderModel.page = 1;
        getNewOrder(userId, deviceId, orderModel, getNewOrder);
    }

    private void getNewOrder(String userId, String deviceId,
                             ShopTransactionController.GetNewOrderModel getNewOrderModel,
                             ShopTransactionController.GetNewOrder getNewOrder){
        shopTransactionController.getNewOrder(
                userId,
                deviceId,
                getNewOrderModel,
                getNewOrder
        );
    }

    public void getTicker(MojitoController.ListenerGetTicker listenerGetTicker) {
        mojitoController.getTicker(listenerGetTicker);
    }

    public void getData(String userId, String deviceId,
                        ShopNetworkController.ShopInfoParam shopInfoParam,
                        final ShopNetworkController.GetShopInfo shopInfo,
                        final NotifNetworkController.GetNotif notif,
                        final InboxResCenterNetworkController.InboxResCenterListener resCenter,
                        final DepositNetworkController.DepositListener deposit,
                        final MojitoController.ListenerGetTicker listenerGetTicker) {

        Observable<Response<TkpdResponse>> shopInfoObservable = shopNetworkController.
                getShopInfo(userId, deviceId, shopInfoParam);

        Observable<Response<TkpdResponse>> notifObservable = notifNetworkController.
                getNotif(userId, deviceId);

        Observable<Response<TkpdResponse>> resCenterListObservable = inboxResCenterNetworkController.
                getResCenterList(userId, deviceId);

        Observable<Response<TkpdResponse>> depositObservable = depositNetworkController.
                getDeposit(userId, deviceId);

        Observable<Response<Ticker>> ticker = mojitoController.getTicker();

        compositeSubscription.add(
                Observable.concat(shopInfoObservable, notifObservable, resCenterListObservable, depositObservable, ticker)
                        .toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                new Subscriber<List<Response<? extends Object>>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e("STUART", "getData : onError : "+e.toString());
                                        shopInfo.onError(e);
                                    }

                                    @Override
                                    public void onNext(List<Response<? extends Object>> responses) {
                                        // get shop info
                                        Response<TkpdResponse> response = convertToTkpdResponse(responses.get(0));
                                        parseResponse(0, shopInfo, response);

                                        // get notification
                                        response = convertToTkpdResponse(responses.get(1));
                                        parseResponse(1, notif, response);

                                        // get complain by resolution center
                                        response = convertToTkpdResponse(responses.get(2));
                                        parseResponse(2, resCenter, response);

                                        // get deposit
                                        response = convertToTkpdResponse(responses.get(3));
                                        parseResponse(3, deposit, response);

                                        // get ticker
                                        Response<Ticker> tickerResponse = convertToTicker(responses.get(4));
                                        parseResponseTicker(listenerGetTicker, tickerResponse);

                                    }
                                }
                        )
        );
    }

    private Response<TkpdResponse> convertToTkpdResponse(Response<? extends Object> tkpdResponseResponse) {
        Object body = tkpdResponseResponse.body();
        if (body != null && body instanceof TkpdResponse) {
            return (Response<TkpdResponse>) tkpdResponseResponse;
        }
        return null;
    }

    private Response<Ticker> convertToTicker(Response<? extends Object> response) {
        Object body = response.body();
        if (body != null && body instanceof Ticker) {
            return (Response<Ticker>) response;
        }
        return null;
    }

    private void parseResponseTicker(@NonNull CommonListener commonListener,
                                     @NonNull Response<Ticker> response) {
        MojitoController.ListenerGetTicker tickerListener =
                (MojitoController.ListenerGetTicker) commonListener;
        if (response.isSuccessful()) {
            Ticker ticker = response.body();
            Ticker.Data data = null;
            if (ticker != null) {
                data = ticker.getData();
            }
            if (data != null) {
                tickerListener.onSuccess(data.getTickers());
            } else {
                tickerListener.onError(new Throwable());
            }
        } else {
            onResponseError(response.code(), tickerListener);
        }
    }

    /**
     * parse responsed in here.
     * @param index
     * @param commonListener
     * @param response
     */
    private void parseResponse(@IntRange(from=0,to=3)int index,
                              @NonNull CommonListener commonListener,
                              @NonNull Response<TkpdResponse> response){
        switch (index){
            case 0:
                ShopNetworkController.GetShopInfo getShopInfo =
                        (ShopNetworkController.GetShopInfo) commonListener;
                if (response.isSuccessful()) {
                    if(response.body().isError()){
                        if(response.body().getStatus()!= null && response.body().getStatus().equalsIgnoreCase("TOO_MANY_REQUEST")){
                            throw new ManyRequestErrorException(response.body().getErrorMessages().get(0));
                        }else {
                            throw new MessageErrorException(response.body().getErrorMessages().get(0));
                        }
                    }else{
                        String stringData = response.body().getStringData();
                        Log.d("STUART", "getShopInfo : onNext : "+stringData);
                        ShopModel shopModel = gson.fromJson(stringData, ShopModel.class);
                        getShopInfo.onSuccess(shopModel);
                    }
                } else {
                    onResponseError(response.code(), getShopInfo);
                }
                break;
            case 1:
                NotifNetworkController.GetNotif getNotif
                        = (NotifNetworkController.GetNotif) commonListener;
                if (response.isSuccessful()) {
                    if(response.body().isError()){
                        throw new MessageErrorException(response.body().getErrorMessages().get(0));
                    }else{
                        String stringData = response.body().getStringData();
                        Log.d("STUART", "getNotif : onNext : "+stringData);
                        Notification.Data notification = gson.fromJson(stringData, Notification.Data.class);
                        getNotif.onSuccess(notification);
                    }
                } else {
                    onResponseError(response.code(), getNotif);
                }
                break;
            case 2:
                InboxResCenterNetworkController.InboxResCenterListener inboxResCenterListener =
                        (InboxResCenterNetworkController.InboxResCenterListener) commonListener;
                if (response.isSuccessful()) {
                    if(response.body().isError()){
                        throw new MessageErrorException(response.body().getErrorMessages().get(0));
                    }else{
                        String stringData = response.body().getStringData();
                        Log.d("STUART", "getResCenterList : onNext : "+stringData);
                        ResCenterInboxData shopModel = gson.fromJson(stringData, ResCenterInboxData.class);
                        inboxResCenterListener.onSuccess(shopModel);
                    }
                } else {
                    onResponseError(response.code(), inboxResCenterListener);
                }
                break;
            case 3:
                DepositNetworkController.DepositListener depositListener =
                        (DepositNetworkController.DepositListener) commonListener;
                if (response.isSuccessful()) {
                    if(response.body().isError()){
                        throw new MessageErrorException(response.body().getErrorMessages().get(0));
                    }else {
                        String stringData = response.body().getStringData();
                        Log.d("STUART", "getDeposit : onNext : "+stringData);
                        DepositModel.Data depositModel = gson.fromJson(stringData, DepositModel.Data.class);
                        depositListener.onSuccess(depositModel);
                    }
                } else {
                    onResponseError(response.code(), depositListener);
                }
                break;
        }
    }

}
