package com.tokopedia.tokocash.activation.domain;

import com.tokopedia.tokocash.activation.data.ActivateRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 16/08/18.
 *  Please don't call this usecase outside SuccessActivateTokocashPresenter
 *
 */
public class GetRefreshWalletTokenUseCase extends UseCase<String> {

    private ActivateRepository repository;

    public GetRefreshWalletTokenUseCase(ActivateRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return repository.getRefreshWalletToken();
    }
}
