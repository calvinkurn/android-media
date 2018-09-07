package com.tokopedia.updateinactivephone.data.mapper;

import com.tokopedia.updateinactivephone.model.request.UploadImageModel;
import com.tokopedia.updateinactivephone.model.response.UploadImageData;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

public class UploadImageMapper implements Func1<Response<UploadImageData>, UploadImageModel> {

    @Inject
    public UploadImageMapper() {

    }

    @Override
    public UploadImageModel call(Response<UploadImageData> response) {
        return mappingResponse(response);
    }

    private UploadImageModel mappingResponse(Response<UploadImageData> response) {
        UploadImageModel model = new UploadImageModel();

        if (response.isSuccessful()) {

            if (response.body() != null) {
                UploadImageData data = response.body();
                model.setSuccess(true);
                model.setUploadImageData(data);
            } else {
                model.setSuccess(false);

            }
        } else {
            model.setSuccess(false);
        }
        model.setResponseCode(response.code());
        return model;
    }
}
