package com.tokopedia.contactus.home.domain;

import com.tokopedia.contactus.home.data.TopBotStatus;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by sandeepgoyal on 24/04/18.
 */

public class ContactUsTopBotUseCase extends UseCase<TopBotStatus> {
    ITopBotRepository repository;

    public ContactUsTopBotUseCase(ITopBotRepository repository) {
        this.repository = repository;
    }
    @Override
    public Observable<TopBotStatus> createObservable(RequestParams requestParams) {
        return repository.getTopBotStatus();

    }
}
