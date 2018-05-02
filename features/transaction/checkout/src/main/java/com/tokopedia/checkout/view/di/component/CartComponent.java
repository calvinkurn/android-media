package com.tokopedia.checkout.view.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.checkout.data.repository.ICartRepository;
import com.tokopedia.checkout.data.repository.ITopPayRepository;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.checkout.domain.mapper.ICheckoutMapper;
import com.tokopedia.checkout.domain.mapper.IMapperUtil;
import com.tokopedia.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.checkout.domain.mapper.ITopPayMapper;
import com.tokopedia.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.checkout.view.di.module.CartUseCaseModule;
import com.tokopedia.checkout.view.di.module.CheckoutUseCaseModule;
import com.tokopedia.checkout.view.di.module.DataMapperModule;
import com.tokopedia.checkout.view.di.module.DataModule;
import com.tokopedia.checkout.view.di.scope.CartScope;

import dagger.Component;

/**
 * @author anggaprasetiyo on 24/04/18.
 */
@CartScope
@Component(
        modules = {DataModule.class, DataMapperModule.class, CartUseCaseModule.class, CheckoutUseCaseModule.class},
        dependencies = BaseAppComponent.class
)
public interface CartComponent {
    ICartRepository cartRepository();

    ITopPayRepository topPayRepository();

    IMapperUtil mapperUtil();

    ICartMapper cartMapper();

    IVoucherCouponMapper voucherCouponMapper();

    ICheckoutMapper checkoutMapper();

    IShipmentMapper shipmentMapper();

    ITopPayMapper topPayMapper();

    void inject(CartComponentInjector cartApiServiceComponentInjector);
}
