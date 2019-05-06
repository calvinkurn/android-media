package com.tokopedia.topads.auto

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button

/**
 * Author errysuprayogi on 06,May,2019
 */
class AutoAdsActivatedActivity : AppCompatActivity() {

    private var seePerformanceBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_autoads_activated)
        seePerformanceBtn = findViewById(R.id.see_performance_btn)
        seePerformanceBtn!!.setOnClickListener {
            finish()
        }
    }
}