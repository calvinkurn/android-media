package com.tokopedia.product.addedit.variant.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.setText
import com.tokopedia.product.addedit.databinding.AddEditProductCustomVariantInputBottomSheetContentBinding
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantTypeSuggestionAdapter
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CustomVariantInputBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<AddEditProductCustomVariantInputBottomSheetContentBinding>()

    init {
        isKeyboardOverlap = false
        isFullpage = true
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    fun setData(items: VariantInputModel?) {

    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this, this@CustomVariantInputBottomSheet.javaClass.simpleName)
        }
    }

    private fun initChildLayout() {
        setTitle(getString(R.string.label_variant_custom_input_bottom_sheet_title))

        binding = AddEditProductCustomVariantInputBottomSheetContentBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        clearContentPadding = true
    }
}