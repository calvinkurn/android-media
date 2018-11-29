package com.tokopedia.checkout.view.feature.cartlist.viewholder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.XcartParam;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.widget.TopAdsCarouselView;

import java.util.List;

import static com.tokopedia.topads.sdk.domain.TopAdsParams.DEFAULT_KEY_EP;

public class CartTopAdsViewHolder extends RecyclerView.ViewHolder implements TopAdsItemClickListener {

    public static final int TYPE_VIEW_CART_TOPADS = R.layout.layout_cart_topads;

    private TopAdsCarouselView topAdsCarouselView;
    private Context context;

    public CartTopAdsViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        topAdsCarouselView = itemView.findViewById(R.id.topads);

    }

    public void renderTopAds(TopAdsModel adsModel) {
        topAdsCarouselView.setAdsItemClickListener(this);
        topAdsCarouselView.setData(adsModel);
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        ProductItem data = new ProductItem();
        data.setId(product.getId());
        data.setName(product.getName());
        data.setPrice(product.getPriceFormat());
        data.setImgUri(product.getImage().getM_ecs());
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(context);
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) { }

    @Override
    public void onAddFavorite(int position, Data data) { }

    @Override
    public void onAddWishList(int position, Data data) { }

}
