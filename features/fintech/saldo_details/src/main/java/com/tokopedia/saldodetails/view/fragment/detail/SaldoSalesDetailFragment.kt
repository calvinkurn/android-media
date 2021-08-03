package com.tokopedia.saldodetails.view.fragment.detail

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants.DetailScreenParams.Companion.SUMMARY_ID
import com.tokopedia.saldodetails.di.SaldoDetailsComponent
import com.tokopedia.saldodetails.response.model.saldo_detail_info.DepositHistoryData
import com.tokopedia.saldodetails.view.viewmodel.DepositHistoryInvoiceDetailViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.saldo_fragment_sales_detail.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class SaldoSalesDetailFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

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
        dataGroup.visible()
        salesProgress.gone()
        tvWithdrawalAmount.text =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(data.totalAmount, false)
        tvInvoiceNumber.text = data.invoiceNumber
        tvWithdrawalDate.text = data.createdTime
        llWithdrawalDetail.setData(data.depositDetail)
    }

    private fun onError(throwable: Throwable) {
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
        val orderUrl = viewModel?.getOrderDetailUrl()
        if (orderUrl?.isEmpty() == false)
            RouteManager.route(context, orderUrl)
    }

    private fun openInvoiceDetailPage() {
       val invoiceUrl = viewModel?.getInvoiceDetailUrl()
        if (invoiceUrl?.isNotEmpty() == true)
            RouteManager.route(activity, ApplinkConstInternalGlobal.WEBVIEW, invoiceUrl)
    }

    private fun copyToClipboard(copiedContent: String) {
        context?.let {
            try {
                val extraSpaceRegexStr = "\\s+".toRegex()
                val clipboard = it.getSystemService(Activity.CLIPBOARD_SERVICE)
                        as ClipboardManager
                val clip = ClipData.newPlainText(COPY_BOARD_LABEL,
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