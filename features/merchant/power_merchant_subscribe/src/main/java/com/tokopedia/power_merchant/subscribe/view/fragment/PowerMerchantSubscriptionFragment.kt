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
        val shopKycResIcon: Int = when (shopInfo.kycStatusId) {
            KYCStatusId.VERIFIED, KYCStatusId.APPROVED -> R.drawable.ic_pm_checked
            KYCStatusId.NOT_VERIFIED -> R.drawable.ic_pm_not_checked
            KYCStatusId.PENDING -> R.drawable.ic_pm_waiting
            else -> R.drawable.ic_pm_failed
        }

        return WidgetRegistrationHeaderUiModel(
                shopInfo = shopInfo,
                terms = listOf(
                        getFirstPmRegistrationTerm(shopInfo),
                        RegistrationTermUiModel(
                                title = "Data Diri Belum Terverifikasi",
                                descriptionHtml = "Daftar langsung untuk bisa segera diverifikasi. yuk! Jika berhasil, tokomu akan jadi Power Merchant.",
                                resDrawableIcon = shopKycResIcon
                        )
                )
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
        if (shopInfo.isNewSeller) { //new seller
            if (shopInfo.hasActiveProduct) {
                title = getString(R.string.pm_already_have_one_active_product)
                description = getString(R.string.pm_label_already_have_one_active_product)
            } else {
                title = getString(R.string.pm_have_not_one_active_product_yet)
                description = getString(R.string.pm_label_have_not_one_active_product_yet)
            }
        } else { //existing seller
            if (shopInfo.isEligibleShopScore) {
                title = "Skor Toko: <font color=\"#03AC0E\">85</font>"
                description = "Skormu sudah mencapai batas <strong>min. skor 60</strong> untuk daftar Power Merchant. Skor akan diperbarui setiap Senin, ya."
            } else {
                title = "Skor Toko: <font color=\"#D6001C\">85</font>"
                description = "Tingkatkan skor tokomu <strong>min. skor 60</strong> untuk mendaftar jadi Power Merchant, ya. <br><strong><a style=\"color:#ddd;\" href=\"tokopedia://shop-score-detail\">Pelajari Performa Toko</a></strong>"
            }
        }

        return RegistrationTermUiModel(
                title = title,
                descriptionHtml = description,
                resDrawableIcon = shopScoreResIcon
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