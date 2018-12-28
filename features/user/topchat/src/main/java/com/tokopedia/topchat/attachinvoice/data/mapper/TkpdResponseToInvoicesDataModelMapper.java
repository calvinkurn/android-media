package com.tokopedia.topchat.attachinvoice.data.mapper;

import com.tokopedia.topchat.attachinvoice.data.model.GetInvoicesPayloadWrapper;
import com.tokopedia.topchat.attachinvoice.data.model.GetInvoicesResponseWrapper;
import com.tokopedia.topchat.attachinvoice.data.model.InvoiceAttributesDataModel;
import com.tokopedia.topchat.attachinvoice.data.model.InvoicesDataModel;
import com.tokopedia.topchat.attachinvoice.domain.model.Invoice;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by Hendri on 21/03/18.
 */

public class TkpdResponseToInvoicesDataModelMapper implements
        Func1<Response<GetInvoicesResponseWrapper>, List<Invoice>> {

    @Inject
    public TkpdResponseToInvoicesDataModelMapper() {
    }

    @Override
    public List<Invoice> call(Response<GetInvoicesResponseWrapper>
                                          getInvoicesResponseWrapperResponse) {
        GetInvoicesResponseWrapper responseWrapper = getInvoicesResponseWrapperResponse.body();
        List<Invoice> domainModel = new ArrayList<>();
        if (responseWrapper.getDataWrapper() != null && responseWrapper.getDataWrapper()
                .getPayloadWrapper() != null) {
            GetInvoicesPayloadWrapper payloadWrapper = responseWrapper.getDataWrapper()
                    .getPayloadWrapper();
            if (payloadWrapper.getInvoices() != null) {
                domainModel = convertPayloadDataToInvoiceList(payloadWrapper);
            }
        }
        return domainModel;
    }

    private List<Invoice> convertPayloadDataToInvoiceList(GetInvoicesPayloadWrapper payloadWrapper){
        List<Invoice> domainModel = new ArrayList<>();
        if (payloadWrapper.getInvoices() != null) {
            for (InvoicesDataModel invoicesDataModel : payloadWrapper.getInvoices()) {
                domainModel.add(convertInvoicefromInvoiceDataModel(invoicesDataModel));
            }
        }
        return domainModel;
    }

    private Invoice convertInvoicefromInvoiceDataModel(InvoicesDataModel invoicesDataModel){
        InvoiceAttributesDataModel invoiceAttributesDataModel = invoicesDataModel
                .getAttribute();
        Invoice.InvoiceBuilder invoiceBuilder = Invoice.InvoiceBuilder.getInstance();
        invoiceBuilder.setStatusInt(invoiceAttributesDataModel.getStatusId())
                .setNumber(invoiceAttributesDataModel.getInvoiceNo())
                .setType(invoicesDataModel.getType())
                .setUrl(invoiceAttributesDataModel.getUrl())
                .setTitle(invoiceAttributesDataModel.getTitle())
                .setDesc(invoiceAttributesDataModel.getDescription())
                .setDate(invoiceAttributesDataModel.getInvoiceDate())
                .setStatus(invoiceAttributesDataModel.getStatus())
                .setTotal(invoiceAttributesDataModel.getAmount())
                .setImageUrl(invoiceAttributesDataModel.getImageUrl())
                .setInvoiceTypeInt(invoicesDataModel.getTypeId())
                .setInvoiceId(invoiceAttributesDataModel.getInvoiceId());
        return invoiceBuilder.createInvoice();
    }
}
