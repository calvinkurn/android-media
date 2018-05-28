package com.tokopedia.contactus.home.domain;

import com.tokopedia.contactus.home.data.ContactUsArticleResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by sandeepgoyal on 03/04/18.
 */

public class ContactUsArticleUseCase extends UseCase<List<ContactUsArticleResponse>> {
    IContactUsDataRepository repository;

    public ContactUsArticleUseCase(IContactUsDataRepository repository) {
        this.repository = repository;
    }


    @Override
    public Observable<List<ContactUsArticleResponse>> createObservable(RequestParams requestParams) {
        return repository.getPopularArticle();
    }
}
