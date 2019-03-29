package com.tokopedia.core.gcm.domain.usecase;

import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by alvarisi on 2/23/17.
 */

public class SavePushNotificationUseCase extends UseCase<String> {
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_RESPONSE = "response";
    public static final String KEY_CUSTOM_INDEX = "custom_index";
    public static final String KEY_SERVER_ID = "server_id";
    private static final String DEFAULT_EMPTY = "";
    private static final String DEFAULT_CATEGORY = "misc";

    private final PushNotificationRepository pushNotificationRepository;

    public SavePushNotificationUseCase(PushNotificationRepository pushNotificationRepository) {
        this.pushNotificationRepository = pushNotificationRepository;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        String category = requestParams.getString(KEY_CATEGORY, DEFAULT_CATEGORY);
        String response = requestParams.getString(KEY_RESPONSE, DEFAULT_EMPTY);
        String customIndex = requestParams.getString(KEY_CUSTOM_INDEX, DEFAULT_EMPTY);
        String serverId = requestParams.getString(KEY_SERVER_ID, DEFAULT_EMPTY);
        return pushNotificationRepository.storePushNotification(category, response, customIndex, serverId);
    }
}
