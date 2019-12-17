package com.tokopedia.attachinvoice.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.attachinvoice.R
import com.tokopedia.attachinvoice.di.AttachInvoiceComponent
import com.tokopedia.attachinvoice.view.adapter.AttachInvoiceTypeFactory
import com.tokopedia.attachinvoice.view.adapter.AttachInvoiceTypeFactoryImpl
import com.tokopedia.attachinvoice.view.viewmodel.AttachInvoiceViewModel
import javax.inject.Inject

class AttachInvoiceFragment : BaseListFragment<Visitable<*>, AttachInvoiceTypeFactory>() {

    private val screenName = "attach-invoice"

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(AttachInvoiceViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.attachinvoice_fragment_attach_invoice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initializeArguments(arguments)
    }

    override fun getScreenName(): String = screenName

    override fun initInjector() {
        getComponent(AttachInvoiceComponent::class.java).inject(this)
    }

    override fun getAdapterTypeFactory(): AttachInvoiceTypeFactory {
        return AttachInvoiceTypeFactoryImpl()
    }

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadData(page: Int) {
        viewModel.loadInvoices(page)
    }

    companion object {
        fun createInstance(extra: Bundle?): AttachInvoiceFragment {
            return AttachInvoiceFragment().apply {
                arguments = extra
            }
        }
    }
}