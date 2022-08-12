package com.tokopedia.wishlistcollection.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.BOOLEAN_EXTRA_SUCCESS
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.PATH_PRODUCT_ID
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.PATH_SRC
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.STRING_EXTRA_COLLECTION_ID
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.STRING_EXTRA_MESSAGE_TOASTER
import com.tokopedia.wishlist.R
import com.tokopedia.wishlistcollection.data.params.AddWishlistCollectionsHostBottomSheetParams
import com.tokopedia.wishlistcollection.data.response.AddWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionsBottomSheetResponse
import com.tokopedia.wishlistcollection.view.adapter.BottomSheetCollectionWishlistAdapter
import com.tokopedia.wishlistcollection.view.bottomsheet.listener.ActionListenerFromPdp
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetAddCollectionWishlist
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetCreateNewCollectionWishlist
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.IS_PRODUCT_ACTIVE

class WishlistCollectionHostBottomSheetFragment: Fragment(),
    BottomSheetCollectionWishlistAdapter.ActionListener,
    BottomSheetAddCollectionWishlist.ActionListener,
    ActionListenerFromPdp {

    private var productId = ""
    private var src = ""
    private var bottomSheetCollection = BottomSheetAddCollectionWishlist()
    private var isProductActive = true

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): WishlistCollectionHostBottomSheetFragment {
            return WishlistCollectionHostBottomSheetFragment().apply {
                arguments = bundle.apply {
                    putString(PATH_PRODUCT_ID, this.getString(PATH_PRODUCT_ID))
                    putString(PATH_SRC, this.getString(PATH_SRC))
                    putBoolean(IS_PRODUCT_ACTIVE, this.getBoolean(IS_PRODUCT_ACTIVE))
                }
            }
        }

        private const val OK = "OK"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productId = arguments?.getString(PATH_PRODUCT_ID) ?: ""
        src = arguments?.getString(PATH_SRC) ?: ""
        isProductActive = arguments?.getBoolean(IS_PRODUCT_ACTIVE) ?: true
        showBottomSheetCollection(childFragmentManager, productId, src, isProductActive)
    }

    private fun showBottomSheetCollection(
        fragmentManager: FragmentManager,
        productId: String,
        source: String,
        isProductActive: Boolean
    ) {
        bottomSheetCollection = BottomSheetAddCollectionWishlist.newInstance(productId, source, isProductActive)
        if (bottomSheetCollection.isAdded || fragmentManager.isStateSaved) return
        bottomSheetCollection.setActionListener(this@WishlistCollectionHostBottomSheetFragment)
        bottomSheetCollection.setOnDismissListener { activity?.finish() }
        bottomSheetCollection.show(fragmentManager)
    }

    override fun onCollectionItemClicked(name: String, id: String) {
        val listProductId = arrayListOf<String>()
        listProductId.add(productId)
        val addWishlistParam = AddWishlistCollectionsHostBottomSheetParams(collectionId = id, collectionName = name, productIds = listProductId)
        bottomSheetCollection.saveToCollection(addWishlistParam)
    }

    override fun onCreateNewCollectionClicked(dataObject: GetWishlistCollectionsBottomSheetResponse.GetWishlistCollectionsBottomsheet.Data) {
        if (dataObject.totalCollection < dataObject.maxLimitCollection) {
            showBottomSheetCreateNewCollection(childFragmentManager)
        } else {
            val intent = Intent()
            intent.putExtra(BOOLEAN_EXTRA_SUCCESS, false)
            intent.putExtra(STRING_EXTRA_MESSAGE_TOASTER, dataObject.wordingMaxLimitCollection)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }
    }

    private fun showBottomSheetCreateNewCollection(fragmentManager: FragmentManager) {
        val bottomSheetCreateCollection = BottomSheetCreateNewCollectionWishlist.newInstance(productId)
        bottomSheetCreateCollection.setListener(this@WishlistCollectionHostBottomSheetFragment)
        if (bottomSheetCreateCollection.isAdded || fragmentManager.isStateSaved) return
        bottomSheetCreateCollection.show(fragmentManager)
    }

    override fun onSuccessSaveItemToCollection(data: AddWishlistCollectionItemsResponse.AddWishlistCollectionItems) {
        val intent = Intent()
        if (data.status == OK && data.dataItem.success) {
            intent.putExtra(BOOLEAN_EXTRA_SUCCESS, data.dataItem.success)
            intent.putExtra(STRING_EXTRA_MESSAGE_TOASTER, data.dataItem.message)
            intent.putExtra(STRING_EXTRA_COLLECTION_ID, data.dataItem.collectionId)

        } else {
            intent.putExtra(BOOLEAN_EXTRA_SUCCESS, false)
            val errorMessage = data.errorMessage.first().ifEmpty { context?.getString(
                R.string.wishlist_v2_common_error_msg) }
            intent.putExtra(STRING_EXTRA_MESSAGE_TOASTER, errorMessage)
        }
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onFailedSaveItemToCollection(errorMessage: String) {
        val intent = Intent()
        intent.putExtra(BOOLEAN_EXTRA_SUCCESS, false)
        intent.putExtra(STRING_EXTRA_MESSAGE_TOASTER, errorMessage)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onSuccessSaveToNewCollection(data: AddWishlistCollectionItemsResponse.AddWishlistCollectionItems.DataItem) {
        bottomSheetCollection.dismiss()
        val intent = Intent()
        intent.putExtra(BOOLEAN_EXTRA_SUCCESS, true)
        intent.putExtra(STRING_EXTRA_MESSAGE_TOASTER, data.message)
        intent.putExtra(STRING_EXTRA_COLLECTION_ID, data.collectionId)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onFailedSaveToNewCollection(errorMessage: String?) {
        bottomSheetCollection.dismiss()
        val intent = Intent()
        intent.putExtra(BOOLEAN_EXTRA_SUCCESS, false)
        intent.putExtra(STRING_EXTRA_MESSAGE_TOASTER, errorMessage)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }
}