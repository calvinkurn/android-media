package com.tokopedia.topads.sdk.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.interactor.MerlinRecomendationUseCase;
import com.tokopedia.topads.sdk.domain.interactor.OpenTopAdsUseCase;
import com.tokopedia.topads.sdk.domain.interactor.PreferedCategoryUseCase;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsUseCase;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.domain.model.WishlistModel;
import com.tokopedia.topads.sdk.listener.PreferedCategoryListener;
import com.tokopedia.topads.sdk.utils.CacheHandler;
import com.tokopedia.topads.sdk.view.AdsView;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class TopAdsPresenter implements AdsPresenter, PreferedCategoryListener {

    private static final String TAG = "TopAdsPresenter";
    private AdsView adsView;
    private PreferedCategoryUseCase preferedCategoryUseCase;
    private MerlinRecomendationUseCase merlinRecomendationUseCase;
    private Config config;

    @Inject
    TopAdsParams adsParams;
    @Inject
    OpenTopAdsUseCase openTopAdsUseCase;
    @Inject
    TopAdsUseCase adsUseCase;
    @Inject
    TopAdsWishlishedUseCase wishlishedUseCase;
    @Inject
    AddWishListUseCase addWishListUseCase;
    @Inject
    RemoveWishListUseCase removeWishListUseCase;
    @Inject
    UserSession userSession;

    public TopAdsPresenter(Context context) {
        this.preferedCategoryUseCase = new PreferedCategoryUseCase(context, this);
        this.merlinRecomendationUseCase = new MerlinRecomendationUseCase(context, this);
    }

    @Override
    public void onSuccessLoadPrefered(int randomCategoryId) {
        adsParams.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, String.valueOf(randomCategoryId));
        adsParams.getParam().put(TopAdsParams.KEY_USER_ID, config.getUserId());
        adsUseCase.setConfig(config);
        adsUseCase.execute(adsParams, adsView);
    }

    @Override
    public void onErrorLoadPrefered() {

    }

    public void setConfig(Config config) {
        this.config = config;
        this.adsParams = config.getTopAdsParams();
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public void setMaxItems(int items) {
        adsParams.getParam().put(TopAdsParams.KEY_ITEM, String.valueOf(items));
    }

    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        adsUseCase.setDisplayMode(displayMode);
        config.setDisplayMode(displayMode);
    }

    public DisplayMode getDisplayMode() {
        return adsUseCase.getDisplayMode();
    }

    @Override
    public void setEndpoinParam(String ep) {
        switch (ep) {
            case "0":
                adsParams.getParam().remove(TopAdsParams.KEY_EP);
                break;
            case "1":
                adsParams.getParam().put(TopAdsParams.KEY_EP, "product");
                break;
            case "2":
                adsParams.getParam().put(TopAdsParams.KEY_EP, "shop");
                break;
        }
    }

    @Override
    public void loadTopAds() {
        Log.d(TAG, "Load TopAds");
        replaceSourceParams();
        if (config.getEndpoint() != null) {
            setEndpoinParam(config.getEndpoint().getDescription());
        }
        if (config.isWithPreferedCategory()) {
            getPreferedCategory();
        } else if (config.isWithMerlinCategory()) {
            getMerlinCategory();
        } else {
            adsParams.getParam().put(TopAdsParams.KEY_USER_ID, config.getUserId());
            adsUseCase.setConfig(config);
            adsUseCase.execute(adsParams, adsView);
        }
    }

    private void replaceSourceParams() {
        if (adsParams.getParam().containsKey(TopAdsParams.KEY_SRC)) {
            if (adsParams.getParam().get(TopAdsParams.KEY_SRC).contains(TopAdsParams.DEFAULT_KEY_SRC)) {
                adsParams.getParam().put(TopAdsParams.KEY_SRC, TopAdsParams.DEFAULT_KEY_SRC);
            }
            if (adsParams.getParam().get(TopAdsParams.KEY_SRC).contains("hot_product")) {
                adsParams.getParam().put(TopAdsParams.KEY_SRC, "hotlist");
            }
        }
    }


    @Override
    public void openProductTopAds(int position, String click_url, final Product product) {
        openTopAdsUseCase.execute(click_url, adsView);
        adsView.notifyProductClickListener(position, product);
    }

    @Override
    public void openShopTopAds(int position, String click_url, final Shop shop) {
        openTopAdsUseCase.execute(click_url, adsView);
        adsView.notifyShopClickListener(position, shop);
    }

    @Override
    public TopAdsParams getTopAdsParam() {
        return adsParams;
    }

    @Override
    public void setParams(TopAdsParams adsParams) {
        this.adsParams = adsParams;
    }

    @Override
    public void attachView(AdsView view) {
        this.adsView = view;
    }

    @Override
    public void detachView() {
        this.adsView = null;
        adsUseCase.unsubscribe();
        openTopAdsUseCase.unsubscribe();
        removeWishListUseCase.unsubscribe();
        addWishListUseCase.unsubscribe();
    }

    @Override
    public boolean isViewAttached() {
        return adsView != null;
    }

    @Override
    public void checkViewAttached() {
        if (!isViewAttached()) {
            throw new AdsViewNotAttachedException();
        }
    }

    public static class AdsViewNotAttachedException extends RuntimeException {
        public AdsViewNotAttachedException() {
            super("Please call Presenter.attachView() before " +
                    "requesting data to the presenter");
        }
    }

    @Override
    public void getPreferedCategory() {
        preferedCategoryUseCase.setConfig(config);
        preferedCategoryUseCase.execute(adsParams, adsView);
    }

    @Override
    public void getMerlinCategory() {
        merlinRecomendationUseCase.setConfig(config);
        merlinRecomendationUseCase.execute(adsParams, adsView);
    }

    @Override
    public void addWishlist(Data data) {
        if (userSession.isLoggedIn()) {

            RequestParams params = RequestParams.create();
            params.putString(TopAdsWishlishedUseCase.WISHSLIST_URL, data.getProductWishlistUrl());
            wishlishedUseCase.execute(params, new Subscriber<WishlistModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    if(isViewAttached())
                        adsView.showErrorAddWishlist();
                }

                @Override
                public void onNext(WishlistModel wishlistModel) {
                    if (wishlistModel.getData() != null && isViewAttached()) {
                        data.getProduct().setWishlist(wishlistModel.getData().isSuccess());
                        adsView.notifyAdapter();
                        adsView.showSuccessAddWishlist();
                    } else if (isViewAttached()) {
                        adsView.showErrorAddWishlist();
                    }
                }
            });
        } else if (isViewAttached()) {
            adsView.doLogin();
        }
    }

    public void removeWishlist(Data data) {
        if (userSession.isLoggedIn()) {
            removeWishListUseCase.createObservable(data.getProduct().getId(), userSession.getUserId(),
                    getWishlistActionListener(data));
        } else if (isViewAttached()) {
            adsView.doLogin();
        }
    }

    @NonNull
    private WishListActionListener getWishlistActionListener(Data data) {
        return new WishListActionListener() {
            @Override
            public void onErrorAddWishList(String errorMessage, String productId) {
                data.getProduct().setWishlist(false);
            }

            @Override
            public void onSuccessAddWishlist(String productId) {
                data.getProduct().setWishlist(true);
                if (isViewAttached())
                    adsView.notifyAdapter();
            }

            @Override
            public void onErrorRemoveWishlist(String errorMessage, String productId) {
                data.getProduct().setWishlist(true);
                if(isViewAttached())
                    adsView.showErrorRemoveWishlist();
            }

            @Override
            public void onSuccessRemoveWishlist(String productId) {
                data.getProduct().setWishlist(false);
                if (isViewAttached()) {
                    adsView.notifyAdapter();
                    adsView.showSuccessRemoveWishlist();
                }
            }

            @Override
            public String getString(int resId) {
                return adsView.getString(resId);
            }
        };
    }
}
