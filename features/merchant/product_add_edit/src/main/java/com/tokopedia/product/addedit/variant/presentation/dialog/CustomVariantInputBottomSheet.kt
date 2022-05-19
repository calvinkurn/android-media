package com.tokopedia.product.addedit.variant.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.util.getTrimmedText
import com.tokopedia.product.addedit.common.util.setText
import com.tokopedia.product.addedit.databinding.AddEditProductCustomVariantInputBottomSheetContentBinding
import com.tokopedia.product.addedit.preview.presentation.model.VariantTitleValidationStatus
import com.tokopedia.product.addedit.tracking.CustomVariantTypeTracking
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.di.DaggerAddEditProductVariantComponent
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantTypeSuggestionAdapter
import com.tokopedia.product.addedit.variant.presentation.viewmodel.AddEditProductVariantViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CustomVariantInputBottomSheet (
    private val variantTypeName: String = "",
    private val selectedVariantDetails: List<VariantDetail> = emptyList(),
    private val customVariantDetails: List<VariantDetail> = emptyList(),
    private val variantDetails: List<VariantDetail> = emptyList()
) : BottomSheetUnify() {

    @Inject
    lateinit var viewModel: AddEditProductVariantViewModel
    @Inject
    lateinit var userSession: UserSessionInterface
    private var binding by autoClearedNullable<AddEditProductCustomVariantInputBottomSheetContentBinding>()
    private var onCustomVariantTypeSubmitted: ((variantTypeName: String) -> Unit)? = null
    private var onPredefinedVariantTypeSubmitted: ((variantDetail: VariantDetail) -> Unit)? = null
    private val variantTypeSuggestionAdapter = VariantTypeSuggestionAdapter()
    private var isEditMode = false

    init {
        isKeyboardOverlap = false
        isFullpage = true
        isEditMode = variantTypeName.isNotEmpty()
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initInjector()
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViewSuggestion()
        setupTextFieldVariantTypeInput(variantTypeName)
        setupButtonSave()

        observeVariantTitleValidationStatus()
        observeVariantTypeSearchResult()
        observeGetVariantDetailResult()
    }

    private fun initInjector() {
        DaggerAddEditProductVariantComponent.builder()
            .addEditProductComponent(AddEditProductComponentBuilder.getComponent(
                    context?.applicationContext as BaseMainApplication))
            .build()
            .inject(this)
    }

    private fun initChildLayout() {
        setTitle( if (isEditMode) {
            getString(R.string.label_variant_custom_input_bottom_sheet_title_edit)
        } else {
            getString(R.string.label_variant_custom_input_bottom_sheet_title_add)
        })

        binding = AddEditProductCustomVariantInputBottomSheetContentBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        clearContentPadding = true
    }

    private fun setupButtonSave() {
        binding?.buttonSave?.text = if (isEditMode) {
            getString(R.string.action_variant_custom_type_edit)
        } else {
            getString(R.string.action_variant_custom_type_add)
        }
        binding?.buttonSave?.setOnClickListener {
            val inputText = binding?.textFieldVariantTypeInput?.getTrimmedText().orEmpty()
            viewModel.validateVariantTitle(inputText, selectedVariantDetails, customVariantDetails,
                variantDetails)
        }
    }

    private fun setupTextFieldVariantTypeInput(variantTypeName: String) {
        binding?.textFieldVariantTypeInput?.setText(variantTypeName)
        binding?.textFieldVariantTypeInput?.editText?.afterTextChanged {
            val cleanedText = it.trim().replace("\\s+".toRegex(), " ")
            binding?.textFieldVariantTypeInput?.setMessage("")
            binding?.textFieldVariantTypeInput?.isInputError = false
            variantTypeSuggestionAdapter.setHighlightCharLength(cleanedText.length)
            viewModel.getAllVariantFromKeyword(cleanedText)
        }
    }

    private fun setupRecyclerViewSuggestion() {
        variantTypeSuggestionAdapter.setDisabledData(selectedVariantDetails)
        variantTypeSuggestionAdapter.setOnItemClickedListener { position, variantTypeName ->
            try {
                binding?.textFieldVariantTypeInput?.setText(variantTypeName)
                binding?.textFieldVariantTypeInput?.editText?.setSelection(variantTypeName.length)
            } catch (e: Exception) {
                // no op
            }
            CustomVariantTypeTracking.clickSelectVariantRecommendation(userSession.shopId,
                    variantTypeName, isEditMode)
        }
        binding?.recyclerViewVariantSuggestion?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = variantTypeSuggestionAdapter
            layoutParams?.height = getScreenHeight()
            requestLayout()
        }
    }

    private fun observeVariantTypeSearchResult() {
        viewModel.variantTypeSearchResult.observe(viewLifecycleOwner) { searchResult ->
            if (searchResult is Success) {
                variantTypeSuggestionAdapter.setData(searchResult.data)
                binding?.typographyRecommendationTitle?.isVisible = searchResult.data.isNotEmpty()
            } else if (searchResult is Fail) {
                val errorMessage = ErrorHandler.getErrorMessage(requireContext(),
                    searchResult.throwable)
                binding?.textFieldVariantTypeInput?.setMessage(errorMessage)
                binding?.textFieldVariantTypeInput?.isInputError = true
            }
        }
    }

    private fun observeVariantTitleValidationStatus() {
        viewModel.variantTitleValidationStatus.observe(viewLifecycleOwner) { validationStatus ->
            binding?.textFieldVariantTypeInput?.apply {
                isInputError = true
                when (validationStatus) {
                    VariantTitleValidationStatus.ILLEGAL_WORD -> {
                        setMessage(getString(R.string.error_illegal_variant_name))
                    }
                    VariantTitleValidationStatus.SYMBOL_ERROR -> {
                        setMessage(getString(R.string.error_symbol_variant_name))
                    }
                    VariantTitleValidationStatus.USED_NAME -> {
                        setMessage(getString(R.string.error_used_variant_name))
                    }
                    VariantTitleValidationStatus.MINIMUM_CHAR -> {
                        setMessage(getString(R.string.error_min_char_variant_name))
                    }
                    else -> {
                        isInputError = false
                        submitData(getTrimmedText())
                    }
                }
            }
        }
    }

    private fun observeGetVariantDetailResult() {
        viewModel.getVariantDetailResult.observe(viewLifecycleOwner) { result ->
            if (result is Success) {
                val variantDetail = result.data.apply { isCustom = true }
                onPredefinedVariantTypeSubmitted?.invoke(variantDetail)
                dismiss()
            } else if (result is Fail){
                val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                binding?.buttonSave?.isLoading = false
                binding?.textFieldVariantTypeInput?.setMessage(errorMessage)
            }
        }
    }

    private fun submitData(variantTitle: String) {
        val newVariantType = variantTypeSuggestionAdapter.getItemFromVariantName(variantTitle)
        if (newVariantType == null) {
            dismiss()
            onCustomVariantTypeSubmitted?.invoke(variantTitle)
        } else {
            viewModel.getVariantDetailFromVariantId(newVariantType.variantId)
        }
        binding?.buttonSave?.isLoading = true
        CustomVariantTypeTracking.clickSubmitVariantSuccess(userSession.shopId, variantTitle, isEditMode)
    }

    fun setOnCustomVariantTypeSubmitted(listener: (variantTypeName: String) -> Unit) {
        onCustomVariantTypeSubmitted = listener
    }

    fun setOnPredefinedVariantTypeSubmitted(listener: (variantDetail: VariantDetail) -> Unit) {
        onPredefinedVariantTypeSubmitted = listener
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this, this@CustomVariantInputBottomSheet.javaClass.simpleName)
        }
    }
}