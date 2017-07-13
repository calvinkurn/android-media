package com.tokopedia.core.drawer2.data.mapper;

import com.tokopedia.core.drawer2.data.pojo.toppoints.TopPointsData;
import com.tokopedia.core.drawer2.data.pojo.toppoints.TopPointsModel;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 5/5/17.
 */

public class TopPointsMapper implements Func1<Response<TkpdResponse>, TopPointsModel> {
    @Override
    public TopPointsModel call(Response<TkpdResponse> response) {
        TopPointsModel model = new TopPointsModel();

        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                TopPointsData data = response.body().convertDataObj(TopPointsData.class);
                model.setSuccess(true);
                model.setTopPointsData(data);
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
