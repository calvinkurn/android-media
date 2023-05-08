package com.tokopedia.wishlist.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.databinding.BottomsheetWishlistFilterBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2FilterBottomSheetAdapter

class WishlistV2FilterBottomSheet : BottomSheetUnify() {
    private var binding by autoClearedNullable<BottomsheetWishlistFilterBinding>()
    private var listener: BottomSheetListener? = null
    private var adapterOptionBottomSheet: WishlistV2FilterBottomSheetAdapter? = null

    companion object {
        private const val TAG: String = "WishlistV2FilterBottomSheet"
        private const val TITLE_BOTTOMSHEET = "title_bottomsheet"
        private const val SELECTION_TYPE = "selection_type"

        @JvmStatic
        fun newInstance(title: String, selectionType: Int): WishlistV2FilterBottomSheet {
            return WishlistV2FilterBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(TITLE_BOTTOMSHEET, title)
                bundle.putInt(SELECTION_TYPE, selectionType)
                arguments = bundle
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BottomsheetWishlistFilterBinding.inflate(LayoutInflater.from(context), null, false)
        binding?.run {
            listener?.let { adapterOptionBottomSheet?.setActionListener(it) }
            rvFilterOption.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = adapterOptionBottomSheet
            }
            btnSave.gone()
        }
        showCloseIcon = true
        showHeader = true
        setChild(binding?.root)
        setTitle(arguments?.getString(TITLE_BOTTOMSHEET) ?: "")
        setCloseClickListener { dismiss() }
    }

    fun setListener(listener: BottomSheetListener) {
        this.listener = listener
    }

    fun setAdapter(adapter: WishlistV2FilterBottomSheetAdapter) {
        this.adapterOptionBottomSheet = adapter
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    fun showButtonSave() {
        binding?.run {
            btnSave.visible()
            btnSave.setOnClickListener { listener?.onSaveCheckboxSelection() }
        }
    }

    interface BottomSheetListener {
        fun onRadioButtonSelected(name: String, optionId: String, label: String)
        fun onCheckboxSelected(name: String, optionId: String, isChecked: Boolean, titleCheckbox: String)
        fun onSaveCheckboxSelection()
    }
}
