package com.tokopedia.dynamicfeatures.config

import android.os.Build
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DFConfig(
    /**
     * If true, try download in background, otherwise will use deferred install
     */
    @SerializedName("dl_in_bg")
    @Expose
    val downloadInBackground: Boolean = false,
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
    val downloadInBackgroundMaxRetry: Int = 3,
    /**
     * Maximum valid insufficient storage
     * Default 1.5 * 1024KB
     */
    @SerializedName("max_thld_insuf_strg")
    @Expose
    val maxThresholdInsufficientStorage: Long = 1536,

    @SerializedName("df_singleton_service")
    @Expose
    val useSingletonService: Boolean = true,

    @SerializedName("module_restricted_in_bg")
    @Expose
    val moduleRestrictInBackground: List<String>? = emptyList(),

    @SerializedName("dl_in_bg_show_fallback_time")
    @Expose
    val timeout: Long = 30,

    @SerializedName("return_if_state_invalid")
    @Expose
    val returnIfStateInvalid: Boolean = false

) {
    fun allowRunningServiceFromActivity(moduleName: String): Boolean {
        return useSingletonService &&
            downloadInBackground &&
            !downloadInBackgroundExcludedSdkVersion.contains(Build.VERSION.SDK_INT) &&
            isModuleAllowedDownloadInBg(moduleName)
    }

    private fun isModuleAllowedDownloadInBg(moduleName: String): Boolean {
        return if (moduleRestrictInBackground.isNullOrEmpty()) {
            true
        } else {
            moduleName !in moduleRestrictInBackground
        }
    }
}