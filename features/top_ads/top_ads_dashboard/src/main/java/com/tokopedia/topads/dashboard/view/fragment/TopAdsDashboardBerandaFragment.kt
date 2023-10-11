package com.tokopedia.topads.dashboard.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topads.common.data.model.ticker.TickerInfo
import com.tokopedia.topads.common.sheet.TopAdsToolTipBottomSheet
import com.tokopedia.topads.credit.history.view.activity.TopAdsCreditHistoryActivity
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_5
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATE_PICKER_DEFAULT_INDEX
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.REQUEST_CODE_ADD_CREDIT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.REQUEST_CODE_TOP_UP_CREDIT
import com.tokopedia.topads.dashboard.data.model.beranda.Chip
import com.tokopedia.topads.dashboard.data.model.beranda.RecommendationStatistics
import com.tokopedia.topads.dashboard.data.model.beranda.SummaryBeranda
import com.tokopedia.topads.dashboard.data.utils.TopAdsDashboardBerandaUtils
import com.tokopedia.topads.dashboard.data.utils.TopAdsDashboardBerandaUtils.getSummaryAdTypes
import com.tokopedia.topads.dashboard.data.utils.TopAdsDashboardBerandaUtils.mapToSummary
import com.tokopedia.topads.dashboard.data.utils.TopAdsDashboardBerandaUtils.showDialogWithCoachMark
import com.tokopedia.topads.dashboard.data.utils.Utils.asString
import com.tokopedia.topads.dashboard.data.utils.Utils.openWebView
import com.tokopedia.topads.dashboard.databinding.FragmentTopadsDashboardBerandaBaseBinding
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.HEADLINE_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INSIGHT_COUNT_PLACE_HOLDER
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_ALL
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.MANAGE_RECOMMENDATION_URL
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PRODUCT_KEY
import com.tokopedia.topads.dashboard.recommendation.common.decoration.RecommendationInsightItemDecoration
import com.tokopedia.topads.dashboard.recommendation.data.mapper.InsightDataMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsTotalAdGroupsWithInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AdGroupUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.EmptyStateUiListModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.views.activities.GroupDetailActivity
import com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation.InsightListAdapter
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity.Companion.INSIGHT_PAGE
import com.tokopedia.topads.dashboard.view.adapter.beranda.LatestReadingTopAdsDashboardRvAdapter
import com.tokopedia.topads.dashboard.view.adapter.beranda.TopAdsBerandaSummaryRvAdapter
import com.tokopedia.topads.dashboard.view.adapter.beranda.TopadsImageRvAdapter
import com.tokopedia.topads.dashboard.view.fragment.education.READ_MORE_URL
import com.tokopedia.topads.dashboard.view.sheet.RecommendationInfoBottomSheet
import com.tokopedia.topads.dashboard.view.sheet.SummaryAdTypesBottomSheet
import com.tokopedia.topads.dashboard.view.sheet.SummaryInformationBottomSheet
import com.tokopedia.topads.dashboard.viewmodel.TopAdsDashboardViewModel
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsAddCreditActivity
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsCreditTopUpActivity
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker.sendClickBalanceEvent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

import javax.inject.Inject

/**
 * Created by Ankit
 */
open class TopAdsDashboardBerandaFragment : BaseDaggerFragment() {

    private lateinit var binding: FragmentTopadsDashboardBerandaBaseBinding

    private val graphLayoutFragment = TopAdsMultiLineGraphFragment()

    private val summaryAdTypeList by lazy(LazyThreadSafetyMode.NONE) {
        context?.resources?.getSummaryAdTypes() ?: listOf()
    }
    private var selectedAdType = Chip()
    private val recommendationInfoBottomSheet by lazy(LazyThreadSafetyMode.NONE) { RecommendationInfoBottomSheet() }
    private val summaryAdTypesBottomSheet by lazy(LazyThreadSafetyMode.NONE) {
        SummaryAdTypesBottomSheet(summaryAdTypeList, ::adTypeChanged)
    }
    private val summaryInformationBottomSheet by lazy(LazyThreadSafetyMode.NONE) { SummaryInformationBottomSheet() }
    private val produkBerpotensiAdapter = TopadsImageRvAdapter()
    private val summaryRvAdapter = TopAdsBerandaSummaryRvAdapter()
    private val latestReadingRvAdapter by lazy(LazyThreadSafetyMode.NONE) {
        LatestReadingTopAdsDashboardRvAdapter { context?.openWebView(it) }
    }

