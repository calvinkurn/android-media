package com.tokopedia.power_merchant.subscribe.view.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMBenefitItemUiModel
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.analytics.tracking.MembershipDetailTracker
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.databinding.FragmentMembershipDetailBinding
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.adapter.MembershipViewPagerAdapter
import com.tokopedia.power_merchant.subscribe.view.model.MembershipDataUiModel
import com.tokopedia.power_merchant.subscribe.view.model.MembershipDetailUiModel
import com.tokopedia.power_merchant.subscribe.view.viewmodel.MembershipDetailViewModel
import com.tokopedia.unifycomponents.setIconUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 26/05/22.
 */

class MembershipDetailFragment : BaseDaggerFragment() {

    companion object {
        private const val SATURATION_INACTIVE = 0.0f
        private const val SATURATION_ACTIVE = 1f
        private const val SPACE = " "

        fun createInstance(): MembershipDetailFragment {
            return MembershipDetailFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var tracker: MembershipDetailTracker

    private val viewModel: MembershipDetailViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(MembershipDetailViewModel::class.java)
    }
    private var binding: FragmentMembershipDetailBinding? = null
    private val pagerAdapter by lazy { MembershipViewPagerAdapter() }
    private var data: MembershipDetailUiModel? = null
    private var tabSelectedListener: TabLayout.OnTabSelectedListener? =
        object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                setOnTabSelected()
                sendTrackerOnTabClicked()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        }

    override fun getScreenName(): String = String.EMPTY

    override fun initInjector() {
        getComponent(PowerMerchantSubscribeComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMembershipDetailBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewBackground()
        setupViewPager()
        observePmShopInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabSelectedListener = null
        binding = null
    }

    private fun setViewBackground() {
        binding?.root?.run {
            val background =
                context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_Background)
            setBackgroundColor(background)
        }
    }

    private fun observePmShopInfo() {
        observe(viewModel.membershipBasicInfo) {
            when (it) {
                is Success -> {
                    showSuccessState()
                    onSuccessPmShopInfo(it.data)
                }
                is Fail -> {
                    showErrorState()
                    onErrorPmShopInfo(it.throwable)
                }
            }
        }
        fetchData()
    }

    private fun fetchData() {
        showLoadingState()
        viewModel.getMembershipBasicInfo()
    }

    private fun showSuccessState() {
        binding?.run {
            loaderMembershipDetail.gone()
            groupMembershipSuccessState.visible()
            globalErrorMembershipDetail.gone()
        }
    }

    private fun showErrorState() {
        binding?.run {
            loaderMembershipDetail.gone()
            groupMembershipSuccessState.gone()
            globalErrorMembershipDetail.visible()
            globalErrorMembershipDetail.setActionClickListener {
                fetchData()
            }
        }
    }

    private fun showLoadingState() {
        binding?.run {
            loaderMembershipDetail.visible()
            groupMembershipSuccessState.gone()
            globalErrorMembershipDetail.gone()
        }
    }

    private fun onErrorPmShopInfo(throwable: Throwable) {
        Timber.e(throwable)
    }

    private fun onSuccessPmShopInfo(data: MembershipDetailUiModel) {
        this.data = data
        showHeaderInfo(data)
        setupPagerItems()
        setupTabs()
        sendImpressionTrackerEvent()
        showShopPerformanceFooterInfo()
    }

    private fun showShopPerformanceFooterInfo() {
        binding?.run {
            tvPmMembership2.text = getString(R.string.pm_membership_detail_info_2).parseAsHtml()
            tvPmMembership2.setOnClickListener {
                RouteManager.route(context, ApplinkConst.SHOP_SCORE_DETAIL)
                sendClickShopPerformanceEvent()
            }
        }
    }

    private fun sendClickShopPerformanceEvent() {
        val currentGrade = data?.gradeName.orEmpty()
        tracker.sendClickLearnShopPerformanceEvent(currentGrade)
    }

    private fun sendImpressionTrackerEvent() {
        val currentGrade = data?.gradeName.orEmpty()
        tracker.sendImpressionMembershipPageEvent(currentGrade)
    }

    private fun setupViewPager() {
        binding?.rvPmMembership?.run {
            val mLayoutManager = object : LinearLayoutManager(requireContext(), HORIZONTAL, false) {
                override fun canScrollVertically(): Boolean = false
            }

            adapter = pagerAdapter
            layoutManager = mLayoutManager
            try {
                PagerSnapHelper().attachToRecyclerView(this)
            } catch (e: IllegalStateException) {
                Timber.e(e)
            }
        }
    }

