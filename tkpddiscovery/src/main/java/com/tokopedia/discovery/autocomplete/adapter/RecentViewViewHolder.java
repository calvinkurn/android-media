package com.tokopedia.discovery.autocomplete.adapter;

import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.autocomplete.adapter.decorater.SpacingItemDecoration;
import com.tokopedia.discovery.autocomplete.viewmodel.BaseItemAutoCompleteSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.PopularSearch;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;
import com.tokopedia.discovery.util.AutoCompleteTracking;

import java.util.ArrayList;
import java.util.List;

public class RecentViewViewHolder extends AbstractViewHolder<PopularSearch> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_recent_view_autocomplete;

    private final ItemClickListener listener;
    private final RecyclerView recyclerView;
    private final RecentViewViewHolder.ItemAdapter adapter;

    public RecentViewViewHolder(View itemView, ItemClickListener clickListener) {
        super(itemView);
        this.listener = clickListener;
        recyclerView = itemView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        int staticDimen8dp = itemView.getContext().getResources().getDimensionPixelOffset(R.dimen.dp_8);
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

    private class ItemAdapter extends RecyclerView.Adapter<RecentViewViewHolder.ItemAdapter.ItemViewHolder> {

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
        public RecentViewViewHolder.ItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_recent_view_item_autocomplete, parent, false);
            return new RecentViewViewHolder.ItemAdapter.ItemViewHolder(itemView, clickListener);
        }

        @Override
        public void onBindViewHolder(RecentViewViewHolder.ItemAdapter.ItemViewHolder holder, final int position) {
            holder.bind(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            private final ItemClickListener clickListener;
            ImageView recentImage;

            public ItemViewHolder(View itemView, ItemClickListener clickListener) {
                super(itemView);
                this.clickListener = clickListener;
                recentImage = itemView.findViewById(R.id.autocomplete_recent_view_item);
            }

            public void bind(final BaseItemAutoCompleteSearch item) {
                BitmapImageViewTarget bitmapImageViewTarget = new BitmapImageViewTarget(
                        recentImage
                ){
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(
                                        itemView.getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        recentImage.setImageDrawable(circularBitmapDrawable);
                    }
                };
                Glide.with(itemView.getContext())
                        .load(item.getImageUrl())
                        .asBitmap()
                        .into(bitmapImageViewTarget);
//                recentImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        AutoCompleteTracking.eventClickPopularSearch(
//                                itemView.getContext(),
//                                String.format(
//                                        "value: %s - po: %s - applink: %s",
//                                        item.getKeyword(),
//                                        String.valueOf(getAdapterPosition() + 1),
//                                        item.getApplink()
//                                )
//                        );
//                        clickListener.onItemSearchClicked(
//                                item.getKeyword(),
//                                item.getCategoryId()
//                        );
//                    }
//                });
            }
        }
    }
}
