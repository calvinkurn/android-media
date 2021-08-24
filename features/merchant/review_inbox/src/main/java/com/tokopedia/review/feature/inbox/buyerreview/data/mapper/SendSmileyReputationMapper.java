package com.tokopedia.review.feature.inbox.buyerreview.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.SendSmileyPojo;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendSmileyReputationDomain;
import com.tokopedia.review.feature.inbox.buyerreview.network.ErrorMessageException;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 8/31/17.
 */

public class SendSmileyReputationMapper implements Func1<Response<TokopediaWsV4Response>,
        SendSmileyReputationDomain> {
    @Override
    public SendSmileyReputationDomain call(Response<TokopediaWsV4Response> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || !response.body().isNullData() && response.body().getErrorMessages() == null) {
                SendSmileyPojo data = response.body().convertDataObj(SendSmileyPojo.class);
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

    private SendSmileyReputationDomain mappingToDomain(SendSmileyPojo data) {
        return new SendSmileyReputationDomain(data.getIsSuccess());
    }
}
