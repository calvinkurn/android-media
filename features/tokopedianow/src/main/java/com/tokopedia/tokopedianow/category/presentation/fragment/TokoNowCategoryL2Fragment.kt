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
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.di.component.DaggerCategoryL2Component
import com.tokopedia.tokopedianow.category.di.module.CategoryContextModule
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryL2Differ
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryL2AdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.adapter.viewpager.CategoryL2TabViewPagerAdapter
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowChooseAddressWidgetCallback
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2TabUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryL2TabViewHolder.CategoryL2TabListener
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryL2ViewModel
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowCategoryL2Binding
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowCategoryL2Fragment : BaseCategoryFragment() {

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
            chooseAddressListener = createChooseAddressWidgetCallback(),
            productAdsCarouselListener = createProductAdsCarouselListener(),
            tokoNowView = createTokoNowViewCallback(),
            tabListener = createTabListener()
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

    override fun initInjector() {
        DaggerCategoryL2Component.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .categoryContextModule(CategoryContextModule(requireContext()))
            .build()
            .inject(this)
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
                binding?.swipeToRefresh?.isEnabled = (verticalOffset == 0)
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
            data.categoryL2Ids.forEach {
                val fragment = TokoNowCategoryL2TabFragment
                    .newInstance(data.categoryIdL1, it, data.componentList)
                viewPagerAdapter.addFragment(fragment)
            }
            viewPager.currentItem = selectedTabPosition
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

    private fun onViewCreated() = viewModel.onViewCreated()

    private fun createOnPageChangeCallback(): OnPageChangeCallback {
        return object : OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                binding?.swipeToRefresh?.isEnabled =
                    state == ViewPager2.SCROLL_STATE_IDLE
            }
        }
    }

    private fun createTabListener(): CategoryL2TabListener {
        return object : CategoryL2TabListener {
            override fun onTabSelected(position: Int) {

            }
        }
    }

    private fun createChooseAddressWidgetCallback(): TokoNowChooseAddressWidgetCallback {
        return TokoNowChooseAddressWidgetCallback {

        }
    }

    private fun createProductAdsCarouselListener() = object : ProductAdsCarouselListener {
        override fun onProductCardClicked(
            position: Int,
            title: String,
            product: ProductCardCompactCarouselItemUiModel
        ) {

        }

        override fun onProductCardImpressed(
            position: Int,
            title: String,
            product: ProductCardCompactCarouselItemUiModel
        ) {
        }

        override fun onProductCardQuantityChanged(
            position: Int,
            product: ProductCardCompactCarouselItemUiModel,
            quantity: Int
        ) {
        }

        override fun onProductCardAddVariantClicked(
            position: Int,
            product: ProductCardCompactCarouselItemUiModel
        ) {
        }

        override fun onProductCardAddToCartBlocked() {

        }
    }
}
