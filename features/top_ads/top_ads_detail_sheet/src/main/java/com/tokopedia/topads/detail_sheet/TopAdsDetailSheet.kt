package com.tokopedia.topads.detail_sheet

import android.app.Activity
import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.widget.FrameLayout
import android.widget.Switch
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.topads.detail_sheet.data.Data
import com.tokopedia.topads.detail_sheet.data.Bulk
import com.tokopedia.topads.detail_sheet.di.DaggerTopAdsSheetComponent
import com.tokopedia.topads.detail_sheet.di.TopAdsSheetComponent
import com.tokopedia.topads.detail_sheet.viewmodel.TopAdsSheetViewModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.pdp_topads_detail_sheet.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 07,May,2019
 */
class TopAdsDetailSheet {

    private var dialog: BottomSheetDialog? = null
    var detailTopAdsClick: (() -> Unit)? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: TopAdsSheetViewModel

    private fun getComponent(context: Context): TopAdsSheetComponent = DaggerTopAdsSheetComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent).build()

    private fun initComponent(context: Context) {
        getComponent(context).inject(this)
        viewModel = ViewModelProviders.of(context as BaseSimpleActivity, viewModelFactory).get(TopAdsSheetViewModel::class.java)
    }

    private fun onErrorGetAds(throwable: Throwable) {
        dialog?.let {
            NetworkErrorHelper.showSnackbar(it.context as Activity, throwable.localizedMessage)
        }
    }

    private fun onSuccessGetAds(data: Data) {
        dialog?.let {
            ImageLoader.LoadImage(it.img_thumb, data.productImageUri)
            it.title.setText(data.productName)
            it.subtitle.setText(String.format("%s %s", data.adPriceBidFmt, data.labelPerClick))
            it.txt_klik.setText(data.statTotalClick)
            it.txt_tampil.setText(data.statTotalImpression)
            it.txt_terjual.setText(data.statTotalConversion)
            it.toggle_switch_ads.isChecked = data.adStatusToogle == 1
            it.txt_active_status.setText(data.adStatusDesc)
            it.toggle_switch_ads.setOnClickListener {
                when ((it as Switch).isChecked) {
                    true -> viewModel.postPromo("toggle_on", data.adId, this::onSuccessPost, this::onErrorPost)
                    false -> viewModel.postPromo("toggle_off", data.adId, this::onSuccessPost, this::onErrorPost)
                }
            }
        }
    }

    private fun setupView(context: Context) {
        dialog?.let {
            it.setOnShowListener { dialogInterface ->
                val dialog = dialogInterface as BottomSheetDialog
                val frameLayout = dialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
                if (frameLayout != null) {
                    val behavior = BottomSheetBehavior.from(frameLayout)
                    behavior.isHideable = false
                }
            }
            it.action_to_topads_dashboard.setOnClickListener {
                RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_DASHBOARD)
            }
            it.action_edit_ads.setOnClickListener {
                detailTopAdsClick?.invoke()
            }
        }
    }

    fun onSuccessPost(data: Bulk) {
        dialog?.let {
            it.toggle_switch_ads.isChecked = data.action.equals("toggle_on")
            it.txt_active_status.setText(data.ads.get(0)?.statusDesc)
        }
    }

    fun onErrorPost(throwable: Throwable) {
        dialog?.let {
            it.toggle_switch_ads.isChecked = false
            it.txt_active_status.setText(it.context.getString(R.string.tidak_aktif))
            NetworkErrorHelper.showSnackbar(it.context as Activity, throwable.localizedMessage)
        }
    }

    fun show(adId: String) {
        viewModel.getAdsProduct(adId, this::onSuccessGetAds, this::onErrorGetAds)
        dialog!!.show()
    }

    fun dismissDialog() {
        dialog!!.dismiss()
    }

    companion object {

        fun newInstance(context: Context): TopAdsDetailSheet {
            val fragment = TopAdsDetailSheet()
            fragment.dialog = BottomSheetDialog(context, R.style.TopAdsDetailBottomSheetDialogTheme)
            fragment.dialog?.let {
                it.setContentView(R.layout.pdp_topads_detail_sheet)
                fragment.setupView(context)
                fragment.initComponent(context)
            }
            return fragment
        }
    }
}
