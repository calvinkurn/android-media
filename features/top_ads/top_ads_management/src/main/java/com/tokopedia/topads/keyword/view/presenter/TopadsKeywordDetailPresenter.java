package com.tokopedia.topads.keyword.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailViewListener;

import java.util.Date;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public abstract class TopadsKeywordDetailPresenter<T extends TopAdsDetailViewListener> extends BaseDaggerPresenter<T> {
    public abstract void refreshAd(Date startDate, Date endDate, String id, int isPositve, String shopId);

    public abstract void deleteAd(String id, String groupId, String shopId);

    public abstract void unSubscribe();

    public abstract void turnOnAd(String id, String groupId, String shopID);

    public abstract void turnOffAd(String id, String groupId, String shopID);
}
