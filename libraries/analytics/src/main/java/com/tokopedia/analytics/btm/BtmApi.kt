package com.tokopedia.analytics.btm

import android.app.Activity
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import com.bytedance.android.btm.api.ActivityPageInstance
import com.bytedance.android.btm.api.BtmSDK
import com.bytedance.android.btm.api.FragmentPageInstance
import com.bytedance.android.btm.api.consts.BtmConst
import timber.log.Timber

@Keep
object BtmApi {
    private fun findSourceBtmToken(page: Any): String? {
        val intent = when (page) {
            is Activity -> page.intent
            is Fragment -> page.activity?.intent
            else -> null
        } ?: return null

        // PlanA get sourceBtmToken from appLink params
        val uri = intent.data ?: return null
        val tokenFromPlanA = uri.getQueryParameter(BtmConst.KEY_SOURCE_BTM_TOKEN)
        if (!tokenFromPlanA.isNullOrEmpty()) return tokenFromPlanA

        // PlanB get sourceBtmToken from Intent bundle
        return intent.extras?.getString(BtmConst.KEY_SOURCE_BTM_TOKEN)
    }

    @JvmStatic
    fun registerBtmPageOnCreate(page: Fragment, pageBtm: Site.Page, sourceBtmToken: String? = null) {
        val isBtmEnabled = BtmLibraFeatureFlag.isBtmEnabled()
        if (isBtmEnabled) {
            registerBtmPage(page, pageBtm, sourceBtmToken)
        }
    }

    @JvmStatic
    fun registerBtmPageOnCreate(page: Activity, pageBtm: Site.Page, sourceBtmToken: String? = null) {
        val isBtmEnabled = BtmLibraFeatureFlag.isBtmEnabled()
        if (isBtmEnabled) {
            registerBtmPage(page, pageBtm, sourceBtmToken)
        }
    }

    private fun registerBtmPage(page: Fragment, pageBtm: Site.Page, sourceBtmToken: String? = null) {
        val token = sourceBtmToken ?: findSourceBtmToken(page)
        Timber.d("[registerBtmPage] pageBtm:$pageBtm(${pageBtm.str}),Page:${page::class.java.name} SourceBtmToken:$token")
        BtmSDK.registerBtmPageOnCreate(
            FragmentPageInstance(
                page,
                pageBtm.str,
                sourceBtmToken = token
            )
        )
    }

    private fun registerBtmPage(page: Activity, pageBtm: Site.Page, sourceBtmToken: String? = null) {
        val token = sourceBtmToken ?: findSourceBtmToken(page)
        Timber.d("[registerBtmPage] pageBtm:$pageBtm(${pageBtm.str}),Page:${page::class.java.name} SourceBtmToken:$token")
        BtmSDK.registerBtmPageOnCreate(
            ActivityPageInstance(
                page,
                pageBtm.str,
                sourceBtmToken = token
            )
        )
    }
}
