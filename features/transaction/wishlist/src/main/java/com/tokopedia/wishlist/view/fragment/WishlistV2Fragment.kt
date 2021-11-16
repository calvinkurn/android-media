package com.tokopedia.wishlist.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.share.DataMapper
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.sortfilter.SortFilterItem
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
import com.tokopedia.utils.text.currency.StringUtils
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.data.model.*
import com.tokopedia.wishlist.databinding.FragmentWishlistV2Binding
import com.tokopedia.wishlist.di.DaggerWishlistV2Component
import com.tokopedia.wishlist.di.WishlistV2Module
import com.tokopedia.wishlist.util.WishlistUtils
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_NOT_FOUND
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_STATE
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_CAROUSEL
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_RECOMMENDATION_TITLE
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_TOPADS
import com.tokopedia.wishlist.util.WishlistV2Consts.WISHLIST_PAGE_NAME
import com.tokopedia.wishlist.util.WishlistV2LayoutPreference
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter
import com.tokopedia.wishlist.view.adapter.WishlistV2FilterBottomSheetAdapter
import com.tokopedia.wishlist.view.adapter.WishlistV2ThreeDotsMenuBottomSheetAdapter
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2FilterBottomSheet
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2ThreeDotsMenuBottomSheet
import com.tokopedia.wishlist.view.viewmodel.WishlistV2ViewModel
import java.io.File
import javax.inject.Inject

/**
 * Created by fwidjaja on 14/10/21.
 */
