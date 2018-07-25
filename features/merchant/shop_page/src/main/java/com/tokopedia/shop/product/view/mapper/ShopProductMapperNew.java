package com.tokopedia.shop.product.view.mapper;

import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.shop.common.util.WishListUtils;
import com.tokopedia.shop.etalase.data.source.cloud.model.EtalaseModel;
import com.tokopedia.shop.etalase.data.source.cloud.model.PagingListOther;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ShopProductMapperNew {

    @Inject
    public ShopProductMapperNew() {

    }

    public List<ShopProductViewModel> convertFromShopProduct(List<ShopProduct> shopProductList, int page, int defaultPerPage) {
        List<ShopProductViewModel> shopProductViewModelList = new ArrayList<>();
        for (int i = 0; i < shopProductList.size(); i++) {
            ShopProduct shopProduct = shopProductList.get(i);
            ShopProductViewModel shopProductViewModel = new ShopProductViewModel(shopProduct);
            shopProductViewModel.setPositionTracking(getCurrentPageView(page, i + 1, defaultPerPage));
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

    /**
     * Merge original etalase list with the selected etalase from user.
     *
     * @param pagingListOther       A - Original Etalase List
     * @param selectedEtalaseIdList B- selected etalase List (outside original Etalase to be merge in index 1,2, and so on)
     * @param limit                 maximum etalase to show
     * @return Merge Etalase List A(0) - B(0) - B(1) - A(1) - A(2)
     */
    public static List<ShopEtalaseViewModel> mergeEtalaseList(PagingListOther<EtalaseModel> pagingListOther,
                                                              final ArrayList<ShopEtalaseViewModel> selectedEtalaseIdList, final int limit) {
        if (pagingListOther.getList() != null && !pagingListOther.getList().isEmpty()) {
            pagingListOther.getListOther().addAll(pagingListOther.getList());
        }
        if (pagingListOther.getListOther().size() == 0) {
            return new ArrayList<>();
        }
        List<ShopEtalaseViewModel> shopEtalaseViewModels = new ArrayList<>();
        // loop to convert to view model, only get until limit.
        for (EtalaseModel etalaseModel : pagingListOther.getListOther()) {
            // add to primary list
            if (shopEtalaseViewModels.size() < limit) {
                ShopEtalaseViewModel model = new ShopEtalaseViewModel(etalaseModel);
                shopEtalaseViewModels.add(model);
            }
        }
        // replace the first with selected id list
        // loop all selected etalase.
        if (selectedEtalaseIdList != null) {
            for (int i = selectedEtalaseIdList.size() - 1; i >= 0; i--) {
                ShopEtalaseViewModel selectedShopEtalaseViewModel = selectedEtalaseIdList.get(i);
                int indexToReplace = shopEtalaseViewModels.size() >= 1 ? 1 : 0;
                shopEtalaseViewModels.add(indexToReplace, selectedShopEtalaseViewModel);
                // check duplicate in index 2 to end, remove if any.
                if (shopEtalaseViewModels.size() > 3) {
                    for (int j = 2; j < shopEtalaseViewModels.size(); j++) {
                        if (shopEtalaseViewModels.get(j).getEtalaseId().equalsIgnoreCase(selectedShopEtalaseViewModel.getEtalaseId())) {
                            shopEtalaseViewModels.remove(j);
                            break;
                        }
                    }
                }
                if (shopEtalaseViewModels.size() > limit) {
                    shopEtalaseViewModels.remove(shopEtalaseViewModels.size() - 1);
                }
            }
        }
        return shopEtalaseViewModels;

    }

}
