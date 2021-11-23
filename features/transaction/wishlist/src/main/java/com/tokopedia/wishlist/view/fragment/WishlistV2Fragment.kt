package com.tokopedia.wishlist.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
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
import com.tokopedia.searchbar.data.HintData
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
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.databinding.FragmentWishlistV2Binding
import com.tokopedia.wishlist.di.DaggerWishlistV2Component
import com.tokopedia.wishlist.di.WishlistV2Module
import com.tokopedia.wishlist.util.WishlistUtils
import com.tokopedia.wishlist.util.WishlistV2LayoutPreference
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter
import com.tokopedia.wishlist.view.adapter.WishlistV2FilterBottomSheetAdapter
import com.tokopedia.wishlist.view.adapter.WishlistV2ThreeDotsMenuBottomSheetAdapter
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2FilterBottomSheet
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2ThreeDotsMenuBottomSheet
import com.tokopedia.wishlist.view.viewmodel.WishlistV2ViewModel
import javax.inject.Inject

@Keep
class WishlistV2Fragment : BaseDaggerFragment(), WishlistV2Adapter.ActionListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoClearedNullable<FragmentWishlistV2Binding>()
    private lateinit var wishlistV2Adapter: WishlistV2Adapter
    private lateinit var scrollRecommendationListener: EndlessRecyclerViewScrollListener
    private var paramWishlistV2 = WishlistV2Params()
    // private var refreshHandler: RefreshHandler? = null
    private var onLoadMore = false
    // private var onLoadMoreRecommendation = false
    private var isFetchRecommendation = false
    private var currPage = 1
    private var currRecommendationListPage = 1
    private var searchQuery = ""
    private var activityWishlistV2 = ""
    private var isBulkDeleteShow = false
    private var listBulkDelete: ArrayList<String> = arrayListOf()
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var checkpoint = false

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
        const val CTA_RESET = "Reset"
        private const val SHARE_PRODUCT_TITLE = "Bagikan Produk Ini"

        private const val topAdsPositionInPage = 4
        private const val newMinItemRegularRule = 24
        private const val WISHLIST_TOPADS_SOURCE = "6"
        private const val WISHLIST_TOPADS_ADS_COUNT = 1
        private const val WISHLIST_TOPADS_DIMENS = 3
        private const val EMPTY_WISHLIST_PAGE_NAME = "empty_wishlist"
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
                    result.data.let { wishlistV2 ->
                        hideLoader()
                        // refreshHandler?.finishRefresh()
                        finishRefresh()
                        scrollRecommendationListener.setHasNextPage(wishlistV2.hasNextPage)

                        if (wishlistV2.totalData == 0) {
                            isFetchRecommendation = true
                        }
                        if (currPage == 1 && wishlistV2.sortFilters.isNotEmpty()) {
                            renderChipsFilter(wishlistV2.sortFilters)
                        }
                        if (wishlistV2.hasNextPage) {
                            currPage += 1
                        }
                    }

                }
                is Fail -> {
                    // refreshHandler?.finishRefresh()
                    finishRefresh()
                    showToaster(ErrorHandler.getErrorMessage(context, result.throwable), "", Toaster.TYPE_ERROR)
                }
            }
        })
    }

    private fun observingWishlistData() {
        if (!checkpoint) {
            checkpoint = true
            wishlistViewModel.wishlistV2Data.observe(viewLifecycleOwner, { result ->
                when (result) {
                    is Success -> {
                        result.data.let { listData ->
                            if (!onLoadMore) {
                                wishlistV2Adapter.addList(listData)
                                scrollRecommendationListener.updateStateAfterGetData()
                            } else {
                                wishlistV2Adapter.appendList(listData)
                                scrollRecommendationListener.updateStateAfterGetData()
                            }
                        }

                    }
                    is Fail -> {
                        showToaster(ErrorHandler.getErrorMessage(context, result.throwable), "", Toaster.TYPE_ERROR)
                    }
                }
            })
        }
    }

    private fun getBaseAppComponent(): BaseAppComponent {
        return (activity?.application as BaseMainApplication).baseAppComponent
    }

    private fun prepareLayout() {
        binding?.run {
            swipeRefreshLayout.setOnRefreshListener {
                doRefresh()
            }
            // refreshHandler = RefreshHandler(swipeRefreshLayout, this@WishlistV2Fragment)
            // refreshHandler?.setPullEnabled(true)
            activityWishlistV2 = arguments?.getString(PARAM_ACTIVITY_WISHLIST_V2, "") as String

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
            var pageSource: String
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
                onLoadMore = true
                if (isFetchRecommendation) {
                    // onLoadMoreRecommendation = true
                    loadRecommendationList()
                } else {
                    paramWishlistV2.page = currPage
                    loadWishlistV2()
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

    private fun loadWishlistV2() {
        checkpoint = false
        paramWishlistV2.page = currPage
        wishlistViewModel.loadWishlistV2(paramWishlistV2, wishlistPref?.getTypeLayout())
    }

    private fun triggerSearch() {
        paramWishlistV2.query = searchQuery
        // refreshHandler?.startRefresh()
        doRefresh()
        scrollRecommendationListener.resetState()
        currRecommendationListPage = 1
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
                            // refreshHandler?.startRefresh()
                            doRefresh()
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
                            // refreshHandler?.startRefresh()
                            doRefresh()
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

    private fun showLoader() {
        wishlistV2Adapter.showLoader()
        binding?.run {
            wishlistSortFilter.gone()
            wishlistLoaderLayout.root.visible()
        }
    }

    private fun hideLoader() {
        binding?.run {
            wishlistSortFilter.visible()
            wishlistLoaderLayout.root.gone()
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
                    // refreshHandler?.startRefresh()
                    doRefresh()
                }
            }
        }
    }

    private fun showBottomSheetFilterOption(filterItem: WishlistV2Response.Data.WishlistV2.SortFiltersItem) {
        val filterBottomSheet = WishlistV2FilterBottomSheet.newInstance(filterItem.text, filterItem.selectionType)
        if (filterBottomSheet.isAdded || childFragmentManager.isStateSaved) return

        val filterBottomSheetAdapter = WishlistV2FilterBottomSheetAdapter()
        filterBottomSheetAdapter.filterItem = filterItem

        val listOptionIdSelected = arrayListOf<String>()
        var nameSelected = ""

        filterBottomSheet.setAdapter(filterBottomSheetAdapter)
        filterBottomSheet.setAction(CTA_RESET) {
            filterBottomSheet.dismiss()
            doResetFilter()
        }
        filterBottomSheet.setListener(object : WishlistV2FilterBottomSheet.BottomSheetListener{
            override fun onRadioButtonSelected(filterItem: WishlistV2Params.WishlistSortFilterParam) {
                filterBottomSheet.dismiss()
                val listSortFilter = arrayListOf<WishlistV2Params.WishlistSortFilterParam>()
                listSortFilter.add(filterItem)
                paramWishlistV2.sortFilters = listSortFilter
                // refreshHandler?.startRefresh()
                doRefresh()
            }

            override fun onCheckboxSelected(name: String, optionId: String) {
                nameSelected = name
                listOptionIdSelected.add(optionId)
            }

            override fun onSaveCheckboxSelection() {
                val filterSortItem = WishlistV2Params.WishlistSortFilterParam(name = nameSelected, selected = listOptionIdSelected)
                val listSortFilter = arrayListOf<WishlistV2Params.WishlistSortFilterParam>()
                listSortFilter.add(filterSortItem)

                paramWishlistV2.sortFilters = listSortFilter
                filterBottomSheet.dismiss()
                // refreshHandler?.startRefresh()
                doRefresh()
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
            }.show()
        }
    }

    override fun onCariBarangClicked() {

    }

    override fun onNotFoundButtonClicked(keyword: String) {

    }

    override fun onProductItemClicked(productId: String) {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
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
        val showButton = listBulkDelete.isNotEmpty()
        if (showButton) {
            binding?.run {
                containerDelete.visible()
                deleteButton.apply {
                    isEnabled = true
                    if (listBulkDelete.size > 1) {
                        text = getString(R.string.wishlist_v2_delete_text_counter, listBulkDelete.size)
                        setOnClickListener {
                            showPopupBulkDeleteConfirmation(listBulkDelete)
                        }
                    } else if (listBulkDelete.size == 1) {
                        text = getString(R.string.wishlist_v2_delete_text)

                        setOnClickListener {
                            wishlistViewModel.deleteWishlistV2(listBulkDelete[0], userSession.userId)
                        }
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

    override fun onResetFilter() {
        doResetFilter()
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

    override fun onManageClicked(showCheckbox: Boolean) {
        if (showCheckbox) {
            wishlistV2Adapter.showCheckbox()
            binding?.run {
                containerDelete.visibility = View.VISIBLE
                deleteButton.isEnabled = false
            }

        } else {
            wishlistV2Adapter.hideCheckbox()
            binding?.containerDelete?.visibility = View.GONE
        }
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

    /*override fun onRefresh(view: View?) {

    }*/

    private fun doRefresh() {
        binding?.run {
            swipeRefreshLayout.isRefreshing = true
        }
        onLoadMore = false
        isFetchRecommendation = false
        // onLoadMoreRecommendation = false
        currPage = 1
        currRecommendationListPage = 1
        isBulkDeleteShow = false
        listBulkDelete = arrayListOf()
        wishlistV2Adapter.hideCheckbox()
        wishlistV2Adapter.isRefreshing = true
        binding?.containerDelete?.visibility = View.GONE
        loadWishlistV2()
    }

    private fun finishRefresh() {
        binding?.run {
            swipeRefreshLayout.isRefreshing = false
        }
    }
}