package com.tokopedia.checkout.view.di.module;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.usecase.CancelAutoApplyCouponUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.checkout.domain.usecase.ResetCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.ResetCartUseCase;
import com.tokopedia.checkout.domain.usecase.UpdateCartUseCase;
import com.tokopedia.checkout.view.adapter.CartListAdapter;
import com.tokopedia.checkout.view.di.scope.CartListScope;
import com.tokopedia.checkout.view.view.cartlist.CartFragment;
import com.tokopedia.checkout.view.view.cartlist.CartItemDecoration;
import com.tokopedia.checkout.view.view.cartlist.CartListPresenter;
import com.tokopedia.checkout.view.view.cartlist.ICartListPresenter;
import com.tokopedia.checkout.view.view.cartlist.ICartListView;
import com.tokopedia.transactiondata.utils.CartApiRequestParamGenerator;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

@Module(includes = {ConverterDataModule.class, TrackingAnalyticsModule.class})
public class CartListModule {

    private final ICartListView cartListView;
    private final CartListAdapter.ActionListener cartListActionListener;

    public CartListModule(CartFragment cartFragment) {
        this.cartListView = cartFragment;
        this.cartListActionListener = cartFragment;
    }

    @Provides
    @CartListScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @CartListScope
    ICartListPresenter provideICartListPresenter(GetCartListUseCase getCartListUseCase,
                                                 DeleteCartUseCase deleteCartUseCase,
                                                 DeleteCartGetCartListUseCase deleteCartGetCartListUseCase,
                                                 UpdateCartUseCase updateCartUseCase,
                                                 ResetCartGetCartListUseCase resetCartGetCartListUseCase,
                                                 ResetCartUseCase resetCartUseCase,
                                                 CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase,
                                                 CompositeSubscription compositeSubscription,
                                                 CartApiRequestParamGenerator cartApiRequestParamGenerator,
                                                 CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase) {
        return new CartListPresenter(
                cartListView, getCartListUseCase, deleteCartUseCase,
                deleteCartGetCartListUseCase, updateCartUseCase,
                resetCartGetCartListUseCase, resetCartUseCase,
                checkPromoCodeCartListUseCase, compositeSubscription,
                cartApiRequestParamGenerator, cancelAutoApplyCouponUseCase
        );
    }

    @Provides
    @CartListScope
    RecyclerView.ItemDecoration provideCartItemDecoration() {
        return new CartItemDecoration();
    }

    @Provides
    @CartListScope
    CartListAdapter provideCartListAdapter() {
        return new CartListAdapter(cartListActionListener);
    }

}
