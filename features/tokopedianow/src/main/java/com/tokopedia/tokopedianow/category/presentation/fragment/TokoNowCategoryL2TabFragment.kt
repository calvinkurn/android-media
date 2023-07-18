package com.tokopedia.tokopedianow.category.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.category.di.component.DaggerCategoryL2TabComponent
import com.tokopedia.tokopedianow.category.di.module.CategoryContextModule
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.Component
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryL2TabAdapter
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryL2TabDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryL2TabAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryL2TabViewModel
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.common.util.RecyclerViewGridUtil.addProductItemDecoration
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowL2TabBinding
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.ProductItemViewHolder
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowCategoryL2TabFragment : Fragment() {

    companion object {
        fun newInstance(
            categoryIdL2: String = "",
            components: List<Component>
        ): TokoNowCategoryL2TabFragment {
            return TokoNowCategoryL2TabFragment().apply {
                this.categoryIdL2 = categoryIdL2
                this.components = components
            }
        }

        private const val SPAN_COUNT = 3
        private const val SPAN_FULL_SPACE = 1
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val adapterTypeFactory by lazy {
        CategoryL2TabAdapterTypeFactory(
            adsCarouselListener = createProductAdsCarouselListener(),
            quickFilterListener = createQuickFilterListener()
        )
    }

    private val viewModel: TokoNowCategoryL2TabViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[TokoNowCategoryL2TabViewModel::class.java]
    }

    private val adapter by lazy { CategoryL2TabAdapter(adapterTypeFactory, CategoryL2TabDiffer()) }

    private var binding by autoClearedNullable<FragmentTokopedianowL2TabBinding>()

    private var categoryIdL2: String = ""
    private var components = listOf<Component>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.apply {
            this.categoryIdL2 = this@TokoNowCategoryL2TabFragment.categoryIdL2
            this.components = this@TokoNowCategoryL2TabFragment.components
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokopedianowL2TabBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        observeLiveData()
        onViewCreated()
    }

    override fun onDestroy() {
        adapterTypeFactory.onDestroy()
        super.onDestroy()
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            adapter = this@TokoNowCategoryL2TabFragment.adapter
            layoutManager = GridLayoutManager(context, SPAN_COUNT).apply {
                addOnScrollListener(createEndlessScrollListener(this))
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (adapter?.getItemViewType(position)) {
                            ProductItemViewHolder.LAYOUT -> SPAN_FULL_SPACE
                            else -> SPAN_COUNT
                        }
                    }
                }
            }
            addProductItemDecoration()
        }
    }

    private fun observeLiveData() {
        observe(viewModel.visitableListLiveData) {
            adapter.submitList(it)
        }
    }

    private fun onViewCreated() {
        viewModel.onViewCreated(components)
    }

    private fun injectDependencies() {
        DaggerCategoryL2TabComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .categoryContextModule(CategoryContextModule(requireContext()))
            .build()
            .inject(this)
    }

    private fun createEndlessScrollListener(
        layoutManager: GridLayoutManager
    ): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {

            }
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

    private fun createQuickFilterListener(): QuickFilterListener {
        return object : QuickFilterListener {
            override fun openFilterPage() {

            }
        }
    }
}

