package com.tokopedia.tkpd.home.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.CacheDuration;
import com.tokopedia.core.network.apiservices.mojito.MojitoAuthService;
import com.tokopedia.core.network.apiservices.mojito.MojitoService;
import com.tokopedia.core.network.entity.wishlist.Pagination;
import com.tokopedia.core.network.entity.wishlist.Wishlist;
import com.tokopedia.core.network.entity.wishlist.WishlistData;
import com.tokopedia.core.network.entity.wishlist.WishlistPaging;
import com.tokopedia.core.router.transactionmodule.TransactionAddToCartRouter;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.interactor.CacheHomeInteractor;
import com.tokopedia.tkpd.home.interactor.CacheHomeInteractorImpl;
import com.tokopedia.tkpd.home.service.FavoritePart1Service;
import com.tokopedia.tkpd.home.wishlist.WishlistViewModelMapper;
import com.tokopedia.tkpd.home.wishlist.domain.SearchWishlistUsecase;
import com.tokopedia.tkpd.home.wishlist.domain.model.DataWishlist;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
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
    WishListView wishListView;

    List<RecyclerViewItem> data = new ArrayList<>();

    WishlistPaging mPaging;

    FavoritePart1Service wishlist;

    CompositeSubscription compositeSubscription;

    CacheHomeInteractor cache;

    MojitoService mojitoService;

    MojitoAuthService mojitoAuthService;

    SearchWishlistUsecase searchWishlistUsecase;

    List<Wishlist> dataWishlist = new ArrayList<>();
    RequestParams params = RequestParams.create();

    public WishListImpl(WishListView wishListView, SearchWishlistUsecase searchWishlistUsecase) {
        this.wishListView = wishListView;
        this.searchWishlistUsecase = searchWishlistUsecase;
        mPaging = new WishlistPaging();
        wishlist = new FavoritePart1Service();
        cache = new CacheHomeInteractorImpl();
        mojitoService = new MojitoService();
        compositeSubscription = new CompositeSubscription();
        mojitoAuthService = new MojitoAuthService();

    }

    private com.tokopedia.core.base.common.service.MojitoService initNewMojitoService() {
        return null;
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
    public void fetchSavedsInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //mPaging.onCreate(savedInstanceState);
            data = Parcels.unwrap(savedInstanceState.getParcelable(WISHLIST_MODEL));
            dataWishlist = Parcels.unwrap(savedInstanceState.getParcelable(WISHLIST_ENTITY));
            if (mPaging != null) {
                Pagination pagination = savedInstanceState.getParcelable(PAGINATION_MODEL);
                mPaging.setPagination(pagination);
            }
        }
    }

    @Override
    public void initAnalyticsHandler(Context context) {
        if (context != null) {

        }

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (context != null)
                    fetchDataFromInternet(context);
            }
        }, 200);

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
        params.putInt(SearchWishlistUsecase.KEY_PAGE, mPaging.getPage());
        searchWishlistUsecase.execute(params, new SearchWishlistSubscriber());
    }

    @Override
    public void saveDataBeforeRotate(Bundle saved) {
        //mPaging.onSavedInstanceState(saved);
        saved.putParcelable(WISHLIST_MODEL, Parcels.wrap(data));
        saved.putParcelable(WISHLIST_ENTITY, Parcels.wrap(dataWishlist));
        if (mPaging != null) {
            saved.putParcelable(PAGINATION_MODEL, mPaging.getPagination());
        }
    }

    @Override
    public boolean isAfterRotation() {
        return data != null && data.size() > 0;
    }

    @Override
    public List<RecyclerViewItem> getData() {
        return data;
    }

    @Override
    public void fetchDataFromInternet(final Context context) {
        Observable<Response<WishlistData>> observable = mojitoAuthService.getApi().getWishlist(
                SessionHandler.getLoginID(context),
                10,
                mPaging.getPage()
        );

        Subscriber<Response<WishlistData>> subscriber = new Subscriber<Response<WishlistData>>() {
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
            public void onNext(Response<WishlistData> response) {
                setData(response.body());
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));

    }

    @Override
    public void setData(com.tokopedia.core.network.entity.wishlist.WishlistData wishlistData) {
        if (mPaging.getPage() == 1) {
            data.clear();
            if (wishlistData.getWishlist().size() == 0)
                wishListView.setSearchNotFound();
        }
        wishListView.displayPull(false);
        dataWishlist.addAll(wishlistData.getWishlist());
        data.addAll(convertToProductItemList(wishlistData.getWishlist()));
        mPaging.setPagination(wishlistData.getPaging());

        if (mPaging.CheckNextPage()) {
            wishListView.displayLoadMore(true);
        } else {
            wishListView.displayLoadMore(false);
        }
        wishListView.setPullEnabled(true);

        wishListView.loadDataChange();
        wishListView.displayContentList(true);
        wishListView.displayLoading(false);
        wishListView.clearSearchView();
    }

    @Override
    public void deleteWishlist(final Context context, final String productId, final int position) {
        wishListView.showProgressDialog();
        Observable<Response<Void>> observable = mojitoAuthService.getApi()
                .deleteWishlist(productId, SessionHandler.getLoginID(context));

        Subscriber<Response<Void>> subscriber = new Subscriber<Response<Void>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                wishListView.dismissProgressDialog();
                wishListView.displayErrorNetwork(true);
            }

            @Override
            public void onNext(Response<Void> voidResponse) {
                sendMoEngageTracker(productId);
                onFinishedDeleteWishlist(position);
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void addToCart(Context context, String productId) {
        for (int i = 0; i < dataWishlist.size(); i++) {
            if (productId.equals(dataWishlist.get(i).getId())) {
                Wishlist dataDetail = dataWishlist.get(i);

                ProductCartPass pass = ProductCartPass.Builder.aProductCartPass()
                        .setImageUri(dataDetail.getImageUrl())
                        .setMinOrder(dataDetail.getMinimumOrder())
                        .setProductId(dataDetail.getId())
                        .setProductName(dataDetail.getName())
                        .setShopId(dataDetail.getShop().getId())
                        .setPrice(dataDetail.getPriceFmt()).build();
                context.startActivity(
                        TransactionAddToCartRouter.createInstanceAddToCartActivity(context, pass)
                );

                return;
            }
        }
    }

    @Override
    public boolean isLoadedFirstPage() {
        return mPaging.getPage() >= 1;
    }

    @Override
    public void searchWishlist(String query) {
        mPaging.resetPage();
        String userId = wishListView.getUserId();
        params.putString(SearchWishlistUsecase.KEY_USER_ID, userId);
        params.putString(SearchWishlistUsecase.KEY_QUERY, query);
        params.putInt(SearchWishlistUsecase.KEY_PAGE, mPaging.getPage());
        searchWishlistUsecase.execute(params, new SearchWishlistSubscriber());
    }

    @Override
    public void subscribe() {
        RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
    }

    @Override
    public void unSubscribe() {
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
        cache.unSubscribeObservable();
    }

    @Override
    public void fetchDataAfterClearSearch(Context context) {
        mPaging.resetPage();
        params = RequestParams.create();
        Observable<Response<WishlistData>> observable = mojitoAuthService.getApi().getWishlist(
                SessionHandler.getLoginID(context),
                10,
                mPaging.getPage()
        );

        Subscriber<Response<WishlistData>> subscriber = new Subscriber<Response<WishlistData>>() {
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
            public void onNext(Response<WishlistData> response) {
                WishlistData wishlistData = response.body();
                data.clear();
                dataWishlist.addAll(wishlistData.getWishlist());
                data.addAll(convertToProductItemList(wishlistData.getWishlist()));
                mPaging.setPagination(wishlistData.getPaging());
                wishListView.loadDataChange();
                wishListView.displayContentList(true);

                if (mPaging.CheckNextPage()) {
                    wishListView.displayLoadMore(true);
                } else {
                    wishListView.displayLoadMore(false);
                }
                wishListView.setPullEnabled(true);
                if(response.body().getWishlist().size() == 0){
                    wishListView.setEmptyState();
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void refreshDataOnSearch(CharSequence query) {
        mPaging.resetPage();
        searchWishlist(query.toString());
    }

    @Override
    public void onResume(Context context) {
        if (isAfterRotation()) {
            handleAfterRotation(context);
        } else {
            fetchDataFromCache(context);
        }
    }

    private void handleAfterRotation(Context context) {
        if (!isLoadedFirstPage()) {
            refreshData(context);
        } else {
            wishListView.displayLoadMore(mPaging.CheckNextPage());
            wishListView.displayContentList(true);
            wishListView.displayLoading(false);
        }
    }

    @Override
    public void fetchDataFromCache(final Context context) {
        fetchDataFromInternet(context);
       /* if(cache.getWishListCache()!=null){
            setData(cache.getWishListCache());
            sendToAppsflyer(context,cache.getWishListCache().receiveData());
        }else{
            wishListView.displayPull(true);
            fetchDataFromInternet(context);
        }*/

//        wishListView.displayPull(true);
//        new android.os.Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                fetchDataFromInternet(context);
//            }
//        }, 50);// 1_000
    }

    private void sendMoEngageTracker(String productId) {
        if(productId != null) {
            for (int i = 0; i < dataWishlist.size(); i++) {
                if (dataWishlist.get(i) != null) {
                    if (productId.equals(dataWishlist.get(i).getId())) {
                        TrackingUtils.sendMoEngageRemoveWishlist(dataWishlist.get(i));
                        break;
                    }
                }
            }
        }
    }

    public Boolean isNextPage(Pagination pagination) {
        return pagination != null;
    }

    public List<ProductItem> convertToProductItemList(List<Wishlist> wishlists) {
        List<ProductItem> products = new ArrayList<>();
        for (int i = 0; i < wishlists.size(); i++) {
            ProductItem product = new ProductItem();
            product.setId(wishlists.get(i).getId());
            product.setImgUri(wishlists.get(i).getImageUrl());
            product.setIsNewGold(wishlists.get(i).getShop().getIsGoldMerchant() ? 1 : 0);
            product.setName(wishlists.get(i).getName());
            product.setPrice(wishlists.get(i).getPriceFmt());
            product.setShop(wishlists.get(i).getShop().getName());
            product.setShopId(Integer.parseInt(wishlists.get(i).getShop().getId()));
            product.setIsWishlist(true);
            product.setIsAvailable(wishlists.get(i).getIsAvailable());
            product.setWholesale(wishlists.get(i).getWholesale().size() > 0 ? "1" : "0");
            product.setPreorder(wishlists.get(i).getIsPreOrder() ? "1" : "0");
            product.setIsGold(wishlists.get(i).getShop().getIsGoldMerchant() ? "1" : "0");
            product.setLuckyShop(wishlists.get(i).getShop().getLuckyMerchant());
            product.setBadges(wishlists.get(i).getBadges());
            product.setLabels(wishlists.get(i).getLabels());
            product.setShopLocation(wishlists.get(i).getShop().getLocation());
            product.setOfficial(wishlists.get(i).getShop().getOfficial());
            products.add(product);
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
                        params.getString(SearchWishlistUsecase.KEY_QUERY, ""), position);
            }
        }, CacheDuration.onSecond(5));
    }

    private class SearchWishlistSubscriber extends Subscriber<DataWishlist> {


        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: ", e);
            if (mPaging.getPage() == 1 && wishListView.isPullToRefresh()) {
                wishListView.displayPull(false);
            }
            wishListView.displayErrorNetwork(false);
        }

        @Override
        public void onNext(DataWishlist wishlist) {
            WishlistData wishlistData = convertToDataWishlistViewModel(wishlist);
            wishListView.displayPull(false);
            if (mPaging.getPage() == 1 || wishlist.getWishlists().size() == 0) {
                data.clear();
                dataWishlist.clear();
                if (wishlist.getWishlists().size() == 0)
                    wishListView.setSearchNotFound();
            }
            dataWishlist.addAll(wishlistData.getWishlist());
            data.addAll(convertToProductItemList(wishlistData.getWishlist()));
            mPaging.setPagination(wishlistData.getPaging());
            if (mPaging.CheckNextPage()) {
                wishListView.displayLoadMore(true);
            } else {
                wishListView.displayLoadMore(false);
            }
            wishListView.setPullEnabled(true);
            wishListView.loadDataChange();
            wishListView.displayContentList(true);
            wishListView.displayLoading(false);
        }

        private WishlistData convertToDataWishlistViewModel(DataWishlist dataWishlist) {
            return new WishlistViewModelMapper(dataWishlist).getWishlistData();
        }
    }
}
