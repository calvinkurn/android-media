package com.tokopedia.topchat.chatroom.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.topchat.chatroom.domain.pojo.getuserstatus.GetUserStatusDataPojo;
import com.tokopedia.topchat.chatroom.domain.pojo.getuserstatus.GetUserStatusResponsePojo;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by Hendri on 31/07/18.
 */
public class GetUserStatusMapper implements Func1<Response<GetUserStatusResponsePojo>,
        GetUserStatusDataPojo> {

    @Inject
    public GetUserStatusMapper() {
    }

    @Override
    public GetUserStatusDataPojo call(Response<GetUserStatusResponsePojo> response) {
        if (response.isSuccessful()
                && response.body() != null
                && response.body().getData() != null) {
            return response.body().getData();
        } else {
            throw new ErrorMessageException(response.message(),
                    response.code());
        }
    }
}
