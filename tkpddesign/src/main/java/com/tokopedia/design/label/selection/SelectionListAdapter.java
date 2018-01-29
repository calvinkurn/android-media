package com.tokopedia.design.label.selection;

import android.support.v7.widget.RecyclerView;

import com.tokopedia.design.item.DeletableItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 8/11/17.
 */

public abstract class SelectionListAdapter<T extends SelectionItem> extends RecyclerView.Adapter<SelectionViewHolder<T>> {

    interface OnDeleteListener<T> {
        void onDelete(T t);
    }

    private List<T> itemList;
    private OnDeleteListener<T> onDeleteListener;

    public void setItemList(List<T> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public void setOnDeleteListener(OnDeleteListener<T> onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public SelectionListAdapter() {
        itemList = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(final SelectionViewHolder<T> holder, int position) {
        holder.bind(itemList.get(position), position, new DeletableItemView.OnDeleteListener() {
            @Override
            public void onDelete() {
                if (onDeleteListener != null) {
                    onDeleteListener.onDelete(itemList.get(holder.getAdapterPosition()));
                }
                itemList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}