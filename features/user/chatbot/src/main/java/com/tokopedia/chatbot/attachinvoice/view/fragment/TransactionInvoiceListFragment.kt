package com.tokopedia.chatbot.attachinvoice.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.chatbot.attachinvoice.di.AttachInvoiceModule
import com.tokopedia.chatbot.attachinvoice.di.DaggerAttachInvoiceComponent
import com.tokopedia.chatbot.attachinvoice.domain.model.InvoiceConstants
import com.tokopedia.chatbot.attachinvoice.view.adapter.TransactionInvoiceListAdapterTypeFactoryImpl
import com.tokopedia.chatbot.attachinvoice.view.model.EmptyTransactionInvoiceUiModel
import com.tokopedia.chatbot.attachinvoice.view.model.TransactionInvoiceUiModel
import com.tokopedia.chatbot.attachinvoice.view.resultmodel.SelectedInvoice
import com.tokopedia.chatbot.attachinvoice.view.viewholder.EmptyViewHolderListener
import com.tokopedia.chatbot.attachinvoice.view.viewholder.TransactionInvoiceViewHolderListener
import com.tokopedia.chatbot.attachinvoice.view.viewmodel.TransactionInvoiceListViewModel
import com.tokopedia.chatbot.view.ChatbotInternalRouter
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

const val KEY_MESSAGE_ID = "messageId"
const val KEY_FILTER_EVENT = "filterEvent"

class TransactionInvoiceListFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(),
        EmptyViewHolderListener, TransactionInvoiceViewHolderListener {

    private var messageId: Int = 0
    private var filterEvent: String = ""
    private var listener: TransactionInvoiceListFragmentListener? = null
    private var isError: Boolean = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TransactionInvoiceListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TransactionInvoiceListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            messageId = getInt(KEY_MESSAGE_ID)
            filterEvent = getString(KEY_FILTER_EVENT, "")
        }
    }

    override fun getScreenName(): String {
        return TransactionInvoiceListFragment::class.java.simpleName
    }

    override fun initInjector() {
        if (activity != null && (activity as Activity).application != null) {
            val appComponent = ((activity as Activity).application as BaseMainApplication)
                    .baseAppComponent
            val daggerAttachInvoiceComponent = DaggerAttachInvoiceComponent.builder()
                    .baseAppComponent(appComponent)
                    .attachInvoiceModule(context?.let { AttachInvoiceModule(it) })
                    .build() as DaggerAttachInvoiceComponent
            daggerAttachInvoiceComponent.inject(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFilteredInvoiceLiveData().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    when (it.data.first()) {
                        is EmptyTransactionInvoiceUiModel -> {
                            adapter.clearAllElements()
                            adapter.addElement(it.data)
                            isError = true
                        }
                        is TransactionInvoiceUiModel -> {
                            renderList(it.data, (it.data.size >= InvoiceConstants.DEFAULT_LIMIT))
                            isError = false
                        }
                    }
                }
                is Fail -> {
                }
            }
        })
        getRecyclerView(view)?.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_MOVE -> rv.parent.parent.requestDisallowInterceptTouchEvent(true)
                }
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val recyclerView = super.getRecyclerView(view)
        setTopMargin(recyclerView)
        if (recyclerView is VerticalRecyclerView) {
            recyclerView.clearItemDecoration()
        }
        return view
    }

    private fun setTopMargin(recyclerView: RecyclerView?) {
        recyclerView?.setMargin(0, context?.resources?.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)?.toInt()
                ?: 0, 0, 0)
    }

    override fun onResume() {
        super.onResume()
        listener?.getButtonView()?.showWithCondition(!isError)
    }

    override fun onItemClicked(item: Visitable<*>?) {
        if (item is TransactionInvoiceUiModel) {
            val data = Intent()
            data.putExtra(ChatbotInternalRouter.Companion
                    .TOKOPEDIA_ATTACH_INVOICE_SELECTED_INVOICE_KEY, SelectedInvoice(item))
            listener?.setResult(data)
        }
    }

    override fun loadData(page: Int) {
        viewModel.getFilteredInvoice(filterEvent, page, messageId)
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return TransactionInvoiceListAdapterTypeFactoryImpl(this, this)
    }

    companion object {

        @JvmStatic
        fun newInstance(messageId: Int, filterEvent: String, listener: TransactionInvoiceListFragmentListener): TransactionInvoiceListFragment {
            return TransactionInvoiceListFragment().apply {
                val args = Bundle()
                args.putInt(KEY_MESSAGE_ID, messageId)
                args.putString(KEY_FILTER_EVENT, filterEvent)
                this.arguments = args
                this.listener = listener
            }
        }
    }

    override fun onTryAgain() {
        loadInitialData()
    }

    override fun onItemClick(invoice: TransactionInvoiceUiModel) {
        activity?.run {
            val data = Intent()
            data.putExtra(ChatbotInternalRouter.Companion
                    .TOKOPEDIA_ATTACH_INVOICE_SELECTED_INVOICE_KEY, SelectedInvoice
            (invoice))
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

}

interface TransactionInvoiceListFragmentListener {
    fun getButtonView(): UnifyButton
    fun setResult(data: Intent)
}