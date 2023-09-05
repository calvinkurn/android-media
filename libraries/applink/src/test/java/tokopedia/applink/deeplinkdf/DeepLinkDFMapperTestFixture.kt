package tokopedia.applink.deeplinkdf

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.tokopedia.applink.DeeplinkDFApp
import com.tokopedia.applink.DeeplinkDFMapper
import com.tokopedia.applink.DeeplinkDFMapper.getDFModuleFromDeeplink
import com.tokopedia.applink.DeeplinkMapper
import com.tokopedia.applink.DeeplinkMapper.getRegisteredNavigation
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.account.DeeplinkMapperAccount
import com.tokopedia.applink.communication.DeeplinkMapperCommunication
import com.tokopedia.applink.home.DeeplinkMapperHome
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant
import com.tokopedia.applink.model.DFPHost
import com.tokopedia.applink.model.DFPPath
import com.tokopedia.applink.model.DFPSchemeToDF
import com.tokopedia.applink.model.DFP
import com.tokopedia.applink.model.PathType
import com.tokopedia.applink.powermerchant.PowerMerchantDeepLinkMapper
import com.tokopedia.applink.purchaseplatform.DeeplinkMapperUoh
import com.tokopedia.applink.user.DeeplinkMapperUser
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import io.mockk.mockkClass
import io.mockk.mockkObject
import io.mockk.mockkStatic
import org.junit.Assert.assertEquals
import org.junit.Before
import java.io.File

open class DeepLinkDFMapperTestFixture {

    private var hasMock = false

    protected lateinit var context: Context

    private var internalDeeplinkDFPatternListCustomerApp: List<DFPSchemeToDF>? = null
    private var internalDeeplinkDFPatternListSellerApp: List<DFPSchemeToDF>? = null

    private var expectedPatternCustomerapp: List<DFPSchemeToDF>? = null
    private var expectedPatternSellerapp: List<DFPSchemeToDF>? = null

    @Before
    open fun setup() {
        context = ApplicationProvider.getApplicationContext()
        if (!hasMock) {
            mockkStatic(Uri::class)
            mockkObject(DeeplinkMapperUoh)
            mockkObject(DeeplinkMapperMerchant)
            mockkObject(DeeplinkMapperHome)
            mockkObject(DeeplinkMapperAccount)
            mockkObject(DeeplinkMapperCommunication)
            mockkObject(DeeplinkMapperUser)
            mockkObject(DeeplinkMapper)
            mockkObject(PowerMerchantDeepLinkMapper)
            mockkClass(GlobalConfig::class)
            mockkStatic(RemoteConfigInstance::class)
            mockkObject(FirebaseRemoteConfigInstance)

            internalDeeplinkDFPatternListCustomerApp = DeeplinkDFApp.getDfCustomerappMap().mapDF()
            internalDeeplinkDFPatternListSellerApp = DeeplinkDFApp.getDfSellerappMap().mapDF()

            expectedPatternCustomerapp = readExpected("ma_df.txt")
            expectedPatternSellerapp = readExpected("sa_df.txt")
            hasMock = true
        }
        setAllowingDebugToolsFalse()
    }

    data class DFObject(
        @SerializedName("dfName")
        val dfName: String,
        @SerializedName("activityList")
        val activityList: List<DFActivity>
    )

    data class DFActivity(
        @SerializedName("pathList")
        val pathList: List<DFPath>
    )

    data class DFPath(
        @SerializedName("scheme")
        val scheme: String,
        @SerializedName("host")
        val host: String,
        @SerializedName("pathType")
        val pathTypeString: String,
        @SerializedName("pathString")
        val pathString: String
    ) {
        fun pathTypeEnum() = when (pathTypeString) {
            "NO_PATH" -> {
                PathType.NO_PATH
            }

            "PATH" -> {
                PathType.PATH
            }

            "PATH_PREFIX" -> {
                PathType.PREFIX
            }

            else -> {
                PathType.PATTERN
            }
        }
    }

