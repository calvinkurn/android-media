package com.tokopedia.search.result.presentation.mapper;

import android.text.TextUtils;

import com.tokopedia.search.result.domain.model.GuidedSearchModel;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel;
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;
import com.tokopedia.search.result.presentation.model.GuidedSearchViewModel;
import com.tokopedia.search.result.presentation.model.LabelItemViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel;
import com.tokopedia.search.result.presentation.model.ProductViewModel;
import com.tokopedia.search.result.presentation.model.RelatedSearchViewModel;
import com.tokopedia.search.result.presentation.model.SuggestionViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModelMapper {

    public ProductViewModel convertToProductViewModel(int lastProductItemPositionFromCache, SearchProductModel searchProductModel) {
        SearchProductModel.SearchProduct searchProduct = searchProductModel.getSearchProduct();
        ProductViewModel productViewModel = new ProductViewModel();
        productViewModel.setAdsModel(searchProductModel.getTopAdsModel());
        if (isListContainItems(searchProductModel.getGlobalNavModel().getData().getGlobalNavItems())) {
            productViewModel.setGlobalNavViewModel(convertToViewModel(searchProductModel.getGlobalNavModel()));
        }
        productViewModel.setCpmModel(searchProductModel.getCpmModel());
        if (searchProduct.getRelated() != null &&
                !TextUtils.isEmpty(searchProduct.getRelated().getRelatedKeyword())) {
            productViewModel.setRelatedSearchModel(convertToRelatedSearchModel(searchProduct.getRelated()));
        } else if (searchProductModel.getGuidedSearchModel() != null) {
            productViewModel.setGuidedSearchViewModel(
                    convertToGuidedSearchViewModel(
                            searchProductModel.getGuidedSearchModel(),
                            searchProduct.getQuery()
                    )
            );
        }
        productViewModel.setProductList(convertToProductItemViewModelList(lastProductItemPositionFromCache, searchProduct.getProducts()));
        productViewModel.setAdsModel(searchProductModel.getTopAdsModel());
        productViewModel.setQuery(searchProduct.getQuery());
        productViewModel.setShareUrl(searchProduct.getShareUrl());
        productViewModel.setSuggestionModel(createSuggestionModel(searchProduct));
        productViewModel.setTotalData(searchProduct.getCount());
        productViewModel.setQuerySafe(searchProduct.isQuerySafe());
        if (searchProductModel.getDynamicFilterModel() != null) {
            productViewModel.setDynamicFilterModel(searchProductModel.getDynamicFilterModel());
        }
        if (searchProductModel.getQuickFilterModel() != null) {
            productViewModel.setQuickFilterModel(searchProductModel.getQuickFilterModel());
        }
        productViewModel.setAdditionalParams(searchProduct.getAdditionalParams());
        productViewModel.setIsQuerySafe(searchProduct.isQuerySafe());

        return productViewModel;
    }

    private boolean isListContainItems(List list) {
        return list != null && !list.isEmpty();
    }

    private GlobalNavViewModel convertToViewModel(SearchProductModel.GlobalNavModel globalNavModel) {
        return new GlobalNavViewModel(
                globalNavModel.getData().getTitle(),
                globalNavModel.getData().getKeyword(),
                globalNavModel.getData().getSeeAllApplink(),
                globalNavModel.getData().getSeeAllUrl(),
                convertToViewModel(globalNavModel.getData().getGlobalNavItems())
        );
    }

    private  List<GlobalNavViewModel.Item> convertToViewModel(List<SearchProductModel.GlobalNavItem> globalNavItems) {
        List<GlobalNavViewModel.Item> itemList = new ArrayList<>();

        int position = 1;
        for (SearchProductModel.GlobalNavItem item : globalNavItems) {
            itemList.add(new GlobalNavViewModel.Item(
                    item.getName(),
                    item.getInfo(),
                    item.getImageUrl(),
                    item.getApplink(),
                    item.getUrl(),
                    position
            ));
            position++;
        }

        return itemList;
    }

    private RelatedSearchViewModel convertToRelatedSearchModel(SearchProductModel.Related related) {
        RelatedSearchViewModel relatedSearchModel = new RelatedSearchViewModel();
        relatedSearchModel.setRelatedKeyword(related.getRelatedKeyword());

        List<RelatedSearchViewModel.OtherRelated> otherRelatedList = new ArrayList<>();
        for (SearchProductModel.OtherRelated otherRelatedResponse : related.getOtherRelated()) {
            RelatedSearchViewModel.OtherRelated otherRelatedViewModel = new RelatedSearchViewModel.OtherRelated();
            otherRelatedViewModel.setKeyword(otherRelatedResponse.getKeyword());
            otherRelatedViewModel.setUrl(otherRelatedResponse.getUrl());
            otherRelatedList.add(otherRelatedViewModel);
        }
        relatedSearchModel.setOtherRelated(otherRelatedList);

        return relatedSearchModel;
    }

    private GuidedSearchViewModel convertToGuidedSearchViewModel(GuidedSearchModel guidedSearchModel, String query) {
        GuidedSearchViewModel model = new GuidedSearchViewModel();
        List<GuidedSearchViewModel.Item> itemList = new ArrayList<>();

        if (guidedSearchModel.getData() != null) {
            for (int position = 0; position < guidedSearchModel.getData().size(); position++) {
                itemList.add(mappingGuidedSearchItem(guidedSearchModel.getData().get(position), position, query));
            }
        }
        model.setItemList(itemList);
        return model;
    }

    private GuidedSearchViewModel.Item mappingGuidedSearchItem(GuidedSearchModel.GuidedSearchItem networkItem, int position, String query) {
        GuidedSearchViewModel.Item viewModelItem = new GuidedSearchViewModel.Item();
        viewModelItem.setKeyword(networkItem.getKeyword());
        viewModelItem.setUrl(networkItem.getUrl());
        viewModelItem.setPosition(position);
        viewModelItem.setPreviousKey(query);
        return viewModelItem;
    }

    private List<ProductItemViewModel> convertToProductItemViewModelList(int lastProductItemPositionFromCache, List<SearchProductModel.Product> productModels) {
        List<ProductItemViewModel> productItemList = new ArrayList<>();

        int position = lastProductItemPositionFromCache;

        for (SearchProductModel.Product productModel : productModels) {
            position++;
            productItemList.add(convertToProductItem(productModel, position));
        }

        return productItemList;
    }

    private ProductItemViewModel convertToProductItem(SearchProductModel.Product productModel, int position) {
        ProductItemViewModel productItem = new ProductItemViewModel();
        productItem.setProductID(productModel.getId());
        productItem.setProductName(productModel.getName());
        productItem.setImageUrl(productModel.getImageUrl());
        productItem.setImageUrl700(productModel.getImageUrlLarge());
        productItem.setRating(productModel.getRating());
        productItem.setCountReview(productModel.getCountReview());
        productItem.setCountCourier(productModel.getCourierCount());
        productItem.setDiscountPercentage(productModel.getDiscountPercentage());
        productItem.setOriginalPrice(productModel.getOriginalPrice());
        productItem.setPrice(productModel.getPrice());
        productItem.setPriceRange(productModel.getPriceRange());
        productItem.setShopID(productModel.getShop().getId());
        productItem.setShopName(productModel.getShop().getName());
        productItem.setShopCity(productModel.getShop().getCity());
        productItem.setGoldMerchant(productModel.getShop().isGoldmerchant());
        productItem.setWishlisted(productModel.isWishlist());
        productItem.setBadgesList(convertToBadgesItemList(productModel.getBadges()));
        productItem.setLabelList(convertToLabelsItemList(productModel.getLabels()));
        productItem.setPosition(position);
        productItem.setTopLabel(isListContainItems(productModel.getTopLabel()) ? productModel.getTopLabel().get(0) : "");
        productItem.setBottomLabel(isListContainItems(productModel.getBottomLabel()) ? productModel.getBottomLabel().get(0) : "");
        productItem.setCategoryID(productModel.getCategoryId());
        productItem.setCategoryName(productModel.getCategoryName());
        productItem.setCategoryBreadcrumb(productModel.getCategoryBreadcrumb());
        productItem.setLabelGroupList(convertToLabelGroupList(productModel.getLabelGroupList()));
        productItem.setIsShopPowerBadge(productModel.getShop().isPowerBadge());
        productItem.setIsShopOfficialStore(productModel.getShop().isOfficial());
        return productItem;
    }

    private List<BadgeItemViewModel> convertToBadgesItemList(List<SearchProductModel.Badge> badgesList) {
        List<BadgeItemViewModel> badgeItemList = new ArrayList<>();

        for (SearchProductModel.Badge badgeModel : badgesList) {
            badgeItemList.add(convertToBadgeItem(badgeModel));
        }
        return badgeItemList;
    }

    private BadgeItemViewModel convertToBadgeItem(SearchProductModel.Badge badgeModel) {
        BadgeItemViewModel badgeItem = new BadgeItemViewModel();
        badgeItem.setImageUrl(badgeModel.getImageUrl());
        badgeItem.setTitle(badgeModel.getTitle());
        badgeItem.setShown(badgeModel.isShown());
        return badgeItem;
    }

    private List<LabelItemViewModel> convertToLabelsItemList(List<SearchProductModel.Label> labelList) {
        List<LabelItemViewModel> labelItemList = new ArrayList<>();
        for (SearchProductModel.Label labelModel : labelList) {
            labelItemList.add(convertToLabelItem(labelModel));
        }
        return labelItemList;
    }

    private LabelItemViewModel convertToLabelItem(SearchProductModel.Label labelModel) {
        LabelItemViewModel labelItem = new LabelItemViewModel();
        labelItem.setTitle(labelModel.getTitle());
        labelItem.setColor(labelModel.getColor());
        return labelItem;
    }

    private List<LabelGroupViewModel> convertToLabelGroupList(List<SearchProductModel.LabelGroup> labelGroupModelList) {
        List<LabelGroupViewModel> labelGroupViewModelList = new ArrayList<>();
        for(SearchProductModel.LabelGroup labelGroupModel : labelGroupModelList) {
            labelGroupViewModelList.add(convertToLabelGroupViewModel(labelGroupModel));
        }

        return labelGroupViewModelList;
    }

    private LabelGroupViewModel convertToLabelGroupViewModel(SearchProductModel.LabelGroup labelGroupModel) {
        LabelGroupViewModel labelGroupViewModel =
                new LabelGroupViewModel(
                        labelGroupModel.getPosition(),
                        labelGroupModel.getType(),
                        labelGroupModel.getTitle()
                );

        return labelGroupViewModel;
    }

    private SuggestionViewModel createSuggestionModel(SearchProductModel.SearchProduct searchProduct) {
        SearchProductModel.Suggestion suggestionModel = searchProduct.getSuggestion();
        SuggestionViewModel suggestionViewModel = new SuggestionViewModel();
        suggestionViewModel.setSuggestionText(suggestionModel.getText());
        suggestionViewModel.setSuggestedQuery(suggestionModel.getQuery());
        suggestionViewModel.setSuggestionCurrentKeyword(suggestionModel.getCurrentKeyword());
        suggestionViewModel.setFormattedResultCount(searchProduct.getCountText());
        return suggestionViewModel;
    }
}
