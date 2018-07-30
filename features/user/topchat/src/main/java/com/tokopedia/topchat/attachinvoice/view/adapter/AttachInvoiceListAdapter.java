package com.tokopedia.topchat.attachinvoice.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.topchat.attachinvoice.view.model.InvoiceViewModel;

/**
 * Created by Hendri on 22/03/18.
 */

public class AttachInvoiceListAdapter extends BaseListAdapter<InvoiceViewModel,
        AttachInvoiceListAdapterTypeFactory> {
    public AttachInvoiceListAdapter(AttachInvoiceListAdapterTypeFactory
                                            baseListAdapterTypeFactory,
                                    OnAdapterInteractionListener<InvoiceViewModel>
                                            onAdapterInteractionListener) {
        super(baseListAdapterTypeFactory, onAdapterInteractionListener);
    }
}
