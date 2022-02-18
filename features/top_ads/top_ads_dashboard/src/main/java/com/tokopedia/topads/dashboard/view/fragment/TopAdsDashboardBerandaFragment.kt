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
import com.tokopedia.topads.dashboard.data.model.beranda.*
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

    private fun kataKunciItemSelected(item: KataKunciHomePageBase) {
        when (item) {
            is KataKunciSimpleButton -> {
                (requireActivity() as TopAdsDashboardActivity).switchTab(3)
            }
            is RecommendationStatistics.Statistics.Data.KeywordRecommendationStats.TopGroup -> {
                kataKunciChipsDetailRvAdapter.addItems(item, resources)
            }
        }
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
            requireContext().openWebView(READ_MORE_URL)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData()
        }
        summaryRvAdapter.infoClicked = { showInformationBottomSheet() }
        summaryRvAdapter.itemClicked = ::onSummaryItemClicked

        binding.layoutRecommendasi.apply {
            layoutkataKunci.button.setOnClickListener {
                (requireActivity() as TopAdsDashboardActivity).switchTab(3)
            }
            layoutProdukBerpostensi.button.setOnClickListener {
                (requireActivity() as TopAdsDashboardActivity).switchTab(3)
            }
            layoutProdukBerpostensi.button.setOnClickListener {
                (requireActivity() as TopAdsDashboardActivity).switchTab(3)
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.layoutRingkasan.rvSummary.adapter = summaryRvAdapter
        binding.layoutLatestReading.rvLatestReading.adapter = latestReadingRvAdapter
        binding.layoutRecommendasi.layoutProdukBerpostensi.recyclerView.adapter =
            produkBerpotensiAdapter
        binding.layoutRecommendasi.layoutAnggaranHarian.rvVertical.adapter =
            anggarnHarianAdapter
        binding.layoutRecommendasi.layoutkataKunci.recyclerView.adapter = kataKunciChipsRvAdapter
        binding.layoutRecommendasi.layoutkataKunci.rvVertical.adapter =
            kataKunciChipsDetailRvAdapter
        latestReadingRvAdapter.addItems(topAdsDashboardViewModel.getLatestReadings())
    }

    private fun showInformationBottomSheet() {
        summaryInformationBottomSheet.show(childFragmentManager, "")
    }

    private fun onSummaryItemClicked(selectedItems: Set<SummaryBeranda>) {
        if (selectedItems.isEmpty()) {
            binding.layoutRingkasan.graphLayout.hide()
            return
        }
        if (!binding.layoutRingkasan.graphLayout.isVisible) {
            binding.layoutRingkasan.graphLayout.show()
        }
        val items = selectedItems.map {
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
            binding, (requireActivity() as TopAdsDashboardActivity).ivEducationTopAdsActionBar
        )
    }

    private fun hideShimmer() {
        if (!binding.shimmerView.root.isVisible) return
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
        topAdsDashboardViewModel.shopDepositLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
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
                    summaryRvAdapter.addItems(it.data.summary.mapToSummary(requireContext()))
                    binding.layoutRingkasan.txtLastUpdated.text = String.format(
                        resources.getString(R.string.topads_dashboard_last_update_text),
                        it.data.summary.lastUpdate
                    )
                }
                is Fail -> {}
            }
        }

        topAdsDashboardViewModel.recommendationStatsLiveData.observe(viewLifecycleOwner) {
            hideShimmer()
            when (it) {
                is Success -> {
                    setRecommendationProdukBerpostensi(it.data.productRecommendationStats)
                    setRecommendationAnggaranHarian(it.data.dailyBudgetRecommendationStats)
                    setRecommendationKataKunci(it.data.keywordRecommendationStats)
                }
                is Fail -> {}
            }
        }
    }

    private fun setRecommendationProdukBerpostensi(item: RecommendationStatistics.Statistics.Data.ProductRecommendationStats) {
        with(binding.layoutRecommendasi.layoutProdukBerpostensi) {
            layoutRoundedView.txtSubTitle.text = String.format(
                resources.getString(R.string.topads_dashboard_kali_hari_value),
                item.totalSearchCount
            )
            txtDescription.text = String.format(
                resources.getString(R.string.topads_dashboard_produk_berpostensi_desc), item.count
            )
            produkBerpotensiAdapter.addItems(item.productList)
        }
    }

    private fun setRecommendationAnggaranHarian(item: RecommendationStatistics.Statistics.Data.DailyBudgetRecommendationStats) {
        with(binding.layoutRecommendasi.layoutAnggaranHarian) {
            layoutRoundedView.txtSubTitle.text = String.format(
                resources.getString(R.string.topads_dashboard_kali_hari_value), item.totalClicks
            )
            txtDescription.text = String.format(
                resources.getString(R.string.topads_dashboard_anggaran_harian_desc), item.count
            )
            val list = item.groupList
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
    }

    private fun setRecommendationKataKunci(item: RecommendationStatistics.Statistics.Data.KeywordRecommendationStats) {
        with(binding.layoutRecommendasi.layoutkataKunci) {
            layoutRoundedView.txtSubTitle.text = String.format(
                resources.getString(R.string.topads_dashboard_n_grup_iklanmu), item.groupCount
            )
            txtDescription.text = String.format(
                resources.getString(R.string.topads_dashboard_kata_kunci_desc), item.groupCount
            )
            kataKunciChipsRvAdapter.addItems(
                item.topGroups,
                binding.root.resources.getString(R.string.topads_dashboard_lihat_semua)
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
        topAdsDashboardViewModel.fetchShopDeposit()
        adTypeChanged(selectedAdType)
        topAdsDashboardViewModel.fetchRecommendationStatistics()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_CREDIT) {
            topAdsDashboardViewModel.fetchShopDeposit()
        } else if (requestCode == REQUEST_CODE_SET_AUTO_TOPUP && resultCode == Activity.RESULT_OK) {
            if (data?.getBooleanExtra("no_redirect", false) != true)
                goToCreditHistory(true)
        }
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
}