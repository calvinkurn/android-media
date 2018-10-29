package com.tokopedia.base.list.seller.view.emptydatabinder;

import com.tokopedia.base.list.seller.view.adapter.BaseEmptyDataBinder;
import com.tokopedia.base.list.seller.view.old.DataBindAdapter;

/**
 * Created by Nisie on 2/26/16.
 */
@Deprecated
public class EmptyDataBinder extends BaseEmptyDataBinder {

    public EmptyDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    public EmptyDataBinder(DataBindAdapter dataBindAdapter, int errorDrawableRes) {
        super(dataBindAdapter, errorDrawableRes);
    }
}