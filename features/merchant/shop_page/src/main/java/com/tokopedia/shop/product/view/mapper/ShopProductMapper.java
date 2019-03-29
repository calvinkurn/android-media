package com.tokopedia.shop.product.view.mapper;

import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel;
import com.tokopedia.shop.common.util.WishListUtils;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ShopProductMapper {

    @Inject
    public ShopProductMapper() {

    }

    public List<ShopProductViewModel> convertFromShopProduct(List<ShopProduct> shopProductList, int page, int defaultPerPage) {
        List<ShopProductViewModel> shopProductViewModelList = new ArrayList<>();
        for (int i = 0; i < shopProductList.size(); i++) {
            ShopProduct shopProduct = shopProductList.get(i);
            ShopProductViewModel shopProductViewModel = new ShopProductViewModel(shopProduct);
            shopProductViewModelList.add(shopProductViewModel);
        }
        return shopProductViewModelList;
    }

    private int getCurrentPageView(int currentPage, int position, int perPage) {
        if (currentPage > 1) {
            return (perPage * (currentPage - 1)) + position;
        } else {
            return position;
        }
    }

    public void mergeShopProductViewModel(List<ShopProductViewModel> shopProductViewModelList,
                                          List<ShopProductCampaign> shopProductCampaignList,
                                          List<String> productWishList, boolean showWishlist) {
        for (ShopProductViewModel shopProductViewModel : shopProductViewModelList) {
            for (ShopProductCampaign shopProductCampaign : shopProductCampaignList) {
                if (shopProductViewModel.getId().equalsIgnoreCase(shopProductCampaign.getProductId())) {
                    shopProductViewModel.setDisplayedPrice(shopProductCampaign.getDiscountedPriceIdr());
                    shopProductViewModel.setOriginalPrice(shopProductCampaign.getOriginalPriceIdr());
                    shopProductViewModel.setDiscountPercentage(shopProductCampaign.getPercentageAmount());
                }
            }
            shopProductViewModel.setWishList(WishListUtils.isWishList(shopProductViewModel.getId(), productWishList));
            shopProductViewModel.setShowWishList(showWishlist);
        }
    }


    public List<ShopProductViewModel> convertFromProductFeatured(List<GMFeaturedProduct> gmFeaturedProductList) {
        List<ShopProductViewModel> shopProductViewModelList = new ArrayList<>();
        for (GMFeaturedProduct gmFeaturedProduct : gmFeaturedProductList) {
            ShopProductViewModel shopProductViewModel = new ShopProductViewModel(gmFeaturedProduct);
            shopProductViewModelList.add(shopProductViewModel);
        }
        return shopProductViewModelList;
    }

    public static ArrayList<ShopEtalaseViewModel> map(ArrayList<ShopEtalaseModel> shopEtalaseModels) {
        if (shopEtalaseModels.size() == 0) {
            return new ArrayList<>();
        }
        ArrayList<ShopEtalaseViewModel> shopEtalaseViewModels = new ArrayList<>();
        // loop to convert to view model, only get until limit.
        for (ShopEtalaseModel etalaseModel : shopEtalaseModels) {
            shopEtalaseViewModels.add(new ShopEtalaseViewModel(etalaseModel));
        }
        return shopEtalaseViewModels;
    }

}
