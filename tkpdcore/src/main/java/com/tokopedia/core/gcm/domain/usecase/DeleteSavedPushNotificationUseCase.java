package com.tokopedia.core.gcm.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.gcm.domain.PushNotificationRepository;

import rx.Observable;

/**
 * Created by alvarisi on 3/2/17.
 */

public class DeleteSavedPushNotificationUseCase extends UseCase<Boolean> {
    private final PushNotificationRepository pushNotificationRepository;
    public DeleteSavedPushNotificationUseCase(ThreadExecutor threadExecutor,
                                              PostExecutionThread postExecutionThread,
                                              PushNotificationRepository pushNotificationRepository) {
        super(threadExecutor, postExecutionThread);
        this.pushNotificationRepository = pushNotificationRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return pushNotificationRepository.clearPushNotificationStorage();
    }
}
