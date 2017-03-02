package com.tokopedia.tkpd.home.favorite.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.database.model.PagingHandler;
import com.tokopedia.core.home.model.HorizontalProductList;
import com.tokopedia.core.network.apiservices.etc.HomeService;
import com.tokopedia.core.network.apiservices.etc.apis.home.FavoriteApi;
import com.tokopedia.core.network.apiservices.mojito.MojitoService;
import com.tokopedia.core.network.apiservices.user.FaveShopActService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.home.FavoriteSendData;
import com.tokopedia.core.network.entity.home.ProductItemData;
import com.tokopedia.core.network.entity.home.ShopItemData;
import com.tokopedia.core.network.entity.home.TopAdsData;
import com.tokopedia.core.network.entity.home.TopAdsHome;
import com.tokopedia.core.network.entity.home.WishlistData;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.ShopItem;
import com.tokopedia.tkpd.home.favorite.model.params.WishlistFromNetworkParams;
import com.tokopedia.tkpd.home.interactor.CacheHomeInteractorImpl;
import com.tokopedia.tkpd.home.model.FavoriteTransformData;
import com.tokopedia.tkpd.home.model.HorizontalShopList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.tkpd.home.favorite.presenter.Favorite.FAVORITE_ISFAVORITE_VALUE;
import static com.tokopedia.tkpd.home.favorite.presenter.Favorite.TOP_ADS_AD_SHOP_VALUE;

/**
 * @author Kulomady on 11/11/16.
 */

public class FavoriteInteractorImpl implements FavoriteInteractor {

    private static final String TAG = FavoriteInteractorImpl.class.getSimpleName();
    private final CompositeSubscription mCompositeSubscription;
    private final CacheHomeInteractorImpl cacheHome;
    private HomeService homeService;
    MojitoService mojitoService;
    private FaveShopActService faveShopActService;
    private Context mContext;

    public FavoriteInteractorImpl(Context context) {
        mContext = context;
        mCompositeSubscription = new CompositeSubscription();
        homeService = new HomeService();
        mojitoService = new MojitoService();
        cacheHome = new CacheHomeInteractorImpl();
        faveShopActService = new FaveShopActService();
    }


    @Override
    public void sendListDataFavorite(ShopItem shopItem, Object... datas) {

    }

