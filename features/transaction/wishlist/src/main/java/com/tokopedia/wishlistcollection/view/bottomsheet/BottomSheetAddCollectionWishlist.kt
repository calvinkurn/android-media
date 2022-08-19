package com.tokopedia.wishlistcollection.view.bottomsheet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.databinding.BottomsheetAddWishlistCollectionBinding
import com.tokopedia.wishlist.R
import com.tokopedia.wishlistcollection.data.model.BottomSheetWishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcollection.data.params.AddWishlistCollectionsHostBottomSheetParams
import com.tokopedia.wishlistcollection.data.params.GetWishlistCollectionsBottomSheetParams
import com.tokopedia.wishlistcollection.data.response.AddWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.di.DaggerBottomSheetWishlistCollectionComponent
import com.tokopedia.wishlistcollection.view.adapter.BottomSheetCollectionWishlistAdapter
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionDetailFragment
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionHostBottomSheetFragment
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.OK
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.PRODUCT_IDs
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.SOURCE
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_COLLECTION_ADDITIONAL_SECTION
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_COLLECTION_ITEM
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_COLLECTION_MAIN_SECTION
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_CREATE_NEW_COLLECTION
import com.tokopedia.wishlistcollection.view.viewmodel.BottomSheetAddCollectionViewModel
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.IS_PRODUCT_ACTIVE
import java.util.ArrayList
import javax.inject.Inject

