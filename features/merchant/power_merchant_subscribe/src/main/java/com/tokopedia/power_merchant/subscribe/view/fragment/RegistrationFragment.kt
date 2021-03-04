package com.tokopedia.power_merchant.subscribe.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.gm.common.constant.GMParamTracker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.power_merchant.subscribe.view.model.*
import kotlinx.android.synthetic.main.fragment_pm_registration.view.*

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class RegistrationFragment : BaseListFragment<BaseWidgetUiModel, WidgetAdapterFactoryImpl>() {

    companion object {
        fun createInstance(): RegistrationFragment {
            return RegistrationFragment()
        }
    }

    private val recyclerView by lazy { super.getRecyclerView(view) }

    override fun getScreenName(): String = GMParamTracker.ScreenName.PM_UPGRADE_SHOP

    override fun initInjector() {
        activity?.let {
            val appComponent = (it.application as BaseMainApplication).baseAppComponent
            DaggerPowerMerchantSubscribeComponent.builder()
                    .baseAppComponent(appComponent)
                    .build()
                    .inject(this)
        }
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvPmRegistration

    override fun getAdapterTypeFactory(): WidgetAdapterFactoryImpl = WidgetAdapterFactoryImpl()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pm_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //renderDummyTransition()
        renderDummyFinalPeriod()
    }

    override fun onItemClicked(t: BaseWidgetUiModel?) {}

    override fun loadData(page: Int) {}

    private fun renderDummyFinalPeriod() {
        view?.pmRegistrationFooterView?.gone()
        val widgets = listOf(
                WidgetQuitSubmissionUiModel(),
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
                WidgetRegistrationHeaderUiModel(
                        terms = listOf(
                                RegistrationTermUiModel(
                                        title = "Skor Toko: <font color=\"#03AC0E\">85</font>",
                                        description = "Skormu sudah mencapai batas <b>min. skor 60</b> untuk daftar Power Merchant. Skor akan diperbarui setiap Senin, ya.",
                                        isChecked = true
                                ),
                                RegistrationTermUiModel(
                                        title = "Data Diri Belum Terverifikasi",
                                        description = "Daftar langsung untuk bisa segera diverifikasi. yuk! Jika berhasil, tokomu akan jadi Power Merchant.",
                                        isChecked = false
                                )
                        )
                ),
                WidgetPotentialUiModel(listOf(
                        PotentialItemUiModel(
                                imgUrl = "https://www.freeiconspng.com/uploads/baby-icon-14.jpg",
                                description = "Lebih sering dikunjungi pembeli hingga <br><b>4,3 kali lipat</b>"
                        ),
                        PotentialItemUiModel(
                                imgUrl = "https://www.freeiconspng.com/uploads/baby-icon-14.jpg",
                                description = "Menerima pesanan lebih banyak hingga <br><b>5 kali lipat</b>"
                        ),
                        PotentialItemUiModel(
                                imgUrl = "https://www.freeiconspng.com/uploads/baby-icon-14.jpg",
                                description = "Meningkatkan penghasilan hingga <br><b>5,5 kali lipat</b>"
                        )
                )),
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
}