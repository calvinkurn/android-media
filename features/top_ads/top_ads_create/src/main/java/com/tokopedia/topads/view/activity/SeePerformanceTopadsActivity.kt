package com.tokopedia.topads.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.constants.MpTopadsConst
import com.tokopedia.topads.create.R
import com.tokopedia.topads.create.databinding.TopadsCreateBottomsheetSeePerformanceBinding
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsAddCreditActivity
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.di.DaggerCreateAdsComponent
import com.tokopedia.topads.view.fragment.AdsPerformanceDateRangePickerBottomSheet
import com.tokopedia.topads.view.fragment.ListBottomSheet
import com.tokopedia.topads.view.model.SeePerformanceTopAdsViewModel
import com.tokopedia.topads.view.uimodel.ItemListUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Success
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Calendar
import javax.inject.Inject

private const val ROTATION_0 = 0f
private const val ROTATION_180 = 180f
private const val REQUEST_CODE = 99

class SeePerformanceTopadsActivity : AppCompatActivity(), HasComponent<CreateAdsComponent> {

    private lateinit var seePerformanceTopadsBottomSheet: BottomSheetUnify
    private var selectedDateFrom: Date = Date()
    private var selectedDateTo: Date = Date()
    private var customDate: String = ""
    private var productId: String = ""

    private lateinit var mainBottomSheetBinding: TopadsCreateBottomsheetSeePerformanceBinding

    @JvmField
    @Inject
    var factory: ViewModelProvider.Factory? = null

