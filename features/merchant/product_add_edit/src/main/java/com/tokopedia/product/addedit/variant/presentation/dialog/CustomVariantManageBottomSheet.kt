package com.tokopedia.product.addedit.variant.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.databinding.AddEditProductCustomVariantManageBottomSheetContentBinding
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantTypeSelectedAdapter
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CustomVariantManageBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<AddEditProductCustomVariantManageBottomSheetContentBinding>()

    init {
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
        val adapter = VariantTypeSelectedAdapter()
        binding?.recyclerViewVariantSelected?.adapter = adapter
        binding?.recyclerViewVariantSelected?.layoutManager = LinearLayoutManager(context)
        adapter.setData(listOf("warna", "ukuran"))
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    fun setData(items: VariantInputModel?) {

    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this, this@CustomVariantManageBottomSheet.javaClass.simpleName)
        }
    }

    private fun initChildLayout() {
        setTitle(getString(R.string.label_variant_custom_manage_bottom_sheet_title))
        binding = AddEditProductCustomVariantManageBottomSheetContentBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        clearContentPadding = true
    }
}