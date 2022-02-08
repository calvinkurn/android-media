package com.tokopedia.media.common.intent

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable

interface BaseIntent<T : Parcelable> {
    val appLink: String
    val keyName: String

    fun extractData(intent: Intent?): ArrayList<T> {
        if (intent == null) error("intent not found")
        return intent.getParcelableArrayListExtra(keyName)?: arrayListOf()
    }

    fun extractData(bundle: Bundle?): ArrayList<T> {
        if (bundle == null) error("intent not found")
        return bundle.getParcelableArrayList(keyName)?: arrayListOf()
    }

}