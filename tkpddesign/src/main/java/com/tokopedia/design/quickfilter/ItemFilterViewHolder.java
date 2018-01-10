package com.tokopedia.design.quickfilter;

import android.content.Context;
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

    private LinearLayout layoutBorder;
    private LinearLayout layoutInside;
    private TextView filterName;
    private Context context;

    public ItemFilterViewHolder(View itemView, Context context, QuickSingleFilterListener listener) {
        super(itemView, listener);
        this.context = context;
        layoutBorder = (LinearLayout) itemView.findViewById(R.id.layout_border);
        layoutInside = (LinearLayout) itemView.findViewById(R.id.layout_inside);
        filterName = (TextView) itemView.findViewById(R.id.filter_name);
    }

    public void renderItemViewHolder(final QuickFilterItem filterItem) {
        filterName.setText(filterItem.getName());
        layoutBorder.setBackgroundResource(R.drawable.bg_round_corner);
        layoutInside.setBackgroundResource(R.drawable.bg_round_corner);
        handleViewFilter(filterItem.isSelected());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.selectFilter(filterItem);
            }
        });
    }

    /**
     * @param selected using mutate for lock the drawable and set the color
     *                 documentation https://android-developers.googleblog.com/2009/05/drawable-mutations.html
     */
    private void handleViewFilter(boolean selected) {
        GradientDrawable drawableBorder = (GradientDrawable) layoutBorder.getBackground().getCurrent().mutate();
        drawableBorder.setColor(selected ? ContextCompat.getColor(context, R.color.tkpd_main_green) :
                ContextCompat.getColor(context, R.color.grey_300));

        GradientDrawable drawableInside = (GradientDrawable) layoutInside.getBackground().getCurrent().mutate();
        drawableInside.setColor(selected ? ContextCompat.getColor(context, R.color.light_green) :
                ContextCompat.getColor(context, R.color.white));

        filterName.setTextColor(ContextCompat.getColor(context, selected ? R.color.tkpd_main_green :
                R.color.font_black_primary_70));
    }
}
