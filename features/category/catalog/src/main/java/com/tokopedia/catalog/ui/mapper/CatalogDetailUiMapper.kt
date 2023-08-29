package com.tokopedia.catalog.ui.mapper

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog.ui.model.CatalogDetailUiModel
import com.tokopedia.catalog.ui.model.NavigationProperties
import com.tokopedia.catalogcommon.uimodel.DummyUiModel
import com.tokopedia.catalogcommon.uimodel.HeroBannerUiModel
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.oldcatalog.model.raw.CatalogResponseData
import javax.inject.Inject

class CatalogDetailUiMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun mapToWidgetVisitables(
        remoteModel: CatalogResponseData.CatalogGetDetailModular
    ): List<Visitable<*>>{
        val isDarkMode = remoteModel.globalStyle?.darkMode.orFalse()
        val bgColor = remoteModel.globalStyle?.bgColor
        val textColorRes = if (remoteModel.globalStyle?.darkMode == true) {
            com.tokopedia.unifycomponents.R.color.Unify_Static_White
        } else {
            com.tokopedia.unifycomponents.R.color.Unify_Static_Black
        }
        return remoteModel.layouts?.map {
            if (it.type == "catalog_hero") {
                HeroBannerUiModel(
                    idWidget = it.type + it.name,
                    widgetType = it.type,
                    widgetName = it.name,
                    widgetBackgroundColor = "#$bgColor".stringHexColorParseToInt(),
                    widgetTextColor = MethodChecker.getColor(context, textColorRes),
                    darkMode = isDarkMode,
                    isPremium = it.data?.style?.isPremium.orFalse(),
                    brandTitle = it.data?.hero?.name.orEmpty(),
                    brandImageUrls = it.data?.hero?.heroSlide?.map { heroSlide -> heroSlide.imageUrl.orEmpty() }.orEmpty()
                )
            } else {
                DummyUiModel(
                    idWidget = it.type + it.name,
                    widgetType = it.type,
                    widgetName = it.name,
                    widgetBackgroundColor = "#$bgColor".stringHexColorParseToInt(),
                    widgetTextColor = MethodChecker.getColor(context, textColorRes),
                    darkMode = isDarkMode,
                    content = it.name
                )
            }
        }.orEmpty()
    }

    fun mapToCatalogDetailUiModel(
        remoteModel: CatalogResponseData.CatalogGetDetailModular
    ): CatalogDetailUiModel {
        val widgets = mapToWidgetVisitables(remoteModel)
        return CatalogDetailUiModel (
            widgets = widgets,
            navigationProperties = mapToNavigationProperties(remoteModel, widgets)
        )
    }

    private fun mapToNavigationProperties(
        remoteModel: CatalogResponseData.CatalogGetDetailModular,
        widgets: List<Visitable<*>>,
    ): NavigationProperties {
        val heroImage = widgets.firstOrNull { it is HeroBannerUiModel } as? HeroBannerUiModel
        return NavigationProperties(
            isDarkMode = remoteModel.globalStyle?.darkMode.orFalse(),
            isPremium = heroImage?.isPremium.orFalse(),
            bgColor = "#${remoteModel.globalStyle?.bgColor}".stringHexColorParseToInt(),
            title = remoteModel.basicInfo.name.orEmpty()
        )
    }
}
