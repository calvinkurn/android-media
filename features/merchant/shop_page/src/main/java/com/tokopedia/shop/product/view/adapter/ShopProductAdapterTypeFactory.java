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
import com.tokopedia.shop.product.view.adapter.viewholder.ErrorNetworkWrapViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.HideViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseHighlightViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseListViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseTitleViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductCarouselViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductPromoViewHolder;
import com.tokopedia.shop.product.view.listener.ShopProductClickedNewListener;
import com.tokopedia.shop.product.view.model.HideViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseHighlightViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseListViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseTitleViewModel;
import com.tokopedia.shop.product.view.model.ShopProductFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.shop.product.view.model.ShopProductPromoViewModel;

import static com.tokopedia.shop.common.constant.ShopPageConstant.SMALL_DATA_LIMIT;

public class ShopProductAdapterTypeFactory extends BaseAdapterTypeFactory {

    private final ShopProductPromoViewHolder.PromoViewHolderListener promoViewHolderListener;
    private final ShopProductClickedNewListener shopProductClickedListener;
    private final EmptyResultViewHolder.Callback emptyProductOnClickListener;
    private final ShopProductEtalaseListViewHolder.OnShopProductEtalaseListViewHolderListener onShopProductEtalaseListViewHolderListener;

    // gridLayout is for main product
    private final boolean isGridSquareLayout;
    private final boolean isFeaturedOnly;
    private final int deviceWidth;
    private ShopProductAdapter shopProductAdapter;

    public ShopProductAdapterTypeFactory(ShopProductPromoViewHolder.PromoViewHolderListener promoViewHolderListener,
                                         ShopProductClickedNewListener shopProductClickedListener,
                                         EmptyResultViewHolder.Callback emptyProductOnClickListener,
                                         ShopProductEtalaseListViewHolder.OnShopProductEtalaseListViewHolderListener
                                                 onShopProductEtalaseListViewHolderListener,
                                         boolean isGridSquareLayout,
                                         int deviceWidth,
                                         boolean isFeaturedOnly) {
        this.promoViewHolderListener = promoViewHolderListener;
        this.shopProductClickedListener = shopProductClickedListener;
        this.emptyProductOnClickListener = emptyProductOnClickListener;
        this.onShopProductEtalaseListViewHolderListener = onShopProductEtalaseListViewHolderListener;
        this.isGridSquareLayout = isGridSquareLayout;
        this.isFeaturedOnly = isFeaturedOnly;
        this.deviceWidth = deviceWidth;
    }

    public void attachAdapter(ShopProductAdapter shopProductAdapter){
        this.shopProductAdapter = shopProductAdapter;
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingShimmeringGridViewHolder.LAYOUT;
    }

    public int type(EmptyModel viewModel) {
        return EmptyResultViewHolder.LAYOUT;
    }

    public int type(ShopProductPromoViewModel shopProductPromoViewModel) {
        if (TextUtils.isEmpty(shopProductPromoViewModel.getUrl())) {
            return HideViewHolder.LAYOUT;
        } else {
            return ShopProductPromoViewHolder.LAYOUT;
        }
    }

    public int type(ShopProductEtalaseHighlightViewModel shopProductEtalaseHighlightViewModel) {
        if (shopProductEtalaseHighlightViewModel.getShopProductViewModelListList().size() == 0) {
            return HideViewHolder.LAYOUT;
        } else {
            return ShopProductEtalaseHighlightViewHolder.LAYOUT;
        }
    }

    public int type(ShopProductFeaturedViewModel shopProductFeaturedViewModel) {
        if (shopProductFeaturedViewModel.getShopProductFeaturedViewModelList().size() == 0) {
            return HideViewHolder.LAYOUT;
        } else {
            return ShopProductCarouselViewHolder.LAYOUT;
        }
    }

    public int type(ShopProductViewModel shopProductViewModel) {
        if (isGridSquareLayout) {
            return ShopProductViewHolder.GRID_LAYOUT;
        } else {
            if (isDataSizeSmall()) {
                return ShopProductViewHolder.LIST_LAYOUT;
            }
            return ShopProductViewHolder.GRID_LAYOUT;
        }
    }

    private boolean isDataSizeSmall() {
        return shopProductAdapter != null &&
                shopProductAdapter.getShopProductViewModelList().size() <= SMALL_DATA_LIMIT;
    }

    private boolean isFeaturedDataSizeSmall() {
        return shopProductAdapter != null &&
                shopProductAdapter.getShopProductFeaturedViewModel().getShopProductFeaturedViewModelList().size()
                        <= SMALL_DATA_LIMIT;
    }

    public int type(ErrorNetworkModel errorNetworkModel) {
        return ErrorNetworkWrapViewHolder.LAYOUT;
    }

    public int type(ShopProductEtalaseListViewModel etalaseLabelViewModel) {
        boolean needShowEtalase = true;
        if (shopProductAdapter != null) {
            needShowEtalase = shopProductAdapter.isNeedToShowEtalase();
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

    public int type(ShopProductEtalaseTitleViewModel shopProductEtalaseTitleViewModel) {
        boolean needShowEtalase = true;
        if (shopProductAdapter != null) {
            needShowEtalase = shopProductAdapter.isNeedToShowEtalase();
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
        } else if (type == EmptyResultViewHolder.LAYOUT) {
            return new EmptyResultViewHolder(parent, emptyProductOnClickListener);
        } else if (type == ErrorNetworkWrapViewHolder.LAYOUT) {
            return new ErrorNetworkWrapViewHolder(parent);
        } else if (type == ShopProductEtalaseTitleViewHolder.LAYOUT) {
            return new ShopProductEtalaseTitleViewHolder(parent);
        } else if (type == ShopProductEtalaseListViewHolder.LAYOUT) {
            return new ShopProductEtalaseListViewHolder(parent, onShopProductEtalaseListViewHolderListener);
        } else if (type == ShopProductPromoViewHolder.LAYOUT) {
            return new ShopProductPromoViewHolder(parent, promoViewHolderListener);
        } else if (type == ShopProductCarouselViewHolder.LAYOUT) {
            return new ShopProductCarouselViewHolder(parent, deviceWidth, shopProductClickedListener,
                    isFeaturedDataSizeSmall(),
                    parent.getContext().getString(R.string.shop_page_label_featured_product));
        } if (type == ShopProductEtalaseHighlightViewHolder.LAYOUT) {
            return new ShopProductEtalaseHighlightViewHolder(parent, deviceWidth);
        } else if (type == ShopProductViewHolder.GRID_LAYOUT ||
                type == ShopProductViewHolder.LIST_LAYOUT) {
            return new ShopProductViewHolder(parent, shopProductClickedListener, !isGridSquareLayout, deviceWidth, isFeaturedOnly, type);
        }
        if (type == HideViewHolder.LAYOUT) {
            return new HideViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}