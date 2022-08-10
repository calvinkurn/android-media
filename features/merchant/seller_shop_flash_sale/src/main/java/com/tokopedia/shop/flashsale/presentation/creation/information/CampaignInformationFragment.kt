package com.tokopedia.shop.flashsale.presentation.creation.information

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
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
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentCampaignInformationBinding
import com.tokopedia.shop.flashsale.common.constant.BundleConstant
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.constant.DateConstant
import com.tokopedia.shop.flashsale.common.constant.QuantityPickerConstant
import com.tokopedia.shop.flashsale.common.constant.QuantityPickerConstant.CAMPAIGN_TEASER_MULTIPLIED_STEP_SIZE
import com.tokopedia.shop.flashsale.common.constant.QuantityPickerConstant.CAMPAIGN_TEASER_NORMAL_STEP_SIZE
import com.tokopedia.shop.flashsale.common.extension.advanceDayBy
import com.tokopedia.shop.flashsale.common.extension.advanceHourBy
import com.tokopedia.shop.flashsale.common.extension.advanceMinuteBy
import com.tokopedia.shop.flashsale.common.extension.advanceMonthBy
import com.tokopedia.shop.flashsale.common.extension.daysDifference
import com.tokopedia.shop.flashsale.common.extension.decreaseHourBy
import com.tokopedia.shop.flashsale.common.extension.disable
import com.tokopedia.shop.flashsale.common.extension.doOnDelayFinished
import com.tokopedia.shop.flashsale.common.extension.enable
import com.tokopedia.shop.flashsale.common.extension.extractMonth
import com.tokopedia.shop.flashsale.common.extension.extractYear
import com.tokopedia.shop.flashsale.common.extension.formatTo
import com.tokopedia.shop.flashsale.common.extension.isValidHexColor
import com.tokopedia.shop.flashsale.common.extension.localFormatTo
import com.tokopedia.shop.flashsale.common.extension.removeHexColorPrefix
import com.tokopedia.shop.flashsale.common.extension.removeTimeZone
import com.tokopedia.shop.flashsale.common.extension.setBackgroundFromGradient
import com.tokopedia.shop.flashsale.common.extension.setFragmentToUnifyBgColor
import com.tokopedia.shop.flashsale.common.extension.setMaxLength
import com.tokopedia.shop.flashsale.common.extension.showError
import com.tokopedia.shop.flashsale.common.extension.showLoading
import com.tokopedia.shop.flashsale.common.extension.stopLoading
import com.tokopedia.shop.flashsale.common.extension.toHexColor
import com.tokopedia.shop.flashsale.common.preference.SharedPreferenceDataStore
import com.tokopedia.shop.flashsale.common.util.DateManager
import com.tokopedia.shop.flashsale.common.util.doOnTextChanged
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.CampaignCreationResult
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.Gradient
import com.tokopedia.shop.flashsale.domain.entity.aggregate.CampaignWithVpsPackages
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import com.tokopedia.shop.flashsale.presentation.creation.information.adapter.GradientColorAdapter
import com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet.CampaignDatePickerBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet.CampaignTeaserInformationBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet.ForbiddenWordsInformationBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet.TimePickerSelectionMode
import com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet.VpsPackageBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.information.dialog.CancelCreateCampaignConfirmationDialog
import com.tokopedia.shop.flashsale.presentation.creation.information.dialog.CancelEditCampaignConfirmationDialog
import com.tokopedia.shop.flashsale.presentation.creation.information.uimodel.VpsPackageUiModel
import com.tokopedia.shop.flashsale.presentation.creation.information.viewmodel.CampaignInformationViewModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.ManageProductActivity
import com.tokopedia.shop.flashsale.presentation.list.container.CampaignListActivity
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.Date
import javax.inject.Inject


class CampaignInformationFragment : BaseDaggerFragment() {

