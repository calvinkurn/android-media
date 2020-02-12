package com.tokopedia.tkpd

import android.content.Context
import android.content.Intent

interface OnNewIntentListener {
    fun onNewIntent(context: Context, intent: Intent)
}