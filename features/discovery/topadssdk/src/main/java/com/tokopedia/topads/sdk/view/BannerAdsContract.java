package com.tokopedia.topads.sdk.view;

import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.CpmModel;

/**
 * Created by errysuprayogi on 12/28/17.
 */

public class BannerAdsContract {

    public interface View {

        void showLoading();

        void displayAds(CpmModel cpmModel);

        void onCanceled();

        void hideLoading();

        void loadTopAds();

        void notifyAdsErrorLoaded(int errorCode, String message);
    }

    public interface Presenter<V extends View> {
        void attachView(V view);

        void detachView();

        boolean isViewAttached();

        void setParams(TopAdsParams adsParams);

        void loadTopAds();

        void setConfig(Config config);
    }
}
