package com.tokopedia.filter.newdynamicfilter.adapter.viewholder;

import androidx.appcompat.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.filter.R;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterView;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class DynamicFilterItemToggleViewHolder extends DynamicFilterViewHolder {

    private TextView title;
    private SwitchCompat toggle;
    private final DynamicFilterView dynamicFilterView;

    public DynamicFilterItemToggleViewHolder(View itemView, DynamicFilterView dynamicFilterView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        toggle = itemView.findViewById(R.id.toggle);
        this.dynamicFilterView = dynamicFilterView;
    }

    @Override
    public void bind(Filter filter) {
        final Option option = filter.getOptions().get(0);
        title.setText(option.getName());

        itemView.setOnClickListener(v -> toggle.setChecked(!toggle.isChecked()));

        bindSwitchForOption(option);
    }

    private void bindSwitchForOption(Option option) {
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
                (buttonView, isChecked) -> dynamicFilterView.saveCheckedState(option, isChecked);

        bindSwitch(toggle,
                dynamicFilterView.loadLastCheckedState(option),
                onCheckedChangeListener);
    }
}
