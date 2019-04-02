package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.topads.dashboard.view.listener.TopAdsGetProductDetailView;

/**
 * Created by zulfikarrahman on 8/15/17.
 */

public interface TopAdsGetProductDetailPresenter<T extends TopAdsGetProductDetailView> extends CustomerPresenter<T> {
    void getProductDetail(String productId);
}
