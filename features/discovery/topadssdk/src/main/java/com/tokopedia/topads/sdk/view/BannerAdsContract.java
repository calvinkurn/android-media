package com.tokopedia.topads.sdk.view;

import com.tokopedia.topads.sdk.domain.model.CpmModel;

/**
 * Created by errysuprayogi on 12/28/17.
 */

public class BannerAdsContract {

    public interface View {

        void showLoading();

        void displayAds(CpmModel cpmModel, int index);

        void onCanceled();

        void hideLoading();
    }

}