    private fun setupTabs() {
        binding?.tabPmMembership?.run {
            tabLayout.removeAllTabs()
            val gradeList = pagerAdapter.getPmGradeList()
            val activeTabIndex = gradeList.indexOfLast { it.isTabActive }
            try {
                gradeList.forEachIndexed { i, page ->
                    val isSelected = i == activeTabIndex
                    addNewTab(page.tabLabel, isSelected)
                    getUnifyTabLayout().getTabAt(i)?.run {
                        setIconUnify(page.tabResIcon)
                        if (i == activeTabIndex) {
                            showTabLabel(this.customView)
                            setUnifyTabIconColorFilter(
                                this.customView,
                                SATURATION_ACTIVE
                            )
                        } else {
                            setUnifyTabIconColorFilter(
                                this.customView,
                                SATURATION_INACTIVE
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }

            tabLayout.tabRippleColor = ColorStateList.valueOf(Color.TRANSPARENT)
            post {
                if (activeTabIndex != RecyclerView.NO_POSITION) {
                    binding?.rvPmMembership?.scrollToPosition(activeTabIndex)
                }
                setListeners()
            }
        }
    }

    private fun setListeners() {
        binding?.run {
            val mLayoutManager = rvPmMembership.layoutManager as LinearLayoutManager
            rvPmMembership.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val position = mLayoutManager.findFirstCompletelyVisibleItemPosition()
                    if (position != RecyclerView.NO_POSITION) {
                        binding?.tabPmMembership?.tabLayout?.getTabAt(position)?.select()
                    }
                }
            })

            tabSelectedListener?.let {
                tabPmMembership.tabLayout.addOnTabSelectedListener(it)
            }
        }
    }

    private fun showTabLabel(customView: View?) {
        customView?.findViewById<TextView>(
            com.tokopedia.unifycomponents.R.id.tab_item_label_new_id
        )?.run {
            visible()
            text = getString(R.string.pm_shop_label)
            setTextColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_GN500))
            try {
                setBackgroundResource(R.drawable.bg_pm_tab_label)
            } catch (e: Exception) {
                setBackgroundColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_GN50))
            }
        }
    }

    private fun sendTrackerOnTabClicked() {
        binding?.run {
            val position = tabPmMembership.tabLayout.selectedTabPosition
            val tabLabel = pagerAdapter.getPmGradeList().getOrNull(position)?.tabLabel.orEmpty()
            tracker.sendClickPmGradeTabEvent(tabLabel)
        }
    }

    private fun setOnTabSelected() {
        binding?.run {
            val selectedPosition = tabPmMembership.tabLayout.selectedTabPosition
            rvPmMembership.smoothScrollToPosition(selectedPosition)
            tabPmMembership.run {
                for (i in Int.ZERO..getUnifyTabLayout().tabCount.minus(Int.ONE)) {
                    val view = getUnifyTabLayout().getTabAt(i)
                    if (i == selectedPosition) {
                        setUnifyTabIconColorFilter(
                            view?.customView,
                            SATURATION_ACTIVE
                        )
                    } else {
                        setUnifyTabIconColorFilter(
                            view?.customView,
                            SATURATION_INACTIVE
                        )
                    }
                }
            }
        }
    }

    private fun setUnifyTabIconColorFilter(view: View?, saturation: Float) {
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(saturation)
        view?.findViewById<IconUnify>(com.tokopedia.unifycomponents.R.id.tab_item_icon_unify_id)
            ?.colorFilter = ColorMatrixColorFilter(colorMatrix)
    }

    private fun showHeaderInfo(data: MembershipDetailUiModel) {
        binding?.run {
            tvPmMembershipPerformancePeriod.text = getString(
                R.string.pm_date_based_on_your_sell,
                data.periodDate,
                getGradeNameCamelCased(data.gradeName)
            ).parseAsHtml()
            tvPmMembershipNextUpdate.text = if (data.isCalibrationDate) {
                getString(
                    R.string.pm_calibration_date_benefit_package, data.nextUpdate
                ).parseAsHtml()
            } else {
                getString(
                    R.string.pm_next_update_benefit_package, data.nextUpdate
                ).parseAsHtml()
            }

            try {
                tvPmMembershipPerformancePeriod.setBackgroundResource(R.drawable.bg_pm_membership_detail_header)
            } catch (e: Exception) {
                tvPmMembershipPerformancePeriod.setBackgroundColor(
                    requireContext().getResColor(com.tokopedia.unifyprinciples.R.color.Unify_GN400)
                )
            }
        }
    }

    private fun getGradeNameCamelCased(gradeName: String): String {
        return gradeName.split(SPACE).joinToString(SPACE) { str ->
            str.replaceFirstChar {
                if (it.isLowerCase()) {
                    it.titlecase(Locale.getDefault())
                } else {
                    it.toString()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupPagerItems() {
        pagerAdapter.clearItems()

        pagerAdapter.addItem(
            getMembershipData(
                gradeBenefit = getPowerMerchantPager(),
                netIncome = data?.netIncomeLast30Days.orZero(),
                totalOrder = data?.totalOrderLast30Days.orZero(),
                orderThreshold = Constant.MembershipConst.PM_ORDER_THRESHOLD,
                netIncomeThreshold = Constant.MembershipConst.PM_INCOME_THRESHOLD,
                netIncomeThresholdFmt = Constant.MembershipConst.PM_INCOME_THRESHOLD_FMT
            )
        )
        pagerAdapter.addItem(
            getMembershipData(
                gradeBenefit = getPmProAdvancePager(),
                netIncome = data?.netIncomeLast90Days.orZero(),
                totalOrder = data?.totalOrderLast90Days.orZero(),
                orderThreshold = Constant.MembershipConst.PM_PRO_ADVANCE_ORDER_THRESHOLD,
                netIncomeThreshold = Constant.MembershipConst.PM_PRO_ADVANCE_INCOME_THRESHOLD,
                netIncomeThresholdFmt = Constant.MembershipConst.PM_PRO_ADVANCE_INCOME_THRESHOLD_FMT
            )
        )
        pagerAdapter.addItem(
            getMembershipData(
                gradeBenefit = getPmProExpertPager(),
                netIncome = data?.netIncomeLast90Days.orZero(),
                totalOrder = data?.totalOrderLast90Days.orZero(),
                orderThreshold = Constant.MembershipConst.PM_PRO_EXPERT_ORDER_THRESHOLD,
                netIncomeThreshold = Constant.MembershipConst.PM_PRO_EXPERT_INCOME_THRESHOLD,
                netIncomeThresholdFmt = Constant.MembershipConst.PM_PRO_EXPERT_INCOME_THRESHOLD_FMT
            )
        )
        pagerAdapter.addItem(
            getMembershipData(
                gradeBenefit = getPmProUltimatePager(),
                netIncome = data?.netIncomeLast90Days.orZero(),
                totalOrder = data?.totalOrderLast90Days.orZero(),
                orderThreshold = Constant.MembershipConst.PM_PRO_ULTIMATE_ORDER_THRESHOLD,
                netIncomeThreshold = Constant.MembershipConst.PM_PRO_ULTIMATE_INCOME_THRESHOLD,
                netIncomeThresholdFmt = Constant.MembershipConst.PM_PRO_ULTIMATE_INCOME_THRESHOLD_FMT
            )
        )

        pagerAdapter.notifyDataSetChanged()
    }

    private fun getMembershipData(
        gradeBenefit: PMGradeWithBenefitsUiModel,
        netIncome: Long,
        totalOrder: Long,
        orderThreshold: Long,
        netIncomeThreshold: Long,
        netIncomeThresholdFmt: String
    ): MembershipDataUiModel {
        return MembershipDataUiModel(
            shopScore = data?.shopScore.orZero(),
            totalOrder = totalOrder,
            netIncome = netIncome,
            orderThreshold = orderThreshold,
            netIncomeThreshold = netIncomeThreshold,
            netIncomeThresholdFmt = netIncomeThresholdFmt,
            gradeBenefit = gradeBenefit
        )
    }

    private fun getPowerMerchantPager(): PMGradeWithBenefitsUiModel.PM {
        val gradeName = data?.gradeName.orEmpty()
        return PMGradeWithBenefitsUiModel.PM(
            gradeName = Constant.POWER_MERCHANT,
            isTabActive = gradeName.equals(PMConstant.ShopGrade.PM, true),
            tabLabel = getString(R.string.pm_power_merchant),
            tabResIcon = IconUnify.BADGE_PM_FILLED,
            benefitList = getBenefitList(
                Constant.PM_TOP_ADS_CREDIT,
                Constant.PM_BROAD_CAST_CHAT,
                Constant.PM_SPECIAL_RELEASE,
                Constant.PM_PRODUCT_BUNDLING
            )
        )
    }

    private fun getPmProAdvancePager(): PMGradeWithBenefitsUiModel.PMProAdvance {
        val gradeName: String = data?.gradeName.orEmpty()
        return PMGradeWithBenefitsUiModel.PMProAdvance(
            gradeName = Constant.PM_PRO_ADVANCED,
            isTabActive = gradeName.equals(
                PMConstant.ShopGrade.PRO_ADVANCE,
                true
            ),
            tabLabel = getString(R.string.pm_pro_advanced),
            tabResIcon = IconUnify.BADGE_PMPRO_FILLED,
            benefitList = getBenefitList(
                Constant.PM_PRO_ADV_TOP_ADS_CREDIT,
                Constant.PM_PRO_ADV_BROAD_CAST_CHAT,
                Constant.PM_PRO_ADV_SPECIAL_RELEASE,
                Constant.PM_PRO_ADV_PRODUCT_BUNDLING
            )
        )
    }

    private fun getPmProExpertPager(): PMGradeWithBenefitsUiModel.PMProExpert {
        val gradeName: String = data?.gradeName.orEmpty()
        return PMGradeWithBenefitsUiModel.PMProExpert(
            gradeName = Constant.PM_PRO_EXPERT,
            isTabActive = gradeName.equals(
                PMConstant.ShopGrade.PRO_EXPERT,
                true
            ),
            tabLabel = getString(R.string.pm_pro_expert),
            tabResIcon = IconUnify.BADGE_PMPRO_FILLED,
            benefitList = getBenefitList(
                Constant.PM_PRO_EXP_TOP_ADS_CREDIT,
                Constant.PM_PRO_EXP_BROAD_CAST_CHAT,
                Constant.PM_PRO_EXP_SPECIAL_RELEASE,
                Constant.PM_PRO_EXP_PRODUCT_BUNDLING
            )
        )
    }

    private fun getPmProUltimatePager(): PMGradeWithBenefitsUiModel.PMProUltimate {
        val gradeName: String = data?.gradeName.orEmpty()
        return PMGradeWithBenefitsUiModel.PMProUltimate(
            gradeName = Constant.PM_PRO_ULTIMATE,
            isTabActive = gradeName.equals(
                PMConstant.ShopGrade.PRO_ULTIMATE,
                true
            ),
            tabLabel = getString(R.string.pm_pro_ultimate),
            tabResIcon = IconUnify.BADGE_PMPRO_FILLED,
            benefitList = getBenefitList(
                Constant.PM_PRO_ULT_TOP_ADS_CREDIT,
                Constant.PM_PRO_ULT_BROAD_CAST_CHAT,
                Constant.PM_PRO_ULT_SPECIAL_RELEASE,
                Constant.PM_PRO_ULT_PRODUCT_BUNDLING
            )
        )
    }

    private fun getBenefitList(
        topAdsCredit: String,
        broadCastChat: String,
        specialRelease: Int,
        productBundling: Int
    ): List<PMBenefitItemUiModel> {
        return listOf(
            PMBenefitItemUiModel(
                iconUrl = Constant.Image.IC_PM_TOP_ADS,
                benefitDescription = getString(R.string.pm_membership_benefit_topads, topAdsCredit)
            ),
            PMBenefitItemUiModel(
                iconUrl = Constant.Image.IC_PM_BROADCAST_CHAT,
                benefitDescription = getString(
                    R.string.pm_membership_benefit_broad_cast_chat,
                    broadCastChat
                )
            ),
            PMBenefitItemUiModel(
                iconUrl = Constant.Image.IC_PM_SPECIAL_RELEASE,
                benefitDescription = getString(
                    R.string.pm_membership_benefit_special_release,
                    specialRelease
                )
            ),
            PMBenefitItemUiModel(
                iconUrl = Constant.Image.IC_PM_PRODUCT_BUNDLING,
                benefitDescription = getString(
                    R.string.pm_membership_benefit_product_bundling,
                    productBundling
                )
            )
        )
    }
}