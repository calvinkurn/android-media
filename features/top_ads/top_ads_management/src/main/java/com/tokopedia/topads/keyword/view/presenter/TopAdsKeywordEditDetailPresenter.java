package com.tokopedia.topads.keyword.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordEditDetailView;
import com.tokopedia.topads.keyword.view.model.KeywordAd;

/**
 * @author sebastianuskh on 5/26/17.
 */

public abstract class TopAdsKeywordEditDetailPresenter extends BaseDaggerPresenter<TopAdsKeywordEditDetailView> {
    public abstract void editTopAdsKeywordDetail(KeywordAd topAdsKeywordEditDetailViewModel);

    public abstract void unSubscribe();
}
