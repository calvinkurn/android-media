package com.tokopedia.shop.product.view.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.EmptyWrapViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ErrorNetworkWrapViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.HideViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseListViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseTitleViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductFeaturedViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductPromoViewHolder;
import com.tokopedia.shop.product.view.listener.ShopProductClickedNewListener;
import com.tokopedia.shop.product.view.model.HideViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseListViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseTitleViewModel;
import com.tokopedia.shop.product.view.model.ShopProductFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.shop.product.view.model.ShopProductPromoViewModel;

public class ShopProductAdapterTypeFactory extends BaseAdapterTypeFactory {

    private final ShopProductPromoViewHolder.PromoViewHolderListener promoViewHolderListener;
    private final ShopProductClickedNewListener shopProductClickedListener;
    private final EmptyWrapViewHolder.Callback emptyProductOnClickListener;
    private final ShopProductEtalaseListViewHolder.OnShopProductEtalaseListViewHolderListener onShopProductEtalaseListViewHolderListener;
    private final boolean isHorizontalLayout;
    private final boolean isFeaturedOnly;

    private OnShopProductAdapterTypeFactoryListener onShopProductAdapterTypeFactoryListener;
    public interface OnShopProductAdapterTypeFactoryListener{
        boolean needToShowEtalase();
    }

    public ShopProductAdapterTypeFactory(ShopProductPromoViewHolder.PromoViewHolderListener promoViewHolderListener,
                                         ShopProductClickedNewListener shopProductClickedListener,
                                         EmptyWrapViewHolder.Callback emptyProductOnClickListener,
                                         ShopProductEtalaseListViewHolder.OnShopProductEtalaseListViewHolderListener
                                                 onShopProductEtalaseListViewHolderListener,
                                         @Nullable OnShopProductAdapterTypeFactoryListener onShopProductAdapterTypeFactoryListener,
                                         boolean isHorizontalLayout,
                                         boolean isFeaturedOnly) {
        this.promoViewHolderListener = promoViewHolderListener;
        this.shopProductClickedListener = shopProductClickedListener;
        this.emptyProductOnClickListener = emptyProductOnClickListener;
        this.onShopProductEtalaseListViewHolderListener = onShopProductEtalaseListViewHolderListener;
        this.onShopProductAdapterTypeFactoryListener = onShopProductAdapterTypeFactoryListener;
        this.isHorizontalLayout = isHorizontalLayout;
        this.isFeaturedOnly = isFeaturedOnly;
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingShimmeringGridViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyModel viewModel) {
        return EmptyWrapViewHolder.LAYOUT;
    }

    public int type(ShopProductPromoViewModel shopProductPromoViewModel) {
        if (TextUtils.isEmpty(shopProductPromoViewModel.getUrl())) {
            return HideViewHolder.LAYOUT;
        } else {
            return ShopProductPromoViewHolder.LAYOUT;
        }
    }

    public int type(ShopProductFeaturedViewModel shopProductFeaturedViewModel) {
        if (shopProductFeaturedViewModel.getShopProductFeaturedViewModelList().size() == 0) {
            return HideViewHolder.LAYOUT;
        } else {
            return ShopProductFeaturedViewHolder.LAYOUT;
        }
    }

    public int type(ShopProductViewModel shopProductViewModel) {
        return ShopProductViewHolder.LAYOUT;
    }

    public int type(ErrorNetworkModel errorNetworkModel) {
        return ErrorNetworkWrapViewHolder.LAYOUT;
    }

    public int type(ShopProductEtalaseListViewModel etalaseLabelViewModel) {
        boolean needShowEtalase = true;
        if (onShopProductAdapterTypeFactoryListener!= null) {
            needShowEtalase = onShopProductAdapterTypeFactoryListener.needToShowEtalase();
        }
        if (!needShowEtalase || etalaseLabelViewModel.getEtalaseModelList().size() == 0) {
            return HideViewHolder.LAYOUT;
        } else {
            return ShopProductEtalaseListViewHolder.LAYOUT;
        }
    }

    public int type(HideViewModel viewModel) {
        return HideViewHolder.LAYOUT;
    }

    public int type (ShopProductEtalaseTitleViewModel shopProductEtalaseTitleViewModel) {
        boolean needShowEtalase = true;
        if (onShopProductAdapterTypeFactoryListener!= null) {
            needShowEtalase = onShopProductAdapterTypeFactoryListener.needToShowEtalase();
        }
        if (!needShowEtalase || TextUtils.isEmpty(shopProductEtalaseTitleViewModel.getEtalaseName())) {
            return HideViewHolder.LAYOUT;
        } else {
            return ShopProductEtalaseTitleViewHolder.LAYOUT;
        }
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
        } else if (type == ShopProductEtalaseListViewHolder.LAYOUT) {
            return new ShopProductEtalaseListViewHolder(parent, onShopProductEtalaseListViewHolderListener);
        } else if (type == ShopProductPromoViewHolder.LAYOUT) {
            return new ShopProductPromoViewHolder(parent, promoViewHolderListener);
        } else if(type == ShopProductFeaturedViewHolder .LAYOUT){
            return new ShopProductFeaturedViewHolder(parent, shopProductClickedListener);
        } else if(type == ShopProductViewHolder.LAYOUT){
            return new ShopProductViewHolder(parent, shopProductClickedListener, isHorizontalLayout, isFeaturedOnly);
        } if (type == HideViewHolder.LAYOUT) {
            return new HideViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}