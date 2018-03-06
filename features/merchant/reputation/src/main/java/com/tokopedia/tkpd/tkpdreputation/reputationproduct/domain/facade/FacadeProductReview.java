package com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.facade;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.database.manager.ProductReviewCacheManager;
import com.tokopedia.core.network.apiservices.product.ProductService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.MapNulRemover;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.helpful_review.HelpfulReviewList;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review.AdvanceReview;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review.ProductReview;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review.ReviewProductModel;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.presenter.ProductReviewPresenter;
import com.tokopedia.core.util.PagingHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Steven on 12/01/2016.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class FacadeProductReview implements FacadeProductReviewImpl {

    private final String NAV;
    private Context context;
    private final ProductReviewPresenter presenter;
    private CompositeSubscription compositeSubscription;
    private ProductService productService;

    public FacadeProductReview(String NAV, Context context, ProductReviewPresenter presenter) {
        this.NAV = NAV;
        this.context = context;
        this.presenter = presenter;
        this.productService = new ProductService();
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getMostHelpfulReview(final HashMap param, final GetHelpfulReviewListener listener) {
        Observable<Response<TkpdResponse>> observable = productService.getApi()
                .getHelpfulReview(MapNulRemover.removeNull(param));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("ASDASDM", e.toString());
                listener.onThrowable(e);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    final TkpdResponse tkpdResponse = response.body();
                    if (!tkpdResponse.isError()) {
                        JSONObject result = tkpdResponse.getJsonData();
                        listener.onSuccess(getListHelpfulReview(result));
                    } else {
                        listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError("Network Unknown Error!");
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError("Network Timeout Error!");
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError("Network Internal Server Error!");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError("Network Bad Request Error!");
                        }

                        @Override
                        public void onForbidden() {

                        }
                    }, response.code());
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }


    @Override
    public void getReputation(@NonNull Context context, final HashMap paramReview, final GetReviewListener listener) {

        Observable<Response<TkpdResponse>> observable = productService.getApi()
                .getReview(AuthUtil.generateParams(context, paramReview));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("ASDASD", e.toString());
                listener.onThrowable(e);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    final TkpdResponse tkpdResponse = response.body();
                    if (!tkpdResponse.isError()) {
                        JSONObject result = tkpdResponse.getJsonData();
                        listener.onSuccess(String.valueOf(paramReview.get("product_id")),
                                String.valueOf(paramReview.get("NAV")),
                                result);
                    } else {
                        listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError("Network Unknown Error!");
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError("Network Timeout Error!");
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError("Network Internal Server Error!");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError("Network Bad Request Error!");
                        }

                        @Override
                        public void onForbidden() {

                        }
                    }, response.code());
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));

    }

    @Override
    public void getReputationFromCache(final String productID, final String NAV, final GetReputationCacheListener listener) {
        Observable.just(productID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, JSONObject>() {
                    @Override
                    public JSONObject call(String s) {
                        ProductReviewCacheManager cache = new ProductReviewCacheManager();
                        try {
                            return cache.getCache(productID, NAV).getJson();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .subscribe(new Subscriber<JSONObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("steven", e.getMessage());
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        if (jsonObject != null)
                            Log.i("steven", "Get The Cache!! " + jsonObject.toString());
                        listener.onSuccess(jsonObject);
                    }
                });
    }

    @Override
    public void unsubscribeNetwork() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public AdvanceReview getModelRatingStats(JSONObject result) {
        if (result != null)
            return new GsonBuilder().create().fromJson(result.optString("advance_review"), AdvanceReview.class);
        else
            return null;
    }

    @Override
    public PagingHandler.PagingHandlerModel getPaging(JSONObject result) {
        if (result == null)
            return null;
        return new GsonBuilder().create().fromJson(result.optString("paging"), PagingHandler.PagingHandlerModel.class);
    }

    @Override
    public void storeFirstPage(String product_id, String NAV, JSONObject result) {
        ProductReviewCacheManager cache = new ProductReviewCacheManager();
        cache.setProductID(product_id, NAV);
        cache.setResult(result);
        cache.setCacheDuration(300);
        Observable.just(cache)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<ProductReviewCacheManager, ProductReviewCacheManager>() {
                    @Override
                    public ProductReviewCacheManager call(ProductReviewCacheManager productReviewCacheManager) {
                        productReviewCacheManager.store();
                        return productReviewCacheManager;
                    }
                })
                .subscribe(new Subscriber<ProductReviewCacheManager>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ProductReviewCacheManager productReviewCacheManager) {
                    }
                });
    }

    @Override
    public List<ReviewProductModel> getListReputation(JSONObject result) {
//
        if (result == null)
            return null;
        ProductReview productReview = new GsonBuilder().create().
                fromJson(result.toString(), ProductReview.class);
        return productReview.getList();
//        return new GsonBuilder().create().fromJson(result.toString(),
//                new TypeToken<List<ReviewProductModel>>(){}.getType());
    }

    public HelpfulReviewList getListHelpfulReview(JSONObject result) {
        return new GsonBuilder().create().fromJson(result.toString(),
                HelpfulReviewList.class);
    }
}
