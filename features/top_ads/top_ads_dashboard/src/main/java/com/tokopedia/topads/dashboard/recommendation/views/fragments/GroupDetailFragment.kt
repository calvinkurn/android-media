package com.tokopedia.topads.dashboard.recommendation.views.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.smoothSnapToPosition
import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.AD_GROUP_ID_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.AD_GROUP_NAME_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.AD_GROUP_TYPE_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.DEFAULT_SELECTED_INSIGHT_TYPE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.HEADLINE_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INSIGHT_TYPE_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INSIGHT_TYPE_LIST_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_ALL
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PRODUCT_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_INSIGHT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PRODUCT_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_SHOP_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.Utils
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AdGroupUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailDataModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.data.model.local.data.ChipsData.chipsList
import com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips.InsightTypeChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.utils.OnItemSelectChangeListener
import com.tokopedia.topads.dashboard.recommendation.viewmodel.GroupDetailViewModel
import com.tokopedia.topads.dashboard.recommendation.viewmodel.ItemListUiModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.GroupDetailAdapter
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.GroupDetailsChipsAdapter
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactoryImpl
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment
import javax.inject.Inject

class GroupDetailFragment : BaseDaggerFragment(), OnItemSelectChangeListener {

    private var adType: String? = ""
    private var insightType: Int = TYPE_INSIGHT
    private var insightList: ArrayList<AdGroupUiModel>? = null
    private var adGroupId: String? = ""
    private var adGroupName: String? = ""
    private var groupDetailsRecyclerView: RecyclerView? = null
    private var groupDetailChipsRv: RecyclerView? = null
    private var groupChipsLayout: View? = null
    private var groupDetailPageShimmer: View? = null
    private var detailPageEmptyState: EmptyStateUnify? = null
    private val groupDetailAdapter by lazy {
        GroupDetailAdapter(
            GroupDetailAdapterFactoryImpl(
                onChipsClick,
                onInsightItemClick,
                ::onInsightTypeChipClick,
                onAccordianItemClick
            )
        )
    }

    private var onAccordianItemClick: (clickedItem: Int) -> Unit = { clickedItem ->
        viewModel.reSyncDetailPageData(adGroupType = utils.convertAdTypeToInt(adType), clickedItem)
    }

    private var groupDetailsChipsAdapter: GroupDetailsChipsAdapter? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var utils: Utils

