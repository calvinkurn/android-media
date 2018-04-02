package com.tokopedia.gamification.domain;

import com.tokopedia.gamification.floatingtoken.model.TokenData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 3/29/18.
 */

public class GetTokenTokopointsUseCase extends UseCase<TokenData> {

    private IGamificationRepository repository;

    @Inject
    public GetTokenTokopointsUseCase(IGamificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<TokenData> createObservable(RequestParams requestParams) {
        return repository.getTokenTokopoints();
    }
}
