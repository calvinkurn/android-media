package com.tokopedia.catalog.ui.mapper

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog.ui.model.CatalogDetailUiModel
import com.tokopedia.catalog.ui.model.NavigationProperties
import com.tokopedia.catalog.ui.model.PriceCtaProperties
import com.tokopedia.catalog.ui.model.PriceCtaSellerOfferingProperties
import com.tokopedia.catalog.ui.model.ProductListConfig
import com.tokopedia.catalog.ui.model.ShareProperties
import com.tokopedia.catalog.ui.model.WidgetTypes
import com.tokopedia.catalogcommon.R as catalogcommonR
import com.tokopedia.catalog.util.ColorConst.COLOR_DEEP_AZURE
import com.tokopedia.catalog.util.ColorConst.COLOR_OCEAN_BLUE
import com.tokopedia.catalog.util.ColorConst.COLOR_WHITE
import com.tokopedia.catalogcommon.uimodel.AccordionInformationUiModel
import com.tokopedia.catalogcommon.uimodel.BannerCatalogUiModel
import com.tokopedia.catalogcommon.uimodel.BaseCatalogUiModel
import com.tokopedia.catalogcommon.uimodel.BlankUiModel
import com.tokopedia.catalogcommon.uimodel.BuyerReviewUiModel
import com.tokopedia.catalogcommon.uimodel.CharacteristicUiModel
import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel
import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel.Companion.CELL_TITLE_ON_3_COLUMN_TYPE
import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel.Companion.COLUMN_TITLE_ON_3_COLUMN_TYPE
import com.tokopedia.catalogcommon.uimodel.ColumnedInfoUiModel.Companion.VALUE_ON_2_COLUMN_TYPE
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.catalogcommon.uimodel.DoubleBannerCatalogUiModel
import com.tokopedia.catalogcommon.uimodel.ExpertReviewUiModel
import com.tokopedia.catalogcommon.uimodel.HeroBannerUiModel
import com.tokopedia.catalogcommon.uimodel.PriceCtaSellerOfferingUiModel
import com.tokopedia.catalogcommon.uimodel.SellerOfferingUiModel
import com.tokopedia.catalogcommon.uimodel.SliderImageTextUiModel
import com.tokopedia.catalogcommon.uimodel.StickyNavigationUiModel
import com.tokopedia.catalogcommon.uimodel.SupportFeaturesUiModel
import com.tokopedia.catalogcommon.uimodel.TextDescriptionUiModel
import com.tokopedia.catalogcommon.uimodel.TopFeaturesUiModel
import com.tokopedia.catalogcommon.uimodel.TrustMakerUiModel
import com.tokopedia.catalogcommon.uimodel.VideoUiModel
import com.tokopedia.catalogcommon.util.colorMapping
import com.tokopedia.catalogcommon.util.getColorDarkMode
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.relativeDate
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isEven
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.isOdd
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.oldcatalog.model.raw.CatalogResponseData
import com.tokopedia.oldcatalog.model.raw.LayoutData
import com.tokopedia.utils.date.DateUtil
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import com.tokopedia.catalog.R as catalogR
import com.tokopedia.unifycomponents.R as unifycomponentsR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CatalogDetailUiMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val LAYOUT_VERSION_4_VALUE = 4
        private const val TOP_COMPARISON_SPEC_COUNT = 5
        private const val COLUMN_INFO_SPEC_COUNT = 5
        private const val COLUMN_INFO_2_COLUMN_DATA_LIMIT = 12
        private const val COLUMN_INFO_3_COLUMN_DATA_LIMIT = 9
        private const val COLUMN_INFO_3_COLUMN_ROW_LIMIT = 3
        private const val INVALID_CATALOG_ID = "0"
        private const val BACKGROUND_ALPHA = 20
        private const val USER_STATS_KEY_TOTAL_COMPLETE_REVIEW = "total-complete-review"
        private const val USER_STATS_KEY_TOTAL_LIKES = "total-likes"
        private const val SERVICE_LANG = "en"
        private const val SERVICE_COUNTRY = "US"
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
                WidgetTypes.CATALOG_COMPARISON.type -> it.mapToComparison(isDarkMode)
                WidgetTypes.CATALOG_VIDEO.type -> it.mapToVideo(isDarkMode)
                WidgetTypes.CATALOG_REVIEW_BUYER.type -> it.mapToBuyerReview(remoteModel.basicInfo)
                WidgetTypes.CATALOG_COLUMN_INFO.type -> it.mapToColumnInfo(isDarkMode)
                WidgetTypes.CATALOG_CTA_PRICE.type -> it.mapToCtaPrice(isDarkMode)
                WidgetTypes.CATALOG_CARD_TOP_SELLER.type -> it.mapToCardTopSeller(
                    remoteModel.basicInfo.name.orEmpty(),
                    "${remoteModel.globalStyle?.bgColor}"
                )

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
        val headerColorProductList = if (remoteModel.productListCfg.headerColor.isNullOrEmpty()) {
            COLOR_DEEP_AZURE
        } else {
            remoteModel.productListCfg.headerColor
        }
        return CatalogDetailUiModel(
            widgets = widgets,
            departmentId = remoteModel.basicInfo.departmentID.orEmpty(),
            brand = remoteModel.basicInfo.brand.orEmpty(),
            navigationProperties = mapToNavigationProperties(remoteModel, widgets),
            priceCtaProperties = mapToPriceCtaProperties(remoteModel),
            priceCtaSellerOfferingProperties = mapToPriceCtaSellerOfferingProperties(remoteModel),
            productSortingStatus = remoteModel.basicInfo.productSortingStatus.orZero(),
            catalogUrl = remoteModel.basicInfo.url.orEmpty(),
            shareProperties = mapToShareProperties(remoteModel, widgets),
            productListConfig = ProductListConfig(
                remoteModel.productListCfg.limit ?: "20",
                headerColorProductList,
                remoteModel.basicInfo.marketPrice?.getOrNull(Int.ZERO)?.min.orZero(),
                remoteModel.basicInfo.marketPrice?.getOrNull(Int.ZERO)?.max.orZero()
            )
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToBuyerReview(
        basicInfo: CatalogResponseData.CatalogGetDetailModular.BasicInfo
    ): BaseCatalogUiModel {
        val maxDisplay = data?.style?.maxDisplay.orZero()
        val items = data?.buyerReviewList.orEmpty().take(maxDisplay).map { buyerReview ->
            val totalCompleteReview =
                buyerReview.userStats.firstOrNull { it.key == USER_STATS_KEY_TOTAL_COMPLETE_REVIEW }?.count.orZero()
            val totalHelpedPeople =
                buyerReview.userStats.firstOrNull { it.key == USER_STATS_KEY_TOTAL_LIKES }?.count.orZero()
            val reviewDate = try {
                val simpleDateFormat = SimpleDateFormat(
                    DateUtil.DEFAULT_VIEW_FORMAT,
                    Locale(
                        SERVICE_LANG,
                        SERVICE_COUNTRY
                    )
                )
                val reviewDate = simpleDateFormat.parse(buyerReview.reviewDate)
                reviewDate?.relativeDate.orEmpty()
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
                buyerReview.reviewDate
            }
            BuyerReviewUiModel.ItemBuyerReviewUiModel(
                shopIcon = buyerReview.shopBadge,
                shopName = buyerReview.shopName,
                reviewerName = buyerReview.reviewerName,
                avatar = buyerReview.reviewerProfilePicture,
                reviewerStatus = buyerReview.reviewerStamp,
                totalCompleteReview = totalCompleteReview,
                totalHelpedPeople = totalHelpedPeople,
                description = buyerReview.reviewText,
                datetime = reviewDate,
                rating = buyerReview.rating.toFloat(),
                variantName = buyerReview.productVariantName,
                images = buyerReview.imageAttachments.map { attachment ->
                    BuyerReviewUiModel.ImgReview(
                        id = attachment.attachmentId,
                        imgUrl = attachment.thumbnailUrl,
                        fullsizeImgUrl = attachment.fullsizeUrl
                    )
                },
                catalogName = basicInfo.name.orEmpty(),
                reviewId = buyerReview.reviewId.toString()
            )
        }

        return if (items.isEmpty()) {
            BlankUiModel()
        } else {
            BuyerReviewUiModel(
                title = data?.section?.title.orEmpty(),
                items = items
            )
        }
    }

    private fun mapToPriceCtaProperties(
        remoteModel: CatalogResponseData.CatalogGetDetailModular
    ): PriceCtaProperties {
        val bgColor = remoteModel.globalStyle?.secondaryColor
        val textColorRes = if (remoteModel.globalStyle?.darkMode == true) {
            unifycomponentsR.color.Unify_Static_White
        } else {
            unifycomponentsR.color.Unify_Static_Black
        }
        return remoteModel.layouts?.firstOrNull {
            it.type == WidgetTypes.CATALOG_CTA_PRICE.type
        }?.data?.run {
            if (style.isHidden || !style.isSticky) return@run PriceCtaProperties()
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
                departmentId = remoteModel.basicInfo.departmentID.orEmpty(),
                brand = remoteModel.basicInfo.brand.orEmpty(),
                price = displayedPrice,
                productName = priceCta.name,
                bgColor = "#$bgColor".stringHexColorParseToInt(),
                MethodChecker.getColor(context, textColorRes),
                isVisible = true
            )
        } ?: PriceCtaProperties()
    }

    private fun mapToPriceCtaSellerOfferingProperties(
        remoteModel: CatalogResponseData.CatalogGetDetailModular
    ): PriceCtaSellerOfferingProperties {
        val bgColor = remoteModel.globalStyle?.secondaryColor
        return remoteModel.layouts?.firstOrNull {
            it.type == WidgetTypes.CATALOG_CTA_PRICE_TOP_SELLER.type
        }?.data?.run {
            if (style.isHidden || !style.isSticky) return@run PriceCtaSellerOfferingProperties()
            val colorBorderButton = if (remoteModel.globalStyle?.darkMode.orFalse()) {
                catalogR.color.catalog_dms_light_color
            } else {
                catalogR.color.catalog_dms_green
            }
            val textColorPrice = if (remoteModel.globalStyle?.darkMode.orFalse()) {
                catalogR.color.catalog_dms_light_color
            } else {
                catalogR.color.catalog_dms_dark_color
            }
            PriceCtaSellerOfferingProperties(
                productId = topSeller.productID,
                warehouseId = topSeller.warehouseID,
                shopId = topSeller.shop.id,
                shopName = topSeller.shop.name,
                price = topSeller.price.text,
                slashPrice = topSeller.price.original,
                shopRating = topSeller.credibility.rating,
                sold = topSeller.credibility.sold,
                badge = topSeller.shop.badge,
                bgColor = "#$bgColor".stringHexColorParseToInt(),
                isDarkTheme = remoteModel.globalStyle?.darkMode == true,
                isVisible = true,
                isVariant = topSeller.isVariant,
                colorBorderButton = colorBorderButton,
                textColorPrice = textColorPrice
            )
        } ?: PriceCtaSellerOfferingProperties()
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

    private fun mapToShareProperties(
        remoteModel: CatalogResponseData.CatalogGetDetailModular,
        widgets: List<Visitable<*>>
    ): ShareProperties {
        val heroImage = widgets.firstOrNull { it is HeroBannerUiModel } as? HeroBannerUiModel
        return ShareProperties(
            catalogId = remoteModel.basicInfo.id,
            title = remoteModel.basicInfo.name.orEmpty(),
            images = heroImage?.brandImageUrls.orEmpty(),
            catalogUrl = remoteModel.basicInfo.mobileURL.orEmpty()
        )
    }

    private fun BaseCatalogUiModel.applyGlobalProperies(
        remoteModel: CatalogResponseData.CatalogGetDetailModular,
        layout: CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout
    ): BaseCatalogUiModel {
        val isDarkMode = remoteModel.globalStyle?.darkMode.orFalse()
        val bgColor = remoteModel.globalStyle?.bgColor
        return apply {
            idWidget = layout.type + "_" + layout.name + "_" + UUID.randomUUID()
            widgetType = layout.type
            widgetName = layout.name
            widgetBackgroundColor = "#$bgColor".stringHexColorParseToInt()
            widgetTextColor = getTextColor(isDarkMode)
            darkMode = isDarkMode
        }
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToHeroBanner(
        darkMode: Boolean
    ) = HeroBannerUiModel(
        isPremium = data?.style?.isPremium.orFalse(),
        brandTitle = data?.hero?.name.orEmpty(),
        brandImageUrls = data?.hero?.heroSlide?.map { heroSlide ->
            heroSlide.imageUrl
        }.orEmpty(),
        brandDescriptions = data?.hero?.heroSlide?.map { heroSlide ->
            heroSlide.subtitle
        }.orEmpty(),
        brandIconUrl = data?.hero?.brandLogoUrl.orEmpty(),
        widgetTextColor = getColorDarkMode(
            context,
            darkMode,
            catalogR.color.catalog_dms_dark_color_banner,
            catalogR.color.catalog_dms_light_color_banner
        )
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
                    backgroundColor = getColorDarkMode(
                        context,
                        isDarkMode,
                        catalogR.color.catalog_dms_dark_color,
                        catalogR.color.catalog_dms_light_color,
                        BACKGROUND_ALPHA
                    ),
                    textColor = getTextColor(isDarkMode)
                )
            }.orEmpty()
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToStickyNavigation(
        remoteModel: CatalogResponseData.CatalogGetDetailModular
    ): StickyNavigationUiModel {
        val isDarkMode = remoteModel.globalStyle?.darkMode.orFalse()
        val textColor = getTextColorNav(isDarkMode)
        return StickyNavigationUiModel(
            content = data?.navigation?.map { nav ->
                val eligibleName = nav.eligibleNames.firstOrNull { eligble ->
                    val found = remoteModel.layouts?.indexOfFirst {
                        it.name == eligble && !it.data?.style?.isHidden.orFalse()
                    }
                    !found.isLessThanZero()
                }.orEmpty()
                StickyNavigationUiModel.StickyNavigationItemData(
                    nav.title,
                    eligibleName,
                    nav.eligibleNames.joinToString(","),
                    textColor
                )
            }.orEmpty()
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToTrustMaker(
        isDarkMode: Boolean
    ): TrustMakerUiModel {
        val textColorSubtitle = getTextColorTrustmaker(isDarkMode)
        val textColor = getTextColor(isDarkMode)
        return TrustMakerUiModel(
            items = data?.trustmaker.orEmpty().map {
                TrustMakerUiModel.ItemTrustMakerUiModel(
                    id = "",
                    icon = it.imageUrl,
                    title = it.title,
                    subTitle = it.subtitle,
                    textColorTitle = textColor,
                    textColorSubTitle = textColorSubtitle
                )
            }
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToCharacteristic(
        isDarkMode: Boolean
    ): CharacteristicUiModel {
        val textColor = getColorDarkMode(
            context,
            isDarkMode,
            catalogR.color.catalog_dms_dark_color_text_description,
            catalogR.color.catalog_dms_light_color_text_description
        )
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
                    textHighlightColor = getColorDarkMode(
                        context,
                        isDarkMode,
                        catalogR.color.catalog_dms_dark_color_banner,
                        catalogR.color.catalog_dms_light_color_banner
                    ),
                    textTitleColor = getColorDarkMode(
                        context,
                        isDarkMode,
                        catalogR.color.catalog_dms_dark_color_image_text,
                        catalogR.color.catalog_dms_light_color_image_text
                    ),
                    textDescriptionColor = getColorDarkMode(
                        context,
                        isDarkMode,
                        catalogR.color.catalog_dms_dark_color_banner,
                        catalogR.color.catalog_dms_light_color_banner
                    )
                )
            }
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToAccordion(
        isDarkMode: Boolean
    ): AccordionInformationUiModel {
        return AccordionInformationUiModel(
            titleWidget = data?.section?.title.orEmpty(),
            widgetTextColor = getColorDarkMode(
                context,
                isDarkMode,
                catalogR.color.catalog_dms_dark_color_accordion_title,
                catalogR.color.catalog_dms_light_color_accordion_title
            ),
            contents = data?.accordion.orEmpty().map {
                AccordionInformationUiModel.ItemAccordionInformationUiModel(
                    title = it.title,
                    description = it.desc,
                    arrowColor = getColorDarkMode(
                        context,
                        isDarkMode,
                        catalogR.color.catalog_dms_dark_color_accordion_arrow,
                        catalogR.color.catalog_dms_light_color_accordion_arrow
                    ),
                    textTitleColor = getColorDarkMode(
                        context,
                        isDarkMode,
                        catalogR.color.catalog_dms_dark_color_accordion_title,
                        catalogR.color.catalog_dms_light_color_accordion_title
                    ),
                    textDescriptionColor = getColorDarkMode(
                        context,
                        isDarkMode,
                        catalogR.color.catalog_dms_dark_color_accordion_description,
                        catalogR.color.catalog_dms_light_color_accordion_description
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
                    textSubTitleColor = getTextColorTrustmaker(isDarkMode),
                    backgroundColor = if (isDarkMode) COLOR_OCEAN_BLUE else COLOR_WHITE,
                    styleIconPlay = ExpertReviewUiModel.StyleIconPlay(
                        iconColor = colorMapping(
                            isDarkMode,
                            unifyprinciplesR.color.Unify_Static_White,
                            unifyprinciplesR.color.Unify_Static_Black
                        ),
                        background = if (isDarkMode) catalogcommonR.color.dms_static_color_ocean_blue else unifyprinciplesR.color.Unify_Static_White
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
            widgetTextColor = getColorDarkMode(
                context,
                isDarkMode,
                catalogR.color.catalog_dms_dark_color_support_feature,
                catalogR.color.catalog_dms_light_color_support_feature
            ),
            items = data?.supportFeature?.map {
                SupportFeaturesUiModel.ItemSupportFeaturesUiModel(
                    id = it.desc,
                    icon = it.iconUrl,
                    title = it.title,
                    description = it.desc,
                    backgroundColor = getColorDarkMode(
                        context,
                        isDarkMode,
                        catalogR.color.catalog_dms_dark_color,
                        catalogR.color.catalog_dms_light_color,
                        BACKGROUND_ALPHA
                    ),
                    descColor = getColorDarkMode(
                        context,
                        isDarkMode,
                        catalogR.color.catalog_dms_dark_color_text_description,
                        catalogR.color.catalog_dms_light_color_text_description
                    ),
                    titleColor = getColorDarkMode(
                        context,
                        isDarkMode,
                        catalogR.color.catalog_dms_dark_color_support_feature,
                        catalogR.color.catalog_dms_light_color_support_feature
                    )
                )
            }.orEmpty()
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToComparison(
        isDarkMode: Boolean
    ): BaseCatalogUiModel {
        var isFirstData = true
        val displayedComparisons = data?.comparison.orEmpty().filter { it.id != INVALID_CATALOG_ID }
        return if (displayedComparisons.size <= Int.ONE) {
            BlankUiModel()
        } else {
            ComparisonUiModel(
                content = displayedComparisons.map {
                    val comparisonSpecs = mutableListOf<ComparisonUiModel.ComparisonSpec>()
                    it.fullSpec.forEach { spec ->
                        comparisonSpecs.add(
                            ComparisonUiModel.ComparisonSpec(
                                isSpecCategoryTitle = true,
                                specCategoryTitle = if (isFirstData) spec.name else "",
                                isDarkMode = isDarkMode
                            )
                        )
                        spec.row.forEach { rowItem ->
                            val insertedItem = ComparisonUiModel.ComparisonSpec(
                                isSpecCategoryTitle = false,
                                specTitle = if (isFirstData) rowItem.key else "",
                                specValue = rowItem.value.ifEmpty { "-" },
                                specTextTitleColor = getTextColor(
                                    isDarkMode,
                                    lightModeColor = catalogR.color.catalog_dms_light_color_text_description,
                                    darkModeColor = catalogR.color.catalog_dms_dark_color_text_description
                                ),
                                isDarkMode = isDarkMode
                            )
                            comparisonSpecs.add(insertedItem)
                        }
                    }
                    isFirstData = false
                    ComparisonUiModel.ComparisonContent(
                        id = it.id,
                        imageUrl = it.catalogImage.firstOrNull { image -> image.isPrimary }?.imageUrl.orEmpty(),
                        productTitle = it.name,
                        price = it.marketPrice.firstOrNull()?.let { marketPrice ->
                            marketPrice.minFmt + " - " + marketPrice.maxFmt
                        }.orEmpty(),
                        comparisonSpecs = comparisonSpecs,
                        topComparisonSpecs = comparisonSpecs.filter { comparisonSpec -> !comparisonSpec.isSpecCategoryTitle }
                            .take(TOP_COMPARISON_SPEC_COUNT).map {
                                it.copy(
                                    isSpecTextTitleBold = true,
                                    specTextTitleColor = getTextColor(isDarkMode),
                                    specTextColor = getTextColor(isDarkMode)
                                )
                            }

                    )
                }.toMutableList()
            )
        }
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToVideo(
        darkMode: Boolean
    ): VideoUiModel {
        return VideoUiModel(
            content = data?.video.orEmpty().map {
                VideoUiModel.ItemVideoUiModel(
                    thumbnailUrl = it.thumbnail,
                    title = it.title,
                    author = it.author,
                    videoLink = it.url,
                    textTitleColor = getTextColor(darkMode),
                    textSubTitleColor = getTextColor(darkMode),
                    backgroundColor = catalogcommonR.drawable.bg_rounded_border_light
                )
            }
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToColumnInfo(
        darkMode: Boolean
    ): BaseCatalogUiModel {
        val columnType = data?.style?.columnType.orEmpty()
        val infoColumn = data?.infoColumn.orEmpty()
        val flattenDataRows = data?.infoColumn.orEmpty().flatMap { it.row }

        // Map column info based on their type
        return if (infoColumn.isEmpty()) {
            BlankUiModel()
        } else if (columnType == COLUMN_TITLE_ON_3_COLUMN_TYPE || columnType == CELL_TITLE_ON_3_COLUMN_TYPE) {
            mapTo3ColumnInfo(data, columnType, flattenDataRows, darkMode)
        } else if (columnType == VALUE_ON_2_COLUMN_TYPE) {
            mapTo2ColumnInfo(data, columnType, flattenDataRows, darkMode)
        } else {
            mapTo1ColumnInfo(data, columnType, flattenDataRows, darkMode)
        }
    }

    private fun mapTo1ColumnInfo(
        data: LayoutData?,
        columnType: String,
        flattenDataRows: List<LayoutData.InfoColumn.Row>,
        darkMode: Boolean
    ): ColumnedInfoUiModel {
        return ColumnedInfoUiModel(
            sectionTitle = data?.section?.title.orEmpty(),
            hasMoreData = flattenDataRows.size > COLUMN_INFO_SPEC_COUNT,
            columnType = columnType,
            widgetContent = ColumnedInfoUiModel.ColumnData(
                rowData = flattenDataRows.take(COLUMN_INFO_SPEC_COUNT).map {
                    Pair(it.key, it.value)
                },
                rowColor = getColumnInfoTextColor(darkMode)
            ),
            fullContent = data?.infoColumn.orEmpty().map {
                ColumnedInfoUiModel.ColumnData(
                    title = it.name,
                    rowData = it.row.map { row ->
                        Pair(row.key, row.value)
                    },
                    rowColor = getColumnInfoBottomSheetTextColor()
                )
            }
        )
    }

    private fun mapTo2ColumnInfo(
        data: LayoutData?,
        columnType: String,
        flattenDataRows: List<LayoutData.InfoColumn.Row>,
        darkMode: Boolean
    ): ColumnedInfoUiModel {
        return ColumnedInfoUiModel(
            sectionTitle = data?.section?.title.orEmpty(),
            hasMoreData = flattenDataRows.size > COLUMN_INFO_2_COLUMN_DATA_LIMIT,
            columnType = columnType,
            widgetContentThreeColumn = listOf(
                ColumnedInfoUiModel.ColumnData(
                    rowData = flattenDataRows.take(
                        COLUMN_INFO_2_COLUMN_DATA_LIMIT
                    ).filterIndexed { index, _ ->
                        index.isEven()
                    }.map { row ->
                        Pair(row.key, row.value)
                    },
                    rowColor = getColumnInfoTextColor(darkMode)
                ),
                ColumnedInfoUiModel.ColumnData(
                    rowData = flattenDataRows.take(
                        COLUMN_INFO_2_COLUMN_DATA_LIMIT
                    ).filterIndexed { index, _ ->
                        index.isOdd()
                    }.map { row ->
                        Pair(row.key, row.value)
                    },
                    rowColor = getColumnInfoTextColor(darkMode)
                )
            ),
            fullContent = data?.infoColumn.orEmpty().map {
                ColumnedInfoUiModel.ColumnData(
                    title = it.name,
                    rowData = it.row.map { row ->
                        Pair(row.value, "")
                    },
                    rowColor = getColumnInfoBottomSheetValueOnlyTextColor()
                )
            }
        )
    }

    private fun mapTo3ColumnInfo(
        data: LayoutData?,
        columnType: String,
        flattenDataRows: List<LayoutData.InfoColumn.Row>,
        darkMode: Boolean
    ): ColumnedInfoUiModel {
        return ColumnedInfoUiModel(
            sectionTitle = data?.section?.title.orEmpty(),
            hasMoreData = flattenDataRows.size > COLUMN_INFO_3_COLUMN_DATA_LIMIT,
            columnType = columnType,
            widgetContentThreeColumn = data?.infoColumn.orEmpty().map {
                ColumnedInfoUiModel.ColumnData(
                    title = it.name,
                    rowData = it.row.take(COLUMN_INFO_3_COLUMN_ROW_LIMIT).map { row ->
                        Pair(it.name, row.value)
                    },
                    rowColor = getColumnInfoTextColor(darkMode)
                )
            },
            fullContent = data?.infoColumn.orEmpty().map {
                ColumnedInfoUiModel.ColumnData(
                    title = it.name,
                    rowData = it.row.map { row ->
                        if (columnType == COLUMN_TITLE_ON_3_COLUMN_TYPE) {
                            Pair(row.value, "")
                        } else {
                            Pair(row.key, row.value)
                        }
                    },
                    rowColor = if (columnType == COLUMN_TITLE_ON_3_COLUMN_TYPE) {
                        getColumnInfoBottomSheetValueOnlyTextColor()
                    } else {
                        getColumnInfoBottomSheetTextColor()
                    }
                )
            }
        )
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToCtaPrice(
        darkMode: Boolean
    ): BaseCatalogUiModel {
        return if (data?.style?.isSticky == false && !data.style.isHidden) {
            val minPrice = data.priceCta.marketPrice.firstOrNull()?.minFmt.orEmpty()
            val maxPrice = data.priceCta.marketPrice.firstOrNull()?.maxFmt.orEmpty()
            val color = if (darkMode) {
                catalogR.color.catalog_dms_dark_color_text_common
            } else {
                catalogR.color.catalog_dms_light_color_text_common
            }
            PriceCtaSellerOfferingUiModel(
                price = if (minPrice == maxPrice) maxPrice else "$minPrice - $maxPrice",
                textTitleColor = color,
                textPriceColor = color,
                iconColor = color,
                shopId = data.topSeller.shop.id,
                productId = data.topSeller.productID,
            )
        } else {
            BlankUiModel()
        }
    }

    private fun CatalogResponseData.CatalogGetDetailModular.BasicInfo.Layout.mapToCardTopSeller(
        catalogName: String,
        bgColor: String
    ): BaseCatalogUiModel {
        return data?.topSeller?.run {
            val bebasOngkirUrl =
                labelGroups.firstOrNull { it.position == "overlay_2" }?.url.orEmpty()
            val productBenefit =
                labelGroups.firstOrNull { it.position == "ri_product_benefit" }?.title.orEmpty()
            val productOffer =
                labelGroups.firstOrNull { it.position == "ri_product_offer" }?.title.orEmpty()
            SellerOfferingUiModel(
                shopId = shop.id,
                productId = productID,
                productImage = mediaUrl.thumbnailUrl,
                shopBadge = shop.badge,
                shopName = shop.name,
                stockBar = SellerOfferingUiModel.Stock(
                    stockRemaining = stock.soldPercentage,
                    stockAlertText = stock.wording,
                    isHidden = stock.isHidden
                ),
                productName = catalogName,
                productPrice = price.text,
                productSlashPrice = price.original,
                shopLocation = shop.city,
                chatResponseTime = shop.stats.chatEta,
                orderProcessTime = shop.stats.orderProcessEta,
                labelPromo = productBenefit,
                labelTotalDisc = productOffer,
                shopRating = credibility.rating,
                totalShopRating = credibility.ratingCount,
                totalSold = credibility.sold,
                freeOngkir = bebasOngkirUrl,
                estimationShipping = delivery.eta,
                isShopGuarantee = paymentOption.desc.isNotEmpty(),
                paymentOption = SellerOfferingUiModel.PaymentOption(
                    paymentOption.desc,
                    paymentOption.iconUrl
                ),
                additionalService = additionalService.name,
                cardColor = if (bgColor.isNotEmpty()) {
                    "#$bgColor"
                } else {
                    String.EMPTY
                }
            )
        } ?: SellerOfferingUiModel()
    }

    private fun getTextColor(darkMode: Boolean): Int {
        val textColorRes = if (darkMode) {
            catalogR.color.catalog_dms_dark_color_text_common
        } else {
            catalogR.color.catalog_dms_light_color_text_common
        }
        return MethodChecker.getColor(context, textColorRes)
    }

    private fun getTextColor(darkMode: Boolean, darkModeColor: Int, lightModeColor: Int): Int {
        val textColorRes = if (darkMode) {
            darkModeColor
        } else {
            lightModeColor
        }
        return MethodChecker.getColor(context, textColorRes)
    }

    private fun getTextColorTrustmaker(darkMode: Boolean): Int {
        val textColorRes = if (darkMode) {
            catalogR.color.catalog_dms_dark_color_text_common
        } else {
            catalogR.color.catalog_dms_light_color_text_common
        }
        return MethodChecker.getColor(context, textColorRes)
    }

    private fun getTextColorNav(darkMode: Boolean): Int {
        val textColorRes = if (darkMode) {
            unifycomponentsR.color.Unify_Static_White
        } else {
            unifycomponentsR.color.Unify_Static_Black
        }
        return MethodChecker.getColor(context, textColorRes)
    }

    private fun getColumnInfoTextColor(darkMode: Boolean) = Pair(
        MethodChecker.getColor(
            context,
            if (darkMode) {
                catalogR.color.catalog_dms_dark_color_text_description
            } else {
                catalogR.color.catalog_dms_light_color_text_description
            }
        ),
        MethodChecker.getColor(
            context,
            if (darkMode) {
                catalogR.color.catalog_dms_column_info_value_color_dark
            } else {
                catalogR.color.catalog_dms_column_info_value_color_light
            }
        )
    )

    private fun getColumnInfoBottomSheetTextColor() = Pair(
        MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN600),
        Int.ZERO
    )

    private fun getColumnInfoBottomSheetValueOnlyTextColor() = Pair(Int.ZERO, Int.ZERO)

    fun isUsingAboveV4Layout(version: Int): Boolean {
        return version >= LAYOUT_VERSION_4_VALUE
    }
}
