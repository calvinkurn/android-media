package com.tokopedia.shop.product.view.adapter;

import android.text.TextUtils;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef;
import com.tokopedia.shop.product.view.adapter.viewholder.ErrorNetworkWrapViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.HideViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductCarouselViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseHighlightViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseListViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseTitleViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductPromoViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.listener.ShopProductClickedNewListener;
import com.tokopedia.shop.product.view.model.EtalaseHighlightCarouselViewModel;
import com.tokopedia.shop.product.view.model.HideViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseHighlightViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseListViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseTitleViewModel;
import com.tokopedia.shop.product.view.model.ShopProductFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductPromoViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import java.util.List;

import static com.tokopedia.shop.common.constant.ShopPageConstant.SMALL_DATA_LIMIT;

public class EtalaseHighlightAdapterTypeFactory extends BaseAdapterTypeFactory {

    private final ShopProductClickedNewListener shopProductClickedListener;

    private final int deviceWidth;

    public EtalaseHighlightAdapterTypeFactory(ShopProductClickedNewListener shopProductClickedListener,
                                              int deviceWidth) {
        this.shopProductClickedListener = shopProductClickedListener;
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
        if (etalaseHighlightCarouselViewModel.getShopProductViewModelList()== null ||
                etalaseHighlightCarouselViewModel.getShopProductViewModelList().size()== 0) {
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
                    null, ShopTrackProductTypeDef.ETALASE_HIGHLIGHT);
        } else if (type == HideViewHolder.LAYOUT) {
            return new HideViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}