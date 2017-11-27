package com.tokopedia.design.quickfilter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.design.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabillasabbaha on 11/22/17.
 */

public class QuickFilterAdapter extends RecyclerView.Adapter {

    private List<QuickFilterItem> filterList;
    private FilterTokoCashListener listener;
    private Context context;
    private QuickFilterItem lastHeaderItemColor;
    private int pos;

    public QuickFilterAdapter() {
        this.filterList = new ArrayList<>();
    }

    public void setListener(FilterTokoCashListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quick_filter_view, parent, false);
        return new ItemViewFilter(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        renderItemViewHolder((ItemViewFilter) holder, filterList.get(position), position);
    }

    private void renderItemViewHolder(final ItemViewFilter itemViewFilter, final QuickFilterItem filterItem, final int position) {
        itemViewFilter.filterName.setText(setTextFilter(filterItem.getName()));
        itemViewFilter.layoutBorder.setBackgroundResource(R.drawable.bg_round_corner);
        itemViewFilter.layoutInside.setBackgroundResource(R.drawable.bg_round_corner);
        handleViewFilter(itemViewFilter, filterItem.isSelected(), filterItem);
        itemViewFilter.layoutBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterItem.isSelected()) {
                    listener.clearFilter();
                    filterItem.setSelected(false);
                } else {
                    listener.selectFilter(filterItem.getType());
                    filterItem.setSelected(true);
                    if (lastHeaderItemColor != null) {
                        filterList.get(pos).setSelected(false);
                        notifyItemChanged(pos);
                    }
                    lastHeaderItemColor = filterItem;
                    pos = position;
                }
                handleViewFilter(itemViewFilter, filterItem.isSelected(), filterItem);
            }
        });
    }

    private String setTextFilter(String name) {
        StringBuilder sb = new StringBuilder();
        String[] splitName = name.split(" ");
        for (int i = 0; i < splitName.length; i++) {
            sb.append(splitName[i]);
            if (i < splitName.length - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     *
     * @param itemViewFilter
     * @param selected
     * @param headerColor
     *
     * using mutate for lock the drawable and set the color
     * documentation https://android-developers.googleblog.com/2009/05/drawable-mutations.html
     */
    private void handleViewFilter(ItemViewFilter itemViewFilter, boolean selected, QuickFilterItem headerColor) {
        GradientDrawable drawableBorder = (GradientDrawable) itemViewFilter.layoutBorder.getBackground().getCurrent().mutate();
        drawableBorder.setColor(ContextCompat.getColor(context, headerColor.getColorFilter()));

        GradientDrawable drawableInside = (GradientDrawable) itemViewFilter.layoutInside.getBackground().getCurrent().mutate();
        drawableInside.setColor(selected ? ContextCompat.getColor(context, headerColor.getColorFilter()) :
                ContextCompat.getColor(context, R.color.white));

        itemViewFilter.filterName.setTextColor(ContextCompat.getColor(context, selected ? R.color.white :
                R.color.font_black_primary_70));
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public void addFilterTokoCashList(List<QuickFilterItem> filterList) {
        this.filterList.clear();
        this.filterList.addAll(filterList);
        notifyDataSetChanged();
    }

    static class ItemViewFilter extends RecyclerView.ViewHolder {

        private LinearLayout layoutBorder;
        private LinearLayout layoutInside;
        private TextView filterName;

        public ItemViewFilter(View itemView) {
            super(itemView);
            layoutBorder = (LinearLayout) itemView.findViewById(R.id.layout_border);
            layoutInside = (LinearLayout) itemView.findViewById(R.id.layout_inside);
            filterName = (TextView) itemView.findViewById(R.id.filter_name);
        }
    }

    public interface FilterTokoCashListener {

        void clearFilter();

        void selectFilter(String typeFilter);
    }
}