package com.tokopedia.topads.debit.autotopup.view.sheet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.databinding.TopadsAutoConfirmationBottomSheetBinding
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.debit.autotopup.data.model.Loading
import com.tokopedia.topads.debit.autotopup.data.model.ResponseSaving
import com.tokopedia.topads.debit.autotopup.view.uimodel.AutoTopUpConfirmationUiModel
import com.tokopedia.topads.debit.autotopup.view.viewmodel.TopAdsAutoTopUpViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.lang.StringBuilder
import javax.inject.Inject

class AutoTopUpConfirmationBottomSheet : BottomSheetUnify(), HasComponent<TopAdsDashboardComponent> {

    private var binding by autoClearedNullable<TopadsAutoConfirmationBottomSheetBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var onSaved: ((isAutoAdsSaved: Boolean) -> Unit)? = null

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TopAdsAutoTopUpViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAutoTopUpConfirmation()
        setupAutoTopUpConfirmationUiModel()
    }

    override fun getComponent(): TopAdsDashboardComponent {
        return DaggerTopAdsDashboardComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isVisible) {
            show(fragmentManager, TAG)
        }
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun setChildView(inflater: LayoutInflater, container: ViewGroup?) {
        binding = TopadsAutoConfirmationBottomSheetBinding.inflate(inflater, container, false)
        showCloseIcon = true
        overlayClickDismiss = false
        clearContentPadding = true
        setTitle(getString(R.string.topads_auto_top_up_confirmation_bottom_sheet_title))
        setChild(binding?.root)
    }

    private fun setupAutoTopUpConfirmationUiModel() {
        val cacheManagerId = arguments?.getString(AUTO_TOP_UP_CONFIRMATION_CACHE_ID)

        val cacheManager = context?.let {
            SaveInstanceCacheManager(
                it,
                cacheManagerId
            )
        }
        val autoTopUpConfirmation = cacheManager?.get<AutoTopUpConfirmationUiModel>(AUTO_TOP_UP_CONFIRMATION_KEY,
            AutoTopUpConfirmationUiModel::class.java
        )

        renderViews(autoTopUpConfirmation)
        setInformationIconClicked(autoTopUpConfirmation?.subTotalStrikethrough.orEmpty())
        setActivateTopAdsBtn(autoTopUpConfirmation)
    }

    private fun observeAutoTopUpConfirmation() {
        observe(viewModel.statusSaveSelection) {
            when (it) {
                is ResponseSaving -> {
                    binding?.btnAutoTopUpConfirmation?.isLoading = false
                    onSaved?.invoke(true)
                }

                else -> {
                    binding?.btnAutoTopUpConfirmation?.isLoading = true
                }
            }
        }
    }

    private fun renderViews(autoTopUpConfirmationUiModel: AutoTopUpConfirmationUiModel?) {
        binding?.run {
            autoTopUpConfirmationUiModel?.let {
                tvAutoTopUpCreditValue.text = it.topAdsCredit
                tvAutoTopUpCreditBonusValue.text = getString(R.string.topads_auto_top_up_confirmation_bonus, it.topAdsBonus)
                tvAutoTopUpFrequencyValue.text = it.frequencyText
                tvSubtotalStrikethroughValue.shouldShowWithAction(it.subTotalStrikethrough.isNotBlank()) {
                    tvSubtotalStrikethroughValue.text = MethodChecker.fromHtml(getString(R.string.topads_auto_top_up_confirmation_actual_subtotal, it.subTotalStrikethrough))
                }

                tvSubtotalActualValue.text = it.subTotalActual

                tvAutoTopUpConfirmationPpn.text = getString(R.string.topads_auto_top_up_confirmation_ppn, it.ppnPercent)
                tvAutoTopUpConfirmationPpnValue.text = StringBuilder("Rp").append(it.ppnAmount)

                tvNominalToBeWithdrawnValue.text = StringBuilder("Rp").append(it.totalAmount)
            }
        }
    }

    private fun setActivateTopAdsBtn(autoTopUpConfirmationUiModel: AutoTopUpConfirmationUiModel?) {
        binding?.btnAutoTopUpConfirmation?.setOnClickListener {
            autoTopUpConfirmationUiModel?.let { autoTopupConfirmation ->
                binding?.btnAutoTopUpConfirmation?.isLoading = true

                viewModel.activateTopAds(
                    true,
                    autoTopupConfirmation.selectedItemId,
                    autoTopupConfirmation.autoTopUpFrequencySelected.toString()
                )
            }
        }
    }

    private fun setInformationIconClicked(subTotalStrikeThrough: String) {

        val subTotalTitle = getString(R.string.topads_auto_top_up_confirmation_subtotal)
        val subTotalDesc = if (subTotalStrikeThrough.isNotBlank())
            getString(R.string.topads_auto_top_up_confirmation_subtotal_discount_tooltip)
        else
            getString(R.string.topads_auto_top_up_confirmation_subtotal_no_discount_tooltip)

        val totalAmountTitle = getString(R.string.topads_auto_top_up_confirmation_nominal_to_be_withdrawn)
        val totalAmountDesc = getString(R.string.topads_auto_top_up_confirmation_nominal_top_be_withdrawn_tooltip)


        binding?.icAutoTopUpConfirmationSubtotal?.setOnClickListener {
            showAutoTopUpConfirmation(subTotalTitle, subTotalDesc)
        }

        binding?.icNominalToBeWithdrawnLabel?.setOnClickListener {
            showAutoTopUpConfirmation(totalAmountTitle, totalAmountDesc)
        }
    }

    private fun showAutoTopUpConfirmation(tooltipTitle: String, tooltipDesc: String) {
        val bottomSheet = AutoTopUpConfirmationTooltipBottomSheet.newInstance(
            tooltipTitle, tooltipDesc
        )
        bottomSheet.show(childFragmentManager)
    }

    companion object {

        private val TAG = AutoTopUpConfirmationBottomSheet::class.java.simpleName

        private const val AUTO_TOP_UP_CONFIRMATION_CACHE_ID = "auto_top_up_confirmation_cache_id_key"

        const val AUTO_TOP_UP_CONFIRMATION_KEY = "auto_top_up_confirmation_key"

        fun newInstance(cacheManagerId: String): AutoTopUpConfirmationBottomSheet {
            return AutoTopUpConfirmationBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(AUTO_TOP_UP_CONFIRMATION_CACHE_ID, cacheManagerId)
                }
            }
        }
    }
}
