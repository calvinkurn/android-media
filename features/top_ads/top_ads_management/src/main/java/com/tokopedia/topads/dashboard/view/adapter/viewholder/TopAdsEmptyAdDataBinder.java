package com.tokopedia.topads.dashboard.view.adapter.viewholder;

import com.tokopedia.base.list.seller.view.adapter.BaseEmptyDataBinder;
import com.tokopedia.base.list.seller.view.old.DataBindAdapter;
import com.tokopedia.topads.R;

/**
 * Created by Nisie on 2/26/16.
 */
public class TopAdsEmptyAdDataBinder extends BaseEmptyDataBinder {

    public TopAdsEmptyAdDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter, R.drawable.ic_empty_state_kaktus);
    }

    public TopAdsEmptyAdDataBinder(DataBindAdapter dataBindAdapter, int errorDrawableRes) {
        super(dataBindAdapter, errorDrawableRes);
    }
}