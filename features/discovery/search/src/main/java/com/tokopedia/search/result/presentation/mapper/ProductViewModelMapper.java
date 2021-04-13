package com.tokopedia.search.result.presentation.mapper;

import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.search.result.presentation.model.BadgeItemDataView;
import com.tokopedia.search.result.presentation.model.BannerDataView;
import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView;
import com.tokopedia.search.result.presentation.model.BroadMatchDataView;
import com.tokopedia.search.result.presentation.model.FreeOngkirDataView;
import com.tokopedia.search.result.presentation.model.GlobalNavDataView;
import com.tokopedia.search.result.presentation.model.InspirationCardOptionDataView;
import com.tokopedia.search.result.presentation.model.InspirationCardDataView;
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView;
import com.tokopedia.search.result.presentation.model.LabelGroupVariantDataView;
import com.tokopedia.search.result.presentation.model.LabelGroupDataView;
import com.tokopedia.search.result.presentation.model.ProductItemDataView;
import com.tokopedia.search.result.presentation.model.ProductDataView;
import com.tokopedia.search.result.presentation.model.RelatedDataView;
import com.tokopedia.search.result.presentation.model.SuggestionDataView;
import com.tokopedia.search.result.presentation.model.TickerDataView;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.CollectionsKt;

public class ProductViewModelMapper {

    public ProductDataView convertToProductViewModel(
            int lastProductItemPositionFromCache,
            SearchProductModel searchProductModel,
            String pageTitle,
            boolean isLocalSearch
    ) {
        SearchProductModel.SearchProduct aceSearchProduct = searchProductModel.getSearchProduct();
        SearchProductModel.SearchProductHeader searchProductHeader = aceSearchProduct.getHeader();
        SearchProductModel.SearchProductData searchProductData = aceSearchProduct.getData();

        ProductDataView productDataView = new ProductDataView();
        productDataView.setAdsModel(searchProductModel.getTopAdsModel());
        if (isListContainItems(searchProductModel.getGlobalSearchNavigation().getData().getGlobalNavItems())) {
            productDataView.setGlobalNavDataView(convertToViewModel(searchProductModel.getGlobalSearchNavigation()));
        }
        productDataView.setCpmModel(searchProductModel.getCpmModel());
        productDataView.setRelatedDataView(convertToRelatedViewModel(searchProductData.getRelated(), isLocalSearch));
        productDataView.setProductList(convertToProductItemViewModelList(
                lastProductItemPositionFromCache, searchProductData.getProductList(), pageTitle
        ));
        productDataView.setAdsModel(searchProductModel.getTopAdsModel());
        productDataView.setTickerModel(createTickerModel(searchProductData));
        productDataView.setSuggestionModel(createSuggestionModel(searchProductData));
        productDataView.setTotalData(searchProductHeader.getTotalData());
        productDataView.setTotalDataText(searchProductHeader.getTotalDataText());
        productDataView.setResponseCode(searchProductHeader.getResponseCode());
        productDataView.setKeywordProcess(searchProductHeader.getKeywordProcess());
        productDataView.setErrorMessage(searchProductHeader.getErrorMessage());
        productDataView.setIsQuerySafe(searchProductData.isQuerySafe());
        productDataView.setInspirationCarouselDataView(
            convertToInspirationCarouselViewModel(searchProductModel.getSearchInspirationCarousel())
        );
        productDataView.setInspirationCardDataView(
            convertToInspirationCardViewModel(searchProductModel.getSearchInspirationWidget())
        );
        productDataView.setAdditionalParams(searchProductHeader.getAdditionalParams());
        productDataView.setAutocompleteApplink(searchProductData.getAutocompleteApplink());
        productDataView.setDefaultView(searchProductHeader.getDefaultView());
        productDataView.setBannerDataView(convertToBannerDataView(searchProductData.getBanner()));

        return productDataView;
    }

    private boolean isListContainItems(List list) {
        return list != null && !list.isEmpty();
    }

    private GlobalNavDataView convertToViewModel(SearchProductModel.GlobalSearchNavigation globalSearchNavigation) {
        return new GlobalNavDataView(
                globalSearchNavigation.getData().getSource(),
                globalSearchNavigation.getData().getTitle(),
                globalSearchNavigation.getData().getKeyword(),
                globalSearchNavigation.getData().getNavTemplate(),
                globalSearchNavigation.getData().getBackground(),
                globalSearchNavigation.getData().getSeeAllApplink(),
                globalSearchNavigation.getData().getSeeAllUrl(),
                globalSearchNavigation.getData().isShowTopAds(),
                convertToViewModel(globalSearchNavigation.getData().getGlobalNavItems())
        );
    }

