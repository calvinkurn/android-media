package com.tokopedia.topads.auto.view.fragment.budget

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch

import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.view.activity.ConfirmationDialogActivity
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
        saveBtn = view.findViewById(R.id.save)
    }

    override fun setListener() {
        super.setListener()
        infoBtn.setOnClickListener {
            SettingAutoAdsInfoSheet.newInstance(context!!).show()
        }
        saveBtn.setOnClickListener {
            startActivity(Intent(context, ConfirmationDialogActivity::class.java))
        }
        switchBudget.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                showBudgetContainer()
            } else{
                hideBudgetContainer()
            }
        }
    }

    private fun showBudgetContainer() {
        budgetContainer.visibility = View.VISIBLE
        seekBar.visibility = View.VISIBLE
    }

    private fun hideBudgetContainer() {
        budgetContainer.visibility = View.GONE
        seekBar.visibility = View.GONE
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
