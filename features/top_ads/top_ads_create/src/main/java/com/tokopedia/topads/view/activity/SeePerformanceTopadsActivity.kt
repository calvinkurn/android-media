package com.tokopedia.topads.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
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
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CURRENT_SITE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.DATE_FORMAT_DD_MMM_YYYY
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.REQUEST_DATE_FORMAT
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.STATUS_IKLAN_ACTION_ACTIVATE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.STATUS_IKLAN_ACTION_DEACTIVATE
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.common.data.util.Utils.convertToCurrency
import com.tokopedia.topads.constants.MpTopadsConst
import com.tokopedia.topads.create.R
import com.tokopedia.topads.create.databinding.BottomsheetProductNameSeePerformanceBinding
import com.tokopedia.topads.create.databinding.TopadsCreateBottomsheetSeePerformanceBinding
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.utils.Utils.convertMoneyToValue
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsAddCreditActivity
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.di.DaggerCreateAdsComponent
import com.tokopedia.topads.trackers.SeePerformanceTopadsTracker
import com.tokopedia.topads.view.fragment.AdsPerformanceDateRangePickerBottomSheet
import com.tokopedia.topads.view.fragment.ListBottomSheet
import com.tokopedia.topads.view.model.SeePerformanceTopAdsViewModel
import com.tokopedia.topads.view.uimodel.ItemListUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.htmltags.HtmlUtil
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

private const val ROTATION_0 = 0f
private const val ROTATION_180 = 180f
private const val REQUEST_CODE = 99
private const val TIPS_VIEW_MORE =
    "https://seller.tokopedia.com/edu/cara-meningkatkan-persentase-klik-iklan-topads/"

class SeePerformanceTopadsActivity : AppCompatActivity(), HasComponent<CreateAdsComponent> {

    private lateinit var seePerformanceTopadsBottomSheet: BottomSheetUnify
    private var selectedDateFrom: Date? = null
    private var selectedDateTo: Date? = null
    private var productId: String = ""
    private var currentSite: String = ""
    private var dateFilterType: Int = 3
    private var startDate: String = getDaysAgo(3, REQUEST_DATE_FORMAT)
    private var endDate: String = getDaysAgo(0, REQUEST_DATE_FORMAT)

