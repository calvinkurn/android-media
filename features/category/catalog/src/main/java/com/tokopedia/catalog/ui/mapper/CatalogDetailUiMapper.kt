package com.tokopedia.catalog.ui.mapper

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog.ui.model.CatalogDetailUiModel
import com.tokopedia.catalog.ui.model.NavigationProperties
import com.tokopedia.catalog.ui.model.PriceCtaProperties
import com.tokopedia.catalog.ui.model.WidgetTypes
import com.tokopedia.catalog.util.ColorConstant.DARK_COLOR
import com.tokopedia.catalog.util.ColorConstant.DARK_COLOR_01
import com.tokopedia.catalog.util.ColorConstant.DARK_COLOR_ACCORDION_01
import com.tokopedia.catalog.util.ColorConstant.DARK_COLOR_ACCORDION_02
import com.tokopedia.catalog.util.ColorConstant.DARK_COLOR_ACCORDION_ARROW
import com.tokopedia.catalog.util.ColorConstant.DARK_COLOR_BANNER
import com.tokopedia.catalog.util.ColorConstant.DARK_COLOR_IMAGE_TEXT
import com.tokopedia.catalog.util.ColorConstant.DARK_COLOR_SUPPORT_FEATURE_01
import com.tokopedia.catalog.util.ColorConstant.LIGHT_COLOR
import com.tokopedia.catalog.util.ColorConstant.LIGHT_COLOR_01
import com.tokopedia.catalog.util.ColorConstant.LIGHT_COLOR_ACCORDION_01
import com.tokopedia.catalog.util.ColorConstant.LIGHT_COLOR_ACCORDION_02
import com.tokopedia.catalog.util.ColorConstant.LIGHT_COLOR_ACCORDION_ARROW
import com.tokopedia.catalog.util.ColorConstant.LIGHT_COLOR_BANNER
import com.tokopedia.catalog.util.ColorConstant.LIGHT_COLOR_IMAGE_TEXT
import com.tokopedia.catalog.util.ColorConstant.LIGHT_COLOR_SUPPORT_FEATURE_01
import com.tokopedia.catalogcommon.uimodel.AccordionInformationUiModel
import com.tokopedia.catalogcommon.uimodel.BannerCatalogUiModel
import com.tokopedia.catalogcommon.uimodel.BaseCatalogUiModel
import com.tokopedia.catalogcommon.uimodel.BlankUiModel
import com.tokopedia.catalogcommon.uimodel.CharacteristicUiModel
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.catalogcommon.uimodel.DoubleBannerCatalogUiModel
import com.tokopedia.catalogcommon.uimodel.ExpertReviewUiModel
import com.tokopedia.catalogcommon.uimodel.HeroBannerUiModel
import com.tokopedia.catalogcommon.uimodel.SliderImageTextUiModel
import com.tokopedia.catalogcommon.uimodel.StickyNavigationUiModel
import com.tokopedia.catalogcommon.uimodel.SupportFeaturesUiModel
import com.tokopedia.catalogcommon.uimodel.TextDescriptionUiModel
import com.tokopedia.catalogcommon.uimodel.TopFeaturesUiModel
import com.tokopedia.catalogcommon.uimodel.TrustMakerUiModel
import com.tokopedia.catalogcommon.util.colorMapping
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.oldcatalog.model.raw.CatalogResponseData
import javax.inject.Inject
import com.tokopedia.catalogcommon.R as catalogcommonR
import com.tokopedia.unifycomponents.R as unifycomponentsR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CatalogDetailUiMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val LAYOUT_VERSION_4_VALUE = 4
    }

    fun mapToWidgetVisitables(
        remoteModel: CatalogResponseData.CatalogGetDetailModular
    ): List<Visitable<*>> {
        val isDarkMode = remoteModel.globalStyle?.darkMode.orFalse()
        return remoteModel.layouts?.filter {
            !it.data?.style?.isHidden.orTrue()
        }?.map {
            when (it.type) {
                WidgetTypes.CATALOG_HERO.type -> it.mapToHeroBanner(isDarkMode)
                WidgetTypes.CATALOG_FEATURE_TOP.type -> it.mapToTopFeature(remoteModel)
                WidgetTypes.CATALOG_TRUSTMAKER.type -> it.mapToTrustMaker(isDarkMode)
                WidgetTypes.CATALOG_CHARACTERISTIC.type -> it.mapToCharacteristic(isDarkMode)
                WidgetTypes.CATALOG_BANNER_SINGLE.type -> it.mapToBannerImage(isDarkMode)
                WidgetTypes.CATALOG_BANNER_DOUBLE.type -> it.mapToDoubleBannerImage(isDarkMode)
                WidgetTypes.CATALOG_NAVIGATION.type -> it.mapToStickyNavigation(remoteModel)
                WidgetTypes.CATALOG_SLIDER_IMAGE.type -> it.mapToSliderImageText(isDarkMode)
                WidgetTypes.CATALOG_TEXT.type -> it.mapToTextDescription(isDarkMode)
                WidgetTypes.CATALOG_REVIEW_EXPERT.type -> it.mapToExpertReview(isDarkMode)
                WidgetTypes.CATALOG_FEATURE_SUPPORT.type -> it.mapToSupportFeature(remoteModel)
                WidgetTypes.CATALOG_ACCORDION.type -> it.mapToAccordion(isDarkMode)
                WidgetTypes.CATALOG_COMPARISON.type -> it.mapToComparison()
                else -> {
                    BlankUiModel()
                }
            }.applyGlobalProperies(remoteModel, it)
        }.orEmpty()
    }

    fun mapToCatalogDetailUiModel(
        remoteModel: CatalogResponseData.CatalogGetDetailModular
    ): CatalogDetailUiModel {
        val widgets = mapToWidgetVisitables(remoteModel)
        return CatalogDetailUiModel(
            widgets = widgets,
            navigationProperties = mapToNavigationProperties(remoteModel, widgets),
            priceCtaProperties = mapToPriceCtaProperties(remoteModel),
            remoteModel.basicInfo.productSortingStatus.orZero(),
            catalogUrl = remoteModel.basicInfo.url.orEmpty()
        )
    }

    private fun mapToPriceCtaProperties(remoteModel: CatalogResponseData.CatalogGetDetailModular): PriceCtaProperties {
        val bgColor = remoteModel.globalStyle?.secondaryColor
        val textColorRes = if (remoteModel.globalStyle?.darkMode == true) {
            unifycomponentsR.color.Unify_Static_White
        } else {
            unifycomponentsR.color.Unify_Static_Black
        }
        return remoteModel.layouts?.firstOrNull {
            it.type == WidgetTypes.CATALOG_CTA_PRICE.type
        }?.data?.run {
            val marketPrice = priceCta.marketPrice.firstOrNull()
            val minFmt = marketPrice?.minFmt.orEmpty()
            val maxFmt = marketPrice?.maxFmt.orEmpty()
            val displayedPrice = if (minFmt == maxFmt) {
                maxFmt
            } else {
                listOf(minFmt, maxFmt).joinToString(" - ")
            }

            PriceCtaProperties(
                catalogId = remoteModel.basicInfo.id,
                price = displayedPrice,
                productName = priceCta.name,
                bgColor = "#$bgColor".stringHexColorParseToInt(),
                MethodChecker.getColor(context, textColorRes)
            )
        } ?: PriceCtaProperties()
    }

    private fun mapToNavigationProperties(
        remoteModel: CatalogResponseData.CatalogGetDetailModular,
        widgets: List<Visitable<*>>
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

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToHeroBanner(
        darkMode: Boolean
    ) =
        HeroBannerUiModel(
            isPremium = data?.style?.isPremium.orFalse(),
            brandTitle = data?.hero?.name.orEmpty(),
            brandImageUrls = data?.hero?.heroSlide?.map { heroSlide ->
                heroSlide.imageUrl
            }.orEmpty(),
            brandDescriptions = data?.hero?.heroSlide?.map { heroSlide ->
                heroSlide.subtitle
            }.orEmpty(),
            brandIconUrl = data?.hero?.brandLogoUrl.orEmpty(),
            widgetTextColor = colorMapping(darkMode, DARK_COLOR_BANNER, LIGHT_COLOR_BANNER)

        )

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToTopFeature(
        remoteModel: CatalogResponseData.CatalogGetDetailModular
    ): TopFeaturesUiModel {
        val isDarkMode = remoteModel.globalStyle?.darkMode.orFalse()
        return TopFeaturesUiModel(
            items = data?.topFeature?.map {
                TopFeaturesUiModel.ItemTopFeatureUiModel(
                    id = it.desc,
                    icon = it.iconUrl,
                    name = it.desc,
                    backgroundColor = colorMapping(isDarkMode, DARK_COLOR, LIGHT_COLOR, 20),
                    textColor = getTextColor(isDarkMode)
                )
            }.orEmpty()
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToStickyNavigation(
        remoteModel: CatalogResponseData.CatalogGetDetailModular
    ): StickyNavigationUiModel {
        return StickyNavigationUiModel(
            content = data?.navigation?.map { nav ->
                val eligibleName = nav.eligibleNames.filter { eligble ->
                    val found = remoteModel.layouts?.indexOfFirst {
                        it.name == eligble && !it.data?.style?.isHidden.orFalse()
                    }
                    found != -1
                }.firstOrNull().orEmpty()
                StickyNavigationUiModel.StickyNavigationItemData(
                    nav.title,
                    eligibleName,
                    nav.eligibleNames.joinToString(",")
                )
            }.orEmpty()
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToTrustMaker(
        isDarkMode: Boolean
    ): TrustMakerUiModel {
        val textColor = getTextColor(isDarkMode)
        return TrustMakerUiModel(
            items = data?.trustmaker.orEmpty().map {
                TrustMakerUiModel.ItemTrustMakerUiModel(
                    id = "",
                    icon = it.imageUrl,
                    title = it.title,
                    subTitle = it.subtitle,
                    textColorTitle = textColor,
                    textColorSubTitle = textColor
                )
            }
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToCharacteristic(
        isDarkMode: Boolean
    ): CharacteristicUiModel {
        val textColor = colorMapping(isDarkMode, DARK_COLOR_01, LIGHT_COLOR_01)
        return CharacteristicUiModel(
            items = data?.characteristic.orEmpty().map {
                CharacteristicUiModel.ItemCharacteristicUiModel(
                    id = "",
                    icon = it.iconUrl,
                    title = it.desc,
                    textColorTitle = textColor
                )
            }
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToSliderImageText(
        isDarkMode: Boolean
    ): SliderImageTextUiModel {
        return SliderImageTextUiModel(
            items = data?.imageSlider.orEmpty().map {
                SliderImageTextUiModel.ItemSliderImageText(
                    image = it.imageUrl,
                    textHighlight = it.subtitle,
                    textTitle = it.title,
                    textDescription = it.desc,
                    textHighlightColor = colorMapping(isDarkMode, DARK_COLOR_01, LIGHT_COLOR_01),
                    textTitleColor = colorMapping(
                        isDarkMode,
                        DARK_COLOR_IMAGE_TEXT,
                        LIGHT_COLOR_IMAGE_TEXT
                    ),
                    textDescriptionColor = colorMapping(isDarkMode, DARK_COLOR_01, LIGHT_COLOR_01)
                )
            }
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToAccordion(
        isDarkMode: Boolean
    ): AccordionInformationUiModel {
        return AccordionInformationUiModel(
            titleWidget = data?.section?.title.orEmpty(),
            widgetTextColor = colorMapping(
                isDarkMode,
                DARK_COLOR_ACCORDION_01,
                LIGHT_COLOR_ACCORDION_01
            ),
            contents = data?.accordion.orEmpty().map {
                AccordionInformationUiModel.ItemAccordionInformationUiModel(
                    title = it.title,
                    description = it.desc,
                    arrowColor = colorMapping(
                        isDarkMode,
                        DARK_COLOR_ACCORDION_ARROW,
                        LIGHT_COLOR_ACCORDION_ARROW
                    ),
                    textTitleColor = colorMapping(
                        isDarkMode,
                        DARK_COLOR_ACCORDION_01,
                        LIGHT_COLOR_ACCORDION_01
                    ),
                    textDescriptionColor = colorMapping(
                        isDarkMode,
                        DARK_COLOR_ACCORDION_02,
                        LIGHT_COLOR_ACCORDION_02
                    )
                )
            }
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToTextDescription(
        isDarkMode: Boolean
    ): TextDescriptionUiModel {
        return TextDescriptionUiModel(
            item = TextDescriptionUiModel.ItemTextDescriptionUiModel(
                highlight = data?.text?.subtitle.orEmpty(),
                title = data?.text?.title.orEmpty(),
                description = data?.text?.desc.orEmpty()
            ),
            isDarkMode = isDarkMode
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToBannerImage(
        darkMode: Boolean
    ): BannerCatalogUiModel {
        return BannerCatalogUiModel(
            darkMode = darkMode,
            imageUrl = data?.singleBanner?.imageUrl.orEmpty(),
            ratio = BannerCatalogUiModel.Ratio.values().firstOrNull {
                it.ratioName == data?.style?.bannerRatio.orEmpty()
            } ?: BannerCatalogUiModel.Ratio.TWO_BY_ONE
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToDoubleBannerImage(
        darkMode: Boolean
    ): DoubleBannerCatalogUiModel {
        return DoubleBannerCatalogUiModel(
            darkMode = darkMode,
            imageUrls = data?.doubleBanner?.map { it.imageUrl }.orEmpty()
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToExpertReview(
        isDarkMode: Boolean
    ): ExpertReviewUiModel {
        return ExpertReviewUiModel(
            content = data?.expertReview.orEmpty().map {
                ExpertReviewUiModel.ItemExpertReviewUiModel(
                    imageReviewer = it.imageUrl,
                    reviewText = it.review,
                    title = it.name,
                    subTitle = it.title,
                    videoLink = it.videoUrl,
                    textReviewColor = getTextColor(isDarkMode),
                    textTitleColor = getTextColor(isDarkMode),
                    textSubTitleColor = getTextColor(isDarkMode),
                    backgroundColor = colorMapping(
                        isDarkMode,
                        catalogcommonR.drawable.bg_rounded_border_dark,
                        catalogcommonR.drawable.bg_rounded_border_light
                    ),
                    styleIconPlay = ExpertReviewUiModel.StyleIconPlay(
                        iconColor = colorMapping(
                            isDarkMode,
                            unifyprinciplesR.color.Unify_Static_White,
                            unifyprinciplesR.color.Unify_Static_Black
                        ),
                        background = colorMapping(
                            isDarkMode,
                            catalogcommonR.drawable.bg_circle_border_dark,
                            catalogcommonR.drawable.bg_circle_border_light
                        )
                    )
                )
            }
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToSupportFeature(
        remoteModel: CatalogResponseData.CatalogGetDetailModular
    ): SupportFeaturesUiModel {
        val isDarkMode = remoteModel.globalStyle?.darkMode.orFalse()
        return SupportFeaturesUiModel(
            titleSection = data?.section?.title.orEmpty(),
            widgetTextColor = colorMapping(
                isDarkMode,
                DARK_COLOR_SUPPORT_FEATURE_01,
                LIGHT_COLOR_SUPPORT_FEATURE_01
            ),
            items = data?.supportFeature?.map {
                SupportFeaturesUiModel.ItemSupportFeaturesUiModel(
                    id = it.desc,
                    icon = it.iconUrl,
                    title = it.title,
                    description = it.desc,
                    backgroundColor = colorMapping(isDarkMode, DARK_COLOR, LIGHT_COLOR, 20),
                    descColor = colorMapping(isDarkMode, DARK_COLOR_01, LIGHT_COLOR_01),
                    titleColor = colorMapping(
                        isDarkMode,
                        DARK_COLOR_SUPPORT_FEATURE_01,
                        LIGHT_COLOR_SUPPORT_FEATURE_01
                    )
                )
            }.orEmpty()
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToComparison(): BaseCatalogUiModel {
        var isFirstData = true
        return ComparisonUiModel(content = data?.comparison.orEmpty().take(2).map {
            val comparisonSpecs = mutableListOf<ComparisonUiModel.ComparisonSpec>()
            it.fullSpec.forEach { spec ->
                comparisonSpecs.add(ComparisonUiModel.ComparisonSpec(
                    isSpecCategoryTitle = true,
                    specCategoryTitle = if (isFirstData) spec.name else ""
                ))
                spec.row.forEach { rowItem ->
                    val insertedItem = ComparisonUiModel.ComparisonSpec(
                        isSpecCategoryTitle = false,
                        specTitle = if (isFirstData) rowItem.key else "",
                        specValue = rowItem.value,
                    )
                    comparisonSpecs.add(insertedItem)
                }
            }
            isFirstData = false
            ComparisonUiModel.ComparisonContent(
                imageUrl = it.catalogImage.firstOrNull{ it.isPrimary }?.imageUrl.orEmpty(),
                productTitle = it.name,
                price = it.marketPrice.firstOrNull()?.let { marketPrice ->
                    marketPrice.minFmt + " - " + marketPrice.maxFmt
                }.orEmpty() ,
                comparisonSpecs = comparisonSpecs
            )
        })
    }

    private fun getTextColor(darkMode: Boolean): Int {
        val textColorRes = if (darkMode) {
            unifycomponentsR.color.Unify_Static_White
        } else {
            unifycomponentsR.color.Unify_Static_Black
        }
        return MethodChecker.getColor(context, textColorRes)
    }

    fun isUsingAboveV4Layout(version: Int): Boolean {
        return version >= LAYOUT_VERSION_4_VALUE
    }
}
