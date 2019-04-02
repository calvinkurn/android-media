package com.tokopedia.digital.product.data.mapper;

import com.tokopedia.digital.exception.MapperDataException;
import com.tokopedia.digital.product.data.entity.response.ResponsePulsaBalance;
import com.tokopedia.digital.product.view.model.PulsaBalance;

/**
 * Created by Rizky on 19/01/18.
 */

public class USSDMapper {

    public PulsaBalance transformPulsaBalance(
            ResponsePulsaBalance responsePulsaBalance) throws MapperDataException {
        return new PulsaBalance.Builder()
                .mobileBalance(responsePulsaBalance.getAttributes().getBalance())
                .plainBalance(responsePulsaBalance.getAttributes().getBalancePlain())
                .success(responsePulsaBalance.getAttributes().isSuccess())
                .expireDate(responsePulsaBalance.getAttributes().getExpireDate()).build();
    }

}
