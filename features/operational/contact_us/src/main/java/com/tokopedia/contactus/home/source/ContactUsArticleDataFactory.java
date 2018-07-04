package com.tokopedia.contactus.home.source;

import com.tokopedia.contactus.home.source.api.ContactUsAPI;


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
