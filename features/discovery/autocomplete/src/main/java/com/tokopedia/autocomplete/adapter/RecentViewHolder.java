package com.tokopedia.autocomplete.adapter;

import androidx.annotation.LayoutRes;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.autocomplete.R;
import com.tokopedia.autocomplete.adapter.decorater.SpacingItemDecoration;
import com.tokopedia.autocomplete.analytics.AutocompleteTracking;
import com.tokopedia.autocomplete.viewmodel.BaseItemAutoCompleteSearch;
import com.tokopedia.autocomplete.viewmodel.RecentSearch;
import com.tokopedia.design.item.DeletableItemView;
import com.tokopedia.discovery.common.constants.SearchApiConst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecentViewHolder extends AbstractViewHolder<RecentSearch> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_recentsearch_autocomplete;

    private final ItemClickListener clickListener;
    private final RecyclerView recyclerView;
    private final ItemAdapter adapter;

    public RecentViewHolder(View itemView, ItemClickListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;
        recyclerView = itemView.findViewById(R.id.recyclerView);
        ChipsLayoutManager layoutManager = ChipsLayoutManager.newBuilder(itemView.getContext())
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build();
        int staticDimen8dp = itemView.getContext().getResources().getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_8);
        recyclerView.addItemDecoration(new SpacingItemDecoration(staticDimen8dp));
        recyclerView.setLayoutManager(layoutManager);
        ViewCompat.setLayoutDirection(recyclerView, ViewCompat.LAYOUT_DIRECTION_LTR);
        adapter = new ItemAdapter(clickListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(RecentSearch element) {
        adapter.setData(element.getList());
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

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
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_recent_item_autocomplete, parent, false);
            return new ItemViewHolder(itemView, listener);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
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
                        AutocompleteTracking.eventClickRecentSearch(
                                itemView.getContext(),
                                String.format(
                                        "value: %s - po: %s - applink: %s",
                                        item.getKeyword(),
                                        String.valueOf(getAdapterPosition() + 1),
                                        item.getApplink()
                                )
                        );
                        clickListener.onItemClicked(item.getApplink(), item.getUrl());
                    }
                });
            }

            private boolean getAutoCompleteItemIsOfficial(BaseItemAutoCompleteSearch autoCompleteSearch) {
                boolean isOfficial = false;

                HashMap<String, String> applinkParameterHashMap = autoCompleteSearch.getApplinkParameterHashmap();

                if(applinkParameterHashMap.containsKey(SearchApiConst.OFFICIAL)) {
                    isOfficial = Boolean.parseBoolean(applinkParameterHashMap.get(SearchApiConst.OFFICIAL));
                }

                return isOfficial;
            }
        }
    }
}
