package com.tokopedia.pms.paymentlist.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.pms.R
import com.tokopedia.pms.paymentlist.di.PmsComponent
import com.tokopedia.pms.paymentlist.domain.data.*
import com.tokopedia.pms.paymentlist.presentation.adapter.DeferredPaymentListAdapter
import com.tokopedia.pms.paymentlist.presentation.bottomsheet.PaymentTransactionActionSheet
import com.tokopedia.pms.paymentlist.presentation.bottomsheet.PaymentTransactionDetailSheet
import com.tokopedia.pms.paymentlist.viewmodel.PaymentListViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_payment_list.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

class DeferredPaymentListFragment : BaseDaggerFragment(), SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val viewModel: PaymentListViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(PaymentListViewModel::class.java)
    }

    private var loader: LoaderDialog?= null

    override fun getScreenName() = ""

    override fun initInjector() = getComponent(PmsComponent::class.java).inject(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        swipe_refresh_layout.setOnRefreshListener(this)
        loadInitialDeferredTransactions()
        observeViewModels()
    }

    private fun loadInitialDeferredTransactions() {
        context?.let { loader = LoaderDialog(it) }
        (recycler_view.adapter as DeferredPaymentListAdapter).clearAllElements()
        viewModel.getPaymentListCount()
    }

    private fun observeViewModels() {
        viewModel.paymentListResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> renderPaymentList(it.data)
                is Fail -> showErrorUi(it.throwable)
                is EmptyState -> showEmptyState()
                is LoadingState -> handleSwipeRefresh(true)
                is ProgressState -> showProgressForDelayedFetch()
                else -> handleSwipeRefresh(false)
            }
        })
        viewModel.cancelPaymentDetailLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> showCancelDetailMessage(it.data)
                is Fail -> showToast(it.throwable.message, Toaster.TYPE_ERROR)
            }
        })
        viewModel.cancelPaymentLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> showCancelPaymentResult(it.data)
                is Fail -> showToast(it.throwable.message, Toaster.TYPE_ERROR)
            }
        })
    }

    private fun showCancelPaymentResult(data: CancelPayment) {
        // refresh page
        if (data.isSuccess) {
            showToast(data.message, Toaster.TYPE_NORMAL)
            onRefresh()
        } else showToast(data.message, Toaster.TYPE_ERROR)
    }

    // show cancel detail dialog
    private fun showCancelDetailMessage(data: CancelDetailWrapper) {
        val description = if (data.cancelDetailData.combineMessage.isNullOrBlank()) {
            data.cancelDetailData.refundMessage ?: ""
        } else data.cancelDetailData.combineMessage + "\n" + data.cancelDetailData.refundMessage
        context?.let {
            val title =
                if (data.productName == null) it.getString(R.string.payment_cancel_title_default)
                else "Yakin ingin batalkan ${data.productName}?"
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(title)
            dialog.setDescription(description.parseAsHtml())
            dialog.setPrimaryCTAText(getString(R.string.payment_cancel_yes))
            dialog.setPrimaryCTAClickListener {
                viewModel.cancelPayment(data.transactionId, data.merchantCode)
                dialog.dismiss()
            }
            dialog.setSecondaryCTAText(getString(R.string.payment_cancel_back))
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun renderPaymentList(data: ArrayList<BasePaymentModel>) {
        handleSwipeRefresh(false)
        hideLoader()
        noPendingTransactionEmptyState.gone()
        paymentListGlobalError.gone()
        recycler_view.visible()
        (recycler_view.adapter as DeferredPaymentListAdapter).addItems(data)
    }

    private fun showErrorUi(throwable: Throwable) {
        handleSwipeRefresh(false)
        hideLoader()
        noPendingTransactionEmptyState.gone()
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> setGlobalErrors(GlobalError.NO_CONNECTION)
            is IllegalStateException -> setGlobalErrors(GlobalError.PAGE_FULL)
            else -> setGlobalErrors(GlobalError.SERVER_ERROR)
        }
    }

    private fun showEmptyState() {
        handleSwipeRefresh(false)
        noPendingTransactionEmptyState.visible()
        noPendingTransactionEmptyState.setPrimaryCTAClickListener {
            RouteManager.route(context, ApplinkConst.HOME)
        }
    }

    private fun setGlobalErrors(errorType: Int) {
        paymentListGlobalError.setType(errorType)
        paymentListGlobalError.visible()
        paymentListGlobalError.setActionClickListener {
            paymentListGlobalError.gone()
            recycler_view.visible()
            onRefresh()
        }
    }

    private fun handleActionRedirection(actionItem: Int, model: BasePaymentModel) {
        when (actionItem) {
            ACTION_HOW_TO_PAY_REDIRECTION -> redirectToHowToPay(model)
            ACTION_INVOICE_PAGE_REDIRECTION -> openInvoiceDetail(model)
            ACTION_INVOICE_PAGE_REDIRECTION_COMBINED_VA -> checkAndOpenInvoiceDetail(model)
            ACTION_CHEVRON_ACTIONS -> openActionBottomSheet(model)
        }
    }

    private fun openInvoiceDetail(model: BasePaymentModel) {
        RouteManager.route(activity, ApplinkConstInternalGlobal.WEBVIEW, model.invoiceDetailUrl)
    }

    private fun redirectToHowToPay(model: BasePaymentModel) {
        RouteManager.route(context, model.howtoPayAppLink)
    }

    private fun checkAndOpenInvoiceDetail(model: BasePaymentModel) {
        (model as VirtualAccountPaymentModel).transactionList.let {
            if (it.size > 1) showCombinedTransactionDetail(model) else openInvoiceDetail(model)
        }
    }

    fun invokeCancelSingleTransaction(
        transactionId: String,
        merchantCode: String,
        productName: String?
    ) {
        viewModel.getCancelPaymentDetail(transactionId, merchantCode, productName)
    }

    private fun openActionBottomSheet(model: BasePaymentModel) {
        val bundle = Bundle()
        bundle.putParcelable(PaymentTransactionActionSheet.PAYMENT_MODEL, model)
        PaymentTransactionActionSheet.show(bundle, childFragmentManager)
    }

    fun showCombinedTransactionDetail(model: BasePaymentModel) {
        (model as VirtualAccountPaymentModel).let {
            val bundle = Bundle()
            bundle.putParcelableArrayList(
                PaymentTransactionDetailSheet.TRANSACTION_LIST,
                model.transactionList
            )
            bundle.putString(PaymentTransactionDetailSheet.GATEWAY_NAME, model.gatewayName)
            PaymentTransactionDetailSheet.show(bundle, childFragmentManager)
        }
    }

    private fun showToast(toastMessage: String?, toastType: Int) {
        Toaster.make(recycler_view, toastMessage ?: "", Toaster.LENGTH_LONG, toastType)
    }

    private fun handleSwipeRefresh(show: Boolean) {
        swipe_refresh_layout.isRefreshing = show
    }

    private fun showProgressForDelayedFetch() {
        handleSwipeRefresh(false)
        loader?.show()
    }

    private fun hideLoader() {
        loader?.dialog?.dismiss()
        loader = null
    }


    companion object {
        const val ACTION_HOW_TO_PAY_REDIRECTION = 1
        const val ACTION_INVOICE_PAGE_REDIRECTION = 2
        const val ACTION_INVOICE_PAGE_REDIRECTION_COMBINED_VA = 3
        const val ACTION_CHEVRON_ACTIONS = 4
        fun createInstance() = DeferredPaymentListFragment()
    }

    override fun onRefresh() {
        viewModel.refreshPage()
        loadInitialDeferredTransactions()
    }

}