    private fun readExpected(fileName: String): List<DFPSchemeToDF>? {
        val file = File(fileName)
        if (file.exists()) {
            val line = file.readLines().joinToString("")
            val dfManifestSource = Gson().fromJson(line, Array<DFObject>::class.java).toList()
            val dfManifestMap = mutableListOf<DFPSchemeToDF>()
            for (dfObject in dfManifestSource) {
                for (dfActivity in dfObject.activityList) {
                    for (dfPath in dfActivity.pathList) {
                        addDFPath(
                            dfManifestMap,
                            dfObject.dfName,
                            DFP(
                                dfPath.scheme,
                                dfPath.host,
                                dfPath.pathTypeEnum(),
                                dfPath.pathString
                            )
                        )
                    }
                }
            }
            return dfManifestMap
        } else {
            throw RuntimeException("DF From manifest ($fileName)is empty, or in wrong format")
        }
    }

    fun Map<String, List<DFP>>.mapDF(): List<DFPSchemeToDF> {
        val dfpSchemeToDf = mutableListOf<DFPSchemeToDF>()
        forEach { dfMapItem ->
            dfMapItem.value.forEach { dfpItem ->
                addDFPath(dfpSchemeToDf, dfMapItem.key, dfpItem)
            }
        }
        return dfpSchemeToDf
    }

    private fun addDFPath(
        listDF: MutableList<DFPSchemeToDF>,
        dfModuleName: String,
        dfp: DFP
    ) {
        var schemeInList = listDF.find { it.scheme == dfp.scheme }
        if (schemeInList == null) {
            schemeInList = DFPSchemeToDF(dfp.scheme, mutableListOf())
            listDF.add(schemeInList)
        }
        var hostInList = schemeInList.hostList.find { it.host == dfp.host }
        if (hostInList == null) {
            hostInList = DFPHost(dfp.host, mutableListOf())
            schemeInList.hostList.add(hostInList)
        }
        val pattern = createPattern(dfp)
        val patternInList = hostInList.dfpPathObj.find { pattern?.pattern == it.pattern?.pattern }
        if (patternInList == null) {
            hostInList.dfpPathObj.add(DFPPath(pattern, dfModuleName, dfp.webviewFallbackLogic))
        } else {
            throw RuntimeException("Duplicate Pattern: " + pattern?.pattern + " " + patternInList.pattern + " " + pattern?.pattern)
        }
    }

    private fun createPattern(dfp: DFP): Regex? {
        return when (dfp.pathType) {
            PathType.NO_PATH -> {
                null
            }

            PathType.PREFIX -> {
                (dfp.pathString + ".*").toRegex()
            }

            PathType.PATH -> {
                (dfp.pathString).toRegex()
            }

            PathType.PATTERN -> {
                (dfp.pathString)
                    .replace("\\", "")
                    .replace(".*", "[a-zA-Z0-9_ %-]*").toRegex()
            }
        }
    }

    data class DeeplinkToCheck(val deeplink: String, val dfTarget: String)

