package com.tokopedia.abstraction.base.view.adapter.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class BaseListCheckableAdapter<T extends Visitable, F extends BaseListCheckableTypeFactory<T>> extends BaseListAdapter<T, F> {

    private HashSet<Integer> checkedPositionList = new HashSet<>();

    private OnCheckableAdapterListener<T> onCheckableAdapterListener;

    public BaseListCheckableAdapter(F baseListAdapterTypeFactory, OnCheckableAdapterListener<T> onCheckableAdapterListener) {
        super(baseListAdapterTypeFactory);
        this.onCheckableAdapterListener = onCheckableAdapterListener;
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

    public List<T> getCheckedDataList() {
        List<T> checkedDataList = new ArrayList<>();
        try {
            List<T> dataList = (List<T>) visitables;
            if (dataList == null || dataList.size() == 0) {
                return new ArrayList<>();
            }
            for (int i = 0, sizei = dataList.size(); i < sizei; i++) {
                if (isChecked(i)) {
                    checkedDataList.add(dataList.get(i));
                }
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return checkedDataList;
    }

    public void updateListByCheck(boolean isItemChecked, int position) {
        if (isItemChecked) {
            checkedPositionList.add(position);
        } else {
            checkedPositionList.remove(position);
        }
        notifyItemChanged(position);
        if (onCheckableAdapterListener != null) {
            try {
                onCheckableAdapterListener.onItemChecked((T) visitables.get(position), isItemChecked);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    public void setCheckedPositionList(HashSet<Integer> checkedPositionList) {
        this.checkedPositionList = checkedPositionList;
    }

    public boolean isChecked(int position) {
        return checkedPositionList.contains(position);
    }

    public interface OnCheckableAdapterListener<T> {
        void onItemChecked(T t, boolean isChecked);
    }

}