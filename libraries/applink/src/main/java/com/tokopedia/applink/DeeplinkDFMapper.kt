package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.tokopedia.applink.ApplinkConst.DFFALLBACKURL_KEY
import com.tokopedia.applink.DeeplinkDFApp.getDeeplinkDFPatternList
import com.tokopedia.applink.DeeplinkDFApp.removeDFModuleFromList
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DYNAMIC_FEATURE_INSTALL
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DYNAMIC_FEATURE_INSTALL_BASE
import com.tokopedia.applink.model.DFPPath
import com.tokopedia.applink.model.DFPSchemeToDF
import com.tokopedia.config.GlobalConfig
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URLDecoder
import kotlin.coroutines.CoroutineContext

/**
 * Dynamic Feature Deeplink Mapper
 *
 * If
 * 1) the deeplink is in the dynamic feature module and
 * 2) the module is not installed int the app yet,
 * this will map the deeplink to [install dynamic feature] deeplink
 *
 * Example tokopedia-android-internal://marketplace/shop-settings-info
 *
 * will map to
 *
 * tokopedia-android-internal://global/dynamic-feature-install/shop_settings_sellerapp?
 * applink=[tokopedia-android-internal://marketplace/shop-settings-info]&auto=true&name=Pengaturan Toko&image=
 *
 */
object DeeplinkDFMapper : CoroutineScope {
    // it should have the same name with the folder of dynamic feature
    const val DF_BASE = "df_base"
    const val DF_BASE_SELLER_APP = "df_base_sellerapp"
    const val DF_CATEGORY_TRADE_IN = "df_category_trade_in"
    const val DF_CATEGORY_EPHARMACY = "df_category_epharmacy"
    const val DF_CATEGORY_CATALOG_LIBRARY = "df_category_catalog_library"
    const val DF_CATEGORY_AFFILIATE = "df_category_affiliate"
    const val DF_MERCHANT_SELLER = "df_merchant_seller"
    const val DF_MERCHANT_NONLOGIN = "df_merchant_nonlogin"
    const val DF_MERCHANT_PRODUCT_AR = "df_merchant_product_ar"
    const val DF_OPERATIONAL_CONTACT_US = "df_operational_contact_us"
    const val DF_SALAM_UMRAH = "df_salam_umrah"
    const val DF_TRAVEL = "df_travel"
    const val DF_USER_LIVENESS = "df_user_liveness"
    const val DF_USER_SETTINGS = "df_user_settings"
    const val DF_KYC_SELLERAPP = "df_kyc_sellerapp"
    const val DF_USER_FINGERPRINT = "df_user_fingerprint"
    const val DF_FLASH_SALE_TOKOPEDIA = "df_flash_sale_tokopedia"
    const val DF_PROMO_GAMIFICATION = "df_promo_gamification"
    const val DF_PROMO_TOKOPOINTS = "df_promo_tokopoints"
    const val DF_PROMO_CHECKOUT = "df_promo_checkout"
    const val DF_GAMIFICATION = "df_gamification"
    const val DF_SHOP_SETTINGS_SELLER_APP = "df_shop_settings_sellerapp"
    const val DF_ENTERTAINMENT = "df_entertainment"
    const val DF_MERCHANT_LOGIN = "df_merchant_login"
    const val DF_TOKOPEDIA_NOW = "df_tokopedianow"
    const val DF_TOKOFOOD = "df_tokofood"
    const val DF_CONTENT_PLAY_BROADCASTER = "df_content_play_broadcaster"
    const val DF_FEED_CONTENT_CREATION = "df_feed_content_creation"
    const val DF_PEOPLE = "df_people"
    const val DF_ALPHA_TESTING = "df_alpha_testing"
    const val DF_DIGITAL = "df_digital"
    const val DF_TOKOCHAT = "df_comm_tokochat"
    const val DF_SELLER_FRONT_FUNNEL = "df_seller_front_funnel"
    const val DF_DILAYANI_TOKOPEDIA = "df_dilayanitokopedia"
    const val DF_CAMPAIGN_LIST = "df_campaign_list"
    const val DF_SELLER_FEEDBACK = "df_seller_feedback"
    const val DF_SELLER_TALK = "df_seller_talk"

    const val SHARED_PREF_TRACK_DF_USAGE = "pref_track_df_usage"
    var dfUsageList = mutableListOf<String>()

    private var manager: SplitInstallManager? = null

