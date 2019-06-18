package com.tokopedia.checkout.domain.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.checkout.domain.datamodel.voucher.CouponListData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartShipmentData;
import com.tokopedia.checkout.domain.datamodel.voucher.promostacking.Data;
import com.tokopedia.checkout.domain.datamodel.voucher.promostacking.ResponseFirstStep;
import com.tokopedia.transaction.common.sharedata.CouponListResult;
import com.tokopedia.transactiondata.entity.response.checkpromocodecartlist.CheckPromoCodeCartListDataResponse;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.promocheckout.common.domain.model.DataVoucher;
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
            DataVoucher dataVoucherPromo
    ) {
        PromoCodeCartListData promoCodeCartListData = new PromoCodeCartListData();
        promoCodeCartListData.setError(mapperUtil.isEmpty(dataVoucherPromo));
        promoCodeCartListData.setErrorMessage(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);

        if (!mapperUtil.isEmpty(dataVoucherPromo)) {
            PromoCodeCartListData.DataVoucher dataVoucher = new PromoCodeCartListData.DataVoucher();
            dataVoucher.setCashbackAmount(dataVoucherPromo.getCashbackAmount());
            dataVoucher.setCashbackTopCashAmount(dataVoucherPromo.getCashbackTopCashAmount());
            dataVoucher.setCashbackVoucherAmount(dataVoucherPromo.getCashbackVoucherAmount());
            dataVoucher.setCashbackVoucherDescription(dataVoucherPromo.getCashbackVoucherDescription());
            dataVoucher.setCode(dataVoucherPromo.getCode());
            dataVoucher.setDiscountAmount(dataVoucherPromo.getDiscountAmount());
            dataVoucher.setExtraAmount(dataVoucherPromo.getExtraAmount());
            dataVoucher.setGatewayId(dataVoucherPromo.getGatewayId());
            dataVoucher.setMessageSuccess(dataVoucherPromo.getMessage().getText());
            dataVoucher.setPromoCodeId(dataVoucherPromo.getPromoCodeId());
            dataVoucher.setSaldoAmount(dataVoucherPromo.getSaldoAmount());
            dataVoucher.setToken(dataVoucherPromo.getToken());
            dataVoucher.setTitleDescription(dataVoucherPromo.getTitleDescription());
            dataVoucher.setState(dataVoucherPromo.getMessage().getState());

            promoCodeCartListData.setDataVoucher(dataVoucher);
        }
        return promoCodeCartListData;
    }

    @Override
    public ResponseFirstStep convertPromoStackingCodeCartListData(Data data) {
        ResponseFirstStep responseFirstStep = new ResponseFirstStep();
        responseFirstStep.setData(data);

        return responseFirstStep;
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
            dataVoucher.setIsCoupon(dataResponse.getDataVoucher().getIsCoupon());
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

    @Override
    public PromoCodeCartShipmentData convertPromoCodeCartShipmentData(DataVoucher dataVoucherShipment) {
        PromoCodeCartShipmentData promoCodeCartShipmentData = new PromoCodeCartShipmentData();
        promoCodeCartShipmentData.setError(mapperUtil.isEmpty(dataVoucherShipment));
        promoCodeCartShipmentData.setErrorMessage(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        if (!mapperUtil.isEmpty(dataVoucherShipment)) {
            PromoCodeCartShipmentData.DataVoucher dataVoucher = new PromoCodeCartShipmentData.DataVoucher();
            int voucherAmount = 0;
            if(!TextUtils.isEmpty(dataVoucherShipment.getDiscountAmount())){
                voucherAmount = CurrencyFormatHelper.convertRupiahToInt(dataVoucherShipment.getDiscountAmount());
            }
            dataVoucher.setVoucherAmount(voucherAmount);
            dataVoucher.setVoucherPromoDesc(dataVoucherShipment.getMessage().getText());
            dataVoucher.setIsCoupon(dataVoucherShipment.isCoupon());
            dataVoucher.setState(dataVoucherShipment.getMessage().getState());
            dataVoucher.setTitleDescription(dataVoucherShipment.getTitleDescription());
            dataVoucher.setPromoCode(dataVoucherShipment.getCode());
            promoCodeCartShipmentData.setDataVoucher(dataVoucher);

        }
        return promoCodeCartShipmentData;
    }
}
