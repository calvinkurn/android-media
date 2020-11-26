package com.tokopedia.product.manage.item.category.view.model

import android.os.Parcelable
import android.text.TextUtils
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductCategory(var categoryId: Int = -1, var categoryName: String = "", var categoryList: Array<String>? = null) : Parcelable {

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

