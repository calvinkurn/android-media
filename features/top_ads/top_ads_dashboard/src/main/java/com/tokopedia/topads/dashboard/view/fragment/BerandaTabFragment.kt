package com.tokopedia.topads.dashboard.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.credit.history.view.activity.TopAdsCreditHistoryActivity
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.Chip
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATA_INSIGHT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.REQUEST_CODE_ADD_CREDIT
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData
import com.tokopedia.topads.dashboard.data.model.insightkey.KeywordInsightDataMain
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.data.utils.Utils.getString
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsWidgetSummaryStatisticsUseCase
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import com.tokopedia.topads.dashboard.view.adapter.beranda.LatestReadingTopAdsDashboardRvAdapter
import com.tokopedia.topads.dashboard.view.adapter.beranda.RingkasanTopAdsDashboardRvAdapter
import com.tokopedia.topads.dashboard.view.adapter.insight.TopAdsInsightTabAdapter
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment.Companion.MANUAL_AD
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsInsightMiniKeyFragment
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.topads.dashboard.view.sheet.RingkasanDropdownBottomSheet
import com.tokopedia.topads.dashboard.view.sheet.RingkasanInformationBottomSheet
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
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_topads_dashboard_beranda_base.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by Pika on 15/5/20.
 */
open class BerandaTabFragment : TopAdsBaseTabFragment() {

    private lateinit var shimmerView: ConstraintLayout
    private lateinit var scrollView: NestedScrollView
    private lateinit var autoTopUp: Typography
    private lateinit var creditAmount: Typography
    private lateinit var txtLastUpdated: Typography
    private lateinit var rvRingkasan: RecyclerView
    private lateinit var rvLatestReading: RecyclerView
    private lateinit var btnReadMore: UnifyButton
    private lateinit var addCredit: UnifyButton
    private lateinit var ivRingkasanDropDown: ImageUnify
    private lateinit var ivRingkasanInformation: ImageUnify
    private lateinit var creditHistoryImage: ImageUnify
    private lateinit var btnRefreshCredits: ImageUnify
    private lateinit var imgAutoDebit: ImageUnify
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var creditHistory: CardUnify
    private lateinit var statisticsPager: ViewPager

    private var dataStatistic: DataStatistic? = null
    private var insightCallBack: GoToInsight? = null
    private var currentDateText: String = ""

    private val ringkasanInformationBottomSheet by lazy { RingkasanInformationBottomSheet.createInstance() }
    private val ringkasanBottomSheet by lazy { RingkasanDropdownBottomSheet.createInstance(::ringkasanClicked) }
    private val ringkasanRvAdapter by lazy { RingkasanTopAdsDashboardRvAdapter.createInstance() }
    private val latestReadingRvAdapter by lazy { LatestReadingTopAdsDashboardRvAdapter.createInstance() }

    companion object {
        private const val REQUEST_CODE_SET_AUTO_TOPUP = 6

        fun createInstance(): BerandaTabFragment {
            return BerandaTabFragment()
        }

        private const val SELLER_CENTER_URL =
            "https://seller.tokopedia.com/edu/about-topads/iklan/?source=help&medium=android"
    }

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter

    @Inject
    lateinit var topAdsDashboardViewModel: TopAdsDashboardViewModel

