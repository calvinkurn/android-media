package com.tokopedia.digital.cart.data.mapper;

import com.tokopedia.digital.cart.data.entity.response.ResponseCartData;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public interface ICartMapperData {

    CartDigitalInfoData transformCartInfoData(ResponseCartData responseCartData);
}
