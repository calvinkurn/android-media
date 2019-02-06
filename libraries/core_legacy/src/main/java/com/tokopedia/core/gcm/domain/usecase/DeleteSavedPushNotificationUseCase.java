package com.tokopedia.core.gcm.domain.usecase;

import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by alvarisi on 3/2/17.
 */

public class DeleteSavedPushNotificationUseCase extends UseCase<Boolean> {
    private final PushNotificationRepository pushNotificationRepository;

    public DeleteSavedPushNotificationUseCase(PushNotificationRepository pushNotificationRepository) {
        this.pushNotificationRepository = pushNotificationRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return pushNotificationRepository.clearPushNotificationStorage();
    }
}
