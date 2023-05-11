package com.tokopedia.wishlistcollection.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform
import com.tokopedia.wishlistcollection.data.response.CreateWishlistCollectionResponse
import com.tokopedia.wishlistcollection.view.IWishlistCollectionFragment
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetCreateNewCollectionWishlist

class WishlistCollectionCreateHostBottomSheetFragment : Fragment(), IWishlistCollectionFragment {

    private var src = ""

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): WishlistCollectionCreateHostBottomSheetFragment {
            return WishlistCollectionCreateHostBottomSheetFragment().apply {
                arguments = bundle.apply {
                    putString(
                        ApplinkConstInternalPurchasePlatform.PATH_SRC, this.getString(
                            ApplinkConstInternalPurchasePlatform.PATH_SRC
                        )
                    )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        src = arguments?.getString(ApplinkConstInternalPurchasePlatform.PATH_SRC) ?: ""
        showBottomSheetCollection(childFragmentManager, arrayListOf(), src)
    }

    private fun showBottomSheetCollection(
        fragmentManager: FragmentManager,
        productIds: ArrayList<String>,
        source: String
    ) {
        val bottomSheetCreateNewCollection = BottomSheetCreateNewCollectionWishlist.newInstance(
            productIds, source
        )
        if (bottomSheetCreateNewCollection.isAdded || fragmentManager.isStateSaved) return
        bottomSheetCreateNewCollection.setListener(this)
        bottomSheetCreateNewCollection.setOnDismissListener { activity?.finish() }
        bottomSheetCreateNewCollection.show(fragmentManager)
    }

    override fun onSuccessCreateNewCollection(
        message: CreateWishlistCollectionResponse.CreateWishlistCollection.DataCreate,
        newCollectionName: String
    ) {
        val intent = Intent()
        intent.putExtra(ApplinkConstInternalPurchasePlatform.BOOLEAN_EXTRA_SUCCESS, true)
        intent.putExtra(ApplinkConstInternalPurchasePlatform.STRING_EXTRA_MESSAGE_TOASTER, message.message)
        intent.putExtra(ApplinkConstInternalPurchasePlatform.STRING_EXTRA_COLLECTION_ID, message.id)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }
}
