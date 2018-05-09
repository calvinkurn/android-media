package com.tokopedia.checkout.domain.mapper;

import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartListResult;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.core.router.transactionmodule.sharedata.CouponListResult;
import com.tokopedia.transactiondata.entity.response.checkpromocodecartlist.CheckPromoCodeCartListDataResponse;
import com.tokopedia.transactiondata.entity.response.checkpromocodefinal.CheckPromoCodeFinalDataResponse;
import com.tokopedia.transactiondata.entity.response.couponlist.CouponDataResponse;
import com.tokopedia.checkout.domain.datamodel.voucher.CouponListData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartShipmentData;

/**
 * @author anggaprasetiyo on 27/02/18.
 */

public interface IVoucherCouponMapper {

    CouponListData convertCouponListData(CouponDataResponse couponDataResponse);

    PromoCodeCartListData convertPromoCodeCartListData(
            CheckPromoCodeCartListDataResponse checkPromoCodeCartListDataResponse
    );

    PromoCodeCartShipmentData convertPromoCodeCartShipmentData(
            CheckPromoCodeFinalDataResponse checkPromoCodeFinalDataResponse
    );

    CouponListResult convertCouponListResult(CouponListData couponListData);

    CheckPromoCodeCartListResult convertCheckPromoCodeCartListResult(
            PromoCodeCartListData promoCodeCartListData
    );

    CheckPromoCodeCartShipmentResult convertCheckPromoCodeCartShipmentResult(
            PromoCodeCartShipmentData promoCodeCartShipmentData
    );
}
