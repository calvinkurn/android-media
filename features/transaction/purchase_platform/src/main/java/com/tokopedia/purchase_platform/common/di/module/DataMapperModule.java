package com.tokopedia.purchase_platform.common.di.module;

import com.tokopedia.purchase_platform.cart.domain.mapper.CartMapper;
import com.tokopedia.purchase_platform.cart.domain.mapper.ICartMapper;
import com.tokopedia.purchase_platform.cart.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.purchase_platform.cart.domain.mapper.VoucherCouponMapper;
import com.tokopedia.purchase_platform.checkout.domain.mapper.CheckoutMapper;
import com.tokopedia.purchase_platform.checkout.domain.mapper.ICheckoutMapper;
import com.tokopedia.purchase_platform.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.purchase_platform.checkout.domain.mapper.ShipmentMapper;
import com.tokopedia.purchase_platform.checkout.view.converter.RatesDataConverter;
import com.tokopedia.purchase_platform.common.base.IMapperUtil;
import com.tokopedia.purchase_platform.common.base.MapperUtil;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 02/05/18.
 */
@Module
public class DataMapperModule {
    @Provides
    IMapperUtil provideIMapperUtil() {
        return new MapperUtil();
    }

    @Provides
    ICartMapper provideICartMapper(IMapperUtil mapperUtil) {
        return new CartMapper(mapperUtil);
    }

    @Provides
    IVoucherCouponMapper provideIVoucherCouponMapper(IMapperUtil mapperUtil) {
        return new VoucherCouponMapper(mapperUtil);
    }

    @Provides
    ICheckoutMapper provideICheckoutMapper(IMapperUtil mapperUtil) {
        return new CheckoutMapper(mapperUtil);
    }

    @Provides
    IShipmentMapper provideIShipmentMapper(IMapperUtil mapperUtil) {
        return new ShipmentMapper(mapperUtil);
    }

    @Provides
    RatesDataConverter provideRatesDataConverter() {
        return new RatesDataConverter();
    }
}
