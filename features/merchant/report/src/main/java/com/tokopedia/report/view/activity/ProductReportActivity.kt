package com.tokopedia.report.view.activity

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.report.data.constant.GeneralConstant
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.data.util.MerchantReportTracking
import com.tokopedia.report.di.DaggerMerchantReportComponent
import com.tokopedia.report.view.adapter.ReportReasonAdapter
import com.tokopedia.report.view.fragment.components.ProductReportComposeContent
import com.tokopedia.report.view.fragment.models.ProductReportUiEvent
import com.tokopedia.report.view.util.extensions.argsExtraString
import com.tokopedia.report.view.viewmodel.ProductReportViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest

class ProductReportActivity : ComponentActivity(), ReportReasonAdapter.OnReasonClick {

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
            val uiState = viewModel.uiState.collectAsState()

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

            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                ProductReportComposeContent(
                    uiState = uiState.value,
                    onEvent = viewModel::onEvent
                )
            }
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
        launcherGoToForm.launch(ProductReportFormActivity.createIntent(this, reason, productId))
    }

    private val launcherGoToForm =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

    override fun onBackPressed() {
        viewModel.onEvent(ProductReportUiEvent.OnBackPressed)
    }

    companion object {
        private const val ARG_PRODUCT_ID = "arg_product_id"
    }
}