package com.tokopedia.topchat.chatroom.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.topchat.chatroom.domain.pojo.rating.SendReasonRatingPojo;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 6/11/18.
 */
public class SendReasonRatingMapper implements Func1<Response<DataResponse<SendReasonRatingPojo>>,
        SendReasonRatingPojo> {

    @Inject
    public SendReasonRatingMapper() {
    }

    @Override
    public SendReasonRatingPojo call(Response<DataResponse<SendReasonRatingPojo>> response) {
        if (response.isSuccessful()
                && response.body() != null
                && response.body().getData().isSuccess()) {
            return response.body().getData();
        } else {
            throw new ErrorMessageException(response.message(),
                    response.code());
        }
    }
}
