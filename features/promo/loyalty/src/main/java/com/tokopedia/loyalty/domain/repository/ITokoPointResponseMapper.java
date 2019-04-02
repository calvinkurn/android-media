package com.tokopedia.loyalty.domain.repository;

import com.tokopedia.loyalty.common.TokoPointDrawerData;
import com.tokopedia.loyalty.domain.entity.response.CouponListDataResponse;
import com.tokopedia.loyalty.domain.entity.response.DigitalVoucherData;
import com.tokopedia.loyalty.domain.entity.response.GqlTokoPointDrawerDataResponse;
import com.tokopedia.loyalty.domain.entity.response.HachikoDrawerDataResponse;
import com.tokopedia.loyalty.domain.entity.response.TokoPointResponse;
import com.tokopedia.loyalty.domain.entity.response.ValidateRedeemCouponResponse;
import com.tokopedia.loyalty.domain.entity.response.VoucherResponse;
import com.tokopedia.loyalty.view.data.CouponData;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.data.CouponsDataWrapper;
import com.tokopedia.loyalty.view.data.VoucherViewModel;

import java.util.List;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public interface ITokoPointResponseMapper {

    String sampleMapper(TokoPointResponse tokoplusResponse);

    List<CouponData> convertCouponListData(CouponListDataResponse couponListDataResponse);

    String getSuccessValidateRedeemMessage(ValidateRedeemCouponResponse response);

    TokoPointDrawerData convertTokoplusPointDrawer(HachikoDrawerDataResponse tokoplusPointDrawerData);

    VoucherViewModel voucherViewModel(VoucherResponse voucherResponse, String voucherCode);

    CouponViewModel couponViewModel(VoucherResponse voucherResponse,
                                    String voucherCode,
                                    String couponTitle);

    VoucherViewModel digtialVoucherViewModel(DigitalVoucherData voucherResponse,
                                             String voucherCode);

    CouponViewModel digitalCouponViewModel(DigitalVoucherData voucherResponse,
                                           String voucherCode,
                                           String couponTitle);

    CouponsDataWrapper convertCouponsDataWraper(CouponListDataResponse couponListDataResponse);
}
