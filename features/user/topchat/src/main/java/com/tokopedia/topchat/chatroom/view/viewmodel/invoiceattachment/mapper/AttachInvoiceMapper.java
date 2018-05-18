package com.tokopedia.topchat.chatroom.view.viewmodel.invoiceattachment.mapper;

import com.tokopedia.topchat.attachinvoice.view.resultmodel.SelectedInvoice;
import com.tokopedia.topchat.chatroom.view.viewmodel.invoiceattachment.AttachInvoiceSingleViewModel;

/**
 * Created by Hendri on 28/03/18.
 */

public class AttachInvoiceMapper {
    public static SelectedInvoice selectedInvoiceViewModelToSelectedInvoice
            (AttachInvoiceSingleViewModel viewModel) {
        return new SelectedInvoice(viewModel.getId(),
                viewModel.getCode(),
                viewModel.getTypeString(),
                viewModel.getType(),
                viewModel.getTitle(),
                viewModel.getImageUrl(),
                viewModel.getDescription(),
                viewModel.getAmount(),
                viewModel.getCreatedTime(),
                viewModel.getUrl(),
                viewModel.getStatus(),
                viewModel.getStatusId());
    }
}
