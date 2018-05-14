package com.tokopedia.checkout.view.di.module;

import android.support.v7.widget.RecyclerView;

import com.tokopedia.checkout.domain.mapper.IMapperUtil;
import com.tokopedia.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.checkout.domain.mapper.ShipmentMapper;
import com.tokopedia.checkout.domain.usecase.DeleteCartUpdateCartUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartUseCase;
import com.tokopedia.checkout.view.adapter.CartRemoveProductAdapter;
import com.tokopedia.checkout.view.di.scope.CartRemoveProductScope;
import com.tokopedia.checkout.view.view.cartlist.CartItemDecoration;
import com.tokopedia.checkout.view.view.cartlist.CartRemoveProductFragment;
import com.tokopedia.checkout.view.view.cartlist.CartRemoveProductPresenter;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Aghny A. Putra on 31/01/18.
 */
@Module(includes = {ConverterDataModule.class, TrackingAnalyticsModule.class})
public class CartRemoveProductModule {

    private final CartRemoveProductAdapter.CartRemoveProductActionListener actionListener;

    public CartRemoveProductModule(CartRemoveProductFragment cartRemoveProductFragment) {
        actionListener = cartRemoveProductFragment;
    }

    @Provides
    @CartRemoveProductScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @CartRemoveProductScope
    IShipmentMapper provideIShipmentMapper(IMapperUtil mapperUtil) {
        return new ShipmentMapper(mapperUtil);
    }

    @Provides
    @CartRemoveProductScope
    RecyclerView.ItemDecoration provideCartItemDecoration() {
        return new CartItemDecoration(40, false, 0);
    }

    @Provides
    @CartRemoveProductScope
    CartRemoveProductPresenter provideCartRemoveProductPresenter(
            CompositeSubscription compositeSubscription,
            DeleteCartUseCase deleteCartUseCase,
            DeleteCartUpdateCartUseCase deleteCartUpdateCartUseCase
    ) {
        return new CartRemoveProductPresenter(
                compositeSubscription, deleteCartUseCase, deleteCartUpdateCartUseCase
        );
    }

    @Provides
    @CartRemoveProductScope
    CartRemoveProductAdapter provideCartRemoveProductAdapter() {
        return new CartRemoveProductAdapter(actionListener);
    }


}
