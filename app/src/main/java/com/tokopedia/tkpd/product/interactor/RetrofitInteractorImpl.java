package com.tokopedia.tkpd.product.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.network.apiservices.ace.AceSearchService;
import com.tokopedia.tkpd.network.apiservices.mojito.MojitoAuthService;
import com.tokopedia.tkpd.network.apiservices.product.ProductActService;
import com.tokopedia.tkpd.network.apiservices.product.ProductService;
import com.tokopedia.tkpd.network.apiservices.shop.MyShopEtalaseService;
import com.tokopedia.tkpd.network.apiservices.user.FaveShopActService;
import com.tokopedia.tkpd.network.apiservices.user.WishListActService;
import com.tokopedia.tkpd.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.network.retrofit.response.ErrorListener;
import com.tokopedia.tkpd.network.retrofit.response.ResponseStatus;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.network.retrofit.utils.MapNulRemover;
import com.tokopedia.tkpd.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.product.facade.NetworkParam;
import com.tokopedia.tkpd.product.listener.ReportProductDialogView;
import com.tokopedia.tkpd.product.model.etalase.EtalaseData;
import com.tokopedia.tkpd.product.model.productdetail.ProductDetailData;
import com.tokopedia.tkpd.product.model.productdink.ProductDinkData;
import com.tokopedia.tkpd.product.model.productother.ProductOther;
import com.tokopedia.tkpd.product.model.productother.ProductOtherData;
import com.tokopedia.tkpd.product.model.productotherace.ProductOtherAce;
import com.tokopedia.tkpd.product.model.productotherace.ProductOtherDataAce;
import com.tokopedia.tkpd.session.model.network.ReportType;
import com.tokopedia.tkpd.session.model.network.ReportTypeModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * RetrofitInteractorImpl
 * Created by Angga.Prasetiyo on 02/12/2015.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class RetrofitInteractorImpl implements RetrofitInteractor {
    private static final String TAG = RetrofitInteractorImpl.class.getSimpleName();
    private static final String DEFAULT_MSG_ERROR = "Terjadi Kesalahan, Mohon ulangi beberapa saat lagi";
    private static final String TIMEOUT_MSG_ERROR = "Timeout connection, Mohon ulangi beberapa saat lagi";

    private final CompositeSubscription compositeSubscription;
    private final ProductService productService;
    private final ProductActService productActService;
    private final MyShopEtalaseService myShopEtalaseService;
    private final FaveShopActService faveShopActService;
    private final WishListActService wishListService;
    private final AceSearchService aceSearchService;
    private final MojitoAuthService mojitoAuthService;

    public RetrofitInteractorImpl() {
        this.productService = new ProductService();
        this.myShopEtalaseService = new MyShopEtalaseService();
        this.faveShopActService = new FaveShopActService();
        this.wishListService = new WishListActService();
        this.productActService = new ProductActService();
        this.compositeSubscription = new CompositeSubscription();
        this.aceSearchService = new AceSearchService();
        this.mojitoAuthService = new MojitoAuthService();
    }

    @Override
    public void getProductDetail(@NonNull final Context context,
                                 @NonNull final TKPDMapParam<String, String> params,
                                 @NonNull final ProductDetailListener listener) {

        Observable<Response<TkpdResponse>> observable = productService.getApi()
                .getProductDetail(AuthUtil.generateParamsNetwork(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onError("Tidak ada Koneksi Internet");
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout(TIMEOUT_MSG_ERROR);
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess(response.body().convertDataObj(ProductDetailData.class));
                    } else {
                        if (response.body().getStatus().equals("REQUEST_DENIED"))
                            listener.onError("");
                        else if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(DEFAULT_MSG_ERROR);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout(TIMEOUT_MSG_ERROR);
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(TIMEOUT_MSG_ERROR);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(TIMEOUT_MSG_ERROR);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(TIMEOUT_MSG_ERROR);
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
    public void getOtherProducts(@NonNull final Context context,
                                 @NonNull final Map<String, String> params,
                                 @NonNull final OtherProductListener listener) {
        getOtherProductsAceApi(context, params, listener);
    }

    @Override
    public void promoteProduct(@NonNull Context context, @NonNull Map<String, String> params,
                               @NonNull final PromoteProductListener listener) {
        Observable<Response<TkpdResponse>> observable = productActService.getApi()
                .promote(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    final TkpdResponse tkpdResponse = response.body();
                    if (!tkpdResponse.isError()) {
                        listener.onSuccess(tkpdResponse.convertDataObj(ProductDinkData.class));
                    } else {
                        listener.onError(DEFAULT_MSG_ERROR);
                    }
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void moveToWarehouse(@NonNull Context context, @NonNull Map<String, String> params,
                                @NonNull final ToWarehouseListener listener) {
        Observable<Response<TkpdResponse>> observable = productActService.getApi()
                .moveToWarehouse(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    final TkpdResponse tkpdResponse = response.body();
                    if (!tkpdResponse.isError()) {
                        try {
                            int status = tkpdResponse.getJsonData().getInt("is_success");
                            listener.onSuccess(status == 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onError(DEFAULT_MSG_ERROR);
                        }
                    }
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getShopEtalase(@NonNull Context context, @NonNull Map<String, String> params,
                               @NonNull final GetEtalaseListener listener) {
        Observable<Response<TkpdResponse>> observable = myShopEtalaseService.getApi()
                .getShopEtalase(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    final TkpdResponse tkpdResponse = response.body();
                    if (!tkpdResponse.isError()) {
                        try {
                            try {
                                EtalaseData etalaseData = tkpdResponse
                                        .convertDataObj(EtalaseData.class);
                                listener.onSuccess(etalaseData.getEtalaseList());
                            } catch (Exception e) {
                                e.printStackTrace();
                                listener.onError(DEFAULT_MSG_ERROR);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onError(DEFAULT_MSG_ERROR);
                        }
                    }
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void moveToEtalase(@NonNull final Context context, @NonNull Map<String, String> params,
                              @NonNull final ToEtalaseListener listener) {
        Observable<Response<TkpdResponse>> observable = productActService.getApi()
                .editEtalase(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onError(context.getString(R.string.msg_connection_timeout));
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    final TkpdResponse tkpdResponse = response.body();
                    if (!tkpdResponse.isError()) {
                        try {
                            int status = tkpdResponse.getJsonData().getInt("is_success");
                            listener.onSuccess(status == 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onError(DEFAULT_MSG_ERROR);
                        }
                    } else {
                        listener.onError(tkpdResponse.getErrorMessages().get(0));
                    }
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }

            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void favoriteShop(@NonNull Context context, @NonNull Map<String, String> params,
                             @NonNull final FaveListener listener) {

        Observable<Response<TkpdResponse>> observable = faveShopActService.getApi()
                .faveShop(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
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
                        try {
                            int status = response.body().getJsonData().getInt("is_success");
                            switch (status) {
                                case 1:
                                    listener.onSuccess(true);
                                    break;
                                case 0:
                                    listener.onSuccess(false);
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onError(DEFAULT_MSG_ERROR);
                        }
                    }
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }

            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void addToWishList(@NonNull Context context, @NonNull Integer params,
                              @NonNull final AddWishListListener listener) {
        Observable<Response<TkpdResponse>> observable = mojitoAuthService.getApi()
                .addWishlist(String.valueOf(params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onError(DEFAULT_MSG_ERROR);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.code() == ResponseStatus.SC_CREATED) {
                    listener.onSuccess();
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void removeFromWishList(@NonNull Context context, @NonNull Integer params,
                                   @NonNull final RemoveWishListListener listener) {
        final Observable<Response<TkpdResponse>> observable = mojitoAuthService.getApi()
                .removeWishlist(String.valueOf(params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                listener.onError(DEFAULT_MSG_ERROR);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess();
                } else {
                    listener.onError(DEFAULT_MSG_ERROR);
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getProductManagePermissions(@NonNull Context context,
                                            @NonNull Map<String, String> params,
                                            @NonNull final ProductManagePermissionListener listener) {
        final Observable<Response<TkpdResponse>> observable = productService.getApi()
                .manage(AuthUtil.generateParams(context, params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                try {
                    if (response.isSuccessful() && response.body().getJsonData()
                            .get("is_product_manager").toString().equals("1")) {
                        listener.onSuccess(response.body().getJsonData().get("is_product_manager")
                                .toString());
                    } else {
                        listener.onError();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError();
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void unSubscribeObservable() {
        compositeSubscription.unsubscribe();
    }

    private void getOtherProductsAceApi(Context context, Map<String, String> params,
                                        final OtherProductListener listener) {

        Log.d(TAG, "getOtherProductsAceApi " + params.toString());
        Observable<Response<ProductOtherDataAce>> observable = aceSearchService.getApi()
                .getOtherProducts(MapNulRemover.removeNull(params));

        Subscriber<java.util.List<ProductOther>> subscriber = new Subscriber<List<ProductOther>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                listener.onError("");
            }

            @Override
            public void onNext(List<ProductOther> productOthers) {
                listener.onSuccess(productOthers);
            }
        };

        Func1<Response<ProductOtherDataAce>, java.util.List<ProductOther>>
                func1 = new Func1<Response<ProductOtherDataAce>, List<ProductOther>>() {
            @Override
            public List<ProductOther> call(Response<ProductOtherDataAce> response) {
                java.util.List<ProductOther> others = new ArrayList<>();
                for (ProductOtherAce data : response.body().getProductOthers()) {
                    others.add(new ProductOther(data));
                }
                return others;
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(func1)
                .subscribe(subscriber));
    }

    @SuppressWarnings("unused")
    @Deprecated
    private void getOtherProductsWS4(Context context, Map<String, String> params,
                                     final OtherProductListener listener) {
        Observable<Response<TkpdResponse>> observable = productService.getApi()
                .getOtherProducts(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onError("");
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        try {
                            listener.onSuccess(response.body()
                                    .convertDataObj(ProductOtherData.class)
                                    .getProductOthers());
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            listener.onError(DEFAULT_MSG_ERROR);
                        }
                    }

                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void downloadReportType(final Context context, Integer productId, final ReportProductDialogView viewListener) {
        ProductService productService = new ProductService();
        final CompositeSubscription compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(
                productService.getApi()
                        .getProductReportType(AuthUtil.generateParams(context,
                                NetworkParam.paramDownloadReportType(productId)))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {
                                compositeSubscription.unsubscribe();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("DownloadReportType", e.getLocalizedMessage());
                                String errorString;
                                if (e instanceof SocketTimeoutException) {
                                    errorString = context.getString(R.string.default_request_error_timeout);
                                } else if (e instanceof UnknownHostException) {
                                    errorString = context.getString(R.string.default_request_error_unknown);
                                } else {
                                    errorString = context.getString(R.string.default_request_error_internal_server);

                                }
                                viewListener.showError(errorString);
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                if (responseData.isSuccessful()) {
                                    TkpdResponse response = responseData.body();
                                    if (!response.isError()) {
                                        JSONObject result = response.getJsonData();
                                        ReportTypeModel reportTypeModel = new GsonBuilder().create()
                                                .fromJson(result.toString(), ReportTypeModel.class);
                                        List<ReportType> reportTypeList = reportTypeModel.getReportType();
                                        viewListener.setReportAdapterFromNetwork(reportTypeList);
                                        viewListener.saveToCache(result.toString());
                                    } else {
                                        viewListener.showError(response.getErrorMessages().get(0));
                                    }
                                } else {
                                    viewListener.showError(context.getString(R.string.default_request_error_unknown));
                                }
                            }
                        })
        );
    }
}
