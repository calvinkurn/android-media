package com.tokopedia.topads.dashboard.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.credit.history.view.activity.TopAdsCreditHistoryActivity
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.REQUEST_CODE_ADD_CREDIT
import com.tokopedia.topads.dashboard.data.model.beranda.Chip
import com.tokopedia.topads.dashboard.data.model.beranda.ImageModel
import com.tokopedia.topads.dashboard.data.model.beranda.RecommendationStatistics
import com.tokopedia.topads.dashboard.data.utils.TopAdsDashboardBerandaUtils.getSummaryAdTypes
import com.tokopedia.topads.dashboard.data.utils.TopAdsDashboardBerandaUtils.mapToSummary
import com.tokopedia.topads.dashboard.data.utils.TopAdsDashboardBerandaUtils.showDialogWithCoachMark
import com.tokopedia.topads.dashboard.data.utils.Utils.asString
import com.tokopedia.topads.dashboard.data.utils.Utils.openWebView
import com.tokopedia.topads.dashboard.databinding.FragmentTopadsDashboardBerandaBaseBinding
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import com.tokopedia.topads.dashboard.view.adapter.beranda.*
import com.tokopedia.topads.dashboard.view.fragment.education.READ_MORE_URL
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.topads.dashboard.view.sheet.SummaryAdTypesBottomSheet
import com.tokopedia.topads.dashboard.view.sheet.SummaryInformationBottomSheet
import com.tokopedia.topads.dashboard.viewmodel.TopAdsDashboardViewModel
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsAddCreditActivity
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsEditAutoTopUpActivity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Ankit
 */
open class TopAdsDashboardBerandaFragment : BaseDaggerFragment() {

    private lateinit var binding: FragmentTopadsDashboardBerandaBaseBinding

    private val graphLayoutFragment by lazy { TopAdsMultiLineGraphFragment() }
    private val checkResponse by lazy { CheckResponse() }

    private val summaryAdTypeList by lazy { resources.getSummaryAdTypes() }
    private lateinit var selectedAdType: Chip
    private val summaryAdTypesBottomSheet by lazy {
        SummaryAdTypesBottomSheet.createInstance(summaryAdTypeList, ::adTypeChanged)
    }
    private val summaryInformationBottomSheet by lazy { SummaryInformationBottomSheet.createInstance() }

    private val kataKunciChipsDetailRvAdapter by lazy { TopAdsBerandsKataKunciChipsDetailRvAdapter() }
    private val kataKunciChipsRvAdapter by lazy { TopAdsBerandsKataKunciChipsRvAdapter(::kataKunciItemSelected) }
    private val anggarnHarianAdapter by lazy { TopAdsBerandaAnggarnHarianAdapter() }
    private val produkBerpotensiAdapter by lazy { TopadsImageRvAdapter.createInstance() }
    private val summaryRvAdapter by lazy { TopAdsBerandaSummaryRvAdapter.createInstance() }
    private val latestReadingRvAdapter by lazy { LatestReadingTopAdsDashboardRvAdapter.createInstance() }

    companion object {
        private const val REQUEST_CODE_SET_AUTO_TOPUP = 6

        fun createInstance(): TopAdsDashboardBerandaFragment {
            return TopAdsDashboardBerandaFragment()
        }
    }

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter

    @Inject
    lateinit var topAdsDashboardViewModel: TopAdsDashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

    private fun kataKunciItemSelected(item: Any) {
        //todo add items to TopAdsBerandsKataKunciChipsDetailRvAdapter
    }

