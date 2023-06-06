package com.tokopedia.topads.dashboard.recommendation.views.fragments

import android.content.Intent
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
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.smoothSnapToPosition
import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.DEFAULT_SELECTED_INSIGHT_TYPE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.HEADLINE_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PRODUCT_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_DAILY_BUDGET
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_GROUP_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_NEGATIVE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_POSITIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PRODUCT_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_SHOP_VALUE
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
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsAddCreditActivity
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject

class GroupDetailFragment : BaseDaggerFragment(), OnItemSelectChangeListener {

    private var adType: String? = ""
    private var insightList: ArrayList<AdGroupUiModel>? = null
    private var adGroupId: String? = ""
    private var groupDetailsRecyclerView: RecyclerView? = null
    private var groupDetailChipsRv: RecyclerView? = null
    private var saveButton: UnifyButton? = null
    private var groupChipsLayout: View? = null
    private val groupDetailAdapter by lazy {
        GroupDetailAdapter(
            GroupDetailAdapterFactoryImpl(
                onChipClick,
                onInsightItemClick,
                ::onInsightTypeChipClick,
                onAccordianItemClick,
                onInsightAction
            )
        )
    }

    private var onAccordianItemClick: (clickedItem: Int) -> Unit = { clickedItem ->
        viewModel.reOrganiseData(clickedItem)
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
        val adGroupId = arguments?.getString("groupId") ?: ""
        val insightType = arguments?.getInt("insightType") ?: 0
        viewModel.loadInsightTypeChips(adType, insightList ?: arrayListOf(), adGroupName)
        viewModel.selectDefaultChips(insightType, adType)
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
                    if (adGroupId.isNullOrEmpty()) {
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
                item.adGroupID,
                item.insightType
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
                        if (viewModel.checkIfGroupChipsAvailable()) {
                            groupChipsLayout?.show()
                            groupDetailsChipsAdapter?.notifyDataSetChanged()
                            groupDetailChipsRv?.smoothSnapToPosition(chipsList.findPositionOfSelected { it.isSelected })
                        }
                    }
                } else {
                    if (position <= 1) {
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
        saveButton = view?.findViewById(R.id.saveButton)

        saveButton?.setOnClickListener {
            var dialog =
                DialogUnify(
                    requireContext(),
                    DialogUnify.HORIZONTAL_ACTION,
                    DialogUnify.WITH_ILLUSTRATION
                )
            dialog.setImageUrl("https://pakar.co.id/storage/2021/07/tokopedia-logo-7AC053EC2E-seeklogo.com_.png")
            dialog.setDescription(
                "5 kata kunci akan ditambahkan ke grup Cuci Gudang Agustus 2022 - Atasan Wanita."
            )
            dialog.setTitle("Terapkan perubahan ini untuk iklanmu?")
            dialog.setPrimaryCTAText("Ya, Terapkan")
            dialog.setSecondaryCTAText("batal")
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private val onChipClick: (Int) -> Unit = {
        viewModel.reOrganiseData()
    }

    private fun onInsightTypeChipClick(groupList: MutableList<InsightListUiModel>?) {
        if (groupList.isNullOrEmpty()) {
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
                "" // don't send group id in case of choose ad type bottomsheet
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
        viewModel.loadDetailPageOnAction(
            adType,
            groupId,
            DEFAULT_SELECTED_INSIGHT_TYPE,
            groupId.isEmpty(),
            groupName
        )
    }

    val onInsightAction = { input: TopadsManagePromoGroupProductInput, type: Int ->
        if(input.keywordOperation.isNullOrEmpty()){
            saveButton?.visibility = View.GONE
        } else {
            saveButton?.visibility = View.VISIBLE
        }
        when(type){
            TYPE_POSITIVE_KEYWORD -> {
                saveButton?.text = String.format(getString(R.string.topads_insight_positive_keywords_cta_text_format), input.keywordOperation?.size)
            }
            TYPE_KEYWORD_BID -> {
                saveButton?.text = String.format(getString(R.string.topads_insight_existing_keywords_cta_text_format), input.keywordOperation?.size)
            }
            TYPE_GROUP_BID -> {
                saveButton?.text = getString(R.string.topads_insight_biaya_iklan_cta_text)
            }
            TYPE_DAILY_BUDGET -> {
                saveButton?.text = getString(R.string.topads_insight_daily_budget_cta_text)
            }
            TYPE_NEGATIVE_KEYWORD_BID -> {
                saveButton?.text = String.format(getString(R.string.topads_insight_negative_keywords_cta_text_format), input.keywordOperation?.size)
            }
            else -> {}
        }
        viewModel.updateTopadsManagePromoGroupProductInput(input, type)
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
