package com.tokopedia.core.drawer2.domain.interactor;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.drawer2.data.pojo.UserData;
import com.tokopedia.core.drawer2.data.repository.UserAttributesRepository;
import com.tokopedia.core.util.SessionHandler;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by Herdi_WORK on 03.10.17.
 */

public class GetSellerUserAttributesUseCase extends UseCase<UserData> {
    public static final String PARAM_USER_ID = "userID";
    private static final String OPERATION_NAME_VALUE = "SellerDrawerData";
    protected UserAttributesRepository userAttributesRepository;

    public GetSellerUserAttributesUseCase(ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread,
                                          UserAttributesRepository userRepository) {
        super(threadExecutor, postExecutionThread);
        userAttributesRepository = userRepository;
    }

    @Override
    public Observable<UserData> createObservable(RequestParams requestParams) {
        return userAttributesRepository.getSellerUserAttributes(requestParams);
    }

    public RequestParams getUserAttrParam(String loginId, String query) {
        Map<String, Object> variables = new HashMap<>();

        variables.put(PARAM_USER_ID,
                Integer.parseInt(loginId));

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(GraphqlHelper.QUERY, query);
        requestParams.putObject(GraphqlHelper.VARIABLES, variables);
        requestParams.putObject(GraphqlHelper.OPERATION_NAME, OPERATION_NAME_VALUE);
        return requestParams;
    }
}
