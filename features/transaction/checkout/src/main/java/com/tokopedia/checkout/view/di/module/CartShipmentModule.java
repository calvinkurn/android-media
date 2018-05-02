package com.tokopedia.checkout.view.di.module;

import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartShipmentUseCase;
import com.tokopedia.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.checkout.domain.usecase.GetThanksToppayUseCase;
import com.tokopedia.checkout.view.di.scope.CartShipmentActivityScope;
import com.tokopedia.checkout.view.view.shipmentform.CartShipmentActivity;
import com.tokopedia.checkout.view.view.shipmentform.CartShipmentPresenter;
import com.tokopedia.checkout.view.view.shipmentform.ICartShipmentActivity;
import com.tokopedia.checkout.view.view.shipmentform.ICartShipmentPresenter;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

@Module(includes = {ConverterDataModule.class})
public class CartShipmentModule {

    private final ICartShipmentActivity viewListener;

    public CartShipmentModule(CartShipmentActivity cartShipmentActivity) {
        this.viewListener = cartShipmentActivity;
    }

    @Provides
    @CartShipmentActivityScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @CartShipmentActivityScope
    ICartShipmentPresenter provideICartShipmentPresenter(CheckoutUseCase checkoutUseCase,
                                                         GetThanksToppayUseCase getThanksToppayUseCase,
                                                         CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase,
                                                         CompositeSubscription compositeSubscription) {
        return new CartShipmentPresenter(compositeSubscription,
                checkoutUseCase, getThanksToppayUseCase,
                checkPromoCodeCartShipmentUseCase, viewListener);
    }

}
