package com.tokopedia.purchase_platform.common.di.module;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.purchase_platform.common.di.scope.CartListScope;
import com.tokopedia.purchase_platform.common.utils.CartApiRequestParamGenerator;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.DeleteCartListUseCase;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.GetCartListUseCase;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.ResetCartGetCartListUseCase;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.UpdateCartUseCase;
import com.tokopedia.purchase_platform.features.checkout.view.adapter.CartListAdapter;
import com.tokopedia.purchase_platform.features.checkout.view.view.cartlist.CartItemDecoration;
import com.tokopedia.purchase_platform.features.checkout.view.view.cartlist.CartListPresenter;
import com.tokopedia.purchase_platform.features.checkout.view.view.cartlist.ICartListPresenter;

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
    private CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase;
    private CartApiRequestParamGenerator cartApiRequestParamGenerator;

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
                                                 CartApiRequestParamGenerator cartApiRequestParamGenerator) {

        return new CartListPresenter(
                getGetCartListUseCase(),
                deleteCartListUseCase, updateCartUseCase,
                resetCartGetCartListUseCase,
                checkPromoCodeCartListUseCase, compositeSubscription,
                cartApiRequestParamGenerator
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