    final fun checkDeeplink(isSellerapp: Boolean) {
        val dfToCheck: List<DFPSchemeToDF>?
        val dfExpected: List<DFPSchemeToDF>?
        if (isSellerapp) {
            dfToCheck = internalDeeplinkDFPatternListSellerApp
            dfExpected = expectedPatternSellerapp
        } else {
            dfToCheck = internalDeeplinkDFPatternListCustomerApp
            dfExpected = expectedPatternCustomerapp
        }
        if (dfToCheck.isNullOrEmpty()) {
            throw RuntimeException("DF to check is null or empty")
        }
        if (dfExpected.isNullOrEmpty()) {
            throw RuntimeException("DF Expected is null or empty")
        }
        val listDeeplinkToCheck = mutableListOf<DeeplinkToCheck>()
        for (dfpSchemeToDf in dfExpected) {
            val schemeCheck = dfToCheck.find { it.scheme == dfpSchemeToDf.scheme }
                ?: throw RuntimeException(dfpSchemeToDf.scheme + " is not available in DeeplinkDFApp. Deeplink: " + dfpSchemeToDf.toPatternString())
            for (host in dfpSchemeToDf.hostList) {
                val hostCheck = schemeCheck.hostList.find { it.host == host.host }
                    ?: throw RuntimeException(
                        host.host + " is not available in DeeplinkDFApp. Deeplink: " + schemeCheck.scheme + "://" + host.toPatternString()
                    )
                for (pathObjExpected in host.dfpPathObj) {
                    val pathCheck =
                        hostCheck.dfpPathObj.find { it.pattern?.pattern?.mapPatternAllToNull() == pathObjExpected.pattern?.pattern?.mapPatternAllToNull() }
                            ?: throw RuntimeException(pathObjExpected.pattern?.pattern + " is not available in DeeplinkDFApp. Deeplink: " + schemeCheck.scheme + "://" + hostCheck.host + "/" + pathObjExpected.toPatternString())
                    if (pathCheck.dfTarget != pathObjExpected.dfTarget) {
                        throw RuntimeException(pathCheck.dfTarget + " should be " + pathObjExpected.dfTarget)
                    }
                    listDeeplinkToCheck.add(
                        DeeplinkToCheck(
                            schemeCheck.scheme + "://" + hostCheck.host + (pathCheck.pattern?.pattern?.toExamplePath()
                                ?: ""),
                            pathCheck.dfTarget
                        )
                    )
                    // add more test: scheme://host/{pathA/pathB}
                    if (pathCheck.pattern == null) {
                        listDeeplinkToCheck.add(
                            DeeplinkToCheck(
                                schemeCheck.scheme + "://" + hostCheck.host + "/example",
                                pathCheck.dfTarget
                            )
                        )
                        listDeeplinkToCheck.add(
                            DeeplinkToCheck(
                                schemeCheck.scheme + "://" + hostCheck.host + "/example/",
                                pathCheck.dfTarget
                            )
                        )
                        listDeeplinkToCheck.add(
                            DeeplinkToCheck(
                                schemeCheck.scheme + "://" + hostCheck.host + "/example/123",
                                pathCheck.dfTarget
                            )
                        )
                    }
                }
            }
        }
        for (dfpSchemeToDf in dfToCheck) {
            val schemeCheckExpected = dfExpected.find { it.scheme == dfpSchemeToDf.scheme }
                ?: throw RuntimeException(dfpSchemeToDf.scheme + " is not available in expected. Deeplink: " + dfpSchemeToDf.toPatternString())
            for (host in dfpSchemeToDf.hostList) {
                val hostCheckExpected = schemeCheckExpected.hostList.find { it.host == host.host }
                    ?: throw RuntimeException(host.host + " is not available in expected. Deeplink: " + dfpSchemeToDf.scheme + "://" + host.toPatternString())
                for (pathObj in host.dfpPathObj) {
                    val pathCheckExpected =
                        hostCheckExpected.dfpPathObj.find { it.pattern?.pattern?.mapPatternAllToNull() == pathObj.pattern?.pattern?.mapPatternAllToNull() }
                            ?: throw RuntimeException(pathObj.pattern?.pattern + " is not available in expected. Deeplink: " + dfpSchemeToDf.scheme + "://" + hostCheckExpected.host + "/" + pathObj.toPatternString())
                    if (pathCheckExpected.dfTarget != pathObj.dfTarget) {
                        throw RuntimeException(pathObj.dfTarget + " should be " + pathCheckExpected.dfTarget)
                    }
                }
            }
        }
        for (deeplinktoCheck in listDeeplinkToCheck) {
            val checkedModule =
                getDFModuleFromInternal(isSellerapp, deeplinktoCheck.deeplink, false)
            println("${deeplinktoCheck.deeplink} -> $checkedModule. Expected: " + deeplinktoCheck.dfTarget)
            if (checkedModule != deeplinktoCheck.dfTarget) {
                throw RuntimeException(deeplinktoCheck.deeplink + " map into " + checkedModule + ". It should be map to " + deeplinktoCheck.dfTarget)
            }
        }
    }

    @JvmName("schemeToPatternString")
    private fun DFPSchemeToDF.toPatternString(): String {
        return this.scheme + "://" + this.hostList.toPatternString()
    }

    @JvmName("listHostToPatternString")
    private fun List<DFPHost>.toPatternString(): String {
        return this[0].toPatternString()
    }

    @JvmName("hostToPatternString")
    private fun DFPHost.toPatternString(): String {
        return this.host + "/" + this.dfpPathObj.toPatternString()
    }

    @JvmName("listPathToPatternString")
    private fun List<DFPPath>.toPatternString(): String {
        return this[0].toPatternString()
    }

    @JvmName("pathToPatternString")
    private fun DFPPath.toPatternString(): String {
        return (pattern?.pattern ?: "") + " -> " + dfTarget
    }

