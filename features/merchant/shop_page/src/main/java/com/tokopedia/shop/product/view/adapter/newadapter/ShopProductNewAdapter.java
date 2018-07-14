package com.tokopedia.shop.product.view.adapter.newadapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModelOld;
import com.tokopedia.shop.product.view.model.newmodel.BaseShopProductViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductFeaturedViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductPromoViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShopProductNewAdapter extends BaseListAdapter<BaseShopProductViewModel, ShopProductAdapterTypeFactory> {

    private static final int DEFAULT_PROMO_POSITION = 0;
    private static final int DEFAULT_FEATURED_POSITION = 1;

    private ShopProductPromoViewModel shopProductPromoViewModel;
    private List<ShopProductViewModel> shopProductViewModelList;
    private ShopProductFeaturedViewModel shopProductFeaturedViewModel;

    public ShopProductNewAdapter(ShopProductAdapterTypeFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory, null);
        shopProductPromoViewModel = new ShopProductPromoViewModel();
        shopProductViewModelList = new ArrayList<>();
        shopProductFeaturedViewModel = new ShopProductFeaturedViewModel();
        visitables.add(shopProductPromoViewModel);
        visitables.add(shopProductFeaturedViewModel);
    }

    public void setShopProductPromoViewModel(ShopProductPromoViewModel shopProductPromoViewModel) {
        if (shopProductPromoViewModel == null) {
            this.shopProductPromoViewModel = new ShopProductPromoViewModel();
        } else {
            this.shopProductPromoViewModel = shopProductPromoViewModel;
        }
        visitables.set(DEFAULT_PROMO_POSITION, this.shopProductPromoViewModel);
        notifyItemChanged(DEFAULT_PROMO_POSITION);
    }

    public void setShopProductFeaturedViewModel(ShopProductFeaturedViewModel shopProductFeaturedViewModel) {
        if (shopProductFeaturedViewModel == null) {
            this.shopProductFeaturedViewModel = new ShopProductFeaturedViewModel();
        } else {
            this.shopProductFeaturedViewModel = shopProductFeaturedViewModel;
        }
        visitables.set(DEFAULT_FEATURED_POSITION, this.shopProductFeaturedViewModel);
        notifyItemChanged(DEFAULT_FEATURED_POSITION);
    }

    @Override
    public void showErrorNetwork(String message, ErrorNetworkModel.OnRetryListener onRetryListener) {
        errorNetworkModel.setErrorMessage(message);
        errorNetworkModel.setOnRetryListener(onRetryListener);
        this.shopProductViewModelList.clear();
        clearAllNonDataElement();
        visitables.add(errorNetworkModel);
        notifyDataSetChanged();
    }

    public void clearProductList() {
        shopProductViewModelList.clear();
    }

    public void addProductList(List<ShopProductViewModel> shopProductViewModelArrayList) {
        this.shopProductViewModelList = shopProductViewModelArrayList;
    }

    @Override
    public void clearAllElements() {
        setShopProductPromoViewModel(null);
        setShopProductFeaturedViewModel(null);
        clearProductList();
    }

    public List<ShopProductViewModel> getShopProductViewModelList() {
        return shopProductViewModelList;
    }

    public void updateWishListStatus(String productId, boolean wishList) {
        List<BaseShopProductViewModel> shopProductBaseViewModelList = getData();
        for (int i = 0; i < shopProductBaseViewModelList.size(); i++) {
            BaseShopProductViewModel shopProductViewModel = shopProductBaseViewModelList.get(i);
            if (shopProductViewModel instanceof ShopProductViewModelOld) {
                if (((ShopProductViewModelOld) shopProductViewModel).getId().equalsIgnoreCase(productId)) {
                    ((ShopProductViewModelOld) shopProductViewModel).setWishList(wishList);
                    notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    public void updateVisibleStatus(boolean visible) {
        List<BaseShopProductViewModel> shopProductBaseViewModelList = getData();
        for (int i = 0; i < shopProductBaseViewModelList.size(); i++) {
            BaseShopProductViewModel shopProductViewModel = shopProductBaseViewModelList.get(i);
            if (shopProductViewModel instanceof ShopProductPromoViewModel) {
                ShopProductPromoViewModel shopProductPromoViewModel = ((ShopProductPromoViewModel) shopProductViewModel);
                if (shopProductPromoViewModel.getShopProductUserVisibleHintListener() != null) {
                    shopProductPromoViewModel.getShopProductUserVisibleHintListener().setUserVisibleHint(visible);
                }
                return;
            }
        }
    }

    @Override
    protected boolean isItemClickableByDefault() {
        return false;
    }

}
