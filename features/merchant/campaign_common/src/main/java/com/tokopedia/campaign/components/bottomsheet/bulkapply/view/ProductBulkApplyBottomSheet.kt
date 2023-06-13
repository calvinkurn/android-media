package com.tokopedia.campaign.components.bottomsheet.bulkapply.view

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.R
import com.tokopedia.campaign.components.bottomsheet.bulkapply.data.uimodel.ProductBulkApplyUiModel
import com.tokopedia.campaign.components.bottomsheet.bulkapply.data.uimodel.ProductBulkApplyResult
import com.tokopedia.campaign.components.bottomsheet.bulkapply.data.uimodel.DiscountType
import com.tokopedia.campaign.databinding.CampaignBottomsheetManageProductBulkApplyBinding
import com.tokopedia.campaign.di.component.DaggerCampaignCommonComponent
import com.tokopedia.campaign.utils.constant.LocaleConstant
import com.tokopedia.campaign.utils.textwatcher.NumberThousandSeparatorTextWatcher
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.text.DecimalFormat
import java.text.NumberFormat
import javax.inject.Inject


class ProductBulkApplyBottomSheet : BottomSheetUnify() {

    companion object {
        private const val ARG_BOTOMSHEET_CONFIG = "ARG_BOTOMSHEET_CONFIG"
        private const val NUMBER_PATTERN = "#,###,###"
        private const val DISCOUNT_PERCENTAGE_MAX_DIGIT = 2

        /**
         * @param bottomSheetConfigModel
         *  - Config model for setup the bottomsheet
         */
        @JvmStatic
        fun newInstance(
            bottomSheetConfigModel: ProductBulkApplyUiModel
        ): ProductBulkApplyBottomSheet {
            val args = Bundle()
            args.putParcelable(ARG_BOTOMSHEET_CONFIG, bottomSheetConfigModel)
            val bottomSheet = ProductBulkApplyBottomSheet().apply {
                arguments = args
            }
            return bottomSheet
        }

    }

    private val bottomSheetConfigModel by lazy {
        arguments?.getParcelable(ARG_BOTOMSHEET_CONFIG) as? ProductBulkApplyUiModel
    }
    private val title by lazy { bottomSheetConfigModel?.bottomSheetTitle.orEmpty() }
    private val isShowTextFieldProductDiscountBottomMessage by lazy {
        bottomSheetConfigModel?.isShowTextFieldProductDiscountBottomMessage.orFalse()
    }
    private val minimumDiscountPrice by lazy {
        bottomSheetConfigModel?.minimumDiscountPrice.orZero()
    }
    private val maximumDiscountPrice by lazy {
        bottomSheetConfigModel?.maximumDiscountPrice.orZero()
    }
    private val minimumDiscountPercentage by lazy {
        bottomSheetConfigModel?.minimumDiscountPercentage.orZero()
    }
    private val maximumDiscountPercentage by lazy {
        bottomSheetConfigModel?.maximumDiscountPercentage.orZero()
    }
    private val minimumStock by lazy {
        bottomSheetConfigModel?.minimumStock.orZero()
    }
    private val maximumStock by lazy {
        bottomSheetConfigModel?.maximumStock.orZero()
    }

    private var discountType: DiscountType = DiscountType.RUPIAH

    private var binding by autoClearedNullable<CampaignBottomsheetManageProductBulkApplyBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy {
        viewModelProvider.get(
            ProductBulkApplyBottomSheetViewModel::class.java
        )
    }

    private var onApplyClickListener: (ProductBulkApplyResult) -> Unit =
        {}

    init {
        clearContentPadding = true
        isSkipCollapseState = true
        isKeyboardOverlap = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }

    private fun setupDependencyInjection() {
        DaggerCampaignCommonComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding =
            CampaignBottomsheetManageProductBulkApplyBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle(title)
        setupProductDiscountTextFieldBottomMessage()
        setupProductStockSection()
    }

    private fun setupProductStockSection() {
        binding?.textStock?.text = bottomSheetConfigModel?.textStock.orEmpty()
        binding?.textStockAdditionalInfo?.text =
            bottomSheetConfigModel?.textStockAdditionalInfo.orEmpty()
        binding?.textStockDescription?.text = bottomSheetConfigModel?.textStockDescription.orEmpty()
    }

