package com.tokopedia.pms.paymentlist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.pms.R
import com.tokopedia.pms.paymentlist.di.PaymentListComponent
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel
import com.tokopedia.pms.paymentlist.presentation.adapter.DeferredPaymentListAdapter
import com.tokopedia.pms.paymentlist.viewmodel.PaymentListViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_payment_list.*
import java.util.*
import javax.inject.Inject

class DeferredPaymentListFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val viewModel: PaymentListViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(PaymentListViewModel::class.java)
    }

    override fun getScreenName() = ""

    override fun initInjector() = getComponent(PaymentListComponent::class.java).inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_payment_list, container, false)
        loadDeferredTransactions()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.apply {
            adapter = DeferredPaymentListAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        observeViewModels()
    }

    private fun loadDeferredTransactions(cursor: String = "") {
        viewModel.getPaymentList(cursor)
    }

    private fun observeViewModels() {
        viewModel.paymentListResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> handlePaymentListSuccess(it.data)
                is Fail -> handlePaymentListError(it.throwable)
            }
        })
    }

    private fun handlePaymentListSuccess(data: ArrayList<BasePaymentModel>) {
        (recycler_view.adapter as DeferredPaymentListAdapter).addItems(data)
    }

    private fun handlePaymentListError(throwable: Throwable) {

    }

    companion object {
        fun createInstance() = DeferredPaymentListFragment()
    }

}