    private  List<GlobalNavDataView.Item> convertToViewModel(List<SearchProductModel.GlobalNavItem> globalNavItems) {
        List<GlobalNavDataView.Item> itemList = new ArrayList<>();

        int position = 1;
        for (SearchProductModel.GlobalNavItem item : globalNavItems) {
            itemList.add(new GlobalNavDataView.Item(
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

    private RelatedDataView convertToRelatedViewModel(SearchProductModel.Related related, boolean isLocalSearch) {
        List<BroadMatchDataView> broadMatchDataViewList = new ArrayList<>();
        for (SearchProductModel.OtherRelated otherRelated: related.getOtherRelatedList()) {
            broadMatchDataViewList.add(convertToBroadMatchViewModel(otherRelated, isLocalSearch));
        }

        return new RelatedDataView(
                related.getRelatedKeyword(),
                related.getPosition(),
                broadMatchDataViewList
        );
    }

    private BroadMatchDataView convertToBroadMatchViewModel(SearchProductModel.OtherRelated otherRelated, boolean isLocalSearch) {
        List<BroadMatchItemDataView> broadMatchItemDataViewList = new ArrayList<>();
        int position = 0;
        for (SearchProductModel.OtherRelatedProduct otherRelatedProduct: otherRelated.getProductList()) {
            position++;
            broadMatchItemDataViewList.add(convertToBroadMatchItemViewModel(otherRelatedProduct, position, otherRelated.getKeyword()));
        }

        return new BroadMatchDataView(
                otherRelated.getKeyword(),
                otherRelated.getUrl(),
                otherRelated.getApplink(),
                isLocalSearch,
                broadMatchItemDataViewList
        );
    }

    private BroadMatchItemDataView convertToBroadMatchItemViewModel(
            SearchProductModel.OtherRelatedProduct otherRelatedProduct,
            int position,
            String alternativeKeyword
    ) {
        return new BroadMatchItemDataView(
                otherRelatedProduct.getId(),
                otherRelatedProduct.getName(),
                otherRelatedProduct.getPrice(),
                otherRelatedProduct.getImageUrl(),
                otherRelatedProduct.getUrl(),
                otherRelatedProduct.getApplink(),
                otherRelatedProduct.getPriceString(),
                otherRelatedProduct.getShop().getCity(),
                convertOtherRelatedProductBadgeToBadgesItemList(otherRelatedProduct.getBadgeList()),
                convertOtherRelatedProductFreeOngkirToFreeOngkirViewModel(otherRelatedProduct.getFreeOngkir()),
                otherRelatedProduct.isWishlisted(),
                position,
                alternativeKeyword,
                otherRelatedProduct.isOrganicAds(),
                otherRelatedProduct.getAds().getProductViewUrl(),
                otherRelatedProduct.getAds().getProductClickUrl(),
                otherRelatedProduct.getAds().getProductWishlistUrl(),
                otherRelatedProduct.getRatingAverage(),
                convertToLabelGroupList(otherRelatedProduct.getLabelGroupList())
        );
    }

    private List<BadgeItemDataView> convertOtherRelatedProductBadgeToBadgesItemList(
            List<SearchProductModel.OtherRelatedProductBadge> badgesList
    ) {
        List<BadgeItemDataView> badgeItemList = new ArrayList<>();

        for (SearchProductModel.OtherRelatedProductBadge badgeModel : badgesList) {
            badgeItemList.add(convertOtherRelatedProductBadgeToBadgeItem(badgeModel));
        }
        return badgeItemList;
    }

    private BadgeItemDataView convertOtherRelatedProductBadgeToBadgeItem(
            SearchProductModel.OtherRelatedProductBadge badgeModel
    ) {
        BadgeItemDataView badgeItem = new BadgeItemDataView();
        badgeItem.setImageUrl(badgeModel.getImageUrl());
        badgeItem.setShown(badgeModel.isShown());
        return badgeItem;
    }

    private FreeOngkirDataView convertOtherRelatedProductFreeOngkirToFreeOngkirViewModel(
            SearchProductModel.OtherRelatedProductFreeOngkir freeOngkir
    ) {
        return new FreeOngkirDataView(freeOngkir.isActive(), freeOngkir.getImageUrl());
    }

    private List<ProductItemDataView> convertToProductItemViewModelList(
            int lastProductItemPositionFromCache,
            List<SearchProductModel.Product> productModels,
            String pageTitle
    ) {
        List<ProductItemDataView> productItemList = new ArrayList<>();

        int position = lastProductItemPositionFromCache;

        for (SearchProductModel.Product productModel : productModels) {
            position++;
            productItemList.add(convertToProductItem(productModel, position, pageTitle));
        }

        return productItemList;
    }

    private ProductItemDataView convertToProductItem(
            SearchProductModel.Product productModel,
            int position,
            String pageTitle
    ) {
        ProductItemDataView productItem = new ProductItemDataView();
        productItem.setProductID(productModel.getId());
        productItem.setWarehouseID(productModel.getWarehouseIdDefault());
        productItem.setProductName(productModel.getName());
        productItem.setImageUrl(productModel.getImageUrl());
        productItem.setImageUrl300(productModel.getImageUrl300());
        productItem.setImageUrl700(productModel.getImageUrl700());
        productItem.setRatingString(productModel.getRatingAverage());
        productItem.setDiscountPercentage(productModel.getDiscountPercentage());
        productItem.setOriginalPrice(productModel.getOriginalPrice());
        productItem.setPrice(productModel.getPrice());
        productItem.setPriceInt(productModel.getPriceInt());
        productItem.setPriceRange(productModel.getPriceRange());
        productItem.setShopID(productModel.getShop().getId());
        productItem.setShopName(productModel.getShop().getName());
        productItem.setShopCity(productModel.getShop().getCity());
        productItem.setShopUrl(productModel.getShop().getUrl());
        productItem.setShopOfficialStore(productModel.getShop().isOfficial());
        productItem.setShopPowerMerchant(productModel.getShop().isPowerBadge());
        productItem.setWishlisted(productModel.isWishlist());
        productItem.setBadgesList(convertToBadgesItemList(productModel.getBadgeList()));
        productItem.setPosition(position);
        productItem.setCategoryID(productModel.getCategoryId());
        productItem.setCategoryName(productModel.getCategoryName());
        productItem.setCategoryBreadcrumb(productModel.getCategoryBreadcrumb());
        productItem.setLabelGroupList(convertToLabelGroupList(productModel.getLabelGroupList()));
        productItem.setLabelGroupVariantList(convertToLabelGroupVariantList(productModel.getLabelGroupVariantList()));
        productItem.setFreeOngkirDataView(convertToFreeOngkirViewModel(productModel.getFreeOngkir()));
        productItem.setBoosterList(productModel.getBoosterList());
        productItem.setSourceEngine(productModel.getSourceEngine());
        productItem.setOrganicAds(productModel.isOrganicAds());
        productItem.setTopadsImpressionUrl(productModel.getAds().getProductViewUrl());
        productItem.setTopadsClickUrl(productModel.getAds().getProductClickUrl());
        productItem.setTopadsWishlistUrl(productModel.getAds().getProductWishlistUrl());
        productItem.setMinOrder(productModel.getMinOrder());
        productItem.setProductUrl(productModel.getUrl());
        productItem.setPageTitle(pageTitle);

        return productItem;
    }

    private List<BadgeItemDataView> convertToBadgesItemList(List<SearchProductModel.ProductBadge> badgesList) {
        List<BadgeItemDataView> badgeItemList = new ArrayList<>();

        for (SearchProductModel.ProductBadge badgeModel : badgesList) {
            badgeItemList.add(convertToBadgeItem(badgeModel));
        }
        return badgeItemList;
    }

    private BadgeItemDataView convertToBadgeItem(SearchProductModel.ProductBadge badgeModel) {
        BadgeItemDataView badgeItem = new BadgeItemDataView();
        badgeItem.setImageUrl(badgeModel.getImageUrl());
        badgeItem.setTitle(badgeModel.getTitle());
        badgeItem.setShown(badgeModel.isShown());
        return badgeItem;
    }

    private List<LabelGroupDataView> convertToLabelGroupList(List<SearchProductModel.ProductLabelGroup> labelGroupModelList) {
        List<LabelGroupDataView> labelGroupDataViewList = new ArrayList<>();
        for(SearchProductModel.ProductLabelGroup labelGroupModel : labelGroupModelList) {
            labelGroupDataViewList.add(convertToLabelGroupViewModel(labelGroupModel));
        }

        return labelGroupDataViewList;
    }

    private LabelGroupDataView convertToLabelGroupViewModel(SearchProductModel.ProductLabelGroup labelGroupModel) {
        return new LabelGroupDataView(
                labelGroupModel.getPosition(),
                labelGroupModel.getType(),
                labelGroupModel.getTitle(),
                labelGroupModel.getUrl()
        );
    }

    private List<LabelGroupVariantDataView> convertToLabelGroupVariantList(
            List<SearchProductModel.ProductLabelGroupVariant> labelGroupVariantList
    ) {
        return CollectionsKt.map(labelGroupVariantList, this::convertToLabelGroupVariantViewModel);
    }

    private LabelGroupVariantDataView convertToLabelGroupVariantViewModel(
            SearchProductModel.ProductLabelGroupVariant labelGroupVariant
    ) {
        return new LabelGroupVariantDataView(
                labelGroupVariant.getTitle(),
                labelGroupVariant.getType(),
                labelGroupVariant.getTypeVariant(),
                labelGroupVariant.getHexColor()
        );
    }

    private FreeOngkirDataView convertToFreeOngkirViewModel(SearchProductModel.ProductFreeOngkir freeOngkir) {
        return new FreeOngkirDataView(freeOngkir.isActive(), freeOngkir.getImageUrl());
    }

    private TickerDataView createTickerModel(SearchProductModel.SearchProductData searchProductData) {
        SearchProductModel.Ticker tickerModel = searchProductData.getTicker();

        TickerDataView tickerDataView = new TickerDataView();
        tickerDataView.setText(tickerModel.getText());
        tickerDataView.setQuery(tickerModel.getQuery());
        tickerDataView.setTypeId(tickerModel.getTypeId());

        return tickerDataView;
    }

    private SuggestionDataView createSuggestionModel(SearchProductModel.SearchProductData searchProduct) {
        SearchProductModel.Suggestion suggestionModel = searchProduct.getSuggestion();

        SuggestionDataView suggestionDataView = new SuggestionDataView();
        suggestionDataView.setSuggestionText(suggestionModel.getText());
        suggestionDataView.setSuggestedQuery(suggestionModel.getQuery());
        suggestionDataView.setSuggestion(suggestionModel.getSuggestion());

        return suggestionDataView;
    }

    private  List<InspirationCarouselDataView> convertToInspirationCarouselViewModel(SearchProductModel.SearchInspirationCarousel searchInspirationCarousel) {
        List<InspirationCarouselDataView> inspirationCarousel = new ArrayList<>();

        for (SearchProductModel.InspirationCarouselData data : searchInspirationCarousel.getData()) {
            inspirationCarousel.add(new InspirationCarouselDataView(
                    data.getTitle(),
                    data.getType(),
                    data.getPosition(),
                    data.getLayout(),
                    convertToInspirationCarouselOptionViewModel(data)
            ));
        }

        return inspirationCarousel;
    }

    private  List<InspirationCarouselDataView.Option> convertToInspirationCarouselOptionViewModel(
            SearchProductModel.InspirationCarouselData data
    ) {
        List<InspirationCarouselDataView.Option> options = new ArrayList<>();
        InspirationCarouselProductDataViewMapper mapper = new InspirationCarouselProductDataViewMapper();

        for (SearchProductModel.InspirationCarouselOption opt : data.getInspirationCarouselOptions()) {
            int index = data.getInspirationCarouselOptions().indexOf(opt);
            int position = index + 1;
            boolean isChipsActive = index == 0;
            options.add(new InspirationCarouselDataView.Option(
                    opt.getTitle(),
                    opt.getUrl(),
                    opt.getApplink(),
                    opt.getBannerImageUrl(),
                    opt.getBannerLinkUrl(),
                    opt.getBannerApplinkUrl(),
                    opt.getIdentifier(),
                    mapper.convertToInspirationCarouselProductDataView(
                            opt.getInspirationCarouselProducts(),
                            position,
                            data.getType(),
                            data.getLayout(),
                            this::convertToLabelGroupList,
                            opt.getTitle()
                    ),
                    data.getType(),
                    data.getLayout(),
                    data.getPosition(),
                    data.getTitle(),
                    position,
                    isChipsActive
            ));
        }

        return options;
    }

    private List<InspirationCardDataView> convertToInspirationCardViewModel(SearchProductModel.SearchInspirationWidget searchInspirationWidget) {
        List<InspirationCardDataView> inspirationCardDataView = new ArrayList<>();

        for (SearchProductModel.InspirationCardData data : searchInspirationWidget.getData()) {
            inspirationCardDataView.add(new InspirationCardDataView(
                    data.getTitle(),
                    data.getType(),
                    data.getPosition(),
                    convertToInspirationCardOptionViewModel(data.getInspiratioWidgetOptions(), data.getType())
            ));
        }

        return inspirationCardDataView;
    }

    private List<InspirationCardOptionDataView> convertToInspirationCardOptionViewModel(List<SearchProductModel.InspirationCardOption> inspiratioWidgetOptions, String inspirationCardType) {
        List<InspirationCardOptionDataView> options = new ArrayList<>();

        for (SearchProductModel.InspirationCardOption option : inspiratioWidgetOptions) {
            options.add(new InspirationCardOptionDataView(
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

    private BannerDataView convertToBannerDataView(SearchProductModel.Banner bannerModel) {
        return new BannerDataView(
                bannerModel.getPosition(),
                bannerModel.getText(),
                bannerModel.getApplink(),
                bannerModel.getImageUrl()
        );
    }
}
