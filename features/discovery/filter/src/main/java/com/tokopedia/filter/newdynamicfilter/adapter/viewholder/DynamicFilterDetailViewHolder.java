package com.tokopedia.filter.newdynamicfilter.adapter.viewholder;

import android.view.View;
import android.widget.CheckBox;

import com.tokopedia.filter.R;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterDetailView;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

/**
 * Created by henrypriyono on 8/29/17.
 */

public abstract class DynamicFilterDetailViewHolder extends AbstractViewHolder<Option> {

    protected CheckBox checkBox;
    private DynamicFilterDetailView filterDetailView;

    public DynamicFilterDetailViewHolder(View itemView, DynamicFilterDetailView filterDetailView) {
        super(itemView);
        this.filterDetailView = filterDetailView;
        checkBox = (CheckBox) itemView.findViewById(R.id.filter_detail_item_checkbox);
    }

    @Override
    public void bind(Option option) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(!checkBox.isChecked());
            }
        });
        OptionHelper.bindOptionWithCheckbox(option, checkBox, filterDetailView);
    }
}