    private var isAutoTopUpActive: Boolean = false
    private var isAutoTopUpSelected: Boolean = false
    private var creditPerformance: String = ""
    private var topUpUCount: Int = 0
    private var autoTopUpBonus: Double = 0.0
    private var showAutoTopUpOldFlow = false

    /*
        Insight Widget
    */
    private val insightListAdapter by lazy { InsightListAdapter(onInsightItemClick) }

    private val onInsightItemClick: (list: ArrayList<AdGroupUiModel>, item: AdGroupUiModel) -> Unit =
        { list, item ->
            moveToInsightDetailPage(list, item)
        }

    private var needToHitInsight = true
    /*
        End
    */

    companion object {
        private const val REQUEST_CODE_SET_AUTO_TOPUP = 6

        fun createInstance(): TopAdsDashboardBerandaFragment {
            return TopAdsDashboardBerandaFragment()
        }

        const val TYPE_INFO = "info"
        const val TYPE_WARNING = "warning"
        const val TYPE_ERROR = "error"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @JvmField @Inject
    var mapper: InsightDataMapper? = null

    private val topAdsDashboardViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TopAdsDashboardViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentTopadsDashboardBerandaBaseBinding.inflate(layoutInflater, container, false)
        initializeView()
        return binding.root
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun getScreenName(): String {
        return TopAdsDashboardBerandaFragment::class.java.name
    }

    private fun initializeView() {
        context?.resources?.let { res ->
            with(binding.layoutRecommendasi.layoutProdukBerpostensi) {
                this.txtTitle.text = res.getString(R.string.topads_dashboard_produk_berpotensi)
                this.layoutRoundedView.txtTitle.text =
                    res.getString(R.string.topads_dashboard_potensi_tampil)
                context?.let {
                    this.layoutRoundedView.imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            it,
                            com.tokopedia.unifycomponents.R.drawable.iconunify_product_budget
                        )
                    )
                }
                this.button.text = res.getString(R.string.topads_dashboard_atur_iklannya)
                rvVertical.hide()
            }
        }
    }

    private fun initializeListener() {
        binding.tambahKreditLayout.root.setOnClickListener {
            sendClickBalanceEvent()
            goToCreditHistory(false)
        }
        binding.tambahKreditLayout.addCredit.setOnClickListener {
            TopadsTopupTracker.sendClickTambahKreditEvent()
            if (showAutoTopUpOldFlow) {
                openOldAutoTopUpBottomSheet()
            } else {
                openNewAutoTopUpBottomSheet()
            }
        }
        binding.layoutRingkasan.ivSummaryDropDown.setOnClickListener {
            summaryAdTypesBottomSheet.show(childFragmentManager, "")
        }
        binding.layoutRingkasan.ivSummaryInformation.setOnClickListener {
            showInformationBottomSheet()
        }
        binding.tambahKreditLayout.btnRefreshCredits.setOnClickListener {
            topAdsDashboardViewModel.fetchShopDeposit()
        }
        binding.layoutLatestReading.btnReadMore.setOnClickListener {
            context?.openWebView(READ_MORE_URL)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData()
        }
        summaryRvAdapter.infoClicked = { showInformationBottomSheet() }
        summaryRvAdapter.itemClicked = ::onSummaryItemClicked

        binding.layoutRecommendasi.apply {
            ivRecommendasiInfo.setOnClickListener {
                recommendationInfoBottomSheet.show(childFragmentManager, "")
            }
            layoutProdukBerpostensi.button.setOnClickListener {
                RouteManager.route(
                    activity,
                    MANAGE_RECOMMENDATION_URL
                )
            }
        }
    }

    private fun openNewAutoTopUpBottomSheet() {
        val intent = Intent(activity, TopAdsCreditTopUpActivity::class.java)
        intent.putExtra(TopAdsCreditTopUpActivity.IS_AUTO_TOP_UP_ACTIVE, isAutoTopUpActive)
        intent.putExtra(TopAdsCreditTopUpActivity.IS_AUTO_TOP_UP_SELECTED, isAutoTopUpSelected)
        intent.putExtra(TopAdsCreditTopUpActivity.CREDIT_PERFORMANCE, creditPerformance)
        intent.putExtra(TopAdsCreditTopUpActivity.TOP_UP_COUNT, topUpUCount)
        intent.putExtra(TopAdsCreditTopUpActivity.AUTO_TOP_UP_BONUS, autoTopUpBonus)
        startActivityForResult(intent, REQUEST_CODE_TOP_UP_CREDIT)
    }

    private fun openOldAutoTopUpBottomSheet() {
        val intent = Intent(activity, TopAdsAddCreditActivity::class.java)
        intent.putExtra(TopAdsAddCreditActivity.SHOW_FULL_SCREEN_BOTTOM_SHEET, true)
        startActivityForResult(intent, REQUEST_CODE_ADD_CREDIT)
    }

    private fun setUpRecyclerView() {
        binding.layoutRingkasan.rvSummary.adapter = summaryRvAdapter
        binding.layoutLatestReading.rvLatestReading.adapter = latestReadingRvAdapter
        binding.layoutRecommendasi.layoutProdukBerpostensi.recyclerView.adapter =
            produkBerpotensiAdapter
        setInsightRecyclerView()
        topAdsDashboardViewModel.getLatestReadings()
    }

    private fun setInsightRecyclerView() {
        val insightRecyclerView = binding.layoutInsight.insightRecyclerview
        val itemDecoration = RecommendationInsightItemDecoration(
            context,
            LinearLayoutManager.VERTICAL
        )
        insightRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        insightRecyclerView.addItemDecoration(itemDecoration)
        insightRecyclerView.adapter = insightListAdapter
    }

    private fun showInformationBottomSheet() {
        summaryInformationBottomSheet.show(childFragmentManager, "")
    }

    private fun onSummaryItemClicked(selectedItems: Set<SummaryBeranda>) {
        if (selectedItems.isEmpty()) {
            binding.layoutRingkasan.graphLayoutBeranda.hide()
            return
        }
        if (!binding.layoutRingkasan.graphLayoutBeranda.isVisible) {
            binding.layoutRingkasan.graphLayoutBeranda.show()
        }
        val items = selectedItems.map {
            TopAdsMultiLineGraphFragment.MultiLineGraph(it.id, it.selectedColor)
        }
        graphLayoutFragment.showLineGraph(items)
    }

    private fun initializeGraph() {
        if (parentFragment?.isStateSaved != true) {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.graph_layout_beranda, graphLayoutFragment, "")
                .commit()
        }
    }

    private fun showFirstTimeDialog() {
        (activity as? TopAdsDashboardActivity)?.let {
            requireActivity().showDialogWithCoachMark(
                binding,
                it.ivEducationTopAdsActionBar
            )
        }
    }

    private fun hideShimmer() {
        if (!binding.shimmerView.root.isVisible) return
        binding.swipeRefreshLayout.isRefreshing = false
        binding.shimmerView.root.hide()
        (activity as? TopAdsDashboardActivity)?.toggleMultiActionButton(true)
        binding.swipeRefreshLayout.show()
        showFirstTimeDialog()
    }

    private fun showShimmer() {
        binding.shimmerView.root.show()
        (activity as? TopAdsDashboardActivity)?.toggleMultiActionButton(false)
        binding.swipeRefreshLayout.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (summaryAdTypeList.isNotEmpty()) {
            selectedAdType = summaryAdTypeList[0]
        }

        setUpRecyclerView()
        observeLiveData()
        initializeListener()
        initializeGraph()
        loadData()
        setInsightWidgetBehaviour()
    }

    private fun observeLiveData() {
        topAdsDashboardViewModel.shopDepositLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    binding.tambahKreditLayout.creditAmount.text = it.data.amountFmt
                }

                is Fail -> {}
            }
        }

        topAdsDashboardViewModel.summaryStatisticsLiveData.observe(viewLifecycleOwner) {
            hideShimmer()
            when (it) {
                is Success -> {
                    graphLayoutFragment.setValue(it.data.cells)
                    context?.let { ctx ->
                        summaryRvAdapter.addItems(it.data.summary.mapToSummary(ctx))
                        binding.layoutRingkasan.txtLastUpdated.text = String.format(
                            ctx.resources.getString(R.string.topads_dashboard_last_update_text),
                            it.data.summary.lastUpdate
                        )
                    }
                }

                is Fail -> {}
            }
        }

        topAdsDashboardViewModel.recommendationStatsLiveData.observe(viewLifecycleOwner) {
            hideShimmer()
            when (it) {
                is Success -> {
                    setRecommendationProdukBerpostensi(it.data.productRecommendationStats)
                }

                is Fail -> {}
            }
        }

        topAdsDashboardViewModel.latestReadingLiveData.observe(viewLifecycleOwner) { data ->
            when (data) {
                is Success -> latestReadingRvAdapter.addItems(data.data)
                else -> {
                    binding.layoutLatestReading.root.hide()
                }
            }
        }

        topAdsDashboardViewModel.tickerLiveData.observe(viewLifecycleOwner) {
            if (it.status.errorCode == 0) {
                showTickerTopads(it.data.tickerInfo)
            } else {
                binding.tickerTopAds.hide()
            }
        }

        observeInsight()
    }

    private fun observeInsight() {
        topAdsDashboardViewModel.productInsights.observe(viewLifecycleOwner) {
            when (it) {
                is TopAdsListAllInsightState.Success -> {
                    onSuccessFetchProductInsight(it)
                }

                is TopAdsListAllInsightState.Fail -> {
                }
                else -> {}
            }
        }

        topAdsDashboardViewModel.adGroupWithInsight.observe(viewLifecycleOwner) {
            when (it) {
                is TopAdsListAllInsightState.Success -> {
                    binding.layoutInsight.topAdsInsightCenterTopWidget.topLevelWidgetShimmer.shimmerLayoutTopLevelRecommendation.hide()
                    renderTopLevelWidget(it.data)
                }

                is TopAdsListAllInsightState.Loading -> {
                }

                is TopAdsListAllInsightState.Fail -> {
                }
            }
        }
    }

    private fun renderTopLevelWidget(data: TopAdsTotalAdGroupsWithInsightResponse) {
        if (context == null) return
        val count =
            data.topAdsGetTotalAdGroupsWithInsightByShopID.totalAdGroupsWithInsight.totalAdGroupsWithInsight
        if (count == Int.ZERO) {
            binding.layoutInsight.topAdsInsightCenterTopWidget.insightWidgetTitle.text = context?.getString(R.string.topads_insight_max_out_title)
            binding.layoutInsight.topAdsInsightCenterTopWidget.insightWidgetIcon.loadImage(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.perfomace_widget_optimized_icon
                )
            )
        } else if (count <= INSIGHT_COUNT_PLACE_HOLDER) {
            binding.layoutInsight.topAdsInsightCenterTopWidget.insightWidgetTitle.text = String.format(
                context?.getString(R.string.topads_insight_title_improve_ads_performance) ?: "",
                "$count"
            )
            binding.layoutInsight.topAdsInsightCenterTopWidget.insightWidgetIcon.loadImage(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.performance_widget_default_icon
                )
            )
        } else {
            binding.layoutInsight.topAdsInsightCenterTopWidget.insightWidgetTitle.text = String.format(
                context?.getString(R.string.topads_insight_title_improve_ads_performance) ?: "",
                "$INSIGHT_COUNT_PLACE_HOLDER+"
            )
            binding.layoutInsight.topAdsInsightCenterTopWidget.insightWidgetIcon?.loadImage(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.performance_widget_default_icon
                )
            )
        }
    }

    private fun onSuccessFetchProductInsight(it: TopAdsListAllInsightState.Success<MutableList<InsightListUiModel>>) {
        val temp = arrayListOf(EmptyStateUiListModel("0", topAdsDashboardViewModel.emptyStateData))
        if (it.data.size.isZero()) {
            insightListAdapter.submitList(temp as? List<InsightListUiModel>)
            binding.layoutInsight.insightWidgetSeeMore.hide()
        } else {
            insightListAdapter.submitList(it.data.take(CONST_5))
            binding.layoutInsight.insightWidgetSeeMore.show()
        }
        binding.layoutInsight.topAdsInsightCenterShimmerBottomPage.shimmerLayoutBottomLevelRecommendationAtHome.hide()
    }

    private fun showTickerTopads(tickerInfo: TickerInfo) {
        binding.tickerTopAds.show()
        binding.tickerTopAds.setHtmlDescription(tickerInfo.tickerMessage)
        binding.tickerTopAds.tickerType = when (tickerInfo.tickerType) {
            TYPE_ERROR -> Ticker.TYPE_ERROR
            TYPE_INFO -> Ticker.TYPE_ANNOUNCEMENT
            TYPE_WARNING -> Ticker.TYPE_WARNING
            else -> Ticker.TYPE_ANNOUNCEMENT
        }
        binding.tickerTopAds.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                RouteManager.route(context, linkUrl.toString())
            }

            override fun onDismiss() {}
        })

        topAdsDashboardViewModel.getAutoTopUpDefaultSate.observe(viewLifecycleOwner) {
            if (it is Success) {
                isAutoTopUpActive = it.data.isAutoTopUp
                setButtonRefreshCreditState(it.data.isAutoTopUp)
                isAutoTopUpSelected = it.data.isAutoTopUpSelected
                creditPerformance = it.data.creditPerformance
                topUpUCount = it.data.countTopUp
            }
        }

        topAdsDashboardViewModel.autoTopUpStatusLiveData.observe(viewLifecycleOwner) {
            if (it is Success) {
                autoTopUpBonus = it.data.statusBonus
            }
        }
    }

    private fun setRecommendationProdukBerpostensi(item: RecommendationStatistics.Statistics.Data.ProductRecommendationStats) {
        with(binding.layoutRecommendasi.layoutProdukBerpostensi) {
            context?.resources?.let {
                layoutRoundedView.txtSubTitle.text = HtmlCompat.fromHtml(
                    String.format(
                        it.getString(R.string.topads_dashboard_kali_hari_value),
                        item.totalSearchCount
                    ),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                txtDescription.text = HtmlCompat.fromHtml(
                    String.format(
                        it.getString(R.string.topads_dashboard_produk_berpostensi_desc),
                        item.count
                    ),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
            produkBerpotensiAdapter.addItems(TopAdsDashboardBerandaUtils.mapImageModel(item.productList))
        }
    }

    // method to be invoked when ad type is changed from ringkasan dropdown section
    private fun adTypeChanged(chip: Chip) {
        fun dismissBottomSheet() {
            if (summaryAdTypesBottomSheet.isVisible) {
                summaryAdTypesBottomSheet.dismiss()
            }
        }
        if (chip.isSelected) {
            dismissBottomSheet()
            return
        }

        selectedAdType.isSelected = false

        chip.isSelected = true
        selectedAdType = chip
        binding.layoutRingkasan.txtAdType.text = chip.title
        loadSummaryStats()
        dismissBottomSheet()
    }

    fun loadSummaryStats() {
        topAdsDashboardViewModel.fetchSummaryStatistics(
            (activity as? TopAdsDashboardActivity)?.startDate.asString(),
            (activity as? TopAdsDashboardActivity)?.endDate.asString(),
            selectedAdType.adTypeId
        )
    }

    private fun loadData() {
        showShimmer()
        binding.swipeRefreshLayout.isEnabled = true
        topAdsDashboardViewModel.fetchShopDeposit()
        adTypeChanged(selectedAdType)
        topAdsDashboardViewModel.fetchRecommendationStatistics()
        topAdsDashboardViewModel.getTopadsTicker()
        topAdsDashboardViewModel.getAutoTopUpStatus()
        topAdsDashboardViewModel.getSelectedTopUpType()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_CREDIT) {
            topAdsDashboardViewModel.fetchShopDeposit()
        } else if (requestCode == REQUEST_CODE_SET_AUTO_TOPUP && resultCode == Activity.RESULT_OK) {
            if (data?.getBooleanExtra("no_redirect", false) != true) {
                goToCreditHistory(true)
            } else {
                topAdsDashboardViewModel.getSelectedTopUpType()
            }
        } else if (requestCode == REQUEST_CODE_TOP_UP_CREDIT) {
            topAdsDashboardViewModel.fetchShopDeposit()
            topAdsDashboardViewModel.getSelectedTopUpType()
            if (resultCode == Activity.RESULT_OK) {
                setButtonRefreshCreditState(true)
                Toaster.build(
                    binding.root,
                    getString(R.string.topads_dash_auto_topup_activated_toast),
                    Snackbar.LENGTH_SHORT,
                    Toaster.TYPE_NORMAL,
                    getString(com.tokopedia.topads.common.R.string.topads_common_text_ok)
                ).show()
            }
        }
    }

    private fun setButtonRefreshCreditState(isActive: Boolean) {
        isAutoTopUpActive = isActive
        if (isActive) {
            context?.let {
                binding.tambahKreditLayout.btnRefreshCredits.setColorFilter(
                    ContextCompat.getColor(
                        it,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
            }
        } else {
            binding.tambahKreditLayout.btnRefreshCredits.clearColorFilter()
        }
    }

    private fun goToCreditHistory(isFromSelection: Boolean = false) {
        context?.let {
            startActivityForResult(
                TopAdsCreditHistoryActivity.createInstance(
                    it,
                    isFromSelection,
                    showAutoTopUpOldFlow,
                    (activity as? TopAdsDashboardActivity)?.datePickerIndex
                        ?: DATE_PICKER_DEFAULT_INDEX
                ),
                REQUEST_CODE_SET_AUTO_TOPUP
            )
        }
    }

    private fun moveToInsightPage() {
        activity?.let {
            if (activity is TopAdsDashboardActivity) {
                (it as TopAdsDashboardActivity).switchTab(INSIGHT_PAGE)
            }
        }
    }


    private fun moveToInsightDetailPage(adGroupList: ArrayList<AdGroupUiModel>, item: AdGroupUiModel) {
        val bundle = Bundle()
        bundle.putString(RecommendationConstants.AD_GROUP_TYPE_KEY, item.adGroupType)
        bundle.putString(RecommendationConstants.AD_GROUP_NAME_KEY, item.adGroupName)
        bundle.putString(RecommendationConstants.AD_GROUP_ID_KEY, item.adGroupID)
        bundle.putInt(RecommendationConstants.AD_GROUP_COUNT_KEY, item.count)
        bundle.putInt(RecommendationConstants.INSIGHT_TYPE_KEY, item.insightType)
        bundle.putParcelableArrayList(RecommendationConstants.INSIGHT_TYPE_LIST_KEY, adGroupList)
        Intent(context, GroupDetailActivity::class.java).apply {
            this.putExtra(RecommendationConstants.GROUP_DETAIL_BUNDLE_KEY, bundle)
            startActivity(this)
        }
    }


    private fun setInsightWidgetBehaviour() {
        binding.layoutInsight.insightWidgetSeeMore.setOnClickListener {
            moveToInsightPage()
        }

        binding.layoutInsight.infoIcon.setOnClickListener{
            TopAdsToolTipBottomSheet.newInstance().also {
                it.setTitle(context?.getString(R.string.topads_beranda_saran) ?: "")
                it.setDescription(
                    context?.getString(R.string.topads_dashboard_saran_ads_info_sheet_desc) ?: ""
                )
            }.show(childFragmentManager)
        }

        binding.scrollView.viewTreeObserver?.addOnScrollChangedListener(object :
                ViewTreeObserver.OnScrollChangedListener {
                private var scrollY = Int.ZERO

                override fun onScrollChanged() {
                    val newScrollY = binding.scrollView.scrollY
                    if (newScrollY != scrollY && needToHitInsight) {
                        needToHitInsight = false
                        fetchInsight()
                    }
                    scrollY = newScrollY
                }
            })
    }

    private fun fetchInsight() {
        topAdsDashboardViewModel.fetchInsightTitle()
        topAdsDashboardViewModel.fetchInsightItems(
            adGroupType = "$PRODUCT_KEY,$HEADLINE_KEY",
            insightType = INSIGHT_TYPE_ALL,
            mapper = mapper
        )
    }
}
