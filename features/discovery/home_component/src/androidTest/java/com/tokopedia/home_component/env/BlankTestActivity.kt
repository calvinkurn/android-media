package com.tokopedia.home_component.test.env

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.tokopedia.home_component.test.R

class BlankTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSustainedPerformanceMode(true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blank_test)
    }
}