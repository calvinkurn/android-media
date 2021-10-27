package com.tokopedia.product.addedit.variant.presentation.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.databinding.AddEditProductCustomVariantInputBottomSheetContentBinding
import com.tokopedia.product.addedit.specification.presentation.adapter.SpecificationValueAdapter
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.android.synthetic.main.add_edit_product_custom_variant_input_bottom_sheet_content.view.*
import kotlinx.android.synthetic.main.add_edit_product_select_variant_main_bottom_sheet_content.view.*

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

        binding?.root?.recyclerViewVariantSuggestion?.layoutManager = LinearLayoutManager(context)
        binding?.root?.recyclerViewVariantSuggestion?.adapter = SpecificationValueAdapter(childFragmentManager)
        binding?.root?.recyclerViewVariantSuggestion?.minimumHeight = 0
        binding?.root?.recyclerViewVariantSuggestion?.layoutParams?.height = getScreenHeight()
        binding?.root?.recyclerViewVariantSuggestion?.requestLayout()
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