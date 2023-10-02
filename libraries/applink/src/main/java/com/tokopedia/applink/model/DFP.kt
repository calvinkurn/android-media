package com.tokopedia.applink.model

class DFP (
    val scheme: String,
    val host: String,
    val pathType: PathType,
    val pathString: String,
    val webviewFallbackLogic: String? = null
)

class DFPSchemeToDF(
    val scheme: String,
    val hostList: MutableList<DFPHost>
)

class DFPHost(
    val host: String,
    val dfpPathObj: MutableList<DFPPath>
)

class DFPPath(
    val pattern: Regex? = null,
    val dfTarget: String,
    val webviewFallbackUrl: String? = null
)

enum class PathType(val typeInt: Int) {
    NO_PATH(-1),
    PATH(0), // complete path
    PREFIX(1), // path starts with
    PATTERN(2), // path regex .* and *
}