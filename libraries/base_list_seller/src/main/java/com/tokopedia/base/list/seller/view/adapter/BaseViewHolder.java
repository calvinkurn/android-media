package com.tokopedia.base.list.seller.view.adapter;

import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by nathan on 6/23/17.
 */
@Deprecated
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public abstract void bindObject(T t);

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    protected String getString(@StringRes int stringRes) {
        return itemView.getContext().getString(stringRes);
    }

    protected String getString(@StringRes int stringRes, String value) {
        return itemView.getContext().getString(stringRes, value);
    }
}