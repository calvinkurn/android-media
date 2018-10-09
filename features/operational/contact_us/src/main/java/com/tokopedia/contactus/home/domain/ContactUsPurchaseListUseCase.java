package com.tokopedia.contactus.home.domain;

import com.tokopedia.contactus.common.data.BuyerPurchaseList;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sandeepgoyal on 06/04/18.
 */

public class ContactUsPurchaseListUseCase extends UseCase<List<BuyerPurchaseList>> {
    IPurchaseListRepository repository;

    @Inject
    public ContactUsPurchaseListUseCase(IPurchaseListRepository repository) {
        this.repository = repository;
    }
    @Override
    public Observable<List<BuyerPurchaseList>> createObservable(RequestParams requestParams) {
        return repository.getPurchaseList();
    }
}
