package com.tokopedia.checkout.view.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.checkout.data.repository.ICartRepository;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.checkout.domain.mapper.IMapperUtil;
import com.tokopedia.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.checkout.view.di.module.CartUseCaseModule;
import com.tokopedia.checkout.view.di.module.DataModule;
import com.tokopedia.checkout.view.di.scope.CartScope;

import dagger.Component;

/**
 * @author anggaprasetiyo on 24/04/18.
 */
@CartScope
@Component(modules = {DataModule.class, CartUseCaseModule.class}, dependencies = BaseAppComponent.class)
public interface CartComponent {
    ICartRepository cartRepository();

    IMapperUtil mapperUtil();

    ICartMapper cartMapper();

    IVoucherCouponMapper voucherCouponMapper();

    void inject(CartComponentInjector cartApiServiceComponentInjector);
}
