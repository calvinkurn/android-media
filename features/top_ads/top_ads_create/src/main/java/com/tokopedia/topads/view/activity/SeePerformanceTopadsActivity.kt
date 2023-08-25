package com.tokopedia.topads.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CURRENT_SITE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.DATE_FORMAT_DD_MMM_YYYY
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.REQUEST_DATE_FORMAT
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.STATUS_IKLAN_ACTION_ACTIVATE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.STATUS_IKLAN_ACTION_DEACTIVATE
import com.tokopedia.topads.common.data.response.nongroupItem.ProductStatisticsResponse
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.common.data.util.Utils.convertToCurrency
import com.tokopedia.topads.constants.MpTopadsConst
import com.tokopedia.topads.create.R
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.topads.common.R as topadscommonR
import com.tokopedia.iconunify.R as iconunifyR
import com.tokopedia.topads.create.databinding.BottomsheetProductNameSeePerformanceBinding
import com.tokopedia.topads.create.databinding.TopadsCreateBottomsheetSeePerformanceBinding
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.utils.Utils.convertMoneyToValue
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsAddCreditActivity
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsCreditTopUpActivity
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.di.DaggerCreateAdsComponent
import com.tokopedia.topads.trackers.SeePerformanceTopadsTracker
import com.tokopedia.topads.view.fragment.AdsPerformanceDateRangePickerBottomSheet
import com.tokopedia.topads.view.fragment.ListBottomSheet
import com.tokopedia.topads.view.model.SeePerformanceTopAdsViewModel
import com.tokopedia.topads.view.uimodel.ItemListUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.htmltags.HtmlUtil
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class SeePerformanceTopadsActivity : AppCompatActivity(), HasComponent<CreateAdsComponent> {

    private var seePerformanceTopadsBottomSheet: BottomSheetUnify? = null
    private var selectedDateFrom: Date? = null
    private var selectedDateTo: Date? = null
    private var productId: String = ""
    private var currentSite: String = ""
    private var dateFilterType: Int = DATE_FILTER_TYPE_LAST_3_DAYS
    private var startDate: String = getDaysAgo(
        3,
        REQUEST_DATE_FORMAT
    ) // Default date filter type is - DATE_FILTER_TYPE_LAST_3_DAYS
    private var endDate: String = getDaysAgo(
        0,
        REQUEST_DATE_FORMAT
    ) // Default date filter type is - DATE_FILTER_TYPE_LAST_3_DAYS

    private val mainBottomSheetBinding by lazy {
        TopadsCreateBottomsheetSeePerformanceBinding.inflate(LayoutInflater.from(this))
    }

    @JvmField
    @Inject
    var factory: ViewModelProvider.Factory? = null

    private val seePerformanceTopAdsViewModel by lazy {
        if (factory == null) {
            null
        } else {
            ViewModelProvider(this, factory!!).get(SeePerformanceTopAdsViewModel::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_performance_topads)
        initInjector()
        productId = intent.data?.getQueryParameter(MpTopadsConst.PRODUCT_ID_PARAM).orEmpty()
        currentSite = intent.data?.getQueryParameter(CURRENT_SITE).orEmpty()
        openMainBottomSheet()
        firstFetch()
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun openMainBottomSheet() {
        attachObservers()
        attachClickListeners()
        mainBottomSheetBinding.dateFilter.chipText = String.format(
            getString(topadscommonR.string.topads_common_date_x_last_days),
            3
        ) // Default date filter type is - DATE_FILTER_TYPE_LAST_3_DAYS

        seePerformanceTopadsBottomSheet = BottomSheetUnify().apply {
            setChild(mainBottomSheetBinding.root)
            isDragable = false
            isHideable = false
            showKnob = true
            clearContentPadding = true
            showCloseIcon = false
            isFullpage = false
            setTitle(this@SeePerformanceTopadsActivity.getString(R.string.your_product_ad_performance))
        }
        seePerformanceTopadsBottomSheet?.show(supportFragmentManager, "mainBottomSheet")

        seePerformanceTopadsBottomSheet?.setOnDismissListener {
            finish()
        }

        mainBottomSheetBinding.dateFilter.apply {
            chip_right_icon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    iconunifyR.drawable.iconunify_chevron_down
                )
            )
            // called the listener just to show the right icon of chip
            setChevronClickListener {}
        }

        mainBottomSheetBinding.includeTips.tipsDescription.text = HtmlUtil.fromHtml(
            getString(R.string.topads_ads_performance_tips_description)
        ).trim()
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
                    mainBottomSheetBinding.includeTambahKredit.creditsLoader.visibility =
                        View.INVISIBLE
                    mainBottomSheetBinding.includeTambahKredit.creditAmount.visibility =
                        View.VISIBLE
                    mainBottomSheetBinding.includeTambahKredit.btnRefreshCredits.visibility =
                        View.VISIBLE
                    mainBottomSheetBinding.includeTambahKredit.creditAmount.text =
                        it.data.topadsDashboardDeposits.data.amountFmt.replace(" ", "")
                }
                else -> {}
            }
        }

        seePerformanceTopAdsViewModel?.productStatistics?.observe(this) {
            when (it) {
                is Success -> {
                    setProductStatistics(it.data.getDashboardProductStatistics.data.firstOrNull())
                    setPerformaTampil(it.data.getDashboardProductStatistics.data.first())
                }
                else -> {}
            }
        }

        seePerformanceTopAdsViewModel?.adId?.observe(this) {
            getProductStatistics(
                seePerformanceTopAdsViewModel?.goalId?.value ?: ADS_PLACEMENT_FILTER_TYPE_ALL
            )
            seePerformanceTopAdsViewModel?.getPromoInfo()
        }

        seePerformanceTopAdsViewModel?.topAdsPromoInfo?.observe(this) {
            when (it.topAdsGetPromo.data.first().status) {
                TOPADS_ACTIVE_AND_VISIBLE -> {
                    ImageViewCompat.setImageTintList(
                        mainBottomSheetBinding.includeStatusIklan.adStatusDot,
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this,
                                unifyprinciplesR.color.Unify_GN500
                            )
                        )
                    )
                    mainBottomSheetBinding.includeStatusIklan.manualAdStatus.text =
                        getString(R.string.ads_active)
                    mainBottomSheetBinding.includeStatusIklan.adStatusDesc.visibility =
                        View.INVISIBLE
                    mainBottomSheetBinding.includeStatusIklan.adStatusInfoBtn.visibility =
                        View.INVISIBLE
                }
                TOPADS_ACTIVE_BUT_NOT_VISIBLE -> {
                    ImageViewCompat.setImageTintList(
                        mainBottomSheetBinding.includeStatusIklan.adStatusDot,
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this,
                                unifyprinciplesR.color.Unify_YN300
                            )
                        )
                    )
                    mainBottomSheetBinding.includeStatusIklan.manualAdStatus.text =
                        getString(R.string.ads_active)
                    mainBottomSheetBinding.includeStatusIklan.adStatusDesc.text =
                        getString(R.string.ads_not_delivered)
                    mainBottomSheetBinding.includeStatusIklan.adStatusDesc.visibility = View.VISIBLE
                    mainBottomSheetBinding.includeStatusIklan.adStatusInfoBtn.visibility =
                        View.VISIBLE
                }
                TOPADS_NOT_ACTIVE -> {
                    ImageViewCompat.setImageTintList(
                        mainBottomSheetBinding.includeStatusIklan.adStatusDot,
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this,
                                unifyprinciplesR.color.Unify_NN500
                            )
                        )
                    )
                    mainBottomSheetBinding.includeStatusIklan.manualAdStatus.text =
                        getString(R.string.topads_non_active)
                    mainBottomSheetBinding.includeStatusIklan.adStatusDesc.visibility =
                        View.INVISIBLE
                    mainBottomSheetBinding.includeStatusIklan.adStatusInfoBtn.visibility =
                        View.INVISIBLE
                }
            }
            seePerformanceTopAdsViewModel?.getGroupInfo()
            mainBottomSheetBinding.includeStatusIklan.statusIklanLoader.visibility = View.INVISIBLE
            mainBottomSheetBinding.includeStatusIklan.statusIklanGroup.visibility = View.VISIBLE
        }

        seePerformanceTopAdsViewModel?.topAdsGetShopInfo?.observe(this) {
            if (it != null) {
                when (it.data.category) {
                    MANUAL_ADS_USER -> setManualAdsUser()
                    AUTO_ADS_USER -> setAutoAdsUser()
                }
            }
        }

        seePerformanceTopAdsViewModel?.isSingleAds?.observe(this) {
            if (seePerformanceTopAdsViewModel?.topAdsGetShopInfo?.value?.data?.category == MANUAL_ADS_USER) {
                if (it) {
                    mainBottomSheetBinding.includeAdGroupManual.root.visibility = View.GONE
                    mainBottomSheetBinding.statisticsSep.visibility = View.GONE
                } else {
                    mainBottomSheetBinding.includeAdGroupManual.root.visibility = View.VISIBLE
                    mainBottomSheetBinding.statisticsSep.visibility = View.VISIBLE
                }
            }
        }

        seePerformanceTopAdsViewModel?.topAdsGetGroupInfo?.observe(this) {
            if (it != null && it.response?.errors.isNullOrEmpty()) {
                mainBottomSheetBinding.includeAdGroupManual.groupName.text =
                    it.response?.data?.firstOrNull()?.groupName
                mainBottomSheetBinding.includeAdGroupManual.productsCount.text =
                    it.response?.data?.firstOrNull()?.totalItem.toString()
                mainBottomSheetBinding.includeAdGroupManual.keywordsCount.text =
                    it.response?.data?.firstOrNull()?.totalKeyword.toString()
                mainBottomSheetBinding.includeAdGroupManual.adCostSearch.text =
                    String.format(
                        getString(R.string.topads_ads_price_format_1),
                        convertToCurrency(
                            it.response?.data?.firstOrNull()?.groupBidSetting?.productSearch?.toLong()
                                ?: 0
                        )
                    )
                mainBottomSheetBinding.includeAdGroupManual.adCostRecommend.text =
                    String.format(
                        getString(R.string.topads_ads_price_format_1),
                        convertToCurrency(
                            it.response?.data?.firstOrNull()?.groupBidSetting?.productBrowse?.toLong()
                                ?: 0
                        )
                    )

                it.response?.data?.firstOrNull()?.groupPriceDaily?.apply {
                    when (this) {
                        INFINITE_DAILY_BUDGET.toFloat() -> {
                            mainBottomSheetBinding.includeAdGroupManual.dailyBudget.text =
                                getString(R.string.tidak_dibatasi)
                            mainBottomSheetBinding.includeAdGroupManual.dailyBudgetProgressBar.visibility =
                                View.GONE
                        }
                        else -> {
                            mainBottomSheetBinding.includeAdGroupManual.dailyBudget.text =
                                String.format(
                                    getString(R.string.topads_ads_price_format_1),
                                    convertToCurrency(
                                        it.response?.data?.firstOrNull()?.groupPriceDaily?.toLong()
                                            ?: 0
                                    )
                                )
                            mainBottomSheetBinding.includeAdGroupManual.dailyBudgetDesc.text =
                                String.format(
                                    getString(R.string.topads_ads_price_format_2),
                                    it.response?.data?.firstOrNull()?.groupPriceDailySpentFmt?.replace(
                                        " ",
                                        ""
                                    ),
                                    convertToCurrency(
                                        it.response?.data?.firstOrNull()?.groupPriceDaily?.toLong()
                                            ?: 0
                                    )
                                )

                            /** ProgressBar unify needs to be updated when rendered & visible on screen */
                            val dailySpentPercent = (100 * convertMoneyToValue(
                                it.response?.data?.firstOrNull()?.groupPriceDailySpentFmt ?: ""
                            ) / this).toInt()
                            val colorArray: IntArray = when {
                                dailySpentPercent > DAILY_BUDGET_SPENT_CRITICAL_THREASHOLD -> intArrayOf(
                                    ContextCompat.getColor(
                                        this@SeePerformanceTopadsActivity,
                                        unifyprinciplesR.color.Unify_RN500
                                    ),
                                    ContextCompat.getColor(
                                        this@SeePerformanceTopadsActivity,
                                        unifyprinciplesR.color.Unify_RN500
                                    )
                                )
                                dailySpentPercent > DAILY_BUDGET_SPENT_MODERATE_THRESHOLD -> intArrayOf(
                                    ContextCompat.getColor(
                                        this@SeePerformanceTopadsActivity,
                                        unifyprinciplesR.color.Unify_YN300
                                    ),
                                    ContextCompat.getColor(
                                        this@SeePerformanceTopadsActivity,
                                        unifyprinciplesR.color.Unify_YN300
                                    )
                                )
                                else -> intArrayOf(
                                    ContextCompat.getColor(
                                        this@SeePerformanceTopadsActivity,
                                        unifyprinciplesR.color.Unify_GN500
                                    ),
                                    ContextCompat.getColor(
                                        this@SeePerformanceTopadsActivity,
                                        unifyprinciplesR.color.Unify_GN500
                                    )
                                )
                            }
                            mainBottomSheetBinding.includeAdGroupManual.dailyBudgetProgressBar.progressBarColor =
                                colorArray
                            mainBottomSheetBinding.includeAdGroupManual.dailyBudgetProgressBar.setValue(
                                dailySpentPercent
                            )
                            mainBottomSheetBinding.includeAdGroupManual.dailyBudgetProgressBar.progressDrawable.cornerRadius =
                                unifyprinciplesR.dimen.abc_progress_bar_height_material.toPx()
                                    .toFloat()
                            mainBottomSheetBinding.includeAdGroupManual.dailyBudgetProgressBar.trackDrawable.cornerRadius =
                                unifyprinciplesR.dimen.abc_progress_bar_height_material.toPx()
                                    .toFloat()
                            mainBottomSheetBinding.includeAdGroupManual.dailyBudgetProgressBar.visibility =
                                View.GONE
                        }
                    }
                }
            }
        }

        seePerformanceTopAdsViewModel?.topAdsGetAutoAds?.observe(this) {
            if (it != null) {
                mainBottomSheetBinding.includeAdGroupAutomatic.adStatus.text =
                    when (it.data.status) {
                        AUTO_ADS_INACTIVE -> {
                            ImageViewCompat.setImageTintList(
                                mainBottomSheetBinding.includeAdGroupAutomatic.adStatusIndicator,
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        this,
                                        unifyprinciplesR.color.Unify_NN500
                                    )
                                )
                            )
                            getString(R.string.ads_deactive)
                        }
                        AUTO_ADS_IN_THE_PROCESS_TYPE_1, AUTO_ADS_IN_THE_PROCESS_TYPE_2, AUTO_ADS_IN_THE_PROCESS_TYPE_3 -> {
                            ImageViewCompat.setImageTintList(
                                mainBottomSheetBinding.includeAdGroupAutomatic.adStatusIndicator,
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        this,
                                        unifyprinciplesR.color.Unify_BN500
                                    )
                                )
                            )
                            getString(R.string.ads_inprogress)
                        }
                        AUTO_ADS_ACTIVE_AND_VISIBLE -> {
                            ImageViewCompat.setImageTintList(
                                mainBottomSheetBinding.includeAdGroupAutomatic.adStatusIndicator,
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        this,
                                        unifyprinciplesR.color.Unify_GN500
                                    )
                                )
                            )
                            getString(R.string.ads_active)
                        }
                        AUTO_ADS_ACTIVE_BUT_INVISIBLE -> {
                            mainBottomSheetBinding.includeAdGroupAutomatic.adStatusInfo.visibility =
                                View.VISIBLE
                            mainBottomSheetBinding.includeAdGroupAutomatic.adStatusInfoBtn.visibility =
                                View.VISIBLE
                            ImageViewCompat.setImageTintList(
                                mainBottomSheetBinding.includeAdGroupAutomatic.adStatusIndicator,
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        this,
                                        unifyprinciplesR.color.Unify_YN300
                                    )
                                )
                            )
                            getString(R.string.ads_active)
                        }
                        else -> ""
                    }
                mainBottomSheetBinding.includeAdGroupAutomatic.dailyBudget2.text =
                    String.format(
                        getString(R.string.topads_ads_price_format_3),
                        convertToCurrency(it.data.dailyBudget.toLong())
                    )
                mainBottomSheetBinding.includeAdGroupAutomatic.dailyBudgetDesc2.text =
                    String.format(
                        getString(R.string.topads_ads_price_format_4),
                        convertToCurrency(it.data.dailyUsage.toLong()),
                        convertToCurrency(it.data.dailyBudget.toLong())
                    )
                if (it.data.dailyBudget.isMoreThanZero()) {
                    mainBottomSheetBinding.includeAdGroupAutomatic.dailyBudgetProgressBar.setValue(
                        100 * it.data.dailyUsage / it.data.dailyBudget
                    )
                } else {
                    mainBottomSheetBinding.includeAdGroupAutomatic.dailyBudgetProgressBar.setValue(
                        EMPTY
                    )
                }
            }
        }

        seePerformanceTopAdsViewModel?.goalId?.observe(this) {
            mainBottomSheetBinding.adPlacementFilter.chipText = when (it) {
                ADS_PLACEMENT_FILTER_TYPE_ALL -> getString(R.string.topads_ads_performance_all_placements_filter_title)
                ADS_PLACEMENT_FILTER_TYPE_IN_SEARCH -> getString(R.string.topads_ads_performance_in_search_filter_title)
                ADS_PLACEMENT_FILTER_TYPE_IN_RECOMMENDATION -> getString(R.string.topads_ads_performance_in_recommendation_filter_title)
                else -> ""
            }
        }
    }

    private fun attachClickListeners() {
        mainBottomSheetBinding.errorCtaButton.setOnClickListener {
            SeePerformanceTopadsTracker.clickMuatUlang(currentSite)
            showMainBottomSheetLoading()
            firstFetch()
        }

        mainBottomSheetBinding.dateFilter.setOnClickListener {
            SeePerformanceTopadsTracker.clickDateRange(currentSite)
            showChooseDateBottomSheet()
        }

        mainBottomSheetBinding.adPlacementFilter.setOnClickListener {
            SeePerformanceTopadsTracker.clickPenempatanIklan(currentSite)
            showAdsPlacingFilterBottomSheet()
        }

        mainBottomSheetBinding.productName.setOnClickListener {
            // if product name has more than 1 line
            if (mainBottomSheetBinding.productName.layout.getEllipsisCount(1) > EMPTY) {
                showDescriptionBottomSheet(
                    getString(R.string.topads_create_group_name),
                    "",
                    "",
                    seePerformanceTopAdsViewModel?.topAdsGetProductManage?.value?.data?.itemName
                        ?: ""
                )
            }
        }

        mainBottomSheetBinding.includeStatusIklan.adStatusDropdown.setOnClickListener {
            SeePerformanceTopadsTracker.clickStatusIklan(currentSite)
            showStatusIklanBottomSheet()
        }

        mainBottomSheetBinding.includeStatusIklan.adStatusInfoBtn.setOnClickListener {
            showDescriptionBottomSheet(
                getString(R.string.topads_ads_performance_status_info_title),
                "",
                "",
                HtmlCompat.fromHtml(
                    getString(R.string.topads_ads_performance_status_info_description),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            )
        }

        mainBottomSheetBinding.includePerformaTampil.adPerformanceInfo.setOnClickListener {
            val statTotalTopSlotImpression =
                (seePerformanceTopAdsViewModel?.productStatistics?.value as? Success<ProductStatisticsResponse>)?.data?.getDashboardProductStatistics?.data?.firstOrNull()?.statTotalTopSlotImpression
            val statTotalImpression =
                (seePerformanceTopAdsViewModel?.productStatistics?.value as? Success<ProductStatisticsResponse>)?.data?.getDashboardProductStatistics?.data?.firstOrNull()?.statTotalImpression

            if (statTotalImpression == null || statTotalImpression == TOPADS_PERFORMANCE_NOT_RATED_THRESHOLD.toString()) {
                showDescriptionBottomSheet(
                    getString(R.string.topads_ads_performance_performa_tampil),
                    "",
                    getString(R.string.topads_ads_performance_not_rated),
                    "",
                    EMPTY,
                    unifyprinciplesR.color.Unify_NN600
                )
            } else {
                val performanceCountTemplate = "%.2f%%"
                val adPerformanceCount = 100 * convertMoneyToValue(
                    statTotalTopSlotImpression ?: ""
                ) / convertMoneyToValue(statTotalImpression ?: "").toFloat()
                when {
                    adPerformanceCount > TOPADS_PERFORMANCE_FREQUENTLY_THRESHOLD -> {
                        showDescriptionBottomSheet(
                            getString(R.string.topads_ads_performance_performa_tampil),
                            String.format(performanceCountTemplate, adPerformanceCount),
                            getString(R.string.topads_ads_performance_top_keyword),
                            String.format(
                                getString(R.string.topads_ads_performance_count),
                                statTotalTopSlotImpression,
                                statTotalImpression
                            ),
                            unifyprinciplesR.color.Unify_GN500
                        )
                    }
                    adPerformanceCount > TOPADS_PERFORMANCE_RARITY_THRESHOLD -> {
                        showDescriptionBottomSheet(
                            getString(R.string.topads_ads_performance_performa_tampil),
                            String.format(performanceCountTemplate, adPerformanceCount),
                            getString(R.string.topads_ads_performance_top_keyword),
                            String.format(
                                getString(R.string.topads_ads_performance_count),
                                statTotalTopSlotImpression,
                                statTotalImpression
                            ),
                            unifyprinciplesR.color.Unify_YN500
                        )
                    }
                    else -> {
                        showDescriptionBottomSheet(
                            getString(R.string.topads_ads_performance_performa_tampil),
                            String.format(performanceCountTemplate, adPerformanceCount),
                            getString(R.string.topads_ads_performance_top_keyword),
                            String.format(
                                getString(R.string.topads_ads_performance_count),
                                statTotalTopSlotImpression,
                                statTotalImpression
                            ),
                            unifyprinciplesR.color.Unify_RN500
                        )
                    }
                }
            }
        }

        mainBottomSheetBinding.includeTips.tips.setOnClickListener {
            SeePerformanceTopadsTracker.clickExpandTips(currentSite)
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

        mainBottomSheetBinding.includeTips.tipsViewMoreButton.setOnClickListener {
            SeePerformanceTopadsTracker.clickIklanLihatSelengkapnya(currentSite)
            RouteManager.route(this, ApplinkConstInternalGlobal.WEBVIEW, TIPS_VIEW_MORE)
        }

        mainBottomSheetBinding.includeAdGroupManual.groupName.setOnClickListener {
            // if group name is longer than 1 line
            if (mainBottomSheetBinding.includeAdGroupManual.groupName.layout.getEllipsisCount(0) > EMPTY) {
                showDescriptionBottomSheet(
                    getString(R.string.topads_create_group_name),
                    "",
                    "",
                    seePerformanceTopAdsViewModel?.topAdsGetGroupInfo?.value?.response?.data?.firstOrNull()?.groupName
                        ?: ""
                )
            }
        }

        mainBottomSheetBinding.includeAdGroupManual.lihatPengaturanGroupBtn.setOnClickListener {
            SeePerformanceTopadsTracker.clickExpandGroupSettings(currentSite)
            mainBottomSheetBinding.includeAdGroupManual.lihatPengaturanGroup.visibility =
                if (mainBottomSheetBinding.includeAdGroupManual.lihatPengaturanGroup.visibility == View.VISIBLE) View.GONE else View.VISIBLE

            seePerformanceTopAdsViewModel?.topAdsGetGroupInfo?.value?.response?.data?.firstOrNull()?.groupPriceDaily.let {
                if (it != INFINITE_DAILY_BUDGET.toFloat()) {
                    mainBottomSheetBinding.includeAdGroupManual.dailyBudgetProgressBar.visibility =
                        if (mainBottomSheetBinding.includeAdGroupManual.dailyBudgetProgressBar.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                }
            }

            if (mainBottomSheetBinding.includeAdGroupManual.groupDetailDropdown.rotation == ROTATION_0) {
                mainBottomSheetBinding.includeAdGroupManual.groupDetailDropdown.animate()
                    .rotation(ROTATION_180).duration = 100L
            } else {
                mainBottomSheetBinding.includeAdGroupManual.groupDetailDropdown.animate()
                    .rotation(ROTATION_0).duration = 100L
            }
        }

        mainBottomSheetBinding.includeAdGroupManual.manualBtnSubmit.setOnClickListener {
            SeePerformanceTopadsTracker.clickGroupIklan(currentSite)
            val intent =
                RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS).apply {
                    putExtra(TopAdsDashboardConstant.TAB_POSITION, MpTopadsConst.CONST_2)
                    putExtra(
                        TopAdsDashboardConstant.GROUPID,
                        seePerformanceTopAdsViewModel?.topAdsPromoInfo?.value?.topAdsGetPromo?.data?.firstOrNull()?.groupID
                            ?: ""
                    )
                    putExtra(TopAdsDashboardConstant.GROUP_STRATEGY, SEE_ADS_PERFORMANCE_TAG)
                }
            startActivity(intent)
            finish()
        }

        mainBottomSheetBinding.includeAdGroupAutomatic.adStatusInfoBtn.setOnClickListener {
            showDescriptionBottomSheet(
                getString(R.string.topads_ad_status),
                "",
                "",
                getString(R.string.topads_ads_performance_not_active_ads_automatic_info)
            )
        }

        mainBottomSheetBinding.includeAdGroupAutomatic.automaticBtnSubmit.setOnClickListener {
            SeePerformanceTopadsTracker.clickIklanOtomatis(currentSite)
            val intent = RouteManager.getIntent(
                this,
                ApplinkConstInternalTopAds.TOPADS_EDIT_AUTOADS
            )
            startActivity(intent)
            finish()
        }

        mainBottomSheetBinding.includeTambahKredit.btnRefreshCredits.setOnClickListener {
            SeePerformanceTopadsTracker.clickCreditTopads(currentSite)
            mainBottomSheetBinding.includeTambahKredit.creditsLoader.visibility = View.VISIBLE
            mainBottomSheetBinding.includeTambahKredit.creditAmount.visibility = View.INVISIBLE
            mainBottomSheetBinding.includeTambahKredit.btnRefreshCredits.visibility = View.INVISIBLE
            seePerformanceTopAdsViewModel?.getTopAdsDeposit()
        }

        mainBottomSheetBinding.includeTambahKredit.addCredit.setOnClickListener {
            SeePerformanceTopadsTracker.clickTambahKredit(currentSite)
            openNewAutoTopUpBottomSheet()
            SeePerformanceTopadsTracker.viewKreditPage(currentSite)
        }
    }

    private fun openNewAutoTopUpBottomSheet() {
        val intent = Intent(this, TopAdsCreditTopUpActivity::class.java)
        intent.putExtra(TopAdsCreditTopUpActivity.IS_AUTO_TOP_UP_ACTIVE, false)
        intent.putExtra(TopAdsCreditTopUpActivity.IS_AUTO_TOP_UP_SELECTED, false)
        intent.putExtra(TopAdsCreditTopUpActivity.CREDIT_PERFORMANCE, "")
        intent.putExtra(TopAdsCreditTopUpActivity.TOP_UP_COUNT, EMPTY)
        intent.putExtra(TopAdsCreditTopUpActivity.AUTO_TOP_UP_BONUS, EMPTY)
        startActivityForResult(intent, TopAdsDashboardConstant.REQUEST_CODE_TOP_UP_CREDIT)
    }

    private fun hideMainBottomSheetContent() {
        SeePerformanceTopadsTracker.viewErrorFetching(currentSite)
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
        mainBottomSheetBinding.adPlacementFilter.visibility = View.GONE
        mainBottomSheetBinding.includeStatusIklan.root.visibility = View.GONE
        mainBottomSheetBinding.includeAdGroupAutomatic.root.visibility = View.VISIBLE
        mainBottomSheetBinding.includeAdGroupManual.root.visibility = View.GONE
        mainBottomSheetBinding.includeTips.root.visibility = View.GONE
    }

    private fun setManualAdsUser() {
        mainBottomSheetBinding.adPlacementFilter.visibility = View.VISIBLE
        mainBottomSheetBinding.includeStatusIklan.root.visibility = View.VISIBLE
        mainBottomSheetBinding.includeAdGroupAutomatic.root.visibility = View.GONE
        mainBottomSheetBinding.includeAdGroupManual.root.visibility = View.VISIBLE
        mainBottomSheetBinding.includeTips.root.visibility = View.VISIBLE
    }

    private fun getProductStatistics(goalId: Int) {
        mainBottomSheetBinding.includeCardStatistics.productStatisticsGroup.visibility =
            View.INVISIBLE
        mainBottomSheetBinding.includeCardStatistics.productStatisticsLoaderGroup.visibility =
            View.VISIBLE

        seePerformanceTopAdsViewModel?.getTopAdsProductStatistics(
            this.resources,
            startDate,
            endDate,
            goalId
        )
    }

    /** title is used as key to check for date filter option user has opted for*/
    fun updateDateFilter(title: String) {
        when (title) {
            getString(topadscommonR.string.topads_common_date_today) -> {
                startDate = getDaysAgo(0, REQUEST_DATE_FORMAT)
                endDate = getDaysAgo(0, REQUEST_DATE_FORMAT)
                dateFilterType = DATE_FILTER_TYPE_TODAY
            }
            getString(topadscommonR.string.topads_common_date_yesterday) -> {
                endDate = getDaysAgo(1, REQUEST_DATE_FORMAT)
                startDate = getDaysAgo(1, REQUEST_DATE_FORMAT)
                dateFilterType = DATE_FILTER_TYPE_YESTERDAY
            }
            String.format(getString(topadscommonR.string.topads_common_date_x_last_days), 3) -> {
                endDate = getDaysAgo(0, REQUEST_DATE_FORMAT)
                startDate = getDaysAgo(3, REQUEST_DATE_FORMAT)
                dateFilterType = DATE_FILTER_TYPE_LAST_3_DAYS
            }
            String.format(getString(topadscommonR.string.topads_common_date_x_last_days), 7) -> {
                endDate = getDaysAgo(1, REQUEST_DATE_FORMAT)
                startDate = getDaysAgo(7, REQUEST_DATE_FORMAT)
                dateFilterType = DATE_FILTER_TYPE_WEEK
            }
            String.format(getString(topadscommonR.string.topads_common_date_x_last_days), 30) -> {
                endDate = getDaysAgo(1, REQUEST_DATE_FORMAT)
                startDate = getDaysAgo(30, REQUEST_DATE_FORMAT)
                dateFilterType = DATE_FILTER_TYPE_MONTH
            }
            getString(topadscommonR.string.topads_common_date_this_month) -> {
                endDate = getDaysAgo(0, REQUEST_DATE_FORMAT)
                startDate = getFirstDateOfMonth(REQUEST_DATE_FORMAT)
                dateFilterType = DATE_FILTER_TYPE_CURRENT_MONTH
            }
            else -> openCalendar()
        }
        if (title != getString(topadscommonR.string.topads_common_custom)) {
            mainBottomSheetBinding.dateFilter.chipText = title
            getProductStatistics(
                seePerformanceTopAdsViewModel?.goalId?.value ?: ADS_PLACEMENT_FILTER_TYPE_ALL
            )
            selectedDateFrom = null
            selectedDateTo = null
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun updateCustomDates(dateFrom: Date, dateTo: Date) {
        selectedDateFrom = dateFrom
        selectedDateTo = dateTo
        startDate = SimpleDateFormat(REQUEST_DATE_FORMAT).format(dateFrom)
        endDate = SimpleDateFormat(REQUEST_DATE_FORMAT).format(dateTo)
        dateFilterType = DATE_FILTER_TYPE_CUSTOM
        mainBottomSheetBinding.dateFilter.chipText =
            getString(topadscommonR.string.topads_common_custom)
        getProductStatistics(
            seePerformanceTopAdsViewModel?.goalId?.value ?: ADS_PLACEMENT_FILTER_TYPE_ALL
        )
    }

    private fun setProductStatistics(dataItem: WithoutGroupDataItem?) {
        mainBottomSheetBinding.includeCardStatistics.productStatisticsLoaderGroup.visibility =
            View.GONE
        mainBottomSheetBinding.includeCardStatistics.productStatisticsGroup.visibility =
            View.VISIBLE

        mainBottomSheetBinding.includeCardStatistics.tampilCount.text =
            dataItem?.statTotalImpression
        mainBottomSheetBinding.includeCardStatistics.klikCount.text = dataItem?.statTotalClick
        mainBottomSheetBinding.includeCardStatistics.totalTerjualCount.text =
            dataItem?.statTotalSold
        mainBottomSheetBinding.includeCardStatistics.pendapatanCount.text =
            dataItem?.statTotalGrossProfit?.replace(" ", "")
        mainBottomSheetBinding.includeCardStatistics.pengeluaranCount.text =
            dataItem?.statTotalSpent?.replace(" ", "")
        mainBottomSheetBinding.includeCardStatistics.efektivitasIklanCount.text =
            dataItem?.statTotalRoas
    }

    private fun setPerformaTampil(dataItem: WithoutGroupDataItem) {
        if (dataItem.statTotalImpression == TOPADS_PERFORMANCE_NOT_RATED_THRESHOLD.toString()) {
            mainBottomSheetBinding.includePerformaTampil.adPerformance.text =
                getString(R.string.topads_ads_performance_not_rated)
            setColorCondition(
                unifyprinciplesR.color.Unify_NN300,
                unifyprinciplesR.color.Unify_NN300,
                unifyprinciplesR.color.Unify_NN300
            )
        } else {
            val adPerformance =
                100 * convertMoneyToValue(dataItem.statTotalTopSlotImpression) / convertMoneyToValue(
                    dataItem.statTotalImpression
                )

            mainBottomSheetBinding.includePerformaTampil.adPerformance.text = when {
                adPerformance > TOPADS_PERFORMANCE_FREQUENTLY_THRESHOLD -> {
                    setColorCondition(
                        unifyprinciplesR.color.Unify_GN500,
                        unifyprinciplesR.color.Unify_GN500,
                        unifyprinciplesR.color.Unify_GN500
                    )
                    getString(R.string.topads_ads_performance_top_frequently)
                }
                adPerformance > TOPADS_PERFORMANCE_RARITY_THRESHOLD -> {
                    setColorCondition(
                        unifyprinciplesR.color.Unify_YN500,
                        unifyprinciplesR.color.Unify_YN500,
                        unifyprinciplesR.color.Unify_NN300
                    )
                    getString(R.string.topads_ads_performance_top_rarity)
                }
                else -> {
                    setColorCondition(
                        unifyprinciplesR.color.Unify_RN500,
                        unifyprinciplesR.color.Unify_NN300,
                        unifyprinciplesR.color.Unify_NN300
                    )
                    getString(R.string.topads_ads_performance_lose_competition)
                }
            }
        }
    }

    private fun setColorCondition(color1: Int, color2: Int, color3: Int) {
        ImageViewCompat.setImageTintList(
            mainBottomSheetBinding.includePerformaTampil.adsPerformanceIndicator.block1,
            ColorStateList.valueOf(ContextCompat.getColor(this, color1))
        )
        ImageViewCompat.setImageTintList(
            mainBottomSheetBinding.includePerformaTampil.adsPerformanceIndicator.block2,
            ColorStateList.valueOf(ContextCompat.getColor(this, color2))
        )
        ImageViewCompat.setImageTintList(
            mainBottomSheetBinding.includePerformaTampil.adsPerformanceIndicator.block3,
            ColorStateList.valueOf(ContextCompat.getColor(this, color3))
        )
    }

    private fun showDescriptionBottomSheet(
        title: String,
        heading1: String,
        heading2: String,
        description: CharSequence,
        heading1Color: Int = 0,
        heading2Color: Int = 0
    ) {
        val binding = BottomsheetProductNameSeePerformanceBinding.inflate(LayoutInflater.from(this))
        val bottomSheet = BottomSheetUnify().apply {
            setChild(binding.root)
            isDragable = false
            isHideable = true
            clearContentPadding = true
            showCloseIcon = true
            isFullpage = false
            setTitle(title)
        }
        if (!heading1.isNullOrEmpty()) {
            binding.heading1.visibility = View.VISIBLE
            binding.heading1.text = heading1
        }
        if (heading1Color > EMPTY) {
            binding.heading1.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this,
                        heading1Color
                    )
                )
            )
        }
        if (heading2Color > EMPTY) {
            binding.heading2.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this,
                        heading2Color
                    )
                )
            )
        }
        if (!heading2.isNullOrEmpty()) {
            binding.heading2.visibility = View.VISIBLE
            binding.heading2.text = heading2
        }
        if (!description.isNullOrEmpty()) {
            binding.description.visibility = View.VISIBLE
            binding.description.text = description
        }
        bottomSheet.show(supportFragmentManager, DESCRIPTION_BOTTOMSHEET_TAG)
    }

    private fun showStatusIklanBottomSheet() {
        val list = arrayListOf(
            ItemListUiModel(
                title = getString(R.string.ads_active),
                isSelected = seePerformanceTopAdsViewModel?.topAdsPromoInfo?.value?.topAdsGetPromo?.data?.firstOrNull()?.status != TOPADS_NOT_ACTIVE
            ),
            ItemListUiModel(
                title = getString(R.string.topads_non_active),
                isSelected = seePerformanceTopAdsViewModel?.topAdsPromoInfo?.value?.topAdsGetPromo?.data?.firstOrNull()?.status == TOPADS_NOT_ACTIVE
            )
        )
        ListBottomSheet.show(
            supportFragmentManager,
            getString(R.string.topads_ad_status),
            list
        )
    }

    fun updateStatusIklan(action: String) {
        mainBottomSheetBinding.includeStatusIklan.statusIklanLoader.visibility = View.VISIBLE
        mainBottomSheetBinding.includeStatusIklan.statusIklanGroup.visibility = View.INVISIBLE
        val updatedStatus = when (action) {
            TOPADS_NOT_ACTIVE -> {
                SeePerformanceTopadsTracker.clickStatusIklanTidakAktif(currentSite)
                STATUS_IKLAN_ACTION_DEACTIVATE
            }
            else -> {
                SeePerformanceTopadsTracker.clickStatusIklanAktif(currentSite)
                STATUS_IKLAN_ACTION_ACTIVATE
            }
        }
        seePerformanceTopAdsViewModel?.setProductAction(
            updatedStatus,
            arrayListOf(seePerformanceTopAdsViewModel?.adId?.value ?: ""),
            seePerformanceTopAdsViewModel?.topAdsPromoInfo?.value?.topAdsGetPromo?.data?.firstOrNull()?.groupID
        )
    }

    private fun showAdsPlacingFilterBottomSheet() {
        val adPlacementFilterList = arrayListOf(
            ItemListUiModel(
                getString(R.string.topads_ads_performance_all_placements_filter_title),
                getString(R.string.topads_ads_performance_all_placements_filter_desc),
                isSelected = seePerformanceTopAdsViewModel?.goalId?.value == ADS_PLACEMENT_FILTER_TYPE_ALL
            ),
            ItemListUiModel(
                getString(R.string.topads_ads_performance_in_search_filter_title),
                getString(R.string.topads_ads_performance_in_search_filter_desc),
                isSelected = seePerformanceTopAdsViewModel?.goalId?.value == ADS_PLACEMENT_FILTER_TYPE_IN_SEARCH
            ),
            ItemListUiModel(
                getString(R.string.topads_ads_performance_in_recommendation_filter_title),
                getString(R.string.topads_ads_performance_in_recommendation_filter_desc),
                isSelected = seePerformanceTopAdsViewModel?.goalId?.value == ADS_PLACEMENT_FILTER_TYPE_IN_RECOMMENDATION
            )
        )
        ListBottomSheet.show(
            supportFragmentManager,
            getString(R.string.topads_ads_performance_ad_placement),
            adPlacementFilterList
        )
    }

    fun updateAdsPlacingFilter(goalId: Int) {
        when (goalId) {
            ADS_PLACEMENT_FILTER_TYPE_ALL -> SeePerformanceTopadsTracker.clickIklanSemuaPenempatan(
                currentSite
            )
            ADS_PLACEMENT_FILTER_TYPE_IN_SEARCH -> SeePerformanceTopadsTracker.clickIklanPencarian(
                currentSite
            )
            ADS_PLACEMENT_FILTER_TYPE_IN_RECOMMENDATION -> SeePerformanceTopadsTracker.clickIklanRekomendasi(
                currentSite
            )
        }
        getProductStatistics(goalId)
    }

    @SuppressLint("SimpleDateFormat")
    private fun showChooseDateBottomSheet() {
        val today = getDaysAgo(0, DATE_FORMAT_DD_MMM_YYYY)
        val yesterday = getDaysAgo(1, DATE_FORMAT_DD_MMM_YYYY)
        val daysAgo3 = getDaysAgo(3, DATE_FORMAT_DD_MMM_YYYY)
        val daysAgo7 = getDaysAgo(7, DATE_FORMAT_DD_MMM_YYYY)
        val daysAgo30 = getDaysAgo(30, DATE_FORMAT_DD_MMM_YYYY)
        val firstDateOfMonth = getFirstDateOfMonth(DATE_FORMAT_DD_MMM_YYYY)

        val dateFilterList = arrayListOf(
            ItemListUiModel(
                getString(topadscommonR.string.topads_common_date_today),
                today,
                dateFilterType == DATE_FILTER_TYPE_TODAY
            ),
            ItemListUiModel(
                getString(topadscommonR.string.topads_common_date_yesterday),
                yesterday,
                dateFilterType == DATE_FILTER_TYPE_YESTERDAY
            ),
            ItemListUiModel(
                String.format(getString(topadscommonR.string.topads_common_date_x_last_days), 3),
                "$daysAgo3 - $today",
                dateFilterType == DATE_FILTER_TYPE_LAST_3_DAYS
            ),
            ItemListUiModel(
                String.format(getString(topadscommonR.string.topads_common_date_x_last_days), 7),
                "$daysAgo7 - ${getDaysAgo(1, DATE_FORMAT_DD_MMM_YYYY)}",
                dateFilterType == DATE_FILTER_TYPE_WEEK
            ),
            ItemListUiModel(
                String.format(getString(topadscommonR.string.topads_common_date_x_last_days), 30),
                "$daysAgo30 - ${getDaysAgo(1, DATE_FORMAT_DD_MMM_YYYY)}",
                dateFilterType == DATE_FILTER_TYPE_MONTH
            ),
            ItemListUiModel(
                getString(topadscommonR.string.topads_common_date_this_month),
                "$firstDateOfMonth - $today",
                dateFilterType == DATE_FILTER_TYPE_CURRENT_MONTH
            ),
            ItemListUiModel(
                getString(topadscommonR.string.topads_common_custom),
                if (dateFilterType == DATE_FILTER_TYPE_CUSTOM) {
                    "${
                        SimpleDateFormat(DATE_FORMAT_DD_MMM_YYYY).format(
                            selectedDateFrom
                        )
                    } - ${SimpleDateFormat(DATE_FORMAT_DD_MMM_YYYY).format(selectedDateTo)}"
                } else {
                    getString(
                        topadscommonR.string.topads_common_select_date
                    )
                },
                dateFilterType == DATE_FILTER_TYPE_CUSTOM
            )
        )
        ListBottomSheet.show(
            supportFragmentManager,
            getString(R.string.topads_ads_performance_choose_time_range),
            dateFilterList
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDaysAgo(daysAgo: Int, dateFormat: String): String {
        val cal = Calendar.getInstance()
        val dateFormat: DateFormat = SimpleDateFormat(dateFormat)
        cal.add(Calendar.DATE, -daysAgo)
        return dateFormat.format(cal.time)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getFirstDateOfMonth(dateFormat: String): String {
        val cal = Calendar.getInstance()
        val dateFormat: DateFormat = SimpleDateFormat(dateFormat)
        val days = cal.getActualMinimum(Calendar.DAY_OF_MONTH)
        cal.set(Calendar.DAY_OF_MONTH, days)
        return dateFormat.format(cal.time)
    }

    fun openCalendar() {
        AdsPerformanceDateRangePickerBottomSheet.getInstanceRange(
            selectedDateFrom,
            selectedDateTo,
            AdsPerformanceDateRangePickerBottomSheet.MAX_RANGE_90
        ).show(supportFragmentManager, CALENDAR_TAG)
    }

    override fun getComponent(): CreateAdsComponent {
        return DaggerCreateAdsComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent
        ).build()
    }

    companion object {
        private const val ROTATION_0 = 0f
        private const val ROTATION_180 = 180f
        private const val TIPS_VIEW_MORE =
            "https://seller.tokopedia.com/edu/cara-meningkatkan-persentase-klik-iklan-topads/"
        private const val DATE_FILTER_TYPE_TODAY = 1
        private const val DATE_FILTER_TYPE_YESTERDAY = 2
        private const val DATE_FILTER_TYPE_LAST_3_DAYS = 3
        private const val DATE_FILTER_TYPE_WEEK = 4
        private const val DATE_FILTER_TYPE_MONTH = 5
        private const val DATE_FILTER_TYPE_CURRENT_MONTH = 6
        private const val DATE_FILTER_TYPE_CUSTOM = 7
        private const val TOPADS_PERFORMANCE_FREQUENTLY_THRESHOLD = 20
        private const val TOPADS_PERFORMANCE_RARITY_THRESHOLD = 5
        private const val TOPADS_PERFORMANCE_NOT_RATED_THRESHOLD = 0
        private const val TOPADS_ACTIVE_AND_VISIBLE = "1"
        const val TOPADS_ACTIVE_BUT_NOT_VISIBLE = "2"
        const val TOPADS_NOT_ACTIVE = "3"
        private const val MANUAL_ADS_USER = 3
        private const val AUTO_ADS_USER = 4
        private const val INFINITE_DAILY_BUDGET = 0
        private const val DAILY_BUDGET_SPENT_MODERATE_THRESHOLD = 60
        private const val DAILY_BUDGET_SPENT_CRITICAL_THREASHOLD = 80
        private const val AUTO_ADS_INACTIVE = 100
        private const val AUTO_ADS_IN_THE_PROCESS_TYPE_1 = 200
        private const val AUTO_ADS_IN_THE_PROCESS_TYPE_2 = 300
        private const val AUTO_ADS_IN_THE_PROCESS_TYPE_3 = 400
        private const val AUTO_ADS_ACTIVE_AND_VISIBLE = 500
        private const val AUTO_ADS_ACTIVE_BUT_INVISIBLE = 600
        const val ADS_PLACEMENT_FILTER_TYPE_ALL = 1
        const val ADS_PLACEMENT_FILTER_TYPE_IN_SEARCH = 2
        const val ADS_PLACEMENT_FILTER_TYPE_IN_RECOMMENDATION = 3
        private const val SEE_ADS_PERFORMANCE_TAG = "see_ads_performance"
        private const val CALENDAR_TAG = "calendar"
        private const val DESCRIPTION_BOTTOMSHEET_TAG = "description_bottomSheet"
        private const val EMPTY = 0
    }
}
