package com.tokopedia.attachinvoice.view.fragment

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.attachinvoice.view.adapter.AttachInvoiceTypeFactory
import com.tokopedia.attachinvoice.view.adapter.AttachInvoiceTypeFactoryImpl

class AttachInvoiceFragment: BaseListFragment<Visitable<*>, AttachInvoiceTypeFactory>() {

    private val screenName = "attach-invoice"

    override fun getScreenName(): String = screenName

    override fun initInjector() { }

    override fun getAdapterTypeFactory(): AttachInvoiceTypeFactory {
        return AttachInvoiceTypeFactoryImpl()
    }

    override fun onItemClicked(t: Visitable<*>?) { }

    override fun loadData(page: Int) {
        
    }

}