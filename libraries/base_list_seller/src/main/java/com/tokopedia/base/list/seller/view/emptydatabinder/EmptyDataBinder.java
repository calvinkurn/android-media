package com.tokopedia.seller.base.view.emptydatabinder;

import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.seller.base.view.adapter.BaseEmptyDataBinder;

/**
 * Created by Nisie on 2/26/16.
 */
public class EmptyDataBinder extends BaseEmptyDataBinder {

    public EmptyDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    public EmptyDataBinder(DataBindAdapter dataBindAdapter, int errorDrawableRes) {
        super(dataBindAdapter, errorDrawableRes);
    }
}