    private fun String?.mapPatternAllToNull(): String? {
        if (this == ".*") {
            return null
        }
        return this
    }

    private fun String?.toExamplePath(): String {
        if (this == null) {
            return ""
        }
        var examplePathString = this
        if (examplePathString.contains("[a-zA-Z0-9_ %-]*")) {
            examplePathString = examplePathString.replace("[a-zA-Z0-9_ %-]*", "example12-3_4")
        }
        if (examplePathString.contains("*")) {
            val charBefore = examplePathString[examplePathString.indexOf("*") - 1]
            examplePathString =
                examplePathString.replace("$charBefore*", "$charBefore$charBefore$charBefore")
        }
        return examplePathString
    }

    open fun setAllowingDebugToolsFalse() {
        GlobalConfig.DEBUG = false
        GlobalConfig.ENABLE_DISTRIBUTION = false
    }

    fun assertEqualDeepLinkMA(
        deeplink: String,
        expectedModule: String
    ) {
        val checkedModule = getDFModuleFromExternal(false, deeplink, false)
        assertEquals(expectedModule.mapBaseToNull(), checkedModule.mapBaseToNull())
    }

    fun assertEqualDeepLinkSA(
        deeplink: String,
        expectedModule: String
    ) {
        val checkedModule = getDFModuleFromExternal(true, deeplink, false)
        assertEquals(expectedModule.mapBaseToNull(), checkedModule.mapBaseToNull())
    }

    private fun assertEqualDeepLinkApp(
        isSellerapp: Boolean,
        deeplink: String
    ) {
        val expectedModule = getDFModuleFromInternal(isSellerapp, deeplink, true)
        val checkedModule = getDFModuleFromInternal(isSellerapp, deeplink, false)
        println("$deeplink -> $checkedModule. Expected: $expectedModule")
        assertEquals(expectedModule.mapBaseToNull(), checkedModule.mapBaseToNull())
    }

    private fun getDFModuleFromInternal(
        isSellerapp: Boolean,
        deeplink: String,
        isExpected: Boolean
    ): String? {
        val deeplinkToCheck: List<DFPSchemeToDF>?
        if (isSellerapp) {
            GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
            deeplinkToCheck = if (isExpected) {
                internalDeeplinkDFPatternListSellerApp
            } else {
                expectedPatternSellerapp
            } ?: emptyList()
        } else {
            GlobalConfig.APPLICATION_TYPE = GlobalConfig.CONSUMER_APPLICATION
            deeplinkToCheck = if (isExpected) {
                internalDeeplinkDFPatternListCustomerApp
            } else {
                expectedPatternCustomerapp
            } ?: emptyList()
        }
        return getDFModuleFromDeeplink(
            Uri.parse(deeplink),
            deeplinkToCheck
        )?.dfTarget
    }

    private fun getDFModuleFromExternal(
        isSellerapp: Boolean,
        deeplink: String,
        isExpected: Boolean
    ): String? {
        val deeplinkToCheck: List<DFPSchemeToDF>?
        val deeplinkTrim = deeplink.replace("{", "").replace("}", "")
        if (isSellerapp) {
            GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
            deeplinkToCheck = if (isExpected) {
                internalDeeplinkDFPatternListSellerApp
            } else {
                expectedPatternSellerapp
            } ?: emptyList()
        } else {
            GlobalConfig.APPLICATION_TYPE = GlobalConfig.CONSUMER_APPLICATION
            deeplinkToCheck = if (isExpected) {
                internalDeeplinkDFPatternListCustomerApp
            } else {
                expectedPatternCustomerapp
            } ?: emptyList()
        }
        var mappedDeeplink = getRegisteredNavigation(context, deeplinkTrim)
        if (mappedDeeplink.isEmpty()) {
            mappedDeeplink = deeplinkTrim
        } else {
            println("mapped deeplink $deeplinkTrim -> $mappedDeeplink")
        }
        return getDFModuleFromDeeplink(
            Uri.parse(mappedDeeplink),
            deeplinkToCheck
        )?.dfTarget
    }

    private fun String?.mapBaseToNull(): String? {
        if (this == null) {
            return null
        }
        if (this == DeeplinkDFMapper.DF_BASE || this == DeeplinkDFMapper.DF_BASE_SELLER_APP) {
            return null
        }
        return this
    }
}