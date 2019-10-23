package com.tokopedia.topads.detail_sheet

import android.content.Context
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.topads.detail_sheet.data.Data
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
    var editTopAdsClick: (() -> Unit)? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: TopAdsSheetViewModel

    private fun getComponent(context: Context): TopAdsSheetComponent = DaggerTopAdsSheetComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent).build()

    private fun initComponent(context: Context){
        getComponent(context).inject(this)
        viewModel = ViewModelProviders.of(context as BaseSimpleActivity, viewModelFactory).get(TopAdsSheetViewModel::class.java)
        viewModel.getAdsProduct("42343963", "2019-10-16", "2019-10-22", this::onSuccessGetAds, this::onErrorGetAds)
    }

    private fun onErrorGetAds(throwable: Throwable) {
        throwable.printStackTrace()
    }

    private fun onSuccessGetAds(data: Data) {
        Log.d("INFO", data.productName)
    }

    private fun setupView(context: Context) {
        dialog!!.setOnShowListener { dialogInterface ->
            val dialog = dialogInterface as BottomSheetDialog
            val frameLayout = dialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            if (frameLayout != null) {
                val behavior = BottomSheetBehavior.from(frameLayout)
                behavior.isHideable = false
            }
        }
        dialog?.let {
            it.action_to_topads_dashboard.setOnClickListener {
                RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_DASHBOARD)
            }
            it.action_edit_ads.setOnClickListener{
                editTopAdsClick?.invoke()
            }
            it.toggle_switch_ads.setOnCheckedChangeListener { buttonView, isChecked ->
                when(isChecked){
                    true -> it.txt_active_status.setText(context.getString(R.string.aktif))
                    false -> it.txt_active_status.setText(context.getString(R.string.tidak_aktif))
                }
            }
        }
    }

    fun show() {
        dialog!!.show()
    }

    fun dismissDialog() {
        dialog!!.dismiss()
    }

    companion object {

        fun newInstance(context: Context): TopAdsDetailSheet {
            val fragment = TopAdsDetailSheet()
            fragment.dialog = BottomSheetDialog(context, R.style.TopAdsDetailBottomSheetDialogTheme)
            fragment.dialog!!.setContentView(R.layout.pdp_topads_detail_sheet)
            fragment.setupView(context)
            fragment.initComponent(context)
            return fragment
        }
    }
}
