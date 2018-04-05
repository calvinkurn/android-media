package com.tokopedia.tkpd.tkpdcontactus.home.source;

import com.tokopedia.tkpd.tkpdcontactus.home.source.api.ContactUsAPI;

import rx.Observable;


/**
 * Created by sandeepgoyal on 03/04/18.
 */

public class ContactUsArticleDataFactory {
    ContactUsAPI contactUsAPI;

    public ContactUsArticleDataFactory(ContactUsAPI contactUsAPI) {
        this.contactUsAPI = contactUsAPI;
    }

    public ContactUsArticleDataStore getCloudArticleRepository()  {
        return new ContactUsArticleDataStore(contactUsAPI);
    }

}
