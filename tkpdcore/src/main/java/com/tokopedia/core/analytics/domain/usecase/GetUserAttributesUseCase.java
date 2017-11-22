package com.tokopedia.core.analytics.domain.usecase;

import com.tokopedia.anals.UserAttribute;
import com.tokopedia.core.analytics.data.UserAttributesRepository;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;

import rx.Observable;

/**
 * Created by Herdi_WORK on 03.10.17.
 */

public class GetUserAttributesUseCase extends UseCase<UserAttribute.Data>{
    public static final String PARAM_USER_ID = "PARAM_USER_ID";
    protected UserAttributesRepository userAttributesRepository;

    public GetUserAttributesUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    UserAttributesRepository userRepository){
        super(threadExecutor, postExecutionThread);
        userAttributesRepository = userRepository;
    }

    @Override
    public Observable<UserAttribute.Data> createObservable(RequestParams requestParams) {
        return userAttributesRepository.getUserAttributes(requestParams) ;
    }

    public RequestParams getUserAttrParam(SessionHandler sessionHandler){
        RequestParams params = RequestParams.create();
        params.putInt(GetUserAttributesUseCase.PARAM_USER_ID,
                Integer.parseInt(sessionHandler.getLoginID()));
        return params;
    }
}
