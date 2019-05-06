package com.tokopedia.topads.auto

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button

/**
 * Author errysuprayogi on 06,May,2019
 */
class StartAutoAdsActivity : AppCompatActivity() {

    private var startAds: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_new_ads_onboarding)
        startAds = findViewById(R.id.startBtn)
        startAds!!.setOnClickListener { startActivity(Intent(this@StartAutoAdsActivity, DailyBudgetActivity::class.java)) }
    }
}
