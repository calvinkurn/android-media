package com.tokopedia.checkout.view.di.module;

import com.google.gson.Gson;
import com.tokopedia.checkout.data.repository.ICartRepository;
import com.tokopedia.checkout.domain.mapper.CartMapper;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.checkout.domain.mapper.IMapperUtil;
import com.tokopedia.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.checkout.domain.mapper.MapperUtil;
import com.tokopedia.checkout.domain.mapper.VoucherCouponMapper;
import com.tokopedia.checkout.domain.usecase.AddToCartUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartShipmentUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetCouponListCartMarketPlaceUseCase;
import com.tokopedia.checkout.domain.usecase.GetMarketPlaceCartCounterUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 25/04/18.
 */
@Module
public class CartUseCaseModule {

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
    AddToCartUseCase addToCartUseCase(ICartRepository cartRepository, Gson gson) {
        return new AddToCartUseCase(cartRepository, gson);
    }

    @Provides
    CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase(ICartRepository cartRepository,
                                                                IVoucherCouponMapper iVoucherCouponMapper) {
        return new CheckPromoCodeCartListUseCase(cartRepository, iVoucherCouponMapper);
    }

    @Provides
    CheckPromoCodeCartShipmentUseCase checkPromoCodeCartShipmentUseCase(ICartRepository cartRepository,
                                                                        IVoucherCouponMapper voucherCouponMapper) {
        return new CheckPromoCodeCartShipmentUseCase(cartRepository, voucherCouponMapper);
    }

    @Provides
    GetCouponListCartMarketPlaceUseCase getCouponListCartMarketPlaceUseCase(ICartRepository cartRepository,
                                                                            IVoucherCouponMapper voucherCouponMapper) {
        return new GetCouponListCartMarketPlaceUseCase(cartRepository, voucherCouponMapper);
    }

    @Provides
    GetMarketPlaceCartCounterUseCase getMarketPlaceCartCounterUseCase(ICartRepository cartRepository) {
        return new GetMarketPlaceCartCounterUseCase(cartRepository);
    }

    @Provides
    GetCartListUseCase getCartListUseCase(ICartRepository cartRepository, ICartMapper mapper) {
        return new GetCartListUseCase(cartRepository, mapper);
    }
}
