package com.tokopedia.search.result.presentation.mapper;

import com.tokopedia.filter.common.data.DataValue;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.search.result.domain.model.OtherRelated;
import com.tokopedia.search.result.domain.model.OtherRelatedProduct;
import com.tokopedia.search.result.domain.model.Product;
import com.tokopedia.search.result.domain.model.ProductBadge;
import com.tokopedia.search.result.domain.model.ProductFreeOngkir;
import com.tokopedia.search.result.domain.model.ProductLabelGroup;
import com.tokopedia.search.result.domain.model.Related;
import com.tokopedia.search.result.domain.model.SearchProduct;
import com.tokopedia.search.result.domain.model.SearchProductData;
import com.tokopedia.search.result.domain.model.SearchProductHeader;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.search.result.domain.model.Suggestion;
import com.tokopedia.search.result.domain.model.Ticker;
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel;
import com.tokopedia.search.result.presentation.model.BroadMatchItemViewModel;
import com.tokopedia.search.result.presentation.model.BroadMatchViewModel;
import com.tokopedia.search.result.presentation.model.FreeOngkirViewModel;
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel;
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel;
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
        SearchProduct aceSearchProduct = searchProductModel.getAceSearchProduct();
        SearchProductHeader searchProductHeader = aceSearchProduct.getHeader();
        SearchProductData searchProductData = aceSearchProduct.getData();

        ProductViewModel productViewModel = new ProductViewModel();
        productViewModel.setAdsModel(searchProductModel.getTopAdsModel());
        if (isListContainItems(searchProductModel.getGlobalNavModel().getData().getGlobalNavItems())) {
            productViewModel.setGlobalNavViewModel(convertToViewModel(searchProductModel.getGlobalNavModel()));
        }
        productViewModel.setCpmModel(searchProductModel.getCpmModel());
        productViewModel.setRelatedViewModel(convertToRelatedViewModel(searchProductData.getRelated()));
        productViewModel.setProductList(convertToProductItemViewModelList(lastProductItemPositionFromCache, searchProductData.getProductList(), useRatingString));
        productViewModel.setAdsModel(searchProductModel.getTopAdsModel());
        productViewModel.setTickerModel(createTickerModel(searchProductData));
        productViewModel.setSuggestionModel(createSuggestionModel(searchProductData));
        productViewModel.setTotalData(searchProductHeader.getTotalData());
        productViewModel.setTotalDataText(searchProductHeader.getTotalDataText());
        productViewModel.setResponseCode(searchProductHeader.getResponseCode());
        productViewModel.setKeywordProcess(searchProductHeader.getKeywordProcess());
        productViewModel.setErrorMessage(searchProductHeader.getErrorMessage());
        productViewModel.setIsQuerySafe(searchProductData.isQuerySafe());
        if (searchProductModel.getDynamicFilterModel() != null) {
            productViewModel.setDynamicFilterModel(searchProductModel.getDynamicFilterModel());
        }
        if (searchProductModel.getQuickFilterModel() != null) {
            productViewModel.setQuickFilterModel(
                    convertToQuickFilterViewModel(
                            searchProductModel.getQuickFilterModel(),
                            searchProductHeader.getTotalDataText()
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
        productViewModel.setAdditionalParams(searchProductHeader.getAdditionalParams());
        productViewModel.setAutocompleteApplink(searchProductData.getAutocompleteApplink());
        productViewModel.setDefaultView(searchProductHeader.getDefaultView());

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

    private RelatedViewModel convertToRelatedViewModel(Related related) {
        List<BroadMatchViewModel> broadMatchViewModelList = new ArrayList<>();
        for (OtherRelated otherRelated: related.getOtherRelatedList()) {
            broadMatchViewModelList.add(convertToBroadMatchViewModel(otherRelated));
        }

        return new RelatedViewModel(
                related.getRelatedKeyword(),
                broadMatchViewModelList
        );
    }

    private BroadMatchViewModel convertToBroadMatchViewModel(OtherRelated otherRelated) {
        List<BroadMatchItemViewModel> broadMatchItemViewModelList = new ArrayList<>();
        int position = 0;
        for (OtherRelatedProduct otherRelatedProduct: otherRelated.getProductList()) {
            position++;
            broadMatchItemViewModelList.add(convertToBroadMatchItemViewModel(otherRelatedProduct, position, otherRelated.getKeyword()));
        }

        return new BroadMatchViewModel(
                otherRelated.getKeyword(),
                otherRelated.getUrl(),
                otherRelated.getApplink(),
                broadMatchItemViewModelList
        );
    }

    private BroadMatchItemViewModel convertToBroadMatchItemViewModel(
            OtherRelatedProduct otherRelatedProduct,
            int position,
            String alternativeKeyword
    ) {
        return new BroadMatchItemViewModel(
                otherRelatedProduct.getId(),
                otherRelatedProduct.getName(),
                otherRelatedProduct.getPrice(),
                otherRelatedProduct.getImageUrl(),
                otherRelatedProduct.getRating(),
                otherRelatedProduct.getCountReview(),
                otherRelatedProduct.getUrl(),
                otherRelatedProduct.getApplink(),
                otherRelatedProduct.getPriceString(),
                position,
                alternativeKeyword
        );
    }

    private List<ProductItemViewModel> convertToProductItemViewModelList(int lastProductItemPositionFromCache, List<Product> productModels, boolean useRatingString) {
        List<ProductItemViewModel> productItemList = new ArrayList<>();

        int position = lastProductItemPositionFromCache;

        for (Product productModel : productModels) {
            position++;
            productItemList.add(convertToProductItem(productModel, position, useRatingString));
        }

        return productItemList;
    }

    private ProductItemViewModel convertToProductItem(Product productModel, int position, boolean useRatingString) {
        ProductItemViewModel productItem = new ProductItemViewModel();
        productItem.setProductID(productModel.getId());
        productItem.setWarehouseID(productModel.getWarehouseIdDefault());
        productItem.setProductName(productModel.getName());
        productItem.setImageUrl(productModel.getImageUrl());
        productItem.setImageUrl300(productModel.getImageUrl300());
        productItem.setImageUrl700(productModel.getImageUrl700());
        productItem.setRatingString(useRatingString ? productModel.getRatingAverage() : "");
        productItem.setRating(useRatingString ? 0 : productModel.getRating());
        productItem.setCountReview(productModel.getCountReview());
        productItem.setDiscountPercentage(productModel.getDiscountPercentage());
        productItem.setOriginalPrice(productModel.getOriginalPrice());
        productItem.setPrice(productModel.getPrice());
        productItem.setPriceInt(productModel.getPriceInt());
        productItem.setPriceRange(productModel.getPriceRange());
        productItem.setShopID(productModel.getShop().getId());
        productItem.setShopCity(productModel.getShop().getCity());
        productItem.setWishlisted(productModel.isWishlist());
        productItem.setBadgesList(convertToBadgesItemList(productModel.getBadgeList()));
        productItem.setPosition(position);
        productItem.setCategoryID(productModel.getCategoryId());
        productItem.setCategoryName(productModel.getCategoryName());
        productItem.setCategoryBreadcrumb(productModel.getCategoryBreadcrumb());
        productItem.setLabelGroupList(convertToLabelGroupList(productModel.getLabelGroupList()));
        productItem.setFreeOngkirViewModel(convertToFreeOngkirViewModel(productModel.getFreeOngkir()));
        productItem.setBoosterList(productModel.getBoosterList());
        productItem.setIsOrganicAds(productModel.isOrganicAds());
        productItem.setTopadsImpressionUrl(productModel.getAds().getProductViewUrl());
        productItem.setTopadsClickUrl(productModel.getAds().getProductClickUrl());
        productItem.setTopadsWishlistUrl(productModel.getAds().getProductWishlistUrl());
        return productItem;
    }

    private List<BadgeItemViewModel> convertToBadgesItemList(List<ProductBadge> badgesList) {
        List<BadgeItemViewModel> badgeItemList = new ArrayList<>();

        for (ProductBadge badgeModel : badgesList) {
            badgeItemList.add(convertToBadgeItem(badgeModel));
        }
        return badgeItemList;
    }

    private BadgeItemViewModel convertToBadgeItem(ProductBadge badgeModel) {
        BadgeItemViewModel badgeItem = new BadgeItemViewModel();
        badgeItem.setImageUrl(badgeModel.getImageUrl());
        badgeItem.setTitle(badgeModel.getTitle());
        badgeItem.setShown(badgeModel.isShown());
        return badgeItem;
    }

    private List<LabelGroupViewModel> convertToLabelGroupList(List<ProductLabelGroup> labelGroupModelList) {
        List<LabelGroupViewModel> labelGroupViewModelList = new ArrayList<>();
        for(ProductLabelGroup labelGroupModel : labelGroupModelList) {
            labelGroupViewModelList.add(convertToLabelGroupViewModel(labelGroupModel));
        }

        return labelGroupViewModelList;
    }

    private LabelGroupViewModel convertToLabelGroupViewModel(ProductLabelGroup labelGroupModel) {
        return new LabelGroupViewModel(
                labelGroupModel.getPosition(),
                labelGroupModel.getType(),
                labelGroupModel.getTitle()
        );
    }

    private FreeOngkirViewModel convertToFreeOngkirViewModel(ProductFreeOngkir freeOngkir) {
        return new FreeOngkirViewModel(freeOngkir.isActive(), freeOngkir.getImageUrl());
    }

    private TickerViewModel createTickerModel(SearchProductData searchProductData) {
        Ticker tickerModel = searchProductData.getTicker();

        TickerViewModel tickerViewModel = new TickerViewModel();
        tickerViewModel.setText(tickerModel.getText());
        tickerViewModel.setQuery(tickerModel.getQuery());
        tickerViewModel.setTypeId(tickerModel.getTypeId());

        return tickerViewModel;
    }

    private SuggestionViewModel createSuggestionModel(SearchProductData searchProduct) {
        Suggestion suggestionModel = searchProduct.getSuggestion();

        SuggestionViewModel suggestionViewModel = new SuggestionViewModel();
        suggestionViewModel.setSuggestionText(suggestionModel.getText());
        suggestionViewModel.setSuggestedQuery(suggestionModel.getQuery());
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
