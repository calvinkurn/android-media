package com.tokopedia.shop.flashsale.presentation.creation.information

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
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
import com.tokopedia.shop.flashsale.common.util.DateManager
import com.tokopedia.shop.flashsale.common.util.doOnTextChanged
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.Gradient
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode
import com.tokopedia.shop.flashsale.presentation.creation.information.adapter.GradientColorAdapter
import com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet.CampaignDatePickerBottomSheet
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject


class CampaignInformationFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_PAGE_MODE = "page_mode"
        private const val FIRST_STEP = 1
        private const val SPAN_COUNT = 6
        private const val HEX_COLOR_TEXT_FIELD_MAX_LENGTH = 6
        private const val ONE_HOUR = 1
        private const val THRESHOLD = 12
        private const val THIRTY_MINUTE = 30


        @JvmStatic
        fun newInstance(pageMode: PageMode): CampaignInformationFragment {
            val fragment = CampaignInformationFragment()
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_KEY_PAGE_MODE, pageMode)
            fragment.arguments = bundle
            return fragment
        }

    }

    private var binding by autoClearedNullable<SsfsFragmentCampaignInformationBinding>()
    private val pageMode by lazy { arguments?.getParcelable(BUNDLE_KEY_PAGE_MODE) as? PageMode }
    private val adapter = GradientColorAdapter()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var dateManager: DateManager

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
        observeValidationResult()
        observeCampaignName()
        observeCampaignCreation()
    }

    private fun observeValidationResult() {
        viewModel.areInputValid.observe(viewLifecycleOwner) { validationResult ->
            handleValidationResult(validationResult)
        }
    }

    private fun observeCampaignName() {
        viewModel.campaignName.observe(viewLifecycleOwner) { validationResult ->
            handleCampaignNameValidationResult(validationResult)
        }
    }

    private fun observeCampaignCreation() {
        viewModel.campaignCreation.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    binding?.btnCreateCampaign?.stopLoading()
                    if (result.data.isSuccess) {

                    } else {
                        showErrorTicker(result.data.errorTitle, result.data.errorDescription)
                    }
                }
                is Fail -> {
                    binding?.btnCreateCampaign?.stopLoading()
                    binding?.cardView showError result.throwable
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
    }

    private fun setupTextFields() {
        binding?.run {
            tauCampaignName.textInputLayout.editText?.doOnTextChanged { text ->
                viewModel.onCampaignNameChange(text)
            }

            tauHexColor.setMaxLength(HEX_COLOR_TEXT_FIELD_MAX_LENGTH)
            tauHexColor.textInputLayout.editText?.doOnTextChanged { text ->
                if (text.length == HEX_COLOR_TEXT_FIELD_MAX_LENGTH) {
                    binding?.btnApply?.visible()
                } else {
                    binding?.btnApply?.invisible()
                }
            }


        }
    }

    private fun setupClickListeners() {
        binding?.run {
            btnDraft.setOnClickListener { validateInput() }
            btnCreateCampaign.setOnClickListener { validateInput() }
            btnApply.setOnClickListener { handleApplyButtonClick() }

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

    private fun handleCampaignNameValidationResult(validationResult: CampaignInformationViewModel.CampaignNameValidationResult) {
        when (validationResult) {
            CampaignInformationViewModel.CampaignNameValidationResult.CampaignNameIsEmpty -> {
                showError(getString(R.string.sfs_error_message_field_not_filled))
                binding?.btnCreateCampaign?.disable()
            }
            CampaignInformationViewModel.CampaignNameValidationResult.CampaignNameBelowMinCharacter -> {
                showError(getString(R.string.sfs_error_message_min_char))
                binding?.btnCreateCampaign?.disable()
            }
            CampaignInformationViewModel.CampaignNameValidationResult.CampaignNameHasForbiddenWords -> {
                showError(getString(R.string.sfs_error_message_forbidden_words))
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
            CampaignInformationViewModel.ValidationResult.LapsedStartDate -> {
                showLapsedScheduleTicker()
                hideLapsedTeaserTicker()
            }
            CampaignInformationViewModel.ValidationResult.LapsedTeaserStartDate -> {
                showLapsedTeaserTicker()
                hideLapsedScheduleTicker()
            }
            CampaignInformationViewModel.ValidationResult.Valid -> {
                hideLapsedTeaserTicker()
                hideLapsedScheduleTicker()

                binding?.btnCreateCampaign?.enable()
                binding?.btnCreateCampaign.showLoading()
                val selection = viewModel.getSelection() ?: return
                viewModel.createCampaign(selection)
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
        val isHexColorTextFieldFilled = binding?.tauHexColor?.editText?.text.toString().trim().length == HEX_COLOR_TEXT_FIELD_MAX_LENGTH
        binding?.recyclerView?.isVisible = !hexColorOptionSelected
        binding?.groupHexColorPicker?.isVisible = hexColorOptionSelected
        binding?.btnApply?.isVisible = hexColorOptionSelected && isHexColorTextFieldFilled

        if (hexColorOptionSelected) {
            val hexColor = binding?.tauHexColor?.editText?.text.toString().trim().toHexColor()
            viewModel.setSelectedColor(Gradient(hexColor, hexColor, isSelected = true))
        }
    }

    private fun handleSelectedColor(selectedGradient: Gradient) {
        viewModel.setSelectedColor(selectedGradient)
        val allGradients = adapter.snapshot()
        val updatedColorSelection = viewModel.markColorAsSelected(selectedGradient, allGradients)
        adapter.submit(updatedColorSelection)
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

    private fun validateInput() {
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

        val selection = CampaignInformationViewModel.Selection(
            binding?.tauCampaignName?.editText?.text.toString().trim(),
            startDate,
            endDate,
            showTeaser,
            teaserDate,
            firstColor,
            secondColor
        )

        viewModel.onNextButtonPressed(selection, Date())
    }


    private fun showError(errorMessage : String) {
        binding?.tauCampaignName?.isInputError = true
        binding?.tauCampaignName?.setMessage(errorMessage)
    }

    private fun hideError() {
        binding?.tauCampaignName?.isInputError = false
        binding?.tauCampaignName?.setMessage(Constant.EMPTY_STRING)
    }

    private fun showLapsedScheduleTicker() {
        binding?.tickerLapsedSchedule?.visible()
        binding?.tickerLapsedSchedule?.tickerTitle = getString(R.string.sfs_error_message_schedule_lapsed_title)
        binding?.tickerLapsedSchedule?.setTextDescription(getString(R.string.sfs_error_message_schedule_lapsed_description))
    }

    private fun showErrorTicker(title : String, description : String) {
        binding?.tickerLapsedSchedule?.visible()
        binding?.tickerLapsedSchedule?.tickerTitle = title
        binding?.tickerLapsedSchedule?.setTextDescription(description)
    }

    private fun hideLapsedScheduleTicker() {
        binding?.tickerLapsedSchedule?.gone()
    }

    private fun showLapsedTeaserTicker() {
        binding?.tickerLapsedTeaser?.visible()
    }

    private fun hideLapsedTeaserTicker() {
        binding?.tickerLapsedTeaser?.gone()
    }

}