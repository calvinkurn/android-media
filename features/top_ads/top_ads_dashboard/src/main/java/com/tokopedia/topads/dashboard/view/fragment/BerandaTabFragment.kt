package com.tokopedia.topads.dashboard.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.credit.history.view.activity.TopAdsCreditHistoryActivity
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.REQUEST_CODE_ADD_CREDIT
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.beranda.Chip
import com.tokopedia.topads.dashboard.data.utils.TopAdsDashboardBerandaUtils.getSummaryAdTypes
import com.tokopedia.topads.dashboard.data.utils.TopAdsDashboardBerandaUtils.mapToSummary
import com.tokopedia.topads.dashboard.data.utils.TopAdsDashboardBerandaUtils.showDialogWithCoachMark
import com.tokopedia.topads.dashboard.data.utils.TopAdsPrefsUtil.berandaDialogShown
import com.tokopedia.topads.dashboard.data.utils.TopAdsPrefsUtil.showBerandaDialog
import com.tokopedia.topads.dashboard.data.utils.Utils.asString
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import com.tokopedia.topads.dashboard.view.adapter.beranda.LatestReadingTopAdsDashboardRvAdapter
import com.tokopedia.topads.dashboard.view.adapter.beranda.TopAdsBerandaSummaryRvAdapter
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment.Companion.MANUAL_AD
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.topads.dashboard.view.sheet.SummaryAdTypesBottomSheet
import com.tokopedia.topads.dashboard.view.sheet.SummaryInformationBottomSheet
import com.tokopedia.topads.dashboard.viewmodel.TopAdsDashboardViewModel
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsAddCreditActivity
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsEditAutoTopUpActivity
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.util.*
import javax.inject.Inject

/**
 * Created by Pika on 15/5/20.
 */
open class BerandaTabFragment : TopAdsBaseTabFragment() {

    private lateinit var shimmerView: ConstraintLayout
    private lateinit var scrollView: NestedScrollView
    private lateinit var autoTopUp: Typography
    private lateinit var creditAmount: Typography
    private lateinit var txtLastUpdated: Typography
    private lateinit var txtAdType: Typography
    private lateinit var rvSummary: RecyclerView
    private lateinit var rvLatestReading: RecyclerView
    private lateinit var btnReadMore: UnifyButton
    private lateinit var addCredit: UnifyButton
    private lateinit var ivSummaryDropDown: ImageUnify
    private lateinit var ivSummaryInformation: ImageUnify
    private lateinit var creditHistoryImage: ImageUnify
    private lateinit var btnRefreshCredits: ImageUnify
    private lateinit var imgAutoDebit: ImageUnify
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var creditHistory: CardUnify
    private lateinit var statisticsPager: ViewPager

    private val checkResponse by lazy { CheckResponse() }
    private var dataStatistic: DataStatistic? = null
    private var currentDateText: String = ""

    private val summaryAdTypeList by lazy { resources.getSummaryAdTypes() }
    private var lastSelectedAdType: Chip? = null
    private val summaryAdTypesBottomSheet by lazy {
        SummaryAdTypesBottomSheet.createInstance(summaryAdTypeList, ::adTypeChanged)
    }
    private val summaryInformationBottomSheet by lazy { SummaryInformationBottomSheet.createInstance() }
    private val summaryRvAdapter by lazy { TopAdsBerandaSummaryRvAdapter.createInstance() }
    private val latestReadingRvAdapter by lazy { LatestReadingTopAdsDashboardRvAdapter.createInstance() }

    companion object {
        private const val REQUEST_CODE_SET_AUTO_TOPUP = 6

        fun createInstance(): BerandaTabFragment {
            return BerandaTabFragment()
        }
    }

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter

    @Inject
    lateinit var topAdsDashboardViewModel: TopAdsDashboardViewModel

    @TopAdsStatisticsType
    internal var selectedStatisticType: Int = TopAdsStatisticsType.PRODUCT_ADS

    override fun getLayoutId(): Int {
        return R.layout.fragment_topads_dashboard_beranda_base
    }

    override fun getChildScreenName(): String {
        return BerandaTabFragment::class.java.name
    }

    override fun loadChildStatisticsData() {
        loadStatisticsData()
    }

    override fun renderGraph() {
        currentStatisticsFragment?.showLineGraph(dataStatistic)
    }

