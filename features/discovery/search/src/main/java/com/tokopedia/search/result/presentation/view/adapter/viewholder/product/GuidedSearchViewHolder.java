package com.tokopedia.search.result.presentation.view.adapter.viewholder.product;

import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.GuidedSearchViewModel;
import com.tokopedia.search.result.presentation.view.listener.GuidedSearchListener;
import com.tokopedia.search.result.presentation.view.listener.ProductListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 14/02/18.
 */

public class GuidedSearchViewHolder extends AbstractViewHolder<GuidedSearchViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.guided_search_layout;

    RecyclerView recyclerView;
    GuidedSearchAdapter adapter;

    public GuidedSearchViewHolder(View itemView, GuidedSearchListener guidedSearchListener) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.recyclerView);
        adapter = new GuidedSearchAdapter(guidedSearchListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(GuidedSearchViewModel element) {
        adapter.setItemList(element.getItemList());
    }

    public static class GuidedSearchAdapter extends RecyclerView.Adapter<ViewHolder> {

        List<GuidedSearchViewModel.Item> itemList = new ArrayList<>();
        GuidedSearchListener guidedSearchListener;

        GuidedSearchAdapter(GuidedSearchListener guidedSearchListener) {
            this.guidedSearchListener = guidedSearchListener;
        }

        void setItemList(List<GuidedSearchViewModel.Item> itemList) {
            this.itemList = itemList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guided_search_item, parent, false);
            return new ViewHolder(view, guidedSearchListener);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(itemList.get(position));
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        GuidedSearchListener guidedSearchListener;

        public ViewHolder(View itemView, GuidedSearchListener guidedSearchListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.guided_search_text);
            this.guidedSearchListener = guidedSearchListener;
        }

        public void bind(final GuidedSearchViewModel.Item item) {
            textView.setText(item.getKeyword());
            textView.setOnClickListener(view -> {
                SearchTracking.eventClickGuidedSearch(textView.getContext(), item.getPreviousKey(), item.getCurrentPage(), item.getKeyword());
                guidedSearchListener.onSearchGuideClicked(Uri.parse(item.getUrl()).getEncodedQuery());
            });
        }
    }
}