    private fun setupProductDiscountTextFieldBottomMessage() {
        if (isShowTextFieldProductDiscountBottomMessage) {
            val bottomMessage = when (discountType) {
                DiscountType.RUPIAH -> {
                    "${minimumDiscountPrice.getCurrencyFormatted()} - ${maximumDiscountPrice.getCurrencyFormatted()}"
                }
                DiscountType.PERCENTAGE -> {
                    "${minimumDiscountPercentage}% - ${maximumDiscountPercentage}%"
                }
            }
            binding?.textFieldDiscountAmount?.isInputError = false
            binding?.textFieldDiscountAmount?.setMessage(bottomMessage)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeInputProductDiscountValidation()
        observeInputProductStockValidation()
        observeDiscountTypeChange()
        observeEnableApplyButton()
    }

    private fun observeEnableApplyButton() {
        viewModel.isEnableApplyButton.observe(viewLifecycleOwner) {
            binding?.btnApply?.isEnabled = it
        }
    }

    private fun observeInputProductStockValidation() {
        viewModel.inputProductStockValidation.observe(viewLifecycleOwner) { validationState ->
            when (validationState) {
                ProductBulkApplyBottomSheetViewModel.ValidationState.InvalidStock -> {
                    binding?.textQuantityEditorError?.apply {
                        show()
                        text = getString(
                            R.string.campaign_common_range_format,
                            minimumStock.toString(),
                            maximumStock.toString()
                        )
                    }
                }
                ProductBulkApplyBottomSheetViewModel.ValidationState.Valid -> {
                    binding?.textQuantityEditorError?.apply {
                        hide()
                        text = ""
                    }
                }
                else -> {}
            }
            checkApplyButton()
        }
    }

    private fun checkApplyButton() {
        viewModel.checkApplyButton()
    }

    private fun observeInputProductDiscountValidation() {
        viewModel.inputProductDiscountValidation.observe(viewLifecycleOwner) { validationState ->
            when (validationState) {
                ProductBulkApplyBottomSheetViewModel.ValidationState.InvalidDiscountMinimumPrice -> {
                    setTextInputLayoutErrorMessage(
                        binding?.textFieldDiscountAmount ?: return@observe,
                        getString(
                            R.string.campaign_common_min_price_error,
                            minimumDiscountPrice.getCurrencyFormatted()
                        )
                    )
                }
                ProductBulkApplyBottomSheetViewModel.ValidationState.InvalidDiscountMaximumPrice -> {
                    setTextInputLayoutErrorMessage(
                        binding?.textFieldDiscountAmount ?: return@observe,
                        getString(
                            R.string.campaign_common_max_price_error,
                            maximumDiscountPrice.getCurrencyFormatted()
                        )
                    )
                }
                ProductBulkApplyBottomSheetViewModel.ValidationState.InvalidDiscountPercentage -> {
                    setTextInputLayoutErrorMessage(
                        binding?.textFieldDiscountAmount ?: return@observe,
                        getString(
                            R.string.campaign_common_range_format,
                            "$minimumDiscountPercentage%",
                            "$maximumDiscountPercentage%"
                        )
                    )
                }
                ProductBulkApplyBottomSheetViewModel.ValidationState.Valid -> {
                    setupProductDiscountTextFieldBottomMessage()
                }
                else -> {}
            }
            checkApplyButton()
        }
    }

    private fun observeDiscountTypeChange() {
        viewModel.discountType.observe(viewLifecycleOwner) { discountType ->
            this.discountType = discountType
            setupProductDiscountTextFieldBottomMessage()
            if (discountType == DiscountType.RUPIAH) {
                binding?.textFieldDiscountAmount?.setLabel(getString(R.string.campaign_common_discount_amount))
                binding?.textFieldDiscountAmount?.textInputLayout?.editText?.text = null
                binding?.textFieldDiscountAmount?.appendText("")
                binding?.textFieldDiscountAmount?.prependText(getString(R.string.campaign_common_rupiah))
                binding?.textFieldDiscountAmount?.editText?.filters = arrayOf()
            } else {
                binding?.textFieldDiscountAmount?.setLabel(getString(R.string.campaign_common_discount_percentage))
                binding?.textFieldDiscountAmount?.textInputLayout?.editText?.text = null
                binding?.textFieldDiscountAmount?.appendText(getString(R.string.campaign_common_percent))
                binding?.textFieldDiscountAmount?.prependText("")
                binding?.textFieldDiscountAmount?.editText?.filters =
                    arrayOf(InputFilter.LengthFilter(DISCOUNT_PERCENTAGE_MAX_DIGIT))
            }
            viewModel.onDiscountAmountChanged(Int.ZERO.toLong())
        }
    }

    private fun setupView() {
        setupDiscountAmountListener()
        binding?.run {
            textFieldDiscountAmount.textInputLayout.errorIconDrawable = null
            setupQuantityEditor()
            contentSwitcherDiscountType.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onDiscountTypeChanged(if (isChecked) DiscountType.PERCENTAGE else DiscountType.RUPIAH)
            }
            btnApply.setOnClickListener {
                val currentSelection = viewModel.getCurrentSelection()
                onApplyClickListener(currentSelection)
                dismiss()
            }
        }
    }

    private fun setupQuantityEditor() {
        binding?.run {
            quantityEditor.minValue = minimumStock
            quantityEditor.maxValue = maximumStock
            quantityEditor.setValueChangedListener { newValue, _, _ ->
                viewModel.onMaxPurchaseQuantityChanged(newValue)
                bottomSheetConfigModel?.let {
                    viewModel.validateInputProductStock(it)
                }
            }
            quantityEditor.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.onMaxPurchaseQuantityChanged(text?.toString().toIntOrZero())
                    bottomSheetConfigModel?.let {
                        viewModel.validateInputProductStock(it)
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })
        }
    }


    private fun setupDiscountAmountListener() {
        val numberFormatter = NumberFormat.getInstance(LocaleConstant.INDONESIA) as DecimalFormat
        numberFormatter.applyPattern(NUMBER_PATTERN)

        binding?.run {
            val watcher = NumberThousandSeparatorTextWatcher(
                textFieldDiscountAmount.textInputLayout.editText ?: return, numberFormatter
            ) { number, formattedNumber ->
                viewModel.onDiscountAmountChanged(number)

                textFieldDiscountAmount.textInputLayout.editText?.setText(formattedNumber)
                textFieldDiscountAmount.textInputLayout.editText?.setSelection(
                    textFieldDiscountAmount.textInputLayout.editText?.text?.length.orZero()
                )
                bottomSheetConfigModel?.let {
                    viewModel.validateInputProductDiscount(it)
                }
            }
            textFieldDiscountAmount.textInputLayout.editText?.addTextChangedListener(watcher)
        }

    }

    fun setOnApplyClickListener(onApplyClickListener: (ProductBulkApplyResult) -> Unit) {
        this.onApplyClickListener = onApplyClickListener
    }

    private fun setTextInputLayoutErrorMessage(view: TextFieldUnify2, errorMessage: String) {
        view.isInputError = true
        view.setMessage(errorMessage)
    }

}
