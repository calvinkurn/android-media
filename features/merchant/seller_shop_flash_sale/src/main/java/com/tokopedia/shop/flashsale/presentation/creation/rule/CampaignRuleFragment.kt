package com.tokopedia.shop.flashsale.presentation.creation.rule

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentCampaignRuleBinding
import com.tokopedia.shop.flashsale.common.extension.toBulletSpan
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.BuyerSettingBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.ChoosePaymentMethodBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.PaymentMethodBottomSheet
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CampaignRuleFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_PAGE_MODE = "page_mode"
        private const val FOURTH_STEP = 4

        @JvmStatic
        fun newInstance(): CampaignRuleFragment {
            return CampaignRuleFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_KEY_PAGE_MODE, pageMode)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val pageMode: PageMode? by lazy { arguments?.getParcelable(BUNDLE_KEY_PAGE_MODE) }

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignRuleViewModel::class.java) }

    private var binding by autoClearedNullable<SsfsFragmentCampaignRuleBinding>()

    override fun getScreenName(): String = CampaignRuleFragment::class.java.canonicalName.orEmpty()
    override fun initInjector() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsFragmentCampaignRuleBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }


    private fun setUpView() {
        setUpToolbar()
        setUpClickListener()
        setUpUniqueAccountTips()
        observeSelectedPaymentMethod()
        observeUniqueAccountSelected()
    }

    private fun setUpToolbar() {
        val header = binding?.header ?: return
        header.headerSubTitle = String.format(
            getString(R.string.sfs_placeholder_step_counter),
            FOURTH_STEP
        )
        header.setNavigationOnClickListener { activity?.finish() }
    }

    private fun setUpClickListener() {
        val binding = binding ?: return
        binding.iconPaymentMethodInfo.setOnClickListener {
            showPaymentMethodBottomSheet()
        }
        binding.iconChoosePaymentMethodInfo.setOnClickListener {
            showChoosePaymentMethodBottomSheet()
        }
        binding.iconBuyerSettingInfo.setOnClickListener {
            showBuyerSettingBottomSheet()
        }

        binding.chipsInstantPaymentMethod.setOnClickListener {
            viewModel.onInstantPaymentMethodSelected()
        }
        binding.chipsRegularPaymentMethod.setOnClickListener {
            viewModel.onRegularPaymentMethodSelected()
        }
        binding.chipsUniqueAccountYes.setOnClickListener {
            viewModel.onNotRequireUniqueAccountSelected()
        }
        binding.chipsUniqueAccountNo.setOnClickListener {
            viewModel.onRequireUniqueAccountSelected()
        }
    }

    private fun observeSelectedPaymentMethod() {
        viewModel.selectedPaymentMethod.observe(viewLifecycleOwner) { selectedPaymentMethod ->
            when (selectedPaymentMethod) {
                PaymentMethod.Instant -> onInstantPaymentMethodSelected()
                PaymentMethod.Regular -> onRegularPaymentMethodSelected()
                else -> clearSelectedPaymentMethod()
            }
        }
    }

    private fun onRegularPaymentMethodSelected() {
        val binding = binding ?: return
        binding.chipsInstantPaymentMethod.chipType = ChipsUnify.TYPE_NORMAL
        binding.chipsRegularPaymentMethod.chipType = ChipsUnify.TYPE_SELECTED
    }

    private fun onInstantPaymentMethodSelected() {
        val binding = binding ?: return
        binding.chipsInstantPaymentMethod.chipType = ChipsUnify.TYPE_SELECTED
        binding.chipsRegularPaymentMethod.chipType = ChipsUnify.TYPE_NORMAL
    }

    private fun clearSelectedPaymentMethod() {
        val binding = binding ?: return
        binding.chipsInstantPaymentMethod.chipType = ChipsUnify.TYPE_NORMAL
        binding.chipsRegularPaymentMethod.chipType = ChipsUnify.TYPE_NORMAL
    }

    private fun showPaymentMethodBottomSheet() {
        val context = context ?: return
        PaymentMethodBottomSheet.createInstance(context)
            .show(parentFragmentManager)
    }

    private fun showChoosePaymentMethodBottomSheet() {
        val context = context ?: return
        ChoosePaymentMethodBottomSheet.createInstance(context)
            .show(parentFragmentManager)
    }

    private fun showBuyerSettingBottomSheet() {
        val context = context ?: return
        BuyerSettingBottomSheet.createInstance(context)
            .show(parentFragmentManager)
    }

    @SuppressLint("ResourcePackage")
    private fun setUpUniqueAccountTips() {
        val binding = binding ?: return
        binding.tgUniqueAccountPoint1.text =
            SpannableString(getString(R.string.campaign_rule_unique_account_tips_point_1)).toBulletSpan()
        binding.tgUniqueAccountPoint2.text =
            SpannableString(getString(R.string.campaign_rule_unique_account_tips_point_2)).toBulletSpan()
    }

    private fun observeUniqueAccountSelected() {
        viewModel.requireUniqueAccount.observe(viewLifecycleOwner) {
            when (it) {
                true -> onUniqueAccountRequired()
                else -> onUniqueAccountNotRequired()
            }
        }
    }

    private fun onUniqueAccountRequired() {
        val binding = binding ?: return
        binding.chipsUniqueAccountYes.chipType = ChipsUnify.TYPE_NORMAL
        binding.chipsUniqueAccountNo.chipType = ChipsUnify.TYPE_SELECTED
        binding.tipsUniqueAccount.visible()
    }

    private fun onUniqueAccountNotRequired() {
        val binding = binding ?: return
        binding.chipsUniqueAccountYes.chipType = ChipsUnify.TYPE_SELECTED
        binding.chipsUniqueAccountNo.chipType = ChipsUnify.TYPE_NORMAL
        binding.tipsUniqueAccount.hide()
    }

}