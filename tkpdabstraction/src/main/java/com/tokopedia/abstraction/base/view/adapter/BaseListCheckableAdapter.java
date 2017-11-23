package com.tokopedia.abstraction.base.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.holder.CheckableBaseViewHolder;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * adapter that store the check position for each data.
 * limitation: only handle added data, if the data is removed, need to recalculate the position
 * Created by hendry on 11/24/16.
 */
public abstract class BaseListCheckableAdapter<T extends ItemType> extends BaseListAdapter<T> {

    private HashSet<Integer> checkedPositionList = new HashSet<>();

    private OnCheckableAdapterListener<T> onCheckableAdapterListener;

    public interface OnCheckableAdapterListener<T> {
        void onItemChecked(T t, boolean isChecked);
    }

    public BaseListCheckableAdapter(Context context, OnBaseListV2AdapterListener<T> onBaseListV2AdapterListener) {
        this(context, null, 0, onBaseListV2AdapterListener, null);
    }

    public BaseListCheckableAdapter(Context context, OnBaseListV2AdapterListener<T> onBaseListV2AdapterListener,
                                    OnCheckableAdapterListener<T> onCheckableAdapterListener) {
        this(context, null, 0, onBaseListV2AdapterListener, onCheckableAdapterListener);
    }

    public BaseListCheckableAdapter(Context context, @Nullable List<T> data, int rowPerPage,
                                    OnBaseListV2AdapterListener<T> onBaseListV2AdapterListener,
                                    OnCheckableAdapterListener<T> onCheckableAdapterListener) {
        super(context, data, rowPerPage, onBaseListV2AdapterListener);
        this.onCheckableAdapterListener = onCheckableAdapterListener;
    }

    @Override
    public abstract CheckableBaseViewHolder<T> onCreateItemViewHolder(ViewGroup parent, int viewType);

    protected void bindItemData(final int position, RecyclerView.ViewHolder viewHolder) {
        final T t = getData().get(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBaseListV2AdapterListener != null) {
                    onBaseListV2AdapterListener.onItemClicked(t);
                }
            }
        });
        if (viewHolder instanceof CheckableBaseViewHolder) {
            boolean isChecked = isChecked(position);
            ((CheckableBaseViewHolder<T>) viewHolder).bindObject(t, isChecked);
        }
    }

    public void resetCheckedItemSet() {
        if (checkedPositionList.size() > 0) {
            checkedPositionList = new HashSet<>();
            notifyDataSetChanged();
        }
    }

    public int getTotalChecked() {
        return checkedPositionList.size();
    }

    public List<T> getCheckedDataList(){
        List<T> dataList = getData();
        if (dataList == null || dataList.size() == 0) {
            return new ArrayList<>();
        }
        List<T> checkedDataList = new ArrayList<>();
        for (int i = 0, sizei = dataList.size(); i<sizei; i++) {
            if (isChecked(i)) {
                checkedDataList.add(dataList.get(i));
            }
        }
        return checkedDataList;
    }

    public void updateListByCheck(boolean isItemChecked, int position){
        if (isItemChecked) {
            checkedPositionList.add(position);
        } else {
            checkedPositionList.remove(position);
        }
        notifyItemChanged(position);
        if (onCheckableAdapterListener!= null) {
            onCheckableAdapterListener.onItemChecked(getData().get(position), isItemChecked);
        }
    }

    public void setCheckedPositionList(HashSet<Integer> checkedPositionList) {
        this.checkedPositionList = checkedPositionList;
    }

    public boolean isChecked(int position) {
        return checkedPositionList.contains(position);
    }

}