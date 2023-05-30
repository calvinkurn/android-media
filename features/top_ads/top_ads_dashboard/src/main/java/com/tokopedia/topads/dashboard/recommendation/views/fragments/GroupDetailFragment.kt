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
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.smoothSnapToPosition
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.HEADLINE_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PRODUCT_KEY
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AdGroupUiModel
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PRODUCT_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_SHOP_VALUE
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

    private var adType : String? = ""
    private var insightList : ArrayList<AdGroupUiModel>? = null
    private var adGroupId : String? = ""
    private var groupDetailsRecyclerView: RecyclerView? = null
    private var groupDetailChipsRv: RecyclerView? = null
    private var groupChipsLayout: View? = null
    private val groupDetailAdapter by lazy {
        GroupDetailAdapter(
            GroupDetailAdapterFactoryImpl(
                onChipClick,
                onInsightItemClick,
                ::onInsightTypeChipClick
            )
        )
    }
    private var groupDetailsChipsAdapter: GroupDetailsChipsAdapter? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

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
    }

    private fun retrieveInitialData() {
        insightList = arguments?.getParcelableArrayList<AdGroupUiModel>("insightTypeList") ?: arrayListOf()
        adType = arguments?.getString("adType")
        val adGroupName = arguments?.getString("adGroupName")
        adGroupId = arguments?.getString("groupId")
        viewModel.loadInsightTypeChips(adType, insightList ?: arrayListOf(), adGroupName)
        if (adType != null && adGroupId != null) {
            loadData(if (PRODUCT_KEY == adType) TYPE_PRODUCT_VALUE else TYPE_SHOP_VALUE, adGroupId ?: "")
        }
    }

    private fun observeLiveData() {
        viewModel.detailPageLiveData.observe(viewLifecycleOwner) { it ->
            when (it) {
                is TopAdsListAllInsightState.Success -> {
                    val list = mutableListOf<GroupDetailDataModel>()
                    it.data.map {
                        if (it.value.isAvailable()) list.add(it.value)
                    }
                    groupDetailAdapter.submitList(list)
                    if(adGroupId.isNullOrEmpty()) {
                        // adGroupId is null/Empty in case ad type is changed in which case new groups list is fetched and first group in list is pre-selected.
                        list.firstOrNull().apply {
                            adGroupId = ((this as? InsightTypeChipsUiModel)?.adGroupList?.firstOrNull() as? AdGroupUiModel)?.adGroupID
                        }
                    }
                    Toast.makeText(context, "fejrfberf", Toast.LENGTH_SHORT).show()
                }
                is TopAdsListAllInsightState.Fail -> {
                }
                is TopAdsListAllInsightState.Loading -> {
                }
            }
        }
    }

    private fun loadData(adGroupType: Int, groupId: String) {
        viewModel.loadDetailPage(adGroupType, groupId)
    }

    private fun setUpChipsRecyclerView() {
        groupDetailsChipsAdapter = GroupDetailsChipsAdapter(onChipsClick)
        groupDetailChipsRv?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        groupDetailChipsRv?.adapter = groupDetailsChipsAdapter
    }

    private val onChipsClick: (Int) -> Unit = { position ->
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
        groupDetailAdapter.updateItem()
    }

    private val onInsightItemClick: (list: ArrayList<AdGroupUiModel>, item: AdGroupUiModel) -> Unit =
        { _, item ->
            viewModel.loadDetailPageOnAction(
                if (item.adGroupType == PRODUCT_KEY) TYPE_PRODUCT_VALUE else TYPE_SHOP_VALUE,
                item.adGroupID
            )
        }

    private fun setUpRecyclerView() {
        groupDetailsRecyclerView?.layoutManager = LinearLayoutManager(context)
        groupDetailsRecyclerView?.adapter = groupDetailAdapter
        groupDetailsRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position =
                    (recyclerView.layoutManager as LinearLayoutManager?)?.findFirstVisibleItemPosition()
                        ?: return

                if (dy > 0) {
                    if (position > 1) {
                        groupChipsLayout?.visibility = View.VISIBLE
                        groupDetailsChipsAdapter?.notifyDataSetChanged()
                        groupDetailChipsRv?.smoothSnapToPosition(chipsList.findPositionOfSelected { it.isSelected })
                    }
                } else {
                    if (position <= 1) {
                        groupChipsLayout?.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun initializeViews() {
        groupDetailsRecyclerView = view?.findViewById(R.id.groupDetailsRecyclerView)
        groupDetailChipsRv = view?.findViewById(R.id.groupDetailChipsRv)
        groupChipsLayout = view?.findViewById(R.id.groupChipsLayout)
    }

    private val onChipClick: (Int) -> Unit = {
        viewModel.reOrganiseData()
    }

    private fun onInsightTypeChipClick(groupList: MutableList<InsightListUiModel>?) {
        if(groupList.isNullOrEmpty()) {
            val list = arrayListOf(
                ItemListUiModel(
                    adType = TYPE_PRODUCT_VALUE,
                    title = getString(R.string.topads_insight_ad_type_product),
                    isSelected = (PRODUCT_KEY == adType)
                ),
                ItemListUiModel(
                    adType = TYPE_SHOP_VALUE,
                    title = getString(R.string.topads_insight_ad_type_shop),
                    isSelected = (adType != PRODUCT_KEY)
                )
            )
            ListBottomSheet.show(
                childFragmentManager,
                getString(R.string.topads_insight_ad_type),
                list,
                ListBottomSheet.CHOOSE_AD_TYPE_BOTTOMSHEET,
                this,
                if (PRODUCT_KEY == adType) TYPE_PRODUCT_VALUE else TYPE_SHOP_VALUE,
                "" //don't send group id in case of choose ad type bottomsheet
            )
        } else {
            val list = arrayListOf<ItemListUiModel>()
            groupList.forEach {
                (it as? AdGroupUiModel)?.apply {
                    list.add(
                        ItemListUiModel(
                            adType = if (PRODUCT_KEY == adType) TYPE_PRODUCT_VALUE else TYPE_SHOP_VALUE,
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
        viewModel.loadDetailPageOnAction(adType, groupId, groupId.isEmpty(), groupName)
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
