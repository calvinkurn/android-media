package com.tokopedia.shop.flashsale.presentation.creation.information

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.*
import android.text.InputFilter.LengthFilter
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentCampaignInformationBinding
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.constant.DateConstant
import com.tokopedia.shop.flashsale.common.constant.QuantityPickerConstant
import com.tokopedia.shop.flashsale.common.constant.QuantityPickerConstant.CAMPAIGN_TEASER_MULTIPLIED_STEP_SIZE
import com.tokopedia.shop.flashsale.common.constant.QuantityPickerConstant.CAMPAIGN_TEASER_NORMAL_STEP_SIZE
import com.tokopedia.shop.flashsale.common.extension.*
import com.tokopedia.shop.flashsale.common.preference.SharedPreferenceDataStore
import com.tokopedia.shop.flashsale.common.util.DateManager
import com.tokopedia.shop.flashsale.common.util.doOnTextChanged
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.CampaignCreationResult
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.Gradient
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode
import com.tokopedia.shop.flashsale.presentation.creation.information.adapter.GradientColorAdapter
import com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet.CampaignDatePickerBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet.CampaignTeaserInformationBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet.ForbiddenWordsInformationBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.information.dialog.BackConfirmationDialog
import com.tokopedia.shop.flashsale.presentation.creation.manage.ManageProductActivity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject


class CampaignInformationFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_PAGE_MODE = "page_mode"
        private const val BUNDLE_KEY_CAMPAIGN_ID = "campaign_id"
        private const val FIRST_STEP = 1
        private const val SPAN_COUNT = 6
        private const val HEX_COLOR_TEXT_FIELD_MAX_LENGTH = 6
        private const val ONE_HOUR = 1
        private const val THRESHOLD = 12
        private const val THIRTY_MINUTE = 30
        private const val CAMPAIGN_NAME_MAX_LENGTH = 15
        private const val LEARN_MORE_CTA_TEXT_LENGTH = 8
        private const val REDIRECT_TO_PREVIOUS_PAGE_DELAY : Long = 2_000


        @JvmStatic
        fun newInstance(pageMode: PageMode, campaignId: Long): CampaignInformationFragment {
            val fragment = CampaignInformationFragment()
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_KEY_PAGE_MODE, pageMode)
            bundle.putLong(BUNDLE_KEY_CAMPAIGN_ID, campaignId)
            fragment.arguments = bundle
            return fragment
        }

    }

    private var binding by autoClearedNullable<SsfsFragmentCampaignInformationBinding>()
    private val pageMode by lazy { arguments?.getParcelable(BUNDLE_KEY_PAGE_MODE) ?: PageMode.CREATE }
    private val campaignId by lazy { arguments?.getLong(BUNDLE_KEY_CAMPAIGN_ID).orZero() }
    private val adapter = GradientColorAdapter()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var dateManager: DateManager

    @Inject
    lateinit var sharedPreference: SharedPreferenceDataStore

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignInformationViewModel::class.java) }

    override fun getScreenName(): String =
        CampaignInformationFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showBackToPreviousPageConfirmation()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsFragmentCampaignInformationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setFragmentToUnifyBgColor()
        observeValidationResult()
        observeCampaignCreation()
        observeCampaignUpdate()
        observeCampaignDetail()
        observeCampaignQuota()
        observeSaveDraft()
        handlePageMode()
        handleCoachMark()
    }

    private fun observeValidationResult() {
        viewModel.areInputValid.observe(viewLifecycleOwner) { validationResult ->
            handleValidationResult(validationResult)
        }
    }

    private fun observeCampaignCreation() {
        viewModel.campaignCreation.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    binding?.btnCreateCampaign?.stopLoading()
                    handleFirstStepOfCampaignCreationSuccess(result.data)
                }
                is Fail -> {
                    binding?.btnCreateCampaign?.stopLoading()
                    binding?.cardView showError result.throwable
                }
            }
        }
    }

    private fun observeCampaignUpdate() {
        viewModel.campaignUpdate.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    binding?.btnCreateCampaign?.stopLoading()
                    handleFirstStepOfCampaignCreationSuccess(result.data)
                }
                is Fail -> {
                    binding?.btnCreateCampaign?.stopLoading()
                    binding?.cardView showError result.throwable
                }
            }
        }
    }

    private fun observeSaveDraft() {
        viewModel.saveDraft.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    binding?.btnDraft?.stopLoading()
                    handleSaveCampaignDraftSuccess(result.data)
                }
                is Fail -> {
                    binding?.btnDraft?.stopLoading()
                    binding?.cardView showError result.throwable
                }
            }
        }
    }


    private fun observeCampaignDetail() {
        viewModel.campaignDetail.observe(viewLifecycleOwner) { result ->
            binding?.loader?.gone()
            when(result) {
                is Success -> {
                    binding?.groupContent?.visible()
                    displayCampaignDetail(result.data)
                }
                is Fail -> {
                    binding?.groupContent?.gone()
                    binding?.root showError result.throwable
                }
            }
        }
    }


    private fun observeCampaignQuota() {
        viewModel.campaignQuota.observe(viewLifecycleOwner) { result ->
            binding?.loader?.gone()
            when (result) {
                is Success -> {
                    binding?.groupContent?.visible()
                    val remainingQuota = result.data
                    viewModel.setRemainingQuota(remainingQuota)
                    handleRemainingQuota(remainingQuota)
                }
                is Fail -> {
                    binding?.groupContent?.gone()
                    binding?.root showError result.throwable
                }
            }
        }
    }


    private fun setupView() {
        binding?.switchTeaser?.isChecked = true
        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        setupTextFields()
        setupDatePicker()
        setupScrollListener()
    }

    private fun setupScrollListener() {
        binding?.scrollView?.isVerticalScrollBarEnabled = false
        binding?.scrollView?.isHorizontalScrollBarEnabled = false
        binding?.scrollView?.viewTreeObserver?.addOnScrollChangedListener {
            val scrollY = binding?.scrollView?.scrollY.orZero()
            if (scrollY.isScrollUp()) {
                binding?.cardView?.visible()
            } else {
                binding?.cardView?.invisible()
            }
        }
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.layoutManager = GridLayoutManager(requireActivity(), SPAN_COUNT)
        binding?.recyclerView?.adapter = adapter
        adapter.submit(campaignGradientColors)
        adapter.setOnGradientClicked { selectedGradient -> handleSelectedColor(selectedGradient) }
    }

    private fun setupToolbar() {
        binding?.header?.headerSubTitle =
            String.format(getString(R.string.sfs_placeholder_step_counter), FIRST_STEP)
        binding?.header?.setNavigationOnClickListener {
            showBackToPreviousPageConfirmation()
        }
    }

    private fun setupTextFields() {
        binding?.run {
            tauCampaignName.textInputLayout.editText?.filters = arrayOf<InputFilter>(
                LengthFilter(
                    CAMPAIGN_NAME_MAX_LENGTH
                )
            )
            tauCampaignName.textInputLayout.editText?.doOnTextChanged { text ->
                displayTextLengthCounter()
                val validationResult = viewModel.validateCampaignName(text)
                handleCampaignNameValidationResult(validationResult)
            }

            tauHexColor.setMaxLength(HEX_COLOR_TEXT_FIELD_MAX_LENGTH)
            tauHexColor.textInputLayout.editText?.doOnTextChanged { text ->
                handleHexColor(text)
            }
        }
    }

    private fun setupClickListeners() {
        binding?.run {
            btnDraft.setOnClickListener { validateDraft() }
            btnCreateCampaign.setOnClickListener {
                viewModel.validateInput(getCurrentSelection(), Date())
            }
            btnApply.setOnClickListener {
                handleApplyButtonClick()
                binding?.cardView?.visible()
            }

            quantityEditor.editText.inputType = InputType.TYPE_NULL
            quantityEditor.setValueChangedListener { newValue, oldValue, _ ->
                handleQuantityEditor(newValue, oldValue)
            }

            switchTeaser.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setShowTeaser(isChecked)
                handleSwitchTeaser(isChecked)
            }

            contentSwitcher.setOnCheckedChangeListener { _, isChecked ->
                handleContentSwitcher(isChecked)
            }
            imgCampaignNameHelper.setOnClickListener {
                ForbiddenWordsInformationBottomSheet().show(childFragmentManager)
            }
            imgCampaignTeaserHelper.setOnClickListener {
                CampaignTeaserInformationBottomSheet().show(childFragmentManager)
            }
        }
    }

    private fun setupDatePicker() {
        val now = dateManager.getDefaultMinimumCampaignStartDate()
        val defaultStartDate = now.localFormatTo(DateConstant.DATE_TIME_MINUTE_LEVEL)
        viewModel.setSelectedStartDate(now)

        val endDate = now.advanceHourBy(ONE_HOUR)
        val defaultEndDate = endDate.localFormatTo(DateConstant.DATE_TIME_MINUTE_LEVEL)
        viewModel.setSelectedEndDate(endDate)

        adjustQuantityPicker(now)


        binding?.run {
            tauStartDate.editText.setText(defaultStartDate)
            tauEndDate.editText.setText(defaultEndDate)

            tauStartDate.editText.inputType = InputType.TYPE_NULL
            tauEndDate.editText.inputType = InputType.TYPE_NULL
            tauStartDate.editText.setOnClickListener { displayStartDatePicker() }
            tauEndDate.editText.setOnClickListener { displayEndDatePicker() }
        }
    }

    private fun handlePageMode() {
        if (pageMode == PageMode.CREATE) {
            viewModel.getCampaignQuota(dateManager.getCurrentMonth(), dateManager.getCurrentYear())
        }

        if (pageMode == PageMode.UPDATE) {
            viewModel.getCampaignDetail(campaignId)
        }
    }

    private fun handleCoachMark() {
        val shouldShowCoachMark = !sharedPreference.isCampaignInfoCoachMarkDismissed()
        if (shouldShowCoachMark) {
            showCoachMark()
        }
    }

    private fun handleCampaignNameValidationResult(validationResult: CampaignInformationViewModel.CampaignNameValidationResult) {
        when (validationResult) {
            CampaignInformationViewModel.CampaignNameValidationResult.CampaignNameIsEmpty -> {
                showError(getString(R.string.sfs_error_message_field_not_filled))
                binding?.cardView showError getString(R.string.sfs_error_message_incomplete_input)
                binding?.btnCreateCampaign?.disable()
            }
            CampaignInformationViewModel.CampaignNameValidationResult.CampaignNameBelowMinCharacter -> {
                showError(getString(R.string.sfs_error_message_min_char))
                binding?.btnCreateCampaign?.disable()
            }
            CampaignInformationViewModel.CampaignNameValidationResult.CampaignNameHasForbiddenWords -> {
                displayForbiddenWordError()
                binding?.btnCreateCampaign?.disable()
            }
            CampaignInformationViewModel.CampaignNameValidationResult.Valid -> {
                hideError()
                binding?.btnCreateCampaign?.enable()
            }
        }
    }

    private fun handleValidationResult(validationResult: CampaignInformationViewModel.ValidationResult) {
        when (validationResult) {
            CampaignInformationViewModel.ValidationResult.NoRemainingQuota -> {
                handleRemainingQuota(viewModel.getRemainingQuota())
            }
            CampaignInformationViewModel.ValidationResult.LapsedStartDate -> {
                showErrorTicker(getString(R.string.sfs_error_message_schedule_lapsed_title), getString(R.string.sfs_error_message_schedule_lapsed_description))
                hideLapsedTeaserTicker()
            }
            CampaignInformationViewModel.ValidationResult.LapsedTeaserStartDate -> {
                showLapsedTeaserTicker()
                hideErrorTicker()
            }
            CampaignInformationViewModel.ValidationResult.InvalidHexColor -> {
                hideLapsedTeaserTicker()
                hideErrorTicker()
                binding?.root showError getString(R.string.sfs_invalid_hex_color)
            }
            CampaignInformationViewModel.ValidationResult.Valid -> {
                hideLapsedTeaserTicker()
                hideErrorTicker()

                binding?.btnCreateCampaign?.enable()
                binding?.btnCreateCampaign.showLoading()
                val selection = getCurrentSelection()

                if (pageMode == PageMode.CREATE) {
                    viewModel.createCampaign(selection)
                } else {
                    viewModel.updateCampaign(selection, campaignId)
                }
            }

        }
    }

    private fun handleApplyButtonClick() {
        val rawHexColor = binding?.tauHexColor?.editText?.text.toString().trim().toHexColor()
        val gradient = Gradient(rawHexColor, rawHexColor, isSelected = true)
        binding?.imgHexColorPreview?.setBackgroundFromGradient(gradient)
        val deselectedColors = viewModel.deselectAllColor(adapter.snapshot())
        adapter.submit(deselectedColors)
        viewModel.setSelectedColor(gradient)
    }

    private fun handleSwitchTeaser(showTeaserSettings: Boolean) {
        binding?.groupTeaserSettings?.isVisible = showTeaserSettings
    }

    private fun handleQuantityEditor(newValue: Int, oldValue: Int) {
        val isIncreasing = newValue > oldValue
        val isDecreasing = newValue < oldValue

        if (isIncreasing && newValue % THRESHOLD == Constant.ZERO) {
            binding?.quantityEditor?.stepValue = CAMPAIGN_TEASER_MULTIPLIED_STEP_SIZE
        } else if (isDecreasing && oldValue == THRESHOLD) {
            binding?.quantityEditor?.stepValue = CAMPAIGN_TEASER_NORMAL_STEP_SIZE
        } else {
            binding?.quantityEditor?.stepValue = CAMPAIGN_TEASER_NORMAL_STEP_SIZE
        }
    }

    private fun handleContentSwitcher(hexColorOptionSelected: Boolean) {
        val isHexColorTextFieldFilled = binding?.tauHexColor?.editText?.text.toString().trim().isValidHexColor()
        binding?.recyclerView?.isVisible = !hexColorOptionSelected
        binding?.groupHexColorPicker?.isVisible = hexColorOptionSelected

        if (hexColorOptionSelected && isHexColorTextFieldFilled) {
            binding?.btnApply?.visible()
        } else {
            binding?.btnApply?.invisible()
        }
    }

    private fun handleSelectedColor(selectedGradient: Gradient) {
        val allGradients = adapter.snapshot()
        val updatedColorSelection = viewModel.markColorAsSelected(selectedGradient, allGradients)
        adapter.submit(updatedColorSelection)

        binding?.tauHexColor?.editText?.setText("")
        binding?.btnApply?.invisible()
        binding?.imgHexColorPreview?.setBackgroundResource(R.drawable.sfs_shape_rounded_color)

        binding?.cardView?.visible()
        viewModel.setSelectedColor(selectedGradient)
    }

    private fun handleHexColor(text: String) {
        if (text.isNotEmpty()) {
            val hexColor = text.toHexColor()

            if (hexColor.isValidHexColor()) {
                binding?.btnApply?.visible()
            } else {
                binding?.btnApply?.invisible()
            }
        }
    }

    private fun displayStartDatePicker() {
        val selectedDate = viewModel.getSelectedStartDate()
        val minimumDate = dateManager.getDefaultMinimumCampaignStartDate()

        val bottomSheet = CampaignDatePickerBottomSheet.newInstance(selectedDate, minimumDate)
        bottomSheet.setOnDateTimePicked { newStartDate ->
            viewModel.setSelectedStartDate(newStartDate)
            binding?.tauStartDate?.editText?.setText(newStartDate.localFormatTo(DateConstant.DATE_TIME_MINUTE_LEVEL))
            adjustEndDate()
            adjustQuantityPicker(newStartDate)
            viewModel.getCampaignQuota(newStartDate.extractMonth(), newStartDate.extractYear())
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun displayEndDatePicker() {
        val startDate = viewModel.getSelectedStartDate().advanceMinuteBy(THIRTY_MINUTE)
        val endDate = viewModel.normalizeEndDate(viewModel.getSelectedEndDate(), startDate)
        val minimumDate = viewModel.getSelectedStartDate().advanceMinuteBy(THIRTY_MINUTE)

        val bottomSheet = CampaignDatePickerBottomSheet.newInstance(endDate, minimumDate)
        bottomSheet.setOnDateTimePicked { newEndDate ->
            viewModel.setSelectedEndDate(newEndDate)
            binding?.tauEndDate?.editText?.setText(newEndDate.localFormatTo(DateConstant.DATE_TIME_MINUTE_LEVEL))
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun adjustEndDate() {
        val startDate = viewModel.getSelectedStartDate().advanceMinuteBy(THIRTY_MINUTE)
        val endDate = viewModel.normalizeEndDate(viewModel.getSelectedEndDate(), startDate)

        viewModel.setSelectedEndDate(endDate)
        binding?.tauEndDate?.editText?.setText(endDate.localFormatTo(DateConstant.DATE_TIME_MINUTE_LEVEL))
    }

    private fun adjustQuantityPicker(newStartDate : Date) {
        val currentValue = binding?.quantityEditor?.editText?.text.toString().trim().toIntOrZero()
        val maxValue = viewModel.getTeaserQuantityEditorMaxValue(newStartDate, Date())
        binding?.quantityEditor?.maxValue = maxValue
        binding?.quantityEditor?.addButton?.isEnabled = currentValue <= QuantityPickerConstant.CAMPAIGN_TEASER_MAXIMUM_UPCOMING_HOUR
        binding?.quantityEditor?.subtractButton?.isEnabled = currentValue > QuantityPickerConstant.CAMPAIGN_TEASER_MINIMUM_UPCOMING_HOUR
    }

    private fun getCurrentSelection(): CampaignInformationViewModel.Selection {
        val startDate = viewModel.getSelectedStartDate()
        val endDate = viewModel.getSelectedEndDate()
        val showTeaser = binding?.switchTeaser?.isChecked.orFalse()
        val decreaseByHour = if (showTeaser) {
            binding?.quantityEditor?.editText?.text.toString().trim().toIntOrZero()
        } else {
            Constant.ZERO
        }
        val teaserDate = startDate.decreaseHourBy(decreaseByHour)
        val firstColor = viewModel.getColor().first
        val secondColor = viewModel.getColor().second
        val paymentType = viewModel.getPaymentType()
        val remainingQuota = viewModel.getRemainingQuota()

        return CampaignInformationViewModel.Selection(
            binding?.tauCampaignName?.editText?.text.toString().trim(),
            startDate,
            endDate,
            showTeaser,
            teaserDate,
            firstColor,
            secondColor,
            paymentType,
            remainingQuota
        )
    }

    private fun validateDraft() {
        val campaignName = binding?.tauCampaignName?.editText?.text.toString().trim()
        val validationResult = viewModel.validateCampaignName(campaignName)

        if (validationResult !is CampaignInformationViewModel.CampaignNameValidationResult.Valid) {
            handleCampaignNameValidationResult(validationResult)
            return
        }

        binding?.btnDraft.showLoading()
        viewModel.saveDraft(pageMode, campaignId, getCurrentSelection())
    }

    private fun displayTextLengthCounter() {
        val length = binding?.tauCampaignName?.editText?.text.toString().trim().length
        val counterTemplate = String.format(getString(R.string.sfs_placeholder_length_counter), length, CAMPAIGN_NAME_MAX_LENGTH)
        binding?.tpgNameLengthCounter?.text = counterTemplate
    }

    private fun displayForbiddenWordError() {
        val errorMessage = getString(R.string.sfs_error_message_forbidden_words)
        val spannableString = SpannableString(errorMessage)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                ForbiddenWordsInformationBottomSheet().show(childFragmentManager)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = MethodChecker.getColor(requireContext(),com.tokopedia.unifyprinciples.R.color.Unify_G500)
                ds.isUnderlineText = false
            }
        }

        val boldSpan = StyleSpan(Typeface.BOLD)
        spannableString.setSpan(boldSpan, errorMessage.length - LEARN_MORE_CTA_TEXT_LENGTH, errorMessage.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(clickableSpan, errorMessage.length - LEARN_MORE_CTA_TEXT_LENGTH, errorMessage.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding?.tpgCampaignNameErrorMessage?.text = spannableString
        binding?.tpgCampaignNameErrorMessage?.movementMethod = LinkMovementMethod.getInstance()
        binding?.tpgCampaignNameErrorMessage?.visible()
    }


    private fun showError(errorMessage : String) {
        binding?.tpgCampaignNameErrorMessage?.text = errorMessage
        binding?.tpgCampaignNameErrorMessage?.visible()
    }

    private fun hideError() {
        binding?.tpgCampaignNameErrorMessage?.invisible()
    }

    private fun showErrorTicker(title : String, description : String) {
        binding?.tickerErrorMessage?.visible()
        binding?.tickerErrorMessage?.tickerTitle = title
        binding?.tickerErrorMessage?.setTextDescription(description)
    }

    private fun hideErrorTicker() {
        binding?.tickerErrorMessage?.gone()
    }

    private fun showLapsedTeaserTicker() {
        binding?.tickerLapsedTeaser?.visible()
    }

    private fun hideLapsedTeaserTicker() {
        binding?.tickerLapsedTeaser?.gone()
    }

    private fun displayCampaignDetail(campaign: CampaignUiModel) {
        binding?.run {
            tauCampaignName.editText.setText(campaign.campaignName)

            val isEditDateEnabled = campaign.status == CampaignStatus.DRAFT
            tauStartDate.editText.setText(campaign.startDate.formatTo(DateConstant.DATE_TIME_MINUTE_LEVEL))
            tauEndDate.editText.setText(campaign.endDate.formatTo(DateConstant.DATE_TIME_MINUTE_LEVEL))
            tauStartDate.isEnabled = isEditDateEnabled
            tauEndDate.isEnabled = isEditDateEnabled

            switchTeaser.isChecked = campaign.useUpcomingWidget
            handleSwitchTeaser(campaign.useUpcomingWidget)
            renderSelectedColor(campaign)
        }

        viewModel.setSelectedStartDate(campaign.startDate)
        viewModel.setSelectedEndDate(campaign.endDate)
        viewModel.setShowTeaser(campaign.useUpcomingWidget)
        viewModel.setSelectedColor(campaign.gradientColor)
        viewModel.setPaymentType(campaign.paymentType)
    }

    private fun renderSelectedColor(campaign: CampaignUiModel) {
        val isUsingHexColor = viewModel.isUsingHexColor(
            campaign.gradientColor.first,
            campaign.gradientColor.second
        )

        binding?.recyclerView?.isVisible = !isUsingHexColor
        binding?.groupHexColorPicker?.isVisible = isUsingHexColor


        if (isUsingHexColor) {
            binding?.btnApply?.visible()
            binding?.contentSwitcher?.isChecked = true
            binding?.tauHexColor?.editText?.setText(campaign.gradientColor.first.removeHexColorPrefix())
            binding?.imgHexColorPreview?.setBackgroundFromGradient(campaign.gradientColor)

            val colors = viewModel.deselectAllColor(adapter.snapshot())
            adapter.submit(colors)
        } else {
            binding?.btnApply?.invisible()
            val colors = viewModel.markColorAsSelected(campaign.gradientColor, adapter.snapshot())
            adapter.submit(colors)
        }
    }

    private fun showBackToPreviousPageConfirmation() {
        val dialog = BackConfirmationDialog(requireActivity())
        dialog.setOnPrimaryActionClick { requireActivity().finish() }
        dialog.setOnSecondaryActionClick {  }
        dialog.setOnThirdActionClick { validateDraft() }
        dialog.show()
    }


    private fun showCoachMark() {
        val coachMark = CoachMark2(requireActivity())
        coachMark.enableClipping = true
        coachMark.showCoachMark(populateCoachMarkItems(), null)
        coachMark.onFinishListener = {
            sharedPreference.markCampaignInfoCoachMarkComplete()
        }
        coachMark.onDismissListener = {
            sharedPreference.markCampaignInfoCoachMarkComplete()
        }
    }

    private fun populateCoachMarkItems(): ArrayList<CoachMark2Item> {
        val firstAnchorView = binding?.btnCreateCampaign ?: return arrayListOf()
        val secondAnchorView = binding?.btnDraft ?: return arrayListOf()

        return arrayListOf(
            CoachMark2Item(
                firstAnchorView,
                getString(R.string.sfs_coachmark_first),
                ""
            ),
            CoachMark2Item(
                secondAnchorView,
                getString(R.string.sfs_coachmark_second),
                ""
            ),
        )
    }

    private fun handleFirstStepOfCampaignCreationSuccess(result: CampaignCreationResult) {
        if (result.isSuccess) {
            ManageProductActivity.start(requireActivity(), result.campaignId)
        } else {
            showErrorTicker(result.errorTitle, result.errorDescription)
        }
    }

    private fun handleSaveCampaignDraftSuccess(result: CampaignCreationResult) {
        binding?.root showToaster getString(R.string.sfs_saved_as_draft)
        //Add some spare time caused by Backend write operation delay
        doOnDelayFinished(REDIRECT_TO_PREVIOUS_PAGE_DELAY) {
            if (result.isSuccess) {
                requireActivity().finish()
            } else {
                showErrorTicker(result.errorTitle, result.errorDescription)
            }
        }
    }

    private fun handleRemainingQuota(remainingQuota: Int) {
        if (remainingQuota == Constant.ZERO) {
            val monthName = viewModel.getSelectedStartDate().formatTo(DateConstant.MONTH)
            val title = String.format(
                getString(R.string.sfs_placeholder_empty_current_month_quota),
                monthName
            )
            showErrorTicker(title, getString(R.string.sfs_create_campaign_on_another_period))
        } else {
            hideErrorTicker()
        }
    }
}