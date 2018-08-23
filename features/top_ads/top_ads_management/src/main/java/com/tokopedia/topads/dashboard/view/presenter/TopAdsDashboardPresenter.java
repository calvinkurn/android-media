package com.tokopedia.topads.dashboard.view.presenter;

import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.data.model.DataDeposit;
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositUseCase;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsStatisticsType;
import com.tokopedia.topads.dashboard.data.model.data.Cell;
import com.tokopedia.topads.dashboard.data.model.data.DashboardPopulateResponse;
import com.tokopedia.topads.dashboard.data.model.data.DataStatistic;
import com.tokopedia.topads.dashboard.data.model.data.TotalAd;
import com.tokopedia.topads.dashboard.data.model.ticker.ResponseTickerTopads;
import com.tokopedia.topads.dashboard.data.model.ticker.TopAdsTicker;
import com.tokopedia.topads.dashboard.domain.interactor.DeleteTopAdsStatisticsUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.DeleteTopAdsTotalAdUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsDatePickerInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetPopulateDataAdUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetStatisticsUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsPopulateTotalAdsUseCase;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardView;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hadi.putra on 23/04/18.
 */

public class TopAdsDashboardPresenter extends BaseDaggerPresenter<TopAdsDashboardView> {

    private final TopAdsGetShopDepositUseCase topAdsGetShopDepositUseCase;
    private final GetShopInfoUseCase getShopInfoUseCase;
    private final TopAdsDatePickerInteractor topAdsDatePickerInteractor;
    private final TopAdsPopulateTotalAdsUseCase topAdsPopulateTotalAdsUseCase;
    private final TopAdsGetStatisticsUseCase topAdsGetStatisticsUseCase;
    private final UserSession userSession;
    private final TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase;
    private final DeleteTopAdsStatisticsUseCase deleteTopAdsStatisticsUseCase;
    private final DeleteTopAdsTotalAdUseCase deleteTopAdsTotalAdUseCase;
    private final TopAdsGetPopulateDataAdUseCase topAdsGetPopulateDataAdUseCase;

    @Inject
    public TopAdsDashboardPresenter(TopAdsGetShopDepositUseCase topAdsGetShopDepositUseCase,
                                    GetShopInfoUseCase getShopInfoUseCase,
                                    TopAdsDatePickerInteractor topAdsDatePickerInteractor,
                                    TopAdsPopulateTotalAdsUseCase topAdsPopulateTotalAdsUseCase,
                                    TopAdsGetStatisticsUseCase topAdsGetStatisticsUseCase,
                                    TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase,
                                    TopAdsGetPopulateDataAdUseCase topAdsGetPopulateDataAdUseCase,
                                    DeleteTopAdsStatisticsUseCase deleteTopAdsStatisticsUseCase,
                                    DeleteTopAdsTotalAdUseCase deleteTopAdsTotalAdUseCase,
                                    UserSession userSession) {
        this.topAdsGetShopDepositUseCase = topAdsGetShopDepositUseCase;
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.topAdsDatePickerInteractor = topAdsDatePickerInteractor;
        this.topAdsPopulateTotalAdsUseCase = topAdsPopulateTotalAdsUseCase;
        this.topAdsGetStatisticsUseCase = topAdsGetStatisticsUseCase;
        this.topAdsAddSourceTaggingUseCase = topAdsAddSourceTaggingUseCase;
        this.topAdsGetPopulateDataAdUseCase = topAdsGetPopulateDataAdUseCase;
        this.deleteTopAdsStatisticsUseCase = deleteTopAdsStatisticsUseCase;
        this.deleteTopAdsTotalAdUseCase = deleteTopAdsTotalAdUseCase;
        this.userSession = userSession;
    }

    public void getPopulateDashboardData(){
        topAdsGetPopulateDataAdUseCase.execute(TopAdsGetPopulateDataAdUseCase.createRequestParams(userSession.getShopId()),
                new Subscriber<DashboardPopulateResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()){
                            getView().onErrorPopulateData(e);
                        }
                    }

