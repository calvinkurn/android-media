package com.tokopedia.imagepicker_insta.util

import android.content.Context
import androidx.annotation.IntDef
import com.tokopedia.imagepicker_insta.util.CameraFacing.Companion.BACK
import com.tokopedia.imagepicker_insta.util.CameraFacing.Companion.FRONT

object Prefs {
    private const val PREF_KEY = "imagepicker_insta"

    private const val CAMERA_FACING = "cam_facing"

    fun saveCameraFacing(context:Context, @CameraFacing cameraFacing:Int){
        val sp = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        sp.edit()
            .putInt(CAMERA_FACING,cameraFacing)
            .apply()
    }

    @CameraFacing
    fun getCameraFacing(context:Context):Int{
        val sp = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        return sp.getInt(CAMERA_FACING, CameraFacing.BACK)
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(BACK,FRONT)
annotation class CameraFacing {
    companion object {
        const val BACK = 0
        const val FRONT = 1
    }
}