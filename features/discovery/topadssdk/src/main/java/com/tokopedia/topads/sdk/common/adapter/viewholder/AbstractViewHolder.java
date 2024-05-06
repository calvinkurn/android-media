package com.tokopedia.topads.sdk.common.adapter.viewholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * @author by errysuprayogi on 3/29/17.
 */

public abstract class AbstractViewHolder<T> extends RecyclerView.ViewHolder {

    public AbstractViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(T element);
}
