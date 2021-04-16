package com.tokopedia.loyalty.view.presenter;

import android.content.res.Resources;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.domain.entity.response.promocodesave.PromoCacheResponse;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.interactor.IPromoInteractor;
import com.tokopedia.loyalty.view.util.PromoTrackingUtil;
import com.tokopedia.loyalty.view.view.IPromoListView;
import com.tokopedia.network.constant.ErrorNetMessage;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * @author anggaprasetiyo on 02/01/18.
 */

public class PromoListPresenter implements IPromoListPresenter {
    private final IPromoInteractor promoInteractor;
    private final IPromoListView view;
    private final PromoTrackingUtil promoTrackingUtil;
    private Observable observableListPromo;
    private int page = 1;

    @Inject
    public PromoListPresenter(IPromoInteractor promoInteractor, IPromoListView view, PromoTrackingUtil promoTrackingUtil) {
        this.promoInteractor = promoInteractor;
        this.view = view;
        this.promoTrackingUtil = promoTrackingUtil;
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void processGetPromoList(String subCategories, final String categoryName) {
        if (observableListPromo == null) {
            view.showProgressLoading();
            view.disableSwipeRefresh();
            TKPDMapParam<String, String> param = new TKPDMapParam<>();
            param.put("categories", subCategories);
            param.put("categories_exclude", "30");
            param.put("page", String.valueOf(page));
            observableListPromo = this.promoInteractor.getPromoList(param, getPromoListSubscriber(categoryName));
        }
    }

    private Subscriber<List<PromoData>> getPromoListSubscriber(String categoryName) {
        return new Subscriber<List<PromoData>>() {
            @Override
            public void onCompleted() {
                observableListPromo = null;
                view.enableSwipeRefresh();
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

                observableListPromo = null;
                handleErrorInitialPage(e);
                if (page == 1) view.stopPerformanceMonitoring();

            }

            @Override
            public void onNext(List<PromoData> promoData) {
                view.hideProgressLoading();
                sendImpressionTrackingData(promoData, categoryName);
                if (promoData.size() > 0) {
                    if (promoData.size() == 10) {
                        page++;
                        view.renderNextPage(true);
                    } else {
                        view.renderNextPage(false);
                    }
                    view.renderPromoDataList(promoData, true);
                    view.stopPerformanceMonitoring();
                } else {
                    view.renderEmptyResultGetPromoDataList();
                }
            }
        };
    }


    public void sendImpressionTrackingData(List<PromoData> promoDataList, String categoryName) {

        List<Object> dataLayerSinglePromoCodeList = new ArrayList<>();
        for (int i = 0, promoDataListSize = promoDataList.size(); i < promoDataListSize; i++) {
            PromoData promoData = promoDataList.get(i);
            dataLayerSinglePromoCodeList.add(DataLayer.mapOf(
                    "id", promoData.getId(),
                    "name", "promo list - P" + page + " - " + categoryName,
                    "creative", promoData.getThumbnailImage(),
                    "position", String.valueOf(i + 1),
                    "promo_id", "0"));
        }

        promoTrackingUtil.eventImpressionPromoList(view.getActivityContext(), dataLayerSinglePromoCodeList, "");
    }

    public void sendClickItemPromoListTrackingData(PromoData promoData, int position, String categoryName) {
        List<Object> dataLayerSinglePromoCodeList = new ArrayList<>();
        dataLayerSinglePromoCodeList.add(DataLayer.mapOf(
                "id", promoData.getId(),
                "name", "promo list - P" + page + " - " + categoryName,
                "creative", promoData.getThumbnailImage(),
                "position", String.valueOf(position + 1),
                "promo_id", "0",
                "promo_code", promoData.isMultiplePromo() ? promoData.getPromoCodeList() : promoData.getPromoCode())
        );
        promoTrackingUtil.eventClickPromoListItem(view.getActivityContext(), dataLayerSinglePromoCodeList, promoData.getTitle());
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
                if (e instanceof JsonSyntaxException) {
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("type", "json");
                    messageMap.put("err", Log.getStackTraceString(e));
                    messageMap.put("req", PromoCodePresenter.class.getCanonicalName());
                    ServerLogger.log(Priority.P2, "LOYALTY_PARSE_ERROR", messageMap);
                }
            }

            @Override
            public void onNext(GraphqlResponse saveCoupon) {
            }
        });
    }


    @Override
    public void processGetPromoListLoadMore(String subCategories, final String categoryName) {
                view.disableSwipeRefresh();
                TKPDMapParam<String, String> param = new TKPDMapParam<>();
                param.put("categories", subCategories);
                param.put("categories_exclude", "30");
                param.put("page", String.valueOf(page));
                promoInteractor.getPromoList(param, new Subscriber<List<PromoData>>() {
                    @Override
                    public void onCompleted() {
                        view.enableSwipeRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        handleErrorNextPage(e, page);
                    }

                    @Override
                    public void onNext(List<PromoData> promoData) {
                        sendImpressionTrackingData(promoData, categoryName);
                        if (promoData.size() > 0) {
                            if (promoData.size() == 10) {
                                page++;
                                view.renderNextPage(true);
                            } else {
                                page = 1;
                                view.renderNextPage(false);
                            }
                        } else {
                            page = 1;
                            view.renderNextPage(false);
                        }
                        view.renderPromoDataList(promoData, false);
                    }
                });
    }

    private void handleErrorInitialPage(Throwable e) {
        e.printStackTrace();
        if (e instanceof UnknownHostException) {
            /* Ini kalau ga ada internet */
            view.renderErrorNoConnectionGetPromoDataList(
                    ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
            );
        } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
            /* Ini kalau timeout */
            view.renderErrorTimeoutConnectionGetPromoDataListt(
                    ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
            );
        } else if (e instanceof HttpErrorException) {
                            /* Ini Http error, misal 403, 500, 404,
                            code http errornya bisa diambil
                             e.getErrorCode */
            view.renderErrorHttpGetPromoDataList(e.getMessage());
        } else {
            /* Ini diluar dari segalanya hahahaha */
            view.renderErrorHttpGetPromoDataList(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }

    private void handleErrorNextPage(Throwable e, int actualPage) {
        e.printStackTrace();
        if (e instanceof UnknownHostException) {
            /* Ini kalau ga ada internet */
            view.renderErrorLoadNextPage(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL, actualPage);
        } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
            /* Ini kalau timeout */
            view.renderErrorLoadNextPage(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT, actualPage);
        } else if (e instanceof HttpErrorException) {
                            /* Ini Http error, misal 403, 500, 404,
                            code http errornya bisa diambil
                             e.getErrorCode */
            view.renderErrorLoadNextPage(e.getMessage(), actualPage);
        } else {
            /* Ini diluar dari segalanya hahahaha */
            view.renderErrorLoadNextPage(ErrorNetMessage.MESSAGE_ERROR_DEFAULT, actualPage);
        }
    }
}
