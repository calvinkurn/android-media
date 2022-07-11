package com.tokopedia.wishlistcollection.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.share.DataMapper
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.HOME_ENABLE_AUTO_REFRESH_WISHLIST
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.data.model.WishlistV2BulkRemoveAdditionalParams
import com.tokopedia.wishlist.data.model.WishlistV2UiModel
import com.tokopedia.wishlist.data.model.response.DeleteWishlistProgressV2Response
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.databinding.FragmentWishlistCollectionDetailBinding
import com.tokopedia.wishlist.util.WishlistV2Analytics
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_GRID
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_GRID_INT
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LIST_INT
import com.tokopedia.wishlist.util.WishlistV2LayoutPreference
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter
import com.tokopedia.wishlist.view.adapter.WishlistV2CleanerBottomSheetAdapter
import com.tokopedia.wishlist.view.adapter.WishlistV2FilterBottomSheetAdapter
import com.tokopedia.wishlist.view.adapter.WishlistV2ThreeDotsMenuBottomSheetAdapter
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2CleanerBottomSheet
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2FilterBottomSheet
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2ThreeDotsMenuBottomSheet
import com.tokopedia.wishlistcollection.data.params.GetWishlistCollectionItemsParams
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.di.DaggerWishlistCollectionDetailComponent
import com.tokopedia.wishlistcollection.di.WishlistCollectionDetailModule
import com.tokopedia.wishlistcollection.view.viewmodel.WishlistCollectionDetailViewModel
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.math.roundToInt
import com.tokopedia.wishlist.R as Rv2

