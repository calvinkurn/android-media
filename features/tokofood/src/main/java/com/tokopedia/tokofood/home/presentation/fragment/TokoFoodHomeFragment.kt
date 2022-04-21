package com.tokopedia.tokofood.home.presentation.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.fragment.IBaseMultiFragment
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.coachmark.util.ViewHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.searchbar.navigation_component.util.StatusBarUtil
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.FragmentTokofoodHomeBinding
import com.tokopedia.tokofood.home.di.DaggerTokoFoodHomeComponent
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutState
import com.tokopedia.tokofood.home.presentation.adapter.CustomLinearLayoutManager
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeAdapter
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeAdapterTypeFactory
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeListDiffer
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeListUiModel
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodHomeBannerComponentCallback
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodHomeCategoryWidgetV2ComponentCallback
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodHomeLegoComponentCallback
import com.tokopedia.tokofood.home.presentation.viewmodel.TokoFoodHomeViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R.dimen as unifyR

@FlowPreview
@ExperimentalCoroutinesApi
class TokoFoodHomeFragment: BaseDaggerFragment(), IBaseMultiFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoClearedNullable<FragmentTokofoodHomeBinding>()
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokoFoodHomeViewModel::class.java)
    }

    private val adapter by lazy {
        TokoFoodHomeAdapter(
            typeFactory = TokoFoodHomeAdapterTypeFactory(
                createLegoBannerCallback(),
                createBannerCallback(),
                createCategoryWidgetCallback()
            ),
            differ = TokoFoodHomeListDiffer()
        )
    }

    companion object {
        private const val ITEM_VIEW_CACHE_SIZE = 20
        fun createInstance(): TokoFoodHomeFragment {
            return TokoFoodHomeFragment()
        }
    }

    private var navToolbar: NavToolbar? = null
    private var rvHome: RecyclerView? = null
    private var statusBarBackground: View? = null
    private var ivHeaderBackground: ImageView? = null
    private var swipeLayout: SwipeRefreshLayout? = null
    private var rvLayoutManager: CustomLinearLayoutManager? = null
    private var movingPosition = 0
    private val homeMainToolbarHeight: Int
        get() {
            val defaultHeight = resources.getDimensionPixelSize(
                R.dimen.tokofood_default_toolbar_status_height)
            val height = (navToolbar?.height ?: defaultHeight)
            val padding = resources.getDimensionPixelSize(unifyR.spacing_lvl3)

            return height + padding
        }
    private val spaceZero: Int
        get() = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0).toInt()

    private val navBarScrollListener by lazy { createNavBarScrollListener() }
    private val homeComponentScrollListener by lazy { createHomeComponentScrollListener() }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        activity?.let {
            DaggerTokoFoodHomeComponent
                .builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }

    override fun getFragmentTitle(): String? {
        return null
    }

    override fun getFragmentToolbar(): Toolbar? {
        return null
    }

    override fun navigateToNewFragment(fragment: Fragment) {
        (activity as? BaseMultiFragActivity)?.navigateToNewFragment(fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokofoodHomeBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideStatusBar()
        setupUi()
        setupNavToolbar()
        setupStatusBar()
        setupRecycleView()
        setupSwipeRefreshLayout()
        observeLiveData()

        loadLayout()
    }

    private fun showLayout() {
        getHomeLayout()
    }

    private fun getHomeLayout() {
        viewModel.getHomeLayout()
    }

    private fun getLayoutComponentData(){
        viewModel.getLayoutComponentData()
    }

    private fun loadLayout(){
        viewModel.getLoadingState()
    }

    private fun setupUi() {
        view?.apply {
            ivHeaderBackground = binding?.viewBackgroundImage
            navToolbar = binding?.navToolbar
            statusBarBackground = binding?.statusBarBg
            rvHome = binding?.rvHome
            swipeLayout = binding?.swipeRefreshLayout
        }
    }

    private fun setupRecycleView() {
        context?.let {
            rvHome?.apply {
                adapter = this@TokoFoodHomeFragment.adapter
                rvLayoutManager = CustomLinearLayoutManager(it)
                layoutManager = rvLayoutManager
            }
        }

        rvHome?.setItemViewCacheSize(ITEM_VIEW_CACHE_SIZE)
        addHomeComponentScrollListener()
    }

    private fun setupNavToolbar() {
        setupTopNavigation()
        setIconNavigation()
    }

    private fun setupTopNavigation() {
        navToolbar?.let { toolbar ->
            viewLifecycleOwner.lifecycle.addObserver(toolbar)
            addNavBarScrollListener()

            activity?.let {
                toolbar.setupToolbarWithStatusBar(it, statusBarTheme = NavToolbar.Companion.StatusBar.STATUS_BAR_DARK)
                toolbar.setToolbarTitle(getString(R.string.tokofood_title))
           }
        }
    }

    private fun setIconNavigation() {
        val icons = IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
            .addIcon(IconList.ID_SHARE, onClick = ::onClickShareButton)
            .addIcon(IconList.ID_LIST_TRANSACTION, onClick = ::onClickListTransactionButton)
            .addIcon(IconList.ID_NAV_GLOBAL, onClick = ::onClickNavGlobalButton)
        navToolbar?.setIcon(icons)
    }

    private fun setupStatusBar() {
        activity?.let {
            statusBarBackground?.apply {
                layoutParams?.height = ViewHelper.getStatusBarHeight(context)
                visibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) View.INVISIBLE else View.VISIBLE
            }
            setStatusBarAlpha()
        }
    }

    private fun setStatusBarAlpha() {
        val drawable = statusBarBackground?.background
        drawable?.alpha = 0
        statusBarBackground?.background = drawable
    }

    private fun onClickShareButton() {
        //TODO SHARE FUNC
    }

    private fun onClickNavGlobalButton() {
        //TODO CLICK NAVGLOBAL
    }

    private fun onClickListTransactionButton() {
        //TODO CLICK LIST TRANSACTION
    }

    private fun observeLiveData() {
        observe(viewModel.homeLayoutList) {
            removeAllScrollListener()
            when (it) {
                is Success -> onSuccessGetHomeLayout(it.data)
            }

            rvHome?.post {
                addScrollListener()
                resetSwipeLayout()
            }
        }
    }

    private fun onSuccessGetHomeLayout(data: TokoFoodHomeListUiModel) {
        when (data.state) {
            TokoFoodHomeLayoutState.SHOW -> onShowHomeLayout(data)
            TokoFoodHomeLayoutState.HIDE -> onHideHomeLayout(data)
            TokoFoodHomeLayoutState.LOADING -> onLoadingHomelayout(data)
            else -> showHomeLayout(data)
        }
    }

    private fun onHideHomeLayout(data: TokoFoodHomeListUiModel) {
        hideHeaderBackground()
    }

    private fun onShowHomeLayout(data: TokoFoodHomeListUiModel) {
        showHomeLayout(data)
        showHeaderBackground()
        getLayoutComponentData()
    }

    private fun onLoadingHomelayout(data: TokoFoodHomeListUiModel) {
        //showHomeLayout(data)
        loadHeaderBackground()
        showLayout()
    }

    private fun showHomeLayout(data: TokoFoodHomeListUiModel) {
        rvHome?.post {
            adapter.submitList(data.items)
        }
    }

    private fun setupSwipeRefreshLayout() {
        context?.let {
            swipeLayout?.setMargin(spaceZero, NavToolbarExt.getToolbarHeight(it), spaceZero, spaceZero)
            swipeLayout?.setOnRefreshListener {
                onRefreshLayout()
            }
        }
    }

    private fun createHomeComponentScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                evaluateHomeComponentOnScroll(recyclerView, dy)
            }
        }
    }

    private fun createNavBarScrollListener(): NavRecyclerViewScrollListener? {
        return navToolbar?.let { toolbar ->
            NavRecyclerViewScrollListener(
                navToolbar = toolbar,
                startTransitionPixel = homeMainToolbarHeight,
                toolbarTransitionRangePixel = resources.getDimensionPixelSize(unifyR.spacing_lvl4),
                navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                    override fun onAlphaChanged(offsetAlpha: Float) { /* nothing to do */ }

                    override fun onSwitchToLightToolbar() { /* nothing to do */ }

                    override fun onSwitchToDarkToolbar() {
                        navToolbar?.hideShadow()
                    }

                    override fun onYposChanged(yOffset: Int) {}
                },
                fixedIconColor = NavToolbar.Companion.Theme.TOOLBAR_LIGHT_TYPE
            )
        }
    }

    private fun evaluateHomeComponentOnScroll(recyclerView: RecyclerView, dy: Int) {
        movingPosition += dy
        ivHeaderBackground?.y = if(movingPosition >= 0) {
            -(movingPosition.toFloat())
        } else {
            resetMovingPosition()
            movingPosition.toFloat()
        }
        if (recyclerView.canScrollVertically(1) || movingPosition != 0) {
            navToolbar?.showShadow(lineShadow = true)
        } else {
            navToolbar?.hideShadow(lineShadow = true)
        }
    }

    private fun resetSwipeLayout() {
        swipeLayout?.isEnabled = true
        swipeLayout?.isRefreshing = false
    }

    private fun resetMovingPosition() {
        movingPosition = 0
    }

    private fun removeAllScrollListener() {
        removeNavBarScrollListener()
        removeHomeComponentScrollListener()
    }

    private fun addScrollListener() {
        addNavBarScrollListener()
        addHomeComponentScrollListener()
    }

    private fun addHomeComponentScrollListener() {
        rvHome?.addOnScrollListener(homeComponentScrollListener)
    }

    private fun removeHomeComponentScrollListener() {
        rvHome?.removeOnScrollListener(homeComponentScrollListener)
    }

    private fun addNavBarScrollListener() {
        navBarScrollListener?.let {
            rvHome?.addOnScrollListener(it)
        }
    }

    private fun removeNavBarScrollListener() {
        navBarScrollListener?.let {
            rvHome?.removeOnScrollListener(it)
        }
    }

    private fun loadHeaderBackground() {
        ivHeaderBackground?.show()
        ivHeaderBackground?.setImageResource(R.drawable.background_tokofood_header_shimmering)
    }

    private fun showHeaderBackground() {
        ivHeaderBackground?.show()
        ivHeaderBackground?.setImageResource(R.drawable.background_tokofood_header)
    }

    private fun hideHeaderBackground() {
        ivHeaderBackground?.hide()
    }

    private fun hideStatusBar() {
        binding?.root?.apply {
            fitsSystemWindows = false
            requestApplyInsets()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !context.isDarkMode()) {
                var flags: Int = systemUiVisibility
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                systemUiVisibility = flags
                activity?.window?.statusBarColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            }

            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                StatusBarUtil.setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
                activity?.window?.statusBarColor = Color.TRANSPARENT
            }
        }
    }

    private fun onRefreshLayout() {
        resetMovingPosition()
        removeAllScrollListener()
        rvLayoutManager?.setScrollEnabled(true)
        loadLayout()
    }


    // region TokoFood Home Component Callback

    private fun createLegoBannerCallback(): TokoFoodHomeLegoComponentCallback {
        return TokoFoodHomeLegoComponentCallback()
    }

    private fun createBannerCallback(): TokoFoodHomeBannerComponentCallback {
        return TokoFoodHomeBannerComponentCallback()
    }

    private fun createCategoryWidgetCallback(): TokoFoodHomeCategoryWidgetV2ComponentCallback {
        return TokoFoodHomeCategoryWidgetV2ComponentCallback()
    }

    // endregion

}