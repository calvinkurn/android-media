package com.tokopedia.device.info.model

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaDrm
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.util.Base64
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.config.GlobalConfig
import com.tokopedia.device.info.DeviceInfo
import com.tokopedia.kotlin.extensions.backgroundCommit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import java.util.*
import kotlin.coroutines.resume

data class AdditionalInfoModel(
    @SerializedName("time")
    @Expose
    val time: String,

    @SerializedName("brand")
    @Expose
    val brand: String,

    @SerializedName("product")
    @Expose
    val product: String,

    @SerializedName("board")
    @Expose
    val board: String,

    @SerializedName("cpuAbi")
    @Expose
    val cpuAbi: String,

    @SerializedName("device")
    @Expose
    val device: String,

    @SerializedName("versionName")
    @Expose
    val versionName: String,

    @SerializedName("advertisingId")
    @Expose
    val advertisingId: String,

    @SerializedName("wideVineId")
    @Expose
    val wideVineId: String
)

object AdditionalDeviceInfo {
    const val WIDEVINEID = "WIDEVINEID"
    const val KEY_WIDEVINEID = "KEY_WIDEVINEID"

    private const val UNKNOWN = "unknown"
    private const val MOST_SIG_BITS = -0x121074568629b532L
    private const val LEAST_SIG_BITS = -0x5c37d8232ae2de13L

    var cacheWidevineId = ""

    fun generate(
        context: Context,
        isEnableGetWidevineId: Boolean,
        isEnableGetWidevineIdSuspend: Boolean,
        whitelistDisableWidevineId: String,
        userId: String
    ): AdditionalInfoModel {
        return AdditionalInfoModel(
            time = System.currentTimeMillis().toString(),
            brand = Build.BRAND,
            product = Build.PRODUCT,
            board = Build.BOARD,
            cpuAbi = Build.CPU_ABI,
            device = Build.DEVICE,
            versionName = GlobalConfig.VERSION_NAME,
            advertisingId = DeviceInfo.getAdsId(context),
            wideVineId = getWidevineId(
                context,
                isEnableGetWidevineId,
                isEnableGetWidevineIdSuspend,
                whitelistDisableWidevineId,
                userId
            )
        )
    }

    fun generateJson(
        context: Context,
        isEnableGetWidevineId: Boolean,
        isEnableGetWidevineIdSuspend: Boolean,
        whitelistDisableWidevineId: String,
        userId: String
    ): String {
        return Gson().toJson(
            generate(
                context,
                isEnableGetWidevineId,
                isEnableGetWidevineIdSuspend,
                whitelistDisableWidevineId,
                userId
            )
        )
    }

    private fun getWidevineId(
        context: Context,
        isEnableGetWidevineId: Boolean,
        isEnableGetWidevineIdSuspend: Boolean,
        whitelistDisableWidevineId: String,
        userId: String
    ): String {
        return if (isEnableGetWidevineIdSuspend) {
            validategetWidevineIdSuspend(context)
        } else if (isEnableGetWidevineId) {
            validateGetWidevineId(whitelistDisableWidevineId, userId)
        } else {
            ""
        }
    }

    private fun validateGetWidevineId(whitelistDisableWidevineId: String, userId: String): String {
        val userIds = whitelistDisableWidevineId.split(",")
        return if (userIds.contains(userId)) {
            ""
        } else {
            processGetWidevineId()
        }
    }

    private fun validategetWidevineIdSuspend(context: Context): String {
        val appContext = context.applicationContext
        val widevineIdCache: String = getCacheWidevineId(appContext)
        return if (widevineIdCache.isNotBlank()) {
            widevineIdCache
        } else {
            try {
                getWidevineIdSuspend(context)
            } catch (e: Exception) {
                Timber.e(e)
            }
            ""
        }
    }

    @JvmOverloads
    @JvmStatic
    fun getWidevineIdSuspend(
        context: Context,
        onSuccessGetWidevineId: ((widevineId: String) -> Unit)? = null,
        timeOutInMillis: Long = 3000L
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val widevineIdCache: String = getCacheWidevineId(context)
                if (widevineIdCache.isNotBlank()) {
                    onSuccessGetWidevineId?.invoke(widevineIdCache)
                } else {
                    val adId = getLatestWidevineId(context, timeOutInMillis)
                    onSuccessGetWidevineId?.invoke(adId)
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun getCacheWidevineId(context: Context): String {
        if (cacheWidevineId.isEmpty()) {
            val sp = context.getSharedPreferences(WIDEVINEID, Context.MODE_PRIVATE)
            cacheWidevineId = sp.getString(KEY_WIDEVINEID, "") ?: ""
        }
        return cacheWidevineId
    }

    suspend fun getLatestWidevineId(context: Context, timeOutInMillis: Long = 10000L): String {
        return withContext(Dispatchers.IO) {
            try {
                val appContext = context.applicationContext
                val finalWidevineId = withTimeout(timeOutInMillis) {
                    val widevineId: String? =
                        suspendCancellableCoroutine { continuation ->
                            try {
                                val widevineId = processGetWidevineId()
                                continuation.resume(widevineId)
                            } catch (e: Exception) {
                                Timber.e(e)
                                continuation.resume(null)
                            }
                        }
                    val widevineIdTemp = if (widevineId == null) {
                        ""
                    } else {
                        setCacheWidevineId(appContext, widevineId)
                        widevineId
                    }
                    widevineIdTemp
                }
                finalWidevineId
            } catch (e: Exception) {
                Timber.e(e)
                ""
            }
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun setCacheWidevineId(context: Context, widevineId: String) {
        val sp = context.getSharedPreferences(WIDEVINEID, Context.MODE_PRIVATE)
        sp.edit().putString(KEY_WIDEVINEID, widevineId).backgroundCommit()

        if (DeviceInfo.enabledBackgroundCommit()) {
            sp.edit().putString(KEY_WIDEVINEID, widevineId).backgroundCommit()
        } else {
            sp.edit().putString(KEY_WIDEVINEID, widevineId).apply()
        }

        cacheWidevineId = widevineId
    }

    @SuppressLint("DeprecatedMethod")
    private fun processGetWidevineId(): String {
        var widevineMediaDrm: MediaDrm? = null

        val widevineId = try {
            widevineMediaDrm = MediaDrm(UUID(MOST_SIG_BITS, LEAST_SIG_BITS))
            val wideVineId =
                widevineMediaDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID)
            Base64.encodeToString(wideVineId, Base64.DEFAULT).trim()
        } catch (e: Exception) {
            Timber.e(e)
            UNKNOWN
        } finally {
            widevineMediaDrm?.let {
                when {
                    SDK_INT >= 28 -> {
                        it.close()
                    }

                    SDK_INT in 18..27 -> {
                        it.release()
                    }

                    else -> {
                        // do nothing
                        // this is to stop linter's warn
                    }
                }
            }
        }

        return widevineId
    }
}
