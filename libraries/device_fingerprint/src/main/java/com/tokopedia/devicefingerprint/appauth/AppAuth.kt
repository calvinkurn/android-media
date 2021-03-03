package com.tokopedia.devicefingerprint.appauth

import android.content.Context
import com.tkpd.util.Base64

fun getDecoder(context: Context) = Base64.GetDecoder(context)