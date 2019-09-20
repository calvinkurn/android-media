package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import android.os.Build
import com.crashlytics.android.Crashlytics
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.tokopedia.applink.ApplinkConst.*
import com.tokopedia.applink.internal.ApplinkConsInternalDigital.TELCO_DIGITAL
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.IMAGE_SEARCH_RESULT
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.SIMILAR_SEARCH_RESULT_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DYNAMIC_FEATURE_INSTALL
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.DYNAMIC_FEATURE_INSTALL_BASE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SETTING_BANK
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SETTING_PROFILE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.OPEN_SHOP
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_SETTINGS_BASE
import com.tokopedia.config.GlobalConfig
import tokopedia.applink.R
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader

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
    private val SHOP_SETTINGS_SELLERAPP = "shop_settings_sellerapp"
    private val SHOP_SETTINGS_CUSTOMERAPP = "shop_settings"
    private val IMAGE_SEARCH = "image_search"
    private val SIMILAR_SEARCH = "similarsearch"
    private val SHOP_OPEN_CUSTOMERAPP = "shop_open"
    private val HOTEL_TRAVEL = "hotel_travel"
    private val DIGITAL_TOPUP = "digital_topup"
    private val USER_PROFILE_COMPLETION = "profilecompletion"
    private val USER_SETTING_BANK = "settingbank"
    private val HOMEPAGE_TRAVEL = "homepage_travel"
    private val HOMEPAGE_DIGITAL = "homepage_digital"
    private var MODULE_FINTECH_INSTANT_LOAN = "instantloan"


    private var manager: SplitInstallManager? = null
    private val deeplinkDFPatternListCustomerApp: List<DFP> by lazy {
        listOf(
            // to enable/disable dynamic feature, edit the dynamic-feature-<customerapp>.cfg file. No longer to comment any lines.
            DFP({ it.startsWith(HOTEL) }, HOTEL_TRAVEL, R.string.title_hotel),
            DFP({ it.startsWith(TRAVEL_SUBHOMEPAGE) }, HOMEPAGE_TRAVEL, R.string.title_travel_homepage),
            DFP({ it.startsWith(TELCO_DIGITAL) }, DIGITAL_TOPUP, R.string.digital_topup_title),
            DFP({ it.startsWith(OPEN_SHOP) }, SHOP_OPEN_CUSTOMERAPP, R.string.title_open_shop),
            DFP({ it.startsWith(SHOP_SETTINGS_BASE) }, SHOP_SETTINGS_CUSTOMERAPP, R.string.shop_settings_title),
            DFP({ it.startsWith(SIMILAR_SEARCH_RESULT_BASE) }, SIMILAR_SEARCH, R.string.title_similar_search),
            DFP({ it.startsWith(IMAGE_SEARCH_RESULT) }, IMAGE_SEARCH, R.string.title_image_search),
            DFP({ it.startsWith(SETTING_PROFILE) }, USER_PROFILE_COMPLETION, R.string.applink_profile_completion_title),
            DFP({ it.startsWith(SETTING_BANK) }, USER_SETTING_BANK, R.string.applink_setting_bank_title),
            DFP({ it.startsWith(DIGITAL_SUBHOMEPAGE) }, HOMEPAGE_DIGITAL, R.string.title_digital_subhomepage),
                DFP({ it.startsWith(INSTANT_LOAN) }, MODULE_FINTECH_INSTANT_LOAN, R.string.instant_loan_title)

        )
    }

    private val deeplinkDFPatternListSellerApp: List<DFP> by lazy {
        listOf(
            // to enable/disable dynamic feature, edit the dynamic-feature-<sellerapp>.cfg file. No longer to comment any lines.
            DFP({ it.startsWith(SHOP_SETTINGS_BASE) }, SHOP_SETTINGS_SELLERAPP, R.string.shop_settings_title)
        )
    }

    /**
     * Deeplink Pattern List for customerapp defined by config in dynamic-feature-customerapp.cfg
     * If the cfg consist only 2 lines (not commented) this pattern will consist also 2 lines.
     */
    private var turnedOnDeeplinkDFPatternCustomerapp: List<DFP>? = null

    /**
     * Deeplink Pattern List for customerapp defined by config in dynamic-feature-sellerapp.cfg
     * If the cfg consist only 2 lines (not commented) this pattern will consist also 2 lines.
     */
    private var turnedOnDeeplinkDFPatternSellerapp: List<DFP>? = null

    /**
     * map the original deeplink to [Dynamic Feature Install] Deeplink
     * (only if the deeplink is dynamic Feature deeplink and if the module is not installed yet)
     */
    @JvmStatic
    fun getDFDeeplinkIfNotInstalled(context: Context, deeplink: String): String? {
        //KITKAT does not support dynamic feature
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return null
        }
        if (deeplink.startsWith(DYNAMIC_FEATURE_INSTALL_BASE)) {
            return null
        }
        return if (GlobalConfig.isSellerApp()) {
            if (turnedOnDeeplinkDFPatternSellerapp == null) {
                turnedOnDeeplinkDFPatternSellerapp = getTurnOnDeeplinkDFPattern(context, deeplinkDFPatternListSellerApp)
            }
            executeDeeplinkPattern(context, deeplink, turnedOnDeeplinkDFPatternSellerapp)
        } else {
            if (turnedOnDeeplinkDFPatternCustomerapp == null) {
                turnedOnDeeplinkDFPatternCustomerapp = getTurnOnDeeplinkDFPattern(context, deeplinkDFPatternListCustomerApp)
            }
            executeDeeplinkPattern(context, deeplink, turnedOnDeeplinkDFPatternCustomerapp)
        }
    }

    private fun executeDeeplinkPattern(context: Context,
                                       deeplink: String,
                                       list: List<DFP>?): String? {
        list?.forEach {
            if (it.logic(deeplink)) {
                return getDFDeeplinkIfNotInstalled(context,
                    deeplink, it.moduleId, context.getString(it.moduleNameResourceId))
            }
        }
        return null
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

    /**
     * Supplied with list of DFP, it will return the list of turned on DFP by the config
     * Example:
     * Input:   original DFP list = {DFP(shop), DFP(hotel), DFP(flight)}
     *          cfg -> setOf(hotel)
     * Output:  DFP list {DFP(hotel)}
     */
    private fun getTurnOnDeeplinkDFPattern(context: Context, dfpList: List<DFP>?): List<DFP>? {
        val turnedOnDfSet = getDFFilterMap(context)
        if (turnedOnDfSet == null || turnedOnDfSet.isEmpty()) {
            return listOf()
        }
        val resultList = mutableListOf<DFP>()
        dfpList?.forEach {
            if (turnedOnDfSet.contains(it.moduleId)) {
                resultList.add(it)
            }
        }
        return resultList
    }

    /**
     * return the set from cfg file. Set will be distinct
     * @return Set of dynamic features that turned on by the config "dynamic-feature-<app>.cfg"
     * Example return: setOf{shop_settings, hotel}
     */
    private fun getDFFilterMap(context: Context): Set<String>? {
        try {
            val set: HashSet<String> = hashSetOf()
            val reader = BufferedReader(InputStreamReader(context.assets.open("df.cfg")))
            var line: String? = reader.readLine()
            while (line != null) {
                if (line.isNotEmpty()) {
                    set.add(line)
                }
                line = reader.readLine()
            }
            return set
        } catch (e: FileNotFoundException) {
            return null
        }
    }

}

/**
 * Class to hold dynamic feature pattern, used for mapping
 */
class DFP(
    val logic: ((deeplink: String) -> Boolean),
    val moduleId: String,
    val moduleNameResourceId: Int
)