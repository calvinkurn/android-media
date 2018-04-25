package com.tokopedia.checkout.view.di.module;

import android.support.v7.widget.RecyclerView;

import com.tokopedia.checkout.data.repository.ICartRepository;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.checkout.domain.mapper.IMapperUtil;
import com.tokopedia.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.checkout.domain.mapper.ShipmentMapper;
import com.tokopedia.checkout.domain.usecase.CartListInteractor;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.ICartListInteractor;
import com.tokopedia.checkout.view.adapter.CartListAdapter;
import com.tokopedia.checkout.view.di.scope.CartListScope;
import com.tokopedia.checkout.view.view.cartlist.CartFragment;
import com.tokopedia.checkout.view.view.cartlist.CartItemDecoration;
import com.tokopedia.checkout.view.view.cartlist.CartListPresenter;
import com.tokopedia.checkout.view.view.cartlist.ICartListPresenter;
import com.tokopedia.checkout.view.view.cartlist.ICartListView;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

@Module(includes = {ConverterDataModule.class})
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
    IShipmentMapper provideIShipmentMapper(IMapperUtil mapperUtil) {
        return new ShipmentMapper(mapperUtil);
    }

    @Provides
    @CartListScope
    ICartListInteractor provideICartListInteractor(CompositeSubscription compositeSubscription,
                                                   ICartRepository cartRepository,
                                                   ICartMapper cartMapper,
                                                   IShipmentMapper shipmentMapper,
                                                   IVoucherCouponMapper voucherCouponMapper) {
        return new CartListInteractor(compositeSubscription, cartRepository, cartMapper, shipmentMapper, voucherCouponMapper);
    }

    @Provides
    @CartListScope
    ICartListPresenter provideICartListPresenter(ICartRepository cartRepository,
                                                 ICartListInteractor cartListInteractor,
                                                 GetCartListUseCase getCartListUseCase,
                                                 CompositeSubscription compositeSubscription) {
        return new CartListPresenter(cartListView, cartListInteractor, getCartListUseCase, compositeSubscription);
    }

    @Provides
    @CartListScope
    RecyclerView.ItemDecoration provideCartItemDecoration() {
        return new CartItemDecoration(40, false, 0);
    }


    @Provides
    @CartListScope
    CartListAdapter provideCartListAdapter() {
        return new CartListAdapter(cartListActionListener);
    }


}
