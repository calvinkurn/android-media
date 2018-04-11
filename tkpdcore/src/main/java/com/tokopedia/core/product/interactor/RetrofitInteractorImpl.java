package com.tokopedia.core.product.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.ace.AceSearchService;
import com.tokopedia.core.network.apiservices.galadriel.GaladrielApi;
import com.tokopedia.core.network.apiservices.galadriel.Galadrielservice;
import com.tokopedia.core.network.apiservices.goldmerchant.GoldMerchantService;
import com.tokopedia.core.network.apiservices.kunyit.KunyitService;
import com.tokopedia.core.network.apiservices.mojito.MojitoAuthService;
import com.tokopedia.core.network.apiservices.mojito.MojitoService;
import com.tokopedia.core.network.apiservices.product.ProductActService;
import com.tokopedia.core.network.apiservices.product.ProductService;
import com.tokopedia.core.network.apiservices.product.ReputationReviewService;
import com.tokopedia.core.network.apiservices.product.PromoTopAdsService;
import com.tokopedia.core.network.apiservices.product.ReputationReviewService;
import com.tokopedia.core.network.apiservices.product.apis.ReputationReviewApi;
import com.tokopedia.core.network.apiservices.shop.MyShopEtalaseService;
import com.tokopedia.core.network.apiservices.tome.TomeService;
import com.tokopedia.core.network.apiservices.user.FaveShopActService;
import com.tokopedia.core.network.entity.intermediary.Product;
import com.tokopedia.core.network.entity.variant.Child;
import com.tokopedia.core.network.entity.variant.ProductStockResponse;
import com.tokopedia.core.network.entity.variant.ProductVariant;
import com.tokopedia.core.network.entity.variant.ProductVariantResponse;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.MapNulRemover;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.product.facade.NetworkParam;
import com.tokopedia.core.product.listener.ReportProductDialogView;
import com.tokopedia.core.product.model.etalase.EtalaseData;
import com.tokopedia.core.product.model.goldmerchant.ProductVideoData;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.mosthelpful.MostHelpfulReviewResponse;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Review;
import com.tokopedia.core.product.model.productdetail.discussion.LatestTalkViewModel;
import com.tokopedia.core.product.model.productdetail.promowidget.DataPromoWidget;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoWidgetResponse;
import com.tokopedia.core.product.model.productdink.ProductDinkData;
import com.tokopedia.core.product.model.productother.ProductOther;
import com.tokopedia.core.product.model.productother.ProductOtherData;
import com.tokopedia.core.product.model.productotherace.ProductOtherAce;
import com.tokopedia.core.product.model.productotherace.ProductOtherDataAce;
import com.tokopedia.core.session.model.network.ReportType;
import com.tokopedia.core.session.model.network.ReportTypeModel;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.core.network.retrofit.response.TkpdResponse.TOO_MANY_REQUEST;

