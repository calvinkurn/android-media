package com.tokopedia.attachinvoice.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.attachinvoice.R
import com.tokopedia.attachinvoice.analytic.AttachInvoiceAnalytic
import com.tokopedia.attachinvoice.data.Invoice
import com.tokopedia.attachinvoice.databinding.FragmentAttachinvoiceAttachInvoiceBinding
import com.tokopedia.attachinvoice.di.AttachInvoiceComponent
import com.tokopedia.attachinvoice.view.adapter.AttachInvoiceAdapter
import com.tokopedia.attachinvoice.view.adapter.AttachInvoiceTypeFactory
import com.tokopedia.attachinvoice.view.adapter.AttachInvoiceTypeFactoryImpl
import com.tokopedia.attachinvoice.view.adapter.viewholder.EmptyAttachInvoiceViewHolder
import com.tokopedia.attachinvoice.view.viewmodel.AttachInvoiceViewModel
import com.tokopedia.attachinvoice.view.widget.AttachInvoiceItemDecoration
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.resources.isDarkMode
import javax.inject.Inject

class AttachInvoiceFragment : BaseListFragment<Visitable<*>, AttachInvoiceTypeFactory>(),
        EmptyAttachInvoiceViewHolder.Listener {

    private val screenName = "attach-invoice"
    private var messageId: String = ""
    private var opponentName: String = ""

    private var _binding: FragmentAttachinvoiceAttachInvoiceBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analytic: AttachInvoiceAnalytic

    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(AttachInvoiceViewModel::class.java) }

    private lateinit var adapter: AttachInvoiceAdapter
    private var listener: Listener? = null

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    interface Listener {
        fun onClickAttachInvoice(intent: Intent)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding =  FragmentAttachinvoiceAttachInvoiceBinding.inflate(inflater, container, false)
        initializeArguments(arguments)
        setupRecyclerView()
        setupObserver()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupShadow()
    }

    override fun getScreenName(): String = screenName

    override fun initInjector() {
        getComponent(AttachInvoiceComponent::class.java).inject(this)
    }

    override fun getAdapterTypeFactory(): AttachInvoiceTypeFactory {
        return AttachInvoiceTypeFactoryImpl(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadData(page: Int) {
        viewModel.loadInvoices(page, messageId)
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, AttachInvoiceTypeFactory> {
        adapter = AttachInvoiceAdapter(adapterTypeFactory)
        return adapter
    }

    override fun hideAttachButton() {
        binding.flAttach.hide()
        binding.attachShadow.hide()
    }

    override fun getOpponentName(): String {
        return opponentName
    }

    private fun initializeArguments(arguments: Bundle?) {
        if (arguments == null) return
        messageId = arguments.getString(ApplinkConst.AttachInvoice.PARAM_MESSAGE_ID, "")
        opponentName = arguments.getString(ApplinkConst.AttachInvoice.PARAM_OPPONENT_NAME, "")
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            clearItemDecoration()
            addItemDecoration(AttachInvoiceItemDecoration(context))
        }
    }

    private fun setupObserver() {
        viewModel.invoices.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> successLoadInvoices(result.data)
                is Fail -> errorLoadInvoices(result.throwable)
            }
        })
        adapter.selectedInvoice.observe(viewLifecycleOwner, Observer { invoice ->
            if (invoice != null) {
                enableAttachButton(invoice)
            } else {
                disableAttachButton()
            }
        })
    }

    private fun enableAttachButton(invoice: Invoice) {
        binding.btnAttach.isEnabled = true
        binding.btnAttach.setOnClickListener {
            analytic.trackOnAttachInvoice(invoice)
            val intent = getInvoicePreviewIntent(invoice)
            listener?.onClickAttachInvoice(intent)
        }
    }

    private fun successLoadInvoices(data: List<Invoice>) {
        renderList(data)
    }

    private fun errorLoadInvoices(throwable: Throwable) {
        showGetListError(throwable)
        if (isFirstPage()) {
            disableAttachButton()
        }
    }

    private fun disableAttachButton() {
        binding.btnAttach.isEnabled = false
    }

    private fun isFirstPage(): Boolean {
        return currentPage == 1
    }

    fun getInvoicePreviewIntent(invoice: Invoice): Intent {
        return Intent().apply {
            putExtra(ApplinkConst.Chat.INVOICE_ID, invoice.id.toString())
            putExtra(ApplinkConst.Chat.INVOICE_CODE, invoice.code)
            putExtra(ApplinkConst.Chat.INVOICE_TITLE, invoice.productName)
            putExtra(ApplinkConst.Chat.INVOICE_DATE, invoice.timeStamp)
            putExtra(ApplinkConst.Chat.INVOICE_IMAGE_URL, invoice.thumbnailUrl)
            putExtra(ApplinkConst.Chat.INVOICE_URL, invoice.url)
            putExtra(ApplinkConst.Chat.INVOICE_STATUS_ID, invoice.statusId.toString())
            putExtra(ApplinkConst.Chat.INVOICE_STATUS, invoice.status)
            putExtra(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, invoice.productPrice)
        }
    }

    private fun setupShadow() {
        @DrawableRes val drawableRes = if (context?.isDarkMode() == true) {
            com.tokopedia.attachcommon.R.drawable.bg_attachcommon_shadow_attachment_dark
        } else {
            com.tokopedia.attachcommon.R.drawable.bg_attachcommon_shadow_attachment_light
        }
        binding.attachShadow?.setBackgroundResource(drawableRes)
    }

    companion object {
        fun createInstance(messageId: String, opponentName: String): AttachInvoiceFragment {
            val args = Bundle().apply {
                putString(ApplinkConst.AttachInvoice.PARAM_MESSAGE_ID, messageId)
                putString(ApplinkConst.AttachInvoice.PARAM_OPPONENT_NAME, opponentName)
            }
            return AttachInvoiceFragment().apply {
                arguments = args
            }
        }
    }
}
