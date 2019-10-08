package com.tokopedia.topads.auto.view.activity

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
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
        setTheme(R.style.AppTheme_NoActionBar_ActivityDialog)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        }
        setContentView(R.layout.layout_dialog_autoads_confirmation)
        findViewById<View>(com.tokopedia.abstraction.R.id.negative_button).setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        findViewById<View>(com.tokopedia.abstraction.R.id.positive_button).setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
