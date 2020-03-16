package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;

/**
 * Created by Nathan on 5/9/16.
 */
public interface TopAdsDetailEditPresenter<T extends TopAdsDetailEditView> extends TopAdsGetProductDetailPresenter<T> {
    void getDetailAd(String adId);
}
