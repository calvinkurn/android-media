package com.tokopedia.topads.common.view.adapter.viewholder;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

/**
 * Created by hadi.putra on 09/05/18.
 */

public abstract class BaseMultipleCheckViewHolder<T extends Visitable> extends AbstractViewHolder<T> {
    protected CheckedCallback<T> checkedCallback;

    public void setCheckedCallback(CheckedCallback<T> checkedCallback) {
        this.checkedCallback = checkedCallback;
    }

    public BaseMultipleCheckViewHolder(View itemView) {
        super(itemView);
    }

    public abstract boolean isChecked();

    public abstract void bindObject(T item, boolean isChecked);

    public abstract void setChecked(boolean checked);

    public abstract void showCheckButton(boolean isInActionMode);

    public interface CheckedCallback<T>{
        void onItemChecked(T item, boolean isChecked);
    }
}
