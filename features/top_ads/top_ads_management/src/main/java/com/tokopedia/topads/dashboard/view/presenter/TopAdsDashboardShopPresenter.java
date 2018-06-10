package com.tokopedia.topads.dashboard.view.presenter;

import java.util.Date;

/**
 * Created by Nisie on 5/9/16.
 */
public interface TopAdsDashboardShopPresenter {

    /**
     * Populate total ad
     */
    void populateShopAd(Date startDate, Date endDate);
}
