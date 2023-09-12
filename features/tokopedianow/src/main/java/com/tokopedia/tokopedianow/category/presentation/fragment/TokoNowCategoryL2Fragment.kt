package com.tokopedia.tokopedianow.category.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.di.component.DaggerCategoryL2Component
import com.tokopedia.tokopedianow.category.di.module.CategoryContextModule
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryL2Differ
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryL2AdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.adapter.viewpager.CategoryL2TabViewPagerAdapter
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2TabUiModel
import com.tokopedia.tokopedianow.category.presentation.view.CategoryL2View
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryL2ViewModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowCategoryBaseBinding
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowCategoryL2Binding
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowCategoryL2Fragment : BaseCategoryFragment(), CategoryL2View {

    companion object {
        fun newInstance(
            categoryL1: String,
            categoryL2: String,
            queryParamMap: HashMap<String, String>?
        ): TokoNowCategoryL2Fragment {
            return TokoNowCategoryL2Fragment().apply {
                this.categoryIdL1 = categoryL1
                this.categoryIdL2 = categoryL2
                this.currentCategoryId = categoryL2
                this.queryParamMap = queryParamMap
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoClearedNullable<FragmentTokopedianowCategoryL2Binding>()

    private val viewPagerAdapter by lazy { CategoryL2TabViewPagerAdapter(this) }

    override val viewModel: TokoNowCategoryL2ViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
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
        setupTabsUnify()
        onViewCreated()
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

    private fun observeLiveData() {
        observe(viewModel.categoryTab) { data ->
            clearAllCategoryTabs()
            addTabFragments(data)
            setupTabsUnifyMediator(data)
            setSelectedTabPosition(data)
        }
    }

    private fun setupAppBarLayout() {
        binding?.apply {
            appBarLayout.addOnOffsetChangedListener { _, verticalOffset ->
                val isEmptyState = viewModel.isEmptyState()
                swipeToRefresh.isEnabled = (verticalOffset == 0) && !isEmptyState
            }
        }
    }

    private fun setupViewPager() {
        binding?.apply {
            val onPageChangeCallback = createOnPageChangeCallback()
            viewPager.registerOnPageChangeCallback(onPageChangeCallback)
            viewPager.offscreenPageLimit = 1
            viewPager.adapter = viewPagerAdapter
        }
    }

    private fun setupTabsUnify() {
        binding?.apply {
            tabsUnify.customTabMode = TabLayout.MODE_SCROLLABLE
            tabsUnify.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.setCurrentItem(tab.position, false)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
        }
    }

    private fun addTabFragments(data: CategoryL2TabUiModel) {
        binding?.apply {
            val selectedTabPosition = data.selectedTabPosition
            data.tabList.forEach { tab ->
                val fragment = TokoNowCategoryL2TabFragment.newInstance(tab)
                fragment.categoryL2View = this@TokoNowCategoryL2Fragment
                viewPagerAdapter.addFragment(fragment)
            }
            viewPager.currentItem = selectedTabPosition + 1
        }
    }

    private fun setupTabsUnifyMediator(data: CategoryL2TabUiModel) {
        binding?.apply {
            TabsUnifyMediator(tabsUnify, viewPager) { tab, position ->
                val title = data.titleList.getOrNull(position).orEmpty()
                if (title.isNotBlank()) tab.setCustomText(title)
            }
        }
    }

    private fun setSelectedTabPosition(data: CategoryL2TabUiModel) {
        binding?.tabsUnify?.apply {
            tabLayout.post {
                val selectedTabPosition = data.selectedTabPosition
                viewPagerAdapter.setSelectedTabPosition(selectedTabPosition)
                tabLayout.getTabAt(selectedTabPosition)?.select()
            }
        }
    }

    private fun clearAllCategoryTabs() {
        binding?.apply {
            tabsUnify.tabLayout.removeAllTabs()
            viewPagerAdapter.clearFragments()
        }
    }

    private fun onViewCreated() {
        viewModel.onViewCreated()
    }

    private fun createOnPageChangeCallback(): OnPageChangeCallback {
        return object : OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                val isEmptyState = viewModel.isEmptyState()
                binding?.swipeToRefresh?.isEnabled =
                    state == ViewPager2.SCROLL_STATE_IDLE && !isEmptyState
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
