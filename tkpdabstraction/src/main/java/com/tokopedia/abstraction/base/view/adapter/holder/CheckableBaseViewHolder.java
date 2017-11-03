package com.tokopedia.abstraction.base.view.adapter.holder;

import android.support.annotation.CallSuper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableV2Adapter;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;

/**
 * Created by User on 11/3/2017.
 */

public abstract class CheckableBaseViewHolder<T extends ItemType> extends BaseViewHolder<T> implements CompoundButton.OnCheckedChangeListener {

    private BaseListCheckableV2Adapter<T> baseListCheckableV2Adapter;
    public CheckableBaseViewHolder(View itemView, BaseListCheckableV2Adapter<T> baseListCheckableV2Adapter) {
        super(itemView);
        this.baseListCheckableV2Adapter = baseListCheckableV2Adapter;
    }

    public final void bindObject(T u) {
        bindObject(u, false);
    }

    public abstract CheckBox getCheckBox();

    @CallSuper
    public void bindObject(T u, boolean isChecked) {
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

    public boolean isChecked(){
        return baseListCheckableV2Adapter.isChecked(getAdapterPosition());
    }

    public void setChecked(boolean checked){
        if (checked != isChecked()) {
            baseListCheckableV2Adapter.updateListByCheck(checked, getAdapterPosition());
            if (getCheckBox()!=null) {
                getCheckBox().setChecked(checked);
            }
        }
    }
}