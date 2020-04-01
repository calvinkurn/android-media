package com.tokopedia.sellerhomedrawer.domain.usecase;

import com.tokopedia.sellerhomedrawer.data.drawernotification.NotificationModel;
import com.tokopedia.sellerhomedrawer.domain.repository.NotificationRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nisie on 5/5/17.
 */

public class NotificationUseCase extends UseCase<NotificationModel> {

    private final NotificationRepository notificationRepository;
    public static final String PARAM_TYPE = "type";

    @Inject
    public NotificationUseCase(NotificationRepository notificationRepository) {
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
