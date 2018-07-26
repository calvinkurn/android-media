package com.tokopedia.topads.dashboard.view.listener;

import com.tokopedia.topads.dashboard.data.model.data.BulkAction;
import com.tokopedia.topads.dashboard.view.model.Ad;

/**
 * Created by zulfikarrahman on 12/30/16.
 */
public interface TopAdsDetailViewListener<V extends Ad> extends TopAdsDetailListener<V> {

    void onTurnOnAdSuccess(BulkAction dataResponseActionAds);

    void onTurnOnAdError(Throwable e);

    void onTurnOffAdSuccess(BulkAction dataResponseActionAds);

    void onTurnOffAdError();

    void onDeleteAdSuccess();

    void onDeleteAdError();


}
