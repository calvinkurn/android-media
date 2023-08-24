package com.tokopedia.topads.dashboard.recommendation.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.databinding.FragmentTopadsChooseGroupBinding
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.DEFAULT_EMPTY_STRING
import com.tokopedia.topads.dashboard.recommendation.data.mapper.ProductRecommendationMapper
import com.tokopedia.topads.dashboard.recommendation.viewmodel.ProductRecommendationViewModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation.GroupListAdapter
import javax.inject.Inject

class ChooseGroupFragment : BaseDaggerFragment() {

    private var binding: FragmentTopadsChooseGroupBinding? = null

    private val groupListAdapter by lazy { GroupListAdapter(::onItemCheckedChangeListener) }

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
        binding?.btnSubmit?.text = String.format(
            getString(R.string.topads_insight_centre_advertise_product_with_count),
            viewModel.getSelectedProductItems()?.size
        )

        binding?.groupsRv?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding?.groupsRv?.adapter = groupListAdapter

        viewModel.getTopadsGroupList(DEFAULT_EMPTY_STRING)

        binding?.btnSubmit?.setOnClickListener {  }
        binding?.searchGroup?.let {
            Utils.setSearchListener(it, context, it, ::searchGroups)
        }
    }

    private fun searchGroups(){
        val searchEditable = binding?.searchGroup?.searchBarTextField?.text
        val search = if(searchEditable.isNullOrEmpty()) DEFAULT_EMPTY_STRING else searchEditable.toString()
        viewModel.getTopadsGroupList(search)
    }

    private fun observeViewModel() {
        viewModel.groupListLiveData.observe(viewLifecycleOwner) {
            groupListAdapter.submitList(it)
        }
    }

    private fun onItemCheckedChangeListener(groupId: String){
        viewModel.groupListLiveData.value?.forEach {
            it.isSelected = it.groupId == groupId
        }
        groupListAdapter.notifyDataSetChanged()
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
