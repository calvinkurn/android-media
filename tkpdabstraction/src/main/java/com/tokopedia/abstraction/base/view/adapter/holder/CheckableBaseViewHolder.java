package com.tokopedia.abstraction.base.view.adapter.holder;

import android.support.annotation.CallSuper;
import android.view.View;
import android.widget.CompoundButton;

import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableAdapter;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;

/**
 * Created by User on 11/3/2017.
 */

public abstract class CheckableBaseViewHolder<T extends ItemType> extends BaseViewHolder<T> implements CompoundButton.OnCheckedChangeListener {

    private BaseListCheckableAdapter<T> baseListCheckableV2Adapter;
    public CheckableBaseViewHolder(View itemView, BaseListCheckableAdapter<T> baseListCheckableV2Adapter) {
        super(itemView);
        this.baseListCheckableV2Adapter = baseListCheckableV2Adapter;
    }

    public final void bindObject(T u) {
        bindObject(u, false);
    }

    public abstract CompoundButton getCheckable();

    @CallSuper
    public void bindObject(T u, boolean isChecked) {
        CompoundButton checkable = getCheckable();
        if (checkable!= null) {
            checkable.setOnCheckedChangeListener (null);
            checkable.setChecked(isChecked);
            checkable.setOnCheckedChangeListener(this);
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

    public boolean isChecked(){
        return baseListCheckableV2Adapter.isChecked(getAdapterPosition());
    }

    public void setChecked(boolean checked){
        if (checked != isChecked()) {
            baseListCheckableV2Adapter.updateListByCheck(checked, getAdapterPosition());
            if (getCheckable()!=null) {
                getCheckable().setChecked(checked);
            }
        }
    }
}