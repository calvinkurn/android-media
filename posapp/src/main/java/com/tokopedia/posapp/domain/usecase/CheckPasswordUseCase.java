package com.tokopedia.posapp.domain.usecase;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.session.model.ErrorModel;
import com.tokopedia.posapp.domain.model.CheckPasswordDomain;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/27/17.
 */

public class CheckPasswordUseCase extends UseCase<CheckPasswordDomain> {
    private AccountsService accountsService;

    public CheckPasswordUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                AccountsService accountsService) {
        super(threadExecutor, postExecutionThread);
        this.accountsService = accountsService;
    }

    @Override
    public Observable<CheckPasswordDomain> createObservable(RequestParams requestParams) {
        return accountsService.getApi()
                .getToken(requestParams.getParamsAllValueInString())
                .map(new Func1<Response<String>, CheckPasswordDomain>() {
                    @Override
                    public CheckPasswordDomain call(Response<String> stringResponse) {
                        ErrorModel errorModel = new GsonBuilder().create().fromJson(stringResponse.body(), ErrorModel.class);
                        CheckPasswordDomain checkPasswordDomain = new CheckPasswordDomain();

                        if (errorModel.getError() == null) {
                            checkPasswordDomain.setStatus(true);
                        } else {
                            checkPasswordDomain.setStatus(false);
                            checkPasswordDomain.setMessage(errorModel.getErrorDescription());
                            checkPasswordDomain.setState(errorModel.getState());
                        }
                        return checkPasswordDomain;
                    }
                });
    }
}
