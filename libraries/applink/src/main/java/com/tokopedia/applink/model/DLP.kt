package com.tokopedia.applink.model

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.UriUtil

class DLPv2(
    val logic: DLPLogicv2,
    val targetDeeplink: (context: Context, uri: Uri, deeplink: String, idList: List<String>?) -> String
) {

    companion object {
        @JvmName("goToStr")
        fun goTo(targetDeeplink: String): DLPv2 {
            return DLPv2(Always) { _, _, _, _ -> targetDeeplink }
        }

        @JvmName("goTo")
        fun goTo(targetDeeplink: (context: Context, uri: Uri, deeplink: String, idList: List<String>?) -> String): DLPv2 {
            return DLPv2(Always, targetDeeplink)
        }

        @JvmName("goToUriDeeplink")
        fun goTo(targetDeeplink: (uri: Uri, deeplink: String) -> String): DLPv2 {
            return DLPv2(Always) { _, uri, deepl, _ -> targetDeeplink(uri, deepl) }
        }
        @JvmName("goToUri")
        fun goTo(targetDeeplink: (uri: Uri) -> String): DLPv2 {
            return DLPv2(Always) { _, uri, _, _ -> targetDeeplink(uri) }
        }

        @JvmName("goToCtxDeeplink")
        fun goTo(targetDeeplink: (context: Context, deeplink: String) -> String): DLPv2 {
            return DLPv2(Always) { c, _, deepl, _ -> targetDeeplink(c, deepl) }
        }

        @JvmName("goToCtxUri")
        fun goTo(targetDeeplink: (context: Context, uri: Uri) -> String): DLPv2 {
            return DLPv2(Always) { c, uri, _, _ -> targetDeeplink(c, uri) }
        }

        @JvmName("goToDeeplink")
        fun goTo(targetDeeplink: (deeplink: String) -> String): DLPv2 {
            return DLPv2(Always) { _, _, deepl, _ -> targetDeeplink(deepl) }
        }

        @JvmName("match")
        fun matchPattern(
            pathCheck: String,
            targetDeeplink: (context: Context, uri: Uri, deeplink: String, idList: List<String>?) -> String
        ): DLPv2 {
            return DLPv2(MatchPattern(pathCheck), targetDeeplink)
        }

        @JvmName("matchDeeplink")
        fun matchPattern(
            pathCheck: String,
            targetDeeplink: (deeplink: String) -> String
        ): DLPv2 {
            return DLPv2(MatchPattern(pathCheck)) { _, _, deeplink, _ -> targetDeeplink(deeplink) }
        }

        @JvmName("matchFunc")
        fun matchPattern(
            pathCheck: String,
            targetDeeplink: () -> String
        ): DLPv2 {
            return DLPv2(MatchPattern(pathCheck)) { _, _, _, _ -> targetDeeplink() }
        }

        @JvmName("matchUri")
        fun matchPattern(
            pathCheck: String,
            targetDeeplink: (uri: Uri) -> String
        ): DLPv2 {
            return DLPv2(MatchPattern(pathCheck)) { _, uri, _, _ -> targetDeeplink(uri) }
        }

        @JvmName("matchUriId")
        fun matchPattern(
            pathCheck: String,
            targetDeeplink: (uri: Uri, idList: List<String>?) -> String
        ): DLPv2 {
            return DLPv2(MatchPattern(pathCheck)) { _, uri, _, idList ->
                targetDeeplink(
                    uri,
                    idList
                )
            }
        }

        @JvmName("matchCtxDeeplink")
        fun matchPattern(
            pathCheck: String,
            targetDeeplink: (ctx: Context, deeplink: String) -> String
        ): DLPv2 {
            return DLPv2(MatchPattern(pathCheck)) { c, _, d, _ ->
                targetDeeplink(
                    c,
                    d
                )
            }
        }

        @JvmName("matchStr")
        fun matchPattern(
            pathCheck: String,
            targetDeeplink: String
        ): DLPv2 {
            return DLPv2(MatchPattern(pathCheck)) { _, _, _, _ -> targetDeeplink }
        }

        @JvmName("startStr")
        fun startsWith(pathCheck: String, targetDeeplink: String): DLPv2 {
            return DLPv2(StartsWith(pathCheck)) { _, _, _, _ -> targetDeeplink }
        }

        @JvmName("startCtxUri")
        fun startsWith(
            pathCheck: String,
            targetDeeplink: (context: Context, uri: Uri) -> String
        ): DLPv2 {
            return DLPv2(StartsWith(pathCheck)) { c, uri, _, _ -> targetDeeplink(c, uri) }
        }

        @JvmName("startCtxDeeplink")
        fun startsWith(
            pathCheck: String,
            targetDeeplink: (context: Context, deeplink: String) -> String
        ): DLPv2 {
            return DLPv2(StartsWith(pathCheck)) { c, _, d, _ -> targetDeeplink(c, d) }
        }

        @JvmName("start")
        fun startsWith(
            pathCheck: String,
            targetDeeplink: (context: Context, uri: Uri, deeplink: String, idList: List<String>?) -> String
        ): DLPv2 {
            return DLPv2(StartsWith(pathCheck), targetDeeplink)
        }

        @JvmName("startFunc")
        fun startsWith(
            pathCheck: String,
            targetDeeplink: () -> String
        ): DLPv2 {
            return DLPv2(StartsWith(pathCheck)) { _, _, _, _ -> targetDeeplink() }
        }

        @JvmName("startUri")
        fun startsWith(
            pathCheck: String,
            targetDeeplink: (uri: Uri) -> String
        ): DLPv2 {
            return DLPv2(StartsWith(pathCheck)) { _, uri, _, _ -> targetDeeplink(uri) }
        }

        @JvmName("startDeeplink")
        fun startsWith(pathCheck: String, targetDeeplink: (deeplink: String) -> String): DLPv2 {
            return DLPv2(StartsWith(pathCheck)) { _, _, deeplink, _ -> targetDeeplink(deeplink) }
        }

        @JvmName("startCtxUriDeeplink")
        fun startsWith(
            pathCheck: String,
            targetDeeplink: (context: Context, uri: Uri, deeplink: String) -> String
        ): DLPv2 {
            return DLPv2(StartsWith(pathCheck)) { c, uri, deeplink, _ ->
                targetDeeplink(
                    c,
                    uri,
                    deeplink
                )
            }
        }
    }
}

