package com.tokopedia.search.result.presentation.view.adapter.viewholder.product;

import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.itemdecoration.LinearHorizontalSpacingDecoration;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.ListHelper;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.RelatedSearchViewModel;
import com.tokopedia.search.result.presentation.view.listener.RelatedSearchListener;

import java.util.ArrayList;
import java.util.List;

public class RelatedSearchViewHolder extends AbstractViewHolder<RelatedSearchViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.related_search_layout;

    RecyclerView recyclerView;
    RelatedSearchAdapter adapter;
    TextView relatedSearchTitle;

    public RelatedSearchViewHolder(View itemView, RelatedSearchListener relatedSearchListener) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.recyclerView);
        relatedSearchTitle = itemView.findViewById(R.id.relatedSearchTitle);
        adapter = new RelatedSearchAdapter(relatedSearchListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new LinearHorizontalSpacingDecoration(
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp_8),
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp_16)
        ));
    }

    @Override
    public void bind(RelatedSearchViewModel element) {
        if (ListHelper.isContainItems(element.getOtherRelated())) {
            recyclerView.setVisibility(View.VISIBLE);
            relatedSearchTitle.setVisibility(View.VISIBLE);
            adapter.setItemList(element.getOtherRelated());
        } else {
            recyclerView.setVisibility(View.GONE);
            relatedSearchTitle.setVisibility(View.GONE);
        }
    }

    public static class RelatedSearchAdapter extends RecyclerView.Adapter<ViewHolder> {

        List<RelatedSearchViewModel.OtherRelated> itemList = new ArrayList<>();
        RelatedSearchListener relatedSearchListener;

        public RelatedSearchAdapter(RelatedSearchListener relatedSearchListener) {
            this.relatedSearchListener = relatedSearchListener;
        }

        public void setItemList(List<RelatedSearchViewModel.OtherRelated> itemList) {
            this.itemList = itemList;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_search_item, parent, false);
            return new ViewHolder(view, relatedSearchListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(itemList.get(position));
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        RelatedSearchListener relatedSearchListener;

        public ViewHolder(View itemView, RelatedSearchListener relatedSearchListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.related_search_text);
            this.relatedSearchListener = relatedSearchListener;
        }

        public void bind(final RelatedSearchViewModel.OtherRelated item) {
            textView.setText(item.getKeyword());
            textView.setOnClickListener(view -> {
                Uri uri = Uri.parse(item.getUrl());
                String keyword = uri.getQueryParameter(SearchApiConst.Q);
                if (relatedSearchListener != null) {
                    relatedSearchListener.onRelatedSearchClicked(uri.getEncodedQuery(), keyword);
                }
            });
        }
    }
}

