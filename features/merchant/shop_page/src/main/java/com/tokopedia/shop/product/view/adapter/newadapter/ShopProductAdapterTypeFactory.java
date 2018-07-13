package com.tokopedia.shop.product.view.adapter.newadapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductEtalaseTitleViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductFeaturedViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductWebViewHolder;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductEtalaseTitleViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductFeaturedViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductPromoViewModel;

public class ShopProductAdapterTypeFactory extends BaseAdapterTypeFactory {

    private final ShopProductWebViewHolder.PromoViewHolderListener promoViewHolderListener;
    private final ShopProductClickedListener shopProductClickedListener;
    private final EmptyViewHolder.Callback emptyProductOnClickListener;

    public ShopProductAdapterTypeFactory(ShopProductWebViewHolder.PromoViewHolderListener promoViewHolderListener,
                                         ShopProductClickedListener shopProductClickedListener,
                                         EmptyViewHolder.Callback emptyProductOnClickListener) {
        this.promoViewHolderListener = promoViewHolderListener;
        this.shopProductClickedListener = shopProductClickedListener;
        this.emptyProductOnClickListener = emptyProductOnClickListener;
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingShimmeringGridViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyModel viewModel) {
        return EmptyViewHolder.LAYOUT;
    }

    public int type(ShopProductEtalaseTitleViewModel shopProductLimitedEtalaseTitleViewHolder) {
        return ShopProductEtalaseTitleViewHolder.LAYOUT;
    }

    public int type(ShopProductPromoViewModel shopProductPromoViewModel) {
        return ShopProductWebViewHolder.LAYOUT;
    }

    public int type(ShopProductFeaturedViewModel shopProductFeaturedViewModel) {
        return ShopProductFeaturedViewHolder.LAYOUT;
    }

    public int type(ShopProductViewModel shopProductViewModel) {
        return ShopProductViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == LoadingShimmeringGridViewHolder.LAYOUT) {
            return new LoadingShimmeringGridViewHolder(parent);
        } else if (type == EmptyViewHolder.LAYOUT) {
            return new EmptyViewHolder(parent, emptyProductOnClickListener);
        } else if (type == ShopProductEtalaseTitleViewHolder.LAYOUT) {
            return new ShopProductEtalaseTitleViewHolder(parent);
        } else if (type == ShopProductWebViewHolder.LAYOUT) {
            return new ShopProductWebViewHolder(parent, promoViewHolderListener);
        } else if(type == ShopProductFeaturedViewHolder .LAYOUT){
            return new ShopProductFeaturedViewHolder(parent);
        } else if(type == ShopProductViewHolder.LAYOUT){
            return new ShopProductViewHolder(parent, shopProductClickedListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}