    private val seePerformanceTopAdsViewModel by lazy {
        if (factory == null) null
        else ViewModelProvider(this, factory!!).get(SeePerformanceTopAdsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_performance_topads)
        initInjector()
        productId = intent.data?.getQueryParameter(MpTopadsConst.PRODUCT_ID_PARAM).orEmpty()
        openMainBottomSheet()
        firstFetch()
//        showChooseDateBottomSheet()
//        showAdsPlacingFilterBottomSheet()
//        showDescriptionBottomSheet("Nama Produk","test")
//        showDescriptionBottomSheet("Kenapa iklanmu tidak tampil?",
//            "   •  Kredit TopAds tidak mencukupi\n   •  Anggaran Harian mencapai batas maksimal\n   •  Stok produk kosong\n   •  Toko sedang tutup\n   •  Biaya iklan di bawah batas minimum")
        //TODO put color on colors.xml using dms
//        showDescriptionBottomSheet(
//            "Performa tampil", getColoredSpanned("52%", "#00AA5B", "250.000", "140"))
//        showDescriptionBottomSheet(
//            "Nama grup iklan", "test")
//        openCalendar()

    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun openMainBottomSheet() {
        mainBottomSheetBinding =
            TopadsCreateBottomsheetSeePerformanceBinding.inflate(LayoutInflater.from(this))
        attachObservers()
        attachClickListeners()

        seePerformanceTopadsBottomSheet = BottomSheetUnify().apply {
            setupContent(mainBottomSheetBinding.root)
            setChild(mainBottomSheetBinding.root)
            isDragable = false
            isHideable = false
            showKnob = true
            clearContentPadding = true
            showCloseIcon = false
            isFullpage = false
            setTitle(this@SeePerformanceTopadsActivity.getString(R.string.your_product_ad_performance))
        }
        seePerformanceTopadsBottomSheet.show(supportFragmentManager, "tagFragment")

        seePerformanceTopadsBottomSheet.setCloseClickListener {
            finish()
        }

        seePerformanceTopadsBottomSheet.setOnDismissListener {
            finish()
        }

        mainBottomSheetBinding.includeTips.tipsDescription.text = HtmlCompat.fromHtml(
            getString(R.string.see_ads_performance_tips_description),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun attachObservers() {
        seePerformanceTopAdsViewModel?.topAdsGetProductManage?.observe(this) {
            if (it == null || it.data.adId.isNullOrEmpty()) {
                hideMainBottomSheetContent()
            } else {
                showMainBottomSheetContent()
                mainBottomSheetBinding.productImage.urlSrc = it.data.itemImage
                mainBottomSheetBinding.productName.text = it.data.itemName
            }
        }

        seePerformanceTopAdsViewModel?.topAdsDeposits?.observe(this) {
            when (it) {
                is Success -> {
                    mainBottomSheetBinding.creditAmount.text =
                        "Rp ${it.data.topadsDashboardDeposits.data.amount}"
                }
                else -> {}
            }
        }

        seePerformanceTopAdsViewModel?.productStatistics?.observe(this) {
            when (it) {
                is Success -> {
                    setProductStatistics(it.data.getDashboardProductStatistics.data[0])
                }
                else -> {}
            }
        }

        seePerformanceTopAdsViewModel?.adId?.observe(this) {
            getProductStatistics()
            seePerformanceTopAdsViewModel?.getGroupId()
        }

        seePerformanceTopAdsViewModel?.topAdsGetShopInfo?.observe(this) {
            if (it != null) {
                when (it.data.category) {
                    3 -> setManualAdsUser()
                    4 -> setAutoAdsUser()
                }
            }
        }

        seePerformanceTopAdsViewModel?.isSingleAds?.observe(this) {
            if(seePerformanceTopAdsViewModel?.topAdsGetShopInfo?.value?.data?.category == 3) {
                if (it) {
                    mainBottomSheetBinding.includeAdGroupManual.root.visibility = View.GONE
                    mainBottomSheetBinding.statisticsSep.visibility = View.GONE
                } else {
                    mainBottomSheetBinding.includeAdGroupManual.root.visibility = View.VISIBLE
                    mainBottomSheetBinding.statisticsSep.visibility = View.VISIBLE
                    seePerformanceTopAdsViewModel?.getGroupInfo()
                }
            }
        }

        seePerformanceTopAdsViewModel?.topAdsGetGroupInfo?.observe(this) {
            if (it != null && it.response?.errors.isNullOrEmpty()) {
                mainBottomSheetBinding.includeAdGroupManual.groupName.text =
                    it.response?.data?.get(0)?.groupName
                mainBottomSheetBinding.includeAdGroupManual.productsCount.text =
                    it.response?.data?.get(0)?.totalItem.toString()
                mainBottomSheetBinding.includeAdGroupManual.keywordsCount.text =
                    it.response?.data?.get(0)?.totalKeyword.toString()
                mainBottomSheetBinding.includeAdGroupManual.adCostSearch.text =
                    it.response?.data?.get(0)?.groupBidSetting?.productSearch.toString()
                mainBottomSheetBinding.includeAdGroupManual.adCostRecommend.text =
                    it.response?.data?.get(0)?.groupBidSetting?.productBrowse.toString()
                mainBottomSheetBinding.includeAdGroupManual.dailyBudget.text =
                    if (it.response?.data?.get(0)?.groupPriceDaily == 0f) getString(R.string.tidak_dibatasi) else "Rp ${
                        it.response?.data?.get(
                            0
                        )?.groupPriceDaily
                    }"
                if (it.response?.data?.get(0)?.groupPriceDaily != 0f) {
                    mainBottomSheetBinding.includeAdGroupManual.dailyBudgetDesc.text =
                        "Rp ${it.response?.data?.get(0)?.groupPriceDailySpentFmt} dari ${
                            it.response?.data?.get(0)?.groupPriceDaily
                        }"
                }
            }
        }

        seePerformanceTopAdsViewModel?.topAdsGetAutoAds?.observe(this) {
            if (it != null) {
                mainBottomSheetBinding.includeAdGroupAutomatic.adStatus.text =
                    when (it.data.status) {
                        100 -> "Tidak Aktif"
                        200, 300, 400 -> "Dalam Proses"
                        500 -> "Aktif"
                        600 -> "Tidak Tampil"
                        else -> ""
                    }
                mainBottomSheetBinding.includeAdGroupAutomatic.dailyBudget2.text =
                    "Rp ${it.data.dailyBudget}"
                mainBottomSheetBinding.includeAdGroupAutomatic.dailyBudgetDesc2.text =
                    "Rp ${it.data.dailyUsage} dari ${
                        it.data.dailyBudget
                    }"
            }
        }
    }

