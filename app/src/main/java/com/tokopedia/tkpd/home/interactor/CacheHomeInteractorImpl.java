package com.tokopedia.tkpd.home.interactor;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.tkpd.database.CacheUtil;
import com.tokopedia.tkpd.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.home.model.HorizontalProductList;
import com.tokopedia.tkpd.home.model.HorizontalShopList;
import com.tokopedia.tkpd.home.model.network.FavoriteTransformData;
import com.tokopedia.tkpd.home.model.network.GetListFaveShopId;
import com.tokopedia.tkpd.home.model.network.ProductFeedData;
import com.tokopedia.tkpd.home.model.network.ProductFeedTransformData;
import com.tokopedia.tkpd.home.model.network.WishlistData;
import com.tokopedia.tkpd.rescenter.detail.model.detailresponsedata.ResCenterTrackShipping;
import com.tokopedia.tkpd.rxjava.RxUtils;
import com.tokopedia.tkpd.util.PagingHandler;
import com.tokopedia.tkpd.var.ProductItem;
import com.tokopedia.tkpd.var.ShopItem;
import com.tokopedia.tkpd.var.TkpdCache;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by ricoharisin on 12/7/15.
 */
public class CacheHomeInteractorImpl implements CacheHomeInteractor {

    private static String TAG = "CacheHome";
    Gson gson = new GsonBuilder().create();
    CompositeSubscription compositeSubscription;

