package com.tokopedia.imagesearch.helper;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.imagesearch.domain.model.BadgeModel;
import com.tokopedia.imagesearch.domain.model.LabelModel;
import com.tokopedia.imagesearch.domain.model.ProductModel;
import com.tokopedia.imagesearch.domain.model.SearchResultModel;
import com.tokopedia.imagesearch.domain.viewmodel.BadgeItem;
import com.tokopedia.imagesearch.domain.viewmodel.LabelItem;
import com.tokopedia.imagesearch.domain.viewmodel.ProductItem;
import com.tokopedia.imagesearch.domain.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 10/9/17.
 */

public class ProductViewModelHelper {

    private static final String SEARCH_RESULT_ENHANCE_ANALYTIC = "SEARCH_RESULT_ENHANCE_ANALYTIC";
    private static final String LAST_POSITION_ENHANCE_PRODUCT = "LAST_POSITION_ENHANCE_PRODUCT";
    private static LocalCacheHandler cache;

    public static ProductViewModel convertToProductViewModelFirstPage(Context context, SearchResultModel searchResultModel) {
        clearPositionCache(context);
        return convertToProductViewModel(context, searchResultModel);
    }

    private static void clearPositionCache(Context context) {
        LocalCacheHandler.clearCache(context, SEARCH_RESULT_ENHANCE_ANALYTIC);
    }

    public static ProductViewModel convertToProductViewModel(Context context, SearchResultModel searchResultModel) {
        ProductViewModel productViewModel = new ProductViewModel();
        productViewModel.setProductList(convertToProductItemList(context, searchResultModel.getProductList()));
        productViewModel.setCategoryFilterModel(searchResultModel.getCategoryFilterModel());
        productViewModel.setQuery(searchResultModel.getQuery());
        productViewModel.setShareUrl(searchResultModel.getShareUrl());
        productViewModel.setHasCatalog(false);
        productViewModel.setTotalData(searchResultModel.getTotalData());
        productViewModel.setAdditionalParams(searchResultModel.getAdditionalParams());
        return productViewModel;
    }

    private static List<ProductItem> convertToProductItemList(Context context, List<ProductModel> productModels) {
        List<ProductItem> productItemList = new ArrayList<>();

        int position = getLastPositionFromCache(context);

        for (ProductModel productModel : productModels) {
            position++;
            productItemList.add(convertToProductItem(productModel, position));
        }

        saveLastPositionToCache(context, position);

        return productItemList;
    }

    private static int getLastPositionFromCache(Context context) {
        if (cache == null) {
            cache = new LocalCacheHandler(context, SEARCH_RESULT_ENHANCE_ANALYTIC);
        }
        return cache.getInt(LAST_POSITION_ENHANCE_PRODUCT, 0);
    }

    private static void saveLastPositionToCache(Context context, int position) {
        if (cache == null) {
            cache = new LocalCacheHandler(context, SEARCH_RESULT_ENHANCE_ANALYTIC);
        }
        cache.putInt(LAST_POSITION_ENHANCE_PRODUCT, position);
        cache.applyEditor();
    }

    private static ProductItem convertToProductItem(ProductModel productModel, int position) {
        ProductItem productItem = new ProductItem();
        productItem.setProductID(productModel.getProductID());
        productItem.setProductName(productModel.getProductName());
        productItem.setImageUrl(productModel.getImageUrl());
        productItem.setImageUrl700(productModel.getImageUrl700());
        productItem.setRating(productModel.getRating());
        productItem.setCountReview(productModel.getCountReview());
        productItem.setCountCourier(productModel.getCountCourier());
        productItem.setDiscountPercentage(productModel.getDiscountPercentage());
        productItem.setOriginalPrice(productModel.getOriginalPrice());
        productItem.setPrice(productModel.getPrice());
        productItem.setPriceRange(productModel.getPriceRange());
        productItem.setShopID(productModel.getShopID());
        productItem.setShopName(productModel.getShopName());
        productItem.setShopCity(productModel.getShopCity());
        productItem.setGoldMerchant(productModel.isGoldMerchant());
        productItem.setOfficial(productModel.isOfficial());
        productItem.setOfficial(productModel.isOfficial());
        productItem.setWishlisted(productModel.isWishlisted());
        productItem.setBadgesList(convertToBadgesItemList(productModel.getBadgesList()));
        productItem.setLabelList(convertToLabelsItemList(productModel.getLabelList()));
        productItem.setPosition(position);
        productItem.setCategoryID(productModel.getCategoryId());
        productItem.setTopLabel(productModel.getTopLabel());
        productItem.setBottomLabel(productModel.getBottomLabel());
        return productItem;
    }

    private static List<BadgeItem> convertToBadgesItemList(List<BadgeModel> badgesList) {
        List<BadgeItem> badgeItemList = new ArrayList<>();

        for (BadgeModel badgeModel : badgesList) {
            badgeItemList.add(convertToBadgeItem(badgeModel));
        }
        return badgeItemList;
    }

    private static BadgeItem convertToBadgeItem(BadgeModel badgeModel) {
        BadgeItem badgeItem = new BadgeItem();
        badgeItem.setImageUrl(badgeModel.getImageUrl());
        badgeItem.setTitle(badgeModel.getTitle());
        badgeItem.setShown(badgeModel.isShown());
        return badgeItem;
    }

    private static List<LabelItem> convertToLabelsItemList(List<LabelModel> labelList) {
        List<LabelItem> labelItemList = new ArrayList<>();
        for (LabelModel labelModel : labelList) {
            labelItemList.add(convertToLabelItem(labelModel));
        }
        return labelItemList;
    }

    private static LabelItem convertToLabelItem(LabelModel labelModel) {
        LabelItem labelItem = new LabelItem();
        labelItem.setTitle(labelModel.getTitle());
        labelItem.setColor(labelModel.getColor());
        return labelItem;
    }
}
