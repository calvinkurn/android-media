package com.tokopedia.core.drawer2.data.mapper;

import com.tokopedia.core.drawer2.data.pojo.deposit.DepositData;
import com.tokopedia.core.drawer2.data.pojo.deposit.DepositModel;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 5/4/17.
 */

public class DepositMapper implements Func1<Response<TkpdResponse>, DepositModel> {

    @Override
    public DepositModel call(Response<TkpdResponse> response) {
        DepositModel model = new DepositModel();

        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                DepositData data = response.body().convertDataObj(DepositData.class);
                model.setSuccess(true);
                model.setDepositData(data);
            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    model.setSuccess(false);
                } else {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                }
            }
            model.setStatusMessage(response.body().getStatusMessageJoined());
            model.setResponseCode(response.code());
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }
}
