package com.tokopedia.tokopoints.view.tokopointhome;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.tokopoints.view.model.rewardtopsection.TokopediaRewardTopSection;
import com.tokopedia.tokopoints.view.model.section.SectionContent;
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel;

import java.util.List;

public interface TokoPointsHomeContract {

    interface View extends CustomerView {
        void onError(String error, boolean hasInternet);

        Context getAppContext();

        Context getActivityContext();

        void openWebView(String url);

        void hideLoading();

        void showLoading();

        void renderExploreSectionTab(List<SectionContent> sections , TokopediaRewardTopSection tokopediaRewardTopSection);

        void onSuccessResponse(TokopediaRewardTopSection data, List<SectionContent> sections );

    }

    interface Presenter{

        void getTokoPointDetail();

    }
}
