package com.tokopedia.notifcenter.data.uimodel


import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.data.entity.notification.Bottomsheet
import com.tokopedia.notifcenter.data.entity.notification.DataNotification
import com.tokopedia.notifcenter.data.entity.notification.Options
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory
import java.util.*

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
        val productData: List<ProductData> = listOf(),
        @SerializedName("read_status")
        val readStatus: Int = 0,
        @SerializedName("section_icon")
        val sectionIcon: String = "",
        @SerializedName("section_id")
        val sectionId: String = "",
        @SerializedName("section_key")
        val sectionKey: String = "",
        @SerializedName("shop_id")
        val shopId: Int = 0,
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
        val userId: Long = 0
) : Visitable<NotificationTypeFactory> {

    @delegate:Transient
    val expireTargetDate: Calendar? by lazy(LazyThreadSafetyMode.NONE) {
        Calendar.getInstance().apply {
            time = Date(expireTimeUnix * 1000)
        }
    }
    var options: Options = Options()
    val product: ProductData? get() = productData.getOrNull(0)

    override fun type(typeFactory: NotificationTypeFactory): Int {
        return typeFactory.type(this)
    }

    /**
     * 1 == new notif
     * 2 == clicked
     */
    fun isRead(): Boolean {
        return readStatus == 2
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

    companion object {
        const val TYPE_DEFAULT = 0
        const val TYPE_BANNER = 4
        const val TYPE_BUY = 5
        const val TYPE_ATC = 3
        const val TYPE_RECOM = 2

        const val BS_TYPE_LongerContent = 0
        const val BS_TYPE_ProductCheckout = 1
        const val BS_TYPE_StockHandler = 2
        const val BS_TYPE_Information = 3
    }
}