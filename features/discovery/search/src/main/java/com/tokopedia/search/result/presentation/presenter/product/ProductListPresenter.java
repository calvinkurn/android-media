package com.tokopedia.search.result.presentation.presenter.product;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.search.result.presentation.ProductListSectionContract;
import com.tokopedia.search.result.presentation.mapper.ProductViewModelMapper;
import com.tokopedia.search.result.presentation.mapper.RecommendationViewModelMapper;
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel;
import com.tokopedia.search.result.presentation.model.FreeOngkirViewModel;
import com.tokopedia.search.result.presentation.model.CpmViewModel;
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.model.ProductViewModel;
import com.tokopedia.search.result.presentation.model.RecommendationItemViewModel;
import com.tokopedia.search.result.presentation.model.RecommendationTitleViewModel;
import com.tokopedia.search.result.presentation.model.BannedProductsEmptySearchViewModel;
import com.tokopedia.search.result.presentation.model.BannedProductsTickerViewModel;
import com.tokopedia.search.result.presentation.presenter.abstraction.SearchSectionPresenter;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Badge;
import com.tokopedia.topads.sdk.domain.model.Cpm;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.FreeOngkir;
import com.tokopedia.topads.sdk.domain.model.LabelGroup;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;

import static com.tokopedia.recommendation_widget_common.PARAM_RECOMMENDATIONKt.DEFAULT_VALUE_X_SOURCE;

