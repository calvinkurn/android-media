package com.tokopedia.analytics.byteio.recommendation

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam

interface AppLogAdditionalParam {
    val parameters: Map<String, Any>
        get() = hashMapOf()

    data class PDP(
        val parentProductId: String = "",
        val parentTrackId: String = "",
        val parentRequestId: String = "",
        val firstTrackId: String = "",
        val firstSourcePage: String = "",
    ): AppLogAdditionalParam {
        override val parameters: Map<String, Any>
            get() = hashMapOf(
                AppLogParam.PARENT_PRODUCT_ID to parentProductId,
                AppLogParam.PARENT_TRACK_ID to parentTrackId,
                AppLogParam.PARENT_REQUEST_ID to parentRequestId,
                AppLogParam.FIRST_TRACK_ID to firstTrackId,
                AppLogParam.FIRST_SOURCE_PAGE to firstSourcePage,
            )

        override fun setAdditionalToGlobalParam() {
            super.setAdditionalToGlobalParam()
            AppLogAnalytics.setGlobalParams(
                parentProductId = parentProductId,
                parentTrackId = parentTrackId,
                parentRequestId = parentRequestId
            )
        }
    }

    fun setAdditionalToGlobalParam() {}

    object None : AppLogAdditionalParam
}
