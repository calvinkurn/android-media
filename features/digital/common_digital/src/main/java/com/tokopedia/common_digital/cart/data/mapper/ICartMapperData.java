package com.tokopedia.common_digital.cart.data.mapper;

import com.tokopedia.common_digital.cart.data.entity.response.ResponseCartData;
import com.tokopedia.common_digital.cart.data.entity.response.ResponseCheckoutData;
import com.tokopedia.common_digital.cart.view.model.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.CheckoutDigitalData;
import com.tokopedia.common_digital.common.MapperDataException;

/**
 * Created by Rizky on 27/08/18.
 */
public interface ICartMapperData {

    CartDigitalInfoData transformCartInfoData(
            ResponseCartData responseCartData
    ) throws MapperDataException;

    CheckoutDigitalData transformCheckoutData(
            ResponseCheckoutData responseCheckoutData
    ) throws MapperDataException;

}
