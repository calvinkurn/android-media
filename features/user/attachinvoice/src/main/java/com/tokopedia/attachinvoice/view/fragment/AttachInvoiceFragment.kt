package com.tokopedia.attachinvoice.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.attachinvoice.R
import com.tokopedia.attachinvoice.view.adapter.AttachInvoiceTypeFactory
import com.tokopedia.attachinvoice.view.adapter.AttachInvoiceTypeFactoryImpl

class AttachInvoiceFragment: BaseListFragment<Visitable<*>, AttachInvoiceTypeFactory>() {

    private val screenName = "attach-invoice"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.attachinvoice_fragment_attach_invoice, container, false)
    }

    override fun getScreenName(): String = screenName

    override fun initInjector() { }

    override fun getAdapterTypeFactory(): AttachInvoiceTypeFactory {
        return AttachInvoiceTypeFactoryImpl()
    }

    override fun onItemClicked(t: Visitable<*>?) { }

    override fun loadData(page: Int) {

    }

}