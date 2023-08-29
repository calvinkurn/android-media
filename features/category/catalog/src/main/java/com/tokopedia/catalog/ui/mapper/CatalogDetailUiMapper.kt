package com.tokopedia.catalog.ui.mapper

import android.content.Context
import android.graphics.Color
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog.ui.model.CatalogDetailUiModel
import com.tokopedia.catalog.ui.model.NavigationProperties
import com.tokopedia.catalog.ui.model.PriceCtaProperties
import com.tokopedia.catalog.ui.model.WidgetTypes
import com.tokopedia.catalogcommon.uimodel.BaseCatalogUiModel
import com.tokopedia.catalogcommon.uimodel.DummyUiModel
import com.tokopedia.catalogcommon.uimodel.HeroBannerUiModel
import com.tokopedia.catalogcommon.uimodel.StickyNavigationUiModel
import com.tokopedia.catalogcommon.uimodel.TopFeaturesUiModel
import com.tokopedia.catalogcommon.uimodel.TrustMakerUiModel
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.oldcatalog.model.raw.CatalogResponseData
import javax.inject.Inject

class CatalogDetailUiMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun mapToWidgetVisitables(
        remoteModel: CatalogResponseData.CatalogGetDetailModular
    ): List<Visitable<*>>{
        return remoteModel.layouts?.filter {
            !it.data?.style?.isHidden.orTrue()
        }?.map {
            when (it.type) {
                WidgetTypes.CATALOG_HERO.type -> it.mapToHeroBanner()
                WidgetTypes.CATALOG_FEATURE_TOP.type -> it.mapToTopFeature(remoteModel)
                WidgetTypes.CATALOG_TRUSTMAKER.type -> it.mapToTrustMaker()
                WidgetTypes.CATALOG_CHARACTERISTIC.type -> { DummyUiModel(content = it.name)}
                WidgetTypes.CATALOG_BANNER_SINGLE.type -> { DummyUiModel(content = it.name)}
                WidgetTypes.CATALOG_BANNER_DOUBLE.type -> { DummyUiModel(content = it.name)}
                WidgetTypes.CATALOG_PANEL_IMAGE.type -> { DummyUiModel(content = it.name)}
                WidgetTypes.CATALOG_NAVIGATION.type -> it.mapToStickyNavigation()
                WidgetTypes.CATALOG_SLIDER_IMAGE.type -> { DummyUiModel(content = it.name)}
                WidgetTypes.CATALOG_TEXT.type -> { DummyUiModel(content = it.name)}
                WidgetTypes.CATALOG_REVIEW_EXPERT.type -> { DummyUiModel(content = it.name)}
                WidgetTypes.CATALOG_FEATURE_SUPPORT.type -> { DummyUiModel(content = it.name)}
                WidgetTypes.CATALOG_ACCORDION.type -> { DummyUiModel(content = it.name)}
                WidgetTypes.CATALOG_COLUMN_INFO.type -> { DummyUiModel(content = it.name)}
                WidgetTypes.CATALOG_COMPARISON.type -> { DummyUiModel(content = it.name)}
                WidgetTypes.CATALOG_SIMILAR_PRODUCT.type -> { DummyUiModel(content = it.name)}
                WidgetTypes.CATALOG_REVIEW_BUYER.type -> { DummyUiModel(content = it.name)}
                WidgetTypes.CATALOG_CTA_PRICE.type -> { DummyUiModel(content = it.name)}
                else -> { DummyUiModel(content = it.name) }
            }.applyGlobalProperies(remoteModel, it)
        }.orEmpty()
    }

    fun mapToCatalogDetailUiModel(
        remoteModel: CatalogResponseData.CatalogGetDetailModular
    ): CatalogDetailUiModel {
        val widgets = mapToWidgetVisitables(remoteModel)
        return CatalogDetailUiModel (
            widgets = widgets,
            navigationProperties = mapToNavigationProperties(remoteModel, widgets),
            priceCtaProperties = mapToPriceCtaProperties(remoteModel)
        )
    }

    private fun mapToPriceCtaProperties(remoteModel: CatalogResponseData.CatalogGetDetailModular): PriceCtaProperties {
        val bgColor = remoteModel.globalStyle?.bgColor
        val ctaColor = remoteModel.globalStyle?.secondaryColor
        val textColorRes = if (remoteModel.globalStyle?.darkMode == true) {
            com.tokopedia.unifycomponents.R.color.Unify_Static_White
        } else {
            com.tokopedia.unifycomponents.R.color.Unify_Static_Black
        }
        return remoteModel.layouts?.firstOrNull {
            it.type == "catalog_cta_price"
        }?.data?.run {
            val marketPrice = priceCta.marketPrice.firstOrNull()
            PriceCtaProperties(
                price = listOf(marketPrice?.minFmt.orEmpty(), marketPrice?.maxFmt.orEmpty()).joinToString(" - ") ,
                productName = priceCta.name.orEmpty(),
                bgColor = "#$bgColor".stringHexColorParseToInt(),
                ctaColor = "#$ctaColor".stringHexColorParseToInt(),
                MethodChecker.getColor(context, textColorRes)
            )
        } ?: PriceCtaProperties()
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

    private fun BaseCatalogUiModel.applyGlobalProperies(
        remoteModel: CatalogResponseData.CatalogGetDetailModular,
        layout: CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout
    ): BaseCatalogUiModel {
        val isDarkMode = remoteModel.globalStyle?.darkMode.orFalse()
        val bgColor = remoteModel.globalStyle?.bgColor
        return apply {
            idWidget = layout.type + layout.name
            widgetType = layout.type
            widgetName = layout.name
            widgetBackgroundColor = "#$bgColor".stringHexColorParseToInt()
            widgetTextColor = getTextColor(isDarkMode)
            darkMode = isDarkMode
        }
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToHeroBanner() =
        HeroBannerUiModel(
            isPremium = data?.style?.isPremium.orFalse(),
            brandTitle = data?.hero?.name.orEmpty(),
            brandImageUrls = data?.hero?.heroSlide?.map { heroSlide ->
                heroSlide.imageUrl.orEmpty()
            }.orEmpty(),
            brandDescriptions = data?.hero?.heroSlide?.map { heroSlide ->
                heroSlide.subtitle.orEmpty()
            }.orEmpty(),
            brandIconUrl = data?.hero?.brandLogoUrl.orEmpty()
        )

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToTopFeature(
        remoteModel: CatalogResponseData.CatalogGetDetailModular
    ): TopFeaturesUiModel {
        val isDarkMode = remoteModel.globalStyle?.darkMode.orFalse()
        return TopFeaturesUiModel(
            items = data?.topFeature?.map {
                TopFeaturesUiModel.ItemTopFeatureUiModel(
                    id = it.desc.orEmpty(),
                    icon = it.iconUrl.orEmpty(),
                    name = it.desc.orEmpty(),
                    backgroundColor = Color.TRANSPARENT,
                    textColor = getTextColor(isDarkMode),
                )
            }.orEmpty()
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToStickyNavigation(): StickyNavigationUiModel {
        return StickyNavigationUiModel(
            content = data?.navigation?.map {
                StickyNavigationUiModel.StickyNavigationItemData(it.title.orEmpty())
            }.orEmpty()
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToTrustMaker(): TrustMakerUiModel {
        return TrustMakerUiModel(
            items = data?.trustmaker.orEmpty().map {
                TrustMakerUiModel.ItemTrustMakerUiModel(
                    id = "",
                    icon = it.imageUrl.orEmpty(),
                    title = it.title.orEmpty(),
                    subTitle = it.subtitle.orEmpty(),
                    textColorTitle = Color.BLACK,
                    textColorSubTitle = Color.BLACK
                )
            }
        )
    }

    private fun getTextColor(darkMode: Boolean): Int {
        val textColorRes = if (darkMode) {
            com.tokopedia.unifycomponents.R.color.Unify_Static_White
        } else {
            com.tokopedia.unifycomponents.R.color.Unify_Static_Black
        }
        return MethodChecker.getColor(context, textColorRes)
    }


}
