package com.tokopedia.abstraction.base.view.adapter.viewholders;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.List;


/**
 * @author kulomady on 1/24/17.
 */
public abstract class AbstractViewHolder<T extends Visitable> extends RecyclerView.ViewHolder {

    public AbstractViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(T element);

    /*
    This method is used to bind the view holder when only some parts of the model is changed.
    This way, the view holder won't be fully bind, you can control which part of it to re-bind.
    You can refer to https://medium.com/livefront/recyclerview-trick-selectively-bind-viewholders-with-payloads-4b28e3d2cce8
    on how to use it.

    Override this method to do the partial bind
     */
    public void bind(T element, @NonNull List<Object> payloads) {
    }


    /*
    Override this to do recycle and clear image resources
     */
    public void onViewRecycled() {
    }

    protected String getString(@StringRes int stringRes) {
        return itemView.getContext().getString(stringRes);
    }

    protected String getString(@StringRes int stringRes, String value) {
        return itemView.getContext().getString(stringRes, value);
    }
}
