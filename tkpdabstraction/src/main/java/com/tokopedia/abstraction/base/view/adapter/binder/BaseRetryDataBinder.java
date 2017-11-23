package com.tokopedia.abstraction.base.view.adapter.binder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.R;

/**
 * Created by Nisie on 2/26/16.
 */
public class BaseRetryDataBinder extends RetryDataBinder {

    private int errorDrawableRes;

    public BaseRetryDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
        this.errorDrawableRes = R.drawable.ic_error_network;
    }

    public BaseRetryDataBinder(DataBindAdapter dataBindAdapter, int errorDrawableRes) {
        super(dataBindAdapter);
        this.errorDrawableRes = errorDrawableRes;
    }

    public static class ViewHolder extends RetryDataBinder.ViewHolder {
        TextView retryDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            retryDescription = (TextView) itemView.findViewById(R.id.retry_description);
        }

        public TextView getRetryDescription() {
            return retryDescription;
        }
    }

    @Override
    public RetryDataBinder.ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_base_network_error, null);
        ((ImageView) view.findViewById(R.id.image_error)).setImageResource(errorDrawableRes);
        if (parent.getMeasuredHeight() < parent.getMeasuredWidth()) {
            view.setLayoutParams(new AbsListView.LayoutParams(-1, parent.getMeasuredWidth()));
        } else {
            view.setLayoutParams(new AbsListView.LayoutParams(-1, parent.getMeasuredHeight()));
        }
        return new ViewHolder(view);
    }
}