package com.tokopedia.topads.auto.view.fragment

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast

import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.router.TopAdsAutoRouter
import com.tokopedia.topads.auto.view.activity.AutoAdsActivatedActivity
import com.tokopedia.topads.auto.view.activity.InsufficientBalanceActivity
import com.tokopedia.topads.auto.view.widget.InfoAutoAdsSheet
import com.tokopedia.topads.auto.view.widget.ManualAdsConfirmationSheet
import com.tokopedia.topads.common.constant.TopAdsReasonOption

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
            when(it!!.adsInfo.reason){
                TopAdsReasonOption.INSUFFICIENT_CREDIT -> insufficientCredit(it!!.adsInfo.message)
                TopAdsReasonOption.ELIGIBLE -> eligible()
                TopAdsReasonOption.NOT_ELIGIBLE -> notEligible()
                else -> activity!!.finish()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_info, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
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
            activatedAds()
        }
    }

    override fun onManualAdsClicked() {
        val intent = (activity!!.application as TopAdsAutoRouter)
                .getTopAdsAddingPromoOptionIntent(activity!!)
        startActivityForResult(intent, REQUEST_CODE_AD_OPTION)
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

        fun newInstance(budget: Int): InitialBudgetFragment {

            val args = Bundle()
            args.putInt(KEY_DAILY_BUDGET, budget)
            val fragment = InitialBudgetFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
