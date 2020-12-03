package com.tokopedia.tkpd.redirect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ClearActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finishAffinity()
    }
}