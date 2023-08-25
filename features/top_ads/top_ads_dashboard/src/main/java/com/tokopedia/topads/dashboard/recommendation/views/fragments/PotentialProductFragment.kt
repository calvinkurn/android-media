package com.tokopedia.topads.dashboard.recommendation.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topads.dashboard.databinding.FragmentTopadsPotentialProductBinding
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.viewmodel.ProductRecommendationViewModel
import javax.inject.Inject
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.DEFAULT_SELECTED_ITEMS_COUNT
import com.tokopedia.topads.dashboard.recommendation.data.mapper.ProductRecommendationMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductItemUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopadsProductListState
import com.tokopedia.topads.dashboard.recommendation.views.activities.RoutingCallback
import com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation.ProductListAdapter

class PotentialProductFragment : BaseDaggerFragment() {

    private var binding: FragmentTopadsPotentialProductBinding? = null

    private val productListAdapter by lazy {
        ProductListAdapter(
            ::onItemCheckedChangeListener,
            ::reloadPage
        )
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ProductRecommendationViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[ProductRecommendationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopadsPotentialProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        setUpProductsList()
        observeViewModel()
        attachClickListener()
    }

    private fun init() {
        if (binding?.productsListRv?.layoutManager == null)
            binding?.productsListRv?.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
        binding?.productsListRv?.adapter = productListAdapter
    }

    private fun setUpProductsList(){
        if(viewModel.productItemsLiveData.value == null){
            loadProducts()
            updateSelectedItemsCount(DEFAULT_SELECTED_ITEMS_COUNT)
        } else {
            when (val products = viewModel.productItemsLiveData.value) {
                is TopadsProductListState.Success -> {
                    showProductsList()
                    productListAdapter.submitList(products.data)
                    val list = products.data.filter { (it as? ProductItemUiModel)?.isSelected ?: false }
                    updateSelectAllCtaState()
                    updateSelectedItemsCount(list.size)
                }
                is TopadsProductListState.Fail -> {
                    showEmptyState()
                    productListAdapter.submitList(viewModel.getMapperInstance().getEmptyProductListDefaultUiModel())
                }
                else -> {}
            }
        }
    }

    private fun updateSelectedItemsCount(itemsSelectedCount: Int) {
        binding?.itemsCount?.text = String.format(
            getString(R.string.topads_insight_centre_choose_potential_products_with_count),
            itemsSelectedCount
        )
    }

    private fun showProductsList() {
        binding?.shimmerLoader?.root?.showWithCondition(false)
        binding?.productsListRv?.showWithCondition(true)
        binding?.selectAllCheckbox?.showWithCondition(true)
        binding?.itemsCount?.showWithCondition(true)
        binding?.selectProductsButton?.showWithCondition(true)
    }

    private fun showEmptyState() {
        binding?.shimmerLoader?.root?.showWithCondition(false)
        binding?.productsListRv?.showWithCondition(true)
        binding?.selectAllCheckbox?.showWithCondition(false)
        binding?.itemsCount?.showWithCondition(false)
        binding?.selectProductsButton?.showWithCondition(false)
    }

    private fun showLoader() {
        binding?.shimmerLoader?.root?.showWithCondition(true)
        binding?.productsListRv?.showWithCondition(false)
        binding?.selectAllCheckbox?.showWithCondition(false)
        binding?.itemsCount?.showWithCondition(false)
        binding?.selectProductsButton?.showWithCondition(false)
    }

    private fun loadProducts() {
        showLoader()
        viewModel.loadProductList()
    }

    private fun observeViewModel() {
        viewModel.productItemsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is TopadsProductListState.Success -> {
                    showProductsList()
                    productListAdapter.submitList(it.data)
                    val list = it.data.filter { (it as? ProductItemUiModel)?.isSelected ?: false }
                    updateSelectedItemsCount(list.size)
                }
                is TopadsProductListState.Fail -> {
                    showEmptyState()
                    productListAdapter.submitList(viewModel.getMapperInstance().getEmptyProductListDefaultUiModel())
                }
                is TopadsProductListState.Loading -> {}
            }
        }
    }

    private fun attachClickListener() {
        binding?.headerPotentialProduct?.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding?.selectProductsButton?.setOnClickListener {
            activity?.let {
                if (it is RoutingCallback) {
                    it.routeToGroupSettings()
                }
            }
        }

        binding?.selectAllCheckbox?.setOnClickListener {
            when (val products = viewModel.productItemsLiveData.value) {
                is TopadsProductListState.Success -> {
                    products.data.forEach { item ->
                        (item as? ProductItemUiModel)?.isSelected =
                            binding?.selectAllCheckbox?.isChecked ?: false
                    }
                    productListAdapter.notifyDataSetChanged()
                    updateSelectAllCtaState()
                }
                else -> {}
            }
        }
    }

    private fun onItemCheckedChangeListener() {
        updateSelectAllCtaState()
    }

    private fun reloadPage() {
        loadProducts()
    }

    private fun updateSelectAllCtaState() {
        when (val products = viewModel.productItemsLiveData.value) {
            is TopadsProductListState.Success -> {
                val list = products.data.filter { (it as? ProductItemUiModel)?.isSelected ?: false }
                updateSelectedItemsCount(list.size)
                binding?.selectAllCheckbox?.isChecked = list.isNotEmpty()
                binding?.selectProductsButton?.isEnabled = list.isNotEmpty()
                binding?.selectAllCheckbox?.setIndeterminate(list.size != products.data.size)
            }
            else -> {}
        }
    }

    companion object {
        fun createInstance(): PotentialProductFragment {
            val fragment = PotentialProductFragment()
            return fragment
        }
    }

    override fun getScreenName(): String = javaClass.name
    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }
}
