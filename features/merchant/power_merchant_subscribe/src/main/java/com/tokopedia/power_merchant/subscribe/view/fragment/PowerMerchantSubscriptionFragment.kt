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
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.power_merchant.subscribe.view.bottomsheet.PowerMerchantNotificationBottomSheet
import com.tokopedia.power_merchant.subscribe.view.helper.PMRegistrationTermHelper
import com.tokopedia.power_merchant.subscribe.view.model.*
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PowerMerchantSubscriptionViewModel
import com.tokopedia.power_merchant.subscribe.view.viewmodel.SubscriptionActivityViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_pm_power_merchant_subscription.view.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class PowerMerchantSubscriptionFragment : BaseListFragment<BaseWidgetUiModel, WidgetAdapterFactoryImpl>() {

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

    private val recyclerView: RecyclerView?
        get() = super.getRecyclerView(view)

    override fun getScreenName(): String = GMParamTracker.ScreenName.PM_UPGRADE_SHOP

    override fun initInjector() {
        getComponent(PowerMerchantSubscribeComponent::class.java).inject(this)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvPmRegistration

    override fun getAdapterTypeFactory(): WidgetAdapterFactoryImpl = WidgetAdapterFactoryImpl()

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
        when (data.periodeType) {
            PeriodType.TRANSITION_PERIOD -> observeShopInfoAndPMGradeBenefits()
            PeriodType.FINAL_PERIOD -> renderDummyFinalPeriod()
        }
    }

    private fun observeShopInfoAndPMGradeBenefits() {
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
        val needTnC = (!shopInfo.isKyc && shopInfo.isEligibleShopScore) || (shopInfo.isKyc && shopInfo.isEligibleShopScore)
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
                shopInfo.isEligiblePm -> submitPMRegistration()
                !shopInfo.isKyc && ((!shopInfo.isNewSeller && shopInfo.isEligibleShopScore)
                        || (shopInfo.isNewSeller && shopInfo.hasActiveProduct)) -> submitKYC()
                else -> showRegistrationTermBottomSheet(shopInfo)
            }
        }
    }

    private fun submitKYC() {
        RouteManager.route(context, ApplinkConst.KYC_SELLER_DASHBOARD)
    }

    private fun showRegistrationTermBottomSheet(shopInfo: PMShopInfoUiModel) {
        val isNewSeller = shopInfo.isNewSeller
        val title = if (isNewSeller) {
            getString(R.string.pm_bottom_sheet_active_product_title)
        } else {
            getString(R.string.pm_bottom_sheet_shop_score_title)
        }
        val description = if (isNewSeller) {
            getString(R.string.pm_bottom_sheet_active_product_description)
        } else {
            getString(R.string.pm_bottom_sheet_shop_score_description)
        }
        val illustrationUrl = if (isNewSeller) {
            Constant.ImageUrl.ADD_PRODUCT_BOTTOM_SHEET
        } else {
            Constant.ImageUrl.SHOP_SCORE_BOTTOM_SHEET
        }
        val ctaText = getString(R.string.pm_learn_shop_performance)
        val appLink = if (isNewSeller) {
            ApplinkConst.SellerApp.PRODUCT_ADD
        } else {
            ApplinkConst.SHOP_SCORE_DETAIL
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

    private fun submitPMRegistration() {

    }

    private fun renderDummyFinalPeriod() {
        view?.pmRegistrationFooterView?.gone()
        val widgets = listOf(
                //WidgetQuitSubmissionUiModel(),
                WidgetShopGradeUiModel(),
                WidgetDividerUiModel,
                WidgetExpandableUiModel(
                        title = "Keuntungan PM Silver",
                        items = listOf(
                                ExpandableSectionUiModel("PENGATURAN PRODUK"),
                                ExpandableItemUiModel("Limit 2000 produk & 200 etalase", ApplinkConst.GOLD_MERCHANT_STATISTIC_DASHBOARD),
                                ExpandableItemUiModel("Limit 2000 produk & 200 etalase"),
                                ExpandableSectionUiModel("PROMOSI PRODUK"),
                                ExpandableItemUiModel("Gratis 200 kuota layanan Broadcast Chat", ApplinkConst.GOLD_MERCHANT_STATISTIC_DASHBOARD),
                                ExpandableItemUiModel("Promosikan toko dengan ekstra 5% kredit TopAds. Yuk, pasang iklan TopAds!", ApplinkConst.GOLD_MERCHANT_STATISTIC_DASHBOARD)
                        )
                ),
                WidgetDividerUiModel,
                WidgetSingleCtaUiModel("Pelajari Selengkapnya Tentang Power Merchant", ApplinkConst.GOLD_MERCHANT_STATISTIC_DASHBOARD)
        )
        renderList(widgets)
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