    private fun initializeView() {
        with(binding.layoutRecommendasi.layoutProdukBerpostensi) {
            this.txtTitle.text = resources.getString(R.string.topads_dashboard_produk_berpotensi)
            this.layoutRoundedView.txtTitle.text =
                resources.getString(R.string.topads_dashboard_potensi_tampil)
            this.layoutRoundedView.imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    com.tokopedia.unifycomponents.R.drawable.iconunify_product_budget
                )
            )
            this.button.text = resources.getString(R.string.topads_dashboard_atur_iklannya)
            rvVertical.hide()
        }

        with(binding.layoutRecommendasi.layoutAnggaranHarian) {
            this.layoutRoundedView.txtTitle.text =
                resources.getString(R.string.topads_dash_potential_click)
            this.txtTitle.text = resources.getString(R.string.topads_dash_anggaran_harian)
            this.layoutRoundedView.imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(), com.tokopedia.unifycomponents.R.drawable.iconunify_saldo
                )
            )
            this.button.text = resources.getString(R.string.topads_dashboard_atur_anggaran_harian)
            recyclerView.hide()
        }

        with(binding.layoutRecommendasi.layoutkataKunci) {
            this.layoutRoundedView.txtTitle.text =
                resources.getString(R.string.topads_dashboard_kata_kunci_yang)
            this.txtTitle.text = resources.getString(R.string.label_top_ads_keyword)
            this.layoutRoundedView.imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(), com.tokopedia.unifycomponents.R.drawable.iconunify_keyword
                )
            )
            this.button.text = resources.getString(R.string.topads_dashboard_atur_kata_kunci)
        }
    }

    private fun initializeListener() {
        binding.tambahKreditLayout.root.setOnClickListener {
            goToCreditHistory(false)
        }
        binding.tambahKreditLayout.addCredit.setOnClickListener {
            val intent = Intent(activity, TopAdsAddCreditActivity::class.java)
            intent.putExtra(TopAdsAddCreditActivity.SHOW_FULL_SCREEN_BOTTOM_SHEET, true)
            startActivityForResult(intent, REQUEST_CODE_ADD_CREDIT)
        }
        binding.tambahKreditLayout.autoTopUp.setOnClickListener {
            startActivity(Intent(context, TopAdsEditAutoTopUpActivity::class.java))
        }
        binding.layoutRingkasan.ivSummaryDropDown.setOnClickListener {
            summaryAdTypesBottomSheet.show(childFragmentManager, "")
        }
        binding.layoutRingkasan.ivSummaryInformation.setOnClickListener {
            showInformationBottomSheet()
        }
        binding.tambahKreditLayout.btnRefreshCredits.setOnClickListener {
            topAdsDashboardPresenter.getShopDeposit(::onLoadTopAdsShopDepositSuccess)
        }
        binding.layoutLatestReading.btnReadMore.setOnClickListener {
            requireContext().openWebView(READ_MORE_URL)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData()
        }
        summaryRvAdapter.infoClicked = { showInformationBottomSheet() }
        summaryRvAdapter.itemClicked = { onSummaryItemClicked() }
    }

    private fun setUpRecyclerView() {
        binding.layoutRingkasan.rvSummary.adapter = summaryRvAdapter
        binding.layoutLatestReading.rvLatestReading.adapter = latestReadingRvAdapter
        binding.layoutRecommendasi.layoutProdukBerpostensi.recyclerView.adapter =
            produkBerpotensiAdapter
        binding.layoutRecommendasi.layoutAnggaranHarian.rvVertical.adapter =
            anggarnHarianAdapter
        binding.layoutRecommendasi.layoutkataKunci.recyclerView.adapter = kataKunciChipsRvAdapter
        binding.layoutRecommendasi.layoutkataKunci.rvVertical.adapter = kataKunciChipsDetailRvAdapter
    }

    private fun showInformationBottomSheet() {
        summaryInformationBottomSheet.show(childFragmentManager, "")
    }

    private fun onSummaryItemClicked() {
        if (summaryRvAdapter.selectedItems.size == 0) {
            binding.layoutRingkasan.graphLayout.hide()
            return
        }
        if (!binding.layoutRingkasan.graphLayout.isVisible) {
            binding.layoutRingkasan.graphLayout.show()
        }
        val items = summaryRvAdapter.selectedItems.map {
            TopAdsMultiLineGraphFragment.MultiLineGraph(it.id, it.selectedColor)
        }
        graphLayoutFragment.showLineGraph(items)
    }

    private fun initializeGraph() {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.graph_layout, graphLayoutFragment, "")
            .commit()
    }

    private fun showFirstTimeDialog() {
        requireActivity().showDialogWithCoachMark(
            binding.scrollView, binding.layoutRingkasan.rvSummary,
            requireView().findViewById(R.id.topads_content_statistics),
            binding.layoutLatestReading.rvLatestReading,
            (requireActivity() as TopAdsDashboardActivity).ivEducationTopAdsActionBar
        )
    }

    private fun hideShimmer() {
        if (!checkResponse.creditHistory || !checkResponse.latestReading || !checkResponse.summaryStats || !checkResponse.recommendation) return
        binding.shimmerView.root.hide()
        (requireActivity() as TopAdsDashboardActivity).toggleMultiActionButton(true)
        binding.swipeRefreshLayout.show()
        showFirstTimeDialog()
    }

    private fun showShimmer() {
        binding.shimmerView.root.show()
        (requireActivity() as TopAdsDashboardActivity).toggleMultiActionButton(false)
        binding.swipeRefreshLayout.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedAdType = summaryAdTypeList[0]

        setUpRecyclerView()

        observeLiveData()
        initializeListener()
        initializeGraph()
        loadData()
    }

    private fun observeLiveData() {
        topAdsDashboardViewModel.summaryStatisticsLiveData.observe(viewLifecycleOwner) {
            checkResponse.summaryStats = true
            when (it) {
                is Success -> {
                    graphLayoutFragment.setValue(it.data.cells)
                    summaryRvAdapter.addItems(it.data.summary.mapToSummary(requireContext()))
                    binding.layoutRingkasan.txtLastUpdated.text = String.format(
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

        topAdsDashboardViewModel.recommendationStatsLiveData.observe(viewLifecycleOwner) {
            checkResponse.recommendation = true
            when (it) {
                is Success -> setRecommendationData(it.data)
                is Fail -> {}
            }
        }
    }

    private fun setRecommendationData(data: RecommendationStatistics.Statistics.Data) {
        with(binding.layoutRecommendasi.layoutProdukBerpostensi) {
            val item = data.productRecommendationStats
            layoutRoundedView.txtSubTitle.text = String.format(
                resources.getString(R.string.topads_dashboard_kali_hari_value),
                item.totalSearchCount
            )
            txtDescription.text = String.format(
                resources.getString(R.string.topads_dashboard_produk_berpostensi_desc), item.count
            )
            val it = listOf(
                ImageModel("https://images-staging.tokopedia.net/img/jJtrdn/2021/7/29/5d358149-b754-4b5e-80a6-badeba45462e.jpg"),
                ImageModel("https://images-staging.tokopedia.net/img/jJtrdn/2021/7/29/5d358149-b754-4b5e-80a6-badeba45462e.jpg"),
                ImageModel("https://images-staging.tokopedia.net/img/jJtrdn/2021/7/29/5d358149-b754-4b5e-80a6-badeba45462e.jpg"),
            )
            //item.productList
            produkBerpotensiAdapter.addItems(it)
        }

        with(binding.layoutRecommendasi.layoutAnggaranHarian) {
            val item = data.dailyBudgetRecommendationStats
            layoutRoundedView.txtSubTitle.text = String.format(
                resources.getString(R.string.topads_dashboard_kali_hari_value), item.totalClicks
            )
            txtDescription.text = String.format(
                resources.getString(R.string.topads_dashboard_anggaran_harian_desc), item.count
            )
            //val list = item.groupList
            val list = listOf(
                RecommendationStatistics.Statistics.Data.DailyBudgetRecommendationStats.GroupInfo("A"),
                RecommendationStatistics.Statistics.Data.DailyBudgetRecommendationStats.GroupInfo("A"),
                RecommendationStatistics.Statistics.Data.DailyBudgetRecommendationStats.GroupInfo("A"),
                RecommendationStatistics.Statistics.Data.DailyBudgetRecommendationStats.GroupInfo("A"),
            )
            if (list.isNotEmpty()) {
                val items = mutableListOf<String>()
                items.add(list[0].groupName)

                val restCount = list.size - 1
                if (restCount > 0) {
                    items.add(
                        String.format(
                            resources.getString(R.string.topads_dashboard_grup_iklan), restCount
                        )
                    )
                }
                anggarnHarianAdapter.addItems(items)
            }
        }

        with(binding.layoutRecommendasi.layoutkataKunci) {
            val item = data.keywordRecommendationStats
            layoutRoundedView.txtSubTitle.text = String.format(
                resources.getString(R.string.topads_dashboard_n_grup_iklanmu), item.groupCount
            )
            txtDescription.text = String.format(
                resources.getString(R.string.topads_dashboard_kata_kunci_desc), item.groupCount
            )
            /*kataKunciChipsRvAdapter.addItems(
                item.topGroups, binding.root.resources.getString(R.string.topads_dashboard_lihat_semua)
            )*/
            kataKunciChipsRvAdapter.addItems(
                listOf(
                    RecommendationStatistics.Statistics.Data.KeywordRecommendationStats.TopGroup(
                        groupName = "ankit"
                    ),
                    RecommendationStatistics.Statistics.Data.KeywordRecommendationStats.TopGroup(
                        groupName = "ankit"
                    ),
                    RecommendationStatistics.Statistics.Data.KeywordRecommendationStats.TopGroup(
                        groupName = "ankit"
                    ),
                    RecommendationStatistics.Statistics.Data.KeywordRecommendationStats.TopGroup(
                        groupName = "ankit"
                    ),
                    RecommendationStatistics.Statistics.Data.KeywordRecommendationStats.TopGroup(
                        groupName = "ankit"
                    )
                ), "Lihat Semua'"
            )
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

        selectedAdType.isSelected = false

        chip.isSelected = true
        selectedAdType = chip
        binding.layoutRingkasan.txtAdType.text = chip.title
        loadSummaryStats()
        dismissBottomSheet()
    }

    fun loadSummaryStats() {
        topAdsDashboardViewModel.fetchSummaryStatistics(
            (requireActivity() as? TopAdsDashboardActivity)?.startDate.asString(),
            (requireActivity() as? TopAdsDashboardActivity)?.endDate.asString(),
            selectedAdType.adTypeId
        )
    }

    private fun loadData() {
        showShimmer()
        binding.swipeRefreshLayout.isEnabled = true
        getAutoTopUpStatus()
        topAdsDashboardPresenter.getShopDeposit(::onLoadTopAdsShopDepositSuccess)
        adTypeChanged(selectedAdType)
        topAdsDashboardViewModel.fetchLatestReading()
        topAdsDashboardViewModel.fetchRecommendationStatistics()
    }

    private fun onLoadTopAdsShopDepositSuccess(dataDeposit: DepositAmount) {
        binding.swipeRefreshLayout.isRefreshing = false
        binding.tambahKreditLayout.creditAmount.text = dataDeposit.amountFmt
    }

    private fun onSuccessGetAutoTopUpStatus(data: AutoTopUpStatus) {
        val isAutoTopUpActive =
            (data.status.toIntOrZero()) != TopAdsDashboardConstant.AUTO_TOPUP_INACTIVE
        if (isAutoTopUpActive) {
            binding.tambahKreditLayout.autoTopUp.visibility = View.VISIBLE
            binding.tambahKreditLayout.addCredit.visibility = View.GONE
            binding.tambahKreditLayout.imgAutoDebit.setImageDrawable(context?.getResDrawable(R.drawable.topads_dash_auto_debit))
            binding.tambahKreditLayout.imgAutoDebit.visibility = View.VISIBLE
        } else {
            binding.tambahKreditLayout.autoTopUp.visibility = View.GONE
            binding.tambahKreditLayout.addCredit.visibility = View.VISIBLE
            binding.tambahKreditLayout.imgAutoDebit.visibility = View.GONE
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

    override fun onDestroy() {
        super.onDestroy()
        topAdsDashboardViewModel.summaryStatisticsLiveData.removeObservers(this)
    }

    interface GoToInsight {
        fun gotToInsights()
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
        var recommendation: Boolean = false
            set(value) {
                field = value
                hideShimmer()
            }
    }
}