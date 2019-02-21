package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.listener.HomeFeedListener;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TopAdsDynamicChannelModel;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.ProductImage;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.home.ProductDynamicChannelViewModel;
import com.tokopedia.topads.sdk.widget.TopAdsDynamicChannelView;

import java.util.ArrayList;
import java.util.List;

public class TopAdsDynamicChannelViewHolder extends AbstractViewHolder<TopAdsDynamicChannelModel> implements TopAdsItemClickListener {

    public static final int LAYOUT = R.layout.layout_item_dynamic_channel_ads;
    private final HomeFeedListener listener;
    private TopAdsDynamicChannelView topAdsDynamicChannelView;
    private Context context;

    public TopAdsDynamicChannelViewHolder(View itemView, HomeFeedListener listener) {
        super(itemView);
        this.context = itemView.getContext();
        this.listener = listener;
        topAdsDynamicChannelView = (TopAdsDynamicChannelView) itemView;
        topAdsDynamicChannelView.setAdsItemClickListener(this);
        topAdsDynamicChannelView.setAdsItemImpressionListener(new TopAdsItemImpressionListener() {
            @Override
            public void onImpressionProductAdsItem(int position, Product product) {
                TopAdsGtmTracker.eventHomeProductView(context, product, position);
            }
        });
    }

    @Override
    public void bind(TopAdsDynamicChannelModel element) {
        topAdsDynamicChannelView.setData(element.getTitle(), element.getItems());
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        listener.onGoToProductDetailFromInspiration(
                product.getId(),
                product.getImage().getM_ecs(),
                product.getName(),
                product.getPriceFormat()
        );
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) { }

    @Override
    public void onAddFavorite(int position, Data data) { }

}
