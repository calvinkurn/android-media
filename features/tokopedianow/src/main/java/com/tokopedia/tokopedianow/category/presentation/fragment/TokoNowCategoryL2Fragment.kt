package com.tokopedia.tokopedianow.category.presentation.fragment

import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.category.di.component.DaggerCategoryL2Component
import com.tokopedia.tokopedianow.category.di.module.CategoryContextModule
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryL2Differ
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryL2AdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowChooseAddressWidgetCallback
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2TabModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryL2TabViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryL2TabViewHolder.CategoryL2TabListener
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryL2ViewModel
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowCategoryBaseBinding
import com.tokopedia.unifycomponents.TabsUnify
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

    private var tabsUnify: TabsUnify? = null
    private var tabScrollListener: OnScrollListener? = null
    private val tabSelectedListener by lazy { createOnTabSelectedListener() }

    override val viewModel: TokoNowCategoryL2ViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[TokoNowCategoryL2ViewModel::class.java]
    }

    override fun createAdapterTypeFactory(): CategoryL2AdapterTypeFactory {
        return CategoryL2AdapterTypeFactory(
            chooseAddressListener = createChooseAddressWidgetCallback(),
            productAdsCarouselListener = createProductAdsCarouselListener(),
            tokoNowView = createTokoNowViewCallback(),
            tabListener = createTabListener()
        )
    }

    override fun createAdapterDiffer() = CategoryL2Differ()

    override fun setupView(binding: FragmentTokopedianowCategoryBaseBinding?) {
        super.setupView(binding)
        binding?.apply {
            setupMainLayout()
            setupTabLayout()
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        observeCategoryTabs()
        observeLoadMore()
    }

    override fun initInjector() {
        DaggerCategoryL2Component.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .categoryContextModule(CategoryContextModule(requireContext()))
            .build()
            .inject(this)
    }

    override fun onResume() {
        super.onResume()
        handleOnResume()
    }

    private fun FragmentTokopedianowCategoryBaseBinding.setupMainLayout() {
        navToolbar.post {
            val statusBarHeight = vStatusBarBackground.height
            val marginTop = statusBarHeight + navToolbarHeight
            val layoutParams = mainLayout.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.topMargin = marginTop
            mainLayout.layoutParams = layoutParams
        }
    }

    private fun FragmentTokopedianowCategoryBaseBinding.setupTabLayout() {
        this@TokoNowCategoryL2Fragment.tabsUnify = tabsUnify
    }

    private fun observeCategoryTabs() {
        observe(viewModel.categoryTabs) {
            addCategoryTab(it)
            setupTabScrollListener()
            setupTabSelectedListener(it)
        }
    }

    private fun observeLoadMore() {
        observe(viewModel.loadMore) {
            onLoadMore()
        }
    }

    private fun addCategoryTab(it: List<CategoryL2TabModel>) {
        tabsUnify?.apply {
            tabLayout.removeAllTabs()
            it.forEach { tab ->
                addNewTab(tab.name)
            }
        }
    }

    private fun setupTabScrollListener() {
        val tabIndex = viewModel.getTabPosition()
        removeScrollListener(tabScrollListener)
        setFloatingTabScrollListener(tabIndex)
        addScrollListener(tabScrollListener)
    }

    private fun setupTabSelectedListener(
        categoryL2TabModels: List<CategoryL2TabModel>
    ) {
        tabsUnify?.apply {
            val selectedTabPosition = categoryL2TabModels
                .indexOfFirst { it.id == categoryIdL2 }
            tabLayout.removeOnTabSelectedListener(tabSelectedListener)
            tabLayout.getTabAt(selectedTabPosition)?.select()
            tabLayout.addOnTabSelectedListener(tabSelectedListener)
            customTabMode = TabLayout.MODE_SCROLLABLE
        }
    }

    private fun onLoadMore() {
        findTabViewHolder {
            it.loadMore()
        }
    }

    private fun handleOnResume() {
        findTabViewHolder {
            it.onResume()
        }
    }

    private fun findTabViewHolder(
        viewHolderMethod: (CategoryL2TabViewHolder) -> Unit
    ) {
        val tabPosition = viewModel.getTabPosition()
        recyclerView?.findViewHolderForAdapterPosition(tabPosition)
            ?.let {
                if (it is CategoryL2TabViewHolder) {
                    viewHolderMethod.invoke(it)
                }
            }
    }

    private fun createTabListener(): CategoryL2TabListener {
        return object : CategoryL2TabListener {
            override fun onTabSelected(position: Int) {
                tabsUnify?.tabLayout
                    ?.getTabAt(position)?.select()
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
    }

    private fun createOnTabSelectedListener() = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            viewModel.onTabSelected(tab.position)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
        }
    }

    private fun setFloatingTabScrollListener(tabIndex: Int) {
        tabScrollListener = object: OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                recyclerView.findViewHolderForAdapterPosition(tabIndex)?.let { viewHolder ->
                    val topPosition = viewHolder.itemView.top

                    if(topPosition < 0) {
                        tabsUnify?.show()
                    } else {
                        tabsUnify?.hide()
                    }
                }
            }
        }
    }
}
