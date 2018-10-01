package com.tokopedia.checkout.view.feature.emptycart;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.checkout.domain.datamodel.recentview.RecentView;
import com.tokopedia.checkout.domain.usecase.CancelAutoApplyCouponUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetRecentViewUseCase;
import com.tokopedia.checkout.view.feature.emptycart.subscriber.CancelAutoApplySubscriber;
import com.tokopedia.checkout.view.feature.emptycart.subscriber.GetCartListSubscriber;
import com.tokopedia.checkout.view.feature.emptycart.subscriber.GetRecentViewSubscriber;
import com.tokopedia.checkout.view.feature.emptycart.subscriber.GetWishlistSubscriber;
import com.tokopedia.checkout.view.feature.emptycart.viewmodel.RecentViewViewModel;
import com.tokopedia.checkout.view.feature.emptycart.viewmodel.WishlistViewModel;
import com.tokopedia.transactiondata.utils.CartApiRequestParamGenerator;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist;
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Irfan Khoirul on 14/09/18.
 */

public class EmptyCartPresenter extends BaseDaggerPresenter<EmptyCartContract.View>
        implements EmptyCartContract.Presenter {

    private static final int LIST_SIZE = 2;

    private final GetCartListUseCase getCartListUseCase;
    private final GetWishlistUseCase getWishlistUseCase;
    private final GetRecentViewUseCase getRecentViewUseCase;
    private final CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase;
    private final CartApiRequestParamGenerator cartApiRequestParamGenerator;
    private final CompositeSubscription compositeSubscription;
    private List<WishlistViewModel> wishlistViewModels = new ArrayList<>();
    private List<RecentViewViewModel> recentViewViewModels = new ArrayList<>();

    @Inject
    public EmptyCartPresenter(GetCartListUseCase getCartListUseCase,
                              GetWishlistUseCase getWishlistUseCase,
                              GetRecentViewUseCase getRecentViewUseCase,
                              CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase,
                              CartApiRequestParamGenerator cartApiRequestParamGenerator,
                              CompositeSubscription compositeSubscription) {
        this.getCartListUseCase = getCartListUseCase;
        this.getWishlistUseCase = getWishlistUseCase;
        this.getRecentViewUseCase = getRecentViewUseCase;
        this.cancelAutoApplyCouponUseCase = cancelAutoApplyCouponUseCase;
        this.cartApiRequestParamGenerator = cartApiRequestParamGenerator;
        this.compositeSubscription = compositeSubscription;
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeSubscription.unsubscribe();
        getWishlistUseCase.unsubscribe();
        getRecentViewUseCase.unsubscribe();
    }

    @Override
    public void processInitialGetCartData() {
        getView().showLoading();
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(
                GetCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING,
                getView().getGeneratedAuthParamNetwork(cartApiRequestParamGenerator.generateParamMapGetCartList(null))
        );
        compositeSubscription.add(getCartListUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new GetCartListSubscriber(getView()))
        );
    }

    @Override
    public void processGetWishlistData() {
        getWishlistUseCase.createObservable(new GetWishlistSubscriber(getView(), this));
    }

    @Override
    public void processGetRecentViewData(int userId) {
        getRecentViewUseCase.createObservable(userId, new GetRecentViewSubscriber(getView(), this));
    }

    @Override
    public void processCancelAutoApply() {
        compositeSubscription.add(cancelAutoApplyCouponUseCase.createObservable(RequestParams.create())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new CancelAutoApplySubscriber(getView()))
        );

    }

    @Override
    public void setWishListViewModels(List<Wishlist> wishLists) {
        wishlistViewModels.clear();
        for (int i = 0; i < wishLists.size(); i++) {
            if (i < LIST_SIZE) {
                WishlistViewModel wishlistViewModel = new WishlistViewModel();
                wishlistViewModel.setWishlist(wishLists.get(i));
                wishlistViewModels.add(wishlistViewModel);
            }
        }
    }

    @Override
    public List<WishlistViewModel> getWishlistViewModels() {
        return wishlistViewModels;
    }

    @Override
    public void setRecentViewListModels(List<RecentView> recentViewList) {
        recentViewViewModels.clear();
        for (int i = 0; i < recentViewList.size(); i++) {
            if (i < LIST_SIZE) {
                RecentViewViewModel recentViewViewModel = new RecentViewViewModel();
                recentViewViewModel.setRecentView(recentViewList.get(i));
                recentViewViewModels.add(recentViewViewModel);
            }
        }
    }

    @Override
    public List<RecentViewViewModel> getRecentViewListModels() {
        return recentViewViewModels;
    }
}
