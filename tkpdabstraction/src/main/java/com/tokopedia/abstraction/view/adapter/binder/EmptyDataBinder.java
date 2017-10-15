package com.tokopedia.abstraction.view.adapter.binder;

import com.tokopedia.abstraction.view.adapter.binder.BaseEmptyDataBinder;
import com.tokopedia.abstraction.view.adapter.binder.DataBindAdapter;

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