package com.tokopedia.topchat.attachinvoice.view;

import android.content.Context;

import com.tokopedia.topchat.attachinvoice.view.model.InvoiceViewModel;
import com.tokopedia.topchat.attachinvoice.view.model.InvoiceViewModel;

import java.util.List;

/**
 * Created by Hendri on 22/03/18.
 */

public interface AttachInvoiceContract {
    interface View {
        void addInvoicesToList(List<InvoiceViewModel> invoices, boolean hasNextPage);

        void hideAllLoadingIndicator();

        void showErrorMessage(Throwable throwable);
    }

    interface Activity {
        String getUserId();

        int getMessageId();
    }

    interface Presenter {
        void loadInvoiceData(String query, String userId, int page, int messageId, Context context);
        void attachView(AttachInvoiceContract.View view);
        void attachActivityContract(AttachInvoiceContract.Activity activityContract);
        void detachView();
    }
}
