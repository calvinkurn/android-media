package com.tokopedia.abstraction.base.view.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;

import java.util.HashSet;
import java.util.List;

/**
 * Created by hendry on 11/24/16.
 */
public abstract class BaseListCheckableV2Adapter<T extends ItemType> extends BaseListV2Adapter<T> {

    private HashSet<Integer> checkedPositionList = new HashSet<>();

    private OnCheckableAdapterListener<T> onCheckableAdapterListener;

    public interface OnCheckableAdapterListener<T> {
        void onItemChecked(T t, boolean isChecked);
    }

    public BaseListCheckableV2Adapter(OnBaseListV2AdapterListener<T> onBaseListV2AdapterListener) {
        this(null, 0, onBaseListV2AdapterListener, null);
    }

    public BaseListCheckableV2Adapter(OnBaseListV2AdapterListener<T> onBaseListV2AdapterListener,
                                      OnCheckableAdapterListener<T> onCheckableAdapterListener) {
        this(null, 0, onBaseListV2AdapterListener, onCheckableAdapterListener);
    }

    public BaseListCheckableV2Adapter(@Nullable List<T> data, int rowPerPage,
                                      OnBaseListV2AdapterListener<T> onBaseListV2AdapterListener,
                                      OnCheckableAdapterListener<T> onCheckableAdapterListener) {
        super(onBaseListV2AdapterListener);
        this.onCheckableAdapterListener = onCheckableAdapterListener;
    }

    @Override
    public abstract CheckableBaseViewHolder<T > onCreateItemViewHolder(ViewGroup parent, int viewType);

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
            boolean isChecked = checkedPositionList.contains(position);
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

    public abstract class CheckableBaseViewHolder<U extends T> extends BaseViewHolder<U> implements CompoundButton.OnCheckedChangeListener {

        public CheckableBaseViewHolder(View itemView) {
            super(itemView);
        }

        public final void bindObject(U u) {
            bindObject(u, false);
        }

        public abstract CheckBox getCheckBox();

        @CallSuper
        public void bindObject(U u, boolean isChecked) {
            CheckBox checkBox = getCheckBox();
            if (checkBox!= null) {
                checkBox.setOnCheckedChangeListener (null);
                checkBox.setChecked(isChecked);
                checkBox.setOnCheckedChangeListener(this);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setChecked(isChecked);
        }

        public void toggle(){
            boolean becomeChecked = !isChecked();
            setChecked(becomeChecked);
        }

        private void updateListByCheck(boolean isItemChecked){
            int position = getAdapterPosition();
            if (isItemChecked) {
                checkedPositionList.add(position);
            } else {
                checkedPositionList.remove(position);
            }
            if (onCheckableAdapterListener!= null) {
                onCheckableAdapterListener.onItemChecked(getData().get(position), isItemChecked);
            }
        }

        public boolean isChecked(){
            return checkedPositionList.contains(getAdapterPosition());
        }

        public void setChecked(boolean checked){
            if (checked != isChecked()) {
                updateListByCheck(checked);
                if (getCheckBox()!=null) {
                    getCheckBox().setChecked(checked);
                }
            }
        }
    }


}