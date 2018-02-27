package com.tokopedia.core.drawer2.data.mapper;

import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 5/5/17.
 */

public class TokoCashMapper implements Func1<Response<TkpdResponse>, TokoCashModel> {
    @Override
    public TokoCashModel call(Response<TkpdResponse> response) {
        TokoCashModel model = new TokoCashModel();

        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                TokoCashData data = response.body().convertDataObj(TokoCashData.class);
                model.setSuccess(true);
                model.setData(data);
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
