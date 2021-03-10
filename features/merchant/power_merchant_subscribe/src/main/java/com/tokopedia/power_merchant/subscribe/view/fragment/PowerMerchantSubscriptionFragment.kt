package com.tokopedia.power_merchant.subscribe.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.gm.common.constant.GMParamTracker
import com.tokopedia.gm.common.constant.KYCStatusId
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.power_merchant.subscribe.view.model.*
import kotlinx.android.synthetic.main.fragment_pm_power_merchant_subscription.view.*

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class PowerMerchantSubscriptionFragment : BaseListFragment<BaseWidgetUiModel, WidgetAdapterFactoryImpl>() {

    companion object {
        fun createInstance(): PowerMerchantSubscriptionFragment {
            return PowerMerchantSubscriptionFragment()
        }
    }

    private val recyclerView by lazy { super.getRecyclerView(view) }

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

        renderDummyTransition()
        //renderDummyFinalPeriod()
    }

    override fun onItemClicked(t: BaseWidgetUiModel?) {}

    override fun loadData(page: Int) {}

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

    private fun renderDummyTransition() {
        view?.pmRegistrationFooterView?.visible()
        val gradeBenefits = listOf(
                GradeBenefitItemUiModel(
                        iconUrl = "https://www.freeiconspng.com/uploads/baby-icon-14.jpg",
                        description = "Dapat menampilkan 5 produk unggulan di bagian atas tokomu"
                ),
                GradeBenefitItemUiModel(
                        iconUrl = "https://www.freeiconspng.com/uploads/baby-icon-14.jpg",
                        description = "Dapat menampilkan 5 produk unggulan di bagian atas tokomu"
                ),
                GradeBenefitItemUiModel(
                        iconUrl = "https://www.freeiconspng.com/uploads/baby-icon-14.jpg",
                        description = "Dapat menampilkan 5 produk unggulan di bagian atas tokomu"
                ),
                GradeBenefitItemUiModel(
                        iconUrl = "https://www.freeiconspng.com/uploads/baby-icon-14.jpg",
                        description = "Dapat menampilkan 5 produk unggulan di bagian atas tokomu"
                ),
                GradeBenefitItemUiModel(
                        iconUrl = "https://www.freeiconspng.com/uploads/baby-icon-14.jpg",
                        description = "Dapat menampilkan 5 produk unggulan di bagian atas tokomu"
                ),
                GradeBenefitItemUiModel(
                        iconUrl = "https://www.freeiconspng.com/uploads/baby-icon-14.jpg",
                        description = "Dapat menampilkan 5 produk unggulan di bagian atas tokomu"
                )
        )

        val widgets = listOf(
                getHeaderWidgetData(PMShopInfoUiModel()),
                getPotentialBenefitWidgetData(),
                WidgetDividerUiModel,
                WidgetGradeBenefitUiModel(
                        benefitPages = listOf(
                                GradeBenefitPagerUiModel(
                                        title = "Bronze",
                                        isSelected = false,
                                        gradeBenefits = gradeBenefits
                                ),
                                GradeBenefitPagerUiModel(
                                        title = "Silver",
                                        isSelected = false,
                                        gradeBenefits = gradeBenefits
                                ),
                                GradeBenefitPagerUiModel(
                                        title = "Gold",
                                        isSelected = true,
                                        gradeBenefits = gradeBenefits
                                ),
                                GradeBenefitPagerUiModel(
                                        title = "Diamond",
                                        isSelected = false,
                                        gradeBenefits = gradeBenefits
                                )
                        )
                )
        )

        renderList(widgets)
    }

    private fun getHeaderWidgetData(shopInfo: PMShopInfoUiModel): WidgetRegistrationHeaderUiModel {
        return WidgetRegistrationHeaderUiModel(
                shopInfo = shopInfo,
                terms = listOf(getFirstPmRegistrationTerm(shopInfo), getSecondPmRegistrationTerm(shopInfo))
        )
    }

    private fun getFirstPmRegistrationTerm(shopInfo: PMShopInfoUiModel): RegistrationTermUiModel {
        val shopScoreResIcon: Int = if ((shopInfo.isNewSeller && shopInfo.hasActiveProduct) || shopInfo.isEligibleShopScore) {
            R.drawable.ic_pm_checked
        } else {
            R.drawable.ic_pm_not_checked
        }

        val title: String
        val description: String
        var ctaText: String? = null
        var ctaAppLink: String? = null
        if (shopInfo.isNewSeller) { //new seller
            if (shopInfo.hasActiveProduct) {
                title = getString(R.string.pm_already_have_one_active_product)
                description = getString(R.string.pm_label_already_have_one_active_product)
            } else {
                title = getString(R.string.pm_have_not_one_active_product_yet)
                description = getString(R.string.pm_label_have_not_one_active_product_yet)
                ctaText = getString(R.string.pm_add_product)
                ctaAppLink = ApplinkConst.SellerApp.PRODUCT_ADD
            }
        } else { //existing seller
            if (shopInfo.isEligibleShopScore) {
                val textColor = "#" + Integer.toHexString(requireContext().getResColor(com.tokopedia.unifyprinciples.R.color.Unify_G500))
                title = getString(R.string.pm_shop_score_eligible, textColor, shopInfo.shopScore)
                description = getString(R.string.pm_shop_score_eligible_description, shopInfo.shopScoreThreshold)
            } else {
                val textColor = "#" + Integer.toHexString(requireContext().getResColor(com.tokopedia.unifyprinciples.R.color.Unify_R600))
                title = getString(R.string.pm_shop_score_not_eligible, textColor, shopInfo.shopScore)
                description = getString(R.string.pm_shop_score_not_eligible_description, shopInfo.shopScoreThreshold)
                ctaText = getString(R.string.pm_learn_shop_performance)
                ctaAppLink = ApplinkConst.SHOP_SCORE_DETAIL
            }
        }

        return RegistrationTermUiModel(
                title = title,
                descriptionHtml = description,
                resDrawableIcon = shopScoreResIcon,
                clickableText = ctaText,
                appLinkOrUrl = ctaAppLink
        )
    }

    private fun getSecondPmRegistrationTerm(shopInfo: PMShopInfoUiModel): RegistrationTermUiModel {
        val shopKycResIcon: Int
        val title: String
        val description: String
        when (shopInfo.kycStatusId) {
            KYCStatusId.VERIFIED, KYCStatusId.APPROVED -> {
                title = getString(R.string.pm_kyc_verified)
                description = getString(R.string.pm_description_kyc_verified)
                shopKycResIcon = R.drawable.ic_pm_checked
            }
            KYCStatusId.NOT_VERIFIED -> {
                title = getString(R.string.pm_kyc_not_verified)
                description = getString(R.string.pm_description_kyc_not_verified)
                shopKycResIcon = R.drawable.ic_pm_not_checked
            }
            KYCStatusId.PENDING -> {
                title = getString(R.string.pm_kyc_verification_waiting)
                description = getString(R.string.pm_description_kyc_verification_waiting)
                shopKycResIcon = R.drawable.ic_pm_waiting
            }
            else -> {
                title = getString(R.string.pm_verification_failed)
                description = getString(R.string.pm_description_kyc_verification_failed)
                shopKycResIcon = R.drawable.ic_pm_failed
            }
        }
        return RegistrationTermUiModel(
                title = title,
                descriptionHtml = description,
                resDrawableIcon = shopKycResIcon
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