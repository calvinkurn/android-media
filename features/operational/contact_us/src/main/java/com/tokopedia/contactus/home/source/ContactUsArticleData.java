package com.tokopedia.contactus.home.source;

import com.tokopedia.contactus.common.data.BuyerPurchaseList;
import com.tokopedia.contactus.home.data.ContactUsArticleResponse;
import com.tokopedia.contactus.home.data.TopBotStatus;
import com.tokopedia.contactus.home.domain.IContactUsDataRepository;

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

    @Override
    public Observable<List<BuyerPurchaseList>> getSellerPurchaseList() {
        return articleDataFactory.getCloudArticleRepository().getSellerPurchaseList();
    }

    @Override
    public Observable<TopBotStatus> getTopBotStatus() {
        return articleDataFactory.getCloudArticleRepository().getTopBotStatus();
    }
}
