package com.tokopedia.notifcenter.data.viewbean

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.data.entity.DataNotification
import com.tokopedia.notifcenter.data.entity.NotificationOptions
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.entity.UserInfo
import com.tokopedia.notifcenter.presentation.adapter.typefactory.base.BaseNotificationTypeFactory

class NotificationItemViewBean(
        override var notificationId: String = "",
        override var isRead: Boolean = false,
        override var iconUrl: String? = "",
        override var contentUrl: String = "",
        override var time: String = "",
        override var label: Int = 1,
        override var title: String = "",
        override var sectionTitle: String = "",
        override var body: String = "",
        override var bodyHtml: String = "",
        override var templateKey: String = "",
        override var appLink: String = "",
        override var hasShop: Boolean = false,
        override var typeLink: Int = 0,
        override var totalProduct: Int = 0,
        override var btnText: String = "",
        override var dataNotification: DataNotification = DataNotification(),
        override var products: List<ProductData> = emptyList(),
        override var isLongerContent: Boolean = false,
        override var isShowBottomSheet: Boolean = false,
        override var typeBottomSheet: Int = 0,
        override var options: NotificationOptions = NotificationOptions(),
        override var userInfo: UserInfo = UserInfo()
) : BaseNotificationItemViewBean(), Parcelable, Visitable<BaseNotificationTypeFactory> {

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