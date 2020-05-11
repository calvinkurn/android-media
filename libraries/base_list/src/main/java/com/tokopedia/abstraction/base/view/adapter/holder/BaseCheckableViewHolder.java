package com.tokopedia.abstraction.base.view.adapter.holder;

import android.view.View;
import android.widget.CompoundButton;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

public abstract class BaseCheckableViewHolder<T extends Visitable> extends AbstractViewHolder<T> implements CompoundButton.OnCheckedChangeListener {

    private CheckableInteractionListener checkableInteractionListener;

    public BaseCheckableViewHolder(View itemView, CheckableInteractionListener checkableInteractionListener) {
        super(itemView);
        this.checkableInteractionListener = checkableInteractionListener;
    }

    public abstract CompoundButton getCheckable();

    public void bind(T u) {
        CompoundButton checkable = getCheckable();
        if (checkable != null) {
            checkable.setOnCheckedChangeListener(null);
            checkable.setChecked(checkableInteractionListener.isChecked(getAdapterPosition()));
            checkable.setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        setChecked(isChecked);
    }

    public void toggle() {
        boolean becomeChecked = !isChecked();
        setChecked(becomeChecked);
    }

    public boolean isChecked() {
        return checkableInteractionListener.isChecked(getAdapterPosition());
    }

    public void setChecked(boolean checked) {
        if (checked != isChecked()) {
            checkableInteractionListener.updateListByCheck(checked, getAdapterPosition());
            if (getCheckable() != null) {
                getCheckable().setChecked(checked);
            }
        }
    }

    public interface CheckableInteractionListener {
        boolean isChecked(int position);

        void updateListByCheck(boolean isChecked, int position);
    }
}