package com.tokopedia.design.quickfilter.custom;

import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.quickfilter.ItemFilterViewHolder;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterListener;

/**
 * Created by nabillasabbaha on 1/9/18.
 */

public class CustomViewItemFilterViewHolder extends ItemFilterViewHolder {

    public CustomViewItemFilterViewHolder(View itemView, QuickSingleFilterListener listener) {
        super(itemView, listener);
    }

    @Override
    protected void updateData(QuickFilterItem filterItem) {
        super.updateData(filterItem);
        updateCustomItemView((CustomViewQuickFilterItem) filterItem);
    }

    private void updateCustomItemView(CustomViewQuickFilterItem filterItem) {
        layoutInside.removeAllViews();
        if (filterItem.isSelected()) {
            if (filterItem.getSelectedView().getParent() != null) {
                ((ViewGroup) filterItem.getSelectedView().getParent()).removeView(filterItem.getSelectedView());
            }
            layoutInside.addView(filterItem.getSelectedView());
        } else {
            if (filterItem.getDefaultView().getParent() != null) {
                ((ViewGroup) filterItem.getDefaultView().getParent()).removeView(filterItem.getDefaultView());
            }
            layoutInside.addView(filterItem.getDefaultView());
        }
    }
}
