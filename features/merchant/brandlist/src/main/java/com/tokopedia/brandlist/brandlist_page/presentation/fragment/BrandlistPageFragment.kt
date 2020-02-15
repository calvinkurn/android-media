package com.tokopedia.brandlist.brandlist_page.presentation.fragment

import android.os.Bundle
import android.os.Handler
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
import com.tokopedia.brandlist.BrandlistInstance
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.analytic.BrandlistTracking
import com.tokopedia.brandlist.brandlist_category.data.model.Category
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper
import com.tokopedia.brandlist.brandlist_page.di.BrandlistPageComponent
import com.tokopedia.brandlist.brandlist_page.di.BrandlistPageModule
import com.tokopedia.brandlist.brandlist_page.di.DaggerBrandlistPageComponent
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.BrandlistPageAdapter
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.BrandlistPageAdapterTypeFactory
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.*
import com.tokopedia.brandlist.brandlist_page.presentation.viewmodel.BrandlistPageViewModel
import com.tokopedia.brandlist.common.listener.BrandlistPageTracking
import com.tokopedia.brandlist.common.listener.RecyclerViewScrollListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class BrandlistPageFragment :
        BaseDaggerFragment(),
        HasComponent<BrandlistPageComponent>, BrandlistPageTracking {

    companion object {
        const val BRANDLIST_GRID_SPAN_COUNT = 3
        const val ALL_BRAND_GRID_SPAN_COUNT = 1
        const val KEY_CATEGORY = "BRAND_LIST_CATEGORY"
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
    private var isInitialDataLoaded: Boolean = false
    private var isScrolling = false

    private val endlessScrollListener: EndlessRecyclerViewScrollListener by lazy {
        object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (swipeRefreshLayout?.isRefreshing == false) {
                    viewModel.loadMoreAllBrands(category)
                    if (adapter?.getVisitables()?.lastOrNull() is AllBrandViewModel) {
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_brandlist_page, container, false)

        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout)

        recyclerView = rootView.findViewById(R.id.recycler_view)
        layoutManager = GridLayoutManager(context, BRANDLIST_GRID_SPAN_COUNT)
        recyclerView?.layoutManager = layoutManager

        val adapterTypeFactory = BrandlistPageAdapterTypeFactory(this)
        adapter = BrandlistPageAdapter(adapterTypeFactory, this)
        recyclerView?.adapter = adapter
        layoutManager?.spanSizeLookup = adapter?.spanSizeLookup

        recyclerView?.addOnScrollListener(endlessScrollListener)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter?.initAdapter()

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

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    private fun createOnRefreshListener(): SwipeRefreshLayout.OnRefreshListener {
        return SwipeRefreshLayout.OnRefreshListener {

            adapter?.getVisitables()?.removeAll {
                it is FeaturedBrandViewModel
                        || it is PopularBrandViewModel
                        || it is NewBrandViewModel
                        || it is AllBrandHeaderViewModel
                        || it is AllBrandGroupHeaderViewModel
                        || it is AllBrandViewModel
            }

            viewModel.resetAllBrandRequestParameter()

            adapter?.notifyDataSetChanged()

            adapter?.initAdapter()

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
        viewModel.getFeaturedBrandResult.observe(this, Observer {
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
        viewModel.getPopularBrandResult.observe(this, Observer {
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
        viewModel.getNewBrandResult.observe(this, Observer {
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
        viewModel.getAllBrandHeaderResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    val title = getString(R.string.brandlist_all_brand)
                    BrandlistPageMapper.mappingAllBrandHeader(title, it.data.totalBrands, adapter, this)
                }
                is Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    showErrorMessage(it.throwable)
                }
            }
        })
    }

    private fun observeAllBrands() {
        viewModel.getAllBrandResult.observe(this, Observer {
            when (it) {
                is Success -> {

                    adapter?.hideLoading()
                    swipeRefreshLayout?.isRefreshing = false

                    endlessScrollListener.updateStateAfterGetData()

                    val currentOffset = viewModel.getCurrentOffset()
                    val groupHeader = viewModel.getCurrentLetter().toUpperCase()

                    if (currentOffset == 0) {
                        BrandlistPageMapper.mappingAllBrandGroupHeader(groupHeader, adapter, this)
                    }

                    BrandlistPageMapper.mappingAllBrand(it.data, adapter, this)

                    viewModel.updateTotalBrandSize(it.data.totalBrands)
                    viewModel.updateCurrentOffset(it.data.brands.size)
                    viewModel.updateCurrentLetter()
                    viewModel.updateAllBrandRequestParameter()
                }
                is Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    showErrorMessage(it.throwable)
                }
            }
        })
    }

    private fun loadData(category: Category, userId: String, isRefresh: Boolean = false) {
        if (!isInitialDataLoaded || isRefresh) {
            viewModel.loadInitialData(category, userId)
            isInitialDataLoaded = true
        }
    }

    override fun clickBrandPopular(shopId: String, shopLogoPosition: String, shopName: String, imgUrl: String) {
        val isLogin = userSession.isLoggedIn
        brandlistTracking?.clickBrandPopular(category.toString(), shopId,
                shopLogoPosition, shopName, imgUrl, isLogin)
    }

    override fun clickBrandPilihan(shopId: String, shopName: String, imgUrl: String, shoplogoPosition: String) {
        val isLogin = userSession.isLoggedIn
        brandlistTracking?.clickBrandPilihan(shopId, isLogin, shopName, imgUrl, shoplogoPosition, category.toString())
    }

    override fun clickLihatSemua() {
        val isLogin = userSession.isLoggedIn
        brandlistTracking?.clickLihatSemua(isLogin, category.toString())
    }





    override fun clickSearchBox() {

    }

    override fun clickBrandOnSearchBox() {

    }



    override fun clickCategory() {

    }

    override fun impressionBrandPilihan() {

    }

    override fun impressionBrandPopular() {

    }

    override fun clickBrandBaruTokopedia() {

    }

    override fun impressionBrandBaru() {

    }

    override fun clickBrand() {

    }

    override fun impressionBrand() {

    }
}