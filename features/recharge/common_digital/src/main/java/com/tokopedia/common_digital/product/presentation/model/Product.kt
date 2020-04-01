package com.tokopedia.common_digital.product.presentation.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 5/3/17.
 */
class Product(
        var productId: String? = null,
        var productType: String? = null,
        var desc: String? = null,
        var detail: String? = null,
        var detailCompact: String? = null,
        var detailUrl: String? = null,
        var detailUrlText: String? = null,
        var info: String? = null,
        var price: String? = null,
        var pricePlain: Long = 0,
        var promo: Promo? = null,
        var status: Int = 0
) : Parcelable, BaseWidgetItem {

    private constructor(builder: Builder): this(
        builder.productId,
        builder.productType,
        builder.desc,
        builder.detail,
        builder.detailCompact,
        detailUrl = builder.detailUrl,
        detailUrlText = builder.detailUrlText,
        info = builder.info,
        price = builder.price,
        pricePlain = builder.pricePlain,
        promo = builder.promo,
        status = builder.status
    )


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.productId)
        dest.writeString(this.productType)
        dest.writeString(this.desc)
        dest.writeString(this.detail)
        dest.writeString(this.detailUrl)
        dest.writeString(this.detailUrlText)
        dest.writeString(this.info)
        dest.writeString(this.price)
        dest.writeLong(this.pricePlain)
        dest.writeParcelable(this.promo, flags)
        dest.writeInt(this.status)
    }

    protected constructor(`in`: Parcel): this(
        productId = `in`.readString(),
        productType = `in`.readString(),
        desc = `in`.readString(),
        detail = `in`.readString(),
        detailUrl = `in`.readString(),
        detailUrlText = `in`.readString(),
        info = `in`.readString(),
        price = `in`.readString(),
        pricePlain = `in`.readLong(),
        promo = `in`.readParcelable(Promo::class.java.classLoader),
        status = `in`.readInt()
    )

    class Builder {
         var productId: String? = null
         var productType: String? = null
         var desc: String? = null
         var detail: String? = null
         var detailCompact: String? = null
         var detailUrl: String? = null
         var detailUrlText: String? = null
         var info: String? = null
         var price: String? = null
         var pricePlain: Long = 0
         var promo: Promo? = null
         var status: Int = 0

        fun productId(`val`: String): Builder {
            productId = `val`
            return this
        }

        fun productType(`val`: String): Builder {
            productType = `val`
            return this
        }

        fun desc(`val`: String): Builder {
            desc = `val`
            return this
        }

        fun detail(`val`: String): Builder {
            detail = `val`
            return this
        }

        fun detailCompact(`val`: String): Builder {
            detailCompact = `val`
            return this
        }

        fun detailUrl(`val`: String): Builder {
            detailUrl = `val`
            return this
        }

        fun detailUrlText(`val`: String): Builder {
            detailUrlText = `val`
            return this
        }

        fun info(`val`: String): Builder {
            info = `val`
            return this
        }

        fun price(`val`: String): Builder {
            price = `val`
            return this
        }

        fun pricePlain(`val`: Long): Builder {
            pricePlain = `val`
            return this
        }

        fun promo(`val`: Promo): Builder {
            promo = `val`
            return this
        }

        fun status(`val`: Int): Builder {
            status = `val`
            return this
        }

        fun build(): Product {
            return Product(this)
        }
    }

    override fun toString(): String {
        return desc.toString()
    }

    companion object {
        val STATUS_OUT_OF_STOCK = 3
        val STATUS_INACTIVE = 2

        @JvmField
        val CREATOR: Parcelable.Creator<Product> = object : Parcelable.Creator<Product> {
            override fun createFromParcel(source: Parcel): Product {
                return Product(source)
            }

            override fun newArray(size: Int): Array<Product?> {
                return arrayOfNulls(size)
            }
        }
    }

}
