package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.topads.common.data.model.DataDeposit;
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsDatePickerInteractor;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hadi.putra on 23/04/18.
 */

public class TopAdsDashboardPresenter extends BaseDaggerPresenter<TopAdsDashboardView> {

    private final TopAdsGetShopDepositUseCase topAdsGetShopDepositUseCase;
    private final GetShopInfoUseCase getShopInfoUseCase;
    private final TopAdsDatePickerInteractor topAdsDatePickerInteractor;

    @Inject
    public TopAdsDashboardPresenter(TopAdsGetShopDepositUseCase topAdsGetShopDepositUseCase,
                                    GetShopInfoUseCase getShopInfoUseCase,
                                    TopAdsDatePickerInteractor topAdsDatePickerInteractor) {
        this.topAdsGetShopDepositUseCase = topAdsGetShopDepositUseCase;
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.topAdsDatePickerInteractor = topAdsDatePickerInteractor;
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
    }

    public int getLastSelectionDatePickerIndex() {
        return topAdsDatePickerInteractor.getLastSelectionDatePickerIndex();
    }

    public int getLastSelectionDatePickerType() {
        return topAdsDatePickerInteractor.getLastSelectionDatePickerType();
    }
}
