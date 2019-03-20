package com.tokopedia.affiliate.feature.explore.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.FilterViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by yfsx on 07/01/19.
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.Holder> {

    public interface OnFilterClickedListener {
        void onItemClicked(List<FilterViewModel> filters);
    }

    private OnFilterClickedListener filterClickedListener;

    private List<FilterViewModel> filterList = new ArrayList<>();
    private int layout;
    private static final int MAX_CHIP = 5;

    public FilterAdapter(OnFilterClickedListener onFilterClickedListener, int layout) {
        this.filterClickedListener = onFilterClickedListener;
        this.layout = layout;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        FilterViewModel filter = filterList.get(position);
        initView(holder, filter);
        initViewListener(holder, filter);
    }

    private void initView(Holder holder, FilterViewModel filter) {

//        holder.chip.setIconUrl(filter.getImage());
//        holder.chip.setTitle(filter.getName());
        holder.itemView.setSelected(filter.isSelected());
    }

    private void initViewListener(Holder holder, FilterViewModel filter) {
        holder.itemView.setOnClickListener(v -> {
            boolean isSelected = filterList.get(holder.getAdapterPosition()).isSelected();
            filterList.get(holder.getAdapterPosition()).setSelected(!isSelected);
            notifyItemChanged(holder.getAdapterPosition());
            filterClickedListener.onItemClicked(getOnlySelectedFilter());
        });
    }

    private List<FilterViewModel> getAllFilterList() {
        return filterList;
    }

    @Override
    public int getItemCount() {
        if (layout == R.layout.item_explore_filter_child && filterList.size() > MAX_CHIP)
            return MAX_CHIP;
        return filterList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView title;

        Holder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
        }
    }

    public void clearAllData() {
        filterList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setList(List<FilterViewModel> filterList) {
        this.filterList = filterList;
        notifyDataSetChanged();
    }

    public List<FilterViewModel> getFilterListCurrentSelectedSorted() {
        List<FilterViewModel> sortedList = new ArrayList<>();
        sortedList.addAll(addSelectedItems());
        sortedList.addAll(addUnselectedItems());
        return sortedList;
    }

    private List<FilterViewModel> addSelectedItems() {
        List<FilterViewModel> sortedList = new ArrayList<>();
        for (FilterViewModel item : getAllFilterList()) {
            if (item.isSelected()) {
                sortedList.add(item);
            }
        }
        return sortedList;
    }

    private List<FilterViewModel> addUnselectedItems() {
        List<FilterViewModel> sortedList = new ArrayList<>();
        for (FilterViewModel item : getAllFilterList()) {
            if (!item.isSelected()) {
                sortedList.add(item);
            }
        }
        return sortedList;
    }

    private List<FilterViewModel> getOnlySelectedFilter() {
        List<FilterViewModel> items = new ArrayList<>();
        for (FilterViewModel filter : getAllFilterList()) {
            if (filter.isSelected()) {
                items.add(filter);
            }
        }
        return items;
    }

    public void resetAllFilters() {
        for (FilterViewModel item : getAllFilterList()) {
            item.setSelected(false);
        }
        notifyDataSetChanged();
    }

    private List<FilterViewModel> getNotSelectedItems() {
        List<FilterViewModel> items = new ArrayList<>();
        for (FilterViewModel item : getAllFilterList()) {
            if (!item.isSelected()) {
                items.add(item);
            }
        }
        return items;
    }
}
