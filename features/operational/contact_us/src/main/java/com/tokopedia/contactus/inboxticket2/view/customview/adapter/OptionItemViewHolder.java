package com.tokopedia.contactus.inboxticket2.view.customview.adapter;

import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.contactus.R;
import com.tokopedia.design.quickfilter.ItemFilterViewHolder;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterListener;

public class OptionItemViewHolder extends ItemFilterViewHolder {

    private ImageView layoutInside;

    public OptionItemViewHolder(View itemView, QuickSingleFilterListener listener) {
        super(itemView, listener);
        layoutInside =itemView.findViewById(R.id.layout_inside1);
    }
    protected void updateData(QuickFilterItem filterItem) {
        filterName.setText(filterItem.getName());
          layoutInside.setBackgroundResource(R.drawable.check_box_bg);
    }

    protected void updateItemColor(boolean selected) {
        if (selected) {
            layoutInside.setBackgroundResource(R.drawable.checked);
            filterName.setTextColor(ContextCompat.getColor(filterName.getContext(), com.tokopedia.design.R.color.font_black_primary_70));
        } else {
            layoutInside.setBackgroundResource(R.drawable.check_box_bg);
            filterName.setTextColor(ContextCompat.getColor(filterName.getContext(), com.tokopedia.design.R.color.grey_500));
        }
    }


}
