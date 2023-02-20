package com.tokopedia.privacycenter.ui.sharingwishlist.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.WishlistByIdTickerDataModel
import com.tokopedia.privacycenter.data.WishlistBydIdDataModel
import com.tokopedia.privacycenter.data.WishlistCollectionByIdDataModel
import com.tokopedia.privacycenter.databinding.SharingWishlistBottomSheetBinding
import com.tokopedia.privacycenter.di.DaggerPrivacyCenterComponent
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistConst.COLLECTION_PRIVATE_ID
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistConst.COLLECTION_PUBLIC_ID
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistConst.KEY_COLLECTION_ID
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistViewModel
import com.tokopedia.privacycenter.utils.getMessage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class SharingWishlistBottomSheet constructor(
    private val listener: Listener
) : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            SharingWishlistViewModel::class.java
        )
    }

    private var viewBinding by autoClearedNullable<SharingWishlistBottomSheetBinding>()
    private var collectionId: Int = 0
    private var collection = WishlistCollectionByIdDataModel()

    private var selectedOption: Int = 0
    private var defaultOption: Int = 0
    private var nameCollection: String = ""

    interface Listener {
        fun onUpdateWithMessage(message: String, isSuccess: Boolean)
    }

    init {
        showCloseIcon = true
        isDragable = false
        showKnob = false
        clearContentPadding = true
        bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun initInjector() {
        DaggerPrivacyCenterComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = SharingWishlistBottomSheetBinding.inflate(inflater, container, false)
        setChild(viewBinding?.root)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectionId = arguments?.getInt(KEY_COLLECTION_ID).orZero()
        initObservers()
        setupView()

        viewModel.getCollectionById(collectionId)
    }

    private fun setupView() {
        viewBinding?.apply {
            publicOption.setOnClickListener {
                privateOption.isChecked = false
                privateTips.show()
                selectedOption = COLLECTION_PUBLIC_ID
                buttonSave.isEnabled = defaultOption != COLLECTION_PUBLIC_ID
            }

            privateOption.setOnClickListener {
                publicOption.isChecked = false
                privateTips.hide()
                selectedOption = COLLECTION_PRIVATE_ID
                buttonSave.isEnabled = defaultOption != COLLECTION_PRIVATE_ID
            }

            buttonSave.setOnClickListener {
                viewModel.updateWishlist(
                    collection.apply {
                        access = selectedOption
                    }
                )
            }

            buttonAnotherSetup.setOnClickListener {
                RouteManager.getIntent(
                    context,
                    ApplinkConst.WISHLIST_COLLECTION_DETAIL,
                    collectionId.toString()
                ).also {
                    startActivity(it)
                }
            }
        }
    }

    private fun initObservers() {
        viewModel.wishlistCollectionById.observe(viewLifecycleOwner) {
            when (it) {
                is PrivacyCenterStateResult.Fail -> {
                    updateWithMessage(it.error.getMessage(context), false)
                }
                is PrivacyCenterStateResult.Loading -> {
                    showInitialLoader(true)
                }
                is PrivacyCenterStateResult.Success -> {
                    showInitialLoader(false)
                    onSuccessGetCollectionBydId(it.data)
                }
            }
        }

        viewModel.updateWishlist.observe(viewLifecycleOwner) {
            when (it) {
                is PrivacyCenterStateResult.Fail -> {
                    updateWithMessage(it.error.getMessage(context), false)
                }
                is PrivacyCenterStateResult.Loading -> {
                    viewBinding?.buttonSave?.isLoading = true
                }
                is PrivacyCenterStateResult.Success -> updateWithMessage(it.data.message, true)
            }
        }
    }

    private fun showInitialLoader(isLoading: Boolean) {
        viewBinding?.apply {
            loader.showWithCondition(isLoading)
            bottomSheetTitle.showWithCondition(!isLoading)
            privateOption.visibleWithCondition(!isLoading)
            publicOption.visibleWithCondition(!isLoading)
            labelPrivate.visibleWithCondition(!isLoading)
            labelPublic.visibleWithCondition(!isLoading)
            privateTips.visibleWithCondition(!isLoading)
            buttonSave.visibleWithCondition(!isLoading)
            buttonAnotherSetup.visibleWithCondition(!isLoading)
        }
    }

    private fun onSuccessGetCollectionBydId(data: WishlistBydIdDataModel) {
        collection = data.collection

        viewBinding?.apply {
            nameCollection = data.collection.name
            bottomSheetTitle.text = getString(R.string.sharing_wishlist_bottom_sheet_title, nameCollection)
            privateTips.description = generateTipsText(data.ticker)
            privateOption.isChecked = data.collection.access == COLLECTION_PRIVATE_ID
            publicOption.isChecked = data.collection.access == COLLECTION_PUBLIC_ID
            defaultOption = data.collection.access
            privateTips.showWithCondition(
                data.collection.access == COLLECTION_PUBLIC_ID
            )
        }
    }

    private fun updateWithMessage(message: String, isSuccess: Boolean) {
        val messageUpdate = if (isSuccess) {
            String.format(
                getString(
                    if (selectedOption == COLLECTION_PUBLIC_ID) {
                        R.string.sharing_wishlist_success_public
                    } else {
                        R.string.sharing_wishlist_success_private
                    }
                ),
                nameCollection
            )
        } else {
            message
        }
        listener.onUpdateWithMessage(messageUpdate, isSuccess)
        this.dismiss()
    }

    private fun generateTipsText(ticker: WishlistByIdTickerDataModel): String {
        var tips = ticker.title
        ticker.descriptions.forEach {
            tips += "\n ●\t $it"
        }

        return tips
    }

    companion object {
        const val TAG = "SharingWishlistBottomSheet"

        fun createInstance(collectionId: Int, listener: Listener): SharingWishlistBottomSheet {
            return SharingWishlistBottomSheet(listener).apply {
                arguments = Bundle().apply {
                    putInt(KEY_COLLECTION_ID, collectionId)
                }
            }
        }
    }
}
