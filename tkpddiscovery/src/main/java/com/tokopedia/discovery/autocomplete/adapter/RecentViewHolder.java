package com.tokopedia.discovery.autocomplete.adapter;

import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.item.DeletableItemView;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.autocomplete.adapter.decorater.SpacingItemDecoration;
import com.tokopedia.discovery.autocomplete.viewmodel.BaseItemAutoCompleteSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.RecentSearch;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;
import com.tokopedia.discovery.util.AutoCompleteTracking;

import java.util.ArrayList;
import java.util.List;

public class RecentViewHolder extends AbstractViewHolder<RecentSearch> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_recentsearch_autocomplete;

    private final ItemClickListener clickListener;
    private final RecyclerView recyclerView;
    private final RecentViewHolder.ItemAdapter adapter;

    public RecentViewHolder(View itemView, ItemClickListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;
        recyclerView = itemView.findViewById(R.id.recyclerView);
        ChipsLayoutManager layoutManager = ChipsLayoutManager.newBuilder(itemView.getContext())
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build();
        int staticDimen8dp = itemView.getContext().getResources().getDimensionPixelOffset(R.dimen.dp_8);
        recyclerView.addItemDecoration(new SpacingItemDecoration(staticDimen8dp));
        recyclerView.setLayoutManager(layoutManager);
        ViewCompat.setLayoutDirection(recyclerView, ViewCompat.LAYOUT_DIRECTION_LTR);
        adapter = new RecentViewHolder.ItemAdapter(clickListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(RecentSearch element) {
        adapter.setData(element.getList());
    }

    private class ItemAdapter extends RecyclerView.Adapter<RecentViewHolder.ItemAdapter.ItemViewHolder> {

        private final ItemClickListener listener;
        private List<BaseItemAutoCompleteSearch> data;

        public ItemAdapter(ItemClickListener clickListener) {
            this.listener = clickListener;
            this.data = new ArrayList<>();
        }

        public void setData(List<BaseItemAutoCompleteSearch> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public RecentViewHolder.ItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_recent_item_autocomplete, parent, false);
            return new RecentViewHolder.ItemAdapter.ItemViewHolder(itemView, listener);
        }

        @Override
        public void onBindViewHolder(RecentViewHolder.ItemAdapter.ItemViewHolder holder, final int position) {
            holder.bind(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            private final ItemClickListener clickListener;
            DeletableItemView textView;

            public ItemViewHolder(View itemView, ItemClickListener clickListener) {
                super(itemView);
                this.clickListener = clickListener;
                textView = itemView.findViewById(R.id.autocomplete_chips_item);
            }

            public void bind(final BaseItemAutoCompleteSearch item) {
                textView.setItemName(item.getKeyword());
                textView.setOnDeleteListener(new DeletableItemView.OnDeleteListener() {
                    @Override
                    public void onDelete() {
                        clickListener.onDeleteRecentSearchItem(item.getKeyword());
                    }
                });
                textView.setOnTextClickListener(new DeletableItemView.OnTextClickListener() {
                    @Override
                    public void onClick() {
                        AutoCompleteTracking.eventClickRecentSearch(
                                itemView.getContext(),
                                String.format(
                                        "value: %s - po: %s - applink: %s",
                                        item.getKeyword(),
                                        String.valueOf(getAdapterPosition() + 1),
                                        item.getApplink()
                                )
                        );
                        clickListener.onItemSearchClicked(
                                item.getKeyword(),
                                item.getCategoryId()
                        );
                    }
                });
            }
        }
    }
}
