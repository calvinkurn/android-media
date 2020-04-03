package com.tokopedia.notifcenter.data.viewbean

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.data.entity.DataNotification
import com.tokopedia.notifcenter.data.entity.NotificationOptions
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.entity.UserInfo
import com.tokopedia.notifcenter.presentation.adapter.typefactory.base.BaseNotificationTypeFactory

open class NotificationItemViewBean(
        var notificationId: String = "",
        var isRead: Boolean = false,
        var iconUrl: String? = "",
        var contentUrl: String = "",
        var time: String = "",
        var label: Int = 1,
        var title: String = "",
        var sectionTitle: String = "",
        var body: String = "",
        var bodyHtml: String = "",
        var templateKey: String = "",
        var appLink: String = "",
        var hasShop: Boolean = false,
        var typeLink: Int = 0,
        var totalProduct: Int = 0,
        var btnText: String = "",
        var dataNotification: DataNotification = DataNotification(),
        var products: List<ProductData> = emptyList(),
        var isLongerContent: Boolean = false,
        var isShowBottomSheet: Boolean = false,
        var typeBottomSheet: Int = 0,
        var options: NotificationOptions = NotificationOptions(),
        var userInfo: UserInfo = UserInfo()
) : Parcelable, Visitable<BaseNotificationTypeFactory> {

    override fun type(typeFactory: BaseNotificationTypeFactory): Int {
        return typeFactory.type(this)
    }

    constructor(`in`: Parcel) : this() {
        notificationId = `in`.readString()?: ""
        isRead = `in`.readInt() != 0
        iconUrl = `in`.readString()
        contentUrl = `in`.readString()?: ""
        time = `in`.readString()?: ""
        label = `in`.readInt()
        title = `in`.readString()?: ""
        body = `in`.readString()?: ""
        bodyHtml = `in`.readString()?: ""
        templateKey = `in`.readString()?: ""
        appLink = `in`.readString()?: ""
        hasShop = `in`.readInt() != 0
        typeLink = `in`.readInt()
        totalProduct = `in`.readInt()
        isLongerContent = `in`.readInt() != 0
        isShowBottomSheet = `in`.readInt() != 0
        typeBottomSheet = `in`.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(notificationId)
        parcel.writeInt(if (isRead) 1 else 0)
        parcel.writeString(iconUrl)
        parcel.writeString(contentUrl)
        parcel.writeString(time)
        parcel.writeInt(label)
        parcel.writeString(title)
        parcel.writeString(body)
        parcel.writeString(bodyHtml)
        parcel.writeString(templateKey)
        parcel.writeString(appLink)
        parcel.writeInt(if (hasShop) 1 else 0)
        parcel.writeInt(typeLink)
        parcel.writeInt(totalProduct)
        parcel.writeInt(if (isLongerContent) 1 else 0)
        parcel.writeInt(if (isShowBottomSheet) 1 else 0)
        parcel.writeInt(typeBottomSheet)
    }

    override fun describeContents(): Int = 0

    fun getAtcProduct(): ProductData? {
        var product: ProductData? = null
        if (products.isNotEmpty()) {
            product = products.first()
        }
        return product
    }

    fun getAtcEventLabel(): String {
        return "$SOURCE - $templateKey - $notificationId - ${getAtcProduct()?.productId}"
    }

    fun getBuyEventAction(): String = BUY_ACTION

    companion object {
        const val BUYER_TYPE = 1
        const val SELLER_TYPE = 2

        const val TYPE_BANNER_1X1 = 0
        const val TYPE_INTERNAL_LINK = 1
        const val TYPE_RECOMMENDATION = 2
        const val TYPE_WISHLIST = 3
        const val TYPE_BANNER_2X1 = 4
        const val TYPE_PRODUCT_CHECKOUT = 5

        private const val BUY_ACTION = "click on by button"
        const val SOURCE = "notifcenter"

        @JvmField
        val CREATOR: Parcelable.Creator<NotificationItemViewBean> = object : Parcelable.Creator<NotificationItemViewBean> {
            override fun createFromParcel(`in`: Parcel): NotificationItemViewBean {
                return NotificationItemViewBean(`in`)
            }

            override fun newArray(size: Int): Array<NotificationItemViewBean?> {
                return arrayOfNulls(size)
            }
        }
    }

}