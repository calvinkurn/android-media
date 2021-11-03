package com.tokopedia.product.manage.feature.violation.view.bottomsheet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.databinding.BottomSheetProductManageViolationBinding
import com.tokopedia.product.manage.feature.violation.di.DaggerViolationReasonComponent
import com.tokopedia.product.manage.feature.violation.di.ViolationReasonComponent
import com.tokopedia.product.manage.feature.violation.view.adapter.ViolationReasonAdapter
import com.tokopedia.product.manage.feature.violation.view.adapter.ViolationReasonItemViewHolder
import com.tokopedia.product.manage.feature.violation.view.uimodel.ViolationReasonUiModel
import com.tokopedia.product.manage.feature.violation.view.viewmodel.ViolationReasonViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
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

    override fun onUrlClicked(url: String) {
        goToUrl(url)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun setupView() {
        // TODO: Setup actual view, maybe add loading
    }

    private fun observeViolationReason() {
        viewModel.violationReasonUiModelLiveData.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    setSuccessView(result.data)
                }
                is Fail -> {
                    setErrorView()
                }
            }
        }
        viewModel.getViolationReason()
    }

    private fun setSuccessView(uiModel: ViolationReasonUiModel) {
        setTitle(uiModel.title)
        binding?.run {
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

    private fun setErrorView() {
        // TODO: Log errors maybe
    }

    private fun goToUrl(url: String) {
        Uri.parse(url).let { uri ->
            val myIntent = Intent(Intent.ACTION_VIEW, uri)
            context?.startActivity(myIntent)
        }
    }

}