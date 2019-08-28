package com.tokopedia.filter.newdynamicfilter.adapter.viewholder;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.filter.common.data.Filter;

/**
 * Created by henrypriyono on 8/11/17.
 */

public abstract class DynamicFilterViewHolder extends AbstractViewHolder<Filter> {

    public DynamicFilterViewHolder(View itemView) {
        super(itemView);
    }

    void bindSwitch(SwitchCompat switchView,
                           Boolean isChecked,
                           CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {

        switchView.setOnCheckedChangeListener(null);

        if (Boolean.TRUE.equals(isChecked)) {
            switchView.setChecked(true);
        } else {
            switchView.setChecked(false);
        }

        switchView.setOnCheckedChangeListener(onCheckedChangeListener);
    }
}
