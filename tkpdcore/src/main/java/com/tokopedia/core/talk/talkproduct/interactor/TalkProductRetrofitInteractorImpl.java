package com.tokopedia.core.talk.talkproduct.interactor;

import android.content.Context;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.network.apiservices.kunyit.KunyitService;
import com.tokopedia.core.network.apiservices.product.ProductService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.talk.talkproduct.model.TalkProductModel;
import com.tokopedia.core.talk.talkproduct.presenter.TalkProductPresenter;
import com.tokopedia.core.talk.talkproduct.presenter.TalkProductPresenterImpl;
import com.tokopedia.core.util.NewPagingHandler;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by stevenfredian on 4/5/16.
 */
public class TalkProductRetrofitInteractorImpl implements TalkProductRetrofitInteractor {

    TalkProductPresenter presenter;
    CompositeSubscription compositeSubscription;
    ProductService productService;
    KunyitService kunyitService;

    public static TalkProductRetrofitInteractor createInstance(TalkProductPresenterImpl presenter) {
        TalkProductRetrofitInteractorImpl facade = new TalkProductRetrofitInteractorImpl();
        facade.presenter = presenter;
        facade.productService = new ProductService();
        facade.kunyitService = new KunyitService();
        facade.compositeSubscription = new CompositeSubscription();
        return facade;
    }

    @Override
    public void getTalkProduct(final Context context, Map<String, String> param, final GetTalkProductListener listener) {
//        Observable<Response<TkpdResponse>> observable = productService.getApi()
//                .getTalk(AuthUtil.generateParams(context,param));
        Observable<Response<TkpdResponse>> observable = productService.getApi()
                .getTalk(AuthUtil.generateParams(context, param));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("Get Product Talk ", e.toString());
                listener.onThrowable(e);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    final TkpdResponse tkpdResponse = response.body();
                    if (!tkpdResponse.isError()) {
                        JSONObject result = tkpdResponse.getJsonData();
                        TalkProductModel model = parse(result);
                        NewPagingHandler.PagingBean pagingBean = parsePaging(result);
                        listener.onSuccess(model, pagingBean);
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
    public TalkProductModel parse(JSONObject result) {
        return new GsonBuilder().create().fromJson(result.toString(), TalkProductModel.class);
    }

    @Override
    public NewPagingHandler.PagingBean parsePaging(JSONObject result) {
        if (result == null)
            return null;
        return new GsonBuilder().create().fromJson(result.optString("paging"), NewPagingHandler.PagingBean.class);
    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }

}
