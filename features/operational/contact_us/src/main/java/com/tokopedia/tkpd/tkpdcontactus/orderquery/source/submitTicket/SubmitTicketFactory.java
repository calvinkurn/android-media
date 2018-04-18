package com.tokopedia.tkpd.tkpdcontactus.orderquery.source.submitTicket;

import com.tokopedia.tkpd.tkpdcontactus.orderquery.source.api.SubmitQueryAPi;

/**
 * Created by sandeepgoyal on 17/04/18.
 */

public class SubmitTicketFactory {
    SubmitQueryAPi submitQueryAPi;


    public SubmitTicketFactory(SubmitQueryAPi submitQueryAPi) {
        this.submitQueryAPi = submitQueryAPi;
    }

    public SubmitTicketDataStore getCloudStoreForTicketSubmit() {
       return new SubmitTicketDataStore(submitQueryAPi);
    }
}
