package com.tokopedia.checkout.view.di.module;

import com.tokopedia.checkout.data.repository.ICartRepository;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.checkout.domain.usecase.CartListInteractor;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormUseCase;
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
    ICartListInteractor provideICartListInteractor(CompositeSubscription compositeSubscription,
                                                   ICartRepository cartRepository,
                                                   ICartMapper cartMapper,
                                                   IShipmentMapper shipmentMapper,
                                                   IVoucherCouponMapper voucherCouponMapper) {
        return new CartListInteractor(compositeSubscription, cartRepository, cartMapper, shipmentMapper, voucherCouponMapper);
    }

    @MultipleAddressShipmentScope
    @Provides
    IMultipleAddressShipmentPresenter providePresenter(CompositeSubscription compositeSubscription,
                                                       GetShipmentAddressFormUseCase getShipmentAddressFormUseCase,
                                                       CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase) {
        return new MultipleAddressShipmentPresenter(view, compositeSubscription,
                getShipmentAddressFormUseCase, checkPromoCodeCartListUseCase);
    }

}
