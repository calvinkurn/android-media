package com.tokopedia.digital.tokocash.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.tokocash.model.HeaderHistory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class FilterTokoCashAdapter extends RecyclerView.Adapter {

    private List<HeaderHistory> headerHistoryList;
    private Context context;
    private FilterTokoCashListener listener;

    public FilterTokoCashAdapter(List<HeaderHistory> headerHistoryList) {
        this.headerHistoryList = headerHistoryList;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewFilter) holder).bindView(headerHistoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return headerHistoryList.size();
    }

    public void addFilterTokoCashList(List<HeaderHistory> headerHistoryList) {
        this.headerHistoryList.clear();
        this.headerHistoryList.addAll(headerHistoryList);
        notifyDataSetChanged();
    }

    class ItemViewFilter extends RecyclerView.ViewHolder {

        @BindView(R2.id.layout_filter_tokocash)
        RelativeLayout layoutFilterTokocash;
        @BindView(R2.id.filter_name)
        TextView filterName;
        @BindView(R2.id.clear_filter)
        ImageView clearFilter;

        public ItemViewFilter(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindView(final HeaderHistory headerHistory) {
            filterName.setText(headerHistory.getName());
            handleViewFilter(headerHistory.isSelected());
            layoutFilterTokocash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (headerHistory.isSelected()) {
                        listener.clearFilter();
                        headerHistory.setSelected(false);
                    } else {
                        listener.selectFilter(headerHistory.getType());
                        headerHistory.setSelected(true);
                    }
                    handleViewFilter(headerHistory.isSelected());
                }
            });
        }

        private void handleViewFilter(boolean selected) {
            layoutFilterTokocash.setBackground(ContextCompat.getDrawable(context, selected ?
                    R.color.filter_tokocash_selected : R.drawable.digital_white_grey_button_more_rounded));
            filterName.setTextColor(ContextCompat.getColor(context, selected ? R.color.white :
                    R.color.black_70));
            clearFilter.setVisibility(selected ? View.VISIBLE : View.GONE);
        }
    }

    public interface FilterTokoCashListener {

        void clearFilter();

        void selectFilter(String typeFilter);
    }
}