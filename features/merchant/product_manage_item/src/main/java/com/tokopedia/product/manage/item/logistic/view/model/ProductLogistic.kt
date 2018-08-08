package com.tokopedia.product.manage.item.logistic.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.product.manage.item.utils.ProductEditPreOrderTimeType
import com.tokopedia.product.manage.item.utils.ProductEditWeightType

data class ProductLogistic(var weight: Int = 0,
                           var weightType: Int = ProductEditWeightType.GRAM,
                           var insurance: Boolean = false,
                           var freeReturn: Boolean = false,
                           var preOrder: Boolean = false,
                           var processTime: Int = 0,
                           var processTimeType: Int = ProductEditPreOrderTimeType.DAY) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(weight)
        parcel.writeInt(weightType)
        parcel.writeByte(if (insurance) 1 else 0)
        parcel.writeByte(if (freeReturn) 1 else 0)
        parcel.writeByte(if (preOrder) 1 else 0)
        parcel.writeInt(processTime)
        parcel.writeInt(processTimeType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductLogistic> {
        override fun createFromParcel(parcel: Parcel): ProductLogistic {
            return ProductLogistic(parcel)
        }

        override fun newArray(size: Int): Array<ProductLogistic?> {
            return arrayOfNulls(size)
        }
    }
}