package com.tokopedia.power_merchant.subscribe.view.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.GMParamTracker
import com.tokopedia.gm.common.constant.KYCStatusId
import com.tokopedia.gm.common.constant.PeriodType
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantSettingInfoUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.common.constant.ShopGrade
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.PMWidgetListener
import com.tokopedia.power_merchant.subscribe.view.bottomsheet.PowerMerchantNotificationBottomSheet
import com.tokopedia.power_merchant.subscribe.view.helper.PMRegistrationTermHelper
import com.tokopedia.power_merchant.subscribe.view.model.*
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PowerMerchantSubscriptionViewModel
import com.tokopedia.power_merchant.subscribe.view.viewmodel.SubscriptionActivityViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_pm_power_merchant_subscription.view.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class PowerMerchantSubscriptionFragment : BaseListFragment<BaseWidgetUiModel, WidgetAdapterFactoryImpl>(),
        PMWidgetListener {

    companion object {
        fun createInstance(): PowerMerchantSubscriptionFragment {
            return PowerMerchantSubscriptionFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val sharedViewModel: SubscriptionActivityViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(SubscriptionActivityViewModel::class.java)
    }
    private val mViewModel: PowerMerchantSubscriptionViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PowerMerchantSubscriptionViewModel::class.java)
    }

    private var shopSettingInfo: PowerMerchantSettingInfoUiModel? = null
    private val recyclerView: RecyclerView?
        get() = super.getRecyclerView(view)

    override fun getScreenName(): String = GMParamTracker.ScreenName.PM_UPGRADE_SHOP

    override fun initInjector() {
        getComponent(PowerMerchantSubscribeComponent::class.java).inject(this)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvPmRegistration

    override fun getAdapterTypeFactory(): WidgetAdapterFactoryImpl = WidgetAdapterFactoryImpl(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pm_power_merchant_subscription, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeShopSettingInfo()
    }

    override fun onItemClicked(t: BaseWidgetUiModel?) {}

    override fun loadData(page: Int) {}

    override fun setOnDeactivatePMClickListener() {

    }

    private fun setupView() = view?.run {

    }

    private fun observeShopSettingInfo() {
        sharedViewModel.powerMerchantSettingInfo.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> fetchPageContent(it.data)
                is Fail -> {
                    //show on error fetch setting info
                }
            }
        })
    }

    private fun fetchPageContent(data: PowerMerchantSettingInfoUiModel) {
        this.shopSettingInfo = data
        when (data.periodeType) {
            PeriodType.TRANSITION_PERIOD -> observeTransitionPeriod()
            PeriodType.FINAL_PERIOD -> observeFinalPeriodData(data)
        }
    }

    private fun observeTransitionPeriod() {
        mViewModel.shopInfoAndPMGradeBenefits.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> renderRegistrationPM(it.data)
                is Fail -> {
                    //show error state
                }
            }
        })
        mViewModel.getPMRegistrationData()
    }

    private fun renderRegistrationPM(data: PMGradeBenefitAndShopInfoUiModel) {
        val widgets = listOf(
                getHeaderWidgetData(data.shopInfo),
                getPotentialBenefitWidgetData(),
                WidgetDividerUiModel,
                WidgetGradeBenefitUiModel(benefitPages = data.gradeBenefitList)
        )
        renderList(widgets)

        setupFooterCta(data.shopInfo)
    }

    private fun setupFooterCta(shopInfo: PMShopInfoUiModel) = view?.run {
        val isEligibleShopScore = !shopInfo.isNewSeller && shopInfo.isEligibleShopScore
        val hasActiveProduct = shopInfo.isNewSeller && shopInfo.hasActiveProduct
        val needTnC = (!shopInfo.isKyc && (isEligibleShopScore || hasActiveProduct))
                || (shopInfo.isKyc && (isEligibleShopScore || hasActiveProduct))
        if (shopInfo.kycStatusId == KYCStatusId.PENDING) {
            pmRegistrationFooterView.gone()
        } else {
            pmRegistrationFooterView.visible()
        }
        pmRegistrationFooterView.setTnCVisibility(needTnC)
        val ctaText = if (needTnC || shopInfo.isNewSeller) {
            getString(R.string.power_merchant_register_now)
        } else {
            getString(R.string.pm_interested_to_register)
        }
        pmRegistrationFooterView.setCtaText(ctaText)

        pmRegistrationFooterView.setOnCtaClickListener {
            when {
                shopInfo.isEligiblePm -> submitPMRegistration(it)
                !shopInfo.isKyc && ((!shopInfo.isNewSeller && shopInfo.isEligibleShopScore)
                        || (shopInfo.isNewSeller && shopInfo.hasActiveProduct)) -> submitKYC(it)
                else -> showRegistrationTermBottomSheet(shopInfo)
            }
        }
    }

    private fun submitKYC(isTncChecked: Boolean) {
        if (!isTncChecked) {
            val message = getString(R.string.pm_tnc_agreement_error_message)
            showErrorToaster(message)
            return
        }

        RouteManager.route(context, ApplinkConst.KYC_SELLER_DASHBOARD)
    }

    private fun showErrorToaster(message: String) {
        view?.run {
            val actionText = getString(R.string.power_merchant_ok_label)
            Toaster.build(rvPmRegistration, message, Toaster.LENGTH_INDEFINITE,
                    Toaster.TYPE_ERROR, actionText)
                    .show()
        }
    }

    private fun showRegistrationTermBottomSheet(shopInfo: PMShopInfoUiModel) {
        val isNewSeller = shopInfo.isNewSeller
        val title: String
        val description: String
        val ctaText: String
        val illustrationUrl: String
        val appLink: String

        if (isNewSeller) {
            title = getString(R.string.pm_bottom_sheet_active_product_title)
            description = getString(R.string.pm_bottom_sheet_active_product_description)
            ctaText = getString(R.string.pm_add_product)
            illustrationUrl = Constant.ImageUrl.ADD_PRODUCT_BOTTOM_SHEET
            appLink = ApplinkConst.SellerApp.PRODUCT_ADD
        } else {
            title = getString(R.string.pm_bottom_sheet_shop_score_title)
            description = getString(R.string.pm_bottom_sheet_shop_score_description)
            ctaText = getString(R.string.pm_learn_shop_performance)
            illustrationUrl = Constant.ImageUrl.SHOP_SCORE_BOTTOM_SHEET
            appLink = ApplinkConst.SHOP_SCORE_DETAIL
        }

        val shopScoreBottomSheet = PowerMerchantNotificationBottomSheet.createInstance(title, description, illustrationUrl)
        shopScoreBottomSheet.setPrimaryButtonClickListener(ctaText) {
            shopScoreBottomSheet.dismiss()
            RouteManager.route(context, appLink)
        }

        //to prevent IllegalStateException: Fragment already added
        if (shopScoreBottomSheet.isAdded || childFragmentManager.isStateSaved) return

        Handler().post {
            shopScoreBottomSheet.show(childFragmentManager)
        }
    }

    private fun submitPMRegistration(isTncChecked: Boolean) {
        if (!isTncChecked) {
            val message = getString(R.string.pm_tnc_agreement_error_message)
            showErrorToaster(message)
            return
        }

        mViewModel.submitPMActivation()
    }

    private fun observeFinalPeriodData(pmSettingInfo: PowerMerchantSettingInfoUiModel) {
        view?.pmRegistrationFooterView?.gone()
        mViewModel.pmFinalPeriod.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> renderPMActiveState(it.data)
                is Fail -> {
                    //show on failed
                }
            }
        })
        mViewModel.getFinalPeriodData()
    }

    private fun renderPMActiveState(data: PMFinalPeriodUiModel) {
        val widgets = mutableListOf<BaseWidgetUiModel>()
        widgets.add(WidgetQuitSubmissionUiModel)
        widgets.add(getShopGradeWidgetData(data))
        widgets.add(WidgetDividerUiModel)
        widgets.add(getCurrentShopGradeBenefit(data))
        widgets.add(WidgetDividerUiModel)
        if (data.nextPMGrade != null) {
            widgets.add(getNextShopGradeWidgetData(data))
            widgets.add(WidgetDividerUiModel)
        }
        widgets.add(WidgetNextUpdateUiModel(data.nextQuarterlyCalibrationRefreshDate))
        widgets.add(WidgetSingleCtaUiModel(getString(R.string.pm_pm_transition_period_learnmore), Constant.Url.POWER_MERCHANT_EDU))
        widgets.add(WidgetPMDeactivateUiModel)
        renderList(widgets)
    }

    private fun getNextShopGradeWidgetData(data: PMFinalPeriodUiModel): WidgetNextShopGradeUiModel {
        val nextGrade = data.nextPMGrade
        return WidgetNextShopGradeUiModel(
                shopLevel = nextGrade?.shopLevel.orZero(),
                shopScoreMin = nextGrade?.shopScoreMin.orZero(),
                gradeName = nextGrade?.gradeName ?: ShopGrade.UNDEFINED,
                gradeBadgeUrl = nextGrade?.imgBadgeUrl.orEmpty(),
                benefitList = data.nextPMBenefits?.map { it.benefitName }.orEmpty()
        )
    }

    private fun getCurrentShopGradeBenefit(data: PMFinalPeriodUiModel): WidgetExpandableUiModel {
        val grade = data.currentPMGrade
        val benefits = mutableListOf<BaseExpandableItemUiModel>()
        data.currentPMBenefits?.forEach { benefit ->
            if (!benefits.any { it.text == benefit.categoryName }) {
                benefits.add(ExpandableSectionUiModel(benefit.categoryName))
            }
            if (benefit.benefitName.isNotBlank()) {
                benefits.add(ExpandableItemUiModel(benefit.benefitName, benefit.appLink.orEmpty()))
            }
        }
        return WidgetExpandableUiModel(
                title = getString(R.string.pm_benefit_shop_grade, grade?.gradeName.orEmpty()),
                items = benefits
        )
    }

    private fun getShopGradeWidgetData(data: PMFinalPeriodUiModel): WidgetShopGradeUiModel {
        val shopGrade = data.currentPMGrade
        return WidgetShopGradeUiModel(
                shopGrade = shopGrade?.gradeName.orEmpty(),
                shopScore = shopGrade?.shopScore.orZero(),
                threshold = 0,
                shopLevel = shopGrade?.shopLevel ?: Constant.ShopLevel.UNDEFINED,
                gradeBadgeImgUrl = shopGrade?.imgBadgeUrl.orEmpty(),
                gradeBackgroundUrl = shopGrade?.backgroundUrl.orEmpty(),
                nextPmCalculationDate = data.nextMonthlyRefreshDate,
                pmStatus = data.pmStatus
        )
    }

    private fun getHeaderWidgetData(shopInfo: PMShopInfoUiModel): WidgetRegistrationHeaderUiModel {
        return WidgetRegistrationHeaderUiModel(
                shopInfo = shopInfo,
                terms = PMRegistrationTermHelper.getPMRegistrationTerms(requireContext(), shopInfo)
        )
    }

    private fun getPotentialBenefitWidgetData(): WidgetPotentialUiModel {
        return WidgetPotentialUiModel(listOf(
                PotentialItemUiModel(
                        resDrawableIcon = R.drawable.ic_pm_benefit_01,
                        description = getString(R.string.pm_potential_benefit_01)
                ),
                PotentialItemUiModel(
                        resDrawableIcon = R.drawable.ic_pm_benefit_02,
                        description = getString(R.string.pm_potential_benefit_02)
                ),
                PotentialItemUiModel(
                        resDrawableIcon = R.drawable.ic_pm_benefit_03,
                        description = getString(R.string.pm_potential_benefit_03)
                )
        ))
    }
}