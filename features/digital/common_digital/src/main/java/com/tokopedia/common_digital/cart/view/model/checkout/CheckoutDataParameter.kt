package com.tokopedia.common_digital.cart.view.model.checkout


import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 3/9/17.
 */

class CheckoutDataParameter : Parcelable {

    var voucherCode: String? = null
        private set
    var cartId: String? = null
        private set
    var transactionAmount: Long = 0
        private set
    var ipAddress: String? = null
        private set
    var userAgent: String? = null
        private set
    var accessToken: String? = null
        private set
    var walletRefreshToken: String? = null
        private set
    var relationType: String? = null
        private set
    var relationId: String? = null
        private set
    var isNeedOtp: Boolean = false
        private set

    private constructor(builder: Builder) {
        voucherCode = builder.voucherCode
        cartId = builder.cartId
        transactionAmount = builder.transactionAmount
        ipAddress = builder.ipAddress
        userAgent = builder.userAgent
        accessToken = builder.accessToken
        walletRefreshToken = builder.walletRefreshToken
        relationType = builder.relationType
        relationId = builder.relationId
        isNeedOtp = builder.needOtp
    }


    class Builder {
        var voucherCode: String? = null
        var cartId: String? = null
        var transactionAmount: Long = 0
        var ipAddress: String? = null
        var userAgent: String? = null
        var accessToken: String? = null
        var walletRefreshToken: String? = null
        var relationType: String? = null
        var relationId: String? = null
        var needOtp: Boolean = false

        constructor() {}

        constructor(checkoutData: CheckoutDataParameter) {
            this.voucherCode = checkoutData.voucherCode
            this.transactionAmount = checkoutData.transactionAmount
            this.ipAddress = checkoutData.ipAddress
            this.userAgent = checkoutData.userAgent
            this.accessToken = checkoutData.accessToken
            this.walletRefreshToken = checkoutData.walletRefreshToken
            this.relationType = checkoutData.relationType
            this.relationId = checkoutData.relationId
            this.needOtp = checkoutData.isNeedOtp
        }

        fun voucherCode(`val`: String): Builder {
            voucherCode = `val`
            return this
        }

        fun cartId(`val`: String): Builder {
            cartId = `val`
            return this
        }

        fun transactionAmount(`val`: Long): Builder {
            transactionAmount = `val`
            return this
        }

        fun ipAddress(`val`: String): Builder {
            ipAddress = `val`
            return this
        }

        fun userAgent(`val`: String): Builder {
            userAgent = `val`
            return this
        }

        fun accessToken(`val`: String): Builder {
            accessToken = `val`
            return this
        }

        fun walletRefreshToken(`val`: String): Builder {
            walletRefreshToken = `val`
            return this
        }

        fun relationType(`val`: String): Builder {
            relationType = `val`
            return this
        }

        fun relationId(`val`: String): Builder {
            relationId = `val`
            return this
        }

        fun needOtp(`val`: Boolean): Builder {
            needOtp = `val`
            return this
        }

        fun build(): CheckoutDataParameter {
            return CheckoutDataParameter(this)
        }
    }

    fun newBuilder(): Builder {
        return Builder(this)
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.voucherCode)
        dest.writeString(this.cartId)
        dest.writeLong(this.transactionAmount)
        dest.writeString(this.ipAddress)
        dest.writeString(this.userAgent)
        dest.writeString(this.accessToken)
        dest.writeString(this.walletRefreshToken)
        dest.writeString(this.relationType)
        dest.writeString(this.relationId)
        dest.writeByte(if (this.isNeedOtp) 1.toByte() else 0.toByte())
    }

    protected constructor(`in`: Parcel) {
        this.voucherCode = `in`.readString()
        this.cartId = `in`.readString()
        this.transactionAmount = `in`.readLong()
        this.ipAddress = `in`.readString()
        this.userAgent = `in`.readString()
        this.accessToken = `in`.readString()
        this.walletRefreshToken = `in`.readString()
        this.relationType = `in`.readString()
        this.relationId = `in`.readString()
        this.isNeedOtp = `in`.readByte().toInt() != 0
    }

    companion object CREATOR : Parcelable.Creator<CheckoutDataParameter> {
        override fun createFromParcel(source: Parcel): CheckoutDataParameter {
            return CheckoutDataParameter(source)
        }

        override fun newArray(size: Int): Array<CheckoutDataParameter?> {
            return arrayOfNulls(size)
        }
    }

}
