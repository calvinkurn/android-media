package com.tokopedia.tkpd.tkpdcontactus.home.domain;

import com.tokopedia.tkpd.tkpdcontactus.common.data.BuyerPurchaseList;
import com.tokopedia.tkpd.tkpdcontactus.home.data.TopBotStatus;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

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
