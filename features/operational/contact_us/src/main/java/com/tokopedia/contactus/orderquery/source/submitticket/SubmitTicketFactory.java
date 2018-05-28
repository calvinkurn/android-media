package com.tokopedia.contactus.orderquery.source.submitticket;

import android.content.Context;

import com.tokopedia.contactus.home.source.api.ContactUsAPI;
import com.tokopedia.contactus.orderquery.data.ContactUsPass;
import com.tokopedia.contactus.orderquery.data.CreateTicketResult;

import rx.Observable;

/**
 * Created by baghira on 16/05/18.
 */

public class SubmitTicketFactory {
    ContactUsAPI contactUsAPI;
    Context context;
    public SubmitTicketFactory(ContactUsAPI contactUsAPI, Context context) {
        this.contactUsAPI = contactUsAPI;
        this.context = context;
    }
    Observable<CreateTicketResult> submitTicket(ContactUsPass contactUsPass){
        return (new SubmitTicketDataStore(contactUsAPI, context)).getSubmitTicket(contactUsPass);
    };
}
