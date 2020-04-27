package com.tokopedia.search.result.presentation.presenter.product;

import android.net.Uri;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.filter.common.data.DynamicFilterModel;
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase;
import com.tokopedia.seamless_login.subscriber.SeamlessLoginSubscriber;
import com.tokopedia.search.analytics.GeneralSearchTrackingModel;
import com.tokopedia.search.di.module.SearchContextModule;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.search.result.presentation.ProductListSectionContract;
import com.tokopedia.search.result.presentation.mapper.ProductViewModelMapper;
import com.tokopedia.search.result.presentation.mapper.RecommendationViewModelMapper;
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel;
import com.tokopedia.search.result.presentation.model.BannedProductsEmptySearchViewModel;
import com.tokopedia.search.result.presentation.model.BannedProductsTickerViewModel;
import com.tokopedia.search.result.presentation.model.CpmViewModel;
import com.tokopedia.search.result.presentation.model.FreeOngkirViewModel;
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel;
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.model.ProductViewModel;
import com.tokopedia.search.result.presentation.model.RecommendationItemViewModel;
import com.tokopedia.search.result.presentation.model.RecommendationTitleViewModel;
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandler;
import com.tokopedia.search.result.presentation.view.fragment.ProductListFragment;
import com.tokopedia.search.utils.UrlParamUtils;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Badge;
import com.tokopedia.topads.sdk.domain.model.Cpm;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.FreeOngkir;
import com.tokopedia.topads.sdk.domain.model.LabelGroup;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;

import static com.tokopedia.discovery.common.constants.SearchConstant.Advertising.APP_CLIENT_ID;
import static com.tokopedia.discovery.common.constants.SearchConstant.Advertising.KEY_ADVERTISING_ID;
import static com.tokopedia.recommendation_widget_common.PARAM_RECOMMENDATIONKt.DEFAULT_VALUE_X_SOURCE;

