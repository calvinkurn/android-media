package com.tokopedia.checkout.view.di.module;

import com.tokopedia.checkout.data.repository.ICartRepository;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.checkout.domain.mapper.IMapperUtil;
import com.tokopedia.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.checkout.domain.mapper.ShipmentMapper;
import com.tokopedia.checkout.domain.usecase.CartListInteractor;
import com.tokopedia.checkout.domain.usecase.ICartListInteractor;
import com.tokopedia.checkout.view.di.scope.MultipleAddressShipmentScope;
import com.tokopedia.checkout.view.view.shipmentform.IMultipleAddressShipmentPresenter;
import com.tokopedia.checkout.view.view.shipmentform.IMultipleAddressShipmentView;
import com.tokopedia.checkout.view.view.shipmentform.MultipleAddressShipmentPresenter;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

@Module
public class MultipleAddressShipmentModule {

    private IMultipleAddressShipmentView view;

    public MultipleAddressShipmentModule(IMultipleAddressShipmentView view) {
        this.view = view;
    }

    @Provides
    @MultipleAddressShipmentScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @MultipleAddressShipmentScope
    IShipmentMapper provideIShipmentMapper(IMapperUtil mapperUtil) {
        return new ShipmentMapper(mapperUtil);
    }

    @Provides
    @MultipleAddressShipmentScope
    ICartListInteractor provideICartListInteractor(CompositeSubscription compositeSubscription,
                                                   ICartRepository cartRepository,
                                                   ICartMapper cartMapper,
                                                   IShipmentMapper shipmentMapper,
                                                   IVoucherCouponMapper voucherCouponMapper) {
        return new CartListInteractor(compositeSubscription, cartRepository, cartMapper, shipmentMapper, voucherCouponMapper);
    }

    @MultipleAddressShipmentScope
    @Provides
    IMultipleAddressShipmentPresenter providePresenter(ICartListInteractor cartListInteractor) {
        return new MultipleAddressShipmentPresenter(view, cartListInteractor);
    }

}
