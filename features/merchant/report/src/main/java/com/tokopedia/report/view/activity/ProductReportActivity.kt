package com.tokopedia.report.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.report.data.constant.GeneralConstant
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.data.util.MerchantReportTracking
import com.tokopedia.report.di.DaggerMerchantReportComponent
import com.tokopedia.report.view.adapter.ReportReasonAdapter
import com.tokopedia.report.view.fragment.ProductReportScreen
import com.tokopedia.report.view.fragment.models.ProductReportUiEvent
import com.tokopedia.report.view.util.extensions.argsExtraString
import com.tokopedia.report.view.viewmodel.ProductReportViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class ProductReportActivity : AppCompatActivity(), ReportReasonAdapter.OnReasonClick {

    private val tracking by lazy { MerchantReportTracking() }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<ProductReportViewModel> { viewModelFactory }

    private val productId by argsExtraString(ARG_PRODUCT_ID, "-1")

    private fun injectComponent() {
        DaggerMerchantReportComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectComponent()
        super.onCreate(savedInstanceState)

        setContent {
            LaunchedEffect(key1 = viewModel.uiEvent, block = {
                viewModel.uiEvent.collectLatest {
                    when (it) {
                        is ProductReportUiEvent.OnFooterClicked -> onFooterClicked()
                        is ProductReportUiEvent.OnScrollTop -> onScrollTop(it.reason)
                        is ProductReportUiEvent.OnGoToForm -> gotoForm(it.reason)
                        is ProductReportUiEvent.OnBackPressed -> {
                            finish()
                        }
                        else -> {
                        }
                    }
                }
            })

            ProductReportScreen(viewModel = viewModel)
        }
    }

    private fun onFooterClicked() {
        val appLink = "${ApplinkConst.WEBVIEW}?url=${GeneralConstant.URL_REPORT_TYPE}"
        tracking.eventReportLearnMore()
        RouteManager.route(this, appLink)
    }

    private fun onScrollTop(reason: ProductReportReason) {
        tracking.eventReportReason(reason.strLabel)
        scrollToTop()
    }

    override fun scrollToTop() {
    }

    override fun gotoForm(reason: ProductReportReason) {
        tracking.eventReportReason(reason.strLabel)
        val intent = ProductReportFormActivity.createIntent(this, reason, productId)
        startActivityForResult(intent, REQ_CODE_GO_FORM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_CODE_GO_FORM) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        viewModel.onEvent(ProductReportUiEvent.OnBackPressed)
    }

    companion object {
        private const val ARG_PRODUCT_ID = "arg_product_id"
        private const val REQ_CODE_GO_FORM = 32
    }
}
