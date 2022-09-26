package com.tokopedia.wishlistcollection.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.databinding.BottomsheetWishlistCollectionSettingsBinding
import com.tokopedia.wishlistcollection.analytics.WishlistCollectionAnalytics
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionDetailFragment
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionFragment
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.COLLECTION_ID
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.COLLECTION_NAME

class BottomSheetWishlistCollectionSettings: BottomSheetUnify() {
    private var binding by autoClearedNullable<BottomsheetWishlistCollectionSettingsBinding>()
    private var actionListener: ActionListener? = null

    companion object {
        private const val TAG: String = "BottomSheetKebabMenuWishlistCollectionItem"

        @JvmStatic
        fun newInstance(collectionName: String, collectionId: String): BottomSheetWishlistCollectionSettings {
            return BottomSheetWishlistCollectionSettings().apply {
                val bundle = Bundle()
                bundle.putString(COLLECTION_NAME, collectionName)
                bundle.putString(COLLECTION_ID, collectionId)
                arguments = bundle
            }
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

    init {
        showCloseIcon = true
        showHeader = true
        isFullpage = false
        isKeyboardOverlap = false
    }

    private fun initLayout() {
        val collectionName = arguments?.getString(COLLECTION_NAME) ?: ""
        val collectionId = arguments?.getString(COLLECTION_ID) ?: ""
        binding = BottomsheetWishlistCollectionSettingsBinding.inflate(LayoutInflater.from(context), null, false)
        binding?.run {
            llKebabMenu1.setOnClickListener {
                dismiss()
                actionListener?.onChangeCollectionName(collectionId, collectionName)
                WishlistCollectionAnalytics.sendClickOptionOnGearIconEvent(wishlistCollectionSettingTvMenu1.text.toString())
            }
            llKebabMenu2.setOnClickListener {
                dismiss()
                actionListener?.onManageCollectionItems()
                WishlistCollectionAnalytics.sendClickOptionOnGearIconEvent(wishlistCollectionSettingTvMenu2.text.toString())
            }
            llKebabMenu3.setOnClickListener {
                dismiss()
                actionListener?.onDeleteCollectionItem(collectionId, collectionName)
                WishlistCollectionAnalytics.sendClickOptionOnGearIconEvent(wishlistCollectionSettingTvMenu3.text.toString())
            }
        }
        setChild(binding?.root)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    interface ActionListener {
        fun onChangeCollectionName(collectionId: String, collectionName: String)
        fun onManageCollectionItems()
        fun onDeleteCollectionItem(collectionId: String, collectionName: String)
    }

    fun setListener(fragment: WishlistCollectionDetailFragment) {
        this.actionListener = fragment
    }
}