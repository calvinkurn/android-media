package com.tokopedia.topads.dashboard.recommendation.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.databinding.FragmentTopadsChooseGroupBinding
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.common.decoration.ChipsInsightItemDecoration
import com.tokopedia.topads.dashboard.recommendation.data.mapper.ProductRecommendationMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductItemUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopadsProductListState
import com.tokopedia.topads.dashboard.recommendation.viewmodel.ProductRecommendationViewModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation.GroupListAdapter
import javax.inject.Inject

class ChooseGroupFragment : BaseDaggerFragment() {

    private var binding: FragmentTopadsChooseGroupBinding? = null

    private val groupListAdapter by lazy { GroupListAdapter() }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var mapper: ProductRecommendationMapper

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
        binding = FragmentTopadsChooseGroupBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        observeViewModel()
    }

    private fun init() {
        when (val products = viewModel.productItemsLiveData.value) {
            is TopadsProductListState.Success -> {
                val selectedItems =
                    products.data.filter {
                        (it as? ProductItemUiModel)?.isSelected ?: false
                    }
                binding?.btnSubmit?.text = String.format(
                    getString(R.string.topads_insight_centre_advertise_product_with_count),
                    selectedItems.size
                )
            }
            else -> {}
        }
        viewModel.getTopadsGroupList("")

        binding?.groupsRv?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding?.groupsRv?.addItemDecoration(
            ChipsInsightItemDecoration()
        )
        binding?.groupsRv?.adapter = groupListAdapter
    }

    private fun observeViewModel() {
        viewModel.groupListLiveData.observe(viewLifecycleOwner) {
            groupListAdapter.submitList(it)
        }
    }

    companion object {
        fun createInstance(): ChooseGroupFragment {
            return ChooseGroupFragment()
        }
    }

    override fun getScreenName(): String = javaClass.name
    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }
}
