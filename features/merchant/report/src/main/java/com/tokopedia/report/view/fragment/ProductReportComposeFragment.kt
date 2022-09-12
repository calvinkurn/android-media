package com.tokopedia.report.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.report.data.constant.GeneralConstant
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.data.util.MerchantReportTracking
import com.tokopedia.report.di.MerchantReportComponent
import com.tokopedia.report.view.activity.ProductReportFormActivity
import com.tokopedia.report.view.adapter.ReportReasonAdapter
import com.tokopedia.report.view.fragment.components.ProductReportComposeContent
import com.tokopedia.report.view.fragment.models.ProductReportUiEvent
import com.tokopedia.report.view.util.extensions.argsString
import com.tokopedia.report.view.viewmodel.ProductReportViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest


class ProductReportComposeFragment : BaseDaggerFragment(), ReportReasonAdapter.OnReasonClick {

    private val tracking by lazy { MerchantReportTracking() }

    override fun scrollToTop() {}

    override fun gotoForm(reason: ProductReportReason) {
        tracking.eventReportReason(reason.strLabel)
        activity?.let {
            startActivityForResult(
                ProductReportFormActivity.createIntent(it, reason, productId),
                REQUEST_CODE_FORM_SUBMIT
            )
        }
    }

    val isInRoot: Boolean
        get() = true

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<ProductReportViewModel> { viewModelFactory }

    private val productId by argsString(ARG_PRODUCT_ID, "-1")

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(MerchantReportComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observe()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViewTreeOwners()

        return ComposeView(requireContext()).apply {
            setContent {
                val uiState = viewModel.uiState.collectAsState()

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
    }

    private fun initViewTreeOwners() {
        // Set the view tree owners before setting the content view so that the inflation process
        // and attach listeners will see them already present
        val decoderView = requireActivity().window.decorView
        ViewTreeLifecycleOwner.set(decoderView, this)
        ViewTreeViewModelStoreOwner.set(decoderView, this)
        ViewTreeSavedStateRegistryOwner.set(decoderView, this)
    }

    private fun observe() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collectLatest {
                when (it) {
                    is ProductReportUiEvent.FooterClicked -> onFooterClicked()
                }
            }
        }
    }

    private fun onFooterClicked() {
        val appLink =  "${ApplinkConst.WEBVIEW}?url=${GeneralConstant.URL_REPORT_TYPE}"
        tracking.eventReportLearnMore()
        RouteManager.route(requireContext(), appLink)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_FORM_SUBMIT) {
            activity?.run {
                setResult(Activity.RESULT_OK)
                finish()
            }
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

    fun onBackPressed() {
    }

    companion object {
        private const val ARG_PRODUCT_ID = "arg_product_id"
        private const val REQUEST_CODE_FORM_SUBMIT = 100

        fun createInstance(productId: String) = ProductReportComposeFragment().apply {
            arguments = Bundle().also {
                it.putString(ARG_PRODUCT_ID, productId)
            }
        }
    }
}