    private fun attachClickListeners() {
        mainBottomSheetBinding.errorCtaButton.setOnClickListener {
            showMainBottomSheetLoading()
            firstFetch()
        }

        mainBottomSheetBinding.includeTips.tips.setOnClickListener {
            mainBottomSheetBinding.includeTips.tipsGroup.visibility =
                if (mainBottomSheetBinding.includeTips.tipsGroup.visibility == View.VISIBLE) View.GONE else View.VISIBLE

            if (mainBottomSheetBinding.includeTips.tipsExpandArrow.rotation == ROTATION_0) {
                mainBottomSheetBinding.includeTips.tipsExpandArrow.animate()
                    .rotation(ROTATION_180).duration = 100L
            } else {
                mainBottomSheetBinding.includeTips.tipsExpandArrow.animate()
                    .rotation(ROTATION_0).duration = 100L
            }
        }

        mainBottomSheetBinding.includeAdGroupManual.lihatPengaturanGroupBtn.setOnClickListener {
            mainBottomSheetBinding.includeAdGroupManual.lihatPengaturanGroup.visibility =
                if (mainBottomSheetBinding.includeAdGroupManual.lihatPengaturanGroup.visibility == View.VISIBLE) View.GONE else View.VISIBLE

            if (mainBottomSheetBinding.includeAdGroupManual.groupDetailDropdown.rotation == ROTATION_0) {
                mainBottomSheetBinding.includeAdGroupManual.groupDetailDropdown.animate()
                    .rotation(ROTATION_180).duration = 100L
            } else {
                mainBottomSheetBinding.includeAdGroupManual.groupDetailDropdown.animate()
                    .rotation(ROTATION_0).duration = 100L
            }
        }

        mainBottomSheetBinding.includeAdGroupManual.manualBtnSubmit.setOnClickListener {
            val intent =
                RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS).apply {
                    putExtra(TopAdsDashboardConstant.TAB_POSITION, MpTopadsConst.CONST_2)
                    putExtra(
                        TopAdsDashboardConstant.GROUPID,
                        seePerformanceTopAdsViewModel?.groupId?.value
                    )
                    putExtra(TopAdsDashboardConstant.GROUP_STRATEGY, "see_ads_performance")
                }
            startActivity(intent)
            finish()
        }

        mainBottomSheetBinding.includeAdGroupAutomatic.automaticBtnSubmit.setOnClickListener {
            val intent = RouteManager.getIntent(this,
                ApplinkConstInternalTopAds.TOPADS_EDIT_AUTOADS)
            startActivity(intent)
            finish()
        }

