package com.tokopedia.checkout.view.di.module;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.checkout.domain.usecase.CancelAutoApplyCouponUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.ResetCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.ResetCartUseCase;
import com.tokopedia.checkout.domain.usecase.UpdateCartUseCase;
import com.tokopedia.checkout.view.adapter.CartListAdapter;
import com.tokopedia.checkout.view.di.scope.CartListScope;
import com.tokopedia.checkout.view.view.cartlist.CartItemDecoration;
import com.tokopedia.checkout.view.view.cartlist.CartListPresenter;
import com.tokopedia.checkout.view.view.cartlist.ICartListPresenter;
import com.tokopedia.transactiondata.utils.CartApiRequestParamGenerator;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

import static org.mockito.Mockito.mock;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

@Module(includes = {ConverterDataModule.class, TestTrackingAnalyticsModule.class})
public class TestCartListModule {
    private final CartListAdapter.ActionListener cartListActionListener;
    private GetCartListUseCase getCartListUseCase;
    private DeleteCartListUseCase deleteCartListUseCase;
    private UpdateCartUseCase updateCartUseCase;
    private ResetCartGetCartListUseCase resetCartGetCartListUseCase;
    private ResetCartUseCase resetCartUseCase;
    private CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase;
    private CartApiRequestParamGenerator cartApiRequestParamGenerator;
    private CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase;

    public TestCartListModule(CartListAdapter.ActionListener cartFragment) {
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
                                                 DeleteCartListUseCase deleteCartListUseCase,
                                                 UpdateCartUseCase updateCartUseCase,
                                                 ResetCartGetCartListUseCase resetCartGetCartListUseCase,
                                                 CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase,
                                                 CompositeSubscription compositeSubscription,
                                                 CartApiRequestParamGenerator cartApiRequestParamGenerator,
                                                 CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase) {

        return new CartListPresenter(
                getGetCartListUseCase(),
                deleteCartListUseCase, updateCartUseCase,
                resetCartGetCartListUseCase,
                checkPromoCodeCartListUseCase, compositeSubscription,
                cartApiRequestParamGenerator, cancelAutoApplyCouponUseCase
        );
    }

    public GetCartListUseCase getGetCartListUseCase() {
        return getCartListUseCase = (getCartListUseCase == null) ? (getCartListUseCase = mock(GetCartListUseCase.class)) : getCartListUseCase;
    }

    @Provides
    @CartListScope
    RecyclerView.ItemDecoration provideCartItemDecoration(Context context) {
        return new CartItemDecoration();
    }

    @Provides
    @CartListScope
    CartListAdapter provideCartListAdapter() {
        return new CartListAdapter(cartListActionListener);
    }
}
