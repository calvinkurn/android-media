package com.tokopedia.checkout.domain.usecase;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.data.entity.response.checkpromocodecartlist.CheckPromoCodeCartListDataResponse;
import com.tokopedia.checkout.data.repository.ICartRepository;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartListResult;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 27/02/18.
 */

public class CheckPromoCodeCartListUseCase extends UseCase<CheckPromoCodeCartListResult> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO = "PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO";
    public static final String PARAM_PROMO_LANG = "lang";

    private final ICartRepository cartRepository;
    private final IVoucherCouponMapper voucherCouponMapper;


    @Inject
    public CheckPromoCodeCartListUseCase(ICartRepository cartRepository,
                                         IVoucherCouponMapper voucherCouponMapper) {
        this.cartRepository = cartRepository;
        this.voucherCouponMapper = voucherCouponMapper;
    }

    public static final String PARAM_PROMO_CODE = "promo_code";
    public static final String PARAM_PROMO_SUGGESTED = "suggested";

    public static final String PARAM_VALUE_SUGGESTED = "1";
    public static final String PARAM_VALUE_NOT_SUGGESTED = "0";
    public static final String PARAM_VALUE_LANG_ID = "id";

    @Override
    @SuppressWarnings("unchecked")
    public Observable<CheckPromoCodeCartListResult> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> param = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO);
        return cartRepository.checkPromoCodeCartList(param)
                .map(
                        new Func1<CheckPromoCodeCartListDataResponse, PromoCodeCartListData>() {
                            @Override
                            public PromoCodeCartListData call(
                                    CheckPromoCodeCartListDataResponse checkPromoCodeCartListDataResponse
                            ) {
                                return voucherCouponMapper.convertPromoCodeCartListData(
                                        checkPromoCodeCartListDataResponse
                                );
                            }
                        }
                ).map(
                        new Func1<PromoCodeCartListData, CheckPromoCodeCartListResult>() {
                            @Override
                            public CheckPromoCodeCartListResult call(PromoCodeCartListData promoCodeCartListData) {
                                return voucherCouponMapper.convertCheckPromoCodeCartListResult(promoCodeCartListData);
                            }
                        });
    }
}
