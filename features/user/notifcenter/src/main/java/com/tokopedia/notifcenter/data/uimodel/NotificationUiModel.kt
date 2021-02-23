package com.tokopedia.notifcenter.data.uimodel


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.notifcenter.data.entity.notification.*
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory
import java.util.*

@SuppressLint("Invalid Data Type")
data class NotificationUiModel(
        @SerializedName("bottomsheet")
        val bottomsheet: Bottomsheet = Bottomsheet(),
        @SerializedName("button_text")
        val buttonText: String = "",
        @SerializedName("content")
        val content: String = "",
        @SerializedName("create_time")
        val createTime: String = "",
        @SerializedName("create_time_unix")
        val createTimeUnix: Long = 0,
        @SerializedName("data_notification")
        val dataNotification: DataNotification = DataNotification(),
        @SerializedName("expire_time")
        val expireTime: String = "",
        @SerializedName("expire_time_unix")
        val expireTimeUnix: Long = 0,
        @SerializedName("is_longer_content")
        val isLongerContent: Boolean = false,
        @SerializedName("notif_id")
        val notifId: String = "",
        @SerializedName("product_data")
        var productData: List<ProductData> = listOf(),
        @SerializedName("image")
        val imageMetaData: List<ImageMetaData> = listOf(),
        @SerializedName("read_status")
        var readStatus: Int = 0,
        @SerializedName("section_icon")
        val sectionIcon: String = "",
        @SerializedName("section_id")
        val sectionId: String = "",
        @SerializedName("section_key")
        val sectionKey: String = "",
        @SerializedName("shop_id")
        val shopId: Long = 0,
        @SerializedName("short_description")
        val shortDescription: String = "",
        @SerializedName("short_description_html")
        val shortDescriptionHtml: String = "",
        @SerializedName("show_bottomsheet")
        val showBottomsheet: Boolean = false,
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("subsection_key")
        val subsectionKey: String = "",
        @SerializedName("template_key")
        val templateKey: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("total_product")
        val totalProduct: Int = 0,
        @SerializedName("type_bottomsheet")
        val typeBottomsheet: Int = 0,
        @SerializedName("type_link")
        val typeLink: Int = 0,
        @SerializedName("type_of_user")
        val typeOfUser: Int = 0,
        @SerializedName("update_time")
        val updateTime: String = "",
        @SerializedName("update_time_unix")
        val updateTimeUnix: Long = 0,
        @SerializedName("user_id")
        val userId: Long = 0,
        @SerializedName("notif_order_type")
        val widgetType: Int = 0,
        @SerializedName("track_history")
        val trackHistory: List<TrackHistory> = listOf(),
        @SerializedName("widget")
        val widget: Widget = Widget(),
        @SerializedName("is_last_journey")
        val isLastJourney: Boolean = false,
        @SerializedName("unique_id")
        val order_id: String = ""
) : Visitable<NotificationTypeFactory> {

    @delegate:Transient
    val expireTargetDate: Calendar? by lazy(LazyThreadSafetyMode.NONE) {
        Calendar.getInstance().apply {
            time = Date(expireTimeUnix * 1000)
        }
    }

    @delegate:Transient
    val widgetTitleHtml: CharSequence by lazy(LazyThreadSafetyMode.NONE) {
        MethodChecker.fromHtml(widget.title)
    }

    @delegate:Transient
    val widgetDescHtml: CharSequence by lazy(LazyThreadSafetyMode.NONE) {
        MethodChecker.fromHtml(widget.description)
    }

    @delegate:Transient
    val widgetMessageHtml: CharSequence by lazy(LazyThreadSafetyMode.NONE) {
        MethodChecker.fromHtml(widget.message)
    }

    @delegate:Transient
    val shortDescHtml: CharSequence by lazy(LazyThreadSafetyMode.NONE) {
        MethodChecker.fromHtml(shortDescriptionHtml)
    }

    var options: Options = Options()
    val product: ProductData? get() = productData.getOrNull(0)
    val expireTimeUnixMillis: Long get() = expireTimeUnix * 1000
    val createTimeUnixMillis: Long get() = createTimeUnix * 1000
    val widgetCtaText: String get() = widget.buttonText
    var isHistoryVisible = false
        private set

    override fun type(typeFactory: NotificationTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getEventLabel(): String {
        return "notif_list - $templateKey - $notifId"
    }

    fun isRead(): Boolean {
        return readStatus == STATUS_READ
    }

    fun isTypeDefault(): Boolean {
        return typeLink == TYPE_DEFAULT
    }

    fun isTypeSingleProduct(): Boolean {
        return (typeLink == TYPE_ATC || typeLink == TYPE_BUY) && productData.size == 1
    }

    fun isCarouselProduct(): Boolean {
        return (typeLink == TYPE_ATC || typeLink == TYPE_BUY) && productData.size > 1
    }

    fun isBanner(): Boolean {
        return typeLink == TYPE_BANNER
    }

    fun isStockHandlerBottomSheet(): Boolean {
        return typeBottomsheet == BS_TYPE_StockHandler
    }

    fun isPromotion(): Boolean {
        return sectionId == "promotion"
    }

    fun markNotificationAsRead() {
        readStatus = STATUS_READ
    }

    fun hasBeenSeen(): Boolean {
        return readStatus != STATUS_UNREAD
    }

    fun hasWidget(): Boolean {
        return isSingleLineWidget() || isMultiLineWidget()
    }

    fun toggleHistoryVisibility() {
        isHistoryVisible = !isHistoryVisible
    }

    fun isSingleLineWidget(): Boolean {
        return widgetType == WIDGET_SINGLE
    }

    fun isMultiLineWidget(): Boolean {
        return widgetType == WIDGET_MULTIPLE
    }

    fun hasSingleLineDesc(): Boolean {
        return isSingleLineWidget() && widget.description.isNotEmpty()
    }

    fun noWidgetWithTrackHistory(): Boolean {
        return typeLink == TYPE_TRACK_HISTORY && widgetType == NO_WIDGET
                && trackHistory.isNotEmpty()
    }

    fun isTrackHistory(): Boolean {
        return typeLink == TYPE_TRACK_HISTORY
    }

    fun hasTrackHistory(): Boolean {
        return trackHistory.isNotEmpty()
    }

    companion object {
        const val STATUS_UNREAD = 1
        const val STATUS_READ = 2

        const val TYPE_DEFAULT = 0
        const val TYPE_BANNER = 4
        const val TYPE_BUY = 5
        const val TYPE_ATC = 3
        const val TYPE_TRACK_HISTORY = 8
        const val TYPE_RECOM = 2

        const val BS_TYPE_LongerContent = 0
        const val BS_TYPE_ProductCheckout = 1
        const val BS_TYPE_StockHandler = 2
        const val BS_TYPE_Information = 3

        const val NO_WIDGET = 0
        const val WIDGET_SINGLE = 1
        const val WIDGET_MULTIPLE = 2
    }
}