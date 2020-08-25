package com.tokopedia.topads.dashboard.view.presenter;

import android.content.res.Resources;

import com.tokopedia.topads.dashboard.data.model.request.DataSuggestions;
import com.tokopedia.topads.dashboard.data.model.response.TopAdsDepositResponse;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailProductViewModel;

import java.util.List;

/**
 * Created by Nathan on 5/9/16.
 */
public interface TopAdsDetailEditProductPresenter<T extends TopAdsDetailEditView> extends TopAdsDetailEditPresenter<T> {

    void saveAd(TopAdsDetailProductViewModel topAdsDetailProductViewModel, TopAdsDepositResponse.Data topAdsDepositResponse);

    void getBalance(Resources resources);

    void getBidInfo(String requestType, List<DataSuggestions> dataSuggestions, String source);
}
