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
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.util.getText
import com.tokopedia.product.addedit.common.util.setText
import com.tokopedia.product.addedit.databinding.AddEditProductCustomVariantInputBottomSheetContentBinding
import com.tokopedia.product.addedit.preview.presentation.model.VariantTitleValidationStatus
import com.tokopedia.product.addedit.variant.di.DaggerAddEditProductVariantComponent
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantTypeSuggestionAdapter
import com.tokopedia.product.addedit.variant.presentation.viewmodel.AddEditProductVariantViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CustomVariantInputBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var viewModel: AddEditProductVariantViewModel

    private var binding by autoClearedNullable<AddEditProductCustomVariantInputBottomSheetContentBinding>()
    private var onDataSubmitted: ((variantTypeName: String) -> Unit)? = null

    init {
        isKeyboardOverlap = false
        isFullpage = true
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
        setupTextFieldVariantTypeInput()
        setupButtonSave()

        observeVariantTitleValidationStatus()
    }

    private fun initInjector() {
        DaggerAddEditProductVariantComponent.builder()
            .addEditProductComponent(AddEditProductComponentBuilder.getComponent(requireContext().applicationContext as BaseMainApplication))
            .build()
            .inject(this)
    }

    private fun initChildLayout() {
        setTitle(getString(R.string.label_variant_custom_input_bottom_sheet_title))

        binding = AddEditProductCustomVariantInputBottomSheetContentBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        clearContentPadding = true
    }

    private fun setupButtonSave() {
        binding?.buttonSave?.setOnClickListener {
            val inputText = binding?.textFieldVariantTypeInput?.getText().orEmpty()
            viewModel.validateVariantTitle(inputText)
        }
    }

    private fun setupTextFieldVariantTypeInput() {
        binding?.textFieldVariantTypeInput?.editText?.afterTextChanged {
            binding?.textFieldVariantTypeInput?.setMessage("")
            binding?.textFieldVariantTypeInput?.isInputError = false
        }
    }

    private fun setupRecyclerViewSuggestion() {
        val adapter = VariantTypeSuggestionAdapter()
        adapter.setData(List(100) { "Warna$it" })
        adapter.setOnItemClickedListener { position, variantTypeName ->
            try {
                binding?.textFieldVariantTypeInput?.setText(variantTypeName)
                binding?.textFieldVariantTypeInput?.editText?.setSelection(variantTypeName.length)
            } catch (e: Exception) {
                // no op
            }
        }
        binding?.recyclerViewVariantSuggestion?.layoutManager = LinearLayoutManager(context)
        binding?.recyclerViewVariantSuggestion?.adapter = adapter
        binding?.recyclerViewVariantSuggestion?.minimumHeight = 0
        binding?.recyclerViewVariantSuggestion?.layoutParams?.height = getScreenHeight()
        binding?.recyclerViewVariantSuggestion?.requestLayout()
        adapter.setHighlightCharLength(3)
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
                        submitData(getText())
                    }
                }
            }
        }
    }

    private fun submitData(variantTitle: String) {
        dismiss()
        onDataSubmitted?.invoke(variantTitle)
    }

    fun setOnDataSubmitted(listener: (variantTypeName: String) -> Unit) {
        onDataSubmitted = listener
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this, this@CustomVariantInputBottomSheet.javaClass.simpleName)
        }
    }
}