    companion object {
        private const val FIRST_STEP = 1
        private const val SINGLE_LINE = 1
        private const val SPAN_COUNT = 6
        private const val HEX_COLOR_TEXT_FIELD_MAX_LENGTH = 6
        private const val ONE_HOUR = 1
        private const val TWELVE = 12
        private const val SIX_DAYS = 6
        private const val THREE_MONTH = 3
        private const val TWO_HOURS = 2
        private const val THIRTY_MINUTE = 30
        private const val CAMPAIGN_NAME_MAX_LENGTH = 15
        private const val LEARN_MORE_CTA_TEXT_LENGTH = 8
        private const val REDIRECT_TO_PREVIOUS_PAGE_DELAY : Long = 1_500
        private const val ONE = 1
        private const val VPS_PACKAGE_ID_NOT_SELECTED : Long = 0
        private const val EMPTY_QUOTA = 0
        private const val ALPHA_DISABLED = 0.3f

        @JvmStatic
        fun newInstance(pageMode: PageMode, campaignId: Long): CampaignInformationFragment {
            val fragment = CampaignInformationFragment()
            val bundle = Bundle()
            bundle.putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, pageMode)
            bundle.putLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID, campaignId)
            fragment.arguments = bundle
            return fragment
        }

    }

    private var binding by autoClearedNullable<SsfsFragmentCampaignInformationBinding>()
    private val pageMode by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) ?: PageMode.CREATE }
    private val campaignId by lazy { arguments?.getLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID).orZero() }
    private val adapter = GradientColorAdapter()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var dateManager: DateManager

    @Inject
    lateinit var sharedPreference: SharedPreferenceDataStore

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignInformationViewModel::class.java) }

    private val defaultState by lazy {
        CampaignInformationViewModel.Selection(
            "",
            dateManager.getDefaultMinimumCampaignStartDate(),
            dateManager.getDefaultMinimumCampaignStartDate().advanceMinuteBy(THIRTY_MINUTE),
            showTeaser = true,
            dateManager.getDefaultMinimumCampaignStartDate().decreaseHourBy(ONE_HOUR),
            defaultGradientColor.first,
            defaultGradientColor.second,
            PaymentType.INSTANT,
            Int.ZERO,
            VPS_PACKAGE_ID_NOT_SELECTED
        )
    }

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
                handleBackConfirmation()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(this, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setCampaignId(campaignId)
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
        observeCurrentMonthRemainingQuota()
        observeValidationResult()
        observeCampaignCreation()
        observeCampaignUpdate()
        observeCampaignDetail()
        observeCampaignQuota()
        observeVpsPackages()
        observeSaveDraft()
        handlePageMode()
        viewModel.getCurrentMonthRemainingQuota()
    }

    private fun observeValidationResult() {
        viewModel.areInputValid.observe(viewLifecycleOwner) { validationResult ->
            handleValidationResult(validationResult)
        }
    }

    private fun observeCurrentMonthRemainingQuota() {
        viewModel.currentMonthRemainingQuota.observe(viewLifecycleOwner) { result ->
            binding?.loader?.gone()
            when (result) {
                is Success -> {
                    binding?.groupContent?.visible()
                    val remainingQuota = result.data
                    viewModel.setRemainingQuota(remainingQuota)
                    handleCoachMark()
                }
                is Fail -> {
                    binding?.groupContent?.gone()
                    binding?.cardView showError result.throwable
                }
            }
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
                    binding?.cardView showError result.throwable
                }
            }
        }
    }


    private fun observeCampaignQuota() {
        viewModel.campaignQuota.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    val remainingQuota = result.data
                    viewModel.setRemainingQuota(remainingQuota)
                    handleRemainingQuota(remainingQuota)
                }
                is Fail -> {
                    binding?.cardView showError result.throwable
                }
            }
        }
    }


    private fun observeVpsPackages() {
        viewModel.vpsPackages.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    val vpsPackages = result.data
                    viewModel.storeVpsPackage(vpsPackages)

                    val nearestExpiredVpsPackage = viewModel.findNearestExpiredVpsPackage(vpsPackages) ?: return@observe
                    viewModel.setSelectedVpsPackage(nearestExpiredVpsPackage)
                    displaySelectedVpsPackage(nearestExpiredVpsPackage)
                }
                is Fail -> {
                    binding?.cardView showError result.throwable
                }
            }
        }
    }


    private fun displaySelectedVpsPackage(vpsPackage : VpsPackageUiModel?) {
        val isUsingShopTierBenefit = vpsPackage?.isShopTierBenefit.orFalse()
        val isUsingVpsPackage = !isUsingShopTierBenefit

        val helperMessage = if (isUsingShopTierBenefit) {
            ""
        } else {
            String.format(
                getString(R.string.sfs_placeholder_remaining_vps_quota),
                vpsPackage?.currentQuota.orZero(),
                vpsPackage?.originalQuota.orZero()
            )
        }

        binding?.tauVpsPackageName?.editText?.setText(vpsPackage?.packageName)
        binding?.tauVpsPackageName?.setMessage(helperMessage)

        if (isUsingVpsPackage && vpsPackage?.currentQuota == EMPTY_QUOTA) {
            displayVpsQuotaEmptyError(vpsPackage.originalQuota)
        }
    }

    private fun setupView() {
        binding?.switchTeaser?.isChecked = true
        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        setupTextFields()
        setupDatePicker()
        setupQuotaSource()
    }


    private fun setupRecyclerView() {
        binding?.recyclerView?.itemAnimator = null
        binding?.recyclerView?.layoutManager = GridLayoutManager(activity ?: return, SPAN_COUNT)
        binding?.recyclerView?.adapter = adapter
        adapter.submit(campaignGradientColors)
        adapter.setOnGradientClicked { selectedGradient -> handleSelectedColor(selectedGradient) }
    }

    private fun setupToolbar() {
        binding?.header?.headerSubTitle = String.format(getString(R.string.sfs_placeholder_step_counter), FIRST_STEP)
        binding?.header?.setNavigationOnClickListener { handleBackConfirmation() }
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
            tauCampaignName.textInputLayout.editText?.maxLines = SINGLE_LINE
            tauCampaignName.textInputLayout.editText?.inputType = InputType.TYPE_CLASS_TEXT
            tpgCampaignNameErrorMessage.invisible()

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
                viewModel.validateInput(pageMode, getCurrentSelection(), Date())
            }
            btnApply.setOnClickListener {
                handleApplyButtonClick()
                binding?.cardView?.visible()
            }

            quantityEditor.editText.inputType = InputType.TYPE_NULL
            quantityEditor.setValueChangedListener { newValue, oldValue, _ ->
                applyUpcomingDurationTimeRule(newValue, oldValue)
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
        viewModel.setSelectedStartDate(defaultState.startDate)
        viewModel.setSelectedEndDate(defaultState.endDate)
        val quantityPickerCurrentValue = binding?.quantityEditor?.editText?.text.toString().trim().toIntOrZero()
        adjustQuantityPicker(quantityPickerCurrentValue, defaultState.startDate)

        binding?.run {
            val defaultStartDate = defaultState.startDate.localFormatTo(DateConstant.DATE_TIME_MINUTE_LEVEL)
            val defaultEndDate = defaultState.endDate.localFormatTo(DateConstant.DATE_TIME_MINUTE_LEVEL)

            tauStartDate.editText.setText(defaultStartDate)
            tauEndDate.editText.setText(defaultEndDate)

            tauStartDate.editText.inputType = InputType.TYPE_NULL
            tauStartDate.editText.isFocusable = false

            tauEndDate.editText.inputType = InputType.TYPE_NULL
            tauEndDate.editText.isFocusable = false

            tauStartDate.editText.setOnClickListener { displayStartDatePicker() }
            tauEndDate.editText.setOnClickListener { displayEndDatePicker() }
        }
    }

    private fun setupQuotaSource() {
        binding?.run {
            tauVpsPackageName.editText.inputType = InputType.TYPE_NULL
            tauVpsPackageName.editText.isFocusable = false
            tauVpsPackageName.editText.setOnClickListener { displayQuotaSourceBottomSheet() }
        }
    }

    private fun handlePageMode() {
        if (pageMode == PageMode.CREATE) {
            viewModel.storeAsDefaultSelection(defaultState)
            viewModel.getVpsPackages(VPS_PACKAGE_ID_NOT_SELECTED)
        }

        if (pageMode == PageMode.UPDATE) {
            binding?.loader?.visible()
            binding?.groupContent?.gone()
            viewModel.getCampaignDetail(campaignId)
            binding?.btnDraft?.gone()
            binding?.tauVpsPackageName?.disable()
            binding?.tauVpsPackageName?.icon1?.alpha = ALPHA_DISABLED
        }
    }

    private fun handleCoachMark() {
        val shouldShowCoachMark = !sharedPreference.isCampaignInfoCoachMarkDismissed()
        if (shouldShowCoachMark && pageMode == PageMode.CREATE) {
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
                hideLapsedTeaserTicker()
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
                binding?.cardView showError getString(R.string.sfs_invalid_hex_color)
            }
            CampaignInformationViewModel.ValidationResult.Valid -> {
                hideLapsedTeaserTicker()
                hideErrorTicker()

                binding?.btnCreateCampaign?.enable()
                binding?.btnCreateCampaign.showLoading()
                val selection = getCurrentSelection()

                viewModel.submit(selection)
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

    private fun applyUpcomingDurationTimeRule(newValue: Int, oldValue: Int) {
        val isIncreasing = newValue > oldValue
        val isDecreasing = newValue < oldValue

        when {
            isIncreasing && oldValue == TWELVE -> {
                val newCounter = oldValue + CAMPAIGN_TEASER_MULTIPLIED_STEP_SIZE
                binding?.quantityEditor?.setValue(newCounter)
            }
            isDecreasing && oldValue == TWELVE -> {
                binding?.quantityEditor?.stepValue = CAMPAIGN_TEASER_NORMAL_STEP_SIZE
            }
            isDecreasing && oldValue == QuantityPickerConstant.CAMPAIGN_TEASER_MAXIMUM_UPCOMING_HOUR -> {
                val newCounter = oldValue - CAMPAIGN_TEASER_MULTIPLIED_STEP_SIZE
                binding?.quantityEditor?.setValue(newCounter)
            }
            else -> {
                binding?.quantityEditor?.stepValue = CAMPAIGN_TEASER_NORMAL_STEP_SIZE
            }
        }
    }

    private fun handleContentSwitcher(hexColorOptionSelected: Boolean) {
        binding?.recyclerView?.isVisible = !hexColorOptionSelected
        binding?.groupHexColorPicker?.isVisible = hexColorOptionSelected
        binding?.btnApply?.isVisible = hexColorOptionSelected
    }

    private fun handleSelectedColor(selectedGradient: Gradient) {
        val allGradients = adapter.snapshot()
        val updatedColorSelection = viewModel.markColorAsSelected(selectedGradient, allGradients)
        adapter.submit(updatedColorSelection)

        binding?.tauHexColor?.editText?.setText("")
        binding?.btnApply?.gone()
        binding?.tpgHexColorErrorMessage?.invisible()
        binding?.imgHexColorPreview?.setBackgroundResource(R.drawable.sfs_shape_rounded_color)

        binding?.cardView?.visible()
        viewModel.setSelectedColor(selectedGradient)
    }

    private fun handleHexColor(unvalidatedHexColor: String) {
        if (unvalidatedHexColor.length < HEX_COLOR_TEXT_FIELD_MAX_LENGTH) {
            binding?.btnApply?.disable()
            binding?.tauHexColor?.isInputError = true
            binding?.tpgHexColorErrorMessage?.visible()
            binding?.tpgHexColorErrorMessage?.text = getString(R.string.sfs_min_hex_color_length)
        } else {
            validateHexColor(unvalidatedHexColor)
        }
    }

    private fun validateHexColor(unvalidatedHexColor: String) {
        val hexColor = unvalidatedHexColor.toHexColor()

        if (hexColor.isValidHexColor()) {
            binding?.btnApply?.enable()
            binding?.tauHexColor?.isInputError = false
            binding?.tpgHexColorErrorMessage?.text = ""
            binding?.tpgHexColorErrorMessage?.invisible()
        } else {
            binding?.tauHexColor?.isInputError = true
            binding?.tpgHexColorErrorMessage?.visible()
            binding?.tpgHexColorErrorMessage?.text = getString(R.string.sfs_invalid_hex_color)
            binding?.btnApply?.disable()
        }
    }

    private fun displayStartDatePicker() {
        val selectedDate = viewModel.getSelectedStartDate()
        val minimumDate = dateManager.getCurrentDate().advanceHourBy(TWO_HOURS)
        val maximumEndDate = dateManager.getCurrentDate().advanceMonthBy(THREE_MONTH)

        val selectedVpsPackage = viewModel.getSelectedVpsPackage() ?: return
        val maximumCampaignEndDate = viewModel.findCampaignMaxEndDateByVpsRule(selectedVpsPackage, maximumEndDate)

        val bottomSheet = CampaignDatePickerBottomSheet.newInstance(
            TimePickerSelectionMode.START_TIME,
            selectedDate,
            minimumDate,
            maximumCampaignEndDate,
            viewModel.getSelectedVpsPackage() ?: return
        )

        bottomSheet.setOnDateTimePicked { newStartDate ->
            viewModel.setSelectedStartDate(newStartDate)
            binding?.tauStartDate?.editText?.setText(newStartDate.localFormatTo(DateConstant.DATE_TIME_MINUTE_LEVEL))
            adjustEndDate()
            val quantityPickerCurrentValue = binding?.quantityEditor?.editText?.text.toString().trim().toIntOrZero()
            adjustQuantityPicker(quantityPickerCurrentValue, newStartDate)
            viewModel.getCampaignQuota(newStartDate.extractMonth(), newStartDate.extractYear())
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun displayEndDatePicker() {
        val startDate = viewModel.getSelectedStartDate().advanceMinuteBy(THIRTY_MINUTE)
        val endDate = viewModel.normalizeEndDate(viewModel.getSelectedEndDate(), startDate)
        val minimumDate = viewModel.getSelectedStartDate().advanceMinuteBy(THIRTY_MINUTE)
        val maximumEndDate = viewModel.getSelectedStartDate().advanceDayBy(SIX_DAYS)

        val bottomSheet = CampaignDatePickerBottomSheet.newInstance(
            TimePickerSelectionMode.END_TIME,
            endDate,
            minimumDate,
            maximumEndDate,
            viewModel.getSelectedVpsPackage() ?: return
        )
        bottomSheet.setOnDateTimePicked { newEndDate ->
            viewModel.setSelectedEndDate(newEndDate)
            binding?.tauEndDate?.editText?.setText(newEndDate.localFormatTo(DateConstant.DATE_TIME_MINUTE_LEVEL))
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun adjustEndDate() {
        val startDate = viewModel.getSelectedStartDate()
        val endDate = viewModel.getSelectedEndDate()

        val differenceInDays = endDate.daysDifference(startDate)
        val modifiedEndDate = if (differenceInDays > SIX_DAYS) {
            viewModel.getSelectedStartDate().advanceDayBy(SIX_DAYS)
        } else {
            viewModel.getSelectedEndDate()
        }

        viewModel.setSelectedEndDate(modifiedEndDate)
        val formattedEndDate = modifiedEndDate.localFormatTo(DateConstant.DATE_TIME_MINUTE_LEVEL)
        binding?.tauEndDate?.editText?.setText(formattedEndDate)
    }

    private fun adjustQuantityPicker(currentValue : Int, newStartDate : Date) {
        val maxValue = viewModel.getTeaserQuantityEditorMaxValue(newStartDate, Date())
        binding?.quantityEditor?.maxValue = maxValue
        binding?.quantityEditor?.addButton?.isEnabled = currentValue <= QuantityPickerConstant.CAMPAIGN_TEASER_MAXIMUM_UPCOMING_HOUR
        binding?.quantityEditor?.subtractButton?.isEnabled = currentValue > QuantityPickerConstant.CAMPAIGN_TEASER_MINIMUM_UPCOMING_HOUR
    }

    private fun getCurrentSelection(): CampaignInformationViewModel.Selection {
        val startDate = viewModel.getSelectedStartDate()
        val endDate = viewModel.getSelectedEndDate()
        val showTeaser = binding?.switchTeaser?.isChecked.orFalse()
        val decreaseByHour = binding?.quantityEditor?.editText?.text.toString().trim().toIntOrZero()
        val teaserDate = startDate.decreaseHourBy(decreaseByHour)
        val firstColor = viewModel.getColor().first
        val secondColor = viewModel.getColor().second
        val paymentType = viewModel.getPaymentType()
        val remainingQuota = viewModel.getRemainingQuota()
        val vpsPackageId = viewModel.getSelectedVpsPackage()?.packageId.orZero()

        return CampaignInformationViewModel.Selection(
            binding?.tauCampaignName?.editText?.text.toString(),
            startDate,
            endDate,
            showTeaser,
            teaserDate,
            firstColor,
            secondColor,
            paymentType,
            remainingQuota,
            vpsPackageId
        )
    }

    private fun validateDraft() {
        val campaignName = binding?.tauCampaignName?.editText?.text.toString()
        val validationResult = viewModel.validateCampaignName(campaignName)

        if (validationResult !is CampaignInformationViewModel.CampaignNameValidationResult.Valid) {
            handleCampaignNameValidationResult(validationResult)
            return
        }

        binding?.btnDraft.showLoading()
        viewModel.saveDraft(pageMode, campaignId, getCurrentSelection())
    }

    private fun displayTextLengthCounter() {
        val length = binding?.tauCampaignName?.editText?.text.toString().length
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

    private fun showUpdateUpcomingDurationError(errorMessage: String) {
        binding?.cardView showError errorMessage
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

    private fun displayCampaignDetail(combinedData: CampaignWithVpsPackages) {
        val campaign = combinedData.campaign
        val quotaSource = viewModel.findDefaultQuotaSourceOnEditMode(campaign.packageInfo.packageId, combinedData.vpsPackages)

        binding?.run {
            tauCampaignName.editText.setText(campaign.campaignName)

            displaySelectedVpsPackage(quotaSource)

            val isEditDateEnabled = campaign.status == CampaignStatus.DRAFT
            tauStartDate.editText.setText(campaign.startDate.formatTo(DateConstant.DATE_TIME_MINUTE_LEVEL))
            tauEndDate.editText.setText(campaign.endDate.formatTo(DateConstant.DATE_TIME_MINUTE_LEVEL))
            tauStartDate.isEnabled = isEditDateEnabled
            tauEndDate.isEnabled = isEditDateEnabled

            val upcomingTimeInHours = viewModel.findUpcomingTimeDifferenceInHour(
                campaign.startDate.removeTimeZone(),
                campaign.upcomingDate.removeTimeZone()
            )
            val maxValue = viewModel.getTeaserQuantityEditorMaxValue( campaign.startDate.removeTimeZone(), Date())
            binding?.quantityEditor?.maxValue = maxValue
            binding?.quantityEditor?.setValue(upcomingTimeInHours)
            binding?.quantityEditor?.addButton?.isEnabled = upcomingTimeInHours != QuantityPickerConstant.CAMPAIGN_TEASER_MAXIMUM_UPCOMING_HOUR
            binding?.quantityEditor?.subtractButton?.isEnabled = upcomingTimeInHours != ONE

            switchTeaser.isChecked = campaign.useUpcomingWidget
            handleSwitchTeaser(campaign.useUpcomingWidget)
            renderSelectedColor(campaign)
        }

        viewModel.setSelectedStartDate(campaign.startDate.removeTimeZone())
        viewModel.setSelectedEndDate(campaign.endDate.removeTimeZone())
        viewModel.setShowTeaser(campaign.useUpcomingWidget)
        viewModel.setSelectedColor(campaign.gradientColor)
        viewModel.setPaymentType(campaign.paymentType)
        viewModel.storeVpsPackage(combinedData.vpsPackages)
        viewModel.setSelectedVpsPackage(quotaSource ?: return)

        viewModel.storeAsDefaultSelection(
            CampaignInformationViewModel.Selection(
                campaign.campaignName,
                campaign.startDate.removeTimeZone(),
                campaign.endDate.removeTimeZone(),
                campaign.useUpcomingWidget,
                campaign.upcomingDate.removeTimeZone(),
                campaign.gradientColor.first,
                campaign.gradientColor.second,
                campaign.paymentType,
                Int.ZERO,
                campaign.packageInfo.packageId
            )
        )
    }

    private fun renderSelectedColor(campaign: CampaignUiModel) {
        val isUsingHexColor = viewModel.isUsingHexColor(
            campaign.gradientColor.first,
            campaign.gradientColor.second
        )

        binding?.recyclerView?.isVisible = !isUsingHexColor
        binding?.groupHexColorPicker?.isVisible = isUsingHexColor


        if (isUsingHexColor) {
            binding?.btnApply?.enable()
            binding?.contentSwitcher?.isChecked = true
            binding?.tauHexColor?.editText?.setText(campaign.gradientColor.first.removeHexColorPrefix())
            binding?.imgHexColorPreview?.setBackgroundFromGradient(campaign.gradientColor)

            val colors = viewModel.deselectAllColor(adapter.snapshot())
            adapter.submit(colors)
        } else {
            binding?.btnApply?.disable()
            val colors = viewModel.markColorAsSelected(campaign.gradientColor, adapter.snapshot())
            adapter.submit(colors)
        }
    }

    private fun handleBackConfirmation() {
        val remainingQuota = viewModel.getRemainingQuota()
        val selectedVpsPackageId = viewModel.getSelectedVpsPackage()?.packageId.orZero()
        val updatedDefaultSelection = viewModel.getDefaultSelection()?.copy(
            remainingQuota = remainingQuota,
            vpsPackageId = selectedVpsPackageId
        ) ?: return

        val isDataChanged = viewModel.isDataChanged(updatedDefaultSelection, getCurrentSelection())
        if (!isDataChanged) {
            activity?.finish()
            return
        }

        if (pageMode == PageMode.CREATE) {
            showCancelCreateCampaignConfirmationDialog()
        } else {
            showCancelEditCampaignConfirmationDialog()
        }
    }

    private fun showCancelCreateCampaignConfirmationDialog() {
        val dialog = CancelCreateCampaignConfirmationDialog(activity ?: return)
        dialog.setOnPrimaryActionClick { activity?.finish() }
        dialog.setOnThirdActionClick { validateDraft() }
        dialog.show()
    }

    private fun showCancelEditCampaignConfirmationDialog() {
        val dialog = CancelEditCampaignConfirmationDialog(activity ?: return)
        dialog.setOnPrimaryActionClick { activity?.finish() }
        dialog.setOnSecondaryActionClick { validateDraft() }
        dialog.show()
    }


    private fun showCoachMark() {
        val coachMark = CoachMark2(activity ?: return)
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
        val thirdAnchorView = binding?.tauVpsPackageName?.icon1 ?: return arrayListOf()

        return arrayListOf(
            CoachMark2Item(
                firstAnchorView,
                getString(R.string.sfs_coachmark_first),
                "",
                CoachMark2.POSITION_TOP
            ),
            CoachMark2Item(
                secondAnchorView,
                getString(R.string.sfs_coachmark_second),
                "",
                CoachMark2.POSITION_TOP
            ),
            CoachMark2Item(
                thirdAnchorView,
                getString(R.string.sfs_coachmark_third),
                "",
                CoachMark2.POSITION_BOTTOM
            )

        )
    }

    private fun handleFirstStepOfCampaignCreationSuccess(result: CampaignCreationResult) {
        if (result.isSuccess) {
            ManageProductActivity.start(activity ?: return, result.campaignId, pageMode)
        } else {
            handleCreateCampaignError(result)
        }
    }

    private fun handleSaveCampaignDraftSuccess(result: CampaignCreationResult) {
        //Add some spare time caused by Backend write operation delay
        doOnDelayFinished(REDIRECT_TO_PREVIOUS_PAGE_DELAY) {
            binding?.btnDraft?.stopLoading()

            if (result.isSuccess) {
                activity?.apply {
                    // handle campaign save draft is not opened from campaign list case
                    if (pageMode == PageMode.UPDATE) {
                        CampaignListActivity.start(
                            this,
                            isSaveDraft = true,
                            previousPageMode = pageMode
                        )
                    } else {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
            } else {
                handleCreateCampaignError(result)
            }
        }

    }

    private fun handleCreateCampaignError(result: CampaignCreationResult) {
        if (result.errorTitle.isNotEmpty() && result.errorDescription.isNotEmpty()) {
            showErrorTicker(result.errorTitle, result.errorDescription)
        } else {
            showUpdateUpcomingDurationError(result.errorMessage)
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
            binding?.tauStartDate?.isInputError = true
            binding?.tauEndDate?.isInputError = true
            binding?.tauEndDate?.applySecondaryColor()
        } else {
            hideErrorTicker()
            binding?.tauStartDate?.isInputError = false
            binding?.tauEndDate?.isInputError = false
        }
    }

    private fun TextFieldUnify2?.applySecondaryColor() {
        val secondaryColorStateList = ColorStateList(binding?.tauEndDate?.disabledStateList, binding?.tauEndDate?.secondaryColorList)
        this?.textInputLayout?.setHelperTextColor(secondaryColorStateList)
    }

    private fun displayQuotaSourceBottomSheet() {
        val selectedVpsPackageId = viewModel.getSelectedVpsPackage()?.packageId.orZero()
        val vpsPackages = ArrayList(viewModel.getStoredVpsPackages())
        val bottomSheet = VpsPackageBottomSheet.newInstance(selectedVpsPackageId, vpsPackages)
        bottomSheet.setOnVpsPackageClicked { selectedVpsPackage ->
            viewModel.setSelectedVpsPackage(selectedVpsPackage)
            displaySelectedVpsPackage(selectedVpsPackage)
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun displayVpsQuotaEmptyError(originalQuota: Int) {
        val errorMessage =  String.format(
            getString(R.string.sfs_placeholder_remaining_vps_quota),
            originalQuota,
            originalQuota
        )
        binding?.tauVpsPackageName?.setMessage(errorMessage)
        binding?.tauVpsPackageName?.isInputError = true
    }
}