package com.tokopedia.shop.product.view.adapter.newadapter;

import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductPromoViewHolder;
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener;
import com.tokopedia.shop.product.view.model.newmodel.BaseShopProductViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductEtalaseLabelViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductFeaturedViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductPromoViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShopProductNewAdapter extends BaseListAdapter<BaseShopProductViewModel, ShopProductAdapterTypeFactory> implements DataEndlessScrollListener.OnDataEndlessScrollListener {

    private static final int DEFAULT_PROMO_POSITION = 0;
    private static final int DEFAULT_FEATURED_POSITION = 1;
    private static final int DEFAULT_ETALASE_POSITION = 2;
    public static final int DATA_OFFSET = 3; // PROMO + FEATURED + ETALASE

    private ShopProductPromoViewModel shopProductPromoViewModel;
    private List<ShopProductViewModel> shopProductViewModelList;
    private ShopProductFeaturedViewModel shopProductFeaturedViewModel;
    private ShopProductEtalaseLabelViewModel shopProductEtalaseLabelViewModel;

    private View promoView;
    private String promoUrl;

    public ShopProductNewAdapter(ShopProductAdapterTypeFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory, null);
        shopProductPromoViewModel = new ShopProductPromoViewModel();
        shopProductViewModelList = new ArrayList<>();
        shopProductFeaturedViewModel = new ShopProductFeaturedViewModel();
        shopProductEtalaseLabelViewModel = new ShopProductEtalaseLabelViewModel();
        visitables.add(shopProductPromoViewModel);
        visitables.add(shopProductFeaturedViewModel);
        visitables.add(shopProductEtalaseLabelViewModel);
    }

    public void setShopProductPromoViewModel(ShopProductPromoViewModel shopProductPromoViewModel) {
        if (shopProductPromoViewModel == null) {
            this.shopProductPromoViewModel = new ShopProductPromoViewModel();
        } else {
            this.shopProductPromoViewModel = shopProductPromoViewModel;
        }
        setVisitable(DEFAULT_PROMO_POSITION, this.shopProductPromoViewModel);
    }

    public void setShopProductFeaturedViewModel(ShopProductFeaturedViewModel shopProductFeaturedViewModel) {
        if (shopProductFeaturedViewModel == null) {
            this.shopProductFeaturedViewModel = new ShopProductFeaturedViewModel();
        } else {
            this.shopProductFeaturedViewModel = shopProductFeaturedViewModel;
        }
        setVisitable(DEFAULT_FEATURED_POSITION, this.shopProductFeaturedViewModel);
    }

    public void setShopProductEtalaseLabelViewModel(ShopProductEtalaseLabelViewModel shopProductEtalaseLabelViewModel) {
        if (shopProductEtalaseLabelViewModel == null) {
            this.shopProductEtalaseLabelViewModel = new ShopProductEtalaseLabelViewModel();
        } else {
            this.shopProductEtalaseLabelViewModel = shopProductEtalaseLabelViewModel;
        }
        setVisitable(DEFAULT_ETALASE_POSITION, this.shopProductEtalaseLabelViewModel);
    }

    private void setVisitable(int position, Visitable visitable) {
        visitables.set(position, visitable);
        notifyItemChanged(position);
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
        visitables.addAll(this.shopProductViewModelList);
    }

    // this is to maintain promo view in recyclerview.
    @Override
    protected View onCreateViewItem(ViewGroup parent, int viewType) {
        if (viewType == ShopProductPromoViewHolder.LAYOUT) {
            if (promoView == null || !promoUrl.equals(shopProductPromoViewModel.getUrl())) {
                promoView = super.onCreateViewItem(parent, viewType);
                promoUrl = shopProductPromoViewModel.getUrl();
            } else {
                return promoView;
            }
        }
        return super.onCreateViewItem(parent, viewType);
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
        for (int i = 0; i < shopProductViewModelList.size(); i++) {
            ShopProductViewModel shopProductViewModel = shopProductViewModelList.get(i);
            if (shopProductViewModel.getId().equalsIgnoreCase(productId)) {
                shopProductViewModel.setWishList(wishList);
                notifyItemChanged(i);
                return;
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
    public void showLoading(){
        if (!isLoading()) {
            if (shopProductViewModelList.size() == 0) {
                visitables.add(loadingModel);
            } else {
                visitables.add(loadingMoreModel);
            }
            notifyItemInserted(visitables.size());
        }
    }

    @Override
    protected boolean isItemClickableByDefault() {
        return false;
    }

    @Override
    public int getEndlessDataSize() {
        return shopProductViewModelList.size();
    }


}
