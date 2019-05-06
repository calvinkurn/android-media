package com.tokopedia.search.result.presentation.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.domain.gql.SearchProductGqlResponse;
import com.tokopedia.discovery.newdiscovery.domain.usecase.ProductWishlistUrlUseCase;
import com.tokopedia.discovery.newdiscovery.helper.GqlSearchHelper;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.ProductViewModelHelper;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.search.result.domain.usecase.GetProductUseCase;
import com.tokopedia.search.result.presentation.ProductListSectionContract;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscriber;

class ProductListPresenter
//        extends SearchSectionPresenter<ProductListSectionContract.View>
//        implements ProductListSectionContract.Presenter
{

//    private static final String TAG = ProductListPresenter.class.getSimpleName();
//
//    private GetProductUseCase getProductUseCase;
//    private AddWishListUseCase addWishlistActionUseCase;
//    private RemoveWishListUseCase removeWishlistActionUseCase;
//    private ProductWishlistUrlUseCase productWishlistUrlUseCase;
//    private UserSessionInterface userSession;
//
//    private WishListActionListener wishlistActionListener;
////    GraphqlUseCase graphqlUseCase;
//    private boolean enableGlobalNavWidget;
//
//    private Subscriber<GraphqlResponse> loadDataSubscriber;
//    private Subscriber<GraphqlResponse> loadMoreDataSubscriber;
//
//    public ProductListPresenter() {
////        SearchComponent component = DaggerSearchComponent.builder()
////                .appComponent(getComponent(context))
////                .build();
////        component.inject(this);
////        graphqlUseCase = new GraphqlUseCase();
//        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
//        enableGlobalNavWidget = remoteConfig.getBoolean(
//                RemoteConfigKey.ENABLE_GLOBAL_NAV_WIDGET,false);
//    }
//
//    @Override
//    public void attachView(ProductListSectionContract.View viewListener,
//                           WishListActionListener wishlistActionListener) {
//        super.attachView(viewListener);
//        this.wishlistActionListener = wishlistActionListener;
//    }
//
//    /**
//     * This method intentionally left blank for ProductListPresenter
//     * Retrieving Product Filter data is already combined together with loadData using GraphQL
//     * This method will be removed When other implementations of SearchSectionPresenter already use GraphQL
//    * */
//    @Override
//    @Deprecated
//    protected void getFilterFromNetwork(RequestParams requestParams) { }
//
//    @Override
//    public void handleWishlistButtonClicked(final ProductItemViewModel productItem) {
//        if (getView().isUserHasLogin()) {
//            getView().disableWishlistButton(productItem.getProductID());
//            if (productItem.isWishlisted()) {
//                removeWishlist(productItem.getProductID(), getView().getUserId());
//            } else if(productItem.isTopAds()){
//                RequestParams params = RequestParams.create();
//                params.putString(ProductWishlistUrlUseCase.PRODUCT_WISHLIST_URL, productItem.getTopadsWishlistUrl());
//                productWishlistUrlUseCase.execute(params, getWishlistSubscriber(productItem));
//            } else {
//                addWishlist(productItem.getProductID(), getView().getUserId());
//            }
//        } else {
//            launchLoginActivity(productItem.getProductID());
//        }
//    }
//
//    @NonNull
//    private Subscriber<Boolean> getWishlistSubscriber(final ProductItemViewModel productItem) {
//        return new Subscriber<Boolean>() {
//            @Override
//            public void onCompleted() {
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                if (isViewAttached()) {
//                    getView().onErrorAddWishList(e.getMessage(), productItem.getProductID());
//                    getView().notifyAdapter();
//                }
//            }
//
//            @Override
//            public void onNext(Boolean result) {
//                if (isViewAttached()){
//                    if (result) {
//                        getView().onSuccessAddWishlist(productItem.getProductID());
//                    } else {
//                        getView().notifyAdapter();
//                    }
//                }
//            }
//        };
//    }
//
//    private void launchLoginActivity(String productId) {
//        Bundle extras = new Bundle();
//        extras.putString("product_id", productId);
//        getView().launchLoginActivity(extras);
//    }
//
//    private void addWishlist(String productId, String userId) {
//        Log.d(this.toString(), "Add Wishlist " + productId);
//        addWishlistActionUseCase.createObservable(productId, userId,
//                wishlistActionListener);
//    }
//
//    private void removeWishlist(String productId, String userId) {
//        Log.d(this.toString(), "Remove Wishlist " + productId);
//        removeWishlistActionUseCase.createObservable(productId, userId, wishlistActionListener);
//    }
//
//    @Override
//    protected RequestParams getDynamicFilterParam() { return RequestParams.create(); }
//
//    @Override
//    public void loadMoreData(final SearchParameter searchParameter, HashMap<String, String> additionalParams) {
//        RequestParams requestParams = getProductUseCase.createInitializeSearchParam(searchParameter.getSearchParameterHashMap(), false);
//        enrichWithFilterAndSortParams(requestParams);
//        enrichWithRelatedSearchParam(requestParams, true);
//        enrichWithAdditionalParams(requestParams, additionalParams);
//        removeDefaultCategoryParam(requestParams);
//
//        unsubscribeLoadMoreDataSubscriberIfStillSubscribe();
//
//        loadMoreDataSubscriber = getLoadMoreDataSubscriber(searchParameter);
//
//        GqlSearchHelper.requestProductLoadMore(context, requestParams, graphqlUseCase, loadMoreDataSubscriber);
//    }
//
//    private void unsubscribeLoadMoreDataSubscriberIfStillSubscribe() {
//        if(loadMoreDataSubscriber != null && !loadMoreDataSubscriber.isUnsubscribed()) {
//            loadMoreDataSubscriber.unsubscribe();
//        }
//    }
//
//    private Subscriber<GraphqlResponse> getLoadMoreDataSubscriber(final SearchParameter searchParameter) {
//        return new Subscriber<GraphqlResponse>() {
//            @Override
//            public void onStart() {
//                loadMoreDataSubscriberOnStartIfViewAttached();
//            }
//
//            @Override
//            public void onNext(GraphqlResponse objects) {
//                loadMoreDataSubscriberOnNextIfViewAttached(objects);
//            }
//
//            @Override
//            public void onCompleted() {
//                loadMoreDataSubscriberOnCompleteIfViewAttached();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                loadMoreDataSubscriberOnErrorIfViewAttached(searchParameter);
//            }
//        };
//    }
//
//    private void loadMoreDataSubscriberOnStartIfViewAttached() {
//        if (isViewAttached()) {
//            getView().incrementStart();
//        }
//    }
//
//    private void loadMoreDataSubscriberOnNextIfViewAttached(GraphqlResponse objects) {
//        SearchProductGqlResponse gqlResponse = objects.getData(SearchProductGqlResponse.class);
//
//        if(gqlResponse == null) return;
//
//        ProductViewModel productViewModel
//                = ProductViewModelHelper.convertToProductViewModel(gqlResponse);
//
//        if (isViewAttached()) {
//            if (productViewModel.getProductList().isEmpty()) {
//                getViewToRemoveLoading();
//            } else {
//                getViewToShowMoreData(productViewModel);
//            }
//
//            getView().storeTotalData(productViewModel.getTotalData());
//        }
//    }
//
//    private void getViewToRemoveLoading() {
//        getView().removeLoading();
//    }
//
//    private void getViewToShowMoreData(ProductViewModel productViewModel) {
//        List<Visitable> list = new ArrayList<Visitable>(ProductViewModelHelper.convertToListOfVisitable(productViewModel));
//        getView().removeLoading();
//        getView().addProductList(list);
//        getView().addLoading();
//    }
//
//    private void loadMoreDataSubscriberOnCompleteIfViewAttached() {
//        if (isViewAttached()) {
//            getView().hideRefreshLayout();
//        }
//    }
//
//    private void loadMoreDataSubscriberOnErrorIfViewAttached(SearchParameter searchParameter) {
//        if (isViewAttached()) {
//            getView().removeLoading();
//            getView().hideRefreshLayout();
//            getView().showNetworkError(searchParameter.getInteger(SearchApiConst.START));
//        }
//    }
//
//    @Override
//    public void loadData(final SearchParameter searchParameter, boolean isForceSearch, HashMap<String, String> additionalParams, boolean isFirstTimeLoad) {
//        RequestParams requestParams = createInitializeSearchParam(searchParameter, false, true);
//        enrichWithFilterAndSortParams(requestParams);
//        enrichWithForceSearchParam(requestParams, isForceSearch);
//        enrichWithRelatedSearchParam(requestParams, true);
//        enrichWithAdditionalParams(requestParams, additionalParams);
//        removeDefaultCategoryParam(requestParams);
//
//        unsubscribeLoadDataSubscriberIfStillSubscribe();
//
//        loadDataSubscriber = getLoadDataSubscriber(isFirstTimeLoad);
//
//        GqlSearchHelper.requestProductFirstPage(context, requestParams, graphqlUseCase, loadDataSubscriber);
//    }
//
//    private void unsubscribeLoadDataSubscriberIfStillSubscribe() {
//        if(loadDataSubscriber != null && !loadDataSubscriber.isUnsubscribed()) {
//            loadDataSubscriber.unsubscribe();
//        }
//    }
//
//    private Subscriber<GraphqlResponse> getLoadDataSubscriber(final boolean isFirstTimeLoad) {
//        return new Subscriber<GraphqlResponse>() {
//            @Override
//            public void onStart() {
//                loadDataSubscriberOnStartIfViewAttached();
//            }
//
//            @Override
//            public void onCompleted() {
//                loadDataSubscriberOnCompleteIfViewAttached();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                loadDataSubscriberOnErrorIfViewAttached();
//            }
//
//            @Override
//            public void onNext(GraphqlResponse objects) {
//                loadDataSubscriberOnNextIfViewAttached(objects, isFirstTimeLoad);
//            }
//        };
//    }
//
//    private void loadDataSubscriberOnStartIfViewAttached() {
//        if (isViewAttached()) {
//            getView().showRefreshLayout();
//            getView().incrementStart();
//        }
//    }
//
//    private void loadDataSubscriberOnCompleteIfViewAttached() {
//        if (isViewAttached()) {
//            getView().hideRefreshLayout();
//        }
//    }
//
//    private void loadDataSubscriberOnErrorIfViewAttached() {
//        if (isViewAttached()) {
//            getView().removeLoading();
//            getView().showNetworkError(0);
//            getView().hideRefreshLayout();
//        }
//    }
//
//    private void loadDataSubscriberOnNextIfViewAttached(GraphqlResponse objects, boolean isFirstTimeLoad) {
//        if (isViewAttached()) {
//            SearchProductGqlResponse gqlResponse = objects.getData(SearchProductGqlResponse.class);
//            ProductViewModel productViewModel
//                    = ProductViewModelHelper.convertToProductViewModelFirstPageGql(gqlResponse);
//
//            if (productViewModel.getProductList().isEmpty()) {
//                getViewToShowEmptySearch();
//            } else {
//                getViewToShowProductList(productViewModel);
//            }
//
//            getView().storeTotalData(productViewModel.getTotalData());
//            getView().renderDynamicFilter(productViewModel.getDynamicFilterModel());
//
//            if(isFirstTimeLoad) {
//                getViewToSendTrackingOnFirstTimeLoad(productViewModel);
//            }
//        }
//    }
//
//    private void getViewToShowEmptySearch() {
//        getView().removeLoading();
//        getView().setEmptyProduct();
//        getView().setTotalSearchResultCount("0");
//    }
//
//    private void getViewToShowProductList(ProductViewModel productViewModel) {
//
//        List<Visitable> list = new ArrayList<>();
//
//        HeaderViewModel headerViewModel = new HeaderViewModel();
//        headerViewModel.setSuggestionModel(productViewModel.getSuggestionModel());
//        if (productViewModel.getGuidedSearchViewModel() != null) {
//            headerViewModel.setGuidedSearch(productViewModel.getGuidedSearchViewModel());
//        }
//        if (productViewModel.getQuickFilterModel() != null
//                && productViewModel.getQuickFilterModel().getFilter() != null) {
//            headerViewModel.setQuickFilterList(getView().getQuickFilterOptions(productViewModel.getQuickFilterModel()));
//        }
//        boolean isGlobalNavWidgetAvailable
//                = productViewModel.getGlobalNavViewModel() != null && enableGlobalNavWidget;
//        if (isGlobalNavWidgetAvailable) {
//            headerViewModel.setGlobalNavViewModel(productViewModel.getGlobalNavViewModel());
//            getView().sendImpressionGlobalNav(productViewModel.getGlobalNavViewModel());
//        }
//        if (productViewModel.getCpmModel() != null && !isGlobalNavWidgetAvailable) {
//            headerViewModel.setCpmModel(productViewModel.getCpmModel());
//        }
//        list.add(headerViewModel);
//        list.addAll(ProductViewModelHelper.convertToListOfVisitable(productViewModel));
//        if (productViewModel.getRelatedSearchModel() != null) {
//            list.add(productViewModel.getRelatedSearchModel());
//        }
//
//        getView().setAdditionalParams(productViewModel.getAdditionalParams());
//        getView().removeLoading();
//        getView().setProductList(list);
//        getView().initQuickFilter(productViewModel.getQuickFilterModel().getFilter());
//        getView().addLoading();
//        getView().setTotalSearchResultCount(productViewModel.getSuggestionModel().getFormattedResultCount());
//        getView().stopTracePerformanceMonitoring();
//    }
//
//    private void getViewToSendTrackingOnFirstTimeLoad(ProductViewModel productViewModel) {
//        JSONArray afProdIds = new JSONArray();
//        HashMap<String, String> category = new HashMap<String, String>();
//        ArrayList<String> prodIdArray = new ArrayList<>();
//
//        if (productViewModel.getProductList().size() > 0) {
//            for (int i = 0; i < productViewModel.getProductList().size(); i++) {
//                if (i < 3) {
//                    prodIdArray.add(productViewModel.getProductList().get(i).getProductID());
//                    afProdIds.put(productViewModel.getProductList().get(i).getProductID());
//                } else {
//                    break;
//                }
//                category.put(String.valueOf(productViewModel.getProductList().get(i).getCategoryID()), productViewModel.getProductList().get(i).getCategoryName());
//
//            }
//        }
//
//        getView().sendTrackingEventAppsFlyerViewListingSearch(afProdIds, productViewModel.getQuery(), prodIdArray);
//        getView().sendTrackingEventMoEngageSearchAttempt(productViewModel.getQuery(), !productViewModel.getProductList().isEmpty(), category);
//        getView().setFirstTimeLoad(false);
//    }
//
//    @Override
//    public void setIsUsingFilterV4(boolean isUsingFilterV4) {
//        this.isUsingFilterV4 = isUsingFilterV4;
//    }
//
//    private RequestParams getQuickFilterRequestParams() {
//        RequestParams requestParams = RequestParams.create();
//        requestParams.putAll(generateParamsNetwork(requestParams));
//        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_QUICK_FILTER);
//        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
//        requestParams.putString(SearchApiConst.Q, getView().getQueryKey());
//        if (!TextUtils.isEmpty(getView().getSearchParameter().get(SearchApiConst.SC))) {
//            requestParams.putString(SearchApiConst.SC, getView().getSearchParameter().get(SearchApiConst.SC));
//        } else {
//            requestParams.putString(SearchApiConst.SC, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SC);
//        }
//        return requestParams;
//    }
//
//    private void enrichWithForceSearchParam(RequestParams requestParams, boolean isForceSearch) {
//        requestParams.putBoolean(SearchApiConst.REFINED, isForceSearch);
//    }
//
//    private void enrichWithRelatedSearchParam(RequestParams requestParams, boolean relatedSearchEnabled) {
//        requestParams.putBoolean(SearchApiConst.RELATED, relatedSearchEnabled);
//    }
//
//    @Override
//    public void detachView() {
//        super.detachView();
//        addWishlistActionUseCase.unsubscribe();
//        removeWishlistActionUseCase.unsubscribe();
//    }
}