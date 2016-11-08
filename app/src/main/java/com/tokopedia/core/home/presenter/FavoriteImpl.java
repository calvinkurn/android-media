package com.tokopedia.core.home.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.home.SimpleHomeActivity;
import com.tokopedia.core.home.api.FavoriteApi;
import com.tokopedia.core.home.interactor.CacheHomeInteractor;
import com.tokopedia.core.home.interactor.CacheHomeInteractorImpl;
import com.tokopedia.core.home.model.HorizontalProductList;
import com.tokopedia.core.home.model.HorizontalShopList;
import com.tokopedia.core.home.model.TopAdsHome;
import com.tokopedia.core.home.model.network.FavoriteSendData;
import com.tokopedia.core.home.model.network.FavoriteTransformData;
import com.tokopedia.core.home.model.network.ShopItemData;
import com.tokopedia.core.home.model.network.TopAdsData;
import com.tokopedia.core.home.model.network.WishlistData;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.NetworkHandler;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.network.apiservices.etc.HomeService;
import com.tokopedia.core.network.apiservices.user.FaveShopActService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.ShopItem;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Presenter of @see FragmentIndexFavoriteV2
 *
 * @author m.normansyah
 * @since 31/10/2015
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class FavoriteImpl implements Favorite {

    public static final String AD_KEY = "ad_key";
    public static final String SRC = "src";
    public static final String FAV_SHOP = "fav_shop";
    FavoriteView view;
    private List<RecyclerViewItem> data;
    private PagingHandler mPaging;

    private int index;
    private Context mContext;

    // this is clicked by lower level view
    private View recommendView;
    HomeService homeService;
    FaveShopActService faveShopActService;
    private CompositeSubscription _subscriptions = new CompositeSubscription();
    private CacheHomeInteractorImpl cacheHome = new CacheHomeInteractorImpl();

    public FavoriteImpl(FavoriteView view) {
        this.view = view;
        mPaging = new PagingHandler();
        homeService = new HomeService();
        faveShopActService = new FaveShopActService();
    }

    @Override
    public void initInstances(Context context) {
        mContext = context;
        view.initHolder();
        if (!isAfterRotate()) {
            data = new ArrayList<>();
        }
        view.initAdapter(data);
        view.initLayoutManager();
    }

    @Override
    public void setLocalyticFlow(Context context) {
        if (context != null) {
            String screenName = context.getString(R.string.home_fav_store);
            ScreenTracking.screenLoca(screenName);
        }
    }

    @Override
    public void sendAppsFlyerData(Context context) {
        ScreenTracking.sendAFGeneralScreenEvent(Jordan.AF_SCREEN_FAVORIT);
    }

    @Override
    public void initData() {
        view.displayLoadMore(true);
        fetchListData(WISHLIST_DATA_TYPE);
    }

    @Override
    public void loadMore() {
        if (mPaging.CheckNextPage()) {
            mPaging.nextPage();
            fetchListData(FAVORITE_DATA_TYPE);
        }
    }

    @Override
    public void sendListData(int listDataType, final ShopItem shopItem, final Object... data) {
        switch (listDataType) {
            case FAVORITE_SEND_DATA_TYPE:
                ShopItem item = (ShopItem) data[0];
                NetworkCalculator favoriteSendData = new NetworkCalculator(NetworkConfig.POST, mContext,
                        TkpdBaseURL.User.URL_FAVE_SHOP_ACTION + TkpdBaseURL.User.PATH_FAVE_SHOP)
                        .setIdentity()
                        .addParam(SHOP_ID_KEY, item.id)
                        .addParam(AD_KEY, item.adKey)
                        .addParam(SRC, FAV_SHOP)
                        .compileAllParam()
                        .finish();

                Map<String, String> params = new HashMap<>();
                params.put(SHOP_ID_KEY, item.id);
                params.put(AD_KEY, item.adKey);
                params.put(SRC, FAV_SHOP);

                _subscriptions.add(
                        faveShopActService.getApi().faveShop2(AuthUtil.generateParams(mContext, params))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .unsubscribeOn(Schedulers.io())
                                .subscribe(new Subscriber<FavoriteSendData>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        SnackbarRetry snackbarRetry = NetworkErrorHelper.createSnackbarWithAction((Activity) mContext,
                                                new NetworkErrorHelper.RetryClickedListener() {
                                                    @Override
                                                    public void onRetryClicked() {
                                                        sendListData(FAVORITE_SEND_DATA_TYPE, shopItem, data);
                                                    }
                                                });
                                        snackbarRetry.showRetrySnackbar();

                                    }

                                    @Override
                                    public void onNext(FavoriteSendData favoriteSendData) {
                                        if (recommendView == null || shopItem == null)
                                            throw new RuntimeException("need to supply data in here !!!");
                                        Log.d(FavoriteImpl.class.getSimpleName(), "add shopItem " + shopItem.name);
                                        FavoriteImpl.this.data.add(FAVORITE_START, shopItem);
                                        view.getAdapter().notifyItemInserted(FAVORITE_START);
                                        if (FavoriteImpl.this.data.size() == 3)
                                            view.loadDataChange();
                                        recommendView.clearAnimation();

                                        HorizontalShopList horizontalShopList = (HorizontalShopList) FavoriteImpl.this.data.get(TOP_ADS_START);

                                        cacheHome.setRecommendedShopCache(horizontalShopList.getShopItemList());
                                    }
                                })
                );

                break;
        }
    }

    @Override
    public void fetchListData(int listDataType) {
        switch (listDataType) {
            case FAVORITE_DATA_TYPE:
                Log.d(TAG, "try to fetch favorite !!!");
                NetworkCalculator favoriteNetworkCalc = new NetworkCalculator(NetworkConfig.GET, mContext,
                        TkpdBaseURL.Etc.URL_HOME + TkpdBaseURL.Etc.PATH_GET_FAVORITE_SHOP)
                        .setIdentity()
                        .addParam(OPTION_LOCATION, DEFAULT_OPTION_LOCATION)
                        .addParam(OPTION_NAME, DEFAULT_OPTION_NAME)
                        .addParam(PER_PAGE_KEY, DEFAULT_PER_PAGE)
                        .addParam(PAGE_KEY, mPaging.getPage() + "")
                        .compileAllParam()
                        .finish();

                Map<String, String> favoriteMap = new HashMap<>();
                favoriteMap.put(OPTION_LOCATION, favoriteNetworkCalc.getContent().get(OPTION_LOCATION));
                favoriteMap.put(OPTION_NAME, favoriteNetworkCalc.getContent().get(OPTION_NAME));
                favoriteMap.put(PER_PAGE_KEY, favoriteNetworkCalc.getContent().get(PER_PAGE_KEY));
                favoriteMap.put(PAGE_KEY, favoriteNetworkCalc.getContent().get(PAGE_KEY));
                _subscriptions.add(
                        homeService.getApi().getFavoriteShop(AuthUtil.generateParams(mContext, favoriteMap))
                                .map(new Func1<TopAdsData, TopAdsData>() {
                                    @Override
                                    public TopAdsData call(TopAdsData topAdsData) {
                                        List<ShopItem> shopItems = new ArrayList<ShopItem>();
                                        for (int i = 0; i < topAdsData.getData().getList().size(); i++) {
                                            ShopItem shopItem = topAdsData.getData().getList().get(i);
                                            shopItem.isFav = FAVORITE_ISFAVORITE_VALUE;
                                            Log.d(getClass().getSimpleName(), "shopItem name " + shopItem.name);
                                            shopItems.add(shopItem);
                                        }
                                        ShopItemData shopItemData = new ShopItemData();
                                        shopItemData.setPagingHandlerModel(topAdsData.getData().getPagingHandlerModel());
                                        shopItemData.setList(shopItems);
                                        topAdsData.setData(shopItemData);
                                        return topAdsData;
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .unsubscribeOn(Schedulers.io())
                                .subscribe(new Subscriber<TopAdsData>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(mContext, mContext.getString(R.string.message_verification_timeout), Toast.LENGTH_LONG).show();
                                        view.displayLoadMore(false);// disable load more
                                        view.displayRetry(true);// enable retry
                                        view.loadDataChange();
                                    }

                                    @Override
                                    public void onNext(TopAdsData topAdsData) {
                                        data.addAll(topAdsData.getData().getList());

                                        mPaging.setHasNext(PagingHandler.CheckHasNext(topAdsData.getData().getPagingHandlerModel()));

                                        view.displayProgressBar(false);
                                        view.displayMainContent(true);
                                        Log.d(TAG, FavoriteImpl.class.getSimpleName() + " is swipe show : " + view.isSwipeShow());
                                        if (view.isSwipeShow()) {
                                            view.displayPull(false);
                                        }
                                        if (mPaging.CheckNextPage()) {
                                            view.displayLoadMore(true);
                                        } else {
                                            view.displayLoadMore(false);
                                        }
                                        view.setPullEnabled(true);

                                        view.loadDataChange();
                                    }
                                })
                );

                break;
            case WISHLIST_DATA_TYPE:
                fetchWishListFromCache();
                break;
            case TOPADS_DATA_TYPE:
                break;
        }
    }

    @Override
    public void subscribe() {
        _subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);
        cacheHome = new CacheHomeInteractorImpl();
    }

    @Override
    public void unSubscribe() {
        RxUtils.unsubscribeIfNotNull(_subscriptions);
        cacheHome.unSubscribeObservable();
    }

    @Override
    public List<RecyclerViewItem> parseJSON(JSONObject Result, int dataType) {
        throw new RuntimeException("dont use this anymore");
    }

    @Override
    public void resetToPageOne() {
        mPaging.resetPage();
//        fetchListData(WISHLIST_DATA_TYPE);
        fetchWishlistFromNetwork(true);
    }

    @Override
    public void onSaveDataBeforeRotate(Bundle outState) {
        // pass paging handler the logic
        mPaging.onSavedInstanceState(outState);
        index = view.getLastPosition();
        outState.putInt(POSITION_VIEW, index);
        outState.putParcelable(DATA, Parcels.wrap(data));
    }

    @Override
    public void onFetchDataAfterRotate(Bundle outState) {
        if (outState != null) {
            Log.d(TAG, "last page : " + mPaging.onCreate(outState));
            index = outState.getInt(POSITION_VIEW, defaultPositionView);
            data = Parcels.unwrap(outState.getParcelable(DATA));
        }
    }

    @Override
    public boolean isAfterRotate() {
        return data != null && data.size() > 0;
    }

    @Override
    public void moveToOtherActivity(RecyclerViewItem data, Class<?> clazz, Object... moreDatas) {

        // untuk klik berdasarkan shop id tertentu
        if (moreDatas != null && moreDatas.length > 0 && ((String) moreDatas[shopIdKeyIndex]).equals(SHOP_ID_KEY)) {
            Bundle bundle = ShopInfoActivity.createBundle((String) moreDatas[shopIdValueIndex], "");
            view.moveToOtherActivity(bundle, ShopInfoActivity.class);
        } else if (data instanceof HorizontalProductList) {// untuk klik show semua wishlist
            sendAllClickEventGTM();

            HorizontalProductList temp = (HorizontalProductList) data;
            Bundle bundle = new Bundle();
            bundle.putParcelable(mContext.getString(R.string.bundle_wishlist), Parcels.wrap(temp.getListProduct()));
            bundle.putInt(SimpleHomeActivity.FRAGMENT_TYPE, SimpleHomeActivity.WISHLIST_FRAGMENT);
            view.moveToOtherActivity(bundle, SimpleHomeActivity.class);
        }
    }

    @Override
    public void cachePageOne(JSONObject jsonObject, int dataType) {
        throw new RuntimeException("dont use this anymore");
    }

    @Override
    public boolean isCacheOneDay() {
//        return (System.currentTimeMillis()/1000) - cache.getLong("expiry") < 3600 && mPaging.getPage()==1;
        return false;
    }

    @Override
    public void setTimeOut() {
        throw new RuntimeException("don't use this method, remove it!!");
    }

    @Override
    public void setRetryListener() {
        view.setOnRetryListenerRV();
    }

    @Override
    public void onRecommendShopClicked(View view, ShopItem item) {
        this.recommendView = view;
        sendListData(FAVORITE_SEND_DATA_TYPE, item, new Object[]{item});
    }

    @Override
    public void scrollToPosition() {
        view.scrollTo(index);
    }

    @Override
    public void sendAllClickEventGTM() {
        UnifyTracking.eventWishlistAll();
    }

    @Override
    public boolean isMorePage() {
        return mPaging.CheckNextPage();
    }

    private void fetchWishListFromCache() {
        cacheHome.getFavoriteCache(new CacheHomeInteractor.GetFavoriteCacheListener() {
            @Override
            public void onSuccess(FavoriteTransformData favoriteTransformData) {

                data.clear();

                data.add(favoriteTransformData.getHorizontalProductList());
                data.add(favoriteTransformData.getHorizontalShopList());
                data.addAll(favoriteTransformData.getShopItems());
                boolean hasNext = PagingHandler.CheckHasNext(favoriteTransformData.getPagingHandlerModel());
                Log.d(TAG, messageTAG + "hasNext : " + hasNext);
                mPaging.setHasNext(hasNext);


                view.displayMainContent(true);
                view.displayProgressBar(false);
//                view.displayPull(true);

                if (mPaging.CheckNextPage()) {
                    view.displayLoadMore(true);
                } else {
                    view.displayLoadMore(false);
                }

                view.loadDataChange();

                fetchWishlistFromNetwork(false);
            }

            @Override
            public void onError(Throwable e) {
                fetchWishlistFromNetwork(false);
            }
        });


    }

    private void fetchWishlistFromNetwork(final Boolean refreshTopAds) {
        Log.d(TAG, "try to fetch wishlist !!!");
        final NetworkCalculator wishListCalc = new NetworkCalculator(NetworkConfig.GET, mContext,
                TkpdBaseURL.Etc.URL_HOME + TkpdBaseURL.Etc.PATH_GET_WISHLIST)
                .setIdentity()
                .addParam(QUERY, DEFAULT_QUERY)
                .addParam(PER_PAGE_KEY, DEFAULT_PER_PAGE_KEY)
                .addParam(PAGE_KEY, mPaging.getPage() + "")
                .compileAllParam()
                .finish();

        final Map<String, String> wishList = new HashMap<>();
        wishList.put(QUERY, DEFAULT_QUERY);
        wishList.put(PER_PAGE_KEY, DEFAULT_PER_PAGE_KEY);
        wishList.put(PAGE_KEY, mPaging.getPage() + "");

        _subscriptions.add(
                homeService.getApi().getWishList(AuthUtil.generateParams(mContext, wishList))
                        .map(new Func1<Response<WishlistData>, WishlistData>() {
                            @Override
                            public WishlistData call(Response<WishlistData> response) {
                                WishlistData wishlistData = response.body();
                                if (wishlistData.getMessageError() == null) {// no error
                                    // do nothing
                                } else if (response.code() == 200 && wishlistData.getMessageError() != null && wishlistData.getMessageError().size() > 0) {
                                    throw new RuntimeException(response.body().getMessageError().get(0));
                                } else {
                                    new RetrofitUtils.DefaultErrorHandler(response.code());
                                }
                                return wishlistData;
                            }
                        })
                        .map(new Func1<WishlistData, FavoriteTransformData>() {
                            @Override
                            public FavoriteTransformData call(WishlistData wishlistData) {
                                FavoriteTransformData favoriteTransformData = new FavoriteTransformData();
                                favoriteTransformData.setHorizontalProductList(new HorizontalProductList(wishlistData.getData().getList()));
                                return favoriteTransformData;
                            }
                        })
                        .map(new Func1<FavoriteTransformData, FavoriteTransformData>() {
                            @Override
                            public FavoriteTransformData call(FavoriteTransformData favoriteTransformData) {
                                NetworkCalculator topAdsCalc = new NetworkCalculator(NetworkConfig.GET, mContext,
                                        TkpdBaseURL.Product.URL_PROMO + TkpdBaseURL.Product.PATH_AD_SHOP_FEED)
                                        .setIdentity()
                                        .compileAllParam()
                                        .finish();
                                favoriteTransformData.setContent(topAdsCalc.getContent());
                                favoriteTransformData.setHeader(topAdsCalc.getHeader());
                                return favoriteTransformData;
                            }
                        })
                        .flatMap(new Func1<FavoriteTransformData, Observable<FavoriteTransformData>>() {
                            @Override
                            public Observable<FavoriteTransformData> call(FavoriteTransformData favoriteTransformData) {
                                if (!cacheHome.isShopAdsCacheAvailable() || refreshTopAds) {
                                    Observable<Response<TopAdsHome>> topAdsHome
                                            = RetrofitUtils.createRetrofit(TkpdBaseURL.TOPADS_DOMAIN)
                                            .create(FavoriteApi.class)
                                            .getTopAdsApi(
                                                    favoriteTransformData.getContent().get(NetworkCalculator.USER_ID),
                                                    favoriteTransformData.getContent().get(NetworkCalculator.DEVICE_ID),
                                                    "android",
                                                    "3",
                                                    "fav_shop",
                                                    "1"
                                            );

                                    return Observable.zip(topAdsHome,
                                            Observable.just(favoriteTransformData),
                                            new Func2<Response<TopAdsHome>, FavoriteTransformData, FavoriteTransformData>() {
                                                @Override
                                                public FavoriteTransformData call(Response<TopAdsHome> topAdsHomeResponse, FavoriteTransformData favoriteTransformData) {
                                                    CommonUtils.dumper("CACHE FROM NETWORK");
                                                    favoriteTransformData.setHorizontalShopList(
                                                            new HorizontalShopList(HorizontalShopList.fromDatas(topAdsHomeResponse.body().getData()))
                                                    );
                                                    return favoriteTransformData;
                                                }
                                            }
                                    );
                                } else {
                                    Observable<HorizontalShopList> topAdsHome = cacheHome.getShopAdsObservable();

                                    return Observable.zip(topAdsHome,
                                            Observable.just(favoriteTransformData),
                                            new Func2<HorizontalShopList, FavoriteTransformData, FavoriteTransformData>() {
                                                @Override
                                                public FavoriteTransformData call(HorizontalShopList horizontalShopList, FavoriteTransformData favoriteTransformData) {
                                                    CommonUtils.dumper("CACHE FROM DISK: " + horizontalShopList);
                                                    for (int i = 0; i < horizontalShopList.getShopItemList().size(); i++) {
                                                        if (horizontalShopList.getShopItemList().get(i).isFav == null) {
                                                            horizontalShopList.getShopItemList().get(i).isFav = TOP_ADS_AD_SHOP_VALUE;
                                                        }
                                                    }
                                                    favoriteTransformData.setHorizontalShopList(horizontalShopList);
                                                    return favoriteTransformData;
                                                }
                                            }
                                    );

                                }

                            }
                        })
                        .map(new Func1<FavoriteTransformData, FavoriteTransformData>() {
                            @Override
                            public FavoriteTransformData call(FavoriteTransformData favoriteTransformData) {
                                NetworkCalculator favoriteNetworkCalc = new NetworkCalculator(NetworkConfig.GET, mContext,
                                        TkpdBaseURL.Etc.URL_HOME + TkpdBaseURL.Etc.PATH_GET_FAVORITE_SHOP)
                                        .setIdentity()
                                        .addParam(OPTION_LOCATION, DEFAULT_OPTION_LOCATION)
                                        .addParam(OPTION_NAME, DEFAULT_OPTION_NAME)
                                        .addParam(PER_PAGE_KEY, DEFAULT_PER_PAGE)
                                        .addParam(PAGE_KEY, mPaging.getPage() + "")
                                        .compileAllParam()
                                        .finish();
                                favoriteTransformData.setHeader(favoriteNetworkCalc.getHeader());
                                favoriteTransformData.setContent(favoriteNetworkCalc.getContent());
                                return favoriteTransformData;
                            }
                        })
                        .flatMap(new Func1<FavoriteTransformData, Observable<FavoriteTransformData>>() {
                            @Override
                            public Observable<FavoriteTransformData> call(FavoriteTransformData favoriteTransformData) {
                                Map<String, String> favoriteMap = new HashMap<>();
                                favoriteMap.put(OPTION_LOCATION, favoriteTransformData.getContent().get(OPTION_LOCATION));
                                favoriteMap.put(OPTION_NAME, favoriteTransformData.getContent().get(OPTION_NAME));
                                favoriteMap.put(PER_PAGE_KEY, favoriteTransformData.getContent().get(PER_PAGE_KEY));
                                favoriteMap.put(PAGE_KEY, favoriteTransformData.getContent().get(PAGE_KEY));

                                final Observable<TopAdsData> favorite
                                        = homeService.getApi().getFavoriteShop(AuthUtil.generateParams(mContext, favoriteMap));

                                return Observable.zip(favorite,
                                        Observable.just(favoriteTransformData),
                                        new Func2<TopAdsData, FavoriteTransformData, FavoriteTransformData>() {
                                            @Override
                                            public FavoriteTransformData call(TopAdsData topAdsData, FavoriteTransformData favoriteTransformData) {
                                                List<ShopItem> shopItems = new ArrayList<ShopItem>();
                                                for (int i = 0; i < topAdsData.getData().getList().size(); i++) {
                                                    ShopItem shopItem = topAdsData.getData().getList().get(i);
                                                    shopItem.isFav = FAVORITE_ISFAVORITE_VALUE;
                                                    shopItems.add(shopItem);
                                                }
                                                favoriteTransformData.setPagingHandlerModel(topAdsData.getData().getPagingHandlerModel());
                                                favoriteTransformData.setShopItems(shopItems);// set shop item
                                                return favoriteTransformData;
                                            }
                                        });
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<FavoriteTransformData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (e != null) {
                                    if (e.getMessage() != null && RetrofitUtils.isSessionInvalid(e.getMessage())) {
                                        NetworkHandler.forceLogout(mContext);
                                    } else {
                                        Log.e(TAG, messageTAG + "onError : " + e.getLocalizedMessage());
                                        Toast.makeText(mContext, mContext.getString(R.string.message_verification_timeout), Toast.LENGTH_LONG).show();
                                        //[START] display retry
                                        view.displayProgressBar(false);
                                        if (data.size() == 0) {
                                            view.displayMainContent(false);
                                            view.displayRetryFull();// display full retry
                                        } else {
                                            view.displayMainContent(true);
                                            view.displayLoadMore(false);// disable load more
                                            view.displayRetry(true);// enable retry
                                        }
                                        view.displayPull(false);
                                        view.loadDataChange();
                                        //[END] display retry
                                    }
                                }
                            }

                            @Override
                            public void onNext(FavoriteTransformData favoriteTransformData) {
                                view.displayRetry(false);
                                view.hideRetryFull();

                                data.clear();

                                if (view.isSwipeShow()) {
                                    view.displayPull(false);

                                    Log.d(TAG, messageTAG + "size data after clear : " + data.size());
                                }

                                data.add(favoriteTransformData.getHorizontalProductList());
                                data.add(favoriteTransformData.getHorizontalShopList());
                                data.addAll(favoriteTransformData.getShopItems());
                                Log.d(TAG, messageTAG + favoriteTransformData.getHorizontalProductList().getListProduct());
                                Log.d(TAG, messageTAG + "size data after set new data : " + data.size());

                                boolean hasNext = PagingHandler.CheckHasNext(favoriteTransformData.getPagingHandlerModel());
                                Log.d(TAG, messageTAG + "hasNext : " + hasNext);
                                mPaging.setHasNext(hasNext);

                                view.displayProgressBar(false);
                                view.displayMainContent(true);


                                Log.d(TAG, messageTAG + "hasNext : " + hasNext);
                                Log.d(TAG, FavoriteImpl.class.getSimpleName() + " is swipe show : " + view.isSwipeShow());

                                view.setPullEnabled(true);

                                view.initAdapter(data);
                                view.setAdapter();

                                if (mPaging.CheckNextPage()) {
                                    view.displayLoadMore(true);
                                } else {
                                    view.displayLoadMore(false);
                                }

                                view.loadDataChange();

                                cacheHome.setFavoriteCache(favoriteTransformData);
                            }
                        })
        )
        ;
    }
}
