package com.tokopedia.checkout.domain.usecase;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.datamodel.voucher.CouponListData;
import com.tokopedia.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.transaction.common.sharedata.CouponListResult;
import com.tokopedia.transactiondata.entity.response.couponlist.CouponDataResponse;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 27/02/18.
 */

public class GetCouponListCartMarketPlaceUseCase extends UseCase<CouponListResult> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING = "PARAM_REQUEST_AUTH_MAP_STRING";
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_PAGE_SIZE = "page_size";
    private final ICartRepository cartRepository;
    private final IVoucherCouponMapper voucherCouponMapper;

    @Inject
    public GetCouponListCartMarketPlaceUseCase(ICartRepository cartRepository,
                                               IVoucherCouponMapper voucherCouponMapper) {
        this.cartRepository = cartRepository;
        this.voucherCouponMapper = voucherCouponMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<CouponListResult> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> param =
                (TKPDMapParam<String, String>) requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING);

        return cartRepository.getCouponList(param)
                .map(new Func1<CouponDataResponse, CouponListData>() {
                    @Override
                    public CouponListData call(CouponDataResponse couponDataResponse) {
                        return voucherCouponMapper.convertCouponListData(couponDataResponse);
                    }
                }).map(new Func1<CouponListData, CouponListResult>() {
                    @Override
                    public CouponListResult call(CouponListData couponListData) {
                        return voucherCouponMapper.convertCouponListResult(couponListData);
                    }
                });
    }
}
