package com.tokopedia.analytics.btm

import android.app.Activity
import androidx.fragment.app.Fragment
import com.bytedance.android.btm.api.ActivityPageInstance
import com.bytedance.android.btm.api.BtmSDK
import com.bytedance.android.btm.api.FragmentPageInstance

object BtmApi {
    fun registerBtmPageOnCreate(page: Fragment, pageBtm: Site.Page, sourceBtmToken: String? = null) {
        BtmSDK.registerBtmPageOnCreate(
            FragmentPageInstance(
                page,
                pageBtm.str,
                sourceBtmToken = sourceBtmToken
            )
        )
    }

    fun registerBtmPageOnCreate(page: Activity, pageBtm: Site.Page, sourceBtmToken: String? = null) {
        BtmSDK.registerBtmPageOnCreate(
            ActivityPageInstance(
                page,
                pageBtm.str,
                sourceBtmToken = sourceBtmToken
            )
        )
    }
}
