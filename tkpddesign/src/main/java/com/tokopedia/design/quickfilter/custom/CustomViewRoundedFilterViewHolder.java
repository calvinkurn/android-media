package com.tokopedia.design.quickfilter.custom;

import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.quickfilter.ItemFilterViewHolder;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterListener;

public class CustomViewRoundedFilterViewHolder extends ItemFilterViewHolder {

    private TextView filterName;

    public CustomViewRoundedFilterViewHolder(View itemView, QuickSingleFilterListener listener) {
        super(itemView, listener);
        filterName = itemView.findViewById(R.id.filter_name);
    }

    @Override
    protected void updateData(QuickFilterItem filterItem) {
        filterName.setText(filterItem.getName());
        layoutBorder.setBackgroundResource(R.drawable.order_filter_selector_chip);
    }

    @Override
    protected void updateItemColor(boolean selected) {
        layoutBorder.setSelected(selected);
        if (selected) {
            filterName.setTextColor(ContextCompat.getColor(filterName.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_G400));
        } else {
            filterName.setTextColor(ContextCompat.getColor(filterName.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
        }
    }
}
