package com.tokopedia.purchase_platform.common.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule;
import com.tokopedia.promocheckout.common.di.PromoCheckoutQualifier;
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeUseCase;
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase;
import com.tokopedia.purchase_platform.common.data.repository.ICartRepository;
import com.tokopedia.purchase_platform.features.cart.domain.mapper.ICartMapper;
import com.tokopedia.purchase_platform.features.cart.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.CheckPromoCodeCartListUseCase;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.DeleteCartListUseCase;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.GetCartListUseCase;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.ResetCartGetCartListUseCase;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.UpdateAndReloadCartUseCase;
import com.tokopedia.purchase_platform.features.cart.domain.usecase.UpdateCartUseCase;
import com.tokopedia.purchase_platform.features.checkout.domain.mapper.IShipmentMapper;
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.GetShipmentAddressFormUseCase;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.domain.usecase.GetCartMultipleAddressListUseCase;
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
    GetCartListUseCase getCartListUseCase(Context context, ICartRepository cartRepository, ICartMapper mapper) {
        return new GetCartListUseCase(context, cartRepository, mapper);
    }

    @Provides
    GetCartMultipleAddressListUseCase getCartMultipleAddressListUseCase(Context context, ICartRepository cartRepository, ICartMapper mapper) {
        return new GetCartMultipleAddressListUseCase(context, cartRepository, mapper);
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
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
