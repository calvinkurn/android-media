package com.tokopedia.core.drawer2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.drawer2.data.viewmodel.TopChatNotificationModel;
import com.tokopedia.core.drawer2.domain.NotificationRepository;

import rx.Observable;

/**
 * @author by nisie on 11/17/17.
 */

public class TopChatNotificationUseCase extends UseCase<TopChatNotificationModel> {

    private NotificationRepository notificationRepository;

    public TopChatNotificationUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread,
                                      NotificationRepository notificationRepository) {
        super(threadExecutor, postExecutionThread);
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Observable<TopChatNotificationModel> createObservable(RequestParams requestParams) {
        return notificationRepository.getNotificationTopChat(requestParams.getParameters());
    }
}
