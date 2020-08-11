package com.tokopedia.search.result.presentation.mapper;

import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel;
import com.tokopedia.search.result.presentation.model.BroadMatchItemViewModel;
import com.tokopedia.search.result.presentation.model.BroadMatchViewModel;
import com.tokopedia.search.result.presentation.model.FreeOngkirViewModel;
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;
import com.tokopedia.search.result.presentation.model.InspirationCardOptionViewModel;
import com.tokopedia.search.result.presentation.model.InspirationCardViewModel;
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel;
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.model.ProductViewModel;
import com.tokopedia.search.result.presentation.model.RelatedViewModel;
import com.tokopedia.search.result.presentation.model.SuggestionViewModel;
import com.tokopedia.search.result.presentation.model.TickerViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModelMapper {

    public ProductViewModel convertToProductViewModel(int lastProductItemPositionFromCache, SearchProductModel searchProductModel, boolean useRatingString) {
        SearchProductModel.SearchProduct aceSearchProduct = searchProductModel.getSearchProduct();
        SearchProductModel.SearchProductHeader searchProductHeader = aceSearchProduct.getHeader();
        SearchProductModel.SearchProductData searchProductData = aceSearchProduct.getData();

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
        productViewModel.setInspirationCarouselViewModel(
            convertToInspirationCarouselViewModel(searchProductModel.getSearchInspirationCarousel())
        );
        productViewModel.setInspirationCardViewModel(
            convertToInspirationCardViewModel(searchProductModel.getSearchInspirationCard())
        );
        productViewModel.setAdditionalParams(searchProductHeader.getAdditionalParams());
        productViewModel.setAutocompleteApplink(searchProductData.getAutocompleteApplink());
        productViewModel.setDefaultView(searchProductHeader.getDefaultView());

        return productViewModel;
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
                globalNavModel.getData().isShowTopAds(),
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
        for (SearchProductModel.OtherRelated otherRelated: related.getOtherRelatedList()) {
            broadMatchViewModelList.add(convertToBroadMatchViewModel(otherRelated));
        }

        return new RelatedViewModel(
                related.getRelatedKeyword(),
                related.getPosition(),
                broadMatchViewModelList
        );
    }

    private BroadMatchViewModel convertToBroadMatchViewModel(SearchProductModel.OtherRelated otherRelated) {
        List<BroadMatchItemViewModel> broadMatchItemViewModelList = new ArrayList<>();
        int position = 0;
        for (SearchProductModel.OtherRelatedProduct otherRelatedProduct: otherRelated.getProductList()) {
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
            SearchProductModel.OtherRelatedProduct otherRelatedProduct,
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
                otherRelatedProduct.getShop().getCity(),
                convertOtherRelatedProductBadgeToBadgesItemList(otherRelatedProduct.getBadgeList()),
                convertOtherRelatedProductFreeOngkirToFreeOngkirViewModel(otherRelatedProduct.getFreeOngkir()),
                otherRelatedProduct.isWishlisted(),
                position,
                alternativeKeyword
        );
    }

    private List<BadgeItemViewModel> convertOtherRelatedProductBadgeToBadgesItemList(
            List<SearchProductModel.OtherRelatedProductBadge> badgesList
    ) {
        List<BadgeItemViewModel> badgeItemList = new ArrayList<>();

        for (SearchProductModel.OtherRelatedProductBadge badgeModel : badgesList) {
            badgeItemList.add(convertOtherRelatedProductBadgeToBadgeItem(badgeModel));
        }
        return badgeItemList;
    }

    private BadgeItemViewModel convertOtherRelatedProductBadgeToBadgeItem(
            SearchProductModel.OtherRelatedProductBadge badgeModel
    ) {
        BadgeItemViewModel badgeItem = new BadgeItemViewModel();
        badgeItem.setImageUrl(badgeModel.getImageUrl());
        badgeItem.setShown(badgeModel.isShown());
        return badgeItem;
    }

    private FreeOngkirViewModel convertOtherRelatedProductFreeOngkirToFreeOngkirViewModel(
            SearchProductModel.OtherRelatedProductFreeOngkir freeOngkir
    ) {
        return new FreeOngkirViewModel(freeOngkir.isActive(), freeOngkir.getImageUrl());
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
        productItem.setShopName(productModel.getShop().getName());
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
        productItem.setSourceEngine(productModel.getSourceEngine());
        productItem.setOrganicAds(productModel.isOrganicAds());
        productItem.setTopadsImpressionUrl(productModel.getAds().getProductViewUrl());
        productItem.setTopadsClickUrl(productModel.getAds().getProductClickUrl());
        productItem.setTopadsWishlistUrl(productModel.getAds().getProductWishlistUrl());
        return productItem;
    }

    private List<BadgeItemViewModel> convertToBadgesItemList(List<SearchProductModel.ProductBadge> badgesList) {
        List<BadgeItemViewModel> badgeItemList = new ArrayList<>();

        for (SearchProductModel.ProductBadge badgeModel : badgesList) {
            badgeItemList.add(convertToBadgeItem(badgeModel));
        }
        return badgeItemList;
    }

    private BadgeItemViewModel convertToBadgeItem(SearchProductModel.ProductBadge badgeModel) {
        BadgeItemViewModel badgeItem = new BadgeItemViewModel();
        badgeItem.setImageUrl(badgeModel.getImageUrl());
        badgeItem.setTitle(badgeModel.getTitle());
        badgeItem.setShown(badgeModel.isShown());
        return badgeItem;
    }

    private List<LabelGroupViewModel> convertToLabelGroupList(List<SearchProductModel.ProductLabelGroup> labelGroupModelList) {
        List<LabelGroupViewModel> labelGroupViewModelList = new ArrayList<>();
        for(SearchProductModel.ProductLabelGroup labelGroupModel : labelGroupModelList) {
            labelGroupViewModelList.add(convertToLabelGroupViewModel(labelGroupModel));
        }

        return labelGroupViewModelList;
    }

    private LabelGroupViewModel convertToLabelGroupViewModel(SearchProductModel.ProductLabelGroup labelGroupModel) {
        return new LabelGroupViewModel(
                labelGroupModel.getPosition(),
                labelGroupModel.getType(),
                labelGroupModel.getTitle()
        );
    }

    private FreeOngkirViewModel convertToFreeOngkirViewModel(SearchProductModel.ProductFreeOngkir freeOngkir) {
        return new FreeOngkirViewModel(freeOngkir.isActive(), freeOngkir.getImageUrl());
    }

    private TickerViewModel createTickerModel(SearchProductModel.SearchProductData searchProductData) {
        SearchProductModel.Ticker tickerModel = searchProductData.getTicker();

        TickerViewModel tickerViewModel = new TickerViewModel();
        tickerViewModel.setText(tickerModel.getText());
        tickerViewModel.setQuery(tickerModel.getQuery());
        tickerViewModel.setTypeId(tickerModel.getTypeId());

        return tickerViewModel;
    }

    private SuggestionViewModel createSuggestionModel(SearchProductModel.SearchProductData searchProduct) {
        SearchProductModel.Suggestion suggestionModel = searchProduct.getSuggestion();

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
                    product.getCountReview(),
                    product.getUrl(),
                    product.getApplink(),
                    position,
                    inspirationCarouselType
            ));
        }

        return products;
    }

    private List<InspirationCardViewModel> convertToInspirationCardViewModel(SearchProductModel.SearchInspirationCard searchInspirationCard) {
        List<InspirationCardViewModel> inspirationCardViewModel = new ArrayList<>();

        for (SearchProductModel.InspirationCardData data : searchInspirationCard.getData()) {
            inspirationCardViewModel.add(new InspirationCardViewModel(
                    data.getTitle(),
                    data.getType(),
                    data.getPosition(),
                    convertToInspirationCardOptionViewModel(data.getInspiratioWidgetOptions(), data.getType())
            ));
        }

        return inspirationCardViewModel;
    }

    private List<InspirationCardOptionViewModel> convertToInspirationCardOptionViewModel(List<SearchProductModel.InspirationCardOption> inspiratioWidgetOptions, String inspirationCardType) {
        List<InspirationCardOptionViewModel> options = new ArrayList<>();

        for (SearchProductModel.InspirationCardOption option : inspiratioWidgetOptions) {
            options.add(new InspirationCardOptionViewModel(
                    option.getText(),
                    option.getImg(),
                    option.getUrl(),
                    option.getColor(),
                    option.getApplink(),
                    inspirationCardType
            ));
        }

        return options;
    }
}
