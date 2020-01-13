package com.rahullohra.fakeresponse

import android.app.Activity
import android.text.TextUtils
import android.widget.Toast

fun Activity.toast(message: String?) {
    if (!TextUtils.isEmpty(message))
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
}