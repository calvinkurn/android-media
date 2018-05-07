package com.tokopedia.gm.featured.view.adapter;

import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.gm.R;
import com.tokopedia.seller.base.view.adapter.BaseEmptyDataBinder;

/**
 * Created by Nisie on 2/26/16.
 */
public class GMProductEmptyDataBinder extends BaseEmptyDataBinder {

    public GMProductEmptyDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter, R.drawable.ic_empty_state_kaktus);
    }
}