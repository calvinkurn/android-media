package com.tokopedia.core.base.adapter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * @author kulomady on 1/24/17.
 */

/**
 * Use abstract view holder from tkpd abstraction
 */
@Deprecated
public abstract class AbstractViewHolder<T> extends RecyclerView.ViewHolder {

    public AbstractViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public abstract void bind(T element);
}