    override fun getCustomDateText(customDateText: String) {
        currentDateText = customDateText
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showShimmer()
        selectedStatisticType = TopAdsStatisticsType.PRODUCT_ADS
        creditHistoryImage.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_wallet))

        observeLiveData()
        setUpRecyclerView()
        setUpClick()
        loadData()
        loadStatisticsData()
        swipeRefreshLayout.setOnRefreshListener {
            showShimmer()
            loadData()
            loadStatisticsData()
        }
    }

    private fun observeLiveData() {
        topAdsDashboardViewModel.summaryStatisticsLiveData.observe(viewLifecycleOwner) {
            checkResponse.summaryStats = true
            when (it) {
                is Success -> {
                    summaryRvAdapter.addItems(it.data.mapToSummary(requireContext()))
                    txtLastUpdated.text = String.format(
                        resources.getString(R.string.topads_dashboard_last_update_text),
                        it.data.lastUpdate
                    )
                }
                is Fail -> {}
            }
        }

        topAdsDashboardViewModel.latestReadingLiveData.observe(viewLifecycleOwner) {
            checkResponse.latestReading = true
            when (it) {
                is Success -> latestReadingRvAdapter.addItems(it.data)
                is Fail -> {}
            }
        }
    }

    //method to be invoked when ad type is changed from ringkasan dropdown section
    private fun adTypeChanged(chip: Chip) {
        fun dismissBottomSheet() {
            if (summaryAdTypesBottomSheet.isVisible)
                summaryAdTypesBottomSheet.dismiss()
        }
        if (chip.isSelected) {
            dismissBottomSheet()
            return
        }

        chip.isSelected = true
        lastSelectedAdType?.isSelected = false
        lastSelectedAdType = chip
        txtAdType.text = chip.title

        topAdsDashboardViewModel.fetchSummaryStatistics(
            startDate.asString(), endDate.asString(), chip.adTypeId
        )
        dismissBottomSheet()
    }

    private fun setUpRecyclerView() {
        rvSummary.layoutManager = GridLayoutManager(requireContext(), 2)
        rvSummary.adapter = summaryRvAdapter

        rvLatestReading.layoutManager = LinearLayoutManager(requireContext())
        rvLatestReading.adapter = latestReadingRvAdapter

        summaryRvAdapter.infoClicked = { showInformationBottomSheet() }
    }

    private fun setUpClick() {
        /*help_section.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, SELLER_CENTER_URL)
        }*/
        creditHistory.setOnClickListener {
            goToCreditHistory(false)
        }
        addCredit.setOnClickListener {
            val intent = Intent(activity, TopAdsAddCreditActivity::class.java)
            intent.putExtra(TopAdsAddCreditActivity.SHOW_FULL_SCREEN_BOTTOM_SHEET, true)
            startActivityForResult(intent, REQUEST_CODE_ADD_CREDIT)
        }
        autoTopUp.setOnClickListener {
            startActivity(Intent(context, TopAdsEditAutoTopUpActivity::class.java))
        }
        ivSummaryDropDown.setOnClickListener {
            summaryAdTypesBottomSheet.show(childFragmentManager, "")
        }
        ivSummaryInformation.setOnClickListener {
            showInformationBottomSheet()
        }
        btnRefreshCredits.setOnClickListener { topAdsDashboardPresenter.getShopDeposit(::onLoadTopAdsShopDepositSuccess) }
        btnReadMore.setOnClickListener {
            (requireActivity() as? TopAdsDashboardActivity)?.switchTab(
                TopAdsDashboardActivity.INSIGHT_PAGE
            )
        }
    }

    private fun showInformationBottomSheet() {
        summaryInformationBottomSheet.show(childFragmentManager, "")
    }

    private fun loadData() {
        swipeRefreshLayout.isEnabled = true
        getAutoTopUpStatus()
        topAdsDashboardPresenter.getShopDeposit(::onLoadTopAdsShopDepositSuccess)
        adTypeChanged(summaryAdTypeList[0])
        topAdsDashboardViewModel.fetchLatestReading()
    }

    private fun onLoadTopAdsShopDepositSuccess(dataDeposit: DepositAmount) {
        swipeRefreshLayout.isRefreshing = false
        creditAmount.text = dataDeposit.amountFmt
    }

    private fun onSuccesGetStatisticsInfo(dataStatistic: DataStatistic) {

        this.dataStatistic = dataStatistic
        if (this.dataStatistic != null && dataStatistic.cells.isNotEmpty()) {
            topAdsTabAdapter?.setSummary(
                dataStatistic.summary,
                resources.getStringArray(R.array.top_ads_tab_statistics_labels)
            )
        }
        val fragment = statisticsPager.adapter?.instantiateItem(
            statisticsPager, statisticsPager.currentItem
        ) as? Fragment
        if (fragment != null && fragment is TopAdsDashStatisticFragment) {
            fragment.showLineGraph(this.dataStatistic)
        }
    }

    private fun onSuccessGetAutoTopUpStatus(data: AutoTopUpStatus) {
        val isAutoTopUpActive =
            (data.status.toIntOrZero()) != TopAdsDashboardConstant.AUTO_TOPUP_INACTIVE
        if (isAutoTopUpActive) {
            autoTopUp.visibility = View.VISIBLE
            addCredit.visibility = View.GONE
            imgAutoDebit.setImageDrawable(context?.getResDrawable(R.drawable.topads_dash_auto_debit))
            imgAutoDebit.visibility = View.VISIBLE
        } else {
            autoTopUp.visibility = View.GONE
            addCredit.visibility = View.VISIBLE
            imgAutoDebit.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_CREDIT) {
            topAdsDashboardPresenter.getShopDeposit(::onLoadTopAdsShopDepositSuccess)
        } else if (requestCode == REQUEST_CODE_SET_AUTO_TOPUP && resultCode == Activity.RESULT_OK) {
            getAutoTopUpStatus()
            if (data?.getBooleanExtra("no_redirect", false) != true)
                goToCreditHistory(true)
        }
    }

    private fun getAutoTopUpStatus() {
        topAdsDashboardPresenter.getAutoTopUpStatus(resources, this::onSuccessGetAutoTopUpStatus)
    }

    private fun goToCreditHistory(isFromSelection: Boolean = false) {
        context?.let {
            startActivityForResult(
                TopAdsCreditHistoryActivity.createInstance(it, isFromSelection),
                REQUEST_CODE_SET_AUTO_TOPUP
            )
        }
    }

    fun loadStatisticsData() {
        if (startDate == null || endDate == null) return
        topAdsDashboardPresenter.getStatistic(
            startDate ?: Date(),
            endDate
                ?: Date(),
            selectedStatisticType,
            (activity as TopAdsDashboardActivity?)?.getAdInfo()
                ?: MANUAL_AD,
            ::onSuccesGetStatisticsInfo
        )
    }

    interface GoToInsight {
        fun gotToInsights()
    }

    override fun onDestroy() {
        super.onDestroy()
        topAdsDashboardViewModel.summaryStatisticsLiveData.removeObservers(this)
    }

    override fun setUpView(view: View) {
        creditHistory = view.findViewById(R.id.credit_history)
        txtAdType = view.findViewById(R.id.txtAdType)
        ivSummaryDropDown = view.findViewById(R.id.ivSummaryDropDown)
        txtLastUpdated = view.findViewById(R.id.txtLastUpdated)
        creditAmount = view.findViewById(R.id.creditAmount)
        ivSummaryInformation = view.findViewById(R.id.ivSummaryInformation)
        rvSummary = view.findViewById(R.id.rvSummary)
        rvLatestReading = view.findViewById(R.id.rvLatestReading)
        btnReadMore = view.findViewById(R.id.btnReadMore)
        scrollView = view.findViewById(R.id.scroll_view)
        shimmerView = view.findViewById(R.id.shimmerView)
        creditHistoryImage = view.findViewById(R.id.creditHistoryImage)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        btnRefreshCredits = view.findViewById(R.id.btnRefreshCredits)
        imgAutoDebit = view.findViewById(R.id.imgAutoDebit)
        addCredit = view.findViewById(R.id.addCredit)
        autoTopUp = view.findViewById(R.id.autoTopUp)
        statisticsPager = view.findViewById(R.id.pager)
    }

    private fun showFirstTimeDialog() {
        if (!requireActivity().showBerandaDialog()) return
        requireContext().showDialogWithCoachMark(
            scrollView,
            rvSummary, requireView().findViewById(R.id.topads_content_statistics),
            rvLatestReading, (requireActivity() as TopAdsDashboardActivity).ivEducationTopAdsActionBar
        )
        requireActivity().berandaDialogShown()
    }

    private fun hideShimmer() {
        if (!checkResponse.creditHistory || !checkResponse.latestReading || !checkResponse.summaryStats) return
        shimmerView.hide()
        (requireActivity() as TopAdsDashboardActivity).toggleMultiActionButton(true)
        swipeRefreshLayout.show()
        showFirstTimeDialog()
    }

    private fun showShimmer() {
        shimmerView.show()
        (requireActivity() as TopAdsDashboardActivity).toggleMultiActionButton(false)
        swipeRefreshLayout.hide()
    }

    //this class holds 3 boolean to keep track if all the 3 api's have been called successfully, as if all values are true will be hiding shimmer view and showing the actual view
    inner class CheckResponse {
        var creditHistory: Boolean = true
            set(value) {
                field = value
                hideShimmer()
            }
        var summaryStats: Boolean = false
            set(value) {
                field = value
                hideShimmer()
            }
        var latestReading: Boolean = false
            set(value) {
                field = value
                hideShimmer()
            }
    }
}