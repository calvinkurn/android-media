package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.topads.dashboard.data.model.request.DataSuggestions;
import com.tokopedia.topads.dashboard.data.model.request.MinimumBidRequest;
import com.tokopedia.topads.dashboard.view.listener.TopAdsManageGroupPromoView;

import java.util.List;

/**
 * Created by zulfikarrahman on 2/16/17.
 */
public interface TopAdsManageGroupPromoPresenter<T extends TopAdsManageGroupPromoView> extends CustomerPresenter<T> {
    void checkIsGroupExist(String keyword);

    void searchGroupName(String keyword);

    void checkIsGroupExistOnSubmitNewGroup(String keyword);

    void getMinimumBid(String requestType, List<DataSuggestions> dataSuggestions, String source);
}
