package com.tokopedia.tkpd.home.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.abstraction.common.network.exception.ResponseDataNullException;
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.toolargetool.TooLargeTool;
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams;
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel;
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.CacheDuration;
import com.tokopedia.core.network.apiservices.mojito.MojitoAuthService;
import com.tokopedia.core.network.apiservices.mojito.MojitoService;
import com.tokopedia.kotlin.util.ContainNullException;
import com.tokopedia.kotlin.util.NullCheckerKt;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistProductViewModel;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistRecomTitleViewModel;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistRecomendationViewModel;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistTopAdsViewModel;
import com.tokopedia.tkpd.home.wishlist.domain.model.GqlWishListDataResponse;
import com.tokopedia.core.network.entity.wishlist.Pagination;
import com.tokopedia.core.network.entity.wishlist.Wishlist;
import com.tokopedia.core.network.entity.wishlist.WishlistPaging;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.graphql.data.ObservableFactory;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.interactor.CacheHomeInteractor;
import com.tokopedia.tkpd.home.interactor.CacheHomeInteractorImpl;
import com.tokopedia.tkpd.home.service.FavoritePart1Service;
import com.tokopedia.tkpd.home.wishlist.mapper.WishlistProductMapper;
import com.tokopedia.track.TrackApp;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;
import com.tokopedia.transactiondata.exception.ResponseCartApiErrorException;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import org.parceler.Parcels;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by m.normansyah on 01/12/2015.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class WishListImpl implements WishList {
    private static final String TAG = WishListImpl.class.getSimpleName();
    public static final String PAGE_NO = "page";
    public static final String ITEM_COUNT = "count";
    public static final String QUERY = "query";
    public static final int TOPADS_INDEX = 4;
    public static final String TOPADS_ITEM = "5";
    public static final String TOPADS_SRC = "wishlist";
    public static final String X_SOURCE_RECOM_WIDGET = "recom_widget";
    public static final String EMPTY_WISHLIST = "empty_wishlist";

    WishListView wishListView;

    List<Visitable> data = new ArrayList<>();

    WishlistPaging mPaging;

    FavoritePart1Service wishlist;

    CompositeSubscription compositeSubscription;

    CacheHomeInteractor cache;

    MojitoService mojitoService;

    MojitoAuthService mojitoAuthService;

    List<Wishlist> dataWishlist = new ArrayList<>();
    RequestParams params = RequestParams.create();

    RemoveWishListUseCase removeWishListUseCase;
    UserSession userSession;
    Context context;
    private String query = "";

    private AddToCartUseCase addToCartUseCase;

    private final GetRecommendationUseCase getRecommendationUseCase;

    public WishListImpl(Context context, WishListView wishListView) {
        this.wishListView = wishListView;
        mPaging = new WishlistPaging();
        wishlist = new FavoritePart1Service();
        cache = new CacheHomeInteractorImpl();
        mojitoService = new MojitoService();
        compositeSubscription = new CompositeSubscription();
        mojitoAuthService = new MojitoAuthService();
        removeWishListUseCase = new RemoveWishListUseCase(context);
        this.context = context;
        userSession = new UserSession(context);
        addToCartUseCase = new AddToCartUseCase(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.mutation_add_to_cart),
                new GraphqlUseCase()
        );
        getRecommendationUseCase = new GetRecommendationUseCase(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.query_recommendation_widget), new GraphqlUseCase(), new UserSession(context));
    }

    @Override
    public void initDataInstance(Context context) {
        if (!isAfterRotation()) {
            data = new ArrayList<>();
            dataWishlist = new ArrayList<>();
        }
        wishListView.initGridLayoutManager();
        wishListView.initItemDecoration();
        wishListView.initAdapterWithData(data);

    }

    @Override
    public void loadMoreRecomendation(int page) {
        getRecommendationUseCase.execute(getRecommendationUseCase.getRecomParams(
                page,
                X_SOURCE_RECOM_WIDGET,
                EMPTY_WISHLIST,
                new ArrayList<>()),
                new Subscriber<List<? extends RecommendationWidget>>() {
                    @Override
                    public void onStart() {
                        wishListView.displayLoadMore(true);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        wishListView.displayLoadMore(false);
                    }

                    @Override
                    public void onNext(List<? extends RecommendationWidget> recommendationWidgets) {
                        wishListView.displayLoadMore(false);
                        RecommendationWidget recommendationWidget = recommendationWidgets.get(0);
                        wishListView.onRenderRecomInbox(getRecommendationVisitables(recommendationWidget));
                        wishListView.loadDataChange();
                    }
                });
    }

    public void getFirstRecomData() {
        getRecommendationUseCase.execute(getRecommendationUseCase.getRecomParams(0,
                X_SOURCE_RECOM_WIDGET,
                EMPTY_WISHLIST,
                new ArrayList<>()),
                new Subscriber<List<? extends RecommendationWidget>>() {
                    @Override
                    public void onStart() {
                        wishListView.displayLoadMore(true);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        wishListView.displayLoadMore(false);
                    }

                    @Override
                    public void onNext(List<? extends RecommendationWidget> recommendationWidgets) {
                        wishListView.displayLoadMore(false);
                        List<Visitable> visitables = new ArrayList<>();
                        RecommendationWidget recommendationWidget = recommendationWidgets.get(0);
                        visitables.add(new WishlistRecomTitleViewModel(recommendationWidget.getTitle()));
                        visitables.addAll(getRecommendationVisitables(recommendationWidget));
                        wishListView.onRenderRecomInbox(visitables);
                        wishListView.loadDataChange();
                    }
                });
    }

    @NonNull
    private List<Visitable> getRecommendationVisitables(RecommendationWidget recommendationWidget) {
        List<Visitable> recomendationList = new ArrayList<>();
        for (RecommendationItem item : recommendationWidget.getRecommendationItemList()) {
            recomendationList.add(new WishlistRecomendationViewModel(item));
        }
        return recomendationList;
    }

    @Override
    public void fetchSavedsInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(WISHLIST_MODEL)) {
                data = Parcels.unwrap(savedInstanceState.getParcelable(WISHLIST_MODEL));
                dataWishlist = Parcels.unwrap(savedInstanceState.getParcelable(WISHLIST_ENTITY));
                if (mPaging != null) {
                    Pagination pagination = savedInstanceState.getParcelable(PAGINATION_MODEL);
                    mPaging.setPagination(pagination);
                }
            }
        }
    }

    @Override
    public void initAnalyticsHandler(Context context) {
    }

    @Override
    public void setData() {
        wishListView.displayPull(false);
        wishListView.loadDataChange();
        wishListView.displayContentList(true);
        wishListView.displayLoading(false);
    }

    @Override
    public void refreshData(final Context context) {
        wishListView.displayLoadMore(false);
        wishListView.loadDataChange();
        mPaging.resetPage();
        if (context != null)
            fetchDataFromInternet(context);
    }

    @Override
    public void loadMore(Context context) {
        wishListView.setPullEnabled(false);
        if (mPaging.getPagination().getNextUrl().contains("search") && mPaging.CheckNextPage()) {
            searchWishlistLoadMore();
        } else if (mPaging.CheckNextPage()) {
            mPaging.nextPage();
            fetchDataFromInternet(context);
        }
    }

    @Override
    public void searchWishlistLoadMore() {
        wishListView.setPullEnabled(false);
        mPaging.nextPage();
        params.putInt(PAGE_NO, mPaging.getPage());
        getWishListDataSearch(context, new LoadMoreSearchWishlistSubscriber());
    }

    @Override
    public void saveDataBeforeRotate(Bundle saved) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(WISHLIST_MODEL, Parcels.wrap(data));
        bundle.putParcelable(WISHLIST_ENTITY, Parcels.wrap(dataWishlist));
        if (TooLargeTool.isPotentialCrash(bundle)) {
            return;
        }
        saved.putAll(bundle);
        if (mPaging != null) {
            saved.putParcelable(PAGINATION_MODEL, mPaging.getPagination());
        }
    }

    @Override
    public boolean isAfterRotation() {
        return data != null && data.size() > 0;
    }

    @Override
    public List<Visitable> getData() {
        return data;
    }

    @Override
    public void fetchDataFromInternet(final Context context) {

        Subscriber<GqlWishListDataResponse> subscriber = new Subscriber<GqlWishListDataResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onStart() {
                wishListView.displayLoadMore(true);
            }

            @Override
            public void onError(Throwable e) {
                if (mPaging.getPage() == 1 && wishListView.isPullToRefresh()) {
                    wishListView.displayPull(false);
                }
                wishListView.displayErrorNetwork(false);
            }

            @Override
            public void onNext(GqlWishListDataResponse response) {
                wishListView.displayLoadMore(false);
                if (response != null) {
                    if (response.getGqlWishList().getWishlistDataList().size() == 0) {
                        wishListView.setEmptyState();
                    } else {
                        setData(response);
                    }
                } else {
                    setData();
                }
            }
        };

        getWishListData(context, subscriber);
    }

    private void getWishListDataSearch(Context context, Subscriber subscriber) {

        Map<String, Object> variables = new HashMap<>();

        variables.put(QUERY, params.getString(QUERY, ""));
        variables.put(PAGE_NO, params.getInt(PAGE_NO, 0));
        variables.put(ITEM_COUNT, 10);

        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_search_wishlist),
                GqlWishListDataResponse.class,
                variables, false);

        List<GraphqlRequest> graphqlRequestList = new ArrayList<>();
        graphqlRequestList.add(graphqlRequest);

        GraphqlCacheStrategy graphqlCacheStrategy =
                new GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build();

        Observable observable = Observable.zip(ObservableFactory.create(graphqlRequestList,
                graphqlCacheStrategy), getRecommendationUseCase.getExecuteObservable(getRecommendationUseCase.getRecomParams(params.getInt(PAGE_NO, 0),
                X_SOURCE_RECOM_WIDGET, TOPADS_SRC, new ArrayList<>())), new WishlistProductMapper());

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));

    }

    @NonNull
    private Map<String, String> getTopAdsParameterMap() {
        Map<String, String> adsParam = new HashMap<>();
        adsParam.put(TopAdsParams.KEY_PAGE, String.valueOf(mPaging.getPage()));
        adsParam.put(TopAdsParams.KEY_ITEM, TOPADS_ITEM);
        adsParam.put(TopAdsParams.KEY_DEVICE, TopAdsParams.DEFAULT_KEY_DEVICE);
        adsParam.put(TopAdsParams.KEY_EP, TopAdsParams.DEFAULT_KEY_EP);
        adsParam.put(TopAdsParams.KEY_USER_ID, userSession.getUserId());
        adsParam.put(TopAdsParams.KEY_SRC, TOPADS_SRC);
        return adsParam;
    }


    private void getWishListData(Context context, Subscriber subscriber) {

        Map<String, Object> variables = new HashMap<>();

        variables.put(PAGE_NO, mPaging.getPage());
        variables.put(ITEM_COUNT, 10);

        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_get_wishlist),
                GqlWishListDataResponse.class,
                variables, false);

        List<GraphqlRequest> graphqlRequestList = new ArrayList<>();
        graphqlRequestList.add(graphqlRequest);

        GraphqlCacheStrategy graphqlCacheStrategy =
                new GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build();

        Observable observable = Observable.zip(ObservableFactory.create(graphqlRequestList,
                graphqlCacheStrategy), getRecommendationUseCase.getExecuteObservable(getRecommendationUseCase.getRecomParams(mPaging.getPage(),
                X_SOURCE_RECOM_WIDGET, TOPADS_SRC, new ArrayList<>())), new WishlistProductMapper());

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));

    }

    @Override
    public void setData(GqlWishListDataResponse gqlWishListDataResponse) {
        GqlWishListDataResponse.GqlWishList wishlistData = gqlWishListDataResponse.getGqlWishList();
        if (mPaging.getPage() == 1) {
            data.clear();
            if (wishlistData.getWishlistDataList().size() == 0)
                wishListView.setSearchNotFound(query);
        }
        wishListView.displayPull(false);

        wishListView.sendWishlistImpressionAnalysis(wishlistData, dataWishlist.size());

        dataWishlist.addAll(wishlistData.getWishlistDataList());
        data.addAll(convertToProductItemList(wishlistData.getWishlistDataList(),
                gqlWishListDataResponse.getTopAdsModel(), query));
        mPaging.setPagination(wishlistData.getPagination());
        wishListView.setPullEnabled(true);

        wishListView.loadDataChange();
        wishListView.displayContentList(true);
        wishListView.displayLoading(false);
        wishListView.clearSearchView();
    }

    @Override
    public void deleteWishlist(final Context context, final String productId, final int position) {
        wishListView.showProgressDialog();
        removeWishListUseCase.createObservable(productId, SessionHandler.getLoginID(context), new WishListActionListener() {
            @Override
            public void onErrorAddWishList(String errorMessage, String productId) {

            }

            @Override
            public void onSuccessAddWishlist(String productId) {

            }

            @Override
            public void onErrorRemoveWishlist(String errorMessage, String productId) {
                wishListView.dismissProgressDialog();
                wishListView.displayErrorNetwork(true);
            }

            @Override
            public void onSuccessRemoveWishlist(String productId) {
                sendMoEngageTracker(productId);
                onFinishedDeleteWishlist(position);
            }

        });

    }

    @Override
    public void addToCart(String productId) {
        for (int i = 0; i < dataWishlist.size(); i++) {
            if (productId.equals(dataWishlist.get(i).getId())) {
                Wishlist dataDetail = dataWishlist.get(i);
                routeToNewCheckout(dataDetail);
                return;
            }
        }
    }

    private void routeToNewCheckout(Wishlist dataDetail) {
        wishListView.showProgressDialog();
        AddToCartRequestParams addToCartRequestParams = new AddToCartRequestParams();
        addToCartRequestParams.setProductId(Integer.parseInt(dataDetail.getId()));
        addToCartRequestParams.setShopId(Integer.parseInt(dataDetail.getShop().getId()));
        addToCartRequestParams.setQuantity(dataDetail.getMinimumOrder());
        addToCartRequestParams.setNotes("");

        com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();
        requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams);
        addToCartUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addToCartSubscriber(dataDetail));
    }

    @Override
    public boolean isLoadedFirstPage() {
        return mPaging.getPage() >= 1;
    }

    @Override
    public void searchWishlist(String query) {
        this.query = query;
        mPaging.resetPage();
        params.putString(QUERY, query);
        params.putInt(PAGE_NO, mPaging.getPage());
        getWishListDataSearch(context, new FirstSearchWishlistSubscriber());
    }

    @Override
    public void subscribe() {
        RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
    }

    @Override
    public void unSubscribe() {
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
        cache.unSubscribeObservable();
        if (removeWishListUseCase != null) {
            removeWishListUseCase.unsubscribe();
        }
        if (addToCartUseCase != null) {
            addToCartUseCase.unsubscribe();
        }
    }

    @Override
    public void fetchDataAfterClearSearch(Context context) {
        mPaging.resetPage();
        params = RequestParams.create();


        Subscriber<GqlWishListDataResponse> subscriber = new Subscriber<GqlWishListDataResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (mPaging.getPage() == 1 && wishListView.isPullToRefresh()) {
                    wishListView.displayPull(false);
                }
                wishListView.displayErrorNetwork(false);
            }

            @Override
            public void onStart() {
                wishListView.displayLoadMore(true);
            }

            @Override
            public void onNext(GqlWishListDataResponse gqlWishListDataResponse) {
                wishListView.displayLoadMore(false);
                if (gqlWishListDataResponse != null) {
                    data.clear();
                    dataWishlist.addAll(gqlWishListDataResponse.getGqlWishList().getWishlistDataList());
                    data.addAll(convertToProductItemList(gqlWishListDataResponse.getGqlWishList().getWishlistDataList(),
                            gqlWishListDataResponse.getTopAdsModel(), query));
                    mPaging.setPagination(gqlWishListDataResponse.getGqlWishList().getPagination());
                    wishListView.loadDataChange();
                    wishListView.displayContentList(true);
                    wishListView.setPullEnabled(true);
                    if (gqlWishListDataResponse.getGqlWishList().getWishlistDataList().size() == 0) {
                        wishListView.setEmptyState();
                    } else {
                        data.add(new WishlistTopAdsViewModel(gqlWishListDataResponse.getTopAdsModel(), query));
                    }
                } else {
                    setData();
                }
            }
        };

        getWishListData(context, subscriber);
    }

    @Override
    public void refreshDataOnSearch(CharSequence query) {
        mPaging.resetPage();
        searchWishlist(query.toString());
    }

    @Override
    public void fetchDataFromCache(final Context context) {
        fetchDataFromInternet(context);
    }

    private void sendMoEngageTracker(String productId) {
        if (productId != null) {
            for (int i = 0; i < dataWishlist.size(); i++) {
                if (dataWishlist.get(i) != null) {
                    if (productId.equals(dataWishlist.get(i).getId())) {
                        sendMoEngageRemoveWishlist(context, dataWishlist.get(i));
                        break;
                    }
                }
            }
        }
    }

    public void sendMoEngageRemoveWishlist(Context context, Wishlist data) {
        if (data != null) {
            Map<String, Object> value = DataLayer.mapOf(
                    AppEventTracking.MOENGAGE.PRODUCT_NAME, data.getName(),
                    AppEventTracking.MOENGAGE.PRODUCT_ID, data.getId(),
                    AppEventTracking.MOENGAGE.PRODUCT_URL, data.getUrl(),
                    AppEventTracking.MOENGAGE.PRODUCT_IMAGE_URL, data.getImageUrl(),
                    AppEventTracking.MOENGAGE.PRODUCT_PRICE, data.getPrice()
            );
            if (data.getShop() != null) {
                value.put(AppEventTracking.MOENGAGE.SHOP_ID, data.getShop().getId());
            }
            TrackApp.getInstance().getMoEngage().sendTrackEvent(value, AppEventTracking.EventMoEngage.PRODUCT_REMOVED_FROM_WISHLIST);
        }
    }

    public List<Visitable> convertToProductItemList(List<Wishlist> wishlists, TopAdsModel adsModel, String query) {
        List<Visitable> products = new ArrayList<>();
        for (int i = 0; i < wishlists.size(); i++) {
            ProductItem product = new ProductItem();
            product.setId(wishlists.get(i).getId());
            product.setImgUri(wishlists.get(i).getImageUrl());
            product.setIsNewGold(wishlists.get(i).getShop().isGoldMerchant() ? 1 : 0);
            product.setName(wishlists.get(i).getName());
            product.setPrice(wishlists.get(i).getPriceFmt());
            product.setShop(wishlists.get(i).getShop().getName());
            product.setShopId(Integer.parseInt(wishlists.get(i).getShop().getId()));
            product.setIsWishlist(true);
            product.setIsAvailable(wishlists.get(i).getIsAvailable());
            product.setWholesale(wishlists.get(i).getWholesale().size() > 0 ? "1" : "0");
            product.setPreorder(wishlists.get(i).getIsPreOrder() ? "1" : "0");
            product.setIsGold(wishlists.get(i).getShop().isGoldMerchant() ? "1" : "0");
            product.setLuckyShop(wishlists.get(i).getShop().getLuckyMerchant());
            product.setBadges(wishlists.get(i).getBadges());
            product.setLabels(wishlists.get(i).getLabels());
            product.setShopLocation(wishlists.get(i).getShop().getLocation());
            product.setOfficial(wishlists.get(i).getShop().isOfficial());
            products.add(new WishlistProductViewModel(product));
        }
        if (products.size() >= TOPADS_INDEX && adsModel != null && !adsModel.getData().isEmpty()) {
            products.add(TOPADS_INDEX, new WishlistTopAdsViewModel(adsModel, query));
        }
        return products;
    }

    private void onFinishedDeleteWishlist(final int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wishListView.dismissProgressDialog();
                data.remove(position);
                wishListView.onSuccessDeleteWishlist(
                        params.getString(QUERY, ""), position);
            }
        }, CacheDuration.onSecond(5));
    }

    private void setDataWishlistOnSearch(GqlWishListDataResponse gqlWishListDataResponse) {
        gqlWishListDataResponse.getGqlWishList().getPagination().setNextUrl("search");
        wishListView.sendWishlistImpressionAnalysis(gqlWishListDataResponse.getGqlWishList()
                , dataWishlist.size());

        dataWishlist.addAll(gqlWishListDataResponse.getGqlWishList().getWishlistDataList());
        data.addAll(convertToProductItemList(gqlWishListDataResponse.getGqlWishList().getWishlistDataList(),
                gqlWishListDataResponse.getTopAdsModel(), query));
        mPaging.setPagination(gqlWishListDataResponse.getGqlWishList().getPagination());
        wishListView.setPullEnabled(true);
        wishListView.loadDataChange();
        wishListView.displayContentList(true);
        wishListView.displayLoading(false);
    }


    private class FirstSearchWishlistSubscriber extends Subscriber<GqlWishListDataResponse> {

        @Override
        public void onStart() {
            wishListView.displayLoadMore(true);
        }

        @Override
        public void onNext(GqlWishListDataResponse gqlWishListDataResponse) {
            wishListView.displayPull(false);
            if (gqlWishListDataResponse != null) {
                data.clear();
                dataWishlist.clear();
                if (mPaging.getPage() == 1 && gqlWishListDataResponse.getGqlWishList().getWishlistDataList().size() == 0) {
                    wishListView.setSearchNotFound(query);
                } else {
                    setDataWishlistOnSearch(gqlWishListDataResponse);
                }
            }
        }


        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            if (mPaging.getPage() == 1 && wishListView.isPullToRefresh()) {
                wishListView.displayPull(false);
            }
            wishListView.displayErrorNetwork(false);
        }

    }

    private class LoadMoreSearchWishlistSubscriber extends Subscriber<GqlWishListDataResponse> {

        @Override
        public void onStart() {
            wishListView.displayLoadMore(true);
        }

        @Override
        public void onNext(GqlWishListDataResponse gqlWishListDataResponse) {
            wishListView.displayLoadMore(false);
            if (gqlWishListDataResponse != null) {
                setDataWishlistOnSearch(gqlWishListDataResponse);
            }
        }


        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            if (mPaging.getPage() == 1 && wishListView.isPullToRefresh()) {
                wishListView.displayPull(false);
            }
            wishListView.displayErrorNetwork(false);
        }

    }

    private Subscriber<AddToCartDataModel> addToCartSubscriber(Wishlist dataDetail) {
        return new Subscriber<AddToCartDataModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                wishListView.dismissProgressDialog();
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    /* Ini kalau ga ada internet */
                    wishListView.showAddToCartErrorMessage(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                    /* Ini kalau timeout */
                    wishListView.showAddToCartErrorMessage(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                } else if (e instanceof ResponseErrorException) {
                    /* Ini kalau error dari API kasih message error */
                    wishListView.showAddToCartErrorMessage(e.getMessage());
                } else if (e instanceof ResponseDataNullException) {
                    /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
                    wishListView.showAddToCartErrorMessage(e.getMessage());
                } else if (e instanceof HttpErrorException) {
                    /* Ini Http error, misal 403, 500, 404,
                     code http errornya bisa diambil
                     e.getErrorCode */
                    wishListView.showAddToCartErrorMessage(e.getMessage());
                } else if (e instanceof ResponseCartApiErrorException) {
                    wishListView.showAddToCartErrorMessage(e.getMessage());
                } else {
                    /* Ini diluar dari segalanya hahahaha */
                    wishListView.showAddToCartErrorMessage(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(AddToCartDataModel addToCartResult) {
                NullCheckerKt.isContainNull(addToCartResult, s -> {
                    ContainNullException exception = new ContainNullException("Found " + s + " on " + WishListImpl.class.getSimpleName());
                    if (!BuildConfig.DEBUG) {
                        Crashlytics.logException(exception);
                    }
                    return Unit.INSTANCE;
                });

                wishListView.dismissProgressDialog();
                if (addToCartResult.getStatus().equalsIgnoreCase(AddToCartDataModel.STATUS_OK) && addToCartResult.getData().getSuccess() == 1) {
                    wishListView.showAddToCartMessage(addToCartResult.getData().getMessage().get(0));
                } else {
                    wishListView.showAddToCartErrorMessage(addToCartResult.getErrorMessage().get(0));
                }
                wishListView.sendAddToCartAnalytics(dataDetail, addToCartResult);
            }
        };
    }

}
