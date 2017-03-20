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

public class DeleteSavedPushNotificationByCategoryUseCase extends UseCase<Boolean> {
    public static final String PARAM_CATEGORY = "category";
    public final String EMPTY_FIELD = "";
    private final PushNotificationRepository pushNotificationRepository;
    public DeleteSavedPushNotificationByCategoryUseCase(ThreadExecutor threadExecutor,
                                                        PostExecutionThread postExecutionThread,
                                                        PushNotificationRepository pushNotificationRepository) {
        super(threadExecutor, postExecutionThread);
        this.pushNotificationRepository = pushNotificationRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        String category = requestParams.getString(PARAM_CATEGORY, EMPTY_FIELD);
        if (!category.equalsIgnoreCase(EMPTY_FIELD)) {
            return pushNotificationRepository.clearPushNotificationStorage(category);
        }else {
            return Observable.empty();
        }
    }
}
