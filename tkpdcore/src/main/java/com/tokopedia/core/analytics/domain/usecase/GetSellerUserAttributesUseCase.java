package com.tokopedia.core.analytics.domain.usecase;

import com.tokopedia.anals.SellerDrawerData;
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

public class GetSellerUserAttributesUseCase extends UseCase<SellerDrawerData.Data> {
    public static final String PARAM_USER_ID = "PARAM_USER_ID";
    protected UserAttributesRepository userAttributesRepository;

    public GetSellerUserAttributesUseCase(ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread,
                                          UserAttributesRepository userRepository) {
        super(threadExecutor, postExecutionThread);
        userAttributesRepository = userRepository;
    }

    @Override
    public Observable<SellerDrawerData.Data> createObservable(RequestParams requestParams) {
        return userAttributesRepository.getSellerUserAttributes(requestParams);
    }

    public RequestParams getUserAttrParam(SessionHandler sessionHandler) {
        RequestParams params = RequestParams.create();
        params.putInt(GetSellerUserAttributesUseCase.PARAM_USER_ID,
                Integer.parseInt(sessionHandler.getLoginID()));
        return params;
    }
}
