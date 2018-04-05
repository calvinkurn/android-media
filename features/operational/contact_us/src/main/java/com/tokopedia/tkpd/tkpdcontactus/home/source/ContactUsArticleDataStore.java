package com.tokopedia.tkpd.tkpdcontactus.home.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpd.tkpdcontactus.home.data.ContactUsArticleResponse;
import com.tokopedia.tkpd.tkpdcontactus.home.source.api.ContactUsAPI;


import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sandeepgoyal on 03/04/18.
 */

public class ContactUsArticleDataStore {

    ContactUsAPI contactUsAPI;


    public ContactUsArticleDataStore(ContactUsAPI contactUsAPI) {
        this.contactUsAPI = contactUsAPI;
    }

    public Observable<List<ContactUsArticleResponse>> getPopularArticle() {
        return this.contactUsAPI.getPopularArticle().map(new Func1<Response<List<ContactUsArticleResponse>>, List<ContactUsArticleResponse>>() {
            @Override
            public List<ContactUsArticleResponse> call(Response<List<ContactUsArticleResponse>> dataResponseResponse) {
                return dataResponseResponse.body().subList(0,5);
            }
        });
    }
}
