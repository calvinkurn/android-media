package com.tokopedia.topchat.chatlist.data.mapper;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatListViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 10/24/17.
 */

public class DeleteMessageMapper implements Func1<Response<TokopediaWsV4Response>, DeleteChatListViewModel> {

    @Inject
    public DeleteMessageMapper() {
    }

    @Override
    public DeleteChatListViewModel call(Response<TokopediaWsV4Response> response) {
        //TODO MAKE ERROR INTERCEPTOR
        if (response.isSuccessful()
                && (!response.body().isNullData()
                && response.body().getErrorMessageJoined().equals(""))
                || !response.body().isNullData() && response.body().getErrorMessages() == null) {
            DeleteChatListViewModel data = response.body().convertDataObj(DeleteChatListViewModel.class);
            return data;
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }
}
