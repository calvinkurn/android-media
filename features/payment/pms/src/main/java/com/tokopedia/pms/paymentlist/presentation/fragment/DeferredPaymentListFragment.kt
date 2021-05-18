package com.tokopedia.pms.paymentlist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.pms.R
import com.tokopedia.pms.payment.data.model.CancelDetail
import com.tokopedia.pms.paymentlist.di.PaymentListComponent
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel
import com.tokopedia.pms.paymentlist.domain.data.VirtualAccountPaymentModel
import com.tokopedia.pms.paymentlist.presentation.adapter.DeferredPaymentListAdapter
import com.tokopedia.pms.paymentlist.presentation.bottomsheet.PaymentTransactionActionSheet
import com.tokopedia.pms.paymentlist.presentation.bottomsheet.PaymentTransactionDetailSheet
import com.tokopedia.pms.paymentlist.viewmodel.PaymentListViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_payment_list.*
import timber.log.Timber
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
        return inflater.inflate(R.layout.fragment_payment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.apply {
            adapter = DeferredPaymentListAdapter { actionItem, model ->
                handleActionRedirection(actionItem, model)
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        loadInitialDeferredTransactions()
        observeViewModels()
    }

    private fun handleActionRedirection(actionItem: Int, model: BasePaymentModel) {
        when(actionItem) {
            ACTION_HOW_TO_PAY_REDIRECTION -> redirectToHowToPay(model)
            ACTION_INVOICE_PAGE_REDIRECTION -> openInvoiceDetail(model)
            ACTION_INVOICE_PAGE_REDIRECTION_COMBINED_VA -> checkAndOpenInvoiceDetail(model)
            ACTION_CHEVRON_ACTIONS -> openActionBottomSheet(model)
        }
    }

    private fun openInvoiceDetail(model: BasePaymentModel) {
        Toaster.make(recycler_view, model.invoiceDetailUrl, Toaster.LENGTH_LONG)
        RouteManager.route(activity, ApplinkConstInternalGlobal.WEBVIEW, model.invoiceDetailUrl)
    }

    private fun redirectToHowToPay(model: BasePaymentModel) {
        Toaster.make(recycler_view, model.howtoPayAppLink, Toaster.LENGTH_LONG)
        //RouteManager.route(context, model.howtoPayAppLink)
    }

    private fun checkAndOpenInvoiceDetail(model: BasePaymentModel) {
        (model as VirtualAccountPaymentModel).transactionList.let {
            if (it.size > 1) showCombinedTransactionDetail(model) else openInvoiceDetail(model)
        }
    }

    private fun loadInitialDeferredTransactions(cursor: String = "") {
        (recycler_view.adapter as DeferredPaymentListAdapter).clearAllElements()
        viewModel.getPaymentList(cursor)
    }

    private fun observeViewModels() {
        viewModel.paymentListResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> handlePaymentListSuccess(it.data)
                is Fail -> handlePaymentListError(it.throwable)
            }
        })
        viewModel.cancelPaymentDetailLiveData.observe(viewLifecycleOwner, {
            when(it) {
                is Success -> showCancelDetailMessage(it.data)
                is Fail -> Timber.d(it.throwable)
            }
        })
        viewModel.cancelPaymentLiveData.observe(viewLifecycleOwner, {
            when(it) {
                is Success -> Timber.d(it.data.message)
                is Fail ->  Timber.d(it.throwable)
            }
        })
    }

    private fun showCancelDetailMessage(data: CancelDetail) {
        // showDialog here
    }

    private fun handlePaymentListSuccess(data: ArrayList<BasePaymentModel>) {
        (recycler_view.adapter as DeferredPaymentListAdapter).addItems(data)
    }

    private fun handlePaymentListError(throwable: Throwable) {
        // show empty state
    }

    fun invokeCancelSingleTransaction(transactionId: String, merchantCode: String) {
        viewModel.getCancelPaymentDetail(transactionId, merchantCode)
    }

    private fun openActionBottomSheet(model: BasePaymentModel) {
        // open list of action bottom sheet
        val bundle = Bundle()
        bundle.putParcelable(PaymentTransactionActionSheet.PAYMENT_MODEL, model)
        PaymentTransactionActionSheet.show(bundle, childFragmentManager)
    }

    fun showCombinedTransactionDetail(model: BasePaymentModel) {
        (model as VirtualAccountPaymentModel).let {
            val bundle = Bundle()
            bundle.putParcelableArrayList(PaymentTransactionDetailSheet.TRANSACTION_LIST, model.transactionList)
            bundle.putString(PaymentTransactionDetailSheet.GATEWAY_NAME, model.gatewayName)
            PaymentTransactionDetailSheet.show(bundle, childFragmentManager)
        }
    }

    companion object {
        const val ACTION_HOW_TO_PAY_REDIRECTION = 1
        const val ACTION_INVOICE_PAGE_REDIRECTION = 2
        const val ACTION_INVOICE_PAGE_REDIRECTION_COMBINED_VA = 3
        const val ACTION_CHEVRON_ACTIONS = 4
        fun createInstance() = DeferredPaymentListFragment()
    }

}