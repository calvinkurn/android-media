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
import com.tokopedia.shop.flashsale.common.extension.*
import com.tokopedia.shop.flashsale.common.util.DateManager
import com.tokopedia.shop.flashsale.common.util.doOnTextChanged
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.Gradient
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode
import com.tokopedia.shop.flashsale.presentation.creation.information.adapter.GradientColorAdapter
import com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet.CampaignDatePickerBottomSheet
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject


class CampaignInformationFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_PAGE_MODE = "page_mode"
        private const val FIRST_STEP = 1
        private const val SPAN_COUNT = 6
        private const val HEX_COLOR_TEXT_FIELD_MAX_LENGTH = 6

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
        observeDataChange()
    }

    private fun observeDataChange() {
        viewModel.areInputValid.observe(viewLifecycleOwner) { validationResult ->
            handleValidationResult(validationResult)
        }
    }

    private fun setupView() {
        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        setupTextFields()
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
                validateInput()
            }

            tauHexColor.setMaxLength(HEX_COLOR_TEXT_FIELD_MAX_LENGTH)
            tauHexColor.textInputLayout.editText?.doOnTextChanged { text ->
                if (text.length == HEX_COLOR_TEXT_FIELD_MAX_LENGTH) {
                    binding?.btnApply?.visible()
                } else {
                    binding?.btnApply?.invisible()
                }
            }

            tauStartDate.editText.inputType = InputType.TYPE_NULL
            tauEndDate.editText.inputType = InputType.TYPE_NULL
            tauStartDate.editText.setOnClickListener { displayStartDatePicker() }
            tauEndDate.editText.setOnClickListener { displayEndDatePicker() }
        }
    }

    private fun setupClickListeners() {
        binding?.run {
            btnDraft.setOnClickListener { validateInput() }
            btnCreateCampaign.setOnClickListener { validateInput() }
            btnApply.setOnClickListener { handleApplyButtonClick() }
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

    private fun handleValidationResult(validationResult: CampaignInformationViewModel.ValidationResult) {
        when (validationResult) {
            CampaignInformationViewModel.ValidationResult.CampaignNameIsEmpty -> {
                showError(getString(R.string.sfs_error_message_field_not_filled))
            }
            CampaignInformationViewModel.ValidationResult.CampaignNameBelowMinCharacter -> {
                showError(getString(R.string.sfs_error_message_min_char))
            }
            CampaignInformationViewModel.ValidationResult.CampaignNameHasForbiddenWords -> {
                showError(getString(R.string.sfs_error_message_forbidden_words))
            }
            CampaignInformationViewModel.ValidationResult.ExceedMaxOverlappedCampaign -> {
                hideError()

            }
            CampaignInformationViewModel.ValidationResult.LapsedStartDate -> {
                hideError()

            }
            CampaignInformationViewModel.ValidationResult.LapsedTeaserStartDate -> {
                hideError()

            }
            CampaignInformationViewModel.ValidationResult.Valid -> {
                hideError()

                binding?.btnCreateCampaign?.enable()
                val selection = viewModel.getSelection() ?: return
                viewModel.createCampaign(selection)
            }
        }
    }

    private fun handleApplyButtonClick() {
        val color = binding?.tauHexColor?.editText?.text.toString().trim().toHexColor()
        binding?.imgHexColorPreview?.setBackgroundFromGradient(
            Gradient(
                color,
                color,
                isSelected = true
            )
        )
    }

    private fun handleSwitchTeaser(showTeaserSettings: Boolean) {
        binding?.groupTeaserSettings?.isVisible = showTeaserSettings
    }

    private fun handleQuantityEditor(newValue: Int, oldValue: Int) {
        val isIncreasing = newValue > oldValue
        if (newValue % 12 == Constant.ZERO) {
            binding?.quantityEditor?.stepValue = 12
            //binding?.quantityEditor?.editText?.setText(24.toString())
        } else {
            binding?.quantityEditor?.stepValue = 1
            //binding?.quantityEditor?.editText?.setText(newValue)
        }
    }

    private fun handleContentSwitcher(hexColorOptionSelected: Boolean) {
        binding?.recyclerView?.isVisible = !hexColorOptionSelected
        binding?.groupHexColorPicker?.isVisible = hexColorOptionSelected

        if (hexColorOptionSelected) {
            val hexColor = binding?.tauHexColor?.editText?.text.toString().trim().toHexColor()
            viewModel.setColor(Gradient(hexColor, hexColor, isSelected = true))
        }
    }

    private fun handleSelectedColor(selectedGradient: Gradient) {
        val allGradients = adapter.getItems()
        val updatedColorSelection = viewModel.markAsSelected(selectedGradient, allGradients)
        adapter.submit(updatedColorSelection)
        viewModel.setColor(selectedGradient)
    }

    private fun displayStartDatePicker() {
        val previouslySelectedStartDate = viewModel.getSelectedStartDate() ?: Date()
        val minimumDate = Date()
        val bottomSheet =
            CampaignDatePickerBottomSheet.newInstance(previouslySelectedStartDate, minimumDate)
        bottomSheet.setOnDatePicked { newStartDate ->
            viewModel.setSelectedStartDate(newStartDate)
            binding?.tauStartDate?.editText?.setText(newStartDate.formatTo(DateConstant.DATE_TIME_MINUTE_LEVEL))
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun displayEndDatePicker() {
        val minimumDate = viewModel.getSelectedStartDate() ?: Date()
        val previouslySelectedEndDate = viewModel.getSelectedEndDate() ?: minimumDate

        val bottomSheet =
            CampaignDatePickerBottomSheet.newInstance(previouslySelectedEndDate, minimumDate)
        bottomSheet.setOnDatePicked { newEndDate ->
            viewModel.setSelectedEndDate(newEndDate)
            binding?.tauEndDate?.editText?.setText(newEndDate.formatTo(DateConstant.DATE_TIME_MINUTE_LEVEL))
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
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
        val teaserDate = startDate?.decreaseHourBy(decreaseByHour)
        val firstColor = viewModel.getColor()?.first.orEmpty()
        val secondColor = viewModel.getColor()?.second.orEmpty()

        val selection = CampaignInformationViewModel.Selection(
            binding?.tauCampaignName?.editText?.text.toString().trim(),
            startDate,
            endDate,
            showTeaser,
            teaserDate,
            firstColor,
            secondColor
        )

        viewModel.setSelection(selection)
        viewModel.validateInput(selection)
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

    private fun hideLapsedScheduleTicker() {
        binding?.tickerLapsedSchedule?.gone()
    }

    private fun showLapsedTeaserTicker() {
        binding?.tickerLapsedTeaser?.visible()
    }

    private fun hideLapsedTeaserTicker() {
        binding?.tickerLapsedTeaser?.gone()
    }

    private fun showMaxCampaignOverlappedTicker() {
        binding?.tickerLapsedSchedule?.visible()
        binding?.tickerLapsedSchedule?.tickerTitle = getString(R.string.sfs_error_message_overlapped_schedule_title)
        binding?.tickerLapsedSchedule?.setTextDescription(getString(R.string.sfs_error_message_overlapped_schedule_description))
    }

    private fun hideMaxCampaignOverlappedTicker() {
        binding?.tickerLapsedSchedule?.gone()
    }
}