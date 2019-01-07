package com.tokopedia.topchat.chatroom.domain;

import com.tokopedia.topchat.chatroom.domain.pojo.replyaction.ReplyActionData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by stevenfredian on 9/12/17.
 */

public class ReplyMessageUseCase extends UseCase<ReplyActionData> {

//    private final ReplyRepository replyRepository;

    @Inject
    public ReplyMessageUseCase() {
        super();
    }

    @Override
    public Observable<ReplyActionData> createObservable(RequestParams requestParams) {
//        return replyRepository.replyMessage(requestParams.getParameters());
        return null;
    }

    public static RequestParams generateParam(String messageId, String messageReply)
    {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("msg_id", messageId);
        requestParams.putString("message_reply", messageReply);
        return requestParams;
    }

    public static RequestParams generateParamAttachImage(String messageId, String filePath)
    {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("msg_id", messageId);
        requestParams.putString("message_reply", "Uploaded Image");
        requestParams.putString("file_path", filePath);
        requestParams.putInt("attachment_type",2);
        return requestParams;
    }
}
