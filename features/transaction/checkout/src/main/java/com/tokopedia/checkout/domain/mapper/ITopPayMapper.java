package com.tokopedia.checkout.domain.mapper;

import com.tokopedia.checkout.domain.datamodel.toppay.ThanksTopPayData;

/**
 * @author anggaprasetiyo on 06/03/18.
 */

public interface ITopPayMapper {

    ThanksTopPayData convertThanksTopPayData(
            com.tokopedia.transaction.common.data.cart.thankstoppaydata.ThanksTopPayData thanksTopPayDataResponse
    );
}
