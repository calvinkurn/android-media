package com.tokopedia.sellerapp.home.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.tkpd.library.utils.network.BaseNetworkController;
import com.tkpd.library.utils.network.CommonListener;
import com.tokopedia.core.network.apiservices.shop.MyShopOrderService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.seller.util.ShopNetworkController;
import com.tokopedia.sellerapp.home.model.orderShipping.OrderShippingData;

import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by normansyahputa on 9/1/16.
 */

public class ShopTransactionController extends BaseNetworkController {

    private MyShopOrderService myShopOrderService;

    public ShopTransactionController(MyShopOrderService myShopOrderService, Context context, Gson gson) {
        super(context, gson);
        this.myShopOrderService = myShopOrderService;
    }

    public static HashMap<String, String> getNewOrderParam(GetNewOrderModel getNewOrderModel) {
        return getNewOrderParam(getNewOrderModel.page, getNewOrderModel.deadline, getNewOrderModel.filter);
    }

    public static HashMap<String, String> getNewOrderParam(int page, int deadline, String filter) {
        HashMap<String, String> params = new NonNullStringMap();
        if (deadline > 0)
            params.put("deadline", Integer.toString(deadline));
        params.put("status", filter);
        params.put("page", Integer.toString(page));
        params.put("per_page", "10");
        return params;
    }

    public void getNewOrder(String userId, String deviceId, GetNewOrderModel getNewOrderModel, final GetNewOrder getNewOrder){
        getNewOrder(userId, deviceId, getNewOrderModel)
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<TkpdResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getNewOrder.onError(e);
                    }

                    @Override
                    public void onNext(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            if(!response.body().isError()) {
                                String stringData = response.body().getStringData();
                                Log.d("STUART", "getNewOrder : onNext : "+stringData);
                                OrderShippingData shopModel = gson.fromJson(stringData, OrderShippingData.class);
                                getNewOrder.onSuccess(shopModel);
                            }else {
                                throw new ShopNetworkController.MessageErrorException(response.body().getErrorMessages().get(0));
                            }
                        } else {
                            onResponseError(response.code(), getNewOrder);
                        }
                    }
                });
    }

    public Observable<Response<TkpdResponse>> getNewOrder(String userId, String deviceId, GetNewOrderModel getNewOrderModel){
        return myShopOrderService.getApi().getOrderNew(AuthUtil.generateParams(userId, deviceId, getNewOrderParam(getNewOrderModel)));
    }

    public interface GetNewOrder extends CommonListener {
        void onSuccess(OrderShippingData orderShippingData);
    }

    public static class GetNewOrderModel{
        public int page;
        public int deadline;
        public String filter;
    }
}
