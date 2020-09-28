package com.tokopedia.topads.dashboard.view.presenter;

import android.content.res.Resources;

import com.tokopedia.topads.dashboard.data.model.response.TopAdsDepositResponse;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;

import java.util.ArrayList;

/**
 * Created by Nathan on 5/9/16.
 */
public interface TopAdsDetailNewProductPresenter extends TopAdsDetailEditProductPresenter<TopAdsDetailEditView> {

    void getBalance(Resources resources);

    void saveAd(TopAdsDetailProductViewModel detailAd, ArrayList<TopAdsProductViewModel> topAdsProductList, String source, TopAdsDepositResponse.Data topAdsDepositResponse);
}
