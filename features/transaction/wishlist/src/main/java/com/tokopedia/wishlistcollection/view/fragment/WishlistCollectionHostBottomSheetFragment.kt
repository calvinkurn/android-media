package com.tokopedia.wishlistcollection.view.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.PATH_PRODUCT_ID
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.PATH_SRC
import com.tokopedia.wishlistcollection.di.DaggerWishlistCollectionHostBottomSheetComponent
import com.tokopedia.wishlistcollection.di.WishlistCollectionHostBottomSheetModule
import com.tokopedia.wishlistcollection.view.adapter.BottomSheetCollectionWishlistAdapter
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetAddCollectionWishlist
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetCreateNewCollectionWishlist
import com.tokopedia.wishlistcollection.view.viewmodel.WishlistCollectionHostBottomSheetViewModel
import javax.inject.Inject

class WishlistCollectionHostBottomSheetFragment: BaseDaggerFragment(),
    BottomSheetCollectionWishlistAdapter.ActionListener {

    private var productId = ""
    private var src = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val collectionHostBottomSheetViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[WishlistCollectionHostBottomSheetViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): WishlistCollectionHostBottomSheetFragment {
            return WishlistCollectionHostBottomSheetFragment().apply {
                arguments = bundle.apply {
                    putString(PATH_PRODUCT_ID, this.getString(PATH_PRODUCT_ID))
                    putString(PATH_SRC, this.getString(PATH_SRC))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = arguments?.getString(PATH_PRODUCT_ID) ?: ""
        src = arguments?.getString(PATH_SRC) ?: ""
        showBottomSheetCollection(childFragmentManager, productId, src)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let { activity ->
            DaggerWishlistCollectionHostBottomSheetComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .wishlistCollectionHostBottomSheetModule(WishlistCollectionHostBottomSheetModule(activity))
                .build()
                .inject(this)
        }
    }

    private fun getBaseAppComponent(): BaseAppComponent {
        return (activity?.application as BaseMainApplication).baseAppComponent
    }

    private fun showBottomSheetCollection(fragmentManager: FragmentManager, productId: String, source: String) {
        val bottomSheetCollection = BottomSheetAddCollectionWishlist.newInstance(productId, source)
        if (bottomSheetCollection.isAdded || fragmentManager.isStateSaved) return
        bottomSheetCollection.setActionListener(this@WishlistCollectionHostBottomSheetFragment)
        bottomSheetCollection.show(fragmentManager)
    }

    override fun onCollectionItemClicked(name: String, id: String) {
        val listId = arrayListOf<String>()
        listId.add(id)
        collectionHostBottomSheetViewModel.saveToWishlistCollection(name, listId)
        activity?.finish()
    }

    override fun onCreateNewCollectionClicked() {
        showBottomSheetCreateNewCollection(childFragmentManager)
    }

    private fun showBottomSheetCreateNewCollection(fragmentManager: FragmentManager) {
        val bottomSheetCreateCollection = BottomSheetCreateNewCollectionWishlist.newInstance(productId)
        if (bottomSheetCreateCollection.isAdded || fragmentManager.isStateSaved) return
        bottomSheetCreateCollection.show(fragmentManager)
    }
}