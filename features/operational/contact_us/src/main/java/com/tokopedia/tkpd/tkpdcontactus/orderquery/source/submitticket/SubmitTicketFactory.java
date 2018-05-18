package com.tokopedia.tkpd.tkpdcontactus.orderquery.source.submitticket;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.apis.AccountsApi;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdcontactus.home.source.api.ContactUsAPI;
import com.tokopedia.tkpd.tkpdcontactus.orderquery.data.ContactUsPass;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by baghira on 16/05/18.
 */

public class SubmitTicketFactory {
    ContactUsAPI contactUsAPI;
    AccountsApi accountsApi;
    Context context;
    public SubmitTicketFactory(ContactUsAPI contactUsAPI, AccountsApi accountsApi, Context context) {
        this.contactUsAPI = contactUsAPI;
        this.accountsApi = accountsApi;
        this.context = context;
    }
    Observable<Response<TkpdResponse>> submitTicket(ContactUsPass contactUsPass){
        return (new SubmitTicketDataStore(contactUsAPI, accountsApi, context)).getSubmitTicket(contactUsPass);
    };
}
