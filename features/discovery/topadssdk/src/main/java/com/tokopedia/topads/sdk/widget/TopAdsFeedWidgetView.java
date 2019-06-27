package com.tokopedia.topads.sdk.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.data.ModelConverter;
import com.tokopedia.topads.sdk.domain.interactor.OpenTopAdsUseCase;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsInfoClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.view.adapter.FeedAdsItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 29/03/18.
 */

public class TopAdsFeedWidgetView extends LinearLayout implements LocalAdsClickListener {

    private static final String TAG = TopAdsFeedWidgetView.class.getSimpleName();
    private static final int DEFAULT_SPAN_COUNT = 3;
    private FeedAdsItemAdapter adapter;
    private TopAdsItemClickListener itemClickListener;
    private TopAdsInfoClickListener infoClickListener;
    private OpenTopAdsUseCase openTopAdsUseCase;
    private GridLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private View promotedLayout;
    private View.OnClickListener onInfoClickListener;

    public TopAdsFeedWidgetView(Context context) {
        super(context);
        inflateView(context, null, 0);
    }

    public TopAdsFeedWidgetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateView(context, attrs, 0);
    }

    public TopAdsFeedWidgetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(context, attrs, defStyleAttr);
    }

    private void inflateView(Context context, AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.layout_ads_feed, this);
        openTopAdsUseCase = new OpenTopAdsUseCase(context);
        adapter = new FeedAdsItemAdapter(getContext());
        adapter.setItemClickListener(this);
        layoutManager = new GridLayoutManager(getContext(),
                DEFAULT_SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false);
        recyclerView = findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        promotedLayout = findViewById(R.id.promoted_layout);
    }

    public void setData(List<Data> data) {
        List<Item> visitables = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Data d = data.get(i);
            if (d.getProduct() != null && !TextUtils.isEmpty(d.getProduct().getId())) {
                layoutManager.setSpanCount(data.size() == 4 ? 2 : 3);
                visitables.add(ModelConverter.convertToProductFeedNewViewModel(d));

                promotedLayout.setVisibility(View.VISIBLE);
                promotedLayout.setOnClickListener(getOnInfoClickListener());
                int leftMargin = (int) getContext().getResources().getDimension(R.dimen.dp_6);
                setRecyclerViewLeftMargin(leftMargin);
            } else if (d.getShop() != null && !TextUtils.isEmpty(d.getShop().getId())) {
                layoutManager.setSpanCount(1);
                visitables.add(ModelConverter.convertToShopFeedNewViewModel(d));

                promotedLayout.setVisibility(View.GONE);
                setRecyclerViewLeftMargin(0);
            }
        }
        adapter.setList(visitables);
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
        Product product = data.getProduct();
        product.setAdRefKey(data.getAdRefKey());
        product.setAdId(data.getId());
        itemClickListener.onProductItemClicked(position, product);
        openTopAdsUseCase.execute(data.getProductClickUrl());
    }

    @Override
    public void onAddFavorite(int position, Data dataShop) {
        itemClickListener.onAddFavorite(position, dataShop);
    }

    @Override
    public void onAddWishLish(int position, Data data) {

    }

    public void setItemClickListener(TopAdsItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setInfoClickListener(TopAdsInfoClickListener infoClickListener) {
        this.infoClickListener = infoClickListener;
    }

    public void notifyDataChange() {
        adapter.notifyDataSetChanged();
    }

    public void setAdapterPosition(int adapterPosition) {
        adapter.setAdapterPosition(adapterPosition);
    }

    public void onViewRecycled() {
        if (adapter == null || recyclerView == null) {
            return;
        }
        for (int i = 0; i < adapter.getItemCount(); i++) {
            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(i);
            if (holder instanceof AbstractViewHolder) {
                adapter.onViewRecycled((AbstractViewHolder) holder);
            }
        }
    }

    private void setRecyclerViewLeftMargin(int margin) {
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        ViewGroup.MarginLayoutParams layoutParams =
                                (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
                        layoutParams.setMargins(
                                margin,
                                0,
                                0,
                                0
                        );
                        recyclerView.requestLayout();
                    }
                }
        );
    }

    private View.OnClickListener getOnInfoClickListener() {
        if (onInfoClickListener == null) {
            onInfoClickListener = v -> infoClickListener.onInfoClicked();
        }
        return onInfoClickListener;
    }
}
