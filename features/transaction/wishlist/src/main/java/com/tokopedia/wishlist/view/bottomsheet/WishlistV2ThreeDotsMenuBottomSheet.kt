package com.tokopedia.wishlist.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.data.model.WishlistV2UiModel
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.databinding.BottomsheetWishlistV2ThreeDotsMenuBinding
import com.tokopedia.wishlist.view.adapter.WishlistV2ThreeDotsMenuBottomSheetAdapter

class WishlistV2ThreeDotsMenuBottomSheet : BottomSheetUnify() {
    private var binding by autoClearedNullable<BottomsheetWishlistV2ThreeDotsMenuBinding>()
    private var listener: BottomSheetListener? = null
    private var adapterThreeDotsMenuBottomSheet: WishlistV2ThreeDotsMenuBottomSheetAdapter? = null

    companion object {
        private const val TAG: String = "WishlistV2ThreeDotsMenuBottomSheet"

        @JvmStatic
        fun newInstance(): WishlistV2ThreeDotsMenuBottomSheet {
            return WishlistV2ThreeDotsMenuBottomSheet()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BottomsheetWishlistV2ThreeDotsMenuBinding.inflate(LayoutInflater.from(context), null, false)
        binding?.run {
            listener?.let { adapterThreeDotsMenuBottomSheet?.setActionListener(it) }
            rvFilterOption.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = adapterThreeDotsMenuBottomSheet
            }
        }
        showCloseIcon = true
        showHeader = true
        setChild(binding?.root)
        setCloseClickListener { dismiss() }
    }

    fun setListener(listener: BottomSheetListener) {
        this.listener = listener
    }

    fun setAdapter(adapter: WishlistV2ThreeDotsMenuBottomSheetAdapter) {
        this.adapterThreeDotsMenuBottomSheet = adapter
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    interface BottomSheetListener {
        fun onThreeDotsMenuItemSelected(wishlistItem: WishlistV2UiModel.Item,
                                        additionalItem: WishlistV2UiModel.Item.Buttons.AdditionalButtonsItem)
    }
}