package com.tokopedia.loyalty.view.presenter;

import android.content.res.Resources;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.domain.entity.response.promocodesave.PromoCacheResponse;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.interactor.IPromoInteractor;
import com.tokopedia.loyalty.view.view.IPromoDetailView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author Aghny A. Putra on 4/4/18
 */

public class PromoDetailPresenter extends IBasePresenter<IPromoDetailView>
        implements IPromoDetailPresenter {

    private static final String KEY_PARAM_SLUG = "slug";

    private final IPromoInteractor promoInteractor;

    @Inject
    public PromoDetailPresenter(IPromoInteractor promoInteractor) {
        this.promoInteractor = promoInteractor;
    }

    @Override
    public void getPromoDetail(String slug) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(KEY_PARAM_SLUG, slug);

        this.promoInteractor.getPromoList(param, getSubscriber());
    }

    private Subscriber<List<PromoData>> getSubscriber() {
        return new Subscriber<List<PromoData>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof JsonSyntaxException) {
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("type", "json");
                    messageMap.put("err", Log.getStackTraceString(e));
                    messageMap.put("req", PromoCodePresenter.class.getCanonicalName());
                    ServerLogger.log(Priority.P2, "LOYALTY_PARSE_ERROR", messageMap);
                }

                if (isViewAttached()) {
                    if (e instanceof UnknownHostException) {
                        // No internet connection
                        getMvpView().renderErrorNoConnectionGetPromoDetail(
                                ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                        );
                    } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                        // Timeout
                        getMvpView().renderErrorTimeoutConnectionGetPromoDetail(
                                ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                        );
                    } else if (e instanceof HttpErrorException) {
                        // Http errors such as 4xx, 5xx
                        getMvpView().renderErrorHttpGetPromoDetail(e.getMessage());
                    } else {
                        // Undefined errors
                        getMvpView().renderErrorHttpGetPromoDetail(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }
                    getMvpView().stopPerformanceMonitoring();
                }
                e.printStackTrace();
            }

            @Override
            public void onNext(List<PromoData> promoData) {
                if (isViewAttached()) {
                    if (promoData != null && !promoData.isEmpty()) {
                        getMvpView().renderPromoDetail(promoData.get(0));
                    } else {
                        getMvpView().renderErrorShowingPromoDetail(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }
                    getMvpView().stopPerformanceMonitoring();
                }
            }
        };
    }

    @Override
    public void cachePromoCodeData(String promoData, Resources resources) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("promoCode", promoData);

        com.tokopedia.graphql.data.model.GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.tokopoints_promo_cache),
                PromoCacheResponse.class,
                variables, false);
        GraphqlUseCase graphqlUseCase = new GraphqlUseCase();
        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(request);
        graphqlUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(GraphqlResponse saveCoupon) {
            }
        });
    }

}