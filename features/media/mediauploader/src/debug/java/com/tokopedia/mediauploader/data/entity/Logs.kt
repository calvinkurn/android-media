package com.tokopedia.mediauploader.data.entity

import android.content.Intent

data class Logs(
    val title: String = "",
    val value: String = "",
    val cta: Pair<String, Intent?> = Pair("", null),
)
