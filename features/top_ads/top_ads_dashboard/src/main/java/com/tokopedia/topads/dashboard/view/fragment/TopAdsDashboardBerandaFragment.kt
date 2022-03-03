package com.tokopedia.topads.dashboard.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.credit.history.view.activity.TopAdsCreditHistoryActivity
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATE_PICKER_DEFAULT_INDEX
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.REQUEST_CODE_ADD_CREDIT
import com.tokopedia.topads.dashboard.data.model.beranda.*
import com.tokopedia.topads.dashboard.data.utils.TopAdsDashboardBerandaUtils
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
import com.tokopedia.topads.dashboard.view.sheet.SummaryAdTypesBottomSheet
import com.tokopedia.topads.dashboard.view.sheet.SummaryInformationBottomSheet
import com.tokopedia.topads.dashboard.viewmodel.TopAdsDashboardViewModel
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsAddCreditActivity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Ankit
 */
open class TopAdsDashboardBerandaFragment : BaseDaggerFragment() {

    private lateinit var binding: FragmentTopadsDashboardBerandaBaseBinding

    private val graphLayoutFragment by lazy(LazyThreadSafetyMode.NONE) { TopAdsMultiLineGraphFragment() }

    private val summaryAdTypeList by lazy(LazyThreadSafetyMode.NONE) {
        context?.resources?.getSummaryAdTypes() ?: listOf()
    }
    private var selectedAdType = Chip()
    private val summaryAdTypesBottomSheet by lazy(LazyThreadSafetyMode.NONE) {
        SummaryAdTypesBottomSheet.createInstance(summaryAdTypeList, ::adTypeChanged)
    }
    private val summaryInformationBottomSheet by lazy(LazyThreadSafetyMode.NONE) { SummaryInformationBottomSheet() }