    private val viewModel: GroupDetailViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory)[GroupDetailViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_group_detail_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrieveInitialData()
        initializeViews()
        setUpRecyclerView()
        setUpChipsRecyclerView()
        observeLiveData()
        settingClicks()
    }

    private fun settingClicks() {
        detailPageEmptyState?.emptyStateCTAID?.setOnClickListener {
            viewModel.selectDefaultChips(insightType)
            if (!adGroupId.isNullOrEmpty() && !adGroupName.isNullOrEmpty()) {
                viewModel.loadDetailPageOnAction(
                    utils.convertAdTypeToInt(this.adType),
                    adGroupId!!,
                    DEFAULT_SELECTED_INSIGHT_TYPE,
                    this.adGroupId!!.isEmpty(),
                    adGroupName!!
                )
            }
            detailPageEmptyState?.hide()
            groupDetailsRecyclerView?.show()
        }
    }

    private fun retrieveInitialData() {
        insightList =
            arguments?.getParcelableArrayList(INSIGHT_TYPE_LIST_KEY) ?: arrayListOf()
        adType = arguments?.getString(AD_GROUP_TYPE_KEY)
        val adGroupName = arguments?.getString(AD_GROUP_NAME_KEY)
        val adGroupId = arguments?.getString(AD_GROUP_ID_KEY) ?: String.EMPTY
        insightType = arguments?.getInt(INSIGHT_TYPE_KEY) ?: Int.ZERO
        viewModel.loadInsightTypeChips(adType, insightList ?: arrayListOf(), adGroupName)
        viewModel.selectDefaultChips(insightType)
        if (adType != null && adGroupId.isNotEmpty()) {
            loadData(
                utils.convertAdTypeToInt(adType),
                adGroupId
            )
        }
    }

    private fun observeLiveData() {
        viewModel.detailPageLiveData.observe(viewLifecycleOwner) { it ->
            when (it) {
                is TopAdsListAllInsightState.Success -> {
                    detailPageEmptyState?.hide()
                    val list = mutableListOf<GroupDetailDataModel>()
                    it.data.map {
                        if (it.value.isAvailable()) list.add(it.value)
                    }
                    groupDetailAdapter.submitList(list)
                    if (adGroupId.isNullOrEmpty()) {
                        // adGroupId is null/Empty in case ad type is changed in which case new groups list is fetched and first group in list is pre-selected.
                        list.firstOrNull().apply {
                            adGroupId = ((this as? InsightTypeChipsUiModel)?.adGroupList?.firstOrNull() as? AdGroupUiModel)?.adGroupID
                        }
                    }
                    Toast.makeText(context, "fejrfberf", Toast.LENGTH_SHORT).show()
                    groupDetailPageShimmer?.hide()
                }
                is TopAdsListAllInsightState.Fail -> {
                    groupDetailPageShimmer?.hide()
                    groupDetailsRecyclerView?.hide()
                    detailPageEmptyState?.show()
                }
                is TopAdsListAllInsightState.Loading -> {
                    groupDetailPageShimmer?.show()
                    detailPageEmptyState?.hide()
                }
            }
        }
    }

    private fun loadData(adGroupType: Int, groupId: String) {
        viewModel.loadDetailPage(adGroupType, groupId)
        viewModel.loadInsightCountForOtherAdType(adGroupType)
    }

    private fun setUpChipsRecyclerView() {
        groupDetailsChipsAdapter = GroupDetailsChipsAdapter(onStickyChipsClick)
        groupDetailChipsRv?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        groupDetailChipsRv?.adapter = groupDetailsChipsAdapter
    }

    private val onStickyChipsClick: (Int) -> Unit = { position ->
        groupDetailChipsRv?.layoutManager?.startSmoothScroll(
            object :
                LinearSmoothScroller(context) {
                override fun getHorizontalSnapPreference(): Int {
                    return SNAP_TO_START
                }

                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                    return TopAdsProductIklanFragment.MILLISECONDS_PER_INCH / displayMetrics.densityDpi
                }
            }.apply { targetPosition = position }
        )
        viewModel.reSyncDetailPageData(
            adGroupType = utils.convertAdTypeToInt(adType),
            clickedItem = TYPE_PRODUCT_VALUE
        )
    }

    private val onInsightItemClick: (list: ArrayList<AdGroupUiModel>, item: AdGroupUiModel) -> Unit =
        { _, item ->
            viewModel.loadDetailPageOnAction(
                utils.convertAdTypeToInt(item.adGroupType),
                item.adGroupID,
                item.insightType
            )
        }

    private fun setUpRecyclerView() {
        groupDetailsRecyclerView?.layoutManager = LinearLayoutManager(context)
        groupDetailsRecyclerView?.adapter = groupDetailAdapter
        handleStickyView()

    }

    private fun handleStickyView() {
        groupDetailsRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position =
                    (recyclerView.layoutManager as LinearLayoutManager?)?.findFirstVisibleItemPosition()
                        ?: return

                if (dy > Int.ZERO) {
                    if (position > Int.ONE) {
                        if (viewModel.checkIfGroupChipsAvailable()) {
                            groupChipsLayout?.show()
                            groupDetailsChipsAdapter?.notifyDataSetChanged()
                            groupDetailChipsRv?.smoothSnapToPosition(chipsList.findPositionOfSelected { it.isSelected })
                        }
                    }
                } else {
                    if (position <= Int.ONE) {
                        groupChipsLayout?.hide()
                    }
                }
            }
        })
    }

    private fun initializeViews() {
        groupDetailsRecyclerView = view?.findViewById(R.id.groupDetailsRecyclerView)
        groupDetailChipsRv = view?.findViewById(R.id.groupDetailChipsRv)
        groupChipsLayout = view?.findViewById(R.id.groupChipsLayout)
        groupDetailPageShimmer = view?.findViewById(R.id.groupDetailPageShimmer)
        detailPageEmptyState = view?.findViewById(R.id.detailPageEmptyState)
    }

    private val onChipsClick: (Int) -> Unit = {
        viewModel.reSyncDetailPageData(
            adGroupType = utils.convertAdTypeToInt(adType),
            clickedItem = TYPE_PRODUCT_VALUE
        )
    }

    private fun onInsightTypeChipClick(groupList: MutableList<InsightListUiModel>?) {
        if (groupList.isNullOrEmpty()) {
            val list = viewModel.getItemListUiModel(
                listOf(
                    getString(R.string.topads_insight_ad_type_product),
                    getString(R.string.topads_insight_ad_type_shop)
                ),
                adType
            )
            ListBottomSheet.show(
                childFragmentManager,
                getString(R.string.topads_insight_ad_type),
                list,
                ListBottomSheet.CHOOSE_AD_TYPE_BOTTOMSHEET,
                this,
                if (PRODUCT_KEY == adType) TYPE_PRODUCT_VALUE else TYPE_SHOP_VALUE,
                "" // don't send group id in case of choose ad type bottomsheet
            )
        } else {
            val list = arrayListOf<ItemListUiModel>()
            groupList.forEach {
                (it as? AdGroupUiModel)?.apply {
                    list.add(
                        ItemListUiModel(
                            adType = utils.convertAdTypeToInt(adType),
                            title = this.adGroupName,
                            groupId = this.adGroupID,
                            isSelected = this.adGroupID == this@GroupDetailFragment.adGroupId
                        )
                    )
                }
            }
            ListBottomSheet.show(
                childFragmentManager,
                getString(R.string.topads_insight_ad_group),
                list,
                ListBottomSheet.CHOOSE_AD_GROUP_BOTTOMSHEET,
                this,
                if (PRODUCT_KEY == adType) TYPE_PRODUCT_VALUE else TYPE_SHOP_VALUE,
                this.adGroupId
            )
        }
    }

    override fun onClickItemListener(adType: Int, groupId: String, groupName: String) {
        // adType changes with choose ad type bottomsheet & vice versa for choose group bottomsheet
        this.adType = if (adType == TYPE_PRODUCT_VALUE) PRODUCT_KEY else HEADLINE_KEY
        this.adGroupId = groupId
        this.insightType = INSIGHT_TYPE_ALL
        this.adGroupName = groupName
        viewModel.selectDefaultChips(insightType)
        viewModel.loadDetailPageOnAction(
            adType,
            groupId,
            DEFAULT_SELECTED_INSIGHT_TYPE,
            groupId.isEmpty(),
            groupName
        )
    }

    val onClickInsightItems: (topAdsManagePromoGroupProductInput: TopadsManagePromoGroupProductInput) -> Unit =
        {

            // do operation when insight is clicked
        }

    private fun applyInsights(topAdsManagePromoGroupProductInput: TopadsManagePromoGroupProductInput) {
        viewModel.applyInsight2(topAdsManagePromoGroupProductInput)
    }

    companion object {
        fun createInstance(bundleExtra: Bundle?): GroupDetailFragment {
            val fragment = GroupDetailFragment()
            fragment.arguments = bundleExtra
            return fragment
        }
    }

    override fun getScreenName(): String = javaClass.name
    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }
}

fun <T> Iterable<T>.findPositionOfSelected(predicate: (T) -> Boolean): Int {
    for ((index, element) in this.withIndex()) if (predicate(element)) return index
    return Int.ZERO
}
