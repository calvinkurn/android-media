package com.tokopedia.shop.product.view.adapter;

import android.text.TextUtils;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder;
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget;
import com.tokopedia.shop.R;
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef;
import com.tokopedia.shop.product.view.adapter.viewholder.ErrorNetworkWrapViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopMerchantVoucherViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductCarouselViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseHighlightViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseListViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseTitleViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.listener.ShopCarouselSeeAllClickedListener;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.EtalaseHighlightCarouselViewModel;
import com.tokopedia.shop.product.view.model.HideViewModel;
import com.tokopedia.shop.product.view.model.ShopMerchantVoucherViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseHighlightViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseListViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseTitleViewModel;
import com.tokopedia.shop.product.view.model.ShopProductFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import static com.tokopedia.shop.common.constant.ShopPageConstant.SMALL_DATA_LIMIT;

public class ShopProductAdapterTypeFactory extends BaseAdapterTypeFactory {

    private final ShopProductClickedListener shopProductClickedListener;
    private final ShopCarouselSeeAllClickedListener shopCarouselSeeAllClickedListener;
    private final EmptyResultViewHolder.Callback emptyProductOnClickListener;
    private final ShopProductEtalaseListViewHolder.OnShopProductEtalaseListViewHolderListener onShopProductEtalaseListViewHolderListener;
    private final MerchantVoucherListWidget.OnMerchantVoucherListWidgetListener onMerchantVoucherListWidgetListener;

    // gridLayout is for main product
    private final boolean isGridSquareLayout;
    private final @ShopTrackProductTypeDef int shopTrackType;
    private final int deviceWidth;
    private ShopProductAdapter shopProductAdapter;

    public ShopProductAdapterTypeFactory(ShopProductClickedListener shopProductClickedListener,
                                         ShopCarouselSeeAllClickedListener shopCarouselSeeAllClickedListener,
                                         EmptyResultViewHolder.Callback emptyProductOnClickListener,
                                         ShopProductEtalaseListViewHolder.OnShopProductEtalaseListViewHolderListener
                                                 onShopProductEtalaseListViewHolderListener,
                                         MerchantVoucherListWidget.OnMerchantVoucherListWidgetListener onMerchantVoucherListWidgetListener,
                                         boolean isGridSquareLayout,
                                         int deviceWidth,
                                         @ShopTrackProductTypeDef int shopTrackType) {
        this.shopProductClickedListener = shopProductClickedListener;
        this.shopCarouselSeeAllClickedListener = shopCarouselSeeAllClickedListener;
        this.emptyProductOnClickListener = emptyProductOnClickListener;
        this.onShopProductEtalaseListViewHolderListener = onShopProductEtalaseListViewHolderListener;
        this.onMerchantVoucherListWidgetListener = onMerchantVoucherListWidgetListener;
        this.isGridSquareLayout = isGridSquareLayout;
        this.shopTrackType = shopTrackType;
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

    public int type(ShopProductEtalaseHighlightViewModel shopProductEtalaseHighlightViewModel) {
        if (shopProductEtalaseHighlightViewModel.getEtalaseHighlightCarouselViewModelList().size() == 0) {
            return HideViewHolder.LAYOUT;
        } else {
            return ShopProductEtalaseHighlightViewHolder.LAYOUT;
        }
    }

    public int type(EtalaseHighlightCarouselViewModel etalaseHighlightCarouselViewModel) {
        if (etalaseHighlightCarouselViewModel.getShopProductViewModelList().size() == 0) {
            return HideViewHolder.LAYOUT;
        } else {
            if (etalaseHighlightCarouselViewModel.getShopProductViewModelList().size() <= SMALL_DATA_LIMIT){
                return ShopProductCarouselViewHolder.VERTICAL_LAYOUT;
            } else {
                return ShopProductCarouselViewHolder.LAYOUT;
            }
        }
    }

    public int type(ShopProductFeaturedViewModel shopProductFeaturedViewModel) {
        if (shopProductFeaturedViewModel.getShopProductFeaturedViewModelList().size() == 0) {
            return HideViewHolder.LAYOUT;
        } else {
            if (shopProductFeaturedViewModel.getShopProductFeaturedViewModelList().size() <= SMALL_DATA_LIMIT){
                return ShopProductCarouselViewHolder.VERTICAL_LAYOUT;
            } else {
                return ShopProductCarouselViewHolder.LAYOUT;
            }
        }
    }

    public int type(ShopMerchantVoucherViewModel shopMerchantVoucherViewModel) {
        if (shopMerchantVoucherViewModel.getShopMerchantVoucherViewModelArrayList().size() ==0) {
            return HideViewHolder.LAYOUT;
        } else {
            return ShopMerchantVoucherViewHolder.LAYOUT;
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
        } if (type == ShopMerchantVoucherViewHolder.LAYOUT) {
            return new ShopMerchantVoucherViewHolder(parent, onMerchantVoucherListWidgetListener);
        } else if (type == ShopProductCarouselViewHolder.LAYOUT ||
                type == ShopProductCarouselViewHolder.VERTICAL_LAYOUT) {
            return new ShopProductCarouselViewHolder(parent, deviceWidth, shopProductClickedListener,
                    type == ShopProductCarouselViewHolder.VERTICAL_LAYOUT,
                    parent.getContext().getString(R.string.shop_page_label_featured_product), ShopTrackProductTypeDef.FEATURED,null);
        } if (type == ShopProductEtalaseHighlightViewHolder.LAYOUT) {
            return new ShopProductEtalaseHighlightViewHolder(parent, deviceWidth, shopProductClickedListener, shopCarouselSeeAllClickedListener);
        } else if (type == ShopProductViewHolder.GRID_LAYOUT ||
                type == ShopProductViewHolder.LIST_LAYOUT) {
            return new ShopProductViewHolder(parent, shopProductClickedListener, !isGridSquareLayout, deviceWidth, shopTrackType, type);
        }
        if (type == HideViewHolder.LAYOUT) {
            return new HideViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}