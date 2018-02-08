package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandDataModel;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BrandsViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class BrandsViewHolder extends AbstractViewHolder<BrandsViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_brands;
    @BindView(R.id.title)
    TextView titleTxt;
    @BindView(R.id.list)
    RecyclerView recyclerView;

    private ItemAdapter adapter;
    private int spanCount = 3;
    private HomeCategoryListener listener;

    public BrandsViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
        adapter = new ItemAdapter(itemView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), spanCount,
                GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount,
                itemView.getResources().getDimensionPixelSize(R.dimen.home_card_page_margin), true));
    }

    @Override
    public void bind(BrandsViewModel element) {
        titleTxt.setText(element.getTitle());
        adapter.setData(element.getData());
    }

    @OnClick(R.id.see_more)
    void onSeeMore() {
        listener.onBrandsMoreClicked(getAdapterPosition());
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

        private final Context context;
        private List<BrandDataModel> data;

        public ItemAdapter(Context context) {
            this.context = context;
            this.data = new ArrayList<>();
        }

        public void setData(List<BrandDataModel> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_brands_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            Glide.with(context).load(data.get(position).getLogoUrl()).into(holder.image);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onBrandsItemClicked(data.get(position), getAdapterPosition(), position);
                }
            });
            if (data.get(position).getIsNew() == 1) {
                holder.newIndicator.setVisibility(View.VISIBLE);
            } else {
                holder.newIndicator.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.image)
            ImageView image;
            @BindView(R.id.new_indicator)
            TextView newIndicator;

            public ItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
