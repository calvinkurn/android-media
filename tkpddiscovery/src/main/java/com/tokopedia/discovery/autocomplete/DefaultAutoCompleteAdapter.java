package com.tokopedia.discovery.autocomplete;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.search.domain.model.SearchData;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class DefaultAutoCompleteAdapter extends RecyclerView.Adapter<DefaultAutoCompleteAdapter.ViewHolder> {


    private List<SearchData> list;
    private ItemClickListener itemClickListener;

    public DefaultAutoCompleteAdapter(ItemClickListener itemClickListener) {
        this.list = new ArrayList<>();
        this.itemClickListener = itemClickListener;
    }

    @Override
    public DefaultAutoCompleteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_default_parent_autocomplete, parent, false);
        return new ViewHolder(itemLayoutView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(DefaultAutoCompleteAdapter.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setViewModel(DefaultAutoCompleteViewModel element) {
        this.list = element.getList();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @LayoutRes
        public static final int LAYOUT = R.layout.default_search_parent_list_item;
        private final ChipsLayoutAdapter resultAdapter;

        CardView cardView;
        RecyclerView recyclerView;
        LinearLayout titleContainer;
        TextView title;
        TextView delete;

        private final Context context;

        public ViewHolder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            context = itemView.getContext();
            cardView = itemView.findViewById(R.id.card_view);
            recyclerView = itemView.findViewById(R.id.list);
            titleContainer = itemView.findViewById(R.id.container_search);
            title = itemView.findViewById(R.id.title);
            delete = itemView.findViewById(R.id.delete);
            FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(context);
            flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
            flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
            FlexboxItemDecoration itemDecoration = new FlexboxItemDecoration(context);
            itemDecoration.setOrientation(FlexboxItemDecoration.BOTH);
            itemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.autocomplete_divider));
            recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setLayoutManager(flexboxLayoutManager);
            recyclerView.setHasFixedSize(true);
            resultAdapter = new ChipsLayoutAdapter(itemClickListener);
            recyclerView.setAdapter(resultAdapter);
        }

        public void bind(SearchData element) {
            title.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            CardView.LayoutParams layoutParams = new CardView.LayoutParams(
                    CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
            cardView.setCardElevation(0);
            switch (element.getId()) {
                case "recent_search":
                    title.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                    title.setText(element.getName());
                    layoutParams.setMargins(0, 0, 0, context.getResources().
                            getDimensionPixelSize(R.dimen.search_parent_item_card_margin));
                    break;
                case "popular_search":
                    title.setVisibility(View.VISIBLE);
                    title.setText(element.getName());
                    layoutParams.setMargins(0, 0, 0, context.getResources().
                            getDimensionPixelSize(R.dimen.search_parent_item_card_margin));
                    break;
            }
            cardView.setLayoutParams(layoutParams);
            resultAdapter.setModel(element);
        }
    }
}
