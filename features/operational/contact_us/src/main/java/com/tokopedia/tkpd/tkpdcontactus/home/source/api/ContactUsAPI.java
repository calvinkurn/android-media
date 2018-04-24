package com.tokopedia.tkpd.tkpdcontactus.home.source.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpd.tkpdcontactus.common.api.ContactUsURL;
import com.tokopedia.tkpd.tkpdcontactus.home.data.BuyerPurchaseData;
import com.tokopedia.tkpd.tkpdcontactus.home.data.ContactUsArticleResponse;
import com.tokopedia.tkpd.tkpdcontactus.home.data.TopBotStatus;

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

    @GET(ContactUsURL.BUYER_LIST)
    Observable<Response<DataResponse<BuyerPurchaseData>>> getBuyerPurchaseList();

    @GET(ContactUsURL.TOPBOT_STATUS)
    Observable<Response<DataResponse<TopBotStatus>>> getTopBotStatus();
}