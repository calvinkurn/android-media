package com.tokopedia.wishlistcollection.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
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
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION_DETAIL_INTERNAL
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
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
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.text.currency.StringUtils
import com.tokopedia.wishlist.data.model.WishlistV2BulkRemoveAdditionalParams
import com.tokopedia.wishlist.data.model.WishlistV2UiModel
import com.tokopedia.wishlist.data.model.response.DeleteWishlistProgressResponse
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.databinding.FragmentWishlistCollectionDetailBinding
import com.tokopedia.wishlist.util.WishlistV2Analytics
import com.tokopedia.wishlist.util.WishlistV2Consts.EXTRA_TOASTER_WISHLIST_COLLECTION_DETAIL
import com.tokopedia.wishlist.util.WishlistV2Consts.MENU_ADD_ITEM_TO_COLLECTION
import com.tokopedia.wishlist.util.WishlistV2Consts.MENU_ADD_WISHLIST
import com.tokopedia.wishlist.util.WishlistV2Consts.MENU_DELETE_WISHLIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_GRID
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_GRID_INT
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LIST_INT
import com.tokopedia.wishlist.util.WishlistV2LayoutPreference
import com.tokopedia.wishlist.util.WishlistV2Utils
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter
import com.tokopedia.wishlist.view.adapter.WishlistV2CleanerBottomSheetAdapter
import com.tokopedia.wishlist.view.adapter.WishlistV2FilterBottomSheetAdapter
import com.tokopedia.wishlist.view.adapter.WishlistV2ThreeDotsMenuBottomSheetAdapter
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2CleanerBottomSheet
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2FilterBottomSheet
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2ThreeDotsMenuBottomSheet
import com.tokopedia.wishlistcollection.analytics.WishlistCollectionAnalytics
import com.tokopedia.wishlistcollection.analytics.WishlistCollectionAnalytics.sendClickShareButtonCollectionEvent
import com.tokopedia.wishlistcollection.data.params.*
import com.tokopedia.wishlistcollection.data.response.AddWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionItemsResponse
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionsBottomSheetResponse
import com.tokopedia.wishlistcollection.di.DaggerWishlistCollectionComponent
import com.tokopedia.wishlistcollection.di.WishlistCollectionModule
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.DELAY_REFETCH_PROGRESS_DELETION
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.DELAY_SHOW_COACHMARK_TOOLBAR
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.EXTRA_COLLECTION_ID_DESTINATION
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.EXTRA_COLLECTION_NAME_DESTINATION
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.EXTRA_IS_BULK_ADD
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.EXTRA_IS_SHOW_CLEANER_BOTTOMSHEET
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.SOURCE_COLLECTION
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.SRC_WISHLIST_COLLECTION
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.SRC_WISHLIST_COLLECTION_BULK_ADD
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.SRC_WISHLIST_COLLECTION_SHARING
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.TYPE_COLLECTION_PRIVATE_SELF
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.TYPE_COLLECTION_PUBLIC_OTHERS
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.TYPE_COLLECTION_PUBLIC_SELF
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.TYPE_COLLECTION_SHARE
import com.tokopedia.wishlistcollection.util.WishlistCollectionSharingUtils
import com.tokopedia.wishlistcollection.util.WishlistCollectionUtils.getStringCollectionType
import com.tokopedia.wishlistcollection.view.activity.WishlistCollectionDetailActivity
import com.tokopedia.wishlistcollection.view.activity.WishlistCollectionEditActivity
import com.tokopedia.wishlistcollection.view.adapter.BottomSheetWishlistCollectionAdapter
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetAddCollectionWishlist
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetCreateNewCollectionWishlist
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetUpdateWishlistCollectionName
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetWishlistCollectionSettings
import com.tokopedia.wishlistcollection.view.bottomsheet.listener.ActionListenerBottomSheetMenu
import com.tokopedia.wishlistcollection.view.bottomsheet.listener.ActionListenerFromPdp
import com.tokopedia.wishlistcollection.view.viewmodel.WishlistCollectionDetailViewModel
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.IS_PRODUCT_ACTIVE
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.OPEN_WISHLIST
import com.tokopedia.wishlistcommon.util.WishlistV2RemoteConfigRollenceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt
import com.tokopedia.wishlist.R as Rv2