    private lateinit var mainBottomSheetBinding: TopadsCreateBottomsheetSeePerformanceBinding

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
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun openMainBottomSheet() {
        mainBottomSheetBinding =
            TopadsCreateBottomsheetSeePerformanceBinding.inflate(LayoutInflater.from(this))
        attachObservers()
        attachClickListeners()
        mainBottomSheetBinding.dateFilter.chipText = String.format(getString(R.string.topads_common_date_x_last_days), 3) // default date filter is 3 Hari terakhir

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
        seePerformanceTopadsBottomSheet.show(supportFragmentManager, "mainBottomSheet")

        seePerformanceTopadsBottomSheet.setOnDismissListener {
            finish()
        }

        mainBottomSheetBinding.dateFilter.apply {
            chip_right_icon.setImageDrawable(ContextCompat.getDrawable(context,
            com.tokopedia.iconunify.R.drawable.iconunify_chevron_down))
            //called the listener just to show the right icon of chip
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
                    mainBottomSheetBinding.includeTambahKredit.creditAmount.text = HtmlCompat.fromHtml(
                        it.data.topadsDashboardDeposits.data.amountHtml.replace(" ",""),
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    ).trim()
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
            getProductStatistics(seePerformanceTopAdsViewModel?.goalId?.value ?: 1)
            seePerformanceTopAdsViewModel?.getPromoInfo()
        }

        seePerformanceTopAdsViewModel?.topAdsPromoInfo?.observe(this) {
            when (it.topAdsGetPromo.data.get(0).status) {
                "1" -> {
                    ImageViewCompat.setImageTintList(
                        mainBottomSheetBinding.includeStatusIklan.adStatusDot,
                        ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Unify_GN500))
                    )
                    mainBottomSheetBinding.includeStatusIklan.manualAdStatus.text =
                        getString(R.string.ads_active)
                    mainBottomSheetBinding.includeStatusIklan.adStatusDesc.visibility =
                        View.INVISIBLE
                    mainBottomSheetBinding.includeStatusIklan.adStatusInfoBtn.visibility =
                        View.INVISIBLE
                }
                "2" -> {
                    ImageViewCompat.setImageTintList(
                        mainBottomSheetBinding.includeStatusIklan.adStatusDot,
                        ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Unify_YN300))
                    )
                    mainBottomSheetBinding.includeStatusIklan.manualAdStatus.text =
                        getString(R.string.ads_active)
                    mainBottomSheetBinding.includeStatusIklan.adStatusDesc.text =
                        getString(R.string.topads_dash_tidak_tampil)
                    mainBottomSheetBinding.includeStatusIklan.adStatusDesc.visibility = View.VISIBLE
                    mainBottomSheetBinding.includeStatusIklan.adStatusInfoBtn.visibility =
                        View.VISIBLE
                }
                "3" -> {
                    ImageViewCompat.setImageTintList(
                        mainBottomSheetBinding.includeStatusIklan.adStatusDot,
                        ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Unify_NN500))
                    )
                    mainBottomSheetBinding.includeStatusIklan.manualAdStatus.text =
                        getString(R.string.topads_non_active)
                    mainBottomSheetBinding.includeStatusIklan.adStatusDesc.visibility =
                        View.INVISIBLE
                    mainBottomSheetBinding.includeStatusIklan.adStatusInfoBtn.visibility =
                        View.INVISIBLE
                }
            }
            mainBottomSheetBinding.includeStatusIklan.statusIklanLoader.visibility = View.INVISIBLE
            mainBottomSheetBinding.includeStatusIklan.statusIklanGroup.visibility = View.VISIBLE
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
            if (seePerformanceTopAdsViewModel?.topAdsGetShopInfo?.value?.data?.category == 3) {
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

                val adPerformance =
                    if (it.response?.data?.get(0)?.statTotalImpression == "0") 0 else {
                        100 * convertMoneyToValue(
                            it.response?.data?.get(0)?.statTotalTopSlotImpression ?: ""
                        ) / convertMoneyToValue(
                            it.response?.data?.get(
                                0
                            )?.statTotalImpression ?: ""
                        )
                    }
                mainBottomSheetBinding.includePerformaTampil.adPerformance.text = when {
                    adPerformance > 20 -> {
                        setGreenCondition()
                        getString(R.string.topads_ads_performance_top_frequently)
                    }
                    adPerformance > 5 -> {
                        setYellowCondition()
                        getString(R.string.topads_ads_performance_top_rarity)
                    }
                    adPerformance > 0 -> {
                        setRedCondition()
                        getString(R.string.topads_ads_performance_lose_competition)
                    }
                    else -> {
                        setGreyCondition()
                        getString(R.string.topads_ads_performance_not_rated)
                    }
                }

                mainBottomSheetBinding.includeAdGroupManual.groupName.text =
                    it.response?.data?.get(0)?.groupName
                mainBottomSheetBinding.includeAdGroupManual.productsCount.text =
                    it.response?.data?.get(0)?.totalItem.toString()
                mainBottomSheetBinding.includeAdGroupManual.keywordsCount.text =
                    it.response?.data?.get(0)?.totalKeyword.toString()
                mainBottomSheetBinding.includeAdGroupManual.adCostSearch.text =
                    String.format(
                        "Rp%s",
                        convertToCurrency(
                            it.response?.data?.get(0)?.groupBidSetting?.productSearch?.toLong() ?: 0
                        )
                    )
                mainBottomSheetBinding.includeAdGroupManual.adCostRecommend.text =
                    String.format(
                        "Rp%s",
                        convertToCurrency(
                            it.response?.data?.get(0)?.groupBidSetting?.productBrowse?.toLong() ?: 0
                        )
                    )
                mainBottomSheetBinding.includeAdGroupManual.dailyBudget.text =
                    if (it.response?.data?.get(0)?.groupPriceDaily == 0f) {
                        getString(R.string.tidak_dibatasi)
                    } else {
                        "Rp ${
                            convertToCurrency(
                                it.response?.data?.get(
                                    0
                                )?.groupPriceDaily?.toLong() ?: 0
                            )
                        }"
                    }
                if (it.response?.data?.get(0)?.groupPriceDaily != 0f) {
                    mainBottomSheetBinding.includeAdGroupManual.dailyBudgetDesc.text =
                        String.format(
                            "%s dari %s",
                            it.response?.data?.get(0)?.groupPriceDailySpentFmt,
                            convertToCurrency(
                                it.response?.data?.get(
                                    0
                                )?.groupPriceDaily?.toLong() ?: 0
                            )
                        )
                }
            } else {
                mainBottomSheetBinding.includePerformaTampil.adPerformance.text =
                    getString(R.string.topads_ads_performance_top_frequently)
                setGreyCondition()
            }
        }

        seePerformanceTopAdsViewModel?.topAdsGetAutoAds?.observe(this) {
            if (it != null) {
                mainBottomSheetBinding.includeAdGroupAutomatic.adStatus.text =
                    when (it.data.status) {
                        100 -> getString(R.string.topads_inactive)
                        200, 300, 400 -> getString(R.string.topads_dalam_proses)
                        500 -> getString(R.string.ads_active)
                        600 -> getString(R.string.topads_dash_tidak_tampil)
                        else -> ""
                    }
                mainBottomSheetBinding.includeAdGroupAutomatic.dailyBudget2.text =
                    String.format("Rp %s", convertToCurrency(it.data.dailyBudget.toLong()))
                mainBottomSheetBinding.includeAdGroupAutomatic.dailyBudgetDesc2.text =
                    String.format(
                        "Rp %s dari %s",
                        convertToCurrency(it.data.dailyUsage.toLong()),
                        convertToCurrency(it.data.dailyBudget.toLong())
                    )
            }
        }

        seePerformanceTopAdsViewModel?.goalId?.observe(this) {
            mainBottomSheetBinding.adPlacementFilter.chipText = when (it) {
                1 -> {
                    SeePerformanceTopadsTracker.clickIklanSemuaPenempatan(currentSite)
                    getString(R.string.topads_ads_performance_all_placements_filter_title)
                }
                2 -> {
                    SeePerformanceTopadsTracker.clickIklanPencarian(currentSite)
                    getString(R.string.topads_ads_performance_in_search_filter_title)
                }
                3 -> {
                    SeePerformanceTopadsTracker.clickIklanRekomendasi(currentSite)
                    getString(R.string.topads_ads_performance_in_recommendation_filter_title)
                }
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
            if (mainBottomSheetBinding.productName.layout.getEllipsisCount(1) > 0) {
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
            val adPerformanceCount =
                if (seePerformanceTopAdsViewModel?.topAdsGetGroupInfo?.value?.response?.data?.get(
                        0
                    )?.statTotalImpression == "0"
                ) 0 else {
                    100 * convertMoneyToValue(
                        seePerformanceTopAdsViewModel?.topAdsGetGroupInfo?.value?.response?.data?.get(
                            0
                        )?.statTotalTopSlotImpression ?: ""
                    ) / convertMoneyToValue(
                        seePerformanceTopAdsViewModel?.topAdsGetGroupInfo?.value?.response?.data?.get(
                            0
                        )?.statTotalImpression ?: ""
                    )
                }
            when {
                adPerformanceCount > 20 -> {
                    showDescriptionBottomSheet(
                        getString(R.string.topads_ads_performance_performa_tampil),
                        "$adPerformanceCount%",
                        getString(R.string.topads_ads_performance_top_keyword),
                        String.format(
                            getString(R.string.topads_ads_performance_count),
                            seePerformanceTopAdsViewModel?.topAdsGetGroupInfo?.value?.response?.data?.get(
                                0
                            )?.statTotalTopSlotImpression,
                            seePerformanceTopAdsViewModel?.topAdsGetGroupInfo?.value?.response?.data?.get(
                                0
                            )?.statTotalImpression
                        ),
                        R.color.Unify_GN500
                    )
                }
                adPerformanceCount > 5 -> {
                    showDescriptionBottomSheet(
                        getString(R.string.topads_ads_performance_performa_tampil),
                        "$adPerformanceCount%",
                        getString(R.string.topads_ads_performance_top_keyword),
                        String.format(
                            getString(R.string.topads_ads_performance_count),
                            seePerformanceTopAdsViewModel?.topAdsGetGroupInfo?.value?.response?.data?.get(
                                0
                            )?.statTotalTopSlotImpression,
                            seePerformanceTopAdsViewModel?.topAdsGetGroupInfo?.value?.response?.data?.get(
                                0
                            )?.statTotalImpression
                        ),
                        R.color.Unify_YN500
                    )
                }
                adPerformanceCount > 0 -> {
                    showDescriptionBottomSheet(
                        getString(R.string.topads_ads_performance_performa_tampil),
                        "$adPerformanceCount%",
                        getString(R.string.topads_ads_performance_top_keyword),
                        String.format(
                            getString(R.string.topads_ads_performance_count),
                            seePerformanceTopAdsViewModel?.topAdsGetGroupInfo?.value?.response?.data?.get(
                                0
                            )?.statTotalTopSlotImpression,
                            seePerformanceTopAdsViewModel?.topAdsGetGroupInfo?.value?.response?.data?.get(
                                0
                            )?.statTotalImpression
                        ),
                        R.color.Unify_RN500
                    )
                }
                else -> {
                    showDescriptionBottomSheet(
                        getString(R.string.topads_ads_performance_performa_tampil),
                        "",
                        getString(R.string.topads_ads_performance_not_rated),
                        "",
                        0,
                        R.color.Unify_NN600
                    )
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
            if (mainBottomSheetBinding.includeAdGroupManual.groupName.layout.getEllipsisCount(0) > 0) {
                showDescriptionBottomSheet(
                    getString(R.string.topads_create_group_name),
                    "",
                    "",
                    seePerformanceTopAdsViewModel?.topAdsGetGroupInfo?.value?.response?.data?.get(0)?.groupName
                        ?: ""
                )
            }
        }

        mainBottomSheetBinding.includeAdGroupManual.lihatPengaturanGroupBtn.setOnClickListener {
            SeePerformanceTopadsTracker.clickExpandGroupSettings(currentSite)
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
            SeePerformanceTopadsTracker.clickGroupIklan(currentSite)
            val intent =
                RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS).apply {
                    putExtra(TopAdsDashboardConstant.TAB_POSITION, MpTopadsConst.CONST_2)
                    putExtra(
                        TopAdsDashboardConstant.GROUPID,
                        seePerformanceTopAdsViewModel?.topAdsPromoInfo?.value?.topAdsGetPromo?.data?.get(
                            0
                        )?.groupID ?: ""
                    )
                    putExtra(TopAdsDashboardConstant.GROUP_STRATEGY, "see_ads_performance")
                }
            startActivity(intent)
            finish()
        }

        mainBottomSheetBinding.includeAdGroupAutomatic.automaticBtnSubmit.setOnClickListener {
            SeePerformanceTopadsTracker.clickIklanOtomatis(currentSite)
            val intent = RouteManager.getIntent(
                this, ApplinkConstInternalTopAds.TOPADS_EDIT_AUTOADS
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
            val intent = Intent(this, TopAdsAddCreditActivity::class.java)
            intent.putExtra(TopAdsAddCreditActivity.SHOW_FULL_SCREEN_BOTTOM_SHEET, true)
            startActivityForResult(intent, REQUEST_CODE)
            SeePerformanceTopadsTracker.viewKreditPage(currentSite)
        }
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
        seePerformanceTopAdsViewModel?.getGroupInfo()
    }

    fun getProductStatistics(goalId: Int) {

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

    /**
     * title is used as key to check for date filter option user has opted for
     */
    fun updateDateFilter(title: String) {
        when (title) {
            getString(R.string.topads_common_date_today) -> {
                startDate = getDaysAgo(0, REQUEST_DATE_FORMAT)
                dateFilterType = 1
            }
            getString(R.string.topads_common_date_yesterday) -> {
                startDate = getDaysAgo(1, REQUEST_DATE_FORMAT)
                dateFilterType = 2
            }
            String.format(getString(R.string.topads_common_date_x_last_days), 3) -> {
                startDate = getDaysAgo(3, REQUEST_DATE_FORMAT)
                dateFilterType = 3
            }
            String.format(getString(R.string.topads_common_date_x_last_days), 7) -> {
                startDate = getDaysAgo(7, REQUEST_DATE_FORMAT)
                dateFilterType = 4
            }
            String.format(getString(R.string.topads_common_date_x_last_days), 30) -> {
                startDate = getDaysAgo(30, REQUEST_DATE_FORMAT)
                dateFilterType = 5
            }
            getString(R.string.topads_common_date_this_month) -> {
                startDate = getFirstDateOfMonth(REQUEST_DATE_FORMAT)
                dateFilterType = 6
            }
            else -> openCalendar()
        }
        if (title != getString(R.string.topads_common_custom)) {
            endDate = getDaysAgo(0, REQUEST_DATE_FORMAT)
            mainBottomSheetBinding.dateFilter.chipText = title
            getProductStatistics(
                seePerformanceTopAdsViewModel?.goalId?.value ?: 1
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
        dateFilterType = 7
        mainBottomSheetBinding.dateFilter.chipText = getString(R.string.topads_common_custom)
        getProductStatistics(
            seePerformanceTopAdsViewModel?.goalId?.value ?: 1
        )
    }

    private fun setProductStatistics(dataItem: WithoutGroupDataItem) {
        mainBottomSheetBinding.includeCardStatistics.productStatisticsLoaderGroup.visibility =
            View.GONE
        mainBottomSheetBinding.includeCardStatistics.productStatisticsGroup.visibility =
            View.VISIBLE

        mainBottomSheetBinding.includeCardStatistics.tampilCount.text = dataItem.statTotalImpression
        mainBottomSheetBinding.includeCardStatistics.klikCount.text = dataItem.statTotalClick
        mainBottomSheetBinding.includeCardStatistics.totalTerjualCount.text = dataItem.statTotalSold
        mainBottomSheetBinding.includeCardStatistics.pendapatanCount.text =
            dataItem.statTotalGrossProfit.replace(" ","")
        mainBottomSheetBinding.includeCardStatistics.pengeluaranCount.text = dataItem.statTotalSpent.replace(" ","")
        mainBottomSheetBinding.includeCardStatistics.efektivitasIklanCount.text =
            dataItem.statTotalRoas
    }

    private fun setGreenCondition() {
        ImageViewCompat.setImageTintList(
            mainBottomSheetBinding.includePerformaTampil.adsPerformanceIndicator.block1,
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Unify_GN500))
        )
        ImageViewCompat.setImageTintList(
            mainBottomSheetBinding.includePerformaTampil.adsPerformanceIndicator.block2,
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Unify_GN500))
        )
        ImageViewCompat.setImageTintList(
            mainBottomSheetBinding.includePerformaTampil.adsPerformanceIndicator.block3,
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Unify_GN500))
        )
    }

    private fun setYellowCondition() {
        ImageViewCompat.setImageTintList(
            mainBottomSheetBinding.includePerformaTampil.adsPerformanceIndicator.block1,
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Unify_YN500))
        )
        ImageViewCompat.setImageTintList(
            mainBottomSheetBinding.includePerformaTampil.adsPerformanceIndicator.block2,
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Unify_YN500))
        )
        ImageViewCompat.setImageTintList(
            mainBottomSheetBinding.includePerformaTampil.adsPerformanceIndicator.block3,
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Unify_NN300))
        )
    }

    private fun setRedCondition() {
        ImageViewCompat.setImageTintList(
            mainBottomSheetBinding.includePerformaTampil.adsPerformanceIndicator.block1,
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Unify_RN500))
        )
        ImageViewCompat.setImageTintList(
            mainBottomSheetBinding.includePerformaTampil.adsPerformanceIndicator.block2,
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Unify_NN300))
        )
        ImageViewCompat.setImageTintList(
            mainBottomSheetBinding.includePerformaTampil.adsPerformanceIndicator.block3,
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Unify_NN300))
        )
    }

    private fun setGreyCondition() {
        ImageViewCompat.setImageTintList(
            mainBottomSheetBinding.includePerformaTampil.adsPerformanceIndicator.block1,
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Unify_NN300))
        )
        ImageViewCompat.setImageTintList(
            mainBottomSheetBinding.includePerformaTampil.adsPerformanceIndicator.block2,
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Unify_NN300))
        )
        ImageViewCompat.setImageTintList(
            mainBottomSheetBinding.includePerformaTampil.adsPerformanceIndicator.block3,
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Unify_NN300))
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
        if (heading1Color > 0) {
            binding.heading1.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this, heading1Color
                    )
                )
            )
        }
        if (heading2Color > 0) {
            binding.heading2.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this, heading2Color
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
        bottomSheet.show(supportFragmentManager, "descriptionBottomSheet")
    }

    private fun showStatusIklanBottomSheet() {
        val list = arrayListOf(
            ItemListUiModel(
                title = getString(R.string.ads_active),
                isSelected = seePerformanceTopAdsViewModel?.topAdsPromoInfo?.value?.topAdsGetPromo?.data?.get(
                    0
                )?.status != "3"
            ), ItemListUiModel(
                title = getString(R.string.topads_non_active),
                isSelected = seePerformanceTopAdsViewModel?.topAdsPromoInfo?.value?.topAdsGetPromo?.data?.get(
                    0
                )?.status == "3"
            )
        )
        ListBottomSheet.show(
            supportFragmentManager, getString(R.string.topads_ad_status), list
        )
    }

    fun updateStatusIklan(action: String) {
        mainBottomSheetBinding.includeStatusIklan.statusIklanLoader.visibility = View.VISIBLE
        mainBottomSheetBinding.includeStatusIklan.statusIklanGroup.visibility = View.INVISIBLE
        val updatedStatus = when (action) {
            "3" -> {
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
            seePerformanceTopAdsViewModel?.topAdsPromoInfo?.value?.topAdsGetPromo?.data?.get(0)?.groupID
        )
    }

    private fun showAdsPlacingFilterBottomSheet() {
        val adPlacementFilterList = arrayListOf(
            ItemListUiModel(
                getString(R.string.topads_ads_performance_all_placements_filter_title),
                getString(R.string.topads_ads_performance_all_placements_filter_desc),
                isSelected = seePerformanceTopAdsViewModel?.goalId?.value == 1
            ), ItemListUiModel(
                getString(R.string.topads_ads_performance_in_search_filter_title),
                getString(R.string.topads_ads_performance_in_search_filter_desc),
                isSelected = seePerformanceTopAdsViewModel?.goalId?.value == 2
            ), ItemListUiModel(
                getString(R.string.topads_ads_performance_in_recommendation_filter_title),
                getString(R.string.topads_ads_performance_in_recommendation_filter_desc),
                isSelected = seePerformanceTopAdsViewModel?.goalId?.value == 3
            )
        )
        ListBottomSheet.show(
            supportFragmentManager,
            getString(R.string.topads_ads_performance_ad_placement),
            adPlacementFilterList
        )
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
                getString(R.string.topads_common_date_today),
                today,
                dateFilterType == 1
            ),
            ItemListUiModel(
                getString(R.string.topads_common_date_yesterday),
                yesterday,
                dateFilterType == 2
            ),
            ItemListUiModel(
                String.format(getString(R.string.topads_common_date_x_last_days), 3),
                "$daysAgo3 - $today",
                dateFilterType == 3
            ),
            ItemListUiModel(
                String.format(getString(R.string.topads_common_date_x_last_days), 7),
                "$daysAgo7 - $today",
                dateFilterType == 4
            ),
            ItemListUiModel(
                String.format(getString(R.string.topads_common_date_x_last_days), 30),
                "$daysAgo30 - $today",
                dateFilterType == 5
            ),
            ItemListUiModel(
                getString(R.string.topads_common_date_this_month),
                "$firstDateOfMonth - $today",
                dateFilterType == 6
            ),
            ItemListUiModel(
                getString(R.string.topads_common_custom),
                if (dateFilterType == 7) "${
                    SimpleDateFormat(DATE_FORMAT_DD_MMM_YYYY).format(
                        selectedDateFrom
                    )
                } - ${SimpleDateFormat(DATE_FORMAT_DD_MMM_YYYY).format(selectedDateTo)}" else getString(
                    R.string.topads_common_select_date
                ),
                dateFilterType == 7
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
            selectedDateFrom, selectedDateTo, AdsPerformanceDateRangePickerBottomSheet.MAX_RANGE_90
        ).show(supportFragmentManager, "calendar")
    }

    override fun getComponent(): CreateAdsComponent {
        return DaggerCreateAdsComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent
        ).build()
    }
}
