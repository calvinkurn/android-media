package com.tokopedia.core.drawer2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.drawer2.data.pojo.UserDrawerData;
import com.tokopedia.core.drawer2.data.repository.UserAttributesRepository;
import com.tokopedia.core.util.SessionHandler;

import rx.Observable;

/**
 * Created by Herdi_WORK on 03.10.17.
 */

public class GetUserAttributesUseCase extends UseCase<UserDrawerData> {
    public static final String PARAM_USER_ID = "PARAM_USER_ID";
    protected UserAttributesRepository userAttributesRepository;

    public GetUserAttributesUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    UserAttributesRepository userRepository) {
        super(threadExecutor, postExecutionThread);
        userAttributesRepository = userRepository;
    }

    @Override
    public Observable<UserDrawerData> createObservable(RequestParams requestParams) {
        return userAttributesRepository.getConsumerUserAttributes(requestParams);
    }

    public RequestParams getUserAttrParam(SessionHandler sessionHandler) {
        RequestParams params = RequestParams.create();
        params.putInt(GetUserAttributesUseCase.PARAM_USER_ID,
                Integer.parseInt(sessionHandler.getLoginID()));
        return params;
    }
}
