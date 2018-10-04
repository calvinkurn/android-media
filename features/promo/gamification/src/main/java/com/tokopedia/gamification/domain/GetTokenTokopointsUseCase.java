package com.tokopedia.gamification.domain;

import com.tokopedia.gamification.floating.view.model.TokenData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 3/29/18.
 */

public class GetTokenTokopointsUseCase extends UseCase<TokenData> {

    private IGamificationRepository repository;

    public GetTokenTokopointsUseCase(IGamificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<TokenData> createObservable(RequestParams requestParams) {
        return repository.getTokenTokopoints();
    }
}
