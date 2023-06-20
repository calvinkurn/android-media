package com.tokopedia.topads.dashboard.recommendation.views.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.kotlin.extensions.view.*
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
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_DAILY_BUDGET
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_GROUP_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_NEGATIVE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_POSITIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_INSIGHT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PRODUCT_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_SHOP_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.Utils
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupInsightsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AdGroupUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailDataModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.data.model.local.data.ChipsData.chipsList
import com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips.InsightTypeChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.common.OnItemSelectChangeListener
import com.tokopedia.topads.dashboard.recommendation.viewmodel.GroupDetailViewModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ItemListUiModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.GroupDetailAdapter
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.GroupDetailsChipsAdapter
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactoryImpl
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Fail
import javax.inject.Inject

class GroupDetailFragment : BaseDaggerFragment(), OnItemSelectChangeListener {

    private var adType: String? = String.EMPTY
    private var insightType: Int = TYPE_INSIGHT
    private var insightList: ArrayList<AdGroupUiModel>? = null
    private var adGroupId: String? = String.EMPTY
    private var adGroupName: String? = String.EMPTY
    private var groupDetailsRecyclerView: RecyclerView? = null
    private var groupDetailChipsRv: RecyclerView? = null
    private var saveButton: UnifyButton? = null
    private var groupChipsLayout: View? = null
    private var groupDetailPageShimmer: View? = null
    private var detailPageEmptyState: EmptyStateUnify? = null
    private var confirmationDailog: DialogUnify? = null
    private val groupDetailAdapter by lazy {
        GroupDetailAdapter(
            GroupDetailAdapterFactoryImpl(
                onChipsClick,
                onInsightItemClick,
                ::onInsightTypeChipClick,
                onAccordianItemClick,
                onInsightAction
            )
        )
    }

    private var onAccordianItemClick: (element: GroupInsightsUiModel) -> Unit = { element ->
        viewModel.reSyncDetailPageData(adGroupType = utils.convertAdTypeToInt(adType), element.type)
        saveButton?.showWithCondition(element.isExpanded)
        saveButton?.tag = element.type
        saveButton?.isEnabled = checkButtonStatus(viewModel.getInputDataFromMapper(element.type),false)
        updateButtonTitle(viewModel.getInputDataFromMapper(element.type))
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
        attachViewClickListeners()
        settingClicks()
    }

    private fun settingClicks() {
        detailPageEmptyState?.emptyStateCTAID?.setOnClickListener {
            viewModel.selectDefaultChips(insightType)
            if (!adGroupId.isNullOrEmpty() && !adGroupName.isNullOrEmpty()) {
                loadDetailPageOnAction(
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
        adGroupName = arguments?.getString(AD_GROUP_NAME_KEY)
        adGroupId = arguments?.getString(AD_GROUP_ID_KEY) ?: String.EMPTY
        insightType = arguments?.getInt(INSIGHT_TYPE_KEY) ?: Int.ZERO
        viewModel.loadInsightTypeChips(adType, insightList ?: arrayListOf(), adGroupName)
        viewModel.selectDefaultChips(insightType)
        if (adType != null && !adGroupId.isNullOrEmpty()) {
            loadData(
                utils.convertAdTypeToInt(adType),
                adGroupId
            )
        }
    }

    private fun updateButtonTitle(input: TopadsManagePromoGroupProductInput?){
        input?.let {
            saveButton?.text = when (saveButton?.tag) {
                TYPE_POSITIVE_KEYWORD -> {
                    String.format(
                        getString(R.string.topads_insight_positive_keywords_cta_text_format),
                        input.keywordOperation?.size
                    )
                }
                TYPE_KEYWORD_BID -> {
                    String.format(
                        getString(R.string.topads_insight_existing_keywords_cta_text_format),
                        input.keywordOperation?.size
                    )
                }
                TYPE_GROUP_BID -> {
                    getString(R.string.topads_insight_biaya_iklan_cta_text)
                }
                TYPE_DAILY_BUDGET -> {
                    getString(R.string.topads_insight_daily_budget_cta_text)
                }
                TYPE_NEGATIVE_KEYWORD_BID -> {
                    String.format(
                        getString(R.string.topads_insight_negative_keywords_cta_text_format),
                        input.keywordOperation?.size
                    )
                }
                else -> String.EMPTY
            }
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
                            adGroupName = ((this as? InsightTypeChipsUiModel)?.adGroupList?.firstOrNull() as? AdGroupUiModel)?.adGroupName
                        }
                    }
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

        viewModel.editInsightLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if(it.data.topadsManageGroupAds.groupResponse.errors.isNullOrEmpty() && it.data.topadsManageGroupAds.keywordResponse.errors.isNullOrEmpty())
                        successfulInsightSubmission()
                    else
                        showFailedInsightApplyDialog()
                }
                is Fail -> {
                    showFailedInsightApplyDialog()
                }
            }
        }

