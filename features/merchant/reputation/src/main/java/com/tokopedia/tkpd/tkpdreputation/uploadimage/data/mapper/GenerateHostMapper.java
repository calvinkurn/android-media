package com.tokopedia.tkpd.tkpdreputation.uploadimage.data.mapper;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.model.GenerateHostDomain;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.pojo.GenerateHostPojo;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 9/5/17.
 */

public class GenerateHostMapper implements Func1<Response<TkpdResponse>, GenerateHostDomain> {
    @Override
    public GenerateHostDomain call(Response<TkpdResponse> response) {

        if (response.isSuccessful()) {
            if (!response.body().isNullData()) {
                GenerateHostPojo data = response.body()
                        .convertDataObj(GenerateHostPojo.class);
                return mappingToDomain(data);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException(MainApplication.getAppContext().getString
                            (R.string.default_request_error_unknown));
                }
            }
        } else {
            if (response.body() == null ||
                    (response.body().getErrorMessages() == null
                            && response.body().getErrorMessages().isEmpty())) {
                throw new RuntimeException(String.valueOf(response.code()));
            } else {
                throw new ErrorMessageException(response.body().getErrorMessageJoined());
            }
        }
    }

    private GenerateHostDomain mappingToDomain(GenerateHostPojo data) {
        return new GenerateHostDomain(
                data.getGeneratedHost().getUploadHost(),
                data.getGeneratedHost().getServerId()
        );
    }
}
