package com.tokopedia.topads.auto

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.support.design.widget.BottomSheetBehavior
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout


/**
 * Author errysuprayogi on 06,May,2019
 */
class DailyBudgetActivity : AppCompatActivity(){

    private var startAutoAdsBtn: Button? = null
    private var startManualAdsBtn: Button? = null
    private var sheetManualAdsBehavior: BottomSheetBehavior<*>? = null
    private var sheetInfoBehavior: BottomSheetBehavior<*>? = null
    private var layoutManualAdsBottomSheet: LinearLayout? = null
    private var layoutInfoBottomSheet: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_start_daily_budget)

        startAutoAdsBtn = findViewById(R.id.start_autoads_btn)
        startManualAdsBtn = findViewById(R.id.start_manual_ads_btn)
        layoutManualAdsBottomSheet = findViewById(R.id.bottom_sheet_manual_ads)
        layoutInfoBottomSheet = findViewById(R.id.bottom_sheet_info_ads)
        sheetManualAdsBehavior = BottomSheetBehavior.from(layoutManualAdsBottomSheet)
        sheetInfoBehavior = BottomSheetBehavior.from(layoutInfoBottomSheet)
        startAutoAdsBtn!!.setOnClickListener {
            startActivity(Intent(this@DailyBudgetActivity, AutoAdsActivatedActivity::class.java))
        }

        startManualAdsBtn!!.setOnClickListener {
            if (sheetManualAdsBehavior!!.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetManualAdsBehavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                sheetManualAdsBehavior!!.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_info, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_info -> {
                if (sheetInfoBehavior!!.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetInfoBehavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)
                } else {
                    sheetInfoBehavior!!.setState(BottomSheetBehavior.STATE_COLLAPSED)
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}