package com.tokopedia.gm.featured.view.adapter;

import com.tokopedia.base.list.seller.view.adapter.BaseEmptyDataBinder;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.gm.R;

/**
 * Created by Nisie on 2/26/16.
 */
public class GMProductEmptyDataBinder extends BaseEmptyDataBinder {

    public GMProductEmptyDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter, R.drawable.ic_empty_state_kaktus);
    }
}