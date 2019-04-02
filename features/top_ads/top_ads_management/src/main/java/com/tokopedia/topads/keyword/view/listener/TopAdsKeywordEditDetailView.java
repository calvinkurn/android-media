package com.tokopedia.topads.keyword.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.topads.keyword.view.model.KeywordAd;

/**
 * @author sebastianuskh on 5/26/17.
 */

public interface TopAdsKeywordEditDetailView extends CustomerView {
    void showError(Throwable detail);

    void onSuccessEditTopAdsKeywordDetail(KeywordAd viewModel);
}
