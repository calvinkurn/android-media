package com.tokopedia.imagepicker_insta.util

import android.content.Context
import androidx.annotation.IntDef
import com.tokopedia.imagepicker_insta.util.CameraFacing.Companion.BACK
import com.tokopedia.imagepicker_insta.util.CameraFacing.Companion.FRONT

object Prefs {
    private const val PREF_KEY = "imagepicker_insta"

    private const val CAMERA_FACING = "cam_facing"
    private const val SHOULD_SHOW_COACHMARK = "should_show_coach_mark"

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

    fun saveShouldShowCoachMarkValue(context: Context) {
        val sp = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        sp.edit()
            .putBoolean(SHOULD_SHOW_COACHMARK, false)
            .apply()
    }

    fun getShouldShowCoachMarkValue(context: Context): Boolean {
        val sp = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        return sp.getBoolean(SHOULD_SHOW_COACHMARK, true)
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