final class ProductListPresenter
        extends SearchSectionPresenter<ProductListSectionContract.View>
        implements ProductListSectionContract.Presenter {

    private List<Integer> searchNoResultCodeList = Arrays.asList(1, 2, 3, 6);
    private static final String SEARCH_PAGE_NAME_RECOMMENDATION = "search_not_found";
    private static final String DEFAULT_PAGE_TITLE_RECOMMENDATION = "Rekomendasi untukmu";

    @Inject
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_FIRST_PAGE_USE_CASE)
    UseCase<SearchProductModel> searchProductFirstPageUseCase;
    @Inject
    @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_LOAD_MORE_USE_CASE)
    UseCase<SearchProductModel> searchProductLoadMoreUseCase;
    @Inject
    @Named(SearchConstant.Wishlist.PRODUCT_WISHLIST_URL_USE_CASE)
    UseCase<Boolean> productWishlistUrlUseCase;
    @Inject
    GetRecommendationUseCase recommendationUseCase;
    @Inject
    AddWishListUseCase addWishlistActionUseCase;
    @Inject
    RemoveWishListUseCase removeWishlistActionUseCase;
    @Inject
    RemoteConfig remoteConfig;

    private boolean enableGlobalNavWidget;
    private boolean changeParamRow;

    @Override
    public void initInjector(ProductListSectionContract.View view) {
        ProductListPresenterComponent component = DaggerProductListPresenterComponent.builder()
                .baseAppComponent(view.getBaseAppComponent())
                .build();

        component.inject(this);

        enableGlobalNavWidget = remoteConfig.getBoolean(RemoteConfigKey.ENABLE_GLOBAL_NAV_WIDGET,true);
        changeParamRow = remoteConfig.getBoolean(SearchConstant.RemoteConfigKey.APP_CHANGE_PARAMETER_ROW, false);
    }

    @Override
    public void requestDynamicFilter(Map<String, Object> searchParameterMap) {
        requestDynamicFilterCheckForNulls();

        Map<String, String> additionalParamsMap = getView().getAdditionalParamsMap();

        if(searchParameterMap == null) return;

        RequestParams params = createRequestDynamicFilterParams(searchParameterMap);

        if(additionalParamsMap != null) {
            enrichWithAdditionalParams(params, additionalParamsMap);
        }

        getDynamicFilterUseCase.execute(params, getDynamicFilterSubscriber(false));
    }

    private RequestParams createRequestDynamicFilterParams(Map<String, Object> searchParameter) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(searchParameter);
        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_PRODUCT);
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);

        return requestParams;
    }

    private void requestDynamicFilterCheckForNulls() {
        if(getDynamicFilterUseCase == null) throw new RuntimeException("UseCase<DynamicFilterModeL> is not injected.");
    }

    @Override
    public void handleWishlistButtonClicked(final ProductItemViewModel productItem) {
        if (getView().isUserHasLogin()) {
            WishListActionListener wishlistActionListener = createWishlistActionListener(productItem);

            getView().disableWishlistButton(productItem.getProductID());
            if (productItem.isWishlisted()) {
                removeWishlist(productItem, wishlistActionListener);
            } else if(productItem.isTopAds()){
                RequestParams params = RequestParams.create();
                params.putString(SearchConstant.Wishlist.PRODUCT_WISHLIST_URL, productItem.getTopadsWishlistUrl());
                productWishlistUrlUseCase.execute(params, getWishlistSubscriber(productItem));
            } else {
                addWishlist(productItem, wishlistActionListener);
            }
        } else {
            getView().sendTrackingWishlistNonLogin(productItem);
            getView().launchLoginActivity(productItem.getProductID());
        }
    }

    private WishListActionListener createWishlistActionListener(ProductItemViewModel productItemViewModel) {
        return new WishListActionListener() {
            @Override
            public void onErrorAddWishList(String errorMessage, String productId) {
                if (getView() == null) return;

                getView().errorAddWishList(errorMessage, productId);
            }

            @Override
            public void onSuccessAddWishlist(String productId) {
                if (getView() == null) return;

                getView().successAddWishlist(productItemViewModel);
            }

            @Override
            public void onErrorRemoveWishlist(String errorMessage, String productId) {
                if (getView() == null) return;

                getView().errorRemoveWishlist(errorMessage, productId);
            }

            @Override
            public void onSuccessRemoveWishlist(String productId) {
                if (getView() == null) return;

                getView().successRemoveWishlist(productItemViewModel);
            }
        };
    }

    private void removeWishlist(ProductItemViewModel productItemViewModel, WishListActionListener wishlistActionListener) {
        getView().logDebug(this.toString(), "Remove Wishlist " + productItemViewModel.getProductID());
        removeWishlistActionUseCase.createObservable(productItemViewModel.getProductID(), getView().getUserId(), wishlistActionListener);
    }

    private void addWishlist(ProductItemViewModel productItemViewModel, WishListActionListener wishlistActionListener) {
        getView().logDebug(this.toString(), "Add Wishlist " + productItemViewModel.getProductID());
        addWishlistActionUseCase.createObservable(productItemViewModel.getProductID(), getView().getUserId(), wishlistActionListener);
    }

    private Subscriber<Boolean> getWishlistSubscriber(final ProductItemViewModel productItem) {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().errorAddWishList(e.getMessage(), productItem.getProductID());
                    getView().notifyAdapter();
                }
            }

            @Override
            public void onNext(Boolean result) {
                if (isViewAttached()){
                    if (result) {
                        getView().successAddWishlist(productItem);
                    } else {
                        getView().notifyAdapter();
                    }
                }
            }
        };
    }

    @Override
    public void handleWishlistButtonClicked(final RecommendationItem recommendationItem) {
        if (getView().isUserHasLogin()) {
            WishListActionListener recommendationItemWishlistActionListener = createRecommendationItemWishlistActionListener();

            getView().disableWishlistButton(String.valueOf(recommendationItem.getProductId()));

            if (recommendationItem.isWishlist()) {
                removeWishlistRecommendationItem(recommendationItem, recommendationItemWishlistActionListener);
            } else if(recommendationItem.isTopAds()){
                RequestParams params = RequestParams.create();
                params.putString(SearchConstant.Wishlist.PRODUCT_WISHLIST_URL, recommendationItem.getWishlistUrl());
                productWishlistUrlUseCase.execute(params, getWishlistSubscriber(recommendationItem));
            } else {
                addWishlistRecommendationItem(recommendationItem, recommendationItemWishlistActionListener);
            }
        } else {
            getView().launchLoginActivity(String.valueOf(recommendationItem.getProductId()));
        }
    }

    private WishListActionListener createRecommendationItemWishlistActionListener() {
        return new WishListActionListener() {
            @Override
            public void onErrorAddWishList(String errorMessage, String productId) {
                getView().errorRecommendationWishlist(errorMessage, productId);
            }

            @Override
            public void onSuccessAddWishlist(String productId) {
                getView().successAddRecommendationWishlist(productId);
            }

            @Override
            public void onErrorRemoveWishlist(String errorMessage, String productId) {
                getView().errorRecommendationWishlist(errorMessage, productId);
            }

            @Override
            public void onSuccessRemoveWishlist(String productId) {
                getView().successRemoveRecommendationWishlist(productId);
            }
        };
    }

    private void removeWishlistRecommendationItem(RecommendationItem recommendationItem, WishListActionListener recommendationItemWishlistActionListener) {
        getView().logDebug(this.toString(), "Remove Wishlist " + recommendationItem.getProductId());
        removeWishlistActionUseCase.createObservable(
                String.valueOf(recommendationItem.getProductId()), getView().getUserId(), recommendationItemWishlistActionListener);
    }

    private void addWishlistRecommendationItem(RecommendationItem recommendationItem, WishListActionListener recommendationItemWishlistActionListener) {
        getView().logDebug(this.toString(), "Add Wishlist " + recommendationItem.getProductId());
        addWishlistActionUseCase.createObservable(
                String.valueOf(recommendationItem.getProductId()), getView().getUserId(), recommendationItemWishlistActionListener);
    }

    private Subscriber<Boolean> getWishlistSubscriber(final RecommendationItem recommendationItem) {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().errorRecommendationWishlist(e.getMessage(), String.valueOf(recommendationItem.getProductId()));
                    getView().notifyAdapter();
                }
            }

            @Override
            public void onNext(Boolean result) {
                if (isViewAttached()){
                    if (result) {
                        getView().successAddRecommendationWishlist(String.valueOf(recommendationItem.getProductId()));
                    } else {
                        getView().notifyAdapter();
                    }
                }
            }
        };
    }

    @Override
    public void loadMoreData(Map<String, Object> searchParameter, Map<String, String> additionalParams) {
        checkViewAttached();
        if(searchParameter == null || additionalParams == null) return;

        RequestParams requestParams = createInitializeSearchParam(searchParameter);
        enrichWithRelatedSearchParam(requestParams);
        enrichWithAdditionalParams(requestParams, additionalParams);

        // Unsubscribe first in case user has slow connection, and the previous loadMoreUseCase has not finished yet.
        searchProductLoadMoreUseCase.unsubscribe();

        searchProductLoadMoreUseCase.execute(requestParams, getLoadMoreDataSubscriber(searchParameter));
    }

    private RequestParams createInitializeSearchParam(Map<String, Object> searchParameter) {
        RequestParams requestParams = RequestParams.create();

        putRequestParamsOtherParameters(requestParams, searchParameter);
        requestParams.putAll(searchParameter);

        return requestParams;
    }

    private void putRequestParamsOtherParameters(RequestParams requestParams, Map<String, Object> searchParameter) {
        putRequestParamsSearchParameters(requestParams, searchParameter);

        putRequestParamsTopAdsParameters(requestParams, searchParameter);

        putRequestParamsDepartmentIdIfNotEmpty(requestParams, searchParameter);
    }

    private void putRequestParamsSearchParameters(RequestParams requestParams, Map<String, Object> searchParameter) {
        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH);
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(SearchApiConst.ROWS, getSearchRows());
        requestParams.putString(SearchApiConst.OB, getSearchSort(searchParameter));
        requestParams.putString(SearchApiConst.START, getSearchStart(searchParameter));
        requestParams.putString(SearchApiConst.IMAGE_SIZE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SIZE);
        requestParams.putString(SearchApiConst.IMAGE_SQUARE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SQUARE);
        requestParams.putString(SearchApiConst.Q, omitNewlineAndPlusSign(getSearchQuery(searchParameter)));
        requestParams.putString(SearchApiConst.UNIQUE_ID, getUniqueId(searchParameter));
    }

    private String getSearchRows() {
        return (changeParamRow) ? SearchConstant.SearchProduct.PARAMETER_ROWS : SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS;
    }

    private String getSearchSort(Map<String, Object> searchParameter) {
        Object sortObject = searchParameter.get(SearchApiConst.OB);
        String sort = sortObject == null ? "" : sortObject.toString();

        return !textIsEmpty(sort) ? sort : SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT;
    }

    private String getSearchStart(Map<String, Object> searchParameter) {
        Object startObject = searchParameter.get(SearchApiConst.START);

        return startObject == null ? "" : startObject.toString();
    }

    private String getSearchQuery(Map<String, Object> searchParameter) {
        Object queryObject = searchParameter.get(SearchApiConst.Q);
        String query = queryObject == null ? "" : queryObject.toString();

        return omitNewlineAndPlusSign(query);
    }

    private String omitNewlineAndPlusSign(String text) {
        return text.replace("\n", "").replace("+", " ");
    }

    private String getUniqueId(Map<String, Object> searchParameter) {
        Object uniqueIdObject = searchParameter.get(SearchApiConst.UNIQUE_ID);

        return uniqueIdObject == null ? "" : uniqueIdObject.toString();
    }

    private void putRequestParamsTopAdsParameters(RequestParams requestParams, Map<String, Object> searchParameter) {
        requestParams.putInt(TopAdsParams.KEY_ITEM, 2);
        requestParams.putString(TopAdsParams.KEY_EP, TopAdsParams.DEFAULT_KEY_EP);
        requestParams.putString(TopAdsParams.KEY_SRC, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH);
        requestParams.putBoolean(TopAdsParams.KEY_WITH_TEMPLATE, true);
        requestParams.putInt(TopAdsParams.KEY_PAGE, getTopAdsKeyPage(searchParameter));
    }

    private int getTopAdsKeyPage(Map<String, Object> searchParameter) {
        try {
            int defaultValueStart = Integer.parseInt(SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS);
            return getIntegerFromSearchParameter(searchParameter, SearchApiConst.START) / defaultValueStart + 1;
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int getIntegerFromSearchParameter(Map<String, Object> searchParameter, String key) {
        try {
            Object object = searchParameter.get(key);
            return Integer.parseInt(object == null ? "0" : object.toString());
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void putRequestParamsDepartmentIdIfNotEmpty(RequestParams requestParams, Map<String, Object> searchParameter) {
        Object departmentIdObject = searchParameter.get(SearchApiConst.SC);
        String departmentId = departmentIdObject == null ? "" : departmentIdObject.toString();

        if (!textIsEmpty(departmentId)) {
            requestParams.putString(SearchApiConst.SC, departmentId);
            requestParams.putString(TopAdsParams.KEY_DEPARTEMENT_ID, departmentId);
        }
    }

    private boolean textIsEmpty(String text) {
        return text == null || text.length() == 0;
    }

    private Subscriber<SearchProductModel> getLoadMoreDataSubscriber(final Map<String, Object> searchParameter) {
        return new Subscriber<SearchProductModel>() {
            @Override
            public void onStart() {
                loadMoreDataSubscriberOnStartIfViewAttached();
            }

            @Override
            public void onNext(SearchProductModel searchProductModel) {
                loadMoreDataSubscriberOnNextIfViewAttached(searchProductModel);
            }

            @Override
            public void onCompleted() {
                loadMoreDataSubscriberOnCompleteIfViewAttached();
            }

            @Override
            public void onError(Throwable e) {
                loadMoreDataSubscriberOnErrorIfViewAttached(searchParameter);
            }
        };
    }

    private void loadMoreDataSubscriberOnStartIfViewAttached() {
        if (isViewAttached()) {
            getView().incrementStart();
        }
    }

    private void loadMoreDataSubscriberOnNextIfViewAttached(SearchProductModel searchProductModel) {
        if (isViewAttached()) {
            int lastProductItemPositionFromCache = getView().getLastProductItemPositionFromCache();

            ProductViewModel productViewModel = new ProductViewModelMapper().convertToProductViewModel(lastProductItemPositionFromCache, searchProductModel);

            saveLastProductItemPositionToCache(lastProductItemPositionFromCache, productViewModel.getProductList());

            if (productViewModel.getProductList().isEmpty()) {
                getViewToRemoveLoading();
            } else {
                getViewToShowMoreData(productViewModel);
            }

            getView().storeTotalData(productViewModel.getTotalData());
        }
    }

    private void saveLastProductItemPositionToCache(int lastProductItemPositionFromCache, List<ProductItemViewModel> productItemViewModelList) {
        lastProductItemPositionFromCache = lastProductItemPositionFromCache +
                (isListContainItems(productItemViewModelList) ? productItemViewModelList.size() : 0);

        getView().saveLastProductItemPositionToCache(lastProductItemPositionFromCache);
    }

    private boolean isListContainItems(List list) {
        return list != null && !list.isEmpty();
    }

    private void getViewToRemoveLoading() {
        getView().removeLoading();
    }

    private void getViewToShowMoreData(ProductViewModel productViewModel) {
        List<Visitable> list = new ArrayList<>(convertToListOfVisitable(productViewModel));
        getView().removeLoading();
        getView().addProductList(list);
        getView().addLoading();
        getView().updateScrollListener();
    }

    private List<Visitable> convertToListOfVisitable(ProductViewModel productViewModel) {
        List<Visitable> list = new ArrayList<>(productViewModel.getProductList());
        int j = 0;
        for (int i = 0; i < productViewModel.getTotalItem(); i++) {
            try {
                if(productViewModel.getAdsModel().getTemplates().size() <= 0) continue;

                if (productViewModel.getAdsModel().getTemplates().get(i).isIsAd()) {
                    Data topAds = productViewModel.getAdsModel().getData().get(j);
                    ProductItemViewModel item = new ProductItemViewModel();
                    item.setProductID(topAds.getProduct().getId());
                    item.setTopAds(true);
                    item.setTopadsImpressionUrl(topAds.getProduct().getImage().getS_url());
                    item.setTopadsClickUrl(topAds.getProductClickUrl());
                    item.setTopadsWishlistUrl(topAds.getProductWishlistUrl());
                    item.setProductName(topAds.getProduct().getName());
                    if(!topAds.getProduct().getTopLabels().isEmpty()) {
                        item.setTopLabel(topAds.getProduct().getTopLabels().get(0));
                    }
                    if(!topAds.getProduct().getBottomLabels().isEmpty()) {
                        item.setBottomLabel(topAds.getProduct().getBottomLabels().get(0));
                    }
                    item.setPrice(topAds.getProduct().getPriceFormat());
                    item.setShopCity(topAds.getShop().getLocation());
                    item.setImageUrl(topAds.getProduct().getImage().getS_ecs());
                    item.setImageUrl700(topAds.getProduct().getImage().getM_ecs());
                    item.setWishlisted(topAds.getProduct().isWishlist());
                    item.setRating(topAds.getProduct().getProductRating());
                    item.setCountReview(convertCountReviewFormatToInt(topAds.getProduct().getCountReviewFormat()));
                    item.setBadgesList(mapBadges(topAds.getShop().getBadges()));
                    item.setNew(topAds.getProduct().isProductNewLabel());
                    item.setIsShopOfficialStore(topAds.getShop().isShop_is_official());
                    item.setShopName(topAds.getShop().getName());
                    item.setOriginalPrice(topAds.getProduct().getCampaign().getOriginalPrice());
                    item.setDiscountPercentage(topAds.getProduct().getCampaign().getDiscountPercentage());
                    item.setLabelGroupList(mapLabelGroupList(topAds.getProduct().getLabelGroupList()));
                    item.setFreeOngkirViewModel(mapFreeOngkir(topAds.getProduct().getFreeOngkir()));
                    list.add(i, item);
                    j++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private int convertCountReviewFormatToInt(String countReviewFormat) {
        String countReviewString = countReviewFormat.replaceAll("[^\\d]", "");

        try {
            return Integer.parseInt(countReviewString);
        }
        catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private List<BadgeItemViewModel> mapBadges(List<Badge> badges) {
        List<BadgeItemViewModel> items = new ArrayList<>();
        for (Badge b:badges) {
            items.add(new BadgeItemViewModel(b.getImageUrl(), b.getTitle(), b.isShow()));
        }
        return items;
    }

    private List<LabelGroupViewModel> mapLabelGroupList(List<LabelGroup> labelGroupList) {
        List<LabelGroupViewModel> labelGroupViewModelList = new ArrayList<>();

        for(LabelGroup labelGroup : labelGroupList) {
            labelGroupViewModelList.add(
                    new LabelGroupViewModel(
                            labelGroup.getPosition(), labelGroup.getType(), labelGroup.getTitle()
                    )
            );
        }

        return labelGroupViewModelList;
    }

    private FreeOngkirViewModel mapFreeOngkir(FreeOngkir freeOngkir) {
        return new FreeOngkirViewModel(freeOngkir.isActive(), freeOngkir.getImageUrl());
    }

    private void loadMoreDataSubscriberOnCompleteIfViewAttached() {
        if (isViewAttached()) {
            getView().hideRefreshLayout();
        }
    }

    private void loadMoreDataSubscriberOnErrorIfViewAttached(Map<String, Object> searchParameter) {
        if (isViewAttached()) {
            getView().removeLoading();
            getView().hideRefreshLayout();
            getView().showNetworkError(getIntegerFromSearchParameter(searchParameter, SearchApiConst.START));
        }
    }

    @Override
    public void loadData(Map<String, Object> searchParameter, Map<String, String> additionalParams, boolean isFirstTimeLoad) {
        checkViewAttached();
        if(searchParameter == null || additionalParams == null) return;

        RequestParams requestParams = createInitializeSearchParam(searchParameter);
        enrichWithRelatedSearchParam(requestParams);

        if (checkShouldEnrichWithAdditionalParams(additionalParams)) {
            enrichWithAdditionalParams(requestParams, additionalParams);
        }

        // Unsubscribe first in case user has slow connection, and the previous loadDataUseCase has not finished yet.
        searchProductFirstPageUseCase.unsubscribe();

        searchProductFirstPageUseCase.execute(requestParams, getLoadDataSubscriber(searchParameter, isFirstTimeLoad));
    }

    private boolean checkShouldEnrichWithAdditionalParams(Map<String, String> additionalParams) {
        return getView().isAnyFilterActive() && additionalParams != null;
    }

    private Subscriber<SearchProductModel> getLoadDataSubscriber(final Map<String, Object> searchParameter, final boolean isFirstTimeLoad) {
        return new Subscriber<SearchProductModel>() {
            @Override
            public void onStart() {
                loadDataSubscriberOnStartIfViewAttached();
            }

            @Override
            public void onCompleted() {
                loadDataSubscriberOnCompleteIfViewAttached(searchParameter);
            }

            @Override
            public void onError(Throwable e) {
                loadDataSubscriberOnErrorIfViewAttached();
            }

            @Override
            public void onNext(SearchProductModel searchProductModel) {
                loadDataSubscriberOnNextIfViewAttached(searchProductModel, isFirstTimeLoad);
            }
        };
    }

    private void loadDataSubscriberOnStartIfViewAttached() {
        if (isViewAttached()) {
            getView().showRefreshLayout();
            getView().incrementStart();
        }
    }

    private void loadDataSubscriberOnCompleteIfViewAttached(Map<String, Object> searchParameter) {
        if (isViewAttached()) {
            getView().hideRefreshLayout();
            requestDynamicFilter(searchParameter);
        }
    }

    private void loadDataSubscriberOnErrorIfViewAttached() {
        if (isViewAttached()) {
            getView().removeLoading();
            getView().showNetworkError(0);
            getView().hideRefreshLayout();
        }
    }

    private void loadDataSubscriberOnNextIfViewAttached(SearchProductModel searchProductModel, boolean isFirstTimeLoad) {
        if (isViewAttached()) {
            if(isSearchRedirected(searchProductModel)) {
                getViewToRedirectSearch(searchProductModel);
            }
            else {
                getViewToProcessSearchResult(searchProductModel, isFirstTimeLoad);
            }
        }
    }

    private boolean isSearchRedirected(SearchProductModel searchProductModel) {
        SearchProductModel.Redirection redirection = searchProductModel.getSearchProduct().getRedirection();

        return redirection != null
                && redirection.getRedirectApplink() != null
                && redirection.getRedirectApplink().length() > 0;
    }

    private void getViewToRedirectSearch(SearchProductModel searchProductModel) {
        String applink = searchProductModel.getSearchProduct().getRedirection().getRedirectApplink();

        getView().redirectSearchToAnotherPage(applink);
    }

    private void getViewToProcessSearchResult(SearchProductModel searchProductModel, boolean isFirstTimeLoad) {
        ProductViewModel productViewModel = createProductViewModelWithPosition(searchProductModel);

        sendTrackingNoSearchResult(productViewModel);
        getView().setAutocompleteApplink(productViewModel.getAutocompleteApplink());
        getView().setDefaultLayoutType(productViewModel.getDefaultView());

        if (productViewModel.getProductList().isEmpty()) {
            getViewToHandleEmptyProductList(searchProductModel.getSearchProduct(), productViewModel);
            getViewToShowRecommendationItem();
        } else {
            getViewToShowProductList(searchProductModel, productViewModel);
        }

        getView().storeTotalData(productViewModel.getTotalData());

        if (isFirstTimeLoad) {
            getViewToSendTrackingOnFirstTimeLoad(productViewModel);
        }
    }

    private void sendTrackingNoSearchResult(ProductViewModel productViewModel) {
        try {
            String alternativeKeyword = "";
            if (productViewModel.getRelatedSearchModel() != null) {
                alternativeKeyword = productViewModel.getRelatedSearchModel().getRelatedKeyword();
            }
            int resultCode = Integer.parseInt(productViewModel.getResponseCode());
            if (searchNoResultCodeList.contains(resultCode)) {
                getView().sendTrackingForNoResult(productViewModel.getResponseCode(), alternativeKeyword);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private ProductViewModel createProductViewModelWithPosition(SearchProductModel searchProductModel) {
        getView().clearLastProductItemPositionFromCache();

        int lastProductItemPositionFromCache = getView().getLastProductItemPositionFromCache();

        ProductViewModel productViewModel = new ProductViewModelMapper().convertToProductViewModel(lastProductItemPositionFromCache, searchProductModel);

        saveLastProductItemPositionToCache(lastProductItemPositionFromCache, productViewModel.getProductList());

        return productViewModel;
    }

    private void getViewToHandleEmptyProductList(SearchProductModel.SearchProduct searchProduct, ProductViewModel productViewModel) {
        if (productViewModel.getErrorMessage() != null && !productViewModel.getErrorMessage().isEmpty()) {
            getViewToHandleEmptySearchWithErrorMessage(searchProduct);
        }
        else {
            getViewToShowEmptySearch(productViewModel);
        }
    }

    private void getViewToHandleEmptySearchWithErrorMessage(SearchProductModel.SearchProduct searchProduct) {
        getView().removeLoading();
        getView().setBannedProductsErrorMessage(createTobaccoErrorMessageAsList(searchProduct));
        getView().trackEventImpressionBannedProducts(true);
        getView().setTotalSearchResultCount("0");
    }

    private List<Visitable> createTobaccoErrorMessageAsList(SearchProductModel.SearchProduct searchProduct) {
        List<Visitable> tobaccoErrorMessageAsList = new ArrayList<>();
        tobaccoErrorMessageAsList.add(new BannedProductsEmptySearchViewModel(searchProduct.getErrorMessage(), searchProduct.getSeamlessLiteUrl()));
        return tobaccoErrorMessageAsList;
    }

    private void getViewToShowEmptySearch(ProductViewModel productViewModel) {
        boolean isGlobalNavWidgetAvailable
                = productViewModel.getGlobalNavViewModel() != null && enableGlobalNavWidget;
        getView().removeLoading();
        getView().setEmptyProduct(isGlobalNavWidgetAvailable ? productViewModel.getGlobalNavViewModel() : null);
        getView().setTotalSearchResultCount("0");
    }

    private void getViewToShowRecommendationItem(){
        getView().addLoading();
        recommendationUseCase.execute(
                recommendationUseCase.getRecomParams(1, DEFAULT_VALUE_X_SOURCE, SEARCH_PAGE_NAME_RECOMMENDATION, new ArrayList<>()),
                new Subscriber<List<? extends RecommendationWidget>>() {
                    @Override
                    public void onCompleted() {
                        getView().removeLoading();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<? extends RecommendationWidget> recommendationWidgets) {
                        if (!recommendationWidgets.isEmpty() && recommendationWidgets.get(0) != null){
                            List<RecommendationItemViewModel> recommendationItemViewModel = new RecommendationViewModelMapper().convertToRecommendationItemViewModel(recommendationWidgets.get(0));
                            List<Visitable> items = new ArrayList<>();
                            RecommendationWidget recommendationWidget = recommendationWidgets.get(0);
                            items.add(new RecommendationTitleViewModel(recommendationWidget.getTitle().isEmpty() ? DEFAULT_PAGE_TITLE_RECOMMENDATION : recommendationWidget.getTitle(), recommendationWidget.getSeeMoreAppLink(), recommendationWidget.getPageName()));
                            items.addAll(recommendationItemViewModel);
                            getView().addRecommendationList(items);
                        }
                    }
                }
        );
    }

    private void getViewToShowProductList(SearchProductModel searchProductModel, ProductViewModel productViewModel) {
        SearchProductModel.SearchProduct searchProduct = searchProductModel.getSearchProduct();

        List<Visitable> list = new ArrayList<>();

        if (!productViewModel.isQuerySafe()) {
            getView().showAdultRestriction();
        }

        boolean isGlobalNavWidgetAvailable
                = productViewModel.getGlobalNavViewModel() != null && enableGlobalNavWidget;

        if (productViewModel.getCpmModel() != null && !isGlobalNavWidgetAvailable && shouldShowCpmShop(productViewModel)) {
            CpmViewModel cpmViewModel = new CpmViewModel();
            cpmViewModel.setCpmModel(productViewModel.getCpmModel());
            list.add(cpmViewModel);
        }

        if (isGlobalNavWidgetAvailable) {
            list.add(productViewModel.getGlobalNavViewModel());
            getView().sendImpressionGlobalNav(productViewModel.getGlobalNavViewModel());
        }

        if (!getView().isTickerHasDismissed()
                && !TextUtils.isEmpty(productViewModel.getTickerModel().getText())) {
            list.add(productViewModel.getTickerModel());
        }

        if (!TextUtils.isEmpty(productViewModel.getSuggestionModel().getSuggestionText())) {
            list.add(productViewModel.getSuggestionModel());
        }

        if (searchProduct.getErrorMessage() != null && !searchProduct.getErrorMessage().isEmpty()) {
            list.add(createTobaccoTickerViewModel(searchProduct.getErrorMessage(), searchProduct.getSeamlessLiteUrl()));
            getView().trackEventImpressionBannedProducts(false);
        }

        if (productViewModel.getQuickFilterModel() != null) {
            list.add(productViewModel.getQuickFilterModel());
        }

        list.addAll(convertToListOfVisitable(productViewModel));
        if (productViewModel.getRelatedSearchModel() != null) {
            list.add(productViewModel.getRelatedSearchModel());
        }

        getView().setAdditionalParams(productViewModel.getAdditionalParams());
        getView().removeLoading();
        getView().setProductList(list);
        getView().showFreeOngkirShowCase(isExistsFreeOngkirBadge(list));

        getView().initQuickFilter(productViewModel.getQuickFilterModel().getQuickFilterList());

        if (productViewModel.getTotalData() > Integer.parseInt(getSearchRows())) {
            getView().addLoading();
        }

        getView().setTotalSearchResultCount(productViewModel.getSuggestionModel().getFormattedResultCount());
        getView().stopTracePerformanceMonitoring();
    }

    private boolean shouldShowCpmShop(ProductViewModel productViewModel) {
        if (productViewModel.getCpmModel().getData().isEmpty()) {
            return false;
        }

        CpmData cpmData = productViewModel.getCpmModel().getData().get(0);

        if (cpmData == null) {
            return false;
        }

        Cpm cpm = cpmData.getCpm();

        if (cpm == null) {
            return false;
        }

        if (isViewWillRenderCpmShop(cpm)) {
            return true;
        } else {
            return isViewWillRenderCpmDigital(cpm);
        }
    }

    private boolean isViewWillRenderCpmShop(Cpm cpm) {
        return cpm.getCpmShop() != null
                && !TextUtils.isEmpty(cpm.getCta())
                && !TextUtils.isEmpty(cpm.getPromotedText());
    }

    private boolean isViewWillRenderCpmDigital(Cpm cpm) {
        return cpm.getTemplateId() == 4;
    }

    private BannedProductsTickerViewModel createTobaccoTickerViewModel(String errorMessage, String encriptedLiteUrl) {
        String htmlErrorMessage = errorMessage
                + " "
                + "Gunakan <a href=\"" + encriptedLiteUrl + "\">browser</a>";

        return new BannedProductsTickerViewModel(htmlErrorMessage);
    }

    private boolean isExistsFreeOngkirBadge(List<Visitable> productList) {
        for(Visitable product: productList) {
            if (product instanceof ProductItemViewModel) {
                ProductItemViewModel productItemViewModel = (ProductItemViewModel) product;

                if(productItemViewModel.getFreeOngkirViewModel() != null
                        && productItemViewModel.getFreeOngkirViewModel().isActive()) {
                    return true;
                }
            }
        }

        return false;
    }

    private void getViewToSendTrackingOnFirstTimeLoad(ProductViewModel productViewModel) {
        JSONArray afProdIds = new JSONArray();
        HashMap<String, String> category = new HashMap<>();
        ArrayList<String> prodIdArray = new ArrayList<>();

        if (productViewModel.getProductList().size() > 0) {
            for (int i = 0; i < productViewModel.getProductList().size(); i++) {
                if (i < 3) {
                    prodIdArray.add(productViewModel.getProductList().get(i).getProductID());
                    afProdIds.put(productViewModel.getProductList().get(i).getProductID());
                } else {
                    break;
                }
                category.put(String.valueOf(productViewModel.getProductList().get(i).getCategoryID()), productViewModel.getProductList().get(i).getCategoryName());

            }
        }

        getView().sendTrackingEventAppsFlyerViewListingSearch(afProdIds, productViewModel.getQuery(), prodIdArray);
        getView().sendTrackingEventMoEngageSearchAttempt(productViewModel.getQuery(), !productViewModel.getProductList().isEmpty(), category);
        getView().setFirstTimeLoad(false);
    }

    private void enrichWithRelatedSearchParam(RequestParams requestParams) {
        requestParams.putBoolean(SearchApiConst.RELATED, true);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(searchProductFirstPageUseCase != null) searchProductFirstPageUseCase.unsubscribe();
        if(searchProductLoadMoreUseCase != null) searchProductLoadMoreUseCase.unsubscribe();
        if(productWishlistUrlUseCase != null) productWishlistUrlUseCase.unsubscribe();
        if(addWishlistActionUseCase != null) addWishlistActionUseCase.unsubscribe();
        if(removeWishlistActionUseCase != null) removeWishlistActionUseCase.unsubscribe();
        if(recommendationUseCase != null) recommendationUseCase.unsubscribe();
    }
}
