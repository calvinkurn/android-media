package com.tokopedia.topads.sdk.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.interactor.OpenTopAdsUseCase;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.view.adapter.DynamicFeedShopAdapter;

import java.util.List;

/**
 * @author by milhamj on 07/01/19.
 */
public class TopAdsDynamicFeedShopView extends LinearLayout implements LocalAdsClickListener {

    private RecyclerView recommendationRv;
    private DynamicFeedShopAdapter adapter;
    private TopAdsItemClickListener itemClickListener;
    private OpenTopAdsUseCase openTopAdsUseCase;

    public TopAdsDynamicFeedShopView(Context context) {
        super(context);
        inflateView(context);
    }

    public TopAdsDynamicFeedShopView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateView(context);
    }

    public TopAdsDynamicFeedShopView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(context);
    }

    private void inflateView(Context context) {
        inflate(context, R.layout.layout_dynamic_feed_shop, this);
        openTopAdsUseCase = new OpenTopAdsUseCase(context);
        adapter = new DynamicFeedShopAdapter(this);

        recommendationRv = findViewById(R.id.recommendationRv);
        recommendationRv.setAdapter(adapter);
    }

    public void bind(List<Data> dataList) {
        adapter.setList(dataList);
    }

    public void notifyItemChanged(int position) {
        adapter.notifyItemChanged(position);
    }

    public void setItemClickListener(TopAdsItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void onViewRecycled() {
        if (adapter == null || recommendationRv == null) {
            return;
        }

        for (int i = 0; i < adapter.getItemCount(); i++) {
            RecyclerView.ViewHolder holder = recommendationRv.findViewHolderForAdapterPosition(i);
            if (holder instanceof DynamicFeedShopAdapter.DynamicFeedShopViewHolder) {
                adapter.onViewRecycled((DynamicFeedShopAdapter.DynamicFeedShopViewHolder) holder);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        openTopAdsUseCase.unsubscribe();
    }

    @Override
    public void onShopItemClicked(int position, Data data) {
        Shop shop = data.getShop();
        shop.setAdRefKey(data.getAdRefKey());
        shop.setAdId(data.getId());
        itemClickListener.onShopItemClicked(position, shop);
        openTopAdsUseCase.execute(data.getShopClickUrl());
    }

    @Override
    public void onProductItemClicked(int position, Data data) {

    }

    @Override
    public void onAddFavorite(int position, Data dataShop) {
        itemClickListener.onAddFavorite(position, dataShop);
    }

    @Override
    public void onAddWishLish(int position, Data data) {

    }
}