@Keep
class WishlistCollectionDetailFragment :
    BaseDaggerFragment(),
    WishlistV2Adapter.ActionListener,
    CoroutineScope,
    BottomSheetUpdateWishlistCollectionName.ActionListener,
    BottomSheetWishlistCollectionAdapter.ActionListener,
    BottomSheetAddCollectionWishlist.ActionListener,
    ActionListenerFromPdp,
    ActionListenerBottomSheetMenu {
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
    private var toasterMessageInitial = ""
    private var newCollectionDetailTitle = ""
    private var isBulkDeleteShow = false
    private var isBulkAddShow = false
    private var isBulkAddFromOtherCollectionShow = false
    private var isShowingCleanerBottomSheet = false
    private var listSelectedProductIdsFromOtherCollection = arrayListOf<String>()
    private var listSelectedProductIds = arrayListOf<String>()
    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfigImpl
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
        getDeleteWishlistProgress()
    }
    private val showCoarchmarkRunnable = Runnable {
        showCoachMarkSharingIcon()
    }
    private var collectionId = ""
    private var collectionName = ""
    private var listSettingButtons = emptyList<GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.Setting.Button>()
    private var collectionType = 0
    private var countDelete = 1
    private var toolbarTitle = ""
    private var bottomSheetCollection = BottomSheetAddCollectionWishlist()
    private var _isDeleteOnly = false
    private var collectionIdDestination = ""
    private var collectionNameDestination = ""
    private var isAturMode = false
    private var isCTAResetOfferFilterClicked = false
    private var bottomSheetCollectionSettings = BottomSheetWishlistCollectionSettings()
    private var isToolbarHasDesc = false
    private var toolbarDesc = ""
    private val coachMarkItemSharingIcon = ArrayList<CoachMark2Item>()
    private var coachMarkSharingIcon: CoachMark2? = null
    private var _currCheckCollectionType = 0
    private var _maxBulk = 0L
    private var _toasterMaxBulk = ""
    private var _isNeedRefreshAndTurnOffBulkModeFromOthers = false
    private var _bulkModeIsAlreadyTurnedOff = false

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
            DaggerWishlistCollectionComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .wishlistCollectionModule(WishlistCollectionModule(activity))
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
                        ApplinkConstInternalPurchasePlatform.PATH_COLLECTION_ID,
                        this.getString(
                            ApplinkConstInternalPurchasePlatform.PATH_COLLECTION_ID
                        )
                    )
                    putString(
                        EXTRA_TOASTER_WISHLIST_COLLECTION_DETAIL,
                        this.getString(
                            EXTRA_TOASTER_WISHLIST_COLLECTION_DETAIL
                        )
                    )
                    putBoolean(EXTRA_IS_BULK_ADD, this.getBoolean(EXTRA_IS_BULK_ADD))
                    putString(
                        EXTRA_COLLECTION_ID_DESTINATION,
                        this.getString(
                            EXTRA_COLLECTION_ID_DESTINATION
                        )
                    )
                    putString(
                        EXTRA_COLLECTION_NAME_DESTINATION,
                        this.getString(
                            EXTRA_COLLECTION_NAME_DESTINATION
                        )
                    )
                }
            }
        }

        const val REQUEST_CODE_LOGIN = 288
        const val REQUEST_CODE_GO_TO_PDP = 788
        const val REQUEST_CODE_GO_TO_COLLECTION_DETAIL = 388
        const val REQUEST_CODE_GO_TO_SEMUA_WISHLIST = 1288
        private const val PARAM_ACTIVITY_WISHLIST_V2 = "activity_wishlist_v2"
        const val PARAM_HOME = "home"
        const val SHARE_LINK_PRODUCT = "SHARE_LINK_PRODUCT"
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
        private const val SRC_WISHLIST = "wishlist"
        private const val SRC_WISHLIST_SHARING = "sharing"
        private const val SRC_WISHLIST_PAGE = "wishlist page"
        private const val OPTION_CLEANER_MANUAL = "sendiri"
        private const val OPTION_CLEANER_AUTOMATIC = "otomatis"
        private const val TOTAL_LOADER = 5
        private const val COLLECTION_ITEMS_EMPTY = "COLLECTION_ITEMS_EMPTY"
        private const val EDIT_WISHLIST_COLLECTION_REQUEST_CODE = 1888
        private const val COACHMARK_WISHLIST_SHARING_ICON_DETAIL_PAGE = "coachmark-wishlist-sharing-icon-detail-page"
        private const val CHECK_COLLECTION_TYPE_FOR_SHOWING_PILIH_BARANG = 1
        private const val CHECK_COLLECTION_TYPE_FOR_TURN_ON_SELECT_ITEMS_MODE = 2
        private const val CHECK_COLLECTION_TYPE_FOR_DIALOG_CONFIRMATION = 3
        private const val ERROR_GENERAL_SYSTEM_FAILURE_ADD_BULK = 1L
        private const val ERROR_MAX_QTY_FAILURE_ADD_BULK = 2L
        private const val ERROR_PARTIAL_MAX_QTY_VALIDATION_FAILURE_ADD_BULK = 3L
        private const val ERROR_COLLECTION_IS_PRIVATE_ADD_BULK = 4L
        private const val ERROR_MAX_BULK_VALIDATION_FAILURE = 5L
        private const val COLLECTION_ID_SEMUA_WISHLIST = "0"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTrackingQueue()
        getCollectionItems()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        launchAutoRefresh(isVisibleToUser)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            return getFirebaseRemoteConfig()?.getBoolean(HOME_ENABLE_AUTO_REFRESH_WISHLIST) ?: false
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
        observingDeleteWishlistV2()
        observingBulkDeleteWishlistV2()
        observingDeleteCollectionItems()
        observingDeleteWishlistCollection()
        observingDeleteProgress()
        observingAtc()
        observeSavingItemToCollections()
        observeUpdateAccessWishlistCollection()
        observeGetCollectionSharingData()
        observingCollectionType()
        observingAddWishlistBulk()
    }

    private fun observingDeleteProgress() {
        wishlistCollectionDetailViewModel.deleteWishlistProgressResult.observe(
            viewLifecycleOwner
        ) { result ->
            when (result) {
                is Success -> {
                    if (result.data.status == OK) {
                        val data = result.data.data
                        if (data.success) {
                            if (data.successfullyRemovedItems >= data.totalItems) {
                                finishDeletionWidget(data)
                            } else {
                                updateDeletionWidget(data)
                                handler.postDelayed(
                                    progressDeletionRunnable,
                                    DELAY_REFETCH_PROGRESS_DELETION
                                )
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

    private fun observingDeleteWishlistCollection() {
        wishlistCollectionDetailViewModel.deleteCollectionResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    finishRefresh()
                    if (result.data.status == WishlistCollectionFragment.OK && result.data.data.success) {
                        val intent = Intent()
                        intent.putExtra(
                            ApplinkConstInternalPurchasePlatform.BOOLEAN_EXTRA_SUCCESS,
                            true
                        )
                        intent.putExtra(
                            ApplinkConstInternalPurchasePlatform.STRING_EXTRA_MESSAGE_TOASTER,
                            result.data.data.message
                        )
                        intent.putExtra(WishlistCollectionConsts.EXTRA_NEED_REFRESH, true)
                        activity?.setResult(Activity.RESULT_OK, intent)
                        activity?.finish()
                    } else {
                        val errorMessage = result.data.errorMessage.first().ifEmpty {
                            context?.getString(
                                com.tokopedia.wishlist.R.string.wishlist_v2_common_error_msg
                            )
                        }
                        errorMessage?.let { showToasterActionOke(it, Toaster.TYPE_ERROR) }
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun observingDeleteCollectionItems() {
        wishlistCollectionDetailViewModel.deleteCollectionItemsResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    result.data.let { deleteCollectionItems ->
                        if (deleteCollectionItems.data.success && deleteCollectionItems.status == OK) {
                            showToasterActionOke(deleteCollectionItems.data.message, Toaster.TYPE_NORMAL)
                            setRefreshing()
                        } else {
                            var errorMessage =
                                context?.getString(Rv2.string.wishlist_v2_common_error_msg)
                            if (deleteCollectionItems.data.message.isNotEmpty()) {
                                errorMessage =
                                    deleteCollectionItems.data.message
                            }
                            errorMessage?.let { showToasterActionOke(it, Toaster.TYPE_ERROR) }
                        }
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun observingBulkDeleteWishlistV2() {
        wishlistCollectionDetailViewModel.bulkDeleteWishlistV2Result.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    result.data.let { bulkDeleteWishlistV2 ->
                        if (bulkDeleteWishlistV2.success) {
                            var btnText = getString(Rv2.string.wishlist_oke_label)
                            if (bulkDeleteWishlistV2.button.text.isNotEmpty()) {
                                btnText = bulkDeleteWishlistV2.button.text
                            }
                            if (bulkDeleteMode == 0) {
                                // normal bulk delete
                                val listId =
                                    bulkDeleteWishlistV2.id.replace("[", "").replace("]", "")
                                        .split(",").toList()
                                var msg = getString(
                                    Rv2.string.wishlist_v2_bulk_delete_msg_toaster,
                                    listId.size
                                )
                                if (bulkDeleteWishlistV2.message.isNotEmpty()) {
                                    msg = bulkDeleteWishlistV2.message
                                }

                                showToaster(msg, btnText, Toaster.TYPE_NORMAL)
                                setRefreshing()
                                (activity as WishlistCollectionDetailActivity).isNeedRefresh(true)
                            } else {
                                // bulkDeleteMode == 1 (manual choose via cleaner bottomsheet)
                                // bulkDeleteMode == 2 (choose automatic deletion)
                                turnOffBulkDeleteCleanMode()
                                hideTotalLabel()
                                binding?.run { rvWishlistCollectionDetail.scrollToPosition(0) }
                            }
                            setSwipeRefreshLayout()
                        } else {
                            listSelectedProductIds.clear()
                            finishDeletionWidget(DeleteWishlistProgressResponse.DeleteWishlistProgress.DataDeleteWishlistProgress())
                            var errorMessage =
                                context?.getString(Rv2.string.wishlist_v2_common_error_msg)
                            if (bulkDeleteWishlistV2.message.isNotEmpty()) {
                                errorMessage =
                                    bulkDeleteWishlistV2.message
                            }
                            errorMessage?.let { showToasterActionOke(it, Toaster.TYPE_ERROR) }
                        }
                    }
                }
                is Fail -> {
                    listSelectedProductIds.clear()
                    finishDeletionWidget(DeleteWishlistProgressResponse.DeleteWishlistProgress.DataDeleteWishlistProgress())
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)

                    val labelError = String.format(
                        getString(Rv2.string.on_error_observing_bulk_delete_wishlist_v2_string_builder),
                        userSession.userId ?: "",
                        errorMessage,
                        result.throwable.message ?: ""
                    )
                    // log error type to newrelic
                    ServerLogger.log(Priority.P2, "WISHLIST_V2_ERROR", mapOf("type" to labelError))
                    // log to crashlytics
                    logToCrashlytics(labelError, result.throwable)
                }
            }
        }
    }

    private fun observingWishlistCollectionItems() {
        wishlistCollectionDetailViewModel.collectionItems.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    hideLoader()
                    finishRefresh()
                    result.data.getWishlistCollectionItems.let { collectionDetail ->
                        rvScrollListener.setHasNextPage(collectionDetail.hasNextPage)

                        if (collectionDetail.showDeleteProgress) {
                            if (!hitCountDeletion) {
                                hitCountDeletion = true
                                isOnProgressDeleteWishlist = true
                                getDeleteWishlistProgress()
                            }
                            hideTotalLabel()
                        } else {
                            hideStickyDeletionProgress()
                        }

                        if (collectionDetail.totalData <= 0) {
                            if (isAturMode && collectionDetail.emptyState.type == COLLECTION_ITEMS_EMPTY) {
                                isAturMode = false
                                clearTextSearchBar()
                                hideSearchBar()
                                clearFilters()
                                hideFilter()
                            }
                            binding?.wishlistCollectionDetailStickyCountManageLabel?.rlWishlistCollectionDetailManage?.gone()
                            hideBottomButtonLayout()
                            if (paramGetCollectionItems.query.isEmpty() && paramGetCollectionItems.sortFilters.isEmpty()) {
                                hideSearchBar()
                                hideFilter()
                            } else {
                                if (paramGetCollectionItems.query.isNotEmpty()) {
                                    hideSortFilter(collectionDetail.sortFilters)
                                }
                                if (collectionDetail.sortFilters.isEmpty() && collectionDetail.items.isEmpty()) {
                                    onFailedGetWishlistV2(ResponseErrorException())
                                } else {
                                    showRvWishlist()
                                    isFetchRecommendation = true
                                    hideTotalLabel()
                                }
                            }
                        } else {
                            showRvWishlist()
                            if (isAutoDeletion && isBulkDeleteShow) {
                                hideSearchBar()
                                hideFilter()
                            } else {
                                showSearchBar()
                                showFilter()
                            }
                            if (!collectionDetail.showDeleteProgress) {
                                updateTotalLabel(
                                    collectionDetail.totalData
                                )
                            }
                        }

                        if (currPage == 1 && collectionDetail.sortFilters.isNotEmpty()) {
                            renderChipsFilter(mapToSortFilterItem(collectionDetail.sortFilters))
                            setupGearIcon()
                            setupLayoutTypeIcon()

                            toolbarTitle = collectionDetail.headerTitle
                            if (isBulkAddShow) {
                                updateCustomToolbarSubTitle(collectionNameDestination)
                            } else {
                                if (collectionDetail.description.isNotEmpty()) {
                                    isToolbarHasDesc = true
                                    toolbarDesc = collectionDetail.description
                                    if (!isBulkAddFromOtherCollectionShow) updateCustomToolbarTitleAndSubTitle(collectionDetail.headerTitle, collectionDetail.description)
                                } else {
                                    updateToolbarTitle(toolbarTitle)
                                }
                            }
                        }
                        if (collectionDetail.hasNextPage) {
                            currPage += 1
                        }

                        if (collectionDetail.errorMessage.isNotEmpty()) {
                            showToasterActionOke(collectionDetail.errorMessage, Toaster.TYPE_ERROR)
                        }

                        countRemovableAutomaticDelete =
                            if (collectionDetail.countRemovableItems > 0) collectionDetail.countRemovableItems else collectionDetail.totalData

                        collectionType = collectionDetail.collectionType
                        if (collectionType == TYPE_COLLECTION_PUBLIC_OTHERS) {
                            checkCollectionType(CHECK_COLLECTION_TYPE_FOR_SHOWING_PILIH_BARANG)
                        }
                        setupIconToolbar()
                        listSettingButtons = collectionDetail.setting.buttons
                        _maxBulk = collectionDetail.addWishlistBulkConfig.maxBulk
                        _toasterMaxBulk = collectionDetail.addWishlistBulkConfig.addWishlistBulkToaster.message

                        if (isShowingCleanerBottomSheet && collectionDetail.storageCleanerBottomsheet.title.isNotEmpty()) {
                            showBottomSheetCleaner(WishlistV2Utils.mapToStorageCleanerBottomSheet(collectionDetail.storageCleanerBottomsheet))
                        }

                        if (_isNeedRefreshAndTurnOffBulkModeFromOthers) {
                            _isNeedRefreshAndTurnOffBulkModeFromOthers = false
                            _bulkModeIsAlreadyTurnedOff = true
                            showBottomSheetCollection(
                                childFragmentManager,
                                listSelectedProductIdsFromOtherCollection.joinToString(),
                                SRC_WISHLIST_COLLECTION_SHARING
                            )
                        }
                    }
                }
                is Fail -> {
                    hideLoader()
                    finishRefresh()
                    onFailedGetWishlistV2(result.throwable)
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)

                    val labelError = String.format(
                        getString(Rv2.string.on_error_observing_wishlist_v2_string_builder),
                        userSession.userId ?: "",
                        errorMessage,
                        result.throwable.message ?: ""
                    )
                    // log error type to newrelic
                    ServerLogger.log(Priority.P2, "WISHLIST_V2_ERROR", mapOf("type" to labelError))
                    // log to crashlytics
                    logToCrashlytics(labelError, result.throwable)
                }
            }
        }
    }

    private fun observingAtc() {
        wishlistCollectionDetailViewModel.atcResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    hideLoadingDialog()
                    if (it.data.isStatusError()) {
                        val atcErrorMessage = it.data.getAtcErrorMessage()
                        if (atcErrorMessage != null) {
                            showToasterActionOke(atcErrorMessage, Toaster.TYPE_ERROR)
                        } else {
                            context?.getString(Rv2.string.wishlist_v2_common_error_msg)
                                ?.let { errorDefaultMsg ->
                                    showToasterActionOke(
                                        errorDefaultMsg,
                                        Toaster.TYPE_ERROR
                                    )
                                }
                        }
                    } else {
                        val successMsg =
                            StringUtils.convertListToStringDelimiter(it.data.data.message, ",")
                        showToasterAtc(successMsg, Toaster.TYPE_NORMAL)
                        WishlistCollectionAnalytics.sendClickAddToCartOnWishlistPageEvent(collectionId, wishlistItemOnAtc, userSession.userId, indexOnAtc, it.data.data.cartId)
                    }
                }
                is Fail -> {
                    hideLoadingDialog()
                    context?.also { ctx ->
                        val throwable = it.throwable
                        var errorMessage = if (throwable is ResponseErrorException) {
                            throwable.message ?: ""
                        } else {
                            ErrorHandler.getErrorMessage(
                                ctx,
                                throwable,
                                ErrorHandler.Builder().withErrorCode(false)
                            )
                        }
                        if (errorMessage.isBlank()) {
                            errorMessage = ctx.getString(Rv2.string.wishlist_v2_common_error_msg)
                        }
                        showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)

                        val labelError = String.format(
                            getString(Rv2.string.on_error_observing_atc_string_builder),
                            userSession.userId ?: "",
                            errorMessage,
                            throwable.message ?: ""
                        )
                        // log error type to newrelic
                        ServerLogger.log(
                            Priority.P2,
                            "WISHLIST_V2_ERROR",
                            mapOf("type" to labelError)
                        )
                        // log to crashlytics
                        logToCrashlytics(labelError, throwable)
                    }
                }
            }
        }
    }

    private fun observeSavingItemToCollections() {
        wishlistCollectionDetailViewModel.addWishlistCollectionItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.dataItem.success) {
                        collectionId = collectionIdDestination
                        if (isBulkAddShow) {
                            isBulkAddShow = false

                            val intent = Intent()
                            intent.putExtra(
                                ApplinkConstInternalPurchasePlatform.BOOLEAN_EXTRA_SUCCESS,
                                true
                            )
                            intent.putExtra(
                                ApplinkConstInternalPurchasePlatform.STRING_EXTRA_MESSAGE_TOASTER,
                                result.data.dataItem.message
                            )
                            intent.putExtra(WishlistCollectionConsts.EXTRA_NEED_REFRESH, true)
                            activity?.setResult(Activity.RESULT_OK, intent)
                            activity?.finish()
                        } else if (isBulkAddFromOtherCollectionShow) {
                            isBulkAddFromOtherCollectionShow = false
                            showToasterActionLihat(result.data.dataItem.message, Toaster.TYPE_NORMAL, result.data.dataItem.collectionId)
                        }
                    } else {
                        val errorMessage = if (result.data.errorMessage.isNotEmpty()) {
                            result.data.errorMessage.firstOrNull() ?: ""
                        } else if (result.data.dataItem.message.isNotEmpty()) {
                            result.data.dataItem.message
                        } else {
                            getString(com.tokopedia.wishlist.R.string.wishlist_v2_common_error_msg)
                        }
                        showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun observeUpdateAccessWishlistCollection() {
        wishlistCollectionDetailViewModel.updateWishlistCollectionResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.data.success && result.data.status == OK) {
                        doRefresh()
                        getCollectionSharingData()
                    } else if (result.data.errorMessage.isNotEmpty()) {
                        showToasterActionOke(result.data.errorMessage[0], Toaster.TYPE_ERROR)
                    } else {
                        context?.getString(Rv2.string.wishlist_v2_common_error_msg)
                            ?.let { showToasterActionOke(it, Toaster.TYPE_ERROR) }
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun observeGetCollectionSharingData() {
        wishlistCollectionDetailViewModel.getWishlistCollectionSharingDataResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.status == OK) {
                        activity?.let { fragmentActivity ->
                            view?.let { view ->
                                WishlistCollectionSharingUtils().showUniversalShareWithMediaBottomSheet(
                                    activity = fragmentActivity,
                                    data = result.data.data,
                                    paramImageGenerator = WishlistCollectionSharingUtils().mapParamImageGenerator(result.data.data),
                                    userId = userSession.userId,
                                    view = view,
                                    childFragmentManager = childFragmentManager,
                                    fragment = this@WishlistCollectionDetailFragment
                                )
                            }
                        }
                    } else {
                        val errorMessage = result.data.errorMessage.first().ifEmpty {
                            context?.getString(
                                com.tokopedia.wishlist.R.string.wishlist_v2_common_error_msg
                            )
                        }
                        errorMessage?.let { showToasterActionOke(it, Toaster.TYPE_ERROR) }
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun observingCollectionType() {
        wishlistCollectionDetailViewModel.collectionType.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    collectionType = result.data.collectionType
                    if (result.data.collectionType == TYPE_COLLECTION_PUBLIC_OTHERS) {
                        when (_currCheckCollectionType) {
                            CHECK_COLLECTION_TYPE_FOR_SHOWING_PILIH_BARANG -> {
                                if (!isBulkAddFromOtherCollectionShow) showSelectItemsOption()
                            }
                            CHECK_COLLECTION_TYPE_FOR_TURN_ON_SELECT_ITEMS_MODE -> {
                                if (userSession.isLoggedIn) {
                                    turnOnBulkAddFromOtherCollectionsMode()
                                } else {
                                    goToLoginPage()
                                }
                            }
                            CHECK_COLLECTION_TYPE_FOR_DIALOG_CONFIRMATION -> {
                                showBulkAddFromOtherCollectionConfirmationDialog()
                            }
                            else -> {
                                if (!isBulkAddFromOtherCollectionShow) showSelectItemsOption()
                            }
                        }
                    } else {
                        doRefresh()
                    }
                }
                is Fail -> {
                    doRefresh()
                }
            }
        }
    }

    private fun observingAddWishlistBulk() {
        wishlistCollectionDetailViewModel.addWishlistBulkResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.success) {
                        if (_isNeedRefreshAndTurnOffBulkModeFromOthers) {
                            turnOffBulkAddFromOtherCollection()
                            setRefreshing()
                        }
                    } else {
                        when (result.data.errorType) {
                            ERROR_GENERAL_SYSTEM_FAILURE_ADD_BULK -> {
                                showIndefiniteToasterWithCTA(
                                    message = result.data.message,
                                    actionText = result.data.button.text,
                                    type = Toaster.TYPE_ERROR
                                ) { doAddWishlistBulk() }
                            }

                            ERROR_MAX_QTY_FAILURE_ADD_BULK -> {
                                showToasterWithCTA(
                                    message = result.data.message,
                                    actionText = result.data.button.text,
                                    type = Toaster.TYPE_ERROR
                                ) {
                                    _isNeedRefreshAndTurnOffBulkModeFromOthers = false
                                    goToWishlistCollectionDetailShowCleanerBottomSheet(COLLECTION_ID_SEMUA_WISHLIST)
                                }
                            }

                            ERROR_PARTIAL_MAX_QTY_VALIDATION_FAILURE_ADD_BULK -> {
                                showToasterWithCTA(
                                    message = result.data.message,
                                    actionText = result.data.button.text,
                                    type = Toaster.TYPE_ERROR
                                ) {
                                    _isNeedRefreshAndTurnOffBulkModeFromOthers = false
                                    goToWishlistCollectionDetailShowCleanerBottomSheet(COLLECTION_ID_SEMUA_WISHLIST)
                                }
                            }

                            ERROR_COLLECTION_IS_PRIVATE_ADD_BULK -> {
                                doRefresh()
                            }

                            ERROR_MAX_BULK_VALIDATION_FAILURE -> {
                                showToasterActionOke(
                                    message = result.data.message,
                                    type = Toaster.TYPE_ERROR
                                )
                            }
                        }
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)

                    val labelError = String.format(
                        getString(Rv2.string.on_error_observing_add_bulk_wishlist_string_builder),
                        userSession.userId ?: "",
                        errorMessage,
                        result.throwable.message ?: ""
                    )
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
                wishlistCollectionDetailSearchbar.gone()
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
                wishlistCollectionDetailSearchbar.gone()
                emptyStateGlobalWishlistCollectionDetail.gone()
                hideSearchBar()
                hideFilter()
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
        if (errorMessage.isEmpty()) {
            errorMessage =
                getString(Rv2.string.wishlist_v2_failed_to_get_information)
        }
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
                    showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)

                    val labelError = String.format(
                        getString(Rv2.string.on_error_observing_wishlist_data_string_builder),
                        userSession.userId ?: "",
                        errorMessage,
                        result.throwable.message ?: ""
                    )
                    // log error type to newrelic
                    ServerLogger.log(Priority.P2, "WISHLIST_V2_ERROR", mapOf("type" to labelError))
                    // log to crashlytics
                    logToCrashlytics(labelError, result.throwable)
                }
            }
        }
    }

    private fun observingDeleteWishlistV2() {
        wishlistCollectionDetailViewModel.deleteWishlistV2Result.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    result.data.let { wishlistRemoveV2 ->
                        context?.let { context ->
                            view?.let { v ->
                                AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(
                                    wishlistRemoveV2,
                                    context,
                                    v
                                )
                            }
                            (activity as WishlistCollectionDetailActivity).isNeedRefresh(true)
                            setRefreshing()
                        }
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)

                    val labelError = String.format(
                        getString(Rv2.string.on_error_observing_delete_wishlist_v2_string_builder),
                        userSession.userId ?: "",
                        errorMessage,
                        result.throwable.message ?: ""
                    )
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
            activity?.window?.decorView?.setBackgroundColor(
                ContextCompat.getColor(
                    it,
                    com.tokopedia.unifyprinciples.R.color.Unify_Background
                )
            )
        }
        activityWishlistV2 = arguments?.getString(PARAM_ACTIVITY_WISHLIST_V2, "") as String
        toasterMessageInitial =
            arguments?.getString(EXTRA_TOASTER_WISHLIST_COLLECTION_DETAIL, "") as String
        collectionId =
            arguments?.getString(ApplinkConstInternalPurchasePlatform.PATH_COLLECTION_ID) ?: ""
        isBulkAddShow = arguments?.getBoolean(EXTRA_IS_BULK_ADD) ?: false
        isShowingCleanerBottomSheet = arguments?.getBoolean(EXTRA_IS_SHOW_CLEANER_BOTTOMSHEET) ?: false
        collectionIdDestination = arguments?.getString(EXTRA_COLLECTION_ID_DESTINATION) ?: ""
        collectionNameDestination = arguments?.getString(EXTRA_COLLECTION_NAME_DESTINATION) ?: ""
        paramGetCollectionItems.collectionId = collectionId

        var titleToolbar = ""
        if (newCollectionDetailTitle.isNotEmpty()) titleToolbar = newCollectionDetailTitle
        updateToolbarTitle(titleToolbar)

        setSwipeRefreshLayout()
        collectionItemsAdapter = WishlistV2Adapter().apply {
            setActionListener(this@WishlistCollectionDetailFragment)
        }
        showLoader()
        addEndlessScrollListener()
        binding?.run {
            wishlistCollectionDetailSearchbar.searchBarTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchQuery =
                        wishlistCollectionDetailSearchbar.searchBarTextField.text.toString()
                    if (searchQuery.isNotEmpty()) {
                        WishlistCollectionAnalytics.sendSubmitSearchFromCariProdukEvent(searchQuery)
                    }
                    hideKeyboardFromSearchBar()
                    triggerSearch()
                }
                true
            }
            wishlistCollectionDetailSearchbar.clearListener = {
                searchQuery = ""
                hideKeyboardFromSearchBar()
                triggerSearch()
            }

            viewLifecycleOwner.lifecycle.addObserver(wishlistCollectionDetailNavtoolbar)

            if (isBulkAddShow) {
                turnOnBulkAddMode()
            } else {
                if (collectionId == "0") {
                    wishlistCollectionDetailStickyCountManageLabel.apply {
                        wishlistCollectionDetailManageLabel.show()
                        wishlistCollectionDetailManageLabel.setOnClickListener { onStickyManageClicked() }
                    }
                    WishlistCollectionAnalytics.sendAllWishListPageOpenedEvent(userSession.isLoggedIn, userSession.userId)
                } else {
                    wishlistCollectionDetailStickyCountManageLabel.apply {
                        wishlistCollectionDetailManageLabel.gone()
                    }
                    WishlistCollectionAnalytics.sendWishListCollectionDetailPageOpenedEvent(userSession.isLoggedIn, userSession.userId)
                }
                wishlistCollectionDetailFb.circleMainMenu.setOnClickListener {
                    rvWishlistCollectionDetail.smoothScrollToPosition(0)
                }
                wishlistCollectionDetailFb.gone()
            }
        }

        if (toasterMessageInitial.isNotEmpty()) {
            showToasterInitial(toasterMessageInitial)
        }
    }

    private fun setupLayoutTypeIcon() {
        setTypeLayoutIcon()
        binding?.run {
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailTypeLayoutIcon.setOnClickListener {
                changeTypeLayout()
                setTypeLayoutIcon()
            }
        }
    }

    private fun setupGearIcon() {
        binding?.run {
            if (collectionId == "0") {
                wishlistCollectionDetailStickyCountManageLabel.apply {
                    iconGearCollectionDetail.gone()
                }
            } else {
                wishlistCollectionDetailStickyCountManageLabel.apply {
                    if (collectionType == TYPE_COLLECTION_PUBLIC_OTHERS) {
                        wishlistCollectionDetailManageLabel.gone()
                        iconGearCollectionDetail.gone()
                        wishlistCollectionSelectItemOption.show()
                    } else {
                        wishlistCollectionSelectItemOption.gone()
                        wishlistCollectionDetailManageLabel.gone()
                        iconGearCollectionDetail.show()
                        iconGearCollectionDetail.setOnClickListener {
                            onCollectionSettingsClicked(
                                collectionId,
                                collectionName
                            )
                            WishlistCollectionAnalytics.sendClickGearIconEvent()
                        }
                    }
                }
            }
        }
    }

    private fun showCoachMarkOnSharingIcon(view: View) {
        if (coachMarkItemSharingIcon.isEmpty()) {
            coachMarkItemSharingIcon.add(
                CoachMark2Item(
                    view,
                    "",
                    getString(com.tokopedia.wishlist.R.string.collection_coachmark_wishlist_detail),
                    CoachMark2.POSITION_BOTTOM
                )
            )
        }
        if (coachMarkSharingIcon == null) {
            coachMarkSharingIcon = CoachMark2(requireContext())
        }

        coachMarkSharingIcon?.let {
            if (!it.isShowing) {
                it.showCoachMark(coachMarkItemSharingIcon, null)
            }
            CoachMarkPreference.setShown(
                requireContext(),
                COACHMARK_WISHLIST_SHARING_ICON_DETAIL_PAGE,
                true
            )
        }
    }

    private fun setupIconToolbar() {
        val pageSource: String
        val icons: IconBuilder
        binding?.run {
            if (activityWishlistV2 != PARAM_HOME) {
                wishlistCollectionDetailNavtoolbar.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
                icons = IconBuilder().apply {
                    if (collectionType == TYPE_COLLECTION_PRIVATE_SELF ||
                        collectionType == TYPE_COLLECTION_PUBLIC_SELF ||
                        collectionType == TYPE_COLLECTION_PUBLIC_OTHERS
                    ) {
                        addIcon(iconId = IconList.ID_SHARE, disableRouteManager = true, onClick = { handleCollectionSharing() }, disableDefaultGtmTracker = true)
                    }
                    addIcon(iconId = IconList.ID_CART, disableRouteManager = true, onClick = { handleGoToCartPage() })
                    addIcon(iconId = IconList.ID_NAV_GLOBAL) {}
                }
            } else {
                pageSource = ApplinkConsInternalNavigation.SOURCE_HOME_WISHLIST_V2
                wishlistCollectionDetailNavtoolbar.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_NONE)
                icons = IconBuilder(IconBuilderFlag(pageSource = pageSource)).apply {
                    if (collectionType == TYPE_COLLECTION_PUBLIC_SELF || collectionType == TYPE_COLLECTION_PUBLIC_OTHERS) {
                        addIcon(iconId = IconList.ID_SHARE) {}
                    }
                    addIcon(iconId = IconList.ID_MESSAGE) {}
                    addIcon(iconId = IconList.ID_NOTIFICATION) {}
                    addIcon(iconId = IconList.ID_CART) {}
                    addIcon(iconId = IconList.ID_NAV_GLOBAL) {}
                }
            }
            wishlistCollectionDetailNavtoolbar.setIcon(icons)
            if (collectionType != TYPE_COLLECTION_PUBLIC_OTHERS && !CoachMarkPreference.hasShown(requireContext(), COACHMARK_WISHLIST_SHARING_ICON_DETAIL_PAGE)) {
                Handler().postDelayed(
                    showCoarchmarkRunnable,
                    DELAY_SHOW_COACHMARK_TOOLBAR
                )
            }
        }
    }

    private fun showCoachMarkSharingIcon() {
        binding?.run {
            wishlistCollectionDetailNavtoolbar.getShareIconView()?.let {
                showCoachMarkOnSharingIcon(it)
            }
        }
    }

    private fun handleCollectionSharing() {
        sendClickShareButtonCollectionEvent(collectionId, collectionType.getStringCollectionType(), userSession.userId)
        if (collectionType == TYPE_COLLECTION_PRIVATE_SELF) {
            showDialogSharePermission()
        } else {
            getCollectionSharingData()
        }
    }

    private fun handleGoToCartPage() {
        if (userSession.isLoggedIn) {
            goToCartPage()
        } else {
            goToLoginPage()
        }
    }

    private fun getCollectionSharingData() {
        wishlistCollectionDetailViewModel.getWishlistCollectionSharingData(collectionId.toLongOrZero())
    }

    private fun showDialogSharePermission() {
        val dialog =
            context?.let { DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.setTitle(getString(Rv2.string.sharing_collection_confirmation_title))
        dialog?.setDescription(getString(Rv2.string.sharing_collection_confirmation_desc))
        dialog?.setPrimaryCTAText(getString(Rv2.string.sharing_collection_primary_button))
        dialog?.setPrimaryCTAClickListener {
            dialog.dismiss()
            updateCollectionAccess()
        }
        dialog?.setSecondaryCTAText(getString(Rv2.string.wishlist_cancel_manage_label))
        dialog?.setSecondaryCTAClickListener {
            dialog.dismiss()
        }
        dialog?.show()
    }

    private fun updateCollectionAccess() {
        val params = UpdateWishlistCollectionParams(
            id = collectionId.toLongOrZero(),
            name = collectionName,
            access = TYPE_COLLECTION_SHARE.toLong()
        )
        wishlistCollectionDetailViewModel.updateAccessWishlistCollection(params)
    }

    private fun hideKeyboardFromSearchBar() {
        binding?.run {
            wishlistCollectionDetailSearchbar.searchBarTextField.clearFocus()
            val `in` =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            `in`.hideSoftInputFromWindow(
                wishlistCollectionDetailSearchbar.searchBarTextField.windowToken,
                0
            )
        }
    }

    private fun showToasterInitial(toasterMessageInitial: String) {
        showToasterActionOke(toasterMessageInitial, Toaster.TYPE_NORMAL)
    }

    private fun updateToolbarTitle(title: String) {
        collectionName = title
        var toolbarTitle = title
        if (isAturMode) toolbarTitle = "${getString(Rv2.string.wishlist_manage_label)} $title"
        binding?.run {
            wishlistCollectionDetailNavtoolbar.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_TITLE)
            wishlistCollectionDetailNavtoolbar.setToolbarTitle(toolbarTitle)
        }
    }

    private fun updateCustomToolbarSubTitle(subtitle: String) {
        val customToolbar = LayoutInflater.from(context).inflate(Rv2.layout.toolbar_custom, null, false)
        val titleLayout = customToolbar?.findViewById<Typography>(Rv2.id.toolbar_title)
        val subtitleLayout = customToolbar?.findViewById<Typography>(Rv2.id.toolbar_subtitle)

        titleLayout?.text = getString(Rv2.string.wishlist_add_label_toolbar)
        subtitleLayout?.text = subtitle

        binding?.run {
            wishlistCollectionDetailNavtoolbar.setCustomViewContentView(customToolbar)
            wishlistCollectionDetailNavtoolbar.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_CUSTOM)
        }
    }

    private fun updateCustomToolbarTitleAndSubTitle(title: String, subtitle: String) {
        collectionName = title
        val customToolbarView = LayoutInflater.from(context).inflate(Rv2.layout.toolbar_custom, null, false)
        val titleLayout = customToolbarView?.findViewById<Typography>(Rv2.id.toolbar_title)
        val subtitleLayout = customToolbarView?.findViewById<Typography>(Rv2.id.toolbar_subtitle)

        titleLayout?.text = title
        subtitleLayout?.text = subtitle

        customToolbarView?.let { toolbarView ->
            binding?.wishlistCollectionDetailNavtoolbar?.apply {
                setCustomViewContentView(toolbarView)
                setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_CUSTOM)
            }
        }
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
            WishlistCollectionAnalytics.sendClickLayoutSettingsEvent(TYPE_GRID)
        } else {
            wishlistPref?.setTypeLayout(TYPE_LIST_INT)
            WishlistCollectionAnalytics.sendClickLayoutSettingsEvent(TYPE_LIST)
        }
        collectionItemsAdapter.changeTypeLayout(wishlistPref?.getTypeLayout())
    }

    private fun setTypeLayoutIcon() {
        binding?.run {
            if (wishlistPref?.getTypeLayout() == TYPE_LIST) {
                wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailTypeLayoutIcon.setImage(
                    IconUnify.VIEW_LIST
                )
            } else if (wishlistPref?.getTypeLayout() == TYPE_GRID) {
                wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailTypeLayoutIcon.setImage(
                    IconUnify.VIEW_GRID
                )
            }
        }
    }

    private fun setRefreshing() {
        isAturMode = false
        isBulkDeleteShow = false
        listExcludedBulkDelete.clear()
        collectionItemsAdapter.hideCheckbox()
        countRemovableAutomaticDelete = 0
        doRefresh()
        showFilter()
        showSearchBar()

        binding?.run {
            bottomButtonLayout.gone()
            if (collectionId == "0") {
                wishlistCollectionDetailStickyCountManageLabel.apply {
                    iconGearCollectionDetail.gone()
                    wishlistCollectionDetailManageLabel.show()
                    wishlistCollectionDetailManageLabel.text =
                        getString(Rv2.string.wishlist_manage_label)
                }
            } else {
                wishlistCollectionDetailStickyCountManageLabel.apply {
                    if (collectionType == TYPE_COLLECTION_PUBLIC_OTHERS) {
                        wishlistCollectionDetailManageLabel.gone()
                        iconGearCollectionDetail.gone()
                        wishlistCollectionSelectItemOption.show()
                        wishlistDivider.show()
                        wishlistCollectionDetailTypeLayoutIcon.show()
                    } else {
                        wishlistCollectionSelectItemOption.gone()
                        wishlistCollectionDetailManageLabel.gone()
                        iconGearCollectionDetail.show()
                    }
                }
            }
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
                if (totalItemsCount > TOTAL_LOADER) {
                    currentPage += 1
                    onLoadMore = true
                    if (isFetchRecommendation) {
                        loadRecommendationList()
                    } else {
                        paramGetCollectionItems.page = currPage
                        getCollectionItems()
                    }
                }
            }

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)
                if (dy != 0) {
                    binding?.run {
                        if (dy > 0) {
                            wishlistCollectionDetailFb.gone()
                        } else {
                            wishlistCollectionDetailFb.visible()
                        }
                    }
                }

                var firstVisibleItems: IntArray? = null
                firstVisibleItems =
                    (layoutManager as StaggeredGridLayoutManager).findFirstVisibleItemPositions(
                        firstVisibleItems
                    )
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

    private fun initTrackingQueue() {
        activity?.let {
            trackingQueue = TrackingQueue(it)
        }
    }

    private fun getCollectionItems() {
        fetchUserLatestAddressData()
        userAddressData?.let { address ->
            paramGetCollectionItems.wishlistChosenAddress =
                GetWishlistCollectionItemsParams.WishlistChosenAddress(
                    districtId = address.district_id,
                    cityId = address.city_id,
                    latitude = address.lat,
                    longitude = address.long,
                    postalCode = address.postal_code,
                    addressId = address.address_id
                )
        }
        /*var inCollection = ""
        if (collectionId.isNotEmpty() && collectionId != "0") {
            inCollection = PARAM_INSIDE_COLLECTION
        }
        paramGetCollectionItems.inCollection = inCollection*/
        paramGetCollectionItems.page = currPage
        wishlistCollectionDetailViewModel.getWishlistCollectionItems(
            paramGetCollectionItems,
            wishlistPref?.getTypeLayout(),
            paramGetCollectionItems.source == SOURCE_AUTOMATIC_DELETION
        )
    }

    private fun triggerSearch() {
        paramGetCollectionItems.query = searchQuery
        // listSelectedProductIds.clear()
        listExcludedBulkDelete.clear()
        if (isBulkDeleteShow) setDefaultLabelDeleteButton()
        doRefresh()
        rvScrollListener.resetState()
    }

    private fun getDeleteWishlistProgress() {
        wishlistCollectionDetailViewModel.getDeleteWishlistProgress()
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
            getDeleteWishlistProgress()
        }
    }

    private fun stopProgressDeletionHandler() {
        handler.removeCallbacks(progressDeletionRunnable)
    }

    private fun stopDeletionAndShowToasterError(message: String) {
        showToasterActionOke(message, Toaster.TYPE_ERROR)
        finishDeletionWidget(DeleteWishlistProgressResponse.DeleteWishlistProgress.DataDeleteWishlistProgress())
        doRefresh()
    }

    private fun finishDeletionWidget(data: DeleteWishlistProgressResponse.DeleteWishlistProgress.DataDeleteWishlistProgress) {
        isOnProgressDeleteWishlist = false
        stopProgressDeletionHandler()
        wishlistCollectionDetailViewModel.deleteWishlistProgressResult.removeObservers(this)
        if (data.totalItems > 0 && data.toasterMessage.isNotEmpty()) {
            val finishData =
                DeleteWishlistProgressResponse.DeleteWishlistProgress.DataDeleteWishlistProgress(
                    totalItems = data.totalItems,
                    successfullyRemovedItems = data.totalItems,
                    message = data.message,
                    tickerColor = data.tickerColor,
                    success = data.success,
                    toasterMessage = data.toasterMessage
                )
            updateDeletionWidget(finishData)
            showToasterActionOke(data.toasterMessage, Toaster.TYPE_NORMAL)
        }
        hideStickyDeletionProgress()
        doRefresh()
    }

    @SuppressLint("SetTextI18n")
    private fun updateDeletionWidget(progressData: DeleteWishlistProgressResponse.DeleteWishlistProgress.DataDeleteWishlistProgress) {
        var message = getString(Rv2.string.wishlist_v2_default_message_deletion_progress)
        if (progressData.message.isNotEmpty()) message = progressData.message

        val percentage = progressData.successfullyRemovedItems.toDouble() / progressData.totalItems
        val indicatorProgressBar = percentage * 100

        binding?.run {
            wishlistCollectionDetailStickyCountManageLabel.stickyProgressDeletionWidget.stickyDeletionCard.cardType =
                CardUnify2.TYPE_SHADOW
            wishlistCollectionDetailStickyCountManageLabel.stickyProgressDeletionWidget.rlDeletionProgress.visible()
            wishlistCollectionDetailStickyCountManageLabel.stickyProgressDeletionWidget.deletionMessage.text =
                message
            wishlistCollectionDetailStickyCountManageLabel.stickyProgressDeletionWidget.deletionProgressBar.setValue(
                indicatorProgressBar.roundToInt(),
                true
            )
            wishlistCollectionDetailStickyCountManageLabel.stickyProgressDeletionWidget.labelProgressBar.text =
                "${progressData.successfullyRemovedItems}/${progressData.totalItems}"
        }
    }

    private fun updateTotalLabel(totalData: Int) {
        binding?.run {
            wishlistCollectionDetailStickyCountManageLabel.apply {
                rlWishlistCollectionDetailManage.visible()
                wishlistCollectionDetailCountLabel.text = "$totalData"
                wishlistBarangLabel.text = getString(Rv2.string.wishlist_v2_barang_label)
            }
        }
    }

    private fun hideTotalLabel() {
        binding?.run {
            wishlistCollectionDetailStickyCountManageLabel.rlWishlistCollectionDetailManage.gone()
        }
    }

    private fun hideFilter() {
        binding?.run {
            clWishlistCollectionDetailHeader.gone()
        }
    }

    private fun showFilter() {
        binding?.run {
            clWishlistCollectionDetailHeader.visible()
        }
    }

    private fun showSearchBar() {
        binding?.run {
            wishlistCollectionDetailSearchbar.visible()
        }
    }

    private fun hideSearchBar() {
        binding?.run {
            wishlistCollectionDetailSearchbar.gone()
        }
    }

    private fun clearTextSearchBar() {
        binding?.run {
            wishlistCollectionDetailSearchbar.searchBarTextField.text.clear()
        }
    }

    private fun hideStickyDeletionProgress() {
        binding?.run {
            wishlistCollectionDetailStickyCountManageLabel.rlWishlistCollectionDetailManage.gone()
            wishlistCollectionDetailStickyCountManageLabel.rlWishlistCollectionDetailManage.visible()
            wishlistCollectionDetailStickyCountManageLabel.stickyProgressDeletionWidget.rlDeletionProgress.gone()
        }
    }

    private fun hideSortFilter(sortFilters: List<GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.SortFiltersItem>) {
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

    private fun hideLoader() {
        binding?.run {
            wishlistCollectionDetailLoaderLayout.root.gone()
            wishlistCollectionDetailSortFilter.visible()
        }
    }

    private fun mapToSortFilterItem(sortFilters: List<GetWishlistCollectionItemsResponse.GetWishlistCollectionItems.SortFiltersItem>): List<WishlistV2Response.Data.WishlistV2.SortFiltersItem> {
        val arrayListNewSortItem = arrayListOf<WishlistV2Response.Data.WishlistV2.SortFiltersItem>()
        sortFilters.forEach { item ->
            val newListOptionsItem =
                arrayListOf<WishlistV2Response.Data.WishlistV2.SortFiltersItem.OptionsItem>()
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
            val typeFilter =
                if (filterItem.isActive) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
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
                    /*var inCollection = ""
                    if (collectionId.isNotEmpty() && collectionId != "0") {
                        inCollection = "inside"
                    }
                    paramGetCollectionItems.inCollection = inCollection*/
                    paramGetCollectionItems.collectionId = collectionId
                    if (searchQuery.isNotEmpty()) paramGetCollectionItems.query = searchQuery
                    doRefresh()
                    WishlistCollectionAnalytics.sendClickXChipsToClearFilterEvent()
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
        val filterBottomSheet = WishlistV2FilterBottomSheet.newInstance(
            setTitleBottomSheet(filterItem.name),
            filterItem.selectionType
        )
        if (filterBottomSheet.isAdded || childFragmentManager.isStateSaved) return

        val filterBottomSheetAdapter = WishlistV2FilterBottomSheetAdapter()
        filterBottomSheetAdapter.filterItem = filterItem
        var listOptionIdSelected = mutableListOf<String>()
        var nameSelected = ""

        filterBottomSheet.setAdapter(filterBottomSheetAdapter)

        if (filterItem.isActive) {
            if (filterItem.name == FILTER_OFFERS) {
                filterBottomSheet.setAction(CTA_RESET) {
                    isCTAResetOfferFilterClicked = true
                    listOptionIdSelected.clear()
                    listTitleCheckboxIdSelected.clear()
                    filterBottomSheetAdapter.isResetCheckbox = true
                    filterBottomSheetAdapter.notifyDataSetChanged()
                    filterBottomSheet.showButtonSave()
                }
            } else {
                filterBottomSheet.setAction(CTA_RESET) {
                    filterBottomSheet.dismiss()
                    removeFilter(filterItem)
                }
            }
        }

        filterBottomSheet.setListener(object : WishlistV2FilterBottomSheet.BottomSheetListener {
            override fun onRadioButtonSelected(name: String, optionId: String, label: String) {
                filterBottomSheet.dismiss()
                paramGetCollectionItems.sortFilters.removeAll { it.name == name }
                paramGetCollectionItems.sortFilters.add(
                    GetWishlistCollectionItemsParams.WishlistSortFilterParam(
                        name = filterItem.name,
                        selected = arrayListOf(optionId)
                    )
                )
                doRefresh()
                hitAnalyticsFilterOptionSelected(name, label)
            }

            override fun onCheckboxSelected(
                name: String,
                optionId: String,
                isChecked: Boolean,
                titleCheckbox: String
            ) {
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
                    if (isCTAResetOfferFilterClicked) {
                        isCTAResetOfferFilterClicked = false
                        paramGetCollectionItems.sortFilters.forEach { sortFilterParam ->
                            if (sortFilterParam.name == FILTER_OFFERS) {
                                listOptionIdSelected.clear()
                            }
                        }
                    }

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
                paramGetCollectionItems.sortFilters.removeAll { it.name == nameSelected }
                if (listOptionIdSelected.isNotEmpty()) {
                    paramGetCollectionItems.sortFilters.add(
                        GetWishlistCollectionItemsParams.WishlistSortFilterParam(
                            name = nameSelected,
                            selected = listOptionIdSelected as ArrayList<String>
                        )
                    )
                }

                filterBottomSheet.dismiss()
                doRefresh()
                WishlistCollectionAnalytics.sendClickSimpanOnPenawaranFilterChipsEvent(
                    listTitleCheckboxIdSelected.toString().replace("[", "").replace("]", "")
                )
            }
        })
        filterBottomSheet.show(childFragmentManager)

        hitAnalyticsWhenClickFilter(filterName = filterItem.name)
    }

    private fun hitAnalyticsWhenClickFilter(filterName: String) {
        when (filterName) {
            FILTER_SORT -> {
                WishlistCollectionAnalytics.sendClickUrutkanFilterChipsEvent()
            }
            FILTER_CATEGORIES -> {
                WishlistCollectionAnalytics.sendClickKategoriFilterChipsEvent()
            }
            FILTER_OFFERS -> {
                WishlistCollectionAnalytics.sendClickPenawaranFilterChipsEvent()
            }
            FILTER_STOCK -> {
                WishlistCollectionAnalytics.sendClickStokFilterChipsEvent()
            }
        }
    }

    private fun hitAnalyticsFilterOptionSelected(filterName: String, label: String) {
        when (filterName) {
            FILTER_SORT -> {
                WishlistCollectionAnalytics.sendClickOptionOnUrutkanFilterChipsEvent(label)
            }
            FILTER_CATEGORIES -> {
                WishlistCollectionAnalytics.sendClickOptionOnKategoriFilterChipsEvent(label)
            }
            FILTER_STOCK -> {
                WishlistCollectionAnalytics.sendClickOptionOnStokFilterChipsEvent(label)
            }
        }
    }

    private fun showBottomSheetThreeDotsMenu(itemWishlist: WishlistV2UiModel.Item) {
        val bottomSheetThreeDotsMenu = WishlistV2ThreeDotsMenuBottomSheet.newInstance()
        if (bottomSheetThreeDotsMenu.isAdded || childFragmentManager.isStateSaved) return

        val threeDotsMenuBottomSheetAdapter = WishlistV2ThreeDotsMenuBottomSheetAdapter()
        threeDotsMenuBottomSheetAdapter.wishlistItem = itemWishlist

        bottomSheetThreeDotsMenu.setAdapter(threeDotsMenuBottomSheetAdapter)
        bottomSheetThreeDotsMenu.setListener(object :
                WishlistV2ThreeDotsMenuBottomSheet.BottomSheetListener {
                override fun onThreeDotsMenuItemSelected(
                    wishlistItem: WishlistV2UiModel.Item,
                    additionalItem: WishlistV2UiModel.Item.Buttons.AdditionalButtonsItem
                ) {
                    bottomSheetThreeDotsMenu.dismiss()
                    if (additionalItem.url.isNotEmpty()) {
                        RouteManager.route(context, additionalItem.url)
                    } else {
                        when (additionalItem.action) {
                            SHARE_LINK_PRODUCT -> {
                                showShareBottomSheet(wishlistItem)
                                WishlistV2Analytics.clickShareLinkProduct(
                                    wishlistId = wishlistItem.wishlistId,
                                    productId = wishlistItem.id,
                                    userId = userSession.userId
                                )
                            }
                            MENU_DELETE_WISHLIST -> {
                                if (paramGetCollectionItems.collectionId == "0") {
                                    showDeleteConfirmationDialog(wishlistItem.id)
                                } else {
                                    showDeleteCollectionItemConfirmationDialog(
                                        countDelete,
                                        wishlistItem.id
                                    )
                                }
                            }
                            MENU_ADD_ITEM_TO_COLLECTION -> {
                                showWishlistCollectionHostBottomSheetActivity(wishlistItem, false)
                            }
                            MENU_ADD_WISHLIST -> {
                                if (userSession.isLoggedIn) {
                                    addToWishlist(wishlistItem, userSession.userId, collectionId)
                                } else {
                                    goToLoginPage()
                                }
                            }
                        }
                    }
                    WishlistCollectionAnalytics.sendClickOptionOnThreeDotMenuEvent(additionalItem.text)
                }
            })
        bottomSheetThreeDotsMenu.show(childFragmentManager)
    }

    private fun showWishlistCollectionHostBottomSheetActivity(wishlistItem: WishlistV2UiModel.Item, checkSourceWishlist: Boolean) {
        var applinkCollection =
            "${ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION_BOTTOMSHEET}?${ApplinkConstInternalPurchasePlatform.PATH_PRODUCT_ID}=${wishlistItem.id}"
        applinkCollection += if (collectionType == TYPE_COLLECTION_PUBLIC_OTHERS && checkSourceWishlist) {
            "&${ApplinkConstInternalPurchasePlatform.PATH_SRC}=$SRC_WISHLIST_SHARING"
        } else {
            "&${ApplinkConstInternalPurchasePlatform.PATH_SRC}=$SRC_WISHLIST"
        }
        val intentBottomSheetWishlistCollection =
            RouteManager.getIntent(context, applinkCollection)
        intentBottomSheetWishlistCollection.putExtra(
            IS_PRODUCT_ACTIVE,
            wishlistItem.available
        )
        startActivityForResult(
            intentBottomSheetWishlistCollection,
            ApplinkConstInternalPurchasePlatform.REQUEST_CODE_ADD_WISHLIST_COLLECTION
        )
    }

    private fun addToWishlist(wishlistItem: WishlistV2UiModel.Item, userId: String, collectionId: String) {
        wishlistCollectionDetailViewModel.addWishListV2(
            productId = wishlistItem.id,
            userId = userId,
            listener = object : WishlistV2ActionListener {
                override fun onErrorAddWishList(throwable: Throwable, productId: String) {
                    val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
                    showToasterActionOke(
                        message = errorMessage,
                        type = Toaster.TYPE_ERROR
                    )
                }

                override fun onSuccessAddWishlist(
                    result: AddToWishlistV2Response.Data.WishlistAddV2,
                    productId: String
                ) {
                    if (result.success) {
                        showWishlistCollectionHostBottomSheetActivity(wishlistItem, true)
                    } else {
                        showToasterWithCTA(
                            message = result.message,
                            actionText = result.button.text,
                            type = Toaster.TYPE_ERROR
                        ) {
                            if (result.button.action == OPEN_WISHLIST) {
                                goToWishlistCollectionDetailShowCleanerBottomSheet(COLLECTION_ID_SEMUA_WISHLIST)
                            }
                        }
                    }
                }

                override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {}

                override fun onSuccessRemoveWishlist(
                    result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
                    productId: String
                ) {}
            },
            sourceCollectionId = collectionId
        )
    }

    private fun showBulkAddConfirmationDialog() {
        val dialog =
            context?.let { DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.setTitle(getString(Rv2.string.collection_item_add_bulk_confirmation_title, listSelectedProductIds.size, collectionNameDestination))
        dialog?.setPrimaryCTAText(getString(Rv2.string.collection_bulk_add_button))
        dialog?.setPrimaryCTAClickListener {
            dialog.dismiss()
            doSaveBulkItems()
        }
        dialog?.setSecondaryCTAText(getString(Rv2.string.wishlist_cancel_manage_label))
        dialog?.setSecondaryCTAClickListener {
            dialog.dismiss()
            WishlistV2Analytics.clickBatalOnPopUpMultipleWishlistProduct()
        }
        dialog?.show()
    }

    private fun showBulkAddFromOtherCollectionConfirmationDialog() {
        val dialog =
            context?.let { DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.setTitle(getString(Rv2.string.collection_item_add_bulk_from_other_collection_confirmation_title, listSelectedProductIdsFromOtherCollection.size))
        dialog?.setPrimaryCTAText(getString(Rv2.string.wishlist_save_label))
        dialog?.setPrimaryCTAClickListener {
            dialog.dismiss()
            _isNeedRefreshAndTurnOffBulkModeFromOthers = true
            doAddWishlistBulk()
        }
        dialog?.setSecondaryCTAText(getString(Rv2.string.wishlist_cancel_manage_label))
        dialog?.setSecondaryCTAClickListener {
            dialog.dismiss()
        }
        dialog?.show()
    }

    private fun doAddWishlistBulk() {
        val paramCollectionSharing = AddWishlistBulkParams.CollectionSharing(
            sourceCollectionId = collectionId
        )
        val addBulkParams = AddWishlistBulkParams(
            listProductId = listSelectedProductIdsFromOtherCollection,
            userId = userSession.userId,
            collectionSharing = paramCollectionSharing
        )
        wishlistCollectionDetailViewModel.addWishlistBulk(addBulkParams)
    }

    private fun doSaveBulkItems() {
        val param = AddWishlistCollectionsHostBottomSheetParams(
            collectionId = collectionIdDestination,
            productIds = listSelectedProductIds,
            collectionName = collectionNameDestination
        )
        wishlistCollectionDetailViewModel.saveNewWishlistCollection(param)
    }

    private fun showDeleteConfirmationDialog(productId: String) {
        val dialog =
            context?.let { DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.setTitle(getString(Rv2.string.collection_item_delete_confirmation_title))
        dialog?.setDescription(getString(Rv2.string.collection_item_delete_confirmation_desc))
        dialog?.setPrimaryCTAText(getString(Rv2.string.wishlist_delete_label))
        dialog?.setPrimaryCTAClickListener {
            dialog.dismiss()
            doDeleteSingleWishlistItem(productId)
        }
        dialog?.setSecondaryCTAText(getString(Rv2.string.wishlist_cancel_manage_label))
        dialog?.setSecondaryCTAClickListener {
            dialog.dismiss()
            WishlistV2Analytics.clickBatalOnPopUpMultipleWishlistProduct()
        }
        dialog?.show()
    }

    private fun showDeleteCollectionItemConfirmationDialog(count: Int, productId: String) {
        var titleDialog = getString(Rv2.string.single_collection_inside_delete_confirmation_title)
        if (count > 1) titleDialog = getString(Rv2.string.collection_inside_delete_confirmation_title, count)
        val dialog =
            context?.let { DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.setTitle(titleDialog)
        dialog?.setDescription(getString(Rv2.string.collection_inside_delete_confirmation_desc))
        dialog?.setPrimaryCTAText(getString(Rv2.string.collection_inside_delete_confirmation_button_primary))
        dialog?.setPrimaryCTAClickListener {
            dialog.dismiss()
            doDeleteCollectionItems(productId)
        }
        dialog?.setSecondaryCTAText(getString(Rv2.string.collection_inside_delete_confirmation_button_secondary))
        dialog?.setSecondaryCTAClickListener {
            dialog.dismiss()
            doDeleteBulkCollectionItems(productId)
        }
        dialog?.show()
    }

    private fun showDeleteCollectionItemConfirmationDialog(count: Int, productId: List<String>) {
        val dialog =
            context?.let { DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.setTitle(getString(Rv2.string.collection_inside_delete_confirmation_title, count))
        dialog?.setDescription(getString(Rv2.string.collection_inside_delete_confirmation_desc))
        dialog?.setPrimaryCTAText(getString(Rv2.string.collection_inside_delete_confirmation_button_primary))
        dialog?.setPrimaryCTAClickListener {
            dialog.dismiss()
            doDeleteCollectionItems(productId)
            WishlistCollectionAnalytics.sendClickHapusDariKoleksiSajaOnMultipleWishlistedProductsEvent()
        }
        dialog?.setSecondaryCTAText(getString(Rv2.string.collection_inside_delete_confirmation_button_secondary))
        dialog?.setSecondaryCTAClickListener {
            dialog.dismiss()
            doDeleteBulkCollectionItems(productId)
            WishlistCollectionAnalytics.sendClickHapusDariSemuaWishlistOnMulitpleWishlistedProductsEvent()
        }
        dialog?.show()
    }

    private fun doDeleteSingleWishlistItem(productId: String) {
        wishlistCollectionDetailViewModel.deleteWishlistV2(
            productId = productId,
            userId = userSession.userId
        )
    }

    private fun doDeleteBulkCollectionItems(productId: String) {
        val source = if (collectionId == "0") "" else SOURCE_COLLECTION
        val listProduct = arrayListOf<String>()
        listProduct.add(productId)
        wishlistCollectionDetailViewModel.bulkDeleteWishlistV2(
            listProduct,
            userSession.userId,
            bulkDeleteMode,
            bulkDeleteAdditionalParams,
            source
        )
    }

    private fun doDeleteBulkCollectionItems(listProductId: List<String>) {
        val source = if (collectionId == "0") "" else SOURCE_COLLECTION
        wishlistCollectionDetailViewModel.bulkDeleteWishlistV2(
            listProductId,
            userSession.userId,
            bulkDeleteMode,
            bulkDeleteAdditionalParams,
            source
        )
    }

    private fun doDeleteCollectionItems(productId: String) {
        val listProduct = arrayListOf<String>()
        listProduct.add(productId)
        wishlistCollectionDetailViewModel.deleteWishlistCollectionItems(listProduct)
    }

    private fun doDeleteCollectionItems(listProductId: List<String>) {
        wishlistCollectionDetailViewModel.deleteWishlistCollectionItems(listProductId)
    }

    private fun showBottomSheetCleaner(cleanerBottomSheet: WishlistV2UiModel.StorageCleanerBottomSheet) {
        val bottomSheetCleaner = WishlistV2CleanerBottomSheet.newInstance(
            cleanerBottomSheet.title,
            cleanerBottomSheet.description,
            cleanerBottomSheet.btnCleanBottomSheet.text
        )
        if (bottomSheetCleaner.isAdded || childFragmentManager.isStateSaved) return

        val cleanerAdapter = WishlistV2CleanerBottomSheetAdapter()
        cleanerAdapter.cleanerBottomSheet = cleanerBottomSheet

        bottomSheetCleaner.setAdapter(cleanerAdapter)
        bottomSheetCleaner.setListener(object :
                WishlistV2CleanerBottomSheet.BottomsheetCleanerListener {
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
                    WishlistCollectionAnalytics.sendClickLihatBarangButtonOnCleanerBottomsheetEvent(
                        if (index == 1) OPTION_CLEANER_AUTOMATIC else OPTION_CLEANER_MANUAL
                    )
                    onTickerCTASortFromLatest()
                    turnOnBulkDeleteMode(true)
                    view?.let {
                        Toaster.build(
                            it,
                            getString(Rv2.string.wishlist_v2_terlama_disimpan),
                            Toaster.LENGTH_SHORT,
                            Toaster.TYPE_NORMAL
                        ).show()
                    }
                }
            })
        bottomSheetCleaner.show(childFragmentManager)
    }

    private fun showShareBottomSheet(wishlistItem: WishlistV2UiModel.Item) {
        var universalShareBottomSheet: UniversalShareBottomSheet? = null
        val shareListener = object : ShareBottomsheetListener {

            override fun onShareOptionClicked(shareModel: ShareModel) {
                val linkerShareResult = DataMapper.getLinkerShareData(
                    LinkerData().apply {
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
                    }
                )

                LinkerManager.getInstance().executeShareRequest(
                    LinkerUtils.createShareRequest(
                        0,
                        linkerShareResult,
                        object : ShareCallback {
                            override fun urlCreated(linkerShareResult: LinkerShareResult?) {
                                val shareString = getString(
                                    Rv2.string.wishlist_v2_share_text,
                                    wishlistItem.name,
                                    wishlistItem.priceFmt,
                                    wishlistItem.shop.name
                                ) + "\n${linkerShareResult?.url}"
                                shareModel.subjectName = userSession.shopName
                                SharingUtil.executeShareIntent(
                                    shareModel,
                                    linkerShareResult,
                                    activity,
                                    view,
                                    shareString
                                )
                                shareModel.channel?.let { ch ->
                                    WishlistV2Analytics.clickSharingChannel(
                                        wishlistId = wishlistItem.wishlistId,
                                        productId = wishlistItem.id,
                                        userId = userSession.userId,
                                        channel = ch
                                    )
                                }
                                universalShareBottomSheet?.dismiss()
                            }

                            override fun onError(linkerError: LinkerError?) {}
                        }
                    )
                )
            }

            override fun onCloseOptionClicked() {
                WishlistV2Analytics.clickCloseShareBottomSheet(
                    wishlistId = wishlistItem.wishlistId,
                    productId = wishlistItem.id,
                    userId = userSession.userId
                )
            }
        }

        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(shareListener)
            setMetaData(
                wishlistItem.name,
                wishlistItem.imageUrl
            )
        }
        universalShareBottomSheet.show(childFragmentManager, this@WishlistCollectionDetailFragment)
        WishlistV2Analytics.viewOnSharingChannel(
            wishlistId = wishlistItem.wishlistId,
            productId = wishlistItem.id,
            userId = userSession.userId
        )
    }

    private fun showToasterWithCTA(message: String, actionText: String, type: Int, listener: View.OnClickListener) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.build(v, message, Toaster.LENGTH_LONG, type, actionText, listener).show()
        }
    }

    private fun showIndefiniteToasterWithCTA(message: String, actionText: String, type: Int, listener: View.OnClickListener) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.build(v, message, Toaster.LENGTH_LONG, type, actionText, listener).show()
        }
    }

    private fun showToaster(message: String, actionText: String, type: Int) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.build(v, message, Toaster.LENGTH_LONG, type, actionText).show()
        }
    }

    private fun showToasterActionLihat(message: String, type: Int, collectionId: String) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.build(
                v,
                message,
                Toaster.LENGTH_LONG,
                type,
                getString(Rv2.string.collection_CTA_lihat)
            ) {
                goToWishlistCollection(collectionId)
            }.show()
        }
    }

    private fun showToasterActionOke(message: String, type: Int) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.build(
                v,
                message,
                Toaster.LENGTH_LONG,
                type,
                getString(Rv2.string.collection_CTA_oke)
            ) {}.show()
        }
    }

    private fun showToasterAtc(message: String, type: Int) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.build(v, message, Toaster.LENGTH_SHORT, type, CTA_ATC) {
                WishlistCollectionAnalytics.sendClickLihatButtonOnAtcSuccessToasterEvent()
                goToCartPage()
            }.show()
        }
    }

    private fun goToWishlistCollection(collectionId: String) {
        val intentCollectionDetail = RouteManager.getIntent(context, WISHLIST_COLLECTION_DETAIL_INTERNAL, collectionId)
        intentCollectionDetail.putExtra(EXTRA_IS_BULK_ADD, false)
        startActivityForResult(intentCollectionDetail, REQUEST_CODE_GO_TO_COLLECTION_DETAIL)
    }

    private fun goToWishlistCollectionDetailShowCleanerBottomSheet(collectionId: String) {
        turnOffBulkAddFromOtherCollection()
        val intentCollectionDetail = RouteManager.getIntent(context, WISHLIST_COLLECTION_DETAIL_INTERNAL, collectionId)
        intentCollectionDetail.putExtra(EXTRA_IS_SHOW_CLEANER_BOTTOMSHEET, true)
        startActivityForResult(intentCollectionDetail, REQUEST_CODE_GO_TO_COLLECTION_DETAIL)
    }

    private fun goToCartPage() {
        RouteManager.route(context, ApplinkConst.CART)
    }

    override fun onCariBarangClicked() {
        RouteManager.route(context, ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE)
        WishlistV2Analytics.clickCariBarangOnEmptyStateNoItems()
    }

    override fun onNotFoundButtonClicked(keyword: String) {
        RouteManager.route(context, "${ApplinkConst.DISCOVERY_SEARCH}?q=$keyword")
        WishlistCollectionAnalytics.sendClickCariDiTokopediaButtonOnEmptyStateNoSearchResultEvent()
    }

    override fun onProductItemClicked(wishlistItem: WishlistV2UiModel.Item, position: Int) {
        WishlistCollectionAnalytics.sendClickProductCardOnWishlistPageEvent(
            collectionId,
            wishlistItem,
            userSession.userId,
            position
        )

        activity?.let {
            val intent: Intent
            if (wishlistItem.url.isNotEmpty()) {
                intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, wishlistItem.id)
                intent.data = Uri.parse(wishlistItem.url)
            } else {
                intent = RouteManager.getIntent(
                    it,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    wishlistItem.id
                )
            }
            startActivityForResult(intent, REQUEST_CODE_GO_TO_PDP)
        }
    }

    override fun onViewProductCard(wishlistItem: WishlistV2UiModel.Item, position: Int) {
        userSession.userId?.let { userId ->
            WishlistCollectionAnalytics.sendViewProductCardOnWishlistPageEvent(
                wishlistItem,
                userId,
                position.toString()
            )
        }
    }

    override fun onBannerTopAdsImpression(
        topAdsImageViewModel: TopAdsImageViewModel,
        position: Int
    ) {
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
        RouteManager.route(context, topAdsImageViewModel.applink)
    }

    override fun onRecommendationItemImpression(
        recommendationItem: RecommendationItem,
        position: Int
    ) {
        if (recommendationItem.isTopAds) {
            TopAdsUrlHitter(context).hitImpressionUrl(
                this::class.java.simpleName,
                recommendationItem.trackerImageUrl,
                recommendationItem.productId.toString(),
                recommendationItem.name,
                recommendationItem.imageUrl
            )
        }
        WishlistV2Analytics.impressionEmptyWishlistRecommendation(
            trackingQueue,
            recommendationItem,
            position
        )
    }

    override fun onRecommendationItemClick(recommendationItem: RecommendationItem, position: Int) {
        WishlistV2Analytics.clickRecommendationItem(
            recommendationItem,
            position,
            userSession.userId
        )
        if (recommendationItem.isTopAds) {
            TopAdsUrlHitter(context).hitClickUrl(
                this::class.java.simpleName,
                recommendationItem.clickUrl,
                recommendationItem.productId.toString(),
                recommendationItem.name,
                recommendationItem.imageUrl
            )
        }
        activity?.let {
            if (recommendationItem.appUrl.isNotEmpty()) {
                RouteManager.route(it, recommendationItem.appUrl)
            } else {
                val intent = RouteManager.getIntent(
                    it,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    recommendationItem.productId.toString()
                )
                startActivity(intent)
            }
        }
    }

    override fun onRecommendationCarouselItemImpression(
        recommendationItem: RecommendationItem,
        position: Int
    ) {
        if (recommendationItem.isTopAds) {
            TopAdsUrlHitter(context).hitImpressionUrl(
                this::class.java.simpleName,
                recommendationItem.trackerImageUrl,
                recommendationItem.productId.toString(),
                recommendationItem.name,
                recommendationItem.imageUrl
            )
        }
        WishlistV2Analytics.impressionCarouselRecommendationItem(
            trackingQueue,
            recommendationItem,
            position
        )
    }

    override fun onRecommendationCarouselItemClick(
        recommendationItem: RecommendationItem,
        position: Int
    ) {
        WishlistV2Analytics.clickCarouselRecommendationItem(
            recommendationItem,
            position,
            userSession.userId
        )
        if (recommendationItem.isTopAds) {
            TopAdsUrlHitter(context).hitClickUrl(
                this::class.java.simpleName,
                recommendationItem.clickUrl,
                recommendationItem.productId.toString(),
                recommendationItem.name,
                recommendationItem.imageUrl
            )
        }
        activity?.let {
            val intent = RouteManager.getIntent(
                it,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                recommendationItem.productId.toString()
            )
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
                name = FILTER_SORT,
                selected = listOptionIdSelected as ArrayList<String>
            )
        )

        doRefresh()
    }

    override fun onTickerCloseIconClicked() {
        collectionItemsAdapter.hideTicker()
    }

    override fun goToWishlistAllToAddCollection() {
        WishlistCollectionAnalytics.sendClickTambahBarangKeKoleksiOnEmptyStateNoCollectionItemsEvent()
        val intentCollectionDetail = RouteManager.getIntent(context, WISHLIST_COLLECTION_DETAIL_INTERNAL, "0")
        intentCollectionDetail.putExtra(EXTRA_IS_BULK_ADD, true)
        intentCollectionDetail.putExtra(EXTRA_COLLECTION_ID_DESTINATION, collectionId)
        intentCollectionDetail.putExtra(EXTRA_COLLECTION_NAME_DESTINATION, collectionName)
        startActivityForResult(intentCollectionDetail, REQUEST_CODE_GO_TO_SEMUA_WISHLIST)
    }

    override fun onChangeCollectionName() {
        showUpdateWishlistCollectionNameBottomSheet(collectionId, collectionName)
        WishlistCollectionAnalytics.sendClickUbahNamaKoleksiButtonOnEmptyStateNoCollectionItemsEvent()
    }

    override fun goToMyWishlist() {
        RouteManager.route(context, ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION)
    }

    override fun goToHome() {
        RouteManager.route(context, ApplinkConst.HOME)
    }

    override fun goToEditWishlistCollectionPage() {
        goToEditCollectionPage()
    }

    private fun showUpdateWishlistCollectionNameBottomSheet(
        collectionId: String,
        collectionName: String
    ) {
        val bottomSheetUpdateWishlistCollectionName =
            BottomSheetUpdateWishlistCollectionName.newInstance(collectionId, collectionName)
        bottomSheetUpdateWishlistCollectionName.setListener(this@WishlistCollectionDetailFragment)
        if (bottomSheetUpdateWishlistCollectionName.isAdded || childFragmentManager.isStateSaved) return
        bottomSheetUpdateWishlistCollectionName.show(childFragmentManager)
    }

    override fun onSuccessUpdateCollectionName(message: String) {
        setRefreshing()
        showToasterActionOke(message, Toaster.TYPE_NORMAL)
    }

    override fun onThreeDotsMenuClicked(itemWishlist: WishlistV2UiModel.Item) {
        showBottomSheetThreeDotsMenu(itemWishlist)
        WishlistCollectionAnalytics.sendClickThreeDotsOnProductCardEvent()
    }

    private fun setBottomButton() {
        val showButton = listSelectedProductIds.isNotEmpty() || listSelectedProductIdsFromOtherCollection.isNotEmpty()

        if (showButton) {
            if (isBulkAddShow) {
                setBulkAddButton()
            } else if (isBulkAddFromOtherCollectionShow) {
                setBulkAddFromOtherCollectionButton()
            } else {
                setLabelDeleteButton()
            }
        } else {
            if (isBulkAddShow) {
                setDefaultAddCollectionButton()
            } else if (isBulkAddFromOtherCollectionShow) {
                setDefaultAddCollectionFromOthersButton()
            } else {
                setDefaultLabelDeleteButton()
            }
        }
    }

    override fun onCheckBulkOption(productId: String, isChecked: Boolean, position: Int) {
        if (isChecked) {
            listSelectedProductIds.add(productId)
        } else {
            listSelectedProductIds.remove(productId)
        }
        collectionItemsAdapter.setCheckbox(position, isChecked)
        setBottomButton()
    }

    override fun onValidateCheckBulkOption(productId: String, isChecked: Boolean, position: Int) {
        if (!isChecked) {
            if (validateCheckedItem()) {
                listSelectedProductIdsFromOtherCollection.add(productId)
                collectionItemsAdapter.setCheckbox(position, true)
                setBottomButton()
            } else {
                showToasterActionOke(message = _toasterMaxBulk, Toaster.TYPE_ERROR)
            }
        } else {
            listSelectedProductIdsFromOtherCollection.remove(productId)
            collectionItemsAdapter.setCheckbox(position, false)
            setBottomButton()
        }
    }

    private fun validateCheckedItem(): Boolean {
        return (listSelectedProductIdsFromOtherCollection.size < _maxBulk && _toasterMaxBulk.isNotEmpty())
    }

    private fun setLabelDeleteButton() {
        if (!_isDeleteOnly) {
            // semua wishlist - 2 buttons : delete & add
            if (collectionId == "0") {
                binding?.run {
                    bottomButtonLayout.visible()
                    containerDeleteCollectionDetail.gone()
                    containerDeleteSemuaWishlist.visible()
                    deleteButtonCollection.apply {
                        isEnabled = true
                        if (listSelectedProductIds.isNotEmpty()) {
                            text =
                                getString(
                                    Rv2.string.wishlist_v2_delete_text_counter,
                                    listSelectedProductIds.size
                                )
                            setOnClickListener {
                                showPopupBulkDeleteConfirmation(listSelectedProductIds.size)
                            }
                        }
                    }
                    addButtonCollection.apply {
                        isEnabled = true
                        if (listSelectedProductIds.isNotEmpty()) {
                            text =
                                getString(
                                    Rv2.string.add_collection_text_counter,
                                    listSelectedProductIds.size
                                )
                            setOnClickListener {
                                showBottomSheetCollection(
                                    childFragmentManager,
                                    listSelectedProductIds.joinToString(),
                                    SRC_WISHLIST_COLLECTION_BULK_ADD
                                )
                            }
                        }
                    }
                }
            } else {
                setLabelDeleteOnlyButton()
            }
        } else {
            setLabelDeleteOnlyButton()
        }
    }

    private fun setLabelDeleteOnlyButton() {
        // collection - 1 button : delete
        binding?.run {
            containerDeleteSemuaWishlist.gone()
            containerDeleteCollectionDetail.visible()
            deleteButtonCollectionDetail.apply {
                isEnabled = true
                if (collectionId == "0") buttonVariant = UnifyButton.Variant.FILLED
                if (listSelectedProductIds.isNotEmpty()) {
                    text =
                        getString(
                            Rv2.string.wishlist_v2_delete_text_counter,
                            listSelectedProductIds.size
                        )
                    setOnClickListener {
                        if (collectionId == "0") {
                            showPopupBulkDeleteConfirmation(listSelectedProductIds.size)
                        } else {
                            showDeleteCollectionItemConfirmationDialog(
                                listSelectedProductIds.size,
                                listSelectedProductIds
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setDefaultAddCollectionButton() {
        binding?.run {
            bottomButtonLayout.visible()
            containerDeleteCollectionDetail.gone()
            containerDeleteSemuaWishlist.gone()
            containerAddBulk.visible()
            bulkAddButton.isEnabled = false
            bulkAddButton.text = getString(Rv2.string.collection_bulk_add_default)
        }
    }

    private fun setDefaultAddCollectionFromOthersButton() {
        binding?.run {
            bottomButtonLayout.visible()
            containerDeleteCollectionDetail.gone()
            containerDeleteSemuaWishlist.gone()
            containerAddBulk.visible()
            bulkAddButton.isEnabled = false
            bulkAddButton.text = getString(Rv2.string.add_collection_bulk_from_other_collection_default_label)
        }
    }

    private fun hideBottomButtonLayout() {
        binding?.run { bottomButtonLayout.gone() }
    }

    private fun setDefaultLabelDeleteButton() {
        binding?.run {
            bottomButtonLayout.visible()
            if (!_isDeleteOnly) {
                if (collectionId == "0") {
                    containerDeleteCollectionDetail.gone()
                    containerDeleteSemuaWishlist.visible()
                    deleteButtonCollection.isEnabled = false
                    deleteButtonCollection.text = getString(Rv2.string.wishlist_v2_delete_text)
                    addButtonCollection.isEnabled = false
                    addButtonCollection.text = getString(Rv2.string.add_collection_text)
                } else {
                    defaultLabelDeleteOnlyButton()
                }
            } else {
                defaultLabelDeleteOnlyButton()
            }
        }
    }

    private fun defaultLabelDeleteOnlyButton() {
        binding?.run {
            containerDeleteSemuaWishlist.gone()
            containerDeleteCollectionDetail.visible()
            deleteButtonCollectionDetail.isEnabled = false
            deleteButtonCollectionDetail.text = getString(Rv2.string.wishlist_v2_delete_text)
            if (collectionId == "0") {
                deleteButtonCollectionDetail.buttonVariant =
                    UnifyButton.Variant.FILLED
            }
        }
    }

    private fun setBulkAddButton() {
        binding?.run {
            containerDeleteSemuaWishlist.gone()
            containerDeleteCollectionDetail.gone()
            bottomButtonLayout.visible()
            containerAddBulk.visible()
            bulkAddButton.apply {
                isEnabled = true
                buttonVariant = UnifyButton.Variant.FILLED
                if (listSelectedProductIds.isNotEmpty()) {
                    text =
                        getString(
                            Rv2.string.collection_bulk_add,
                            listSelectedProductIds.size
                        )
                    setOnClickListener {
                        showBulkAddConfirmationDialog()
                    }
                }
            }
        }
    }

    private fun setBulkAddFromOtherCollectionButton() {
        binding?.run {
            containerDeleteSemuaWishlist.gone()
            containerDeleteCollectionDetail.gone()
            bottomButtonLayout.visible()
            containerAddBulk.visible()
            bulkAddButton.apply {
                isEnabled = true
                buttonVariant = UnifyButton.Variant.FILLED
                if (listSelectedProductIdsFromOtherCollection.isNotEmpty()) {
                    text =
                        getString(
                            Rv2.string.add_collection_bulk_from_other_collection_label,
                            listSelectedProductIdsFromOtherCollection.size
                        )
                    setOnClickListener {
                        checkCollectionType(CHECK_COLLECTION_TYPE_FOR_DIALOG_CONFIRMATION)
                    }
                }
            }
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
            val countExistingRemovable = countRemovableAutomaticDelete - listExcludedBulkDelete.size

            deleteButtonCollectionDetail.apply {
                isEnabled = true
                text = getString(Rv2.string.wishlist_v2_delete_text_counter, countExistingRemovable)
                setOnClickListener {
                    bulkDeleteAdditionalParams = WishlistV2BulkRemoveAdditionalParams(
                        listExcludedBulkDelete,
                        countRemovableAutomaticDelete.toLong()
                    )
                    showPopupBulkDeleteConfirmation(countExistingRemovable)
                }
            }
        }
    }

    override fun onAtc(wishlistItem: WishlistV2UiModel.Item, position: Int) {
        wishlistItemOnAtc = wishlistItem
        indexOnAtc = position

        if (userSession.isLoggedIn) {
            doAtc()
        } else {
            goToLoginPage()
        }
    }

    private fun goToLoginPage() {
        startActivityForResult(
            RouteManager.getIntent(context, ApplinkConst.LOGIN),
            REQUEST_CODE_LOGIN
        )
    }

    private fun doAtc() {
        showLoadingDialog()
        val atcParam = AddToCartRequestParams(
            productId = wishlistItemOnAtc.id,
            productName = wishlistItemOnAtc.name,
            price = wishlistItemOnAtc.originalPriceFmt,
            quantity = wishlistItemOnAtc.minOrder.toIntOrZero(),
            shopId = wishlistItemOnAtc.shop.id,
            atcFromExternalSource = AtcFromExternalSource.ATC_FROM_WISHLIST
        )
        wishlistCollectionDetailViewModel.doAtc(atcParam)
    }

    override fun onCheckSimilarProduct(url: String) {
        RouteManager.route(context, url)
        WishlistCollectionAnalytics.sendClickLihatBarangSerupaOnProductCardEvent()
    }

    override fun onResetFilter() {
        doResetFilter()
        WishlistCollectionAnalytics.sendClickResetFilterButtonOnEmptyStateNoFilterResultEvent()
    }

    private fun doResetFilter() {
        clearFilters()
        doRefresh()
    }

    private fun clearFilters() {
        binding?.run {
            wishlistCollectionDetailSortFilter.run {
                resetAllFilters()
                paramGetCollectionItems = GetWishlistCollectionItemsParams()
                /*var inCollection = ""
                if (collectionId.isNotEmpty() && collectionId != "0") {
                    inCollection = "inside"
                }
                paramGetCollectionItems.inCollection = inCollection*/
                if (collectionId != "0") {
                    paramGetCollectionItems.collectionId = collectionId
                }
            }
        }
    }

    private fun removeFilter(filterItem: WishlistV2Response.Data.WishlistV2.SortFiltersItem) {
        paramGetCollectionItems.sortFilters.removeAll { it.name == filterItem.name }
        doRefresh()
    }

    private fun onStickyManageClicked() {
        isAutoDeletion = false
        if (!isBulkDeleteShow) {
            turnOnBulkDeleteMode(false)
            binding?.run {
                wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailManageLabel.text =
                    getString(Rv2.string.wishlist_cancel_manage_label)
            }
            WishlistV2Analytics.clickAturOnWishlist()
        } else {
            turnOffBulkDeleteMode()
        }
    }

    private fun onCollectionSettingsClicked(collectionId: String, collectionName: String) {
        bottomSheetCollectionSettings =
            BottomSheetWishlistCollectionSettings.newInstance(collectionName, collectionId, collectionType, listSettingButtons)
        bottomSheetCollectionSettings.setListener(this@WishlistCollectionDetailFragment)
        if (bottomSheetCollectionSettings.isAdded || childFragmentManager.isStateSaved) return
        bottomSheetCollectionSettings.show(childFragmentManager)
    }

    private fun turnOnBulkDeleteMode(isDeleteOnly: Boolean) {
        _isDeleteOnly = isDeleteOnly
        binding?.run {
            wishlistCollectionDetailStickyCountManageLabel.apply {
                iconGearCollectionDetail.gone()
                wishlistCollectionDetailManageLabel.visible()
                wishlistCollectionDetailManageLabel.text =
                    getString(Rv2.string.wishlist_cancel_manage_label)
                wishlistCollectionDetailManageLabel.setOnClickListener { turnOffBulkDeleteMode() }
            }
        }
        isBulkDeleteShow = true
        onManageClicked(showCheckbox = true, isDeleteOnly, false)
        updateToolbarTitle(toolbarTitle)
    }

    private fun turnOnBulkAddMode() {
        binding?.run {
            wishlistCollectionDetailStickyCountManageLabel.apply {
                llAturDanLayout.visible().also {
                    iconGearCollectionDetail.gone()
                    wishlistCollectionDetailManageLabel.visible()
                    wishlistCollectionDetailManageLabel.text =
                        getString(Rv2.string.wishlist_cancel_manage_label)
                    wishlistCollectionDetailManageLabel.setOnClickListener {
                        isBulkAddShow = false
                        activity?.finish()
                    }
                }
                llTotalBarang.visible()
            }
        }
        isBulkDeleteShow = true
        onManageClicked(showCheckbox = true, isDeleteOnly = false, isBulkAdd = true)
        setDefaultAddCollectionButton()
    }

    private fun turnOnBulkAddFromOtherCollectionsMode() {
        listSelectedProductIdsFromOtherCollection.clear()
        binding?.run {
            wishlistCollectionDetailStickyCountManageLabel.apply {
                llAturDanLayout.visible().also {
                    iconGearCollectionDetail.gone()
                    wishlistCollectionSelectItemOption.gone()
                    wishlistCollectionDetailManageLabel.visible()
                    wishlistCollectionDetailManageLabel.text =
                        getString(Rv2.string.wishlist_cancel_manage_label)
                    wishlistCollectionDetailManageLabel.setOnClickListener {
                        isBulkAddFromOtherCollectionShow = false
                        turnOffBulkAddFromOtherCollection()
                    }
                }
                llTotalBarang.visible()
            }
        }
        onPilihBarangClicked()
        updateCustomToolbarTitleAndSubTitle(getString(Rv2.string.collection_other_select_items_label, toolbarTitle), toolbarDesc)
    }

    private fun turnOffBulkMode() {
        onManageClicked(showCheckbox = false, isDeleteOnly = false, isBulkAdd = false)
        binding?.run {
            if (collectionId == "0") {
                wishlistCollectionDetailStickyCountManageLabel.apply {
                    iconGearCollectionDetail.gone()
                    wishlistCollectionDetailManageLabel.show()
                    wishlistCollectionDetailManageLabel.text =
                        getString(Rv2.string.wishlist_manage_label)
                    wishlistCollectionDetailManageLabel.setOnClickListener { onStickyManageClicked() }
                }
            } else {
                wishlistCollectionDetailStickyCountManageLabel.apply {
                    wishlistCollectionDetailManageLabel.gone()
                    iconGearCollectionDetail.show()
                    iconGearCollectionDetail.setOnClickListener {
                        onCollectionSettingsClicked(
                            collectionId,
                            collectionName
                        )
                        WishlistCollectionAnalytics.sendClickGearIconEvent()
                    }
                }
            }
        }
    }

    private fun turnOffBulkDeleteMode() {
        isBulkDeleteShow = false
        turnOffBulkMode()
        if (isToolbarHasDesc) {
            updateCustomToolbarTitleAndSubTitle(toolbarTitle, toolbarDesc)
        } else {
            updateToolbarTitle(toolbarTitle)
        }
    }

    private fun onPilihBarangClicked() {
        isBulkAddFromOtherCollectionShow = true
        disableSwipeRefreshLayout()
        listSelectedProductIds.clear()
        collectionItemsAdapter.showCheckboxAddBulkFromOthers()
        showSearchBar()
        showFilter()
        setBottomButton()
        binding?.run {
            wishlistCollectionDetailStickyCountManageLabel.wishlistDivider.gone()
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailTypeLayoutIcon.gone()
        }
    }

    override fun onManageClicked(showCheckbox: Boolean, isDeleteOnly: Boolean, isBulkAdd: Boolean) {
        if (showCheckbox) {
            disableSwipeRefreshLayout()
            listSelectedProductIds.clear()
            listExcludedBulkDelete.clear()
            collectionItemsAdapter.showCheckbox(isAutoDeletion)
            binding?.run {
                if (isAutoDeletion) {
                    hideSearchBar()
                    hideFilter()
                } else {
                    showSearchBar()
                    showFilter()
                }
                wishlistCollectionDetailStickyCountManageLabel.wishlistDivider.gone()
                wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailTypeLayoutIcon.gone()
                bottomButtonLayout.visible()
                if (!isDeleteOnly) {
                    isAturMode = true
                    if (collectionId == "0") {
                        containerAddBulk.gone()
                        containerDeleteCollectionDetail.gone()
                        containerDeleteSemuaWishlist.visible()
                        deleteButtonCollection.apply {
                            isEnabled = isAutoDeletion
                            text = if (isAutoDeletion) {
                                getString(
                                    Rv2.string.wishlist_v2_delete_text_counter,
                                    countRemovableAutomaticDelete
                                )
                            } else {
                                getString(Rv2.string.wishlist_v2_delete_text)
                            }
                            if (isAutoDeletion) {
                                setOnClickListener {
                                    bulkDeleteAdditionalParams =
                                        WishlistV2BulkRemoveAdditionalParams(
                                            listExcludedBulkDelete,
                                            countRemovableAutomaticDelete.toLong()
                                        )
                                    showPopupBulkDeleteConfirmation(
                                        countRemovableAutomaticDelete
                                    )
                                }
                            }
                        }
                        addButtonCollection.apply {
                            isEnabled = isAutoDeletion
                            text = if (isAutoDeletion) {
                                getString(
                                    Rv2.string.add_collection_text_counter,
                                    countRemovableAutomaticDelete
                                )
                            } else {
                                getString(Rv2.string.add_collection_text)
                            }
                            if (isAutoDeletion) {
                                setOnClickListener {
                                    showBottomSheetCollection(
                                        childFragmentManager,
                                        listSelectedProductIds.toString(),
                                        SRC_WISHLIST_COLLECTION
                                    )
                                }
                            }
                        }
                    } else {
                        showButtonDeleteOnly()
                    }
                } else {
                    showButtonDeleteOnly()
                }
            }
        } else {
            isAturMode = false
            collectionItemsAdapter.hideCheckbox()
            setSwipeRefreshLayout()
            binding?.run {
                containerDeleteSemuaWishlist.gone()
                containerAddBulk.gone()
                containerDeleteCollectionDetail.gone()
                showSearchBar()
                showFilter()
                wishlistCollectionDetailStickyCountManageLabel.wishlistDivider.visible()
                wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailTypeLayoutIcon.visible()
            }
            if (isAutoDeletion) {
                paramGetCollectionItems.source = SRC_WISHLIST_COLLECTION
                doRefresh()
            }
        }
    }

    private fun showButtonDeleteOnly() {
        binding?.run {
            containerDeleteSemuaWishlist.gone()
            containerAddBulk.gone()
            containerDeleteCollectionDetail.visible()
            deleteButtonCollectionDetail.apply {
                isEnabled = isAutoDeletion
                if (collectionId == "0") buttonVariant = UnifyButton.Variant.FILLED
                text = if (isAutoDeletion) {
                    getString(
                        Rv2.string.wishlist_v2_delete_text_counter,
                        countRemovableAutomaticDelete
                    )
                } else {
                    getString(Rv2.string.wishlist_v2_delete_text)
                }
                if (isAutoDeletion) {
                    setOnClickListener {
                        bulkDeleteAdditionalParams = WishlistV2BulkRemoveAdditionalParams(
                            listExcludedBulkDelete,
                            countRemovableAutomaticDelete.toLong()
                        )
                        showPopupBulkDeleteConfirmation(countRemovableAutomaticDelete)
                    }
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
            showFilter()
            showSearchBar()
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailManageLabel.text =
                getString(Rv2.string.wishlist_manage_label)
            wishlistCollectionDetailStickyCountManageLabel.wishlistDivider.visible()
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailTypeLayoutIcon.visible()
        }
    }

    private fun showPopupBulkDeleteConfirmation(count: Int) {
        val dialog =
            context?.let { DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.setTitle(getString(Rv2.string.wishlist_collection_popup_delete_bulk_title, count))
        dialog?.setDescription(getString(Rv2.string.wishlist_v2_popup_delete_bulk_desc))
        dialog?.setPrimaryCTAText(getString(Rv2.string.wishlist_delete_label))
        dialog?.setPrimaryCTAClickListener {
            dialog.dismiss()
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
        val source = if (collectionId == "0") SRC_WISHLIST_COLLECTION else SOURCE_COLLECTION
        wishlistCollectionDetailViewModel.bulkDeleteWishlistV2(
            listSelectedProductIds,
            userSession.userId,
            bulkDeleteMode,
            bulkDeleteAdditionalParams,
            source
        )
        WishlistCollectionAnalytics.sendClickHapusButtonOnConfirmationDeletionPopupEvent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                getCollectionItems()
            } else {
                activity?.finish()
            }
        } else if (requestCode == ApplinkConstInternalPurchasePlatform.REQUEST_CODE_ADD_WISHLIST_COLLECTION && resultCode == Activity.RESULT_OK && data != null) {
            val isSuccess = data.getBooleanExtra(
                ApplinkConstInternalPurchasePlatform.BOOLEAN_EXTRA_SUCCESS,
                false
            )
            val messageToaster =
                data.getStringExtra(ApplinkConstInternalPurchasePlatform.STRING_EXTRA_MESSAGE_TOASTER)
            val collectionId =
                data.getStringExtra(ApplinkConstInternalPurchasePlatform.STRING_EXTRA_COLLECTION_ID)
            if (messageToaster != null) {
                if (isSuccess) {
                    if (collectionId != null) {
                        showToasterActionLihat(messageToaster, Toaster.TYPE_NORMAL, collectionId)
                    } else {
                        showToasterActionOke(messageToaster, Toaster.TYPE_NORMAL)
                    }
                } else {
                    showToasterActionOke(messageToaster, Toaster.TYPE_ERROR)
                }
            }
            (activity as WishlistCollectionDetailActivity).isNeedRefresh(true)
        } else if (requestCode == REQUEST_CODE_GO_TO_SEMUA_WISHLIST && data != null) {
            doRefresh()
            showToasterFromIntent(data)
        } else if (requestCode == EDIT_WISHLIST_COLLECTION_REQUEST_CODE && data != null) {
            val isFinishActivity = data.getBooleanExtra(
                ApplinkConstInternalPurchasePlatform.NEED_FINISH_ACTIVITY,
                false
            )
            if (isFinishActivity) {
                val isSuccess = data.getBooleanExtra(
                    ApplinkConstInternalPurchasePlatform.BOOLEAN_EXTRA_SUCCESS,
                    false
                )
                val messageToaster =
                    data.getStringExtra(ApplinkConstInternalPurchasePlatform.STRING_EXTRA_MESSAGE_TOASTER)

                val intent = Intent()
                intent.putExtra(
                    ApplinkConstInternalPurchasePlatform.NEED_FINISH_ACTIVITY,
                    true
                )
                intent.putExtra(
                    ApplinkConstInternalPurchasePlatform.BOOLEAN_EXTRA_SUCCESS,
                    isSuccess
                )
                intent.putExtra(
                    ApplinkConstInternalPurchasePlatform.STRING_EXTRA_MESSAGE_TOASTER,
                    messageToaster
                )
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            } else {
                doRefresh()
                showToasterFromIntent(data)
            }
        } else if (requestCode == REQUEST_CODE_GO_TO_COLLECTION_DETAIL) {
            doRefresh()
        } else if (requestCode == REQUEST_CODE_GO_TO_PDP) {
            if (resultCode == Activity.RESULT_OK && data?.getBooleanExtra(ApplinkConstInternalPurchasePlatform.BOOLEAN_EXTRA_NEED_REFRESH, false) == true) {
                doRefresh()
            }
            showToasterFromIntent(data)
        }
    }

    private fun showToasterFromIntent(data: Intent?) {
        val isSuccess = data?.getBooleanExtra(
            ApplinkConstInternalPurchasePlatform.BOOLEAN_EXTRA_SUCCESS,
            false
        )
        val messageToaster =
            data?.getStringExtra(ApplinkConstInternalPurchasePlatform.STRING_EXTRA_MESSAGE_TOASTER)

        if (isSuccess == true) {
            messageToaster?.let { showToasterActionOke(it, Toaster.TYPE_NORMAL) }
        } else {
            messageToaster?.let { showToasterActionOke(it, Toaster.TYPE_ERROR) }
        }
    }

    private fun doRefresh() {
        _currCheckCollectionType = 0
        isToolbarHasDesc = false
        listSelectedProductIds.clear()
        onLoadMore = false
        isFetchRecommendation = false
        currPage = 1
        currRecommendationListPage = 1
        hitCountDeletion = false
        getCollectionItems()
        refreshLayout()
        if (isBulkDeleteShow) setBottomButton()
    }

    private fun refreshLayout() {
        showLoader()
        binding?.run {
            swipeRefreshLayout.isRefreshing = true
            wishlistCollectionDetailStickyCountManageLabel.wishlistDivider.visible()
            if (isBulkAddShow || isBulkDeleteShow || isBulkAddFromOtherCollectionShow) {
                wishlistCollectionDetailStickyCountManageLabel.wishlistDivider.gone()
                wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailTypeLayoutIcon.gone()
            } else {
                wishlistCollectionDetailStickyCountManageLabel.wishlistDivider.visible()
                wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionDetailTypeLayoutIcon.visible()
            }
        }
        addEndlessScrollListener()
        collectionItemsAdapter.resetTicker()
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

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private fun showDialogDeleteCollection(collectionId: String, collectionName: String) {
        val dialog =
            context?.let { DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.setTitle(
            getString(
                com.tokopedia.wishlist.R.string.wishlist_collection_detail_delete_confirmation_title,
                collectionName
            )
        )
        dialog?.setDescription(getString(com.tokopedia.wishlist.R.string.wishlist_collection_detail_delete_confirmation_desc))
        dialog?.setPrimaryCTAText(getString(com.tokopedia.wishlist.R.string.wishlist_delete_label))
        dialog?.setPrimaryCTAClickListener {
            dialog.dismiss()
            doDeleteCollection(collectionId)
            WishlistCollectionAnalytics.sendClickHapusOnWishlistProductEvent()
        }
        dialog?.setSecondaryCTAText(getString(com.tokopedia.wishlist.R.string.wishlist_cancel_manage_label))
        dialog?.setSecondaryCTAClickListener {
            dialog.dismiss()
            // WishlistV2Analytics.clickBatalOnPopUpMultipleWishlistProduct()
        }
        dialog?.show()
    }

    private fun doDeleteCollection(collectionId: String) {
        wishlistCollectionDetailViewModel.deleteWishlistCollection(collectionId)
    }

    private fun showBottomSheetCollection(
        fragmentManager: FragmentManager,
        productId: String,
        source: String
    ) {
        bottomSheetCollection = BottomSheetAddCollectionWishlist.newInstance(
            productId,
            source,
            false
        )
        if (bottomSheetCollection.isAdded || fragmentManager.isStateSaved) return
        bottomSheetCollection.setActionListener(this@WishlistCollectionDetailFragment)
        bottomSheetCollection.show(fragmentManager)
    }

    private fun showBottomSheetCreateNewCollection(fragmentManager: FragmentManager) {
        var listProductId = listSelectedProductIds
        if (isBulkAddFromOtherCollectionShow) listProductId = listSelectedProductIdsFromOtherCollection
        val bottomSheetCreateCollection =
            BottomSheetCreateNewCollectionWishlist.newInstance(listProductId, SRC_WISHLIST_PAGE)
        bottomSheetCreateCollection.setListener(this@WishlistCollectionDetailFragment)
        if (bottomSheetCreateCollection.isAdded || fragmentManager.isStateSaved) return
        bottomSheetCreateCollection.show(fragmentManager)
    }

    private fun showSelectItemsOption() {
        binding?.run {
            wishlistCollectionDetailStickyCountManageLabel.iconGearCollectionDetail.gone()
            wishlistCollectionDetailStickyCountManageLabel.wishlistCollectionSelectItemOption.apply {
                visible()
                setOnClickListener {
                    checkCollectionType(CHECK_COLLECTION_TYPE_FOR_TURN_ON_SELECT_ITEMS_MODE)
                }
            }
        }
    }

    private fun checkCollectionType(checkCollectionTypePurpose: Int) {
        _currCheckCollectionType = checkCollectionTypePurpose
        wishlistCollectionDetailViewModel.getWishlistCollectionType(collectionId)
    }

    private fun turnOffBulkAddFromOtherCollection() {
        if (!_isNeedRefreshAndTurnOffBulkModeFromOthers) {
            listSelectedProductIdsFromOtherCollection.clear()
            isBulkAddFromOtherCollectionShow = false
        }
        if (isToolbarHasDesc) {
            updateCustomToolbarTitleAndSubTitle(toolbarTitle, toolbarDesc)
        } else {
            updateToolbarTitle(toolbarTitle)
        }

        collectionItemsAdapter.hideCheckbox()
        setSwipeRefreshLayout()
        binding?.run {
            containerDeleteSemuaWishlist.gone()
            containerAddBulk.gone()
            containerDeleteCollectionDetail.gone()
            showSearchBar()
            showFilter()
            wishlistCollectionDetailStickyCountManageLabel.apply {
                iconGearCollectionDetail.gone()
                wishlistCollectionDetailManageLabel.gone()
                wishlistDivider.visible()
                wishlistCollectionDetailTypeLayoutIcon.visible()
                wishlistCollectionSelectItemOption.visible()
                wishlistCollectionSelectItemOption.setOnClickListener {
                    if (userSession.isLoggedIn) {
                        checkCollectionType(CHECK_COLLECTION_TYPE_FOR_TURN_ON_SELECT_ITEMS_MODE)
                    } else {
                        goToLoginPage()
                    }
                }
            }
        }
    }

    override fun onCollectionItemClicked(name: String, id: String) {
        var listProductId = listSelectedProductIds
        if (isBulkAddFromOtherCollectionShow) listProductId = listSelectedProductIdsFromOtherCollection
        val addWishlistParam = AddWishlistCollectionsHostBottomSheetParams(
            collectionId = id,
            collectionName = name,
            productIds = listProductId
        )
        bottomSheetCollection.saveToCollection(addWishlistParam)
        WishlistCollectionAnalytics.sendClickCollectionFolderEvent(id, listProductId.toString(), SRC_WISHLIST)
    }

    override fun onCreateNewCollectionClicked(dataObject: GetWishlistCollectionsBottomSheetResponse.GetWishlistCollectionsBottomsheet.Data) {
        if (dataObject.totalCollection < dataObject.maxLimitCollection) {
            bottomSheetCollection.dismiss()
            showBottomSheetCreateNewCollection(childFragmentManager)
        } else {
            val intent = Intent()
            intent.putExtra(ApplinkConstInternalPurchasePlatform.BOOLEAN_EXTRA_SUCCESS, false)
            intent.putExtra(
                ApplinkConstInternalPurchasePlatform.STRING_EXTRA_MESSAGE_TOASTER,
                dataObject.wordingMaxLimitCollection
            )
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }
    }

    override fun onSuccessSaveItemToCollection(data: AddWishlistCollectionItemsResponse.AddWishlistCollectionItems) {
        if (data.status == OK && data.dataItem.success) {
            showToasterActionLihat(data.dataItem.message, Toaster.TYPE_NORMAL, data.dataItem.collectionId)
        } else {
            val errorMessage = if (data.errorMessage.isNotEmpty()) {
                data.errorMessage.firstOrNull() ?: ""
            } else if (data.dataItem.message.isNotEmpty()) {
                data.dataItem.message
            } else {
                getString(com.tokopedia.wishlist.R.string.wishlist_v2_common_error_msg)
            }
            showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)
        }
        if (!_bulkModeIsAlreadyTurnedOff) {
            if (!isBulkAddFromOtherCollectionShow) {
                turnOffBulkDeleteMode()
            } else {
                turnOffBulkAddFromOtherCollection()
            }
        } else {
            _bulkModeIsAlreadyTurnedOff = false
            listSelectedProductIdsFromOtherCollection.clear()
            isBulkAddFromOtherCollectionShow = false
        }
    }

    override fun onFailedSaveItemToCollection(errorMessage: String) {
        showToasterActionOke(errorMessage, Toaster.TYPE_ERROR)
        turnOffBulkDeleteMode()
    }

    override fun onSuccessSaveToNewCollection(dataItem: AddWishlistCollectionItemsResponse.AddWishlistCollectionItems.DataItem) {
        showToasterActionLihat(dataItem.message, Toaster.TYPE_NORMAL, dataItem.collectionId)
        if (!isBulkAddFromOtherCollectionShow) {
            turnOffBulkMode()
            updateToolbarTitle(toolbarTitle)
        } else {
            turnOffBulkAddFromOtherCollection()
        }
    }

    override fun onFailedSaveToNewCollection(errorMessage: String?) {
        errorMessage?.let { showToasterActionOke(it, Toaster.TYPE_ERROR) }
    }

    override fun onEditCollection(collectionId: String, collectionName: String, actionText: String) {
        bottomSheetCollectionSettings.dismiss()
        goToEditCollectionPage()
    }

    private fun goToEditCollectionPage() {
        val intent = Intent(context, WishlistCollectionEditActivity::class.java)
        intent.putExtra(WishlistCollectionConsts.COLLECTION_ID, collectionId)
        intent.putExtra(WishlistCollectionConsts.COLLECTION_NAME, collectionName)
        startActivityForResult(intent, EDIT_WISHLIST_COLLECTION_REQUEST_CODE)
    }

    override fun onDeleteCollection(collectionId: String, collectionName: String, actionText: String) {
        bottomSheetCollectionSettings.dismiss()
        showDialogDeleteCollection(collectionId, collectionName)
        WishlistCollectionAnalytics.sendClickOptionOnGearIconEvent(actionText)
    }

    override fun onShareCollection(
        collectionId: String,
        collectionName: String,
        actionText: String,
        _collectionIndicatorTitle: String
    ) {
        // used in WishlistCollectionFragment
    }

    override fun onManageItemsInCollection(actionText: String) {
        bottomSheetCollectionSettings.dismiss()
        turnOnBulkDeleteMode(false)
        WishlistCollectionAnalytics.sendClickOptionOnGearIconEvent(actionText)
    }

    override fun onShareItemShown(anchorView: View) {
        // used in WishlistCollectionFragment - to show coachmark
    }
}
