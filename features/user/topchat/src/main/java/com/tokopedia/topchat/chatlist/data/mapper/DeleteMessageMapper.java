package com.tokopedia.topchat.chatlist.data.mapper;

import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatListUiModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 10/24/17.
 */

public class DeleteMessageMapper implements Func1<Response<DataResponse<DeleteChatListUiModel>>, DeleteChatListUiModel> {

    @Inject
    public DeleteMessageMapper() {
    }

    @Override
    public DeleteChatListUiModel call(Response<DataResponse<DeleteChatListUiModel>> response) {
        if (response.isSuccessful() &&
                response.body().getHeader() == null ||
                (response.body().getHeader() != null && response.body().getHeader().getMessages().isEmpty()
                ) || (response.body().getHeader() != null && response.body().getHeader().getMessages().get(0).equals(""))) {
            DeleteChatListUiModel data = response.body().getData();
            return data;
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }
}
