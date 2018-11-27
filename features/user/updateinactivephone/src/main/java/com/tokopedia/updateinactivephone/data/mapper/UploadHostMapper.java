package com.tokopedia.updateinactivephone.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.updateinactivephone.model.request.UploadHostModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

public class UploadHostMapper implements Func1<Response<TkpdResponse>, UploadHostModel> {

    @Inject
    public UploadHostMapper() {

    }

    @Override
    public UploadHostModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private UploadHostModel mappingResponse(Response<TkpdResponse> response) {
        UploadHostModel model = new UploadHostModel();

        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                UploadHostModel.UploadHostData data = response.body().convertDataObj(UploadHostModel.UploadHostData.class);
                model.setSuccess(true);
                model.setUploadHostData(data);
            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    model.setSuccess(false);
                } else {
                    model.setSuccess(false);
                    model.setErrorMessage(response.body().getErrorMessageJoined());
                }
            }
            model.setStatusMessage(response.body().getStatusMessageJoined());
        } else {
            model.setSuccess(false);
        }
        model.setResponseCode(response.code());
        return model;
    }
}
