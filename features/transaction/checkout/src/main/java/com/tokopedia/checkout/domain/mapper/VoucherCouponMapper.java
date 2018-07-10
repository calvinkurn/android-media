package com.tokopedia.checkout.domain.mapper;

import com.tokopedia.checkout.domain.datamodel.voucher.CouponListData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartShipmentData;
import com.tokopedia.core.router.transactionmodule.sharedata.CouponListResult;
import com.tokopedia.transactiondata.entity.response.checkpromocodecartlist.CheckPromoCodeCartListDataResponse;
import com.tokopedia.transactiondata.entity.response.checkpromocodefinal.CheckPromoCodeFinalDataResponse;
import com.tokopedia.transactiondata.entity.response.couponlist.Coupon;
import com.tokopedia.transactiondata.entity.response.couponlist.CouponDataResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 27/02/18.
 */

public class VoucherCouponMapper implements IVoucherCouponMapper {

    private final IMapperUtil mapperUtil;

    @Inject
    public VoucherCouponMapper(IMapperUtil mapperUtil) {
        this.mapperUtil = mapperUtil;
    }

    @Override
    public CouponListData convertCouponListData(CouponDataResponse couponDataResponse) {
        CouponListData couponListData = new CouponListData();

        List<CouponListData.Coupon> couponList = new ArrayList<>();
        for (Coupon coupon : couponDataResponse.getCoupons()) {
            couponList.add(new CouponListData.Coupon.Builder()
                    .code(coupon.getCode())
                    .description(coupon.getDescription())
                    .expired(coupon.getExpired())
                    .icon(coupon.getIcon())
                    .id(coupon.getId())
                    .imageUrl(coupon.getImageUrl())
                    .imageUrlMobile(coupon.getImageUrlMobile())
                    .promoId(coupon.getPromoId())
                    .subTitle(coupon.getSubTitle())
                    .title(coupon.getTitle())
                    .build());
        }
        couponListData.setCoupons(couponList);
        return couponListData;
    }

    @Override
    public PromoCodeCartListData convertPromoCodeCartListData(
            CheckPromoCodeCartListDataResponse dataResponse
    ) {
        PromoCodeCartListData promoCodeCartListData = new PromoCodeCartListData();
        promoCodeCartListData.setError(!mapperUtil.isEmpty(dataResponse.getError()));
        promoCodeCartListData.setErrorMessage(dataResponse.getError());

        if (!mapperUtil.isEmpty(dataResponse.getDataVoucher())) {
            PromoCodeCartListData.DataVoucher dataVoucher = new PromoCodeCartListData.DataVoucher();
            dataVoucher.setCashbackAmount(dataResponse.getDataVoucher().getCashbackAmount());
            dataVoucher.setCashbackTopCashAmount(dataResponse.getDataVoucher().getCashbackTopCashAmount());
            dataVoucher.setCashbackVoucherAmount(dataResponse.getDataVoucher().getCashbackVoucherAmount());
            dataVoucher.setCashbackVoucherDescription(dataResponse.getDataVoucher().getCashbackVoucherDescription());
            dataVoucher.setCode(dataResponse.getDataVoucher().getCode());
            dataVoucher.setDiscountAmount(dataResponse.getDataVoucher().getDiscountAmount());
            dataVoucher.setExtraAmount(dataResponse.getDataVoucher().getExtraAmount());
            dataVoucher.setGatewayId(dataResponse.getDataVoucher().getGatewayId());
            dataVoucher.setMessageSuccess(dataResponse.getDataVoucher().getMessageSuccess());
            dataVoucher.setPromoCodeId(dataResponse.getDataVoucher().getPromoCodeId());
            dataVoucher.setSaldoAmount(dataResponse.getDataVoucher().getSaldoAmount());
            dataVoucher.setToken(dataResponse.getDataVoucher().getToken());

            promoCodeCartListData.setDataVoucher(dataVoucher);
        }
        return promoCodeCartListData;
    }

    @Override
    public PromoCodeCartShipmentData convertPromoCodeCartShipmentData(CheckPromoCodeFinalDataResponse dataResponse) {
        PromoCodeCartShipmentData promoCodeCartShipmentData = new PromoCodeCartShipmentData();
        promoCodeCartShipmentData.setError(!mapperUtil.isEmpty(dataResponse.getError()));
        promoCodeCartShipmentData.setErrorMessage(dataResponse.getError());
        if (!mapperUtil.isEmpty(dataResponse.getDataVoucher())) {
            PromoCodeCartShipmentData.DataVoucher dataVoucher = new PromoCodeCartShipmentData.DataVoucher();
            dataVoucher.setVoucherAmount(dataResponse.getDataVoucher().getVoucherAmount());
            dataVoucher.setVoucherAmountIdr(dataResponse.getDataVoucher().getVoucherAmountIdr());
            dataVoucher.setVoucherNoOtherPromotion(dataResponse.getDataVoucher().getVoucherNoOtherPromotion());
            dataVoucher.setVoucherStatus(dataResponse.getDataVoucher().getVoucherStatus());
            dataVoucher.setVoucherPromoDesc(dataResponse.getDataVoucher().getVoucherPromoDesc());
            promoCodeCartShipmentData.setDataVoucher(dataVoucher);
        }
        return promoCodeCartShipmentData;
    }

    @Override
    public CouponListResult convertCouponListResult(CouponListData couponListData) {
        CouponListResult couponListResult = new CouponListResult();
        List<CouponListResult.Coupon> couponList = new ArrayList<>();
        for (CouponListData.Coupon coupon : couponListData.getCoupons()) {
            couponList.add(
                    new CouponListResult.Coupon.Builder()
                            .code(coupon.getCode())
                            .description(coupon.getDescription())
                            .expired(coupon.getExpired())
                            .icon(coupon.getIcon())
                            .id(coupon.getId())
                            .imageUrl(coupon.getImageUrl())
                            .imageUrlMobile(coupon.getImageUrl())
                            .promoId(coupon.getPromoId())
                            .subTitle(coupon.getSubTitle())
                            .title(coupon.getTitle())
                            .build()
            );
        }
        couponListResult.setCoupons(couponList);
        return couponListResult;
    }

}
