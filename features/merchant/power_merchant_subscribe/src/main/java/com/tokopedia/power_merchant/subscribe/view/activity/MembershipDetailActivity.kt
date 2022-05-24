package com.tokopedia.power_merchant.subscribe.view.activity

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PMBenefitItemUiModel
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantBasicInfoUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.databinding.ActivityMembershipDetailBinding
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.adapter.MembershipViewPagerAdapter
import com.tokopedia.power_merchant.subscribe.view.fragment.MembershipDetailFragment
import com.tokopedia.power_merchant.subscribe.view.model.MembershipDataUiModel
import com.tokopedia.power_merchant.subscribe.view.viewmodel.MembershipActivityViewModel
import com.tokopedia.unifycomponents.setIconUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 23/05/22.
 */

class MembershipDetailActivity : BaseActivity(),
    HasComponent<PowerMerchantSubscribeComponent> {

    companion object {
        private const val SATURATION_INACTIVE = 0.0f
        private const val SATURATION_ACTIVE = 1f
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MembershipActivityViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(MembershipActivityViewModel::class.java)
    }
    private var binding: ActivityMembershipDetailBinding? = null
    private val pagerAdapter by lazy {
        MembershipViewPagerAdapter(this@MembershipDetailActivity)
    }
    private var pmBasicInfo: PowerMerchantBasicInfoUiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMembershipDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initInjector()

        setupViewPager()
        observePmShopInfo()
    }

    override fun getComponent(): PowerMerchantSubscribeComponent {
        val appComponent = (applicationContext as BaseMainApplication).baseAppComponent
        return DaggerPowerMerchantSubscribeComponent.builder()
            .baseAppComponent(appComponent)
            .build()
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun observePmShopInfo() {
        observe(viewModel.powerMerchantBasicInfo) {
            when (it) {
                is Success -> onSuccessPmShopInfo(it.data)
                is Fail -> onErrorPmShopInfo(it.throwable)
            }
        }
        viewModel.getPowerMerchantBasicInfo(true)
    }

    private fun onSuccessPmShopInfo(data: PowerMerchantBasicInfoUiModel) {
        pmBasicInfo = data
        setupPagerItems(data.shopInfo)
        setupTabs()
    }

    private fun onErrorPmShopInfo(throwable: Throwable) {

    }

    private fun setupViewPager() {
        binding?.vpPmMembership?.run {
            adapter = pagerAdapter
        }
    }

    private fun setupTabs() {
        binding?.tabPmMembership?.run {
            tabLayout.removeAllTabs()
            val gradeList = pagerAdapter.getPmGradeList()
            val activeTabIndex = gradeList.indexOfLast { it.isTabActive }
            try {
                gradeList.forEachIndexed { i, page ->
                    addNewTab(page.tabLabel)
                    getUnifyTabLayout().getTabAt(i)?.run {
                        setIconUnify(page.tabResIcon)
                        if (i == activeTabIndex) {
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
                    tabLayout.getTabAt(activeTabIndex)?.select()
                }
            }
        }
    }

    private fun setUnifyTabIconColorFilter(view: View?, saturation: Float) {
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(saturation)
        val colorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
        view?.findViewById<IconUnify>(com.tokopedia.unifycomponents.R.id.tab_item_icon_unify_id)?.colorFilter =
            colorMatrixColorFilter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupPagerItems(shopInfo: PMShopInfoUiModel) {
        pagerAdapter.clearFragments()

        val powerMerchant = getPowerMerchantPager(shopInfo)
        pagerAdapter.addFragment(
            MembershipDetailFragment.createInstance(
                getMembershipData(
                    shopInfo,
                    powerMerchant.benefitList,
                    powerMerchant.isTabActive
                )
            ),
            powerMerchant
        )

        val pmProAdvance = getPmProAdvancePager(shopInfo)
        pagerAdapter.addFragment(
            MembershipDetailFragment.createInstance(
                getMembershipData(
                    shopInfo,
                    pmProAdvance.benefitList,
                    pmProAdvance.isTabActive
                )
            ),
            pmProAdvance
        )

        val pmProExpert = getPmProExpertPager(shopInfo)
        pagerAdapter.addFragment(
            MembershipDetailFragment.createInstance(
                getMembershipData(
                    shopInfo,
                    pmProAdvance.benefitList,
                    pmProAdvance.isTabActive
                )
            ),
            pmProExpert
        )

        val pmProUltimate = getPmProUltimatePager(shopInfo)
        pagerAdapter.addFragment(
            MembershipDetailFragment.createInstance(
                getMembershipData(
                    shopInfo,
                    pmProUltimate.benefitList,
                    pmProUltimate.isTabActive
                )
            ),
            pmProUltimate
        )

        pagerAdapter.notifyDataSetChanged()
    }

    private fun getMembershipData(
        shopInfo: PMShopInfoUiModel,
        benefitList: List<PMBenefitItemUiModel>,
        isTabActive: Boolean
    ): MembershipDataUiModel {
        return MembershipDataUiModel(
            shopScore = shopInfo.shopScore,
            shopScoreThreshold = shopInfo.shopScorePmProThreshold,
            totalOrder = shopInfo.itemSoldOneMonth,
            orderThreshold = shopInfo.itemSoldPmProThreshold,
            netIncome = shopInfo.netItemValueOneMonth,
            netIncomeThreshold = shopInfo.netItemValuePmProThreshold,
            isActive = isTabActive,
            benefitList = benefitList
        )
    }

    private fun getPowerMerchantPager(shopInfo: PMShopInfoUiModel): PMGradeWithBenefitsUiModel.PM {
        return PMGradeWithBenefitsUiModel.PM(
            gradeName = Constant.POWER_MERCHANT,
            isTabActive = shopInfo.shopLevel <= PMConstant.ShopLevel.ONE
                    || !shopInfo.isEligiblePm || !shopInfo.isEligiblePmPro,
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

    private fun getPmProAdvancePager(shopInfo: PMShopInfoUiModel): PMGradeWithBenefitsUiModel.PMProAdvance {
        return PMGradeWithBenefitsUiModel.PMProAdvance(
            gradeName = Constant.PM_PRO_ADVANCED,
            isTabActive = shopInfo.shopLevel == PMConstant.ShopLevel.TWO && shopInfo.isEligiblePmPro,
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

    private fun getPmProExpertPager(shopInfo: PMShopInfoUiModel): PMGradeWithBenefitsUiModel.PMProExpert {
        return PMGradeWithBenefitsUiModel.PMProExpert(
            gradeName = Constant.PM_PRO_EXPERT,
            isTabActive = shopInfo.shopLevel == PMConstant.ShopLevel.THREE && shopInfo.isEligiblePmPro,
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

    private fun getPmProUltimatePager(shopInfo: PMShopInfoUiModel): PMGradeWithBenefitsUiModel.PMProUltimate {
        return PMGradeWithBenefitsUiModel.PMProUltimate(
            gradeName = Constant.PM_PRO_ULTIMATE,
            isTabActive = shopInfo.shopLevel == PMConstant.ShopLevel.FOUR && shopInfo.isEligiblePmPro,
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