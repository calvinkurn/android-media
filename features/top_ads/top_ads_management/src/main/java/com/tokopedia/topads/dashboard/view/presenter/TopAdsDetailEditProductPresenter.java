package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.topads.dashboard.data.model.request.GetSuggestionBody;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailProductViewModel;

import java.util.List;

/**
 * Created by Nathan on 5/9/16.
 */
public interface TopAdsDetailEditProductPresenter<T extends TopAdsDetailEditView> extends TopAdsDetailEditPresenter<T> {

    void saveAd(TopAdsDetailProductViewModel topAdsDetailProductViewModel);

    void getSuggestionBid(List<String> ids, String source);
}
