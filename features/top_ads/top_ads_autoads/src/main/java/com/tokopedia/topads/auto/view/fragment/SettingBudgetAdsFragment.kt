package com.tokopedia.topads.auto.view.fragment

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch

import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.internal.AutoAdsStatus
import com.tokopedia.topads.auto.internal.TopAdsUserStatusInfo
import com.tokopedia.topads.auto.view.activity.ConfirmationDialogActivity
import com.tokopedia.topads.auto.view.widget.SettingAutoAdsConfirmationSheet
import com.tokopedia.topads.auto.view.widget.SettingAutoAdsInfoSheet

/**
 * Author errysuprayogi on 09,May,2019
 */
class SettingBudgetAdsFragment : DailyBudgetFragment() {

    private lateinit var infoBtn: ImageView
    private lateinit var budgetContainer: View
    private lateinit var switchBudget: Switch
    private lateinit var saveBtn: Button

    override fun getLayoutId(): Int {
        return R.layout.layout_setting_daily_budget_ads
    }

    override fun setUpView(view: View) {
        infoBtn = view.findViewById(R.id.btn_info)
        budgetContainer = view.findViewById(R.id.budget_container)
        switchBudget = view.findViewById(R.id.switch_ads)
        saveBtn = view.findViewById(R.id.post_btn)
    }

    override fun setListener() {
        super.setListener()
        infoBtn.setOnClickListener {
            SettingAutoAdsInfoSheet.newInstance(context!!).show()
        }
        saveBtn.setOnClickListener {
            if (switchBudget.isChecked) {
                if(shopStatus == TopAdsUserStatusInfo.MANUAL_USER){
                    startActivityForResult(Intent(activity!!, ConfirmationDialogActivity::class.java), REQUEST_CODE_CONFIRMATION)
                } else{
                    activatedAds()
                }
            } else {
                var settingConfirmationSheet =  SettingAutoAdsConfirmationSheet.newInstance(context!!)
                settingConfirmationSheet.setActionListener(object: SettingAutoAdsConfirmationSheet.ActionListener{
                    override fun nonActiveAutoAds() {
                        deactivedAds()
                    }

                    override fun activeAutoAds() {
                        settingConfirmationSheet.dismissDialog()
                        switchBudget.isChecked = true
                    }
                })
                settingConfirmationSheet.show()
            }
            budgetViewModel.autoAdsData.observe(this, Observer {
                when(it!!.status){
                    AutoAdsStatus.STATUS_IN_PROGRESS_ACTIVE -> inProgressActive(it!!.adsInfo)
                    AutoAdsStatus.STATUS_IN_PROGRESS_INACTIVE -> inProgressInactive()
                }
            })
        }
        switchBudget.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                showBudgetContainer()
            } else {
                hideBudgetContainer()
            }
        }
    }

    override fun showLoading(){
        cardView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        saveBtn.isEnabled = false
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
        cardView.visibility = View.VISIBLE
        saveBtn.isEnabled = true
    }

    private fun showBudgetContainer() {
        budgetContainer.visibility = View.VISIBLE
        seekBar.visibility = View.VISIBLE
    }

    private fun hideBudgetContainer() {
        budgetContainer.visibility = View.GONE
        seekBar.visibility = View.GONE
    }

    override fun getScreenName(): String? {
        return null
    }

    companion object {

        fun newInstance(status: Int, budget: Int): SettingBudgetAdsFragment {

            val args = Bundle()
            args.putInt(KEY_DAILY_BUDGET, budget)
            args.putInt(KEY_AUTOADS_STATUS, status)
            val fragment = SettingBudgetAdsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
