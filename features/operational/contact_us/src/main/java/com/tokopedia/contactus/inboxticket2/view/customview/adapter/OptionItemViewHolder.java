package com.tokopedia.contactus.inboxticket2.view.customview.adapter;

import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.tokopedia.contactus.R;
import com.tokopedia.design.quickfilter.ItemFilterViewHolder;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterListener;

public class OptionItemViewHolder extends ItemFilterViewHolder {

    public OptionItemViewHolder(View itemView, QuickSingleFilterListener listener) {
        super(itemView, listener);
    }
    protected void updateData(QuickFilterItem filterItem) {
        filterName.setText(filterItem.getName());
        layoutBorder.setBackgroundResource(R.drawable.rounded_stroke_grey);
        layoutInside.setBackgroundResource(R.drawable.rounded_stroke_grey);
    }

    protected void updateItemColor(boolean selected) {
        GradientDrawable drawableInside = (GradientDrawable) layoutInside.getBackground().getCurrent().mutate();
        GradientDrawable drawableBorder = (GradientDrawable) layoutBorder.getBackground().getCurrent().mutate();
        if (selected) {
            drawableBorder.setColor(ContextCompat.getColor(layoutInside.getContext(), com.tokopedia.design.R.color.tkpd_main_green));
            drawableInside.setColor(ContextCompat.getColor(layoutBorder.getContext(), com.tokopedia.design.R.color.light_green));
            filterName.setTextColor(ContextCompat.getColor(filterName.getContext(), com.tokopedia.design.R.color.font_black_primary_70));
        } else {
            drawableBorder.setColor(ContextCompat.getColor(layoutInside.getContext(), com.tokopedia.design.R.color.grey_500));
            drawableInside.setColor(ContextCompat.getColor(layoutBorder.getContext(), com.tokopedia.design.R.color.white));
            filterName.setTextColor(ContextCompat.getColor(filterName.getContext(), com.tokopedia.design.R.color.font_black_primary_70));
        }
    }


}
