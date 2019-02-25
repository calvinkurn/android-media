package com.tokopedia.design.quickfilter;

import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.design.R;

/**
 * Created by nabillasabbaha on 1/9/18.
 */

public class ItemFilterViewHolder extends BaseItemFilterViewHolder {

    protected LinearLayout layoutBorder;
    protected LinearLayout layoutInside;
    private TextView filterName;

    public ItemFilterViewHolder(View itemView, QuickSingleFilterListener listener) {
        super(itemView, listener);
        layoutBorder = (LinearLayout) itemView.findViewById(R.id.layout_border);
        layoutInside = (LinearLayout) itemView.findViewById(R.id.layout_inside);
        filterName = (TextView) itemView.findViewById(R.id.filter_name);
    }

    public void renderItemViewHolder(final QuickFilterItem filterItem) {
        updateData(filterItem);
        updateItemColor(filterItem.isSelected());
        itemView.setOnClickListener(view -> {
            if (listener != null)
                listener.selectFilter(filterItem);
        });
    }

    protected void updateData(QuickFilterItem filterItem) {
        filterName.setText(filterItem.getName());
        layoutBorder.setBackgroundResource(R.drawable.bg_round_corner);
        layoutInside.setBackgroundResource(R.drawable.bg_round_corner);
    }

    /**
     * @param selected using mutate for lock the drawable and set the color
     *                 documentation https://android-developers.googleblog.com/2009/05/drawable-mutations.html
     */
    protected void updateItemColor(boolean selected) {
        GradientDrawable drawableInside = (GradientDrawable) layoutInside.getBackground().getCurrent().mutate();
        GradientDrawable drawableBorder = (GradientDrawable) layoutBorder.getBackground().getCurrent().mutate();
        if (selected) {
            drawableBorder.setColor(ContextCompat.getColor(layoutBorder.getContext(), R.color.tkpd_main_green));
            drawableInside.setColor(ContextCompat.getColor(layoutInside.getContext(), R.color.light_green));
            filterName.setTextColor(ContextCompat.getColor(filterName.getContext(), R.color.tkpd_main_green));
        } else {
            drawableBorder.setColor(ContextCompat.getColor(layoutBorder.getContext(), R.color.grey_300));
            drawableInside.setColor(ContextCompat.getColor(layoutInside.getContext(), R.color.white));
            filterName.setTextColor(ContextCompat.getColor(filterName.getContext(), R.color.font_black_primary_70));
        }
    }
}
