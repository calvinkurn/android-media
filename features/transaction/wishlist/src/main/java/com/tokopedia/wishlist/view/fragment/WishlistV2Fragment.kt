package com.tokopedia.wishlist.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.navigation_common.listener.MainParentStateListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.data.model.WishlistV2Params
import com.tokopedia.wishlist.data.model.WishlistV2Response
import com.tokopedia.wishlist.databinding.FragmentWishlistV2Binding
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.di.DaggerWishlistV2Component
import com.tokopedia.wishlist.di.WishlistV2Module
import com.tokopedia.wishlist.util.WishlistUtils
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_NOT_FOUND
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_STATE
import com.tokopedia.wishlist.util.WishlistV2LayoutPreference
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter
import com.tokopedia.wishlist.view.adapter.WishlistV2FilterBottomSheetAdapter
import com.tokopedia.wishlist.view.bottomsheet.WishlistV2FilterBottomSheet
import com.tokopedia.wishlist.view.viewmodel.WishlistV2ViewModel
import javax.inject.Inject

/**
 * Created by fwidjaja on 14/10/21.
 */
class WishlistV2Fragment : BaseDaggerFragment(), RefreshHandler.OnRefreshHandlerListener, WishlistV2Adapter.ActionListener {
    private var binding by autoClearedNullable<FragmentWishlistV2Binding>()
    private lateinit var wishlistV2Adapter: WishlistV2Adapter
    private lateinit var scrollRecommendationListener: EndlessRecyclerViewScrollListener
    private lateinit var remoteConfigInstance: RemoteConfigInstance
    private var paramWishlistV2 = WishlistV2Params()
    private var refreshHandler: RefreshHandler? = null
    private var onLoadMore = false
    private var onLoadMoreRecommendation = false
    private var isFetchRecommendation = false
    private var currPage = 1
    private var currRecommendationListPage = 0
    private var searchQuery = ""
    private var activityWishlistV2 = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val wishlistViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[WishlistV2ViewModel::class.java]
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
        observingWishlistV2()
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
                                showToaster(getString(R.string.error_message_minimum_search_keyword), Toaster.TYPE_ERROR)
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
                statusbar.visibility = View.GONE
            } else {
                pageSource = ApplinkConsInternalNavigation.SOURCE_HOME_UOH
            }
            val icons = IconBuilder(
                    IconBuilderFlag(pageSource = pageSource)
            )
            icons.apply {
                addIcon(IconList.ID_MESSAGE) {}
                addIcon(IconList.ID_NOTIFICATION) {}
                addIcon(IconList.ID_CART) {}
                if (isNavRevamp()) {
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
                    WishlistV2Adapter.LAYOUT_GRID -> 1
                    else -> 2
                }
            }
        }

        scrollRecommendationListener = object : EndlessRecyclerViewScrollListener(glm) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                currentPage += 1
                if (isFetchRecommendation) {
                    onLoadMoreRecommendation = true
                    // loadRecommendationList()
                } else {
                    onLoadMore = true
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

    private fun checkLogin() {
        if (userSession.isLoggedIn) {
            loadWishlistV2()
        } else {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
        }
    }

    private fun loadWishlistV2() {
        wishlistViewModel.loadWishlistV2(paramWishlistV2)
    }

    private fun triggerSearch() {
        paramWishlistV2.query = searchQuery
        refreshHandler?.startRefresh()
        // scrollRecommendationListener.resetState()
    }

    private fun observingWishlistV2() {
        showLoader()
        wishlistViewModel.wishlistV2Result.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    refreshHandler?.finishRefresh()
                    result.data.let { wishlistV2 ->
                        hideLoader()
                        updateTotalLabel(wishlistV2.totalData)
                        if (currPage == 1 && wishlistV2.sortFilters.isNotEmpty()) {
                            renderChipsFilter(wishlistV2.sortFilters)
                        }
                        if (wishlistV2.items.isNotEmpty()) {
                            currPage += 1
                            renderWishlist(wishlistV2.items)

                        } else {
                            if (wishlistV2.query.isNotEmpty()) {
                                onWishlistSearchNotFound(wishlistV2.query)
                            } else {
                                renderEmpty()
                            }
                        }
                    }
                }
                is Fail -> {
                    refreshHandler?.finishRefresh()
                    showToaster(ErrorHandler.getErrorMessage(context, result.throwable), Toaster.TYPE_ERROR)
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

    private fun renderEmpty() {
        val emptyData = arrayListOf<WishlistV2TypeLayoutData>().apply {
            add(WishlistV2TypeLayoutData("",  TYPE_EMPTY_STATE))
        }
        wishlistV2Adapter.addList(emptyData)
    }

    private fun showLoader() {
        wishlistV2Adapter.showLoader()
        binding?.run {
            rlWishlistSort.visibility = View.GONE
            rlWishlistSortLoader.visibility = View.VISIBLE
        }
    }

    private fun hideLoader() {
        binding?.run {
            rlWishlistSort.visibility = View.VISIBLE
            rlWishlistSortLoader.visibility = View.GONE
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
                chipItems?.forEach { sortFilterItem ->
                    sortFilterItem.refChipUnify.setChevronClickListener {}
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

    private fun renderWishlist(items: List<WishlistV2Response.Data.WishlistV2.ItemsItem>) {
        val listItem = arrayListOf<WishlistV2TypeLayoutData>()
        items.forEach { item ->
            val productModel = ProductCardModel(
                    productImageUrl = item.imageUrl,
                    isWishlistVisible = true,
                    productName = item.name,
                    shopName = item.shop.name,
                    formattedPrice = item.priceFmt,
                    shopLocation = item.shop.location,
                    isShopRatingYellow = true,
                    hasSecondaryButton = true,
                    hasTambahKeranjangButton = true)
            listItem.add(WishlistV2TypeLayoutData(productModel, wishlistPref?.getTypeLayout()))
        }

        if (!onLoadMore) {
            wishlistV2Adapter.addList(listItem)
            // scrollRecommendationListener.resetState()
        } else {
            wishlistV2Adapter.appendList(listItem)
            // scrollRecommendationListener.updateStateAfterGetData()
        }
    }

    private fun showToaster(message: String, type: Int) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.build(v, message, Toaster.LENGTH_SHORT, type, "").show()
        }
    }

    private fun getAbTestPlatform(): AbTestPlatform {
        if (!::remoteConfigInstance.isInitialized) {
            remoteConfigInstance = RemoteConfigInstance(activity?.application)
        }
        return remoteConfigInstance.abTestPlatform
    }

    private fun isNavRevamp(): Boolean {
        return try {
            return (context as? MainParentStateListener)?.isNavigationRevamp ?: (getAbTestPlatform().getString(
                RollenceKey.NAVIGATION_EXP_TOP_NAV, RollenceKey.NAVIGATION_VARIANT_OLD
            ) == RollenceKey.NAVIGATION_VARIANT_REVAMP) ||
                    (getAbTestPlatform().getString(
                        RollenceKey.NAVIGATION_EXP_TOP_NAV2, RollenceKey.NAVIGATION_VARIANT_OLD
                    ) == RollenceKey.NAVIGATION_VARIANT_REVAMP2)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun onWishlistSearchNotFound(keyword: String) {
        val listItem = arrayListOf<WishlistV2TypeLayoutData>().apply {
            add(WishlistV2TypeLayoutData(keyword, TYPE_EMPTY_NOT_FOUND))
        }
        wishlistV2Adapter.addList(listItem)
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