package com.tokopedia.review.feature.inbox.buyerreview.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.report.ReportReviewPojo;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.report.ReportReviewDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 9/13/17.
 */

public class ReportReviewMapper implements Func1<Response<TokopediaWsV4Response>, ReportReviewDomain> {

    @Override
    public ReportReviewDomain call(Response<TokopediaWsV4Response> response) {
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
                    String messageError = response.body().getErrorMessageJoined();
                    return mappingToDomain(messageError);
                } else {
                    throw new ErrorMessageException("");
                }
            }
        } else {
            String messageError = "";
            if (response.body() != null) {
                messageError = response.body().getErrorMessageJoined();
            }
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

    private ReportReviewDomain mappingToDomain(String errorMessage) {
        return new ReportReviewDomain(errorMessage);
    }
}
