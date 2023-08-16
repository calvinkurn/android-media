package com.tokopedia.base.list.seller.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.tokopedia.base.list.seller.R;
import com.tokopedia.base.list.seller.view.old.DataBindAdapter;
import com.tokopedia.base.list.seller.view.old.RetryDataBinder;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

/**
 * Created by Nisie on 2/26/16.
 */
@Deprecated
public class BaseRetryDataBinder extends RetryDataBinder {

    private int errorDrawableRes;

    public BaseRetryDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
        this.errorDrawableRes = com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection;
    }

    public BaseRetryDataBinder(DataBindAdapter dataBindAdapter, int errorDrawableRes) {
        super(dataBindAdapter);
        this.errorDrawableRes = errorDrawableRes;
    }

    @Override
    public RetryDataBinder.ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.tokopedia.abstraction.R.layout.item_base_network_error, null);
        ((ImageView) view.findViewById(com.tokopedia.abstraction.R.id.image_error)).setImageDrawable(MethodChecker.getDrawable(parent.getContext(),errorDrawableRes));
        if (parent.getMeasuredHeight() < parent.getMeasuredWidth()) {
            view.setLayoutParams(new AbsListView.LayoutParams(-1, parent.getMeasuredWidth()));
        } else {
            view.setLayoutParams(new AbsListView.LayoutParams(-1, parent.getMeasuredHeight()));
        }
        return new ViewHolder(view);
    }
}