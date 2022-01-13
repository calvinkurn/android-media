package com.tokopedia.wishlist.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
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
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
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
import com.tokopedia.wishlist.R as Rv2
import com.tokopedia.wishlist.data.model.*
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.databinding.FragmentWishlistV2Binding
import com.tokopedia.wishlist.di.DaggerWishlistV2Component
import com.tokopedia.wishlist.di.WishlistV2Module
import com.tokopedia.wishlist.util.WishlistV2Analytics
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_GRID
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_GRID_INT
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LIST_INT
import com.tokopedia.wishlist.util.WishlistV2LayoutPreference
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter
import com.tokopedia.wishlist.view.adapter.WishlistV2FilterBottomSheetAdapter
import com.tokopedia.wishlist.view.adapter.WishlistV2ThreeDotsMenuBottomSheetAdapter
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2FilterBottomSheet
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2ThreeDotsMenuBottomSheet
import com.tokopedia.wishlist.view.viewmodel.WishlistV2ViewModel
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@Keep
class WishlistV2Fragment : BaseDaggerFragment(), WishlistV2Adapter.ActionListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoClearedNullable<FragmentWishlistV2Binding>()
    private lateinit var wishlistV2Adapter: WishlistV2Adapter
    private lateinit var rvScrollListener: EndlessRecyclerViewScrollListener
    private var paramWishlistV2 = WishlistV2Params()
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
    private var wishlistItemOnAtc = WishlistV2Response.Data.WishlistV2.Item()
    private var indexOnAtc = 0
    private val listTitleCheckboxIdSelected = arrayListOf<String>()
    private var loaderDialog: LoaderDialog? = null

    private val wishlistViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[WishlistV2ViewModel::class.java]
    }

    private val userSession: UserSessionInterface by lazy { UserSession(activity) }
    private val wishlistPref: WishlistV2LayoutPreference? by lazy {
        activity?.let { WishlistV2LayoutPreference(it) }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let { activity ->
            DaggerWishlistV2Component.builder()
                    .baseAppComponent(getBaseAppComponent())
                    .wishlistV2Module(WishlistV2Module(activity))
                    .build()
                    .inject(this)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): WishlistV2Fragment {
            return WishlistV2Fragment().apply {
                arguments = bundle.apply {
                    putString(PARAM_ACTIVITY_WISHLIST_V2, this.getString(
                            PARAM_ACTIVITY_WISHLIST_V2))
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
        private const val PADDING_RV = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLogin()
        initTrackingQueue()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        launchAutoRefresh(isVisibleToUser)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWishlistV2Binding.inflate(inflater, container, false)
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
        if (isVisibleToUser && isAutoRefreshEnabled()) {
            turnOffBulkDeleteMode()
            doResetFilter()
            binding?.run {
                rvWishlist.scrollToPosition(0)
            }
        }
    }

    private fun observingData() {
        observingWishlistV2()
        observingWishlistData()
        observingDeleteWishlistV2()
        observingBulkDeleteWishlistV2()
        observingAtc()
    }

    private fun observingWishlistV2() {
        showLoader()
        wishlistViewModel.wishlistV2.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    finishRefresh()
                    result.data.let { wishlistV2 ->
                        rvScrollListener.setHasNextPage(wishlistV2.hasNextPage)

                        if (wishlistV2.totalData <= 0) {
                            if (wishlistV2.sortFilters.isEmpty() && wishlistV2.items.isEmpty()) {
                                onFailedGetWishlistV2(ResponseErrorException())
                            } else {
                                hideLoader()
                                showRvWishlist()
                                addPaddingRv()
                                isFetchRecommendation = true
                                hideTotalLabel()
                                hideSortFilter(wishlistV2.sortFilters)
                            }
                        } else {
                            hideLoader()
                            showRvWishlist()
                            setPaddingReferToTypeLayout()
                            updateTotalLabel(wishlistV2.totalData)
                        }

                        if (currPage == 1 && wishlistV2.sortFilters.isNotEmpty()) {
                            renderChipsFilter(wishlistV2.sortFilters)
                        }
                        if (wishlistV2.hasNextPage) {
                            currPage += 1
                        }

                        if (wishlistV2.errorMessage.isNotEmpty()) {
                            showToaster(wishlistV2.errorMessage, "", Toaster.TYPE_ERROR)
                        }
                    }
                }
                is Fail -> {
                    finishRefresh()
                    onFailedGetWishlistV2(result.throwable)
                    showToaster(ErrorHandler.getErrorMessage(context, result.throwable), "", Toaster.TYPE_ERROR)
                }
            }
        })
    }

    private fun showRvWishlist() {
        binding?.run {
            globalErrorWishlistV2.gone()
            emptyStateGlobalWishlistV2.gone()
            rvWishlist.show()
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
                wishlistLoaderLayout.root.gone()
                rvWishlist.gone()
                globalErrorWishlistV2.gone()
                emptyStateGlobalWishlistV2.apply {
                    visible()
                    showMessageExceptionError(throwable)
                }
            }
        } else {
            binding?.run {
                wishlistLoaderLayout.root.gone()
                rvWishlist.gone()
                emptyStateGlobalWishlistV2.gone()
                globalErrorWishlistV2.visible()
                globalErrorWishlistV2.setType(errorType)
                globalErrorWishlistV2.setActionClickListener { doRefresh() }
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

    private fun addPaddingRv() {
        binding?.run {
            rvWishlist.setPadding(PADDING_RV.toPx(), 0, PADDING_RV.toPx(), 0)
        }
    }

    private fun removePaddingRv() {
        binding?.run {
            rvWishlist.setPadding(0, 0, 0, 0)
        }
    }

    private fun observingWishlistData() {
        wishlistViewModel.wishlistV2Data.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    finishRefresh()
                    result.data.let { listData ->
                        if (!onLoadMore) {
                            wishlistV2Adapter.addList(listData)
                            rvScrollListener.updateStateAfterGetData()
                        } else {
                            wishlistV2Adapter.appendList(listData)
                            rvScrollListener.updateStateAfterGetData()
                        }
                    }

                }
                is Fail -> {
                    finishRefresh()
                    showToaster(ErrorHandler.getErrorMessage(context, result.throwable), "", Toaster.TYPE_ERROR)
                }
            }
        })
    }

    private fun getBaseAppComponent(): BaseAppComponent {
        return (activity?.application as BaseMainApplication).baseAppComponent
    }

    private fun prepareLayout() {
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        }
        setSwipeRefreshLayout()
        binding?.run {
            activityWishlistV2 = arguments?.getString(PARAM_ACTIVITY_WISHLIST_V2, "") as String

            viewLifecycleOwner.lifecycle.addObserver(wishlistNavtoolbar)
            wishlistNavtoolbar.setupSearchbar(searchbarType = NavToolbar.Companion.SearchBarType.TYPE_EDITABLE, hints = arrayListOf(
                    HintData(getString(Rv2.string.hint_cari_wishlist) )),
                    editorActionCallback = { query ->
                        searchQuery = query
                        if (query.isNotEmpty()) {
                            WishlistV2Analytics.submitSearchFromCariProduk(query)
                        }
                        wishlistNavtoolbar.hideKeyboard()
                        triggerSearch()
                    }
            )
            val pageSource: String
            val icons: IconBuilder
            if(activityWishlistV2 != PARAM_HOME) {
                wishlistNavtoolbar.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
                icons = IconBuilder(IconBuilderFlag()).apply {
                    addIcon(IconList.ID_CART) {}
                    addIcon(IconList.ID_NAV_GLOBAL) {}
                }
            } else {
                pageSource = ApplinkConsInternalNavigation.SOURCE_HOME_WISHLIST_V2
                wishlistNavtoolbar.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_NONE)
                icons = IconBuilder(IconBuilderFlag(pageSource = pageSource)).apply {
                    addIcon(IconList.ID_MESSAGE) {}
                    addIcon(IconList.ID_NOTIFICATION) {}
                    addIcon(IconList.ID_CART) {}
                    addIcon(IconList.ID_NAV_GLOBAL) {}
                }
            }
            wishlistNavtoolbar.setIcon(icons)
            wishlistV2StickyCountManageLabel.wishlistManageLabel.setOnClickListener { onStickyManageClicked() }
            wishlistV2Fb.circleMainMenu.setOnClickListener {
                rvWishlist.smoothScrollToPosition(0)
            }
            wishlistV2Fb.gone()
            setTypeLayoutIcon()
            wishlistV2StickyCountManageLabel.wishlistTypeLayoutIcon.setOnClickListener {
                changeTypeLayout()
                setTypeLayoutIcon()
            }
        }

        wishlistV2Adapter = WishlistV2Adapter().apply {
            setActionListener(this@WishlistV2Fragment)
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
        setPaddingReferToTypeLayout()
        wishlistV2Adapter.changeTypeLayout(wishlistPref?.getTypeLayout())
    }

    private fun setPaddingReferToTypeLayout() {
        if (wishlistPref?.getTypeLayout() == TYPE_LIST) {
            removePaddingRv()
        } else {
            addPaddingRv()
        }
    }

    private fun setTypeLayoutIcon() {
        binding?.run {
            if (wishlistPref?.getTypeLayout() == TYPE_LIST) {
                wishlistV2StickyCountManageLabel.wishlistTypeLayoutIcon.setImage(IconUnify.VIEW_LIST)
            } else if (wishlistPref?.getTypeLayout() == TYPE_GRID) {
                wishlistV2StickyCountManageLabel.wishlistTypeLayoutIcon.setImage(IconUnify.VIEW_GRID)
            }
        }
    }

    private fun setRefreshing() {
        doRefresh()
        isBulkDeleteShow = false
        listBulkDelete.clear()
        wishlistV2Adapter.hideCheckbox()

        binding?.run {
            containerDelete.gone()
            clWishlistHeader.visible()
            wishlistV2StickyCountManageLabel.wishlistManageLabel.text = getString(Rv2.string.wishlist_manage_label)
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
                    paramWishlistV2.page = currPage
                    loadWishlistV2()
                }
            }

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)
                if (dy != 0) {
                    binding?.run {
                        wishlistNavtoolbar.clearFocus()
                        wishlistNavtoolbar.hideKeyboard()
                        topLayoutShadow.visible()

                        if (dy > 0) {
                            wishlistV2Fb.gone()
                        } else {
                            wishlistV2Fb.visible()
                        }
                    }
                }

                var firstVisibleItems: IntArray? = null
                firstVisibleItems = (layoutManager as StaggeredGridLayoutManager).findFirstVisibleItemPositions(firstVisibleItems)
                if (firstVisibleItems != null && firstVisibleItems.isNotEmpty()) {
                    if (firstVisibleItems[0] == 0) {
                        binding?.run {
                            topLayoutShadow.gone()
                            wishlistV2Fb.gone()
                        }
                    }
                }
            }
        }

        binding?.run {
            rvWishlist.apply {
                layoutManager = staggeredGlm
                adapter = wishlistV2Adapter
                addOnScrollListener(rvScrollListener)
                itemAnimator = null
            }
        }
    }

    private fun loadRecommendationList() {
        currRecommendationListPage += 1
        wishlistViewModel.loadRecommendation(currRecommendationListPage)
    }

    private fun checkLogin() {
        if (userSession.isLoggedIn) {
            loadWishlistV2()
        } else {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
        }
    }

    private fun initTrackingQueue() {
        activity?.let {
            trackingQueue = TrackingQueue(it)
        }
    }

    private fun loadWishlistV2() {
        paramWishlistV2.page = currPage
        wishlistViewModel.loadWishlistV2(paramWishlistV2, wishlistPref?.getTypeLayout())
    }

    private fun triggerSearch() {
        paramWishlistV2.query = searchQuery
        doRefresh()
        rvScrollListener.resetState()
        currRecommendationListPage = 1
    }

    private fun observingDeleteWishlistV2() {
        wishlistViewModel.deleteWishlistV2Result.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    result.data.let { wishlistRemoveV2 ->
                        if (wishlistRemoveV2.success) {
                            var msg = getString(Rv2.string.wishlist_v2_delete_msg_default)
                            if (wishlistRemoveV2.message.isNotEmpty()) {
                                msg = wishlistRemoveV2.message
                            }

                            var btnText = getString(Rv2.string.wishlist_tutup_label)
                            if (wishlistRemoveV2.button.text.isNotEmpty()) {
                                btnText = wishlistRemoveV2.button.text
                            }

                            showToaster(msg, btnText, Toaster.TYPE_NORMAL)
                            doRefresh()
                        } else {
                            context?.getString(Rv2.string.wishlist_v2_common_error_msg)?.let {
                                errorDefaultMsg -> showToaster(errorDefaultMsg, "", Toaster.TYPE_ERROR) }
                        }
                    }
                }
                is Fail -> {
                    showToaster(ErrorHandler.getErrorMessage(context, result.throwable), "", Toaster.TYPE_ERROR)
                }
            }
        })
    }

    private fun observingBulkDeleteWishlistV2() {
        wishlistViewModel.bulkDeleteWishlistV2Result.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    result.data.let { bulkDeleteWishlistV2 ->
                        if (bulkDeleteWishlistV2.success) {
                            val listId = bulkDeleteWishlistV2.id.replace("[","").replace("]","").split(",").toList()
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
                            setSwipeRefreshLayout()

                        } else {
                            context?.getString(Rv2.string.wishlist_v2_common_error_msg)?.let { errorDefaultMsg -> showToaster(errorDefaultMsg, "", Toaster.TYPE_ERROR) }
                        }
                    }
                }
                is Fail -> {
                    showToaster(ErrorHandler.getErrorMessage(context, result.throwable), "", Toaster.TYPE_ERROR)
                }
            }
        })
    }

    private fun observingAtc() {
        wishlistViewModel.atcResult.observe(viewLifecycleOwner, {
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
                    }
                }
            }
        })
    }

    private fun updateTotalLabel(totalData: Int) {
        binding?.run {
            wishlistV2StickyCountManageLabel.root.visible()
            wishlistV2StickyCountManageLabel.wishlistCountLabel.text = "$totalData"
        }
    }

    private fun hideTotalLabel() {
        binding?.run {
            wishlistV2StickyCountManageLabel.root.gone()
        }
    }

    private fun hideSortFilter(sortFilters: List<WishlistV2Response.Data.WishlistV2.SortFiltersItem>) {
        var isFilterActive = false
        sortFilters.forEach { filterItem ->
            if (filterItem.isActive) isFilterActive = true
        }
        if (!isFilterActive) {
            binding?.run {
                clWishlistHeader.gone()
            }
        }
    }

    private fun showLoader() {
        wishlistV2Adapter.showLoader(wishlistPref?.getTypeLayout())
        binding?.run {
            wishlistSortFilter.gone()
            wishlistV2StickyCountManageLabel.root.gone()
            wishlistLoaderLayout.root.visible()
        }
    }

    private fun hideLoader() {
        binding?.run {
            wishlistLoaderLayout.root.gone()
            wishlistSortFilter.visible()
            wishlistV2StickyCountManageLabel.root.visible()
        }
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
            wishlistSortFilter.run {
                addItem(chips)
                sortFilterPrefix.setOnClickListener {
                    resetAllFilters()
                    paramWishlistV2 = WishlistV2Params()
                    if (searchQuery.isNotEmpty()) paramWishlistV2.query = searchQuery
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
                paramWishlistV2.sortFilters.removeAll { it.name == name }
                paramWishlistV2.sortFilters.add(WishlistV2Params.WishlistSortFilterParam(
                        name = filterItem.name, selected = arrayListOf(optionId)))
                doRefresh()
                hitAnalyticsFilterOptionSelected(name, label)
            }

            override fun onCheckboxSelected(name: String, optionId: String, isChecked: Boolean, titleCheckbox: String) {
                paramWishlistV2.sortFilters.forEach { sortFilterParam ->
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
                    paramWishlistV2.sortFilters.removeAll { it.name == nameSelected }
                    paramWishlistV2.sortFilters.add(WishlistV2Params.WishlistSortFilterParam(
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

    private fun showBottomSheetThreeDotsMenu(itemWishlist: WishlistV2Response.Data.WishlistV2.Item) {
        val bottomSheetThreeDotsMenu = WishlistV2ThreeDotsMenuBottomSheet.newInstance()
        if (bottomSheetThreeDotsMenu.isAdded || childFragmentManager.isStateSaved) return

        val threeDotsMenuBottomSheetAdapter = WishlistV2ThreeDotsMenuBottomSheetAdapter()
        threeDotsMenuBottomSheetAdapter.wishlistItem = itemWishlist

        bottomSheetThreeDotsMenu.setAdapter(threeDotsMenuBottomSheetAdapter)
        bottomSheetThreeDotsMenu.setListener(object : WishlistV2ThreeDotsMenuBottomSheet.BottomSheetListener{
            override fun onThreeDotsMenuItemSelected(wishlistItem: WishlistV2Response.Data.WishlistV2.Item,
                                                     additionalItem: WishlistV2Response.Data.WishlistV2.Item.Buttons.AdditionalButtonsItem) {
                bottomSheetThreeDotsMenu.dismiss()
                if (additionalItem.url.isNotEmpty()) {
                    RouteManager.route(context, additionalItem.url)
                } else {
                    if (additionalItem.action == SHARE_LINK_PRODUCT) {
                        showShareBottomSheet(wishlistItem)
                    } else if (additionalItem.action == DELETE_WISHLIST) {
                        wishlistViewModel.deleteWishlistV2(itemWishlist.id, userSession.userId)
                    }
                }
                WishlistV2Analytics.clickOptionOnThreeDotsMenu(additionalItem.text)
            }
        })
        bottomSheetThreeDotsMenu.show(childFragmentManager)
    }

    private fun showShareBottomSheet(wishlistItem: WishlistV2Response.Data.WishlistV2.Item) {
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

                                universalShareBottomSheet?.dismiss()
                            }

                            override fun onError(linkerError: LinkerError?) {}
                        })
                )
            }

            override fun onCloseOptionClicked() {}
        }

        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(shareListener)
            setMetaData(
                    wishlistItem.name,
                    wishlistItem.imageUrl
            )
        }
        universalShareBottomSheet?.show(childFragmentManager, this@WishlistV2Fragment)
    }

    private fun showToaster(message: String, actionText: String, type: Int) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.build(v, message, Toaster.LENGTH_SHORT, type, actionText).show()
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

    override fun onProductItemClicked(wishlistItem: WishlistV2Response.Data.WishlistV2.Item, position: Int) {
        WishlistV2Analytics.clickProductCard(wishlistItem, userSession.userId, position)
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, wishlistItem.id)
            startActivity(intent)
        }
    }

    override fun onProductRecommItemClicked(productId: String) {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
            startActivity(intent)
        }
    }

    override fun onViewProductCard(wishlistItem: WishlistV2Response.Data.WishlistV2.Item, position: Int) {
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

    override fun onThreeDotsMenuClicked(itemWishlist: WishlistV2Response.Data.WishlistV2.Item) {
        showBottomSheetThreeDotsMenu(itemWishlist)
        WishlistV2Analytics.clickThreeDotsOnProductCard()
    }

    override fun onCheckBulkDeleteOption(productId: String, isChecked: Boolean, position: Int) {
        if (isChecked) {
            listBulkDelete.add(productId)
        } else {
            listBulkDelete.remove(productId)
        }
        wishlistV2Adapter.setCheckbox(position, isChecked)
        val showButton = listBulkDelete.isNotEmpty()
        if (showButton) {
            binding?.run {
                containerDelete.visible()
                deleteButton.apply {
                    isEnabled = true
                    if (listBulkDelete.isNotEmpty()) {
                        text = getString(Rv2.string.wishlist_v2_delete_text_counter, listBulkDelete.size)
                        setOnClickListener {
                            showPopupBulkDeleteConfirmation(listBulkDelete)
                        }
                    }
                }
            }
        } else {
            binding?.run {
                containerDelete.visible()
                deleteButton.isEnabled = false
                deleteButton.text = getString(Rv2.string.wishlist_v2_delete_text)
            }
        }
    }

    override fun onAtc(wishlistItem: WishlistV2Response.Data.WishlistV2.Item, position: Int) {
        showLoadingDialog()
        val atcParam = AddToCartRequestParams(
                productId = wishlistItem.id.toLong(),
                productName = wishlistItem.name,
                price = wishlistItem.originalPriceFmt,
                quantity = wishlistItem.minOrder.toInt(),
                shopId = wishlistItem.shop.id.toInt(),
                atcFromExternalSource = AtcFromExternalSource.ATC_FROM_WISHLIST)
        wishlistViewModel.doAtc(atcParam)
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
            wishlistSortFilter.run {
                resetAllFilters()
                paramWishlistV2 = WishlistV2Params()
                wishlistNavtoolbar.clearSearchbarText()
                doRefresh()
            }
        }
    }

    private fun removeFilter(filterItem: WishlistV2Response.Data.WishlistV2.SortFiltersItem) {
        paramWishlistV2.sortFilters.removeAll { it.name == filterItem.name }
        doRefresh()
    }

    private fun onStickyManageClicked() {
        if (!isBulkDeleteShow) {
            isBulkDeleteShow = true
            binding?.run {
                wishlistV2StickyCountManageLabel.wishlistManageLabel.text = getString(Rv2.string.wishlist_cancel_manage_label)
            }
            onManageClicked(showCheckbox = true)
            WishlistV2Analytics.clickAturOnWishlist()
        } else {
            turnOffBulkDeleteMode()
        }
    }

    private fun turnOffBulkDeleteMode() {
        isBulkDeleteShow = false
        onManageClicked(showCheckbox = false)
        binding?.run {
            wishlistV2StickyCountManageLabel.wishlistManageLabel.text = getString(Rv2.string.wishlist_manage_label)
        }
    }

    override fun onManageClicked(showCheckbox: Boolean) {
        if (showCheckbox) {
            disableSwipeRefreshLayout()
            listBulkDelete.clear()
            wishlistV2Adapter.showCheckbox()
            binding?.run {
                clWishlistHeader.gone()
                wishlistV2StickyCountManageLabel.wishlistDivider.gone()
                wishlistV2StickyCountManageLabel.wishlistTypeLayoutIcon.gone()
                containerDelete.visible()
                deleteButton.isEnabled = false
                deleteButton.text = getString(Rv2.string.wishlist_v2_delete_text)
            }
        } else {
            setSwipeRefreshLayout()
            wishlistV2Adapter.hideCheckbox()
            binding?.run {
                containerDelete.gone()
                clWishlistHeader.visible()
                wishlistV2StickyCountManageLabel.wishlistDivider.show()
                wishlistV2StickyCountManageLabel.wishlistTypeLayoutIcon.show()
            }
        }
        WishlistV2Analytics.clickAturOnWishlist()
    }

    private fun showPopupBulkDeleteConfirmation(listBulkDelete: ArrayList<String>) {
        val dialog = context?.let { DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.setTitle(getString(Rv2.string.wishlist_v2_popup_delete_bulk_title, listBulkDelete.size))
        dialog?.dialogDesc?.gone()
        dialog?.setPrimaryCTAText(getString(Rv2.string.wishlist_delete_label))
        dialog?.setPrimaryCTAClickListener {
            dialog.dismiss()
            wishlistViewModel.bulkDeleteWishlistV2(listBulkDelete, userSession.userId)
            WishlistV2Analytics.clickHapusOnPopUpMultipleWishlistProduct()
        }
        dialog?.setSecondaryCTAText(getString(Rv2.string.wishlist_cancel_manage_label))
        dialog?.setSecondaryCTAClickListener {
            dialog.dismiss()
            WishlistV2Analytics.clickBatalOnPopUpMultipleWishlistProduct()
        }
        dialog?.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                loadWishlistV2()
            } else {
                activity?.finish()
            }
        }
    }

    private fun doRefresh() {
        binding?.run {
            swipeRefreshLayout.isRefreshing = true
        }
        onLoadMore = false
        isFetchRecommendation = false
        currPage = 1
        currRecommendationListPage = 1
        loadWishlistV2()
        showSortFilter()
    }

    private fun showSortFilter() {
        wishlistV2Adapter.isRefreshing = true
        binding?.run {
            containerDelete.gone()
            if (isBulkDeleteShow) {
                clWishlistHeader.gone()
            } else {
                clWishlistHeader.visible()
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
}