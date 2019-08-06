package com.tokopedia.checkout.domain.usecase;

import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.promocheckout.common.di.PromoCheckoutQualifier;
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeUseCase;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author anggaprasetiyo on 27/02/18.
 */

public class CheckPromoCodeCartListUseCase extends UseCase<PromoCodeCartListData> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO = "PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO";
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART = "PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART";

    public static final String PARAM_PROMO_LANG = "lang";
    public static final String PARAM_PROMO_CODE = "promo_code";
    public static final String PARAM_PROMO_SUGGESTED = "suggested";
    public static final String PARAM_VALUE_SUGGESTED = "1";
    public static final String PARAM_VALUE_NOT_SUGGESTED = "0";
    public static final String PARAM_VALUE_LANG_ID = "id";
    public static final String PARAM_CARTS = "carts";
    public static final String PARAM_ONE_CLICK_SHIPMENT = "is_one_click_shipment";

    private final ICartRepository cartRepository;
    private final IVoucherCouponMapper voucherCouponMapper;
    private CheckPromoCodeUseCase checkPromoCodeUseCase;


    @Inject
    public CheckPromoCodeCartListUseCase(ICartRepository cartRepository,
                                         IVoucherCouponMapper voucherCouponMapper,
                                         @PromoCheckoutQualifier CheckPromoCodeUseCase checkPromoCodeUseCase) {
        this.cartRepository = cartRepository;
        this.voucherCouponMapper = voucherCouponMapper;
        this.checkPromoCodeUseCase = checkPromoCodeUseCase;
    }


    @Override
    @SuppressWarnings("unchecked")
    public Observable<PromoCodeCartListData> createObservable(RequestParams requestParams) {
        Map<String, String> paramCheckPromo = (Map<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO);
        Map<String, String> paramUpdateCart = (Map<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART);

        if (paramUpdateCart != null)
            return cartRepository.updateCartData(paramUpdateCart)
                    .flatMap(updateCartDataResponse -> checkPromoCodeUseCase.createObservable(checkPromoCodeUseCase.createRequestParams(
                            paramCheckPromo.get(PARAM_PROMO_CODE), false,
                            (paramCheckPromo.get(PARAM_PROMO_SUGGESTED) != null &&
                                    paramCheckPromo.get(PARAM_PROMO_SUGGESTED).equals(PARAM_VALUE_SUGGESTED)),
                            paramCheckPromo.get(PARAM_ONE_CLICK_SHIPMENT) != null &&
                                    Boolean.parseBoolean(paramCheckPromo.get(PARAM_ONE_CLICK_SHIPMENT))))
                            .map(
                                    voucherCouponMapper::convertPromoCodeCartListData
                            ));
        else
            return checkPromoCodeUseCase.createObservable(checkPromoCodeUseCase.createRequestParams(
                    paramCheckPromo.get(PARAM_PROMO_CODE), false,
                    (paramCheckPromo.get(PARAM_PROMO_SUGGESTED) != null &&
                            paramCheckPromo.get(PARAM_PROMO_SUGGESTED).equals(PARAM_VALUE_SUGGESTED)),
                    paramCheckPromo.get(PARAM_ONE_CLICK_SHIPMENT) != null &&
                            Boolean.parseBoolean(paramCheckPromo.get(PARAM_ONE_CLICK_SHIPMENT))))
                    .map(
                            voucherCouponMapper::convertPromoCodeCartListData
                    );
    }
}
