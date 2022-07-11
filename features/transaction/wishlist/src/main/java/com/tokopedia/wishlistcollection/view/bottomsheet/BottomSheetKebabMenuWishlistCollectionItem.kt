package com.tokopedia.wishlistcollection.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.databinding.BottomsheetKebabMenuWishlistCollectionItemBinding

class BottomSheetKebabMenuWishlistCollectionItem: BottomSheetUnify() {
    private var binding by autoClearedNullable<BottomsheetKebabMenuWishlistCollectionItemBinding>()

    companion object {
        private const val TAG: String = "BottomSheetKebabMenuWishlistCollectionItem"

        @JvmStatic
        fun newInstance(): BottomSheetKebabMenuWishlistCollectionItem {
            return BottomSheetKebabMenuWishlistCollectionItem()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initLayout() {
        binding = BottomsheetKebabMenuWishlistCollectionItemBinding.inflate(LayoutInflater.from(context), null, false)
        showCloseIcon = true
        showHeader = true
        setChild(binding?.root)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }
}