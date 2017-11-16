package com.tokopedia.digital.tokocash.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.tokocash.model.HeaderColor;
import com.tokopedia.digital.tokocash.model.HeaderHistory;
import com.tokopedia.digital.tokocash.model.HeaderItemColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class FilterTokoCashAdapter extends RecyclerView.Adapter {

    private static final String ALL_TRANSACTION_TYPE = "all";

    private List<HeaderItemColor> headerItemColorList;
    private List<HeaderItemColor> headerHistoryList;
    private List<HeaderColor> headerColorList;
    private FilterTokoCashListener listener;
    private Random randomGenerator;
    private Context context;
    private HeaderItemColor lastHeaderItemColor;
    private int pos;
    private ItemViewFilter itemViewFilter;
    private HeaderItemColor headerHistory;


    public FilterTokoCashAdapter() {
        this.headerHistoryList = new ArrayList<>();
        headerItemColorList = new ArrayList<>();
        randomGenerator = new Random();
        addColorFilter();
    }

    public void setListener(FilterTokoCashListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tokocash_filter_history, parent, false);
        return new ItemViewFilter(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        itemViewFilter = (ItemViewFilter) holder;
        headerHistory = headerHistoryList.get(position);

        itemViewFilter.filterName.setText(headerHistory.getHeaderHistory().getName());
        handleViewFilter(itemViewFilter, headerHistory.getHeaderHistory().isSelected(), headerHistory.getHeaderColor());
        itemViewFilter.layoutFilterTokocash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (headerHistory.getHeaderHistory().isSelected()) {
                    listener.clearFilter();
                    headerHistory.getHeaderHistory().setSelected(false);
                } else {
                    listener.selectFilter(headerHistory.getHeaderHistory().getType());
                    headerHistory.getHeaderHistory().setSelected(true);
                    if (lastHeaderItemColor != null) {
                        headerItemColorList.get(pos).getHeaderHistory().setSelected(false);
                        notifyItemChanged(pos);
                    }
                    lastHeaderItemColor = headerHistory;
                    pos = position;
                }
                handleViewFilter(itemViewFilter, headerHistory.getHeaderHistory().isSelected(), headerHistory.getHeaderColor());
            }
        });
    }

    private void handleViewFilter(ItemViewFilter itemViewFilter, boolean selected, HeaderColor headerColor) {
        itemViewFilter.layoutFilterTokocash.setBackground(ContextCompat.getDrawable(context, selected ?
                headerColor.getColor() : headerColor.getBackground()));
        itemViewFilter.filterName.setTextColor(ContextCompat.getColor(context, selected ? R.color.white :
                R.color.black_70));
        itemViewFilter.clearFilter.setVisibility(selected ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return headerHistoryList.size();
    }

    public void addFilterTokoCashList(List<HeaderHistory> headerHistoryList) {
        headerItemColorList.clear();
        for (int i = headerHistoryList.size() - 1; i > -1; i--) {
            if (headerHistoryList.get(i).getType().equals(ALL_TRANSACTION_TYPE)) {
                headerHistoryList.remove(headerHistoryList.get(i));
            } else {
                int index = randomGenerator.nextInt(headerColorList.size());
                headerItemColorList.add(new HeaderItemColor(headerHistoryList.get(i),
                        headerColorList.get(index)));
            }
        }
        this.headerHistoryList.clear();
        this.headerHistoryList.addAll(headerItemColorList);
        notifyDataSetChanged();
    }

    private void addColorFilter() {
        headerColorList = new ArrayList<>();
        headerColorList.add(new HeaderColor(R.color.filter_inside_blue, R.drawable.digital_white_filter_blue));
        headerColorList.add(new HeaderColor(R.color.filter_inside_green, R.drawable.digital_white_filter_green));
        headerColorList.add(new HeaderColor(R.color.filter_inside_orange, R.drawable.digital_white_filter_orange));
        headerColorList.add(new HeaderColor(R.color.filter_inside_green_medium, R.drawable.digital_white_filter_green_medium));
    }

    static class ItemViewFilter extends RecyclerView.ViewHolder {

        @BindView(R2.id.layout_filter_tokocash)
        LinearLayout layoutFilterTokocash;
        @BindView(R2.id.filter_name)
        TextView filterName;
        @BindView(R2.id.clear_filter)
        ImageView clearFilter;

        public ItemViewFilter(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface FilterTokoCashListener {

        void clearFilter();

        void selectFilter(String typeFilter);
    }
}