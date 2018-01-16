package com.tokopedia.core.drawer2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.drawer2.domain.NotificationRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nisie on 5/5/17.
 */

public class NotificationUseCase extends UseCase<NotificationModel> {

    private final NotificationRepository notificationRepository;
    private static final String PARAM_TYPE = "type";

    @Inject
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

    public static RequestParams getRequestParam(boolean isSeller) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_TYPE, isSeller ? 2 : 1);
        return params;
    }
}
