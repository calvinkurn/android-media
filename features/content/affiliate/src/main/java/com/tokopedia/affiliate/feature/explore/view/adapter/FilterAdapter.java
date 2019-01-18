package com.tokopedia.affiliate.feature.explore.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.FilterViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private List<FilterViewModel> currentSelectedFilter = new ArrayList<>();
    private Context context;
    private int layout;
    private static final int MAX_CHIP = 5;
    private int PADDING_DEFAULT;

    public FilterAdapter(Context context, List<FilterViewModel> filterList, OnFilterClickedListener onFilterClickedListener, int layout) {
        this.filterList = filterList;
        this.context = context;
        this.filterClickedListener = onFilterClickedListener;
        this.layout = layout;
        PADDING_DEFAULT = context.getResources().getDimensionPixelOffset(R.dimen.dp_16);
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
        holder.text.setText(filter.getName());
        ImageHandler.loadImageRounded2(context, holder.imageView, filter.getImage());
        holder.layer.setBackgroundColor(getLayerBackground(filter.isSelected()));
        holder.cardView.setPadding(getPadding(PADDING_DEFAULT), 0, getPadding(PADDING_DEFAULT), 0);
    }

    private int getPadding(int padding) {
        return (int)(padding *  context.getResources().getDisplayMetrics().density);
    }

    private void initViewListener(Holder holder, FilterViewModel filter) {
        holder.cardView.setOnClickListener(v -> {
            enableCurrentItem(filter);
            if (layout ==  R.layout.item_explore_filter && filter.isSelected()) {
                moveSelectedSingleItemToFront(filter);
            }
            filterClickedListener.onItemClicked(getOnlySelectedFilter());
        });
    }

    private int getLayerBackground(boolean isSelected) {
        return context.getResources().getColor(isSelected ?
                R.color.filter_background_active :
                R.color.filter_background_inactive);
    }
    private void enableCurrentItem(FilterViewModel filter) {
        for (FilterViewModel item: getAllFilterList()) {
            if (item.getName().equals(filter.getName())) {
                item.setSelected(!item.isSelected());
                processCurrentSelected(item);
                break;
            }
        }
        notifyDataSetChanged();
    }

    private void processCurrentSelected(FilterViewModel filter) {
        if (filter.isSelected() && !containsInCurrentSelected(filter)) {
            currentSelectedFilter.add(filter);
        } else
            removeItemFromSelected(filter);
    }

    private boolean containsInCurrentSelected(FilterViewModel filter) {
        for (FilterViewModel item : currentSelectedFilter) {
            if (item.getName().equals(filter.getName()))
                return true;
        }
        return false;
    }

    private void removeItemFromSelected(FilterViewModel filter) {
        List<FilterViewModel> newItemList = new ArrayList<>();
        for (FilterViewModel item : currentSelectedFilter) {
            if (!item.getName().equals(filter.getName())) {
                newItemList.add(item);
            }
        }
        currentSelectedFilter = newItemList;
    }

    public List<FilterViewModel> getAllFilterList() {
        return filterList;
    }

    @Override
    public int getItemCount() {
        if (layout ==  R.layout.item_explore_filter && filterList.size() > MAX_CHIP) return MAX_CHIP;
        return filterList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView text;
        private View layer;
        private CardView cardView;
        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.category);
            layer = itemView.findViewById(R.id.backgroundView);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    public void clearAllData() {
        filterList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addItem(List<FilterViewModel> filterList) {
        this.filterList = filterList;
        notifyDataSetChanged();
    }

    public List<FilterViewModel> getFilterListCurrentSelectedSorted() {
        List<FilterViewModel> sortedList = new ArrayList<>();
        sortedList.addAll(currentSelectedFilter);
        sortedList.addAll(addRemainingItemNotCurrentlySelected());
        return sortedList;
    }

    private List<FilterViewModel> addRemainingItemNotCurrentlySelected() {
        List<FilterViewModel> sortedList = new ArrayList<>();
        for (FilterViewModel item : getAllFilterList()) {
            if (!containsInCurrentSelected(item)) {
                sortedList.add(item);
            }
        }
        Collections.sort(sortedList, (item1, item2) -> Boolean.compare(item2.isSelected(), item1.isSelected()));
        return sortedList;
    }

    public List<FilterViewModel> getOnlySelectedFilter() {
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

    private void moveSelectedSingleItemToFront(FilterViewModel filter) {
        List<FilterViewModel> items = new ArrayList<>();
        items.add(filter);
        for (FilterViewModel item : getAllFilterList()) {
            if (!item.getName().equals(filter.getName())) {
                items.add(item);
            }
        }
        addItem(items);
        notifyDataSetChanged();
    }
}
