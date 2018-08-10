package com.tokopedia.product.manage.item.category.view.model

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils

data class ProductCategory(var categoryId: Int = -1, var categoryName: String = "", var categoryList: Array<String>? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.createStringArray()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(categoryId)
        parcel.writeString(categoryName)
        parcel.writeStringArray(categoryList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductCategory> {
        override fun createFromParcel(parcel: Parcel): ProductCategory {
            return ProductCategory(parcel)
        }

        override fun newArray(size: Int): Array<ProductCategory?> {
            return arrayOfNulls(size)
        }
    }

    private fun setPrintedString() {
        val size = categoryList?.size ?: 0
        for (i in 0 until size) {
            val productCategoryName = categoryList?.get(i)
            if(productCategoryName != null) {
                if (!TextUtils.isEmpty(categoryName)) {
                    categoryName += " / "
                }
                categoryName += productCategoryName
            }
        }
    }

    fun getPrintedString(): String {
        if (TextUtils.isEmpty(categoryName)) {
            setPrintedString()
        }
        return categoryName
    }

    fun getCategoryLastName(): String {
        if(categoryList != null){
            var tempName : String = ""
            for(name in categoryList!!){
                tempName = name
            }
            return tempName
        }
        return categoryName
    }

}

