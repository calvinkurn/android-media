package com.tokopedia.topchat.chatroom.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.topchat.chatroom.data.repository.ReplyRepository;
import com.tokopedia.topchat.chatroom.domain.pojo.existingchat.ExistingChatPojo;


import rx.Observable;

/**
 * Created by Hendri on 11/06/18.
 */
public class GetExistingChatUseCase extends UseCase<ExistingChatPojo> {

    private final ReplyRepository replyRepository;

    public GetExistingChatUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ReplyRepository replyRepository) {
        super(threadExecutor, postExecutionThread);
        this.replyRepository = replyRepository;
    }

    @Override
    public Observable<ExistingChatPojo> createObservable(RequestParams requestParams) {
        return replyRepository.getExistingChat(requestParams.getParameters());
    }

    public static RequestParams generateParam(boolean isUserAskShop, String
            destinationId, String source)
    {
        RequestParams requestParams = RequestParams.create();
        if(isUserAskShop) requestParams.putString("to_shop_id",destinationId);
        else requestParams.putString("to_user_id",destinationId);

        requestParams.putString("source",source);
        return requestParams;
    }
}
