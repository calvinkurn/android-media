package com.tokopedia.topchat.chatlist.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.topchat.chatlist.data.repository.MessageRepository;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class GetMessageListUseCase extends UseCase<InboxChatViewModel>{

    MessageRepository messageRepository;

    public GetMessageListUseCase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 MessageRepository messageRepository) {
        super(threadExecutor, postExecutionThread);
        this.messageRepository = messageRepository;
    }

    @Override
    public Observable<InboxChatViewModel> createObservable(RequestParams requestParams) {
        return messageRepository.getMessage(requestParams.getParameters());
    }

    public static RequestParams generateParam(int page)
    {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("tab", "inbox");
        requestParams.putString("filter", "all");
        requestParams.putString("page", String.valueOf(page));
        requestParams.putString("per_page", "10");
        requestParams.putString("platform","android");
        return requestParams;
    }
}
