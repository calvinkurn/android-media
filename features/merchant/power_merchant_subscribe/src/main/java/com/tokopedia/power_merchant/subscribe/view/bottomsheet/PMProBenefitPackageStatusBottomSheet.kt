package com.tokopedia.power_merchant.subscribe.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil.setTextMakeHyperlink
import com.tokopedia.power_merchant.subscribe.databinding.BottomSheetBenefitPackagePmProStatusBinding
import com.tokopedia.power_merchant.subscribe.view.adapter.PMProStatusStepperAdapter
import com.tokopedia.power_merchant.subscribe.view.model.PMProStatusStepperUiModel
import com.tokopedia.unifyprinciples.Typography

class PMProBenefitPackageStatusBottomSheet :
    BaseBottomSheet<BottomSheetBenefitPackagePmProStatusBinding>() {

    private var pmProStatus = ""
    private val pmProStatusStepperAdapter by lazy { PMProStatusStepperAdapter() }

    override fun bind(view: View) = BottomSheetBenefitPackagePmProStatusBinding.bind(view)

    override fun getChildResLayout(): Int = R.layout.bottom_sheet_benefit_package_pm_pro_status

    override fun setupView() = binding?.run {
        pmProStatus = arguments?.getString(PM_PRO_STATUS_KEY) ?: ""
        tvDescBenefitPackagePmProStatus.setTextMakeHyperlink(
            getString(R.string.pm_desc_benefit_package_pm_pro_status)
        ) {
            context?.let {
                RouteManager.route(
                    it,
                    ApplinkConstInternalGlobal.WEBVIEW,
                    Constant.Url.PM_PRO_BENEFIT_PACKAGE_EDU
                )
            }
        }
        setupPMProStatusStepper()
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    private fun setupPMProStatusStepper() {
        val gridLayoutManager = GridLayoutManager(context, MAX_SPAN_COUNT)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (getBenefitPackagePMProStatusList().value.size == position + 1) {
                    SPAN_WIDTH_LAST_ITEM
                } else SPAN_WIDTH_DEFAULT
            }
        }
        binding?.rvBenefitPackagePmProStatus?.apply {
            layoutManager = gridLayoutManager
            adapter = pmProStatusStepperAdapter
        }
        pmProStatusStepperAdapter.setPMProStatusList(getBenefitPackagePMProStatusList().value)
    }

    private fun getBenefitPackagePMProStatusList(): Lazy<List<PMProStatusStepperUiModel>> {
        return lazy {
            mutableListOf<PMProStatusStepperUiModel>().apply {
                when {
                    pmProStatus.equals(Constant.PM_PRO_ADVANCED, ignoreCase = true) -> {
                        add(
                            PMProStatusStepperUiModel(
                                titleStepper = Constant.PM_PRO_ADVANCED.capitalize(),
                                colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                                colorDivider = com.tokopedia.unifyprinciples.R.color.Unify_NN100,
                                isStepperShow = true,
                                isCurrentActive = true
                            )
                        )
                        add(
                            PMProStatusStepperUiModel(
                                titleStepper = Constant.PM_PRO_EXPERT.capitalize(),
                                colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_NN400,
                                isStepperShow = true
                            )
                        )
                        add(
                            PMProStatusStepperUiModel(
                                titleStepper = Constant.PM_PRO_ULTIMATE.capitalize(),
                                colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_NN400,
                                isStepperShow = false
                            )
                        )
                    }
                    pmProStatus.equals(Constant.PM_PRO_EXPERT, ignoreCase = true) -> {
                        add(
                            PMProStatusStepperUiModel(
                                titleStepper = Constant.PM_PRO_ADVANCED.capitalize(),
                                colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_NN400,
                                colorDivider = com.tokopedia.unifyprinciples.R.color.Unify_GN300,
                                isStepperShow = true,
                                isPassedActive = true
                            )
                        )
                        add(
                            PMProStatusStepperUiModel(
                                titleStepper = Constant.PM_PRO_EXPERT.capitalize(),
                                colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                                colorDivider = com.tokopedia.unifyprinciples.R.color.Unify_NN100,
                                isStepperShow = true,
                                isCurrentActive = true
                            )
                        )
                        add(
                            PMProStatusStepperUiModel(
                                titleStepper = Constant.PM_PRO_ULTIMATE.capitalize(),
                                colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_NN400,
                                isStepperShow = false,
                                isCurrentActive = false
                            )
                        )
                    }
                    pmProStatus.equals(Constant.PM_PRO_ULTIMATE, ignoreCase = true) -> {
                        add(
                            PMProStatusStepperUiModel(
                                titleStepper = Constant.PM_PRO_ADVANCED.capitalize(),
                                colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_NN400,
                                colorDivider = com.tokopedia.unifyprinciples.R.color.Unify_GN300,
                                isStepperShow = true,
                                isPassedActive = true
                            )
                        )
                        add(
                            PMProStatusStepperUiModel(
                                titleStepper = Constant.PM_PRO_EXPERT.capitalize(),
                                colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_NN400,
                                colorDivider = com.tokopedia.unifyprinciples.R.color.Unify_GN300,
                                isStepperShow = true,
                                isPassedActive = true
                            )
                        )
                        add(
                            PMProStatusStepperUiModel(
                                titleStepper = Constant.PM_PRO_ULTIMATE.capitalize(),
                                colorStatusTitle = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
                                isStepperShow = false,
                                isCurrentActive = true
                            )
                        )
                    }
                }
            }
        }
    }


    companion object {
        private const val TAG = "PMProBenefitPackageStatusBottomSheet"
        private const val PM_PRO_STATUS_KEY = "pmProStatusKey"

        private const val SPAN_WIDTH_DEFAULT = 2
        private const val SPAN_WIDTH_LAST_ITEM = 1
        private const val MAX_SPAN_COUNT = 5

        fun createInstance(pmProStatus: String): PMProBenefitPackageStatusBottomSheet {
            return PMProBenefitPackageStatusBottomSheet().apply {
                val args = Bundle()
                args.putString(PM_PRO_STATUS_KEY, pmProStatus)
                arguments = args
            }
        }
    }

}