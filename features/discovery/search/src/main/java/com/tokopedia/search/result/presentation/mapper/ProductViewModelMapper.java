package com.tokopedia.search.result.presentation.mapper;

import com.tokopedia.filter.common.data.DataValue;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel;
import com.tokopedia.search.result.presentation.model.BroadMatchItemViewModel;
import com.tokopedia.search.result.presentation.model.BroadMatchViewModel;
import com.tokopedia.search.result.presentation.model.FreeOngkirViewModel;
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel;
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel;
import com.tokopedia.search.result.presentation.model.LabelItemViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.model.ProductViewModel;
import com.tokopedia.search.result.presentation.model.QuickFilterViewModel;
import com.tokopedia.search.result.presentation.model.RelatedViewModel;
import com.tokopedia.search.result.presentation.model.SuggestionViewModel;
import com.tokopedia.search.result.presentation.model.TickerViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModelMapper {

    public ProductViewModel convertToProductViewModel(int lastProductItemPositionFromCache, SearchProductModel searchProductModel, boolean useRatingString) {
        SearchProductModel.SearchProduct searchProduct = searchProductModel.getSearchProduct();
        ProductViewModel productViewModel = new ProductViewModel();
        productViewModel.setAdsModel(searchProductModel.getTopAdsModel());
        if (isListContainItems(searchProductModel.getGlobalNavModel().getData().getGlobalNavItems())) {
            productViewModel.setGlobalNavViewModel(convertToViewModel(searchProductModel.getGlobalNavModel()));
        }
        productViewModel.setCpmModel(searchProductModel.getCpmModel());
        if (searchProduct.getRelated() != null) {
            productViewModel.setRelatedViewModel(convertToRelatedViewModel(searchProduct.getRelated()));
        }
        productViewModel.setProductList(convertToProductItemViewModelList(lastProductItemPositionFromCache, searchProduct.getProducts(), useRatingString));
        productViewModel.setAdsModel(searchProductModel.getTopAdsModel());
        productViewModel.setQuery(searchProduct.getQuery());
        productViewModel.setTickerModel(createTickerModel(searchProduct));
        productViewModel.setSuggestionModel(createSuggestionModel(searchProduct));
        productViewModel.setTotalData(searchProduct.getCount());
        productViewModel.setResponseCode(searchProduct.getResponseCode());
        productViewModel.setKeywordProcess(searchProduct.getKeywordProcess());
        productViewModel.setErrorMessage(searchProduct.getErrorMessage());
        productViewModel.setIsQuerySafe(searchProduct.isQuerySafe());
        if (searchProductModel.getDynamicFilterModel() != null) {
            productViewModel.setDynamicFilterModel(searchProductModel.getDynamicFilterModel());
        }
        if (searchProductModel.getQuickFilterModel() != null) {
            productViewModel.setQuickFilterModel(
                    convertToQuickFilterViewModel(
                            searchProductModel.getQuickFilterModel(),
                            searchProduct.getCountText()
                    )
            );
        }
        if (searchProductModel.getSearchInspirationCarousel() != null) {
            productViewModel
                    .setInspirationCarouselViewModel(
                            convertToInspirationCarouselViewModel(searchProductModel.getSearchInspirationCarousel()
                            )
                    );
        }
        productViewModel.setAdditionalParams(searchProduct.getAdditionalParams());
        productViewModel.setAutocompleteApplink(searchProduct.getAutocompleteApplink());
        productViewModel.setDefaultView(searchProduct.getDefaultView());

        return productViewModel;
    }

    private QuickFilterViewModel convertToQuickFilterViewModel(DataValue dynamicFilterModel, String formattedResultCount) {
        QuickFilterViewModel quickFilterViewModel = new QuickFilterViewModel();
        quickFilterViewModel.setFormattedResultCount(formattedResultCount);
        quickFilterViewModel.setQuickFilterList(dynamicFilterModel.getFilter());
        quickFilterViewModel.setQuickFilterOptions(getQuickFilterOptions(dynamicFilterModel));
        return quickFilterViewModel;
    }

    private List<Option> getQuickFilterOptions(DataValue dynamicFilterModel) {
        ArrayList<Option> optionList = new ArrayList<>();

        if (dynamicFilterModel.getFilter() == null) {
            return optionList;
        }

        for (Filter filter : dynamicFilterModel.getFilter()) {
            optionList.addAll(filter.getOptions());
        }

        return optionList;
    }

    private boolean isListContainItems(List list) {
        return list != null && !list.isEmpty();
    }

    private GlobalNavViewModel convertToViewModel(SearchProductModel.GlobalNavModel globalNavModel) {
        return new GlobalNavViewModel(
                globalNavModel.getData().getSource(),
                globalNavModel.getData().getTitle(),
                globalNavModel.getData().getKeyword(),
                globalNavModel.getData().getNavTemplate(),
                globalNavModel.getData().getBackground(),
                globalNavModel.getData().getSeeAllApplink(),
                globalNavModel.getData().getSeeAllUrl(),
                globalNavModel.getData().getIsShowTopAds(),
                convertToViewModel(globalNavModel.getData().getGlobalNavItems())
        );
    }

    private  List<GlobalNavViewModel.Item> convertToViewModel(List<SearchProductModel.GlobalNavItem> globalNavItems) {
        List<GlobalNavViewModel.Item> itemList = new ArrayList<>();

        int position = 1;
        for (SearchProductModel.GlobalNavItem item : globalNavItems) {
            itemList.add(new GlobalNavViewModel.Item(
                    item.getCategoryName(),
                    item.getName(),
                    item.getInfo(),
                    item.getImageUrl(),
                    item.getApplink(),
                    item.getUrl(),
                    item.getSubtitle(),
                    item.getStrikethrough(),
                    item.getBackgroundUrl(),
                    item.getLogoUrl(),
                    position
            ));
            position++;
        }

        return itemList;
    }

    private RelatedViewModel convertToRelatedViewModel(SearchProductModel.Related related) {
        List<BroadMatchViewModel> broadMatchViewModelList = new ArrayList<>();
        for (SearchProductModel.OtherRelated otherRelated: related.getOtherRelated()) {
            broadMatchViewModelList.add(convertToBroadMatchViewModel(otherRelated));
        }

        return new RelatedViewModel(
                related.getRelatedKeyword(),
                broadMatchViewModelList
        );
    }

    private BroadMatchViewModel convertToBroadMatchViewModel(SearchProductModel.OtherRelated otherRelated) {
        List<BroadMatchItemViewModel> broadMatchItemViewModelList = new ArrayList<>();
        for (SearchProductModel.OtherRelatedProduct otherRelatedProduct: otherRelated.getOtherRelatedProductList()) {
            broadMatchItemViewModelList.add(convertToBroadMatchItemViewModel(otherRelatedProduct));
        }

        return new BroadMatchViewModel(
                otherRelated.getKeyword(),
                otherRelated.getUrl(),
                otherRelated.getApplink(),
                broadMatchItemViewModelList
        );
    }

    private BroadMatchItemViewModel convertToBroadMatchItemViewModel(SearchProductModel.OtherRelatedProduct otherRelatedProduct) {
        return new BroadMatchItemViewModel(
                otherRelatedProduct.getId(),
                otherRelatedProduct.getName(),
                otherRelatedProduct.getPrice(),
                otherRelatedProduct.getImageUrl(),
                otherRelatedProduct.getRating(),
                otherRelatedProduct.getCountReview(),
                otherRelatedProduct.getUrl(),
                otherRelatedProduct.getApplink(),
                otherRelatedProduct.getPriceString()
        );
    }

    private List<ProductItemViewModel> convertToProductItemViewModelList(int lastProductItemPositionFromCache, List<SearchProductModel.Product> productModels, boolean useRatingString) {
        List<ProductItemViewModel> productItemList = new ArrayList<>();

        int position = lastProductItemPositionFromCache;

        for (SearchProductModel.Product productModel : productModels) {
            position++;
            productItemList.add(convertToProductItem(productModel, position, useRatingString));
        }

        return productItemList;
    }

    private ProductItemViewModel convertToProductItem(SearchProductModel.Product productModel, int position, boolean useRatingString) {
        ProductItemViewModel productItem = new ProductItemViewModel();
        productItem.setProductID(productModel.getId());
        productItem.setWarehouseID(productModel.getWarehouseId());
        productItem.setProductName(productModel.getName());
        productItem.setImageUrl(productModel.getImageUrl());
        productItem.setImageUrl700(productModel.getImageUrlLarge());
        productItem.setRatingString(useRatingString ? productModel.getRatingAverage() : "");
        productItem.setRating(useRatingString ? 0 : productModel.getRating());
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
        productItem.setFreeOngkirViewModel(convertToFreeOngkirViewModel(productModel.getFreeOngkir()));
        productItem.setBoosterList(productModel.getBoosterList());
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

    private FreeOngkirViewModel convertToFreeOngkirViewModel(SearchProductModel.FreeOngkir freeOngkir) {
        return new FreeOngkirViewModel(freeOngkir.isActive(), freeOngkir.getImageUrl());
    }

    private TickerViewModel createTickerModel(SearchProductModel.SearchProduct searchProduct) {
        SearchProductModel.Ticker tickerModel = searchProduct.getTicker();
        TickerViewModel tickerViewModel = new TickerViewModel();
        tickerViewModel.setText(tickerModel.getText());
        tickerViewModel.setQuery(tickerModel.getQuery());
        return tickerViewModel;
    }

    private SuggestionViewModel createSuggestionModel(SearchProductModel.SearchProduct searchProduct) {
        SearchProductModel.Suggestion suggestionModel = searchProduct.getSuggestion();
        SuggestionViewModel suggestionViewModel = new SuggestionViewModel();
        suggestionViewModel.setSuggestionText(suggestionModel.getText());
        suggestionViewModel.setSuggestedQuery(suggestionModel.getQuery());
        suggestionViewModel.setSuggestionCurrentKeyword(suggestionModel.getCurrentKeyword());
        suggestionViewModel.setFormattedResultCount(searchProduct.getCountText());
        suggestionViewModel.setSuggestion(suggestionModel.getSuggestion());
        return suggestionViewModel;
    }

    private  List<InspirationCarouselViewModel> convertToInspirationCarouselViewModel(SearchProductModel.SearchInspirationCarousel searchInspirationCarousel) {
        List<InspirationCarouselViewModel> inspirationCarousel = new ArrayList<>();

        for (SearchProductModel.InspirationCarouselData data : searchInspirationCarousel.getData()) {
            inspirationCarousel.add(new InspirationCarouselViewModel(
                    data.getTitle(),
                    data.getType(),
                    data.getPosition(),
                    convertToInspirationCarouselOptionViewModel(data.getInspirationCarouselOptions(), data.getType())
            ));
        }

        return inspirationCarousel;
    }

    private  List<InspirationCarouselViewModel.Option> convertToInspirationCarouselOptionViewModel(List<SearchProductModel.InspirationCarouselOption> inspirationCarouselOptions, String inspirationCarouselType) {
        List<InspirationCarouselViewModel.Option> options = new ArrayList<>();

        for (SearchProductModel.InspirationCarouselOption opt : inspirationCarouselOptions) {
            int position = inspirationCarouselOptions.indexOf(opt) + 1;
            options.add(new InspirationCarouselViewModel.Option(
                    opt.getTitle(),
                    opt.getUrl(),
                    opt.getApplink(),
                    convertToInspirationCarouselProductViewModel(opt.getInspirationCarouselProducts(), position, inspirationCarouselType),
                    inspirationCarouselType
            ));
        }

        return options;
    }

    private  List<InspirationCarouselViewModel.Option.Product> convertToInspirationCarouselProductViewModel(List<SearchProductModel.InspirationCarouselProduct> inspirationCarouselProduct, int position, String inspirationCarouselType) {
        List<InspirationCarouselViewModel.Option.Product> products = new ArrayList<>();

        for (SearchProductModel.InspirationCarouselProduct product : inspirationCarouselProduct) {
            products.add(new InspirationCarouselViewModel.Option.Product(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getPriceStr(),
                    product.getImgUrl(),
                    product.getRating(),
                    product.countReview(),
                    product.getUrl(),
                    product.getApplink(),
                    position,
                    inspirationCarouselType
            ));
        }

        return products;
    }
}
