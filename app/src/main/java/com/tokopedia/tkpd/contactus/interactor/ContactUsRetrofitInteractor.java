package com.tokopedia.tkpd.contactus.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.tkpd.contactus.model.ContactUsPass;
import com.tokopedia.tkpd.contactus.model.contactuscategory.ContactUsCategory;
import com.tokopedia.tkpd.contactus.model.contactusform.TicketForm;

import java.util.Map;

/**
 * Created by nisie on 8/12/16.
 */
public interface ContactUsRetrofitInteractor {

    void getCategory(@NonNull Context context, @NonNull Map<String, String> params,
                 @NonNull GetCategoryListener listener);

    void getFormModelContactUs(@NonNull Context context, @NonNull Map<String, String> params,
                     @NonNull GetContactFormListener listener);

    void sendTicket(@NonNull Context context, @NonNull ContactUsPass params,
                               @NonNull SendTicketListener listener);

    void unsubscribe();

    public interface GetCategoryListener {
        void onSuccess(ContactUsCategory result);

        void onNoNetworkConnection();

        void onTimeout(String error);

        void onError(String s);

        void onNullData();

    }

    public interface GetContactFormListener{
        void onSuccess(TicketForm ticketForm);

        void onNoNetworkConnection();

        void onTimeout(String error);

        void onError(String s);

        void onNullData();

    }

    public interface SendTicketListener {
        void onSuccess();

        void onNoNetworkConnection();

        void onTimeout(String error);

        void onError(String s);

        void onNullData();

    }
}
