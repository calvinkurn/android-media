package com.tokopedia.topchat.chatroom.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.topchat.chatroom.domain.pojo.rating.SetChatRatingPojo;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by alvinatin on 28/03/18.
 */

public class SetChatRatingMapper implements
        Func1<Response<DataResponse<SetChatRatingPojo>>, SetChatRatingPojo> {

    @Inject
    public SetChatRatingMapper() {
    }

    @Override
    public SetChatRatingPojo call(Response<DataResponse<SetChatRatingPojo>>
                                          setChatRatingPojoResponse) {
        return getDataorError(setChatRatingPojoResponse);
    }

    private SetChatRatingPojo getDataorError(Response<DataResponse<SetChatRatingPojo>> response) {
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