    private val kataKunciChipsDetailRvAdapter by lazy(LazyThreadSafetyMode.NONE) { TopAdsBerandsKataKunciChipsDetailRvAdapter() }
    private val kataKunciChipsRvAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TopAdsBerandsKataKunciChipsRvAdapter(::kataKunciItemSelected)
    }
    private val anggarnHarianAdapter by lazy(LazyThreadSafetyMode.NONE) { TopAdsBerandaAnggarnHarianAdapter() }
    private val produkBerpotensiAdapter by lazy(LazyThreadSafetyMode.NONE) { TopadsImageRvAdapter() }
    private val summaryRvAdapter by lazy(LazyThreadSafetyMode.NONE) { TopAdsBerandaSummaryRvAdapter() }
    private val latestReadingRvAdapter by lazy(LazyThreadSafetyMode.NONE) { LatestReadingTopAdsDashboardRvAdapter() }

    companion object {
        private const val REQUEST_CODE_SET_AUTO_TOPUP = 6

        fun createInstance(): TopAdsDashboardBerandaFragment {
            return TopAdsDashboardBerandaFragment()
        }
    }

    @Inject
    lateinit var topAdsDashboardViewModel: TopAdsDashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
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
                (activity as? TopAdsDashboardActivity)?.switchTab(3)
            }
            is RecommendationStatistics.Statistics.Data.KeywordRecommendationStats.TopGroup -> {
                context?.resources?.let {
                    kataKunciChipsDetailRvAdapter.addItems(item, it)
                }
            }
        }
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
                            it, com.tokopedia.unifycomponents.R.drawable.iconunify_product_budget
                        )
                    )
                }
                this.button.text = res.getString(R.string.topads_dashboard_atur_iklannya)
                rvVertical.hide()
            }

            with(binding.layoutRecommendasi.layoutAnggaranHarian) {
                this.layoutRoundedView.txtTitle.text =
                    res.getString(R.string.topads_dash_potential_click)
                this.txtTitle.text = res.getString(R.string.topads_dash_anggaran_harian)
                context?.let {
                    this.layoutRoundedView.imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            it, com.tokopedia.unifycomponents.R.drawable.iconunify_saldo
                        )
                    )
                }
                this.button.text = res.getString(R.string.topads_dashboard_atur_anggaran_harian)
                recyclerView.hide()
            }

            with(binding.layoutRecommendasi.layoutkataKunci) {
                this.layoutRoundedView.txtTitle.text =
                    res.getString(R.string.topads_dashboard_kata_kunci_yang)
                this.txtTitle.text = res.getString(R.string.label_top_ads_keyword)
                context?.let {
                    this.layoutRoundedView.imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            it, com.tokopedia.unifycomponents.R.drawable.iconunify_keyword
                        )
                    )
                }
                this.button.text = res.getString(R.string.topads_dashboard_atur_kata_kunci)
            }
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
            context?.openWebView(READ_MORE_URL)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData()
        }
        summaryRvAdapter.infoClicked = { showInformationBottomSheet() }
        summaryRvAdapter.itemClicked = ::onSummaryItemClicked

        binding.layoutRecommendasi.apply {
            layoutkataKunci.button.setOnClickListener {
                (activity as? TopAdsDashboardActivity)?.switchTab(3)
            }
            layoutProdukBerpostensi.button.setOnClickListener {
                (activity as? TopAdsDashboardActivity)?.switchTab(3)
            }
            layoutProdukBerpostensi.button.setOnClickListener {
                (activity as? TopAdsDashboardActivity)?.switchTab(3)
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
        (activity as? TopAdsDashboardActivity)?.let {
            requireActivity().showDialogWithCoachMark(
                binding, it.ivEducationTopAdsActionBar
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

        if (summaryAdTypeList.isNotEmpty())
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
                    setRecommendationAnggaranHarian(it.data.dailyBudgetRecommendationStats)
                    setRecommendationKataKunci(it.data.keywordRecommendationStats)
                }
                is Fail -> {}
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
                    ), HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                txtDescription.text = HtmlCompat.fromHtml(
                    String.format(
                        it.getString(R.string.topads_dashboard_produk_berpostensi_desc),
                        item.count
                    ), HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
            produkBerpotensiAdapter.addItems(TopAdsDashboardBerandaUtils.mapImageModel(item.productList))
        }
    }

    private fun setRecommendationAnggaranHarian(item: RecommendationStatistics.Statistics.Data.DailyBudgetRecommendationStats) {
        with(binding.layoutRecommendasi.layoutAnggaranHarian) {
            context?.resources?.let {
                layoutRoundedView.txtSubTitle.text = HtmlCompat.fromHtml(
                    String.format(
                        it.getString(R.string.topads_dashboard_kali_hari_value), item.totalClicks
                    ), HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                txtDescription.text = HtmlCompat.fromHtml(
                    String.format(
                        it.getString(R.string.topads_dashboard_anggaran_harian_desc), item.count
                    ), HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                val list = item.groupList
                if (list.isNotEmpty()) {
                    val items = mutableListOf<String>()
                    items.add(list[0].groupName)

                    val restCount = list.size - 1
                    if (restCount > 0) {
                        items.add(
                            String.format(
                                it.getString(R.string.topads_dashboard_grup_iklan), restCount
                            )
                        )
                    }
                    anggarnHarianAdapter.addItems(items)
                }
            }
        }
    }

    private fun setRecommendationKataKunci(item: RecommendationStatistics.Statistics.Data.KeywordRecommendationStats) {
        with(binding.layoutRecommendasi.layoutkataKunci) {
            context?.resources?.let {
                layoutRoundedView.txtSubTitle.text = HtmlCompat.fromHtml(
                    String.format(
                        it.getString(R.string.topads_dashboard_n_grup_iklanmu), item.groupCount
                    ), HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                txtDescription.text = HtmlCompat.fromHtml(
                    String.format(
                        it.getString(R.string.topads_dashboard_kata_kunci_desc), item.groupCount
                    ), HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                kataKunciChipsRvAdapter.addItems(
                    item.topGroups,
                    it.getString(R.string.topads_dashboard_lihat_semua)
                )
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
                TopAdsCreditHistoryActivity.createInstance(
                    it,
                    isFromSelection,
                    (activity as? TopAdsDashboardActivity)?.datePickerIndex
                        ?: DATE_PICKER_DEFAULT_INDEX
                ),
                REQUEST_CODE_SET_AUTO_TOPUP
            )
        }
    }
}