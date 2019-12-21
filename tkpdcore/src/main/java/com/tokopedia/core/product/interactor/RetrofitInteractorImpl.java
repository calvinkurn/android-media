package com.tokopedia.core.product.interactor;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.JsonSyntaxException;
import com.tokopedia.core.base.data.executor.JobExecutor;
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
import com.tokopedia.core.network.apiservices.product.PromoTopAdsService;
import com.tokopedia.core.network.apiservices.product.ReputationReviewService;
import com.tokopedia.core.network.apiservices.shop.MyShopEtalaseService;
import com.tokopedia.core.network.apiservices.tome.TomeService;
import com.tokopedia.core.network.apiservices.user.FaveShopActService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.MapNulRemover;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.promowidget.DataPromoWidget;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoWidgetResponse;
import com.tokopedia.core.product.model.productother.ProductOther;
import com.tokopedia.core.product.model.productother.ProductOtherData;
import com.tokopedia.core.product.model.productotherace.ProductOtherAce;
import com.tokopedia.core.product.model.productotherace.ProductOtherDataAce;

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
}
