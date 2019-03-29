package com.tokopedia.navigation.domain;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by yfsx on 15/11/18.
 */
public class GetBottomNavNotificationUseCase extends UseCase<NotificationEntity> {

    private GetDrawerNotificationUseCase getNotificationUseCase;
    private GetNewFeedCheckerUseCase getNewFeedCheckerUseCase;

    public GetBottomNavNotificationUseCase(
            GetDrawerNotificationUseCase getNotificationUseCase,
            GetNewFeedCheckerUseCase getNewFeedCheckerUseCase) {
        this.getNotificationUseCase = getNotificationUseCase;
        this.getNewFeedCheckerUseCase = getNewFeedCheckerUseCase;
    }

    @Override
    public Observable<NotificationEntity> createObservable(RequestParams requestParams) {
        return getNewFeedCheckerUseCase.createObservable(requestParams)
                .flatMap((Func1<RequestParams, Observable<NotificationEntity>>) requestParams1 ->
                        getNotificationUseCase.createObservable(requestParams1));
    }
}
