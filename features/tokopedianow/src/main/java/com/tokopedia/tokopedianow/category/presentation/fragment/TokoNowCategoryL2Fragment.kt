package com.tokopedia.tokopedianow.category.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.analytic.CategoryL2Analytic
import com.tokopedia.tokopedianow.category.di.component.DaggerCategoryL2Component
import com.tokopedia.tokopedianow.category.di.module.CategoryContextModule
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryL2Differ
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryL2AdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.adapter.viewpager.CategoryL2TabViewPagerAdapter
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2TabUiModel
import com.tokopedia.tokopedianow.category.presentation.view.CategoryL2View
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryL2HeaderViewHolder.CategoryL2HeaderListener
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryL2ViewModel
import com.tokopedia.tokopedianow.common.util.addViewOnScreenObserver
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowCategoryBaseBinding
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowCategoryL2Binding
import com.tokopedia.tokopedianow.category.domain.model.CategorySharingModel
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowCategoryL2Fragment : BaseCategoryFragment(), CategoryL2View {

    companion object {
        fun newInstance(
            categoryL1: String,
            categoryL2: String,
            deepLink: String,
            queryParamMap: HashMap<String, String>
        ): TokoNowCategoryL2Fragment {
            return TokoNowCategoryL2Fragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_CATEGORY_ID_L1, categoryL1)
                    putString(EXTRA_CATEGORY_ID_L2, categoryL2)
                }
                this.currentCategoryId = categoryL2
                this.queryParamMap = queryParamMap
                this.deepLink = deepLink
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var categoryL2Analytic: CategoryL2Analytic

    private var binding by autoClearedNullable<FragmentTokopedianowCategoryL2Binding>()

    private val tabSelectedListener by lazy { createTabSelectedListener() }

    private var viewPagerAdapter: CategoryL2TabViewPagerAdapter? = null

    override val viewModel: TokoNowCategoryL2ViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[TokoNowCategoryL2ViewModel::class.java]
    }

    override val swipeRefreshLayout: SwipeToRefresh?
        get() = binding?.swipeToRefresh

    override val recyclerView: RecyclerView?
        get() = binding?.recyclerView

    override fun createAdapterTypeFactory(): CategoryL2AdapterTypeFactory {
        return CategoryL2AdapterTypeFactory(
            tokoNowView = createTokoNowViewCallback(),
            headerListener = createHeaderListener(),
            chooseAddressListener = createChooseAddressWidgetCallback()
        )
    }

    override fun createAdapterDiffer() = CategoryL2Differ()

    override fun createMainView(): View? {
        binding = FragmentTokopedianowCategoryL2Binding.inflate(LayoutInflater.from(context))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        setupAppBarLayout()
        setupViewPager()
        setupTracker()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onViewResume()
    }

    override fun initInjector() {
        DaggerCategoryL2Component.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .categoryContextModule(CategoryContextModule(requireContext()))
            .build()
            .inject(this)
    }

    override fun setupMainLayout(
        container: ConstraintLayout?,
        mainLayout: FrameLayout?
    ) {
        super.setupMainLayout(container, mainLayout)
        container?.let {
            val constraintSet = ConstraintSet().apply {
                clone(container)
            }
            constraintSet.connect(
                R.id.main_layout,
                ConstraintSet.TOP,
                R.id.nav_toolbar,
                ConstraintSet.BOTTOM
            )
            constraintSet.applyTo(container)
        }
    }

    override fun showPageLoading(
        binding: FragmentTokopedianowCategoryBaseBinding?
    ) {
        binding?.apply {
            mainLayout.show()
            globalError.hide()
            oocLayout.hide()
        }
        viewModel.showPageLoading()
    }

    override fun updateToolbarNotificationCounter() {
        updateToolbarNotification()
    }

    override fun showFragment(fragment: Fragment) {
        binding?.root?.let { container ->
            childFragmentManager.beginTransaction()
                .add(container.id, fragment)
                .commit()
        }
    }

    override fun setShareModel(model: CategorySharingModel) {
        setCategorySharingModel(model)
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        val tabLiveData = viewModel.categoryTabLiveData.value
        val selectedTabPosition = tabLiveData?.selectedTabPosition.orZero()
        val selectedTabFragment = viewPagerAdapter?.fragments?.getOrNull(selectedTabPosition)
        selectedTabFragment?.onCartItemsUpdated(miniCartSimplifiedData)
    }

    override fun updateMiniCartWidget(data: MiniCartSimplifiedData?) {
        if (data != null) {
            renderMiniCartWidget(data)
        } else {
            updateMiniCartData()
        }
    }

    private fun observeLiveData() {
        observe(viewModel.categoryTabLiveData) { data ->
            removeTabSelectedListener()
            clearAllCategoryTabs()
            addTabFragments(data)
            setupTabsUnify(data)
        }

        observe(viewModel.onTabSelectedLiveData) {
            trackClickCategoryTab(it)
        }
    }

    private fun setupAppBarLayout() {
        binding?.apply {
            appBarLayout.addOnOffsetChangedListener { _, verticalOffset ->
                swipeToRefresh.isEnabled = (verticalOffset == 0)
            }
        }
    }

    private fun setupViewPager() {
        binding?.apply {
            val onPageChangeCallback = createOnPageChangeCallback()
            viewPager.registerOnPageChangeCallback(onPageChangeCallback)
            viewPager.offscreenPageLimit = 1
        }
    }

    private fun addTabFragments(data: CategoryL2TabUiModel) {
        binding?.apply {
            val fragments = data.tabList.mapIndexed { _, tab ->
                val fragment = TokoNowCategoryL2TabFragment.newInstance(tab)
                fragment.categoryL2View = this@TokoNowCategoryL2Fragment
                fragment
            }
            viewPagerAdapter = CategoryL2TabViewPagerAdapter(
                fragment = this@TokoNowCategoryL2Fragment,
                fragments = fragments
            )
            viewPager.adapter = viewPagerAdapter
        }
    }

    private fun setupTabsUnify(data: CategoryL2TabUiModel) {
        binding?.apply {
            TabsUnifyMediator(tabsUnify, viewPager) { tab, position ->
                tab.setCustomText(data.tabList[position].title)
            }
            viewPager.post {
                val selectedTabPosition = data.selectedTabPosition
                setViewPagerCurrentItem(selectedTabPosition)
                addOnTabSelectedListener()
            }
            tabsUnify.apply {
                customTabMode = TabLayout.MODE_SCROLLABLE
                customTabGravity = TabLayout.GRAVITY_START
                whiteShadeRight.gone()
                whiteShadeLeft.gone()
            }
        }
    }

    private fun addOnTabSelectedListener() {
        binding?.apply {
            tabsUnify.tabLayout.addOnTabSelectedListener(tabSelectedListener)
        }
    }

    private fun removeTabSelectedListener() {
        binding?.apply {
            tabsUnify.tabLayout.removeOnTabSelectedListener(tabSelectedListener)
        }
    }

    private fun createTabSelectedListener(): OnTabSelectedListener {
        return object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.onTabSelected(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        }
    }

    private fun setViewPagerCurrentItem(position: Int) {
        binding?.viewPager?.setCurrentItem(position, false)
    }

    private fun clearAllCategoryTabs() {
        binding?.apply {
            tabsUnify.tabLayout.removeAllTabs()
        }
    }

    private fun setupTracker() {
        binding?.tabsUnify?.addViewOnScreenObserver {
            categoryL2Analytic.sendImpressionLCategoryNavigationEvent(
                categoryIdL1,
                categoryIdL2,
                viewModel.getWarehouseIds()
            )
        }
    }

    private fun openAllCategoryPage() {
        RouteManager.route(
            context,
            ApplinkConstInternalTokopediaNow.SEE_ALL_CATEGORY
        )
    }

    private fun trackClickOtherCategory() {
        categoryL2Analytic.sendClickOtherCategoriesEvent(
            categoryIdL1,
            viewModel.getWarehouseIds()
        )
    }

    private fun trackClickCategoryTab(tab: CategoryL2TabUiModel) {
        val selectedTabPosition = tab.selectedTabPosition
        val categoryIdL2 = tab.tabList[selectedTabPosition].categoryIdL2
        categoryL2Analytic.sendClickLCategoryNavigationEvent(
            categoryIdL1,
            categoryIdL2,
            viewModel.getWarehouseIds()
        )
    }

    private fun createHeaderListener(): CategoryL2HeaderListener {
        return object : CategoryL2HeaderListener {
            override fun onClickOtherCategory() {
                openAllCategoryPage()
                trackClickOtherCategory()
            }
        }
    }

    private fun createOnPageChangeCallback(): OnPageChangeCallback {
        return object : OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                binding?.swipeToRefresh?.isEnabled =
                    state == ViewPager2.SCROLL_STATE_IDLE
            }
        }
    }

    private fun createChooseAddressWidgetCallback(): TokoNowChooseAddressWidgetListener {
        return object : TokoNowChooseAddressWidgetListener {
            override fun onChooseAddressWidgetRemoved() {
                viewModel.removeChooseAddressWidget()
            }

            override fun onClickChooseAddressWidgetTracker() {
            }
        }
    }
}
