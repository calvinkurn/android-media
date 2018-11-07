package com.tokopedia.kelontongapp.webview

import android.content.Intent

interface FilePickerInterface {
    fun startActivityForResult(intent: Intent, action: Int)
}
