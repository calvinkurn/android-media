package com.tokopedia.design.label.selection;

import android.support.v7.widget.RecyclerView;

import com.tokopedia.design.item.DeletableItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 8/11/17.
 */

public abstract class SelectionListAdapter<T> extends RecyclerView.Adapter<SelectionViewHolder<T>> {

    private List<T> selectionDataList;

    public SelectionListAdapter() {
        selectionDataList = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(final SelectionViewHolder<T> holder, int position) {
        holder.bind(selectionDataList.get(position), position, new DeletableItemView.OnDeleteListener() {
            @Override
            public void onDelete() {
                selectionDataList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectionDataList.size();
    }

    public void setSelectionDataList(List<T> selectionDataList) {
        this.selectionDataList = selectionDataList;
        notifyDataSetChanged();
    }
}