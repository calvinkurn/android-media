package com.tokopedia.discovery.autocomplete.adapter;

import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.autocomplete.viewmodel.BaseItemAutoCompleteSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.PopularSearch;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class RecentViewHolder extends AbstractViewHolder<PopularSearch> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_recentsearch_autocomplete;

    private final ItemClickListener listener;
    private final RecyclerView recyclerView;
    private final RecentViewHolder.ItemAdapter adapter;

    public RecentViewHolder(View itemView, ItemClickListener clickListener) {
        super(itemView);
        this.listener = clickListener;
        recyclerView = itemView.findViewById(R.id.recyclerView);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(itemView.getContext());
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        FlexboxItemDecoration itemDecoration = new FlexboxItemDecoration(itemView.getContext());
        itemDecoration.setOrientation(FlexboxItemDecoration.BOTH);
        itemDecoration.setDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.autocomplete_divider));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(flexboxLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new RecentViewHolder.ItemAdapter(clickListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(PopularSearch element) {
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
                    .inflate(R.layout.layout_popular_item_autocomplete, parent, false);
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
            TextView textView;

            public ItemViewHolder(View itemView, ItemClickListener clickListener) {
                super(itemView);
                this.clickListener = clickListener;
                textView = itemView.findViewById(R.id.autocomplete_chips_item);
            }

            public void bind(final BaseItemAutoCompleteSearch item) {
                textView.setText(item.getKeyword());
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListener.onItemClicked(item.getApplink(), item.getUrl());
                    }
                });
            }
        }
    }
}
