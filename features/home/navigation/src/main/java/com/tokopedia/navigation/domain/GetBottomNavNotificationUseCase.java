package com.tokopedia.navigation.domain;

import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by yfsx on 15/11/18.
 */
public class GetBottomNavNotificationUseCase extends UseCase<NotificationEntity> {

    private GetDrawerNotificationUseCase getNotificationUseCase;

    public GetBottomNavNotificationUseCase(GetDrawerNotificationUseCase getNotificationUseCase) {
        this.getNotificationUseCase = getNotificationUseCase;
    }

    @Override
    public Observable<NotificationEntity> createObservable(RequestParams requestParams) {
        return getNotificationUseCase.createObservable(requestParams);
    }
}
