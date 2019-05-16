package com.tokopedia.topads.auto.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch

import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.view.activity.ConfirmationDialogActivity
import com.tokopedia.topads.auto.view.widget.SettingAutoAdsInfoSheet
import kotlinx.android.synthetic.main.layout_setting_daily_budget_ads.*

/**
 * Author errysuprayogi on 09,May,2019
 */
class SettingBudgetAdsFragment : DailyBudgetFragment() {

    override fun getLayoutId(): Int {
        return R.layout.layout_setting_daily_budget_ads
    }

    override fun setListener() {
        super.setListener()
        btn_info.setOnClickListener {
            SettingAutoAdsInfoSheet.newInstance(context!!).show()
        }
        save.setOnClickListener {
            startActivity(Intent(context, ConfirmationDialogActivity::class.java))
        }
        switch_ads.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                showBudgetContainer()
            } else{
                hideBudgetContainer()
            }
        }
    }

    private fun showBudgetContainer() {
        budget_container.visibility = View.VISIBLE
        seekbar.visibility = View.VISIBLE
    }

    private fun hideBudgetContainer() {
        budget_container.visibility = View.GONE
        seekbar.visibility = View.GONE
    }

    override fun initInjector() {

    }

    override fun getScreenName(): String? {
        return null
    }

    companion object {

        fun newInstance(): SettingBudgetAdsFragment {

            val args = Bundle()

            val fragment = SettingBudgetAdsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
