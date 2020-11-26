package com.tokopedia.topads.dashboard.view.presenter;

import android.content.res.Resources;

import com.tokopedia.topads.dashboard.data.model.response.TopAdsDepositResponse;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailNewGroupView;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;

import java.util.List;

/**
 * Created by Nathan on 5/9/16.
 */
public interface TopAdsDetailNewGroupPresenter<T extends TopAdsDetailNewGroupView> extends TopAdsDetailEditGroupPresenter<T> {
    void saveAdNew(String groupName,
                   TopAdsDetailGroupViewModel topAdsDetailProductViewModel,
                   List<TopAdsProductViewModel> topAdsProductViewModelList, String source, String shopId, TopAdsDepositResponse.Data topAdsDepositResponse);

    void getBalance(Resources resources);

    void saveAdExisting(String groupId,
                        List<TopAdsProductViewModel> topAdsProductViewModelList, String source, TopAdsDepositResponse.Data topAdsDepositResponse);
}
