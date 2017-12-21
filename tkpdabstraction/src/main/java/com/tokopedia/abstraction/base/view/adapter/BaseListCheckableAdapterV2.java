package com.tokopedia.abstraction.base.view.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class BaseListCheckableAdapterV2<T extends Visitable, F extends BaseListCheckableTypeFactory<T>> extends BaseListAdapterV2<T, F> {

    private HashSet<Integer> checkedPositionList = new HashSet<>();

    private OnCheckableAdapterListener<T> onCheckableAdapterListener;

    public BaseListCheckableAdapterV2(F baseListAdapterTypeFactory, OnCheckableAdapterListener<T> onCheckableAdapterListener) {
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
        List<T> dataList = (List<T>) visitables;
        if (dataList == null || dataList.size() == 0) {
            return new ArrayList<>();
        }
        List<T> checkedDataList = new ArrayList<>();
        for (int i = 0, sizei = dataList.size(); i < sizei; i++) {
            if (isChecked(i)) {
                checkedDataList.add(dataList.get(i));
            }
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
            onCheckableAdapterListener.onItemChecked((T) visitables.get(position), isItemChecked);
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