                    @Override
                    public void onNext(DashboardPopulateResponse dashboardPopulateResponse) {
                        if (isViewAttached()){
                            getView().onSuccessPopulateData(dashboardPopulateResponse);
                        }
                    }
                });
    }

    public void getShopDeposit(){
        topAdsGetShopDepositUseCase.execute(TopAdsGetShopDepositUseCase.createParams(userSession.getShopId()),
                new Subscriber<DataDeposit>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()){
                    getView().onLoadTopAdsShopDepositError(e);
                }
            }

            @Override
            public void onNext(DataDeposit dataDeposit) {
                if (isViewAttached()){
                    getView().onLoadTopAdsShopDepositSuccess(dataDeposit);
                }
            }
        });
    }

    public void getShopInfo() {
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(userSession.getShopId()), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().onErrorGetShopInfo(e);
                }
            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                getView().onSuccessGetShopInfo(shopInfo);
            }
        });
    }

    public void saveSourceTagging(@TopAdsSourceOption String source){
        topAdsAddSourceTaggingUseCase.execute(TopAdsAddSourceTaggingUseCase.createRequestParams(source),
                new Subscriber<Void>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Void aVoid) {
                //do nothing
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsGetShopDepositUseCase.unsubscribe();
        getShopInfoUseCase.unsubscribe();
        topAdsPopulateTotalAdsUseCase.unsubscribe();
        topAdsGetStatisticsUseCase.unsubscribe();
        topAdsAddSourceTaggingUseCase.unsubscribe();
        deleteTopAdsStatisticsUseCase.unsubscribe();
        deleteTopAdsTotalAdUseCase.unsubscribe();
        topAdsGetPopulateDataAdUseCase.unsubscribe();
    }

    public int getLastSelectionDatePickerIndex() {
        return topAdsDatePickerInteractor.getLastSelectionDatePickerIndex();
    }

    public int getLastSelectionDatePickerType() {
        return topAdsDatePickerInteractor.getLastSelectionDatePickerType();
    }

    public boolean isDateUpdated(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return true;
        }
        String dateText = new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate);
        String dateTextCache = new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(getStartDate());
        if (!dateText.equalsIgnoreCase(dateTextCache)) {
            return true;
        }
        dateText = new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate);
        dateTextCache = new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(getEndDate());
        if (!dateText.equalsIgnoreCase(dateTextCache)) {
            return true;
        }
        return false;
    }

    public Date getEndDate() {
        Calendar endCalendar = Calendar.getInstance();
        return topAdsDatePickerInteractor.getEndDate(endCalendar.getTime());
    }

    public Date getStartDate() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DAY_OF_YEAR, -DatePickerConstant.DIFF_ONE_WEEK);
        return topAdsDatePickerInteractor.getStartDate(startCalendar.getTime());
    }

    public void saveDate(Date startDate, Date endDate) {
        topAdsDatePickerInteractor.saveDate(startDate, endDate);
    }

    public void saveSelectionDatePicker(int selectionType, int lastSelection) {
        topAdsDatePickerInteractor.saveSelectionDatePicker(selectionType, lastSelection);
    }

    public void populateTotalAds() {
        topAdsPopulateTotalAdsUseCase.execute(TopAdsPopulateTotalAdsUseCase.createRequestParams(userSession.getShopId()),
                new Subscriber<TotalAd>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()){
                    getView().onErrorPopulateTotalAds(e);
                }
            }

            @Override
            public void onNext(TotalAd totalAd) {
                if (isViewAttached()){
                    getView().onSuccessPopulateTotalAds(totalAd);
                }
            }
        });
    }

    public void getTopAdsStatistic(Date startDate, Date endDate, @TopAdsStatisticsType int selectedStatisticType) {
        topAdsGetStatisticsUseCase.execute(TopAdsGetStatisticsUseCase.createRequestParams(startDate, endDate,
                selectedStatisticType, userSession.getShopId()), new Subscriber<DataStatistic>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()){
                    getView().onErrorGetStatisticsInfo(e);
                }
            }

            @Override
            public void onNext(DataStatistic dataStatistic) {
                if (isViewAttached()){
                    getView().onSuccesGetStatisticsInfo(dataStatistic);
                }
            }
        });
    }

    public void getTickerTopAds(Resources resources){
        GraphqlUseCase graphqlUseCase = new GraphqlUseCase();
        Map<String, Object> variables = new HashMap<>();
        variables.put(TopAdsConstant.SHOP_ID, userSession.getShopId());
        GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.query_ticker), TopAdsTicker.class, variables);
        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().onErrorGetTicker(e);
                }
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                TopAdsTicker topAdsTicker = graphqlResponse.getData(TopAdsTicker.class);
                getView().onSuccessGetTicker(topAdsTicker.getData().getMessage());
            }
        });
    }

    public void clearStatisticsCache() {
        deleteTopAdsStatisticsUseCase.executeSync();
    }

    public void clearTotalAdCache() {
        deleteTopAdsTotalAdUseCase.executeSync();
    }

    public void resetDate() {
        topAdsDatePickerInteractor.resetDate();
    }
}