        mainBottomSheetBinding.addCredit.setOnClickListener {
            val intent = Intent(this, TopAdsAddCreditActivity::class.java)
            intent.putExtra(TopAdsAddCreditActivity.SHOW_FULL_SCREEN_BOTTOM_SHEET, true)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    private fun hideMainBottomSheetContent() {
        mainBottomSheetBinding.mainLoader.visibility = View.GONE
        mainBottomSheetBinding.errorText.visibility = View.VISIBLE
        mainBottomSheetBinding.errorCtaButton.visibility = View.VISIBLE
        mainBottomSheetBinding.mainBottomSheet.visibility = View.GONE
    }

    private fun showMainBottomSheetContent() {
        mainBottomSheetBinding.mainLoader.visibility = View.GONE
        mainBottomSheetBinding.errorText.visibility = View.GONE
        mainBottomSheetBinding.errorCtaButton.visibility = View.GONE
        mainBottomSheetBinding.mainBottomSheet.visibility = View.VISIBLE
    }

    private fun showMainBottomSheetLoading() {
        mainBottomSheetBinding.mainLoader.visibility = View.VISIBLE
        mainBottomSheetBinding.errorText.visibility = View.GONE
        mainBottomSheetBinding.errorCtaButton.visibility = View.GONE
        mainBottomSheetBinding.mainBottomSheet.visibility = View.GONE
    }

    private fun firstFetch() {
        seePerformanceTopAdsViewModel?.getProductManage(productId)
        seePerformanceTopAdsViewModel?.getTopAdsDeposit()
        seePerformanceTopAdsViewModel?.getShopInfo()
    }

    private fun setAutoAdsUser() {
        seePerformanceTopAdsViewModel?.getAutoAdsInfo()
        mainBottomSheetBinding.includeAdGroupAutomatic.root.visibility = View.VISIBLE
        mainBottomSheetBinding.includeAdGroupManual.root.visibility = View.GONE
        mainBottomSheetBinding.includeTips.root.visibility = View.GONE
    }

    private fun setManualAdsUser() {
        mainBottomSheetBinding.includeAdGroupAutomatic.root.visibility = View.GONE
        mainBottomSheetBinding.includeAdGroupManual.root.visibility = View.VISIBLE
        mainBottomSheetBinding.includeTips.root.visibility = View.VISIBLE
    }

    private fun getColoredSpanned(
        text: String,
        color: String,
        multiply: String,
        other: String
    ): Spanned {
        return MethodChecker.fromHtml("<strong><b><big><big><font color=$color>$text </font></big> <font color=#212121><big>teratas</font></big></big></b></strong> <br>${multiply}x teratas dari $other total tampil")
    }

    private fun getProductStatistics() {
        var startDate = "2023-01-01"
        var endDate = "2023-03-14"
        seePerformanceTopAdsViewModel?.getTopAdsProductStatistics(
            this.resources,
            startDate ?: "",
            endDate ?: "",
            1
        )
    }

    private fun setProductStatistics(dataItem: WithoutGroupDataItem) {
        mainBottomSheetBinding.includeCardStatistics.tampilCount.text = dataItem.statTotalImpression
        mainBottomSheetBinding.includeCardStatistics.klikCount.text = dataItem.statTotalClick
        mainBottomSheetBinding.includeCardStatistics.totalTerjualCount.text = dataItem.statTotalSold
        mainBottomSheetBinding.includeCardStatistics.pendapatanCount.text =
            dataItem.statTotalGrossProfit
        mainBottomSheetBinding.includeCardStatistics.pengeluaranCount.text = dataItem.statTotalSpent
        mainBottomSheetBinding.includeCardStatistics.efektivitasIklanCount.text =
            dataItem.statTotalRoas
    }

    private fun setupContent(content: View?) {
        setGreyCondition(content)
    }

    private fun setGreenCondition(content: View?) {
        content?.findViewById<ImageView>(R.id.block)?.setColorFilter(Color.parseColor("#00AA5B"))
        content?.findViewById<ImageView>(R.id.block_2)?.setColorFilter(Color.parseColor("#00AA5B"))
        content?.findViewById<ImageView>(R.id.block_3)?.setColorFilter(Color.parseColor("#00AA5B"))
    }


    private fun setYellowCondition(content: View?) {
        content?.findViewById<ImageView>(R.id.block)?.setColorFilter(Color.parseColor("#FF7F17"))
        content?.findViewById<ImageView>(R.id.block_2)?.setColorFilter(Color.parseColor("#FF7F17"))
        content?.findViewById<ImageView>(R.id.block_3)?.setColorFilter(Color.parseColor("#BFC9D9"))
    }


    private fun setRedCondition(content: View?) {
        content?.findViewById<ImageView>(R.id.block)?.setColorFilter(Color.parseColor("#F94D63"))
        content?.findViewById<ImageView>(R.id.block_2)?.setColorFilter(Color.parseColor("#BFC9D9"))
        content?.findViewById<ImageView>(R.id.block_3)?.setColorFilter(Color.parseColor("#BFC9D9"))
    }


    private fun setGreyCondition(content: View?) {
        content?.findViewById<ImageView>(R.id.block)?.setColorFilter(Color.parseColor("#BFC9D9"))
        content?.findViewById<ImageView>(R.id.block_2)?.setColorFilter(Color.parseColor("#BFC9D9"))
        content?.findViewById<ImageView>(R.id.block_3)?.setColorFilter(Color.parseColor("#BFC9D9"))
    }

    private fun showDescriptionBottomSheet(title: String, description: CharSequence) {
        val contentView =
            View.inflate(this, R.layout.bottomsheet_product_name_see_performance, null)
        val descriptionView = contentView.findViewById<Typography>(R.id.description)
        descriptionView.text = description
        val bottomSheet = BottomSheetUnify().apply {
            setChild(contentView)
            isDragable = true
            isHideable = false
            clearContentPadding = true
            showCloseIcon = true
            setTitle(title)
        }

        bottomSheet.show(supportFragmentManager, "tagFragment")
    }


    private fun showChooseDateBottomSheet() {
        val today = getDaysAgo(0)
        val yesterday = getDaysAgo(1)
        val daysAgo3 = getDaysAgo(3)
        val daysAgo7 = getDaysAgo(7)
        val daysAgo30 = getDaysAgo(30)
        val firstDateOfMonth = getFirstDateOfMonth()

        val temporaryData = arrayListOf(
            ItemListUiModel("Hari ini", today),
            ItemListUiModel("Kemarin", yesterday),
            ItemListUiModel("3 hari terakhir", "$daysAgo3 - $today"),
            ItemListUiModel("7 hari terakhir", "$daysAgo7 - $today"),
            ItemListUiModel("30 hari terakhir", "$daysAgo30 - $today"),
            ItemListUiModel("Bulan ini", "$firstDateOfMonth - $today"),
            ItemListUiModel("Custom", getRangeCustomDate()),
        )
        ListBottomSheet.show(supportFragmentManager, "Pilih rentang waktu", temporaryData)
    }

    private fun getRangeCustomDate(): String {
        if (customDate.isBlank()) {
            return DEFAULT_CUSTOM_DATE_PLACEHOLDER
        }
        return "custom"
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDaysAgo(daysAgo: Int): String {
        val cal = Calendar.getInstance()
        val dateFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        cal.add(Calendar.DATE, -daysAgo)
        return dateFormat.format(cal.time)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getFirstDateOfMonth(): String {
        val cal = Calendar.getInstance()
        val dateFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        val days = cal.getActualMinimum(Calendar.DAY_OF_MONTH)
        cal.set(Calendar.DAY_OF_MONTH, days);
        return dateFormat.format(cal.time)
    }

    private fun showAdsPlacingFilterBottomSheet() {
        val temporaryData = arrayListOf(
            ItemListUiModel(
                "Semua Penempatan",
                "Iklan produk yang tampil di halaman pencarian dan rekomendasi."
            ),
            ItemListUiModel("Di Pencarian", "Iklan produk yang tampil di halaman pencarian"),
            ItemListUiModel("Di Rekomendasi", "Iklan produk yang tampil di bagian rekomendasi"),

            )
        ListBottomSheet.show(supportFragmentManager, "Penempatan Iklan", temporaryData)
    }

    fun openCalendar() {
        AdsPerformanceDateRangePickerBottomSheet.getInstanceRange(
            selectedDateFrom,
            selectedDateTo,
            AdsPerformanceDateRangePickerBottomSheet.MAX_RANGE_90
        ).show(supportFragmentManager, "TAG")
    }

    companion object {
        var DEFAULT_CUSTOM_DATE_PLACEHOLDER = "Pilih tanggal"
    }

    override fun getComponent(): CreateAdsComponent {
        return DaggerCreateAdsComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent
        ).build()
    }
}
