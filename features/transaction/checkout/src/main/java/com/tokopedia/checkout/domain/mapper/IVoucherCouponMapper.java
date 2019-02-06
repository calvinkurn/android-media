package com.tokopedia.checkout.domain.mapper;

import com.tokopedia.checkout.domain.datamodel.voucher.CouponListData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartShipmentData;
import com.tokopedia.promocheckout.common.domain.model.DataVoucher;
import com.tokopedia.transaction.common.sharedata.CouponListResult;
import com.tokopedia.transactiondata.entity.response.checkpromocodecartlist.CheckPromoCodeCartListDataResponse;
import com.tokopedia.transactiondata.entity.response.checkpromocodefinal.CheckPromoCodeFinalDataResponse;
import com.tokopedia.transactiondata.entity.response.couponlist.CouponDataResponse;

/**
 * @author anggaprasetiyo on 27/02/18.
 */

public interface IVoucherCouponMapper {

    CouponListData convertCouponListData(CouponDataResponse couponDataResponse);

    PromoCodeCartListData convertPromoCodeCartListData(
            DataVoucher dataVoucher
    );

    PromoCodeCartShipmentData convertPromoCodeCartShipmentData(
            CheckPromoCodeFinalDataResponse checkPromoCodeFinalDataResponse
    );

    CouponListResult convertCouponListResult(CouponListData couponListData);

    PromoCodeCartShipmentData convertPromoCodeCartShipmentData(DataVoucher dataVoucher);
}
