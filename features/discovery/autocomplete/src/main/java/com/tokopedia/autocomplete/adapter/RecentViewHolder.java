package com.tokopedia.autocomplete.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.autocomplete.R;
import com.tokopedia.autocomplete.analytics.AutocompleteTracking;
import com.tokopedia.autocomplete.viewmodel.BaseItemAutoCompleteSearch;
import com.tokopedia.autocomplete.viewmodel.RecentSearch;
import com.tokopedia.unifyprinciples.Typography;

import java.util.ArrayList;
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
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
            ConstraintLayout recentSearch;
            Typography text;
            ImageView removeButton;

            public ItemViewHolder(View itemView, ItemClickListener clickListener) {
                super(itemView);
                this.clickListener = clickListener;
                recentSearch = itemView.findViewById(R.id.autocompleteRecentSearchItem);
                text = itemView.findViewById(R.id.recentSearchTextView);
                removeButton = itemView.findViewById(R.id.actionRemoveButton);
            }

            public void bind(final BaseItemAutoCompleteSearch item) {
                text.setText(item.getKeyword());
                removeButton.setOnClickListener(v -> clickListener.onDeleteRecentSearchItem(item.getKeyword()));
                recentSearch.setOnClickListener(v -> {
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
                });
            }
        }
    }
}
