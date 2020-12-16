package com.tokopedia.utils.permission


import android.content.ContentProvider
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.service.voice.VoiceInteractionService
import androidx.slice.SliceManager

object SlicePermission {

    const val RECHARGE_SLICE_AUTHORITY = "com.tokopedia.tkpd.recharge_slice"
    const val SELLER_ORDER_AUTHORITY = "com.tokopedia.seller.action.slices"

    @JvmStatic
    fun initPermission(context: Context, authority: String) {
        grantAssistantPermissions(context, authority)
    }

    private fun grantAssistantPermissions(context: Context, authority: String) {
        val provider = context.contentResolver.acquireContentProviderClient(authority)
        getAssistantPackage(context)?.let { assistantPackage ->
            val sliceProviderUri = Uri.Builder()
                    .scheme(ContentResolver.SCHEME_CONTENT)
                    .authority(authority)
                    .build()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                SliceManager.getInstance(context).grantSlicePermission(assistantPackage, sliceProviderUri)
            }
        }
    }

    /**
     * Find the assistant package name
     */
    private fun getAssistantPackage(context: Context): String? {
        val packageManager = context.packageManager
        val resolveInfoList = packageManager?.queryIntentServices(
                Intent(VoiceInteractionService.SERVICE_INTERFACE), 0
        )
        return resolveInfoList?.firstOrNull()?.serviceInfo?.packageName
    }
}