        viewModel.editHeadlineInsightLiveData.observe(viewLifecycleOwner){
            when (it) {
                is Success -> {
                    if(it.data.topadsManageHeadlineAd.errors.isNullOrEmpty())
                        successfulInsightSubmission()
                    else
                        showFailedInsightApplyDialog()
                }
                is Fail -> {
                    showFailedInsightApplyDialog()
                }
            }
        }
    }

    private fun successfulInsightSubmission(){
        confirmationDailog?.dismiss()
        val successMsg = when(saveButton?.tag) {
            TYPE_POSITIVE_KEYWORD -> getString(R.string.topads_insight_kata_kunci_submit_success_toast_msg)
            TYPE_KEYWORD_BID -> getString(R.string.topads_insight_biaya_kata_kunci_submit_success_toast_msg)
            TYPE_GROUP_BID -> getString(R.string.topads_insight_biaya_iklan_submit_success_toast_msg)
            TYPE_DAILY_BUDGET -> getString(R.string.topads_insight_daily_budget_submit_success_toast_msg)
            TYPE_NEGATIVE_KEYWORD_BID -> getString(R.string.topads_insight_negative_kata_kunci_submit_success_toast_msg)
            else -> String.EMPTY
        }
        showSuccessToast(successMsg)
        loadData(
            utils.convertAdTypeToInt(adType),
            adGroupId
        )
    }

    private fun showFailedInsightApplyDialog() {
        if (confirmationDailog?.isShowing != true) {
            confirmationDailog =
                DialogUnify(
                    requireContext(),
                    DialogUnify.HORIZONTAL_ACTION,
                    DialogUnify.WITH_ILLUSTRATION
                )
            confirmationDailog?.show()
        }

        val description =
            getString(R.string.topads_dashboard_submit_insight_fail_dialog_description)
        val input = viewModel.getInputDataFromMapper(saveButton?.tag as? Int)
        val title = when (saveButton?.tag) {
            TYPE_POSITIVE_KEYWORD -> String.format(
                getString(R.string.topads_dashboard_submit_kata_kunci_insight_fail_title),
                input?.keywordOperation?.size ?: 0
            )
            TYPE_KEYWORD_BID -> String.format(
                getString(R.string.topads_dashboard_submit_biaya_kata_kunci_insight_fail_title),
                input?.keywordOperation?.size ?: 0
            )
            TYPE_GROUP_BID -> getString(R.string.topads_dashboard_submit_biaya_iklan_insight_fail_title)
            TYPE_DAILY_BUDGET -> getString(R.string.topads_dashboard_submit_daily_budget_insight_fail_title)
            TYPE_NEGATIVE_KEYWORD_BID -> String.format(
                getString(R.string.topads_dashboard_submit_negative_kata_kunci_insight_fail_title),
                input?.keywordOperation?.size ?: 0
            )
            else -> String.EMPTY
        }

        confirmationDailog?.let {
            it.setTitle(title)
            it.setImageUrl(unsuccessfulSubmitInsightDialogImageUrl)
            it.setDescription(description)

            if (it.dialogPrimaryCTA.isLoading)
                it.dialogPrimaryCTA.isLoading = false

            it.setPrimaryCTAText(getString(R.string.title_try_again))
            it.setSecondaryCTAText(getString(R.string.label_close))
        }
    }

    private fun showSuccessToast(text: String){
        view?.let {
            Toaster.build(
                it,
                text,
                Snackbar.LENGTH_SHORT,
                Toaster.TYPE_NORMAL,
                getString(com.tokopedia.topads.common.R.string.topads_common_text_ok)
            ).show()
        }
    }

    private fun loadData(adGroupType: Int, groupId: String?) {
        groupId?.let { viewModel.loadDetailPage(adGroupType, it) }
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
            loadDetailPageOnAction(
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
        saveButton = view?.findViewById(R.id.saveButton)
    }

    private fun attachViewClickListeners(){
        saveButton?.setOnClickListener {
            val input = viewModel.getInputDataFromMapper(saveButton?.tag as? Int)
            showConfirmationDialog(input)
        }
    }

    private fun showConfirmationDialog(input: TopadsManagePromoGroupProductInput?) {
        confirmationDailog?.dismiss() //if already showing
        confirmationDailog =
            DialogUnify(
                requireContext(),
                DialogUnify.HORIZONTAL_ACTION,
                DialogUnify.WITH_ILLUSTRATION
            )
        val description : String = when(saveButton?.tag){
            TYPE_POSITIVE_KEYWORD -> String.format(getString(R.string.topads_insight_confirmation_dialog_desc_kata_kunci), input?.keywordOperation?.size ?: 0, adGroupName)
            TYPE_KEYWORD_BID -> String.format(getString(R.string.topads_insight_confirmation_dialog_desc_biaya_kata_kunci), adGroupName)
            TYPE_GROUP_BID -> String.format(getString(R.string.topads_insight_confirmation_dialog_desc_biaya_iklan), adGroupName)
            TYPE_DAILY_BUDGET -> String.format(getString(R.string.topads_insight_confirmation_dialog_desc_daily_budget), adGroupName)
            TYPE_NEGATIVE_KEYWORD_BID -> String.format(getString(R.string.topads_insight_confirmation_dialog_desc_negative_kata_kunci), input?.keywordOperation?.size ?: 0, adGroupName)
            else -> String.EMPTY
        }
        confirmationDailog?.setDescription(description)
        confirmationDailog?.setImageUrl(successfulSubmitInsightDialogImageUrl)
        confirmationDailog?.setTitle(getString(R.string.topads_insight_confirmation_dialog_title))
        confirmationDailog?.setPrimaryCTAText(getString(R.string.topads_yes_apply))
        confirmationDailog?.setSecondaryCTAText(getString(R.string.top_ads_batal))
        confirmationDailog?.setPrimaryCTAClickListener {
            confirmationDailog?.dialogPrimaryCTA?.isLoading = true
            viewModel.submitInsights(input, adGroupId, adType, saveButton?.tag as? Int, adGroupName)
        }
        confirmationDailog?.setSecondaryCTAClickListener {
            confirmationDailog?.dismiss()
        }
        confirmationDailog?.show()
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
                String.EMPTY // don't send group id in case of choose ad type bottomsheet
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
        loadDetailPageOnAction(
            adType,
            groupId,
            DEFAULT_SELECTED_INSIGHT_TYPE,
            groupId.isEmpty(),
            groupName
        )
    }

    val onInsightAction = {hasErrors: Boolean ->
        saveButton?.isEnabled = checkButtonStatus(viewModel.getInputDataFromMapper(saveButton?.tag as? Int), hasErrors)
        updateButtonTitle(viewModel.getInputDataFromMapper(saveButton?.tag as? Int))
    }

    private fun loadDetailPageOnAction(adType: Int, adgroupID: String, insightType: Int, isSwitchAdType: Boolean = false, groupName: String = String.EMPTY){
        viewModel.loadDetailPageOnAction(
            adType,
            adgroupID,
            insightType,
            isSwitchAdType,
            groupName
        )
        saveButton?.gone()
    }

    private fun checkButtonStatus(input: TopadsManagePromoGroupProductInput?, hasErrors: Boolean): Boolean{
        if(hasErrors)
            return false
        else {
            input?.let {
                return when (saveButton?.tag) {
                    TYPE_POSITIVE_KEYWORD, TYPE_KEYWORD_BID, TYPE_NEGATIVE_KEYWORD_BID -> {
                        !input.keywordOperation.isNullOrEmpty() && input.keywordOperation?.firstOrNull() != null
                    }
                    TYPE_GROUP_BID, TYPE_DAILY_BUDGET -> {
                        input.groupInput != null
                    }
                    else -> true
                }
            }
            return true
        }
    }

    companion object {
        fun createInstance(bundleExtra: Bundle?): GroupDetailFragment {
            val fragment = GroupDetailFragment()
            fragment.arguments = bundleExtra
            return fragment
        }

        private const val successfulSubmitInsightDialogImageUrl = "https://images.tokopedia.net/img/android/topads/insight_center_page/illustration.png"
        private const val unsuccessfulSubmitInsightDialogImageUrl = "https://images.tokopedia.net/img/android/topads/insight_center_page/rekomendasi_gagal_new_2.png"
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
