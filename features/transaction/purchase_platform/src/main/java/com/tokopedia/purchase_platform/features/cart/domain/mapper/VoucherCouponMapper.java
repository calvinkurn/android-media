package com.tokopedia.purchase_platform.features.cart.domain.mapper;

import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.promocheckout.common.domain.model.DataVoucher;
import com.tokopedia.purchase_platform.features.cart.domain.model.voucher.PromoCodeCartListData;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 27/02/18.
 */

public class VoucherCouponMapper implements IVoucherCouponMapper {

    @Inject
    public VoucherCouponMapper() {}

    @Override
    public PromoCodeCartListData convertPromoCodeCartListData(
            DataVoucher dataVoucherPromo
    ) {
        PromoCodeCartListData promoCodeCartListData = new PromoCodeCartListData();
        promoCodeCartListData.setError(dataVoucherPromo == null);
        promoCodeCartListData.setErrorMessage(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);

        if (dataVoucherPromo != null) {
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

}
