package com.tokopedia.search.result.presentation.presenter.product;

import androidx.annotation.Nullable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.model.ProductCardOptionsModel;
import com.tokopedia.discovery.common.model.ProductCardOptionsModel.WishlistResult;
import com.tokopedia.discovery.common.model.WishlistTrackingModel;
import com.tokopedia.filter.common.data.DataValue;
import com.tokopedia.filter.common.data.DynamicFilterModel;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.abtest.AbTestPlatform;
import com.tokopedia.search.analytics.GeneralSearchTrackingModel;
import com.tokopedia.search.analytics.SearchEventTracking;
import com.tokopedia.search.analytics.SearchTracking;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.search.result.presentation.ProductListSectionContract;
import com.tokopedia.search.result.presentation.mapper.ProductViewModelMapper;
import com.tokopedia.search.result.presentation.mapper.RecommendationViewModelMapper;
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel;
import com.tokopedia.search.result.presentation.model.BannedProductsEmptySearchViewModel;
import com.tokopedia.search.result.presentation.model.BannedProductsTickerViewModel;
import com.tokopedia.search.result.presentation.model.BroadMatchItemViewModel;
import com.tokopedia.search.result.presentation.model.BroadMatchViewModel;
import com.tokopedia.search.result.presentation.model.CpmViewModel;
import com.tokopedia.search.result.presentation.model.EmptySearchProductViewModel;
import com.tokopedia.search.result.presentation.model.FreeOngkirViewModel;
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;
import com.tokopedia.search.result.presentation.model.InspirationCardViewModel;
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel;
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.model.ProductViewModel;
import com.tokopedia.search.result.presentation.model.RecommendationItemViewModel;
import com.tokopedia.search.result.presentation.model.RecommendationTitleViewModel;
import com.tokopedia.search.result.presentation.model.RelatedViewModel;
import com.tokopedia.search.result.presentation.model.SearchInTokopediaViewModel;
import com.tokopedia.search.result.presentation.model.SearchProductCountViewModel;
import com.tokopedia.search.result.presentation.model.SearchProductTitleViewModel;
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageViewModel;
import com.tokopedia.search.result.presentation.model.SeparatorViewModel;
import com.tokopedia.search.result.presentation.model.SuggestionViewModel;
import com.tokopedia.search.utils.SchedulersProvider;
import com.tokopedia.search.utils.SearchFilterUtilsKt;
import com.tokopedia.search.utils.SearchKotlinExtKt;
import com.tokopedia.search.utils.UrlParamUtils;
import com.tokopedia.sortfilter.SortFilterItem;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Badge;
import com.tokopedia.topads.sdk.domain.model.Cpm;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.CpmModel;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.FreeOngkir;
import com.tokopedia.topads.sdk.domain.model.LabelGroup;
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel;
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter;
import com.tokopedia.unifycomponents.ChipsUnify;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Lazy;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.discovery.common.constants.SearchConstant.ABTestRemoteConfigKey.AB_TEST_KEY_THREE_DOTS_SEARCH;
import static com.tokopedia.discovery.common.constants.SearchConstant.ABTestRemoteConfigKey.AB_TEST_THREE_DOTS_SEARCH_FULL_OPTIONS;
import static com.tokopedia.discovery.common.constants.SearchConstant.DefaultViewType.VIEW_TYPE_NAME_BIG_GRID;
import static com.tokopedia.discovery.common.constants.SearchConstant.DefaultViewType.VIEW_TYPE_NAME_LIST;
import static com.tokopedia.discovery.common.constants.SearchConstant.DefaultViewType.VIEW_TYPE_NAME_SMALL_GRID;
import static com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_ANNOTATION;
import static com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_CATEGORY;
import static com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_CURATED;
import static com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_GUIDED;
import static com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.TYPE_RELATED;
import static com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID;
import static com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_INFO;
import static com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_LIST;
import static com.tokopedia.discovery.common.constants.SearchConstant.OnBoarding.THREE_DOTS_ONBOARDING_SHOWN;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_PARAMS;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_GLOBAL_NAV;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_HEADLINE_ADS;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_INSPIRATION_CAROUSEL;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_INSPIRATION_WIDGET;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_PRODUCT_ADS;
import static com.tokopedia.recommendation_widget_common.PARAM_RECOMMENDATIONKt.DEFAULT_VALUE_X_SOURCE;

