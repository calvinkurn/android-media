package com.tokopedia.shop.product.view.adapter.newadapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.EmptyWrapViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ErrorNetworkWrapViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductEtalaseTitleViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductFeaturedViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductPromoViewHolder;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductEtalaseTitleViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductFeaturedViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductPromoViewModel;

public class ShopProductAdapterTypeFactory extends BaseAdapterTypeFactory {

    private final ShopProductPromoViewHolder.PromoViewHolderListener promoViewHolderListener;
    private final ShopProductClickedListener shopProductClickedListener;
    private final EmptyWrapViewHolder.Callback emptyProductOnClickListener;

    public ShopProductAdapterTypeFactory(ShopProductPromoViewHolder.PromoViewHolderListener promoViewHolderListener,
                                         ShopProductClickedListener shopProductClickedListener,
                                         EmptyWrapViewHolder.Callback emptyProductOnClickListener) {
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
        return EmptyWrapViewHolder.LAYOUT;
    }

    public int type(ShopProductEtalaseTitleViewModel shopProductLimitedEtalaseTitleViewHolder) {
        return ShopProductEtalaseTitleViewHolder.LAYOUT;
    }

    public int type(ShopProductPromoViewModel shopProductPromoViewModel) {
        return ShopProductPromoViewHolder.LAYOUT;
    }

    public int type(ShopProductFeaturedViewModel shopProductFeaturedViewModel) {
        return ShopProductFeaturedViewHolder.LAYOUT;
    }

    public int type(ShopProductViewModel shopProductViewModel) {
        return ShopProductViewHolder.LAYOUT;
    }

    public int type(ErrorNetworkModel errorNetworkModel) {
        return ErrorNetworkWrapViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == LoadingShimmeringGridViewHolder.LAYOUT) {
            return new LoadingShimmeringGridViewHolder(parent);
        } else if (type == EmptyWrapViewHolder.LAYOUT) {
            return new EmptyWrapViewHolder(parent, emptyProductOnClickListener);
        } else if (type == ErrorNetworkWrapViewHolder.LAYOUT){
            return new ErrorNetworkWrapViewHolder(parent);
        } else if (type == ShopProductEtalaseTitleViewHolder.LAYOUT) {
            return new ShopProductEtalaseTitleViewHolder(parent);
        } else if (type == ShopProductPromoViewHolder.LAYOUT) {
            return new ShopProductPromoViewHolder(parent, promoViewHolderListener);
        } else if(type == ShopProductFeaturedViewHolder .LAYOUT){
            return new ShopProductFeaturedViewHolder(parent);
        } else if(type == ShopProductViewHolder.LAYOUT){
            return new ShopProductViewHolder(parent, shopProductClickedListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}