package com.tokopedia.wishlistcommon.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist_common.databinding.BottomsheetAddToWishlistCollectionBinding

class AddToCollectionWishlistBottomSheet: BottomSheetUnify() {
    private var binding by autoClearedNullable<BottomsheetAddToWishlistCollectionBinding>()

    companion object {
        private const val TAG: String = "AddToCollectionWishlistBottomSheet"

        @JvmStatic
        fun newInstance(): AddToCollectionWishlistBottomSheet { return AddToCollectionWishlistBottomSheet() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BottomsheetAddToWishlistCollectionBinding.inflate(LayoutInflater.from(context), null, false)
        /*binding?.run {
            rvAllWishlistCollections.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            }
        }*/
        showCloseIcon = false
        showHeader = true
        setChild(binding?.root)
        setTitle("Tersimpan di Wishlist!")
        setAction("Cek Wishlist") { context?.let { it1 -> goToWishlistPage(it1) } }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun goToWishlistPage(context: Context) {
        RouteManager.route(context, ApplinkConst.NEW_WISHLIST)
    }
}