final class ProductListPresenter
        extends BaseDaggerPresenter<ProductListSectionContract.View>
        implements ProductListSectionContract.Presenter {

    private static final List<Integer> searchNoResultCodeList = Arrays.asList(1, 2, 3, 4, 5, 6, 8);
    private static final List<String> showBroadMatchResponseCodeList = Arrays.asList("0", "4", "5");
    private static final List<String> generalSearchTrackingRelatedKeywordResponseCodeList = Arrays.asList("3", "4", "5", "6");
    private static final List<String> showSuggestionResponseCodeList = Arrays.asList("3", "6", "7");
    private static final List<String> showInspirationCarouselLayout =
            Arrays.asList(LAYOUT_INSPIRATION_CAROUSEL_INFO, LAYOUT_INSPIRATION_CAROUSEL_LIST, LAYOUT_INSPIRATION_CAROUSEL_GRID);
    private static final List<String> showInspirationCardType =
            Arrays.asList(TYPE_ANNOTATION, TYPE_CATEGORY, TYPE_GUIDED, TYPE_CURATED, TYPE_RELATED);
    private static final String SEARCH_PAGE_NAME_RECOMMENDATION = "empty_search";
    private static final String DEFAULT_PAGE_TITLE_RECOMMENDATION = "Rekomendasi untukmu";
    private static final String DEFAULT_USER_ID = "0";
    private static final int QUICK_FILTER_MINIMUM_SIZE = 2;
    private static final List<String> LOCAL_SEARCH_KEY_PARAMS =
            Arrays.asList(SearchApiConst.NAVSOURCE, SearchApiConst.SRP_PAGE_ID, SearchApiConst.SRP_PAGE_TITLE);
    public static final String EMPTY_LOCAL_SEARCH_RESPONSE_CODE = "11";

    private UseCase<SearchProductModel> searchProductFirstPageUseCase;
    private UseCase<SearchProductModel> searchProductLoadMoreUseCase;
    private GetRecommendationUseCase recommendationUseCase;
    private UserSessionInterface userSession;
    private LocalCacheHandler searchOnBoardingLocalCache;
    private Lazy<UseCase<DynamicFilterModel>> getDynamicFilterUseCase;
    private Lazy<UseCase<String>> getProductCountUseCase;
    private Lazy<UseCase<SearchProductModel>> getLocalSearchRecommendationUseCase;
    private TopAdsUrlHitter topAdsUrlHitter;
    private SchedulersProvider schedulersProvider;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    private boolean enableGlobalNavWidget = true;
    private String additionalParams = "";
    private boolean isFirstTimeLoad = false;
    private boolean isTickerHasDismissed = false;
    private int startFrom = 0;
    private int totalData = 0;
    private boolean hasLoadData = false;
    private String responseCode = "";
    private int topAdsCount = 1;
    private String navSource = "";
    private String pageId = "";
    private String pageTitle = "";
    private String autoCompleteApplink = "";
    private boolean isGlobalNavWidgetAvailable = false;
    private boolean isShowHeadlineAdsBasedOnGlobalNav = false;

    private List<Visitable> productList;
    private List<InspirationCarouselViewModel> inspirationCarouselViewModel = new ArrayList<>();
    private List<InspirationCardViewModel> inspirationCardViewModel = new ArrayList<>();
    private List<TopAdsImageViewModel> topAdsImageViewModelList = new ArrayList<>();
    private SuggestionViewModel suggestionViewModel = null;
    private RelatedViewModel relatedViewModel = null;
    private List<Option> quickFilterOptionList = new ArrayList<>();
    private DynamicFilterModel dynamicFilterModel;
    @Nullable private ProductItemViewModel threeDotsProductItem = null;
    private int firstProductPosition = 0;
    private boolean hasFullThreeDotsOptions = false;
    @Nullable private CpmModel cpmModel = null;
    @Nullable private List<CpmData> cpmDataList = null;
    private boolean isABTestNavigationRevamp = false;

    @Inject
    ProductListPresenter(
            @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_FIRST_PAGE_USE_CASE)
            UseCase<SearchProductModel> searchProductFirstPageUseCase,
            @Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_LOAD_MORE_USE_CASE)
            UseCase<SearchProductModel> searchProductLoadMoreUseCase,
            GetRecommendationUseCase recommendationUseCase,
            UserSessionInterface userSession,
            @Named(SearchConstant.OnBoarding.LOCAL_CACHE_NAME)
            LocalCacheHandler searchOnBoardingLocalCache,
            @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE)
            Lazy<UseCase<DynamicFilterModel>> getDynamicFilterUseCase,
            @Named(SearchConstant.SearchProduct.GET_PRODUCT_COUNT_USE_CASE)
            Lazy<UseCase<String>> getProductCountUseCase,
            @Named(SearchConstant.SearchProduct.GET_LOCAL_SEARCH_RECOMMENDATION_USE_CASE)
            Lazy<UseCase<SearchProductModel>> getLocalSearchRecommendationUseCase,
            TopAdsUrlHitter topAdsUrlHitter,
            SchedulersProvider schedulersProvider,
            Lazy<RemoteConfig> remoteConfig
    ) {
        this.searchProductFirstPageUseCase = searchProductFirstPageUseCase;
        this.searchProductLoadMoreUseCase = searchProductLoadMoreUseCase;
        this.recommendationUseCase = recommendationUseCase;
        this.userSession = userSession;
        this.searchOnBoardingLocalCache = searchOnBoardingLocalCache;
        this.getDynamicFilterUseCase = getDynamicFilterUseCase;
        this.getProductCountUseCase = getProductCountUseCase;
        this.getLocalSearchRecommendationUseCase = getLocalSearchRecommendationUseCase;
        this.topAdsUrlHitter = topAdsUrlHitter;
        this.schedulersProvider = schedulersProvider;
    }

    @Override
    public void attachView(ProductListSectionContract.View view) {
        super.attachView(view);

        hasFullThreeDotsOptions = getHasFullThreeDotsOptions();
        isABTestNavigationRevamp = isABTestNavigationRevamp();
    }

    private boolean isABTestNavigationRevamp() {
        try {
            return getView().getABTestRemoteConfig()
                    .getString(AbTestPlatform.NAVIGATION_EXP_TOP_NAV, AbTestPlatform.NAVIGATION_VARIANT_OLD)
                    .equals(AbTestPlatform.NAVIGATION_VARIANT_REVAMP);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean getHasFullThreeDotsOptions() {
        try {
            return getView().getABTestRemoteConfig()
                    .getString(AB_TEST_KEY_THREE_DOTS_SEARCH)
                    .equals(AB_TEST_THREE_DOTS_SEARCH_FULL_OPTIONS);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
    public void loadMoreData(Map<String, Object> searchParameter) {
        if (isShowLocalSearchRecommendation()) getLocalSearchRecommendation();
        else searchProductLoadMore(searchParameter);
    }

    private void searchProductLoadMore(Map<String, Object> searchParameter) {
        checkViewAttached();

        if (searchParameter == null) return;

        RequestParams requestParams = createInitializeSearchParam(searchParameter);
        enrichWithRelatedSearchParam(requestParams);
        enrichWithAdditionalParams(requestParams);

        RequestParams useCaseRequestParams = createSearchProductRequestParams(requestParams);

        // Unsubscribe first in case user has slow connection, and the previous loadMoreUseCase has not finished yet.
        searchProductLoadMoreUseCase.unsubscribe();
        searchProductLoadMoreUseCase.execute(useCaseRequestParams, getLoadMoreDataSubscriber(requestParams.getParameters()));
    }

    private RequestParams createInitializeSearchParam(Map<String, Object> searchParameter) {
        RequestParams requestParams = RequestParams.create();

        putRequestParamsOtherParameters(requestParams, searchParameter);
        requestParams.putAll(searchParameter);

        return requestParams;
    }

    private void putRequestParamsOtherParameters(RequestParams requestParams, Map<String, Object> searchParameter) {
        putRequestParamsSearchParameters(requestParams, searchParameter);

        putRequestParamsTopAdsParameters(requestParams);

        putRequestParamsDepartmentIdIfNotEmpty(requestParams, searchParameter);
    }

    private void putRequestParamsSearchParameters(RequestParams requestParams, Map<String, Object> searchParameter) {
        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH);
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(SearchApiConst.ROWS, getSearchRows());
        requestParams.putString(SearchApiConst.OB, getSearchSort(searchParameter));
        requestParams.putString(SearchApiConst.START, String.valueOf(getStartFrom()));
        requestParams.putString(SearchApiConst.IMAGE_SIZE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SIZE);
        requestParams.putString(SearchApiConst.IMAGE_SQUARE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SQUARE);
        requestParams.putString(SearchApiConst.Q, omitNewlineAndPlusSign(getSearchQuery(searchParameter)));
        requestParams.putString(SearchApiConst.UNIQUE_ID, getUniqueId());
        requestParams.putString(SearchApiConst.USER_ID, getUserId());
    }

    private String getSearchRows() {
        return SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS;
    }

    private String getSearchSort(Map<String, Object> searchParameter) {
        Object sortObject = searchParameter.get(SearchApiConst.OB);
        String sort = sortObject == null ? "" : sortObject.toString();

        return !textIsEmpty(sort) ? sort : SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT;
    }

    private String getSearchQuery(Map<String, Object> searchParameter) {
        Object queryObject = searchParameter.get(SearchApiConst.Q);
        String query = queryObject == null ? "" : queryObject.toString();

        return omitNewlineAndPlusSign(query);
    }

    private String omitNewlineAndPlusSign(String text) {
        return text.replace("\n", "").replace("+", " ");
    }

    private String getUniqueId() {
        return userSession.isLoggedIn() ?
                AuthHelper.getMD5Hash(userSession.getUserId()) :
                AuthHelper.getMD5Hash(userSession.getDeviceId());
    }

    private void putRequestParamsTopAdsParameters(RequestParams requestParams) {
        requestParams.putInt(TopAdsParams.KEY_ITEM, 2);
        requestParams.putString(TopAdsParams.KEY_EP, TopAdsParams.DEFAULT_KEY_EP);
        requestParams.putString(TopAdsParams.KEY_SRC, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH);
        requestParams.putBoolean(TopAdsParams.KEY_WITH_TEMPLATE, true);
        requestParams.putInt(TopAdsParams.KEY_PAGE, getTopAdsKeyPage());
    }

    private int getTopAdsKeyPage() {
        try {
            int defaultValueStart = Integer.parseInt(SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS);
            return getStartFrom() / defaultValueStart + 1;
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

    private RequestParams createSearchProductRequestParams(RequestParams requestParams) {
        boolean isLocalSearch = isLocalSearch();
        boolean isSkipGlobalNavWidget = isLocalSearch || getView().isAnyFilterActive() || getView().isAnySortActive();

        RequestParams useCaseRequestParams = RequestParams.create();

        useCaseRequestParams.putObject(SEARCH_PRODUCT_PARAMS, requestParams.getParameters());
        useCaseRequestParams.putBoolean(SEARCH_PRODUCT_SKIP_PRODUCT_ADS, isLocalSearch);
        useCaseRequestParams.putBoolean(SEARCH_PRODUCT_SKIP_HEADLINE_ADS, isLocalSearch);
        useCaseRequestParams.putBoolean(SEARCH_PRODUCT_SKIP_INSPIRATION_CAROUSEL, isLocalSearch);
        useCaseRequestParams.putBoolean(SEARCH_PRODUCT_SKIP_INSPIRATION_WIDGET, isLocalSearch);
        useCaseRequestParams.putBoolean(SEARCH_PRODUCT_SKIP_GLOBAL_NAV, isSkipGlobalNavWidget);

        return useCaseRequestParams;
    }

    private Subscriber<SearchProductModel> getLoadMoreDataSubscriber(final Map<String, Object> searchParameter) {
        return new Subscriber<SearchProductModel>() {
            @Override
            public void onStart() {
                loadMoreDataSubscriberOnStartIfViewAttached();
            }

            @Override
            public void onNext(SearchProductModel searchProductModel) {
                loadMoreDataSubscriberOnNextIfViewAttached(searchParameter, searchProductModel);
            }

            @Override
            public void onCompleted() {
                loadMoreDataSubscriberOnCompleteIfViewAttached();
            }

            @Override
            public void onError(Throwable error) {
                loadMoreDataSubscriberOnErrorIfViewAttached(searchParameter, error);
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

    private void loadMoreDataSubscriberOnNextIfViewAttached(Map<String, Object> searchParameter, SearchProductModel searchProductModel) {
        if (isViewAttached()) {
            int lastProductItemPositionFromCache = getView().getLastProductItemPositionFromCache();

            ProductViewModelMapper mapper = new ProductViewModelMapper();
            ProductViewModel productViewModel = mapper
                    .convertToProductViewModel(lastProductItemPositionFromCache, searchProductModel, pageTitle);

            saveLastProductItemPositionToCache(lastProductItemPositionFromCache, productViewModel.getProductList());

            additionalParams = productViewModel.getAdditionalParams();

            if (productViewModel.getProductList().isEmpty()) {
                getViewToProcessEmptyResultDuringLoadMore(searchProductModel.getSearchProduct());
            } else {
                getViewToShowMoreData(searchParameter, searchProductModel, productViewModel);
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

    private void getViewToProcessEmptyResultDuringLoadMore(SearchProductModel.SearchProduct searchProduct) {
        List<Visitable> list = new ArrayList<>();
        processBroadMatch(searchProduct, list);

        addSearchInTokopedia(searchProduct, list);

        getView().removeLoading();
        getView().addProductList(list);
    }

    private void getViewToShowMoreData(Map<String, Object> searchParameter, SearchProductModel searchProductModel, ProductViewModel productViewModel) {
        List<Visitable> list = new ArrayList<>(createProductItemVisitableList(productViewModel));
        productList.addAll(list);

        processHeadlineAds(searchParameter, list);
        processTopAdsImageViewModel(searchParameter, list);
        processInspirationCardPosition(searchParameter, list);
        processInspirationCarouselPosition(searchParameter, list);
        processBroadMatch(searchProductModel.getSearchProduct(), list);

        addSearchInTokopedia(searchProductModel.getSearchProduct(), list);

        getView().removeLoading();
        getView().addProductList(list);
        getView().addLoading();
        getView().updateScrollListener();
    }

    private List<Visitable> createProductItemVisitableList(ProductViewModel productViewModel) {
        List<Visitable> list = new ArrayList<>(productViewModel.getProductList());

        if (isLocalSearch()) return list;

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
                    item.setTopadsClickShopUrl(topAds.getShopClickUrl());
                    item.setProductName(topAds.getProduct().getName());
                    item.setPrice(topAds.getProduct().getPriceFormat());
                    item.setShopCity(topAds.getShop().getLocation());
                    item.setImageUrl(topAds.getProduct().getImage().getS_ecs());
                    item.setImageUrl300(topAds.getProduct().getImage().getM_ecs());
                    item.setImageUrl700(topAds.getProduct().getImage().getM_ecs());
                    item.setWishlisted(topAds.getProduct().isWishlist());
                    item.setRatingString(topAds.getProduct().getProductRatingFormat());
                    item.setBadgesList(mapBadges(topAds.getShop().getBadges()));
                    item.setNew(topAds.getProduct().isProductNewLabel());
                    item.setShopID(topAds.getShop().getId());
                    item.setShopName(topAds.getShop().getName());
                    item.setShopOfficialStore(topAds.getShop().isShop_is_official());
                    item.setShopPowerMerchant(topAds.getShop().isGoldShop());
                    item.setShopUrl(topAds.getShop().getUri());
                    item.setOriginalPrice(topAds.getProduct().getCampaign().getOriginalPrice());
                    item.setDiscountPercentage(topAds.getProduct().getCampaign().getDiscountPercentage());
                    item.setLabelGroupList(mapLabelGroupList(topAds.getProduct().getLabelGroupList()));
                    item.setFreeOngkirViewModel(mapFreeOngkir(topAds.getProduct().getFreeOngkir()));
                    item.setPosition(topAdsCount);
                    item.setCategoryID(topAds.getProduct().getCategory().getId());
                    item.setCategoryBreadcrumb(topAds.getProduct().getCategoryBreadcrumb());
                    item.setProductUrl(topAds.getProduct().getUri());
                    item.setMinOrder(topAds.getProduct().getProductMinimumOrder());

                    list.add(i, item);
                    j++;
                    topAdsCount++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private boolean isLocalSearch() {
        return !textIsEmpty(navSource) && !textIsEmpty(pageId);
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
                            labelGroup.getPosition(), labelGroup.getType(), labelGroup.getTitle(), ""
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

    private void loadMoreDataSubscriberOnErrorIfViewAttached(Map<String, Object> searchParameter, Throwable error) {
        if (isViewAttached()) {
            decrementStart();

            getView().removeLoading();
            getView().hideRefreshLayout();
            getView().showNetworkError(getStartFrom());
            getView().logWarning(UrlParamUtils.generateUrlParamString(searchParameter), error);
        }
    }

    private void decrementStart() {
        startFrom = startFrom - Integer.parseInt(SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS);
    }

    @Override
    public void loadData(Map<String, Object> searchParameter) {
        checkViewAttached();

        setNavSource(SearchKotlinExtKt.getValueString(searchParameter, SearchApiConst.NAVSOURCE));
        setPageId(SearchKotlinExtKt.getValueString(searchParameter, SearchApiConst.SRP_PAGE_ID));
        setPageTitle(SearchKotlinExtKt.getValueString(searchParameter, SearchApiConst.SRP_PAGE_TITLE));
        resetAdditionalParams();

        if (searchParameter == null) return;

        RequestParams requestParams = createInitializeSearchParam(searchParameter);
        enrichWithRelatedSearchParam(requestParams);

        RequestParams useCaseRequestParams = createSearchProductRequestParams(requestParams);

        getView().stopPreparePagePerformanceMonitoring();
        getView().startNetworkRequestPerformanceMonitoring();

        // Unsubscribe first in case user has slow connection, and the previous loadDataUseCase has not finished yet.
        searchProductFirstPageUseCase.unsubscribe();
        searchProductFirstPageUseCase.execute(useCaseRequestParams, getLoadDataSubscriber(requestParams.getParameters()));
    }

    private void setNavSource(String navSource) {
        this.navSource = navSource;
    }

    private void setPageId(String pageId) {
        this.pageId = pageId;
    }

    private void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    private void resetAdditionalParams() {
        this.additionalParams = "";
    }

    private Subscriber<SearchProductModel> getLoadDataSubscriber(final Map<String, Object> searchParameter) {
        return new Subscriber<SearchProductModel>() {
            @Override
            public void onStart() {
                loadDataSubscriberOnStartIfViewAttached();
            }

            @Override
            public void onCompleted() {
                loadDataSubscriberOnCompleteIfViewAttached();
            }

            @Override
            public void onError(Throwable error) {
                loadDataSubscriberOnErrorIfViewAttached(searchParameter, error);
            }

            @Override
            public void onNext(SearchProductModel searchProductModel) {
                loadDataSubscriberOnNextIfViewAttached(searchParameter, searchProductModel);
            }
        };
    }

    private void loadDataSubscriberOnStartIfViewAttached() {
        if (isViewAttached()) {
            getView().showRefreshLayout();
            incrementStart();
        }
    }

    private void loadDataSubscriberOnCompleteIfViewAttached() {
        if (isViewAttached()) {
            getView().hideRefreshLayout();
        }
    }

    private void loadDataSubscriberOnErrorIfViewAttached(Map<String, Object> searchParameter, Throwable throwable) {
        if (isViewAttached()) {
            decrementStart();

            getView().removeLoading();
            getView().showNetworkError(0);
            getView().hideRefreshLayout();
            getView().logWarning(UrlParamUtils.generateUrlParamString(searchParameter), throwable);
        }
    }

    private void loadDataSubscriberOnNextIfViewAttached(Map<String, Object> searchParameter, SearchProductModel searchProductModel) {
        if (isViewAttached()) {
            getView().stopNetworkRequestPerformanceMonitoring();
            getView().startRenderPerformanceMonitoring();

            if (isSearchRedirected(searchProductModel)) {
                getViewToRedirectSearch(searchProductModel);
            } else {
                getViewToProcessSearchResult(searchParameter, searchProductModel);
            }
        }
    }

    private boolean isSearchRedirected(SearchProductModel searchProductModel) {
        SearchProductModel.Redirection redirection = searchProductModel.getSearchProduct().getData().getRedirection();

        return redirection != null
                && !textIsEmpty(redirection.getRedirectApplink());
    }

    private void getViewToRedirectSearch(SearchProductModel searchProductModel) {
        ProductViewModel productViewModel = createProductViewModelWithPosition(searchProductModel);
        getViewToSendTrackingSearchAttempt(productViewModel);

        String applink = searchProductModel.getSearchProduct().getData().getRedirection().getRedirectApplink();
        getView().redirectSearchToAnotherPage(applink);
    }

    private void getViewToProcessSearchResult(Map<String, Object> searchParameter, SearchProductModel searchProductModel) {
        updateValueEnableGlobalNavWidget();

        ProductViewModel productViewModel = createProductViewModelWithPosition(searchProductModel);

        setResponseCode(productViewModel.getResponseCode());
        setSuggestionViewModel(productViewModel.getSuggestionModel());
        setRelatedViewModel(productViewModel.getRelatedViewModel());
        setAutoCompleteApplink(productViewModel.getAutocompleteApplink());
        setTotalData(productViewModel.getTotalData());

        doInBackground(productViewModel, this::sendTrackingNoSearchResult);

        getView().setAutocompleteApplink(productViewModel.getAutocompleteApplink());
        getView().setDefaultLayoutType(productViewModel.getDefaultView());

        if (productViewModel.getProductList().isEmpty()) {
            getViewToHandleEmptyProductList(searchProductModel.getSearchProduct(), productViewModel, searchParameter);
        } else {
            getViewToShowProductList(searchParameter, searchProductModel, productViewModel);
            processDefaultQuickFilter(searchProductModel);
            processQuickFilter(searchProductModel.getQuickFilterModel());
        }

        getView().updateScrollListener();

        if (isFirstTimeLoad) {
            getViewToSendTrackingSearchAttempt(productViewModel);
        }
    }

    private void updateValueEnableGlobalNavWidget() {
        if (getView() != null) {
            enableGlobalNavWidget = !getView().isLandingPage();
        }
    }

    private void sendTrackingNoSearchResult(ProductViewModel productViewModel) {
        try {
            String alternativeKeyword = "";
            if (productViewModel.getRelatedViewModel() != null) {
                alternativeKeyword = productViewModel.getRelatedViewModel().getRelatedKeyword();
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

        ProductViewModelMapper mapper = new ProductViewModelMapper();
        ProductViewModel productViewModel = mapper
                .convertToProductViewModel(lastProductItemPositionFromCache, searchProductModel, pageTitle);

        saveLastProductItemPositionToCache(lastProductItemPositionFromCache, productViewModel.getProductList());

        return productViewModel;
    }

    private void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    private void setSuggestionViewModel(SuggestionViewModel suggestionViewModel) {
        this.suggestionViewModel = suggestionViewModel;
    }

    private void setRelatedViewModel(RelatedViewModel relatedViewModel) {
        this.relatedViewModel = relatedViewModel;
    }

    private void setAutoCompleteApplink(String autoCompleteApplink) {
        this.autoCompleteApplink = autoCompleteApplink;
    }

    private void getViewToHandleEmptyProductList(
            SearchProductModel.SearchProduct searchProduct,
            ProductViewModel productViewModel,
            Map<String, Object> searchParameter
    ) {
        getView().hideQuickFilterShimmering();

        if (isShowBroadMatch()) {
            getViewToShowBroadMatchToReplaceEmptySearch();
        } else {
            if (productViewModel.getErrorMessage() != null && !productViewModel.getErrorMessage().isEmpty()) {
                getViewToHandleEmptySearchWithErrorMessage(searchProduct);
            } else {
                getViewToShowEmptySearch(productViewModel);
            }

            getViewToShowRecommendationItem();
        }
    }

    private boolean isShowBroadMatch() {
        return showBroadMatchResponseCodeList.contains(responseCode)
                && relatedViewModel != null
                && !relatedViewModel.getBroadMatchViewModelList().isEmpty();
    }

    private void getViewToShowBroadMatchToReplaceEmptySearch() {
        List<Visitable> visitableList = new ArrayList<>();

        addBroadMatchToVisitableList(visitableList);

        getView().removeLoading();
        getView().setProductList(visitableList);
        getView().backToTop();
    }

    private void addBroadMatchToVisitableList(List<Visitable> visitableList) {
        if (suggestionViewModel != null && !textIsEmpty(suggestionViewModel.getSuggestionText())) {
            visitableList.add(suggestionViewModel);

            suggestionViewModel = null;
        }

        if (relatedViewModel != null) {
            visitableList.addAll(relatedViewModel.getBroadMatchViewModelList());

            relatedViewModel = null;
        }
    }

    private void getViewToHandleEmptySearchWithErrorMessage(SearchProductModel.SearchProduct searchProduct) {
        getView().removeLoading();
        getView().setBannedProductsErrorMessage(createBannedProductsErrorMessageAsList(searchProduct));
        getView().trackEventImpressionBannedProducts(true);
    }

    private List<Visitable> createBannedProductsErrorMessageAsList(SearchProductModel.SearchProduct searchProduct) {
        List<Visitable> bannedProductsErrorMessageAsList = new ArrayList<>();
        bannedProductsErrorMessageAsList.add(new BannedProductsEmptySearchViewModel(searchProduct.getHeader().getErrorMessage()));
        return bannedProductsErrorMessageAsList;
    }

    private void getViewToShowEmptySearch(ProductViewModel productViewModel) {
        getView().removeLoading();

        GlobalNavViewModel globalNavViewModel = getGlobalNavViewModel(productViewModel);
        boolean isBannerAdsAllowed = globalNavViewModel == null;

        clearData();
        getView().setEmptyProduct(globalNavViewModel, createEmptySearchViewModel(isBannerAdsAllowed));
    }

    private GlobalNavViewModel getGlobalNavViewModel(ProductViewModel productViewModel) {
        boolean isGlobalNavWidgetAvailable
                = productViewModel.getGlobalNavViewModel() != null && enableGlobalNavWidget;

        return isGlobalNavWidgetAvailable ? productViewModel.getGlobalNavViewModel() : null;
    }

    private EmptySearchProductViewModel createEmptySearchViewModel(boolean isBannerAdsAllowed) {
        EmptySearchProductViewModel emptySearchViewModel = new EmptySearchProductViewModel();

        emptySearchViewModel.setBannerAdsAllowed(isBannerAdsAllowed);
        emptySearchViewModel.setIsFilterActive(getView().isAnyFilterActive());

        if (isShowLocalSearchRecommendation() && !getView().isAnyFilterActive()) {
            emptySearchViewModel.setLocalSearch(true);
            emptySearchViewModel.setGlobalSearchApplink(constructGlobalSearchApplink());
            emptySearchViewModel.setKeyword(getView().getQueryKey());
            emptySearchViewModel.setPageTitle(pageTitle);
        }

        return emptySearchViewModel;
    }

    private boolean isShowLocalSearchRecommendation() {
        return isLocalSearch() && responseCode.equals(EMPTY_LOCAL_SEARCH_RESPONSE_CODE);
    }

    private void getViewToShowRecommendationItem() {
        getView().addLoading();

        if (isShowLocalSearchRecommendation()) getLocalSearchRecommendation();
        else getGlobalSearchRecommendation();
    }

    private void getLocalSearchRecommendation() {
        getLocalSearchRecommendationUseCase.get().execute(
                createLocalSearchRequestParams(),
                createLocalSearchRecommenationSubscriber()
        );
    }

    @NotNull
    private RequestParams createLocalSearchRequestParams() {
        RequestParams requestParams = RequestParams.create();

        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH);
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(SearchApiConst.NAVSOURCE, navSource);
        requestParams.putString(SearchApiConst.SRP_PAGE_TITLE, pageTitle);
        requestParams.putString(SearchApiConst.SRP_PAGE_ID, pageId);
        requestParams.putString(SearchApiConst.START, String.valueOf(getStartFrom()));
        requestParams.putString(SearchApiConst.ROWS, getSearchRows());

        return requestParams;
    }

    @NotNull
    private Subscriber<SearchProductModel> createLocalSearchRecommenationSubscriber() {
        return new Subscriber<SearchProductModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e != null) e.printStackTrace();
            }

            @Override
            public void onNext(SearchProductModel searchProductModel) {
                getLocalSearchRecommendationSuccess(searchProductModel);
            }
        };
    }

    private void getLocalSearchRecommendationSuccess(SearchProductModel searchProductModel) {
        if (isViewNotAttached()) return;

        ProductViewModel productViewModel = createProductViewModelMapperLocalSearchRecommendation(searchProductModel);

        List<Visitable> visitableList = new ArrayList<>();

        if (startFrom == 0) visitableList.add(new SearchProductTitleViewModel(pageTitle));

        visitableList.addAll(productViewModel.getProductList());

        incrementStart();
        setTotalData(searchProductModel.getSearchProduct().getHeader().getTotalData());

        getView().removeLoading();
        getView().addLocalSearchRecommendation(visitableList);

        if (hasNextPage()) getView().addLoading();

        getView().updateScrollListener();
    }

    private ProductViewModel createProductViewModelMapperLocalSearchRecommendation(SearchProductModel searchProductModel) {
        if (startFrom == 0) getView().clearLastProductItemPositionFromCache();

        int lastProductItemPositionFromCache = getView().getLastProductItemPositionFromCache();

        ProductViewModelMapper mapper = new ProductViewModelMapper();
        ProductViewModel productViewModel = mapper
                .convertToProductViewModel(lastProductItemPositionFromCache, searchProductModel, pageTitle);

        saveLastProductItemPositionToCache(lastProductItemPositionFromCache, productViewModel.getProductList());

        return productViewModel;
    }

    private void getGlobalSearchRecommendation() {
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

    private void getViewToShowProductList(Map<String, Object> searchParameter, SearchProductModel searchProductModel, ProductViewModel productViewModel) {
        SearchProductModel.SearchProduct searchProduct = searchProductModel.getSearchProduct();

        List<Visitable> list = new ArrayList<>();

        if (!productViewModel.isQuerySafe()) {
            getView().showAdultRestriction();
        }

        if (isABTestNavigationRevamp) {
            list.add(new SearchProductCountViewModel(list.size(), searchProduct.getHeader().getTotalDataText()));
        }

        addPageTitle(list);

        isGlobalNavWidgetAvailable = getIsGlobalNavWidgetAvailable(productViewModel);

        if (isGlobalNavWidgetAvailable) {
            list.add(productViewModel.getGlobalNavViewModel());
            getView().sendImpressionGlobalNav(productViewModel.getGlobalNavViewModel());

            isShowHeadlineAdsBasedOnGlobalNav = productViewModel.getGlobalNavViewModel().getIsShowTopAds();
        }

        if (!isTickerHasDismissed
                && !textIsEmpty(productViewModel.getTickerModel().getText())) {
            list.add(productViewModel.getTickerModel());
            int typeId = productViewModel.getTickerModel().getTypeId();
            getView().trackEventImpressionTicker(typeId);
        }

        if (shouldShowSuggestion(productViewModel)) {
            list.add(productViewModel.getSuggestionModel());
        }

        if (searchProduct.getHeader().getErrorMessage() != null && !searchProduct.getHeader().getErrorMessage().isEmpty()) {
            list.add(createBannedProductsTickerViewModel(searchProduct.getHeader().getErrorMessage()));
            getView().trackEventImpressionBannedProducts(false);
        }

        if(productViewModel.getCpmModel() != null) {
            cpmModel = productViewModel.getCpmModel();
            cpmDataList = productViewModel.getCpmModel().getData();
        }

        topAdsCount = 1;
        productList = createProductItemVisitableList(productViewModel);
        list.addAll(productList);

        processHeadlineAds(searchParameter, list);

        additionalParams = productViewModel.getAdditionalParams();

        inspirationCarouselViewModel = productViewModel.getInspirationCarouselViewModel();
        processInspirationCarouselPosition(searchParameter, list);

        inspirationCardViewModel = productViewModel.getInspirationCardViewModel();
        processInspirationCardPosition(searchParameter, list);

        processBroadMatch(searchProduct, list);

        topAdsImageViewModelList = searchProductModel.getTopAdsImageViewModelList();
        processTopAdsImageViewModel(searchParameter, list);

        addSearchInTokopedia(searchProduct, list);

        firstProductPosition = getFirstProductPosition(list);

        getView().removeLoading();
        getView().setProductList(list);
        getView().backToTop();
        getView().showFreeOngkirShowCase(isExistsFreeOngkirBadge(list));

        if (productViewModel.getTotalData() > Integer.parseInt(getSearchRows())) {
            getView().addLoading();
        }

        getView().stopTracePerformanceMonitoring();
    }

    private int getFirstProductPosition(List<Visitable> list) {
        if (productList.isEmpty()) return 0;

        int firstProductPosition = list.indexOf(productList.get(0));

        return Math.max(firstProductPosition, 0);
    }

    private void addPageTitle(List<Visitable> list) {
        if (pageTitle == null || pageTitle.isEmpty()) return;

        list.add(new SearchProductTitleViewModel(pageTitle));
    }

    private boolean getIsGlobalNavWidgetAvailable(ProductViewModel productViewModel) {
        return productViewModel.getGlobalNavViewModel() != null
                && enableGlobalNavWidget
                && !getView().isAnyFilterActive()
                && !getView().isAnySortActive();
    }

    private void addSearchInTokopedia(SearchProductModel.SearchProduct searchProduct, List<Visitable> list) {
        if (isLastPage(searchProduct) && isLocalSearch()) {
            String globalSearchApplink = constructGlobalSearchApplink();
            SearchInTokopediaViewModel searchInTokopediaViewModel = new SearchInTokopediaViewModel(globalSearchApplink);

            list.add(searchInTokopediaViewModel);
        }
    }

    private String constructGlobalSearchApplink() {
        String globalSearchApplink = ApplinkConstInternalDiscovery.SEARCH_RESULT;

        String queryParams = UrlParamUtils.getQueryParams(autoCompleteApplink);
        String globalSearchQueryParams = UrlParamUtils.removeKeysFromQueryParams(queryParams, LOCAL_SEARCH_KEY_PARAMS);

        if (!textIsEmpty(globalSearchQueryParams))
            globalSearchApplink += "?" + globalSearchQueryParams;

        return globalSearchApplink;
    }

    private boolean shouldShowSuggestion(ProductViewModel productViewModel) {
        return showSuggestionResponseCodeList.contains(responseCode)
                && !textIsEmpty(productViewModel.getSuggestionModel().getSuggestionText());
    }

    private void processHeadlineAds(Map<String, Object> searchParameter, List<Visitable> visitableList) {
        boolean canProcessHeadlineAds = isHeadlineAdsAllowed() && cpmDataList != null && cpmDataList.size() > 0;

        if (!canProcessHeadlineAds) return;

        Iterator<CpmData> cpmDataIterator = cpmDataList.iterator();

        while(cpmDataIterator.hasNext()) {
            CpmData data = cpmDataIterator.next();
            int position = data.getCpm() == null ? -1 : data.getCpm().getPosition();

            if (position < 0 || !shouldShowCpmShop(data)) {
                cpmDataIterator.remove();
                continue;
            }

            if (position > productList.size()) continue;

            try {
                CpmViewModel cpmViewModel = createCpmViewModel(data);

                if (position == 0 || position == 1) processHeadlineAdsAtTop(visitableList, cpmViewModel);
                else processHeadlineAdsAtPosition(visitableList, position, cpmViewModel);

                cpmDataIterator.remove();
            } catch (Exception exception) {
                exception.printStackTrace();
                getView().logWarning(UrlParamUtils.generateUrlParamString(searchParameter), exception);
            }
        }
    }

    private boolean isHeadlineAdsAllowed() {
        return !isLocalSearch()
                && (!isGlobalNavWidgetAvailable || isShowHeadlineAdsBasedOnGlobalNav);
    }

    private boolean shouldShowCpmShop(CpmData cpmData) {
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

    @Nullable
    private CpmViewModel createCpmViewModel(CpmData cpmData) {
        if (cpmModel == null) return null;

        CpmModel cpmForViewModel = createCpmForViewModel(cpmData);

        if (cpmForViewModel == null) return null;

        CpmViewModel cpmViewModel = new CpmViewModel();
        cpmViewModel.setCpmModel(cpmForViewModel);

        return cpmViewModel;
    }

    @Nullable
    private CpmModel createCpmForViewModel(CpmData cpmData) {
        if (cpmModel == null) return null;

        CpmModel cpmForViewModel = new CpmModel();
        cpmForViewModel.setHeader(cpmModel.getHeader());
        cpmForViewModel.setStatus(cpmModel.getStatus());
        cpmForViewModel.setError(cpmModel.getError());

        List<CpmData> cpmList = new ArrayList<>();
        cpmList.add(cpmData);
        cpmForViewModel.setData(cpmList);

        return cpmForViewModel;
    }

    private void processHeadlineAdsAtTop(List<Visitable> visitableList, CpmViewModel cpmViewModel) {
        Visitable product = productList.get(0);
        visitableList.add(visitableList.indexOf(product), cpmViewModel);
    }

    private void processHeadlineAdsAtPosition(List<Visitable> visitableList, int position, CpmViewModel cpmViewModel) {
        Visitable product = productList.get(position - 1);
        visitableList.add(visitableList.indexOf(product) + 1, cpmViewModel);
    }

    private BannedProductsTickerViewModel createBannedProductsTickerViewModel(String errorMessage) {
        String htmlErrorMessage = errorMessage + " Gunakan browser";

        return new BannedProductsTickerViewModel(htmlErrorMessage);
    }

    private void processInspirationCardPosition(Map<String, Object> searchParameter, List<Visitable> list) {
        if (inspirationCardViewModel.size() > 0) {
            Iterator<InspirationCardViewModel> inspirationCardViewModelIterator = inspirationCardViewModel.iterator();

            while(inspirationCardViewModelIterator.hasNext()) {
                InspirationCardViewModel data = inspirationCardViewModelIterator.next();

                if (data.getPosition() <= 0) {
                    inspirationCardViewModelIterator.remove();
                    continue;
                }

                if (data.getPosition() <= productList.size() && shouldShowInspirationCard(data.getType())) {
                    try {
                        Visitable product = productList.get(data.getPosition() - 1);
                        list.add(list.indexOf(product) + 1, data);
                        inspirationCardViewModelIterator.remove();
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                        getView().logWarning(UrlParamUtils.generateUrlParamString(searchParameter), exception);
                    }
                }
            }
        }
    }

    private boolean shouldShowInspirationCard(String type) {
        return showInspirationCardType.contains(type);
    }

    private void processInspirationCarouselPosition(Map<String, Object> searchParameter, List<Visitable> list) {
        if (inspirationCarouselViewModel.size() > 0) {
            Iterator<InspirationCarouselViewModel> inspirationCarouselViewModelIterator = inspirationCarouselViewModel.iterator();

            while(inspirationCarouselViewModelIterator.hasNext()) {
                InspirationCarouselViewModel data = inspirationCarouselViewModelIterator.next();

                if (data.getPosition() <= 0) {
                    inspirationCarouselViewModelIterator.remove();
                    continue;
                }

                if (data.getPosition() <= productList.size() && shouldShowInspirationCarousel(data.getLayout())) {
                    try {
                        Visitable product = productList.get(data.getPosition() - 1);
                        list.add(list.indexOf(product) + 1, data);
                        inspirationCarouselViewModelIterator.remove();
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                        getView().logWarning(UrlParamUtils.generateUrlParamString(searchParameter), exception);
                    }
                }
            }
        }
    }

    private boolean shouldShowInspirationCarousel(String layout) {
        return showInspirationCarouselLayout.contains(layout);
    }

    private void processBroadMatch(SearchProductModel.SearchProduct searchProduct, List<Visitable> list) {
        try {
            if (isShowBroadMatch()) {
                int broadMatchPosition = relatedViewModel.getPosition();

                if (broadMatchPosition == 0) processBroadMatchAtBottom(searchProduct, list);
                else if (broadMatchPosition == 1) processBroadMatchAtTop(list);
                else if (broadMatchPosition > 1) processBroadMatchAtPosition(list, broadMatchPosition);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void processBroadMatchAtBottom(@NotNull SearchProductModel.SearchProduct searchProduct, List<Visitable> list) {
        if (isLastPage(searchProduct)) {
            list.add(new SeparatorViewModel());
            addBroadMatchToVisitableList(list);
        }
    }

    private boolean isLastPage(@NotNull SearchProductModel.SearchProduct searchProduct) {
        boolean hasNextPage = getStartFrom() < searchProduct.getHeader().getTotalData();

        return !hasNextPage;
    }

    private void processBroadMatchAtTop(List<Visitable> list) {
        List<Visitable> broadMatchVisitableList = new ArrayList<>();

        addBroadMatchToVisitableList(broadMatchVisitableList);
        broadMatchVisitableList.add(new SeparatorViewModel());

        list.addAll(list.indexOf(productList.get(0)), broadMatchVisitableList);
    }

    private void processBroadMatchAtPosition(List<Visitable> list, int broadMatchPosition) {
        if (productList.size() < broadMatchPosition) return;

        List<Visitable> broadMatchVisitableList = new ArrayList<>();

        broadMatchVisitableList.add(new SeparatorViewModel());
        addBroadMatchToVisitableList(broadMatchVisitableList);
        broadMatchVisitableList.add(new SeparatorViewModel());

        Visitable productItemAtBroadMatchPosition = productList.get(broadMatchPosition - 1);
        int broadMatchIndex = list.indexOf(productItemAtBroadMatchPosition) + 1;

        list.addAll(broadMatchIndex, broadMatchVisitableList);
    }

    private void processTopAdsImageViewModel(Map<String, Object> searchParameter, List<Visitable> list) {
        if (topAdsImageViewModelList.size() == 0) return;

        Iterator<TopAdsImageViewModel> topAdsImageViewModelIterator = topAdsImageViewModelList.iterator();

        while(topAdsImageViewModelIterator.hasNext()) {
            TopAdsImageViewModel data = topAdsImageViewModelIterator.next();

            if (data.getPosition() <= 0) {
                topAdsImageViewModelIterator.remove();
                continue;
            }

            if (data.getPosition() <= productList.size()) {
                try {
                    processTopAdsImageViewModelInPosition(list, data);
                    topAdsImageViewModelIterator.remove();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    getView().logWarning(UrlParamUtils.generateUrlParamString(searchParameter), exception);
                }
            }
        }
    }

    private void processTopAdsImageViewModelInPosition(List<Visitable> list, TopAdsImageViewModel data) {
        boolean isTopPosition = data.getPosition() == 1;
        SearchProductTopAdsImageViewModel searchProductTopAdsImageViewModel = new SearchProductTopAdsImageViewModel(data);

        if (isTopPosition) {
            int index = getIndexOfTopAdsImageViewModelAtTop(list);
            list.add(index, searchProductTopAdsImageViewModel);
        }
        else {
            Visitable product = productList.get(data.getPosition() - 1);
            list.add(list.indexOf(product) + 1, searchProductTopAdsImageViewModel);
        }
    }

    private int getIndexOfTopAdsImageViewModelAtTop(List<Visitable> list) {
        int index = 0;

        while (shouldIncrementIndexForTopAdsImageViewModel(index, list))
            index++;

        return index;
    }

    private boolean shouldIncrementIndexForTopAdsImageViewModel(int index, List<Visitable> list) {
        if (index >= list.size()) return false;

        boolean isCPMOrProductItem = list.get(index) instanceof CpmViewModel || list.get(index) instanceof ProductItemViewModel;

        return !isCPMOrProductItem;
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

    private void processDefaultQuickFilter(SearchProductModel searchProductModel) {
        DataValue quickFilter = searchProductModel.getQuickFilterModel();

        if (quickFilter.getFilter().size() < QUICK_FILTER_MINIMUM_SIZE) {
            quickFilter = SearchFilterUtilsKt.createSearchProductDefaultQuickFilter();
            searchProductModel.setQuickFilterModel(quickFilter);
        }
    }

    private void processQuickFilter(DataValue quickFilterData) {
        if (this.dynamicFilterModel == null)
            getView().initFilterControllerForQuickFilter(quickFilterData.getFilter());

        List<SortFilterItem> sortFilterItems = new ArrayList<>();
        quickFilterOptionList = new ArrayList<>();

        for (Filter filter : quickFilterData.getFilter()) {
            List<Option> options = filter.getOptions();
            quickFilterOptionList.addAll(options);
            sortFilterItems.addAll(convertToSortFilterItem(filter.getTitle(), options));
        }

        if (sortFilterItems.size() > 0) {
            getView().hideQuickFilterShimmering();
            getView().setQuickFilter(sortFilterItems);
        }
    }

    private List<SortFilterItem> convertToSortFilterItem(String title, List<Option> options) {
        List<SortFilterItem> list = new ArrayList<>();
        for (Option option : options) {
            list.add(createSortFilterItem(title, option));
        }
        return list;
    }

    private SortFilterItem createSortFilterItem(String title, Option option) {
        SortFilterItem item = new SortFilterItem(title, () -> Unit.INSTANCE);

        setSortFilterItemListener(item, option);
        setSortFilterItemState(item, option);

        return item;
    }

    private void setSortFilterItemListener(SortFilterItem item, Option option) {
        item.setListener(() -> {
            getView().onQuickFilterSelected(option);
            return Unit.INSTANCE;
        });
    }

    private void setSortFilterItemState(SortFilterItem item, Option option) {
        if(getView().isQuickFilterSelected(option)){
            item.setType(ChipsUnify.TYPE_SELECTED);
            item.setTypeUpdated(false);
        }
    }

    @Override
    public List<Option> getQuickFilterOptionList() {
        return quickFilterOptionList;
    }

    private void getViewToSendTrackingSearchAttempt(ProductViewModel productViewModel) {
        if (getView() == null) return;

        isFirstTimeLoad = false;

        doInBackground(productViewModel, this::sendGeneralSearchTracking);
    }

    private <T> void doInBackground(T observable, final Action1<? super T> onNext) {
        Subscription subscription =
                Observable.just(observable)
                        .subscribeOn(schedulersProvider.computation())
                        .subscribe(onNext, Throwable::printStackTrace);

        compositeSubscription.add(subscription);
    }

    private void sendGeneralSearchTracking(ProductViewModel productViewModel) {
        String query = getView().getQueryKey();

        if (textIsEmpty(query)) return;

        JSONArray afProdIds = new JSONArray();
        HashMap<String, String> moengageTrackingCategory = new HashMap<>();
        Set<String> categoryIdMapping = new HashSet<>();
        Set<String> categoryNameMapping = new HashSet<>();
        ArrayList<String> prodIdArray = new ArrayList<>();

        if (productViewModel.getProductList().size() > 0) {
            for (int i = 0; i < productViewModel.getProductList().size(); i++) {
                if (i < 3) {
                    prodIdArray.add(productViewModel.getProductList().get(i).getProductID());
                    afProdIds.put(productViewModel.getProductList().get(i).getProductID());
                    moengageTrackingCategory.put(String.valueOf(productViewModel.getProductList().get(i).getCategoryID()), productViewModel.getProductList().get(i).getCategoryName());
                }

                categoryIdMapping.add(String.valueOf(productViewModel.getProductList().get(i).getCategoryID()));
                categoryNameMapping.add(productViewModel.getProductList().get(i).getCategoryName());
            }
        }

        getView().sendTrackingEventAppsFlyerViewListingSearch(afProdIds, query, prodIdArray);
        getView().sendTrackingEventMoEngageSearchAttempt(query, !productViewModel.getProductList().isEmpty(), moengageTrackingCategory);
        getView().sendTrackingGTMEventSearchAttempt(createGeneralSearchTrackingModel(productViewModel, query, categoryIdMapping, categoryNameMapping));
    }

    private GeneralSearchTrackingModel createGeneralSearchTrackingModel(ProductViewModel productViewModel, String query, Set<String> categoryIdMapping, Set<String> categoryNameMapping) {
        return new GeneralSearchTrackingModel(
                createGeneralSearchTrackingEventCategory(),
                createGeneralSearchTrackingEventLabel(productViewModel, query),
                getUserId(),
                Boolean.toString(!productViewModel.getProductList().isEmpty()),
                StringUtils.join(categoryIdMapping, ","),
                StringUtils.join(categoryNameMapping, ","),
                createGeneralSearchTrackingRelatedKeyword(productViewModel)
        );
    }

    private String createGeneralSearchTrackingEventCategory() {
        return SearchEventTracking.Category.EVENT_TOP_NAV + (textIsEmpty(pageTitle) ? "" : " - " + pageTitle);
    }

    private String createGeneralSearchTrackingEventLabel(ProductViewModel productViewModel, String query) {
        String source = getTopNavSource(productViewModel.getGlobalNavViewModel());
        return String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                query,
                getKeywordProcess(productViewModel),
                productViewModel.getResponseCode(),
                source,
                getNavSourceForGeneralSearchTracking(),
                getPageTitleForGeneralSearchTracking()
        );
    }

    private String getTopNavSource(GlobalNavViewModel globalNavViewModel) {
        if (globalNavViewModel == null) return SearchEventTracking.NONE;
        if (globalNavViewModel.getSource().isEmpty()) return SearchEventTracking.OTHER;
        return globalNavViewModel.getSource();
    }

    private String getKeywordProcess(ProductViewModel productViewModel) {
        String keywordProcess = productViewModel.getKeywordProcess();

        return textIsEmpty(keywordProcess) ? "0" : keywordProcess;
    }

    private String getNavSourceForGeneralSearchTracking() {
        return textIsEmpty(navSource) ? SearchEventTracking.NONE : navSource;
    }

    private String getPageTitleForGeneralSearchTracking() {
        return textIsEmpty(pageTitle) ? SearchEventTracking.NONE : pageTitle;
    }

    private String createGeneralSearchTrackingRelatedKeyword(ProductViewModel productViewModel) {
        String previousKeyword = getPreviousKeywordForGeneralSearchTracking();
        String alternativeKeyword = getAlternativeKeywordForGeneralSearchTracking(productViewModel);

        return previousKeyword + " - " + alternativeKeyword;
    }

    private String getPreviousKeywordForGeneralSearchTracking() {
        if (getView() == null) return SearchEventTracking.NONE;

        String previousKeyword = getView().getPreviousKeyword();
        if (previousKeyword.isEmpty()) return SearchEventTracking.NONE;

        return previousKeyword;
    }

    private String getAlternativeKeywordForGeneralSearchTracking(ProductViewModel productViewModel) {
        String alternativeKeyword = SearchEventTracking.NONE;

        if (isAlternativeKeywordFromRelated(productViewModel)) {
            alternativeKeyword = getAlternativeKeywordFromRelated(productViewModel.getRelatedViewModel());
        }
        else if (isAlternativeKeywordFromSuggestion(productViewModel)) {
            alternativeKeyword = productViewModel.getSuggestionModel().getSuggestion();
        }

        return alternativeKeyword;
    }

    private boolean isAlternativeKeywordFromRelated(ProductViewModel productViewModel) {
        String responseCode = productViewModel.getResponseCode();

        boolean isResponseCodeForRelatedKeyword = generalSearchTrackingRelatedKeywordResponseCodeList.contains(responseCode);
        boolean canConstructAlternativeKeywordFromRelated = canConstructAlternativeKeywordFromRelated(productViewModel);

        return isResponseCodeForRelatedKeyword && canConstructAlternativeKeywordFromRelated;
    }

    private boolean canConstructAlternativeKeywordFromRelated(ProductViewModel productViewModel) {
        RelatedViewModel relatedViewModel = productViewModel.getRelatedViewModel();

        if (relatedViewModel == null) return false;

        String relatedKeyword = relatedViewModel.getRelatedKeyword();
        List<BroadMatchViewModel> broadMatchViewModelList = relatedViewModel.getBroadMatchViewModelList();

        return !relatedKeyword.isEmpty() || !broadMatchViewModelList.isEmpty();
    }

    private String getAlternativeKeywordFromRelated(RelatedViewModel relatedViewModel) {
        String broadMatchKeywords = "";

        if (!relatedViewModel.getBroadMatchViewModelList().isEmpty()) {
            broadMatchKeywords = joinToString(relatedViewModel.getBroadMatchViewModelList(), ",", BroadMatchViewModel::getKeyword);
        }

        boolean shouldAppendComma =
                !relatedViewModel.getRelatedKeyword().isEmpty()
                && !broadMatchKeywords.isEmpty();

        return relatedViewModel.getRelatedKeyword()
                + (shouldAppendComma ? "," : "")
                + broadMatchKeywords;
    }

    private <T> String joinToString(Iterable<T> list, String separator, Function1<T, CharSequence> transform) {
        if (list == null) return "";

        return CollectionsKt.joinToString(list, separator, "", "", -1, "...", transform);
    }

    private boolean isAlternativeKeywordFromSuggestion(ProductViewModel productViewModel) {
        String responseCode = productViewModel.getResponseCode();

        boolean isResponseCodeForSuggestion = responseCode.equals("7");
        boolean suggestionIsNotEmpty =
                productViewModel.getSuggestionModel() != null
                && !productViewModel.getSuggestionModel().getSuggestion().isEmpty();

        return isResponseCodeForSuggestion && suggestionIsNotEmpty;
    }

    private void enrichWithRelatedSearchParam(RequestParams requestParams) {
        requestParams.putBoolean(SearchApiConst.RELATED, true);
    }

    protected void enrichWithAdditionalParams(RequestParams requestParams) {
        Map<String, String> additionalParams = getAdditionalParamsMap();
        requestParams.putAllString(additionalParams);
    }

    private Map<String, String> getAdditionalParamsMap() {
        return UrlParamUtils.getParamMap(additionalParams);
    }

    @Override
    public void handleWishlistAction(ProductCardOptionsModel productCardOptionsModel) {
        if (productCardOptionsModel == null) return;

        if (productCardOptionsModel.isRecommendation()) {
            handleWishlistRecommendationProduct(productCardOptionsModel);
        }
        else {
            handleWishlistNonRecommendationProduct(productCardOptionsModel);
        }
    }

    private void handleWishlistRecommendationProduct(ProductCardOptionsModel productCardOptionsModel) {
        WishlistResult wishlistResult = productCardOptionsModel.getWishlistResult();

        if (wishlistResult.isUserLoggedIn()) {
            handleWishlistRecommendationProductWithLoggedInUser(productCardOptionsModel);
        }
        else {
            handleWishlistRecommendationProductWithNotLoggedInUser(productCardOptionsModel);
        }
    }

    private void handleWishlistRecommendationProductWithLoggedInUser(ProductCardOptionsModel productCardOptionsModel) {
        WishlistResult wishlistResult = productCardOptionsModel.getWishlistResult();

        if (!wishlistResult.isSuccess()) {
            getView().showMessageFailedWishlistAction(wishlistResult.isAddWishlist());
        } else {
            getView().trackWishlistRecommendationProductLoginUser(!productCardOptionsModel.isWishlisted());
            getView().updateWishlistStatus(productCardOptionsModel.getProductId(), wishlistResult.isAddWishlist());
            getView().showMessageSuccessWishlistAction(wishlistResult.isAddWishlist());
        }
    }

    private void handleWishlistRecommendationProductWithNotLoggedInUser(ProductCardOptionsModel productCardOptionsModel) {
        getView().trackWishlistRecommendationProductNonLoginUser();
        getView().launchLoginActivity(productCardOptionsModel.getProductId());
    }

    private void handleWishlistNonRecommendationProduct(ProductCardOptionsModel productCardOptionsModel) {
        WishlistResult wishlistResult = productCardOptionsModel.getWishlistResult();

        if (wishlistResult.isUserLoggedIn()) {
            handleWishlistNonRecommendationProductWithLoggedInUser(productCardOptionsModel);
        }
        else {
            handleWishlistNonRecommendationProductWithNotLoggedInUser(productCardOptionsModel);
        }
    }

    private void handleWishlistNonRecommendationProductWithLoggedInUser(ProductCardOptionsModel productCardOptionsModel) {
        WishlistResult wishlistResult = productCardOptionsModel.getWishlistResult();

        if (!wishlistResult.isSuccess()) {
            getView().showMessageFailedWishlistAction(wishlistResult.isAddWishlist());
        } else {
            getView().trackWishlistProduct(createWishlistTrackingModel(productCardOptionsModel, productCardOptionsModel.getWishlistResult().isAddWishlist()));
            getView().updateWishlistStatus(productCardOptionsModel.getProductId(), wishlistResult.isAddWishlist());
            getView().showMessageSuccessWishlistAction(wishlistResult.isAddWishlist());
        }
    }

    private WishlistTrackingModel createWishlistTrackingModel(ProductCardOptionsModel productCardOptionsModel, boolean isAddWishlist) {
        if (productCardOptionsModel == null) return null;

        WishlistTrackingModel wishlistTrackingModel = new WishlistTrackingModel();

        wishlistTrackingModel.setProductId(productCardOptionsModel.getProductId());
        wishlistTrackingModel.setTopAds(productCardOptionsModel.isTopAds());
        wishlistTrackingModel.setKeyword(getView().getQueryKey());
        wishlistTrackingModel.setUserLoggedIn(productCardOptionsModel.getWishlistResult().isUserLoggedIn());
        wishlistTrackingModel.setAddWishlist(isAddWishlist);

        return wishlistTrackingModel;
    }

    private void handleWishlistNonRecommendationProductWithNotLoggedInUser(ProductCardOptionsModel productCardOptionsModel) {
        getView().trackWishlistProduct(createWishlistTrackingModel(productCardOptionsModel, !productCardOptionsModel.isWishlisted()));
        getView().launchLoginActivity(productCardOptionsModel.getProductId());
    }

    @Override
    public void onProductImpressed(ProductItemViewModel item) {
        if (getView() == null || item == null) return;

        if (item.isTopAds())
            getViewToTrackImpressedTopAdsProduct(item);
        else
            getViewToTrackImpressedOrganicProduct(item);
    }

    private void getViewToTrackImpressedTopAdsProduct(ProductItemViewModel item) {
        topAdsUrlHitter.hitImpressionUrl(
                getView().getClassName(),
                item.getTopadsImpressionUrl(),
                item.getProductID(),
                item.getProductName(),
                item.getImageUrl(),
                SearchConstant.TopAdsComponent.TOP_ADS
        );

        getView().sendTopAdsGTMTrackingProductImpression(item);
    }

    private void getViewToTrackImpressedOrganicProduct(ProductItemViewModel item) {
        if (item.isOrganicAds())
            topAdsUrlHitter.hitImpressionUrl(
                    getView().getClassName(),
                    item.getTopadsImpressionUrl(),
                    item.getProductID(),
                    item.getProductName(),
                    item.getImageUrl(),
                    SearchConstant.TopAdsComponent.ORGANIC_ADS
            );

        getView().sendProductImpressionTrackingEvent(item);
    }

    @Override
    public void onProductClick(ProductItemViewModel item, int adapterPosition) {
        if (getView() == null || item == null) return;

        if (item.isTopAds())
            getViewToTrackOnClickTopAdsProduct(item);
        else
            getViewToTrackOnClickOrganicProduct(item);

        getView().routeToProductDetail(item, adapterPosition);
    }

    private void getViewToTrackOnClickTopAdsProduct(ProductItemViewModel item) {
        topAdsUrlHitter.hitClickUrl(
                getView().getClassName(),
                item.getTopadsClickUrl(),
                item.getProductID(),
                item.getProductName(),
                item.getImageUrl(),
                SearchConstant.TopAdsComponent.TOP_ADS
        );

        getView().sendTopAdsGTMTrackingProductClick(item);
    }

    private void getViewToTrackOnClickOrganicProduct(ProductItemViewModel item) {
        if (item.isOrganicAds())
            topAdsUrlHitter.hitClickUrl(
                    getView().getClassName(),
                    item.getTopadsClickUrl(),
                    item.getProductID(),
                    item.getProductName(),
                    item.getImageUrl(),
                    SearchConstant.TopAdsComponent.ORGANIC_ADS
            );

        getView().sendGTMTrackingProductClick(item, getUserId());
    }

    @Override
    public void getProductCount(Map<String, String> mapParameter) {
        if (getView() == null) return;
        if (mapParameter == null) {
            getView().setProductCount("0");
            return;
        }

        RequestParams getProductCountRequestParams = createGetProductCountRequestParams(mapParameter);
        Subscriber<String> getProductCountSubscriber = createGetProductCountSubscriber();

        getProductCountUseCase.get().execute(getProductCountRequestParams, getProductCountSubscriber);
    }

    private RequestParams createGetProductCountRequestParams(Map<String, String> mapParameter) {
        RequestParams requestParams = createInitializeSearchParam(new HashMap<>(mapParameter));

        enrichWithRelatedSearchParam(requestParams);

        requestParams.putString(SearchApiConst.ROWS, "0");

        return requestParams;
    }

    private Subscriber<String> createGetProductCountSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) { setProductCount("0"); }

            @Override
            public void onNext(String productCountText) { setProductCount(productCountText); }
        };
    }

    private void setProductCount(String productCountText) {
        if (isViewAttached()) {
            getView().setProductCount(productCountText);
        }
    }

    public void onFreeOngkirOnBoardingShown() {
        if (getView() != null && !isSearchOnBoardingShown()) {
            getView().showOnBoarding(firstProductPosition, hasFullThreeDotsOptions);
            toggleSearchOnBoardingShown();
        }
    }

    private Boolean isSearchOnBoardingShown() {
        return searchOnBoardingLocalCache.getBoolean(THREE_DOTS_ONBOARDING_SHOWN) || !hasFullThreeDotsOptions;
    }

    private void toggleSearchOnBoardingShown() {
        if (hasFullThreeDotsOptions) searchOnBoardingLocalCache.putBoolean(THREE_DOTS_ONBOARDING_SHOWN, true);
        searchOnBoardingLocalCache.applyEditor();
    }

    @Override
    public void openFilterPage(Map<String, Object> searchParameter) {
        if (getView() == null || searchParameter == null) return;

        getView().sendTrackingOpenFilterPage();
        getView().openBottomSheetFilter(this.dynamicFilterModel);

        if (this.dynamicFilterModel == null) {
            getDynamicFilterUseCase.get().
                    execute(createRequestDynamicFilterParams(searchParameter), createGetDynamicFilterModelSubscriber());
        }
    }

    private RequestParams createRequestDynamicFilterParams(Map<String, Object> searchParameter) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(searchParameter);
        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_PRODUCT);
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);

        return requestParams;
    }

    private Subscriber<DynamicFilterModel> createGetDynamicFilterModelSubscriber() {
        return new Subscriber<DynamicFilterModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                handleGetDynamicFilterFailed();
            }

            @Override
            public void onNext(DynamicFilterModel dynamicFilterModel) {
                handleGetDynamicFilterSuccess(dynamicFilterModel);
            }
        };
    }

    private void handleGetDynamicFilterSuccess(DynamicFilterModel dynamicFilterModel) {
        if (dynamicFilterModel.isEmpty()) {
            handleGetDynamicFilterFailed();
        }
        else {
            this.dynamicFilterModel = dynamicFilterModel;
            getViewToSetDynamicFilterModel(dynamicFilterModel);
        }
    }

    private void getViewToSetDynamicFilterModel(DynamicFilterModel dynamicFilterModel) {
        if (getView() == null) return;

        getView().setDynamicFilter(dynamicFilterModel);
    }

    private void handleGetDynamicFilterFailed() {
        getViewToSetDynamicFilterModel(SearchFilterUtilsKt.createSearchProductDefaultFilter());
    }

    @Override
    public void onBroadMatchItemImpressed(@NotNull BroadMatchItemViewModel broadMatchItemViewModel) {
        if (getView() == null) return;

        if (broadMatchItemViewModel.isOrganicAds()) getViewToImpressBroadMatchAdsItem(broadMatchItemViewModel);

        getView().trackBroadMatchImpression(broadMatchItemViewModel);
    }

    private void getViewToImpressBroadMatchAdsItem(BroadMatchItemViewModel broadMatchItemViewModel) {
        topAdsUrlHitter.hitImpressionUrl(
                getView().getClassName(),
                broadMatchItemViewModel.getTopAdsViewUrl(),
                broadMatchItemViewModel.getId(),
                broadMatchItemViewModel.getName(),
                broadMatchItemViewModel.getImageUrl(),
                SearchConstant.TopAdsComponent.BROAD_MATCH_ADS
        );
    }

    @Override
    public void onBroadMatchItemClick(@NotNull BroadMatchItemViewModel broadMatchItemViewModel) {
        if (getView() == null) return;

        getView().trackEventClickBroadMatchItem(broadMatchItemViewModel);
        getView().redirectionStartActivity(broadMatchItemViewModel.getApplink(), broadMatchItemViewModel.getUrl());

        if (broadMatchItemViewModel.isOrganicAds())
            topAdsUrlHitter.hitClickUrl(
                    getView().getClassName(),
                    broadMatchItemViewModel.getTopAdsClickUrl(),
                    broadMatchItemViewModel.getId(),
                    broadMatchItemViewModel.getName(),
                    broadMatchItemViewModel.getImageUrl(),
                    SearchConstant.TopAdsComponent.BROAD_MATCH_ADS
            );
    }

    @Override
    public void onThreeDotsClick(ProductItemViewModel item, int adapterPosition) {
        if (getView() == null) return;

        this.threeDotsProductItem = item;

        getView().trackEventLongPress(item.getProductID());
        getView().showProductCardOptions(createProductCardOptionsModel(item));
    }

    private ProductCardOptionsModel createProductCardOptionsModel(ProductItemViewModel item) {
        ProductCardOptionsModel productCardOptionsModel = new ProductCardOptionsModel();

        productCardOptionsModel.setHasWishlist(item.isWishlistButtonEnabled());
        productCardOptionsModel.setHasSimilarSearch(true);
        productCardOptionsModel.setWishlisted(item.isWishlisted());
        productCardOptionsModel.setKeyword(getView().getQueryKey());
        productCardOptionsModel.setProductId(item.getProductID() == null ? "" : item.getProductID());
        productCardOptionsModel.setTopAds(item.isTopAds() || item.isOrganicAds());
        productCardOptionsModel.setTopAdsWishlistUrl(item.getTopadsWishlistUrl() == null ? "" : item.getTopadsWishlistUrl());
        productCardOptionsModel.setRecommendation(false);
        productCardOptionsModel.setScreenName(SearchEventTracking.Category.SEARCH_RESULT);
        productCardOptionsModel.setSeeSimilarProductEvent(SearchTracking.EVENT_CLICK_SEARCH_RESULT);

        productCardOptionsModel.setHasAddToCart(hasFullThreeDotsOptions);
        productCardOptionsModel.setAddToCartParams(new ProductCardOptionsModel.AddToCartParams(item.getMinOrder()));
        productCardOptionsModel.setCategoryName(item.getCategoryString());
        productCardOptionsModel.setProductName(item.getProductName());
        productCardOptionsModel.setFormattedPrice(item.getPrice());

        ProductCardOptionsModel.Shop shop = new ProductCardOptionsModel.Shop();
        shop.setShopId(item.getShopID());
        shop.setShopName(item.getShopName());
        shop.setShopUrl(item.getShopUrl());
        productCardOptionsModel.setShop(shop);

        productCardOptionsModel.setHasVisitShop(hasFullThreeDotsOptions);

        productCardOptionsModel.setHasShareProduct(hasFullThreeDotsOptions);
        productCardOptionsModel.setProductImageUrl(item.getImageUrl());
        productCardOptionsModel.setProductUrl(item.getProductUrl());

        return productCardOptionsModel;
    }

    @Override
    public void handleAddToCartAction(@NotNull ProductCardOptionsModel productCardOptionModel) {
        if (getView() == null) return;

        ProductCardOptionsModel.AddToCartResult addToCartResult = productCardOptionModel.getAddToCartResult();

        if (!addToCartResult.isUserLoggedIn())
            getView().launchLoginActivity("");
        else {
            handleAddToCartForLoginUser(productCardOptionModel.getAddToCartResult());
        }

        this.threeDotsProductItem = null;
    }

    private void handleAddToCartForLoginUser(ProductCardOptionsModel.AddToCartResult addToCartResult) {
        if (addToCartResult.isSuccess()) {
            handleAddToCartSuccess(addToCartResult);
        } else {
            handleAddToCartError(addToCartResult);
        }
    }

    private void handleAddToCartSuccess(ProductCardOptionsModel.AddToCartResult addToCartResult) {
        if (threeDotsProductItem == null || getView() == null) return;

        String cartId = addToCartResult.getCartId();
        Object addToCartDataLayer = threeDotsProductItem.getProductAsATCObjectDataLayer(cartId);

        if (threeDotsProductItem.isAds())
            topAdsUrlHitter.hitClickUrl(
                    getView().getClassName(),
                    threeDotsProductItem.getTopadsClickUrl(),
                    threeDotsProductItem.getProductID(),
                    threeDotsProductItem.getProductName(),
                    threeDotsProductItem.getImageUrl(),
                    SearchConstant.TopAdsComponent.TOP_ADS
            );

        getView().trackSuccessAddToCartEvent(threeDotsProductItem.isAds(), addToCartDataLayer);
        getView().showAddToCartSuccessMessage();
    }

    private void handleAddToCartError(ProductCardOptionsModel.AddToCartResult addToCartResult) {
        if (getView() == null) return;

        getView().showAddToCartFailedMessage(addToCartResult.getErrorMessage());
    }

    @Override
    public void handleVisitShopAction() {
        if (getView() == null || threeDotsProductItem == null) return;

        if (threeDotsProductItem.isTopAds()) {
            topAdsUrlHitter.hitClickUrl(
                    getView().getClassName(),
                    threeDotsProductItem.getTopadsClickShopUrl(),
                    threeDotsProductItem.getProductID(),
                    threeDotsProductItem.getProductName(),
                    threeDotsProductItem.getImageUrl(),
                    SearchConstant.TopAdsComponent.TOP_ADS
            );
        }

        getView().routeToShopPage(threeDotsProductItem.getShopID());
        getView().trackEventGoToShopPage(threeDotsProductItem.getProductAsShopPageObjectDataLayer());

        this.threeDotsProductItem = null;
    }

    @Override
    public void handleChangeView(int position, SearchConstant.ViewType currentLayoutType) {
        if (getView() == null) return;

        switch(currentLayoutType) {
            case LIST:
                getView().switchSearchNavigationLayoutTypeToBigGridView(position);
                getView().trackEventSearchResultChangeView(VIEW_TYPE_NAME_BIG_GRID);
                break;
            case SMALL_GRID:
                getView().switchSearchNavigationLayoutTypeToListView(position);
                getView().trackEventSearchResultChangeView(VIEW_TYPE_NAME_LIST);
                break;
            case BIG_GRID:
                getView().switchSearchNavigationLayoutTypeToSmallGridView(position);
                getView().trackEventSearchResultChangeView(VIEW_TYPE_NAME_SMALL_GRID);
                break;
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        if (getDynamicFilterUseCase != null) getDynamicFilterUseCase.get().unsubscribe();
        if (searchProductFirstPageUseCase != null) searchProductFirstPageUseCase.unsubscribe();
        if (searchProductLoadMoreUseCase != null) searchProductLoadMoreUseCase.unsubscribe();
        if (recommendationUseCase != null) recommendationUseCase.unsubscribe();
        if (getProductCountUseCase != null) getProductCountUseCase.get().unsubscribe();
        if (getLocalSearchRecommendationUseCase != null) getLocalSearchRecommendationUseCase.get().unsubscribe();
        if (compositeSubscription != null && compositeSubscription.isUnsubscribed()) unsubscribeCompositeSubscription();
    }

    private void unsubscribeCompositeSubscription() {
        compositeSubscription.unsubscribe();
        compositeSubscription = null;
    }
}
