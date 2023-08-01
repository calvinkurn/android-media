package com.tokopedia.shop.flashsale.presentation.creation.rule

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.SpannedString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.campaign.data.response.RollenceGradualRollout
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentCampaignRuleBinding
import com.tokopedia.shop.flashsale.common.constant.BundleConstant
import com.tokopedia.shop.flashsale.common.extension.disable
import com.tokopedia.shop.flashsale.common.extension.doOnDelayFinished
import com.tokopedia.shop.flashsale.common.extension.enable
import com.tokopedia.shop.flashsale.common.extension.setFragmentToUnifyBgColor
import com.tokopedia.shop.flashsale.common.extension.showError
import com.tokopedia.shop.flashsale.common.extension.showLoading
import com.tokopedia.shop.flashsale.common.extension.stopLoading
import com.tokopedia.shop.flashsale.common.extension.toBulletSpan
import com.tokopedia.shop.flashsale.common.util.FlashSaleTokoUtil
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC
import com.tokopedia.shop.flashsale.domain.entity.RelatedCampaign
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import com.tokopedia.shop.flashsale.domain.entity.enums.isDraft
import com.tokopedia.shop.flashsale.presentation.creation.information.CampaignInformationActivity
import com.tokopedia.shop.flashsale.presentation.creation.rule.adapter.OnRemoveRelatedCampaignListener
import com.tokopedia.shop.flashsale.presentation.creation.rule.adapter.RelatedCampaignAdapter
import com.tokopedia.shop.flashsale.presentation.creation.rule.adapter.RelatedCampaignItemDecoration
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.BuyerSettingBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.ChoosePaymentMethodBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.MerchantCampaignTNCBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.PaymentMethodBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign.ChooseRelatedCampaignBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.relatedcampaign.ChooseRelatedCampaignBottomSheetListener
import com.tokopedia.shop.flashsale.presentation.creation.rule.dialog.CreateCampaignConfirmationDialog
import com.tokopedia.shop.flashsale.presentation.list.container.CampaignListActivity
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CampaignRuleFragment :
    BaseDaggerFragment(),
    OnRemoveRelatedCampaignListener,
    MerchantCampaignTNCBottomSheet.ConfirmationClickListener,
    ChooseRelatedCampaignBottomSheetListener,
    CreateCampaignConfirmationDialog.CreateCampaignConfirmationListener {

    companion object {
        private const val FOURTH_STEP = 4
        private const val RELATED_CAMPAIGN_OFFSET_DP = 8
        private const val REDIRECTION_TO_CAMPAIGN_LIST_PAGE_DELAY: Long = 1000

        @JvmStatic
        fun newInstance(campaignId: Long, pageMode: PageMode): CampaignRuleFragment {
            return CampaignRuleFragment().apply {
                arguments = Bundle().apply {
                    putLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID, campaignId)
                    putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, pageMode)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    private val sellerEduArticleUrl = "https://www.tokopedia.com/help/seller/article/flash-sale-toko-alokasi-stok-campaign"
    private val campaignId by lazy { arguments?.getLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID) }
    private val pageMode by lazy {
        arguments?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) ?: PageMode.CREATE
    }
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignRuleViewModel::class.java) }

    private var binding by autoClearedNullable<SsfsFragmentCampaignRuleBinding>()

    private var relatedCampaignAdapter: RelatedCampaignAdapter? = null

    private var errorToaster: Snackbar? = null
    private var irisSession: IrisSession? = null

    private val tncCheckboxChangeListener = object : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            if (isChecked) {
                buttonView?.isChecked = false
                viewModel.onTNCCheckboxChecked()
            } else {
                viewModel.onTNCCheckboxUnchecked()
            }
        }
    }

    override fun getScreenName(): String = CampaignRuleFragment::class.java.canonicalName.orEmpty()
    override fun initInjector() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsFragmentCampaignRuleBinding.inflate(inflater, container, false)
        if (irisSession == null && container != null) irisSession = IrisSession(container.context)
        userSession = UserSession(context)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentToUnifyBgColor()
        initCampaignDetail()
        setUpView()
    }

    private fun initCampaignDetail() {
        val campaignId = campaignId ?: return
        binding?.loader?.visible()
        viewModel.getCampaignDetail(campaignId)
    }

    private fun setUpView() {
        getRollenceGradualRollout()
        handlePageMode()
        setUpToolbar()
        setUpClickListeners()
        setUpUniqueAccountTips()
        setUpTNCText()
        setUpRelatedCampaignRecyclerView()
        observeOutOfStockStatus()
        observeCampaign()
        observeSelectedPaymentMethod()
        observeSelectedOosState()
        observeUniqueAccountSelected()
        observeCampaignRelationSelected()
        observeRelatedCampaignRemovedEvent()
        observeAllInputValid()
        observeTNCClickEvent()
        observeTNCConfirmationClick()
        observeCampaignCreationAllowed()
        observeAddRelatedCampaignButtonEvent()
        observeSaveDraftState()
        observeCreateCampaignState()
    }

    private fun setUpToolbar() {
        val header = binding?.header ?: return
        header.headerSubTitle = String.format(
            getString(R.string.sfs_placeholder_step_counter),
            FOURTH_STEP
        )
        header.setNavigationOnClickListener { activity?.finish() }
    }

    private fun setUpClickListeners() {
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

        binding.radioOosHandlingOptions.setOnCheckedChangeListener { _, checkedId ->
            val isEnableTransaction = checkedId == R.id.radio_oos_option_can_transact
            viewModel.setOosStatus(isEnableTransaction = isEnableTransaction)
        }

        binding.radioUniqueAccountHandling.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radio_unique_account_yes) {
                viewModel.onNotRequireUniqueAccountSelected()
            } else {
                viewModel.onRequireUniqueAccountSelected()
            }
        }

        binding.radioHandlingCampaignRelations.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radio_option_campaign_relations_yes) {
                viewModel.onAllowCampaignRelation()
            } else {
                viewModel.onDisallowCampaignRelation()
            }
        }

        binding.checkboxCampaignRuleTnc.setOnCheckedChangeListener(tncCheckboxChangeListener)

        binding.btnSaveDraft.setOnClickListener {
            viewModel.saveCampaignCreationDraft()
        }

        binding.btnCreateCampaign.setOnClickListener {
            viewModel.onCreateCampaignButtonClicked()
        }

        binding.btnChoosePreviousCampaign.setOnClickListener {
            viewModel.onAddRelatedCampaignButtonClicked()
        }

        childFragmentManager.registerFragmentLifecycleCallbacks(
            object :
                FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                    super.onFragmentResumed(fm, f)
                    when (f) {
                        is MerchantCampaignTNCBottomSheet -> {
                            f.setConfirmationClickListener(this@CampaignRuleFragment)
                        }
                        is ChooseRelatedCampaignBottomSheet -> {
                            f.setChooseRelatedCampaignListener(this@CampaignRuleFragment)
                        }
                        is CreateCampaignConfirmationDialog -> {
                            f.setCreateCampaignConfirmationListener(this@CampaignRuleFragment)
                        }
                    }
                }
            },
            false
        )
    }

    private fun handlePageMode() {
        if (pageMode == PageMode.UPDATE || pageMode == PageMode.DRAFT) {
            binding?.btnSaveDraft?.gone()
        }
    }

    private fun getTNCText(): SpannableString? {
        val context = context ?: return null
        val tncText = getText(R.string.campaign_rule_tnc_checkbox_label) as SpannedString
        val styledSpans = tncText.getSpans(0, tncText.length, StyleSpan::class.java)
        val spannableString = SpannableString(tncText)
        val color = ContextCompat.getColor(
            context,
            com.tokopedia.unifyprinciples.R.color.Unify_GN500
        )
        styledSpans.forEach { span ->
            spannableString.setSpan(
                ForegroundColorSpan(color),
                tncText.getSpanStart(span),
                tncText.getSpanEnd(span),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannableString
    }

    private fun setUpTNCText() {
        val binding = binding ?: return
        binding.checkboxCampaignRuleTnc.text = getTNCText()
    }

    private fun setupOosHandler(isShowOosSection: Boolean) {
        val binding = binding ?: return
        if (isShowOosSection) {
            binding.viewGroupOosHandling.visible()
            binding.tgCampaignFsOosTitle.apply {
                val txtOosWording: String = getText(R.string.campaign_rule_fs_out_of_stock_title).toString()
                text = getOosSpan(txtOosWording)
                movementMethod = LinkMovementMethod.getInstance()
            }
        } else {
            binding.viewGroupOosHandling.gone()
        }
    }

    private fun getOosSpan(spanString: String): CharSequence? {
        val spannableString = SpannableString(spanString)
        val urlLink = "${ApplinkConst.WEBVIEW}?url=$sellerEduArticleUrl"
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                RouteManager.route(context, urlLink)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                ds.typeface = Typeface.DEFAULT_BOLD
            }
        }
        val start = (spanString.length - 15)
        val end = spanString.length
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    private fun observeSelectedPaymentMethod() {
        viewModel.selectedPaymentType.observe(viewLifecycleOwner) { selectedPaymentMethod ->
            when (selectedPaymentMethod) {
                PaymentType.INSTANT -> onInstantPaymentMethodSelected()
                PaymentType.REGULAR -> onRegularPaymentMethodSelected()
                else -> clearSelectedPaymentMethod()
            }
        }
    }

    private fun observeSelectedOosState() {
        viewModel.selectedOosState.observe(viewLifecycleOwner) { it ->
            showOosStateRadioSelected(it)
        }
    }

    // Get Rollence Gradual Rollout to check whether the new feature is available to user or not
    private fun getRollenceGradualRollout() {
        irisSession?.let { iris_session ->
            viewModel.getRollenceGradualRollout(
                shopId = userSession.shopId,
                irisSessionId = iris_session.getSessionId()
            )
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

    private fun showOosStateRadioSelected(oosState: Boolean) {
        val binding = binding ?: return
        if (oosState) {
            binding.radioOosOptionCanTransact.isChecked = true
        } else {
            binding.radioOosOptionCanNotTransact.isChecked = true
        }
    }

    private fun showPaymentMethodBottomSheet() {
        PaymentMethodBottomSheet().show(childFragmentManager)
    }

    private fun showChoosePaymentMethodBottomSheet() {
        ChoosePaymentMethodBottomSheet().show(childFragmentManager)
    }

    private fun showBuyerSettingBottomSheet() {
        BuyerSettingBottomSheet().show(childFragmentManager)
    }

    private fun setUpUniqueAccountTips() {
        val binding = binding ?: return
        binding.tgUniqueAccountPoint1.text = SpannableString(
            getText(R.string.campaign_rule_unique_account_tips_point_1)
        ).toBulletSpan()
        binding.tgUniqueAccountPoint2.text = SpannableString(
            getText(R.string.campaign_rule_unique_account_tips_point_2)
        ).toBulletSpan()
    }

    private fun observeUniqueAccountSelected() {
        viewModel.isUniqueBuyer.observe(viewLifecycleOwner) {
            when (it) {
                false -> onUniqueAccountRequired()
                true -> onUniqueAccountNotRequired()
                else -> {
                    // no-op
                }
            }
        }
    }

    private fun onUniqueAccountRequired() {
        val binding = binding ?: return
        binding.radioUniqueAccountYes.isChecked = false
        binding.radioUniqueAccountNo.isChecked = true
        binding.tipsUniqueAccount.visible()
        binding.groupAllowCampaignRelation.visible()
    }

    private fun onUniqueAccountNotRequired() {
        val binding = binding ?: return
        binding.radioUniqueAccountYes.isChecked = true
        binding.radioUniqueAccountNo.isChecked = false
        binding.tipsUniqueAccount.hide()
        binding.groupAllowCampaignRelation.hide()
    }

    private fun observeCampaignRelationSelected() {
        viewModel.isCampaignRelation.observe(viewLifecycleOwner) {
            when (it) {
                true -> renderNotCampaignRelation()
                false -> renderCampaignRelation()
                else -> {
                    // no-op
                }
            }
        }
        viewModel.isRelatedCampaignsVisible.observe(viewLifecycleOwner) {
            when (it) {
                true -> showRelatedCampaignsGroup()
                false -> hideRelatedCampaignsGroup()
                else -> {
                    // no-op
                }
            }
        }
        viewModel.isRelatedCampaignButtonActive.observe(viewLifecycleOwner) {
            renderAddRelatedCampaignButton(it)
        }
        viewModel.relatedCampaigns.observe(viewLifecycleOwner) {
            relatedCampaignAdapter?.submitList(it)
        }
    }

    private fun renderAddRelatedCampaignButton(active: Boolean?) {
        val choosePreviousCampaignButton = binding?.btnChoosePreviousCampaign ?: return
        when (active) {
            true -> choosePreviousCampaignButton.enable()
            false -> choosePreviousCampaignButton.disable()
            else -> {
                // no-op
            }
        }
    }

    private fun renderNotCampaignRelation() {
        val binding = binding ?: return
        binding.radioOptionCampaignRelationsYes.isSelected = true
        binding.radioOptionCampaignRelationsNo.isSelected = false
    }

    private fun showRelatedCampaignsGroup() {
        val binding = binding ?: return
        binding.groupChoosePreviousCampaign.visible()
    }

    private fun hideRelatedCampaignsGroup() {
        val binding = binding ?: return
        binding.groupChoosePreviousCampaign.hide()
    }

    private fun renderCampaignRelation() {
        val binding = binding ?: return
        binding.radioOptionCampaignRelationsYes.isSelected = false
        binding.radioOptionCampaignRelationsNo.isSelected = true
    }

    private fun setUpRelatedCampaignRecyclerView() {
        val binding = binding ?: return
        relatedCampaignAdapter = RelatedCampaignAdapter(this)
        binding.rvSelectedPreviousCampaign.apply {
            adapter = relatedCampaignAdapter
            addItemDecoration(
                RelatedCampaignItemDecoration(
                    RELATED_CAMPAIGN_OFFSET_DP.toPx(),
                    RELATED_CAMPAIGN_OFFSET_DP.toPx()
                )
            )
            layoutManager = ChipsLayoutManager.newBuilder(context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build()
        }
    }

    override fun onRelatedCampaignRemoved(relatedCampaign: RelatedCampaign) {
        viewModel.onRemoveRelatedCampaign(relatedCampaign)
    }

    private fun showRelatedCampaignRemovedToaster() {
        val binding = binding ?: return
        Toaster.build(
            binding.wrapperCampaignRuleContent,
            getString(R.string.campaign_rule_previous_campaign_item_removed),
            actionText = getString(R.string.campaign_rule_previous_campaign_item_removed_cta)
        )
            .setAnchorView(binding.cardButtonWrapper)
            .show()
    }

    private fun observeRelatedCampaignRemovedEvent() {
        viewModel.isRelatedCampaignRemoved.observe(viewLifecycleOwner) {
            showRelatedCampaignRemovedToaster()
        }
    }

    private fun observeAllInputValid() {
        viewModel.isAllInputValid.observe(viewLifecycleOwner) { valid ->
            if (valid) {
                showTNCSection()
            } else {
                hideTNCSection()
            }
        }
    }

    private fun showTNCSection() {
        val binding = binding ?: return
        binding.groupCampaignRuleTnc.show()
    }

    private fun hideTNCSection() {
        val binding = binding ?: return
        binding.groupCampaignRuleTnc.hide()
    }

    private fun observeTNCClickEvent() {
        viewModel.tncClickEvent.observe(viewLifecycleOwner) { request ->
            showTNCBottomSheet(request)
        }
    }

    private fun showTNCBottomSheet(request: MerchantCampaignTNC.TncRequest) {
        MerchantCampaignTNCBottomSheet.createInstance(
            showTickerAndButton = true,
            tncRequest = request
        )
            .show(childFragmentManager)
    }

    override fun onTNCConfirmationClicked() {
        viewModel.onTNCConfirmationClicked()
    }

    private fun observeTNCConfirmationClick() {
        viewModel.isTNCConfirmed.observe(viewLifecycleOwner) { isTNCConfirmed ->
            if (isTNCConfirmed) {
                checkTNCCheckbox()
            } else {
                unCheckTNCCheckbox()
            }
        }
    }

    private fun checkTNCCheckbox() {
        val binding = binding ?: return
        binding.checkboxCampaignRuleTnc.setOnCheckedChangeListener(null)
        binding.checkboxCampaignRuleTnc.isChecked = true
        binding.checkboxCampaignRuleTnc.setOnCheckedChangeListener(tncCheckboxChangeListener)
    }

    private fun unCheckTNCCheckbox() {
        val binding = binding ?: return
        binding.checkboxCampaignRuleTnc.isChecked = false
    }

    private fun observeCampaignCreationAllowed() {
        viewModel.isCampaignCreationAllowed.observe(viewLifecycleOwner) { allowed ->
            if (allowed) enableCreateCampaignButton() else disableCreateCampaignButton()
        }
    }

    private fun enableCreateCampaignButton() {
        val binding = binding ?: return
        binding.btnCreateCampaign.enable()
    }

    private fun disableCreateCampaignButton() {
        val binding = binding ?: return
        binding.btnCreateCampaign.disable()
    }

    private fun observeCampaign() {
        viewModel.campaign.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> showCampaignRuleInputForm(result.data)
                is Fail -> showError(result.throwable)
            }
        }
    }

    private fun showCampaignRuleInputForm(data: CampaignUiModel) {
        val binding = binding ?: return
        renderCampaignRuleButton(data)
        binding.wrapperCampaignRuleContent.visible()
        binding.loader.hide()
    }

    private fun renderCampaignRuleButton(data: CampaignUiModel) {
        val binding = binding ?: return
        binding.cardButtonWrapper.visible()
        binding.btnCreateCampaign.text = if (data.status.isDraft()) {
            getString(R.string.sfs_create_campaign)
        } else {
            getString(R.string.sfs_save_changes)
        }
    }

    private fun showError(throwable: Throwable) {
        val binding = binding ?: return
        binding.cardButtonWrapper.hide()
        binding.wrapperCampaignRuleContent.hide()
        binding.loader.hide()
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        errorToaster = Toaster.build(
            binding.root,
            text = errorMessage,
            duration = Toaster.LENGTH_INDEFINITE,
            type = Toaster.TYPE_ERROR,
            actionText = getString(com.tokopedia.abstraction.R.string.title_try_again),
            clickListener = { onCampaignErrorActionClicked() }
        ).also {
            it.show()
        }
    }

    private fun onCampaignErrorActionClicked() {
        binding?.loader?.visible()
        viewModel.reFetchCampaignDetail()
    }

    private fun observeSaveDraftState() {
        viewModel.saveDraftActionState.observe(viewLifecycleOwner) {
            when (it) {
                is CampaignRuleActionResult.Loading -> {
                    showSaveDraftButtonLoading()
                }
                is CampaignRuleActionResult.ValidationFail -> {
                    hideSaveDraftButtonLoading()
                    showValidationErrorMessage(it.result)
                }
                is CampaignRuleActionResult.Success -> routeToCampaignListPage(isSaveDraft = true)
                is CampaignRuleActionResult.Fail -> {
                    showActionErrorMessage(it.error)
                    hideSaveDraftButtonLoading()
                }
                else -> {
                    // no-op
                }
            }
        }
    }

    private fun observeOutOfStockStatus() {
        viewModel.isGetGradualRollout.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    val isFeatureAvailable = isUserGetGradualRollout(result.data)
                    setupOosHandler(isShowOosSection = isFeatureAvailable)
                }
                is Fail -> {
                    // Disable UI OOS
                    setupOosHandler(isShowOosSection = false)
                }
            }
        }
    }

    private fun isUserGetGradualRollout(data: RollenceGradualRollout): Boolean {
        // Validate the list if it's not empty
        // If it's not empty, get the index 0 & read the value
        // Update the liveData based on the value is empty or not

        val rollenceList = FlashSaleTokoUtil().getKeysByPrefix(
            prefix = RollenceKey.FLASH_SALE_OUT_OF_STOCK_GRADUAL_ROLLOUT,
            dataRollout = data.dataRollout
        )

        return if (rollenceList.isNotEmpty()) {
            val rollenceValue = rollenceList[RollenceKey.FLASH_SALE_OUT_OF_STOCK_GRADUAL_ROLLOUT]
            val isGettingRollenceGradualRollout = rollenceValue != ""
            isGettingRollenceGradualRollout
        } else {
            false
        }
    }

    private fun observeCreateCampaignState() {
        viewModel.createCampaignActionState.observe(viewLifecycleOwner) {
            when (it) {
                is CampaignRuleActionResult.Loading -> {
                    showCreateCampaignButtonLoading()
                }
                is CampaignRuleActionResult.ValidationFail -> {
                    hideCreateCampaignButtonLoading()
                    showValidationErrorMessage(it.result)
                }
                is CampaignRuleActionResult.Success -> routeToCampaignListPage()
                is CampaignRuleActionResult.Fail -> {
                    showActionErrorMessage(it.error)
                    hideCreateCampaignButtonLoading()
                }
                is CampaignRuleActionResult.ShowConfirmation -> {
                    hideCreateCampaignButtonLoading()
                    showCreateCampaignConfirmationDialog()
                }
                else -> {
                    // no-op
                }
            }
        }
    }

    private fun showActionErrorMessage(error: CampaignRuleError) {
        if (error.message.isNotEmpty()) {
            binding?.cardButtonWrapper showError error.message
        } else if (error.cause != null) {
            binding?.cardButtonWrapper showError error.cause
        }
    }

    @SuppressLint("ResourcePackage")
    private fun showValidationErrorMessage(result: CampaignRuleValidationResult) {
        val context = context ?: return
        when (result) {
            is CampaignRuleValidationResult.InvalidCampaignTime -> {
                showInvalidCampaignTimeDialog(context, result)
            }
            is CampaignRuleValidationResult.NotEligible -> showNotEligibleDialog(context)
            CampaignRuleValidationResult.BothSectionsInvalid -> showErrorMessageToaster(R.string.campaign_rule_both_section_invalid_message)
            CampaignRuleValidationResult.InvalidBuyerOptions -> showErrorMessageToaster(R.string.campaign_rule_invalid_buyer_options_message)
            CampaignRuleValidationResult.InvalidPaymentMethod -> showErrorMessageToaster(R.string.campaign_rule_invalid_payment_type_message)
            CampaignRuleValidationResult.TNCNotAccepted -> showErrorMessageToaster(R.string.campaign_rule_tnc_not_accepted_message)
            else -> {
                // no-op
            }
        }
    }

    private fun showErrorMessageToaster(@StringRes stringId: Int) {
        showErrorMessageToaster(getString(stringId))
    }

    private fun showErrorMessageToaster(errorMessage: String) {
        val binding = binding ?: return
        binding.cardButtonWrapper showError errorMessage
    }

    private fun showInvalidCampaignTimeDialog(
        context: Context,
        result: CampaignRuleValidationResult.InvalidCampaignTime
    ) {
        val dialog = DialogUnify(
            context,
            DialogUnify.SINGLE_ACTION,
            DialogUnify.NO_IMAGE
        ).apply {
            setTitle(getString(R.string.campaign_rule_invalid_campaign_time_dialog_title))
            setDescription(getString(R.string.campaign_rule_invalid_campaign_time_dialog_description))
            setPrimaryCTAText(getString(R.string.campaign_rule_invalid_campaign_time_dialog_cta))
            setPrimaryCTAClickListener {
                routeToCampaignInformationPage(result.campaignId)
            }
        }
        dialog.show()
    }

    private fun routeToCampaignInformationPage(campaignId: Long) {
        val context = context ?: return
        CampaignInformationActivity.startUpdateMode(context, campaignId, true)
    }

    private fun showNotEligibleDialog(context: Context) {
        val dialog = DialogUnify(
            context,
            DialogUnify.VERTICAL_ACTION,
            DialogUnify.NO_IMAGE
        ).apply {
            setTitle(getString(R.string.campaign_rule_not_eligible_dialog_title))
            setDescription(getString(R.string.campaign_rule_not_eligible_dialog_message))
            setPrimaryCTAText(getString(R.string.campaign_rule_not_eligible_dialog_cta))
            setPrimaryCTAClickListener {
                routeToPmSubscribePage()
            }
            setSecondaryCTAText(getString(R.string.action_back))
            setSecondaryCTAClickListener {
                this.dismiss()
            }
        }
        dialog.show()
    }

    private fun routeToPmSubscribePage() {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConst.SellerApp.POWER_MERCHANT_SUBSCRIBE
        )
        startActivity(intent)
    }

    private fun showCreateCampaignConfirmationDialog() {
        CreateCampaignConfirmationDialog()
            .show(childFragmentManager)
    }

    private fun showSaveDraftButtonLoading() {
        binding?.btnSaveDraft.showLoading()
    }

    private fun hideSaveDraftButtonLoading() {
        binding?.btnSaveDraft.stopLoading()
    }

    private fun showCreateCampaignButtonLoading() {
        binding?.btnCreateCampaign.showLoading()
    }

    private fun hideCreateCampaignButtonLoading() {
        binding?.btnCreateCampaign.stopLoading()
    }

    private fun routeToCampaignListPage(isSaveDraft: Boolean = false) {
        val context = context ?: return

        // Add some spare time caused by Backend write operation delay
        doOnDelayFinished(REDIRECTION_TO_CAMPAIGN_LIST_PAGE_DELAY) {
            CampaignListActivity.start(
                context,
                isSaveDraft = isSaveDraft,
                pageMode
            )
        }
    }

    private fun observeAddRelatedCampaignButtonEvent() {
        viewModel.addRelatedCampaignButtonEvent.observe(viewLifecycleOwner) {
            showChooseRelatedCampaignBottomSheet(it)
        }
    }

    private fun showChooseRelatedCampaignBottomSheet(request: AddRelatedCampaignRequest) {
        ChooseRelatedCampaignBottomSheet.createInstance(
            request.campaignId,
            request.selectedRelatedCampaign
        )
            .show(childFragmentManager)
    }

    override fun onRelatedCampaignsAddButtonClicked(relatedCampaigns: List<RelatedCampaign>) {
        viewModel.onRelatedCampaignsChanged(relatedCampaigns)
    }

    override fun onCreateCampaignButtonClicked() {
        viewModel.onCreateCampaignConfirmed()
    }
}
