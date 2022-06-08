package com.tokopedia.shop.flashsale.presentation.creation.information

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentCampaignInformationBinding
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.constant.DateConstant
import com.tokopedia.shop.flashsale.common.extension.decreaseHourBy
import com.tokopedia.shop.flashsale.common.extension.formatTo
import com.tokopedia.shop.flashsale.common.extension.setBackgroundFromGradient
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

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var dateManager: DateManager

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignInformationViewModel::class.java) }

    @SuppressLint("UnsupportedDarkModeColor")
    private val colors = listOf(
        Gradient("#26A116", "#60BB55"),
        Gradient("#019751", "#00AA5B"),
        Gradient("#00615B", "#04837E"),
        Gradient("#2B4A62", "#3E6786"),
        Gradient("#2059A1", "#2F89FC"),
        Gradient("#1C829E", "#28B9E1"),

        Gradient("#EF4C60", "#E96E7D"),
        Gradient("#AE1720", "#D74049"),
        Gradient("#850623", "#B31D40"),
        Gradient("#B8266C", "#CD5D99"),
        Gradient("#4F2DC0", "#7F66D1"),
        Gradient("#58197E", "#854C9E"),

        Gradient("#685447", "#836857"),
        Gradient("#DD8F00", "#F8A400"),
    )

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
            when (validationResult) {
                CampaignInformationViewModel.ValidationResult.CampaignNameBelowMinCharacter -> TODO()
                CampaignInformationViewModel.ValidationResult.CampaignNameHasForbiddenWords -> TODO()
                CampaignInformationViewModel.ValidationResult.CampaignNameIsEmpty -> TODO()
                CampaignInformationViewModel.ValidationResult.ExceedMaxOverlappedCampaign -> TODO()
                CampaignInformationViewModel.ValidationResult.LapsedStartDate -> TODO()
                CampaignInformationViewModel.ValidationResult.LapsedTeaserStartDate -> TODO()
                CampaignInformationViewModel.ValidationResult.Valid -> {
                    val selection = viewModel.getSelection() ?: return@observe
                    viewModel.createCampaign(selection)
                }
            }
        }
    }

    private fun setupView() {
        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        setupTextFields()
    }

    private fun setupRecyclerView() {
        val recyclerViewAdapter = GradientColorAdapter()
        binding?.recyclerView?.apply {
            layoutManager = GridLayoutManager(requireActivity(), SPAN_COUNT)
            adapter = recyclerViewAdapter
        }
        recyclerViewAdapter.submit(colors)
        recyclerViewAdapter.setOnGradientClicked { gradient ->
            viewModel.setColor(gradient)
        }
    }

    private fun setupToolbar() {
        binding?.header?.headerSubTitle =
            String.format(getString(R.string.sfs_placeholder_step_counter), FIRST_STEP)
    }

    private fun setupTextFields() {
        binding?.run {
            tauCampaignName.textInputLayout.editText?.doOnTextChanged { text ->

            }
            tauHexColor.textInputLayout.editText?.doOnTextChanged { text ->
                binding?.imgHexColorPreview?.setBackgroundFromGradient(Gradient(text, text))
            }

        }
    }

    private fun setupClickListeners() {
        binding?.run {
            btnDraft.setOnClickListener { validateInput() }
            btnCreateCampaign.setOnClickListener { validateInput() }
            tauStartDate.textInputLayout.setOnClickListener { displayStartDatePicker() }
            tauEndDate.textInputLayout.setOnClickListener { displayEndDatePicker() }
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

    private fun handleSwitchTeaser(showTeaserSettings : Boolean) {
        binding?.groupTeaserSettings?.isVisible = showTeaserSettings
    }

    private fun handleQuantityEditor(newValue : Int, oldValue : Int) {
        val isIncreasing = newValue > oldValue
        if (newValue % 12 == Constant.ZERO) {
            binding?.quantityEditor?.stepValue = 12
            //binding?.quantityEditor?.editText?.setText(24.toString())
        } else {
            binding?.quantityEditor?.stepValue = 1
            //binding?.quantityEditor?.editText?.setText(newValue)
        }
    }

    private fun handleContentSwitcher(hexColorOptionSelected : Boolean) {
        binding?.recyclerView?.isVisible = !hexColorOptionSelected
        binding?.groupHexColorPicker?.isVisible = hexColorOptionSelected

        if (hexColorOptionSelected) {
            val color = binding?.tauHexColor?.editText?.text.toString().trim()
            val hexColor = "#{${color}}"
            viewModel.setColor(Gradient(hexColor, hexColor))
        }
    }

    private fun displayStartDatePicker() {
        val previouslySelectedStartDate = viewModel.getSelectedStartDate() ?: Date()
        val minimumDate = Date()
        val bottomSheet = CampaignDatePickerBottomSheet.newInstance(previouslySelectedStartDate, minimumDate)
        bottomSheet.setOnDatePicked { newStartDate ->
            viewModel.setSelectedStartDate(newStartDate)
            binding?.tauStartDate?.editText?.setText(newStartDate.formatTo(DateConstant.DATE_TIME_MINUTE_LEVEL))
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun displayEndDatePicker() {
        val minimumDate = viewModel.getSelectedStartDate() ?: Date()
        val previouslySelectedEndDate = viewModel.getSelectedEndDate() ?: minimumDate

        val bottomSheet = CampaignDatePickerBottomSheet.newInstance(previouslySelectedEndDate, minimumDate)
        bottomSheet.setOnDatePicked { newEndDate ->
            viewModel.setSelectedEndDate(newEndDate)
            binding?.tauEndDate?.editText?.setText(newEndDate.formatTo(DateConstant.DATE_TIME_MINUTE_LEVEL))
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }


    private fun validateInput() {
        val startDate = viewModel.getSelectedStartDate() ?: return
        val endDate = viewModel.getSelectedEndDate() ?: return
        val decreaseByHour = binding?.quantityEditor?.editText?.text.toString().trim().toIntOrZero()
        val teaserDate = startDate.decreaseHourBy(decreaseByHour)
        val firstColor = viewModel.getColor()?.first ?: return
        val secondColor = viewModel.getColor()?.second ?: return

        val selection = CampaignInformationViewModel.Selection(
            binding?.tauCampaignName?.editText?.text.toString().trim(),
            startDate,
            endDate,
            binding?.switchTeaser?.isChecked.orFalse(),
            teaserDate,
            firstColor,
            secondColor
        )

        viewModel.setSelection(selection)
        viewModel.validateInput(selection)
    }


}