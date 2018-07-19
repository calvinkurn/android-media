package com.tokopedia.shop.product.view.adapter.newadapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.EmptyWrapViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ErrorNetworkWrapViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.HideViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductEtalaseListViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductEtalaseTitleViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductFeaturedViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductNewViewHolder;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductPromoViewHolder;
import com.tokopedia.shop.product.view.listener.newlistener.ShopProductClickedNewListener;
import com.tokopedia.shop.product.view.model.newmodel.HideViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductEtalaseListViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductEtalaseTitleViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductFeaturedViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductPromoViewModel;

public class ShopProductAdapterTypeFactory extends BaseAdapterTypeFactory {

    private final ShopProductPromoViewHolder.PromoViewHolderListener promoViewHolderListener;
    private final ShopProductClickedNewListener shopProductClickedListener;
    private final EmptyWrapViewHolder.Callback emptyProductOnClickListener;
    private final ShopProductEtalaseListViewHolder.OnShopProductEtalaseListViewHolderListener onShopProductEtalaseListViewHolderListener;
    private final boolean isHorizontalLayout;
    private boolean needToShowEtalase;

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
                                         boolean isHorizontalLayout) {
        this.promoViewHolderListener = promoViewHolderListener;
        this.shopProductClickedListener = shopProductClickedListener;
        this.emptyProductOnClickListener = emptyProductOnClickListener;
        this.onShopProductEtalaseListViewHolderListener = onShopProductEtalaseListViewHolderListener;
        this.onShopProductAdapterTypeFactoryListener = onShopProductAdapterTypeFactoryListener;
        this.isHorizontalLayout = isHorizontalLayout;
    }

    public void setShowEtalase(boolean needToShow) {
        needToShowEtalase = needToShow;
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
        return ShopProductNewViewHolder.LAYOUT;
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
        } else if(type == ShopProductNewViewHolder.LAYOUT){
            return new ShopProductNewViewHolder(parent, shopProductClickedListener, isHorizontalLayout);
        } if (type == HideViewHolder.LAYOUT) {
            return new HideViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}