package com.tokopedia.topads.dashboard.recommendation.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.common.R as topadscommonR
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.databinding.FragmentTopadsChooseGroupBinding
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.DEFAULT_EMPTY_STRING
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.DEFAULT_GROUP_TYPE
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.DEFAULT_TOPADS_DEPOSITS
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.IDR_CONST
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupItemUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopadsProductListState
import com.tokopedia.topads.dashboard.recommendation.viewmodel.ProductRecommendationViewModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation.GroupListAdapter
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsCreditTopUpActivity
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

class ChooseGroupFragment : BaseDaggerFragment() {

    private var binding: FragmentTopadsChooseGroupBinding? = null
    private var searchGroup: String = DEFAULT_EMPTY_STRING
    private var topadsDeposits: Int = DEFAULT_TOPADS_DEPOSITS

    private val groupListAdapter by lazy {
        GroupListAdapter(
            ::onItemCheckedChangeListener,
            ::reloadPage
        )
    }

    @JvmField
    @Inject
    var viewModelFactory: ViewModelFactory? = null

    private val viewModel: ProductRecommendationViewModel? by lazy(LazyThreadSafetyMode.NONE) {
        viewModelFactory?.let {
            ViewModelProvider(
                requireActivity(),
                it
            )[ProductRecommendationViewModel::class.java]
        }
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
            viewModel?.getSelectedProductItems()?.size
        )

        binding?.groupsRv?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding?.groupsRv?.adapter = groupListAdapter

        viewModel?.getTopAdsDeposit()
        viewModel?.getTopadsGroupList(DEFAULT_EMPTY_STRING, DEFAULT_GROUP_TYPE)