open class DLPLogicv2(
    val logic: ((context: Context, uri: Uri, deeplink: String) -> Pair<Boolean, List<String>?>)
) {
    operator fun plus(additionalLogic: () -> Boolean): DLPLogicv2 {
        return DLPLogicv2(logic = { context, uri, deeplink ->
            val resultFirstLogic = logic.invoke(context, uri, deeplink)
            val isMatchResult = resultFirstLogic.first
            val idListResult = resultFirstLogic.second
            ((isMatchResult && additionalLogic.invoke()) to idListResult)
        })
    }

    operator fun plus(additionalLogic: (context: Context, uri: Uri, deeplink: String) -> Boolean): DLPLogicv2 {
        return DLPLogicv2(logic = { context, uri, deeplink ->
            val resultFirstLogic = logic.invoke(context, uri, deeplink)
            val isMatchResult = resultFirstLogic.first
            val idListResult = resultFirstLogic.second
            val resultSecondLogic = additionalLogic.invoke(context, uri, deeplink)
            ((isMatchResult && resultSecondLogic) to idListResult)
        })
    }
}

infix fun DLPLogicv2.or(other: DLPLogicv2): DLPLogicv2 {
    return DLPLogicv2(logic = { context, uri, deeplink ->
        val resultFirstLogic = this.logic.invoke(context, uri, deeplink)
        val isMatchResult = resultFirstLogic.first
        val resultSecondLogic = other.logic.invoke(context, uri, deeplink)
        val isMatchResultOther = resultSecondLogic.first
        val idListResultOther = resultSecondLogic.second
        val idList: List<String>? = resultFirstLogic.second ?: idListResultOther
        ((isMatchResult || isMatchResultOther) to idList)
    })
}

object Always : DLPLogicv2(logic = { _, _, _ ->
    (true to null)
})

class StartsWith(sourcePath: String) : DLPLogicv2(logic = { _, uri, _ ->
    val uriPath = uri.path
    val result = if (uriPath == null) {
        (false to null)
    } else {
        (uriPath.trimSlash().startsWith(sourcePath.trimSlash()) to null)
    }
    result

})

fun String.trimSlash(): String {
    var output = this
    if (this.startsWith("/")) {
        output = this.drop(1)
    }
    if (output.endsWith("/")) {
        output = output.dropLast(1)
    }
    return output
}

class MatchPattern(sourcePath: String) : DLPLogicv2(logic = { _, uri, _ ->
    val list = UriUtil.matchPathsWithPattern(
        sourcePath.trimSlash().split("/"),
        uri.pathSegments
    )
    ((list != null) to list)
})