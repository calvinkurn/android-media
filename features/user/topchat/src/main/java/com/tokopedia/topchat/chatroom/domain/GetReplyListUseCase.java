package com.tokopedia.topchat.chatroom.domain;

import com.tokopedia.topchat.chatroom.data.repository.ReplyRepository;
import com.tokopedia.topchat.chatroom.view.viewmodel.ChatRoomViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by stevenfredian on 9/12/17.
 */

public class GetReplyListUseCase extends UseCase<ChatRoomViewModel> {

    public static final String PARAM_GET_ALL = "GET_ALL";

    private final ReplyRepository replyRepository;

    @Inject
    public GetReplyListUseCase(ReplyRepository replyRepository) {
        super();
        this.replyRepository = replyRepository;
    }

    @Override
    public Observable<ChatRoomViewModel> createObservable(RequestParams requestParams) {
        return replyRepository.getReply(String.valueOf(requestParams.getParameters().get("msg_id")), requestParams.getParameters());
    }

    public static RequestParams generateParam(String messageId, int page)
    {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("msg_id", messageId);
        requestParams.putString("page", String.valueOf(page));
        requestParams.putString("platform","android");
        requestParams.putString("per_page","10");
        return requestParams;
    }

    public static RequestParams generateParamSearch(String messageId)
    {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("msg_id", messageId);
        requestParams.putString("page", "0");
        requestParams.putString("platform","android");
        return requestParams;
    }
}
