package com.tokopedia.tokopoints.view.util

import android.content.Context
import androidx.core.content.ContextCompat

object ColorUtil {

    private const val HEX_CODE_TRANSPARENCY: Int = 0x00ffffff

    fun getColorFromResToString(context: Context, colorResId: Int): String {
        return try {
            "#" + Integer.toHexString(ContextCompat.getColor(context, colorResId) and HEX_CODE_TRANSPARENCY)
        }catch (err: Throwable){
            ""
        }
    }
}