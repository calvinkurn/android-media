package com.tokopedia.discovery.autocomplete.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.autocomplete.viewmodel.BaseItemAutoCompleteSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.CategorySearch;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;
import com.tokopedia.discovery.util.AutoCompleteTracking;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewHolder extends AbstractViewHolder<CategorySearch> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_category_autocomplete;

    private final ItemClickListener listener;
    private final RecyclerView recyclerView;
    private final ItemAdapter adapter;

    public CategoryViewHolder(View itemView, ItemClickListener clickListener) {
        super(itemView);
        this.listener = clickListener;
        recyclerView = itemView.findViewById(R.id.recyclerView);
        adapter = new ItemAdapter(itemView.getContext(), clickListener);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(CategorySearch element) {
        adapter.setData(element.getList());
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

        private final Context context;
        private final ItemClickListener listener;
        private List<BaseItemAutoCompleteSearch> data;

        public ItemAdapter(Context context, ItemClickListener clickListener) {
            this.context = context;
            this.listener = clickListener;
            this.data = new ArrayList<>();
        }

        public void setData(List<BaseItemAutoCompleteSearch> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public ItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemAdapter.ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_item_autocomplete, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemAdapter.ItemViewHolder holder, final int position) {
            final BaseItemAutoCompleteSearch rowModel = data.get(position);
            holder.title.setText(rowModel.getRecom());
            ImageHandler.loadImageThumbs(context, holder.icon, rowModel.getImageUrl());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AutoCompleteTracking.eventClickCategory(
                            itemView.getContext(),
                            String.format(
                                    "keyword: %s - cat: %s - cat id: %s - po: %s - applink: %s",
                                    rowModel.getSearchTerm(),
                                    rowModel.getRecom(),
                                    rowModel.getCategoryId(),
                                    String.valueOf(getAdapterPosition() + 1),
                                    rowModel.getApplink()
                            )
                    );
                    listener.onItemClicked(rowModel.getApplink(), rowModel.getUrl());
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            ImageView icon;
            TextView title;
            CardView container;

            public ItemViewHolder(View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.image);
                title = itemView.findViewById(R.id.title);
                container = itemView.findViewById(R.id.container);
            }
        }
    }
}
