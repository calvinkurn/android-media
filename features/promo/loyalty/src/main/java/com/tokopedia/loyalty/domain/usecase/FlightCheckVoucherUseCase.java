package com.tokopedia.loyalty.domain.usecase;

import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by alvarisi on 3/29/18.
 */

public class FlightCheckVoucherUseCase extends UseCase<VoucherViewModel> {
    private static final String PARAM_CART_ID = "PARAM_CART_ID";
    private static final String PARAM_VOUCHER_CODE = "PARAM_VOUCHER_CODE";
    private static final String PARAM_IS_COUPON = "PARAM_IS_COUPON";
    private static final String DEFAULT_IS_COUPON = "1";
    private static final String DEFAULT_IS_VOUCHER = "0";

    private LoyaltyModuleRouter loyaltyModuleRouter;

    @Inject
    public FlightCheckVoucherUseCase(LoyaltyModuleRouter loyaltyModuleRouter) {
        this.loyaltyModuleRouter = loyaltyModuleRouter;
    }

    @Override
    public Observable<VoucherViewModel> createObservable(RequestParams requestParams) {
        return loyaltyModuleRouter.checkFlightVoucher(
                requestParams.getString(PARAM_VOUCHER_CODE, ""),
                requestParams.getString(PARAM_CART_ID, ""),
                requestParams.getString(PARAM_IS_COUPON, ""));
    }

    public RequestParams createCouponRequest(String cartId, String voucherCode) {
        return createRequest(cartId, voucherCode, DEFAULT_IS_COUPON);
    }

    public RequestParams createRequest(String cartId, String voucherCode, String isCoupon) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_CART_ID, cartId);
        requestParams.putString(PARAM_VOUCHER_CODE, voucherCode);
        requestParams.putString(PARAM_IS_COUPON, isCoupon);
        return requestParams;
    }

    public RequestParams createVoucherRequest(String cartId, String voucherCode) {
        return createRequest(cartId, voucherCode, DEFAULT_IS_VOUCHER);
    }
}
