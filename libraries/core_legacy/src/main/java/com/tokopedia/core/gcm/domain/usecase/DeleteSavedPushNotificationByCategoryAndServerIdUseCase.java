package com.tokopedia.core.gcm.domain.usecase;

import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by alvarisi on 3/3/17.
 */

public class DeleteSavedPushNotificationByCategoryAndServerIdUseCase extends UseCase<Boolean> {
    public static final String PARAM_CATEGORY = "category";
    public static final String PARAM_SERVER_ID = "serverId";
    public final String EMPTY_FIELD = "";
    private final PushNotificationRepository pushNotificationRepository;
    public DeleteSavedPushNotificationByCategoryAndServerIdUseCase(
                                                        PushNotificationRepository pushNotificationRepository) {
        this.pushNotificationRepository = pushNotificationRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        String category = requestParams.getString(PARAM_CATEGORY, EMPTY_FIELD);
        String serverId = requestParams.getString(PARAM_SERVER_ID, EMPTY_FIELD);
        if (!category.equalsIgnoreCase(EMPTY_FIELD)) {
            return pushNotificationRepository.clearPushNotificationStorage(category, serverId);
        }else {
            return Observable.empty();
        }
    }
}
