package com.tokopedia.tkpd.tkpdcontactus.home.source;

import com.tokopedia.tkpd.tkpdcontactus.common.data.BuyerPurchaseList;
import com.tokopedia.tkpd.tkpdcontactus.home.data.ContactUsArticleResponse;
import com.tokopedia.tkpd.tkpdcontactus.home.domain.IContactUsDataRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by sandeepgoyal on 03/04/18.
 */

public class ContactUsArticleData implements IContactUsDataRepository {
    ContactUsArticleDataFactory articleDataFactory;

    public ContactUsArticleData(ContactUsArticleDataFactory articleDataFactory) {
        this.articleDataFactory = articleDataFactory;
    }

    @Override
    public Observable<List<ContactUsArticleResponse>> getPopularArticle() {
        return articleDataFactory.getCloudArticleRepository().getPopularArticle();
    }

    @Override
    public Observable<List<BuyerPurchaseList>> getPurchaseList() {
        return articleDataFactory.getCloudArticleRepository().getBuyerPurchaseList();
    }
}
