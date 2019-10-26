package com.tokopedia.filter.newdynamicfilter.adapter.viewholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.tokopedia.filter.R;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterDetailView;

public class DynamicFilterDetailViewHolder extends RecyclerView.ViewHolder {

    protected CheckBox checkBox;
    private DynamicFilterDetailView filterDetailView;

    public DynamicFilterDetailViewHolder(View itemView, DynamicFilterDetailView filterDetailView) {
        super(itemView);
        this.filterDetailView = filterDetailView;
        checkBox = (CheckBox) itemView.findViewById(R.id.filter_detail_item_checkbox);
    }

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
