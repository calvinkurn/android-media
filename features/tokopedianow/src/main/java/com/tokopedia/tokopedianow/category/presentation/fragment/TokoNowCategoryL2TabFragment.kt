package com.tokopedia.tokopedianow.category.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.category.di.component.DaggerCategoryL2TabComponent
import com.tokopedia.tokopedianow.category.di.module.CategoryContextModule
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryL2TabAdapter
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryL2TabDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryL2TabAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryL2TabViewModel
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowL2TabBinding
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowCategoryL2TabFragment : Fragment() {

    companion object {
        fun newInstance(
            components: List<GetCategoryLayoutResponse.Component>
        ): TokoNowCategoryL2TabFragment {
            return TokoNowCategoryL2TabFragment().apply {
                categoryComponents = components
            }
        }
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

    var categoryComponents = listOf<GetCategoryLayoutResponse.Component>()

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
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeLiveData() {
        observe(viewModel.visitableListLiveData) {
            adapter.submitList(it)
        }
    }

    private fun onViewCreated() {
        viewModel.onViewCreated(categoryComponents)
    }

    private fun injectDependencies() {
        DaggerCategoryL2TabComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .categoryContextModule(CategoryContextModule(requireContext()))
            .build()
            .inject(this)
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