    /**
     * map the original deeplink to [Dynamic Feature Install] Deeplink
     * (only if the deeplink is dynamic Feature deeplink and if the module is not installed yet)
     */
    @JvmStatic
    fun getDFDeeplinkIfNotInstalled(context: Context, deeplink: String): String? {
        if (deeplink.startsWith(DYNAMIC_FEATURE_INSTALL_BASE)) {
            return null
        }
        return executeDeeplinkPatternv2(
            context,
            Uri.parse(deeplink),
            deeplink,
            getDeeplinkDFPatternList(GlobalConfig.isSellerApp(), context)
        )
    }

    private fun executeDeeplinkPatternv2(
        context: Context,
        uri: Uri,
        deeplink: String,
        list: List<DFPSchemeToDF>
    ): String? {
        val dfModuleFromDeeplinkPath = getDFModuleFromDeeplink(uri, list)
        return if (dfModuleFromDeeplinkPath == null) {
            null
        } else {
            getDFDeeplinkIfNotInstalled(
                context,
                deeplink,
                dfModuleFromDeeplinkPath.dfTarget,
                dfModuleFromDeeplinkPath.webviewFallbackUrl
            )
        }
    }

    fun getDFModuleFromDeeplink(
        uri: Uri,
        list: List<DFPSchemeToDF>
    ): DFPPath? {
        val uriPathSingle = uri.path?.replace("//", "/")
        for (dfpSchemeToDF in list) {
            if (dfpSchemeToDF.scheme != uri.scheme) {
                continue
            }
            val hostList = dfpSchemeToDF.hostList
            for (dfpHost in hostList) {
                if (dfpHost.host != uri.host) {
                    continue
                }
                val dfpPathList = dfpHost.dfpPathObj
                for (dfpPath in dfpPathList) {
                    if (dfpPath.pattern == null || dfpPath.pattern.matches(
                            (uriPathSingle ?: "").toString()
                        )
                    ) {
                        return dfpPath
                    }
                }
            }
        }
        return null
    }

    private fun getDFDeeplinkIfNotInstalled(
        context: Context,
        deeplink: String,
        moduleId: String,
        fallbackUrl: String? = ""
    ): String? {
        getSplitManager(context)?.let {
            val hasInstalled = it.installedModules.contains(moduleId)
            return if (hasInstalled) {
                // for performance, remove all df that already installed, from df pattern
                removeDFModuleFromList(GlobalConfig.isSellerApp(), moduleId)
                trackDFUsageOnce(context.applicationContext, moduleId, deeplink)
                null
            } else {
                UriUtil.buildUri(
                    DYNAMIC_FEATURE_INSTALL,
                    moduleId,
                    Uri.encode(deeplink).toString(),
                    if (fallbackUrl.isNullOrEmpty()) {
                        getDefaultFallbackUrl(deeplink)
                    } else {
                        fallbackUrl
                    }
                )
            }
        } ?: return null
    }

    private fun trackDFUsageOnce(context: Context, moduleId: String, deeplink: String) {
        if (moduleId == DF_BASE || moduleId in dfUsageList) {
            return
        }
        launch(Dispatchers.IO) {
            try {
                val sp =
                    context.getSharedPreferences(SHARED_PREF_TRACK_DF_USAGE, Context.MODE_PRIVATE)
                val hasAccessedModule = sp.getBoolean(moduleId, false)
                if (!hasAccessedModule) {
                    ServerLogger.log(
                        Priority.P1,
                        "DFM_OPENED",
                        mapOf("type" to moduleId, "deeplink" to deeplink)
                    )
                    sp.edit().putBoolean(moduleId, true).apply()
                }
                dfUsageList.add(moduleId)
            } catch (ignored: Exception) {
            }
        }
    }

    private fun getSplitManager(context: Context): SplitInstallManager? {
        if (manager === null) {
            manager = SplitInstallManagerFactory.create(context.applicationContext)
        }
        return manager
    }

    private fun getDefaultFallbackUrl(deeplink: String?): String {
        if (deeplink.isNullOrEmpty()) {
            return ""
        }
        val uri = Uri.parse(deeplink)
        val fallbackUrl = uri.getQueryParameter(DFFALLBACKURL_KEY) ?: return ""
        return URLDecoder.decode(fallbackUrl, "UTF-8")
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + CoroutineExceptionHandler { _, _ -> }
}

fun String.startsWithPattern(prefix: String): Boolean {
    return startsWith(prefix.substringBefore("{")) || startsWith(prefix.substringBefore("?"))
}
