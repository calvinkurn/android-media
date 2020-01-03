package com.tokopedia.notifcenter.data.viewbean

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.data.entity.ProductData
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
        var templateKey: String = "",
        var appLink: String = "",
        var hasShop: Boolean = false,
        var typeLink: Int = 0,
        var totalProduct: Int = 0,
        var btnText: String = "",
        var products: List<ProductData> = emptyList()
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
        templateKey = `in`.readString()?: ""
        appLink = `in`.readString()?: ""
        hasShop = `in`.readInt() != 0
        typeLink = `in`.readInt()
        totalProduct = `in`.readInt()
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
        parcel.writeString(templateKey)
        parcel.writeString(appLink)
        parcel.writeInt(if (hasShop) 1 else 0)
        parcel.writeInt(typeLink)
        parcel.writeInt(totalProduct)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getAtcProduct(): ProductData? {
        var product: ProductData? = null
        if (products.isNotEmpty()) {
            product = products[0]
        }
        return product
    }

    fun getProductIdImpression(): String {
        if (products.isEmpty()) return ""
        var productId = ""
        val product = products[0]
        if (isWishlistPriceDrop()) {
            productId = product.productId
        }
        return productId
    }

    private fun isWishlistPriceDrop(): Boolean {
        return typeLink == 3
    }

    fun getImpressionTrackLabel(location: String): String {
        val productId = getProductIdImpression()
        return "$location - $templateKey - $productId"
    }

    companion object {

        var BUYER_TYPE = 1
        var SELLER_TYPE = 2

        var TYPE_BANNER_1X1 = 0
        var TYPE_INTERNAL_LINK = 1
        var TYPE_RECOMMENDATION = 2
        var TYPE_WISHLIST = 3
        var TYPE_BANNER_2X1 = 4

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