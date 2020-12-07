package com.tokopedia.utils.permission


import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.service.voice.VoiceInteractionService
import androidx.slice.SliceManager

object SlicePermission {

    const val RECHARGE_SLICE_PERMISSION = "com.tokopedia.tkpd.recharge_slice"

    @JvmStatic
    fun initPermission(context: Context){
        grantAssistantPermissions(context)
    }

    private fun grantAssistantPermissions(context: Context) {
        getAssistantPackage(context)?.let { assistantPackage ->
            val sliceProviderUri = Uri.Builder()
                    .scheme(ContentResolver.SCHEME_CONTENT)
                    .authority(RECHARGE_SLICE_PERMISSION)
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