package com.tokopedia.purchase_platform.common.di.component;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.purchase_platform.checkout.data.AddressRepository;
import com.tokopedia.purchase_platform.checkout.subfeature.address_choice.data.repository.PeopleAddressRepository;
import com.tokopedia.purchase_platform.cart.domain.mapper.ICartMapper;
import com.tokopedia.purchase_platform.checkout.domain.mapper.ICheckoutMapper;
import com.tokopedia.purchase_platform.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.purchase_platform.cart.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.purchase_platform.common.router.ICheckoutModuleRouter;
import com.tokopedia.purchase_platform.common.di.module.CartUseCaseModule;
import com.tokopedia.purchase_platform.common.di.module.CheckoutRouterModule;
import com.tokopedia.purchase_platform.common.di.module.CheckoutUseCaseModule;
import com.tokopedia.purchase_platform.common.di.module.DataMapperModule;
import com.tokopedia.purchase_platform.common.di.module.DataModule;
import com.tokopedia.purchase_platform.common.di.module.DataUtilModule;
import com.tokopedia.purchase_platform.common.di.module.ShipmentUseCaseModule;
import com.tokopedia.purchase_platform.common.di.scope.CartScope;
import com.tokopedia.logisticdata.data.repository.RatesRepository;
import com.tokopedia.purchase_platform.common.data.repository.ICartRepository;
import com.tokopedia.transactiondata.repository.ITopPayRepository;
import com.tokopedia.purchase_platform.common.utils.CartApiRequestParamGenerator;

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
                        DataUtilModule.class,
                        DataModule.class,
                        CartUseCaseModule.class,
                        CheckoutUseCaseModule.class,
                        ShipmentUseCaseModule.class
                },
        dependencies = BaseAppComponent.class
)
public interface CartComponent {

    ICheckoutModuleRouter checkoutModuleRouter();

    ICartRepository cartRepository();

    ITopPayRepository topPayRepository();

    RatesRepository ratesRepository();

    AddressRepository addressRepository();

    PeopleAddressRepository peopleAddressRepository();

    ICartMapper cartMapper();

    IVoucherCouponMapper voucherCouponMapper();

    ICheckoutMapper checkoutMapper();

    IShipmentMapper shipmentMapper();

    AbstractionRouter abstractionRouter();

    Context context();

    CartApiRequestParamGenerator cartApiRequestParamGenerator();

    void inject(CartComponentInjector cartApiServiceComponentInjector);
}
