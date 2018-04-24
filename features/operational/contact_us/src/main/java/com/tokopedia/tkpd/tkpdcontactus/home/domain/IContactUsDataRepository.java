package com.tokopedia.tkpd.tkpdcontactus.home.domain;

import com.tokopedia.tkpd.tkpdcontactus.home.data.ContactUsArticleResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by sandeepgoyal on 03/04/18.
 */

public interface IContactUsDataRepository extends IPurchaseListRepository,ITopBotRepository{
     Observable<List<ContactUsArticleResponse>> getPopularArticle();
}
