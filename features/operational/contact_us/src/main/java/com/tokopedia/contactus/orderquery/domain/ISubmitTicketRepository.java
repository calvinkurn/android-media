package com.tokopedia.contactus.orderquery.domain;

import com.tokopedia.contactus.orderquery.data.ContactUsPass;
import com.tokopedia.contactus.orderquery.data.CreateTicketResult;

import rx.Observable;

/**
 * Created by baghira on 16/05/18.
 */

public interface ISubmitTicketRepository {
    Observable<CreateTicketResult> getQueryTickets(ContactUsPass ticket);
}