class WishlistV2Fragment : BaseDaggerFragment(), RefreshHandler.OnRefreshHandlerListener, WishlistV2Adapter.ActionListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoClearedNullable<FragmentWishlistV2Binding>()
    private lateinit var wishlistV2Adapter: WishlistV2Adapter
    private lateinit var scrollRecommendationListener: EndlessRecyclerViewScrollListener
    private var paramWishlistV2 = WishlistV2Params()
    private var refreshHandler: RefreshHandler? = null
    private var onLoadMore = false
    private var onLoadMoreRecommendation = false
    private var isFetchRecommendation = false
    private var currPage = 1
    private var currRecommendationListPage = 0
    private var searchQuery = ""
    private var activityWishlistV2 = ""
    private var isBulkDeleteShow = false
    private val listBulkDelete: ArrayList<String> = arrayListOf()
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null

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
        const val MIN_KEYWORD_CHARACTER_COUNT = 1
        private const val PARAM_ACTIVITY_WISHLIST_V2 = "activity_wishlist_v2"
        const val PARAM_HOME = "home"
        const val SHARE_LINK_PRODUCT = "SHARE_LINK_PRODUCT"
        const val DELETE_WISHLIST = "DELETE_WISHLIST"
        const val ATC_WISHLIST = "ADD_TO_CART"
        const val CTA_ATC = "Lihat"
        private const val SHARE_PRODUCT_TITLE = "Bagikan Produk Ini"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLogin()
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

    private fun observingData() {
        observingFilterWishlistV2()
        observingTotalDataWishlistV2()
        observingHasNextPage()
        observingWishlistV2()
        observingDeleteWishlistV2()
        observingBulkDeleteWishlistV2()
        observingAtc()
        // observingWishlistData()
    }

    private fun observingWishlistData() {
        // TODO : optimize lagi supaya observe nya 1 pintu aja
        wishlistViewModel.wishlistData.observe(viewLifecycleOwner, Observer {
            if (it.sortFilters.isNotEmpty() && currPage == 1) {
                renderChipsFilter(it.sortFilters)
            }
            if (currPage == 1 && it.totalData != 0) {
                updateTotalLabel(it.totalData)
            }
            if(it.items.isEmpty()) {
                isFetchRecommendation = true
            }
        })
    }

    private fun observingFilterWishlistV2() {
        wishlistViewModel.wishlistV2Filters.observe(viewLifecycleOwner, { filters ->
            renderChipsFilter(filters)
        })
    }

    private fun observingTotalDataWishlistV2() {
        wishlistViewModel.wishlistV2TotalData.observe(viewLifecycleOwner, { totalData ->
            updateTotalLabel(totalData)
        })
    }

    private fun observingHasNextPage() {
        wishlistViewModel.wishlistV2HasNextPage.observe(viewLifecycleOwner, { hasNextPage ->
            if (hasNextPage) currPage += 1
        })
    }

    private fun observingWishlistV2() {
        showLoader()
        wishlistViewModel.wishlistV2Data.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    refreshHandler?.finishRefresh()
                    hideLoader()

                    if (!onLoadMore) {
                        wishlistV2Adapter.addList(result.data)
                        scrollRecommendationListener.resetState()
                    } else {
                        wishlistV2Adapter.appendList(result.data)
                        scrollRecommendationListener.updateStateAfterGetData()
                    }
                }
                is Fail -> {
                    refreshHandler?.finishRefresh()
                    showToaster(ErrorHandler.getErrorMessage(context, result.throwable), "", Toaster.TYPE_ERROR)
                }
            }
        })
    }

    /*private fun observingWishlistV2() {
        showLoader()
        wishlistViewModel.wishlistData.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    refreshHandler?.finishRefresh()
                    val data = result.data
                    hideLoader()

                    if (it.sortFilters.isNotEmpty() && currPage == 1) {
                        renderChipsFilter(it.sortFilters)
                    }
                    if (currPage == 1 && it.totalData != 0) {
                        updateTotalLabel(it.totalData)
                    }
                    if(it.items.isEmpty()) {
                        isFetchRecommendation = true
                    }

                    if (currPage == 1) {
                        renderChipsFilter(data)

                    } else {
                        currPage += 1
                    }
                    renderWishlistV2Data(data)
                }
                is Fail -> {
                    refreshHandler?.finishRefresh()
                    showToaster(ErrorHandler.getErrorMessage(context, result.throwable), "", Toaster.TYPE_ERROR)
                }
            }
        })
    }*/

    private fun renderWishlistV2() {

    }

    private fun getBaseAppComponent(): BaseAppComponent {
        return (activity?.application as BaseMainApplication).baseAppComponent
    }

    private fun prepareLayout() {
        binding?.run {
            refreshHandler = RefreshHandler(swipeRefreshLayout, this@WishlistV2Fragment)
            refreshHandler?.setPullEnabled(true)

            statusbar.layoutParams.height = ViewHelper.getStatusBarHeight(activity)
            viewLifecycleOwner.lifecycle.addObserver(wishlistNavtoolbar)
            wishlistNavtoolbar.setupSearchbar(searchbarType = NavToolbar.Companion.SearchBarType.TYPE_EDITABLE, hints = arrayListOf(
                    HintData(getString(R.string.hint_cari_wishlist) )),
                    editorActionCallback = {query ->
                        searchQuery = query
                        when {
                            searchQuery.isBlank() -> {
                                context?.let { WishlistUtils.hideKeyBoard(it, root) }
                                triggerSearch()
                            }
                            searchQuery.length in 1 until MIN_KEYWORD_CHARACTER_COUNT -> {
                                showToaster(getString(R.string.error_message_minimum_search_keyword), "", Toaster.TYPE_ERROR)
                            }
                            else -> {
                                wishlistNavtoolbar.hideKeyboard()
                                triggerSearch()
                            }
                        }
                    }
            )
            var pageSource = ""
            if(activityWishlistV2 != PARAM_HOME) {
                wishlistNavtoolbar.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
                statusbar.gone()
            } else {
                pageSource = ApplinkConsInternalNavigation.SOURCE_HOME_WISHLIST_V2
            }
            val icons = IconBuilder(
                    IconBuilderFlag(pageSource = pageSource)
            )
            icons.apply {
                addIcon(IconList.ID_MESSAGE) {}
                addIcon(IconList.ID_NOTIFICATION) {}
                addIcon(IconList.ID_CART) {}
                addIcon(IconList.ID_NAV_GLOBAL) {}
            }
            wishlistNavtoolbar.setIcon(icons)

            wishlistManageLabel.setOnClickListener {
                if (!isBulkDeleteShow) {
                    wishlistManageLabel.text = getString(R.string.wishlist_cancel_manage_label)
                    wishlistV2Adapter.showCheckbox()
                } else {
                    wishlistManageLabel.text = getString(R.string.wishlist_manage_label)
                    wishlistV2Adapter.hideCheckbox()
                    binding?.containerDelete?.visibility = View.GONE
                }
                isBulkDeleteShow = !isBulkDeleteShow
            }
        }

        wishlistV2Adapter = WishlistV2Adapter().apply {
            setActionListener(this@WishlistV2Fragment)
        }
        addEndlessScrollListener()
    }

    private fun addEndlessScrollListener() {
        val glm = GridLayoutManager(activity, 2)
        glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (wishlistV2Adapter.getItemViewType(position)) {
                    WishlistV2Adapter.LAYOUT_LIST -> 2
                    WishlistV2Adapter.LAYOUT_GRID, WishlistV2Adapter.LAYOUT_RECOMMENDATION_LIST -> 1
                    else -> 2
                }
            }
        }

        scrollRecommendationListener = object : EndlessRecyclerViewScrollListener(glm) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                currentPage += 1
                if (isFetchRecommendation) {
                    onLoadMoreRecommendation = true
                    loadRecommendationList()
                } else {
                    onLoadMore = true
                    paramWishlistV2.page = currPage
                    wishlistViewModel.loadWishlistV2(paramWishlistV2, wishlistPref?.getTypeLayout())
                    // wishlistViewModel.getNextPageWishlistData(paramWishlistV2)
                }
            }
        }

        binding?.run {
            rvWishlist.apply {
                layoutManager = glm
                adapter = wishlistV2Adapter
                addOnScrollListener(scrollRecommendationListener)
            }
        }
    }

    private fun loadRecommendationList() {
        isFetchRecommendation = true
        wishlistViewModel.getRecommendation(currRecommendationListPage, WISHLIST_PAGE_NAME, listOf())
    }

    private fun checkLogin() {
        if (userSession.isLoggedIn) {
            loadWishlistV2()
        } else {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
        }
    }

    private fun loadWishlistV2() {
        paramWishlistV2.page = currPage
        wishlistViewModel.loadWishlistV2(paramWishlistV2, wishlistPref?.getTypeLayout())
    }

    private fun triggerSearch() {
        paramWishlistV2.query = searchQuery
        refreshHandler?.startRefresh()
        scrollRecommendationListener.resetState()
        currRecommendationListPage = 0
    }

    private fun renderWishlistV2Data(data: List<WishlistV2Data>) {
        val adapterData = arrayListOf<WishlistV2TypeLayoutData>()
        data.forEach {
            when(it) {
                is WishlistV2TopAdsDataModel -> {
                    adapterData.add(WishlistV2TypeLayoutData(it.topAdsData, TYPE_TOPADS))
                }
                is WishlistV2RecommendationDataModel -> {
                    adapterData.add(WishlistV2TypeLayoutData(it.title, TYPE_RECOMMENDATION_TITLE))
                    /*if (it.isCarousel) {
                        adapterData.add(WishlistV2TypeLayoutData(it, TYPE_RECOMMENDATION_CAROUSEL))
                    } else {
                        currRecommendationListPage += 1
                        it.recommendationData.forEach { recommendationItem ->
                            adapterData.add(
                                WishlistV2TypeLayoutData(
                                    recommendationItem,
                                    TYPE_RECOMMENDATION_LIST
                                )
                            )
                        }
                    }*/
                }
                is WishlistV2DataModel -> {
                    adapterData.add(
                        WishlistV2TypeLayoutData(
                            it.isChecked,
                            wishlistPref?.getTypeLayout(),
                            it.item
                        )
                    )
                }
                is WishlistV2EmptyDataModel -> {
                    isFetchRecommendation = true
                    if (it.query.isNotEmpty()) {
                        adapterData.add(WishlistV2TypeLayoutData(it.query, TYPE_EMPTY_NOT_FOUND))
                    } else {
                        adapterData.add(WishlistV2TypeLayoutData("", TYPE_EMPTY_STATE))
                    }
                }
            }
        }

        // TODO: harusnya ga dsini currPage nya
        /*if (data.any { element -> element is WishlistV2DataModel }) {
            currPage += 1
        }*/

        // TODO: fix here
        if (isBulkDeleteShow) {
            wishlistV2Adapter.addListOnBulk(adapterData)
            binding?.rvWishlist?.post {
                wishlistV2Adapter.notifyDataSetChanged()
            }
        } else {
            if ((!isFetchRecommendation) || (!onLoadMoreRecommendation && isFetchRecommendation) ) {
                wishlistV2Adapter.addList(adapterData)
                scrollRecommendationListener.resetState()
            } else {
                wishlistV2Adapter.appendList(adapterData)
                scrollRecommendationListener.updateStateAfterGetData()
            }
        }
    }

    private fun observingDeleteWishlistV2() {
        wishlistViewModel.deleteWishlistV2Result.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    result.data.let { wishlistRemoveV2 ->
                        if (wishlistRemoveV2.success) {
                            var msg = getString(R.string.wishlist_v2_delete_msg_default)
                            if (wishlistRemoveV2.message.isNotEmpty()) {
                                msg = wishlistRemoveV2.message
                            }

                            var btnText = getString(R.string.wishlist_tutup_label)
                            if (wishlistRemoveV2.button.text.isNotEmpty()) {
                                btnText = wishlistRemoveV2.button.text
                            }

                            showToaster(msg, btnText, Toaster.TYPE_NORMAL)
                            refreshHandler?.startRefresh()
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
                            var msg = getString(R.string.wishlist_v2_bulk_delete_msg_toaster, listId.size)
                            if (bulkDeleteWishlistV2.message.isNotEmpty()) {
                                msg = bulkDeleteWishlistV2.message
                            }

                            var btnText = getString(R.string.wishlist_oke_label)
                            if (bulkDeleteWishlistV2.button.text.isNotEmpty()) {
                                btnText = bulkDeleteWishlistV2.button.text
                            }

                            showToaster(msg, btnText, Toaster.TYPE_NORMAL)
                            refreshHandler?.startRefresh()
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
                    if (it.data.isStatusError()) {
                        val atcErrorMessage = it.data.getAtcErrorMessage()
                        if (atcErrorMessage != null) {
                            showToaster(atcErrorMessage, "", Toaster.TYPE_ERROR)
                        } else {
                            context?.getString(R.string.wishlist_v2_common_error_msg)?.let { errorDefaultMsg -> showToaster(errorDefaultMsg, "", Toaster.TYPE_ERROR) }
                        }
                    } else {
                        val successMsg = StringUtils.convertListToStringDelimiter(it.data.data.message, ",")
                        showToasterAtc(successMsg, Toaster.TYPE_NORMAL)
                    }
                }
                is Fail -> {
                    context?.also { ctx ->
                        val throwable = it.throwable
                        var errorMessage = if (throwable is ResponseErrorException) {
                            throwable.message ?: ""
                        } else {
                            ErrorHandler.getErrorMessage(ctx, throwable, ErrorHandler.Builder().withErrorCode(false))
                        }
                        if (errorMessage.isBlank()) {
                            errorMessage = ctx.getString(R.string.wishlist_v2_common_error_msg)
                        }
                        showToaster(errorMessage, "", Toaster.TYPE_ERROR)
                    }
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalLabel(totalData: Int) {
        binding?.run {
            wishlistCountLabel.text = getString(R.string.wishlist_count_label, totalData)
        }
    }

    private fun showLoader() {
        wishlistV2Adapter.showLoader()
        binding?.run {
            rlWishlistSort.gone()
            rlWishlistSortLoader.visible()
        }
    }

    private fun hideLoader() {
        binding?.run {
            rlWishlistSort.visible()
            rlWishlistSortLoader.gone()
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
                    refreshHandler?.startRefresh()
                }
            }
        }
    }

    private fun showBottomSheetFilterOption(filterItem: WishlistV2Response.Data.WishlistV2.SortFiltersItem) {
        val filterBottomSheet = WishlistV2FilterBottomSheet.newInstance(filterItem.text, filterItem.selectionType)
        if (filterBottomSheet.isAdded || childFragmentManager.isStateSaved) return

        val filterBottomSheetAdapter = WishlistV2FilterBottomSheetAdapter()
        filterBottomSheetAdapter.filterItem = filterItem

        val listFilterOffers = arrayListOf<WishlistV2Params.WishlistSortFilterParam>()

        filterBottomSheet.setAdapter(filterBottomSheetAdapter)
        filterBottomSheet.setListener(object : WishlistV2FilterBottomSheet.BottomSheetListener{
            override fun onRadioButtonSelected(filterItem: WishlistV2Params.WishlistSortFilterParam) {
                filterBottomSheet.dismiss()
                val listSortFilter = arrayListOf<WishlistV2Params.WishlistSortFilterParam>()
                listSortFilter.add(filterItem)
                paramWishlistV2.sortFilters = listSortFilter
                refreshHandler?.startRefresh()
            }

            override fun onCheckboxSelected(filterItem: WishlistV2Params.WishlistSortFilterParam) {
                listFilterOffers.add(filterItem)
            }

            override fun onSaveCheckboxSelection() {
                filterBottomSheet.dismiss()
                paramWishlistV2.sortFilters = listFilterOffers
                refreshHandler?.startRefresh()
            }
        })
        filterBottomSheet.show(childFragmentManager)
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
            }
        })
        bottomSheetThreeDotsMenu.show(childFragmentManager)
    }

    private fun showShareBottomSheet(wishlistItem: WishlistV2Response.Data.WishlistV2.Item) {
        val shareListener = object : ShareBottomsheetListener {

            override fun onShareOptionClicked(shareModel: ShareModel) {
                // openIntentShare(null, wishlistItem.name, "", wishlistItem.url)

                val linkerShareData = DataMapper.getLinkerShareData(LinkerData().apply {
                    type = LinkerData.PRODUCT_TYPE
                    uri = wishlistItem.url
                    id = userSession.userId
                    //set and share in the Linker Data
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
                        LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
                            override fun urlCreated(linkerShareData: LinkerShareResult?) {
                                val shareString = getString(
                                        R.string.wishlist_v2_share_text,
                                        userSession.shopName,
                                        linkerShareData?.shareContents
                                )
                                shareModel.subjectName = userSession.shopName
                                SharingUtil.executeShareIntent(
                                        shareModel,
                                        linkerShareData,
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

            override fun onCloseOptionClicked() {
                //no op
            }
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

    private fun openIntentShare(file: File?, title: String?, shareContent: String, shareUri: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = if (file == null) "text/plain" else "image/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (file != null) {
                putExtra(Intent.EXTRA_STREAM, MethodChecker.getUri(activity, file))
            }
            putExtra(Intent.EXTRA_REFERRER, shareUri)
            putExtra(Intent.EXTRA_HTML_TEXT, shareContent)
            putExtra(Intent.EXTRA_TITLE, title)
            putExtra(Intent.EXTRA_TEXT, shareContent)
            putExtra(Intent.EXTRA_SUBJECT, title)
        }

        activity?.startActivity(Intent.createChooser(shareIntent, SHARE_PRODUCT_TITLE))
    }

    private fun renderWishlist(items: List<WishlistV2Response.Data.WishlistV2.Item>) {
        val listItem = arrayListOf<WishlistV2TypeLayoutData>()
        items.forEach { item ->
            val listGroupLabel = arrayListOf<ProductCardModel.LabelGroup>()

            item.labelGroup.forEach { labelGroupItem ->
                val labelGroup = ProductCardModel.LabelGroup(
                        position = labelGroupItem.position,
                        title = labelGroupItem.title,
                        type = labelGroupItem.type,
                        imageUrl = labelGroupItem.url)
                listGroupLabel.add(labelGroup)
            }

            val isButtonAtc = item.buttons.primaryButton.action == ATC_WISHLIST

            val productModel = ProductCardModel(
                    productImageUrl = item.imageUrl,
                    isWishlistVisible = true,
                    productName = item.name,
                    shopName = item.shop.name,
                    formattedPrice = item.priceFmt,
                    shopLocation = item.shop.location,
                    isShopRatingYellow = true,
                    hasSecondaryButton = true,
                    tambahKeranjangButton = isButtonAtc,
                    lihatBarangSerupaButton = !isButtonAtc,
                    labelGroupList = listGroupLabel)
            listItem.add(WishlistV2TypeLayoutData(productModel, wishlistPref?.getTypeLayout(), item))
        }

        if (!onLoadMore) {
            wishlistV2Adapter.addList(listItem)
            scrollRecommendationListener.resetState()
        } else {
            wishlistV2Adapter.appendList(listItem)
            scrollRecommendationListener.updateStateAfterGetData()
        }
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
            toasterSuccess.build(v, message, Toaster.LENGTH_SHORT, type, CTA_ATC, View.OnClickListener {
                RouteManager.route(context, ApplinkConst.CART)
            }).show()
        }
    }

    override fun onCariBarangClicked() {

    }

    override fun onNotFoundButtonClicked(keyword: String) {

    }

    override fun onProductRecommendationClicked(productId: String) {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.PRODUCT_INFO, productId)
            startActivity(intent)
        }
    }

    override fun onThreeDotsMenuClicked(itemWishlist: WishlistV2Response.Data.WishlistV2.Item) {
        showBottomSheetThreeDotsMenu(itemWishlist)
    }

    override fun onCheckBulkDeleteOption(productId: String, isChecked: Boolean) {
        if (isChecked) {
            listBulkDelete.add(productId)
        } else {
            listBulkDelete.remove(productId)
        }
        // wishlistViewModel.onCheckedBulkDeleteWishlist(productId, isChecked)
        val showButton = listBulkDelete.isNotEmpty()
        if (showButton) {
            binding?.run {
                containerDelete.visible()
                deleteButton.apply {
                    setText(getString(R.string.wishlist_v2_delete_text, listBulkDelete.size))
                    setOnClickListener {
                        showPopupBulkDeleteConfirmation(listBulkDelete)
                    }
                }
            }
        } else {
            binding?.run {
                containerDelete.gone()
            }
        }
    }

    override fun onAtc(wishlistItem: WishlistV2Response.Data.WishlistV2.Item) {
        val atcParam = AddToCartRequestParams(
                productId = wishlistItem.id.toLong(),
                productName = wishlistItem.name,
                price = wishlistItem.originalPriceFmt,
                quantity = wishlistItem.minOrder.toInt(),
                shopId = wishlistItem.shop.id.toInt(),
                // category = wishlistItem.category.,
                atcFromExternalSource = AtcFromExternalSource.ATC_FROM_WISHLIST)
        wishlistViewModel.doAtc(atcParam)
    }

    override fun onCheckSimilarProduct(url: String) {
        RouteManager.route(context, url)
    }

    private fun showPopupBulkDeleteConfirmation(listBulkDelete: ArrayList<String>) {
        val dialog = context?.let { DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.setTitle(getString(R.string.wishlist_v2_popup_delete_bulk_title, listBulkDelete.size))
        dialog?.setPrimaryCTAText(getString(R.string.wishlist_delete_label))
        dialog?.setPrimaryCTAClickListener {
            dialog.dismiss()
            wishlistViewModel.bulkDeleteWishlistV2(listBulkDelete, userSession.userId)
        }
        dialog?.setSecondaryCTAText(getString(R.string.wishlist_cancel_manage_label))
        dialog?.setSecondaryCTAClickListener { dialog.dismiss() }
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

    override fun onRefresh(view: View?) {
            onLoadMore = false
            isFetchRecommendation = false
            onLoadMoreRecommendation = false
            currPage = 1
            currRecommendationListPage = 1
            paramWishlistV2.page = 1
            loadWishlistV2()
    }
}