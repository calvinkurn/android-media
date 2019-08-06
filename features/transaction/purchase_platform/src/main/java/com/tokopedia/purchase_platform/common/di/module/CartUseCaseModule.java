package com.tokopedia.purchase_platform.common.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.purchase_platform.cart.domain.mapper.ICartMapper;
import com.tokopedia.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.purchase_platform.common.feature.promo.domain.CancelAutoApplyCouponUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartShipmentUseCase;
import com.tokopedia.purchase_platform.cart.domain.usecase.DeleteCartListUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartUpdateCartUseCase;
import com.tokopedia.purchase_platform.cart.domain.usecase.GetCartListUseCase;
import com.tokopedia.purchase_platform.checkout.subfeature.multiple_address.domain.usecase.GetCartMultipleAddressListUseCase;
import com.tokopedia.checkout.domain.usecase.GetCouponListCartMarketPlaceUseCase;
import com.tokopedia.checkout.domain.usecase.GetMarketPlaceCartCounterUseCase;
import com.tokopedia.purchase_platform.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.purchase_platform.cart.domain.usecase.ResetCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.ResetCartUseCase;
import com.tokopedia.purchase_platform.cart.domain.usecase.UpdateAndReloadCartUseCase;
import com.tokopedia.purchase_platform.cart.domain.usecase.UpdateCartUseCase;
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule;
import com.tokopedia.promocheckout.common.di.PromoCheckoutQualifier;
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeUseCase;
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 25/04/18.
 */
@Module(includes = {PromoCheckoutModule.class})
public class CartUseCaseModule {

    @Provides
    CheckPromoCodeCartListUseCase checkPromoCodeCartListUseCase(ICartRepository cartRepository,
                                                                IVoucherCouponMapper iVoucherCouponMapper,
                                                                @PromoCheckoutQualifier CheckPromoCodeUseCase checkPromoCodeUseCase) {
        return new CheckPromoCodeCartListUseCase(cartRepository, iVoucherCouponMapper, checkPromoCodeUseCase);
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
    GetCartListUseCase getCartListUseCase(Context context, ICartRepository cartRepository, ICartMapper mapper) {
        return new GetCartListUseCase(context, cartRepository, mapper);
    }

    @Provides
    GetCartMultipleAddressListUseCase getCartMultipleAddressListUseCase(Context context, ICartRepository cartRepository, ICartMapper mapper) {
        return new GetCartMultipleAddressListUseCase(context, cartRepository, mapper);
    }

    @Provides
    DeleteCartUpdateCartUseCase deleteCartUpdateCartUseCase(ICartRepository cartRepository, ICartMapper mapper) {
        return new DeleteCartUpdateCartUseCase(cartRepository, mapper);
    }

    @Provides
    DeleteCartListUseCase deleteCartGetCartListUseCase(ICartRepository cartRepository, ICartMapper mapper, ClearCacheAutoApplyStackUseCase clearCacheAutoApplyStackUseCase) {
        return new DeleteCartListUseCase(cartRepository, mapper, clearCacheAutoApplyStackUseCase);
    }

    @Provides
    UpdateCartUseCase updateCartGetShipmentAddressFormUseCase(
            ICartRepository cartRepository, ICartMapper cartMapper
    ) {
        return new UpdateCartUseCase(cartRepository, cartMapper);
    }

    @Provides
    GetShipmentAddressFormUseCase getShipmentAddressFormUseCase(
            ICartRepository cartRepository, IShipmentMapper shipmentMapper
    ) {
        return new GetShipmentAddressFormUseCase(cartRepository, shipmentMapper);
    }

    @Provides
    ResetCartGetCartListUseCase resetCartGetCartListUseCase(
            Context context, ICartRepository cartRepository, ICartMapper cartMapper
    ) {
        return new ResetCartGetCartListUseCase(context, cartRepository, cartMapper);
    }

    @Provides
    UpdateAndReloadCartUseCase updateAndReloadCartUseCase(
            Context context, ICartRepository cartRepository, ICartMapper cartMapper
    ) {
        return new UpdateAndReloadCartUseCase(context, cartRepository, cartMapper);
    }

    @Provides
    ResetCartUseCase resetCartGetShipmentFormUseCase(
            ICartRepository cartRepository, ICartMapper cartMapper
    ) {
        return new ResetCartUseCase(cartRepository, cartMapper);
    }

    @Provides
    CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase(ICartRepository iCartRepository) {
        return new CancelAutoApplyCouponUseCase(iCartRepository);
    }

    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
