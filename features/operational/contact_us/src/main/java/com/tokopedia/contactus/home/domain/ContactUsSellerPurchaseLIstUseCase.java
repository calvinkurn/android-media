package com.tokopedia.contactus.home.domain;

import com.tokopedia.contactus.common.data.BuyerPurchaseList;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sandeepgoyal on 14/05/18.
 */

public class ContactUsSellerPurchaseLIstUseCase extends UseCase<List<BuyerPurchaseList>> {
    IContactUsDataRepository repository;

    @Inject
    public ContactUsSellerPurchaseLIstUseCase(IContactUsDataRepository repository) {
        this.repository = repository;
    }
    @Override
    public Observable<List<BuyerPurchaseList>> createObservable(RequestParams requestParams) {
        return repository.getSellerPurchaseList();
    }
}
