package com.tokopedia.buyerorder.recharge_download.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.buyerorder.recharge_download.di.OrderDetailRechargeDownloadWebviewComponent
import com.tokopedia.buyerorder.recharge_download.presentation.activity.OrderDetailRechargeDownloadWebviewActivity
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

    private var categoryName: String = ""
    private var productName: String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: OrderDetailRechargeDownloadWebviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        savedInstanceState?.let {
            categoryName = it.getString(OrderDetailRechargeDownloadWebviewActivity.KEY_CATEGORY_NAME, "")
            productName = it.getString(OrderDetailRechargeDownloadWebviewActivity.KEY_PRODUCT_NAME, "")
        }

        arguments?.let { args ->
            args.getString(OrderDetailRechargeDownloadWebviewActivity.KEY_CATEGORY_NAME)?.let {
                if (it.isNotEmpty()) categoryName = it
            }
            args.getString(OrderDetailRechargeDownloadWebviewActivity.KEY_PRODUCT_NAME)?.let {
                if (it.isNotEmpty()) productName = it
            }
        }

        super.onCreate(savedInstanceState)

        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        viewModel = viewModelProvider.get(OrderDetailRechargeDownloadWebviewViewModel::class.java)
        viewModel.fetchInvoiceData(invoiceId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.sendOpenScreen(categoryName, productName)

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(OrderDetailRechargeDownloadWebviewActivity.KEY_CATEGORY_NAME, categoryName)
        outState.putString(OrderDetailRechargeDownloadWebviewActivity.KEY_PRODUCT_NAME, productName)
    }

    override fun sendDownloadTrack() {
        viewModel.sendDownload(categoryName, productName)
    }

    companion object {

        fun getInstance(invoiceId: String, htmlContent: String, categoryName: String, productName: String)
                : OrderDetailRechargeDownloadWebviewFragment =
                OrderDetailRechargeDownloadWebviewFragment().also {
                    it.arguments = Bundle().apply {
                        putString(BaseDownloadHtmlActivity.KEY_INVOICE_ID, invoiceId)
                        putString(BaseDownloadHtmlActivity.KEY_HTML_CONTENT, htmlContent)
                        putString(OrderDetailRechargeDownloadWebviewActivity.KEY_CATEGORY_NAME, categoryName)
                        putString(OrderDetailRechargeDownloadWebviewActivity.KEY_PRODUCT_NAME, productName)
                    }
                }

    }

}