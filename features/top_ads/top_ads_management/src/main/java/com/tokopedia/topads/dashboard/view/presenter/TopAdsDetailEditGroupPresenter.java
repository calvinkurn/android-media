package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.topads.dashboard.data.model.request.GetSuggestionBody;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;

/**
 * Created by Nathan on 5/9/16.
 */
public interface TopAdsDetailEditGroupPresenter<T extends TopAdsDetailEditView> extends TopAdsDetailEditPresenter<T> {

    void saveAd(TopAdsDetailGroupViewModel topAdsDetailGroupViewModel);

    void getTopAdsSuggestionBid(GetSuggestionBody getSuggestionBody);
}
