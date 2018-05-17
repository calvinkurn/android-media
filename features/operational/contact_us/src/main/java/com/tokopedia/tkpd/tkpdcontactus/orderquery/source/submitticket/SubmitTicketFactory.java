package com.tokopedia.tkpd.tkpdcontactus.orderquery.source.submitticket;

import com.tokopedia.tkpd.tkpdcontactus.home.source.api.ContactUsAPI;

/**
 * Created by baghira on 16/05/18.
 */

public class SubmitTicketFactory {
    ContactUsAPI contactUsAPI;
    public SubmitTicketFactory(ContactUsAPI contactUsAPI) {
        this.contactUsAPI = contactUsAPI;
    }
    SubmitTicketDataStore getCloudSubmitTicketRepository(){
        return new SubmitTicketDataStore(contactUsAPI);
    };
}
