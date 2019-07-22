package com.tokopedia.gm.featured.view.adapter;

import android.view.ViewGroup;

import com.tokopedia.base.list.seller.view.adapter.BaseEmptyDataBinder;
import com.tokopedia.base.list.seller.view.old.DataBindAdapter;

/**
 * Created by Nisie on 2/26/16.
 */
@Deprecated
public class GMFeatureProductEmptyDataBinder extends BaseEmptyDataBinder {

    public GMFeatureProductEmptyDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    public GMFeatureProductEmptyDataBinder(DataBindAdapter dataBindAdapter, int errorDrawableRes) {
        super(dataBindAdapter, errorDrawableRes);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        return super.newViewHolder(parent);
    }
}