@Keep
class WishlistCollectionDetailFragment : BaseDaggerFragment(), WishlistV2Adapter.ActionListener {
    private var binding by autoClearedNullable<FragmentWishlistCollectionDetailBinding>()
    private lateinit var collectionItemsAdapter: WishlistV2Adapter
    private lateinit var rvScrollListener: EndlessRecyclerViewScrollListener
    private var paramGetCollectionItems = GetWishlistCollectionItemsParams()
    private var onLoadMore = false
    private var isFetchRecommendation = false
    private var currPage = 1
    private var currRecommendationListPage = 1
    private var searchQuery = ""
    private var activityWishlistV2 = ""
    private var isBulkDeleteShow = false
    private var listBulkDelete = arrayListOf<String>()
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private lateinit var firebaseRemoteConfig : FirebaseRemoteConfigImpl
    private lateinit var trackingQueue: TrackingQueue
    private var wishlistItemOnAtc = WishlistV2UiModel.Item()
    private var indexOnAtc = 0
    private val listTitleCheckboxIdSelected = arrayListOf<String>()
    private var loaderDialog: LoaderDialog? = null
    private var isAutoDeletion = false
    private var bulkDeleteMode = 0
    private var bulkDeleteAdditionalParams = WishlistV2BulkRemoveAdditionalParams()
    private var listExcludedBulkDelete = arrayListOf<Long>()
    private var countRemovableAutomaticDelete = 0
    private var hitCountDeletion = false
    private val handler = Handler(Looper.getMainLooper())
    private var userAddressData: LocalCacheModel? = null
    private var isOnProgressDeleteWishlist = false
    private val progressDeletionRunnable = Runnable {
        getCountDeletionProgress()
    }
    private var collectionId = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val wishlistCollectionDetailViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[WishlistCollectionDetailViewModel::class.java]
    }

    private val userSession: UserSessionInterface by lazy { UserSession(activity) }
    private val wishlistPref: WishlistV2LayoutPreference? by lazy {
        activity?.let { WishlistV2LayoutPreference(it) }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let { activity ->
            DaggerWishlistCollectionDetailComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .wishlistCollectionDetailModule(WishlistCollectionDetailModule(activity))
                .build()
                .inject(this)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): WishlistCollectionDetailFragment {
            return WishlistCollectionDetailFragment().apply {
                arguments = bundle.apply {
                    putString(
                        ApplinkConstInternalPurchasePlatform.PATH_COLLECTION_ID, this.getString(
                            ApplinkConstInternalPurchasePlatform.PATH_COLLECTION_ID
                        ))
                }
            }
        }

        const val REQUEST_CODE_LOGIN = 288
        private const val PARAM_ACTIVITY_WISHLIST_V2 = "activity_wishlist_v2"
        const val PARAM_HOME = "home"
        const val SHARE_LINK_PRODUCT = "SHARE_LINK_PRODUCT"
        const val DELETE_WISHLIST = "DELETE_WISHLIST"
        const val ATC_WISHLIST = "ADD_TO_CART"
        const val CTA_ATC = "Lihat"
        const val CTA_RESET = "Reset"

        private const val FILTER_SORT = "sort"
        private const val FILTER_OFFERS = "offers"
        private const val FILTER_STOCK = "stock"
        private const val FILTER_CATEGORIES = "categories"
        private const val FILTER_SORT_LABEL = "Urutkan"
        private const val FILTER_OFFERS_LABEL = "Penawaran"
        private const val FILTER_STOCK_LABEL = "Stok"
        private const val FILTER_CATEGORIES_LABEL = "Kategori"
        private const val OPTION_ID_SORT_OLDEST = "6"
        private const val SOURCE_AUTOMATIC_DELETION = "wishlist_automatic_delete"
        private const val OK = "OK"
        private const val DELAY_REFETCH_PROGRESS_DELETION = 5000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLogin()
        initTrackingQueue()
        collectionId = arguments?.getString(ApplinkConstInternalPurchasePlatform.PATH_COLLECTION_ID) ?: ""
        paramGetCollectionItems.collectionId = collectionId
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        launchAutoRefresh(isVisibleToUser)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWishlistCollectionDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareLayout()
        observingData()
    }

    private fun isAutoRefreshEnabled(): Boolean {
        return try {
            return getFirebaseRemoteConfig()?.getBoolean(HOME_ENABLE_AUTO_REFRESH_WISHLIST)?:false
        } catch (e: Exception) {
            false
        }
    }

    private fun getFirebaseRemoteConfig(): FirebaseRemoteConfigImpl? {
        if (!::firebaseRemoteConfig.isInitialized) {
            context?.let {
                firebaseRemoteConfig = FirebaseRemoteConfigImpl(context)
                return firebaseRemoteConfig
            }
            return null
        } else {
            return firebaseRemoteConfig
        }
    }

    private fun launchAutoRefresh(isVisibleToUser: Boolean = true) {
        if (isVisibleToUser) {
            turnOffBulkDeleteMode()
            doResetFilter()
            binding?.run {
                rvWishlistCollectionDetail.scrollToPosition(0)
            }
        }
    }

    private fun observingData() {
        observingWishlistCollectionItems()
        observingCollectionItemsData()
    }

    private fun observingWishlistCollectionItems() {
        showLoader()
        wishlistCollectionDetailViewModel.collectionItems.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    finishRefresh()
                    result.data.let { collectionDetail ->
                        rvScrollListener.setHasNextPage(collectionDetail.hasNextPage)

                        if (collectionDetail.showDeleteProgress) {
                            if (!hitCountDeletion) {
                                hitCountDeletion = true
                                isOnProgressDeleteWishlist = true
                                getCountDeletionProgress()
                            }
                            hideTotalLabel()
                            // showStickyDeletionProgress()
                        } else {
                            hideStickyDeletionProgress()
                        }

                        if (collectionDetail.totalData <= 0) {
                            if (collectionDetail.sortFilters.isEmpty() && collectionDetail.items.isEmpty()) {
                                onFailedGetWishlistV2(ResponseErrorException())
                            } else {
                                hideLoader(collectionDetail. showDeleteProgress)
                                showRvWishlist()
                                isFetchRecommendation = true
                                hideTotalLabel()
                                hideSortFilter(collectionDetail.sortFilters)
                            }
                        } else {
                            hideLoader(collectionDetail.showDeleteProgress)
                            showRvWishlist()
                            if (!collectionDetail.showDeleteProgress) updateTotalLabel(collectionDetail.totalData)
                        }

                        if (currPage == 1 && collectionDetail.sortFilters.isNotEmpty()) {
                            renderChipsFilter(mapToSortFilterItem(collectionDetail.sortFilters))
                        }
                        if (collectionDetail.hasNextPage) {
                            currPage += 1
                        }

                        if (collectionDetail.errorMessage.isNotEmpty()) {
                            showToaster(collectionDetail.errorMessage, "", Toaster.TYPE_ERROR)
                        }

                        countRemovableAutomaticDelete = if (collectionDetail.countRemovableItems > 0) collectionDetail.countRemovableItems else collectionDetail.totalData
                    }
                }
                is Fail -> {
                    finishRefresh()
                    onFailedGetWishlistV2(result.throwable)
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToaster(errorMessage, "", Toaster.TYPE_ERROR)

                    val labelError = String.format(
                        getString(Rv2.string.on_error_observing_wishlist_v2_string_builder),
                        userSession.userId ?: "",
                        errorMessage,
                        result.throwable.message ?: "")
                    // log error type to newrelic
                    ServerLogger.log(Priority.P2, "WISHLIST_V2_ERROR", mapOf("type" to labelError))
                    // log to crashlytics
                    logToCrashlytics(labelError, result.throwable)
                }
            }
        }
    }

    private fun observingWishlistV2() {
        showLoader()
        wishlistCollectionDetailViewModel.collectionItems.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    finishRefresh()
                    result.data.let { collectionItem ->
                        rvScrollListener.setHasNextPage(collectionItem.hasNextPage)

                        if (collectionItem.showDeleteProgress) {
                            if (!hitCountDeletion) {
                                hitCountDeletion = true
                                isOnProgressDeleteWishlist = true
                                getCountDeletionProgress()
                            }
                            hideTotalLabel()
                            // showStickyDeletionProgress()
                        } else {
                            hideStickyDeletionProgress()
                        }

                        if (collectionItem.totalData <= 0) {
                            if (collectionItem.sortFilters.isEmpty() && collectionItem.items.isEmpty()) {
                                onFailedGetWishlistV2(ResponseErrorException())
                            } else {
                                hideLoader(collectionItem.showDeleteProgress)
                                showRvWishlist()
                                isFetchRecommendation = true
                                hideTotalLabel()
                                hideSortFilter(collectionItem.sortFilters)
                            }
                        } else {
                            hideLoader(collectionItem.showDeleteProgress)
                            showRvWishlist()
                            if (!collectionItem.showDeleteProgress) updateTotalLabel(collectionItem.totalData)
                        }

                        if (currPage == 1 && collectionItem.sortFilters.isNotEmpty()) {
                            renderChipsFilter(mapToSortFilterItem(collectionItem.sortFilters))
                        }
                        if (collectionItem.hasNextPage) {
                            currPage += 1
                        }

                        if (collectionItem.errorMessage.isNotEmpty()) {
                            showToaster(collectionItem.errorMessage, "", Toaster.TYPE_ERROR)
                        }

                        countRemovableAutomaticDelete = if (collectionItem.countRemovableItems > 0) collectionItem.countRemovableItems else collectionItem.totalData
                    }
                }
                is Fail -> {
                    finishRefresh()
                    onFailedGetWishlistV2(result.throwable)
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToaster(errorMessage, "", Toaster.TYPE_ERROR)

                    val labelError = String.format(
                            getString(Rv2.string.on_error_observing_wishlist_v2_string_builder),
                            userSession.userId ?: "",
                            errorMessage,
                            result.throwable.message ?: "")
                    // log error type to newrelic
                    ServerLogger.log(Priority.P2, "WISHLIST_V2_ERROR", mapOf("type" to labelError))
                    // log to crashlytics
                    logToCrashlytics(labelError, result.throwable)
                }
            }
        }
    }

    private fun showRvWishlist() {
        binding?.run {
            globalErrorWishlistCollectionDetail.gone()
            emptyStateGlobalWishlistCollectionDetail.gone()
            rvWishlistCollectionDetail.show()
        }
    }

    private fun onFailedGetWishlistV2(throwable: Throwable) {
        val errorType = when (throwable) {
            is MessageErrorException -> null
            is SocketTimeoutException, is UnknownHostException -> GlobalError.NO_CONNECTION
            else -> GlobalError.SERVER_ERROR
        }
        if (errorType == null) {
            binding?.run {
                wishlistCollectionDetailLoaderLayout.root.gone()
                rvWishlistCollectionDetail.gone()
                globalErrorWishlistCollectionDetail.gone()
                emptyStateGlobalWishlistCollectionDetail.apply {
                    visible()
                    showMessageExceptionError(throwable)
                }
            }
        } else {
            binding?.run {
                wishlistCollectionDetailLoaderLayout.root.gone()
                rvWishlistCollectionDetail.gone()
                emptyStateGlobalWishlistCollectionDetail.gone()
                clWishlistCollectionDetailHeader.gone()
                hideTotalLabel()
                globalErrorWishlistCollectionDetail.visible()
                globalErrorWishlistCollectionDetail.setType(errorType)
                globalErrorWishlistCollectionDetail.setActionClickListener {
                    setRefreshing()
                }
            }
        }
    }

    private fun EmptyStateUnify.showMessageExceptionError(throwable: Throwable) {
        var errorMessage = context?.let {
            ErrorHandler.getErrorMessage(it, throwable)
        } ?: ""
        if (errorMessage.isEmpty()) errorMessage = getString(Rv2.string.wishlist_v2_failed_to_get_information)
        setDescription(errorMessage)
    }

    private fun observingCollectionItemsData() {
        wishlistCollectionDetailViewModel.collectionData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    finishRefresh()
                    result.data.let { listData ->
                        if (!onLoadMore) {
                            collectionItemsAdapter.addList(listData)
                            rvScrollListener.updateStateAfterGetData()
                        } else {
                            collectionItemsAdapter.appendList(listData)
                            rvScrollListener.updateStateAfterGetData()
                        }
                    }
                }
                is Fail -> {
                    finishRefresh()
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToaster(errorMessage, "", Toaster.TYPE_ERROR)

                    val labelError = String.format(
                            getString(Rv2.string.on_error_observing_wishlist_data_string_builder),
                            userSession.userId ?: "",
                            errorMessage,
                            result.throwable.message ?: "")
                    // log error type to newrelic
                    ServerLogger.log(Priority.P2, "WISHLIST_V2_ERROR", mapOf("type" to labelError))
                    // log to crashlytics
                    logToCrashlytics(labelError, result.throwable)
                }
            }
        }
    }

    private fun getBaseAppComponent(): BaseAppComponent {
        return (activity?.application as BaseMainApplication).baseAppComponent
    }

    private fun fetchUserLatestAddressData() {
        context?.let {
            userAddressData = ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    private fun prepareLayout() {
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        }
        setSwipeRefreshLayout()
        binding?.run {
            activityWishlistV2 = arguments?.getString(PARAM_ACTIVITY_WISHLIST_V2, "") as String

            viewLifecycleOwner.lifecycle.addObserver(wishlistCollectionDetailNavtoolbar)
            wishlistCollectionDetailNavtoolbar.setupSearchbar(searchbarType = NavToolbar.Companion.SearchBarType.TYPE_EDITABLE, hints = arrayListOf(
                    HintData(getString(Rv2.string.hint_cari_wishlist) )),
                    editorActionCallback = { query ->
                        searchQuery = query
                        if (query.isNotEmpty()) {
                            WishlistV2Analytics.submitSearchFromCariProduk(query)
                        }
                        wishlistCollectionDetailNavtoolbar.hideKeyboard()
                        triggerSearch()
                    }
            )
            val pageSource: String
            val icons: IconBuilder
            if(activityWishlistV2 != PARAM_HOME) {
                wishlistCollectionDetailNavtoolbar.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
                icons = IconBuilder(IconBuilderFlag()).apply {
                    addIcon(IconList.ID_CART) {}
                    addIcon(IconList.ID_NAV_GLOBAL) {}
                }
            } else {
                pageSource = ApplinkConsInternalNavigation.SOURCE_HOME_WISHLIST_V2
                wishlistCollectionDetailNavtoolbar.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_NONE)
                icons = IconBuilder(IconBuilderFlag(pageSource = pageSource)).apply {
                    addIcon(IconList.ID_MESSAGE) {}
                    addIcon(IconList.ID_NOTIFICATION) {}
                    addIcon(IconList.ID_CART) {}
                    addIcon(IconList.ID_NAV_GLOBAL) {}
                }
            }
            wishlistCollectionDetailNavtoolbar.setIcon(icons)
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailManageLabel.setOnClickListener {
                onStickyManageClicked() }
            wishlistCollectionDetailFb.circleMainMenu.setOnClickListener {
                rvWishlistCollectionDetail.smoothScrollToPosition(0)
            }
            wishlistCollectionDetailFb.gone()
            setTypeLayoutIcon()
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailTypeLayoutIcon.setOnClickListener {
                changeTypeLayout()
                setTypeLayoutIcon()
            }
        }

        collectionItemsAdapter = WishlistV2Adapter().apply {
            setActionListener(this@WishlistCollectionDetailFragment)
        }
        addEndlessScrollListener()
    }

    private fun setSwipeRefreshLayout() {
        binding?.run {
            swipeRefreshLayout.isEnabled = true
            swipeRefreshLayout.setOnRefreshListener {
                setRefreshing()
            }
        }
    }

    private fun changeTypeLayout() {
        if (wishlistPref?.getTypeLayout() == TYPE_LIST) {
            wishlistPref?.setTypeLayout(TYPE_GRID_INT)
            WishlistV2Analytics.clickLayoutSettings(TYPE_GRID)
        } else {
            wishlistPref?.setTypeLayout(TYPE_LIST_INT)
            WishlistV2Analytics.clickLayoutSettings(TYPE_LIST)
        }
        collectionItemsAdapter.changeTypeLayout(wishlistPref?.getTypeLayout())
    }

    private fun setTypeLayoutIcon() {
        binding?.run {
            if (wishlistPref?.getTypeLayout() == TYPE_LIST) {
                wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailTypeLayoutIcon.setImage(IconUnify.VIEW_LIST)
            } else if (wishlistPref?.getTypeLayout() == TYPE_GRID) {
                wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailTypeLayoutIcon.setImage(IconUnify.VIEW_GRID)
            }
        }
    }

    private fun setRefreshing() {
        isBulkDeleteShow = false
        listBulkDelete.clear()
        listExcludedBulkDelete.clear()
        collectionItemsAdapter.hideCheckbox()
        countRemovableAutomaticDelete = 0
        doRefresh()

        binding?.run {
            containerDeleteCollectionDetail.gone()
            clWishlistCollectionDetailHeader.visible()
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailManageLabel.text = getString(Rv2.string.wishlist_manage_label)
        }
    }

    private fun disableSwipeRefreshLayout() {
        binding?.run {
            swipeRefreshLayout.isEnabled = false
        }
    }

    private fun addEndlessScrollListener() {
        val staggeredGlm = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        rvScrollListener = object : EndlessRecyclerViewScrollListener(staggeredGlm) {

            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                currentPage += 1
                onLoadMore = true
                if (isFetchRecommendation) {
                    loadRecommendationList()
                } else {
                    paramGetCollectionItems.page = currPage
                    getCollectionItems()
                }
            }

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)
                if (dy != 0) {
                    binding?.run {
                        wishlistCollectionDetailNavtoolbar.clearFocus()
                        wishlistCollectionDetailNavtoolbar.hideKeyboard()
                        topLayoutShadow.visible()

                        if (dy > 0) {
                            wishlistCollectionDetailFb.gone()
                        } else {
                            wishlistCollectionDetailFb.visible()
                        }
                    }
                }

                var firstVisibleItems: IntArray? = null
                firstVisibleItems = (layoutManager as StaggeredGridLayoutManager).findFirstVisibleItemPositions(firstVisibleItems)
                if (firstVisibleItems != null && firstVisibleItems.isNotEmpty()) {
                    if (firstVisibleItems[0] == 0) {
                        binding?.run {
                            topLayoutShadow.gone()
                            wishlistCollectionDetailFb.gone()
                        }
                    }
                }
            }
        }

        binding?.run {
            rvWishlistCollectionDetail.apply {
                layoutManager = staggeredGlm
                adapter = collectionItemsAdapter
                addOnScrollListener(rvScrollListener)
                itemAnimator = null
            }
        }
    }

    private fun loadRecommendationList() {
        currRecommendationListPage += 1
        wishlistCollectionDetailViewModel.loadRecommendation(currRecommendationListPage)
    }

    private fun checkLogin() {
        if (userSession.isLoggedIn) {
            getCollectionItems()
        } else {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
        }
    }

    private fun initTrackingQueue() {
        activity?.let {
            trackingQueue = TrackingQueue(it)
        }
    }

    private fun getCollectionItems() {
        fetchUserLatestAddressData()
        userAddressData?.let { address ->
            paramGetCollectionItems.wishlistChosenAddress = GetWishlistCollectionItemsParams.WishlistChosenAddress(
                districtId = address.district_id,
                cityId = address.city_id,
                latitude = address.lat,
                longitude = address.long,
                postalCode = address.postal_code,
                addressId = address.address_id)
        }
        var inCollection = ""
        if (collectionId != "0") {
            inCollection = "inside"
        }
        paramGetCollectionItems.inCollection = inCollection
        paramGetCollectionItems.page = currPage
        wishlistCollectionDetailViewModel.getWishlistCollectionItems(paramGetCollectionItems, wishlistPref?.getTypeLayout(),
            paramGetCollectionItems.source == SOURCE_AUTOMATIC_DELETION
        )
    }

    private fun triggerSearch() {
        paramGetCollectionItems.query = searchQuery
        listBulkDelete.clear()
        listExcludedBulkDelete.clear()
        if (isBulkDeleteShow) setDefaultLabelDeleteButton()
        doRefresh()
        rvScrollListener.resetState()
    }

    /*private fun observingDeleteWishlistV2() {
        wishlistCollectionDetailViewModel.deleteWishlistV2Result.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    result.data.let { wishlistRemoveV2 ->
                        context?.let { context ->
                            view?.let { v ->
                                AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(wishlistRemoveV2, context, v)
                            }
                            setRefreshing()
                        }
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToaster(errorMessage, "", Toaster.TYPE_ERROR)

                    val labelError = String.format(
                            getString(Rv2.string.on_error_observing_delete_wishlist_v2_string_builder),
                            userSession.userId ?: "",
                            errorMessage,
                            result.throwable.message ?: "")
                    // log error type to newrelic
                    ServerLogger.log(Priority.P2, "WISHLIST_V2_ERROR", mapOf("type" to labelError))
                    // log to crashlytics
                    logToCrashlytics(labelError, result.throwable)
                }
            }
        }
    }*/

    /*private fun observingBulkDeleteWishlistV2() {
        wishlistCollectionDetailViewModel.bulkDeleteWishlistV2Result.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    result.data.let { bulkDeleteWishlistV2 ->
                        if (bulkDeleteWishlistV2.success) {
                            if (bulkDeleteMode == 0) {
                                // normal bulk delete
                                val listId = bulkDeleteWishlistV2.id.replace("[", "").replace("]", "").split(",").toList()
                                var msg = getString(Rv2.string.wishlist_v2_bulk_delete_msg_toaster, listId.size)
                                if (bulkDeleteWishlistV2.message.isNotEmpty()) {
                                    msg = bulkDeleteWishlistV2.message
                                }

                                var btnText = getString(Rv2.string.wishlist_oke_label)
                                if (bulkDeleteWishlistV2.button.text.isNotEmpty()) {
                                    btnText = bulkDeleteWishlistV2.button.text
                                }

                                showToaster(msg, btnText, Toaster.TYPE_NORMAL)
                                setRefreshing()
                            } else {
                                // bulkDeleteMode == 1 (manual choose via cleaner bottomsheet)
                                // bulkDeleteMode == 2 (choose automatic deletion)
                                turnOffBulkDeleteCleanMode()
                                hideTotalLabel()
                                binding?.run { rvWishlistCollectionDetail.scrollToPosition(0) }
                            }
                            setSwipeRefreshLayout()

                        } else {
                            var errorMessage = context?.getString(Rv2.string.wishlist_v2_common_error_msg)
                            if (bulkDeleteWishlistV2.message.isNotEmpty()) errorMessage = bulkDeleteWishlistV2.message
                            errorMessage?.let { showToaster(it, "", Toaster.TYPE_ERROR) }
                        }
                    }
                }
                is Fail -> {
                    finishDeletionWidget(DeleteWishlistProgressV2Response.Data.DeleteWishlistProgress.DataDeleteWishlistProgress())
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToaster(errorMessage, "", Toaster.TYPE_ERROR)

                    val labelError = String.format(
                            getString(Rv2.string.on_error_observing_bulk_delete_wishlist_v2_string_builder),
                            userSession.userId ?: "",
                            errorMessage,
                            result.throwable.message ?: "")
                    // log error type to newrelic
                    ServerLogger.log(Priority.P2, "WISHLIST_V2_ERROR", mapOf("type" to labelError))
                    // log to crashlytics
                    logToCrashlytics(labelError, result.throwable)
                }
            }
        }
    }*/

    private fun getCountDeletionProgress() {
        // wishlistCollectionDetailViewModel.getCountDeletionWishlistV2()
    }

    override fun onPause() {
        stopProgressDeletionHandler()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        checkProgressDeletion()
    }

    private fun checkProgressDeletion() {
        if (isOnProgressDeleteWishlist) {
            getCountDeletionProgress()
        }
    }

    private fun stopProgressDeletionHandler() {
        handler.removeCallbacks(progressDeletionRunnable)
    }

    /*private fun observingCountDeletion() {
        if (wishlistCollectionDetailAdapter.getCountData() > 0) {
            wishlistCollectionDetailViewModel.countDeletionWishlistV2.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Success -> {
                        if (result.data.status == OK) {
                            val data = result.data.data
                            if (data.success) {
                                if (data.successfullyRemovedItems >= data.totalItems) {
                                    finishDeletionWidget(data)
                                } else {
                                    updateDeletionWidget(data)
                                    handler.postDelayed(progressDeletionRunnable, DELAY_REFETCH_PROGRESS_DELETION)
                                }
                            } else {
                                stopDeletionAndShowToasterError(data.toasterMessage)
                            }
                        } else {
                            if (result.data.errorMessage.isNotEmpty()) {
                                stopDeletionAndShowToasterError(result.data.errorMessage[0])
                            }
                        }
                    }
                    is Fail -> {
                        val errorMessage = getString(Rv2.string.wishlist_v2_common_error_msg)
                        stopDeletionAndShowToasterError(errorMessage)
                    }
                }
            }
        }
    }*/

    private fun stopDeletionAndShowToasterError(message: String) {
        showToaster(message, "", Toaster.TYPE_ERROR)
        finishDeletionWidget(DeleteWishlistProgressV2Response.Data.DeleteWishlistProgress.DataDeleteWishlistProgress())
        doRefresh()
    }

    private fun finishDeletionWidget(data: DeleteWishlistProgressV2Response.Data.DeleteWishlistProgress.DataDeleteWishlistProgress) {
        isOnProgressDeleteWishlist = false
        stopProgressDeletionHandler()
        // wishlistCollectionDetailViewModel.countDeletionWishlistV2.removeObservers(this)
        if (data.totalItems > 0 && data.toasterMessage.isNotEmpty()) {
            val finishData = DeleteWishlistProgressV2Response.Data.DeleteWishlistProgress.DataDeleteWishlistProgress(
                totalItems = data.totalItems,
                successfullyRemovedItems = data.totalItems,
                message = data.message,
                tickerColor = data.tickerColor,
                success = data.success,
                toasterMessage = data.toasterMessage
            )
            updateDeletionWidget(finishData)
            showToaster(data.toasterMessage, "", Toaster.TYPE_NORMAL)
        }
        hideStickyDeletionProgress()
        doRefresh()
    }

    private fun updateDeletionWidget(progressData: DeleteWishlistProgressV2Response.Data.DeleteWishlistProgress.DataDeleteWishlistProgress) {
        var message = getString(Rv2.string.wishlist_v2_default_message_deletion_progress)
        if (progressData.message.isNotEmpty()) message = progressData.message

        val percentage = progressData.successfullyRemovedItems.toDouble()/progressData.totalItems
        val indicatorProgressBar = percentage*100

        binding?.run {
            wishlistCollectionDetailStickyCountManageLabel.cardWishlistCollectionDetailStickyDeletion.cardType = CardUnify2.TYPE_SHADOW
            wishlistCollectionDetailStickyCountManageLabel.rlWishlistCollectionDetailStickyProgressDeletionWidget.visible()
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailCountDeletionMessage.text = message
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailCountDeletionProgressbar.setValue(indicatorProgressBar.roundToInt(), true)
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailLabelProgressBar.text = "${progressData.successfullyRemovedItems}/${progressData.totalItems}"
        }
    }

    /*private fun observingAtc() {
        wishlistCollectionDetailViewModel.atcResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    hideLoadingDialog()
                    if (it.data.isStatusError()) {
                        val atcErrorMessage = it.data.getAtcErrorMessage()
                        if (atcErrorMessage != null) {
                            showToaster(atcErrorMessage, "", Toaster.TYPE_ERROR)
                        } else {
                            context?.getString(Rv2.string.wishlist_v2_common_error_msg)?.let { errorDefaultMsg -> showToaster(errorDefaultMsg, "", Toaster.TYPE_ERROR) }
                        }
                    } else {
                        val successMsg = StringUtils.convertListToStringDelimiter(it.data.data.message, ",")
                        showToasterAtc(successMsg, Toaster.TYPE_NORMAL)
                        WishlistV2Analytics.clickAtcOnWishlist(wishlistItemOnAtc, userSession.userId, indexOnAtc, it.data.data.cartId)
                    }
                }
                is Fail -> {
                    hideLoadingDialog()
                    context?.also { ctx ->
                        val throwable = it.throwable
                        var errorMessage = if (throwable is ResponseErrorException) {
                            throwable.message ?: ""
                        } else {
                            ErrorHandler.getErrorMessage(ctx, throwable, ErrorHandler.Builder().withErrorCode(false))
                        }
                        if (errorMessage.isBlank()) {
                            errorMessage = ctx.getString(Rv2.string.wishlist_v2_common_error_msg)
                        }
                        showToaster(errorMessage, "", Toaster.TYPE_ERROR)

                        val labelError = String.format(
                                getString(Rv2.string.on_error_observing_atc_string_builder),
                                userSession.userId ?: "",
                                errorMessage,
                                throwable.message ?: "")
                        // log error type to newrelic
                        ServerLogger.log(Priority.P2, "WISHLIST_V2_ERROR", mapOf("type" to labelError))
                        // log to crashlytics
                        logToCrashlytics(labelError, throwable)
                    }
                }
            }
        }
    }*/

    private fun updateTotalLabel(totalData: Int) {
        binding?.run {
            wishlistCollectionDetailStickyCountManageLabel.rlWishlistCollectionDetailManage.visible()
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailCountLabel.text = "$totalData"
        }
    }

    private fun hideTotalLabel() {
        binding?.run {
            wishlistCollectionDetailStickyCountManageLabel.rlWishlistCollectionDetailManage.gone()
        }
    }

    private fun hideStickyDeletionProgress() {
        binding?.run {
            wishlistCollectionDetailStickyCountManageLabel.rlWishlistCollectionDetailStickyProgressDeletionWidget.gone()
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailCountDeletionMessage.text = ""
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailCountDeletionProgressbar.setValue(0)
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailLabelProgressBar.text = "0/0"
        }
    }

    private fun hideSortFilter(sortFilters: List<GetWishlistCollectionItemsResponse.Data.GetWishlistCollectionItems.SortFiltersItem>) {
        var isFilterActive = false
        sortFilters.forEach { filterItem ->
            if (filterItem.isActive) isFilterActive = true
        }
        if (!isFilterActive) {
            binding?.run {
                clWishlistCollectionDetailHeader.gone()
            }
        }
    }

    private fun showLoader() {
        collectionItemsAdapter.showLoader(wishlistPref?.getTypeLayout())
        binding?.run {
            wishlistCollectionDetailSortFilter.gone()
            wishlistCollectionDetailStickyCountManageLabel.rlWishlistCollectionDetailManage.gone()
            wishlistCollectionDetailLoaderLayout.root.visible()
        }
    }

    private fun hideLoader(showDeleteProgress: Boolean) {
        binding?.run {
            wishlistCollectionDetailLoaderLayout.root.gone()
            wishlistCollectionDetailSortFilter.visible()
            if (!showDeleteProgress) wishlistCollectionDetailStickyCountManageLabel.rlWishlistCollectionDetailManage.visible()
        }
    }

    private fun mapToSortFilterItem(sortFilters: List<GetWishlistCollectionItemsResponse.Data.GetWishlistCollectionItems.SortFiltersItem>): List<WishlistV2Response.Data.WishlistV2.SortFiltersItem> {
        val arrayListNewSortItem = arrayListOf<WishlistV2Response.Data.WishlistV2.SortFiltersItem>()
        sortFilters.forEach { item ->
            val newListOptionsItem = arrayListOf<WishlistV2Response.Data.WishlistV2.SortFiltersItem.OptionsItem>()
            item.options.forEach { optionsItem ->
                val newOptionsItem = WishlistV2Response.Data.WishlistV2.SortFiltersItem.OptionsItem(
                    isSelected = optionsItem.isSelected,
                    description = optionsItem.description,
                    optionId = optionsItem.optionId,
                    text = optionsItem.text
                )
                newListOptionsItem.add(newOptionsItem)
            }

            val newItem = WishlistV2Response.Data.WishlistV2.SortFiltersItem(
                selectionType = item.selectionType,
                isActive = item.isActive,
                name = item.name,
                id = item.id,
                text = item.text,
                options = newListOptionsItem
            )
            arrayListNewSortItem.add(newItem)
        }
        return arrayListNewSortItem
    }

    private fun renderChipsFilter(sortFilters: List<WishlistV2Response.Data.WishlistV2.SortFiltersItem>) {
        val chips = arrayListOf<SortFilterItem>()

        sortFilters.forEach { filterItem ->
            val typeFilter = if (filterItem.isActive) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
            val chipFilter = SortFilterItem(filterItem.text, typeFilter, ChipsUnify.SIZE_SMALL)
            chipFilter.listener = {
                showBottomSheetFilterOption(filterItem)
            }
            chipFilter.chevronListener = {
                showBottomSheetFilterOption(filterItem)
            }
            chips.add(chipFilter)
        }

        binding?.run {
            wishlistCollectionDetailSortFilter.run {
                addItem(chips)
                sortFilterPrefix.setOnClickListener {
                    resetAllFilters()
                    paramGetCollectionItems = GetWishlistCollectionItemsParams()
                    if (searchQuery.isNotEmpty()) paramGetCollectionItems.query = searchQuery
                    doRefresh()
                    WishlistV2Analytics.clickXChipsToClearFilter()
                }
            }
        }
    }

    private fun setTitleBottomSheet(name: String): String {
        var title = ""
        when (name) {
            FILTER_SORT -> {
                title = FILTER_SORT_LABEL
            }
            FILTER_OFFERS -> {
                title = FILTER_OFFERS_LABEL
            }
            FILTER_STOCK -> {
                title = FILTER_STOCK_LABEL
            }
            FILTER_CATEGORIES -> {
                title = FILTER_CATEGORIES_LABEL
            }
        }
        return title
    }

    private fun showBottomSheetFilterOption(filterItem: WishlistV2Response.Data.WishlistV2.SortFiltersItem) {
        val filterBottomSheet = WishlistV2FilterBottomSheet.newInstance(setTitleBottomSheet(filterItem.name), filterItem.selectionType)
        if (filterBottomSheet.isAdded || childFragmentManager.isStateSaved) return

        val filterBottomSheetAdapter = WishlistV2FilterBottomSheetAdapter()
        filterBottomSheetAdapter.filterItem = filterItem
        var listOptionIdSelected = mutableListOf<String>()
        var nameSelected = ""

        filterBottomSheet.setAdapter(filterBottomSheetAdapter)

        if (filterItem.isActive) {
            if (filterItem.name == FILTER_OFFERS) {
                filterBottomSheet.setAction(CTA_RESET) {
                    filterBottomSheetAdapter.isResetCheckbox = true
                    filterBottomSheetAdapter.notifyDataSetChanged()
                    filterBottomSheet.showButtonSave()
                    listOptionIdSelected.clear()
                    listTitleCheckboxIdSelected.clear()
                }
            } else {
                filterBottomSheet.setAction(CTA_RESET) {
                    filterBottomSheet.dismiss()
                    removeFilter(filterItem)
                }
            }
        }

        filterBottomSheet.setListener(object : WishlistV2FilterBottomSheet.BottomSheetListener{
            override fun onRadioButtonSelected(name: String, optionId: String, label: String) {
                filterBottomSheet.dismiss()
                paramGetCollectionItems.sortFilters.removeAll { it.name == name }
                paramGetCollectionItems.sortFilters.add(
                    GetWishlistCollectionItemsParams.WishlistSortFilterParam(
                        name = filterItem.name, selected = arrayListOf(optionId)))
                doRefresh()
                hitAnalyticsFilterOptionSelected(name, label)
            }

            override fun onCheckboxSelected(name: String, optionId: String, isChecked: Boolean, titleCheckbox: String) {
                paramGetCollectionItems.sortFilters.forEach { sortFilterParam ->
                    if (sortFilterParam.name == FILTER_OFFERS) {
                        listOptionIdSelected = sortFilterParam.selected
                        nameSelected = FILTER_OFFERS
                    }
                }
                if (isChecked) {
                    nameSelected = name
                    if (!listOptionIdSelected.contains(optionId)) {
                        listOptionIdSelected.add(optionId)
                    }
                    if (!listTitleCheckboxIdSelected.contains(titleCheckbox)) {
                        listTitleCheckboxIdSelected.add(titleCheckbox)
                    }
                } else {
                    listOptionIdSelected.remove(optionId)
                    listTitleCheckboxIdSelected.remove(titleCheckbox)
                }

                filterBottomSheet.showButtonSave()

                if (!filterItem.isActive) {
                    filterBottomSheet.bottomSheetAction.text = CTA_RESET
                    filterBottomSheet.bottomSheetAction.visible()
                    filterBottomSheet.bottomSheetAction.setOnClickListener {
                        listOptionIdSelected.clear()
                        listTitleCheckboxIdSelected.clear()
                        filterBottomSheetAdapter.isResetCheckbox = true
                        filterBottomSheetAdapter.notifyDataSetChanged()
                    }
                    filterBottomSheet.showButtonSave()
                }
            }

            override fun onSaveCheckboxSelection() {
                if (listOptionIdSelected.isNotEmpty()) {
                    paramGetCollectionItems.sortFilters.removeAll { it.name == nameSelected }
                    paramGetCollectionItems.sortFilters.add(
                        GetWishlistCollectionItemsParams.WishlistSortFilterParam(
                            name = nameSelected, selected = listOptionIdSelected as ArrayList<String>))
                }

                filterBottomSheet.dismiss()
                doRefresh()
                WishlistV2Analytics.clickSimpanOnPenawaranFilterChips(listTitleCheckboxIdSelected.toString().replace("[", "").replace("]", ""))
            }
        })
        filterBottomSheet.show(childFragmentManager)

        hitAnalyticsWhenClickFilter(filterName = filterItem.name)
    }

    private fun hitAnalyticsWhenClickFilter(filterName: String) {
        when (filterName) {
            FILTER_SORT -> {
                WishlistV2Analytics.clickUrutkanFilterChips()
            }
            FILTER_CATEGORIES -> {
                WishlistV2Analytics.clickKategoriFilterChips()
            }
            FILTER_OFFERS -> {
                WishlistV2Analytics.clickPenawaranFilterChips()
            }
            FILTER_STOCK -> {
                WishlistV2Analytics.clickStokFilterChips()
            }
        }
    }

    private fun hitAnalyticsFilterOptionSelected(filterName: String, label: String) {
        when (filterName) {
            FILTER_SORT -> {
                WishlistV2Analytics.clickOptionOnUrutkanFilterChips(label)
            }
            FILTER_CATEGORIES -> {
                WishlistV2Analytics.clickOptionOnKategoriFilterChips(label)
            }
            FILTER_STOCK -> {
                WishlistV2Analytics.clickOptionOnStokFilterChips(label)
            }
        }
    }

    private fun showBottomSheetThreeDotsMenu(itemWishlist: WishlistV2UiModel.Item) {
        val bottomSheetThreeDotsMenu = WishlistV2ThreeDotsMenuBottomSheet.newInstance()
        if (bottomSheetThreeDotsMenu.isAdded || childFragmentManager.isStateSaved) return

        val threeDotsMenuBottomSheetAdapter = WishlistV2ThreeDotsMenuBottomSheetAdapter()
        threeDotsMenuBottomSheetAdapter.wishlistItem = itemWishlist

        bottomSheetThreeDotsMenu.setAdapter(threeDotsMenuBottomSheetAdapter)
        bottomSheetThreeDotsMenu.setListener(object : WishlistV2ThreeDotsMenuBottomSheet.BottomSheetListener{
            override fun onThreeDotsMenuItemSelected(wishlistItem: WishlistV2UiModel.Item,
                                                     additionalItem: WishlistV2UiModel.Item.Buttons.AdditionalButtonsItem) {
                bottomSheetThreeDotsMenu.dismiss()
                if (additionalItem.url.isNotEmpty()) {
                    RouteManager.route(context, additionalItem.url)
                } else {
                    if (additionalItem.action == SHARE_LINK_PRODUCT) {
                        showShareBottomSheet(wishlistItem)
                        WishlistV2Analytics.clickShareLinkProduct(wishlistId = wishlistItem.wishlistId,
                                productId = wishlistItem.id, userId = userSession.userId)
                    } else if (additionalItem.action == DELETE_WISHLIST) {
                        // wishlistCollectionDetailViewModel.deleteWishlistV2(itemWishlist.id, userSession.userId)
                    }
                }
                WishlistV2Analytics.clickOptionOnThreeDotsMenu(additionalItem.text)
            }
        })
        bottomSheetThreeDotsMenu.show(childFragmentManager)
    }

    private fun showBottomSheetCleaner(cleanerBottomSheet: WishlistV2UiModel.StorageCleanerBottomSheet) {
        val bottomSheetCleaner = WishlistV2CleanerBottomSheet.newInstance(cleanerBottomSheet.title, cleanerBottomSheet.description, cleanerBottomSheet.btnCleanBottomSheet.text)
        if (bottomSheetCleaner.isAdded || childFragmentManager.isStateSaved) return

        val cleanerAdapter = WishlistV2CleanerBottomSheetAdapter()
        cleanerAdapter.cleanerBottomSheet = cleanerBottomSheet

        bottomSheetCleaner.setAdapter(cleanerAdapter)
        bottomSheetCleaner.setListener(object : WishlistV2CleanerBottomSheet.BottomsheetCleanerListener{
            override fun onButtonCleanerClicked(index: Int) {
                if (index == 0 || index == -1) {
                    // manual
                    isAutoDeletion = false
                    bulkDeleteMode = 1
                    bulkDeleteAdditionalParams = WishlistV2BulkRemoveAdditionalParams()
                } else if (index == 1) {
                    // auto
                    isAutoDeletion = true
                    bulkDeleteMode = 2
                    paramGetCollectionItems.source = SOURCE_AUTOMATIC_DELETION
                }
                onTickerCTASortFromLatest()
                turnOnBulkDeleteMode()
                binding?.run {
                    wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailManageLabel.text = getString(Rv2.string.wishlist_cancel_manage_label)
                }
                view?.let { Toaster.build(it, getString(Rv2.string.wishlist_v2_terlama_disimpan),
                    Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show() }
            }
        })
        bottomSheetCleaner.show(childFragmentManager)
    }

    private fun showShareBottomSheet(wishlistItem: WishlistV2UiModel.Item) {
        val shareListener = object : ShareBottomsheetListener {

            override fun onShareOptionClicked(shareModel: ShareModel) {
                val linkerShareResult = DataMapper.getLinkerShareData(LinkerData().apply {
                    type = LinkerData.PRODUCT_TYPE
                    uri = wishlistItem.url
                    id = wishlistItem.id
                    feature = shareModel.feature
                    channel = shareModel.channel
                    campaign = shareModel.campaign
                    ogTitle = "${wishlistItem.name} - ${wishlistItem.priceFmt}"
                    ogDescription = wishlistItem.shop.name
                    if (shareModel.ogImgUrl != null && shareModel.ogImgUrl?.isNotEmpty() == true) {
                        ogImageUrl = shareModel.ogImgUrl
                    }
                })

                LinkerManager.getInstance().executeShareRequest(
                        LinkerUtils.createShareRequest(0, linkerShareResult, object : ShareCallback {
                            override fun urlCreated(linkerShareResult: LinkerShareResult?) {
                                val shareString = getString(Rv2.string.wishlist_v2_share_text,
                                        wishlistItem.name, wishlistItem.priceFmt,
                                        wishlistItem.shop.name) + "\n${linkerShareResult?.url}"
                                shareModel.subjectName = userSession.shopName
                                SharingUtil.executeShareIntent(
                                        shareModel,
                                        linkerShareResult,
                                        activity,
                                        view,
                                        shareString
                                )
                                shareModel.channel?.let { ch ->
                                    WishlistV2Analytics.clickSharingChannel(wishlistId = wishlistItem.wishlistId,
                                            productId = wishlistItem.id, userId = userSession.userId, channel = ch)
                                }
                                universalShareBottomSheet?.dismiss()
                            }

                            override fun onError(linkerError: LinkerError?) {}
                        })
                )
            }

            override fun onCloseOptionClicked() {
                WishlistV2Analytics.clickCloseShareBottomSheet(wishlistId = wishlistItem.wishlistId,
                productId = wishlistItem.id, userId = userSession.userId)
            }
        }

        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(shareListener)
            setMetaData(
                    wishlistItem.name,
                    wishlistItem.imageUrl
            )
        }
        universalShareBottomSheet?.show(childFragmentManager, this@WishlistCollectionDetailFragment)
        WishlistV2Analytics.viewOnSharingChannel(wishlistId = wishlistItem.wishlistId,
                productId = wishlistItem.id, userId = userSession.userId)
    }

    private fun showToaster(message: String, actionText: String, type: Int) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.build(v, message, Toaster.LENGTH_LONG, type, actionText).show()
        }
    }

    private fun showToasterAtc(message: String, type: Int) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.build(v, message, Toaster.LENGTH_SHORT, type, CTA_ATC) {
                RouteManager.route(context, ApplinkConst.CART)
                WishlistV2Analytics.clickLihatButtonOnAtcSuccessToaster()
            }.show()
        }
    }

    override fun onCariBarangClicked() {
        RouteManager.route(context, ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE)
        WishlistV2Analytics.clickCariBarangOnEmptyStateNoItems()
    }

    override fun onNotFoundButtonClicked(keyword: String) {
        RouteManager.route(context, "${ApplinkConst.DISCOVERY_SEARCH}?q=$keyword")
        WishlistV2Analytics.clickCariDiTokopediaOnEmptyStateNoSearchResult()
    }

    override fun onProductItemClicked(wishlistItem: WishlistV2UiModel.Item, position: Int) {
        WishlistV2Analytics.clickProductCard(wishlistItem, userSession.userId, position)
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, wishlistItem.id)
            startActivity(intent)
        }
    }

    override fun onProductRecommItemClicked(recommendationItem: RecommendationItem) {
        if(recommendationItem.isTopAds) {
            TopAdsUrlHitter(context).hitClickUrl(
                this::class.java.simpleName,
                recommendationItem.clickUrl,
                recommendationItem.productId.toString(),
                recommendationItem.name,
                recommendationItem.imageUrl
            )
        }
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                recommendationItem.productId.toString())
            startActivity(intent)
        }
    }

    override fun onViewProductCard(wishlistItem: WishlistV2UiModel.Item, position: Int) {
        userSession.userId?.let { userId ->
            WishlistV2Analytics.viewProductCard(trackingQueue, wishlistItem, userId, position.toString())
        }
    }

    override fun onBannerTopAdsImpression(topAdsImageViewModel: TopAdsImageViewModel, position: Int) {
        TopAdsUrlHitter(context).hitImpressionUrl(
                this::class.java.simpleName,
                topAdsImageViewModel.adViewUrl,
                "",
                "",
                topAdsImageViewModel.imageUrl
        )
        WishlistV2Analytics.impressTopAdsBanner(userSession.userId, topAdsImageViewModel, position)
    }

    override fun onBannerTopAdsClick(topAdsImageViewModel: TopAdsImageViewModel, position: Int) {
        TopAdsUrlHitter(context).hitClickUrl(
                this::class.java.simpleName,
                topAdsImageViewModel.adClickUrl,
                "",
                "",
                topAdsImageViewModel.imageUrl
        )
        WishlistV2Analytics.clickTopAdsBanner(topAdsImageViewModel, userSession.userId, position)
        RouteManager.route(context, topAdsImageViewModel.applink)
    }

    override fun onRecommendationItemImpression(recommendationItem: RecommendationItem, position: Int) {
        if(recommendationItem.isTopAds) {
            TopAdsUrlHitter(context).hitImpressionUrl(
                    this::class.java.simpleName,
                    recommendationItem.trackerImageUrl,
                    recommendationItem.productId.toString(),
                    recommendationItem.name,
                    recommendationItem.imageUrl
            )
        }
        WishlistV2Analytics.impressionEmptyWishlistRecommendation(trackingQueue, recommendationItem, position)
    }

    override fun onRecommendationItemClick(recommendationItem: RecommendationItem, position: Int) {
        WishlistV2Analytics.clickRecommendationItem(recommendationItem, position)
        if(recommendationItem.isTopAds) {
            TopAdsUrlHitter(context).hitClickUrl(
                    this::class.java.simpleName,
                    recommendationItem.clickUrl,
                    recommendationItem.productId.toString(),
                    recommendationItem.name,
                    recommendationItem.imageUrl
            )
        }
    }

    override fun onRecommendationCarouselItemImpression(recommendationItem: RecommendationItem, position: Int) {
        if(recommendationItem.isTopAds) {
            TopAdsUrlHitter(context).hitImpressionUrl(
                    this::class.java.simpleName,
                    recommendationItem.trackerImageUrl,
                    recommendationItem.productId.toString(),
                    recommendationItem.name,
                    recommendationItem.imageUrl
            )
        }
        WishlistV2Analytics.impressionCarouselRecommendationItem(trackingQueue, recommendationItem, position)
    }

    override fun onRecommendationCarouselItemClick(recommendationItem: RecommendationItem, position: Int) {
        WishlistV2Analytics.clickCarouselRecommendationItem(recommendationItem, position)
        if(recommendationItem.isTopAds) {
            TopAdsUrlHitter(context).hitClickUrl(
                    this::class.java.simpleName,
                    recommendationItem.clickUrl,
                    recommendationItem.productId.toString(),
                    recommendationItem.name,
                    recommendationItem.imageUrl
            )
        }
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, recommendationItem.productId.toString())
            startActivity(intent)
        }
    }

    override fun onTickerCTAShowBottomSheet(bottomSheetCleanerData: WishlistV2UiModel.StorageCleanerBottomSheet) {
        showBottomSheetCleaner(bottomSheetCleanerData)
    }

    override fun onTickerCTASortFromLatest() {
        val listOptionIdSelected = mutableListOf<String>()
        listOptionIdSelected.add(OPTION_ID_SORT_OLDEST)
        paramGetCollectionItems.sortFilters.clear()
        paramGetCollectionItems.sortFilters.add(
                GetWishlistCollectionItemsParams.WishlistSortFilterParam(
                name = FILTER_SORT, selected = listOptionIdSelected as ArrayList<String>))

        doRefresh()
    }

    override fun onTickerCloseIconClicked() {
        collectionItemsAdapter.hideTicker()
    }

    override fun onThreeDotsMenuClicked(itemWishlist: WishlistV2UiModel.Item) {
        showBottomSheetThreeDotsMenu(itemWishlist)
        WishlistV2Analytics.clickThreeDotsOnProductCard()
    }

    override fun onCheckBulkDeleteOption(productId: String, isChecked: Boolean, position: Int) {
        if (isChecked) {
            listBulkDelete.add(productId)
        } else {
            listBulkDelete.remove(productId)
        }
        collectionItemsAdapter.setCheckbox(position, isChecked)
        val showButton = listBulkDelete.isNotEmpty()
        if (showButton) {
            setLabelDeleteButton()
        } else {
            setDefaultLabelDeleteButton()
        }
    }

    private fun setLabelDeleteButton() {
        binding?.run {
            containerDeleteCollectionDetail.visible()
            deleteButtonCollection.apply {
                isEnabled = true
                if (listBulkDelete.isNotEmpty()) {
                    text = getString(Rv2.string.wishlist_v2_delete_text_counter, listBulkDelete.size)
                    setOnClickListener {
                        showPopupBulkDeleteConfirmation(listBulkDelete.size)
                    }
                }
            }
        }
    }

    private fun setDefaultLabelDeleteButton() {
        binding?.run {
            containerDeleteCollectionDetail.visible()
            deleteButtonCollection.isEnabled = false
            deleteButtonCollection.text = getString(Rv2.string.wishlist_v2_delete_text)
        }
    }

    override fun onUncheckAutomatedBulkDelete(
        productId: String,
        isChecked: Boolean,
        position: Int
    ) {
        if (!isChecked) {
            listExcludedBulkDelete.add(productId.toLong())
        } else {
            listExcludedBulkDelete.remove(productId.toLong())
        }
        collectionItemsAdapter.setCheckbox(position, isChecked)
        binding?.run {
            containerDeleteCollectionDetail.visible()
            deleteButtonCollection.isEnabled = true

            val countExistingRemovable = countRemovableAutomaticDelete - listExcludedBulkDelete.size
            deleteButtonCollection.text = getString(Rv2.string.wishlist_v2_delete_text_counter, countExistingRemovable)
            deleteButtonCollection.setOnClickListener {
                bulkDeleteAdditionalParams = WishlistV2BulkRemoveAdditionalParams(listExcludedBulkDelete, countRemovableAutomaticDelete.toLong())
                showPopupBulkDeleteConfirmation(countExistingRemovable)
            }
        }
    }

    override fun onAtc(wishlistItem: WishlistV2UiModel.Item, position: Int) {
        showLoadingDialog()
        val atcParam = AddToCartRequestParams(
                productId = wishlistItem.id.toLong(),
                productName = wishlistItem.name,
                price = wishlistItem.originalPriceFmt,
                quantity = wishlistItem.minOrder.toInt(),
                shopId = wishlistItem.shop.id.toInt(),
                atcFromExternalSource = AtcFromExternalSource.ATC_FROM_WISHLIST)
        // wishlistCollectionDetailViewModel.doAtc(atcParam)
        wishlistItemOnAtc = wishlistItem
        indexOnAtc = position
    }

    override fun onCheckSimilarProduct(url: String) {
        RouteManager.route(context, url)
        WishlistV2Analytics.clickLihatBarangSerupaOnProductCard()
    }

    override fun onResetFilter() {
        doResetFilter()
        WishlistV2Analytics.clickResetFilterOnEmptyStateNoFilterResult()
    }

    private fun doResetFilter() {
        binding?.run {
            wishlistCollectionDetailSortFilter.run {
                resetAllFilters()
                paramGetCollectionItems = GetWishlistCollectionItemsParams()
                wishlistCollectionDetailNavtoolbar.clearSearchbarText()
            }
        }
        doRefresh()
    }

    private fun removeFilter(filterItem: WishlistV2Response.Data.WishlistV2.SortFiltersItem) {
        paramGetCollectionItems.sortFilters.removeAll { it.name == filterItem.name }
        doRefresh()
    }

    private fun onStickyManageClicked() {
        isAutoDeletion = false
        if (!isBulkDeleteShow) {
            turnOnBulkDeleteMode()
            binding?.run {
                wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailManageLabel.text = getString(Rv2.string.wishlist_cancel_manage_label)
            }
            WishlistV2Analytics.clickAturOnWishlist()
        } else {
            turnOffBulkDeleteMode()
        }
    }

    private fun turnOnBulkDeleteMode() {
        isBulkDeleteShow = true
        onManageClicked(showCheckbox = true)
    }

    private fun turnOffBulkDeleteMode() {
        isBulkDeleteShow = false
        onManageClicked(showCheckbox = false)
        binding?.run {
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailManageLabel.text = getString(Rv2.string.wishlist_manage_label)
        }
    }

    override fun onManageClicked(showCheckbox: Boolean) {
        if (showCheckbox) {
            disableSwipeRefreshLayout()
            listBulkDelete.clear()
            listExcludedBulkDelete.clear()
            collectionItemsAdapter.showCheckbox(isAutoDeletion)
            binding?.run {
                clWishlistCollectionDetailHeader.gone()
                wishlistCollectionDetailStickyCountManageLabel.wishlistDivider.gone()
                wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailTypeLayoutIcon.gone()
                containerDeleteCollectionDetail.visible()
                deleteButtonCollection.apply {
                    isEnabled = isAutoDeletion
                    text = if (isAutoDeletion) getString(Rv2.string.wishlist_v2_delete_text_counter, countRemovableAutomaticDelete) else getString(Rv2.string.wishlist_v2_delete_text)
                    if (isAutoDeletion) {
                        setOnClickListener {
                            bulkDeleteAdditionalParams = WishlistV2BulkRemoveAdditionalParams(listExcludedBulkDelete, countRemovableAutomaticDelete.toLong())
                            showPopupBulkDeleteConfirmation(countRemovableAutomaticDelete)
                        }
                    }
                }
            }
        } else {
            if (isAutoDeletion) {
                doResetFilter()
            } else {
                setSwipeRefreshLayout()
                collectionItemsAdapter.hideCheckbox()
                binding?.run {
                    containerDeleteCollectionDetail.gone()
                    clWishlistCollectionDetailHeader.visible()
                    wishlistCollectionDetailStickyCountManageLabel.wishlistDivider.visible()
                    wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailTypeLayoutIcon.visible()
                }
            }
        }
    }

    private fun turnOffBulkDeleteCleanMode() {
        doResetFilter()
        isBulkDeleteShow = false
        setSwipeRefreshLayout()
        collectionItemsAdapter.hideCheckbox()
        binding?.run {
            containerDeleteCollectionDetail.gone()
            clWishlistCollectionDetailHeader.visible()
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailManageLabel.text = getString(Rv2.string.wishlist_manage_label)
            wishlistCollectionDetailStickyCountManageLabel.wishlistDivider.visible()
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailTypeLayoutIcon.visible()
        }
    }

    private fun showPopupBulkDeleteConfirmation(count: Int) {
        val dialog = context?.let { DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.setTitle(getString(Rv2.string.wishlist_v2_popup_delete_bulk_title, count))
        dialog?.dialogDesc?.gone()
        dialog?.setPrimaryCTAText(getString(Rv2.string.wishlist_delete_label))
        dialog?.setPrimaryCTAClickListener {
            dialog.dismiss()
            bulkDeleteAdditionalParams.totalOverlimitItems = count.toLong()
            doBulkDelete()
        }
        dialog?.setSecondaryCTAText(getString(Rv2.string.wishlist_cancel_manage_label))
        dialog?.setSecondaryCTAClickListener {
            dialog.dismiss()
            WishlistV2Analytics.clickBatalOnPopUpMultipleWishlistProduct()
        }
        dialog?.show()
    }

    private fun doBulkDelete() {
        // wishlistCollectionDetailViewModel.bulkDeleteWishlistV2(listBulkDelete, userSession.userId, bulkDeleteMode, bulkDeleteAdditionalParams)
        WishlistV2Analytics.clickHapusOnPopUpMultipleWishlistProduct()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                getCollectionItems()
            } else {
                activity?.finish()
            }
        }
    }

    private fun doRefresh() {
        onLoadMore = false
        isFetchRecommendation = false
        currPage = 1
        currRecommendationListPage = 1
        hitCountDeletion = false
        getCollectionItems()
        refreshLayout()
    }

    private fun refreshLayout() {
        binding?.run {
            swipeRefreshLayout.isRefreshing = true
            wishlistCollectionDetailStickyCountManageLabel.wishlistDivider.visible()
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailTypeLayoutIcon.visible()
        }
        showSortFilter()
        addEndlessScrollListener()
        collectionItemsAdapter.resetTicker()
    }

    private fun showSortFilter() {
        collectionItemsAdapter.isRefreshing = true
        binding?.run {
            containerDeleteCollectionDetail.gone()
            if (isBulkDeleteShow) {
                clWishlistCollectionDetailHeader.gone()
            } else {
                clWishlistCollectionDetailHeader.visible()
            }
        }
    }

    private fun finishRefresh() {
        binding?.run {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun showLoadingDialog() {
        context?.let {
            loaderDialog = LoaderDialog(it)
            loaderDialog?.show()
        }
    }

    private fun hideLoadingDialog() {
        loaderDialog?.dialog?.dismiss()
    }

    private fun logToCrashlytics(errorMsg: String, throwable: Throwable) {
        if (!GlobalConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().recordException(Exception(errorMsg, throwable))
        } else {
            throwable.printStackTrace()
        }
    }
}