package com.tokopedia.thankyou_native.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.di.component.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.adapter.DetailedInvoiceAdapter
import com.tokopedia.thankyou_native.presentation.viewModel.DetailInvoiceViewModel
import kotlinx.android.synthetic.main.thank_payment_invoice_bsheet.*
import javax.inject.Inject

class InvoiceFragment : BaseDaggerFragment() {

    override fun getScreenName(): String = ""

    private lateinit var thanksPageData: ThanksPageData

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var invoiceAdapter: dagger.Lazy<DetailedInvoiceAdapter>

    private val detailInvoiceViewModel: DetailInvoiceViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(DetailInvoiceViewModel::class.java)
    }


    override
    fun initInjector() {
        getComponent(ThankYouPageComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_THANK_PAGE_DATA)) {
                thanksPageData = it.getParcelable(ARG_THANK_PAGE_DATA)!!
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.thank_payment_invoice_bsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeViewModel()
        detailInvoiceViewModel.createInvoiceData(thanksPageData)
    }

    private fun initRecyclerView() {
        val recycleListView = recyclerView
        recycleListView.layoutManager = LinearLayoutManager(context)
        recycleListView.adapter = invoiceAdapter.get()
    }

    private fun observeViewModel() {
        detailInvoiceViewModel.mutableInvoiceVisitables.observe(this, Observer {
            it?.let {
                addDataToAdapter(it)
            }
        })
    }

    private fun addDataToAdapter(list: ArrayList<Visitable<*>>) {
        invoiceAdapter.get().addItems(list)
        invoiceAdapter.get().notifyDataSetChanged()
    }

    companion object {
        private const val ARG_THANK_PAGE_DATA = "arg_thank_page_data"
        fun getInvoiceFragment(thanksPageData: ThanksPageData)
                : InvoiceFragment = InvoiceFragment().apply {
            val bundle = Bundle()
            bundle?.let {
                arguments = bundle
                bundle.putParcelable(ARG_THANK_PAGE_DATA, thanksPageData)
            }
        }
    }
}