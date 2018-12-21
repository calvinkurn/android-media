package com.tokopedia.common_digital.cart.view.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 2/24/17.
 */

class DigitalCheckoutPassData : Parcelable {


    var action: String? = null
    var categoryId: String? = null
    var clientNumber: String? = null
    var productId: String? = null
    var operatorId: String? = null
    var isPromo: String? = null
    var instantCheckout: String? = null
    var utmSource: String? = null
    var utmMedium: String? = null
    var utmCampaign: String? = null
    var utmContent: String? = null
    var idemPotencyKey: String? = null
    var voucherCodeCopied: String? = null
    var source: Int = 0

    private constructor(builder: Builder) {
        action = builder.action
        categoryId = builder.categoryId
        clientNumber = builder.clientNumber
        productId = builder.productId
        operatorId = builder.operatorId
        isPromo = builder.isPromo
        instantCheckout = builder.instantCheckout
        utmSource = builder.utmSource
        utmMedium = builder.utmMedium
        utmCampaign = builder.utmCampaign
        utmContent = builder.utmContent
        idemPotencyKey = builder.idemPotencyKey
        voucherCodeCopied = builder.voucherCodeCopied
        source = builder.source
    }

    protected constructor(`in`: Parcel) {
        action = `in`.readString()
        categoryId = `in`.readString()
        clientNumber = `in`.readString()
        productId = `in`.readString()
        operatorId = `in`.readString()
        isPromo = `in`.readString()
        instantCheckout = `in`.readString()
        utmSource = `in`.readString()
        utmMedium = `in`.readString()
        utmCampaign = `in`.readString()
        utmContent = `in`.readString()
        idemPotencyKey = `in`.readString()
        voucherCodeCopied = `in`.readString()
        source = `in`.readInt()
    }

    constructor() {}

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(action)
        parcel.writeString(categoryId)
        parcel.writeString(clientNumber)
        parcel.writeString(productId)
        parcel.writeString(operatorId)
        parcel.writeString(isPromo)
        parcel.writeString(instantCheckout)
        parcel.writeString(utmSource)
        parcel.writeString(utmMedium)
        parcel.writeString(utmCampaign)
        parcel.writeString(utmContent)
        parcel.writeString(idemPotencyKey)
        parcel.writeString(voucherCodeCopied)
        parcel.writeInt(source)
    }

    class Builder {
         var action: String? = null
         var categoryId: String? = null
         var clientNumber: String? = null
         var productId: String? = null
         var operatorId: String? = null
         var isPromo: String? = null
         var instantCheckout: String? = null
         var utmSource: String? = null
         var utmMedium: String? = null
         var utmCampaign: String? = null
         var utmContent: String? = null
         var idemPotencyKey: String? = null
         var voucherCodeCopied: String? = null
         var source: Int = 0

        fun action(`val`: String): Builder {
            action = `val`
            return this
        }

        fun categoryId(`val`: String): Builder {
            categoryId = `val`
            return this
        }

        fun clientNumber(`val`: String): Builder {
            clientNumber = `val`
            return this
        }

        fun productId(`val`: String): Builder {
            productId = `val`
            return this
        }

        fun operatorId(`val`: String): Builder {
            operatorId = `val`
            return this
        }

        fun isPromo(`val`: String): Builder {
            isPromo = `val`
            return this
        }

        fun instantCheckout(`val`: String): Builder {
            instantCheckout = `val`
            return this
        }

        fun utmSource(`val`: String): Builder {
            utmSource = `val`
            return this
        }

        fun utmMedium(`val`: String): Builder {
            utmMedium = `val`
            return this
        }

        fun utmCampaign(`val`: String): Builder {
            utmCampaign = `val`
            return this
        }

        fun utmContent(`val`: String): Builder {
            utmContent = `val`
            return this
        }

        fun idemPotencyKey(`val`: String): Builder {
            idemPotencyKey = `val`
            return this
        }

        fun voucherCodeCopied(`val`: String): Builder {
            voucherCodeCopied = `val`
            return this
        }

        fun source(`val`: Int): Builder {
            source = `val`
            return this
        }

        fun build(): DigitalCheckoutPassData {
            return DigitalCheckoutPassData(this)
        }
    }

    companion object {
        val PARAM_WIDGET = 1
        val PARAM_NATIVE = 2
        val PARAM_ACTION = "action"
        val PARAM_CATEGORY_ID = "category_id"
        val PARAM_CLIENT_NUMBER = "client_number"
        val PARAM_PRODUCT_ID = "product_id"
        val PARAM_OPERATOR_ID = "operator_id"
        val PARAM_IS_PROMO = "is_promo"
        val PARAM_INSTANT_CHECKOUT = "instant_checkout"
        val PARAM_UTM_SOURCE = "utm_source"
        val PARAM_UTM_MEDIUM = "utm_medium"
        val PARAM_UTM_CAMPAIGN = "utm_campaign"
        val PARAM_UTM_CONTENT = "utm_content"
        val PARAM_IDEM_POTENCY_KEY = "idem_potency_key"
        val DEFAULT_ACTION = "init_data"
        val UTM_SOURCE_ANDROID = "android"
        val UTM_MEDIUM_WIDGET = "widget"

        @JvmField
        val CREATOR: Parcelable.Creator<DigitalCheckoutPassData> = object : Parcelable.Creator<DigitalCheckoutPassData> {
            override fun createFromParcel(`in`: Parcel): DigitalCheckoutPassData {
                return DigitalCheckoutPassData(`in`)
            }

            override fun newArray(size: Int): Array<DigitalCheckoutPassData?> {
                return arrayOfNulls(size)
            }
        }
    }

}
