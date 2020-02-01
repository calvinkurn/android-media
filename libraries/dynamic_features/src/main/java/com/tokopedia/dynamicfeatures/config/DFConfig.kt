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
         * How long it will retry download in background if failed
         */
        @SerializedName("retry_dl_in_bg")
        @Expose
        val retryDownloadInBackground: Int = 60,
        /**
         * Excluded android version code to download in background
         */
        @SerializedName("excl_ver_code_dl_in_bg")
        @Expose
        val excludedVersionCodeDownloadInBackground: List<Int> = emptyList()
)

