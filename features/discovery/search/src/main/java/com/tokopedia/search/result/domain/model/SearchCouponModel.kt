package com.tokopedia.search.result.domain.model

import com.google.gson.annotations.SerializedName

data class SearchCouponModel(
    @SerializedName("promoCatalogGetCouponListWidget") var promoCatalogGetCouponListWidget: PromoCatalogGetCouponListWidget? = PromoCatalogGetCouponListWidget()
) {
    data class Data(
        @SerializedName("promoCatalogGetCouponListWidget") var promoCatalogGetCouponListWidget: PromoCatalogGetCouponListWidget? = PromoCatalogGetCouponListWidget()
    )

    data class PromoCatalogGetCouponListWidget(
        @SerializedName("resultStatus") var resultStatus: ResultStatus? = ResultStatus(),
        @SerializedName("couponListWidget") var couponListWidget: ArrayList<CouponListWidget> = arrayListOf()
    )

    data class CouponListWidget(
        @SerializedName("widgetInfo") var widgetInfo: WidgetInfo? = WidgetInfo()
    )

    data class WidgetInfo(
        @SerializedName("headerList") var headerList: ArrayList<HeaderList> = arrayListOf(),
        @SerializedName("titleList") var titleList: ArrayList<Title> = arrayListOf(),
        @SerializedName("subtitleList") var subtitleList: ArrayList<Subtitle> = arrayListOf(),
        @SerializedName("footerList") var footerList: ArrayList<FooterList> = arrayListOf(),
        @SerializedName("ctaList") var ctaList: ArrayList<Cta> = arrayListOf(),
        @SerializedName("badgeList") var badgeList: ArrayList<Badge> = arrayListOf(),
        @SerializedName("iconURL") var iconURL: String? = null,
        @SerializedName("backgroundInfo") var backgroundInfo: BackgroundInfo? = BackgroundInfo(),
        @SerializedName("actionInfo") var actionInfo: ActionInfo? = ActionInfo()
    )

    data class ActionInfo(
        @SerializedName("text") var text: String? = null,
        @SerializedName("url") var url: String? = null,
        @SerializedName("applink") var applink: String? = null,
        @SerializedName("type") var type: String? = null,
        @SerializedName("isDisabled") var isDisabled: Boolean? = null,
        @SerializedName("jsonMetadata") var jsonMetadata: String? = null
    )

    data class BackgroundInfo(
        @SerializedName("imageURL") var imageURL: String? = null
    )

    data class Cta(
        @SerializedName("text") var text: String? = null,
        @SerializedName("type") var type: String? = null,
        @SerializedName("isDisabled") var isDisabled: Boolean? = null,
        @SerializedName("jsonMetadata") var jsonMetadata: String? = null,
        @SerializedName("toasters") var toasters: ArrayList<String> = arrayListOf()
    )

    data class Subtitle(
        @SerializedName("key") var key: String? = null,
        @SerializedName("parent") var parent: Parent? = Parent()
    )

    data class Parent(
        @SerializedName("key") var key: String? = null,
        @SerializedName("text") var text: String? = null,
        @SerializedName("colorInfo") var colorInfo: ColorInfo? = ColorInfo()
    )

    data class Title(
        @SerializedName("key") var key: String? = null,
        @SerializedName("parent") var parent: Parent? = Parent()
    )

    data class Badge(
        @SerializedName("key") var key: String? = null,
        @SerializedName("parent") var parent: Parent? = Parent()
    )

    data class ColorInfo(
        @SerializedName("colorList") var colorList: ArrayList<String> = arrayListOf()
    )

    data class HeaderList(
        @SerializedName("key") var key: String? = null,
        @SerializedName("parent") var parent: Parent? = Parent(),
        @SerializedName("child") var child: ArrayList<Child> = arrayListOf()
    )

    data class FooterList(
        @SerializedName("key") var key: String? = null,
        @SerializedName("parent") var parent: Parent? = Parent(),
        @SerializedName("child") var child: ArrayList<Child> = arrayListOf()
    )

    data class Child(
        @SerializedName("key") var key: String? = null,
        @SerializedName("text") var text: String? = null,
        @SerializedName("colorInfo") var colorInfo: ColorInfo? = ColorInfo()
    )

    data class ResultStatus(
        @SerializedName("code") var code: String? = null,
        @SerializedName("status") var status: String? = null
    )

    companion object {
        private const val TEXT_OUT_OF_STOCK = "Habis"
        private const val TYPE_INFO = "info"

        internal fun SearchCouponModel.Cta?.isValidCoupon(): Boolean {
            if (this == null) return false
            return !(this.text == TEXT_OUT_OF_STOCK && this.isDisabled == true && this.type == TYPE_INFO)
        }
    }
}
