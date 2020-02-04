package com.tokopedia.dynamicfeatures.config

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DFConfig(
        /**
         * If true, try download in background, otherwise will use deferred install
         */
        @SerializedName("dl_in_bg")
        @Expose
        val dowloadInBackground: Boolean = false,
        /**
         * Excluded android version code to download in background
         */
        @SerializedName("dl_in_bg_excl_sdk_version")
        @Expose
        val downloadInBackgroundExcludedSdkVersion: List<Int> = emptyList(),
        /**
         * How long it will retry download in background if failed in minute
         */
        @SerializedName("dl_in_bg_allow_retry")
        @Expose
        val downloadInBackgroundAllowRetry: Boolean = false,
        /**
         * How long it will retry download in background if failed in minute
         */
        @SerializedName("dl_in_bg_retry_time")
        @Expose
        val downloadInBackgroundRetryTime: Long = 10,
        /**
         * Max retry download in background
         */
        @SerializedName("dl_in_bg_max_retry")
        @Expose
        val downloadInBackgroundMaxRetry: Int = 3
)

