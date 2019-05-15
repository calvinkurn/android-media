package com.tokopedia.topads.auto.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View

import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.router.TopAdsAutoRouter
import com.tokopedia.topads.auto.view.activity.AutoAdsActivatedActivity
import com.tokopedia.topads.auto.view.widget.InfoAutoAdsSheet
import com.tokopedia.topads.auto.view.widget.ManualAdsConfirmationSheet
import kotlinx.android.synthetic.main.layout_start_daily_budget.*

/**
 * Author errysuprayogi on 14,May,2019
 */
class InitialBudgetFragment : DailyBudgetFragment(), View.OnClickListener, ManualAdsConfirmationSheet.ActionListener {


    private lateinit var adsConfirmationSheet: ManualAdsConfirmationSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        adsConfirmationSheet = ManualAdsConfirmationSheet.newInstance(activity!!)
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
        } else if (id == R.id.start_autoads_btn) {
            startActivity(Intent(activity, AutoAdsActivatedActivity::class.java))
        }
    }

    override fun onManualAdsClicked() {
        val intent = (activity!!.application as TopAdsAutoRouter)
                .getTopAdsAddingPromoOptionIntent(activity!!)
        startActivityForResult(intent, DailyBudgetFragment.REQUEST_CODE_AD_OPTION)
    }

    override fun onAutoAdsClicked() {
        activity!!.finish()
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_start_daily_budget
    }

    override fun setUpView(view: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setListener() {
        super.setListener()
        start_manual_ads_btn.setOnClickListener(this)
        start_autoads_btn.setOnClickListener(this)
        adsConfirmationSheet.setActionListener(this)
    }

    override fun initInjector() {

    }

    override fun getScreenName(): String? {
        return null
    }

    companion object {

        fun newInstance(): InitialBudgetFragment {

            val args = Bundle()

            val fragment = InitialBudgetFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
