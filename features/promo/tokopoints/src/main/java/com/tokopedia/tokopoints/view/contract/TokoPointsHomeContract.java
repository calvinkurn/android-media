package com.tokopedia.tokopoints.view.contract;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokopoints.notification.model.PopupNotification;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.LobDetails;
import com.tokopedia.tokopoints.view.model.LuckyEggEntity;
import com.tokopedia.tokopoints.view.model.TokoPointEntity;
import com.tokopedia.tokopoints.view.model.TokoPointSumCoupon;
import com.tokopedia.tokopoints.view.model.section.SectionContent;

import java.util.List;

public interface TokoPointsHomeContract {

    interface View extends CustomerView {
        void onError(String error, boolean hasInternet);

        Context getAppContext();

        Context getActivityContext();

        void openWebView(String url);

        void hideLoading();

        void showLoading();

        void showRedeemCouponDialog(String cta, String code, String title);

        void showConfirmRedeemDialog(String cta, String code, String title);

        void showValidationMessageDialog(CatalogsValueEntity item, String title, String message, int resCode);

        void onSuccessTokenDetail(LuckyEggEntity tokenDetail);

        void showTokoPointCoupon(TokoPointSumCoupon data);

        void onFinishRendering();

        /* New UI callback contract*/

        /**
         * Look for
         *
         * @param content SectionContent
         */
        void renderTicker(SectionContent content);

        void renderCategory(SectionContent content);

        void renderToolbarWithHeader(TokoPointEntity data);

        void renderSections(List<SectionContent> sections);

        void renderPurchaseBottomsheet(LobDetails data);

        void renderExploreSectionTab(List<SectionContent> sections);

        void onSuccessResponse(TokoPointEntity data, List<SectionContent> sections);
    }

    interface Presenter extends CustomerPresenter<View> {
        void destroyView();

        void getTokoPointDetail();

        void tokopointOnboarding2020();
    }
}