class BottomSheetAddCollectionWishlist: BottomSheetUnify(), HasComponent<com.tokopedia.wishlistcollection.di.BottomSheetWishlistCollectionComponent> {
    private var binding by autoClearedNullable<BottomsheetAddWishlistCollectionBinding>()
    private val userSession: UserSessionInterface by lazy { UserSession(activity) }
    private val collectionAdapter = BottomSheetCollectionWishlistAdapter()
    private var actionListener: ActionListener? = null
    private var isProductActive: Boolean = true
    private var toasterErrorMessage: String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val getCollectionsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[BottomSheetAddCollectionViewModel::class.java]
    }

    interface ActionListener {
        fun onSuccessSaveItemToCollection(data: AddWishlistCollectionItemsResponse.AddWishlistCollectionItems)
        fun onFailedSaveItemToCollection(errorMessage: String)
    }

    /*override fun onDismiss(dialog: DialogInterface) {
        // TODO: need to change to applink if needed to show toaster
        if (toasterErrorMessage.isNotEmpty()) {
            showToaster(toasterErrorMessage, "", Toaster.TYPE_ERROR)
            toasterErrorMessage = ""
        }
        super.onDismiss(dialog)
    }*/

    companion object {
        private const val TAG: String = "AddToCollectionWishlistBottomSheet"
        const val REQUEST_CODE_LOGIN = 288
        const val OPEN_WISHLIST_COLLECTION = "OPEN_WISHLIST_COLLECTION"

        @JvmStatic
        fun newInstance(productId: String, source: String, isProductActive: Boolean): BottomSheetAddCollectionWishlist {
            return BottomSheetAddCollectionWishlist().apply {
                val bundle = Bundle()
                bundle.putString(PRODUCT_IDs, productId)
                bundle.putString(SOURCE, source)
                bundle.putBoolean(IS_PRODUCT_ACTIVE, isProductActive)
                arguments = bundle
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        checkLogin()
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
        isDragable = true
        isKeyboardOverlap = false
        isFullpage = false
        showCloseIcon = false
        showHeader = true
        customPeekHeight = (getScreenHeight()/2).toDp()
    }

    private fun initLayout() {
        binding = BottomsheetAddWishlistCollectionBinding.inflate(LayoutInflater.from(context), null, false)
        binding?.rvAddWishlistCollection?.adapter = collectionAdapter
        binding?.rvAddWishlistCollection?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        setChild(binding?.root)
        if (!isProductActive) {
            binding?.tickerOos?.apply {
                visible()
                closeButtonVisibility = View.GONE
                setTextDescription(getString(R.string.collection_ticker_oos))
                tickerType = Ticker.TYPE_ANNOUNCEMENT
            }
        } else {
            binding?.tickerOos?.gone()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                loadData()
            } else {
                activity?.finish()
            }
        }
    }

    private fun checkLogin() {
        if (userSession.isLoggedIn) {
            loadData()
        } else {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetBehaviorKnob(view, true)
        initObserver()
    }

    private fun loadData() {
        val productId = arguments?.get(PRODUCT_IDs).toString()
        val source = arguments?.get(SOURCE).toString()
        isProductActive = arguments?.get(IS_PRODUCT_ACTIVE) as Boolean
        val param = GetWishlistCollectionsBottomSheetParams(
            productIds = productId,
            source = source
        )
        getCollectionsViewModel.getWishlistCollections(param)
    }

    private fun initObserver() {
        observingListCollection()
        observeSavingItemToCollections()
    }

    private fun observingListCollection() {
        getCollectionsViewModel.collectionsBottomSheet.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.status == OK) {
                        val dataGetBottomSheetCollections = result.data.data
                        updateBottomSheet(dataGetBottomSheetCollections)
                        collectionAdapter.addList(mapDataCollectionsBottomSheet(dataGetBottomSheetCollections))
                    } else {
                        val errorMessage = result.data.errorMessage.first().ifEmpty { context?.getString(
                            R.string.wishlist_common_error_msg) }
                        if (errorMessage != null) {
                            toasterErrorMessage = errorMessage
                        }
                        dismiss()
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    toasterErrorMessage = errorMessage
                    dismiss()
                }
            }
        }
    }

    private fun observeSavingItemToCollections() {
        getCollectionsViewModel.saveItemToCollections.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.dataItem.success) {
                        actionListener?.onSuccessSaveItemToCollection(result.data)
                        dismiss()
                    } else {
                        val errorMessage = if (result.data.errorMessage.isNotEmpty()) {
                            result.data.errorMessage.firstOrNull() ?: ""
                        } else if (result.data.dataItem.message.isNotEmpty()) {
                            result.data.dataItem.message
                        } else {
                            getString(R.string.wishlist_v2_common_error_msg)
                        }
                        actionListener?.onFailedSaveItemToCollection(errorMessage)
                        dismiss()
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    actionListener?.onFailedSaveItemToCollection(errorMessage)
                    dismiss()
                }
            }
        }
    }

    private fun updateBottomSheet(dataGetBottomSheetCollections: com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionsBottomSheetResponse.GetWishlistCollectionsBottomsheet.Data) {
        setTitle(dataGetBottomSheetCollections.title)
        setAction(dataGetBottomSheetCollections.titleButton.text) {
            if (dataGetBottomSheetCollections.titleButton.action == OPEN_WISHLIST_COLLECTION) {
                context?.let { c ->
                    dismiss()
                    goToWishlistPage(c) }
            }
        }
    }

    private fun mapDataCollectionsBottomSheet(data: com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionsBottomSheetResponse.GetWishlistCollectionsBottomsheet.Data): ArrayList<BottomSheetWishlistCollectionTypeLayoutData> {
        val listData = arrayListOf<BottomSheetWishlistCollectionTypeLayoutData>()
        listData.add(
            BottomSheetWishlistCollectionTypeLayoutData(
                data.mainSection.text,
                TYPE_COLLECTION_MAIN_SECTION
            )
        )

        data.mainSection.collections.forEach { mainSectionItem ->
            listData.add(
                BottomSheetWishlistCollectionTypeLayoutData(
                    mainSectionItem,
                    TYPE_COLLECTION_ITEM
                )
            )
        }

        // data.placeholder needs for layout, and need validation data for click listener
        if (data.placeholder.text.isNotEmpty()) {
            listData.add(
                BottomSheetWishlistCollectionTypeLayoutData(
                    data,
                    TYPE_CREATE_NEW_COLLECTION
                )
            )
        }

        if (data.additionalSection.text.isNotEmpty()) {
            listData.add(
                BottomSheetWishlistCollectionTypeLayoutData(
                    data.additionalSection.text,
                    TYPE_COLLECTION_ADDITIONAL_SECTION
                )
            )
        }

        if (data.additionalSection.collections.isNotEmpty()) {
            data.additionalSection.collections.forEach { additionalItem ->
                listData.add(
                    BottomSheetWishlistCollectionTypeLayoutData(
                        additionalItem,
                        TYPE_COLLECTION_ITEM
                    )
                )
            }
        }
        return listData
    }

    private fun showToaster(message: String, actionText: String, type: Int) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.build(v, message, Toaster.LENGTH_LONG, type, actionText).show()
        }
    }

    private fun initInjector() {
        component.inject(this)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun goToWishlistPage(context: Context) {
        RouteManager.route(context, ApplinkConst.NEW_WISHLIST)
    }

    override fun getComponent(): com.tokopedia.wishlistcollection.di.BottomSheetWishlistCollectionComponent {
        return DaggerBottomSheetWishlistCollectionComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    fun setActionListener(wishlistCollectionBottomSheetFragment: WishlistCollectionHostBottomSheetFragment) {
        this.actionListener = wishlistCollectionBottomSheetFragment
        collectionAdapter.setActionListener(wishlistCollectionBottomSheetFragment)
    }

    fun setActionListener(wishlistCollectionBottomSheetFragment: WishlistCollectionDetailFragment) {
        this.actionListener = wishlistCollectionBottomSheetFragment
        collectionAdapter.setActionListener(wishlistCollectionBottomSheetFragment)
    }

    fun saveToCollection(addWishlistParam: AddWishlistCollectionsHostBottomSheetParams) {
        getCollectionsViewModel.saveToWishlistCollection(addWishlistParam)
    }
}