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
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Success
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
import com.tokopedia.tokofood.home.domain.data.USPResponse
import com.tokopedia.tokofood.home.presentation.adapter.CustomLinearLayoutManager
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeAdapter
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeAdapterTypeFactory
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeListDiffer
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodHomeUSPViewHolder
import com.tokopedia.tokofood.home.presentation.bottomsheet.TokoFoodUSPBottomSheet
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeListUiModel
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodHomeBannerComponentCallback
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodHomeCategoryWidgetV2ComponentCallback
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodHomeLegoComponentCallback
import com.tokopedia.tokofood.home.presentation.viewmodel.TokoFoodHomeViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyR

@FlowPreview
@ExperimentalCoroutinesApi
class TokoFoodHomeFragment : BaseDaggerFragment(),
        IBaseMultiFragment,
        TokoFoodHomeUSPViewHolder.TokoFoodUSPListener
{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoClearedNullable<FragmentTokofoodHomeBinding>()
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokoFoodHomeViewModel::class.java)
    }

    private val adapter by lazy {
        TokoFoodHomeAdapter(
            typeFactory = TokoFoodHomeAdapterTypeFactory(
                dynamicLegoBannerCallback = createLegoBannerCallback(),
                bannerComponentCallback = createBannerCallback(),
                categoryWidgetCallback = createCategoryWidgetCallback(),
                uspListener = this,
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
    private var swipeLayout: SwipeRefreshLayout? = null
    private var rvLayoutManager: CustomLinearLayoutManager? = null
    private var movingPosition = 0
    private val homeMainToolbarHeight: Int
        get() {
            val defaultHeight = resources.getDimensionPixelSize(
                R.dimen.tokofood_default_toolbar_status_height
            )
            val height = (navToolbar?.height ?: defaultHeight)
            val padding = resources.getDimensionPixelSize(unifyR.dimen.spacing_lvl3)

            return height + padding
        }
    private val spaceZero: Int
        get() = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0).toInt()

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
        setupUi()
        setupNavToolbar()
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

    private fun getLayoutComponentData() {
        viewModel.getLayoutComponentData()
    }

    private fun loadLayout() {
        viewModel.getLoadingState()
    }

    private fun setupUi() {
        view?.apply {
            navToolbar = binding?.navToolbar
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
    }

    private fun setupNavToolbar() {
        setupTopNavigation()
        setIconNavigation()
    }

    private fun setupTopNavigation() {
        navToolbar?.let { toolbar ->
            viewLifecycleOwner.lifecycle.addObserver(toolbar)
            activity?.let {
                toolbar.showShadow(true)
                toolbar.setupToolbarWithStatusBar(it, applyPadding = false, applyPaddingNegative = true)
                toolbar.setToolbarTitle(getString(R.string.tokofood_title))
            }
        }
    }

    private fun setIconNavigation() {
        val icons =
            IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
                .addIcon(IconList.ID_SHARE, onClick = ::onClickShareButton)
                .addIcon(IconList.ID_LIST_TRANSACTION, onClick = ::onClickListTransactionButton)
                .addIcon(IconList.ID_NAV_GLOBAL, onClick = ::onClickNavGlobalButton)
        navToolbar?.setIcon(icons)
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
    }

    private fun onShowHomeLayout(data: TokoFoodHomeListUiModel) {
        showHomeLayout(data)
        getLayoutComponentData()
    }

    private fun onLoadingHomelayout(data: TokoFoodHomeListUiModel) {
        //showHomeLayout(data)
        showLayout()
    }

    private fun showHomeLayout(data: TokoFoodHomeListUiModel) {
        rvHome?.post {
            adapter.submitList(data.items)
        }
    }

    private fun setupSwipeRefreshLayout() {
        context?.let {
            swipeLayout?.setMargin(
                spaceZero,
                NavToolbarExt.getToolbarHeight(it),
                spaceZero,
                spaceZero
            )
            swipeLayout?.setOnRefreshListener {
                onRefreshLayout()
            }
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
    }

    private fun addScrollListener() {
    }

    private fun onRefreshLayout() {
        resetMovingPosition()
        removeAllScrollListener()
        rvLayoutManager?.setScrollEnabled(true)
        loadLayout()
    }

    private fun showUSPBottomSheet(uspResponse: USPResponse) {
        val tokoFoodUSPBottomSheet = TokoFoodUSPBottomSheet.getInstance()
        tokoFoodUSPBottomSheet.setUSP(uspResponse)
        tokoFoodUSPBottomSheet.show(parentFragmentManager, "")
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

    // region listener TokoFood

    override fun onUSPClicked(uspResponse: USPResponse) {
        showUSPBottomSheet(uspResponse)
    }

    // endregion

}