    @Override
    public void fetchListDataFavorite(Subscriber<TopAdsData> topAdsDataSubscriber,
                                      TKPDMapParam<String, String> favoriteMapParams) {
        mCompositeSubscription.add(
                homeService.getApi().getFavoriteShop(favoriteMapParams)
                        .map(new Func1<TopAdsData, TopAdsData>() {
                            @Override
                            public TopAdsData call(TopAdsData topAdsData) {
                                List<ShopItem> shopItems = new ArrayList<>();
                                for (int i = 0; i < topAdsData.getData().getList().size(); i++) {
                                    ShopItem shopItem = topAdsData.getData().getList().get(i);
                                    shopItem.isFav = FAVORITE_ISFAVORITE_VALUE;
                                    Log.d(TAG, "shopItem name " + shopItem.name);
                                    shopItems.add(shopItem);
                                }
                                ShopItemData shopItemData = new ShopItemData();
                                shopItemData.setPagingHandlerModel(topAdsData.getData()
                                        .getPagingHandlerModel());

                                shopItemData.setList(shopItems);
                                topAdsData.setData(shopItemData);
                                return topAdsData;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                topAdsDataSubscriber
                        ));
    }

    @Override
    public void fetchWishlistFromNetwork(Subscriber<FavoriteTransformData> subscriber,
                                         final WishlistFromNetworkParams params) {
        if (params == null) {
            return;
        }

        mCompositeSubscription.add(mojitoService.getApi().getWishlist(SessionHandler.getLoginID(mContext), 4, 1)
                .map(new Func1<Response<com.tokopedia.core.network.entity.wishlist.WishlistData>, WishlistData>() {
                    @Override
                    public WishlistData call(Response<com.tokopedia.core.network.entity.wishlist.WishlistData> response) {
                        return getWishlistData(response);
                    }
                })
                .map(new Func1<WishlistData, FavoriteTransformData>() {
                    @Override
                    public FavoriteTransformData call(WishlistData wishlistData) {
                        return getFavoriteTransformData(wishlistData);
                    }
                })
                .flatMap(new Func1<FavoriteTransformData, Observable<FavoriteTransformData>>() {
                    @Override
                    public Observable<FavoriteTransformData> call(
                            FavoriteTransformData favoriteTransformData) {

                        if (!cacheHome.isShopAdsCacheAvailable() || params.getRefreshTopAds()) {
                            return getFavoriteTransformDataFromNetwork(favoriteTransformData, params);
                        } else {
                            return getFavoriteTransformDataFromDisk(favoriteTransformData);

                        }

                    }
                })
                .flatMap(new Func1<FavoriteTransformData, Observable<FavoriteTransformData>>() {
                    @Override
                    public Observable<FavoriteTransformData> call(
                            FavoriteTransformData favoriteTransformData) {

                        return getFavoriteTransformDataObservable(favoriteTransformData, params);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
        );
    }

    @Override
    public void fetchWishlistFromCache() {

    }

    @Override
    public void removeAllSubscriptions() {
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void sendData(Subscriber<FavoriteSendData> favoriteSendDataSubscriber,
                         TKPDMapParam<String, String> params) {
        mCompositeSubscription.add(faveShopActService.getApi().faveShop2(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(favoriteSendDataSubscriber)
        );

    }

    @Override
    public void fetchFavoriteShop(Subscriber<TopAdsData> topAdsDataSubscriber,
                                  TKPDMapParam<String, String> params) {

        mCompositeSubscription.add(
                homeService.getApi().getFavoriteShop(params)
                        .map(new Func1<TopAdsData, TopAdsData>() {
                            @Override
                            public TopAdsData call(TopAdsData topAdsData) {
                                List<ShopItem> shopItems = new ArrayList<>();
                                for (int i = 0; i < topAdsData.getData().getList().size(); i++) {
                                    ShopItem shopItem = topAdsData.getData().getList().get(i);
                                    shopItem.isFav = FAVORITE_ISFAVORITE_VALUE;
                                    Log.d(TAG, "shopItem name " + shopItem.name);
                                    shopItems.add(shopItem);
                                }
                                ShopItemData shopItemData = new ShopItemData();

                                shopItemData.setPagingHandlerModel(
                                        topAdsData.getData().getPagingHandlerModel());

                                shopItemData.setList(shopItems);
                                topAdsData.setData(shopItemData);
                                return topAdsData;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(topAdsDataSubscriber)
        );
    }

    @NonNull
    private WishlistData getWishlistData(Response<com.tokopedia.core.network.entity.wishlist.WishlistData> response) {
        WishlistData wishlistData = new WishlistData();
        if (response.body() != null) {
            ProductItemData itemData = new ProductItemData();
            ArrayList<ProductItem> items = new ArrayList<>();
            for (com.tokopedia.core.network.entity.wishlist.Wishlist data : response.body().getWishlist()){
                ProductItem item = new ProductItem();
                item.setBadges(data.getBadges());
                item.setId(data.getId());
                item.setImgUri(data.getImageUrl());
                item.setIsAvailable(data.getIsAvailable());
                item.setLabels(data.getLabels());
                item.setName(data.getName());
                item.setPrice(data.getPriceFmt());
                item.setShopId(Integer.parseInt(data.getShop().getId()));
                item.setShop(data.getShop().getName());
                items.add(item);
            }
            itemData.setList(items);
            wishlistData.setData(itemData);
        } else {
            new RetrofitUtils.DefaultErrorHandler(response.code());
        }
        return wishlistData;
    }

    @NonNull
    private FavoriteTransformData getFavoriteTransformData(WishlistData wishlistData) {
        FavoriteTransformData favoriteTransformData = new FavoriteTransformData();
        HorizontalProductList horizontalProductList =
                new HorizontalProductList(wishlistData.getData().getList());
        favoriteTransformData.setHorizontalProductList(horizontalProductList);
        return favoriteTransformData;
    }

    @NonNull
    private Observable<FavoriteTransformData> getFavoriteTransformDataFromNetwork(
            FavoriteTransformData favoriteTransformData,
            WishlistFromNetworkParams wishlistNetworkParams) {

        Observable<Response<TopAdsHome>> topAdsHome
                = RetrofitUtils.createRetrofit(TkpdBaseURL.TOPADS_DOMAIN)
                .create(FavoriteApi.class)
                .getTopAdsApi(
                        wishlistNetworkParams.getTopAdsParams().getUserId(),
                        wishlistNetworkParams.getTopAdsParams().getTkpdSessionId(),
                        wishlistNetworkParams.getTopAdsParams().getxDevice(),
                        wishlistNetworkParams.getTopAdsParams().getItem(),
                        wishlistNetworkParams.getTopAdsParams().getSrc(),
                        wishlistNetworkParams.getTopAdsParams().getPage()
                );

        return Observable.zip(topAdsHome,
                Observable.just(favoriteTransformData),
                new Func2<Response<TopAdsHome>, FavoriteTransformData, FavoriteTransformData>() {
                    @Override
                    public FavoriteTransformData call(
                            Response<TopAdsHome> topAdsHomeResponse,
                            FavoriteTransformData favoriteTransformData) {

                        CommonUtils.dumper("CACHE FROM NETWORK");
                        TopAdsHome.Data[] datas = topAdsHomeResponse.body().getData();
                        List<ShopItem> shopItemList = HorizontalShopList.fromDatas(datas);
                        favoriteTransformData.setHorizontalShopList(
                                new HorizontalShopList(shopItemList)
                        );
                        return favoriteTransformData;
                    }
                }
        );
    }


    @NonNull
    private Observable<FavoriteTransformData> getFavoriteTransformDataFromDisk(
            FavoriteTransformData favoriteTransformData) {

        Observable<HorizontalShopList> topAdsHome = cacheHome.getShopAdsObservable();

        return Observable.zip(topAdsHome,
                Observable.just(favoriteTransformData),
                new Func2<HorizontalShopList, FavoriteTransformData, FavoriteTransformData>() {
                    @Override
                    public FavoriteTransformData call(HorizontalShopList horizontalShopList,
                                                      FavoriteTransformData favoriteTransformData) {

                        CommonUtils.dumper("CACHE FROM DISK: " + horizontalShopList);
                        List<ShopItem> shopItemList = horizontalShopList.getShopItemList();

                        for (int i = 0; i < shopItemList.size(); i++) {
                            if (shopItemList.get(i).isFav == null) {
                                shopItemList.get(i).isFav = TOP_ADS_AD_SHOP_VALUE;
                            }
                        }
                        favoriteTransformData.setHorizontalShopList(horizontalShopList);
                        return favoriteTransformData;
                    }
                }
        );
    }


    @NonNull
    private Observable<FavoriteTransformData> getFavoriteTransformDataObservable(
            FavoriteTransformData favoriteTransformData, WishlistFromNetworkParams params) {
        final Observable<TopAdsData> favorite
                = homeService.getApi().getFavoriteShop(params.getFavoriteMapParams());

        return Observable.zip(favorite,
                Observable.just(favoriteTransformData),
                new Func2<TopAdsData, FavoriteTransformData, FavoriteTransformData>() {
                    @Override
                    public FavoriteTransformData call(
                            TopAdsData topAdsData, FavoriteTransformData favoriteTransformData) {

                        return injectShoplistToFavoriteTransformData(topAdsData,
                                favoriteTransformData);
                    }
                });
    }

    @NonNull
    private FavoriteTransformData injectShoplistToFavoriteTransformData(
            TopAdsData topAdsData, FavoriteTransformData favoriteTransformData) {

        List<ShopItem> shopItems = new ArrayList<>();
        ShopItemData data = topAdsData.getData();

        for (int i = 0; i < data.getList().size(); i++) {
            ShopItem shopItem = data.getList().get(i);
            shopItem.isFav = FAVORITE_ISFAVORITE_VALUE;
            shopItems.add(shopItem);
        }
        PagingHandler.PagingHandlerModel pagingHandlerModel =
                data.getPagingHandlerModel();

        favoriteTransformData.setPagingHandlerModel(pagingHandlerModel);
        favoriteTransformData.setShopItems(shopItems);// set shop item
        return favoriteTransformData;
    }
}
