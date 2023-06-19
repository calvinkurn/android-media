package com.tokopedia.applink.model.legacy

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.UriUtil

class DLP(
    val logic: DLPLogic,
    val targetDeeplink: (context: Context, uri: Uri, deeplink: String, idList: List<String>?) -> String
) {
    object DLPType {
        const val START_WITH = "StartsWith"
        const val MATCH_PATTERN = "MatchPattern"
        const val EXACT = "Exact"
        const val HOST = "Host"
    }
    companion object {
        fun startWith(deeplinkCheck: String, targetDeeplink: String): DLP {
            return DLP(StartsWith(deeplinkCheck)) { _, _, _, _ -> targetDeeplink }
        }

        fun startWith(deeplinkCheck: String, targetDeeplink: (context: Context, uri: Uri, deeplink: String, idList: List<String>?) -> String): DLP {
            return DLP(StartsWith(deeplinkCheck), targetDeeplink)
        }

        fun matchPattern(deeplinkCheck: String, targetDeeplink: (context: Context, uri: Uri, deeplink: String, idList: List<String>?) -> String): DLP {
            return DLP(MatchPattern(deeplinkCheck), targetDeeplink)
        }

        fun exact(deeplinkCheck: String, targetDeeplink: String): DLP {
            return DLP(Exact(deeplinkCheck)) { _, _, _, _ -> targetDeeplink }
        }

        fun host(host: String, targetDeeplink: (context: Context, uri: Uri, deeplink: String, idList: List<String>?) -> String): DLP {
            return DLP(Host(host), targetDeeplink)
        }

        fun exact(deeplinkCheck: String, targetDeeplink: (context: Context, uri: Uri, deeplink: String, idList: List<String>?) -> String): DLP {
            return DLP(Exact(deeplinkCheck), targetDeeplink)
        }
    }
}

open class DLPLogic(
    val logic: ((context: Context, uri: Uri, deeplink: String) -> Pair<Boolean, List<String>?>),
    val needTrimBeforeLogicRun: Boolean = false
) {
    constructor(mapLogic: (context: Context, uri: Uri, deeplink: String) -> Boolean) : this(
        logic = { context, uri, deeplink -> mapLogic(context, uri, deeplink) to null },
        false
    )

    operator fun plus(additionalLogic: () -> Boolean): DLPLogic {
        return DLPLogic(logic = { context, uri, deeplink ->
            val resultFirstLogic = logic.invoke(context, uri, deeplink)
            val isMatchResult = resultFirstLogic.first
            val idListResult = resultFirstLogic.second
            ((isMatchResult && additionalLogic.invoke()) to idListResult)
        }, needTrimBeforeLogicRun)
    }

    operator fun plus(additionalLogic: (context: Context, uri: Uri, deeplink: String) -> Boolean): DLPLogic {
        return DLPLogic(logic = { context, uri, deeplink ->
            val resultFirstLogic = logic.invoke(context, uri, deeplink)
            val isMatchResult = resultFirstLogic.first
            val idListResult = resultFirstLogic.second
            val resultSecondLogic = additionalLogic.invoke(context, uri, deeplink)
            ((isMatchResult && resultSecondLogic) to idListResult)
        }, needTrimBeforeLogicRun)
    }
}

infix fun DLPLogic.or(other: DLPLogic): DLPLogic {
    return DLPLogic(logic = { context, uri, deeplink ->
        val resultFirstLogic = this.logic.invoke(context, uri, deeplink)
        val isMatchResult = resultFirstLogic.first
        val resultSecondLogic = other.logic.invoke(context, uri, deeplink)
        val isMatchResultOther = resultSecondLogic.first
        val idListResultOther = resultSecondLogic.second
        val idList: List<String>? = resultFirstLogic.second ?: idListResultOther
        ((isMatchResult || isMatchResultOther) to idList)
    }, needTrimBeforeLogicRun)
}

class Exact(target: String) : DLPLogic(logic = { _, _, deeplink -> ((deeplink == target) to null) }, true)
class StartsWith(target: String) : DLPLogic(logic = { _, _, deeplink ->
    (deeplink.startsWith(target) to null)
})

class MatchPattern(target: String) : DLPLogic(logic = { _, uri, _ ->
    val list = UriUtil.matchWithPattern(target, uri, false)
    ((list != null) to list)
})

class Host(target: String) : DLPLogic(logic = { _, uri, _ ->
    ((uri.host == target) to null)
})