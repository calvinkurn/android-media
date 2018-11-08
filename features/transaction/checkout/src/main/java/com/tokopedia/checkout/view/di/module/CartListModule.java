package com.tokopedia.checkout.view.di.module;

import android.support.v7.widget.RecyclerView;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.checkout.domain.usecase.CancelAutoApplyCouponUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.ResetCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.UpdateAndReloadCartUseCase;
import com.tokopedia.checkout.domain.usecase.UpdateCartUseCase;
import com.tokopedia.checkout.view.di.scope.CartListScope;
import com.tokopedia.checkout.view.feature.cartlist.CartFragment;
import com.tokopedia.checkout.view.feature.cartlist.CartItemDecoration;
import com.tokopedia.checkout.view.feature.cartlist.CartListPresenter;
import com.tokopedia.checkout.view.feature.cartlist.ICartListPresenter;
import com.tokopedia.checkout.view.feature.cartlist.ICartListView;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartAdapter;
import com.tokopedia.checkout.view.feature.cartlist.adapter.CartItemAdapter;
import com.tokopedia.transactiondata.utils.CartApiRequestParamGenerator;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

@Module(includes = {ConverterDataModule.class, TrackingAnalyticsModule.class})
public class CartListModule {

    private final ICartListView cartListView;
    private final CartAdapter.ActionListener cartActionListener;
    private final CartItemAdapter.ActionListener cartItemActionListener;

    public CartListModule(CartFragment cartFragment) {
        this.cartListView = cartFragment;
        this.cartActionListener = cartFragment;
        this.cartItemActionListener = cartFragment;
    }

    @Provides
    @CartListScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @CartListScope
    AddWishListUseCase providesAddWishListUseCase() {
        return new AddWishListUseCase(cartListView.getActivity());
    }

    @Provides
    @CartListScope
    RemoveWishListUseCase providesTkpdRemoveWishListUseCase() {
        return new RemoveWishListUseCase(cartListView.getActivity());
    }

    @Provides
    @CartListScope
    ICartListPresenter provideICartListPresenter(GetCartListUseCase getCartListUseCase,
                                                 DeleteCartUseCase deleteCartUseCase,
                                                 DeleteCartGetCartListUseCase deleteCartGetCartListUseCase,
                                                 UpdateCartUseCase updateCartUseCase,
                                                 ResetCartGetCartListUseCase resetCartGetCartListUseCase,
                                                 CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase,
                                                 CompositeSubscription compositeSubscription,
                                                 CartApiRequestParamGenerator cartApiRequestParamGenerator,
                                                 CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase,
                                                 AddWishListUseCase addWishListUseCase,
                                                 RemoveWishListUseCase removeWishListUseCase,
                                                 UpdateAndReloadCartUseCase updateAndReloadCartUseCase) {
        return new CartListPresenter(
                cartListView, getCartListUseCase, deleteCartUseCase, deleteCartGetCartListUseCase,
                updateCartUseCase, resetCartGetCartListUseCase, checkPromoCodeCartListUseCase,
                compositeSubscription, cartApiRequestParamGenerator, cancelAutoApplyCouponUseCase,
                addWishListUseCase, removeWishListUseCase, updateAndReloadCartUseCase
        );
    }

    @Provides
    @CartListScope
    RecyclerView.ItemDecoration provideCartItemDecoration() {
        return new CartItemDecoration();
    }

    @Provides
    @CartListScope
    CartAdapter provideCartListAdapter(UserSession userSession) {
        return new CartAdapter(cartActionListener, cartItemActionListener, userSession);
    }

}
