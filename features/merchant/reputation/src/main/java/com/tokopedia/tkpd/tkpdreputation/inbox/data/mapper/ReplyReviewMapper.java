package com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail.ReplyReviewPojo;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendReplyReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.network.ErrorMessageException;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 9/28/17.
 */

public class ReplyReviewMapper implements Func1<Response<TokopediaWsV4Response>, SendReplyReviewDomain> {
    @Override
    public SendReplyReviewDomain call(Response<TokopediaWsV4Response> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || !response.body().isNullData() && response.body().getErrorMessages() == null) {
                ReplyReviewPojo data = response.body().convertDataObj(ReplyReviewPojo.class);
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

    private SendReplyReviewDomain mappingToDomain(ReplyReviewPojo data) {
        return new SendReplyReviewDomain(data.getIsSuccess() == 1);
    }
}
