package com.tokopedia.power_merchant.subscribe.view.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.GMParamTracker
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.Constant
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.adapter.PMFeatureAdapter
import com.tokopedia.power_merchant.subscribe.view.bottomsheet.ContentSliderBottomSheet
import com.tokopedia.power_merchant.subscribe.view.bottomsheet.PowerMerchantCancelBottomSheet
import com.tokopedia.power_merchant.subscribe.view.model.ContentSliderUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.fragment_pm_subscription.view.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 25/02/21
 */

class SubscriptionFragment : BaseFragment() {

    companion object {
        fun createInstance(): SubscriptionFragment {
            return SubscriptionFragment()
        }
    }

    @Inject
    lateinit var pmFeatureAdapter: PMFeatureAdapter

    private var bottomSheetCancel: PowerMerchantCancelBottomSheet? = null

    override fun getScreenName(): String = GMParamTracker.ScreenName.PM_UPGRADE_SHOP

    override fun getResLayout(): Int = R.layout.fragment_pm_subscription

    override fun initInjector() {
        activity?.let {
            val appComponent = (it.application as BaseMainApplication).baseAppComponent
            DaggerPowerMerchantSubscribeComponent.builder()
                    .baseAppComponent(appComponent)
                    .build()
                    .inject(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observePmStatusInfo()
        observeTickers()
    }

    private fun setupView() = view?.run {
        setupPmFeatures()

        val unsubscribeText = getString(R.string.power_merchant_cancel_membership)
        tvPmUnsubscribe.text = unsubscribeText.parseAsHtml()
        tvPmUnsubscribe.setOnClickListener {
            showUnsubscribeBottomSheet()
        }

        btnPmLearnMore.setOnClickListener {
            openPmEdu()
        }

        tvPmLabelFeatures.setBackgroundResource(R.drawable.bg_pm_feature_title)

        setOnSuccessGetPMStatus()
    }

    private fun setupPmFeatures() = view?.rvPmFeatures?.run {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = pmFeatureAdapter

        pmFeatureAdapter.setItems(Constant.Subscription.getPmFeatureList(context))
        pmFeatureAdapter.notifyDataSetChanged()
    }

    private fun setOnSuccessGetPMStatus() {
        setupPMCancelBottomSheet()
    }

    private fun setupPMCancelBottomSheet() {
        bottomSheetCancel = PowerMerchantCancelBottomSheet.newInstance("20 Jul 2021", true)
                .apply {
                    setListener(object : PowerMerchantCancelBottomSheet.BottomSheetCancelListener {
                        override fun onClickCancelButton() {

                        }

                        override fun onClickBackButton() {

                        }
                    })
                }
    }

    private fun showUnsubscribeBottomSheet() {
        bottomSheetCancel?.show(childFragmentManager)
    }

    private fun observeTickers() {
        view?.tickerPmCommunicationPeriod?.tickerTitle = "Ada perubahan pada Power Merchant"
        view?.tickerPmCommunicationPeriod?.setHtmlDescription("Perubahan Power Merchant mulai berlaku pada\n" +
                "1 Jan 2021. <a href=\"${Constant.Url.FREE_SHIPPING_TERMS_AND_CONDITION}\">Pelajari Perubahan</a>")
        view?.tickerPmCommunicationPeriod?.tickerType = Ticker.TYPE_ANNOUNCEMENT
        view?.tickerPmCommunicationPeriod?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                println("Ticker : onDescriptionViewClick -> $linkUrl")
                showPowerMerchantBottomSheet()
            }

            override fun onDismiss() {
                println("Ticker : onDismiss")
            }
        })
    }

    private fun showPowerMerchantBottomSheet() {
        if (childFragmentManager.isStateSaved) return

        val items = listOf(
                ContentSliderUiModel(
                        "Syarat Baru Power Merchant",
                        "Mulai 1 Jan 2021, ada perhitungan skor baru yang didasarkan pada level & skor tokomu. Pertahankan skor toko min. 60 poin untuk tetap jadi Power Merchant, ya.",
                        "https://images.tokopedia.net/img/android/merchant/seller_review/img_sir_thank_you.png"
                ),
                ContentSliderUiModel(
                        "Syarat Baru Power Merchant",
                        "Mulai 1 Jan 2021, ada perhitungan skor baru yang didasarkan pada level & skor tokomu. Pertahankan skor toko min. 60 poin untuk tetap jadi Power Merchant, ya.",
                        "https://images.tokopedia.net/img/android/merchant/seller_review/img_sir_thank_you.png"
                ),
                ContentSliderUiModel(
                        "Syarat Baru Power Merchant",
                        "Mulai 1 Jan 2021, ada perhitungan skor baru yang didasarkan pada level & skor tokomu. Pertahankan skor toko min. 60 poin untuk tetap jadi Power Merchant, ya.",
                        "https://images.tokopedia.net/img/android/merchant/seller_review/img_sir_thank_you.png"
                )
        )

        val mTitle = "Perubahan Power Merchant"
        val mSubTitle = "Mulai 1 Jan 2021"
        ContentSliderBottomSheet.createInstance()
                .apply {
                    setContent(mTitle, mSubTitle, items)
                    setOnCtaClickListener {

                    }
                    show(this@SubscriptionFragment.childFragmentManager)
                }
    }

    private fun observePmStatusInfo() {
        val pmStatusInfo = PowerMerchantStatus(

        )
        view?.viewPmStatus?.show(pmStatusInfo) {

        }
    }

    private fun openPmEdu() {
        RouteManager.route(context, Constant.Url.POWER_MERCHANT_EDU)
    }
}