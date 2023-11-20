package com.tokopedia.wishlist.detail.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.databinding.BottomsheetThreeDotsMenuWishlistCollectionDetailBinding
import com.tokopedia.wishlist.detail.data.model.WishlistUiModel
import com.tokopedia.wishlist.detail.view.adapter.BottomSheetThreeDotsMenuWishlistAdapter

class BottomSheetThreeDotsMenuWishlist : BottomSheetUnify() {
    private var binding by autoClearedNullable<BottomsheetThreeDotsMenuWishlistCollectionDetailBinding>()
    private var listener: BottomSheetListener? = null
    private var adapterThreeDotsMenuBottomSheet: BottomSheetThreeDotsMenuWishlistAdapter? = null

    companion object {
        private const val TAG: String = "BottomSheetThreeDotsMenuWishlist"

        @JvmStatic
        fun newInstance(): BottomSheetThreeDotsMenuWishlist {
            return BottomSheetThreeDotsMenuWishlist()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BottomsheetThreeDotsMenuWishlistCollectionDetailBinding.inflate(LayoutInflater.from(context), null, false)
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

    fun setAdapter(adapter: BottomSheetThreeDotsMenuWishlistAdapter) {
        this.adapterThreeDotsMenuBottomSheet = adapter
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    interface BottomSheetListener {
        fun onThreeDotsMenuItemSelected(
                wishlistItem: WishlistUiModel.Item,
                additionalItem: WishlistUiModel.Item.Buttons.AdditionalButtonsItem
        )
    }
}
