package com.tokopedia.checkout.domain.usecase;

import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartShipmentData;
import com.tokopedia.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author anggaprasetiyo on 27/02/18.
 */

public class CheckPromoCodeCartShipmentUseCase extends UseCase<PromoCodeCartShipmentData> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO = "PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO";
    public static final String PARAM_PROMO_LANG = "lang";
    public static final String PARAM_CARTS = "carts";
    public static final String PARAM_PROMO_SUGGESTED = "suggested";
    public static final String PARAM_VALUE_SUGGESTED = "1";
    public static final String PARAM_VALUE_NOT_SUGGESTED = "0";
    public static final String PARAM_VALUE_LANG_ID = "id";
    public static final String PARAM_ONE_CLICK_SHIPMENT = "is_one_click_shipment";


    private final ICartRepository cartRepository;
    private final IVoucherCouponMapper voucherCouponMapper;


    @Inject
    public CheckPromoCodeCartShipmentUseCase(ICartRepository cartRepository,
                                             IVoucherCouponMapper voucherCouponMapper) {
        this.cartRepository = cartRepository;
        this.voucherCouponMapper = voucherCouponMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<PromoCodeCartShipmentData> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> param =
                (TKPDMapParam<String, String>) requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO);
        return cartRepository.checkPromoCodeCartShipment(param)
                .map(
                        voucherCouponMapper::convertPromoCodeCartShipmentData
                );
    }
}
