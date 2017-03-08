package com.tokopedia.digital.cart.data.mapper;

import com.tokopedia.digital.cart.data.entity.response.ResponseCartData;
import com.tokopedia.digital.cart.data.entity.response.ResponseVoucherData;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;
import com.tokopedia.digital.cart.model.VoucherDigital;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public interface ICartMapperData {

    CartDigitalInfoData transformCartInfoData(ResponseCartData responseCartData);

    VoucherDigital transformVoucherDigitalData(ResponseVoucherData responseVoucherData);
}
