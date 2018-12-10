package com.tokopedia.chatbot.attachinvoice.view.adapter


import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.chatbot.attachinvoice.view.model.InvoiceViewModel

/**
 * Created by Hendri on 22/03/18.
 */

class AttachInvoiceListAdapter(baseListAdapterTypeFactory: AttachInvoiceListAdapterTypeFactory,
                               onAdapterInteractionListener: BaseListAdapter.OnAdapterInteractionListener<InvoiceViewModel>)
    : BaseListAdapter<InvoiceViewModel, AttachInvoiceListAdapterTypeFactory>(baseListAdapterTypeFactory, onAdapterInteractionListener)