        binding?.btnSubmit?.setOnClickListener { }
        binding?.searchGroup?.let {
            Utils.setSearchListener(it, context, it, ::searchGroups)
        }
    }

    private fun searchGroups() {
        val searchEditable = binding?.searchGroup?.searchBarTextField?.text
        searchGroup =
            if (searchEditable.isNullOrEmpty()) DEFAULT_EMPTY_STRING else searchEditable.toString()
        viewModel?.getTopadsGroupList(searchGroup, DEFAULT_GROUP_TYPE)
    }

    private fun observeViewModel() {
        viewModel?.groupListLiveData?.observe(viewLifecycleOwner) {
            when (val groups = it) {
                is TopadsProductListState.Success -> {
                    groupListAdapter.submitList(groups.data)
                }
                is TopadsProductListState.Empty -> {
                    groupListAdapter.submitList(groups.data)
                }
                is TopadsProductListState.Fail -> {
                    groupListAdapter.submitList(
                        viewModel?.getMapperInstance()?.getFailedGroupStateDefaultUiModel()
                    )
                }
                is TopadsProductListState.Loading -> {
                    groupListAdapter.submitList(
                        viewModel?.getMapperInstance()?.getGroupShimmerUiModel()
                    )
                }
            }
        }

        viewModel?.createGroupLiveData?.observe(viewLifecycleOwner) {
            when (val data = it) {
                is TopadsProductListState.Success -> {
                    if (topadsDeposits > DEFAULT_TOPADS_DEPOSITS) {
                        openSuccessDialog(data.data)
                    } else {
                        openInsufficientCreditsDialog()
                    }
                }
                else -> {
                    view?.let {
                        Toaster.build(
                            it,
                            getString(topadscommonR.string.topads_common_failed_to_create_ads_toast_msg),
                            Snackbar.LENGTH_LONG,
                            Toaster.TYPE_ERROR,
                            getString(topadscommonR.string.topads_common_try_again)
                        ).show()
                    }

                    binding?.btnSubmit?.let {
                        if (it.isLoading) {
                            it.isLoading = false
                        }
                    }
                }
            }
        }

        viewModel?.topadsDeposits?.observe(viewLifecycleOwner) {
            topadsDeposits = it.topadsDashboardDeposits.data.amount
        }
    }

    private fun attachClickListener() {
        binding?.btnSubmit?.setOnClickListener { btn ->
            if (binding?.btnSubmit?.isLoading != null && !(binding?.btnSubmit?.isLoading!!)) {
                val productIds = mutableListOf<String>()
                val list = viewModel?.getSelectedProductItems()
                list?.forEach { productIds.add(it.id()) }
                viewModel?.getGroupList()?.filter { (it as? GroupItemUiModel)?.isSelected ?: false }
                    ?.firstOrNull()
                    ?.let {
                        viewModel?.topAdsMoveGroup(
                            (it as GroupItemUiModel).groupId,
                            productIds
                        )
                    }
            }
        }
    }

    private fun openSuccessDialog(createdGroupId: String) {
        val dialog = DialogUnify(
            requireContext(),
            DialogUnify.VERTICAL_ACTION,
            DialogUnify.WITH_ILLUSTRATION
        )
        dialog.setImageUrl(TopAdsProductRecommendationConstants.CREATE_GROUP_SUCCESS_DIALOG_IMG_URL)
        dialog.setDescription(getString(topadscommonR.string.topads_common_create_group_success_dailog_desc))
        dialog.setTitle(getString(topadscommonR.string.topads_common_product_successfully_advertised))
        dialog.setPrimaryCTAText(getString(topadscommonR.string.topads_common_manage_ads_group))
        dialog.setSecondaryCTAText(getString(topadscommonR.string.topads_common_stay_here))
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

    private fun openInsufficientCreditsDialog() {
        val dialog =
            DialogUnify(requireContext(), DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setDescription(
            getString(topadscommonR.string.topads_common_insufficient_credits_error_desc).replace(
                IDR_CONST,
                "Rp${topadsDeposits}"
            )
        )
        dialog.setTitle(getString(topadscommonR.string.topads_common_ads_created_successfully_but_cant_appear_yet))
        dialog.setPrimaryCTAText(getString(topadscommonR.string.topads_common_add_credit))
        dialog.setSecondaryCTAText(getString(topadscommonR.string.topads_common_later_keyword))
        dialog.setPrimaryCTAClickListener {
            openNewAutoTopUpBottomSheet()
        }
        dialog.setSecondaryCTAClickListener { activity?.finish() }
        dialog.show()
    }

    private fun openNewAutoTopUpBottomSheet() {
        val intent = Intent(activity, TopAdsCreditTopUpActivity::class.java)
        intent.putExtra(TopAdsCreditTopUpActivity.IS_AUTO_TOP_UP_ACTIVE, false)
        intent.putExtra(TopAdsCreditTopUpActivity.IS_AUTO_TOP_UP_SELECTED, false)
        intent.putExtra(TopAdsCreditTopUpActivity.CREDIT_PERFORMANCE, "")
        intent.putExtra(
            TopAdsCreditTopUpActivity.TOP_UP_COUNT,
            TopAdsProductRecommendationConstants.COUNT_ZERO
        )
        intent.putExtra(
            TopAdsCreditTopUpActivity.AUTO_TOP_UP_BONUS,
            TopAdsProductRecommendationConstants.COUNT_ZERO
        )
        startActivityForResult(intent, TopAdsDashboardConstant.REQUEST_CODE_TOP_UP_CREDIT)
    }

    private fun onItemCheckedChangeListener(groupId: String) {
        viewModel?.getGroupList()?.forEach {
            val groupItem = it as? GroupItemUiModel
            groupItem?.isSelected = groupItem?.groupId == groupId
        }
        groupListAdapter.notifyDataSetChanged()
        updateSubmitBtnState()
    }

    private fun updateSubmitBtnState() {
        binding?.btnSubmit?.isEnabled =
            !viewModel?.getGroupList()?.filter { (it as? GroupItemUiModel)?.isSelected ?: false }
                .isNullOrEmpty()
    }

    private fun reloadPage() {
        viewModel?.getTopadsGroupList(searchGroup, DEFAULT_GROUP_TYPE)
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