    @TopAdsStatisticsType
    internal var selectedStatisticType: Int = TopAdsStatisticsType.PRODUCT_ADS
    private val topAdsInsightTabAdapter: TopAdsInsightTabAdapter? by lazy {
        context?.run { TopAdsInsightTabAdapter() }
    }

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
        selectedStatisticType = TopAdsStatisticsType.PRODUCT_ADS
        creditHistoryImage.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_wallet))
        //arrow.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_arrow))

        observeLiveData()
        showNewTopAdsDialog()
        setUpRecyclerView()
        setUpClick()
        loadData()
        loadStatisticsData()
        swipeRefreshLayout.setOnRefreshListener {
            loadData()
            loadStatisticsData()
        }
    }

    private fun observeLiveData() {
        topAdsDashboardViewModel.summaryStatisticsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    it.data.cells.let { it1 -> ringkasanRvAdapter.addItems(it1) }
                }
                is Fail -> {}
            }
        }
    }

    private fun ringkasanClicked(it: Chip) {

    }

    private fun setUpRecyclerView() {
        rvRingkasan.layoutManager = GridLayoutManager(requireContext(), 2)
        rvRingkasan.adapter = ringkasanRvAdapter

        rvLatestReading.layoutManager = LinearLayoutManager(requireContext())
        rvLatestReading.adapter = latestReadingRvAdapter

        ringkasanRvAdapter.infoClicked = { showInformationBottomSheet() }
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
        goToInsights.setOnClickListener {
            insightCallBack?.gotToInsights()
        }
        autoTopUp.setOnClickListener {
            startActivity(Intent(context, TopAdsEditAutoTopUpActivity::class.java))
        }
        ivRingkasanDropDown.setOnClickListener {
            ringkasanBottomSheet.show(childFragmentManager, "")
        }
        ivRingkasanInformation.setOnClickListener {
            showInformationBottomSheet()
        }
    }

    private fun showInformationBottomSheet() {
        ringkasanInformationBottomSheet.show(childFragmentManager, "")
    }

    private fun renderInsightViewPager(data: HashMap<String, KeywordInsightDataMain>) {
        viewPagerInsight?.adapter = getViewPagerAdapter(data)
        viewPagerInsight?.disableScroll(true)
        viewPagerInsight?.currentItem = 0
        viewPagerInsight?.offscreenPageLimit = TopAdsDashboardConstant.OFFSCREEN_PAGE_LIMIT
    }

    private fun getViewPagerAdapter(data: HashMap<String, KeywordInsightDataMain>): TopAdsDashboardBasePagerAdapter? {
        val list: ArrayList<FragmentTabItem> = arrayListOf()
        val bundle = Bundle()
        bundle.putSerializable(DATA_INSIGHT, data)
        list.add(FragmentTabItem("", TopAdsInsightMiniKeyFragment.createInstance(bundle)))
        val adapter = TopAdsDashboardBasePagerAdapter(childFragmentManager, 0)
        adapter.setList(list)
        return adapter
    }

    private fun onSuccessGetInsightData(response: InsightKeyData) {
        if (response.data.isEmpty()) {
            insightCard.visibility = View.GONE
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putBoolean(TopAdsDashboardConstant.FIRST_LAUNCH, false)
                commit()
            }
        } else {
            insightCard.visibility = View.VISIBLE
            initInsightTabAdapter(response)
            renderInsightViewPager(response.data)
        }
    }

    private fun loadData() {
        swipe_refresh_layout.isEnabled = true
        getAutoTopUpStatus()
        topAdsDashboardPresenter.getShopDeposit(::onLoadTopAdsShopDepositSuccess)
        topAdsDashboardPresenter.getInsight(resources, ::onSuccessGetInsightData)
        topAdsDashboardViewModel.getSummaryStatistics(
            startDate.getString(), endDate.getString(), "0"
        )
    }

    private fun onLoadTopAdsShopDepositSuccess(dataDeposit: DepositAmount) {
        swipe_refresh_layout.isRefreshing = false
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

    private fun initInsightTabAdapter(response: InsightKeyData) {
        val tabLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvTabInsight.layoutManager = tabLayoutManager
        topAdsInsightTabAdapter?.setListener(object :
            TopAdsInsightTabAdapter.OnRecyclerTabItemClick {
            override fun onTabItemClick(position: Int) {
                viewPagerInsight.currentItem = position
                if (position == 1 || position == 2) {
                    goToInsights.visibility = View.GONE
                    arrow.visibility = View.GONE
                } else {
                    goToInsights.visibility = View.VISIBLE
                    arrow.visibility = View.VISIBLE
                }
            }
        })
        topAdsInsightTabAdapter?.setTabTitles(resources, 0, 0, response.data.size)
        rvTabInsight.adapter = topAdsInsightTabAdapter
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GoToInsight) {
            insightCallBack = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        insightCallBack = null
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
        ivRingkasanDropDown = view.findViewById(R.id.ivRingkasanDropDown)
        txtLastUpdated = view.findViewById(R.id.txtLastUpdated)
        creditAmount = view.findViewById(R.id.creditAmount)
        ivRingkasanInformation = view.findViewById(R.id.ivRingkasanInformation)
        rvRingkasan = view.findViewById(R.id.rvRingkasan)
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

    private fun showNewTopAdsDialog() {
        DialogUnify(
            requireContext(), DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ILLUSTRATION
        ).apply {
            setTitle(resources.getString(R.string.topads_dashboard_home_dialog_title))
            setDescription(resources.getString(R.string.topads_dashboard_home_dialog_description))
            setPrimaryCTAText(resources.getString(R.string.topads_dashboard_home_dialog_button_text))
            setImageDrawable(R.drawable.topads_dashboard_dialog_img)
            setPrimaryCTAClickListener {
                dismiss()
                showCoachMark()
            }
        }.show()
    }

    private fun showCoachMark() {
        val coachMarkItems = arrayListOf(
            CoachMark2Item(
                rvRingkasan,
                resources.getString(R.string.topads_dashboard_home_coachmark_1_title),
                resources.getString(R.string.topads_dashboard_home_coachmark_1_desc),
                CoachMark2.POSITION_TOP
            ), CoachMark2Item(
                requireView().findViewById(R.id.topads_content_statistics),
                resources.getString(R.string.topads_dashboard_home_coachmark_2_title),
                resources.getString(R.string.topads_dashboard_home_coachmark_2_desc),
                CoachMark2.POSITION_TOP
            ),
            CoachMark2Item(
                rvLatestReading,
                resources.getString(R.string.topads_dashboard_home_coachmark_4_title),
                resources.getString(R.string.topads_dashboard_home_coachmark_4_desc),
                CoachMark2.POSITION_TOP
            )
        )
        val coachMark = CoachMark2(requireContext())
        coachMark.showCoachMark(coachMarkItems)
    }
}