final class ProductListPresenter
        extends BaseDaggerPresenter<ProductListSectionContract.View>
        implements ProductListSectionContract.Presenter {

    private List<Integer> searchNoResultCodeList = Arrays.asList(1, 2, 3, 6);
    private static final String SEARCH_PAGE_NAME_RECOMMENDATION = "empty_search";
    private static final String DEFAULT_PAGE_TITLE_RECOMMENDATION = "Rekomendasi untukmu";
    private static final String DEFAULT_USER_ID = "0";

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
    SeamlessLoginUsecase seamlessLoginUsecase;
    @Inject
    UserSessionInterface userSession;
    @Inject
    RemoteConfig remoteConfig;
    @Inject
    @Named(SearchConstant.Advertising.ADVERTISING_LOCAL_CACHE)
    LocalCacheHandler advertisingLocalCache;
    @Inject
    @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE)
    UseCase<DynamicFilterModel> getDynamicFilterUseCase;
    @Inject
    SearchLocalCacheHandler searchLocalCacheHandler;

    private boolean enableGlobalNavWidget = true;
    private boolean changeParamRow = false;
    private boolean isUsingBottomSheetFilter = true;
    private String additionalParams = "";
    private boolean isFirstTimeLoad = false;
    private boolean isTickerHasDismissed = false;
    private int startFrom = 0;
    private int totalData = 0;
    private boolean hasLoadData = false;

    private List<Visitable> productList;
    private List<InspirationCarouselViewModel> inspirationCarouselViewModel;

    @Override
    public void initInjector(ProductListSectionContract.View view) {
        ProductListPresenterComponent component = DaggerProductListPresenterComponent.builder()
                .baseAppComponent(view.getBaseAppComponent())
                .searchContextModule(createSearchContextModule(view))
                .build();

        component.inject(this);

        enableGlobalNavWidget = remoteConfig.getBoolean(RemoteConfigKey.ENABLE_GLOBAL_NAV_WIDGET, true);
        changeParamRow = remoteConfig.getBoolean(SearchConstant.RemoteConfigKey.APP_CHANGE_PARAMETER_ROW, false);
        isUsingBottomSheetFilter = remoteConfig.getBoolean(RemoteConfigKey.ENABLE_BOTTOM_SHEET_FILTER, true);
    }

    /**
     * Very ugly hack.
     * It is only intended for hotfix to reduce number of file changed
    * */
    @Deprecated
    private SearchContextModule createSearchContextModule(ProductListSectionContract.View view) {
        ProductListFragment fragment = (ProductListFragment)view;

        if (fragment == null || fragment.getActivity() == null) return null;

        return new SearchContextModule(fragment.getActivity());
    }

    @Override
    public boolean isUsingBottomSheetFilter() {
        return this.isUsingBottomSheetFilter;
    }

    @Override
    public String getUserId() {
        return userSession.isLoggedIn() ? userSession.getUserId() : DEFAULT_USER_ID;
    }

    @Override
    public boolean isUserLoggedIn() {
        return userSession.isLoggedIn();
    }

    @Override
    public String getDeviceId() {
        return userSession.getDeviceId();
    }

    private Map<String, String> getAdditionalParamsMap() {
        return UrlParamUtils.getParamMap(additionalParams);
    }

    @Override
    public void onPriceFilterTickerDismissed() {
        this.isTickerHasDismissed = true;
    }

    @Override
    public boolean getIsTickerHasDismissed() {
        return isTickerHasDismissed;
    }

    private void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    @Override
    public boolean hasNextPage() {
        return startFrom < totalData;
    }

    @Override
    public void clearData() {
        startFrom = 0;
        totalData = 0;
    }

    @Override
    public void setStartFrom(int startFrom) {
        this.startFrom = startFrom;
    }

    @Override
    public int getStartFrom() {
        return this.startFrom;
    }

    @Override
    public void onViewCreated() {
        boolean isFirstActiveTab = getView().isFirstActiveTab();
        if (isFirstActiveTab && !hasLoadData) {
            hasLoadData = true;
            onViewFirstTimeLaunch();
        }
    }

    private void onViewFirstTimeLaunch() {
        isFirstTimeLoad = true;

        getView().reloadData();
    }

    @Override
    public void onViewVisibilityChanged(boolean isViewVisible, boolean isViewAdded) {
        if (isViewVisible) {
            getView().setupSearchNavigation();
            getView().trackScreenAuthenticated();

            if (isViewAdded && !hasLoadData) {
                hasLoadData = true;
                onViewFirstTimeLaunch();
            }
        }
    }

    @Override
    public void requestDynamicFilter(Map<String, Object> searchParameterMap) {
        requestDynamicFilterCheckForNulls();

        Map<String, String> additionalParamsMap = getAdditionalParamsMap();

        if (searchParameterMap == null) return;

        RequestParams params = createRequestDynamicFilterParams(searchParameterMap);

        if (additionalParamsMap != null) {
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
        if (getDynamicFilterUseCase == null)
            throw new RuntimeException("UseCase<DynamicFilterModeL> is not injected.");
    }

    @Override
    public void handleWishlistButtonClicked(final ProductItemViewModel productItem) {
        if (isUserLoggedIn()) {
            WishListActionListener wishlistActionListener = createWishlistActionListener(productItem);

            getView().disableWishlistButton(productItem.getProductID());
            if (productItem.isWishlisted()) {
                removeWishlist(productItem, wishlistActionListener);
            } else if (productItem.isTopAds()) {
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
        removeWishlistActionUseCase.createObservable(productItemViewModel.getProductID(), getUserId(), wishlistActionListener);
    }

    private void addWishlist(ProductItemViewModel productItemViewModel, WishListActionListener wishlistActionListener) {
        getView().logDebug(this.toString(), "Add Wishlist " + productItemViewModel.getProductID());
        addWishlistActionUseCase.createObservable(productItemViewModel.getProductID(), getUserId(), wishlistActionListener);
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
                if (isViewAttached()) {
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
        if (isUserLoggedIn()) {
            WishListActionListener recommendationItemWishlistActionListener = createRecommendationItemWishlistActionListener();

            getView().disableWishlistButton(String.valueOf(recommendationItem.getProductId()));

            if (recommendationItem.isWishlist()) {
                removeWishlistRecommendationItem(recommendationItem, recommendationItemWishlistActionListener);
            } else if (recommendationItem.isTopAds()) {
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
                String.valueOf(recommendationItem.getProductId()), getUserId(), recommendationItemWishlistActionListener);
    }

    private void addWishlistRecommendationItem(RecommendationItem recommendationItem, WishListActionListener recommendationItemWishlistActionListener) {
        getView().logDebug(this.toString(), "Add Wishlist " + recommendationItem.getProductId());
        addWishlistActionUseCase.createObservable(
                String.valueOf(recommendationItem.getProductId()), getUserId(), recommendationItemWishlistActionListener);
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
                if (isViewAttached()) {
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
    public void loadMoreData(Map<String, Object> searchParameter) {
        checkViewAttached();

        Map<String, String> additionalParams = getAdditionalParamsMap();
        if (searchParameter == null || additionalParams == null) return;

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
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int getIntegerFromSearchParameter(Map<String, Object> searchParameter, String key) {
        try {
            Object object = searchParameter.get(key);
            return Integer.parseInt(object == null ? "0" : object.toString());
        } catch (NumberFormatException e) {
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
            incrementStart();
        }
    }

    private void incrementStart() {
        startFrom = startFrom + Integer.parseInt(SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS);
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

            setTotalData(productViewModel.getTotalData());
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
        productList.addAll(convertToListOfVisitable(productViewModel));
        List<Visitable> list = new ArrayList<>(convertToListOfVisitable(productViewModel));

        if (inspirationCarouselViewModel.size() > 0) {
            Iterator<InspirationCarouselViewModel> inspirationCarouselViewModelIterator = inspirationCarouselViewModel.iterator();
            while(inspirationCarouselViewModelIterator.hasNext()) {
                InspirationCarouselViewModel data = inspirationCarouselViewModelIterator.next();
                if (data.getPosition() < getView().getLastProductItemPositionFromCache()) {
                    Visitable product = productList.get(data.getPosition());
                    list.add(list.indexOf(product), data);
                    getView().sendImpressionInspirationCarousel(data);
                    inspirationCarouselViewModelIterator.remove();
                }
            }
        }

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
                if (productViewModel.getAdsModel().getTemplates().size() <= 0) continue;

                if (productViewModel.getAdsModel().getTemplates().get(i).isIsAd()) {
                    Data topAds = productViewModel.getAdsModel().getData().get(j);
                    ProductItemViewModel item = new ProductItemViewModel();
                    item.setProductID(topAds.getProduct().getId());
                    item.setTopAds(true);
                    item.setTopadsImpressionUrl(topAds.getProduct().getImage().getS_url());
                    item.setTopadsClickUrl(topAds.getProductClickUrl());
                    item.setTopadsWishlistUrl(topAds.getProductWishlistUrl());
                    item.setProductName(topAds.getProduct().getName());
                    if (!topAds.getProduct().getTopLabels().isEmpty()) {
                        item.setTopLabel(topAds.getProduct().getTopLabels().get(0));
                    }
                    if (!topAds.getProduct().getBottomLabels().isEmpty()) {
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
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private List<BadgeItemViewModel> mapBadges(List<Badge> badges) {
        List<BadgeItemViewModel> items = new ArrayList<>();
        for (Badge b : badges) {
            items.add(new BadgeItemViewModel(b.getImageUrl(), b.getTitle(), b.isShow()));
        }
        return items;
    }

    private List<LabelGroupViewModel> mapLabelGroupList(List<LabelGroup> labelGroupList) {
        List<LabelGroupViewModel> labelGroupViewModelList = new ArrayList<>();

        for (LabelGroup labelGroup : labelGroupList) {
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
    public void loadData(Map<String, Object> searchParameter) {
        checkViewAttached();

        Map<String, String> additionalParams = getAdditionalParamsMap();
        if (searchParameter == null || additionalParams == null) return;

        RequestParams requestParams = createInitializeSearchParam(searchParameter);
        enrichWithRelatedSearchParam(requestParams);

        if (checkShouldEnrichWithAdditionalParams(additionalParams)) {
            enrichWithAdditionalParams(requestParams, additionalParams);
        }

        // Unsubscribe first in case user has slow connection, and the previous loadDataUseCase has not finished yet.
        searchProductFirstPageUseCase.unsubscribe();

        searchProductFirstPageUseCase.execute(requestParams, getLoadDataSubscriber(searchParameter));
    }

    private boolean checkShouldEnrichWithAdditionalParams(Map<String, String> additionalParams) {
        return getView().isAnyFilterActive() && additionalParams != null;
    }

    private Subscriber<SearchProductModel> getLoadDataSubscriber(final Map<String, Object> searchParameter) {
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
                loadDataSubscriberOnNextIfViewAttached(searchProductModel);
            }
        };
    }

    private void loadDataSubscriberOnStartIfViewAttached() {
        if (isViewAttached()) {
            getView().showRefreshLayout();
            incrementStart();
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

    private void loadDataSubscriberOnNextIfViewAttached(SearchProductModel searchProductModel) {
        if (isViewAttached()) {
            if (isSearchRedirected(searchProductModel)) {
                getViewToRedirectSearch(searchProductModel);
            } else {
                getViewToProcessSearchResult(searchProductModel);
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

    private void getViewToProcessSearchResult(SearchProductModel searchProductModel) {
        ProductViewModel productViewModel = createProductViewModelWithPosition(searchProductModel);

        sendTrackingNoSearchResult(productViewModel);
        getView().setAutocompleteApplink(productViewModel.getAutocompleteApplink());
        getView().setDefaultLayoutType(productViewModel.getDefaultView());

        if (productViewModel.getProductList().isEmpty()) {
            getViewToHandleEmptyProductList(searchProductModel.getSearchProduct(), productViewModel);
            getViewToShowRecommendationItem();
            getView().hideBottomNavigation();
        } else {
            getViewToShowProductList(searchProductModel, productViewModel);
            getView().showBottomNavigation();
        }

        setTotalData(productViewModel.getTotalData());

        getView().updateScrollListener();

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
                getView().sendTrackingForNoResult(productViewModel.getResponseCode(), alternativeKeyword, productViewModel.getKeywordProcess());
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
        } else {
            getViewToShowEmptySearch(productViewModel);
        }
    }

    private void getViewToHandleEmptySearchWithErrorMessage(SearchProductModel.SearchProduct searchProduct) {
        getView().removeLoading();
        getView().setBannedProductsErrorMessage(createBannedProductsErrorMessageAsList(searchProduct));
        getView().trackEventImpressionBannedProducts(true);
        getView().setTotalSearchResultCount("0");
    }

    private List<Visitable> createBannedProductsErrorMessageAsList(SearchProductModel.SearchProduct searchProduct) {
        List<Visitable> bannedProductsErrorMessageAsList = new ArrayList<>();
        bannedProductsErrorMessageAsList.add(new BannedProductsEmptySearchViewModel(searchProduct.getErrorMessage(), searchProduct.getLiteUrl()));
        return bannedProductsErrorMessageAsList;
    }

    private void getViewToShowEmptySearch(ProductViewModel productViewModel) {
        boolean isGlobalNavWidgetAvailable
                = productViewModel.getGlobalNavViewModel() != null && enableGlobalNavWidget;
        getView().removeLoading();
        getView().setEmptyProduct(isGlobalNavWidgetAvailable ? productViewModel.getGlobalNavViewModel() : null);
        getView().setTotalSearchResultCount("0");
    }

    private void getViewToShowRecommendationItem() {
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
                        if (!recommendationWidgets.isEmpty() && recommendationWidgets.get(0) != null) {
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

        if (isGlobalNavWidgetAvailable) {
            list.add(productViewModel.getGlobalNavViewModel());
            getView().sendImpressionGlobalNav(productViewModel.getGlobalNavViewModel());
        }

        if (!isTickerHasDismissed
                && !textIsEmpty(productViewModel.getTickerModel().getText())) {
            list.add(productViewModel.getTickerModel());
            getView().trackEventImpressionSortPriceMinTicker();
        }

        if (!textIsEmpty(productViewModel.getSuggestionModel().getSuggestionText())) {
            list.add(productViewModel.getSuggestionModel());
        }

        if (searchProduct.getErrorMessage() != null && !searchProduct.getErrorMessage().isEmpty()) {
            list.add(createBannedProductsTickerViewModel(searchProduct.getErrorMessage(), searchProduct.getLiteUrl()));
            getView().trackEventImpressionBannedProducts(false);
        }

        if (productViewModel.getQuickFilterModel() != null) {
            list.add(productViewModel.getQuickFilterModel());
        }

        if (productViewModel.getCpmModel() != null && shouldShowCpmShop(productViewModel)) {
            if (!isGlobalNavWidgetAvailable || productViewModel.getGlobalNavViewModel().getIsShowTopAds()) {
                CpmViewModel cpmViewModel = new CpmViewModel();
                cpmViewModel.setCpmModel(productViewModel.getCpmModel());
                list.add(cpmViewModel);
            }
        }

        productList = convertToListOfVisitable(productViewModel);
        list.addAll(productList);
        if (productViewModel.getRelatedSearchModel() != null) {
            list.add(productViewModel.getRelatedSearchModel());
        }

        if (!textIsEmpty(productViewModel.getAdditionalParams())) {
            additionalParams = productViewModel.getAdditionalParams();
        }

        inspirationCarouselViewModel = productViewModel.getInspirationCarouselViewModel();

        if (inspirationCarouselViewModel.size() > 0) {
            Iterator<InspirationCarouselViewModel> inspirationCarouselViewModelIterator = inspirationCarouselViewModel.iterator();
            while(inspirationCarouselViewModelIterator.hasNext()) {
                InspirationCarouselViewModel data = inspirationCarouselViewModelIterator.next();
                if (data.getPosition() < list.size()) {
                    Visitable product = productList.get(data.getPosition());
                    list.add(list.indexOf(product), data);
                    getView().sendImpressionInspirationCarousel(data);
                    inspirationCarouselViewModelIterator.remove();
                }
            }
        }

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
                && !textIsEmpty(cpm.getCta())
                && !textIsEmpty(cpm.getPromotedText());
    }

    private boolean isViewWillRenderCpmDigital(Cpm cpm) {
        return cpm.getTemplateId() == 4;
    }

    private BannedProductsTickerViewModel createBannedProductsTickerViewModel(String errorMessage, String liteUrl) {
        String htmlErrorMessage = errorMessage
                + " "
                + "Gunakan <a href=\"" + liteUrl + "\">browser</a>";

        return new BannedProductsTickerViewModel(htmlErrorMessage);
    }

    private boolean isExistsFreeOngkirBadge(List<Visitable> productList) {
        for (Visitable product : productList) {
            if (product instanceof ProductItemViewModel) {
                ProductItemViewModel productItemViewModel = (ProductItemViewModel) product;

                if (productItemViewModel.getFreeOngkirViewModel() != null
                        && productItemViewModel.getFreeOngkirViewModel().isActive()) {
                    return true;
                }
            }
        }

        return false;
    }

    private List<String> getListOfImageUrl(InspirationCarouselViewModel data){
        List<String> urlImages = new ArrayList<>();
        for(InspirationCarouselViewModel.Option option : data.getOptions()){
            for(InspirationCarouselViewModel.Option.Product product : option.getProduct()){
                urlImages.add(product.getImgUrl());
            }
        }
        return urlImages;
    }


    private void getViewToSendTrackingOnFirstTimeLoad(ProductViewModel productViewModel) {
        JSONArray afProdIds = new JSONArray();
        HashMap<String, String> moengageTrackingCategory = new HashMap<>();
        HashMap<String, String> gtmTrackingCategory = new HashMap<>();
        ArrayList<String> prodIdArray = new ArrayList<>();

        if (productViewModel.getProductList().size() > 0) {
            for (int i = 0; i < productViewModel.getProductList().size(); i++) {
                if (i < 3) {
                    prodIdArray.add(productViewModel.getProductList().get(i).getProductID());
                    afProdIds.put(productViewModel.getProductList().get(i).getProductID());
                    moengageTrackingCategory.put(String.valueOf(productViewModel.getProductList().get(i).getCategoryID()), productViewModel.getProductList().get(i).getCategoryName());
                }

                gtmTrackingCategory.put(String.valueOf(productViewModel.getProductList().get(i).getCategoryID()), productViewModel.getProductList().get(i).getCategoryName());
            }
        }

        getView().sendTrackingEventAppsFlyerViewListingSearch(afProdIds, productViewModel.getQuery(), prodIdArray);
        getView().sendTrackingEventMoEngageSearchAttempt(productViewModel.getQuery(), !productViewModel.getProductList().isEmpty(), moengageTrackingCategory);
        getView().sendTrackingGTMEventSearchAttempt(createGeneralSearchTrackingModel(productViewModel, gtmTrackingCategory));

        isFirstTimeLoad = false;
    }

    private GeneralSearchTrackingModel createGeneralSearchTrackingModel(ProductViewModel productViewModel, Map<String, String> category) {
        return new GeneralSearchTrackingModel(
                productViewModel.getQuery(),
                productViewModel.getKeywordProcess(),
                productViewModel.getResponseCode(),
                !productViewModel.getProductList().isEmpty(),
                category
        );
    }

    private void enrichWithRelatedSearchParam(RequestParams requestParams) {
        requestParams.putBoolean(SearchApiConst.RELATED, true);
    }

    @Override
    public void onBannedProductsGoToBrowserClick(String liteUrl) {
        String liteUrlWithParameters = appendUrlWithParameters(liteUrl);

        if (userSession.isLoggedIn()) {
            generateSeamlessLoginUrlForLoggedInUser(liteUrlWithParameters);
        } else {
            getViewToRedirectToBrowser(liteUrlWithParameters);
        }
    }

    private String appendUrlWithParameters(String liteUrl) {
        Uri liteUrlUri = Uri.parse(liteUrl);
        Uri.Builder liteUrlWithParametersUriBuilder = liteUrlUri.buildUpon();

        String appClientId = advertisingLocalCache.getString(KEY_ADVERTISING_ID);
        if (appClientId != null && !appClientId.isEmpty()) {
            liteUrlWithParametersUriBuilder.appendQueryParameter(APP_CLIENT_ID, appClientId);
        }

        return liteUrlWithParametersUriBuilder.toString();
    }

    private void generateSeamlessLoginUrlForLoggedInUser(String liteUrl) {
        SeamlessLoginSubscriber seamlessLoginSubscriber = createSeamlessLoginSubscriber(liteUrl);
        seamlessLoginUsecase.generateSeamlessUrl(liteUrl, seamlessLoginSubscriber);
    }

    private SeamlessLoginSubscriber createSeamlessLoginSubscriber(String liteUrl) {
        return new SeamlessLoginSubscriber() {
            @Override
            public void onUrlGenerated(@NotNull String url) {
                getViewToRedirectToBrowser(url);
            }

            @Override
            public void onError(@NotNull String msg) {
                getViewToRedirectToBrowser(liteUrl);
            }
        };
    }

    private void getViewToRedirectToBrowser(String url) {
        if (getView() != null) {
            getView().redirectToBrowser(url);
        }
    }

    protected void enrichWithAdditionalParams(RequestParams requestParams,
                                              Map<String, String> additionalParams) {
        requestParams.putAllString(additionalParams);
    }

    protected Subscriber<DynamicFilterModel> getDynamicFilterSubscriber(final boolean shouldSaveToLocalDynamicFilterDb) {
        return new Subscriber<DynamicFilterModel>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (e != null) {
                    e.printStackTrace();
                }

                getView().renderFailRequestDynamicFilter();
            }

            @Override
            public void onNext(DynamicFilterModel dynamicFilterModel) {
                if (dynamicFilterModel == null) {
                    getView().renderFailRequestDynamicFilter();
                    return;
                }

                if (shouldSaveToLocalDynamicFilterDb && searchLocalCacheHandler != null) {
                    searchLocalCacheHandler.saveDynamicFilterModelLocally(getView().getScreenNameId(), dynamicFilterModel);
                }

                getView().renderDynamicFilter(dynamicFilterModel);
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        if (getDynamicFilterUseCase != null) getDynamicFilterUseCase.unsubscribe();
        if (searchProductFirstPageUseCase != null) searchProductFirstPageUseCase.unsubscribe();
        if (searchProductLoadMoreUseCase != null) searchProductLoadMoreUseCase.unsubscribe();
        if (productWishlistUrlUseCase != null) productWishlistUrlUseCase.unsubscribe();
        if (addWishlistActionUseCase != null) addWishlistActionUseCase.unsubscribe();
        if (removeWishlistActionUseCase != null) removeWishlistActionUseCase.unsubscribe();
        if (recommendationUseCase != null) recommendationUseCase.unsubscribe();
    }
}
