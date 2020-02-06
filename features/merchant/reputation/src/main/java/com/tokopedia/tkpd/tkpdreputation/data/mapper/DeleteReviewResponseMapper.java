package com.tokopedia.tkpd.tkpdreputation.data.mapper;

import android.content.Context;
import android.text.TextUtils;
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.tkpd.tkpdreputation.data.pojo.DeleteReviewResponsePojo;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.network.ErrorMessageException;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 9/27/17.
 */

public class DeleteReviewResponseMapper implements Func1<Response<TokopediaWsV4Response>, DeleteReviewResponseDomain> {

    @Override
    public DeleteReviewResponseDomain call(Response<TokopediaWsV4Response> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || !response.body().isNullData() && response.body().getErrorMessages() ==
                    null) {
                DeleteReviewResponsePojo data = response.body().convertDataObj(
                        DeleteReviewResponsePojo.class);
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

    private DeleteReviewResponseDomain mappingToDomain(DeleteReviewResponsePojo data) {
        return new DeleteReviewResponseDomain(data.getIsSuccess());
    }
}
