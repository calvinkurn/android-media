package com.tokopedia.tokopoints.view.tokopointhome;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.tokopoints.view.model.homeresponse.RewardsRecommendation;
import com.tokopedia.tokopoints.view.model.homeresponse.TopSectionResponse;
import com.tokopedia.tokopoints.view.model.section.SectionContent;

import java.util.List;

public interface TokoPointsHomeContract {

    interface View extends CustomerView {
        void onError(int error, boolean hasInternet);

        Context getAppContext();

        Context getActivityContext();

        void openWebView(String url);

        void hideLoading();

        void showLoading();

        void renderRewardUi(TopSectionResponse topSectionResponse, List<SectionContent> sections , RewardsRecommendation rewardsRecommendation);

    }

    interface Presenter{

        void getTokoPointDetail();

    }
}
