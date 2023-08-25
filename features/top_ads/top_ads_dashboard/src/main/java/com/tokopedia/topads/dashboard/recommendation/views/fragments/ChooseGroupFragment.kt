package com.tokopedia.topads.dashboard.recommendation.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.databinding.FragmentTopadsChooseGroupBinding
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.DEFAULT_EMPTY_STRING
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.DEFAULT_GROUP_TYPE
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopadsProductListState
import com.tokopedia.topads.dashboard.recommendation.viewmodel.ProductRecommendationViewModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation.GroupListAdapter
import javax.inject.Inject

class ChooseGroupFragment : BaseDaggerFragment() {

    private var binding: FragmentTopadsChooseGroupBinding? = null

    private val groupListAdapter by lazy { GroupListAdapter(::onItemCheckedChangeListener) }

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
        binding = FragmentTopadsChooseGroupBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        observeViewModel()
        attachClickListener()
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

        viewModel.getTopadsGroupList(DEFAULT_EMPTY_STRING, DEFAULT_GROUP_TYPE)

        binding?.btnSubmit?.setOnClickListener { }
        binding?.searchGroup?.let {
            Utils.setSearchListener(it, context, it, ::searchGroups)
        }
    }

    private fun searchGroups() {
        val searchEditable = binding?.searchGroup?.searchBarTextField?.text
        val search =
            if (searchEditable.isNullOrEmpty()) DEFAULT_EMPTY_STRING else searchEditable.toString()
        viewModel.getTopadsGroupList(search,DEFAULT_GROUP_TYPE)
    }

    private fun observeViewModel() {
        viewModel.groupListLiveData.observe(viewLifecycleOwner) {
            groupListAdapter.submitList(it)
        }

        viewModel.createGroupLiveData.observe(viewLifecycleOwner) {
            when (val data = it) {
                is TopadsProductListState.Success -> {
                    openSuccessDialog(data.data)
                }
                else -> {}
            }
        }
    }

    private fun attachClickListener() {
        binding?.btnSubmit?.setOnClickListener { btn ->
            val productIds = mutableListOf<String>()
            val list = viewModel.getSelectedProductItems()
            list?.forEach { productIds.add(it.id()) }
            viewModel.groupListLiveData.value?.filter { it.isSelected }?.firstOrNull()
                ?.let { viewModel.topAdsMoveGroup(it.groupId, productIds) }
        }
    }

    private fun openSuccessDialog(createdGroupId: String) {
        val dialog = DialogUnify(
            requireContext(),
            DialogUnify.VERTICAL_ACTION,
            DialogUnify.WITH_ILLUSTRATION
        )
        dialog.setImageUrl(TopAdsProductRecommendationConstants.CREATE_GROUP_SUCCESS_DIALOG_IMG_URL)
        dialog.setDescription(getString(com.tokopedia.topads.common.R.string.topads_common_create_group_success_dailog_desc))
        dialog.setTitle(getString(com.tokopedia.topads.common.R.string.topads_common_product_successfully_advertised))
        dialog.setPrimaryCTAText(getString(com.tokopedia.topads.common.R.string.topads_common_manage_ads_group))
        dialog.setSecondaryCTAText(getString(com.tokopedia.topads.common.R.string.topads_common_stay_here))
        dialog.setPrimaryCTAClickListener {
            val intent =
                RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS).apply {
                    putExtra(
                        TopAdsDashboardConstant.TAB_POSITION,
                        TopAdsProductRecommendationConstants.CONST_2
                    )
                    putExtra(TopAdsDashboardConstant.GROUPID, createdGroupId)
                    putExtra(
                        TopAdsDashboardConstant.GROUP_STRATEGY,
                        TopAdsProductRecommendationConstants.AUTO_BID_CONST
                    )
                }
            startActivity(intent)
            dialog.dismiss()
            requireActivity().finish()
        }
        dialog.setSecondaryCTAClickListener { activity?.finish() }
        dialog.show()
    }

    private fun onItemCheckedChangeListener(groupId: String) {
        viewModel.groupListLiveData.value?.forEach {
            it.isSelected = it.groupId == groupId
        }
        groupListAdapter.notifyDataSetChanged()
        updateSubmitBtnState()
    }

    private fun updateSubmitBtnState() {
        binding?.btnSubmit?.isEnabled =
            !viewModel.groupListLiveData.value?.filter { it.isSelected }.isNullOrEmpty()
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
