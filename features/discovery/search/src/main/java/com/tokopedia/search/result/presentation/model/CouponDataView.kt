package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel
import com.tokopedia.discovery_component.widgets.automatecoupon.DynamicColorText
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.domain.model.SearchCouponModel
import com.tokopedia.search.result.domain.model.SearchCouponModel.Companion.isValidCoupon
import com.tokopedia.search.result.domain.model.SearchRedeemCouponModel
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.usecase.RequestParams
import org.json.JSONObject

data class CouponDataView(
    val applink: String = "",
    val dimension90: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
    val actualKeyword: String = "",
    val valueId: String = "",
    val valueName: String = "",
    val carouselItem: InspirationCarouselDataView
) : Visitable<ProductListTypeFactory>,
    ImpressHolder(),
    SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption,
        keyword = actualKeyword,
        componentId = componentId,
        applink = applink,
        valueId = valueId,
        valueName = valueName,
        dimension90 = dimension90
    ) {
    var couponModel1: AutomateCouponModel? = null
        private set
    var couponWidgetData1: SearchCouponModel.CouponListWidget? = null
        private set
    var couponModel2: AutomateCouponModel? = null
        private set
    var couponWidgetData2: SearchCouponModel.CouponListWidget? = null
        private set
    private var searchCouponModel: SearchCouponModel? = null
    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun updateCouponModel(couponData: SearchCouponModel) {
        val validCouponWidgetList =
            couponData.promoCatalogGetCouponListWidget?.couponListWidget?.filter {
                it.widgetInfo?.ctaList?.get(0).isValidCoupon()
            }
        searchCouponModel = couponData.copy(
            promoCatalogGetCouponListWidget = couponData.promoCatalogGetCouponListWidget?.copy(
                couponListWidget = validCouponWidgetList as ArrayList<SearchCouponModel.CouponListWidget>
            )
        )
        couponWidgetData1 =
            validCouponWidgetList?.getOrNull(0)
        val automateCoupon1 = couponListWidgetToAutomateCouponModel(
            couponWidgetData1
        )
        couponWidgetData2 =
            validCouponWidgetList?.getOrNull(1)
        val automateCoupon2 = couponListWidgetToAutomateCouponModel(
            couponWidgetData2
        )
        if (automateCoupon1 == null) return
        couponModel1 = automateCoupon1
        couponModel2 = automateCoupon2
    }

    private fun couponListWidgetToAutomateCouponModel(couponListWidget: SearchCouponModel.CouponListWidget?): AutomateCouponModel? {
        if (couponListWidget == null) return null
        val benefitType =
            couponListWidget.widgetInfo?.headerList?.find { it.key == WIDGET_KEY_BENEFIT_TYPE }
        val storeName =
            couponListWidget.widgetInfo?.headerList?.find { it.key == WIDGET_KEY_STORE_NAME }
        val benefitAmount =
            couponListWidget.widgetInfo?.titleList?.find { it.key == WIDGET_KEY_BENEFIT_AMOUNT }
        val tnc = couponListWidget.widgetInfo?.subtitleList?.find { it.key == WIDGET_KEY_TNC }
        return AutomateCouponModel.Grid(
            type = DynamicColorText(
                value = benefitType?.parent?.text ?: "",
                colorHex = benefitType?.parent?.colorInfo?.colorList?.getOrNull(0)
                    ?: ""
            ),
            benefit = DynamicColorText(
                value = benefitAmount?.parent?.text ?: "",
                colorHex = benefitAmount?.parent?.colorInfo?.colorList?.getOrNull(0)
                    ?: ""
            ),
            tnc = DynamicColorText(
                value = tnc?.parent?.text ?: "",
                colorHex = tnc?.parent?.colorInfo?.colorList?.getOrNull(0) ?: ""
            ),
            backgroundUrl = couponListWidget.widgetInfo?.backgroundInfo?.imageURL ?: "",
            iconUrl = couponListWidget.widgetInfo?.iconURL ?: "",
            shopName = DynamicColorText(
                value = storeName?.parent?.text ?: "",
                colorHex = storeName?.parent?.colorInfo?.colorList?.getOrNull(0) ?: ""
            ),
            badgeText = couponListWidget.widgetInfo?.badgeList?.getOrNull(0)?.parent?.text ?: ""
        )
    }

    fun createGetCouponDataRequestParam(isDarkMode: Boolean = false): RequestParams {
        val slugList = getSlugList()
        val themeType = if(isDarkMode) REQUEST_PARAM_THEME_TYPE_DARK else REQUEST_PARAM_THEME_TYPE_LIGHT
        return RequestParams.create().apply {
            putObject(REQUEST_PARAM_SLUGS, slugList)
            putObject(REQUEST_PARAM_THEME_TYPE, themeType)
        }
    }

    private fun getSlugList(): List<String> {
        return carouselItem.options.getOrNull(0)?.identifier?.split(",") ?: listOf()
    }

    fun shouldShowLoading() = searchCouponModel == null

    fun createRedeemRequestParam(item: SearchCouponModel.CouponListWidget): RequestParams {
        val jsonMetaDataCta = item.widgetInfo?.ctaList?.getOrNull(0)?.jsonMetadata?.let {
            JSONObject(
                it
            )
        }
        val catalogId = (jsonMetaDataCta?.getLong(JSON_METADATA_CATALOG_ID) ?: 0)
        val requestParams = RequestParams.create().apply {
            putObject(REQUEST_PARAM_CATALOG_ID, catalogId)
        }
        return requestParams
    }

    fun updateCouponCtaData(
        couponRedeem: SearchRedeemCouponModel,
        couponWidgetData: SearchCouponModel.CouponListWidget
    ) {
        val redeemCta = couponRedeem.hachikoRedeem?.ctaList?.getOrNull(0) ?: return
        val couponEdit =
            (if (couponWidgetData == couponWidgetData1) couponWidgetData1 else couponWidgetData2)
                ?: return
        val redeemCtaSearchCoupon = SearchCouponModel.Cta(
            text = redeemCta.text,
            type = redeemCta.type,
            isDisabled = redeemCta.isDisabled,
            jsonMetadata = redeemCta.jsonMetadata,
            toasters = redeemCta.toasters
        )
        couponEdit.widgetInfo?.ctaList?.removeAt(0)
        couponEdit.widgetInfo?.ctaList?.add(redeemCtaSearchCoupon)
    }

    fun handleClickCouponTracking(analytics: Analytics, item: SearchCouponModel.CouponListWidget) {
        val valueName = item.widgetInfo?.ctaList?.getOrNull(0)?.text ?: ""
        val slugList = getSlugList()
        val valueId =
            (if (item == couponWidgetData1) slugList.getOrNull(0) else slugList.getOrNull(1)) ?: ""
        val searchComponentImpl = searchComponentTracking(
            trackingOption = trackingOption,
            keyword = actualKeyword,
            valueName = valueName,
            valueId = valueId,
            componentId = componentId,
            applink = applink,
            dimension90 = dimension90
        )
        searchComponentImpl.click(analytics)
    }

    companion object {
        private const val REQUEST_PARAM_SLUGS = "slugs"
        private const val REQUEST_PARAM_THEME_TYPE = "themeType"
        private const val REQUEST_PARAM_CATALOG_ID = "catalog_id"
        private const val REQUEST_PARAM_THEME_TYPE_LIGHT = "light"
        private const val REQUEST_PARAM_THEME_TYPE_DARK = "dark"
        private const val WIDGET_KEY_BENEFIT_TYPE = "benefit-type"
        private const val WIDGET_KEY_STORE_NAME = "store-name"
        private const val WIDGET_KEY_BENEFIT_AMOUNT = "benefit-amount"
        private const val WIDGET_KEY_TNC = "tnc"
        private const val WIDGET_KEY_TIME_LIMIT = "time-limit"
        internal const val CTA_TYPE_CLAIM = "claim"
        internal const val CTA_TYPE_REDIRECT = "redirect"
        internal const val JSON_METADATA_APP_LINK = "app_link"
        internal const val JSON_METADATA_URL = "url"
        internal const val JSON_METADATA_CATALOG_ID = "catalog_id"
        fun create(
            item: InspirationCarouselDataView
        ) = CouponDataView(
            applink = item.options.getOrNull(0)?.applink ?: "",
            dimension90 = item.options.getOrNull(0)?.dimension90 ?: "",
            componentId = item.options.getOrNull(0)?.componentId ?: "",
            trackingOption = item.trackingOption,
            actualKeyword = item.options.getOrNull(0)?.keyword ?: "",
            valueId = item.options.getOrNull(0)?.identifier ?: "",
            valueName = item.options.getOrNull(0)?.title ?: "",
            carouselItem = item
        )
    }
}
