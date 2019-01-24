package com.tokopedia.topads.sdk.view.adapter.factory;

import android.content.Context;
import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.adapter.exception.TypeNotSupportedException;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.view.adapter.viewholder.discovery.ProductCarouselListViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.discovery.ProductGridViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.discovery.ProductBigViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.discovery.ProductListViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.discovery.ShopGridViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.discovery.ShopListViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.feed.ProductFeedViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.feed.ShopFeedViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.home.DynamicChannelViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ProductBigViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ProductCarouselListViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ProductGridViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ProductListViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ShopGridViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ShopListViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feed.ProductFeedViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feed.ShopFeedViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.home.ProductDynamicChannelViewModel;

/**
 * @author by errysuprayogi on 3/29/17.
 */

public class AdsAdapterTypeFactory implements AdsTypeFactory {

    private int clickPosition;
    private LocalAdsClickListener itemClickListener;
    private ImageLoader imageLoader;
    private TopAdsItemImpressionListener itemImpressionListener;
    private boolean enableWishlist;
    private int offset;

    public AdsAdapterTypeFactory(Context context) {
        this(context, -1);
    }

    public AdsAdapterTypeFactory(Context context, int clickPosition) {
        imageLoader = new ImageLoader(context);
        this.clickPosition = clickPosition;
    }

    public void setItemClickListener(LocalAdsClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setEnableWishlist(boolean enableWishlist) {
        this.enableWishlist = enableWishlist;
    }

    public void setClickPosition(int adapterPosition) {
        clickPosition = adapterPosition;
    }

    @Override
    public int type(ProductGridViewModel viewModel) {
        return ProductGridViewHolder.LAYOUT;
    }

    @Override
    public int type(ProductListViewModel viewModel) {
        return ProductListViewHolder.LAYOUT;
    }

    @Override
    public int type(ShopGridViewModel viewModel) {
        return ShopGridViewHolder.LAYOUT;
    }

    @Override
    public int type(ShopListViewModel viewModel) {
        return ShopListViewHolder.LAYOUT;
    }

    @Override
    public int type(ShopFeedViewModel viewModel) {
        return ShopFeedViewHolder.LAYOUT;
    }

    @Override
    public int type(ProductFeedViewModel viewModel) {
        return ProductFeedViewHolder.LAYOUT;
    }

    @Override
    public int type(ProductBigViewModel viewModel) {
        return ProductBigViewHolder.LAYOUT;
    }

    @Override
    public int type(ProductCarouselListViewModel viewModel) {
        return ProductCarouselListViewHolder.LAYOUT;
    }

    @Override
    public int type(ProductDynamicChannelViewModel productDynamicChannelViewModel) {
        return DynamicChannelViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(ViewGroup view, int viewType) {
        AbstractViewHolder holder;
        if (viewType == ProductGridViewHolder.LAYOUT) {
            holder = new ProductGridViewHolder(view, imageLoader, itemClickListener, clickPosition, enableWishlist);
        } else if (viewType == ProductListViewHolder.LAYOUT) {
            holder = new ProductListViewHolder(view, imageLoader, itemClickListener, clickPosition, enableWishlist);
        } else if (viewType == ProductBigViewHolder.LAYOUT) {
            holder = new ProductBigViewHolder(view, imageLoader, itemClickListener, clickPosition, enableWishlist);
        } else if (viewType == ShopGridViewHolder.LAYOUT) {
            holder = new ShopGridViewHolder(view, imageLoader, itemClickListener);
        } else if (viewType == ShopListViewHolder.LAYOUT) {
            holder = new ShopListViewHolder(view, imageLoader, itemClickListener);
        } else if (viewType == ShopFeedViewHolder.LAYOUT) {
            holder = new ShopFeedViewHolder(view, imageLoader, itemClickListener);
        } else if (viewType == DynamicChannelViewHolder.LAYOUT) {
            holder = new DynamicChannelViewHolder(view, itemClickListener, itemImpressionListener);
        } else if (viewType == ProductFeedViewHolder.LAYOUT) {
            holder = new ProductFeedViewHolder(view, itemClickListener);
        } else if (viewType == ProductCarouselListViewHolder.LAYOUT) {
            holder = new ProductCarouselListViewHolder(view, itemClickListener, clickPosition,
                    itemImpressionListener, enableWishlist, offset);
        } else {
            throw TypeNotSupportedException.create("Layout not supported");
        }
        return holder;
    }

    public void setItemImpressionListener(TopAdsItemImpressionListener itemImpressionListener) {
        this.itemImpressionListener = itemImpressionListener;
    }
}
