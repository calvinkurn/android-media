package com.tokopedia.home.recharge.interactor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.database.model.category.CategoryData;
import com.tokopedia.core.database.recharge.operator.OperatorData;
import com.tokopedia.core.database.recharge.product.ProductData;
import com.tokopedia.core.database.recharge.recentNumber.RecentData;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.database.recharge.status.Status;
import com.tokopedia.core.network.apiservices.recharge.RechargeService;
import com.tokopedia.core.network.retrofit.utils.MapNulRemover;

import java.util.Map;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author  ricoharisin on 7/11/16.
 * Modified by kulomady on 08/23/2016
 */
public class RechargeNetworkInteractorImpl implements RechargeNetworkInteractor {
    private static final String TAG = RechargeNetworkInteractorImpl.class.getSimpleName();

    private CompositeSubscription compositeSubscription;
    private RechargeService rechargeService;

    public RechargeNetworkInteractorImpl() {
        compositeSubscription = new CompositeSubscription();
        rechargeService = new RechargeService();
    }

    @Override
    public void getStatus(@NonNull final OnGetStatusListener listener) {
        compositeSubscription.add(rechargeService.getApi().getStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Response<Status>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        listener.onNetworkError();
                    }

                    @Override
                    public void onNext(Response<Status> statusResponse) {
                        if (statusResponse.isSuccessful()) {
                            listener.onSuccess(statusResponse.body());
                        } else {
                            listener.onNetworkError();
                        }
                    }
                }));

    }

    @Override
    public void getAllCategory(@NonNull final OnGetCategoryListener listener) {
        compositeSubscription.add(rechargeService.getApi().getCategory()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Response<CategoryData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(TAG, "onError: ", e);
                        listener.onNetworkError();
                    }

                        @Override
                        public void onNext(Response<CategoryData> categoryResponse) {
                            if (categoryResponse.isSuccessful()) {
                                listener.onSuccess(categoryResponse.body());
                            } else {
                                listener.onNetworkError();
                            }
                        }
                    }));
    }

    @Override
    public void getAllOperator(@NonNull final OnGetOperatorListener listener) {
        compositeSubscription.add(rechargeService.getApi().getOperator()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Response<OperatorData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        listener.onNetworkError();
                    }

                        @Override
                        public void onNext(Response<OperatorData> operatorResponse) {
                            if (operatorResponse.isSuccessful()) {
                                listener.onSuccess(operatorResponse.body());
                            } else {
                                listener.onNetworkError();
                            }
                        }
                    }));
    }

    @Override
    public void getAllProduct(@NonNull final OnGetProductListener listener) {
        compositeSubscription.add(rechargeService.getApi().getProduct()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Response<ProductData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        listener.onNetworkError();
                    }

                        @Override
                        public void onNext(Response<ProductData> productResponse) {
                            if (productResponse.isSuccessful()) {
                                listener.onSuccess(productResponse.body());
                            } else {
                                listener.onNetworkError();
                            }
                        }
                    }));
    }

    @Override
    public void getRecentNumbers(Map<String, String> params, final OnGetRecentNumbersListener listener) {
        compositeSubscription.add(rechargeService.getApi().getRecentNumbers(MapNulRemover.removeNull(params))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Response<RecentData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        listener.onNetworkError();
                    }

                    @Override
                    public void onNext(Response<RecentData> recentNumberResponse) {
                        if (recentNumberResponse.isSuccessful()) {
                            listener.onGetRecentNumbersSuccess(recentNumberResponse.body());
                        } else {
                            listener.onNetworkError();
                        }
                    }
                }));
    }

    @Override
    public void getLastOrder(Map<String, String> params, final OnGetRecentOrderListener listener) {
        compositeSubscription.add(rechargeService.getApi().getLastOrder(MapNulRemover.removeNull(params))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Response<LastOrder>>() {
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
                    public void onNext(Response<LastOrder> recentNumberResponse) {
                        if (recentNumberResponse.isSuccessful()) {
                            listener.onGetLastOrderSuccess(recentNumberResponse.body());
                        } else {
                            listener.onNetworkError();
                        }
                    }
                }));
    }

    @Override
    public void getOperator(int categoryId) {

    }

    @Override
    public void getProduct(int productId) {

    }


    public void unsubscribe() {
        compositeSubscription.unsubscribe();
    }
}
