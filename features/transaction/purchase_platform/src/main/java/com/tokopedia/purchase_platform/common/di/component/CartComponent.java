package com.tokopedia.purchase_platform.common.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.purchase_platform.common.di.module.CartUseCaseModule;
import com.tokopedia.purchase_platform.common.di.module.CheckoutRouterModule;
import com.tokopedia.purchase_platform.common.di.module.CheckoutUseCaseModule;
import com.tokopedia.purchase_platform.common.di.module.DataMapperModule;
import com.tokopedia.purchase_platform.common.di.module.DataModule;
import com.tokopedia.purchase_platform.common.di.module.DataUtilModule;
import com.tokopedia.purchase_platform.common.di.module.ShipmentUseCaseModule;
import com.tokopedia.purchase_platform.common.di.scope.CartScope;

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

//    ICheckoutModuleRouter checkoutModuleRouter();

//    ICartRepository cartRepository();

//    AddressRepository addressRepository();

//    ICartMapper cartMapper();

//    IVoucherCouponMapper voucherCouponMapper();

//    ICheckoutMapper checkoutMapper();

//    IShipmentMapper shipmentMapper();

//    AbstractionRouter abstractionRouter();

//    Context context();

//    CartApiRequestParamGenerator cartApiRequestParamGenerator();

    void inject(CartComponentInjector cartApiServiceComponentInjector);
}
