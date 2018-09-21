package com.tokopedia.checkout.view.feature.emptycart;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.usecase.CancelAutoApplyCouponUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.view.feature.emptycart.subscriber.CancelAutoApplySubscriber;
import com.tokopedia.checkout.view.feature.emptycart.subscriber.GetCartListSubscriber;
import com.tokopedia.checkout.view.feature.emptycart.subscriber.GetWishlistSubscriber;
import com.tokopedia.checkout.view.feature.emptycart.viewmodel.WishlistViewModel;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.transactiondata.utils.CartApiRequestParamGenerator;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist;
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Irfan Khoirul on 14/09/18.
 */

public class EmptyCartPresenter extends BaseDaggerPresenter<EmptyCartContract.View>
        implements EmptyCartContract.Presenter {

    private final GetCartListUseCase getCartListUseCase;
    private final GetWishlistUseCase getWishlistUseCase;
    private final CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase;
    private final CartApiRequestParamGenerator cartApiRequestParamGenerator;
    private final CompositeSubscription compositeSubscription;
    private List<WishlistViewModel> wishlistViewModels = new ArrayList<>();

    @Inject
    public EmptyCartPresenter(GetCartListUseCase getCartListUseCase,
                              GetWishlistUseCase getWishlistUseCase,
                              CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase,
                              CartApiRequestParamGenerator cartApiRequestParamGenerator,
                              CompositeSubscription compositeSubscription) {
        this.getCartListUseCase = getCartListUseCase;
        this.getWishlistUseCase = getWishlistUseCase;
        this.cancelAutoApplyCouponUseCase = cancelAutoApplyCouponUseCase;
        this.cartApiRequestParamGenerator = cartApiRequestParamGenerator;
        this.compositeSubscription = compositeSubscription;
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeSubscription.unsubscribe();
        getWishlistUseCase.unsubscribe();
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
    public void processGetRecentViewdata() {

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
    public ProductPass generateProductPassProductDetailPage(Wishlist wishlist) {
        return ProductPass.Builder.aProductPass()
                .setProductId(wishlist.getId())
                .setProductImage(wishlist.getImageUrl())
                .setProductName(wishlist.getName())
                .setProductPrice(wishlist.getPriceFmt())
                .build();
    }

    @Override
    public void setWishListViewModels(List<Wishlist> wishLists) {
        wishlistViewModels.clear();
        for (Wishlist wishlist : wishLists) {
            WishlistViewModel wishlistViewModel = new WishlistViewModel();
            wishlistViewModel.setWishlist(wishlist);
            wishlistViewModels.add(wishlistViewModel);
        }
    }

    @Override
    public List<WishlistViewModel> getWishlistViewModels() {
        return wishlistViewModels;
    }
}
