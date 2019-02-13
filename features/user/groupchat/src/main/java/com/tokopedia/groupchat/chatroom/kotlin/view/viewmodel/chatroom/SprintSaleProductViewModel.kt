package com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel.chatroom

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by nisie on 3/22/18.
 */

class SprintSaleProductViewModel : Parcelable {

    var sprintSaleCampaignId: String? = null
        private set
    var productId: String? = null
        private set
    var productName: String? = null
        private set
    var productImage: String? = null
        private set
    var discountLabel: String? = null
        private set
    var productPrice: String? = null
        private set
    var productPriceBeforeDiscount: String? = null
        private set
    var stockPercentage: Double = 0.toDouble()
        private set
    var stockText: String? = null
        private set
    var productUrl: String? = null
        private set

    constructor(sprintSaleCampaignId: String?,
                productId: String?, productName: String?,
                productImage: String?,
                discountLabel: String?,
                productPrice: String?, productPriceBeforeDiscount: String?,
                stockPercentage: Double, stockText: String?, productUrl: String?) {
        this.sprintSaleCampaignId = sprintSaleCampaignId
        this.productId = productId
        this.productName = productName
        this.productImage = productImage
        this.discountLabel = discountLabel
        this.productPrice = productPrice
        this.productPriceBeforeDiscount = productPriceBeforeDiscount
        this.stockPercentage = stockPercentage
        this.stockText = stockText
        this.productUrl = productUrl
    }

    protected constructor(`in`: Parcel) {
        sprintSaleCampaignId = `in`.readString()
        productId = `in`.readString()
        productName = `in`.readString()
        productImage = `in`.readString()
        discountLabel = `in`.readString()
        productPrice = `in`.readString()
        productPriceBeforeDiscount = `in`.readString()
        stockPercentage = `in`.readDouble()
        stockText = `in`.readString()
        productUrl = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(sprintSaleCampaignId)
        dest.writeString(productId)
        dest.writeString(productName)
        dest.writeString(productImage)
        dest.writeString(discountLabel)
        dest.writeString(productPrice)
        dest.writeString(productPriceBeforeDiscount)
        dest.writeDouble(stockPercentage)
        dest.writeString(stockText)
        dest.writeString(productUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<SprintSaleProductViewModel> = object : Parcelable.Creator<SprintSaleProductViewModel> {
            override fun createFromParcel(`in`: Parcel): SprintSaleProductViewModel {
                return SprintSaleProductViewModel(`in`)
            }

            override fun newArray(size: Int): Array<SprintSaleProductViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }


}
