package com.tokopedia.report.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.report.data.constant.GeneralConstant
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.data.util.MerchantReportTracking
import com.tokopedia.report.di.DaggerMerchantReportComponent
import com.tokopedia.report.view.fragment.ProductReportScreen
import com.tokopedia.report.view.fragment.models.ProductReportUiEvent
import com.tokopedia.report.view.viewmodel.ProductReportViewModel
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

class ProductReportActivity : AppCompatActivity() {

    private val tracking by lazy { MerchantReportTracking() }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory)[ProductReportViewModel::class.java]
    }

    private val productId by lazy {
        intent.data?.lastPathSegment ?: intent.extras?.getString(ARG_PRODUCT_ID) ?: "-1"
    }

    private fun injectComponent() {
        DaggerMerchantReportComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectComponent()
        super.onCreate(savedInstanceState)

        viewModel.onEvent(ProductReportUiEvent.LoadData)

        setContent {
            NestTheme {
                ProductReportScreen(
                    viewModel = viewModel,
                    onFooterClicked = ::onFooterClicked,
                    onScrollTop = ::onScrollTop,
                    onGoToForm = ::gotoForm,
                    onFinish = ::finish,
                    onToasterError = ::showToasterError
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
    }

    private fun gotoForm(reason: ProductReportReason) {
        tracking.eventReportReason(reason.strLabel)
        val intent = ProductReportFormActivity.createIntent(this, reason, productId)
        startActivityForResult(intent, REQ_CODE_GO_FORM)
    }

    private fun showToasterError(throwable: Throwable) {
        Toaster.build(
            view = findViewById(android.R.id.content),
            text = ErrorHandler.getErrorMessage(this, throwable),
            duration = Snackbar.LENGTH_INDEFINITE,
            type = Toaster.TYPE_ERROR,
            actionText = getString(com.tokopedia.abstraction.R.string.close)
        ).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_CODE_GO_FORM && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
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
