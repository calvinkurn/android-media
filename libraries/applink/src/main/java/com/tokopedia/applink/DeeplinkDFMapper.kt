package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DYNAMIC_FEATURE_INSTALL
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DYNAMIC_FEATURE_INSTALL_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_SETTINGS_BASE
import com.tokopedia.config.GlobalConfig
import tokopedia.applink.R
import com.crashlytics.android.Crashlytics;

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
object DeeplinkDFMapper {
    // it should have the same name with the folder of dynamic feature
    private val MODULE_SHOP_SETTINGS_SELLERAPP = "shop_settings_sellerapp"

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
        if (GlobalConfig.isSellerApp()) {
            return when {
                deeplink.startsWith(SHOP_SETTINGS_BASE) -> {
                    getDFDeeplinkIfNotInstalled(context,
                        deeplink, MODULE_SHOP_SETTINGS_SELLERAPP,
                        context.getString(R.string.shop_settings_title))
                }
                else -> null
            }
        } else {
            // currently cust app has no dynamic features module
            return null
        }
    }

    private fun getDFDeeplinkIfNotInstalled(context: Context, deeplink: String,
                                            moduleId: String, moduleName: String,
                                            isAuto: Boolean? = true,
                                            imageUrl: String? = ""): String? {
        getSplitManager(context)?.let {
            if (it.installedModules.contains(moduleId)) {
                if (!GlobalConfig.DEBUG) {
                    Crashlytics.logException(Exception("Open module " + moduleId));
                }
                return null
            } else {
                if (!GlobalConfig.DEBUG) {
                    Crashlytics.logException(Exception("Install module " + moduleId));
                }
                return UriUtil.buildUri(
                    DYNAMIC_FEATURE_INSTALL,
                    moduleId,
                    moduleName,
                    Uri.encode(deeplink).toString(),
                    isAuto.toString(),
                    imageUrl)
            }
        } ?: return null
    }

    private fun getSplitManager(context: Context): SplitInstallManager? {
        if (manager === null) {
            manager = SplitInstallManagerFactory.create(context.applicationContext)
        }
        return manager
    }

}