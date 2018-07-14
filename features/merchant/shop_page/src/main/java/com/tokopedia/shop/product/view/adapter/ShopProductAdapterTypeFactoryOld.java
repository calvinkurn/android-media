package com.tokopedia.shop.product.view.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductListViewHolderOld;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductSingleViewHolderOld;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolderOld;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.ShopProductViewModelOld;

/**
 * Created by alvarisi on 12/7/17.
 */
@Deprecated
public class ShopProductAdapterTypeFactoryOld extends BaseAdapterTypeFactory {

    private final ShopProductClickedListener shopProductClickedListener;
    private TypeFactoryListener typeFactoryListener;

    public ShopProductAdapterTypeFactoryOld(@Nullable TypeFactoryListener typeFactoryListener,
                                            ShopProductClickedListener shopProductClickedListener) {
        this.typeFactoryListener = typeFactoryListener;
        this.shopProductClickedListener = shopProductClickedListener;
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingShimmeringGridViewHolder.LAYOUT;
    }

    public int type(ShopProductViewModelOld shopProductViewModelOld) {
        if (typeFactoryListener != null) {
            return typeFactoryListener.getType(shopProductViewModelOld);
        }
        return ShopProductViewHolderOld.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        if (viewType == LoadingShimmeringGridViewHolder.LAYOUT) {
            return new LoadingShimmeringGridViewHolder(view);
        } else if (viewType == ShopProductViewHolderOld.LAYOUT) {
            return new ShopProductViewHolderOld(view, shopProductClickedListener);
        } else if (viewType == ShopProductListViewHolderOld.LAYOUT) {
            return new ShopProductListViewHolderOld(view, shopProductClickedListener);
        } else if (viewType == ShopProductSingleViewHolderOld.LAYOUT) {
            return new ShopProductSingleViewHolderOld(view, shopProductClickedListener);
        } else {
            return super.createViewHolder(view, viewType);
        }
    }

    public interface TypeFactoryListener<E> {
        int getType(E type);
    }
}
