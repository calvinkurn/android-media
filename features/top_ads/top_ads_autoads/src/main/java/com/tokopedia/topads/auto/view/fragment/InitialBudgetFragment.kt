package com.tokopedia.topads.auto.view.fragment

import androidx.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import com.tokopedia.applink.AppUtil
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds

import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.internal.TopAdsUserStatusInfo
import com.tokopedia.topads.auto.view.activity.ConfirmationDialogActivity
import com.tokopedia.topads.auto.view.widget.InfoAutoAdsSheet
import com.tokopedia.topads.auto.view.widget.ManualAdsConfirmationSheet

/**
 * Author errysuprayogi on 14,May,2019
 */
class InitialBudgetFragment : DailyBudgetFragment(), View.OnClickListener, ManualAdsConfirmationSheet.ActionListener {


    private lateinit var adsConfirmationSheet: ManualAdsConfirmationSheet
    private lateinit var btnStartManual: Button
    private lateinit var btnStartAuto: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        adsConfirmationSheet = ManualAdsConfirmationSheet.newInstance(activity!!)
        budgetViewModel.autoAdsData.observe(this, Observer {
            if (topAdsDeposit < 0) {
                insufficientCredit()
            } else
                eligible()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.menu_info, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = item!!.itemId
        if (i == R.id.action_info) {
            InfoAutoAdsSheet.newInstance(activity!!).show()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.start_manual_ads_btn) {
            adsConfirmationSheet.show()
        } else if (id == R.id.post_btn) {
            if(shopStatus == TopAdsUserStatusInfo.MANUAL_USER){
                startActivityForResult(Intent(activity!!, ConfirmationDialogActivity::class.java), REQUEST_CODE_CONFIRMATION)
            } else{
                activatedAds()
            }
        }
    }

    override fun onManualAdsClicked() {
        if(AppUtil.isSellerInstalled(context)) {
            RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_CREATE_ADS)
        } else {
            RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL)
        }
    }

    override fun onAutoAdsClicked() {
        adsConfirmationSheet.dismissDialog()
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_start_daily_budget
    }

    override fun setUpView(view: View) {
        btnStartManual = view.findViewById(R.id.start_manual_ads_btn)
        btnStartAuto = view.findViewById(R.id.post_btn)
    }

    override fun setListener() {
        super.setListener()
        btnStartManual.setOnClickListener(this)
        btnStartAuto.setOnClickListener(this)
        adsConfirmationSheet.setActionListener(this)
    }

    override fun showLoading() {
        cardView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        btnStartAuto.isEnabled = false
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
        cardView.visibility = View.VISIBLE
        btnStartAuto.isEnabled = true
    }

    override fun getScreenName(): String? {
        return null
    }

    companion object {

        fun newInstance(status: Int, budget: Int): InitialBudgetFragment {
            val args = Bundle()
            args.putInt(KEY_DAILY_BUDGET, budget)
            args.putInt(KEY_AUTOADS_STATUS, status)
            val fragment = InitialBudgetFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
