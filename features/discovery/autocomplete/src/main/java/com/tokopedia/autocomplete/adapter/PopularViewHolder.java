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
import com.tokopedia.autocomplete.viewmodel.PopularSearch;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.unifycomponents.ChipsUnify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PopularViewHolder extends AbstractViewHolder<PopularSearch> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_popular_autocomplete;

    private final ItemClickListener listener;
    private final RecyclerView recyclerView;
    private final ItemAdapter adapter;

    public PopularViewHolder(View itemView, ItemClickListener clickListener) {
        super(itemView);
        this.listener = clickListener;
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
    public void bind(PopularSearch element) {
        adapter.setData(element.getList());
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

        private final ItemClickListener clickListener;
        private List<BaseItemAutoCompleteSearch> data;

        public ItemAdapter(ItemClickListener clickListener) {
            this.clickListener = clickListener;
            this.data = new ArrayList<>();
        }

        public void setData(List<BaseItemAutoCompleteSearch> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_popular_item_autocomplete, parent, false);
            return new ItemViewHolder(itemView, clickListener);
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
            ChipsUnify chipsUnify;

            public ItemViewHolder(View itemView, ItemClickListener clickListener) {
                super(itemView);
                this.clickListener = clickListener;
                chipsUnify = itemView.findViewById(R.id.autocompleteRecentSearchItem);
            }

            public void bind(final BaseItemAutoCompleteSearch item) {
                chipsUnify.getChip_text().setText(item.getKeyword());
                chipsUnify.setOnClickListener(view -> {
                    AutocompleteTracking.eventClickPopularSearch(
                            itemView.getContext(),
                            String.format(
                                    "value: %s - po: %s - applink: %s",
                                    item.getKeyword(),
                                    String.valueOf(getAdapterPosition() + 1),
                                    item.getApplink()
                            )
                    );
                    clickListener.onItemClicked(item.getApplink(), item.getUrl());
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
