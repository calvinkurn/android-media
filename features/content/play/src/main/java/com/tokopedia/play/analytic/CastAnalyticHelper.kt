package com.tokopedia.play.analytic

import android.content.Context
import java.util.*

/**
 * Created By : Jonathan Darwin on August 16, 2021
 */
class CastAnalyticHelper(
    private val context: Context,
    private val analytic: PlayAnalytic
) {

    fun startRecording() {
        val date = Calendar.getInstance().time
    }

    fun stopRecording() {
        val date = Calendar.getInstance().time
    }
}