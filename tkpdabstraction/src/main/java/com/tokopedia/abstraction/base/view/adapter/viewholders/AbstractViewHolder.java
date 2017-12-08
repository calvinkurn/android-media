package com.tokopedia.abstraction.base.view.adapter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * @author kulomady on 1/24/17.
 */
public abstract class AbstractViewHolder<T> extends RecyclerView.ViewHolder {

    public AbstractViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(T element);

    public abstract int viewType();
}
