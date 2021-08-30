package com.tokopedia.saldodetails.transactionDetailPages.penjualan

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsAnalytics
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants.DetailScreenParams.Companion.SUMMARY_ID
import com.tokopedia.saldodetails.commom.di.component.SaldoDetailsComponent
import com.tokopedia.saldodetails.commom.utils.SaldoDateUtil
import com.tokopedia.saldodetails.transactionDetailPages.invoice.InvoiceDetailActivity
import com.tokopedia.saldodetails.transactionDetailPages.invoice.InvoiceDetailActivity.Companion.PARAM_INVOICE_NUMBER
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.webview.KEY_URL
import kotlinx.android.synthetic.main.saldo_fragment_sales_detail.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class SaldoSalesDetailFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var saldoDetailsAnalytics: SaldoDetailsAnalytics
    var salesDetailData: DepositHistoryData? = null

    private val viewModel: DepositHistoryInvoiceDetailViewModel? by lazy(LazyThreadSafetyMode.NONE) {
        activity?.let {
            val viewModelProvider = ViewModelProvider(it, viewModelFactory.get())
            viewModelProvider.get(DepositHistoryInvoiceDetailViewModel::class.java)
        } ?: run {
            null
        }
    }

    private val summaryId: Long by lazy {
        arguments?.getLong(SUMMARY_ID) ?: run { 0 }
    }

    override fun getScreenName(): String? = null
    override fun initInjector() = getComponent(SaldoDetailsComponent::class.java).inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.saldo_fragment_sales_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        loadSalesDetailInvoiceData()
        tvInvoiceNumber.setOnClickListener {
            openInvoiceDetailPage()
        }
        btnViewOrderDetails.setOnClickListener {
            openOrderDetailPage()
        }
        icCopy.setOnClickListener {
            copyToClipboard(tvInvoiceNumber.text.toString())
        }
    }

    private fun loadSalesDetailInvoiceData() {
        salesProgress.visible()
        viewModel?.getInvoiceDetail(summaryId.toString())
    }

    private fun initObservers() {
        viewModel?.depositHistoryLiveData?.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSuccessSalesDetailLoaded(it.data)
                is Fail -> onError(it.throwable)
            }
        })
    }

    private fun onSuccessSalesDetailLoaded(data: DepositHistoryData) {
        salesDetailData = data
        val transDateStr = DateUtil.formatDate(
            SaldoDateUtil.DATE_PATTERN_FROM_SERVER,
            SaldoDateUtil.DATE_PATTERN_FOR_UI,
            data.createdTime,
        )
        dataGroup.visible()
        salesProgress.gone()
        tvWithdrawalAmount.text =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(data.totalAmount, false)
        tvInvoiceNumber.text = data.invoiceNumber
        tvWithdrawalDate.text = context?.resources?.getString(R.string.sp_date_time_view, transDateStr) ?: ""
        llWithdrawalDetail.setData(data.depositDetail, context?.getString(R.string.saldo_sales_info_details))
    }

    private fun onError(throwable: Throwable) {
        saldoDetailsAnalytics.sendApiFailureEvents(SaldoDetailsConstants.EventLabel.SALDO_FETCH_SALES_DETAIL)
        salesProgress.gone()
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> setGlobalErrors(GlobalError.NO_CONNECTION)
            is IllegalStateException -> setGlobalErrors(GlobalError.PAGE_FULL)
            else -> setGlobalErrors(GlobalError.SERVER_ERROR)
        }
    }

    private fun setGlobalErrors(errorType: Int) {
        dataGroup.gone()
        saldoSalesDetailGlobalError.setType(errorType)
        saldoSalesDetailGlobalError.visible()
        saldoSalesDetailGlobalError.setActionClickListener {
            saldoSalesDetailGlobalError.gone()
            loadSalesDetailInvoiceData()
        }
    }

    private fun openOrderDetailPage() {
        saldoDetailsAnalytics.sendTransactionHistoryEvents(SaldoDetailsConstants.Action.SALDO_INVOICE_DETAIL_CLICK)
        val orderUrl = salesDetailData?.orderUrl
        if (orderUrl?.isEmpty() == false)
            RouteManager.route(context, orderUrl)
    }

    private fun openInvoiceDetailPage() {
        saldoDetailsAnalytics.sendTransactionHistoryEvents(SaldoDetailsConstants.Action.SALDO_INVOICE_NUMBER_CLICK)
        val invoiceUrl = salesDetailData?.invoiceUrl
        if (invoiceUrl?.isNotEmpty() == true) {
            Intent(context, InvoiceDetailActivity::class.java).apply {
                putExtra(KEY_URL, invoiceUrl)
                putExtra(PARAM_INVOICE_NUMBER, salesDetailData?.invoiceNumber)
                startActivity(this)
            }
        }
    }

    private fun copyToClipboard(copiedContent: String) {
        context?.let {
            try {
                val extraSpaceRegexStr = "\\s+".toRegex()
                val clipboard = it.getSystemService(Activity.CLIPBOARD_SERVICE)
                        as ClipboardManager
                val clip = ClipData.newPlainText(
                    COPY_BOARD_LABEL,
                    copiedContent.replace(extraSpaceRegexStr, ""))
                clipboard.setPrimaryClip(clip)
                Toaster.build(icCopy, it.getString(R.string.saldo_sales_invoice_copied_toast), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
            }catch (e : Exception){}
        }
    }

    companion object {
        private const val COPY_BOARD_LABEL = "Tokopedia"

        fun newInstance(summaryId: Long): SaldoSalesDetailFragment {
            val fragment = SaldoSalesDetailFragment()
            val bundle = Bundle().apply { putLong(SUMMARY_ID, summaryId) }
            fragment.arguments = bundle
            return fragment
        }
    }
}