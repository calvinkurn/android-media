package com.tokopedia.updateinactivephone.data.mapper;

import com.tokoepdia.updateinactivephone.model.response.UploadImageData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.updateinactivephone.model.request.UploadImageModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

public class UploadImageMapper implements Func1<Response<TkpdResponse>, UploadImageModel> {

    @Inject
    public UploadImageMapper(){

    }

    @Override
    public UploadImageModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private UploadImageModel mappingResponse(Response<TkpdResponse> response) {
        UploadImageModel model = new UploadImageModel();

        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                UploadImageData data = response.body().convertDataObj(UploadImageData.class);
                model.setSuccess(true);
                model.setUploadImageData(data);
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
