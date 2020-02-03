package com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.sendreview.SendReviewValidatePojo;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewValidateDomain;
import com.tokopedia.tkpd.tkpdreputation.network.ErrorMessageException;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 8/31/17.
 */

public class SendReviewValidateMapper implements Func1<Response<TokopediaWsV4Response>,
        SendReviewValidateDomain> {

    @Override
    public SendReviewValidateDomain call(Response<TokopediaWsV4Response> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || !response.body().isNullData() && response.body().getErrorMessages() == null) {
                SendReviewValidatePojo data = response.body().convertDataObj(SendReviewValidatePojo.class);
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
            String messageError = null;
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

    private SendReviewValidateDomain mappingToDomain(SendReviewValidatePojo data) {
        return new SendReviewValidateDomain(
                data.getPostKey(),
                data.getReviewId(),
                data.getIsSuccess()
        );
    }
}
