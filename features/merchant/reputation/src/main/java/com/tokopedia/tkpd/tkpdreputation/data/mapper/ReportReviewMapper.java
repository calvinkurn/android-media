package com.tokopedia.tkpd.tkpdreputation.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.data.pojo.ReportReviewPojo;
import com.tokopedia.tkpd.tkpdreputation.domain.model.ReportReviewDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 9/13/17.
 */

public class ReportReviewMapper implements Func1<Response<TkpdResponse>, ReportReviewDomain> {

    @Override
    public ReportReviewDomain call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || !response.body().isNullData() && response.body().getErrorMessages() ==
                    null) {
                ReportReviewPojo data = response.body().convertDataObj(ReportReviewPojo.class);
                return mappingToDomain(data);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException("");
                }
            }
        } else {
            String messageError = ErrorHandler.getErrorMessage(response);
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private ReportReviewDomain mappingToDomain(ReportReviewPojo data) {
        return new ReportReviewDomain(data.getIsSuccess());
    }
}
