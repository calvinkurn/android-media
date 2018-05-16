package com.tokopedia.checkout.domain.mapper;

import com.tokopedia.checkout.domain.datamodel.toppay.ThanksTopPayData;

/**
 * @author anggaprasetiyo on 06/03/18.
 */

public interface ITopPayMapper {

    ThanksTopPayData convertThanksTopPayData(
            com.tokopedia.transactiondata.entity.response.thankstoppaydata.ThanksTopPayData thanksTopPayDataResponse
    );
}
