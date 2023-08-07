package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.minicart.common.domain.data.ProductBundleRecomResponse
import com.tokopedia.tokopedianow.common.model.TokoNowBundleUiModel
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel

object BundleMapper {
    fun mapToProductBundleLayout(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val bundleUiModel = TokoNowBundleUiModel(id = response.id, title = response.header.name)
        return HomeLayoutItemUiModel(bundleUiModel, state)
    }

    fun mapToProductBundleUiModel(
        item: TokoNowBundleUiModel,
        widgetResponse: ProductBundleRecomResponse
    ) = item.copy(bundleIds = mapToProductBundleListItemUiModel(widgetResponse.tokonowBundleWidget.data.widgetData))

    fun mapToProductBundleListItemUiModel(
        widgetData: List<ProductBundleRecomResponse.TokonowBundleWidget.Data.WidgetData>
    ): List<String> {
        val bundleIds = mutableListOf<String>()
        widgetData.forEach {
            it.bundleDetails.forEach { bundle ->
                bundleIds.add(bundle.bundleID)
            }
        }
        return bundleIds
    }
}
