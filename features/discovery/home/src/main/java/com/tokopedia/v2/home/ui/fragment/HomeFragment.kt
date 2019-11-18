package com.tokopedia.v2.home.ui.fragment

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.home.R
import com.tokopedia.home.beranda.di.DaggerBerandaComponent
import com.tokopedia.home.beranda.presentation.view.adapter.LinearLayoutManagerWithSmoothScroller
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.HomeRecyclerDecoration
import com.tokopedia.home.beranda.presentation.view.customview.NestedRecyclerView
import com.tokopedia.home.constant.ConstantKey
import com.tokopedia.home.widget.ToggleableSwipeRefreshLayout
import com.tokopedia.searchbar.HomeMainToolbar
import com.tokopedia.v2.home.model.vo.Resource
import com.tokopedia.v2.home.ui.adapter.HomeAdapter
import com.tokopedia.v2.home.viewModel.HomePageViewModel
import javax.inject.Inject

class HomeFragment : BaseDaggerFragment(){
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy{ ViewModelProviders.of(this, viewModelFactory) }
    private val homeViewModel: HomePageViewModel by lazy { viewModelProvider.get(HomePageViewModel::class.java) }

    private val homeMainToolbar by lazy { view?.findViewById<HomeMainToolbar>(R.id.toolbar) }
    private val statusBarBackground by lazy { view?.findViewById<View>(R.id.status_bar_bg) }
    private val homeRecyclerView by lazy { view?.findViewById<NestedRecyclerView>(R.id.list) }
    private val refreshLayout by lazy { view?.findViewById<ToggleableSwipeRefreshLayout>(R.id.home_swipe_refresh_layout) }

    private var startToTransitionOffset = 0
    private var searchBarTransitionRange = 0

    private val adapter: HomeAdapter by lazy { HomeAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    override fun getScreenName(): String {
        return ConstantKey.Analytics.AppScreen.UnifyTracking.SCREEN_UNIFY_HOME_BERANDA
    }

    override fun initInjector() {
        if (activity != null) {
            val component = DaggerBerandaComponent.builder().baseAppComponent((activity!!.application as BaseMainApplication).baseAppComponent).build()
            component.inject(this)
        }
    }

    /**
     * View Handler
     */

    private fun initView(){
        initRootRecyclerView()
        initSwipeRefresh()
        statusBarBackground?.background = ColorDrawable(
                ContextCompat.getColor(activity!!, R.color.green_600)
        )

        //initial condition for status and searchbar
        setStatusBarAlpha(0f)

        calculateSearchBarView(0)
    }

    private fun initRootRecyclerView() {
        val layoutManager = LinearLayoutManagerWithSmoothScroller(context)
        homeRecyclerView?.layoutManager = layoutManager
        homeRecyclerView?.itemAnimator!!.changeDuration = 0

        homeRecyclerView?.adapter = adapter
        if (homeRecyclerView?.itemDecorationCount == 0) {
            homeRecyclerView?.addItemDecoration(HomeRecyclerDecoration(resources.getDimensionPixelSize(R.dimen.home_recyclerview_item_spacing)))
        }
    }

    private fun initSwipeRefresh(){
        refreshLayout?.setOnRefreshListener {
            homeViewModel.getData()
        }
    }

    private fun setStatusBarAlpha(alpha: Float) {
        val drawable = statusBarBackground?.background
        drawable?.alpha = alpha.toInt()
        statusBarBackground?.background = drawable
    }

    private fun calculateSearchBarView(offset: Int) {

        val endTransitionOffset = startToTransitionOffset + searchBarTransitionRange
        val maxTransitionOffset = endTransitionOffset - startToTransitionOffset

        //mapping alpha to be rendered per pixel for x height
        var offsetAlpha = 255f / maxTransitionOffset * (offset - startToTransitionOffset)
        //2.5 is maximum
        if (offsetAlpha < 0) {
            offsetAlpha = 0f
        }

        if (offsetAlpha >= 255) {
            offsetAlpha = 255f
            homeMainToolbar?.switchToDarkToolbar()
        } else {
            homeMainToolbar?.switchToLightToolbar()
        }

        if (offsetAlpha in 0.0..255.0) {
            homeMainToolbar?.setBackgroundAlpha(offsetAlpha)
            setStatusBarAlpha(offsetAlpha)
        }
    }

    /**
     * Data Handler
     */

    private fun initData(){
        homeViewModel.homeData.observe(viewLifecycleOwner, Observer { res ->
            refreshLayout?.isRefreshing = false
            if(res.isNotEmpty()){
                adapter.submitList(res)
            }
        })
        homeViewModel.getData()
    }
}