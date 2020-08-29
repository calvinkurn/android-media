package com.tokopedia.tokopoints.view.tokopointhome;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.tokopoints.notification.model.PopupNotification;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.LobDetails;
import com.tokopedia.tokopoints.view.model.LuckyEggEntity;
import com.tokopedia.tokopoints.view.model.TokoPointEntity;
import com.tokopedia.tokopoints.view.model.TokoPointSumCoupon;
import com.tokopedia.tokopoints.view.model.rewardtopsection.TokopediaRewardTopSection;
import com.tokopedia.tokopoints.view.model.section.SectionContent;
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel;
import com.tokopedia.topads.sdk.widget.TopAdsImageView;

import java.util.List;

public interface TokoPointsHomeContract {

    interface View extends CustomerView {
        void onError(String error, boolean hasInternet);

        Context getAppContext();

        Context getActivityContext();

        void openWebView(String url);

        void hideLoading();

        void showLoading();

        /* New UI callback contract*/

        /**
         * Look for
         *
         * @param content SectionContent
         */
        void renderTicker(SectionContent content);

        void renderCategory(SectionContent content);

        void renderToolbarWithHeader(TokopediaRewardTopSection data);

        void renderSections(List<SectionContent> sections );

        void renderExploreSectionTab(List<SectionContent> sections );

        void onSuccessResponse(TokopediaRewardTopSection data, List<SectionContent> sections);

    }

    interface Presenter{

        void getTokoPointDetail();

    }
}
