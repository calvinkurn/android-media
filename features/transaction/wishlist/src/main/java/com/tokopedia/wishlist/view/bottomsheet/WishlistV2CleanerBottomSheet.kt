package com.tokopedia.wishlist.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.databinding.BottomsheetWishlistStorageCleanerBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2CleanerBottomSheetAdapter

class WishlistV2CleanerBottomSheet : BottomSheetUnify() {
    private var binding by autoClearedNullable<BottomsheetWishlistStorageCleanerBinding>()
    private var adapterOptionBottomSheet: WishlistV2CleanerBottomSheetAdapter? = null
    private var listener: BottomsheetCleanerListener? = null

    companion object {
        private const val TAG: String = "WishlistV2CleanerBottomSheet"
        private const val TITLE_BOTTOMSHEET = "title_bottomsheet"
        private const val DESC_BOTTOMSHEET = "desc_bottomsheet"
        private const val BUTTON_TEXT = "button_text"

        @JvmStatic
        fun newInstance(title: String, desc: String, buttonText: String): WishlistV2CleanerBottomSheet {
            return WishlistV2CleanerBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(TITLE_BOTTOMSHEET, title)
                bundle.putString(DESC_BOTTOMSHEET, desc)
                bundle.putString(BUTTON_TEXT, buttonText)
                arguments = bundle
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BottomsheetWishlistStorageCleanerBinding.inflate(LayoutInflater.from(context), null, false)
        binding?.run {
            rvCleanOptions.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = adapterOptionBottomSheet
            }
            btnSeeItems.apply {
                visible()
                text = arguments?.getString(BUTTON_TEXT) ?: ""
                setOnClickListener {
                    dismiss()

                    val indexSelected = adapterOptionBottomSheet?.getSelectedIndex()
                    indexSelected?.let { index -> listener?.onButtonCleanerClicked(index) }
                }
            }
            btnSeeItems.text = arguments?.getString(BUTTON_TEXT) ?: ""
            storageCleanerDesc.text = arguments?.getString(DESC_BOTTOMSHEET) ?: ""
        }
        showCloseIcon = true
        showHeader = true
        setChild(binding?.root)
        setTitle(arguments?.getString(TITLE_BOTTOMSHEET) ?: "")
        setCloseClickListener { dismiss() }
    }

    fun setAdapter(adapter: WishlistV2CleanerBottomSheetAdapter) {
        this.adapterOptionBottomSheet = adapter
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    fun setListener(listener: BottomsheetCleanerListener) {
        this.listener = listener
    }

    interface BottomsheetCleanerListener {
        fun onButtonCleanerClicked(index: Int)
    }
}
