package com.tokopedia.abstraction.base.view.adapter.holder;

import android.view.View;

/**
 * Created by nathan on 6/23/17.
 */

public abstract class BaseMultipleCheckViewHolder<T> extends BaseViewHolder<T> {

    public interface CheckedCallback<T> {

        void onItemChecked(T t, boolean checked);
    }

    protected CheckedCallback<T> checkedCallback;

    public void setCheckedCallback(CheckedCallback<T> checkedCallback) {
        this.checkedCallback = checkedCallback;
    }

    public abstract void bindObject(T t, boolean checked);

    public abstract boolean isChecked();

    public abstract void setChecked(boolean checked);

    public BaseMultipleCheckViewHolder(View itemView) {
        super(itemView);
    }
}