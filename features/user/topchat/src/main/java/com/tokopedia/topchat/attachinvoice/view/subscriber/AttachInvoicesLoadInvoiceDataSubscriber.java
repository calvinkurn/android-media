package com.tokopedia.topchat.attachinvoice.view.subscriber;

import com.tokopedia.topchat.attachinvoice.domain.usecase.AttachInvoicesUseCase;
import com.tokopedia.topchat.attachinvoice.view.AttachInvoiceContract;
import com.tokopedia.topchat.attachinvoice.view.model.InvoiceViewModel;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Hendri on 22/03/18.
 */

public class AttachInvoicesLoadInvoiceDataSubscriber extends Subscriber<List<InvoiceViewModel>> {
    private final AttachInvoiceContract.View view;

    public AttachInvoicesLoadInvoiceDataSubscriber(AttachInvoiceContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.showErrorMessage(e);
    }

    @Override
    public void onNext(List<InvoiceViewModel> invoiceViewModels) {
        view.addInvoicesToList(invoiceViewModels,
                (invoiceViewModels.size() >= AttachInvoicesUseCase.DEFAULT_LIMIT));
    }
}
