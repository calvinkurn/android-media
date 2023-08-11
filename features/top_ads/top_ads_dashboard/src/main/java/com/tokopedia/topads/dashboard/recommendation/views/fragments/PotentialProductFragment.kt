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
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.advertiseProducts
import com.tokopedia.topads.dashboard.databinding.FragmentTopadsPotentialProductBinding
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.viewmodel.ProductRecommendationViewModel
import com.tokopedia.topads.dashboard.recommendation.views.activities.ProductRecommendationActivity
import javax.inject.Inject
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.DEFAULT_SELECTED_ITEMS_COUNT
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopadsProductListState
import com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation.ProductListAdapter

class PotentialProductFragment : BaseDaggerFragment() {

    private var binding: FragmentTopadsPotentialProductBinding? = null

    private val layoutManager by lazy {
        LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private val productListAdapter by lazy { ProductListAdapter(::onItemCheckedChangeListener) }

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
        (activity as? ProductRecommendationActivity)?.setUpHeader(advertiseProducts)
        initializeViews()
        getProducts()
        observeViewModel()
        attachClickListener()
    }

    private fun initializeViews() {
        binding?.productsListRv?.layoutManager = layoutManager
        binding?.productsListRv?.adapter = productListAdapter
        updateSelectedItemsCount(DEFAULT_SELECTED_ITEMS_COUNT)
    }

    private fun updateSelectedItemsCount(itemsSelectedCount: Int) {
        binding?.itemsCount?.text = String.format(
            getString(R.string.topads_insight_centre_choose_potential_products_with_count),
            itemsSelectedCount
        )
    }

    private fun hideLoader() {
        binding?.productsGroup?.showWithCondition(true)
        binding?.shimmerLoader?.root?.showWithCondition(false)
    }

    private fun showLoader() {
        binding?.productsGroup?.showWithCondition(false)
        binding?.shimmerLoader?.root?.showWithCondition(true)
    }

    private fun getProducts() {
        viewModel.getProductList()
    }

    private fun observeViewModel() {
        viewModel.productItemsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is TopadsProductListState.Success -> {
                    hideLoader()
                    productListAdapter.submitList(it.data)
                }
                is TopadsProductListState.Fail -> {}
                is TopadsProductListState.Loading -> {}
            }
        }
    }

    private fun attachClickListener(){
        binding?.selectProductsButton?.setOnClickListener {

        }

        binding?.selectAllCheckbox?.setOnClickListener {
            when (val products = viewModel.productItemsLiveData.value) {
                is TopadsProductListState.Success -> {
                    products.data.forEach { item ->
                        item.isSelected = binding?.selectAllCheckbox?.isChecked ?: false
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

    private fun updateSelectAllCtaState() {
        when (val products = viewModel.productItemsLiveData.value) {
            is TopadsProductListState.Success -> {
                val list = products.data.filter { it.isSelected }
                updateSelectedItemsCount(list.size)
                binding?.selectAllCheckbox?.isChecked = list.isNotEmpty()
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
