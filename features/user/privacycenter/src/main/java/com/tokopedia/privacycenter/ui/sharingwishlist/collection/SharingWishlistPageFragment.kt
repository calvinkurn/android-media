package com.tokopedia.privacycenter.ui.sharingwishlist.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.asLowerCase
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.data.WishlistCollectionsDataModel
import com.tokopedia.privacycenter.data.WishlistDataModel
import com.tokopedia.privacycenter.databinding.SharingWishlistPageBinding
import com.tokopedia.privacycenter.di.PrivacyCenterComponent
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistConst.COLLECTION_PRIVATE
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistConst.COLLECTION_PUBLIC
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistConst.COLLECTION_PUBLIC_ID
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistConst.PARAM_COLLECTION_TYPE
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistSharedViewModel
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistStateResult
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistViewModel
import com.tokopedia.privacycenter.ui.sharingwishlist.collection.adapter.SharingWishlistCollectionAdapter
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class SharingWishlistPageFragment :
    BaseDaggerFragment(),
    SharingWishlistCollectionAdapter.Listener,
    SharingWishlistBottomSheet.Listener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewBinding by autoClearedNullable<SharingWishlistPageBinding>()

    private var collectionAccess: Int = 0

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            SharingWishlistViewModel::class.java
        )
    }

    private val sharedViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(
            SharingWishlistSharedViewModel::class.java
        )
    }

    private val collectionAdapter = SharingWishlistCollectionAdapter(this)

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(PrivacyCenterComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = SharingWishlistPageBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()

        viewBinding?.sharingWishlistData?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = collectionAdapter
        }

        collectionAccess = arguments?.getInt(PARAM_COLLECTION_TYPE).orZero()
        viewModel.getWishlistCollections(collectionAccess)
    }

    private fun initObservers() {
        viewModel.wishlistCollection.observe(viewLifecycleOwner) {
            when (it) {
                is SharingWishlistStateResult.Fail -> showLocalLoad()
                is SharingWishlistStateResult.Loading -> showLoading()
                is SharingWishlistStateResult.CollectionEmpty -> onCollectionEmpty()
                is SharingWishlistStateResult.RenderCollection -> onRenderCollection(it.data)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            sharedViewModel.wishlistCollectionState.collectLatest {
                viewModel.getWishlistCollections(collectionAccess)
            }
        }
    }

    private fun onRenderCollection(data: WishlistDataModel) {
        collectionAdapter.apply {
            val previousSize = collectionAdapter.itemCount

            clearAllItems()
            notifyItemRangeRemoved(0, previousSize)

            addItems(data.collections)
            notifyItemRangeInserted(0, data.collections.size)
        }

        viewBinding?.apply {
            loader.hide()
            localLoad.hide()
            emptyPage.root.hide()

            sharingWishlistData.show()
        }
    }

    private fun onCollectionEmpty() {
        viewBinding?.apply {
            sharingWishlistData.hide()
            loader.hide()
            localLoad.hide()
        }

        viewBinding?.emptyPage?.apply {
            if (collectionAccess == COLLECTION_PUBLIC_ID) {
                errorPageTitle.text = getString(R.string.sharing_wishlist_empty_page_title_public)
                errorPageDescription.text = getString(R.string.sharing_wishlist_empty_page_description_public)
            } else {
                errorPageTitle.text = getString(R.string.sharing_wishlist_empty_page_title_private)
                errorPageDescription.text = getString(R.string.sharing_wishlist_empty_page_description_private)
            }

            errorPageImage.loadImage(getString(R.string.sharing_wishlist_empty_page))
        }?.root?.show()
    }

    private fun showLoading() {
        viewBinding?.apply {
            localLoad.hide()
            emptyPage.root.hide()
            sharingWishlistData.hide()

            loader.show()
        }
    }

    private fun showLocalLoad() {
        viewBinding?.apply {
            loader.hide()
            emptyPage.root.hide()
            sharingWishlistData.hide()
            localLoad.apply {
                title?.text = getString(R.string.sharing_wishlist_failed_title)
                refreshBtn?.setOnClickListener {
                    localLoad.hide()
                    viewModel.getWishlistCollections(collectionAccess)
                }
            }.show()
        }
    }

    override fun onCollectionItemClicked(data: WishlistCollectionsDataModel) {
        SharingWishlistBottomSheet.createInstance(
            data.id,
            this
        ).show(
            parentFragmentManager,
            SharingWishlistBottomSheet.TAG
        )
    }

    override fun onUpdateWithMessage(message: String, isSuccess: Boolean) {
        var snackbar: Snackbar? = null

        snackbar = if (isSuccess) {
            Toaster.build(
                view = requireView(),
                message,
                type = Toaster.TYPE_NORMAL,
                actionText = getString(R.string.sharing_wishlist_oke),
                clickListener = { snackbar?.dismiss() }
            )
        } else {
            Toaster.build(view = requireView(), message, type = Toaster.TYPE_ERROR)
        }

        snackbar.show()

        if (isSuccess) {
            sharedViewModel.notifyPager(collectionAccess)
        }
    }

    companion object {
        fun createInstance(collectionType: Int) = SharingWishlistPageFragment().apply {
            arguments = Bundle().apply {
                putInt(PARAM_COLLECTION_TYPE, collectionType)
            }
        }
    }
}
