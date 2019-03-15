package com.tokopedia.checkout.view.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.checkout.domain.usecase.AddToCartOneClickShipmentUseCase;
import com.tokopedia.checkout.domain.usecase.AddToCartUseCase;
import com.tokopedia.checkout.domain.usecase.CancelAutoApplyCouponUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.checkout.domain.usecase.CheckPromoCodeCartShipmentUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartUpdateCartUseCase;
import com.tokopedia.checkout.domain.usecase.DeleteCartUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartMultipleAddressListUseCase;
import com.tokopedia.checkout.domain.usecase.GetCouponListCartMarketPlaceUseCase;
import com.tokopedia.checkout.domain.usecase.GetMarketPlaceCartCounterUseCase;
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.checkout.domain.usecase.ResetCartGetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.ResetCartUseCase;
import com.tokopedia.checkout.domain.usecase.UpdateAndReloadCartUseCase;
import com.tokopedia.checkout.domain.usecase.UpdateCartUseCase;
import com.tokopedia.checkout.view.feature.shipment.di.ShipmentScope;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil;
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule;
import com.tokopedia.promocheckout.common.di.PromoCheckoutQualifier;
import com.tokopedia.checkout.view.di.scope.CartListScope;
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeUseCase;
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase;
import com.tokopedia.promocheckout.common.domain.mapper.ICheckPromoStackingCodeMapper;
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
    AddToCartUseCase addToCartUseCase(ICartRepository cartRepository, Gson gson) {
        return new AddToCartUseCase(cartRepository, gson);
    }

    @Provides
    AddToCartOneClickShipmentUseCase addToCartOneClickShipmentUseCase(ICartRepository cartRepository, Gson gson) {
        return new AddToCartOneClickShipmentUseCase(cartRepository, gson);
    }

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
    DeleteCartUseCase deleteCartUseCase(ICartRepository cartRepository, ICartMapper mapper) {
        return new DeleteCartUseCase(cartRepository, mapper);
    }

    @Provides
    DeleteCartUpdateCartUseCase deleteCartUpdateCartUseCase(ICartRepository cartRepository, ICartMapper mapper) {
        return new DeleteCartUpdateCartUseCase(cartRepository, mapper);
    }

    @Provides
    DeleteCartGetCartListUseCase deleteCartGetCartListUseCase(Context context, ICartRepository cartRepository, ICartMapper mapper) {
        return new DeleteCartGetCartListUseCase(context, cartRepository, mapper);
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

}
