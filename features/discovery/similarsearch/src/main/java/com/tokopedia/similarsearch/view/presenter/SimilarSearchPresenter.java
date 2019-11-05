package com.tokopedia.similarsearch.view.presenter;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.discovery.common.model.WishlistTrackingModel;
import com.tokopedia.similarsearch.analytics.SimilarSearchTracking;
import com.tokopedia.similarsearch.domain.GetSimilarProductUseCase;
import com.tokopedia.similarsearch.model.ProductsItem;
import com.tokopedia.similarsearch.view.SimilarSearchContract;
import com.tokopedia.similarsearch.view.SimilarSearchFragment;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class SimilarSearchPresenter extends BaseDaggerPresenter<SimilarSearchContract.View> implements SimilarSearchContract.Presenter{


    AddWishListUseCase addWishlistActionUseCase;
    RemoveWishListUseCase removeWishlistActionUseCase;
    GetSimilarProductUseCase getSimilarProductUseCase;

    private WishListActionListener wishListListener;
    private UserSessionInterface userSession;


    @Inject
    public SimilarSearchPresenter(
            AddWishListUseCase addWishlistActionUseCase,
            RemoveWishListUseCase removeWishlistActionUseCase,
            GetSimilarProductUseCase getSimilarProductUseCase,
            UserSessionInterface userSession
    ) {
        this.addWishlistActionUseCase = addWishlistActionUseCase;
        this.removeWishlistActionUseCase = removeWishlistActionUseCase;
        this.getSimilarProductUseCase = getSimilarProductUseCase;
        this.userSession = userSession;
    }

    @Override
    public void attachView(SimilarSearchContract.View view) {
        super.attachView(view);
        List<ProductsItem> productList = new ArrayList<>();
        productList.add(new ProductsItem());
        productList.add(new ProductsItem());
        productList.add(new ProductsItem());
        productList.add(new ProductsItem());
        productList.add(new ProductsItem());
        getView().setProductList(productList);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("product_id", getView().getProductID());
        requestParams.putString("params", "page=0&page_size=100&device=android");
        requestParams.putString("userId",getView().getUserId());

        getSimilarProductUseCase.execute(requestParams, new Subscriber<List<ProductsItem>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(getView() != null && getView().getAppContext() != null) {
                    SimilarSearchTracking.eventUserSeeNoSimilarProduct(getView().getProductID(), getView().getScreenName());
                    getView().setEmptyLayoutVisible();
                    getView().setContentLayoutGone();
                }
            }

            @Override
            public void onNext(List<ProductsItem> productsItems) {
                if(getView() != null && getView().getAppContext() != null) {
                    if (productsItems != null && productsItems.size() > 0) {
                        getView().setProductList(productsItems);
                    } else {
                        getView().setEmptyLayoutVisible();
                        SimilarSearchTracking.eventUserSeeNoSimilarProduct(getView().getProductID(), getView().getScreenName());
                        getView().setContentLayoutGone();
                    }
                }
            }
        });
    }

    @Override
    public void handleWishlistButtonClicked(ProductsItem productItem, int adapterPosition) {
        if (getView().isUserHasLogin()) {
            getView().disableWishlistButton(adapterPosition);
            if (productItem.isWishListed()) {
                removeWishlist(productItem);
            } else {
                addWishlist(productItem);
            }
        } else {
            trackWishlistNonLogin(productItem);
            launchLoginActivity(String.valueOf(productItem.getId()));
        }
    }

    private void trackWishlistNonLogin(ProductsItem productsItem) {
        WishlistTrackingModel wishlistTrackingModel = createWishlistTrackingModel(productsItem);
        wishlistTrackingModel.setAddWishlist(!productsItem.isWishListed());

        SimilarSearchTracking.eventSuccessWishlistSimilarProduct(wishlistTrackingModel);
    }

    private void launchLoginActivity(String productId) {
        Bundle extras = new Bundle();
        extras.putString("product_id", productId);
        getView().launchLoginActivity(extras);
    }

    private void addWishlist(ProductsItem productsItem) {
        trackSuccessAddWishlist(productsItem);
        addWishlistActionUseCase.createObservable(String.valueOf(productsItem.getId()), getView().getUserId(), wishListListener);
    }

    private void trackSuccessAddWishlist(ProductsItem productsItem) {
        WishlistTrackingModel wishlistTrackingModel = createWishlistTrackingModel(productsItem);
        wishlistTrackingModel.setAddWishlist(true);

        SimilarSearchTracking.eventSuccessWishlistSimilarProduct(wishlistTrackingModel);
    }

    private WishlistTrackingModel createWishlistTrackingModel(ProductsItem productsItem) {
        WishlistTrackingModel wishlistTrackingModel = new WishlistTrackingModel();

        wishlistTrackingModel.setProductId(String.valueOf(productsItem.getId()));
        wishlistTrackingModel.setTopAds(false);
        wishlistTrackingModel.setKeyword(getView().getQueryKey());
        wishlistTrackingModel.setUserLoggedIn(userSession.isLoggedIn());

        return wishlistTrackingModel;
    }

    private void removeWishlist(ProductsItem productsItem) {
        trackSuccessRemoveWishlist(productsItem);
        removeWishlistActionUseCase.createObservable(String.valueOf(productsItem.getId()), getView().getUserId(), wishListListener);
    }

    private void trackSuccessRemoveWishlist(ProductsItem productsItem) {
        WishlistTrackingModel wishlistTrackingModel = createWishlistTrackingModel(productsItem);
        wishlistTrackingModel.setAddWishlist(false);

        SimilarSearchTracking.eventSuccessWishlistSimilarProduct(wishlistTrackingModel);
    }

    public void setWishListListener(SimilarSearchFragment wishListListener) {
        this.wishListListener = wishListListener;
    }
}
