package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.dashboard.data.model.data.GroupAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;

import java.util.List;

/**
 * Created by Nathaniel on 12/28/2016.
 */

public interface TopAdsGroupAdInteractor {

    void searchAd(SearchAdRequest searchAdRequest, final ListenerInteractor<PageDataResponse<List<GroupAd>>> listener);

    void bulkAction(DataRequest<GroupAdBulkAction> bulkActionDataRequest, final ListenerInteractor<GroupAdBulkAction> listener);

    void unSubscribe();
}
