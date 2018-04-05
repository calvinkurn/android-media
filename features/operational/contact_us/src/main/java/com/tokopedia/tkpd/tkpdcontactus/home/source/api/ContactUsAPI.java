package com.tokopedia.tkpd.tkpdcontactus.home.source.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpd.tkpdcontactus.home.data.ContactUsArticleResponse;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

public interface ContactUsAPI {

    @GET(ContactUsURL.POPULAR_ARTICLE)
    Observable<Response<List<ContactUsArticleResponse>>> getPopularArticle();

}