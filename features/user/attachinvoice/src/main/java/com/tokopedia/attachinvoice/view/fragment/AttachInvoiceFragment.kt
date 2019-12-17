package com.tokopedia.attachinvoice.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.attachinvoice.R
import com.tokopedia.attachinvoice.data.Invoice
import com.tokopedia.attachinvoice.di.AttachInvoiceComponent
import com.tokopedia.attachinvoice.view.adapter.AttachInvoiceTypeFactory
import com.tokopedia.attachinvoice.view.adapter.AttachInvoiceTypeFactoryImpl
import com.tokopedia.attachinvoice.view.viewmodel.AttachInvoiceViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.attachinvoice_fragment_attach_invoice.*
import javax.inject.Inject

class AttachInvoiceFragment : BaseListFragment<Visitable<*>, AttachInvoiceTypeFactory>() {

    private val screenName = "attach-invoice"

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(AttachInvoiceViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.attachinvoice_fragment_attach_invoice, container, false).also {
            viewModel.initializeArguments(arguments)
            setupObserver()
        }
    }

    private fun setupObserver() {
        viewModel.invoices.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> successLoadInvoices(result.data)
                is Fail -> errorLoadInvoices(result.throwable)
            }
        })
    }

    private fun successLoadInvoices(data: List<Invoice>) {
        renderList(data)
    }

    private fun errorLoadInvoices(throwable: Throwable) {
        showGetListError(throwable)
        if (isFirstPage()) {
            disableAttachBtn()
        }
    }

    private fun disableAttachBtn() {
        btnAttach?.isEnabled = false
    }

    private fun isFirstPage(): Boolean {
        return currentPage == 1
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