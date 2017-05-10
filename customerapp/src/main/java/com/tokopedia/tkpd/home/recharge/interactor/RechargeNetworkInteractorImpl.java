package com.tokopedia.tkpd.home.recharge.interactor;

import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.core.database.recharge.recentNumber.RecentData;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.network.apiservices.digital.DigitalEndpointService;
import com.tokopedia.core.network.apiservices.recharge.RechargeService;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.MapNulRemover;

import java.util.Map;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author ricoharisin on 7/11/16.
 *         Modified by kulomady on 08/23/2016
 */
public class RechargeNetworkInteractorImpl implements RechargeNetworkInteractor {
    private static final String TAG = RechargeNetworkInteractorImpl.class.getSimpleName();

    private CompositeSubscription compositeSubscription;
    private RechargeService rechargeService;
    private DigitalEndpointService digitalEndpointService;

    public RechargeNetworkInteractorImpl() {
        compositeSubscription = new CompositeSubscription();
        rechargeService = new RechargeService();
        digitalEndpointService = new DigitalEndpointService();
    }

    @Override
    public void getRecentNumbers(Map<String, String> params, final OnGetRecentNumbersListener listener) {
//        compositeSubscription.add(rechargeService.getApi().getRecentNumbers(MapNulRemover.removeNull(params))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.newThread())
//                .subscribe(new Subscriber<Response<RecentData>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        listener.onNetworkError();
//                    }
//
//                    @Override
//                    public void onNext(Response<RecentData> recentNumberResponse) {
//                        if (recentNumberResponse.isSuccessful()) {
//                            listener.onGetRecentNumbersSuccess(recentNumberResponse.body());
//                        } else {
//                            listener.onNetworkError();
//                        }
//                    }
//                }));

        compositeSubscription.add(digitalEndpointService.getApi().getRecentNumber(MapNulRemover.removeNull(params))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Response<TkpdDigitalResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        listener.onNetworkError();
                    }

                    @Override
                    public void onNext(Response<TkpdDigitalResponse> recentNumberResponse) {
                        if (recentNumberResponse.isSuccessful()) {
                            RecentData recentData = new Gson().fromJson(
                                    recentNumberResponse.body().getStrResponse(), RecentData.class
                            );
                            listener.onGetRecentNumbersSuccess(recentData);
                        } else {
                            listener.onNetworkError();
                        }
                    }
                }));
    }

    @Override
    public void getLastOrder(Map<String, String> params, final OnGetRecentOrderListener listener) {
//        compositeSubscription.add(rechargeService.getApi().getLastOrder(MapNulRemover.removeNull(params))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.newThread())
//                .unsubscribeOn(Schedulers.newThread())
//                .subscribe(new Subscriber<Response<LastOrder>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e(TAG, "onError: " + e.getCause() + " message : " + e.getMessage());
//                        e.printStackTrace();
//                        listener.onNetworkError();
//                    }
//
//                    @Override
//                    public void onNext(Response<LastOrder> recentNumberResponse) {
//                        if (recentNumberResponse.isSuccessful()) {
//                            listener.onGetLastOrderSuccess(recentNumberResponse.body());
//                        } else {
//                            listener.onNetworkError();
//                        }
//                    }
//                }));

        compositeSubscription.add(digitalEndpointService.getApi()
                .getLastOrder(MapNulRemover.removeNull(params))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Response<TkpdDigitalResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getCause() + " message : " + e.getMessage());
                        e.printStackTrace();
                        listener.onNetworkError();
                    }

                    @Override
                    public void onNext(Response<TkpdDigitalResponse> recentNumberResponse) {
                        if (recentNumberResponse.isSuccessful()) {
                            LastOrder lastOrder = new Gson().fromJson(
                                    recentNumberResponse.body().getStrResponse(), LastOrder.class
                            );
                            listener.onGetLastOrderSuccess(lastOrder);
                        } else {
                            listener.onNetworkError();
                        }
                    }
                }));
    }

    public void unsubscribe() {
        compositeSubscription.unsubscribe();
    }
}
