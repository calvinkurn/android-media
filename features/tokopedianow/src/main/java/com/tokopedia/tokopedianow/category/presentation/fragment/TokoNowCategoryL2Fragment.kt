package com.tokopedia.tokopedianow.category.presentation.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.category.di.component.DaggerCategoryL2Component
import com.tokopedia.tokopedianow.category.di.module.CategoryL2ContextModule
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryL2Differ
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryL2AdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowChooseAddressWidgetCallback
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2TabUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryL2TabViewHolder.CategoryL2TabListener
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryL2ViewModel
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
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

    private var floatingTab: TabsUnify? = null
    private var floatingTabScrollListener: OnScrollListener? = null

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

    override fun observeLiveData() {
        super.observeLiveData()
        observeTabCategoryNameList()
    }

    override fun observeVisitableList() {
        observe(viewModel.visitableListLiveData) {
            setupTabScrollListener(it)
            submitList(it)
        }
    }

    override fun setupTabLayout(tab: TabsUnify) {
        floatingTab = tab
    }

    override fun initInjector() {
        DaggerCategoryL2Component.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .categoryL2ContextModule(CategoryL2ContextModule(requireContext()))
            .build()
            .inject(this)
    }

    private fun observeTabCategoryNameList() {
        observe(viewModel.categoryTab) {
            it.forEach { title ->
                floatingTab?.addNewTab(title)
            }
        }
    }

    private fun setupTabScrollListener(visitables: List<Visitable<*>>) {
        visitables.find { it is CategoryL2TabUiModel }?.let {
            val tabIndex = visitables.indexOf(it)
            removeScrollListener(floatingTabScrollListener)
            setFloatingTabScrollListener(tabIndex)
            addScrollListener(floatingTabScrollListener)
        }
    }

    private fun setFloatingTabScrollListener(tabIndex: Int) {
        floatingTabScrollListener = object: OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                recyclerView.findViewHolderForAdapterPosition(tabIndex)?.let { viewHolder ->
                    val topPosition = viewHolder.itemView.top

                    if(topPosition < 0) {
                        floatingTab?.show()
                    } else {
                        floatingTab?.hide()
                    }
                }
            }
        }
    }

    private fun createTabListener(): CategoryL2TabListener {
        return object : CategoryL2TabListener {
            override fun onTabSelected(position: Int) {
                floatingTab?.tabLayout
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
}
