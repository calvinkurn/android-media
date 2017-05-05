package com.tokopedia.core.drawer2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.drawer2.domain.NotificationRepository;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;

import rx.Observable;

/**
 * Created by nisie on 5/5/17.
 */

public class NotificationUseCase extends UseCase<NotificationModel> {

    private final NotificationRepository notificationRepository;

    public NotificationUseCase(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread,
                               NotificationRepository notificationRepository) {
        super(threadExecutor, postExecutionThread);
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Observable<NotificationModel> createObservable(RequestParams requestParams) {
        return notificationRepository.getNotification(requestParams.getParameters());
    }
}
