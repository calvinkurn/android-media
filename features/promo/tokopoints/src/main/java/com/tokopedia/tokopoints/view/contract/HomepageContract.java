package com.tokopedia.tokopoints.view.contract;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.LobDetails;
import com.tokopedia.tokopoints.view.model.LuckyEggEntity;
import com.tokopedia.tokopoints.view.model.TickerContainer;
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity;
import com.tokopedia.tokopoints.view.model.TokoPointStatusPointsEntity;
import com.tokopedia.tokopoints.view.model.TokoPointStatusTierEntity;

import java.util.List;

public interface HomepageContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void onError(String error);

        void onSuccess(TokoPointStatusTierEntity tierData, TokoPointStatusPointsEntity pointData, LobDetails lobDetails);

        void onErrorPromos(String error);

        void onSuccessPromos(TokoPointPromosEntity data);

        Context getAppContext();

        Context getActivityContext();

        void openWebView(String url);

        void gotoCatalog();

        void gotoCoupons();

        void showRedeemCouponDialog(String cta, String code, String title);

        void showConfirmRedeemDialog(String cta, String code, String title);

        void showValidationMessageDialog(CatalogsValueEntity item, String title, String message, int resCode);

        void onSuccessTokenDetail(LuckyEggEntity tokenDetail);

        void onSuccessTicker(List<TickerContainer> tickers);

        void onErrorTicker(String errorMessage);

        void showRedeemFullError(CatalogsValueEntity item, String title);
    }

    interface Presenter extends CustomerPresenter<View> {
        void destroyView();

        void getTokoPointDetail();

        void getPromos();
    }
}
