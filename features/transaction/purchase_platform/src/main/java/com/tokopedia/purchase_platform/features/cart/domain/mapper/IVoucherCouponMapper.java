package com.tokopedia.purchase_platform.features.cart.domain.mapper;

import com.tokopedia.purchase_platform.features.cart.domain.model.voucher.PromoCodeCartListData;
import com.tokopedia.promocheckout.common.domain.model.DataVoucher;

/**
 * @author anggaprasetiyo on 27/02/18.
 */

public interface IVoucherCouponMapper {

    PromoCodeCartListData convertPromoCodeCartListData(
            DataVoucher dataVoucher
    );

}
