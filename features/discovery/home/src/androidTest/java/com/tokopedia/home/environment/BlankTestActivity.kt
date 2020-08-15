package com.tokopedia.home.environment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tokopedia.home.test.R

class BlankTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blank_test)
        window.setSustainedPerformanceMode(true)
    }
}