    public CacheHomeInteractorImpl() {
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getProductFeedCache(final GetProductFeedCacheListener listener) {
        compositeSubscription.add(Observable.just(TkpdCache.Key.PRODUCT_FEED)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, ProductFeedTransformData>() {
                    @Override
                    public ProductFeedTransformData call(String s) {
                        GlobalCacheManager cacheFeed = new GlobalCacheManager();
                        GlobalCacheManager cacheHistory = new GlobalCacheManager();
                        Type type = new TypeToken<List<ProductItem>>() {
                        }.getType();

                        ProductFeedTransformData productFeedTransformData = new ProductFeedTransformData();
                        List<ProductItem> listFeed = CacheUtil.convertStringToListModel(cacheFeed.getValueString(TkpdCache.Key.PRODUCT_FEED), type);
                        List<ProductItem> listHistory = CacheUtil.convertStringToListModel(cacheHistory.getValueString(TkpdCache.Key.RECENT_PRODUCT), type);

                        productFeedTransformData.setListProductItems(listFeed);
                        productFeedTransformData.setGetListFaveShopId((GetListFaveShopId) CacheUtil.convertStringToModel(new GlobalCacheManager().getValueString(TkpdCache.Key.FAV_SHOP), new TypeToken<GetListFaveShopId>() {}.getType()));
                        productFeedTransformData.setHorizontalProductList(new HorizontalProductList(listHistory));

                        return productFeedTransformData;
                    }
                })
                .subscribe(new Subscriber<ProductFeedTransformData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(ProductFeedTransformData productFeedTransformData) {
                        listener.onSuccess(productFeedTransformData);
                    }
                }));
    }

    @Override
    public void setProductFeedCache(ProductFeedTransformData productFeedTransformData) {
        compositeSubscription.add(Observable.just(productFeedTransformData)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<ProductFeedTransformData, Boolean>() {
                    @Override
                    public Boolean call(ProductFeedTransformData productFeedTransformData) {
                        GlobalCacheManager cacheFeed = new GlobalCacheManager();
                        cacheFeed.setKey(TkpdCache.Key.PRODUCT_FEED);
                        cacheFeed.setValue(CacheUtil.convertListModelToString(productFeedTransformData.getListProductItems(),
                                new TypeToken<List<ProductItem>>() {
                                }.getType()));
                        cacheFeed.setCacheDuration(600);
                        cacheFeed.store();

                        GlobalCacheManager cacheHistory = new GlobalCacheManager();
                        cacheHistory.setKey(TkpdCache.Key.RECENT_PRODUCT);
                        cacheHistory.setValue(CacheUtil.convertListModelToString(productFeedTransformData.getHorizontalProductList().getListProduct(),
                                new TypeToken<List<ProductItem>>() {
                                }.getType()));
                        cacheHistory.setCacheDuration(600);
                        cacheHistory.store();

                        GlobalCacheManager cacheFavShop = new GlobalCacheManager();
                        cacheFavShop.setKey(TkpdCache.Key.FAV_SHOP);
                        cacheFavShop.setValue(CacheUtil.convertModelToString(productFeedTransformData.getGetListFaveShopId(), new TypeToken<GetListFaveShopId>() {}.getType()));
                        cacheFavShop.setCacheDuration(600);
                        cacheFavShop.store();
                        Log.i(TAG, "END Storing cache");
                        return true;
                    }
                })
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                        new GlobalCacheManager().delete(TkpdCache.Key.PRODUCT_FEED);
                        new GlobalCacheManager().delete(TkpdCache.Key.RECENT_PRODUCT);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                }));

    }

    @Override
    public void getFavoriteCache(final GetFavoriteCacheListener listener) {
        if(compositeSubscription.isUnsubscribed()) compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Boolean, FavoriteTransformData>() {

                    @Override
                    public FavoriteTransformData call(Boolean aBoolean) {
                        Log.i(TAG, "start to getting a cache");


                        GlobalCacheManager cacheFavorite = new GlobalCacheManager();
                        GlobalCacheManager cacheWishlist = new GlobalCacheManager();
                        GlobalCacheManager cacheAdShop = new GlobalCacheManager();
                        GlobalCacheManager cachePagingFav = new GlobalCacheManager();
                        Type typeProduct = new TypeToken<List<ProductItem>>() {
                        }.getType();
                        Type typeShop = new TypeToken<List<ShopItem>>() {
                        }.getType();

                        FavoriteTransformData favoriteTransformData = new FavoriteTransformData();
                        List<ProductItem> listWishlist = CacheUtil.convertStringToListModel(cacheWishlist.getValueString(TkpdCache.Key.WISHLIST), typeProduct);
                        List<ShopItem> listFavorite = CacheUtil.convertStringToListModel(cacheFavorite.getValueString(TkpdCache.Key.FAVORITE_SHOP), typeShop);
                        List<ShopItem> listAdShop = CacheUtil.convertStringToListModel(cacheAdShop.getValueString(TkpdCache.Key.RECOMMEND_SHOP), typeShop);
                        PagingHandler.PagingHandlerModel pagingHandlerModel =
                                CacheUtil.convertStringToModel(
                                        cachePagingFav.getValueString(TkpdCache.Key.FAVORITE_PAGING),
                                        PagingHandler.PagingHandlerModel.class);

                        favoriteTransformData.setHorizontalProductList(new HorizontalProductList(listWishlist));
                        favoriteTransformData.setHorizontalShopList(new HorizontalShopList(listAdShop));
                        favoriteTransformData.setShopItems(listFavorite);
                        favoriteTransformData.setPagingHandlerModel(pagingHandlerModel);

                        Log.i(TAG, "end of getting cache: " + favoriteTransformData);

                        return favoriteTransformData;
                    }
                })
                .subscribe(new Subscriber<FavoriteTransformData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(FavoriteTransformData favoriteTransformData) {
                        listener.onSuccess(favoriteTransformData);
                    }
                }));
    }

    @Override
    public void setFavoriteCache(FavoriteTransformData favoriteTransformData) {
        compositeSubscription.add(Observable.just(favoriteTransformData)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<FavoriteTransformData, Boolean>() {
                    @Override
                    public Boolean call(FavoriteTransformData favoriteTransformData) {
                        Log.i(TAG, "start to storing a cache: " + favoriteTransformData);

                        GlobalCacheManager cache = new GlobalCacheManager();
                        List<String> listKey = new ArrayList<>();
                        List<String> listValue = new ArrayList<>();

                        listKey.add(TkpdCache.Key.FAVORITE_SHOP);
                        listValue.add(CacheUtil.convertListModelToString(favoriteTransformData.getShopItems(),
                                new TypeToken<List<ShopItem>>() {
                                }.getType()));

                        listKey.add(TkpdCache.Key.WISHLIST);
                        listValue.add(CacheUtil.convertListModelToString(favoriteTransformData.getHorizontalProductList().getListProduct(),
                                new TypeToken<List<ProductItem>>() {
                                }.getType()));

                        listKey.add(TkpdCache.Key.RECOMMEND_SHOP);
                        listValue.add(CacheUtil.convertListModelToString(favoriteTransformData.getHorizontalShopList().getShopItemList(),
                                new TypeToken<List<ShopItem>>() {
                                }.getType()));

                        listKey.add(TkpdCache.Key.FAVORITE_PAGING);
                        listValue.add(CacheUtil.convertModelToString(favoriteTransformData.getPagingHandlerModel(), PagingHandler.PagingHandlerModel.class));

                        cache.bulkInsert(listKey, listValue);

                        Log.i(TAG, "END Storing cache");

                        return true;
                    }
                })
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                }));
    }

    @Override
    public void setProdHistoryCache(ProductFeedData productFeedData) {
        new GlobalCacheManager()
                .setKey(TkpdCache.Key.RECENT_PRODUCT_ALL)
                .setCacheDuration(600)
                .setValue(gson.toJson(productFeedData))
                .store();
    }

    @Override
    public ProductFeedData getProdHistoryCache() {
        ProductFeedData result = null;
        try {
            result = gson.fromJson(new GlobalCacheManager().getValueString(TkpdCache.Key.RECENT_PRODUCT_ALL), ProductFeedData.class);
        }catch(Exception e){}

        return result;
    }

    @Override
    public void setWishListCache(WishlistData wishlistData) {
        new GlobalCacheManager()
                .setKey(TkpdCache.Key.WISHLIST_ALL)
                .setCacheDuration(600)
                .setValue(gson.toJson(wishlistData))
                .store();
    }

    @Override
    public WishlistData getWishListCache() {
        WishlistData result = null;
        try{
            result = gson.fromJson(new GlobalCacheManager().getValueString(TkpdCache.Key.WISHLIST_ALL), WishlistData.class);
        }catch (Exception e){}
        return result;
    }

    @Override
    public Observable<HorizontalShopList> getShopAdsObservable() {
        return Observable.create(new Observable.OnSubscribe<HorizontalShopList>() {
            @Override
            public void call(Subscriber<? super HorizontalShopList> subscriber) {
                GlobalCacheManager cacheAdShop = new GlobalCacheManager();
                Type typeShop = new TypeToken<List<ShopItem>>() {}.getType();
                List<ShopItem> listAdShop = CacheUtil.convertStringToListModel(cacheAdShop.getValueString(TkpdCache.Key.RECOMMEND_SHOP), typeShop);
                subscriber.onNext(new HorizontalShopList(listAdShop));
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Boolean isShopAdsCacheAvailable() {
        return GlobalCacheManager.isAvailable(TkpdCache.Key.RECOMMEND_SHOP);
    }

    @Override
    public void setRecommendedShopCache(List<ShopItem> shopItem) {
        compositeSubscription.add(Observable.just(shopItem)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<List<ShopItem>, Boolean>() {
                    @Override
                    public Boolean call(List<ShopItem> shopItem) {

                        new GlobalCacheManager()
                                .setKey(TkpdCache.Key.RECOMMEND_SHOP)
                                .setValue(CacheUtil.convertListModelToString(shopItem,
                                        new TypeToken<List<ShopItem>>() {
                                        }.getType()))
                                .store();

                        return true;
                    }
                })
                .subscribe());
    }

    @Override
    public void unSubscribeObservable() {
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    public static void deleteAllCache(){
        new GlobalCacheManager().deleteAll();
    }
}
