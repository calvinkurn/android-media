package com.tokopedia.topads.auto.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

import com.tokopedia.topads.auto.R

/**
 * Author errysuprayogi on 09,May,2019
 */
class ConfirmationDialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_dialog_autoads_confirmation)
        findViewById<View>(R.id.negative_button).setOnClickListener {
            finish()
        }
        findViewById<View>(R.id.positive_button).setOnClickListener {
            finish()
        }
    }
}
