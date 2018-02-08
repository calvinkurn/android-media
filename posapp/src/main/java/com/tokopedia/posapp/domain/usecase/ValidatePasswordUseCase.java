package com.tokopedia.posapp.domain.usecase;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.session.model.ErrorModel;
import com.tokopedia.posapp.data.repository.AccountRepository;
import com.tokopedia.posapp.domain.model.CheckPasswordDomain;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/27/17.
 */

public class ValidatePasswordUseCase extends UseCase<CheckPasswordDomain> {
    private AccountRepository accountRepository;

    public ValidatePasswordUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   AccountRepository accountRepository) {
        super(threadExecutor, postExecutionThread);
        this.accountRepository = accountRepository;
    }

    @Override
    public Observable<CheckPasswordDomain> createObservable(RequestParams requestParams) {
        return accountRepository.validatePassword(requestParams);
    }
}
