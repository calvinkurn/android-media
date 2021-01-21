package com.tokopedia.buyerorder.recharge_download.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.buyerorder.recharge_download.di.OrderDetailRechargeDownloadWebviewComponent
import com.tokopedia.buyerorder.recharge_download.presentation.viewmodel.OrderDetailRechargeDownloadWebviewViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.webview.download.BaseDownloadHtmlActivity
import com.tokopedia.webview.download.BaseDownloadHtmlFragment
import javax.inject.Inject

/**
 * @author by furqan on 21/01/2021
 */
class OrderDetailRechargeDownloadWebviewFragment : BaseDownloadHtmlFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: OrderDetailRechargeDownloadWebviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        viewModel = viewModelProvider.get(OrderDetailRechargeDownloadWebviewViewModel::class.java)
        viewModel.fetchInvoiceData(invoiceId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.invoiceData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    htmlContent = it.data.data
                    renderView()
                }
                is Fail -> {

                }
            }
        })
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(OrderDetailRechargeDownloadWebviewComponent::class.java).inject(this)
    }

    companion object {

        fun getInstance(invoiceId: String, htmlContent: String): OrderDetailRechargeDownloadWebviewFragment =
                OrderDetailRechargeDownloadWebviewFragment().also {
                    it.arguments = Bundle().apply {
                        putString(BaseDownloadHtmlActivity.KEY_INVOICE_ID, invoiceId)
                        putString(BaseDownloadHtmlActivity.KEY_HTML_CONTENT, htmlContent)
                    }
                }

    }

}