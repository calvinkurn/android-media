package com.tokopedia.core.drawer2.data.mapper;

import com.tokopedia.anals.GetDepositQuery;
import com.tokopedia.core.drawer2.data.pojo.deposit.DepositData;
import com.tokopedia.core.drawer2.data.pojo.deposit.DepositModel;
import com.tokopedia.core.network.ErrorMessageException;

import rx.functions.Func1;

/**
 * Created by nisie on 5/4/17.
 */

public class DepositMapper implements Func1<GetDepositQuery.Data, DepositModel> {

    @Override
    public DepositModel call(GetDepositQuery.Data response) {
        DepositModel model = new DepositModel();

        String depositFormat = response.saldo().deposit_fmt();
        if (!depositFormat.equalsIgnoreCase("ERROR FAIL")) {
            model.setSuccess(true);
            DepositData data = new DepositData();
            data.setDepositTotal(depositFormat);
            model.setDepositData(data);
        } else {
            throw new ErrorMessageException(depositFormat);
        }
        return model;
    }
}
