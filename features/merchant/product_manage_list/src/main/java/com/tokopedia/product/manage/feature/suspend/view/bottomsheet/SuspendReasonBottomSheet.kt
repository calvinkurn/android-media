package com.tokopedia.product.manage.feature.suspend.view.bottomsheet

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.BottomSheetProductManageSuspendBinding
import com.tokopedia.product.manage.feature.suspend.di.DaggerSuspendReasonComponent
import com.tokopedia.product.manage.feature.suspend.di.SuspendReasonComponent
import com.tokopedia.product.manage.feature.suspend.view.uimodel.SuspendReasonUiModel
import com.tokopedia.product.manage.feature.suspend.view.viewmodel.SuspendReasonViewModel
import com.tokopedia.product.manage.feature.violation.view.adapter.ViolationReasonAdapter
import com.tokopedia.product.manage.feature.violation.view.adapter.ViolationReasonItemViewHolder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

open class SuspendReasonBottomSheet : BottomSheetUnify(), HasComponent<SuspendReasonComponent>,
    ViolationReasonItemViewHolder.Listener {

    companion object {
        fun createInstance(
            productId: String,
            suspendListener: Listener
        ): SuspendReasonBottomSheet {
            return SuspendReasonBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(PRODUCT_ID_KEY, productId)
                }
                listener = suspendListener
            }
        }

        private const val TAG = "SuspendReasonBottomSheet"

        private const val PRODUCT_ID_KEY = "product_id"

        private const val SCHEME_EXTERNAL = "tokopedia"
        private const val SCHEME_SELLERAPP = "sellerapp"

        private const val APPLINK_FORMAT_ALLOW_OVERRIDE = "%s?allow_override=%b&url=%s"
    }

    @Inject
    lateinit var viewModel: SuspendReasonViewModel

    private val productId by lazy {
        arguments?.getString(PRODUCT_ID_KEY).orEmpty()
    }

    private var binding by autoClearedNullable<BottomSheetProductManageSuspendBinding>()

    var listener: Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        component?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetProductManageSuspendBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = context?.getString(R.string.product_manage_suspend_title).orEmpty()
        setTitle(title)
        setupView()
        observeViolationReason()
    }

    override fun getComponent(): SuspendReasonComponent? {
        return activity?.run {
            DaggerSuspendReasonComponent.builder()
                .productManageComponent(ProductManageInstance.getComponent(application))
                .build()
        }
    }

    override fun onLinkClicked(link: String) {
        Uri.parse(link).let { uri ->
            when {
                URLUtil.isValidUrl(link) -> {
                    val appLink =
                        String.format(
                            APPLINK_FORMAT_ALLOW_OVERRIDE,
                            ApplinkConst.WEBVIEW,
                            false,
                            link
                        )
                    RouteManager.route(context, appLink)
                }
                isAppLink(uri) -> {
                    RouteManager.route(context, link)
                }
                else -> {
                    goToDefaultIntent(context, uri)
                }
            }
        }
    }

    fun show(fm: FragmentManager) {
        if (!fm.isStateSaved) {
            show(fm, TAG)
        }
    }

    private fun setupView() {
        setLoadingView()
    }

    private fun observeViolationReason() {
        viewModel.suspendReasonUiModelLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    setSuccessView(result.data)
                }
                is Fail -> {
                    setErrorView(result.throwable)
                }
            }
        }
        viewModel.getSuspendReason(productId)
    }

    private fun setLoadingView() {
        binding?.productManageSuspendLoadingGroup?.show()
        binding?.productManageSuspendSuccessGroup?.gone()
    }

    private fun setSuccessView(uiModel: SuspendReasonUiModel) {
        binding?.run {
            productManageSuspendLoadingGroup.gone()
            productManageSuspendSuccessGroup.show()
            tvProductManageSuspendReason.text = uiModel.infoReason
            tvProductManageSuspendInfoImpact.text = uiModel.infoImpact
            tvProductManageSuspendInfoFootNote.text = uiModel.infoFootNote
            btnProductManageSuspendAction.run {
                text = uiModel.buttonText
                setOnClickListener {
                    onLinkClicked(uiModel.buttonApplink)
                }
            }

            context?.let {
                val adapter = ViolationReasonAdapter(
                    it,
                    uiModel.infoToResolve,
                    this@SuspendReasonBottomSheet
                )
                rvProductManageSuspendStep.layoutManager = LinearLayoutManager(it)
                rvProductManageSuspendStep.adapter = adapter
            }
        }
    }

    private fun setErrorView(throwable: Throwable) {
        listener?.onSuspendError(throwable)
        dismiss()
    }

    private fun isAppLink(uri: Uri): Boolean {
        return uri.scheme == SCHEME_EXTERNAL || uri.scheme == SCHEME_SELLERAPP
    }


    private fun goToDefaultIntent(context: Context?, uri: Uri) {
        try {
            val myIntent = Intent(Intent.ACTION_VIEW, uri)
            context?.startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    interface Listener {
        fun onSuspendError(throwable: Throwable)
    }

}
