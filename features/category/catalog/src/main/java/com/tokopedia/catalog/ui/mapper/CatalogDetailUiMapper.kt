package com.tokopedia.catalog.ui.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.catalog.ui.model.CatalogDetailUiModel
import com.tokopedia.catalogcommon.uimodel.DummyUiModel
import com.tokopedia.oldcatalog.model.raw.CatalogResponseData

object CatalogDetailUiMapper {
    fun mapToWidgetVisitables(
        remoteModel: CatalogResponseData.CatalogGetDetailModular
    ): List<Visitable<*>>{
        return remoteModel.layouts?.map {
            DummyUiModel(
                idWidget = "",
                widgetType = it.type,
                widgetName = it.name,
                widgetBackgroundColor = null,
                widgetTextColor = null,
                darkMode = false,
                content = it.name
            )
        }.orEmpty()
    }

    fun mapToCatalogDetailUiModel(
        remoteModel: CatalogResponseData.CatalogGetDetailModular
    ) = CatalogDetailUiModel (
        widgets = mapToWidgetVisitables(remoteModel)
    )
}
