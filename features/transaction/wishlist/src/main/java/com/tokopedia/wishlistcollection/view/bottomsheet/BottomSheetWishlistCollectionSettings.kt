package com.tokopedia.wishlistcollection.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.databinding.BottomsheetWishlistCollectionSettingsBinding
import com.tokopedia.wishlistcollection.data.model.BottomSheetKebabActionItemData
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.COLLECTION_ACTIONS
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.COLLECTION_ID
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.COLLECTION_NAME
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.COLLECTION_TYPE
import com.tokopedia.wishlistcollection.view.adapter.BottomSheetWishlistCollectionKebabMenuItemAdapter
import com.tokopedia.wishlistcollection.view.bottomsheet.listener.ActionListenerBottomSheetMenu
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionDetailFragment

class BottomSheetWishlistCollectionSettings : BottomSheetUnify() {
    private var binding by autoClearedNullable<BottomsheetWishlistCollectionSettingsBinding>()
    private var actionListener: ActionListenerBottomSheetMenu? = null
    private val collectionKebabItemAdapter = BottomSheetWishlistCollectionKebabMenuItemAdapter()

    companion object {
        private const val TAG: String = "BottomSheetKebabMenuWishlistCollectionItem"

        @JvmStatic
        fun newInstance(
            collectionName: String,
            collectionId: String,
            collectionType: Int,
            actions: List<GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.Setting.Button>
        ): BottomSheetWishlistCollectionSettings {
            return BottomSheetWishlistCollectionSettings().apply {
                val actionItems = actions.map {
                    BottomSheetKebabActionItemData(
                        text = it.text,
                        action = it.action,
                        url = it.url
                    )
                }.toTypedArray()

                arguments = Bundle().apply {
                    putString(COLLECTION_NAME, collectionName)
                    putString(COLLECTION_ID, collectionId)
                    putInt(COLLECTION_TYPE, collectionType)
                    putParcelableArray(COLLECTION_ACTIONS, actionItems)
                }
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
        val collectionType = arguments?.getInt(COLLECTION_TYPE, 0)
        val collectionActionItems = (arguments?.getParcelableArray(COLLECTION_ACTIONS) as? Array<BottomSheetKebabActionItemData>)?.toList() ?: emptyList()
        binding = BottomsheetWishlistCollectionSettingsBinding.inflate(LayoutInflater.from(context), null, false)
        binding?.run {
            rvMenu.adapter = collectionKebabItemAdapter
            rvMenu.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            collectionKebabItemAdapter.apply {
                setActionListener(actionListener)
                addList(collectionActionItems)
                _collectionId = collectionId
                _collectionName = collectionName
            }
        }
        setChild(binding?.root)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    fun setListener(fragment: WishlistCollectionDetailFragment) {
        this.actionListener = fragment
    }
}
