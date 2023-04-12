package com.tokopedia.tokopedianow.data

import com.tokopedia.tokopedianow.common.model.TokoNowBundleUiModel
import com.tokopedia.tokopedianow.home.domain.model.Header
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse

object BundleWidgetDataFactory {
    fun createBundleUiModel(
        id: String,
        bundleIds: List<String>
    ): TokoNowBundleUiModel {
        return TokoNowBundleUiModel(
            id = id,
            bundleIds = bundleIds
        )
    }

    fun createBundleResponseList(
        id: String
    ): List<HomeLayoutResponse> {
        return listOf(
            HomeLayoutResponse(
                id = id,
                layout = "product_bundling",
                header = Header(
                    name = "Product Bundling",
                    serverTimeUnix = 0
                )
            )
        )
    }
}
