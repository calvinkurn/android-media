package com.tokopedia.checkout.view.di.component;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.checkout.data.repository.AddressRepository;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.transactiondata.repository.ITopPayRepository;
import com.tokopedia.logisticdata.data.repository.RatesRepository;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.checkout.domain.mapper.ICheckoutMapper;
import com.tokopedia.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.checkout.domain.mapper.ITopPayMapper;
import com.tokopedia.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.checkout.view.di.module.CartUseCaseModule;
import com.tokopedia.checkout.view.di.module.CheckoutRouterModule;
import com.tokopedia.checkout.view.di.module.CheckoutUseCaseModule;
import com.tokopedia.checkout.view.di.module.DataMapperModule;
import com.tokopedia.checkout.view.di.module.DataModule;
import com.tokopedia.checkout.view.di.module.ShipmentUseCaseModule;
import com.tokopedia.checkout.view.di.scope.CartScope;

import dagger.Component;

/**
 * @author anggaprasetiyo on 24/04/18.
 */
@CartScope
@Component(
        modules =
                {
                        CheckoutRouterModule.class,
                        DataMapperModule.class,
                        DataModule.class,
                        CartUseCaseModule.class,
                        CheckoutUseCaseModule.class,
                        ShipmentUseCaseModule.class
                },
        dependencies = BaseAppComponent.class
)
public interface CartComponent {
    ICartRepository cartRepository();

    ITopPayRepository topPayRepository();

    RatesRepository ratesRepository();

    AddressRepository addressRepository();

    ICartMapper cartMapper();

    IVoucherCouponMapper voucherCouponMapper();

    ICheckoutMapper checkoutMapper();

    IShipmentMapper shipmentMapper();

    ITopPayMapper topPayMapper();

    UserSession userSession();

    void inject(CartComponentInjector cartApiServiceComponentInjector);
}
