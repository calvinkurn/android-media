package com.tokopedia.dynamicfeatures.service

import android.os.Handler
import java.util.concurrent.TimeUnit

/**
 * Created by hendry on 15/12/19.
 */
object DFServiceConstant {
    const val SHARED_PREF_NAME = "df_job_srv"
    const val KEY_SHARED_PREF_MODULE = "module_list"
    const val DELIMITER = "#"
    const val DELIMITER_2 = ":"
    const val MAX_ATTEMPT_DOWNLOAD = 3
    const val INITIAL_DELAY_DURATION_IN_SECOND = 3600L

    var isServiceRunning = false
}