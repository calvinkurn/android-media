package com.tokopedia.tokopedianow.data

import com.tokopedia.tokopedianow.common.model.TokoNowBundleUiModel
import com.tokopedia.tokopedianow.home.domain.model.Header
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse

object BundleWidgetDataFactory {
    fun createBundleUiModel(
        id: String,
        bundleIds: List<String>,
        title: String
    ): TokoNowBundleUiModel {
        return TokoNowBundleUiModel(
            id = id,
            bundleIds = bundleIds,
            title = title
        )
    }

    fun createBundleResponseList(
        id: String,
        headerName: String
    ): List<HomeLayoutResponse> {
        return listOf(
            HomeLayoutResponse(
                id = id,
                layout = "bundling_widget",
                header = Header(
                    name = headerName,
                    serverTimeUnix = 0
                )
            )
        )
    }
}
