package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.topads.common.data.model.DataDeposit;
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositUseCase;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.data.model.data.TotalAd;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsDatePickerInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsPopulateTotalAdsUseCase;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    @Inject
    public TopAdsDashboardPresenter(TopAdsGetShopDepositUseCase topAdsGetShopDepositUseCase,
                                    GetShopInfoUseCase getShopInfoUseCase,
                                    TopAdsDatePickerInteractor topAdsDatePickerInteractor,
                                    TopAdsPopulateTotalAdsUseCase topAdsPopulateTotalAdsUseCase) {
        this.topAdsGetShopDepositUseCase = topAdsGetShopDepositUseCase;
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.topAdsDatePickerInteractor = topAdsDatePickerInteractor;
        this.topAdsPopulateTotalAdsUseCase = topAdsPopulateTotalAdsUseCase;
    }

    public void getShopDeposit(String shopId){
        topAdsGetShopDepositUseCase.execute(TopAdsGetShopDepositUseCase.createParams(shopId), new Subscriber<DataDeposit>() {
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

    public void getShopInfo(String shopId) {
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(shopId), new Subscriber<ShopInfo>() {
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

    @Override
    public void detachView() {
        super.detachView();
        topAdsGetShopDepositUseCase.unsubscribe();
        getShopInfoUseCase.unsubscribe();
        topAdsPopulateTotalAdsUseCase.unsubscribe();
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

    public void populateTotalAds(String shopId) {
        topAdsPopulateTotalAdsUseCase.execute(TopAdsPopulateTotalAdsUseCase.createRequestParams(shopId), new Subscriber<TotalAd>() {
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
}
