package com.tokopedia.brandlist.brandlist_page.presentation.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.brandlist.BrandlistInstance
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.analytic.BrandlistTracking
import com.tokopedia.brandlist.brandlist_category.data.model.Category
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreAllBrands
import com.tokopedia.brandlist.brandlist_page.di.BrandlistPageComponent
import com.tokopedia.brandlist.brandlist_page.di.BrandlistPageModule
import com.tokopedia.brandlist.brandlist_page.di.DaggerBrandlistPageComponent
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.BrandlistPageAdapter
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.BrandlistPageAdapterTypeFactory
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.AllBrandNotFoundViewHolder
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.adapter.BrandlistHeaderBrandInterface
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.*
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget.MarginItemDecoration
import com.tokopedia.brandlist.brandlist_page.presentation.viewmodel.BrandlistPageViewModel
import com.tokopedia.brandlist.common.Constant.DEFAULT_SELECTED_CHIPS
import com.tokopedia.brandlist.common.LoadAllBrandState
import com.tokopedia.brandlist.common.listener.BrandlistPageTrackingListener
import com.tokopedia.brandlist.common.listener.RecyclerViewScrollListener
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_brandlist_page.*
import javax.inject.Inject

class BrandlistPageFragment :
        BaseDaggerFragment(),
        HasComponent<BrandlistPageComponent>,
        BrandlistPageTrackingListener,
        BrandlistHeaderBrandInterface,
        AllBrandNotFoundViewHolder.Listener {

    companion object {
        const val BRANDLIST_GRID_SPAN_COUNT = 3
        const val ALL_BRAND_GRID_SPAN_COUNT = 1
        const val KEY_CATEGORY = "BRAND_LIST_CATEGORY"
        const val CATEGORY_INTENT = "category"

        @JvmStatic
        fun newInstance(bundle: Bundle?) = BrandlistPageFragment().apply { arguments = bundle }
    }

    @Inject
    lateinit var viewModel: BrandlistPageViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private var brandlistTracking: BrandlistTracking? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var recyclerView: RecyclerView? = null
    private var layoutManager: GridLayoutManager? = null
    private var category: Category? = null
    private var adapter: BrandlistPageAdapter? = null
    private var isLoadedOnce: Boolean = false
    private var isScrolling: Boolean = false
    private var categoryName = ""
    private var totalBrandsNumber: Int = 0
    private var selectedChip: Int = DEFAULT_SELECTED_CHIPS
    private var selectedCategoryName: String = ""
    private var stateLoadBrands: String = LoadAllBrandState.LOAD_INITIAL_ALL_BRAND
    private var isLoadMore: Boolean = false
    private val defaultBrandLetter: String = ""
    private var selectedBrandLetter: String = "A"
    private var recyclerViewLastState: Parcelable? = null
    private var recyclerViewTopPadding = 0
    private var isChipSelected: Boolean = false
    private var lastTimeChipIsClicked: Long = 0L

    private val endlessScrollListener: EndlessRecyclerViewScrollListener by lazy {
        object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (swipeRefreshLayout?.isRefreshing == false) {
                    val brandFirstLetter: String = if (stateLoadBrands == LoadAllBrandState.LOAD_BRAND_PER_ALPHABET) selectedBrandLetter else defaultBrandLetter
                    viewModel.loadMoreAllBrands(category, brandFirstLetter)
                    isLoadMore = true

                    if (adapter?.getVisitables()?.lastOrNull() is AllBrandUiModel) {
                        adapter?.showLoading()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getParcelable(KEY_CATEGORY)
        }
        context?.let {
            brandlistTracking = BrandlistTracking(it)
        }
        category?.let {
            categoryName = it.title
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_brandlist_page, container, false)
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout)
        recyclerView = rootView.findViewById(R.id.recycler_view)
        layoutManager = GridLayoutManager(context, BRANDLIST_GRID_SPAN_COUNT)
        recyclerView?.layoutManager = layoutManager

        val adapterTypeFactory = BrandlistPageAdapterTypeFactory(this, this)
        adapter = BrandlistPageAdapter(adapterTypeFactory, this)
        recyclerView?.adapter = adapter
        recyclerView?.addItemDecoration(MarginItemDecoration(resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2).toInt()))
        layoutManager?.spanSizeLookup = adapter?.spanSizeLookup

        recyclerView?.addOnScrollListener(endlessScrollListener)
        recyclerView?.isNestedScrollingEnabled = true
        recyclerViewTopPadding = recyclerView?.paddingTop ?: 0

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter?.initAdapter(recyclerViewLastState)

        observeFeaturedBrands()
        observePopularBrands()
        observeNewBrands()
        observeAllBrandHeader()
        observeAllBrands()

        swipeRefreshLayout?.setOnRefreshListener(createOnRefreshListener())

        if (parentFragment is RecyclerViewScrollListener) {
            val scrollListener = parentFragment as RecyclerViewScrollListener
            layoutManager?.let {
                recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        swipeRefreshLayout?.isEnabled = layoutManager?.findFirstCompletelyVisibleItemPosition() == 0
                        if (!isScrolling) {
                            isScrolling = true
                            scrollListener.onContentScrolled(dy)

                            Handler().postDelayed({
                                isScrolling = false
                            }, 200)
                        }

                    }

                })
            }
        }

    }

    override fun onResume() {
        super.onResume()
        category?.let { loadData(it, userSession.userId) }
    }

    override fun getComponent(): BrandlistPageComponent? {
        return activity?.run {
            DaggerBrandlistPageComponent
                    .builder()
                    .brandlistPageModule(BrandlistPageModule())
                    .brandlistComponent(BrandlistInstance.getComponent(application))
                    .build()
        }
    }

    override fun onPause() {
        super.onPause()
        brandlistTracking?.sendAll()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    private fun createOnRefreshListener(): SwipeRefreshLayout.OnRefreshListener {
        return SwipeRefreshLayout.OnRefreshListener {
            adapter?.getVisitables()?.removeAll {
                it is FeaturedBrandUiModel
                        || it is PopularBrandUiModel
                        || it is NewBrandUiModel
                        || it is AllBrandHeaderUiModel
                        || it is AllBrandGroupHeaderUiModel
                        || it is AllbrandNotFoundUiModel
                        || it is AllBrandUiModel
            }

            isChipSelected = false
            viewModel.resetAllBrandRequestParameter()
            adapter?.notifyDataSetChanged()
            adapter?.initAdapter(recyclerViewLastState)
            category?.let { loadData(it, userSession.userId, true) }
        }
    }

    private fun showErrorMessage(t: Throwable) {
        view?.let {
            Toaster.showError(it,
                    ErrorHandler.getErrorMessage(context, t),
                    Snackbar.LENGTH_LONG)
        }
    }

    private fun observeFeaturedBrands() {
        viewModel.getFeaturedBrandResult.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    BrandlistPageMapper.mappingFeaturedBrand(it.data, adapter, this)
                }
                is Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    showErrorMessage(it.throwable)
                }
            }
        })
    }

    private fun observePopularBrands() {
        viewModel.getPopularBrandResult.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    BrandlistPageMapper.mappingPopularBrand(it.data, adapter, this)
                }
                is Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    showErrorMessage(it.throwable)
                }
            }
        })
    }

    private fun observeNewBrands() {
        viewModel.getNewBrandResult.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    BrandlistPageMapper.mappingNewBrand(it.data, adapter, this)
                }
                is Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    showErrorMessage(it.throwable)
                }
            }
        })
    }

    private fun observeAllBrandHeader() {
        viewModel.getAllBrandHeaderResult.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    totalBrandsNumber = it.data.totalBrands
                }
                is Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    showErrorMessage(it.throwable)
                }
            }
        })
    }

    private fun observeAllBrands() {
        viewModel.getAllBrandResult.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    val totalBrandPerCharacter = it.data.totalBrands
                    val totalBrandsFiltered = if (stateLoadBrands == LoadAllBrandState.LOAD_ALL_BRAND ||
                            stateLoadBrands == LoadAllBrandState.LOAD_INITIAL_ALL_BRAND) totalBrandsNumber else it.data.totalBrands
                    adapter?.hideLoading()

                    swipeRefreshLayout?.isRefreshing = false
                    endlessScrollListener.updateStateAfterGetData()

                    BrandlistPageMapper.mappingAllBrandGroupHeader(
                            adapter, this, totalBrandsFiltered, selectedChip, lastTimeChipIsClicked, recyclerViewLastState)

                    if (totalBrandPerCharacter == 0) {
                        val emptyList = OfficialStoreAllBrands()
                        BrandlistPageMapper.mappingBrandNotFound(emptyList, isLoadMore, adapter)

                        recyclerView?.smoothScrollBy(0, recyclerViewTopPadding * 2)
                        layoutManager?.scrollToPositionWithOffset(
                                BrandlistPageMapper.ALL_BRAND_POSITION,
                                stickySingleHeaderView.containerHeight
                        )

                    } else {
                        BrandlistPageMapper.mappingAllBrand(it.data, adapter, this, stateLoadBrands, isLoadMore)

                        if (isChipSelected && !isLoadMore) {
                            recyclerView?.smoothScrollBy(0, recyclerViewTopPadding * 2)
                            layoutManager?.scrollToPositionWithOffset(
                                    BrandlistPageMapper.ALL_BRAND_POSITION,
                                    stickySingleHeaderView.containerHeight
                            )
                        }
                    }

                    viewModel.updateTotalBrandSize(it.data.totalBrands)
                    viewModel.updateCurrentOffset(it.data.brands.size)
                }
                is Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    showErrorMessage(it.throwable)
                }
            }
        })
    }

    private fun loadData(category: Category, userId: String, isRefresh: Boolean = false) {
        if (userVisibleHint && isAdded && ::viewModel.isInitialized) {
            if (!isLoadedOnce || isRefresh) {
                isLoadMore = false
                setStateLoadBrands(LoadAllBrandState.LOAD_INITIAL_ALL_BRAND)
                viewModel.loadInitialData(category, userId)
                isLoadedOnce = true

                if (!isRefresh) {
                    brandlistTracking?.sendScreen(categoryName.toEmptyStringIfNull())
                }
            }
        }
    }

    private fun setStateLoadBrands(stateLoadData: String) {
        stateLoadBrands = stateLoadData
    }


    override fun clickBrandPopular(shopId: String, shopLogoPosition: String, shopName: String, imgUrl: String) {
        val isLogin = userSession.isLoggedIn
        brandlistTracking?.clickBrandPopular(categoryName, shopId,
                shopLogoPosition, shopName, imgUrl, isLogin)
    }

    override fun clickBrandPilihan(shopId: String, shopName: String, imgUrl: String, shoplogoPosition: String) {
        val isLogin = userSession.isLoggedIn
        brandlistTracking?.clickBrandPilihan(shopId, isLogin, shopName,
                imgUrl, shoplogoPosition, categoryName)
    }

    override fun clickLihatSemua() {
        val isLogin = userSession.isLoggedIn
        brandlistTracking?.clickLihatSemua(isLogin, categoryName)
    }

    override fun clickBrandBaruTokopedia(shopId: String, shopName: String, imgUrl: String, shoplogoPosition: String) {
        val isLogin = userSession.isLoggedIn
        brandlistTracking?.clickBrandBaruTokopedia(
                isLogin, shopId, categoryName,
                shoplogoPosition, shopName, imgUrl)
    }

    override fun clickBrand(shopId: String, shoplogoPosition: String, shopName: String, imgUrl: String) {
        val isLogin = userSession.isLoggedIn
        brandlistTracking?.clickBrand(
                isLogin, shopId, categoryName, shoplogoPosition, shopName,
                imgUrl, false, "")
    }

    override fun impressionBrandPilihan(shopId: String, shoplogoPosition: String, imgUrl: String, shopName: String) {
        val isLogin = userSession.isLoggedIn
        brandlistTracking?.impressionBrandPilihan(
                isLogin, categoryName, shopId, shoplogoPosition,
                imgUrl, shopName)
    }

    override fun impressionBrandPopular(shopId: String, shoplogoPosition: String, shopName: String,
                                        imgUrl: String) {
        val isLogin = userSession.isLoggedIn
        brandlistTracking?.impressionBrandPopular(isLogin, shopId, categoryName,
                shoplogoPosition, shopName, imgUrl)
    }

    override fun impressionBrandBaru(shopId: String, shoplogoPosition: String, shopName: String,
                                     imgUrl: String) {
        val isLogin = userSession.isLoggedIn
        brandlistTracking?.impressionBrandBaru(isLogin, shopId, categoryName, shoplogoPosition,
                shopName, imgUrl)
    }

    override fun impressionBrand(shopId: String, shoplogoPosition: String, shopName: String,
                                 imgUrl: String) {
        val isLogin = userSession.isLoggedIn
        brandlistTracking?.impressionBrand(isLogin, shopId, categoryName, shoplogoPosition, shopName,
                imgUrl, false, "")
    }

    override fun onClickedChip(position: Int, chipName: String, current: Long, recyclerViewState: Parcelable?) {
        selectedChip = position
        selectedCategoryName = categoryName
        recyclerViewLastState = recyclerViewState
        isChipSelected = true
        lastTimeChipIsClicked = current

        resetCurrentBrandRecom()
        showLoadingBrandRecom()

        if (position > 0 && position < 2) {     // Load Semua Brand
            isLoadMore = false
            selectedBrandLetter = defaultBrandLetter
            setStateLoadBrands(LoadAllBrandState.LOAD_ALL_BRAND)
            viewModel.resetAllBrandRequestParameter()
            Handler().postDelayed({
                viewModel.loadAllBrands(category)
            }, 100)

        } else if (position >= 2) {     // Load per alphabet
            isLoadMore = false
            selectedBrandLetter = chipName
            setStateLoadBrands(LoadAllBrandState.LOAD_BRAND_PER_ALPHABET)
            viewModel.resetAllBrandRequestParameter()
            Handler().postDelayed({
                viewModel.loadBrandsPerAlphabet(category, chipName)
            }, 100)
        }
    }

    private fun showLoadingBrandRecom() {
        BrandlistPageMapper.mappingLoadingBrandRecomm(adapter)
    }

    private fun resetCurrentBrandRecom() {
        BrandlistPageMapper.mappingRemoveBrandRecom(adapter)
    }

    override fun onClickSearchButton() {
        category?.let {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMechant.BRANDLIST_SEARCH)
            intent.putExtra(CATEGORY_INTENT, it)
            startActivity(intent)
            activity?.supportFragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.slide_up, R.anim.no_change)?.commit()
        }
    }
}