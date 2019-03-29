package com.tokopedia.shop.product.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder;
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef;
import com.tokopedia.shop.product.view.adapter.viewholder.ErrorNetworkWrapViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductCarouselViewHolder;
import com.tokopedia.shop.product.view.listener.ShopCarouselSeeAllClickedListener;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.EtalaseHighlightCarouselViewModel;
import com.tokopedia.shop.product.view.model.HideViewModel;

import static com.tokopedia.shop.common.constant.ShopPageConstant.SMALL_DATA_LIMIT;

public class EtalaseHighlightAdapterTypeFactory extends BaseAdapterTypeFactory {

    private final ShopProductClickedListener shopProductClickedListener;
    private final ShopCarouselSeeAllClickedListener shopCarouselSeeAllClickedListener;

    private final int deviceWidth;

    public EtalaseHighlightAdapterTypeFactory(ShopProductClickedListener shopProductClickedListener,
                                              ShopCarouselSeeAllClickedListener shopCarouselSeeAllClickedListener,
                                              int deviceWidth) {
        this.shopProductClickedListener = shopProductClickedListener;
        this.shopCarouselSeeAllClickedListener = shopCarouselSeeAllClickedListener;
        this.deviceWidth = deviceWidth;
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingShimmeringGridViewHolder.LAYOUT;
    }

    public int type(EmptyModel viewModel) {
        return EmptyResultViewHolder.LAYOUT;
    }

    public int type(EtalaseHighlightCarouselViewModel etalaseHighlightCarouselViewModel) {
        if (etalaseHighlightCarouselViewModel.getShopProductViewModelList() == null ||
                etalaseHighlightCarouselViewModel.getShopProductViewModelList().size() == 0) {
            return HideViewHolder.LAYOUT;
        } else {
            if (etalaseHighlightCarouselViewModel.getShopProductViewModelList().size() <= SMALL_DATA_LIMIT) {
                return ShopProductCarouselViewHolder.VERTICAL_LAYOUT;
            } else {
                return ShopProductCarouselViewHolder.LAYOUT;
            }
        }
    }

    public int type(ErrorNetworkModel errorNetworkModel) {
        return ErrorNetworkWrapViewHolder.LAYOUT;
    }

    public int type(HideViewModel viewModel) {
        return HideViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == LoadingShimmeringGridViewHolder.LAYOUT) {
            return new LoadingShimmeringGridViewHolder(parent);
        } else if (type == ErrorNetworkWrapViewHolder.LAYOUT) {
            return new ErrorNetworkWrapViewHolder(parent);
        } else if (type == ShopProductCarouselViewHolder.LAYOUT ||
                type == ShopProductCarouselViewHolder.VERTICAL_LAYOUT) {
            return new ShopProductCarouselViewHolder(parent, deviceWidth, shopProductClickedListener,
                    type == ShopProductCarouselViewHolder.VERTICAL_LAYOUT,
                    null, ShopTrackProductTypeDef.ETALASE_HIGHLIGHT, shopCarouselSeeAllClickedListener);
        } else if (type == HideViewHolder.LAYOUT) {
            return new HideViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}