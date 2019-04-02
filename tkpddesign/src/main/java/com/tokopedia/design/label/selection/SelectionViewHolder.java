package com.tokopedia.design.label.selection;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.design.R;
import com.tokopedia.design.item.DeletableItemView;

/**
 * Created by nathan on 10/29/17.
 */

public abstract class SelectionViewHolder<T extends SelectionItem> extends RecyclerView.ViewHolder {

    protected DeletableItemView selectedItem;

    public SelectionViewHolder(View itemView) {
        super(itemView);
        selectedItem = (DeletableItemView) itemView.findViewById(R.id.selected_item);
    }

    public void bind(T t, final int position, DeletableItemView.OnDeleteListener onDeleteListener) {
        selectedItem.setOnDeleteListener(onDeleteListener);
    }
}