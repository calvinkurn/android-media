package com.tokopedia.topads.dashboard.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.*
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
import com.tokopedia.topads.dashboard.data.utils.Utils
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
open class BerandaTabFragment : BaseDaggerFragment() {

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
    private lateinit var graphContainer: FrameLayout

    private val graphLayout by lazy { TopAdsMultiLineGraphFragment() }
    private val checkResponse by lazy { CheckResponse() }

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

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val layout =
            inflater.inflate(R.layout.fragment_topads_dashboard_beranda_base, container, false)
        setUpView(layout)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showShimmer()
        creditHistoryImage.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_wallet))

        observeLiveData()
        setUpRecyclerView()
        setUpClick()
        loadData()
        initializeGraph()
        swipeRefreshLayout.setOnRefreshListener {
            showShimmer()
            loadData()
        }
    }

    private fun initializeGraph() {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.graph_layout, graphLayout, "")
            .commit()
    }

    private fun observeLiveData() {
        topAdsDashboardViewModel.summaryStatisticsLiveData.observe(viewLifecycleOwner) {
            checkResponse.summaryStats = true
            when (it) {
                is Success -> {
                    graphLayout.setValue(it.data.cells)
                    summaryRvAdapter.addItems(it.data.summary.mapToSummary(requireContext()))
                    txtLastUpdated.text = String.format(
                        resources.getString(R.string.topads_dashboard_last_update_text),
                        it.data.summary.lastUpdate
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
            Utils.getStartDate().asString(), Utils.getEndDate().asString(), chip.adTypeId
        )
        dismissBottomSheet()
    }

    private fun setUpRecyclerView() {
        rvSummary.layoutManager = GridLayoutManager(requireContext(), 2)
        rvSummary.adapter = summaryRvAdapter

        rvLatestReading.layoutManager = LinearLayoutManager(requireContext())
        rvLatestReading.adapter = latestReadingRvAdapter

        summaryRvAdapter.infoClicked = { showInformationBottomSheet() }
        summaryRvAdapter.itemClicked = { onSummaryItemClicked() }
    }

    private fun onSummaryItemClicked() {
        if (summaryRvAdapter.selectedItems.size == 0) {
            graphContainer.hide()
            return
        }
        if (!graphContainer.isVisible) {
            graphContainer.show()
        }
        val items = summaryRvAdapter.selectedItems.map {
            TopAdsMultiLineGraphFragment.MultiLineGraph(it.id, it.selectedColor)
        }
        graphLayout.showLineGraph(items)
    }

    private fun setUpClick() {
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

    interface GoToInsight {
        fun gotToInsights()
    }

    override fun onDestroy() {
        super.onDestroy()
        topAdsDashboardViewModel.summaryStatisticsLiveData.removeObservers(this)
    }

    override fun getScreenName(): String {
        return BerandaTabFragment::class.java.name
    }

    private fun setUpView(view: View) {
        graphContainer = view.findViewById(R.id.graph_layout)
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
    }

    private fun showFirstTimeDialog() {
        if (!requireActivity().showBerandaDialog()) return
        requireContext().showDialogWithCoachMark(
            scrollView,
            rvSummary,
            requireView().findViewById(R.id.topads_content_statistics),
            rvLatestReading,
            (requireActivity() as TopAdsDashboardActivity).ivEducationTopAdsActionBar
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