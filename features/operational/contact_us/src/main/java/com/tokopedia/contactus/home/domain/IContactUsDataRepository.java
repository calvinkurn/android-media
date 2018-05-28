package com.tokopedia.contactus.home.domain;

import com.tokopedia.contactus.home.data.ContactUsArticleResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by sandeepgoyal on 03/04/18.
 */

public interface IContactUsDataRepository extends IPurchaseListRepository,ITopBotRepository {
     Observable<List<ContactUsArticleResponse>> getPopularArticle();
}