/**
 * RetrofitInteractorImpl
 * Created by Angga.Prasetiyo on 02/12/2015.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class RetrofitInteractorImpl implements RetrofitInteractor {
    private static final String TAG = RetrofitInteractorImpl.class.getSimpleName();

    private final CompositeSubscription compositeSubscription;
    private final ProductService productService;
    private final ProductActService productActService;
    private final MyShopEtalaseService myShopEtalaseService;
    private final FaveShopActService faveShopActService;
    private final AceSearchService aceSearchService;
    private final MojitoAuthService mojitoAuthService;
    private final GoldMerchantService goldMerchantService;
    private final MojitoService mojitoService;
    private final Galadrielservice galadrielservice;
    private final KunyitService kunyitService;
    private final PromoTopAdsService promoTopAdsService;
    private final ReputationReviewService reputationReviewService;
    private final TomeService tomeService;
    private final int SERVER_ERROR_CODE = 500;
    private static final String ERROR_MESSAGE = "message_error";
    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;

    public RetrofitInteractorImpl() {
        this.productService = new ProductService();
        this.myShopEtalaseService = new MyShopEtalaseService();
        this.faveShopActService = new FaveShopActService();
        this.productActService = new ProductActService();
        this.compositeSubscription = new CompositeSubscription();
        this.aceSearchService = new AceSearchService();
        this.mojitoAuthService = new MojitoAuthService();
        this.goldMerchantService = new GoldMerchantService();
        this.mojitoService = new MojitoService();
        this.reputationReviewService = new ReputationReviewService();
        this.kunyitService = new KunyitService();
        this.threadExecutor = new JobExecutor();
        this.postExecutionThread = new UIThread();
        this.promoTopAdsService = new PromoTopAdsService();
        this.galadrielservice = new Galadrielservice();
        this.tomeService = new TomeService();
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
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(final Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess(response.body().convertDataObj(ProductDetailData.class));
                    } else {
                        if (response.body().getStatus().equals("REQUEST_DENIED"))
                            listener.onError("");
                        else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                            listener.onError(response.body().getErrorMessageJoined());
                        } else if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onTimeout() {
                            if (response.code() != SERVER_ERROR_CODE)
                                listener.onTimeout();
                            else listener.onReportServerProblem();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                if (e instanceof UnknownHostException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }

            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    final TkpdResponse tkpdResponse = response.body();
                    if (!tkpdResponse.isError()) {
                        listener.onSuccess(tkpdResponse.convertDataObj(ProductDinkData.class));
                    } else {
                        listener.onError((response.body().getErrorMessages() != null
                                && !response.body().getErrorMessages().isEmpty())
                                ? response.body().getErrorMessages().get(0)
                                : ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
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
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                    } else {
                        listener.onError((response.body().getErrorMessages() != null
                                && !response.body().getErrorMessages().isEmpty())
                                ? response.body().getErrorMessages().get(0)
                                : ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
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
                                listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                    } else {
                        listener.onError((response.body().getErrorMessages() != null
                                && !response.body().getErrorMessages().isEmpty())
                                ? response.body().getErrorMessages().get(0)
                                : ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
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
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                    } else {
                        listener.onError((response.body().getErrorMessages() != null
                                && !response.body().getErrorMessages().isEmpty())
                                ? response.body().getErrorMessages().get(0)
                                : ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
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
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                    } else {
                        listener.onError((response.body().getErrorMessages() != null
                                && !response.body().getErrorMessages().isEmpty())
                                ? response.body().getErrorMessages().get(0)
                                : ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                .addWishlist(String.valueOf(params), SessionHandler.getLoginID(context));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.code() == ResponseStatus.SC_CREATED) {
                    listener.onSuccess();
                } else if (response.code() == ResponseStatus.SC_BAD_REQUEST) {
                    try {
                        String msgError = "";
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONArray jsonArray = jsonObject.getJSONArray(ERROR_MESSAGE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            msgError += jsonArray.get(i).toString() + " ";
                        }
                        listener.onError(msgError);
                    } catch (Exception e) {
                        listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                .removeWishlist(String.valueOf(params), SessionHandler.getLoginID(context));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess();
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
                e.printStackTrace();
                listener.onError();
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                try {
                    if (response.isSuccessful() && !response.body().isError() && response.body().getJsonData()
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
    public void requestProductVideo(@NonNull Context context,
                                    @NonNull String productId,
                                    @NonNull final VideoLoadedListener listener) {

        Observable<Response<ProductVideoData>> observable = goldMerchantService.getApi()
                .fetchVideo(productId);

        Subscriber<Response<ProductVideoData>> subscriber =
                new Subscriber<Response<ProductVideoData>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<ProductVideoData> productVideoDataResponse) {
                        if (productVideoDataResponse.body() != null)
                            listener.onSuccess(productVideoDataResponse.body().getData().get(0));
                    }
                };
        compositeSubscription.add(observable
                .subscribeOn(Schedulers.newThread())
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

        Observable<Response<ProductOtherDataAce>> observable = aceSearchService.getApi()
                .getOtherProducts(MapNulRemover.removeNull(params));

        Subscriber<List<ProductOther>> subscriber = new Subscriber<List<ProductOther>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION);
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else {
                    listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(List<ProductOther> productOthers) {
                listener.onSuccess(productOthers);
            }
        };

        Func1<Response<ProductOtherDataAce>, List<ProductOther>>
                func1 = new Func1<Response<ProductOtherDataAce>, List<ProductOther>>() {
            @Override
            public List<ProductOther> call(Response<ProductOtherDataAce> response) {
                List<ProductOther> others = new ArrayList<>();
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
                            listener.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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
        compositeSubscription.add(
                productService.getApi()
                        .getProductReportType(AuthUtil.generateParams(context,
                                NetworkParam.paramDownloadReportType(productId)))
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
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
                                    errorString = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
                                } else if (e instanceof UnknownHostException) {
                                    errorString = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
                                } else {
                                    errorString = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;

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
                                    viewListener.showError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                                }
                            }
                        })
        );
    }

    @Override
    public void getProductVariant(@NonNull Context context, @NonNull String productId, final @NonNull ProductVariantListener listener) {
        Observable<Response<ProductVariantResponse>> observable = tomeService.getApi().getProductVariant(productId);
        Subscriber<ProductVariant> subscriber = new Subscriber<ProductVariant>() {
            @Override
            public void onCompleted() {
               
            }

            @Override
            public void onError(Throwable e) {
                listener.onError("");
            }

            @Override
            public void onNext(ProductVariant variant) {
                if (variant!=null) {
                    listener.onSucccess(variant);
                } else {
                    listener.onError("");
                }
            }
        };

        Func1<Response<ProductVariantResponse>, ProductVariant> mapper =
                new Func1<Response<ProductVariantResponse>, ProductVariant>() {
                    @Override
                    public ProductVariant call(Response<ProductVariantResponse> productVariantResponse) {
                        if (productVariantResponse != null && productVariantResponse.body()!=null) {
                            return productVariantResponse.body().getData();
                        }
                        return null;
                    }
                };

        compositeSubscription.add(
                observable.subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(mapper)
                        .subscribe(subscriber)
        );
    }

    @Override
    public void getProductStock(@NonNull Context context, @NonNull String productId, final @NonNull ProductStockListener listener) {
        Observable<Response<ProductStockResponse>> observable = tomeService.getApi().getProductStock(productId);
        Subscriber<Child> subscriber = new Subscriber<Child>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onError("");
            }

            @Override
            public void onNext(Child productStock) {
                if (productStock!=null) {
                    listener.onSucccess(productStock);
                } else {
                    listener.onError("");
                }
            }
        };

        Func1<Response<ProductStockResponse>, Child> mapper =
                new Func1<Response<ProductStockResponse>, Child>() {
                    @Override
                    public Child call(Response<ProductStockResponse> productStockResponse) {
                        if (productStockResponse != null && productStockResponse.body()!=null) {
                            return productStockResponse.body().getData();
                        }
                        return null;
                    }
                };

        compositeSubscription.add(
                observable.subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(mapper)
                        .subscribe(subscriber)
        );
    }

    @Override
    public void updateRecentView(@NonNull Context context, @NonNull String productId) {
        Observable<Response<TkpdResponse>> observable =
                mojitoAuthService.getApi().updateRecentView(productId,SessionHandler.isV4Login(context) ? SessionHandler.getLoginID(context) : "");

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
               
            }

            @Override
            public void onError(Throwable e) {
                
            }

            @Override
            public void onNext(Response<TkpdResponse> variant) {
              
            }
        };

        compositeSubscription.add(
                observable.subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void getPromo(@NonNull Context context,
                         @NonNull String targetType, @NonNull String userId,  @NonNull String shopType,
                         final @NonNull PromoListener listener) {
        Observable<Response<PromoWidgetResponse>> observable = galadrielservice
                .getApi().getPromoWidget(GaladrielApi.VALUE_PDP_WIDGET, targetType, GaladrielApi.VALUE_DEVICE, GaladrielApi.VALUE_LANG, userId, shopType);

        Subscriber<DataPromoWidget> subscriber = new Subscriber<DataPromoWidget>() {
            @Override
            public void onCompleted() {
               
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(DataPromoWidget promoWidget) {
                listener.onSucccess(promoWidget);
            }
        };

        Func1<Response<PromoWidgetResponse>, DataPromoWidget> mapper =
                new Func1<Response<PromoWidgetResponse>, DataPromoWidget>() {
                    @Override
                    public DataPromoWidget call(Response<PromoWidgetResponse> promoWidgetRespone) {
                        if (promoWidgetRespone.body().getData() != null) {
                            return promoWidgetRespone.body().getData();
                        }
                        return null;
                    }
                };

        compositeSubscription.add(
                observable.subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(mapper)
                        .subscribe(subscriber)
        );

    }

    @Override
    public void getMostHelpfulReview(@NonNull Context context,
                                     @NonNull String productId,
                                     @NonNull String shopId,
                                     final @NonNull MostHelpfulListener listener) {
        try {
            Observable<Response<MostHelpfulReviewResponse>> observable = reputationReviewService
                    .getApi().getMostHelpfulReview(
                            AuthUtil.generateParamsNetwork2(
                                    MainApplication.getAppContext(),
                                    getMostHelpfulParam(productId, shopId)));

            Subscriber<List<Review>> subscriber = new Subscriber<List<Review>>() {
                @Override
                public void onCompleted() {
                   
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(List<Review> reviews) {
                    listener.onSucccess(reviews);
                }
            };

            Func1<Response<MostHelpfulReviewResponse>, List<Review>> mapper =
                    new Func1<Response<MostHelpfulReviewResponse>, List<Review>>() {
                        @Override
                        public List<Review> call(Response<MostHelpfulReviewResponse> mostHelpfulReview) {
                            return mostHelpfulReview.body().getData().getReviews();
                        }
                    };

            compositeSubscription.add(
                    observable.subscribeOn(Schedulers.newThread())
                            .unsubscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(mapper)
                            .subscribe(subscriber)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TKPDMapParam getMostHelpfulParam(String productId, String shopId) {
        RequestParams params = RequestParams.create();
        params.putString(ReputationReviewApi.ID, productId);
        params.putString(ReputationReviewApi.SHOP_ID, shopId);
        params.putString(ReputationReviewApi.PER_PAGE, String.valueOf(1));
        params.putString(ReputationReviewApi.PARAM_SOURCE, ReputationReviewApi.VALUE_SNEAK_PEAK);
        return params.getParameters();
    }

    @Override
    public void getProductDiscussion(@Nonnull final Context context,
                                     @Nonnull final String productId,
                                     @Nonnull final String shopId,
                                     @Nonnull final DiscussionListener listener) {

        Observable<Response<TkpdResponse>> observableGetProductTalk = kunyitService
                .getApi().getProductTalk(
                        AuthUtil.generateParams(context, NetworkParam.paramProductSneakPeakTalk(productId, shopId))
                );

        Subscriber<LatestTalkViewModel> subscriber = new Subscriber<LatestTalkViewModel>() {
            @Override
            public void onCompleted() {
                
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(LatestTalkViewModel modelDomain) {
                listener.onSucccess(modelDomain);
            }
        };

        DiscussionMapper discussionMapper = new DiscussionMapper();

        compositeSubscription.add(
                observableGetProductTalk.subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(discussionMapper)
                        .subscribe(subscriber)
        );
    }

    @Override
    public void getProductTalkComment(@Nonnull Context context,
                                      @Nonnull String talkId,
                                      @Nonnull String shopId,
                                      @Nonnull final DiscussionListener listener) {
        Observable<Response<TkpdResponse>> observableGetTalkComment =
                kunyitService.getApi().getCommentTalk(
                        AuthUtil.generateParams(context, NetworkParam.paramSneakPeakTalkComment(talkId, shopId))
                );

        Subscriber<LatestTalkViewModel> subscriber = new Subscriber<LatestTalkViewModel>() {
            @Override
            public void onCompleted() {
                
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(LatestTalkViewModel modelDomain) {
                listener.onSucccess(modelDomain);
            }
        };

        DiscussionCommentMapper getCommentMapper = new DiscussionCommentMapper();

        compositeSubscription.add(
                observableGetTalkComment.subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(getCommentMapper)
                        .subscribe(subscriber)
        );
    }

    @Override
    public void checkPromoAds(@NonNull String shopId, @NonNull int itemId,
                              @NonNull String userId, final CheckPromoAdsListener listener) {
        Observable<Response<String>> observable = promoTopAdsService.getApi()
                .checkPromoAds(NetworkParam.paramCheckAds(shopId, String.valueOf(itemId), userId, "1"));
        Subscriber<Response<String>> subscriber = new Subscriber<Response<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getLocalizedMessage());
            }

            @Override
            public void onNext(Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(response.body());
                        String adsId = object.getJSONObject("data").getString("ad_id");
                        listener.onSuccess(adsId);
                    } catch (JSONException e) {
                        listener.onError(e.getLocalizedMessage());
                    }
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }
}
