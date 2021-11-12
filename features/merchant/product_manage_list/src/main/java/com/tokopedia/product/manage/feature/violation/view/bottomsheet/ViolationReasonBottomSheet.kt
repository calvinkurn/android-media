package com.tokopedia.product.manage.feature.violation.view.bottomsheet

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
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.databinding.BottomSheetProductManageViolationBinding
import com.tokopedia.product.manage.feature.violation.di.DaggerViolationReasonComponent
import com.tokopedia.product.manage.feature.violation.di.ViolationReasonComponent
import com.tokopedia.product.manage.feature.violation.view.adapter.ViolationReasonAdapter
import com.tokopedia.product.manage.feature.violation.view.adapter.ViolationReasonItemViewHolder
import com.tokopedia.product.manage.feature.violation.view.uimodel.ViolationReasonUiModel
import com.tokopedia.product.manage.feature.violation.view.viewmodel.ViolationReasonViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ViolationReasonBottomSheet : BottomSheetUnify(), HasComponent<ViolationReasonComponent>,
        ViolationReasonItemViewHolder.Listener {

    companion object {
        fun createInstance(): ViolationReasonBottomSheet {
            return ViolationReasonBottomSheet()
        }

        private const val TAG = "ViolationReasonBottomSheet"

        private const val SCHEME_EXTERNAL = "tokopedia"
        private const val SCHEME_SELLERAPP = "sellerapp"

        private const val APPLINK_FORMAT_ALLOW_OVERRIDE = "%s?allow_override=%b&url=%s"
    }

    @Inject
    lateinit var viewModel: ViolationReasonViewModel

    private var binding by autoClearedNullable<BottomSheetProductManageViolationBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        component?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetProductManageViolationBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeViolationReason()
    }

    override fun getComponent(): ViolationReasonComponent? {
        return activity?.run {
            DaggerViolationReasonComponent.builder()
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
        show(fm, TAG)
    }

    private fun setupView() {
        setLoadingView()
    }

    private fun observeViolationReason() {
        viewModel.violationReasonUiModelLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    setSuccessView(result.data)
                }
                is Fail -> {
                    setErrorView(result.throwable)
                }
            }
        }
        viewModel.getViolationReason()
    }

    private fun setLoadingView() {
        binding?.productManageViolationLoadingGroup?.show()
        binding?.productManageViolationSuccessGroup?.gone()
    }

    private fun setSuccessView(uiModel: ViolationReasonUiModel) {
        setTitle(uiModel.title)
        binding?.run {
            productManageViolationLoadingGroup.gone()
            productManageViolationSuccessGroup.show()
            tvProductManageViolationTitle.text = uiModel.descTitle
            tvProductManageViolationReason.text = uiModel.descReason
            tvProductManageViolationStepTitle.text = uiModel.stepTitle
            btnProductManageViolationAction.run {
                text = uiModel.buttonText
                setOnClickListener {
                    context?.let {
                        RouteManager.route(it, uiModel.buttonApplink)
                    }
                }
            }

            context?.let {
                val adapter = ViolationReasonAdapter(
                        it,
                        uiModel.stepList,
                        this@ViolationReasonBottomSheet
                )
                rvProductManageViolationStep.layoutManager = LinearLayoutManager(it)
                rvProductManageViolationStep.adapter = adapter
            }
        }
    }

    private fun setErrorView(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        binding?.root?.let {
            Toaster.build(
                    it,
                    type = Toaster.TYPE_ERROR,
                    text = errorMessage,
                    duration = Toaster.LENGTH_LONG,
            ).show()
        }
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

}