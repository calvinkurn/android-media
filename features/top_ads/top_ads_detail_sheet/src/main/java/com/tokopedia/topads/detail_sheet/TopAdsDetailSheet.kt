package com.tokopedia.topads.detail_sheet

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.topads.auto.view.widget.AutoAdsWidget
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.detail_sheet.data.AdData
import com.tokopedia.topads.detail_sheet.di.DaggerTopAdsSheetComponent
import com.tokopedia.topads.detail_sheet.di.TopAdsSheetComponent
import com.tokopedia.topads.detail_sheet.viewmodel.TopAdsSheetViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.topads_pdp_bottom_sheet_single_ad.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 07,May,2019
 */
class TopAdsDetailSheet : BottomSheetUnify() {

    private var dialog: BottomSheetDialog? = null
    private var groupId: String = "0"
    val autoAdsWidget: AutoAdsWidget?
        get() = autoads_widget

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: TopAdsSheetViewModel

    private fun getComponent(context: Context): TopAdsSheetComponent = DaggerTopAdsSheetComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent).build()


    private fun onErrorGetAds(throwable: Throwable) {
        dialog?.let {
            NetworkErrorHelper.showSnackbar(it.context as Activity, throwable.localizedMessage)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getComponent(context!!).inject(this)
        viewModel = ViewModelProviders.of(context as BaseSimpleActivity, viewModelFactory).get(TopAdsSheetViewModel::class.java)
        setFragment()
        bottomSheetBehaviorKnob(view, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val childView = View.inflate(context, R.layout.topads_pdp_bottom_sheet_single_ad, null)
        setChild(childView)
        showCloseIcon = false
        setTitle(getString(R.string.detail_iklan))
    }

    private fun onSuccessPost(action: String) {
        btn_switch?.isChecked = action == ACTION_ACTIVATE
    }


    private fun setFragment() {
        val adId = arguments?.getString("adId") ?: "0"
        val categoryType = arguments?.getInt("cat") ?: 3
        val adType = arguments?.getString("adType") ?: "0"
        viewModel.getGroupId(userSession.shopId, adId, ::onSuccessGroupId)
        viewModel.getProductStats(resources, listOf(adId), ::onsuccessProductStats)
        when (categoryType) {

            3 -> {
                autoads_widget?.visibility = View.GONE
            }
            4 -> {
                imgProduct?.visibility = View.GONE
                txtTitleProduct?.visibility = View.GONE
                txtBudget?.visibility = View.GONE
                btn_switch?.visibility = View.GONE
                autoads_widget?.visibility = View.VISIBLE
                autoAdsWidget?.loadData(2)

                if (adId.isNotEmpty() && adId.toInt() > 0 && adType == "1") {
                    editAd?.text = getString(R.string.topads_detail_sheet_auto)
                    viewModel.getProductStats(resources, listOf(adId), ::onsuccessProductStats)
                } else {
                    editAd?.text = getString(R.string.topads_detail_sheet_dashboard)
                    tickerInfo?.visibility = View.VISIBLE
                    adInfo?.visibility = View.GONE
                }
            }
        }

        editAd.setOnClickListener {
            if (categoryType == 3) {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS)?.apply {
                    putExtra(GROUPID, groupId)
                }
                startActivity(intent)
            } else if (categoryType == 4) {
                if ((arguments?.getString("adId")
                                ?: "0").toInt() > 0 && arguments?.getString("adTType") == "1") {


                    val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_AUTOADS)
                    startActivity(intent)
                }
                else{
                    RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_SELLER)
                }
            }
        }

        btn_switch?.setOnClickListener {
            when ((it as Switch).isChecked) {
                true -> viewModel.setProductAction(::onSuccessPost, ACTION_ACTIVATE,
                        listOf(adId), resources, null)
                false -> viewModel.setProductAction(::onSuccessPost, ACTION_DEACTIVATE,
                        listOf(adId), resources, null)
            }
        }

        createAd?.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_SELLER)
        }
    }

    private fun onSuccessGroupId(data: List<AdData>) {
        groupId = data[0].groupID
        btn_switch?.isChecked = data[0].status == STATUS_ACTIVE || data[0].status == STATUS_TIDAK_TAMPIL
        txtBudget?.text = String.format(getString(R.string.topads_detail_budget), data[0].priceBid)
        viewModel.getGroupProductData(resources, groupId.toInt(), ::onSuccessProductInfo)
        //TODO eligibility for single ads
        if (groupId == "0") {
            singleAd.visibility = View.VISIBLE
            createGroup.setOnClickListener {
                RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_CREATE_ADS)
            }
        }
    }

    private fun onSuccessProductInfo(data: List<WithoutGroupDataItem>) {
        data.forEachIndexed { index, it ->
            if (it.adId.toString() == arguments?.getString("adId") ?: "0") {
                ImageHandler.LoadImage(imgProduct, data[index].productImageUri)
                txtTitleProduct.text = data[index].productName
                pendapatan_count.text = data[index].statTotalGrossProfit
                return@forEachIndexed
            }
        }
    }


    private fun onsuccessProductStats(data: List<WithoutGroupDataItem>) {
        tampil_count.text = data[0].statTotalImpression
        klik_count.text = data[0].statTotalClick
        persentase_klik_count.text = data[0].statTotalCtr
        pengeluaran_count.text = data[0].statTotalSpent
        produk_terjual_count.text = data[0].statTotalConversion
    }

    companion object {
        const val ACTION_ACTIVATE = "toggle_on"
        const val ACTION_DEACTIVATE = "toggle_off"
        const val GROUPID = "groupId"
        const val STATUS_ACTIVE = "1"
        const val STATUS_TIDAK_TAMPIL = "2"

        fun newInstance(adType: String, adId: String, categoryType: Int): TopAdsDetailSheet =
                TopAdsDetailSheet().also {
                    it.arguments = Bundle().apply {
                        putString("adType", adType)
                        putInt("cat", categoryType)
                        putString("adId", adId)
                    }
                }
    }
}
