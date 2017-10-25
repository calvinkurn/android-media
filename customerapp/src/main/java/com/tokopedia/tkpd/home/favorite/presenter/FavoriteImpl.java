package com.tokopedia.tkpd.home.favorite.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.home.model.HorizontalProductList;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.network.entity.home.FavoriteSendData;
import com.tokopedia.core.network.entity.home.TopAdsData;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.ShopItem;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.SimpleHomeActivity;
import com.tokopedia.tkpd.home.favorite.interactor.FavoriteInteractor;
import com.tokopedia.tkpd.home.favorite.interactor.FavoriteInteractorImpl;
import com.tokopedia.tkpd.home.favorite.model.params.TopAddParams;
import com.tokopedia.tkpd.home.favorite.model.params.WishlistFromNetworkParams;
import com.tokopedia.tkpd.home.favorite.view.FavoriteView;
import com.tokopedia.tkpd.home.interactor.CacheHomeInteractor;
import com.tokopedia.tkpd.home.interactor.CacheHomeInteractorImpl;
import com.tokopedia.tkpd.home.model.FavoriteTransformData;
import com.tokopedia.tkpd.home.model.HorizontalShopList;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Presenter of @see FragmentIndexFavoriteV2
 *
 * @author m.normansyah
 * @since 31/10/2015
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class FavoriteImpl implements Favorite {

    private static final String AD_KEY = "ad_key";
    private static final String SRC = "src";
    private static final String FAV_SHOP = "fav_shop";

    private static final String PARAM_VALUE_XDEVICE = "android";
    private static final String PARAM_VALUE_ITEM = "3";
    private static final String PARAM_VALUE_SRC = "fav_shop";
    private static final String PARAM_VALUE_PAGE = "1";

    private FavoriteView view;
    private List<RecyclerViewItem> data;
    private PagingHandler mPaging;
    private int index;
    private Context mContext;
    // this is clicked by lower level view
    private View recommendView;
    private CompositeSubscription _subscriptions = new CompositeSubscription();
    private CacheHomeInteractorImpl cacheHome = new CacheHomeInteractorImpl();
    private FavoriteInteractor favoriteInteractor;

    public FavoriteImpl(FavoriteView view) {
        this.view = view;
        mPaging = new PagingHandler();

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
        favoriteInteractor = new FavoriteInteractorImpl(context);
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
                TKPDMapParam<String, String> params = new TKPDMapParam<>();
                params.put(SHOP_ID_KEY, item.id);
                params.put(AD_KEY, item.adKey);
                params.put(SRC, FAV_SHOP);

                favoriteInteractor.sendData(
                        buildSendDataSubscriber(shopItem, data),
                        AuthUtil.generateParamsNetwork(mContext, params)
                );

                break;
        }
    }

    @NonNull
    private Subscriber<FavoriteSendData> buildSendDataSubscriber(final ShopItem shopItem,
                                                                 final Object[] data) {

        return new Subscriber<FavoriteSendData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                SnackbarRetry snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(
                        (Activity) mContext,
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

                HorizontalShopList horizontalShopList =
                        (HorizontalShopList) FavoriteImpl.this.data.get(TOP_ADS_START);

                cacheHome.setRecommendedShopCache(horizontalShopList.getShopItemList());
//                mContext.sendBroadcast(new Intent(FragmentProductFeed.ACTION));
            }
        };
    }

    @Override
    public void fetchListData(int listDataType) {
        switch (listDataType) {
            case FAVORITE_DATA_TYPE:
                Log.d(TAG, "try to fetch favorite !!!");
                TKPDMapParam<String, String> favoriteMap = new TKPDMapParam<>();
                favoriteMap.put(OPTION_LOCATION, DEFAULT_OPTION_LOCATION);
                favoriteMap.put(OPTION_NAME, DEFAULT_OPTION_NAME);
                favoriteMap.put(PER_PAGE_KEY, DEFAULT_PER_PAGE);
                favoriteMap.put(PAGE_KEY, String.valueOf(mPaging.getPage()));

                favoriteInteractor.fetchFavoriteShop(buildFetchDataSubriber(),
                        AuthUtil.generateParamsNetwork(mContext, favoriteMap));

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
//        outState.putParcelable(DATA, Parcels.wrap(data));
    }

    @Override
    public void onFetchDataAfterRotate(Bundle outState) {
        if (outState != null) {
            Log.d(TAG, "last page : " + mPaging.onCreate(outState));
            index = outState.getInt(POSITION_VIEW, defaultPositionView);
//            data = Parcels.unwrap(outState.getParcelable(DATA));
        }
    }

    @Override
    public boolean isAfterRotate() {
        return data != null && data.size() > 0;
    }

    @Override
    public void moveToOtherActivity(RecyclerViewItem data, Class<?> clazz, Object... moreDatas) {

        // untuk klik berdasarkan shop id tertentu
        if (moreDatas != null && moreDatas.length > 0 && (moreDatas[shopIdKeyIndex]).equals(SHOP_ID_KEY)) {
            Bundle bundle = ShopInfoActivity.createBundle((String) moreDatas[shopIdValueIndex], "");
            view.moveToOtherActivity(bundle, ShopInfoActivity.class);
        } else if (data instanceof HorizontalProductList) {// untuk klik show semua wishlist
            sendAllClickEventGTM();

            HorizontalProductList temp = (HorizontalProductList) data;
            Bundle bundle = new Bundle();
            bundle.putParcelable(mContext.getString(R.string.bundle_wishlist),
                    Parcels.wrap(temp.getListProduct()));

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
        sendListData(FAVORITE_SEND_DATA_TYPE, item, item);
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
        favoriteInteractor.fetchWishlistFromNetwork(
                buildWishlistSubscriber(), buildWishNetworkListParams(refreshTopAds));
    }


    @NonNull
    private Subscriber<TopAdsData> buildFetchDataSubriber() {
        return new Subscriber<TopAdsData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(mContext, mContext.getString(
                        R.string.message_verification_timeout), Toast.LENGTH_LONG).show();

                view.displayLoadMore(false);// disable load more
                view.displayRetry(true);// enable retry
                view.loadDataChange();
            }

            @Override
            public void onNext(TopAdsData topAdsData) {
                data.addAll(topAdsData.getData().getList());

                mPaging.setHasNext(
                        PagingHandler.CheckHasNext(topAdsData.getData().getPagingHandlerModel()));

                view.displayProgressBar(false);
                view.displayMainContent(true);
                Log.d(TAG, " is swipe show : " + view.isSwipeShow());
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
        };
    }

    private Subscriber<FavoriteTransformData> buildWishlistSubscriber() {

        return new Subscriber<FavoriteTransformData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e != null) {
                    Log.e(TAG, messageTAG + "onError : " + e.getLocalizedMessage());
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
                Log.d(TAG, " is swipe show : " + view.isSwipeShow());

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
        };
    }

    private WishlistFromNetworkParams buildWishNetworkListParams(final boolean refreshTopAds) {

        WishlistFromNetworkParams wishlistFromNetworkParams = new WishlistFromNetworkParams();

        wishlistFromNetworkParams.setWishlistParams(buildWishlistTkpdMapParam());
        wishlistFromNetworkParams.setTopAdsParams(buildTopAdsParams());
        wishlistFromNetworkParams.setFavoriteMapParams(buildFavoriteMapParams());
        wishlistFromNetworkParams.setRefreshTopAds(refreshTopAds);

        return wishlistFromNetworkParams;
    }

    private TKPDMapParam<String, String> buildFavoriteMapParams() {
        TKPDMapParam<String, String> favoriteMap = new TKPDMapParam<>();
        favoriteMap.put(OPTION_LOCATION, DEFAULT_OPTION_LOCATION);
        favoriteMap.put(OPTION_NAME, DEFAULT_OPTION_NAME);
        favoriteMap.put(PER_PAGE_KEY, DEFAULT_PER_PAGE);
        favoriteMap.put(PAGE_KEY, String.valueOf(mPaging.getPage()));

        return AuthUtil.generateParamsNetwork(mContext, favoriteMap);

    }

    private TKPDMapParam<String, String> buildWishlistTkpdMapParam() {
        final TKPDMapParam<String, String> wishList = new TKPDMapParam<>();
        wishList.put(QUERY, DEFAULT_QUERY);
        wishList.put(PER_PAGE_KEY, DEFAULT_PER_PAGE_KEY);
        wishList.put(PAGE_KEY, String.valueOf(mPaging.getPage()));
        return AuthUtil.generateParamsNetwork(mContext, wishList);
    }

    private TopAddParams buildTopAdsParams() {
        TopAddParams topAddParams = new TopAddParams();
        topAddParams.setUserId(SessionHandler.getLoginID(mContext));
        topAddParams.setTkpdSessionId(GCMHandler.getRegistrationId(mContext));
        topAddParams.setxDevice(PARAM_VALUE_XDEVICE);
        topAddParams.setItem(PARAM_VALUE_ITEM);
        topAddParams.setSrc(PARAM_VALUE_SRC);
        topAddParams.setPage(PARAM_VALUE_PAGE);
        return topAddParams;
    }

    @Override
    public void onDestroy() {
        favoriteInteractor.removeAllSubscriptions();
    }
}